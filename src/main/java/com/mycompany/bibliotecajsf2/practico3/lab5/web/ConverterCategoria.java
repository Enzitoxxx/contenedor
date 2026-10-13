/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.bibliotecajsf2.practico3.lab5.web;


import com.mycompany.bibliotecajsf2.practico3.lab5.Servicios.SCategoriaServicio;
import com.mycompany.bibliotecajsf2.practico3.lab5.entidades.Categoria;
import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.convert.Converter;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.util.Optional;

/**
 *
 * @author Usuario
 */
@RequestScoped
@Named (value="converterCategoria")
public class ConverterCategoria implements Converter<Categoria> {
     @Inject
    SCategoriaServicio servicioCategoria;

    @Override
    public Categoria getAsObject(FacesContext fc, UIComponent uic, String id) {
        if (id.isEmpty() || Integer.parseInt(id) == 0) {
            return null;
        } else {
            Optional<Categoria> aux = servicioCategoria.recuperarPorId(Integer.parseInt(id));
            if (aux.isPresent()) {
                return aux.get();
            } else {
                return null;
            }
        }

    }

    @Override
    public String getAsString(FacesContext fc, UIComponent uic, Categoria t) {
        if (t == null) {
            return "0";
        } else {
            return t.getId().toString();
        }
    }
}
