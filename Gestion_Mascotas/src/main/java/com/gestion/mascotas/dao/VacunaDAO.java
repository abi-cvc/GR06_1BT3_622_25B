package com.gestion.mascotas.dao;

import com.gestion.mascotas.modelo.Vacuna;
import jakarta.persistence.*;

import java.util.List;

public class VacunaDAO {

    private EntityManagerFactory emf = Persistence.createEntityManagerFactory("gestionMascotasPU");

    public void guardar(Vacuna vacuna) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.persist(vacuna);
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    public List<Vacuna> obtenerTodas() {
        EntityManager em = emf.createEntityManager();
        try {
            return em.createQuery("SELECT v FROM Vacuna v", Vacuna.class).getResultList();
        } finally {
            em.close();
        }
    }

    public Vacuna obtenerPorId(Long id) {
        EntityManager em = emf.createEntityManager();
        try {
            return em.find(Vacuna.class, id);
        } finally {
            em.close();
        }
    }

    public void eliminar(Long id) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            Vacuna vacuna = em.find(Vacuna.class, id);
            if (vacuna != null) {
                em.remove(vacuna);
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
