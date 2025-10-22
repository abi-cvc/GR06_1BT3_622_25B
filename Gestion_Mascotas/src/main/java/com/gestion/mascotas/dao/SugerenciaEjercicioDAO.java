package com.gestion.mascotas.dao;

import com.gestion.mascotas.modelo.entidades.SugerenciaEjercicio;
import com.gestion.mascotas.modelo.enums.TipoMascota;
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
            if (sugerencia.getId() == null) {
                em.persist(sugerencia);
            } else {
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

    /**
     * Obtiene sugerencias de ejercicio filtradas por criterios (case-insensitive para raza)
     */
    public List<SugerenciaEjercicio> obtenerPorCriterios(TipoMascota tipoMascota, String raza, Integer edad, Double peso) {
        EntityManager em = emf.createEntityManager();
        try {
            StringBuilder jpql = new StringBuilder("SELECT se FROM SugerenciaEjercicio se WHERE se.tipoMascota = :tipo");

            if (raza != null && !raza.trim().isEmpty()) {
                jpql.append(" AND LOWER(se.raza) = LOWER(:raza)");
            }

            if (edad != null) {
                jpql.append(" AND (se.edadMin IS NULL OR se.edadMin <= :edad)");
                jpql.append(" AND (se.edadMax IS NULL OR se.edadMax >= :edad)");
            }

            if (peso != null) {
                jpql.append(" AND (se.pesoMin IS NULL OR se.pesoMin <= :peso)");
                jpql.append(" AND (se.pesoMax IS NULL OR se.pesoMax >= :peso)");
            }

            var query = em.createQuery(jpql.toString(), SugerenciaEjercicio.class);
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