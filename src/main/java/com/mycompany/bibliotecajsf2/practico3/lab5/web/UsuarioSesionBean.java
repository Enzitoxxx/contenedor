/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.bibliotecajsf2.practico3.lab5.web;

import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Named;
import java.io.Serializable;

/**
 *
 * @author Usuario
 */
@SessionScoped //Mantiene los datos vivos durante toda la sesion
@Named(value ="usuarioSBean")
public class UsuarioSesionBean implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private Integer id;
    private String nombreUsuario;
    private String rol;
    private Boolean logueado = false; //Me vad decir rapido si inicio sesion o no.
    
    public UsuarioSesionBean() {
    }

    /**
     * Método útil para saber si la sesión está activa desde las vistas o controladores
     */
    public boolean isLogueado() {
        return logueado;
    }

    //get y set
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public Boolean getLogueado() {
        return logueado;
    }

    public void setLogueado(Boolean logueado) {
        this.logueado = logueado;
    }
    
    
}
