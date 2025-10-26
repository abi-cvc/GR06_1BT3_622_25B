package com.gestion.mascotas.dao;

import com.gestion.mascotas.modelo.entidades.SugerenciaAlimentacion;
import com.gestion.mascotas.modelo.enums.TipoMascota;
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
            if (sugerencia.getId() == null) {
                em.persist(sugerencia);
            } else {
                em.merge(sugerencia);
            }
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            e.printStackTrace();
            throw new RuntimeException("Error al guardar la sugerencia de alimentación", e);
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
            throw new RuntimeException("Error al actualizar la sugerencia de alimentación", e);
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
            throw new RuntimeException("Error al eliminar la sugerencia de alimentación", e);
        } finally {
            em.close();
        }
    }

    /**
     * Obtiene sugerencias de alimentación filtradas por criterios (case-insensitive para raza)
     */
    public List<SugerenciaAlimentacion> obtenerPorCriterios(TipoMascota tipoMascota, String raza, Integer edad, Double peso) {
        EntityManager em = emf.createEntityManager();
        try {
            StringBuilder jpql = new StringBuilder("SELECT sa FROM SugerenciaAlimentacion sa WHERE sa.tipoMascota = :tipo");

            if (raza != null && !raza.trim().isEmpty()) {
                jpql.append(" AND LOWER(sa.raza) = LOWER(:raza)");
            }

            if (edad != null) {
                jpql.append(" AND (sa.edadMin IS NULL OR sa.edadMin <= :edad)");
                jpql.append(" AND (sa.edadMax IS NULL OR sa.edadMax >= :edad)");
            }

            if (peso != null) {
                jpql.append(" AND (sa.pesoMin IS NULL OR sa.pesoMin <= :peso)");
                jpql.append(" AND (sa.pesoMax IS NULL OR sa.pesoMax >= :peso)");
            }

            var query = em.createQuery(jpql.toString(), SugerenciaAlimentacion.class);
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