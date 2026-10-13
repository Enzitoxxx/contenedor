/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.bibliotecajsf2.practico3.lab5.web;


import com.mycompany.bibliotecajsf2.practico3.lab5.Servicios.SLibrosServicios;
import com.mycompany.bibliotecajsf2.practico3.lab5.entidades.Libro;
import jakarta.faces.view.ViewScoped; // ¡CLAVE! Para que retenga los datos al cargar la página
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.io.Serializable;

@Named(value = "detalleLibroBean") // <-- FORZÁ EL NOMBRE EXACTO ACÁ EN MINÚSCULA
@ViewScoped                        // <-- REVISÁ QUE SEA ESTE IMPORT DE JAKARTA
public class DetalleLibroBean implements Serializable {

    private int idLibro;
    private Libro libro;

    @Inject
    SLibrosServicios servicioLibro;

    public void cargarLibro() {
        if (idLibro > 0) {
            this.libro = servicioLibro.recuperarPorId(idLibro).orElse(null);
        }
    }

    // --- GETTERS Y SETTERS CRÍTICOS ---
    public int getIdLibro() {
        return idLibro;
    }

    public void setIdLibro(int idLibro) {
        this.idLibro = idLibro;
    }

    public Libro getLibro() {
        return libro;
    }

    public void setLibro(Libro libro) {
        this.libro = libro;
    }
}