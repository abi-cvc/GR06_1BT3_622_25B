package com.gestion.mascotas.dao;

import com.gestion.mascotas.modelo.entidades.VisitaVeterinaria;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.List;

public class VisitaVeterinariaDAO {

    private EntityManagerFactory emf = Persistence.createEntityManagerFactory("GestionMascotasPU");

    public void guardar(VisitaVeterinaria visita) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            if (visita.getId() == null) {
                em.persist(visita);
            } else {
                em.merge(visita);
            }
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            e.printStackTrace();
            throw new RuntimeException("Error al guardar la visita veterinaria", e);
        } finally {
            em.close();
        }
    }

    public List<VisitaVeterinaria> obtenerTodas() {
        EntityManager em = emf.createEntityManager();
        try {
            return em.createQuery(
                    "SELECT v FROM VisitaVeterinaria v ORDER BY v.fecha DESC",
                    VisitaVeterinaria.class
            ).getResultList();
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
            throw new RuntimeException("Error al eliminar la visita veterinaria", e);
        } finally {
            em.close();
        }
    }

    public List<VisitaVeterinaria> obtenerPorMascota(Long mascotaId) {
        EntityManager em = emf.createEntityManager();
        try {
            return em.createQuery(
                            "SELECT v FROM VisitaVeterinaria v WHERE v.mascota.id = :mascotaId ORDER BY v.fecha DESC",
                            VisitaVeterinaria.class
                    )
                    .setParameter("mascotaId", mascotaId)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    public List<VisitaVeterinaria> obtenerPorUsuario(Long usuarioId) {
        EntityManager em = emf.createEntityManager();
        try {
            return em.createQuery(
                            "SELECT v FROM VisitaVeterinaria v WHERE v.mascota.usuario.id = :usuarioId ORDER BY v.fecha DESC",
                            VisitaVeterinaria.class
                    )
                    .setParameter("usuarioId", usuarioId)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    public long contarVisitasPorMascota(Long mascotaId) {
        EntityManager em = emf.createEntityManager();
        try {
            return em.createQuery(
                            "SELECT COUNT(v) FROM VisitaVeterinaria v WHERE v.mascota.id = :mascotaId",
                            Long.class
                    )
                    .setParameter("mascotaId", mascotaId)
                    .getSingleResult();
        } finally {
            em.close();
        }
    }

    public long contarVisitasPorUsuario(Long usuarioId) {
        EntityManager em = emf.createEntityManager();
        try {
            return em.createQuery(
                            "SELECT COUNT(v) FROM VisitaVeterinaria v WHERE v.mascota.usuario.id = :usuarioId",
                            Long.class
                    )
                    .setParameter("usuarioId", usuarioId)
                    .getSingleResult();
        } finally {
            em.close();
        }
    }

    public long contarPorMascotaYFechas(Long mascotaId, LocalDate fechaInicio, LocalDate fechaFin) {
        EntityManager em = emf.createEntityManager();
        try {
            return em.createQuery(
                            "SELECT COUNT(v) " +
                                    "FROM VisitaVeterinaria v " +
                                    "WHERE v.mascota.id = :mascotaId " +
                                    "AND v.fecha BETWEEN :fechaInicio AND :fechaFin",
                            Long.class
                    )
                    .setParameter("mascotaId", mascotaId)
                    .setParameter("fechaInicio", fechaInicio)
                    .setParameter("fechaFin", fechaFin)
                    .getSingleResult();
        } finally {
            em.close();
        }
    }

}