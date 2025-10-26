package com.gestion.mascotas.servicio;

import com.gestion.mascotas.dao.MascotaDAO;
import com.gestion.mascotas.dao.RecordatorioAlimentacionDAO;
import com.gestion.mascotas.modelo.entidades.Mascota;
import com.gestion.mascotas.modelo.entidades.RecordatorioAlimentacion;
import com.gestion.mascotas.modelo.enums.TipoMascota;
import com.gestion.mascotas.modelo.entidades.Usuario;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * Test unitario con MOCKS para RecordatorioAlimentacionService
 * Siguiendo TDD (Test-Driven Development)
 *
 * Mockea las dependencias externas:
 * - RecordatorioAlimentacionDAO: Acceso a base de datos
 * - MascotaDAO: Acceso a base de datos (heredado de RecordatorioService)
 *
 * Objetivo: Probar SOLO la lógica del servicio, no sus dependencias
 *
 * IMPORTANTE: Este test usa reflexión para inyectar mocks y evitar
 * la conexión real a la base de datos durante las pruebas unitarias.
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("Tests con Mocks - RecordatorioAlimentacionService")
class RecordatorioAlimentacionServiceTest {

    @Mock
    private RecordatorioAlimentacionDAO recordatorioDAO;

    @Mock
    private MascotaDAO mascotaDAO;

    private RecordatorioAlimentacionService service;

    private Usuario usuarioMock;
    private Usuario otroUsuarioMock;
    private Mascota mascotaMock;
    private RecordatorioAlimentacion recordatorioMock;

    @BeforeEach
    void setUp() throws Exception {
        // 1. Inicializa los mocks (@Mock)
        MockitoAnnotations.openMocks(this);

        // 2. Instanciar el servicio usando el constructor
        // y pasándole los mocks.
        // Esto es todo lo que necesitas para la inyección.
        service = new RecordatorioAlimentacionService(recordatorioDAO, mascotaDAO);

        // 3. Configurar objetos mock de prueba
        usuarioMock = new Usuario();
        usuarioMock.setId(1L);
        usuarioMock.setNombreUsuario("testUser");
        usuarioMock.setNombre("Test User");

        otroUsuarioMock = new Usuario();
        otroUsuarioMock.setId(2L);
        otroUsuarioMock.setNombreUsuario("otherUser");
        otroUsuarioMock.setNombre("Other User");

        mascotaMock = new Mascota();
        mascotaMock.setId(1L);
        mascotaMock.setNombre("Firulais");
        mascotaMock.setTipo(TipoMascota.PERRO);
        mascotaMock.setUsuario(usuarioMock);

        recordatorioMock = new RecordatorioAlimentacion();
        recordatorioMock.setId(1L);
        recordatorioMock.setMascota(mascotaMock);
        recordatorioMock.setFrecuencia("2");
        recordatorioMock.setTipoAlimento("Croquetas Premium");
        recordatorioMock.setActivo(true);
    }

    @Nested
    @DisplayName("Tests de Registro de Recordatorios")
    class RegistroRecordatorios {

