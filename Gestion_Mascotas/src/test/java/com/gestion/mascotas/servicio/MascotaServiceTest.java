package com.gestion.mascotas.servicio;

import com.gestion.mascotas.dao.MascotaDAO;
import com.gestion.mascotas.dao.UsuarioDAO;
import com.gestion.mascotas.modelo.enums.TipoMascota;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class MascotaServiceTest {

    private MascotaService mascotaService;

    protected MascotaDAO mascotaDAO;
    protected UsuarioDAO usuarioDAO;

    @BeforeEach
    void setUp() {
        mascotaDAO = new MascotaDAO(); // O null
        usuarioDAO = new UsuarioDAO(); // O null

        mascotaService = new MascotaService();
    }

    // --- Test: Pruebas con Edades Inválidas ---
    static Stream<Arguments> proveedorEdadesInvalidas() {
        return Stream.of(
                Arguments.of(-1, "edad debe ser positiva"),
                Arguments.of(31, "edad excede el límite")
                // Null se prueba por separado con @NullSource si el método lo permite
        );
    }

    @ParameterizedTest(name = "Edad Inválida: {0}")
    @MethodSource("proveedorEdadesInvalidas")
    @NullSource
    @DisplayName("1. Validar Datos Mascota con Edad Inválida")
    void validarDatosMascotaDetallado_conEdadInvalida_debeRetornarError(Integer edadInvalida) {
        // Arrange: Datos válidos para los otros campos
        String nombreValido = "Firulais";
        TipoMascota tipoValido = TipoMascota.PERRO;
        String razaValida = "Mestizo";
        Double pesoValido = 10.5;
        String colorValido = "Negro";
        String errorEsperado = (edadInvalida == null) ? "edad es obligatoria" : "edad";

        String resultado = mascotaService.validarDatosMascotaDetallado(
                nombreValido, tipoValido, razaValida, edadInvalida, pesoValido, colorValido);

        assertNotNull(resultado, "Debería retornar un mensaje de error para edad: " + edadInvalida);
        assertTrue(resultado.toLowerCase().contains(errorEsperado.toLowerCase()),
                "El mensaje de error '" + resultado + "' no contiene '" + errorEsperado + "'");
    }

    // --- Test: Pruebas con Pesos Inválidos ---
    static Stream<Arguments> proveedorPesosInvalidos() {
        return Stream.of(
                Arguments.of(0.0, "peso debe ser mayor a cero"),
                Arguments.of(-5.5, "peso debe ser mayor a cero"),
                Arguments.of(151.0, "peso excede el límite")
                // Null se prueba por separado
        );
    }

    @ParameterizedTest(name = "Peso Inválido: {0}")
    @MethodSource("proveedorPesosInvalidos")
    @NullSource
    @DisplayName("2. Validar Datos Mascota con Peso Inválido")
    void validarDatosMascotaDetallado_conPesoInvalido_debeRetornarError(Double pesoInvalido) {
        String nombreValido = "Mishi";
        TipoMascota tipoValido = TipoMascota.GATO;
        String razaValida = "Siames";
        Integer edadValida = 5;
        String colorValido = "Blanco";
        String errorEsperado = (pesoInvalido == null) ? "peso es obligatorio" : "peso";

        String resultado = mascotaService.validarDatosMascotaDetallado(
                nombreValido, tipoValido, razaValida, edadValida, pesoInvalido, colorValido);

        assertNotNull(resultado, "Debería retornar un mensaje de error para peso: " + pesoInvalido);
        assertTrue(resultado.toLowerCase().contains(errorEsperado.toLowerCase()),
                "El mensaje de error '" + resultado + "' no contiene '" + errorEsperado + "'");
    }

    // --- Test: Datos válidos ---

    @Test
    @DisplayName("3. Validar Datos Mascota con Datos Válidos")
    void validarDatosMascotaDetallado_conDatosValidos_debeRetornarNull() {
        String nombreValido = "Buddy";
        TipoMascota tipoValido = TipoMascota.PERRO;
        String razaValida = "Golden";
        Integer edadValida = 3;
        Double pesoValido = 25.0;
        String colorValido = "Dorado";

        String resultado = mascotaService.validarDatosMascotaDetallado(
                nombreValido, tipoValido, razaValida, edadValida, pesoValido, colorValido);

        assertNull(resultado, "No debería retornar error para datos válidos.");
    }
}