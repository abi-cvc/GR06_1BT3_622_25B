package com.gestion.mascotas.modelo.enums;

import com.gestion.mascotas.util.EstrategiaActividad;
import com.gestion.mascotas.servicio.EstrategiaActividadGato;
import com.gestion.mascotas.servicio.EstrategiaActividadPerro;

public enum TipoMascota {
    PERRO(new EstrategiaActividadPerro()),
    GATO(new EstrategiaActividadGato());

    private final EstrategiaActividad estrategiaActividad;

    TipoMascota(EstrategiaActividad estrategiaActividad) {
        this.estrategiaActividad = estrategiaActividad;
    }

    public String calcularNivelActividad(Integer edad) {
        return estrategiaActividad.calcularNivelActividad(edad);
    }
}
