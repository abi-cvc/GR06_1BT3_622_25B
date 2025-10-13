package com.gestion.mascotas.modelo;

public class EstrategiaActividadPerro implements EstrategiaActividad {
    @Override
    public String calcularNivelActividad(Integer edad) {
        if (edad == null) return "MEDIA";
        if (edad < 2) return "MEDIA";
        if (edad < 8) return "ALTA";
        return "BAJA";
    }
}