        @Test
        @DisplayName("Registrar recordatorio válido - Guarda correctamente")
        void testRegistrarRecordatorioValido_GuardaCorrectamente() {
            // Arrange: Configurar datos válidos
            String mascotaIdStr = "1";
            String frecuencia = "2";
            String tipoAlimento = "Croquetas Premium";
            String[] diasSeleccionados = {"Lunes", "Miércoles", "Viernes"};

            // Mock de función para obtener parámetros (formato correcto: horario1, horario2)
            Function<String, String> getParameter = key -> {
                if (key.equals("horario1")) return "08:00";
                if (key.equals("horario2")) return "18:00";
                return null;
            };

            // Configurar comportamiento del DAO mockeado
            when(mascotaDAO.obtenerPorId(1L)).thenReturn(mascotaMock);
            when(recordatorioDAO.guardar(any(RecordatorioAlimentacion.class))).thenReturn(true);

            // Act: Ejecutar el método a probar
            boolean resultado = service.configurarRecordatorio(
                    mascotaIdStr, frecuencia, tipoAlimento,
                    diasSeleccionados, getParameter, usuarioMock
            );

            // Assert: Verificar resultados
            assertTrue(resultado, "El registro debe ser exitoso");

            // Verificar que se llamó al DAO con los parámetros correctos
            verify(mascotaDAO).obtenerPorId(1L);

            // Capturar el recordatorio que se intentó guardar
            ArgumentCaptor<RecordatorioAlimentacion> captor = ArgumentCaptor.forClass(RecordatorioAlimentacion.class);
            verify(recordatorioDAO).guardar(captor.capture());

            // Verificar que los datos del recordatorio son correctos
            RecordatorioAlimentacion guardado = captor.getValue();
            assertAll("El recordatorio guardado debe tener todos los datos correctos",
                    () -> assertEquals(mascotaMock, guardado.getMascota()),
                    () -> assertTrue(guardado.isActivo()),
                    () -> assertEquals("2", guardado.getFrecuencia()),
                    () -> assertEquals("Croquetas Premium", guardado.getTipoAlimento()),
                    () -> assertTrue(guardado.getDescripcion().contains("Recordatorio de alimentación"))
            );
        }

        @Test
        @DisplayName("Registrar con frecuencia inválida - Lanza excepción")
        void testRegistrarConFrecuenciaInvalida_LanzaExcepcion() {
            // Arrange: Frecuencia inválida
            String mascotaIdStr = "1";
            String frecuencia = "abc"; // No es un número
            String tipoAlimento = "Croquetas";
            String[] diasSeleccionados = {"Lunes"};
            Function<String, String> getParameter = key -> null;

            // Act & Assert: Verificar que lanza excepción
            IllegalArgumentException exception = assertThrows(
                    IllegalArgumentException.class,
                    () -> service.configurarRecordatorio(
                            mascotaIdStr, frecuencia, tipoAlimento,
                            diasSeleccionados, getParameter, usuarioMock
                    )
            );

            // Verificar que NO se intentó guardar nada en la base de datos
            verify(recordatorioDAO, never()).guardar(any());
        }

        @Test
        @DisplayName("Registrar con mascota inexistente - Lanza excepción")
        void testRegistrarConMascotaInexistente_LanzaExcepcion() {
            // Arrange: Mascota no existe
            String mascotaIdStr = "999";
            String frecuencia = "2";
            String tipoAlimento = "Croquetas";
            String[] diasSeleccionados = {"Lunes"};
            Function<String, String> getParameter = key -> {
                if (key.equals("horario1")) return "08:00";
                if (key.equals("horario2")) return "18:00";
                return null;
            };

            // Configurar mock para que retorne null (mascota no existe)
            when(mascotaDAO.obtenerPorId(999L)).thenReturn(null);

            // Act & Assert
            assertThrows(
                    IllegalArgumentException.class,
                    () -> service.configurarRecordatorio(
                            mascotaIdStr, frecuencia, tipoAlimento,
                            diasSeleccionados, getParameter, usuarioMock
                    ),
                    "Debe lanzar excepción cuando la mascota no existe"
            );

            // Verificar que se intentó buscar la mascota pero NO se guardó nada
            verify(mascotaDAO).obtenerPorId(999L);
            verify(recordatorioDAO, never()).guardar(any());
        }

        @Test
        @DisplayName("Registrar con mascota de otro usuario - Lanza excepción")
        void testRegistrarConMascotaOtroUsuario_LanzaExcepcion() {
            // Arrange: Mascota pertenece a otro usuario
            mascotaMock.setUsuario(otroUsuarioMock);

            String mascotaIdStr = "1";
            String frecuencia = "2";
            String tipoAlimento = "Croquetas";
            String[] diasSeleccionados = {"Lunes"};
            Function<String, String> getParameter = key -> {
                if (key.equals("horario1")) return "08:00";
                if (key.equals("horario2")) return "18:00";
                return null;
            };

            when(mascotaDAO.obtenerPorId(1L)).thenReturn(mascotaMock);

            // Act & Assert
            assertThrows(
                    IllegalArgumentException.class,
                    () -> service.configurarRecordatorio(
                            mascotaIdStr, frecuencia, tipoAlimento,
                            diasSeleccionados, getParameter, usuarioMock
                    ),
                    "Debe lanzar excepción cuando intenta usar mascota de otro usuario"
            );

            // Verificar que se buscó la mascota pero NO se guardó nada
            verify(mascotaDAO).obtenerPorId(1L);
            verify(recordatorioDAO, never()).guardar(any());
        }

