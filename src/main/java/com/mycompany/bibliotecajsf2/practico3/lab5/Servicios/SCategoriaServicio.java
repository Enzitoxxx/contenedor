/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.bibliotecajsf2.practico3.lab5.Servicios;

import com.mycompany.bibliotecajsf2.practico3.lab5.entidades.Categoria;
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
public class SCategoriaServicio {

    @PersistenceContext
    EntityManager em;

    public void guardar(Categoria c) {
        em.persist(c);
    }

    public void editar(Categoria c) {
        em.merge(c);
    }

    public Optional<Categoria> recuperarPorId(int id) {
        return Optional.ofNullable(em.find(Categoria.class, id));
    }

    public void Eliminar(int id) {

        recuperarPorId(id).ifPresent(c -> {
            em.remove(c);
        });

    }

    public List<Categoria> RecuperarCategorias(String filtro) {
        Query q = em.createQuery("SELECT c FROM Categoria c "
                + "WHERE c.nombre LIKE :f", Categoria.class);
        q.setParameter("f", "%" + filtro + "%");
        return q.getResultList();
    }
}
