package com.gestion.mascotas.modelo;

import com.gestion.mascotas.dao.SugerenciaAlimentacionDAO;
import com.gestion.mascotas.dao.SugerenciaEjercicioDAO;
import com.gestion.mascotas.modelo.Mascota;
import com.gestion.mascotas.modelo.SugerenciaAlimentacion;
import com.gestion.mascotas.modelo.SugerenciaEjercicio;
import com.gestion.mascotas.modelo.TipoMascota;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Clase encargada de analizar los datos de las mascotas y obtener
 * las sugerencias más apropiadas según sus características
 */
public class AnalizadorDatos {

    private SugerenciaAlimentacionDAO sugerenciaAlimentacionDAO;
    private SugerenciaEjercicioDAO sugerenciaEjercicioDAO;

    public AnalizadorDatos() {
        this.sugerenciaAlimentacionDAO = new SugerenciaAlimentacionDAO();
        this.sugerenciaEjercicioDAO = new SugerenciaEjercicioDAO();
    }

    /**
     * Analiza una mascota y obtiene todas las sugerencias recomendadas
     * @param mascota La mascota a analizar
     * @return Map con las listas de sugerencias de alimentación y ejercicio
     */
    public Map<String, List<?>> analizarMascota(Mascota mascota) {
        Map<String, List<?>> resultados = new HashMap<>();

        // Obtener sugerencias de alimentación
        List<SugerenciaAlimentacion> sugerenciasAlimentacion =
                obtenerSugerenciasAlimentacion(mascota);

        // Obtener sugerencias de ejercicio
        List<SugerenciaEjercicio> sugerenciasEjercicio =
                obtenerSugerenciasEjercicio(mascota);

        resultados.put("alimentacion", sugerenciasAlimentacion);
        resultados.put("ejercicio", sugerenciasEjercicio);

        // Log de análisis
        System.out.println("=== ANÁLISIS DE MASCOTA ===");
        System.out.println("Nombre: " + mascota.getNombre());
        System.out.println("Tipo: " + mascota.getTipo());
        System.out.println("Raza: " + mascota.getRaza());
        System.out.println("Edad: " + mascota.getEdad() + " años");
        System.out.println("Peso: " + mascota.getPeso() + " kg");
        System.out.println("Sugerencias de alimentación encontradas: " + sugerenciasAlimentacion.size());
        System.out.println("Sugerencias de ejercicio encontradas: " + sugerenciasEjercicio.size());
        System.out.println("===========================");

        return resultados;
    }

    /**
     * Obtiene sugerencias de alimentación personalizadas para la mascota
     * Implementa estrategia de búsqueda en cascada:
     * 1. Buscar por raza específica
     * 2. Si no hay, buscar genéricas del tipo
     * 3. Si no hay, buscar sin restricciones de edad/peso
     */
    public List<SugerenciaAlimentacion> obtenerSugerenciasAlimentacion(Mascota mascota) {
        List<SugerenciaAlimentacion> sugerencias = new ArrayList<>();

        // Estrategia 1: Búsqueda específica por raza
        if (mascota.getRaza() != null && !mascota.getRaza().trim().isEmpty()) {
            sugerencias = sugerenciaAlimentacionDAO.obtenerPorCriterios(
                    mascota.getTipo(),
                    mascota.getRaza(),
                    mascota.getEdad(),
                    mascota.getPeso()
            );

            if (!sugerencias.isEmpty()) {
                System.out.println("✓ Sugerencias específicas encontradas para raza: " + mascota.getRaza());
                return sugerencias;
            }
        }

        // Estrategia 2: Búsqueda genérica por tipo (sin raza)
        sugerencias = sugerenciaAlimentacionDAO.obtenerPorCriterios(
                mascota.getTipo(),
                null,
                mascota.getEdad(),
                mascota.getPeso()
        );

        if (!sugerencias.isEmpty()) {
            System.out.println("✓ Sugerencias genéricas encontradas para " + mascota.getTipo());
            return sugerencias;
        }

        // Estrategia 3: Búsqueda solo por tipo (sin edad ni peso)
        sugerencias = sugerenciaAlimentacionDAO.obtenerPorCriterios(
                mascota.getTipo(),
                null,
                null,
                null
        );

        if (!sugerencias.isEmpty()) {
            System.out.println("✓ Sugerencias básicas encontradas para " + mascota.getTipo());
        } else {
            System.out.println("⚠ No se encontraron sugerencias de alimentación");
        }

        return sugerencias;
    }

