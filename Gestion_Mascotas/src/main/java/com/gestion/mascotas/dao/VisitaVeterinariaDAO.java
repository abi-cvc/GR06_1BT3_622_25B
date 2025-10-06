package com.gestion.mascotas.dao;

import com.gestion.mascotas.modelo.VisitaVeterinaria;
import jakarta.persistence.*;

import java.util.List;

public class VisitaVeterinariaDAO {

    private EntityManagerFactory emf = Persistence.createEntityManagerFactory("GestionMascotasPU");

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

    /**
     * Obtiene el total de visitas veterinarias para una mascota específica.
     * @param mascotaId El ID de la mascota.
     * @return El número total de visitas de la mascota.
     */
    public long contarVisitasPorMascota(Long mascotaId) {
        EntityManager em = emf.createEntityManager();
        try {
            return em.createQuery("SELECT COUNT(v) FROM VisitaVeterinaria v WHERE v.mascota.id = :mascotaId", Long.class)
                    .setParameter("mascotaId", mascotaId)
                    .getSingleResult();
        } finally {
            em.close();
        }
    }
}
