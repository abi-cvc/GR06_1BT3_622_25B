package com.gestion.mascotas.servicio;

import com.gestion.mascotas.dao.RecordatorioAlimentacionDAO;
import com.gestion.mascotas.modelo.entidades.Mascota;
import com.gestion.mascotas.modelo.entidades.RecordatorioAlimentacion;
import com.gestion.mascotas.modelo.entidades.Usuario;
import com.gestion.mascotas.util.ValidadorRecordatorio;
import com.gestion.mascotas.util.GestorHorarios;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

public class RecordatorioAlimentacionService extends RecordatorioService {

    private RecordatorioAlimentacionDAO recordatorioDAO;

    public RecordatorioAlimentacionService() {
        super();
        this.recordatorioDAO = new RecordatorioAlimentacionDAO();
    }

    public boolean configurarRecordatorio(String mascotaIdStr, String frecuencia,
                                          String tipoAlimento, String[] diasSeleccionados,
                                          java.util.function.Function<String, String> getParameter,
                                          Usuario usuario) {
        // Validar frecuencia
        String error = ValidadorRecordatorio.validarFrecuencia(frecuencia);
        if (error != null) {
            throw new IllegalArgumentException(error);
        }

        // Validar y obtener mascota
        Mascota mascota = validarYObtenerMascota(mascotaIdStr, usuario);

        // Obtener horarios
        int numHorarios = Integer.parseInt(frecuencia);
        List<LocalTime> horarios = GestorHorarios.extraerHorarios(numHorarios, "horario", getParameter);

        // Validar horarios
        error = GestorHorarios.validarHorariosNoVacios(horarios);
        if (error != null) {
            throw new IllegalArgumentException(error);
        }

        // Validar tipo de alimento
        error = ValidadorRecordatorio.validarTipoAlimento(tipoAlimento);
        if (error != null) {
            throw new IllegalArgumentException(error);
        }

        // Construir días de semana
        String diasSemana = GestorHorarios.construirDiasSemana(diasSeleccionados);

        // Crear recordatorio
        RecordatorioAlimentacion recordatorio = new RecordatorioAlimentacion();
        recordatorio.setMascota(mascota);
        recordatorio.setActivo(true);
        recordatorio.setDescripcion("Recordatorio de alimentación: " +
                frecuencia + " vez/veces al día - " + tipoAlimento);
        recordatorio.setFrecuencia(frecuencia);
        recordatorio.setHorarios(GestorHorarios.convertirHorariosAString(horarios));
        recordatorio.setTipoAlimento(tipoAlimento.trim());
        recordatorio.setDiasSemana(diasSemana);
        recordatorio.setFechaHoraRecordatorio(LocalDateTime.now());

        System.out.println("Guardando recordatorio: " + recordatorio);

        return recordatorioDAO.guardar(recordatorio);
    }

    public boolean modificarRecordatorio(Long recordatorioId, String frecuencia,
                                         String tipoAlimento, String[] diasSeleccionados,
                                         java.util.function.Function<String, String> getParameter,
                                         Usuario usuario) {
        RecordatorioAlimentacion recordatorio = recordatorioDAO.obtenerPorId(recordatorioId);

        // Validar permisos
        validarPermisos(recordatorio, usuario,
                r -> ((RecordatorioAlimentacion) r).getMascota().getUsuario());

        // Obtener horarios
        int numHorarios = Integer.parseInt(frecuencia);
        List<LocalTime> horarios = GestorHorarios.extraerHorarios(numHorarios, "horario", getParameter);

        // Construir días de semana
        String diasSemana = GestorHorarios.construirDiasSemana(diasSeleccionados);

        // Actualizar datos
        recordatorio.setFrecuencia(frecuencia);
        recordatorio.setHorarios(GestorHorarios.convertirHorariosAString(horarios));
        recordatorio.setTipoAlimento(tipoAlimento.trim());
        recordatorio.setDiasSemana(diasSemana);
        recordatorio.setDescripcion("Recordatorio de alimentación: " +
                frecuencia + " vez/veces al día - " + tipoAlimento);

        return recordatorioDAO.actualizar(recordatorio);
    }

    public boolean eliminarRecordatorio(String idStr, Usuario usuario) {
        // Validar ID
        String error = ValidadorRecordatorio.validarIdRecordatorio(idStr);
        if (error != null) {
            throw new IllegalArgumentException(error);
        }

        Long recordatorioId = Long.parseLong(idStr);

        System.out.println("Buscando recordatorio con ID: " + recordatorioId);
        RecordatorioAlimentacion recordatorio = recordatorioDAO.obtenerPorId(recordatorioId);

        // Validar permisos
        validarPermisos(recordatorio, usuario,
                r -> ((RecordatorioAlimentacion) r).getMascota().getUsuario());

        System.out.println("Intentando eliminar recordatorio...");
        boolean eliminado = recordatorioDAO.eliminar(recordatorioId);

        if (eliminado) {
            System.out.println("✓ Recordatorio eliminado exitosamente");
        } else {
            System.err.println("✗ Error al eliminar el recordatorio - el DAO retornó false");
        }

        return eliminado;
    }

    public boolean desactivarRecordatorio(Long recordatorioId, Usuario usuario) {
        RecordatorioAlimentacion recordatorio = recordatorioDAO.obtenerPorId(recordatorioId);

        // Validar permisos
        validarPermisos(recordatorio, usuario,
                r -> ((RecordatorioAlimentacion) r).getMascota().getUsuario());

        return recordatorioDAO.desactivarRecordatorio(recordatorioId);
    }

    public List<RecordatorioAlimentacion> obtenerRecordatoriosPorMascota(Long mascotaId) {
        return recordatorioDAO.obtenerRecordatoriosAlimentacionPorMascota(mascotaId);
    }
}