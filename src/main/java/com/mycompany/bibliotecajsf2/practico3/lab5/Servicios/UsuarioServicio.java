/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.bibliotecajsf2.practico3.lab5.Servicios;

import com.mycompany.bibliotecajsf2.practico3.lab5.entidades.Usuario;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import java.util.Optional;

/**
 *
 * @author Usuario
 */
@Stateless
public class UsuarioServicio {
    @PersistenceContext
    EntityManager em;
    
    
//    Guarda un usuario en la BD 
    public void agregar(Usuario u){
        em.persist(u);
    }
    
    /*Busco un usuario por el nombre utilizando el NNamedQuery de la entidad.
    y retorna un Optional para manejar de forma segura si existe o no.*/
    
    public Optional<Usuario> buscarPorNombre(String nombreUsuario){
        try {
            TypedQuery<Usuario> query = em.createNamedQuery("Usuario.findByNombreUsuario", Usuario.class);
            query.setParameter("nombreUsuario", nombreUsuario);
            
            // singleResult puede tirar NoResultException si no encuentra nada, 
            // por eso lo envolvemos en un try-catch limpio.
            return Optional.ofNullable(query.getSingleResult());
        } catch (Exception e) {
            return Optional.empty();
        }
    }
}
