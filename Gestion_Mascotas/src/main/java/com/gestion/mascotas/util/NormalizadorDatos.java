package com.gestion.mascotas.util;
public class NormalizadorDatos {

    public static String normalizarNombre(String nombre) {
        return (nombre != null) ? nombre.trim() : null;
    }

    public static String normalizarEmail(String email) {
        return (email != null) ? email.trim().toLowerCase() : null;
    }

    public static String normalizarTelefono(String telefono) {
        return (telefono != null && !telefono.trim().isEmpty()) ? telefono.trim() : null;
    }
}
