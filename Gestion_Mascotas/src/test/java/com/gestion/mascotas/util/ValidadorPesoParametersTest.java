package com.gestion.mascotas.util;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.*;

@RunWith(value = Parameterized.class)
public class ValidadorPesoParametersTest {

    private double peso;
    private String expectedError;

    @Parameterized.Parameters
    public static Iterable<Object[]> parameters() {
        List<Object[]> objects = new ArrayList<>();
        objects.add(new Object[]{0.0, "El peso debe ser un valor mayor que cero."});
        objects.add(new Object[]{-5.0, "El peso debe ser un valor mayor que cero."});
        objects.add(new Object[]{-100.0, "El peso debe ser un valor mayor que cero."});
        objects.add(new Object[]{1.0, null}); // Válido
        objects.add(new Object[]{50.0, null}); // Válido
        objects.add(new Object[]{250.0, null}); // Válido
        objects.add(new Object[]{251.0, "El peso no puede exceder los 250 kg."});
        objects.add(new Object[]{300.0, "El peso no puede exceder los 250 kg."});
        return objects;
    }

    public ValidadorPesoParametersTest(double peso, String expectedError) {
        this.peso = peso;
        this.expectedError = expectedError;
    }

    @Test
    public void given_parameters_when_validate_then_correct_result() {
        ValidadorPeso validador = new ValidadorPeso();
        String resultado = validador.validarPeso(peso);
        assertEquals(expectedError, resultado);
    }
}


