package com.gestion.mascotas.dao;

import com.gestion.mascotas.modelo.RecordatorioAlimentacion;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import java.util.List;

public class RecordatorioAlimentacionDAO {

    private EntityManagerFactory emf = Persistence.createEntityManagerFactory("GestionMascotasPU");

    public void guardar(RecordatorioAlimentacion recordatorio) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.persist(recordatorio);
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    public List<RecordatorioAlimentacion> obtenerTodos() {
        EntityManager em = emf.createEntityManager();
        try {
            return em.createQuery("SELECT ra FROM RecordatorioAlimentacion ra", RecordatorioAlimentacion.class).getResultList();
        } finally {
            em.close();
        }
    }

    public RecordatorioAlimentacion obtenerPorId(Long id) {
        EntityManager em = emf.createEntityManager();
        try {
            return em.find(RecordatorioAlimentacion.class, id);
        } finally {
            em.close();
        }
    }

    public void actualizar(RecordatorioAlimentacion recordatorio) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(recordatorio);
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
            RecordatorioAlimentacion recordatorio = em.find(RecordatorioAlimentacion.class, id);
            if (recordatorio != null) {
                em.remove(recordatorio);
            }
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    public List<RecordatorioAlimentacion> obtenerRecordatoriosAlimentacionPorMascota(Long mascotaId) {
        EntityManager em = emf.createEntityManager();
        try {
            return em.createQuery("SELECT ra FROM RecordatorioAlimentacion ra WHERE ra.mascota.id = :mascotaId", RecordatorioAlimentacion.class)
                    .setParameter("mascotaId", mascotaId)
                    .getResultList();
        } finally {
            em.close();
        }
    }
}