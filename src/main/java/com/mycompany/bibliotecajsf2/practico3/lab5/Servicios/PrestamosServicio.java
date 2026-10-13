/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.bibliotecajsf2.practico3.lab5.Servicios;

import com.mycompany.bibliotecajsf2.practico3.lab5.entidades.Libro;
import com.mycompany.bibliotecajsf2.practico3.lab5.entidades.Librosprestamo;
import com.mycompany.bibliotecajsf2.practico3.lab5.entidades.Prestamo;
import com.mycompany.bibliotecajsf2.practico3.lab5.entidades.Socio;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import static java.util.concurrent.TimeUnit.MILLISECONDS;

/**
 * @author Usuario
 */
@Stateless
public class PrestamosServicio {

    @PersistenceContext
    EntityManager em;

    // Metodo para guardar el prestamo con todas las validaciones.
    public void guardarPrestamosDelCarrito(Integer dniSocio, List<Libro> carrito) throws Exception {

        // Paso 1 -> Busco  al socio por su DNI usando una consulta JPQL 
        Socio socioActivo = null;
        try {
            socioActivo = em.createQuery("SELECT s FROM Socio s WHERE s.dni = :dni", Socio.class)
                    .setParameter("dni", dniSocio)
                    .getSingleResult();
        } catch (Exception e) {
            // Si salta una excepción es porque no encontró ningún registro con ese DNI
            throw new Exception("El socio con DNI " + dniSocio + " no está registrado en el sistema.");
        }

        // Paso 1.b -> Validación de Mora/Deudor
        if ("mora".equalsIgnoreCase(socioActivo.getEstado())) {
            throw new Exception("El socio " + socioActivo.getApellido() + ", " + socioActivo.getNombres() + " no puede solicitar préstamos porque está en MORA.");
        }

        //Paso 1.c -> Valido los libros vencidos (seria el punto b del tp)
        if (tieneLibrosVencidos(socioActivo.getId())) {
            throw new Exception("El socio tiene libros vencidos sin devolver. No puede retirar nuevos.");
        }
        //Esta pregunta es para validar el limite de 3 libros en el carrito(seria no puede pedir un socio 3 prestamos)
        if (carrito == null || carrito.size() > 3) {
            throw new Exception("El límite máximo permitido es de 3 libros por préstamo.");

        }

        //Y esta validacion seria casi lo mismo que la de arriba, solamente que esta valida que un socio no tenga mas de 3 prestamos activos
        long cantidadActivos = em.createQuery(
                "SELECT COUNT(p) FROM Prestamo p WHERE p.sociosId.id = :socioId AND p.estado = 'ACTIVO'", Long.class)
                .setParameter("socioId", socioActivo.getId())
                .getSingleResult();

        if (cantidadActivos >= 3) {
            throw new Exception("El socio ya tiene 3 préstamos activos. Debe devolver alguno antes de retirar más.");
        }
        /* Paso 2 -> Creo la cabecera del préstamo */
        Prestamo nuevoPrestamo = new Prestamo();
        nuevoPrestamo.setFecha(new Date());
        nuevoPrestamo.setMulta(0.0f);

        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.add(Calendar.DAY_OF_YEAR, 7);// 7 dias maximo para devolver
        nuevoPrestamo.setFechaDevuelve(cal.getTime());

        //  Le asoco el Socio real que se encuentra mediante su DNI
        nuevoPrestamo.setSociosId(socioActivo);

        // Guardo en la BD (Se insertará el ID  del socio en la columna socios_id)
        em.persist(nuevoPrestamo);

        /* Paso 3 -> Bucle de libros  */
        for (Libro libroCarrito : carrito) {
            Libro libroBd = em.find(Libro.class, libroCarrito.getId());

            if (libroBd.getDisponibles() <= 0) {
                throw new Exception("Lo siento, el libro '" + libroBd.getTitulo() + "' se quedó sin stock disponible.");
            }

            libroBd.setDisponibles(libroBd.getDisponibles() - 1);
            em.merge(libroBd);

            Librosprestamo nuevoDetalle = new Librosprestamo();
            nuevoDetalle.setPrestamosId(nuevoPrestamo);
            nuevoDetalle.setLibrosId(libroBd);

            em.persist(nuevoDetalle);
        }
    }

    // Obtengo la lista completa de préstamos para mostrarlos en la tabla
    public List<Prestamo> obtenerTodosLosPrestamos() {
        em.clear();
        return em.createQuery("SELECT DISTINCT p FROM Prestamo p LEFT JOIN FETCH p.librosprestamoCollection lp LEFT JOIN FETCH lp.librosId ORDER BY p.fecha DESC", Prestamo.class)
                .getResultList();
    }

    //  MÉTODO PARA EL BACKEND: Procesa la devolución presencial de un libro
    public float registrarDevolucion(Integer idPrestamo) throws Exception {
        Prestamo p = em.find(Prestamo.class, idPrestamo);
        if (p == null) {
            throw new Exception("Préstamo no encontrado.");
        }

        // Normalizar fechas a medianoche para evitar conflictos con la hora actual
        Calendar calHoy = Calendar.getInstance();
        calHoy.set(Calendar.HOUR_OF_DAY, 0);
        calHoy.set(Calendar.MINUTE, 0);
        calHoy.set(Calendar.SECOND, 0);
        calHoy.set(Calendar.MILLISECOND, 0);

        Calendar calVenc = Calendar.getInstance();
        calVenc.setTime(p.getFechaDevuelve());
        calVenc.set(Calendar.HOUR_OF_DAY, 0);
        calVenc.set(Calendar.MINUTE, 0);
        calVenc.set(Calendar.SECOND, 0);
        calVenc.set(Calendar.MILLISECOND, 0);

        float multaCalculada = 0.0f;

        // Comparar usando los milisegundos de la medianoche
        if (calHoy.getTime().after(calVenc.getTime())) {
            long diff = calHoy.getTimeInMillis() - calVenc.getTimeInMillis();
            long diasAtraso = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);

            if (diasAtraso > 0) {
                int cantidadLibros = p.getLibrosprestamoCollection().size();
                multaCalculada = 500.0f * diasAtraso * cantidadLibros;
            }
        }

        p.setMulta(multaCalculada);
        p.setEstado("DEVUELTO");

        // Devolver stock
        for (Librosprestamo detalle : p.getLibrosprestamoCollection()) {
            Libro libroBd = em.find(Libro.class, detalle.getLibrosId().getId());
            libroBd.setDisponibles(libroBd.getDisponibles() + 1);
            em.merge(libroBd);
        }

        em.merge(p);
        return multaCalculada;
    }

    //Este metodo me sirve para verificar si el socio tiene deudas(libros vencidos).
    public boolean tieneLibrosVencidos(Integer socioId) {

        List<Prestamo> prestamos = em.createQuery(
                "SELECT p FROM Prestamo p WHERE p.sociosId.id = :socioId AND p.estado = 'ACTIVO'", Prestamo.class)
                .setParameter("socioId", socioId)
                .getResultList();

        Date fechaHoy = new Date();

        for (Prestamo p : prestamos) {
            // Solo si esta activo y la fecha ya paso, entonces si esta vencido.
            if (p.getFechaDevuelve() != null && p.getFechaDevuelve().before(fechaHoy)) {
                return true; //quiere decir que tiene al menos uno vencido
            }
        }
        return false;
    }

    //Este metodo me sirve para buscar socio por id
    public Socio buscarSocioPorDni(Integer dni) {
        try {
            return em.createQuery("SELECT s FROM Socio s WHERE s.dni = :dni", Socio.class)
                    .setParameter("dni", dni)
                    .getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }
}
