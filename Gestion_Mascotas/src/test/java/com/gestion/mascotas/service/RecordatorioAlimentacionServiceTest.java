package com.gestion.mascotas.service;

import com.gestion.mascotas.dao.MascotaDAO;
import com.gestion.mascotas.dao.RecordatorioAlimentacionDAO;
import com.gestion.mascotas.modelo.Mascota;
import com.gestion.mascotas.modelo.RecordatorioAlimentacion;
import com.gestion.mascotas.modelo.TipoMascota;
import com.gestion.mascotas.modelo.Usuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Field;
import java.util.Arrays;
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
    private Mascota mascotaMock;
    private RecordatorioAlimentacion recordatorioMock;

    @BeforeEach
    void setUp() throws Exception {
        service = new RecordatorioAlimentacionService();

        // Inyectar los DAOs mockeados usando reflexión
        Field recordatorioDAOField = RecordatorioAlimentacionService.class.getDeclaredField("recordatorioDAO");
        recordatorioDAOField.setAccessible(true);
        recordatorioDAOField.set(service, recordatorioDAO);

        Field mascotaDAOField = RecordatorioService.class.getDeclaredField("mascotaDAO");
        mascotaDAOField.setAccessible(true);
        mascotaDAOField.set(service, mascotaDAO);

        // Configurar usuario mock
        usuarioMock = new Usuario();
        usuarioMock.setId(1L);
        usuarioMock.setNombreUsuario("testUser");
        usuarioMock.setNombre("Test User");

        // Configurar mascota mock
        mascotaMock = new Mascota();
        mascotaMock.setId(1L);
        mascotaMock.setNombre("Firulais");
        mascotaMock.setTipo(TipoMascota.PERRO);
        mascotaMock.setUsuario(usuarioMock);

        // Configurar recordatorio mock
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

            // Configurar comportamiento del DAO
            when(mascotaDAO.obtenerPorId(1L)).thenReturn(mascotaMock);
            when(recordatorioDAO.guardar(any(RecordatorioAlimentacion.class))).thenReturn(true);

            // Act
            boolean resultado = service.registrarRecordatorio(
                    mascotaIdStr, frecuencia, tipoAlimento, 
                    diasSeleccionados, getParameter, usuarioMock
            );

            // Assert
            assertTrue(resultado, "El registro debe ser exitoso");
            
            // Capturar el recordatorio guardado
            ArgumentCaptor<RecordatorioAlimentacion> captor = ArgumentCaptor.forClass(RecordatorioAlimentacion.class);
            verify(recordatorioDAO).guardar(captor.capture());
            
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

            // Act & Assert
            IllegalArgumentException exception = assertThrows(
                    IllegalArgumentException.class,
                    () -> service.registrarRecordatorio(
                            mascotaIdStr, frecuencia, tipoAlimento, 
                            diasSeleccionados, getParameter, usuarioMock
                    )
            );

            // Verificar que NO se intentó guardar
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

            when(mascotaDAO.obtenerPorId(999L)).thenReturn(null);

            // Act & Assert
            IllegalArgumentException exception = assertThrows(
                    IllegalArgumentException.class,
                    () -> service.registrarRecordatorio(
                            mascotaIdStr, frecuencia, tipoAlimento, 
                            diasSeleccionados, getParameter, usuarioMock
                    )
            );

            // Verificar que NO se intentó guardar
            verify(recordatorioDAO, never()).guardar(any());
        }

        @Test
        @DisplayName("Registrar con mascota de otro usuario - Lanza excepción")
        void testRegistrarConMascotaOtroUsuario_LanzaExcepcion() {
            // Arrange: Mascota pertenece a otro usuario
            Usuario otroUsuario = new Usuario();
            otroUsuario.setId(2L);
            mascotaMock.setUsuario(otroUsuario);

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
            IllegalArgumentException exception = assertThrows(
                    IllegalArgumentException.class,
                    () -> service.registrarRecordatorio(
                            mascotaIdStr, frecuencia, tipoAlimento, 
                            diasSeleccionados, getParameter, usuarioMock
                    )
            );

            // Verificar que NO se intentó guardar
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
            IllegalArgumentException exception = assertThrows(
                    IllegalArgumentException.class,
                    () -> service.registrarRecordatorio(
                            mascotaIdStr, frecuencia, tipoAlimento, 
                            diasSeleccionados, getParameter, usuarioMock
                    )
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
            boolean resultado = service.actualizarRecordatorio(
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
            Usuario otroUsuario = new Usuario();
            otroUsuario.setId(2L);
            mascotaMock.setUsuario(otroUsuario);

            Long recordatorioId = 1L;
            String frecuencia = "3";
            String tipoAlimento = "Alimento Húmedo";
            String[] diasSeleccionados = {"Lunes"};
            Function<String, String> getParameter = key -> "08:00";

            when(recordatorioDAO.obtenerPorId(1L)).thenReturn(recordatorioMock);

            // Act & Assert
            IllegalArgumentException exception = assertThrows(
                    IllegalArgumentException.class,
                    () -> service.actualizarRecordatorio(
                            recordatorioId, frecuencia, tipoAlimento, 
                            diasSeleccionados, getParameter, usuarioMock
                    )
            );

            // Verificar que NO se actualizó
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
            Function<String, String> getParameter = key -> "08:00";

            when(recordatorioDAO.obtenerPorId(999L)).thenReturn(null);

            // Act & Assert
            IllegalArgumentException exception = assertThrows(
                    IllegalArgumentException.class,
                    () -> service.actualizarRecordatorio(
                            recordatorioId, frecuencia, tipoAlimento, 
                            diasSeleccionados, getParameter, usuarioMock
                    )
            );

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
            IllegalArgumentException exception = assertThrows(
                    IllegalArgumentException.class,
                    () -> service.eliminarRecordatorio(idStr, usuarioMock)
            );

            verify(recordatorioDAO, never()).eliminar(anyLong());
        }

        @Test
        @DisplayName("Eliminar recordatorio inexistente - Lanza excepción")
        void testEliminarRecordatorioInexistente_LanzaExcepcion() {
            // Arrange
            String idStr = "999";
            when(recordatorioDAO.obtenerPorId(999L)).thenReturn(null);

            // Act & Assert
            IllegalArgumentException exception = assertThrows(
                    IllegalArgumentException.class,
                    () -> service.eliminarRecordatorio(idStr, usuarioMock)
            );

            verify(recordatorioDAO, never()).eliminar(anyLong());
        }

        @Test
        @DisplayName("Eliminar recordatorio de otro usuario - Lanza excepción")
        void testEliminarRecordatorioOtroUsuario_LanzaExcepcion() {
            // Arrange: Recordatorio pertenece a otro usuario
            Usuario otroUsuario = new Usuario();
            otroUsuario.setId(2L);
            mascotaMock.setUsuario(otroUsuario);

            String idStr = "1";
            when(recordatorioDAO.obtenerPorId(1L)).thenReturn(recordatorioMock);

            // Act & Assert
            IllegalArgumentException exception = assertThrows(
                    IllegalArgumentException.class,
                    () -> service.eliminarRecordatorio(idStr, usuarioMock)
            );

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
            Usuario otroUsuario = new Usuario();
            otroUsuario.setId(2L);
            mascotaMock.setUsuario(otroUsuario);

            Long recordatorioId = 1L;
            when(recordatorioDAO.obtenerPorId(1L)).thenReturn(recordatorioMock);

            // Act & Assert
            IllegalArgumentException exception = assertThrows(
                    IllegalArgumentException.class,
                    () -> service.desactivarRecordatorio(recordatorioId, usuarioMock)
            );

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
            List<RecordatorioAlimentacion> recordatoriosEsperados = Arrays.asList(
                    recordatorioMock,
                    new RecordatorioAlimentacion()
            );
            when(recordatorioDAO.obtenerRecordatoriosAlimentacionPorMascota(1L))
                    .thenReturn(recordatoriosEsperados);

            // Act
            List<RecordatorioAlimentacion> resultado = service.obtenerRecordatoriosPorMascota(mascotaId);

            // Assert
            verify(recordatorioDAO).obtenerRecordatoriosAlimentacionPorMascota(1L);
            assertEquals(recordatoriosEsperados, resultado);
            assertEquals(2, resultado.size());
        }

        @Test
        @DisplayName("Obtener recordatorios de mascota sin recordatorios - Retorna lista vacía")
        void testObtenerRecordatoriosSinRecordatorios_RetornaListaVacia() {
            // Arrange
            Long mascotaId = 999L;
            when(recordatorioDAO.obtenerRecordatoriosAlimentacionPorMascota(999L))
                    .thenReturn(Arrays.asList());

            // Act
            List<RecordatorioAlimentacion> resultado = service.obtenerRecordatoriosPorMascota(mascotaId);

            // Assert
            verify(recordatorioDAO).obtenerRecordatoriosAlimentacionPorMascota(999L);
            assertTrue(resultado.isEmpty(), "Debe retornar una lista vacía");
        }
    }
}
