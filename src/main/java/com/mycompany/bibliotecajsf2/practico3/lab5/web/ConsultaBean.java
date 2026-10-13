/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.bibliotecajsf2.practico3.lab5.web;

import com.mycompany.bibliotecajsf2.practico3.lab5.Servicios.SConsultaServicio;
import com.mycompany.bibliotecajsf2.practico3.lab5.entidades.Consulta;
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
@Named(value="consultaBean")
public class ConsultaBean implements Serializable{
    
    private Consulta c;
    private List<Consulta> consultas;
    private int idConsulta;
    
    @Inject
    SConsultaServicio servicioConsulta;
    
    @PostConstruct
    public void init(){
        consultas= servicioConsulta.listarConsulta("");
        c= new Consulta();
    }
    public void guardar(){
        if(c.getId() != null){
            servicioConsulta.editar(c);
        }else{
            servicioConsulta.agregar(c);
            c = new Consulta();
        }
        c = new Consulta();
        consultas = servicioConsulta.listarConsulta("");
    }
    public void seleccionar(){
        c= servicioConsulta.recuperarPorId(idConsulta).get();
    }
    
    public void eliminar(){
        servicioConsulta.eliminar(idConsulta);
        consultas = servicioConsulta.listarConsulta("");
        c = new Consulta();
    }

    public Consulta getC() {
        return c;
    }

    public void setC(Consulta c) {
        this.c = c;
    }

    public List<Consulta> getConsultas() {
        return consultas;
    }

    public void setConsultas(List<Consulta> consultas) {
        this.consultas = consultas;
    }

    public int getIdConsulta() {
        return idConsulta;
    }

    public void setIdConsulta(int idConsulta) {
        this.idConsulta = idConsulta;
    }
    
    
    
}
