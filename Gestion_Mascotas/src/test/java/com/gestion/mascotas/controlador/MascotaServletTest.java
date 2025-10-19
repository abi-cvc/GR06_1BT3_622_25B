package com.gestion.mascotas.controlador;

import com.gestion.mascotas.dao.MascotaDAO;
import com.gestion.mascotas.dao.UsuarioDAO;
import com.gestion.mascotas.modelo.Mascota;
import com.gestion.mascotas.modelo.TipoMascota;
import com.gestion.mascotas.modelo.Usuario;
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
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.lang.reflect.Field;
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
    private MascotaDAO mascotaDAO;

    @Mock
    private UsuarioDAO usuarioDAO;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private HttpSession session;

    @Mock
    private RequestDispatcher requestDispatcher;

    @InjectMocks
    private MascotaServlet servlet;

    private Usuario usuarioMock;
    private Mascota mascotaMock;

    @BeforeEach
    void setUp() throws Exception {
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

        // Inyectar los mocks manualmente usando reflexión
        Field mascotaDAOField = MascotaServlet.class.getDeclaredField("mascotaDAO");
        mascotaDAOField.setAccessible(true);
        mascotaDAOField.set(servlet, mascotaDAO);

        Field usuarioDAOField = MascotaServlet.class.getDeclaredField("usuarioDAO");
        usuarioDAOField.setAccessible(true);
        usuarioDAOField.set(servlet, usuarioDAO);
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
            verify(mascotaDAO, never()).obtenerMascotasPorUsuario(anyLong());
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
            verify(mascotaDAO, never()).obtenerMascotasPorUsuario(anyLong());
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
            verify(mascotaDAO, never()).guardar(any());
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
            when(session.getAttribute("usuarioId")).thenReturn(1L);
            when(request.getParameter("action")).thenReturn("listar");

            // Crear lista de mascotas mock
            Mascota mascota2 = new Mascota();
            mascota2.setId(2L);
            mascota2.setNombre("Michi");
            mascota2.setTipo(TipoMascota.GATO);
            List<Mascota> mascotasMock = Arrays.asList(mascotaMock, mascota2);

            // Configurar DAO para retornar lista
            when(mascotaDAO.obtenerMascotasPorUsuario(1L)).thenReturn(mascotasMock);
            when(request.getRequestDispatcher("/jsp/listaMascotas.jsp")).thenReturn(requestDispatcher);

            // Act
            servlet.doGet(request, response);

            // Assert: Verificar que se llamó al DAO correctamente
            verify(mascotaDAO).obtenerMascotasPorUsuario(1L);
            
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
            when(session.getAttribute("usuarioId")).thenReturn(1L);
            when(request.getParameter("action")).thenReturn(null); // Sin action
            when(mascotaDAO.obtenerMascotasPorUsuario(1L)).thenReturn(Arrays.asList(mascotaMock));
            when(request.getRequestDispatcher("/jsp/listaMascotas.jsp")).thenReturn(requestDispatcher);

            // Act
            servlet.doGet(request, response);

            // Assert: Debe comportarse como "listar"
            verify(mascotaDAO).obtenerMascotasPorUsuario(1L);
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
            
            // DAO retorna la mascota del usuario
            when(mascotaDAO.obtenerPorId(1L)).thenReturn(mascotaMock);

            // Act
            servlet.doGet(request, response);

            // Assert: Verificar que se eliminó la mascota
            verify(mascotaDAO).obtenerPorId(1L);
            verify(mascotaDAO).eliminar(1L);
            verify(response).sendRedirect("/app/dashboard?success=mascota_eliminada");
        }

        @Test
        @DisplayName("Eliminar mascota inexistente - Retorna error")
        void testEliminarMascotaInexistente_RetornaError() throws ServletException, IOException {
            // Arrange
            when(request.getSession(false)).thenReturn(session);
            when(session.getAttribute("usuario")).thenReturn(usuarioMock);
            when(request.getParameter("action")).thenReturn("eliminar");
            when(request.getParameter("id")).thenReturn("999");
            when(request.getContextPath()).thenReturn("/app");
            
            // DAO retorna null (mascota no encontrada)
            when(mascotaDAO.obtenerPorId(999L)).thenReturn(null);

            // Act
            servlet.doGet(request, response);

            // Assert: No debe intentar eliminar
            verify(mascotaDAO).obtenerPorId(999L);
            verify(mascotaDAO, never()).eliminar(anyLong());
            verify(response).sendRedirect("/app/dashboard?error=mascota_no_encontrada");
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
            when(mascotaDAO.obtenerPorId(1L)).thenReturn(mascotaMock);

            // Act
            servlet.doGet(request, response);

            // Assert: No debe permitir eliminar
            verify(mascotaDAO).obtenerPorId(1L);
            verify(mascotaDAO, never()).eliminar(anyLong());
            verify(response).sendRedirect("/app/dashboard?error=acceso_denegado");
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
            
            // DAO retorna el usuario
            when(usuarioDAO.obtenerPorId(1L)).thenReturn(usuarioMock);

            // Act
            servlet.doPost(request, response);

            // Assert: Capturar la mascota guardada
            ArgumentCaptor<Mascota> mascotaCaptor = ArgumentCaptor.forClass(Mascota.class);
            verify(mascotaDAO).guardar(mascotaCaptor.capture());
            
            Mascota mascotaGuardada = mascotaCaptor.getValue();
            assertAll("La mascota guardada debe tener todos los datos correctos",
                    () -> assertEquals("Rex", mascotaGuardada.getNombre()),
                    () -> assertEquals(TipoMascota.PERRO, mascotaGuardada.getTipo()),
                    () -> assertEquals("Pastor Alemán", mascotaGuardada.getRaza()),
                    () -> assertEquals(2, mascotaGuardada.getEdad()),
                    () -> assertEquals(30.5, mascotaGuardada.getPeso()),
                    () -> assertEquals("Negro", mascotaGuardada.getColor()),
                    () -> assertEquals(usuarioMock, mascotaGuardada.getUsuario())
            );
            
            verify(response).sendRedirect("/app/mascota?action=listar&success=registrado");
        }

        @Test
        @DisplayName("Registrar mascota con usuario inexistente - Retorna error")
        void testRegistrarMascotaUsuarioInexistente_RetornaError() throws ServletException, IOException {
            // Arrange
            when(request.getSession(false)).thenReturn(session);
            when(session.getAttribute("usuario")).thenReturn(usuarioMock);
            when(request.getParameter("action")).thenReturn("registrar");
            when(request.getParameter("nombre")).thenReturn("Rex");
            when(request.getParameter("tipo")).thenReturn("PERRO");
            when(request.getParameter("raza")).thenReturn("Pastor Alemán");
            when(request.getParameter("edad")).thenReturn("2");
            when(request.getParameter("peso")).thenReturn("30.5");
            when(request.getParameter("color")).thenReturn("Negro");
            when(request.getParameter("usuarioId")).thenReturn("999");
            when(session.getAttribute("usuarioId")).thenReturn(1L);
            
            // DAO retorna null (usuario no encontrado)
            when(usuarioDAO.obtenerPorId(999L)).thenReturn(null);
            when(request.getRequestDispatcher("/jsp/registrarMascota.jsp")).thenReturn(requestDispatcher);

            // Act
            servlet.doPost(request, response);

            // Assert: No debe guardar la mascota
            verify(usuarioDAO).obtenerPorId(999L);
            verify(mascotaDAO, never()).guardar(any());
            verify(request).setAttribute("error", "Usuario no encontrado.");
            verify(requestDispatcher).forward(request, response);
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
            when(mascotaDAO.obtenerPorId(1L)).thenReturn(mascotaMock);
            when(request.getRequestDispatcher("/jsp/detallesMascota.jsp")).thenReturn(requestDispatcher);

            // Act
            servlet.doGet(request, response);

            // Assert
            verify(mascotaDAO).obtenerPorId(1L);
            verify(request).setAttribute("mascota", mascotaMock);
            verify(requestDispatcher).forward(request, response);
        }

        @Test
        @DisplayName("Mostrar detalles de mascota inexistente - Redirige a lista")
        void testMostrarDetallesMascotaInexistente_RedigigeALista() throws ServletException, IOException {
            // Arrange
            when(request.getSession(false)).thenReturn(session);
            when(session.getAttribute("usuario")).thenReturn(usuarioMock);
            when(session.getAttribute("usuarioId")).thenReturn(1L);
            when(request.getParameter("action")).thenReturn("detalles");
            when(request.getParameter("id")).thenReturn("999");
            when(mascotaDAO.obtenerPorId(999L)).thenReturn(null);
            when(mascotaDAO.obtenerMascotasPorUsuario(1L)).thenReturn(Arrays.asList());
            when(request.getRequestDispatcher("/jsp/listaMascotas.jsp")).thenReturn(requestDispatcher);

            // Act
            servlet.doGet(request, response);

            // Assert
            verify(mascotaDAO).obtenerPorId(999L);
            verify(request).setAttribute("error", "Mascota no encontrada.");
            verify(requestDispatcher).forward(request, response);
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
            when(mascotaDAO.obtenerMascotasPorUsuario(1L)).thenReturn(mascotasEsperadas);

            // Act
            List<Mascota> resultado = servlet.obtenerPorUsuario(1L);

            // Assert
            verify(mascotaDAO).obtenerMascotasPorUsuario(1L);
            assertEquals(mascotasEsperadas, resultado);
            assertEquals(1, resultado.size());
            assertEquals("Firulais", resultado.get(0).getNombre());
        }
    }
}
