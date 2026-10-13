/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.bibliotecajsf2.practico3.lab5.web;

import com.mycompany.bibliotecajsf2.practico3.lab5.Servicios.ProveedorServicio;
import com.mycompany.bibliotecajsf2.practico3.lab5.entidades.Proveedor;
import jakarta.annotation.PostConstruct;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.io.Serializable;
import java.util.List;

/**
 *
 * @author Alumnos
 */
@ViewScoped
@Named(value = "proveedorBean")
public class ProveedorBean implements Serializable {

    private Proveedor prov;
    private List<Proveedor> proveedores;
    private int idProveedor;
    private String filtro = "";
    
    private Long totalProveedores ;
    private List<Object[]> categoriasPopulares;


    @Inject
    ProveedorServicio servicioProveedor;

//    @PostConstruct
//    public void init (){
//        proveedores = servicioProveedor.listar(filtro);
//        prov = new Proveedor();
//    }
    @PostConstruct
    public void init() {
        try {
            System.out.println("Entró a init()");
            proveedores = servicioProveedor.listar(filtro);
            prov = new Proveedor();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void buscar() {
        proveedores = servicioProveedor.listar(filtro);
    }

    public void guardar() {
        if (prov.getId() != null) {
            servicioProveedor.editar(prov);
        } else {
            servicioProveedor.agregar(prov);
        }
        prov = new Proveedor();
        proveedores = servicioProveedor.listar("");
        filtro = "";
    }

    public void seleccionar() {
        prov = servicioProveedor.recuperarPorId(idProveedor).get();
    }

    public void eliminar() {
        servicioProveedor.eliminar(idProveedor);
        proveedores = servicioProveedor.listar("");
        prov = new Proveedor();
    }

    public void prepararNuevo() {
        prov = new Proveedor();
        filtro = "";
    }

    public int getIdProveedor() {
        return idProveedor;
    }

    public void setIdProveedor(int idProveedor) {
        this.idProveedor = idProveedor;
    }

    public String getFiltro() {
        return filtro;
    }

    public void setFiltro(String filtro) {
        this.filtro = filtro;
    }

    public List<Proveedor> getProveedores() {
        return proveedores;
    }

    public void setProveedores(List<Proveedor> proveedores) {
        this.proveedores = proveedores;
    }

    public Proveedor getProv() {
        return prov;
    }

    public void setProv(Proveedor prov) {
        this.prov = prov;
    }
   

    public Long getTotalProveedores() {
        return servicioProveedor.contarTotalProveedores();
    }

    public List<Object[]> getCategoriasPopulares() {
        return servicioProveedor.obtenerCategoriasMasPopulares();
    }

    
}
