package com.gestion.mascotas.servicio;

import com.gestion.mascotas.dao.UsuarioDAO; // Aún necesitamos importar
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test; // Importar @Test


import static org.junit.jupiter.api.Assertions.*;

class UsuarioServiceTest {

    private UsuarioService usuarioService;
    protected UsuarioDAO usuarioDAO;

    // Datos válidos por defecto
    private final String nombreValido = "Nombre Apellido";
    private final String emailValido = "valido@test.com";
    private final String telefonoValido = "0987654321";
    private final String contrasenaValida = "Passw123";
    private final String confirmarContrasenaValida = "Passw123";
    // Usaremos un nombre de usuario base y lo haremos único si es necesario
    private final String nombreUsuarioBase = "usuarioValido";

    @BeforeEach
    void setUp() {
        usuarioDAO = new UsuarioDAO();
        usuarioService = new UsuarioService();
    }

    // --- Tests Individuales para Nombre de Usuario Inválido ---

    @Test
    @DisplayName("Crear Perfil con Nombre de Usuario Muy Corto")
    void crearPerfil_conNombreUsuarioMuyCorto_debeLanzarExcepcion() {
        String nombreUsuarioInvalido = "us"; // Muy corto
        String errorEsperado = "inválido (3-50";

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            usuarioService.crearPerfil(nombreUsuarioInvalido, nombreValido, emailValido, telefonoValido,
                    contrasenaValida, confirmarContrasenaValida);
        });
        assertTrue(exception.getMessage().toLowerCase().contains(errorEsperado.toLowerCase()));
        System.out.println("Test 1: Nombre de usuario muy corto");
    }

    @Test
    @DisplayName("Crear Perfil con Nombre de Usuario con Espacios")
    void crearPerfil_conNombreUsuarioConEspacios_debeLanzarExcepcion() {
        String nombreUsuarioInvalido = "user con espacio"; // Con espacios
        String errorEsperado = "inválido (3-50";

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            usuarioService.crearPerfil(nombreUsuarioInvalido, nombreValido, emailValido, telefonoValido,
                    contrasenaValida, confirmarContrasenaValida);
        });
        assertTrue(exception.getMessage().toLowerCase().contains(errorEsperado.toLowerCase()));
        System.out.println("Test 2: Nombre de usuario con espacios");
    }

    @Test
    @DisplayName("Crear Perfil con Nombre de Usuario con Caracteres Inválidos")
    void crearPerfil_conNombreUsuarioCaracteresInvalidos_debeLanzarExcepcion() {
        String nombreUsuarioInvalido = "user!@#"; // Caracteres inválidos
        String errorEsperado = "inválido (3-50";

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            usuarioService.crearPerfil(nombreUsuarioInvalido, nombreValido, emailValido, telefonoValido,
                    contrasenaValida, confirmarContrasenaValida);
        });
        assertTrue(exception.getMessage().toLowerCase().contains(errorEsperado.toLowerCase()));
        System.out.println("Test 3: Nombre de usuario con caracteres inválidos");
    }

    // --- Tests Individuales para Contraseñas Inválidas ---

    @Test
    @DisplayName("Crear Perfil con Contraseña Muy Corta")
    void crearPerfil_conContrasenaMuyCorta_debeLanzarExcepcion() {
        String contrasenaInvalida = "12345"; // Muy corta
        String errorEsperado = "al menos 6 caracteres";
        String nombreUsuarioUnico = nombreUsuarioBase + System.currentTimeMillis();

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            usuarioService.crearPerfil(nombreUsuarioUnico, nombreValido, emailValido, telefonoValido,
                    contrasenaInvalida, contrasenaInvalida); // Pasa la misma en confirmación
        });
        assertTrue(exception.getMessage().toLowerCase().contains(errorEsperado.toLowerCase()));
        System.out.println("Test 4: Contraseña muy corta");
    }

    @Test
    @DisplayName("Crear Perfil con Contraseñas que No Coinciden")
    void crearPerfil_conContrasenasNoCoinciden_debeLanzarExcepcion() {
        String contrasena = "Passw123";
        String confirmacionDiferente = "Passw1234"; // No coincide
        String errorEsperado = "contraseñas no coinciden";
        String nombreUsuarioUnico = nombreUsuarioBase + System.currentTimeMillis();

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            usuarioService.crearPerfil(nombreUsuarioUnico, nombreValido, emailValido, telefonoValido,
                    contrasena, confirmacionDiferente);
        });
        assertTrue(exception.getMessage().toLowerCase().contains(errorEsperado.toLowerCase()));
        System.out.println("Test 5: Contraseñas no coinciden");
    }

    @Test
    @DisplayName("Crear Perfil con Contraseña Débil (Solo Números)")
    void crearPerfil_conContrasenaDebilNumeros_debeLanzarExcepcion() {
        String contrasenaDebil = "12345678"; // Solo números
        String errorEsperado = "débil";
        String nombreUsuarioUnico = nombreUsuarioBase + System.currentTimeMillis();

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            usuarioService.crearPerfil(nombreUsuarioUnico, nombreValido, emailValido, telefonoValido,
                    contrasenaDebil, contrasenaDebil);
        });
        assertTrue(exception.getMessage().toLowerCase().contains(errorEsperado.toLowerCase()));
        System.out.println("Test 6: Contraseña débil (solo números)");
    }

    @Test
    @DisplayName("Crear Perfil con Contraseña Débil (Solo Minúsculas)")
    void crearPerfil_conContrasenaDebilMinusculas_debeLanzarExcepcion() {
        String contrasenaDebil = "password"; // Solo minúsculas
        String errorEsperado = "débil";
        String nombreUsuarioUnico = nombreUsuarioBase + System.currentTimeMillis();

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            usuarioService.crearPerfil(nombreUsuarioUnico, nombreValido, emailValido, telefonoValido,
                    contrasenaDebil, contrasenaDebil);
        });
        assertTrue(exception.getMessage().toLowerCase().contains(errorEsperado.toLowerCase()));
        System.out.println("Test 7: Contraseña débil (solo minúsculas)");
    }

    // --- Test para datos válidos ---
    @Test
    @DisplayName("Crear Perfil con Datos Válidos - No debe lanzar excepción (validación)")
    void crearPerfil_conDatosValidos_noDebeLanzarExcepcionDeValidacion() {
        String nombreUsuarioUnico = "validUser" + System.currentTimeMillis();
        String emailUnico = "valid" + System.currentTimeMillis() + "@test.com";

        assertDoesNotThrow(() -> {
             usuarioService.crearPerfil(nombreUsuarioUnico, nombreValido, emailUnico, telefonoValido,
                                        contrasenaValida, confirmarContrasenaValida);
        }, "No debería lanzar excepción de validación para datos correctos.");
        System.out.println("Test 8: Datos válidos (solo validación)");
    }
}