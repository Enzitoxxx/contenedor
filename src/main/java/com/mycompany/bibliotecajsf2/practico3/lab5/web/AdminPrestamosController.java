package com.mycompany.bibliotecajsf2.practico3.lab5.web;

import com.mycompany.bibliotecajsf2.practico3.lab5.Servicios.PrestamosServicio;
import com.mycompany.bibliotecajsf2.practico3.lab5.entidades.Prestamo;
import jakarta.annotation.PostConstruct;
import jakarta.ejb.EJB;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Usuario
 */
@Named(value = "adminPrestamosController")
@ViewScoped // Mantiene la lista fresca mientras opera la tabla
public class AdminPrestamosController implements Serializable {

    private static final long serialVersionUID = 1L;

    private List<Prestamo> listaPrestamos;

    @EJB
    private PrestamosServicio prestamosServicio;

    //Me van a servir para mostrar 2 tablas, una con los prestamos activos y otra en general.
    private List<Prestamo> prestamosActivos;
    private List<Prestamo> prestamosHistorial;

    @PostConstruct
    public void init() {
        cargarPrestamos();
    }

    public void cargarPrestamos() {
        List<Prestamo> todos = prestamosServicio.obtenerTodosLosPrestamos();
        this.prestamosActivos = new ArrayList<>();
        this.prestamosHistorial = new ArrayList<>();

        for (Prestamo p : todos) {
            // --- DIAGNÓSTICO ---
            String estadoDetectado = (p.getEstado() != null) ? p.getEstado().trim() : "NULL";
            System.out.println("DEBUG: ID=" + p.getId() + " | Estado encontrado='" + estadoDetectado + "'");

            // --- LÓGICA CON LIMPIEZA DE ESPACIOS ---
            if ("ACTIVO".equalsIgnoreCase(estadoDetectado)) {
                this.prestamosActivos.add(p);
            } else {
                this.prestamosHistorial.add(p);
            }
        }
        this.listaPrestamos = todos;
    }

    // Acción que se dispara al presionar "Registrar Devolución" en la tabla
    public void devolver(Integer idPrestamo) {
        try {
            float multa = prestamosServicio.registrarDevolucion(idPrestamo);

            // 1. Recargar datos ANTES de añadir el mensaje
            cargarPrestamos();

            // 2. Usar Flash Scope explícito
            FacesContext ctx = FacesContext.getCurrentInstance();
            ctx.getExternalContext().getFlash().setKeepMessages(true);

            if (multa > 0) {
                ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Multa aplicada", "El socio debe abonar: $" + multa));
            } else {
                ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Éxito", "Devolución registrada."));
            }
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", e.getMessage()));
        }
    }

    public List<Prestamo> getListaPrestamos() {
        // Si la lista está vacía al iniciar, la cargamos
        if (this.listaPrestamos == null) {
            cargarPrestamos();
        }
        return this.listaPrestamos;
    }

    public void setListaPrestamos(List<Prestamo> listaPrestamos) {
        this.listaPrestamos = listaPrestamos;
    }

    public List<Prestamo> getPrestamosActivos() {
        return prestamosActivos;
    }

    public List<Prestamo> getPrestamosHistorial() {
        return prestamosHistorial;
    }
}
