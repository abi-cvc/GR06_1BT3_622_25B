package com.gestion.mascotas.servicio;

import com.gestion.mascotas.dao.MascotaDAO;
import com.gestion.mascotas.dao.VisitaVeterinariaDAO;
import com.gestion.mascotas.modelo.entidades.Mascota;
import com.gestion.mascotas.modelo.entidades.Usuario;
import com.gestion.mascotas.modelo.entidades.VisitaVeterinaria;

import java.time.LocalDate;
import java.util.List;

public class VisitaVeterinariaService {

    private final VisitaVeterinariaDAO visitaDAO;
    private final MascotaDAO mascotaDAO;

    public VisitaVeterinariaService() {
        this.visitaDAO = new VisitaVeterinariaDAO();
        this.mascotaDAO = new MascotaDAO();
    }

    public VisitaVeterinariaService(VisitaVeterinariaDAO visitaDAO, MascotaDAO mascotaDAO) {
        this.visitaDAO = visitaDAO;
        this.mascotaDAO = mascotaDAO;
    }

    /**
     * Válida los datos de entrada para registrar una visita.
     * @return Mensaje de error o null si es válido.
     */
    private String validarDatos(LocalDate fecha, String motivo, String diagnostico,
                                String tratamiento, String observaciones,
                                String nombreVeterinario, Mascota mascota) {
        if (fecha == null) {
            return "La fecha de la visita es obligatoria.";
        }
        if (fecha.isAfter(LocalDate.now())) {
            return "La fecha de la visita no puede ser futura.";
        }
        if (motivo == null || motivo.trim().isEmpty()) {
            return "El motivo de la visita es obligatorio.";
        }
        if (mascota == null) {
            return "La mascota asociada no fue encontrada.";
        }

        if (diagnostico != null && diagnostico.length() > 1000) {
            return "El diagnóstico es demasiado largo.";
        }
        if (tratamiento != null && tratamiento.length() > 1000) {
            return "El tratamiento es demasiado largo.";
        }
        if (observaciones != null && observaciones.length() > 1000) {
            return "Las observaciones son demasiado largas.";
        }
        if (nombreVeterinario != null && nombreVeterinario.length() > 100) {
            return "El nombre del veterinario no puede exceder los 100 caracteres.";
        }

        return null;
    }

    /**
     * Registra una nueva visita veterinaria.
     * @throws IllegalArgumentException si los datos no son válidos o la mascota no existe.
     * @throws SecurityException si el usuario no tiene permiso sobre la mascota.
     * @throws RuntimeException si ocurre un error al guardar.
     */
    public void registrarVisita(LocalDate fecha, String motivo, String diagnostico,
                                String tratamiento, String observaciones,
                                String nombreVeterinario, Long mascotaId,
                                Usuario usuarioActual) {
        Mascota mascota = mascotaDAO.obtenerPorId(mascotaId);

        String errorValidacion = validarDatos(fecha, motivo, diagnostico,
                tratamiento, observaciones,
                nombreVeterinario, mascota);
        if (errorValidacion != null) {
            throw new IllegalArgumentException(errorValidacion);
        }

        // Verificar permiso: La mascota debe pertenecer al usuario actual
        if (!mascota.getUsuario().getId().equals(usuarioActual.getId())) {
            throw new SecurityException("No tienes permiso para registrar visitas para esta mascota.");
        }

        VisitaVeterinaria nuevaVisita = new VisitaVeterinaria();
        nuevaVisita.setFecha(fecha);
        nuevaVisita.setMotivo(motivo.trim());
        nuevaVisita.setDiagnostico(diagnostico.trim());
        nuevaVisita.setTratamiento(tratamiento.trim());
        nuevaVisita.setObservaciones(observaciones.trim());
        nuevaVisita.setNombreVeterinario(nombreVeterinario.trim());
        nuevaVisita.setMascota(mascota);

        try {
            visitaDAO.guardar(nuevaVisita);
        } catch (Exception e) {
            // Loggear el error e.printStackTrace();
            throw new RuntimeException("Error al guardar la visita en la base de datos.");
        }
    }

    /**
     * Consulta el historial de visitas de todas las mascotas de un usuario.
     */
    public List<VisitaVeterinaria> consultarHistorialPorUsuario(Long usuarioId) {
        return visitaDAO.obtenerPorUsuario(usuarioId);
    }

    /**
     * Elimina el registro de una visita veterinaria.
     * @throws IllegalArgumentException si la visita no existe.
     * @throws SecurityException si el usuario no tiene permiso.
     * @throws RuntimeException si ocurre un error al eliminar.
     */
    public void eliminarVisita(Long visitaId, Usuario usuarioActual) {
        VisitaVeterinaria visita = visitaDAO.obtenerPorId(visitaId);
        if (visita == null) {
            throw new IllegalArgumentException("Registro de visita no encontrado.");
        }

        // Verificar permiso
        if (!visita.getMascota().getUsuario().getId().equals(usuarioActual.getId())) {
            throw new SecurityException("No tienes permiso para eliminar este registro de visita.");
        }

        try {
            visitaDAO.eliminar(visitaId);
        } catch (Exception e) {
            // Loggear el error e.printStackTrace();
            throw new RuntimeException("Error al eliminar la visita de la base de datos.");
        }
    }

    /**
     * Cuenta las visitas de una mascota en un rango de fechas.
     * @throws IllegalArgumentException si el rango es inválido o la mascota no existe.
     * @throws SecurityException si el usuario no tiene permiso.
     */
    public long contarVisitasEnRango(Long mascotaId, LocalDate fechaInicio,
                                     LocalDate fechaFin, Usuario usuarioActual) {
        // Validar rango de fechas PRIMERO (antes de consultar la BD)
        if (fechaInicio == null || fechaFin == null || fechaInicio.isAfter(fechaFin)) {
            throw new IllegalArgumentException("Rango de fechas inválido.");
        }

        Mascota mascota = mascotaDAO.obtenerPorId(mascotaId);
        if (mascota == null) {
            throw new IllegalArgumentException("Mascota no encontrada.");
        }

        if (!mascota.getUsuario().getId().equals(usuarioActual.getId())) {
            throw new SecurityException("No tiene permiso para consultar esta mascota");
        }

        return visitaDAO.contarPorMascotaYFechas(mascotaId, fechaInicio, fechaFin);
    }
}