        @Test
        @DisplayName("Registrar con tipo de alimento vacío - Lanza excepción")
        void testRegistrarConTipoAlimentoVacio_LanzaExcepcion() {
            // Arrange
            String mascotaIdStr = "1";
            String frecuencia = "2";
            String tipoAlimento = "   "; // Vacío
            String[] diasSeleccionados = {"Lunes"};
            Function<String, String> getParameter = key -> {
                if (key.equals("horario1")) return "08:00";
                if (key.equals("horario2")) return "18:00";
                return null;
            };

            when(mascotaDAO.obtenerPorId(1L)).thenReturn(mascotaMock);

            // Act & Assert
            assertThrows(
                    IllegalArgumentException.class,
                    () -> service.configurarRecordatorio(
                            mascotaIdStr, frecuencia, tipoAlimento,
                            diasSeleccionados, getParameter, usuarioMock
                    ),
                    "Debe lanzar excepción cuando el tipo de alimento está vacío"
            );

            verify(recordatorioDAO, never()).guardar(any());
        }
    }

    @Nested
    @DisplayName("Tests de Actualización de Recordatorios")
    class ActualizacionRecordatorios {

        @Test
        @DisplayName("Actualizar recordatorio propio - Actualiza correctamente")
        void testActualizarRecordatorioPropio_ActualizaCorrectamente() {
            // Arrange
            Long recordatorioId = 1L;
            String frecuencia = "3";
            String tipoAlimento = "Alimento Húmedo";
            String[] diasSeleccionados = {"Lunes", "Miércoles", "Viernes"};
            Function<String, String> getParameter = key -> {
                if (key.equals("horario1")) return "07:00";
                if (key.equals("horario2")) return "13:00";
                if (key.equals("horario3")) return "19:00";
                return null;
            };

            when(recordatorioDAO.obtenerPorId(1L)).thenReturn(recordatorioMock);
            when(recordatorioDAO.actualizar(any(RecordatorioAlimentacion.class))).thenReturn(true);

            // Act
            boolean resultado = service.modificarRecordatorio(
                    recordatorioId, frecuencia, tipoAlimento,
                    diasSeleccionados, getParameter, usuarioMock
            );

            // Assert
            assertTrue(resultado, "La actualización debe ser exitosa");
            verify(recordatorioDAO).obtenerPorId(1L);
            verify(recordatorioDAO).actualizar(recordatorioMock);

            // Verificar que se actualizaron los campos
            assertEquals("3", recordatorioMock.getFrecuencia());
            assertEquals("Alimento Húmedo", recordatorioMock.getTipoAlimento());
        }

        @Test
        @DisplayName("Actualizar recordatorio de otro usuario - Lanza excepción")
        void testActualizarRecordatorioOtroUsuario_LanzaExcepcion() {
            // Arrange: Recordatorio pertenece a otro usuario
            mascotaMock.setUsuario(otroUsuarioMock);

            Long recordatorioId = 1L;
            String frecuencia = "3";
            String tipoAlimento = "Alimento Húmedo";
            String[] diasSeleccionados = {"Lunes"};
            Function<String, String> getParameter = key -> {
                if (key.equals("horario1")) return "08:00";
                if (key.equals("horario2")) return "13:00";
                if (key.equals("horario3")) return "19:00";
                return null;
            };

            when(recordatorioDAO.obtenerPorId(1L)).thenReturn(recordatorioMock);

            // Act & Assert
            assertThrows(
                    IllegalArgumentException.class,
                    () -> service.modificarRecordatorio(
                            recordatorioId, frecuencia, tipoAlimento,
                            diasSeleccionados, getParameter, usuarioMock
                    ),
                    "Debe lanzar excepción cuando intenta modificar recordatorio de otro usuario"
            );

            // Verificar que se buscó el recordatorio pero NO se actualizó
            verify(recordatorioDAO).obtenerPorId(1L);
            verify(recordatorioDAO, never()).actualizar(any());
        }

