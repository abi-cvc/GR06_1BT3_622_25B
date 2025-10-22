package com.gestion.mascotas.servicio;

import com.gestion.mascotas.dao.MascotaDAO;
import com.gestion.mascotas.dao.VisitaVeterinariaDAO;
import com.gestion.mascotas.modelo.entidades.Mascota;
import com.gestion.mascotas.modelo.entidades.Usuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class) // Habilita Mockito
class VisitaVeterinariaServiceTest {

    @Mock // Mock para el DAO de Visitas
    private VisitaVeterinariaDAO visitaDAO;

    @Mock // Mock para el DAO de Mascotas (necesario para verificar la mascota)
    private MascotaDAO mascotaDAO;

    @InjectMocks // Instancia del servicio con los mocks inyectados
    private VisitaVeterinariaService visitaService;

    private Usuario usuarioActual;
    private Mascota mascotaUsuarioActual;
    private Long idMascotaExistente = 10L;
    private Long idUsuarioActual = 1L;

    @BeforeEach
    void setUp() {
        // Configurar usuario y mascota para las pruebas
        usuarioActual = new Usuario();
        usuarioActual.setId(idUsuarioActual);
        mascotaUsuarioActual = new Mascota();
        mascotaUsuarioActual.setId(idMascotaExistente);
        mascotaUsuarioActual.setUsuario(usuarioActual); // La mascota pertenece al usuario
    }

    // --- Test 1: Conteo Exitoso con Mock ---
    @Test
    @DisplayName("1. Contar Visitas en Rango Válido (Mock)")
    void contarVisitasEnRango_cuandoHayVisitas_debeRetornarConteoDelDAO() {
        // Fechas de ejemplo para el rango
        LocalDate fechaInicio = LocalDate.of(2025, 1, 1);
        LocalDate fechaFin = LocalDate.of(2025, 12, 31);
        long conteoEsperado = 5; // El número que esperamos que el DAO devuelva

        // Configurar el mock de MascotaDAO para devolver la mascota
        when(mascotaDAO.obtenerPorId(idMascotaExistente)).thenReturn(mascotaUsuarioActual);

        // Configurar el mock de VisitaVeterinariaDAO para que devuelva el conteo esperado
        when(visitaDAO.contarPorMascotaYFechas(idMascotaExistente, fechaInicio, fechaFin))
                .thenReturn(conteoEsperado);

        // Llamar al método del servicio que queremos probar
        long resultado = visitaService.contarVisitasEnRango(idMascotaExistente, fechaInicio, fechaFin, usuarioActual);

        // Verificar que el resultado del servicio es el mismo que devolvió el mock del DAO
        assertEquals(conteoEsperado, resultado);

        // Verificar que se llamó a obtenerPorId de MascotaDAO una vez
        verify(mascotaDAO, times(1)).obtenerPorId(idMascotaExistente);
        // Verificar que se llamó al método contarPorMascotaYFechas del DAO una vez con los parámetros exactos
        verify(visitaDAO, times(1)).contarPorMascotaYFechas(idMascotaExistente, fechaInicio, fechaFin);
    }

    // --- Test: Validación de Rango con Parámetros ---
    static Stream<Arguments> proveedorRangosDeFechasInvalidos() {
        LocalDate hoy = LocalDate.now();
        return Stream.of(
                // Caso 1: Fecha de inicio posterior a fecha fin
                Arguments.of(hoy.plusDays(1), hoy, "Inicio después de Fin"),
                // Caso 2: Fecha de inicio nula
                Arguments.of(null, hoy, "Inicio Nulo"),
                // Caso 3: Fecha de fin nula
                Arguments.of(hoy, null, "Fin Nulo")
        );
    }

    @ParameterizedTest(name = "2. Rango Inválido: {2}") // Usa el tercer argumento (nombre) en el display name
    @MethodSource("proveedorRangosDeFechasInvalidos")
    @DisplayName("Contar Visitas con Rango Inválido - Debe lanzar IllegalArgumentException")
    void contarVisitasEnRango_cuandoRangoEsInvalido_debeLanzarExcepcion(LocalDate fechaInicio, LocalDate fechaFin, String descripcionCaso) {
        when(mascotaDAO.obtenerPorId(idMascotaExistente)).thenReturn(mascotaUsuarioActual);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            visitaService.contarVisitasEnRango(idMascotaExistente, fechaInicio, fechaFin, usuarioActual);
        });

        assertTrue(exception.getMessage().toLowerCase().contains("rango de fechas inválido"));

        verify(mascotaDAO, times(1)).obtenerPorId(idMascotaExistente);
        verify(visitaDAO, never()).contarPorMascotaYFechas(anyLong(), any(), any());
    }
}
