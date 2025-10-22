package com.gestion.mascotas.servicio;

import com.gestion.mascotas.dao.MascotaDAO;
import com.gestion.mascotas.dao.VisitaVeterinariaDAO;
import com.gestion.mascotas.modelo.entidades.Mascota;
import com.gestion.mascotas.modelo.entidades.Usuario;
import com.gestion.mascotas.modelo.entidades.VisitaVeterinaria;

import java.time.LocalDate;
import java.util.List;

public class VisitaVeterinariaService {

    private VisitaVeterinariaDAO visitaDAO = new VisitaVeterinariaDAO();
    private MascotaDAO mascotaDAO = new MascotaDAO();

    /**
     * Valida los datos de entrada para registrar una visita. (Corresponde a validarDatos)
     * @return Mensaje de error o null si es válido.
     */
    private String validarDatos(LocalDate fecha, String motivo, Mascota mascota) {
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
        // Podrían añadirse más validaciones (longitud del motivo, etc.)
        return null;
    }

    /**
     * Registra una nueva visita veterinaria. (Corresponde a registrarVisita)
     * @throws IllegalArgumentException si los datos no son válidos o la mascota no existe.
     * @throws SecurityException si el usuario no tiene permiso sobre la mascota.
     * @throws RuntimeException si ocurre un error al guardar.
     */
    public void registrarVisita(LocalDate fecha, String motivo, Long mascotaId, Usuario usuarioActual) {
        Mascota mascota = mascotaDAO.obtenerPorId(mascotaId);

        String errorValidacion = validarDatos(fecha, motivo, mascota);
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
        nuevaVisita.setMascota(mascota);

        try {
            visitaDAO.guardar(nuevaVisita);
        } catch (Exception e) {
            // Loggear el error e.printStackTrace();
            throw new RuntimeException("Error al guardar la visita en la base de datos.");
        }
    }

    /**
     * Consulta el historial de visitas de todas las mascotas de un usuario. (Corresponde a consultarHistorial)
     */
    public List<VisitaVeterinaria> consultarHistorialPorUsuario(Long usuarioId) {
        return visitaDAO.obtenerPorUsuario(usuarioId);
    }

    /**
     * Consulta el historial de visitas de una mascota específica.
     */
    public List<VisitaVeterinaria> consultarHistorialPorMascota(Long mascotaId, Usuario usuarioActual) {
        Mascota mascota = mascotaDAO.obtenerPorId(mascotaId);
        if (mascota == null) {
            throw new IllegalArgumentException("Mascota no encontrada.");
        }
        // Verificar permiso
        if (!mascota.getUsuario().getId().equals(usuarioActual.getId())) {
            throw new SecurityException("No tienes permiso para ver las visitas de esta mascota.");
        }
        return visitaDAO.obtenerPorMascota(mascotaId);
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

    /*
    public void actualizarVisita(Long visitaId, LocalDate nuevaFecha, String nuevoMotivo, Usuario usuarioActual) {
        VisitaVeterinaria visita = visitaDAO.obtenerPorId(visitaId);
        if (visita == null) {
            throw new IllegalArgumentException("Visita no encontrada.");
        }
        if (!visita.getMascota().getUsuario().getId().equals(usuarioActual.getId())) {
            throw new SecurityException("No tienes permiso para actualizar esta visita.");
        }

        String errorValidacion = validarDatos(nuevaFecha, nuevoMotivo, visita.getMascota());
        if (errorValidacion != null) {
            throw new IllegalArgumentException(errorValidacion);
        }

        visita.setFecha(nuevaFecha);
        visita.setMotivo(nuevoMotivo.trim());

        try {
            visitaDAO.guardar(visita); // Usará merge por tener ID
        } catch (Exception e) {
            throw new RuntimeException("Error al actualizar la visita.");
        }
    }
    */

    public long contarVisitasEnRango(Long mascotaId, LocalDate fechaInicio, LocalDate fechaFin, Usuario usuarioActual) {
        Mascota mascota = mascotaDAO.obtenerPorId(mascotaId);
        if (mascota == null) {
            throw new IllegalArgumentException("Mascota no encontrada.");
        }
        if (!mascota.getUsuario().getId().equals(usuarioActual.getId())) {
            throw new SecurityException("No tiene permiso para consultar esta mascota");
        }
        if (fechaInicio == null || fechaFin == null || fechaInicio.isAfter(fechaFin)) {
            throw new IllegalArgumentException("Rango de fechas inválido.");
        }
        return visitaDAO.contarPorMascotaYFechas(mascotaId, fechaInicio, fechaFin);
    }
}