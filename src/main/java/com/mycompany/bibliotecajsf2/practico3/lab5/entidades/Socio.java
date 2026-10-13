/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.bibliotecajsf2.practico3.lab5.entidades;

import jakarta.persistence.Basic;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlTransient;
import java.io.Serializable;
import java.util.Collection;

/**
 * @author Usuario
 */
@Entity
@Table(name = "socios")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Socio.findAll", query = "SELECT s FROM Socio s"),
    @NamedQuery(name = "Socio.findById", query = "SELECT s FROM Socio s WHERE s.id = :id"),
    @NamedQuery(name = "Socio.findByApellido", query = "SELECT s FROM Socio s WHERE s.apellido = :apellido"),
    @NamedQuery(name = "Socio.findByNombres", query = "SELECT s FROM Socio s WHERE s.nombres = :nombres"),
    @NamedQuery(name = "Socio.findByDni", query = "SELECT s FROM Socio s WHERE s.dni = :dni"),
    @NamedQuery(name = "Socio.findByNumeroSocio", query = "SELECT s FROM Socio s WHERE s.numeroSocio = :numeroSocio"),
    @NamedQuery(name = "Socio.findByTelefono", query = "SELECT s FROM Socio s WHERE s.telefono = :telefono"),
    @NamedQuery(name = "Socio.findByEstado", query = "SELECT s FROM Socio s WHERE s.estado = :estado"),
    @NamedQuery(name = "Socio.findByGenero", query = "SELECT s FROM Socio s WHERE s.genero = :genero")})
public class Socio implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Size(max = 100)
    @NotEmpty(message = "El apellido es obligatorio")
    @Column(name = "apellido")
    private String apellido;

    @Size(max = 255)
    @NotEmpty(message = "El nombre es obligatorio")
    @Column(name = "nombres")
    private String nombres;

    @NotNull(message = "El DNI es obligatorio")
    @Min(value = 1000000, message = "El DNI debe tener al menos 7 dígitos")
    @Max(value = 99999999, message = "El DNI no puede exceder los 8 dígitos")
    @Column(name = "dni")
    private Long dni; 

    @Basic(optional = false)
    @NotNull(message = "El número de socio es obligatorio")
    @Size(min = 1, max = 50, message = "Número de socio inválido")
    @Column(name = "numero_socio")
    private String numeroSocio;

    @Size(max = 45)
    @Column(name = "telefono")
    private String telefono;

    @Basic(optional = false)
    @NotNull(message = "El estado es obligatorio")
    @Column(name = "estado")
    private String estado;

    @Basic(optional = false)
    @NotNull(message = "El género es obligatorio")
    @Column(name = "genero")
    private String genero;

    // ⚡ Relación inversa recuperada correctamente para el mostrador
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "sociosId")
    private Collection<Prestamo> prestamoCollection;

    public Socio() {
    }

    public Socio(Integer id) {
        this.id = id;
    }

    public Socio(Integer id, String numeroSocio, String estado, String genero) {
        this.id = id;
        this.numeroSocio = numeroSocio;
        this.estado = estado;
        this.genero = genero;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public Long getDni() {
        return dni;
    }

    public void setDni(Long dni) {
        this.dni = dni;
    }

    public String getNumeroSocio() {
        return numeroSocio;
    }

    public void setNumeroSocio(String numeroSocio) {
        this.numeroSocio = numeroSocio;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        if (telefono != null && !telefono.isEmpty()) {
          
            String soloNumeros = telefono.replaceAll("[^0-9]", "");

            
            this.telefono = "+54 " + soloNumeros;
        } else {
            this.telefono = null;
        }
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    @XmlTransient
    public Collection<Prestamo> getPrestamoCollection() {
        return prestamoCollection;
    }

    public void setPrestamoCollection(Collection<Prestamo> prestamoCollection) {
        this.prestamoCollection = prestamoCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Socio)) {
            return false;
        }
        Socio other = (Socio) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.mycompany.bibliotecajsf2.practico3.lab5.entidades.Socio[ id=" + id + " ]";
    }

}
