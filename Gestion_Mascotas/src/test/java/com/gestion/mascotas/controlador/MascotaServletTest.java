package com.gestion.mascotas.controlador;

import com.gestion.mascotas.modelo.entidades.Mascota;
import com.gestion.mascotas.modelo.enums.TipoMascota;
import com.gestion.mascotas.modelo.entidades.Usuario;
import com.gestion.mascotas.servicio.MascotaService;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * Test unitario con MOCKS para MascotaServlet
 * Siguiendo TDD (Test-Driven Development)
 *
 * Mockea las dependencias externas:
 * - MascotaDAO: Acceso a base de datos
 * - UsuarioDAO: Acceso a base de datos
 * - HttpServletRequest: Request HTTP
 * - HttpServletResponse: Response HTTP
 * - HttpSession: Sesión del usuario
 *
 * Objetivo: Probar SOLO la lógica del servlet, no sus dependencias
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("Tests con Mocks - MascotaServlet")
class MascotaServletTest {

    @Mock
    private MascotaService mascotaService; // <-- Mock del servicio (perfecto)

    // @Mock
    // private UsuarioDAO usuarioDAO; // <-- Ya no es necesario, el servicio lo mockea

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private HttpSession session;

    @Mock
    private RequestDispatcher requestDispatcher;

    // --- CAMBIO 1: Quita @InjectMocks ---
    private MascotaServlet servlet; // <-- Solo decláralo

    private Usuario usuarioMock;
    private Mascota mascotaMock;

