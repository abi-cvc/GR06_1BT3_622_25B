package com.gestion.mascotas.dao;

import com.gestion.mascotas.modelo.entidades.HistorialPeso;
import jakarta.persistence.*;
import java.util.List;

public class HistorialPesoDAO {

    private EntityManagerFactory emf = Persistence.createEntityManagerFactory("GestionMascotasPU");

    public void guardar(HistorialPeso historialPeso) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            if (historialPeso.getId() == null) {
                em.persist(historialPeso);
            } else {
                em.merge(historialPeso);
            }
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            throw new RuntimeException("Error al guardar el historial de peso", e);
        } finally {
            em.close();
        }
    }

    /**
     * Obtiene todos los registros de peso de una mascota SIN ordenar.
     * El ordenamiento se realiza en la capa de servicio.
     */
    public List<HistorialPeso> obtenerPorMascota(Long mascotaId) {
        EntityManager em = emf.createEntityManager();
        try {
            return em.createQuery(
                            "SELECT h FROM HistorialPeso h WHERE h.mascota.id = :mascotaId",
                            HistorialPeso.class)
                    .setParameter("mascotaId", mascotaId)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    public HistorialPeso obtenerPorId(Long id) {
        EntityManager em = emf.createEntityManager();
        try {
            return em.find(HistorialPeso.class, id);
        } finally {
            em.close();
        }
    }
}