        @Test
        @DisplayName("Actualizar recordatorio inexistente - Lanza excepción")
        void testActualizarRecordatorioInexistente_LanzaExcepcion() {
            // Arrange
            Long recordatorioId = 999L;
            String frecuencia = "3";
            String tipoAlimento = "Alimento";
            String[] diasSeleccionados = {"Lunes"};
            Function<String, String> getParameter = key -> {
                if (key.equals("horario1")) return "08:00";
                if (key.equals("horario2")) return "13:00";
                if (key.equals("horario3")) return "19:00";
                return null;
            };

            when(recordatorioDAO.obtenerPorId(999L)).thenReturn(null);

            // Act & Assert
            assertThrows(
                    IllegalArgumentException.class,
                    () -> service.modificarRecordatorio(
                            recordatorioId, frecuencia, tipoAlimento,
                            diasSeleccionados, getParameter, usuarioMock
                    ),
                    "Debe lanzar excepción cuando el recordatorio no existe"
            );

            verify(recordatorioDAO).obtenerPorId(999L);
            verify(recordatorioDAO, never()).actualizar(any());
        }
    }

    @Nested
    @DisplayName("Tests de Eliminación de Recordatorios")
    class EliminacionRecordatorios {

        @Test
        @DisplayName("Eliminar recordatorio propio - Elimina correctamente")
        void testEliminarRecordatorioPropio_EliminaCorrectamente() {
            // Arrange
            String idStr = "1";
            when(recordatorioDAO.obtenerPorId(1L)).thenReturn(recordatorioMock);
            when(recordatorioDAO.eliminar(1L)).thenReturn(true);

            // Act
            boolean resultado = service.eliminarRecordatorio(idStr, usuarioMock);

            // Assert
            assertTrue(resultado, "La eliminación debe ser exitosa");
            verify(recordatorioDAO).obtenerPorId(1L);
            verify(recordatorioDAO).eliminar(1L);
        }

        @Test
        @DisplayName("Eliminar con ID inválido - Lanza excepción")
        void testEliminarConIdInvalido_LanzaExcepcion() {
            // Arrange
            String idStr = "abc"; // ID inválido

            // Act & Assert
            assertThrows(
                    IllegalArgumentException.class,
                    () -> service.eliminarRecordatorio(idStr, usuarioMock),
                    "Debe lanzar excepción cuando el ID no es numérico"
            );

            verify(recordatorioDAO, never()).eliminar(anyLong());
            verify(recordatorioDAO, never()).obtenerPorId(anyLong());
        }

        @Test
        @DisplayName("Eliminar recordatorio inexistente - Lanza excepción")
        void testEliminarRecordatorioInexistente_LanzaExcepcion() {
            // Arrange
            String idStr = "999";
            when(recordatorioDAO.obtenerPorId(999L)).thenReturn(null);

            // Act & Assert
            assertThrows(
                    IllegalArgumentException.class,
                    () -> service.eliminarRecordatorio(idStr, usuarioMock),
                    "Debe lanzar excepción cuando el recordatorio no existe"
            );

            verify(recordatorioDAO).obtenerPorId(999L);
            verify(recordatorioDAO, never()).eliminar(anyLong());
        }

        @Test
        @DisplayName("Eliminar recordatorio de otro usuario - Lanza excepción")
        void testEliminarRecordatorioOtroUsuario_LanzaExcepcion() {
            // Arrange: Recordatorio pertenece a otro usuario
            mascotaMock.setUsuario(otroUsuarioMock);

            String idStr = "1";
            when(recordatorioDAO.obtenerPorId(1L)).thenReturn(recordatorioMock);

            // Act & Assert
            assertThrows(
                    IllegalArgumentException.class,
                    () -> service.eliminarRecordatorio(idStr, usuarioMock),
                    "Debe lanzar excepción cuando intenta eliminar recordatorio de otro usuario"
            );

            verify(recordatorioDAO).obtenerPorId(1L);
            verify(recordatorioDAO, never()).eliminar(anyLong());
        }
    }

