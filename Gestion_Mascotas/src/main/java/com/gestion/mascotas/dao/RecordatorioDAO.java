package com.gestion.mascotas.dao;

import com.gestion.mascotas.modelo.Recordatorio;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import java.util.List;

public class RecordatorioDAO {

    private EntityManagerFactory emf = Persistence.createEntityManagerFactory("GestionMascotasPU");

    public void guardar(Recordatorio recordatorio) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            if (recordatorio.getId() == null) { // Si el ID es nulo, es una nueva entidad
                em.persist(recordatorio);
            } else { // Si el ID existe, se actualiza
                em.merge(recordatorio);
            }
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            e.printStackTrace();
            throw new RuntimeException("Error al guardar el recordatorio", e);
        } finally {
            em.close();
        }
    }

    public List<Recordatorio> obtenerTodos() {
        EntityManager em = emf.createEntityManager();
        try {
            return em.createQuery("SELECT r FROM Recordatorio r", Recordatorio.class).getResultList();
        } finally {
            em.close();
        }
    }

    public Recordatorio obtenerPorId(Long id) {
        EntityManager em = emf.createEntityManager();
        try {
            return em.find(Recordatorio.class, id);
        } finally {
            em.close();
        }
    }

    public void actualizar(Recordatorio recordatorio) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(recordatorio);
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            e.printStackTrace();
            throw new RuntimeException("Error al actualizar el recordatorio", e);
        } finally {
            em.close();
        }
    }

    public void eliminar(Long id) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            Recordatorio recordatorio = em.find(Recordatorio.class, id);
            if (recordatorio != null) {
                em.remove(recordatorio);
            }
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            e.printStackTrace();
            throw new RuntimeException("Error al eliminar el recordatorio", e);
        } finally {
            em.close();
        }
    }

    public List<Recordatorio> obtenerRecordatoriosPorMascota(Long mascotaId) {
        EntityManager em = emf.createEntityManager();
        try {
            return em.createQuery("SELECT r FROM Recordatorio r WHERE r.mascota.id = :mascotaId", Recordatorio.class)
                    .setParameter("mascotaId", mascotaId)
                    .getResultList();
        } finally {
            em.close();
        }
    }
}