package com.gestion.mascotas.dao;

import com.gestion.mascotas.modelo.RecordatorioPaseo;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import java.util.List;

public class RecordatorioPaseoDAO {

    private EntityManagerFactory emf = Persistence.createEntityManagerFactory("GestionMascotasPU");

    public void guardar(RecordatorioPaseo recordatorio) {
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

    public List<RecordatorioPaseo> obtenerTodos() {
        EntityManager em = emf.createEntityManager();
        try {
            return em.createQuery("SELECT rp FROM RecordatorioPaseo rp", RecordatorioPaseo.class).getResultList();
        } finally {
            em.close();
        }
    }

    public RecordatorioPaseo obtenerPorId(Long id) {
        EntityManager em = emf.createEntityManager();
        try {
            return em.find(RecordatorioPaseo.class, id);
        } finally {
            em.close();
        }
    }

    public void actualizar(RecordatorioPaseo recordatorio) {
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
            RecordatorioPaseo recordatorio = em.find(RecordatorioPaseo.class, id);
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

    public List<RecordatorioPaseo> obtenerRecordatoriosPaseoPorMascota(Long mascotaId) {
        EntityManager em = emf.createEntityManager();
        try {
            return em.createQuery("SELECT rp FROM RecordatorioPaseo rp WHERE rp.mascota.id = :mascotaId", RecordatorioPaseo.class)
                    .setParameter("mascotaId", mascotaId)
                    .getResultList();
        } finally {
            em.close();
        }
    }
}