    @Nested
    @DisplayName("Tests de Desactivación de Recordatorios")
    class DesactivacionRecordatorios {

        @Test
        @DisplayName("Desactivar recordatorio propio - Desactiva correctamente")
        void testDesactivarRecordatorioPropio_DesactivaCorrectamente() {
            // Arrange
            Long recordatorioId = 1L;
            when(recordatorioDAO.obtenerPorId(1L)).thenReturn(recordatorioMock);
            when(recordatorioDAO.desactivarRecordatorio(1L)).thenReturn(true);

            // Act
            boolean resultado = service.desactivarRecordatorio(recordatorioId, usuarioMock);

            // Assert
            assertTrue(resultado, "La desactivación debe ser exitosa");
            verify(recordatorioDAO).obtenerPorId(1L);
            verify(recordatorioDAO).desactivarRecordatorio(1L);
        }

        @Test
        @DisplayName("Desactivar recordatorio de otro usuario - Lanza excepción")
        void testDesactivarRecordatorioOtroUsuario_LanzaExcepcion() {
            // Arrange
            mascotaMock.setUsuario(otroUsuarioMock);

            Long recordatorioId = 1L;
            when(recordatorioDAO.obtenerPorId(1L)).thenReturn(recordatorioMock);

            // Act & Assert
            assertThrows(
                    IllegalArgumentException.class,
                    () -> service.desactivarRecordatorio(recordatorioId, usuarioMock),
                    "Debe lanzar excepción cuando intenta desactivar recordatorio de otro usuario"
            );

            verify(recordatorioDAO).obtenerPorId(1L);
            verify(recordatorioDAO, never()).desactivarRecordatorio(anyLong());
        }
    }

    @Nested
    @DisplayName("Tests de Consulta de Recordatorios")
    class ConsultaRecordatorios {

        @Test
        @DisplayName("Obtener recordatorios por mascota - Delega al DAO correctamente")
        void testObtenerRecordatoriosPorMascota_DelegaAlDAO() {
            // Arrange
            Long mascotaId = 1L;
            RecordatorioAlimentacion segundoRecordatorio = new RecordatorioAlimentacion();
            segundoRecordatorio.setId(2L);
            segundoRecordatorio.setMascota(mascotaMock);
            segundoRecordatorio.setTipoAlimento("Comida Húmeda");

            List<RecordatorioAlimentacion> recordatoriosEsperados = Arrays.asList(
                    recordatorioMock,
                    segundoRecordatorio
            );

            when(recordatorioDAO.obtenerRecordatoriosAlimentacionPorMascota(1L))
                    .thenReturn(recordatoriosEsperados);

            // Act
            List<RecordatorioAlimentacion> resultado = service.obtenerRecordatoriosPorMascota(mascotaId);

            // Assert
            verify(recordatorioDAO).obtenerRecordatoriosAlimentacionPorMascota(1L);
            assertEquals(recordatoriosEsperados, resultado);
            assertEquals(2, resultado.size());
            assertEquals("Croquetas Premium", resultado.get(0).getTipoAlimento());
            assertEquals("Comida Húmeda", resultado.get(1).getTipoAlimento());
        }

        @Test
        @DisplayName("Obtener recordatorios de mascota sin recordatorios - Retorna lista vacía")
        void testObtenerRecordatoriosSinRecordatorios_RetornaListaVacia() {
            // Arrange
            Long mascotaId = 999L;
            when(recordatorioDAO.obtenerRecordatoriosAlimentacionPorMascota(999L))
                    .thenReturn(Collections.emptyList());

            // Act
            List<RecordatorioAlimentacion> resultado = service.obtenerRecordatoriosPorMascota(mascotaId);

            // Assert
            verify(recordatorioDAO).obtenerRecordatoriosAlimentacionPorMascota(999L);
            assertNotNull(resultado, "No debe retornar null");
            assertTrue(resultado.isEmpty(), "Debe retornar una lista vacía");
            assertEquals(0, resultado.size());
        }
    }
}