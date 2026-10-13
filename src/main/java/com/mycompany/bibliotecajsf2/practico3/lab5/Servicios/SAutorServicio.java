/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.bibliotecajsf2.practico3.lab5.Servicios;

import com.mycompany.bibliotecajsf2.practico3.lab5.entidades.Autor;
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
public class SAutorServicio {

    @PersistenceContext
    EntityManager em;

    public void agregar(Autor a) {
        em.persist(a);
    }

    public void editar(Autor a) {
        em.merge(a);
    }

    public Optional<Autor> recuperarPorId(int id) {
        return Optional.ofNullable(em.find(Autor.class, id));
    }

    public void eliminar(int id) {
        recuperarPorId(id).ifPresent(a -> {
            em.remove(a);
        });
    }

    public List<Autor> Listar(String cadena) {
        Query query = em.createQuery("SELECT a FROM Autor a "
                + "WHERE a.nombres LIKE :nombre OR a.apellido LIKE :apellido",
                Autor.class);
        query.setParameter("nombre", "%" + cadena + "%");
        query.setParameter("apellido", "%" + cadena + "%");
        return query.getResultList();
    }
}