    // --- CAMBIO 2: Modifica setUp ---
    @BeforeEach
    void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);

        servlet = new MascotaServlet(mascotaService);

        // Configurar usuario mock
        usuarioMock = new Usuario();
        usuarioMock.setId(1L);
        usuarioMock.setNombreUsuario("testUser");
        usuarioMock.setNombre("Test User");
        usuarioMock.setEmail("test@example.com");

        // Configurar mascota mock
        mascotaMock = new Mascota();
        mascotaMock.setId(1L);
        mascotaMock.setNombre("Firulais");
        mascotaMock.setTipo(TipoMascota.PERRO);
        mascotaMock.setRaza("Labrador");
        mascotaMock.setEdad(3);
        mascotaMock.setPeso(25.0);
        mascotaMock.setColor("Dorado");
        mascotaMock.setUsuario(usuarioMock);
    }

    @Nested
    @DisplayName("Tests de Control de Acceso y Sesión")
    class ControlAccesoSesion {

        @Test
        @DisplayName("GET sin sesión - Redirige a login")
        void testGetSinSesion_RedireccionaLogin() throws ServletException, IOException {
            // Arrange: Simular que NO hay sesión
            when(request.getSession(false)).thenReturn(null);
            when(request.getContextPath()).thenReturn("/app");

            // Act
            servlet.doGet(request, response);

            // Assert: Debe redirigir a login
            verify(response).sendRedirect("/app/login");
            verify(mascotaService, never()).consultarDatos(anyLong());
        }

        @Test
        @DisplayName("GET con sesión sin usuario - Redirige a login")
        void testGetConSesionSinUsuario_RedireccionaLogin() throws ServletException, IOException {
            // Arrange: Sesión existe pero sin usuario
            when(request.getSession(false)).thenReturn(session);
            when(session.getAttribute("usuario")).thenReturn(null);
            when(request.getContextPath()).thenReturn("/app");

            // Act
            servlet.doGet(request, response);

            // Assert: Debe redirigir a login
            verify(response).sendRedirect("/app/login");
            verify(mascotaService, never()).consultarDatos(anyLong());
        }

        @Test
        @DisplayName("POST sin sesión - Redirige a login")
        void testPostSinSesion_RedireccionaLogin() throws ServletException, IOException {
            // Arrange
            when(request.getSession(false)).thenReturn(null);
            when(request.getContextPath()).thenReturn("/app");

            // Act
            servlet.doPost(request, response);

            // Assert
            verify(response).sendRedirect("/app/login");
            verify(mascotaService, never()).registrarMascota(anyString(),any(),anyString(),anyInt(),anyDouble(),anyString(),anyLong());
        }
    }

    @Nested
    @DisplayName("Tests de Listado de Mascotas")
    class ListadoMascotas {

        @Test
        @DisplayName("Listar mascotas del usuario - Retorna lista correcta")
        void testListarMascotas_RetornaListaCorrecta() throws ServletException, IOException {
            // Arrange: Configurar sesión válida
            when(request.getSession(false)).thenReturn(session);
            when(session.getAttribute("usuario")).thenReturn(usuarioMock);
            when(request.getParameter("action")).thenReturn("listar");

            // Crear lista de mascotas mock
            Mascota mascota2 = new Mascota();
            mascota2.setId(2L);
            mascota2.setNombre("Michi");
            mascota2.setTipo(TipoMascota.GATO);
            List<Mascota> mascotasMock = Arrays.asList(mascotaMock, mascota2);

            // Configurar DAO para retornar lista
            when(mascotaService.listarMascotasPorUsuario(1L)).thenReturn(mascotasMock); // <-- Llama al SERVICIO
            when(request.getRequestDispatcher("/jsp/listaMascotas.jsp")).thenReturn(requestDispatcher);

            // Act
            servlet.doGet(request, response);

            // Assert: Verificar que se llamó al SERVICIO correctamente
            verify(mascotaService).listarMascotasPorUsuario(1L); // <-- Verifica el SERVICIO
            verify(request).setAttribute("mascotas", mascotasMock);
            verify(requestDispatcher).forward(request, response);

            // Verificar que se configuraron los atributos correctos
            verify(request).setAttribute("mascotas", mascotasMock);
            verify(request).setAttribute("tiposMascota", TipoMascota.values());

            // Verificar que se hizo forward al JSP correcto
            verify(requestDispatcher).forward(request, response);
        }

        @Test
        @DisplayName("Listar mascotas sin action - Default a listar")
        void testListarMascotasSinAction_DefaultListar() throws ServletException, IOException {
            // Arrange
            when(request.getSession(false)).thenReturn(session);
            when(session.getAttribute("usuario")).thenReturn(usuarioMock);
            when(request.getParameter("action")).thenReturn(null); // Sin action

            when(mascotaService.listarMascotasPorUsuario(1L)).thenReturn(Arrays.asList(mascotaMock));
            when(request.getRequestDispatcher("/jsp/listaMascotas.jsp")).thenReturn(requestDispatcher);

            // Act
            servlet.doGet(request, response);

            // Assert: CORREGIDO: Verifica el método de listado
            verify(mascotaService).listarMascotasPorUsuario(1L);
            verify(request).setAttribute(eq("mascotas"), anyList());
            verify(requestDispatcher).forward(request, response);
        }
    }

    @Nested
    @DisplayName("Tests de Eliminación de Mascotas")
    class EliminacionMascotas {

        @Test
        @DisplayName("Eliminar mascota propia - Elimina correctamente")
        void testEliminarMascotaPropia_EliminaCorrectamente() throws ServletException, IOException {
            // Arrange
            when(request.getSession(false)).thenReturn(session);
            when(session.getAttribute("usuario")).thenReturn(usuarioMock);
            when(request.getParameter("action")).thenReturn("eliminar");
            when(request.getParameter("id")).thenReturn("1");
            when(request.getContextPath()).thenReturn("/app");

            // Act
            servlet.doGet(request, response);

            // Assert: Verificar que se eliminó la mascota
            verify(mascotaService).eliminarMascota(1L, usuarioMock);
            verify(response).sendRedirect("/app/dashboard?success=mascota_eliminada");
        }

        @Test
        @DisplayName("Eliminar mascota inexistente - Retorna error")
        void testEliminarMascotaInexistente_RetornaError() throws ServletException, IOException {
            // Arrange
            when(request.getSession(false)).thenReturn(session);
            when(request.getSession()).thenReturn(session); // <-- AÑADE ESTO para arreglar el NPE
            when(session.getAttribute("usuario")).thenReturn(usuarioMock);
            when(request.getParameter("action")).thenReturn("eliminar");
            when(request.getParameter("id")).thenReturn("999");
            when(request.getContextPath()).thenReturn("/app");

            // Simula que el SERVICIO lanza la excepción que el servlet debe atrapar
            doThrow(new IllegalArgumentException("Mascota no encontrada"))
                    .when(mascotaService).eliminarMascota(999L, usuarioMock);

            // Act
            servlet.doGet(request, response);

            // Assert: Verificar que el servicio fue llamado
            verify(mascotaService).eliminarMascota(999L, usuarioMock);
            // Verificar que el servlet manejó la excepción y la puso en sesión
            verify(session).setAttribute("error", "Mascota no encontrada");
            verify(response).sendRedirect("/app/dashboard");
        }

        @Test
        @DisplayName("Eliminar mascota de otro usuario - Acceso denegado")
        void testEliminarMascotaOtroUsuario_AccesoDenegado() throws ServletException, IOException {
            // Arrange: Configurar otro usuario
            Usuario otroUsuario = new Usuario();
            otroUsuario.setId(2L);
            otroUsuario.setNombreUsuario("otroUser");

            mascotaMock.setUsuario(otroUsuario); // Mascota pertenece a otro usuario

            when(request.getSession(false)).thenReturn(session);
            when(session.getAttribute("usuario")).thenReturn(usuarioMock); // Usuario actual es ID 1
            when(request.getParameter("action")).thenReturn("eliminar");
            when(request.getParameter("id")).thenReturn("1");
            when(request.getContextPath()).thenReturn("/app");
            when(request.getSession()).thenReturn(session);

            // Simula que el SERVICIO lanza la excepción de seguridad
            doThrow(new SecurityException("acceso_denegado"))
                    .when(mascotaService).eliminarMascota(1L, usuarioMock);

            // Act
            servlet.doGet(request, response);

// Assert: Verificar que el servlet intentó eliminar
            verify(mascotaService).eliminarMascota(1L, usuarioMock); // <-- SÍ se llamó

// Verificar que el servlet manejó la excepción y redirigió
            verify(session).setAttribute("error", "acceso_denegado");
            verify(response).sendRedirect("/app/dashboard");
        }
    }

    @Nested
    @DisplayName("Tests de Registro de Mascotas")
    class RegistroMascotas {

        @Test
        @DisplayName("Registrar mascota con datos válidos - Guarda correctamente")
        void testRegistrarMascotaValida_GuardaCorrectamente() throws ServletException, IOException {
            // Arrange: Configurar request con datos válidos
            when(request.getSession(false)).thenReturn(session);
            when(session.getAttribute("usuario")).thenReturn(usuarioMock);
            when(request.getParameter("action")).thenReturn("registrar");
            when(request.getParameter("nombre")).thenReturn("Rex");
            when(request.getParameter("tipo")).thenReturn("PERRO");
            when(request.getParameter("raza")).thenReturn("Pastor Alemán");
            when(request.getParameter("edad")).thenReturn("2");
            when(request.getParameter("peso")).thenReturn("30.5");
            when(request.getParameter("color")).thenReturn("Negro");
            when(request.getParameter("usuarioId")).thenReturn("1");
            when(request.getContextPath()).thenReturn("/app");

            // Act
            servlet.doPost(request, response);

            // Assert: Verificar que el servicio fue llamado con los datos correctos
            verify(mascotaService).registrarMascota(
                    eq("Rex"),
                    eq(TipoMascota.PERRO),
                    eq("Pastor Alemán"),
                    eq(2),
                    eq(30.5),
                    eq("Negro"),
                    eq(1L)
            );

            // Verificar que se redirigió correctamente
            verify(response).sendRedirect("/app/mascota?action=listar&success=registrado");
        }

        @Test
        @DisplayName("Registrar mascota con datos inválidos - Retorna error")
        void testRegistrarMascotaDatosInvalidos_RetornaError() throws ServletException, IOException {
            // Arrange
            when(request.getSession(false)).thenReturn(session);
            when(session.getAttribute("usuario")).thenReturn(usuarioMock);
            when(request.getParameter("action")).thenReturn("registrar");
            // Parámetros de la request
            when(request.getParameter("nombre")).thenReturn("Rex");
            when(request.getParameter("tipo")).thenReturn("PERRO");
            when(request.getParameter("raza")).thenReturn("Pastor Alemán");
            when(request.getParameter("edad")).thenReturn("2");
            when(request.getParameter("peso")).thenReturn("30.5");
            when(request.getParameter("color")).thenReturn("Negro");
            when(request.getParameter("usuarioId")).thenReturn("999"); // Usuario que no existe

            // Simula que el SERVICIO lanza la excepción
            doThrow(new IllegalArgumentException("Usuario no encontrado."))
                    .when(mascotaService).registrarMascota(
                            anyString(), any(), anyString(),
                            anyInt(), anyDouble(), anyString(), eq(999L)
                    );

            // Mockea el dispatcher para el forward
            when(request.getRequestDispatcher("/jsp/registrarMascota.jsp")).thenReturn(requestDispatcher);

            // Act
            servlet.doPost(request, response);

            // Assert: Verificar que se intentó registrar
            verify(mascotaService).registrarMascota(
                    eq("Rex"), eq(TipoMascota.PERRO), eq("Pastor Alemán"),
                    eq(2), eq(30.5), eq("Negro"), eq(999L)
            );

            // Verificar que se manejó el error y se hizo forward
            verify(request).setAttribute("error", "Usuario no encontrado.");
            verify(request).setAttribute(eq("tiposMascota"), any()); // Verifica que se repobló el enum
            verify(requestDispatcher).forward(request, response);
            verify(response, never()).sendRedirect(anyString()); // No debe redirigir
        }

        @Test
        @DisplayName("Mostrar formulario de registro - Configura atributos correctamente")
        void testMostrarFormularioRegistro_ConfiguraAtributos() throws ServletException, IOException {
            // Arrange
            when(request.getSession(false)).thenReturn(session);
            when(session.getAttribute("usuario")).thenReturn(usuarioMock);
            when(session.getAttribute("usuarioId")).thenReturn(1L);
            when(request.getParameter("action")).thenReturn("registrar");
            when(request.getRequestDispatcher("/jsp/registrarMascota.jsp")).thenReturn(requestDispatcher);

            // Act
            servlet.doGet(request, response);

            // Assert
            verify(request).setAttribute("usuarioId", 1L);
            verify(request).setAttribute("tiposMascota", TipoMascota.values());
            verify(requestDispatcher).forward(request, response);
        }
    }

    @Nested
    @DisplayName("Tests de Detalles de Mascotas")
    class DetallesMascotas {

        @Test
        @DisplayName("Mostrar detalles de mascota existente - Retorna datos correctos")
        void testMostrarDetallesMascotaExistente_RetornaDatos() throws ServletException, IOException {
            // Arrange
            when(request.getSession(false)).thenReturn(session);
            when(session.getAttribute("usuario")).thenReturn(usuarioMock);
            when(request.getParameter("action")).thenReturn("detalles");
            when(request.getParameter("id")).thenReturn("1");
            when(mascotaService.consultarDatos(1L)).thenReturn(mascotaMock);
            when(request.getRequestDispatcher("/jsp/detallesMascota.jsp")).thenReturn(requestDispatcher);

            // Act
            servlet.doGet(request, response);

            // Assert
            verify(mascotaService).consultarDatos(1L);
            verify(request).setAttribute("mascota", mascotaMock);
            verify(requestDispatcher).forward(request, response);
        }

        @Test
        @DisplayName("Mostrar detalles de mascota inexistente - Redirige a lista")
        void testMostrarDetallesMascotaInexistente_RedigigeALista() throws ServletException, IOException {
            // Arrange
            when(request.getSession(false)).thenReturn(session);
            when(session.getAttribute("usuario")).thenReturn(usuarioMock); // <-- NECESARIO para listarMascotas

            when(request.getParameter("action")).thenReturn("detalles");
            when(request.getParameter("id")).thenReturn("999");

            when(mascotaService.consultarDatos(999L)).thenReturn(null); // Simula mascota nula

            // Mocks para el método listarMascotas(), que ahora será llamado
            when(mascotaService.listarMascotasPorUsuario(1L)).thenReturn(Arrays.asList());
            when(request.getRequestDispatcher("/jsp/listaMascotas.jsp")).thenReturn(requestDispatcher);

            // Act
            servlet.doGet(request, response);

            // Assert
            verify(mascotaService).consultarDatos(999L);
            verify(request).setAttribute("error", "Mascota no encontrada.");

            // Verificar que se llamó a listarMascotas como fallback
            verify(mascotaService).listarMascotasPorUsuario(1L);
            verify(requestDispatcher).forward(request, response);

            // Asegurarse de que NO intentó ir a detallesMascota.jsp
            verify(request, never()).getRequestDispatcher("/jsp/detallesMascota.jsp");
        }
    }

    @Nested
    @DisplayName("Tests del Método obtenerPorUsuario")
    class MetodoObtenerPorUsuario {

        @Test
        @DisplayName("Obtener mascotas por usuario - Delega al DAO correctamente")
        void testObtenerPorUsuario_DelegaAlDAO() {
            // Arrange
            List<Mascota> mascotasEsperadas = Arrays.asList(mascotaMock);
            when(mascotaService.listarMascotasPorUsuario(1L)).thenReturn(mascotasEsperadas);

            // Act
            List<Mascota> resultado = servlet.obtenerPorUsuario(1L);

            // Assert
            verify(mascotaService).listarMascotasPorUsuario(1L);
            assertEquals(mascotasEsperadas, resultado);
            assertEquals(1, resultado.size());
            assertEquals("Firulais", resultado.get(0).getNombre());
        }
    }
}