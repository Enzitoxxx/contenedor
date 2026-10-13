/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.bibliotecajsf2.practico3.lab5.web;

import com.mycompany.bibliotecajsf2.practico3.lab5.Servicios.SLibrosServicios;
import com.mycompany.bibliotecajsf2.practico3.lab5.entidades.Libro;
import jakarta.annotation.PostConstruct;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.io.Serializable;
import java.util.List;

/**
 * Managed Bean para la gestión de Libros (Backend y Frontend)
 *
 * @author Usuario
 */
@ViewScoped
@Named(value = "libroBean")
public class LibroBean implements Serializable {

    private Libro libro;
    private List<Libro> libros;
    private int idLibro;          // Para operaciones de ABM (seleccionar/eliminar por ID)
    private String filtro = "";   // Para capturar el texto del buscador de inicio y catálogo

    @Inject
    SLibrosServicios servicioLibro;

    @PostConstruct
    public void init() {
        // Al cargar la página, si 'filtro' tiene texto busca por ese criterio,
        // si está vacío "" trae todos los libros de la base de datos.
        libros = servicioLibro.Listar(filtro);
        libro = new Libro();
    }

    /*@PostConstruct
public void init() {
    try {
        System.out.println("Entró a init()");
        libros = servicioLibro.Listar(filtro);
        libro = new Libro();
    } catch (Exception e) {
        e.printStackTrace();
    }
}*/

    /**
     * Permite ejecutar la búsqueda directamente (útil para f:viewAction o eventos ajax)
     */
    public void buscar() {
        libros = servicioLibro.Listar(filtro);
    }

    /**
     * Guarda un libro nuevo o edita uno existente, limpiando el formulario y refrescando la lista al finalizar.
     */
    public void guardar() {
        if (libro.getId() != null) {
            servicioLibro.editar(libro);
        } else {
            servicioLibro.agregar(libro);
        }
        // Limpiamos el objeto e inicializamos un nuevo Libro para el formulario
        libro = new Libro();
        // Refrescamos el listado de la tabla automáticamente
        libros = servicioLibro.Listar("");
        // Reseteamos el filtro por si veníamos de una búsqueda
        filtro = "";
    }

    /**
     * Carga los datos del libro seleccionado de la tabla en el formulario de alta
     */
    public void seleccionar() {
        libro = servicioLibro.recuperarPorId(idLibro).get();
    }

    /**
     * Elimina un libro por su ID y actualiza el listado
     */
    public void eliminar() {
        servicioLibro.EliminarLibro(idLibro);
        libros = servicioLibro.Listar("");
        libro = new Libro(); // Limpia el formulario por si estaba seleccionado
    }

    /**
     * Método auxiliar para limpiar manualmente el formulario desde la vista
     */
    public void prepararNuevo() {
        libro = new Libro();
        filtro = "";
    }

    // ==========================================================================
    // GETTERS Y SETTERS
    // ==========================================================================
    public Libro getLibro() {
        return libro;
    }

    public void setLibro(Libro libro) {
        this.libro = libro;
    }

    public List<Libro> getLibros() {
        return libros;
    }

    public void setLibros(List<Libro> libros) {
        this.libros = libros;
    }

    public int getIdLibro() {
        return idLibro;
    }

    public void setIdLibro(int idLibro) {
        this.idLibro = idLibro;
    }

    public String getFiltro() {
        return filtro;
    }

    public void setFiltro(String filtro) {
        this.filtro = filtro;
    }
}
