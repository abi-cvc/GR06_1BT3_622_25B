package com.gestion.mascotas.modelo;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase de servicio que se encarga de analizar los datos de una mascota
 * para generar sugerencias de alimentación y ejercicio.
 * Esta clase actúa como intermediario entre los datos de la mascota y las sugerencias.
 */
public class AnalizadorDatos {

    /**
     * Analiza el perfil de una mascota y genera una sugerencia específica.
     *
     * @param mascota La mascota para la cual se generará la sugerencia.
     * @param tipoSugerencia El tipo de sugerencia a generar ("alimentacion" o "ejercicio").
     * @return Un objeto Sugerencia (SugerenciaAlimentacion o SugerenciaEjercicio)
     *         o null si el tipo de sugerencia no es reconocido.
     */
    public Sugerencia analizarPerfilMascota(Mascota mascota, String tipoSugerencia) {
        if (mascota == null) {
            System.err.println("Error: La mascota no puede ser nula para generar sugerencias.");
            return null;
        }

        Sugerencia sugerencia = null;
        switch (tipoSugerencia.toLowerCase()) {
            case "alimentacion":
                sugerencia = new SugerenciaAlimentacion();
                sugerencia.generarSugerencia(mascota);
                break;
            case "ejercicio":
                sugerencia = new SugerenciaEjercicio();
                sugerencia.generarSugerencia(mascota);
                break;
            default:
                System.err.println("Tipo de sugerencia no reconocido: " + tipoSugerencia);
                break;
        }
        return sugerencia;
    }

    /**
     * Genera y devuelve una lista de todas las sugerencias aplicables para una mascota.
     * Actualmente genera sugerencias de alimentación y ejercicio.
     *
     * @param mascota La mascota para la cual se generarán las sugerencias.
     * @return Una lista de objetos Sugerencia.
     */
    public List<Sugerencia> obtenerSugerencias(Mascota mascota) {
        List<Sugerencia> sugerencias = new ArrayList<>();

        SugerenciaAlimentacion sugAlimentacion = (SugerenciaAlimentacion) analizarPerfilMascota(mascota, "alimentacion");
        if (sugAlimentacion != null) {
            sugerencias.add(sugAlimentacion);
        }

        SugerenciaEjercicio sugEjercicio = (SugerenciaEjercicio) analizarPerfilMascota(mascota, "ejercicio");
        if (sugEjercicio != null) {
            sugerencias.add(sugEjercicio);
        }

        return sugerencias;
    }

    /**
     * Método de ejemplo para simular la consulta de datos de visitas veterinarias
     * y generar una sugerencia basada en el historial.
     * (Requiere acceso a VisitaVeterinariaDAO para ser funcional)
     *
     * @param mascota La mascota para la cual se generará la sugerencia.
     * @return Una sugerencia de visita veterinaria si es necesaria, o null.
     */
    public Sugerencia generarSugerenciaVisitaVeterinaria(Mascota mascota) {
        // Lógica de ejemplo: Si no hay visitas en el último año, sugerir una revisión.
        // En un sistema real, esto consultaría el VisitaVeterinariaDAO.
        // List<VisitaVeterinaria> visitasRecientes = visitaVeterinariaDAO.obtenerVisitasRecientes(mascota.getId(), 12);
        // if (visitasRecientes.isEmpty()) {
        Sugerencia sugerencia = new SugerenciaAlimentacion(); // Usamos SugerenciaAlimentacion como base, pero debería ser una nueva clase SugerenciaSalud
        sugerencia.setMascota(mascota);
        sugerencia.setFechaGeneracion(LocalDate.now());
        sugerencia.setTitulo("Sugerencia: Revisión Veterinaria Anual");
        sugerencia.setContenido("Se recomienda programar una revisión veterinaria anual para " + mascota.getNombre() +
                " para asegurar su bienestar general y detectar posibles problemas de salud a tiempo.");
        return sugerencia;
        // }
        // return null;
    }
}