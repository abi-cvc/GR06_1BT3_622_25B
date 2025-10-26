package com.gestion.mascotas.dao;

import com.gestion.mascotas.modelo.entidades.RecordatorioAlimentacion;
import com.gestion.mascotas.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.hibernate.Hibernate;

import java.util.List;

public class RecordatorioAlimentacionDAO {

    // Guardar o actualizar recordatorio de alimentación
    public boolean guardar(RecordatorioAlimentacion recordatorio) {
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

    // Obtener todos los recordatorios de alimentación
    public List<RecordatorioAlimentacion> obtenerTodos() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery(
                    "FROM RecordatorioAlimentacion ra ORDER BY ra.mascota.nombre, ra.horarios",
                    RecordatorioAlimentacion.class
            ).list();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // Obtener recordatorio por ID
    public RecordatorioAlimentacion obtenerPorId(Long id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            // Usar JOIN FETCH para cargar la mascota junto con el recordatorio
            Query<RecordatorioAlimentacion> query = session.createQuery(
                    "FROM RecordatorioAlimentacion ra " +
                            "LEFT JOIN FETCH ra.mascota m " +
                            "LEFT JOIN FETCH m.usuario " +
                            "WHERE ra.id = :id",
                    RecordatorioAlimentacion.class
            );
            query.setParameter("id", id);
            RecordatorioAlimentacion recordatorio = query.uniqueResult();

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
    public boolean actualizar(RecordatorioAlimentacion recordatorio) {
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

            RecordatorioAlimentacion recordatorio = session.get(RecordatorioAlimentacion.class, id);
            if (recordatorio != null) {
                session.remove(recordatorio);
                transaction.commit();
                System.out.println("Recordatorio de alimentación eliminado exitosamente: ID = " + id);
                return true;
            } else {
                System.err.println("No se encontró recordatorio de alimentación con ID: " + id);
                if (transaction != null) {
                    transaction.rollback();
                }
                return false;
            }
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            System.err.println("Error al eliminar recordatorio de alimentación con ID: " + id);
            e.printStackTrace();
            return false;
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
    }

    // Obtener recordatorios de alimentación por mascota
    public List<RecordatorioAlimentacion> obtenerRecordatoriosAlimentacionPorMascota(Long mascotaId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<RecordatorioAlimentacion> query = session.createQuery(
                    "FROM RecordatorioAlimentacion ra WHERE ra.mascota.id = :mascotaId ORDER BY ra.horarios",
                    RecordatorioAlimentacion.class
            );
            query.setParameter("mascotaId", mascotaId);
            return query.list();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // Obtener recordatorios activos por mascota
    public List<RecordatorioAlimentacion> obtenerRecordatoriosActivosPorMascota(Long mascotaId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<RecordatorioAlimentacion> query = session.createQuery(
                    "FROM RecordatorioAlimentacion ra WHERE ra.mascota.id = :mascotaId AND ra.activo = true ORDER BY ra.horarios",
                    RecordatorioAlimentacion.class
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
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();

            RecordatorioAlimentacion recordatorio = session.get(RecordatorioAlimentacion.class, id);
            if (recordatorio != null) {
                recordatorio.setActivo(false);
                session.merge(recordatorio);
                transaction.commit();
                return true;
            }

            return false;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
            return false;
        }
    }
}