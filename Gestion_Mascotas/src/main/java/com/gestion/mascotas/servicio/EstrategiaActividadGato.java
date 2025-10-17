package com.gestion.mascotas.servicio;

import com.gestion.mascotas.util.EstrategiaActividad;

public class EstrategiaActividadGato implements EstrategiaActividad {
    @Override
    public String calcularNivelActividad(Integer edad) {
        if (edad < 10) return "MEDIA";
        return "BAJA";
    }
}
