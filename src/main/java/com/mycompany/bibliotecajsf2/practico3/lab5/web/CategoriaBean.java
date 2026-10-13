/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.bibliotecajsf2.practico3.lab5.web;

import com.mycompany.bibliotecajsf2.practico3.lab5.Servicios.SCategoriaServicio;
import com.mycompany.bibliotecajsf2.practico3.lab5.entidades.Categoria;
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
@Named(value="categoriaBean")
public class CategoriaBean implements Serializable{
    
    private Categoria cat;
    private List<Categoria> categorias;
    int idCategoria;
    
    @Inject
    SCategoriaServicio servicioCategoria;
    
        
    @PostConstruct
    public void init(){
        categorias = servicioCategoria.RecuperarCategorias("");
        cat = new Categoria();
    }
    
    public void guardar(){
        if (cat.getId() == null){
            servicioCategoria.guardar(cat);
        } else {
            servicioCategoria.editar(cat);
        }
        
        cat = new Categoria();
        categorias = servicioCategoria.RecuperarCategorias("");
    }
    
    public void seleccionar(){
        servicioCategoria.recuperarPorId(idCategoria).ifPresent(c -> {
            cat = c;
        });
    }
    
    public void eliminar(){
        servicioCategoria.Eliminar(idCategoria);
        categorias = servicioCategoria.RecuperarCategorias("");
    }
    
    public List<Categoria> recuperarTodas(){
        return servicioCategoria.RecuperarCategorias("");
    }

    public Categoria getCat() {
        return cat;
    }

    public void setCat(Categoria cat) {
        this.cat = cat;
    }

    public List<Categoria> getCategorias() {
        return categorias;
    }

    public void setCategorias(List<Categoria> categorias) {
        this.categorias = categorias;
    }
   
    
    
}
