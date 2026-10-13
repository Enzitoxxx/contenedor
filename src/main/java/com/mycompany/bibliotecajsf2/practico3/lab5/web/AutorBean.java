/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.bibliotecajsf2.practico3.lab5.web;

import com.mycompany.bibliotecajsf2.practico3.lab5.Servicios.SAutorServicio;
import com.mycompany.bibliotecajsf2.practico3.lab5.entidades.Autor;
import jakarta.annotation.PostConstruct;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.io.Serializable;
import java.util.List;

/**
 *
 * @author Usuario
 */
@ViewScoped
@Named(value = "autorBean")
public class AutorBean implements Serializable {

    private Autor autor;
    private List<Autor> autores;
    private int idAutor;

    @Inject
    SAutorServicio servicioAutor;

    @PostConstruct
    public void init() {
        autores = servicioAutor.Listar("");
        autor = new Autor();
    }

    public void agregar() {
        System.out.println("ID Autor:" + autor.getId());
        if (autor.getId() != null) {
            servicioAutor.agregar(autor);
        } else {
            servicioAutor.editar(autor);
        }
        autor = new Autor();
        autores = servicioAutor.Listar("");
    }

    public void seleccionar() {
        autor = servicioAutor.recuperarPorId(idAutor).get();
    }

    public void eliminar() {
        servicioAutor.eliminar(idAutor);
        autores = servicioAutor.Listar("");
    }

    public List<Autor> recuperarTodos() {
        return servicioAutor.Listar("");
    }

    public Autor getAutor() {
        return autor;
    }

    public void setAutor(Autor autor) {
        this.autor = autor;
    }

    public List<Autor> getAutores() {
        return autores;
    }

    public void setAutores(List<Autor> autores) {
        this.autores = autores;
    }

    public int getIdAutor() {
        return idAutor;
    }

    public void setIdAutor(int idAutor) {
        this.idAutor = idAutor;
    }

}
