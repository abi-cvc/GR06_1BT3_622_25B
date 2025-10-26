package com.gestion.mascotas.servicio;

import com.gestion.mascotas.dao.MascotaDAO;
import com.gestion.mascotas.dao.VisitaVeterinariaDAO;
import com.gestion.mascotas.modelo.entidades.Mascota;
import com.gestion.mascotas.modelo.entidades.Usuario;
import com.gestion.mascotas.modelo.entidades.VisitaVeterinaria;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test unitario con MOCKS para VisitaVeterinariaService
 *
 * IMPORTANTE: Este test usa reflexión para inyectar mocks.
 * Requiere que los campos DAO NO sean 'final' en el servicio.
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("Tests con Mocks - VisitaVeterinariaService")
class VisitaVeterinariaServiceTest {

    @Mock
    private VisitaVeterinariaDAO visitaDAO;

    @Mock
    private MascotaDAO mascotaDAO;

    private VisitaVeterinariaService visitaService;

    private Usuario usuarioActual;
    private Usuario otroUsuario;
    private Mascota mascotaUsuarioActual;
    private VisitaVeterinaria visitaMock;

    @BeforeEach
    void setUp() throws Exception {
        // 1. Inicializa los mocks (@Mock)
        MockitoAnnotations.openMocks(this);
        // 2. Instanciar el servicio
        visitaService = new VisitaVeterinariaService(visitaDAO, mascotaDAO);

        // 2. Configurar objetos mock de prueba
        configurarMocksBase();
    }

    private void configurarMocksBase() {
        usuarioActual = new Usuario();
        usuarioActual.setId(1L);
        usuarioActual.setNombreUsuario("testUser");

        otroUsuario = new Usuario();
        otroUsuario.setId(2L);
        otroUsuario.setNombreUsuario("otherUser");

        mascotaUsuarioActual = new Mascota();
        mascotaUsuarioActual.setId(1L);
        mascotaUsuarioActual.setNombre("Firulais");
        mascotaUsuarioActual.setUsuario(usuarioActual);

        visitaMock = new VisitaVeterinaria();
        visitaMock.setId(10L);
        visitaMock.setMascota(mascotaUsuarioActual);
        visitaMock.setFecha(LocalDate.now().minusDays(5));
        visitaMock.setMotivo("Vacunación");
        visitaMock.setDiagnostico("Saludable");
        visitaMock.setTratamiento("Vacuna antirrábica");
    }

    @Nested
    @DisplayName("Tests de Registro de Visitas")
    class RegistroVisitas {

        @Test
        @DisplayName("Registrar visita válida - Guarda correctamente")
        void registrarVisita_conDatosValidos_guardaCorrectamente() {
            // Arrange
            LocalDate fecha = LocalDate.now().minusDays(1);
            String motivo = "Vacunación";
            String diagnostico = "Saludable";
            String tratamiento = "Vacuna antirrábica";
            String observaciones = "Sin reacciones adversas";
            String nombreVeterinario = "Dr. García";

            when(mascotaDAO.obtenerPorId(1L)).thenReturn(mascotaUsuarioActual);
            doNothing().when(visitaDAO).guardar(any(VisitaVeterinaria.class));

            // Act
            assertDoesNotThrow(() -> {
                visitaService.registrarVisita(fecha, motivo, diagnostico, tratamiento,
                        observaciones, nombreVeterinario, 1L, usuarioActual);
            });

            // Assert
            verify(mascotaDAO).obtenerPorId(1L);

            ArgumentCaptor<VisitaVeterinaria> captor = ArgumentCaptor.forClass(VisitaVeterinaria.class);
            verify(visitaDAO).guardar(captor.capture());

            VisitaVeterinaria visitaGuardada = captor.getValue();
            assertAll("Verificar datos de la visita guardada",
                    () -> assertEquals(fecha, visitaGuardada.getFecha()),
                    () -> assertEquals(motivo, visitaGuardada.getMotivo()),
                    () -> assertEquals(diagnostico, visitaGuardada.getDiagnostico()),
                    () -> assertEquals(tratamiento, visitaGuardada.getTratamiento()),
                    () -> assertEquals(observaciones, visitaGuardada.getObservaciones()),
                    () -> assertEquals(nombreVeterinario, visitaGuardada.getNombreVeterinario()),
                    () -> assertEquals(mascotaUsuarioActual, visitaGuardada.getMascota())
            );
        }

        @Test
        @DisplayName("Registrar con fecha futura - Lanza excepción")
        void registrarVisita_conFechaFutura_lanzaExcepcion() {
            // Arrange
            LocalDate fechaFutura = LocalDate.now().plusDays(1);

            when(mascotaDAO.obtenerPorId(1L)).thenReturn(mascotaUsuarioActual);

            // Act & Assert
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
                visitaService.registrarVisita(fechaFutura, "Motivo", "Diagnóstico",
                        "Tratamiento", "Observaciones", "Dr. Test", 1L, usuarioActual);
            });

