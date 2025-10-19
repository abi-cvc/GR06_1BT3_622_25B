package com.gestion.mascotas.servicio;

import com.gestion.mascotas.dao.MascotaDAO;
import com.gestion.mascotas.dao.RecordatorioPaseoDAO;
import com.gestion.mascotas.modelo.entidades.Mascota;
import com.gestion.mascotas.modelo.entidades.RecordatorioPaseo;
import com.gestion.mascotas.modelo.entidades.Usuario;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Servicio que maneja la lógica de negocio para los recordatorios de paseo.
 */
public class RecordatorioPaseoService {

    private final RecordatorioPaseoDAO recordatorioDAO;
    private final MascotaDAO mascotaDAO;

    public RecordatorioPaseoService() {
        recordatorioDAO = new RecordatorioPaseoDAO();
        mascotaDAO = new MascotaDAO();
    }

    /**
     * Registra un nuevo recordatorio de paseo para una mascota del usuario.
     */
    public boolean registrarRecordatorio(Usuario usuario,
                                         Long mascotaId,
                                         int frecuencia,
                                         Integer duracionMinutos,
                                         String[] diasSeleccionados,
                                         List<LocalTime> horarios) {

        // Validar mascota
        Mascota mascota = mascotaDAO.obtenerPorId(mascotaId);
        if (mascota == null) {
            throw new IllegalArgumentException("La mascota seleccionada no existe.");
        }

        if (!mascota.getUsuario().getId().equals(usuario.getId())) {
            throw new IllegalArgumentException("No tiene permisos para registrar recordatorios para esta mascota.");
        }

        // Validaciones de datos
        if (frecuencia <= 0) {
            throw new IllegalArgumentException("La frecuencia de paseo debe ser mayor que cero.");
        }

        if (horarios == null || horarios.isEmpty()) {
            throw new IllegalArgumentException("Debe ingresar al menos un horario de paseo.");
        }

        // Crear el recordatorio
        RecordatorioPaseo recordatorio = new RecordatorioPaseo();
        recordatorio.setMascota(mascota);
        recordatorio.setActivo(true);
        recordatorio.setDuracionMinutos(duracionMinutos);
        recordatorio.setDiasSemana(
                (diasSeleccionados != null) ? String.join(",", diasSeleccionados) : ""
        );
        recordatorio.setHorarios(
                horarios.stream()
                        .map(LocalTime::toString)
                        .collect(Collectors.joining(","))
        );
        recordatorio.setFechaHoraRecordatorio(LocalDateTime.now());

        String descripcion = "Recordatorio de paseo: " + frecuencia + " vez/veces al día";
        if (duracionMinutos != null && duracionMinutos > 0) {
            descripcion += " - " + duracionMinutos + " minutos";
        }
        recordatorio.setDescripcion(descripcion);

        // Guardar en la base de datos
        return recordatorioDAO.guardar(recordatorio);
    }

    /**
     * Actualiza un recordatorio existente.
     */
    public boolean actualizarRecordatorio(Long idRecordatorio, Usuario usuario,
                                          int frecuencia, Integer duracionMinutos,
                                          String[] diasSeleccionados, List<LocalTime> horarios) {

        RecordatorioPaseo existente = recordatorioDAO.obtenerPorId(idRecordatorio);
        if (existente == null) {
            throw new IllegalArgumentException("El recordatorio no existe.");
        }

        if (!existente.getMascota().getUsuario().getId().equals(usuario.getId())) {
            throw new IllegalArgumentException("No tiene permisos para modificar este recordatorio.");
        }

        existente.setDuracionMinutos(duracionMinutos);
        existente.setDiasSemana(
                (diasSeleccionados != null) ? String.join(",", diasSeleccionados) : ""
        );
        existente.setHorarios(
                horarios.stream()
                        .map(LocalTime::toString)
                        .collect(Collectors.joining(","))
        );

        String descripcion = "Recordatorio de paseo actualizado: " + frecuencia + " vez/veces al día";
        if (duracionMinutos != null && duracionMinutos > 0) {
            descripcion += " - " + duracionMinutos + " minutos";
        }
        existente.setDescripcion(descripcion);

        return recordatorioDAO.actualizar(existente);
    }

    /**
     * Elimina un recordatorio (solo si pertenece al usuario).
     */
    public boolean eliminarRecordatorio(Long idRecordatorio, Usuario usuario) {
        RecordatorioPaseo recordatorio = recordatorioDAO.obtenerPorId(idRecordatorio);

        if (recordatorio == null) {
            throw new IllegalArgumentException("El recordatorio no existe.");
        }

        if (!recordatorio.getMascota().getUsuario().getId().equals(usuario.getId())) {
            throw new IllegalArgumentException("No tiene permisos para eliminar este recordatorio.");
        }

        return recordatorioDAO.eliminar(idRecordatorio);
    }

    /**
     * Desactiva un recordatorio (sin eliminarlo).
     */
    public boolean desactivarRecordatorio(Long idRecordatorio, Usuario usuario) {
        RecordatorioPaseo recordatorio = recordatorioDAO.obtenerPorId(idRecordatorio);

        if (recordatorio == null) {
            throw new IllegalArgumentException("El recordatorio no existe.");
        }

        if (!recordatorio.getMascota().getUsuario().getId().equals(usuario.getId())) {
            throw new IllegalArgumentException("No tiene permisos para desactivar este recordatorio.");
        }

        recordatorio.setActivo(false);
        return recordatorioDAO.actualizar(recordatorio);
    }
}
