/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.bibliotecajsf2.practico3.lab5.Servicios;

import com.mycompany.bibliotecajsf2.practico3.lab5.entidades.Libro;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import java.util.List;
import java.util.Optional;

/**
 *
 * @author Usuario
 */
@Stateless
public class SLibrosServicios {

    @PersistenceContext
    EntityManager em;

    public void agregar(Libro l) {
        em.persist(l);
    }

    public void editar(Libro l) {
        em.merge(l);
    }

    public Optional<Libro> recuperarPorId(int id) {
        return Optional.ofNullable(em.find(Libro.class, id));
    }

    public List<Libro> Listar(String filtro) {
        Query q = em.createQuery("SELECT l FROM Libro l WHERE l.titulo "
                + "LIKE :titulo OR l.editorial LIKE :editorial", Libro.class);
        q.setParameter("titulo", "%" + filtro + "%");
        q.setParameter("editorial", "%" + filtro + "%");
        return q.getResultList();
    }

    public void EliminarLibro(int id) {
        recuperarPorId(id).ifPresent(l -> {
            em.remove(l);
        });
    }
}
