package com.gestion.mascotas.dao;

import com.gestion.mascotas.modelo.Sugerencia;
import com.gestion.mascotas.modelo.TipoMascota;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import java.util.List;

public class SugerenciaDAO {

    private EntityManagerFactory emf = Persistence.createEntityManagerFactory("GestionMascotasPU");

    public void guardar(Sugerencia sugerencia) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            if (sugerencia.getId() == null) {
                em.persist(sugerencia);
            } else {
                em.merge(sugerencia);
            }
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            e.printStackTrace();
            throw new RuntimeException("Error al guardar la sugerencia", e);
        } finally {
            em.close();
        }
    }

    public List<Sugerencia> obtenerTodos() {
        EntityManager em = emf.createEntityManager();
        try {
            return em.createQuery("SELECT s FROM Sugerencia s", Sugerencia.class).getResultList();
        } finally {
            em.close();
        }
    }

    public Sugerencia obtenerPorId(Long id) {
        EntityManager em = emf.createEntityManager();
        try {
            return em.find(Sugerencia.class, id);
        } finally {
            em.close();
        }
    }

    public void actualizar(Sugerencia sugerencia) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(sugerencia);
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            e.printStackTrace();
            throw new RuntimeException("Error al actualizar la sugerencia", e);
        } finally {
            em.close();
        }
    }

    public void eliminar(Long id) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            Sugerencia sugerencia = em.find(Sugerencia.class, id);
            if (sugerencia != null) {
                em.remove(sugerencia);
            }
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            e.printStackTrace();
            throw new RuntimeException("Error al eliminar la sugerencia", e);
        } finally {
            em.close();
        }
    }

    /**
     * Obtiene sugerencias filtradas por tipo de mascota, raza (case-insensitive), edad y peso
     */
    public List<Sugerencia> obtenerSugerenciasPorCriterios(TipoMascota tipoMascota, String raza, Integer edad, Double peso) {
        EntityManager em = emf.createEntityManager();
        try {
            StringBuilder jpql = new StringBuilder("SELECT s FROM Sugerencia s WHERE s.tipoMascota = :tipo");

            // Agregar filtro de raza (case-insensitive)
            if (raza != null && !raza.trim().isEmpty()) {
                jpql.append(" AND LOWER(s.raza) = LOWER(:raza)");
            }

            // Agregar filtro de edad
            if (edad != null) {
                jpql.append(" AND (s.edadMin IS NULL OR s.edadMin <= :edad)");
                jpql.append(" AND (s.edadMax IS NULL OR s.edadMax >= :edad)");
            }

            // Agregar filtro de peso
            if (peso != null) {
                jpql.append(" AND (s.pesoMin IS NULL OR s.pesoMin <= :peso)");
                jpql.append(" AND (s.pesoMax IS NULL OR s.pesoMax >= :peso)");
            }

            var query = em.createQuery(jpql.toString(), Sugerencia.class);
            query.setParameter("tipo", tipoMascota);

            if (raza != null && !raza.trim().isEmpty()) {
                query.setParameter("raza", raza);
            }
            if (edad != null) {
                query.setParameter("edad", edad);
            }
            if (peso != null) {
                query.setParameter("peso", peso);
            }

            return query.getResultList();
        } finally {
            em.close();
        }
    }
}