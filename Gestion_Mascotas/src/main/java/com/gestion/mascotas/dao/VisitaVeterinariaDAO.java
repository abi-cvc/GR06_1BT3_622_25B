package com.gestionmascotas.app.dao;

import com.gestionmascotas.app.model.VisitaVeterinaria;
import jakarta.persistence.*;

import java.util.List;

public class VisitaVeterinariaDAO {

    private EntityManagerFactory emf = Persistence.createEntityManagerFactory("gestionMascotasPU");

    public void guardar(VisitaVeterinaria visita) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.persist(visita);
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    public List<VisitaVeterinaria> obtenerTodas() {
        EntityManager em = emf.createEntityManager();
        try {
            return em.createQuery("SELECT v FROM VisitaVeterinaria v", VisitaVeterinaria.class).getResultList();
        } finally {
            em.close();
        }
    }

    public VisitaVeterinaria obtenerPorId(Long id) {
        EntityManager em = emf.createEntityManager();
        try {
            return em.find(VisitaVeterinaria.class, id);
        } finally {
            em.close();
        }
    }

    public void eliminar(Long id) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            VisitaVeterinaria visita = em.find(VisitaVeterinaria.class, id);
            if (visita != null) {
                em.remove(visita);
            }
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
    }
}
