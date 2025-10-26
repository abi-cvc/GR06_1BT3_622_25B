package com.gestion.mascotas.servicio;

import com.gestion.mascotas.dao.UsuarioDAO;
import com.gestion.mascotas.modelo.entidades.Usuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class) // 1. Habilitar Mockito
class UsuarioServiceTest {

    @Mock // 2. Crear un mock del DAO
    private UsuarioDAO usuarioDAO;

    private UsuarioService usuarioService; // 3. Instancia del servicio

    // Datos válidos por defecto
    private final String nombreValido = "Nombre Apellido";
    private final String emailValido = "valido@test.com";
    private final String telefonoValido = "0987654321";
    private final String contrasenaValida = "Passw123";
    private final String confirmarContrasenaValida = "Passw123";
    private final String nombreUsuarioBase = "usuarioValido";

    @BeforeEach
    void setUp() throws Exception {
        // 4. Instanciar el servicio
        usuarioService = new UsuarioService();

        // 5. Inyectar el mock en el servicio usando reflexión
        // Esto reemplaza el "new UsuarioDAO()" dentro del servicio
        Field daoField = UsuarioService.class.getDeclaredField("usuarioDAO");
        daoField.setAccessible(true);
        daoField.set(usuarioService, usuarioDAO);
    }

    // --- Tests de validación (no necesitan mocks) ---

    @Test
    @DisplayName("Crear Perfil con Contraseña Débil (solo minúsculas)")
    void crearPerfil_conContrasenaDebilMinusculas_debeLanzarExcepcion() {
        String contrasenaDebil = "password";
        String errorEsperado = "Tu contraseña es muy débil. Intenta incluir mayúsculas, minúsculas, números o caracteres especiales.";

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            usuarioService.crearPerfil(nombreUsuarioBase, nombreValido, emailValido, telefonoValido,
                    contrasenaDebil, contrasenaDebil);
        });
        assertTrue(exception.getMessage().contains(errorEsperado));
    }

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
        String nombreUsuarioInvalido = "user con espacio";
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
        String nombreUsuarioInvalido = "user!@#";
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
                    contrasenaInvalida, contrasenaInvalida);
        });
        assertTrue(exception.getMessage().toLowerCase().contains(errorEsperado.toLowerCase()));
        System.out.println("Test 4: Contraseña muy corta");
    }

    @Test
    @DisplayName("Crear Perfil con Contraseñas que No Coinciden")
    void crearPerfil_conContrasenasNoCoinciden_debeLanzarExcepcion() {
        String contrasena = "Passw123";
        String confirmacionDiferente = "Passw1234";
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
        String contrasenaDebil = "12345678";
        String errorEsperado = "débil";
        String nombreUsuarioUnico = nombreUsuarioBase + System.currentTimeMillis();

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            usuarioService.crearPerfil(nombreUsuarioUnico, nombreValido, emailValido, telefonoValido,
                    contrasenaDebil, contrasenaDebil);
        });
        assertTrue(exception.getMessage().toLowerCase().contains(errorEsperado.toLowerCase()));
        System.out.println("Test 6: Contraseña débil (solo números)");
    }


    // --- Test de lógica de negocio (necesita mocks) ---

    @Test
    @DisplayName("Crear Perfil con Datos Válidos - Llama al DAO para guardar")
    void crearPerfil_conDatosValidos_noDebeLanzarExcepcionDeValidacion() {
        String nombreUsuarioUnico = "validUser" + System.currentTimeMillis();
        String emailUnico = "valid" + System.currentTimeMillis() + "@test.com";

        // Arrange: Simular que el usuario y el email no existen
        when(usuarioDAO.buscarPorNombreUsuario(nombreUsuarioUnico.toLowerCase())).thenReturn(null);
        when(usuarioDAO.buscarPorEmail(emailUnico)).thenReturn(null);

        // Act
        assertDoesNotThrow(() -> {
            usuarioService.crearPerfil(nombreUsuarioUnico, nombreValido, emailUnico, telefonoValido,
                    contrasenaValida, confirmarContrasenaValida);
        }, "No debería lanzar excepción de validación para datos correctos.");

        // Assert: Verificar que el método crearUsuario del DAO fue llamado
        ArgumentCaptor<Usuario> usuarioCaptor = ArgumentCaptor.forClass(Usuario.class);
        verify(usuarioDAO).crearUsuario(usuarioCaptor.capture());

        // Verificar que los datos guardados son correctos
        Usuario usuarioGuardado = usuarioCaptor.getValue();
        assertEquals(nombreUsuarioUnico.toLowerCase(), usuarioGuardado.getNombreUsuario());
        assertEquals(emailUnico, usuarioGuardado.getEmail());
        assertEquals(nombreValido, usuarioGuardado.getNombre());
    }

    @Test
    @DisplayName("Crear Perfil con Nombre de Usuario duplicado - Lanza excepción")
    void crearPerfil_conNombreUsuarioDuplicado_lanzaExcepcion() {
        String nombreUsuarioDuplicado = "usuarioExistente";

        // Arrange: Simular que el DAO encuentra un usuario
        when(usuarioDAO.buscarPorNombreUsuario(nombreUsuarioDuplicado.toLowerCase())).thenReturn(new Usuario()); // <-- CORRECTO: Usa minúsculas

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            usuarioService.crearPerfil(nombreUsuarioDuplicado, nombreValido, emailValido, telefonoValido,
                    contrasenaValida, confirmarContrasenaValida);
        });

        assertTrue(exception.getMessage().contains("El nombre de usuario ya está en uso"));
        verify(usuarioDAO, never()).crearUsuario(any());
    }

    @Test
    @DisplayName("Crear Perfil con Email duplicado - Lanza excepción")
    void crearPerfil_conEmailDuplicado_lanzaExcepcion() {
        String emailExistente = "existente@test.com";

        // Arrange: Simular que el nombre de usuario es único
        when(usuarioDAO.buscarPorNombreUsuario(nombreUsuarioBase.toLowerCase())).thenReturn(null);
        when(usuarioDAO.buscarPorEmail(emailExistente)).thenReturn(new Usuario());

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            usuarioService.crearPerfil(nombreUsuarioBase, nombreValido, emailExistente, telefonoValido,
                    contrasenaValida, confirmarContrasenaValida);
        });

        assertTrue(exception.getMessage().contains("El correo electrónico ya está registrado."));
        verify(usuarioDAO, never()).crearUsuario(any()); // No se debe guardar nada
    }
}