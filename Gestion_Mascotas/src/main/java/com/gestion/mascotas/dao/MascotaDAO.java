package com.gestion.mascotas.dao;

import com.gestion.mascotas.modelo.entidades.Mascota;
import jakarta.persistence.*;

import java.util.List;

public class MascotaDAO {

    private EntityManagerFactory emf = Persistence.createEntityManagerFactory("GestionMascotasPU"); // Asegúrate de que el nombre de la PU sea correcto

    public void guardar(Mascota mascota) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            // --- CAMBIO AQUÍ ---
            if (mascota.getId() == null) {
                // Si es nueva, usa persist
                em.persist(mascota);
            } else {
                // Si ya existe (tiene ID), usa merge para actualizar
                em.merge(mascota);
            }
            // --- FIN DEL CAMBIO ---
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            e.printStackTrace(); // Es buena idea loggear el error
            // Podrías lanzar una excepción personalizada aquí si prefieres
            // throw new RuntimeException("Error al guardar la mascota", e);
        } finally {
            em.close();
        }
    }

    public List<Mascota> obtenerTodas() {
        EntityManager em = emf.createEntityManager();
        try {
            return em.createQuery("SELECT m FROM Mascota m", Mascota.class).getResultList();
        } finally {
            em.close();
        }
    }

    public Mascota obtenerPorId(Long id) {
        EntityManager em = emf.createEntityManager();
        try {
            return em.find(Mascota.class, id);
        } finally {
            em.close();
        }
    }

    public void eliminar(Long id) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            Mascota mascota = em.find(Mascota.class, id);
            if (mascota != null) {
                em.remove(mascota);
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
     * Obtiene todas las mascotas asociadas a un usuario específico.
     * @param usuarioId El ID del usuario.
     * @return Una lista de mascotas del usuario.
     */
    public List<Mascota> obtenerMascotasPorUsuario(Long usuarioId) {
        EntityManager em = emf.createEntityManager();
        try {
            return em.createQuery("SELECT m FROM Mascota m WHERE m.usuario.id = :usuarioId", Mascota.class)
                    .setParameter("usuarioId", usuarioId)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    public List<Mascota> obtenerPorUsuario(Long usuarioId) {
        EntityManager em = emf.createEntityManager();
        try {
            return em.createQuery(
                            "SELECT m FROM Mascota m WHERE m.usuario.id = :usuarioId ORDER BY m.nombre",
                            Mascota.class)
                    .setParameter("usuarioId", usuarioId)
                    .getResultList();
        } finally {
            em.close();
        }
    }
}
