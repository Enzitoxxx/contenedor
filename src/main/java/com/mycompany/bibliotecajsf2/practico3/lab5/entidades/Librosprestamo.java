/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.bibliotecajsf2.practico3.lab5.entidades;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;
import jakarta.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

/**
 *
 * @author Usuario
 */
@Entity
@Table(name = "librosprestamos")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Librosprestamo.findAll", query = "SELECT l FROM Librosprestamo l"),
    @NamedQuery(name = "Librosprestamo.findById", query = "SELECT l FROM Librosprestamo l WHERE l.id = :id")})
public class Librosprestamo implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    
    @JoinColumn(name = "libros_id", referencedColumnName = "id")
    @ManyToOne(optional = false,fetch = FetchType.LAZY)
    private Libro librosId;
    @JoinColumn(name = "prestamos_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Prestamo prestamosId;

    public Librosprestamo() {
    }

    public Librosprestamo(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Libro getLibrosId() {
        return librosId;
    }

    public void setLibrosId(Libro librosId) {
        this.librosId = librosId;
    }

    public Prestamo getPrestamosId() {
        return prestamosId;
    }

    public void setPrestamosId(Prestamo prestamosId) {
        this.prestamosId = prestamosId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Librosprestamo)) {
            return false;
        }
        Librosprestamo other = (Librosprestamo) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.mycompany.bibliotecajsf2.practico3.lab5.entidades.Librosprestamo[ id=" + id + " ]";
    }
    
}
