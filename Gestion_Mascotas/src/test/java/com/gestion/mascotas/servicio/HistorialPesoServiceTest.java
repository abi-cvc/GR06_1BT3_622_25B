package com.gestion.mascotas.servicio;

import com.gestion.mascotas.dao.HistorialPesoDAO;
import com.gestion.mascotas.dao.MascotaDAO;
import com.gestion.mascotas.modelo.entidades.HistorialPeso;
import com.gestion.mascotas.modelo.entidades.Mascota;
import com.gestion.mascotas.util.ValidadorPeso;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

public class HistorialPesoServiceTest {

    private HistorialPesoService service;
    private HistorialPesoDAO historialPesoDAOMock;
    private MascotaDAO mascotaDAOMock;
    private ValidadorPeso validadorPeso;

    @Before
    public void setUp() {
        historialPesoDAOMock = mock(HistorialPesoDAO.class);
        mascotaDAOMock = mock(MascotaDAO.class);
        validadorPeso = new ValidadorPeso();
        service = new HistorialPesoService(historialPesoDAOMock, mascotaDAOMock, validadorPeso);
    }

    @Test
    public void given_valid_weight_when_save_then_updates_pet_and_returns_success() {
        Long mascotaId = 1L;
        double peso = 15.5;
        Mascota mascota = new Mascota();
        mascota.setId(mascotaId);
        mascota.setPeso(10.0);

        when(mascotaDAOMock.obtenerPorId(mascotaId)).thenReturn(mascota);

        HistorialPeso registro = new HistorialPeso(mascota, peso, LocalDateTime.now(), null);
        registro.setId(1L);

        when(historialPesoDAOMock.obtenerPorMascota(mascotaId)).thenReturn(List.of(registro));

        String resultado = service.registrarPeso(mascotaId, peso, LocalDateTime.now(), null);

        assertNull(resultado);
        assertEquals(15.5, mascota.getPeso(), 0.01);
        verify(historialPesoDAOMock, times(1)).guardar(any(HistorialPeso.class));
        verify(mascotaDAOMock, times(1)).guardar(mascota);
    }


    @Test
    public void given_multiple_records_when_get_history_then_ordered_descending() {
        Long mascotaId = 1L;
        Mascota mascota = new Mascota();
        mascota.setId(mascotaId);

        // Registros en orden aleatorio
        HistorialPeso registro1 = new HistorialPeso(mascota, 10.0,
                LocalDateTime.of(2025, 1, 1, 10, 0), null);
        HistorialPeso registro2 = new HistorialPeso(mascota, 12.0,
                LocalDateTime.of(2025, 2, 1, 10, 0), null);
        HistorialPeso registro3 = new HistorialPeso(mascota, 11.5,
                LocalDateTime.of(2025, 3, 1, 10, 0), null);

        // Mock retorna lista SIN ordenar
        List<HistorialPeso> historialSinOrdenar = Arrays.asList(registro2, registro1, registro3);
        when(historialPesoDAOMock.obtenerPorMascota(mascotaId)).thenReturn(historialSinOrdenar);

        List<HistorialPeso> resultado = service.obtenerHistorial(mascotaId);

        assertEquals(3, resultado.size());
        // Verificar que está ordenado descendente (más reciente primero)
        assertEquals(11.5, resultado.get(0).getPeso(), 0.01); // Marzo (más reciente)
        assertEquals(12.0, resultado.get(1).getPeso(), 0.01); // Febrero
        assertEquals(10.0, resultado.get(2).getPeso(), 0.01); // Enero (más antiguo)
    }
}


