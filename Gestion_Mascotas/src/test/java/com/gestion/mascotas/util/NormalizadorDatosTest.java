package com.gestion.mascotas.util;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

class NormalizadorDatosTest {

    @BeforeEach
    void setUp() {
        System.out.println("Starting NormalizadorDatos test...");
    }

    @AfterEach
    void tearDown() {
        System.out.println("Test finished.");
    }

    @Test
    void given_name_with_spaces_when_normalize_then_trimmed() {
        String name = "  Javier  ";
        String result = NormalizadorDatos.normalizarNombre(name);
        assertEquals("Javier", result);
    }

    @Test
    void given_null_name_when_normalize_then_null() {
        String name = null;
        String result = NormalizadorDatos.normalizarNombre(name);
        assertNull(result);
    }

    @Test
    void given_email_with_spaces_and_uppercase_when_normalize_then_trimmed_and_lowercase() {
        String email = "  EjEmPlO@Correo.COM  ";
        String result = NormalizadorDatos.normalizarEmail(email);
        assertEquals("ejemplo@correo.com", result);
    }

    @Test
    void given_null_email_when_normalize_then_null() {
        String email = null;
        String result = NormalizadorDatos.normalizarEmail(email);
        assertNull(result);
    }

    @Test
    void given_phone_with_spaces_when_normalize_then_trimmed() {
        String phone = "  0987654321  ";
        String result = NormalizadorDatos.normalizarTelefono(phone);
        assertEquals("0987654321", result);
    }

    @Test
    void given_empty_phone_when_normalize_then_null() {
        String phone = "   ";
        String result = NormalizadorDatos.normalizarTelefono(phone);
        assertNull(result);
    }

    @Test
    void given_null_phone_when_normalize_then_null() {
        String phone = null;
        String result = NormalizadorDatos.normalizarTelefono(phone);
        assertNull(result);
    }
}
