package com.gestion.mascotas.servicio;

import com.gestion.mascotas.util.EstrategiaActividad;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

class EstrategiaActividadGatoTest {
    private EstrategiaActividad estrategia;
    @BeforeEach
    void setUp() {estrategia = new EstrategiaActividadGato();}

    @AfterEach
    void tearDown() {estrategia = null;}

    @Test
    void given_age_less_than_10_when_calc_then_return_MEDIA() {
        String result = estrategia.calcularNivelActividad(5);
        assertEquals("MEDIA", result);
    }

    @Test
    void given_age_equal_10_when_calc_then_return_BAJA() {
        String result = estrategia.calcularNivelActividad(10);
        assertEquals("BAJA", result);
    }

    @Test
    void given_age_greater_than_10_when_calc_then_return_BAJA() {
        String result = estrategia.calcularNivelActividad(15);
        assertEquals("BAJA", result);
    }
}
