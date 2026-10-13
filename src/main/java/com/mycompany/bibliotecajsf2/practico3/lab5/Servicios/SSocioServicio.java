/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.bibliotecajsf2.practico3.lab5.Servicios;

import com.mycompany.bibliotecajsf2.practico3.lab5.entidades.Socio;
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
public class SSocioServicio {

    @PersistenceContext
    EntityManager em;

    public void agregar(Socio s) {
        em.persist(s);
    }

    public void editar(Socio s) {
        em.merge(s);
    }

    public Optional<Socio> recuperarPorId(int id) {
        return Optional.ofNullable(em.find(Socio.class, id));
    }
    
    public void eliminar(int id){
        recuperarPorId(id).ifPresent(s ->{
            em.remove(s);
        });
    }

    public List<Socio> listarSocio(String filtro) {
        Query q = em.createQuery("SELECT s FROM Socio s "
                + "WHERE s.nombres LIKE :f", Socio.class);
        q.setParameter("f", "%" + filtro + "%");
        return q.getResultList();
    }

}
