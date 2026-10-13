package com.mycompany.bibliotecajsf2.practico3.lab5.entidades;

import jakarta.persistence.Basic;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlTransient;
import java.io.Serializable;
import java.util.Collection;
import java.util.Date;

/**
 * @author Usuario
 */
@Entity
@Table(name = "prestamos")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Prestamo.findAll", query = "SELECT p FROM Prestamo p"),
    @NamedQuery(name = "Prestamo.findById", query = "SELECT p FROM Prestamo p WHERE p.id = :id"),
    @NamedQuery(name = "Prestamo.findByFecha", query = "SELECT p FROM Prestamo p WHERE p.fecha = :fecha"),
    @NamedQuery(name = "Prestamo.findByFechaDevuelve", query = "SELECT p FROM Prestamo p WHERE p.fechaDevuelve = :fechaDevuelve"),
    @NamedQuery(name = "Prestamo.findByMulta", query = "SELECT p FROM Prestamo p WHERE p.multa = :multa"),
    @NamedQuery(name = "Prestamo.findByEstado", query = "SELECT p FROM Prestamo p WHERE p.estado = :estado")})
public class Prestamo implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "fecha")
    @Temporal(TemporalType.DATE)
    private Date fecha;
    
    @Column(name = "fecha_devuelve")
    @Temporal(TemporalType.DATE)
    private Date fechaDevuelve;
    
    @Column(name = "multa")
    private Float multa;
    
    @Column(name = "estado")
    private String estado;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "prestamosId")
    private Collection<Librosprestamo> librosprestamoCollection;
    
    @JoinColumn(name = "socios_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Socio sociosId;

    public Prestamo() {
        this.estado = "ACTIVO";
    }

    public Prestamo(Integer id) {
        this.id = id;
    }

    public Prestamo(Integer id, Date fecha) {
        this.id = id;
        this.fecha = fecha;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public Date getFechaDevuelve() {
        return fechaDevuelve;
    }

    public void setFechaDevuelve(Date fechaDevuelve) {
        this.fechaDevuelve = fechaDevuelve;
    }

    public Float getMulta() {
        return multa;
    }

    public void setMulta(Float multa) {
        this.multa = multa;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    @XmlTransient
    public Collection<Librosprestamo> getLibrosprestamoCollection() {
        return librosprestamoCollection;
    }

    public void setLibrosprestamoCollection(Collection<Librosprestamo> librosprestamoCollection) {
        this.librosprestamoCollection = librosprestamoCollection;
    }

    public Socio getSociosId() {
        return sociosId;
    }

    public void setSociosId(Socio sociosId) {
        this.sociosId = sociosId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Prestamo)) {
            return false;
        }
        Prestamo other = (Prestamo) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.mycompany.bibliotecajsf2.practico3.lab5.entidades.Prestamo[ id=" + id + " ]";
    }
}