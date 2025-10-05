package com.gestion.mascotas.dao;

import com.gestion.mascotas.modelo.SugerenciaAlimentacion;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import java.util.List;

public class SugerenciaAlimentacionDAO {

    private EntityManagerFactory emf = Persistence.createEntityManagerFactory("GestionMascotasPU");

    public void guardar(SugerenciaAlimentacion sugerencia) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.persist(sugerencia);
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    public List<SugerenciaAlimentacion> obtenerTodos() {
        EntityManager em = emf.createEntityManager();
        try {
            return em.createQuery("SELECT sa FROM SugerenciaAlimentacion sa", SugerenciaAlimentacion.class).getResultList();
        } finally {
            em.close();
        }
    }

    public SugerenciaAlimentacion obtenerPorId(Long id) {
        EntityManager em = emf.createEntityManager();
        try {
            return em.find(SugerenciaAlimentacion.class, id);
        } finally {
            em.close();
        }
    }

    public void actualizar(SugerenciaAlimentacion sugerencia) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(sugerencia);
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    public void eliminar(Long id) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            SugerenciaAlimentacion sugerencia = em.find(SugerenciaAlimentacion.class, id);
            if (sugerencia != null) {
                em.remove(sugerencia);
            }
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    public List<SugerenciaAlimentacion> obtenerSugerenciasAlimentacionPorMascota(Long mascotaId) {
        EntityManager em = emf.createEntityManager();
        try {
            return em.createQuery("SELECT sa FROM SugerenciaAlimentacion sa WHERE sa.mascota.id = :mascotaId", SugerenciaAlimentacion.class)
                    .setParameter("mascotaId", mascotaId)
                    .getResultList();
        } finally {
            em.close();
        }
    }
}