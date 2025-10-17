package com.gestion.mascotas.util;

import com.gestion.mascotas.modelo.entidades.Mascota;
import com.gestion.mascotas.modelo.enums.TipoMascota;

/**
 * Refactorización #9: Replace Method with Method Object
 * Clase dedicada a validar si una mascota requiere atención especial
 * Encapsula toda la lógica de validación para una mejor cohesión.
 */
public class ValidadorAtencionEspecial {

    private static final int EDAD_MINIMA_CACHORRO = 1;
    private static final int EDAD_MAXIMA_ADULTO = 10;
    private static final double PESO_MINIMO_GENERAL = 2.0;
    private static final double PESO_MAXIMO_PERRO = 50.0;
    private static final double PESO_MAXIMO_GATO = 10.0;

    private final Mascota mascota;

    public ValidadorAtencionEspecial(Mascota mascota) {
        this.mascota = mascota;
    }

    /**
     * Evalúa si la mascota necesita atención especial basándose en edad y peso
     * @return true si requiere atención especial, false en caso contrario
     */
    public boolean necesitaAtencion() {
        return necesitaAtencionPorEdad() || necesitaAtencionPorPeso();
    }

    /**
     * Verifica si la edad de la mascota está fuera del rango normal
     */
    private boolean necesitaAtencionPorEdad() {
        if (mascota.getEdad() == null) {
            return false;
        }
        return mascota.getEdad() < EDAD_MINIMA_CACHORRO ||
                mascota.getEdad() > EDAD_MAXIMA_ADULTO;
    }

    /**
     * Verifica si el peso de la mascota está fuera del rango saludable según su tipo
     */
    private boolean necesitaAtencionPorPeso() {
        if (mascota.getPeso() == null) {
            return false;
        }

        if (mascota.getTipo() == TipoMascota.PERRO) {
            return esPesoExtremoPerro();
        } else if (mascota.getTipo() == TipoMascota.GATO) {
            return esPesoExtremoGato();
        }

        return false;
    }

    /**
     * Evalúa si el peso del perro está fuera del rango saludable (2-50 kg)
     */
    private boolean esPesoExtremoPerro() {
        return mascota.getPeso() < PESO_MINIMO_GENERAL ||
                mascota.getPeso() > PESO_MAXIMO_PERRO;
    }

    /**
     * Evalúa si el peso del gato está fuera del rango saludable (2-10 kg)
     */
    private boolean esPesoExtremoGato() {
        return mascota.getPeso() < PESO_MINIMO_GENERAL ||
                mascota.getPeso() > PESO_MAXIMO_GATO;
    }
}