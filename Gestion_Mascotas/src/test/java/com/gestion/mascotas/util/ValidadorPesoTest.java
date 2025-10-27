package com.gestion.mascotas.util;

import org.junit.Test;
import static org.junit.Assert.*;

public class ValidadorPesoTest {

    @Test
    public void given_valid_positive_weight_when_validate_then_null() {
        ValidadorPeso validador = new ValidadorPeso();
        String resultado = validador.validarPeso(25.5);
        assertNull(resultado);
    }

    @Test
    public void given_zero_weight_when_validate_then_error_message() {
        ValidadorPeso validador = new ValidadorPeso();
        String resultado = validador.validarPeso(0.0);
        assertEquals("El peso debe ser un valor mayor que cero.", resultado);
    }

    @Test
    public void given_negative_weight_when_validate_then_error_message() {
        ValidadorPeso validador = new ValidadorPeso();
        String resultado = validador.validarPeso(-5.0);
        assertEquals("El peso debe ser un valor mayor que cero.", resultado);
    }

    @Test
    public void given_max_weight_when_validate_then_null() {
        ValidadorPeso validador = new ValidadorPeso();
        assertNull(validador.validarPeso(250));
    }
}