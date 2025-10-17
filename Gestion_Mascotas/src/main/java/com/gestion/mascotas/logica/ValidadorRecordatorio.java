package com.gestion.mascotas.logica;

import com.gestion.mascotas.modelo.Mascota;
import com.gestion.mascotas.modelo.Usuario;

public class ValidadorRecordatorio {

    public static String validarMascotaId(String mascotaIdStr) {
        if (mascotaIdStr == null || mascotaIdStr.trim().isEmpty()) {
            return "mascota_id_invalido";
        }
        return null;
    }

    public static String validarFrecuencia(String frecuencia) {
        if (frecuencia == null || frecuencia.trim().isEmpty()) {
            return "frecuencia_vacia";
        }
        return null;
    }

    public static String validarMascotaExiste(Mascota mascota) {
        if (mascota == null) {
            return "mascota_no_encontrada";
        }
        return null;
    }

    public static String validarPerteneceUsuario(Mascota mascota, Usuario usuario) {
        if (!mascota.getUsuario().getId().equals(usuario.getId())) {
            return "acceso_denegado";
        }
        return null;
    }

    public static String validarTipoAlimento(String tipoAlimento) {
        if (tipoAlimento == null || tipoAlimento.trim().isEmpty()) {
            return "tipo_alimento_vacio";
        }
        return null;
    }

    public static String validarDuracion(Integer duracionMinutos) {
        if (duracionMinutos != null && duracionMinutos <= 0) {
            return "duracion_invalida";
        }
        return null;
    }

    public static String validarRecordatorioExiste(Object recordatorio) {
        if (recordatorio == null) {
            return "recordatorio_no_encontrado";
        }
        return null;
    }

    public static String validarIdRecordatorio(String idStr) {
        if (idStr == null || idStr.trim().isEmpty()) {
            return "id_recordatorio_invalido";
        }
        return null;
    }
}