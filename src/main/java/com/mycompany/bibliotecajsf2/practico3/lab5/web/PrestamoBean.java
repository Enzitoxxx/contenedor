 ///*
// * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
// * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
// */
package com.mycompany.bibliotecajsf2.practico3.lab5.web;

import com.mycompany.bibliotecajsf2.practico3.lab5.Servicios.PrestamosServicio;
import com.mycompany.bibliotecajsf2.practico3.lab5.entidades.Libro;
import com.mycompany.bibliotecajsf2.practico3.lab5.entidades.Socio;

import jakarta.ejb.EJB;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;

import java.util.List;

@SessionScoped // para que no se borre nada al cambiar de pagina
@Named(value = "prestamoBean")
public class PrestamoBean implements Serializable {

    private static final long serialVersionUID = 1L;

    // La canasta donde se van a ir acumulando los libros.
    private List<Libro> canasta = new ArrayList<>();

    //  Aqui almacena el DNI que el bibliotecario escribe en mostrador
    private Integer dniSocioForm;

    @EJB // EJB es para los servicios @Stateless
    private PrestamosServicio servicioPrestamo;

    @Inject
    private UsuarioSesionBean usuarioBean; // este sabe el ID del usuario que navega

    /* Paso 1:
    El metodo agregarLibro, va disparar cuando el socio toque "ReservarLibro"
    verificamos que el libro no este ya en la canasta para evitar que se lleve 2 libros iguales.
     */
    public void agregarAlCarrito(Libro libroSeleccionado) {

        if (canasta.size() > 3) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Límite excedido", "No puedes agregar más de 3 libros por préstamo."));
            return;
        }
        // Reccoro el carrito actual para ver si el libro ya fue agregado
        for (Libro l : canasta) {
            if (l.getId().equals(libroSeleccionado.getId())) {
                // Si el ID es igual, le mando un aviso y freno el metodo
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Aviso", "Este libro ya esta en tu carrito"));
                return;
            }
        }

        // Si el bucle termino y no lo encontro, lo sumo a la canasta con exito
        canasta.add(libroSeleccionado);
        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_INFO, "Éxito", "Libro añadido al carrito."));
    }

    /* Si el usuario quiere sacar un libro de la canasta*/
    public void quitarDelCarrito(Libro libroAQuitar) {
        canasta.remove(libroAQuitar);
        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_INFO, "Removido", "Libro quitado del carrito."));
    }

    /* Metodo para confirmar el prestamo presencial en mostrador por DNI*/
    public String confirmarPrestamo() {

        // Controlo la seguridad del operador que maneja la sesión (que seria El administrador)
        if (usuarioBean == null || !usuarioBean.getLogueado()) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Debes iniciar sesión para solicitar préstamos."));
            return "/login.xhtml?faces-redirect=true";
        }

        // Controlo el carrito, si esta vacio
        if (canasta.isEmpty()) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_WARN, "Carrito Vacío", "No tienes libros seleccionados."));
            return null;
        }

        //VALIDACIÓN DE SOCIO: Frena la operación si falta ingresar el documento
        if (dniSocioForm == null) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Falta DNI", "Debe ingresar el DNI del Socio para procesar."));
            return null;
        }

        try {
            // LLAMA AL EJB POR DNI: Le pasamos el DNI del formulario y la lista de libros
            servicioPrestamo.guardarPrestamosDelCarrito(dniSocioForm, canasta);

            // Si se guardó en la BD sin errores, vaciamos la canasta y limpiamos el campo del DNI
            canasta.clear();
            dniSocioForm = null;

            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "¡Éxito!", "Tu solicitud de préstamo fue procesada correctamente."));

            // Forzamos la redirección para refrescar el layout
            FacesContext.getCurrentInstance().getExternalContext().redirect(
                    FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath() + "/index.xhtml"
            );
            return null;

        } catch (Exception e) {
            // ESTO MOSTRARÁ EL MENSAJE DE ERROR CUANDO HAGAN CLIC
            FacesContext.getCurrentInstance().addMessage("formSessionHeader:btnConfirmar",
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", e.getMessage()));
            return null;
        }
    }

    /*Devuelve true si es que el socio es valido y no tiene libros(prestamos) vencidos*/

    public boolean isSocioHabilitado() {
        if (dniSocioForm == null) {
            return false;
        }
        try {
            // 1. Obtengo el socio real por DNI
            Socio s = servicioPrestamo.buscarSocioPorDni(dniSocioForm);
            if (s == null) {
                return false; // El socio no existe
            }

            // 2.  Pasamos el ID del socio (s.getId()), no el DNI
            return !servicioPrestamo.tieneLibrosVencidos(s.getId());

        } catch (Exception e) {
            return false;
        }
    }

    // ==========================================================================
    // GETTERS Y SETTERS
    // ==========================================================================
    public List<Libro> getCanasta() {
        return canasta;
    }

    public void setCanasta(List<Libro> canasta) {
        this.canasta = canasta;
    }

    public int getCantidadItems() {
        return canasta.size();
    }

    //  Seteados como correspondía para rastrear el DNI
    public Integer getDniSocioForm() {
        return dniSocioForm;
    }

    public void setDniSocioForm(Integer dniSocioForm) {
        this.dniSocioForm = dniSocioForm;
    }
}
