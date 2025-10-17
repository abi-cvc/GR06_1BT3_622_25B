package com.gestion.mascotas.service;

import com.gestion.mascotas.dao.RecordatorioPaseoDAO;
import com.gestion.mascotas.modelo.Mascota;
import com.gestion.mascotas.modelo.RecordatorioPaseo;
import com.gestion.mascotas.modelo.Usuario;
import com.gestion.mascotas.logica.ValidadorRecordatorio;
import com.gestion.mascotas.logica.GestorHorarios;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

public class RecordatorioPaseoService extends RecordatorioService {

    private RecordatorioPaseoDAO recordatorioDAO;

    public RecordatorioPaseoService() {
        super();
        this.recordatorioDAO = new RecordatorioPaseoDAO();
    }

    public boolean registrarRecordatorio(String mascotaIdStr, String frecuenciaStr,
                                         String duracionMinutosStr, String[] diasSeleccionados,
                                         java.util.function.Function<String, String> getParameter,
                                         Usuario usuario) {
        // Validar frecuencia
        String error = ValidadorRecordatorio.validarFrecuencia(frecuenciaStr);
        if (error != null) {
            throw new IllegalArgumentException(error);
        }

        // Validar y obtener mascota
        Mascota mascota = validarYObtenerMascota(mascotaIdStr, usuario);

        // Obtener horarios
        int frecuencia = Integer.parseInt(frecuenciaStr);
        List<LocalTime> horarios = GestorHorarios.extraerHorarios(frecuencia, "horarioPaseo", getParameter);

        // Validar horarios
        error = GestorHorarios.validarHorariosNoVacios(horarios);
        if (error != null) {
            throw new IllegalArgumentException(error);
        }

        // Validar duración
        Integer duracionMinutos = null;
        if (duracionMinutosStr != null && !duracionMinutosStr.trim().isEmpty()) {
            duracionMinutos = Integer.parseInt(duracionMinutosStr);
            error = ValidadorRecordatorio.validarDuracion(duracionMinutos);
            if (error != null) {
                throw new IllegalArgumentException(error);
            }
        }

        // Construir días de semana
        String diasSemana = GestorHorarios.construirDiasSemana(diasSeleccionados);

        // Crear recordatorio
        RecordatorioPaseo recordatorio = new RecordatorioPaseo();
        recordatorio.setMascota(mascota);
        recordatorio.setActivo(true);

        String descripcion = "Recordatorio de paseo: " + frecuencia + " vez/veces al día";
        if (duracionMinutos != null) {
            descripcion += " - " + duracionMinutos + " minutos";
        }
        recordatorio.setDescripcion(descripcion);

        recordatorio.setHorarios(GestorHorarios.convertirHorariosAString(horarios));
        recordatorio.setDiasSemana(diasSemana);
        recordatorio.setDuracionMinutos(duracionMinutos);
        recordatorio.setFechaHoraRecordatorio(LocalDateTime.now());

        System.out.println("Guardando recordatorio de paseo: " + recordatorio);

        return recordatorioDAO.guardar(recordatorio);
    }

    public boolean actualizarRecordatorio(Long recordatorioId, String frecuenciaStr,
                                          String duracionMinutosStr, String[] diasSeleccionados,
                                          java.util.function.Function<String, String> getParameter,
                                          Usuario usuario) {
        RecordatorioPaseo recordatorio = recordatorioDAO.obtenerPorId(recordatorioId);

        // Validar permisos
        validarPermisos(recordatorio, usuario,
                r -> ((RecordatorioPaseo) r).getMascota().getUsuario());

        // Obtener horarios
        int frecuencia = Integer.parseInt(frecuenciaStr);
        List<LocalTime> horarios = GestorHorarios.extraerHorarios(frecuencia, "horarioPaseo", getParameter);

        // Validar duración
        Integer duracionMinutos = null;
        if (duracionMinutosStr != null && !duracionMinutosStr.trim().isEmpty()) {
            duracionMinutos = Integer.parseInt(duracionMinutosStr);
        }

        // Construir días de semana
        String diasSemana = GestorHorarios.construirDiasSemana(diasSeleccionados);

        // Actualizar datos
        recordatorio.setHorarios(GestorHorarios.convertirHorariosAString(horarios));
        recordatorio.setDiasSemana(diasSemana);
        recordatorio.setDuracionMinutos(duracionMinutos);

        String descripcion = "Recordatorio de paseo: " + frecuencia + " vez/veces al día";
        if (duracionMinutos != null) {
            descripcion += " - " + duracionMinutos + " minutos";
        }
        recordatorio.setDescripcion(descripcion);

        return recordatorioDAO.actualizar(recordatorio);
    }

    public boolean eliminarRecordatorio(String idStr, Usuario usuario) {
        // Validar ID
        String error = ValidadorRecordatorio.validarIdRecordatorio(idStr);
        if (error != null) {
            throw new IllegalArgumentException(error);
        }

        Long recordatorioId = Long.parseLong(idStr);

        System.out.println("Buscando recordatorio de paseo con ID: " + recordatorioId);
        RecordatorioPaseo recordatorio = recordatorioDAO.obtenerPorId(recordatorioId);

        // Validar permisos
        validarPermisos(recordatorio, usuario,
                r -> ((RecordatorioPaseo) r).getMascota().getUsuario());

        System.out.println("Intentando eliminar recordatorio de paseo...");
        boolean eliminado = recordatorioDAO.eliminar(recordatorioId);

        if (eliminado) {
            System.out.println("✓ Recordatorio de paseo eliminado exitosamente");
        } else {
            System.err.println("✗ Error al eliminar el recordatorio de paseo - el DAO retornó false");
        }

        return eliminado;
    }

    public boolean desactivarRecordatorio(Long recordatorioId, Usuario usuario) {
        RecordatorioPaseo recordatorio = recordatorioDAO.obtenerPorId(recordatorioId);

        // Validar permisos
        validarPermisos(recordatorio, usuario,
                r -> ((RecordatorioPaseo) r).getMascota().getUsuario());

        return recordatorioDAO.desactivarRecordatorio(recordatorioId);
    }

    public List<RecordatorioPaseo> obtenerRecordatoriosPorMascota(Long mascotaId) {
        return recordatorioDAO.obtenerRecordatoriosPaseoPorMascota(mascotaId);
    }
}