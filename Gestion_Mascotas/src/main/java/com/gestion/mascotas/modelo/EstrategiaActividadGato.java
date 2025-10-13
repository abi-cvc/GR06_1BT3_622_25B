package com.gestion.mascotas.modelo;

public class EstrategiaActividadGato implements EstrategiaActividad {
    @Override
    public String calcularNivelActividad(Integer edad) {
        if (edad < 10) return "MEDIA";
        return "BAJA";
    }
}
