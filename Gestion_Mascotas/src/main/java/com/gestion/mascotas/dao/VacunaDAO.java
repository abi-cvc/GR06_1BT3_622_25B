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

    // Agregar estos métodos a tu clase VacunaDAO existente

    /**
     * Cuenta el total de vacunas registradas para las mascotas de un usuario
     * @param usuarioId ID del usuario
     * @return Número total de vacunas
     */
    public int contarPorUsuario(Long usuarioId) {
        EntityManager em = emf.createEntityManager();
        try {
            Long count = em.createQuery(
                            "SELECT COUNT(v) FROM Vacuna v WHERE v.mascota.usuario.id = :usuarioId",
                            Long.class)
                    .setParameter("usuarioId", usuarioId)
                    .getSingleResult();
            return count.intValue();
        } finally {
            em.close();
        }
    }

    /**
     * Obtiene todas las vacunas de las mascotas de un usuario específico
     * @param usuarioId ID del usuario
     * @return Lista de vacunas
     */
    public List<Vacuna> obtenerPorUsuario(Long usuarioId) {
        EntityManager em = emf.createEntityManager();
        try {
            return em.createQuery(
                            "SELECT v FROM Vacuna v WHERE v.mascota.usuario.id = :usuarioId ORDER BY v.fecha DESC",
                            Vacuna.class)
                    .setParameter("usuarioId", usuarioId)
                    .getResultList();
        } finally {
            em.close();
        }
    }
}
