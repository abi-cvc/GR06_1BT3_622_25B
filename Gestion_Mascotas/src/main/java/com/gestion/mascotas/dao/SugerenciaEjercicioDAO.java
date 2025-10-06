package com.gestion.mascotas.dao;

import com.gestion.mascotas.modelo.SugerenciaEjercicio;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import java.util.List;

public class SugerenciaEjercicioDAO {

    private EntityManagerFactory emf = Persistence.createEntityManagerFactory("GestionMascotasPU");

    public void guardar(SugerenciaEjercicio sugerencia) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            if (sugerencia.getId() == null) { // Si el ID es nulo, es una nueva entidad
                em.persist(sugerencia);
            } else { // Si el ID existe, se actualiza
                em.merge(sugerencia);
            }
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            e.printStackTrace();
            throw new RuntimeException("Error al guardar la sugerencia de ejercicio", e);
        } finally {
            em.close();
        }
    }

    public List<SugerenciaEjercicio> obtenerTodos() {
        EntityManager em = emf.createEntityManager();
        try {
            return em.createQuery("SELECT se FROM SugerenciaEjercicio se", SugerenciaEjercicio.class).getResultList();
        } finally {
            em.close();
        }
    }

    public SugerenciaEjercicio obtenerPorId(Long id) {
        EntityManager em = emf.createEntityManager();
        try {
            return em.find(SugerenciaEjercicio.class, id);
        } finally {
            em.close();
        }
    }

    public void actualizar(SugerenciaEjercicio sugerencia) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(sugerencia);
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            e.printStackTrace();
            throw new RuntimeException("Error al actualizar la sugerencia de ejercicio", e);
        } finally {
            em.close();
        }
    }

    public void eliminar(Long id) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            SugerenciaEjercicio sugerencia = em.find(SugerenciaEjercicio.class, id);
            if (sugerencia != null) {
                em.remove(sugerencia);
            }
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            e.printStackTrace();
            throw new RuntimeException("Error al eliminar la sugerencia de ejercicio", e);
        } finally {
            em.close();
        }
    }

    public List<SugerenciaEjercicio> obtenerSugerenciasEjercicioPorMascota(Long mascotaId) {
        EntityManager em = emf.createEntityManager();
        try {
            return em.createQuery("SELECT se FROM SugerenciaEjercicio se WHERE se.mascota.id = :mascotaId", SugerenciaEjercicio.class)
                    .setParameter("mascotaId", mascotaId)
                    .getResultList();
        } finally {
            em.close();
        }
    }
}