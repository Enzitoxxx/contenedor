/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.bibliotecajsf2.practico3.lab5.Servicios;

import com.mycompany.bibliotecajsf2.practico3.lab5.entidades.Consulta;

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
public class SConsultaServicio {

    @PersistenceContext
    EntityManager em;

    public void agregar(Consulta c) {
        em.persist(c);
    }

    public void editar(Consulta c) {
        em.merge(c);
    }

    public Optional<Consulta> recuperarPorId(int id) {
        return Optional.ofNullable(em.find(Consulta.class, id));
    }

    public void eliminar(int id) {
        recuperarPorId(id).ifPresent(C -> {
            em.remove(C);
        });
    }

    public List<Consulta> listarConsulta(String filtro) {
        
        Query q = em.createQuery("SELECT c FROM Consulta c "
                + "WHERE c.nombreCompleto LIKE :f", Consulta.class);
        q.setParameter("f", "%" + filtro + "%");
        return q.getResultList();
    }
}
