/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.bibliotecajsf2.practico3.lab5.web;

import com.mycompany.bibliotecajsf2.practico3.lab5.Servicios.SSocioServicio;
import com.mycompany.bibliotecajsf2.practico3.lab5.entidades.Socio;
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
@Named(value = "socioBean")
public class SocioBean implements Serializable {

    private Socio so;
    private List<Socio> socios;
    private int idSocio;

    @Inject
    SSocioServicio servicioSocio;

    @PostConstruct
    public void init() {
        socios = servicioSocio.listarSocio("");
        so = new Socio();
    }

    public void guardar() {
        if (so.getId() != null) {
            // Esto estaba bien, pero asegúrate de refrescar la lista después
            servicioSocio.editar(so);
        } else {
            servicioSocio.agregar(so);
        }

        // Después de guardar o editar, limpiamos y refrescamos
        so = new Socio();
        socios = servicioSocio.listarSocio("");
    }

    public void seleccionar() {
        so = servicioSocio.recuperarPorId(idSocio).get();
    }

    public void eliminar() {
        servicioSocio.eliminar(idSocio);
        socios = servicioSocio.listarSocio("");
        so = new Socio();
    }

//   GETTER AND SETTERS 
    public Socio getSo() {
        return so;
    }

    public void setSo(Socio so) {
        this.so = so;
    }

    public List<Socio> getSocios() {
        return socios;
    }

    public void setSocios(List<Socio> socios) {
        this.socios = socios;
    }

    public int getIdSocio() {
        return idSocio;
    }

    public void setIdSocio(int idSocio) {
        this.idSocio = idSocio;
    }

}
