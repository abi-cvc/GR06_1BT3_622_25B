package com.gestion.mascotas.servicio;

import com.gestion.mascotas.dao.HistorialPesoDAO;
import com.gestion.mascotas.dao.MascotaDAO;
import com.gestion.mascotas.modelo.entidades.HistorialPeso;
import com.gestion.mascotas.modelo.entidades.Mascota;
import com.gestion.mascotas.util.ValidadorPeso;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import java.time.LocalDateTime;

public class HistorialPesoService {

    private final HistorialPesoDAO historialPesoDAO;
    private final MascotaDAO mascotaDAO;
    private final ValidadorPeso validadorPeso;

    /**
     * Constructor con parámetros
     */
    public HistorialPesoService(HistorialPesoDAO historialPesoDAO, MascotaDAO mascotaDAO, ValidadorPeso validadorPeso) {
        this.historialPesoDAO = historialPesoDAO;
        this.mascotaDAO = mascotaDAO;
        this.validadorPeso = validadorPeso;
    }

    /**
     * Constructor sin parámetros
     */
    public HistorialPesoService() {
        this.historialPesoDAO = new HistorialPesoDAO();
        this.mascotaDAO = new MascotaDAO();
        this.validadorPeso = new ValidadorPeso();
    }

    // Registrar nuevo peso
    public String registrarPeso(Long mascotaId, double peso, LocalDateTime fechaRegistro, String comentarios) {
        // Validar peso
        String errorValidacion = validadorPeso.validarPeso(peso);
        if (errorValidacion != null) {
            return errorValidacion;
        }

        // Obtener mascota
        Mascota mascota = mascotaDAO.obtenerPorId(mascotaId);
        if (mascota == null) {
            return "Mascota no encontrada.";
        }

        // Crear y guardar registro
        HistorialPeso registro = new HistorialPeso(mascota, peso, fechaRegistro, comentarios);
        historialPesoDAO.guardar(registro);

        // Actualizar peso actual de la mascota SOLO si este registro es el más reciente
        List<HistorialPeso> historial = historialPesoDAO.obtenerPorMascota(mascotaId);
        HistorialPeso registroMasReciente = historial.stream()
                .max(Comparator.comparing(HistorialPeso::getFechaRegistro))
                .orElse(null);

        if (registroMasReciente != null && registroMasReciente.getId().equals(registro.getId())) {
            // Actualizar peso actual de la mascota
            mascota.setPeso(peso);
            mascotaDAO.guardar(mascota);

        } else if (registroMasReciente != null) {
            // Si hay un registro más reciente, actualizar con ese peso
            mascota.setPeso(registroMasReciente.getPeso());
            mascotaDAO.guardar(mascota);
        }

        return null;
    }

    // Obtener historial de peso ordenado por fecha descendente
    public List<HistorialPeso> obtenerHistorial(Long mascotaId) {
        List<HistorialPeso> historial = historialPesoDAO.obtenerPorMascota(mascotaId);
        return ordenarPorFechaDescendente(historial);
    }

    // Método privado para ordenar por fecha descendente
    private List<HistorialPeso> ordenarPorFechaDescendente(List<HistorialPeso> historial) {
        return historial.stream()
                .sorted(Comparator.comparing(HistorialPeso::getFechaRegistro).reversed())
                .collect(Collectors.toList());
    }


}