            assertTrue(exception.getMessage().contains("no puede ser futura"));
            verify(visitaDAO, never()).guardar(any());
        }

        @Test
        @DisplayName("Registrar con motivo vacío - Lanza excepción")
        void registrarVisita_conMotivoVacio_lanzaExcepcion() {
            // Arrange
            LocalDate fecha = LocalDate.now();

            when(mascotaDAO.obtenerPorId(1L)).thenReturn(mascotaUsuarioActual);

            // Act & Assert
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
                visitaService.registrarVisita(fecha, "   ", "Diagnóstico",
                        "Tratamiento", "Observaciones", "Dr. Test", 1L, usuarioActual);
            });

            assertTrue(exception.getMessage().toLowerCase().contains("motivo"));
            verify(visitaDAO, never()).guardar(any());
        }

        @Test
        @DisplayName("Registrar con mascota inexistente - Lanza excepción")
        void registrarVisita_conMascotaInexistente_lanzaExcepcion() {
            // Arrange
            LocalDate fecha = LocalDate.now();

            when(mascotaDAO.obtenerPorId(999L)).thenReturn(null);

            // Act & Assert
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
                visitaService.registrarVisita(fecha, "Motivo", "Diagnóstico",
                        "Tratamiento", "Observaciones", "Dr. Test", 999L, usuarioActual);
            });

            assertTrue(exception.getMessage().toLowerCase().contains("mascota"));
            verify(visitaDAO, never()).guardar(any());
        }

        @Test
        @DisplayName("Registrar con mascota de otro usuario - Lanza SecurityException")
        void registrarVisita_conMascotaDeOtroUsuario_lanzaSecurityException() {
            // Arrange
            LocalDate fecha = LocalDate.now();
            mascotaUsuarioActual.setUsuario(otroUsuario);

            when(mascotaDAO.obtenerPorId(1L)).thenReturn(mascotaUsuarioActual);

            // Act & Assert
            SecurityException exception = assertThrows(SecurityException.class, () -> {
                visitaService.registrarVisita(fecha, "Motivo", "Diagnóstico",
                        "Tratamiento", "Observaciones", "Dr. Test", 1L, usuarioActual);
            });

            assertTrue(exception.getMessage().toLowerCase().contains("permiso"));
            verify(visitaDAO, never()).guardar(any());
        }

        @Test
        @DisplayName("Registrar con diagnóstico muy largo - Lanza excepción")
        void registrarVisita_conDiagnosticoMuyLargo_lanzaExcepcion() {
            // Arrange
            LocalDate fecha = LocalDate.now();
            String diagnosticoLargo = "a".repeat(1001);

            when(mascotaDAO.obtenerPorId(1L)).thenReturn(mascotaUsuarioActual);

            // Act & Assert
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
                visitaService.registrarVisita(fecha, "Motivo", diagnosticoLargo,
                        "Tratamiento", "Observaciones", "Dr. Test", 1L, usuarioActual);
            });

            assertTrue(exception.getMessage().toLowerCase().contains("diagnóstico"));
            verify(visitaDAO, never()).guardar(any());
        }
    }

    @Nested
    @DisplayName("Tests de Consulta de Historial")
    class ConsultaHistorial {

        @Test
        @DisplayName("Consultar historial por usuario - Retorna lista de visitas")
        void consultarHistorialPorUsuario_retornaListaDeVisitas() {
            // Arrange
            List<VisitaVeterinaria> visitasEsperadas = Arrays.asList(
                    visitaMock,
                    new VisitaVeterinaria()
            );

            when(visitaDAO.obtenerPorUsuario(1L)).thenReturn(visitasEsperadas);

            // Act
            List<VisitaVeterinaria> resultado = visitaService.consultarHistorialPorUsuario(1L);

            // Assert
            verify(visitaDAO).obtenerPorUsuario(1L);
            assertEquals(2, resultado.size());
            assertEquals(visitasEsperadas, resultado);
        }

        @Test
        @DisplayName("Consultar historial sin visitas - Retorna lista vacía")
        void consultarHistorialPorUsuario_sinVisitas_retornaListaVacia() {
            // Arrange
            when(visitaDAO.obtenerPorUsuario(1L)).thenReturn(Collections.emptyList());

            // Act
            List<VisitaVeterinaria> resultado = visitaService.consultarHistorialPorUsuario(1L);

            // Assert
            verify(visitaDAO).obtenerPorUsuario(1L);
            assertTrue(resultado.isEmpty());
        }
    }

    @Nested
    @DisplayName("Tests de Eliminación de Visitas")
    class EliminacionVisitas {

        @Test
        @DisplayName("Eliminar visita propia - Elimina correctamente")
        void eliminarVisita_visitaPropia_eliminaCorrectamente() {
            // Arrange
            when(visitaDAO.obtenerPorId(10L)).thenReturn(visitaMock);
            doNothing().when(visitaDAO).eliminar(10L);

            // Act
            assertDoesNotThrow(() -> {
                visitaService.eliminarVisita(10L, usuarioActual);
            });

            // Assert
            verify(visitaDAO).obtenerPorId(10L);
            verify(visitaDAO).eliminar(10L);
        }

        @Test
        @DisplayName("Eliminar visita inexistente - Lanza excepción")
        void eliminarVisita_visitaInexistente_lanzaExcepcion() {
            // Arrange
            when(visitaDAO.obtenerPorId(999L)).thenReturn(null);

            // Act & Assert
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
                visitaService.eliminarVisita(999L, usuarioActual);
            });

            assertTrue(exception.getMessage().toLowerCase().contains("no encontrado"));
            verify(visitaDAO, never()).eliminar(anyLong());
        }

        @Test
        @DisplayName("Eliminar visita de otro usuario - Lanza SecurityException")
        void eliminarVisita_visitaDeOtroUsuario_lanzaSecurityException() {
            // Arrange
            mascotaUsuarioActual.setUsuario(otroUsuario);

            when(visitaDAO.obtenerPorId(10L)).thenReturn(visitaMock);

            // Act & Assert
            SecurityException exception = assertThrows(SecurityException.class, () -> {
                visitaService.eliminarVisita(10L, usuarioActual);
            });

            assertTrue(exception.getMessage().toLowerCase().contains("permiso"));
            verify(visitaDAO, never()).eliminar(anyLong());
        }
    }

    @Nested
    @DisplayName("Tests de Conteo de Visitas en Rango")
    class ConteoVisitasEnRango {

        @Test
        @DisplayName("Contar visitas con rango válido - Retorna conteo correcto")
        void contarVisitasEnRango_conRangoValido_retornaConteo() {
            // Arrange
            LocalDate inicio = LocalDate.now().minusDays(30);
            LocalDate fin = LocalDate.now();

            when(mascotaDAO.obtenerPorId(1L)).thenReturn(mascotaUsuarioActual);
            when(visitaDAO.contarPorMascotaYFechas(1L, inicio, fin)).thenReturn(5L);

            // Act
            long resultado = visitaService.contarVisitasEnRango(1L, inicio, fin, usuarioActual);

            // Assert
            assertEquals(5L, resultado);
            verify(mascotaDAO).obtenerPorId(1L);
            verify(visitaDAO).contarPorMascotaYFechas(1L, inicio, fin);
        }

        @Test
        @DisplayName("Contar visitas con mascota inexistente - Lanza excepción")
        void contarVisitasEnRango_conMascotaInexistente_lanzaExcepcion() {
            // Arrange
            LocalDate inicio = LocalDate.now().minusDays(30);
            LocalDate fin = LocalDate.now();

            when(mascotaDAO.obtenerPorId(999L)).thenReturn(null);

            // Act & Assert
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
                visitaService.contarVisitasEnRango(999L, inicio, fin, usuarioActual);
            });

            assertTrue(exception.getMessage().toLowerCase().contains("mascota no encontrada"));
            verify(visitaDAO, never()).contarPorMascotaYFechas(anyLong(), any(), any());
        }

        @Test
        @DisplayName("Contar visitas con mascota de otro usuario - Lanza SecurityException")
        void contarVisitasEnRango_conMascotaDeOtroUsuario_lanzaSecurityException() {
            // Arrange
            LocalDate inicio = LocalDate.now().minusDays(30);
            LocalDate fin = LocalDate.now();
            mascotaUsuarioActual.setUsuario(otroUsuario);

            when(mascotaDAO.obtenerPorId(1L)).thenReturn(mascotaUsuarioActual);

            // Act & Assert
            SecurityException exception = assertThrows(SecurityException.class, () -> {
                visitaService.contarVisitasEnRango(1L, inicio, fin, usuarioActual);
            });

            assertTrue(exception.getMessage().toLowerCase().contains("permiso"));
            verify(visitaDAO, never()).contarPorMascotaYFechas(anyLong(), any(), any());
        }

        // Tests parametrizados para validar rangos de fechas inválidos
        static Stream<Arguments> proveedorRangosDeFechasInvalidos() {
            LocalDate hoy = LocalDate.now();
            return Stream.of(
                    Arguments.of(hoy.plusDays(1), hoy, "Inicio después de Fin"),
                    Arguments.of(null, hoy, "Inicio Nulo"),
                    Arguments.of(hoy, null, "Fin Nulo")
            );
        }

        @ParameterizedTest(name = "Rango Inválido: {2}")
        @MethodSource("proveedorRangosDeFechasInvalidos")
        @DisplayName("Contar visitas con rango inválido - Lanza IllegalArgumentException")
        void contarVisitasEnRango_conRangoInvalido_lanzaExcepcion(
                LocalDate fechaInicio, LocalDate fechaFin, String descripcionCaso) {

            // Act & Assert (la validación ocurre ANTES de consultar la mascota)
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
                visitaService.contarVisitasEnRango(1L, fechaInicio, fechaFin, usuarioActual);
            });

            assertTrue(exception.getMessage().toLowerCase().contains("rango de fechas inválido"));

            // La mascota NO debe consultarse si el rango es inválido
            verify(mascotaDAO, never()).obtenerPorId(anyLong());
            verify(visitaDAO, never()).contarPorMascotaYFechas(anyLong(), any(), any());
        }
    }
}