    /**
     * Obtiene sugerencias de ejercicio personalizadas para la mascota
     * Implementa estrategia de búsqueda en cascada similar a alimentación
     */
    public List<SugerenciaEjercicio> obtenerSugerenciasEjercicio(Mascota mascota) {
        List<SugerenciaEjercicio> sugerencias = new ArrayList<>();

        // Estrategia 1: Búsqueda específica por raza
        if (mascota.getRaza() != null && !mascota.getRaza().trim().isEmpty()) {
            sugerencias = sugerenciaEjercicioDAO.obtenerPorCriterios(
                    mascota.getTipo(),
                    mascota.getRaza(),
                    mascota.getEdad(),
                    mascota.getPeso()
            );

            if (!sugerencias.isEmpty()) {
                System.out.println("✓ Sugerencias de ejercicio específicas para raza: " + mascota.getRaza());
                return sugerencias;
            }
        }

        // Estrategia 2: Búsqueda genérica por tipo (sin raza)
        sugerencias = sugerenciaEjercicioDAO.obtenerPorCriterios(
                mascota.getTipo(),
                null,
                mascota.getEdad(),
                mascota.getPeso()
        );

        if (!sugerencias.isEmpty()) {
            System.out.println("✓ Sugerencias de ejercicio genéricas para " + mascota.getTipo());
            return sugerencias;
        }

        // Estrategia 3: Búsqueda solo por tipo (sin edad ni peso)
        sugerencias = sugerenciaEjercicioDAO.obtenerPorCriterios(
                mascota.getTipo(),
                null,
                null,
                null
        );

        if (!sugerencias.isEmpty()) {
            System.out.println("✓ Sugerencias de ejercicio básicas para " + mascota.getTipo());
        } else {
            System.out.println("⚠ No se encontraron sugerencias de ejercicio");
        }

        return sugerencias;
    }

    /**
     * Calcula el nivel de actividad recomendado basado en la edad de la mascota
     */
    public String calcularNivelActividadRecomendado(Mascota mascota) {
        if (mascota.getEdad() == null) {
            return "MEDIA";
        }

        if (mascota.getTipo() == TipoMascota.PERRO) {
            if (mascota.getEdad() < 2) {
                return "MEDIA"; // Cachorros necesitan actividad moderada
            } else if (mascota.getEdad() < 8) {
                return "ALTA"; // Adultos necesitan más actividad
            } else {
                return "BAJA"; // Seniors necesitan actividad reducida
            }
        } else if (mascota.getTipo() == TipoMascota.GATO) {
            if (mascota.getEdad() < 2) {
                return "MEDIA";
            } else if (mascota.getEdad() < 10) {
                return "MEDIA";
            } else {
                return "BAJA";
            }
        }

        return "MEDIA";
    }

    /**
     * Calcula las calorías diarias recomendadas basadas en peso y edad
     * Fórmula simplificada: peso * factor de actividad
     */
    public Integer calcularCaloriasRecomendadas(Mascota mascota) {
        if (mascota.getPeso() == null) {
            return null;
        }

        double peso = mascota.getPeso();
        String nivelActividad = calcularNivelActividadRecomendado(mascota);

        // Factores según nivel de actividad
        double factor = 30.0; // Por defecto
        switch (nivelActividad) {
            case "BAJA":
                factor = 25.0;
                break;
            case "MEDIA":
                factor = 30.0;
                break;
            case "ALTA":
                factor = 35.0;
                break;
        }

        return (int) (peso * factor);
    }

    /**
     * Genera un reporte de análisis de la mascota
     */
    public String generarReporteAnalisis(Mascota mascota) {
        StringBuilder reporte = new StringBuilder();

        reporte.append("========== REPORTE DE ANÁLISIS ==========\n");
        reporte.append("Mascota: ").append(mascota.getNombre()).append("\n");
        reporte.append("Tipo: ").append(mascota.getTipo()).append("\n");
        reporte.append("Raza: ").append(mascota.getRaza() != null ? mascota.getRaza() : "No especificada").append("\n");
        reporte.append("Edad: ").append(mascota.getEdad()).append(" años\n");
        reporte.append("Peso: ").append(mascota.getPeso()).append(" kg\n");
        reporte.append("-----------------------------------------\n");
        reporte.append("Nivel de actividad recomendado: ").append(calcularNivelActividadRecomendado(mascota)).append("\n");
        reporte.append("Calorías diarias recomendadas: ").append(calcularCaloriasRecomendadas(mascota)).append(" kcal\n");
        reporte.append("=========================================\n");

        return reporte.toString();
    }

    /**
     * Verifica si una mascota necesita atención especial basada en su edad y peso
     */
    public boolean necesitaAtencionEspecial(Mascota mascota) {
        // Mascotas muy jóvenes o muy viejas necesitan atención especial
        if (mascota.getEdad() != null) {
            if (mascota.getEdad() < 1 || mascota.getEdad() > 10) {
                return true;
            }
        }

        // Verificar peso extremo (muy bajo o muy alto para el tipo)
        if (mascota.getPeso() != null) {
            if (mascota.getTipo() == TipoMascota.PERRO) {
                if (mascota.getPeso() < 2.0 || mascota.getPeso() > 50.0) {
                    return true;
                }
            } else if (mascota.getTipo() == TipoMascota.GATO) {
                if (mascota.getPeso() < 2.0 || mascota.getPeso() > 10.0) {
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * Obtiene recomendaciones priorizadas (las más relevantes primero)
     */
    public Map<String, List<?>> obtenerRecomendacionesPriorizadas(Mascota mascota) {
        Map<String, List<?>> resultados = analizarMascota(mascota);

        // Aquí podrías implementar lógica adicional de priorización
        // Por ejemplo, ordenar por relevancia, fecha, etc.

        return resultados;
    }
}