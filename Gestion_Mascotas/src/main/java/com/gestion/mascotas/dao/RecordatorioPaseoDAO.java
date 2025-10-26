package com.gestion.mascotas.dao;

import com.gestion.mascotas.modelo.entidades.RecordatorioPaseo;
import com.gestion.mascotas.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.hibernate.Hibernate;

import java.util.List;

public class RecordatorioPaseoDAO {

    // Guardar o actualizar recordatorio de paseo
    public boolean guardar(RecordatorioPaseo recordatorio) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();

            if (recordatorio.getId() == null) {
                session.persist(recordatorio);
            } else {
                session.merge(recordatorio);
            }

            transaction.commit();
            return true;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
            return false;
        }
    }

    // Obtener todos los recordatorios de paseo
    public List<RecordatorioPaseo> obtenerTodos() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery(
                    "FROM RecordatorioPaseo rp ORDER BY rp.mascota.nombre, rp.horarios",
                    RecordatorioPaseo.class
            ).list();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // Obtener recordatorio por ID
    public RecordatorioPaseo obtenerPorId(Long id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            // Usar JOIN FETCH para cargar la mascota junto con el recordatorio
            Query<RecordatorioPaseo> query = session.createQuery(
                    "FROM RecordatorioPaseo rp " +
                            "LEFT JOIN FETCH rp.mascota m " +
                            "LEFT JOIN FETCH m.usuario " +
                            "WHERE rp.id = :id",
                    RecordatorioPaseo.class
            );
            query.setParameter("id", id);
            RecordatorioPaseo recordatorio = query.uniqueResult();

            // Inicializar explícitamente la mascota si existe
            if (recordatorio != null && recordatorio.getMascota() != null) {
                Hibernate.initialize(recordatorio.getMascota());
                Hibernate.initialize(recordatorio.getMascota().getUsuario());
            }

            return recordatorio;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // Actualizar recordatorio
    public boolean actualizar(RecordatorioPaseo recordatorio) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.merge(recordatorio);
            transaction.commit();
            return true;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
            return false;
        }
    }

    // Eliminar recordatorio
    public boolean eliminar(Long id) {
        Transaction transaction = null;
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            transaction = session.beginTransaction();

            RecordatorioPaseo recordatorio = session.get(RecordatorioPaseo.class, id);
            if (recordatorio != null) {
                session.remove(recordatorio);
                transaction.commit();
                System.out.println("Recordatorio de paseo eliminado exitosamente: ID = " + id);
                return true;
            } else {
                System.err.println("No se encontró recordatorio de paseo con ID: " + id);
                if (transaction != null) {
                    transaction.rollback();
                }
                return false;
            }
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            System.err.println("Error al eliminar recordatorio de paseo con ID: " + id);
            e.printStackTrace();
            return false;
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
    }

    // Obtener recordatorios de paseo por mascota
    public List<RecordatorioPaseo> obtenerRecordatoriosPaseoPorMascota(Long mascotaId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<RecordatorioPaseo> query = session.createQuery(
                    "FROM RecordatorioPaseo rp WHERE rp.mascota.id = :mascotaId ORDER BY rp.horarios",
                    RecordatorioPaseo.class
            );
            query.setParameter("mascotaId", mascotaId);
            return query.list();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // Obtener recordatorios activos por mascota
    public List<RecordatorioPaseo> obtenerRecordatoriosActivosPorMascota(Long mascotaId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<RecordatorioPaseo> query = session.createQuery(
                    "FROM RecordatorioPaseo rp WHERE rp.mascota.id = :mascotaId AND rp.activo = true ORDER BY rp.horarios",
                    RecordatorioPaseo.class
            );
            query.setParameter("mascotaId", mascotaId);
            return query.list();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // Desactivar recordatorio (sin eliminarlo)
    public boolean desactivarRecordatorio(Long id) {
        Transaction transaction = null;
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            transaction = session.beginTransaction();

            RecordatorioPaseo recordatorio = session.get(RecordatorioPaseo.class, id);
            if (recordatorio != null) {
                recordatorio.setActivo(false);
                session.merge(recordatorio);
                transaction.commit();
                return true;
            }

            return false;
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            e.printStackTrace();
            return false;
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
    }
}