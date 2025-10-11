package com.gestion.mascotas.modelo;

import com.gestion.mascotas.dao.SugerenciaAlimentacionDAO;
import com.gestion.mascotas.dao.SugerenciaEjercicioDAO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Clase encargada de analizar los datos de las mascotas y obtener
 * las sugerencias más apropiadas según sus características
 */
public class AnalizadorDatos {

    private final EstrategiaActividadPerro estrategiaActividadPerro = new EstrategiaActividadPerro();
    public SugerenciaAlimentacionDAO sugerenciaAlimentacionDAO;
    public SugerenciaEjercicioDAO sugerenciaEjercicioDAO;
    private final EstrategiaBusquedaSugerencias estrategiaBusquedaSugerencias;

    public AnalizadorDatos() {
        this.sugerenciaAlimentacionDAO = new SugerenciaAlimentacionDAO();
        this.sugerenciaEjercicioDAO = new SugerenciaEjercicioDAO();
        this.estrategiaBusquedaSugerencias = new EstrategiaBusquedaSugerencias(
                sugerenciaAlimentacionDAO, sugerenciaEjercicioDAO);
    }

    /**
     * Analiza una mascota y obtiene todas las sugerencias recomendadas
     * @param mascota La mascota a analizar
     * @return Map con las listas de sugerencias de alimentación y ejercicio
     */
    public Map<String, List<?>> analizarMascota(Mascota mascota) {
        Map<String, List<?>> resultados = new HashMap<>();
/**
 * Refactorizacion N.-2: Inline Temp, como  las variables sugerenciasAlimentacion y sugerenciasEjercicio
 * solo se usan para pasarle un valor al metodo,entonces las eliminamos y directamente llamamos al metodo
 */

        resultados.put("alimentacion", obtenerSugerenciasAlimentacion(mascota));
        resultados.put("ejercicio", obtenerSugerenciasEjercicio(mascota));

        mostrarResumenAnalisis(mascota, resultados);

        return resultados;
    }

    /**
     * Refactorizacion N.-1: Extract Method,extraemos la funcionalidad de imprimir
     * el log de la mascota  otro metodo para mas dinamismo.
     */
    public void mostrarResumenAnalisis(Mascota mascota,Map<String, List<?>> resultados){
        System.out.println("=== ANÁLISIS DE MASCOTA ===");
        System.out.println("Nombre: " + mascota.getNombre());
        System.out.println("Tipo: " + mascota.getTipo());
        System.out.println("Raza: " + mascota.getRaza());
        System.out.println("Edad: " + mascota.getEdad() + " años");
        System.out.println("Peso: " + mascota.getPeso() + " kg");
        System.out.println("Sugerencias de alimentacion encontradas: " + (resultados.get("alimentacion").size()));
        System.out.println("Sugerencias de ejercicio encontradas: " + (resultados.get("ejercicio").size()));
        System.out.println("===========================");
    }
    /** Refactorizacion N.-4
     * Obtiene sugerencias de alimentación personalizadas para la mascota
     * Implementa estrategia de búsqueda en cascada:
     * 1. Buscar por raza específica
     * 2. Si no hay, buscar genéricas del tipo
     * 3. Si no hay, buscar sin restricciones de edad/peso
     */
    public List<SugerenciaAlimentacion> obtenerSugerenciasAlimentacion(Mascota mascota) {
        List<SugerenciaAlimentacion> sugerencias = new ArrayList<>();

        // Estrategia 1: Búsqueda específica por raza
        sugerencias = estrategiaBusquedaSugerencias.buscarPorRaza(mascota);
        if (!sugerencias.isEmpty()) return sugerencias;

        // Estrategia 2: Búsqueda genérica por tipo (sin raza)
        sugerencias = estrategiaBusquedaSugerencias.buscarPorTipoEdadPeso(mascota);
        if (!sugerencias.isEmpty()) return sugerencias;

        // Estrategia 3: Búsqueda solo por tipo (sin edad ni peso)
        sugerencias = estrategiaBusquedaSugerencias.buscarSoloPorTipo(mascota);
        if (!sugerencias.isEmpty()) return sugerencias;

        return sugerencias;
    }
    /** Refactorizacion N.-5
     * Obtiene sugerencias de ejercicio personalizadas para la mascota
     * Implementa estrategia de búsqueda en cascada similar a alimentación
     */
    public List<SugerenciaEjercicio> obtenerSugerenciasEjercicio(Mascota mascota) {
        List<SugerenciaEjercicio> sugerencias = new ArrayList<>();

        // Estrategia 1: Búsqueda específica por raza
        sugerencias = estrategiaBusquedaSugerencias.buscarPorRazaEjercicio(mascota);
        if (!sugerencias.isEmpty()) return sugerencias;

        // Estrategia 2: Búsqueda genérica por tipo (sin raza)
        sugerencias = estrategiaBusquedaSugerencias.buscarPorTipoEdadPesoEjercicio(mascota);
        if (!sugerencias.isEmpty()) return sugerencias;

        // Estrategia 3: Búsqueda solo por tipo (sin edad ni peso)
        sugerencias = estrategiaBusquedaSugerencias.buscarSoloPorTipoEjercicio(mascota);
        if (!sugerencias.isEmpty()) return sugerencias;
        
        return sugerencias;
    }

    /**
     * Refactorizacion N.-3 y 7, Replace Nested Conditional with Guard Clauses, usamos clausas de guardia para simplificar
     * las condicionales y evitar anidamientos profundos
     */
    public String calcularNivelActividadRecomendado(Mascota mascota) {
        if (mascota.getEdad() == null) return "MEDIA";

        return mascota.getTipo().calcularNivelActividad(mascota.getEdad());
    }

    /**
     * Calcula las calorías diarias recomendadas basadas en peso y nivel de actividad.
     * Refactorización #8: Replace Temp with Query - Se eliminó la variable temporal 'factor'
     * reemplazándola por una llamada al método obtenerCaloriasPorKilo()
     */
    public Integer calcularCaloriasRecomendadas(Mascota mascota) {
        if (mascota.getPeso() == null) {
            return null;
        }

        return (int) (mascota.getPeso() * obtenerCaloriasPorKilo(mascota));
    }

    /**
     * Determina las calorías por kilogramo según el nivel de actividad de la mascota.
     * @param mascota --> mascota a evaluar
     * @return --> Calorías por kg (25 para BAJA, 30 para MEDIA, 35 para ALTA)
     */
    private double obtenerCaloriasPorKilo(Mascota mascota) {
        String nivelActividad = calcularNivelActividadRecomendado(mascota);

        switch (nivelActividad) {
            case "BAJA":
                return 25.0;
            case "MEDIA":
                return 30.0;
            case "ALTA":
                return 35.0;
            default:
                return 30.0;
        }
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
     * Refactorización #9: Replace Method with Method Object - Se delegó la lógica
     * de validación a la clase ValidadorAtencionEspecial.
     */
    public boolean necesitaAtencionEspecial(Mascota mascota) {
        ValidadorAtencionEspecial validador = new ValidadorAtencionEspecial(mascota);
        return validador.necesitaAtencion();
    }

    /**
     * Obtiene recomendaciones priorizadas (las más relevantes primero)
     */
    public Map<String, List<?>> obtenerRecomendacionesPriorizadas(Mascota mascota) {
        Map<String, List<?>> resultados = analizarMascota(mascota);

        return resultados;
    }
}
