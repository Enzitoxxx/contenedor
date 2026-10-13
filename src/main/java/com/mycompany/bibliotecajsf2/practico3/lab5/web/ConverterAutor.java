/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.bibliotecajsf2.practico3.lab5.web;

import com.mycompany.bibliotecajsf2.practico3.lab5.Servicios.SAutorServicio;
import com.mycompany.bibliotecajsf2.practico3.lab5.entidades.Autor;
import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.faces.convert.Converter;
import java.util.Optional;

/**
 *
 * @author Usuario
 */
@RequestScoped
@Named (value="converterAutor")
public class ConverterAutor implements Converter<Autor> {

    @Inject
    SAutorServicio servicioAutor;

    @Override
    public Autor getAsObject(FacesContext fc, UIComponent uic, String id) {
        if (id.isEmpty() || Integer.parseInt(id) == 0) {
            return null;
        } else {
            Optional<Autor> aux = servicioAutor.recuperarPorId(Integer.parseInt(id));
            if (aux.isPresent()) {
                return aux.get();
            } else {
                return null;
            }
        }

    }

    @Override
    public String getAsString(FacesContext fc, UIComponent uic, Autor t) {
        if (t == null) {
            return "0";
        } else {
            return t.getId().toString();
        }
    }
}
