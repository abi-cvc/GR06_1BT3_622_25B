package com.gestion.mascotas.util;

public class ValidadorPeso {

    public String validarPeso(double peso) {
        if (peso <= 0) {
            return "El peso debe ser un valor mayor que cero.";
        }
        if (peso > 250) {
            return "El peso no puede exceder los 250 kg.";
        }
        return null;
    }

}

