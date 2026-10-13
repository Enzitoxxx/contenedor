/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.bibliotecajsf2.practico3.lab5.Servicios;

import com.mycompany.bibliotecajsf2.practico3.lab5.entidades.Proveedor;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import java.util.List;
import java.util.Optional;

/**
 *
 * @author Alumnos
 */
@Stateless
public class ProveedorServicio {

    @PersistenceContext
    EntityManager em;

    public void agregar(Proveedor p) {
        em.persist(p);
    }

    public void editar(Proveedor p) {
        em.merge(p);
    }

    public Optional<Proveedor> recuperarPorId(int id) {
        return Optional.ofNullable(em.find(Proveedor.class, id));
    }

    public List<Proveedor> listar(String filtro) {
        Query q = em.createQuery("SELECT p FROM Proveedor p WHERE p.apellidoNombre LIKE :filtro", Proveedor.class);
        q.setParameter("filtro", "%" + filtro + "%");
        return q.getResultList();
    }

    public void eliminar(int id) {
        recuperarPorId(id).ifPresent(p -> {
            em.remove(p);
        });
    }
   // En ProveedorServicio.java

    public Long contarTotalProveedores() {
        return em.createQuery("SELECT COUNT(p) FROM Proveedor p", Long.class).getSingleResult();
    }

    public List<Object[]> obtenerCategoriasMasPopulares() {
        
        return em.createQuery("SELECT c.nombre, COUNT(p) FROM Proveedor p JOIN p.idCategoria c GROUP BY c.nombre ORDER BY COUNT(p) DESC", Object[].class)
                .setMaxResults(3)
                .getResultList();
    }
}
