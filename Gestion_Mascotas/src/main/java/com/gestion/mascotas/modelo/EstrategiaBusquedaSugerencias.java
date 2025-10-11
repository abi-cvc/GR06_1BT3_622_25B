package com.gestion.mascotas.modelo;

import com.gestion.mascotas.dao.SugerenciaAlimentacionDAO;
import com.gestion.mascotas.dao.SugerenciaEjercicioDAO;

import java.util.ArrayList;
import java.util.List;

public class EstrategiaBusquedaSugerencias {
    private final SugerenciaAlimentacionDAO sugerenciaAlimentacionDAO;
    private final SugerenciaEjercicioDAO sugerenciaEjercicioDAO;

    public EstrategiaBusquedaSugerencias(SugerenciaAlimentacionDAO sugerenciaAlimentacionDAO,
                                         SugerenciaEjercicioDAO sugerenciaEjercicioDAO) {
        this.sugerenciaAlimentacionDAO = sugerenciaAlimentacionDAO;
        this.sugerenciaEjercicioDAO = sugerenciaEjercicioDAO;
    }

    /**
     * REFACTORIZACIÓN 4: EXTRACT METHOD
     * Extraemos la logica de busqueda por raza a un metodo aparte para mejorar la legibilidad y mantenimiento del codigo
     **/
    // Estrategia 1: Búsqueda específica por raza
    List<SugerenciaAlimentacion> buscarPorRaza(Mascota mascota) {
        if (mascota.getRaza() == null || mascota.getRaza().trim().isEmpty())
            return new ArrayList<SugerenciaAlimentacion>();
        List<SugerenciaAlimentacion> sugerencias = sugerenciaAlimentacionDAO.obtenerPorCriterios(
                mascota.getTipo(),
                mascota.getRaza(),
                mascota.getEdad(),
                mascota.getPeso()
        );

        if (!sugerencias.isEmpty())
            System.out.println("✓ Sugerencias específicas encontradas para raza: " + mascota.getRaza());

        return sugerencias;
    }

    // Estrategia 2: Búsqueda genérica por tipo (sin raza)
    List<SugerenciaAlimentacion> buscarPorTipoEdadPeso(Mascota mascota) {
        List<SugerenciaAlimentacion> sugerencias = sugerenciaAlimentacionDAO.obtenerPorCriterios(
            mascota.getTipo(),
            null,
            mascota.getEdad(),
            mascota.getPeso()
        );
        if (!sugerencias.isEmpty()) System.out.println("✓ Sugerencias genéricas encontradas para " + mascota.getTipo());
        return sugerencias;
    }

    // Estrategia 3: Búsqueda solo por tipo (sin edad ni peso)
    List<SugerenciaAlimentacion> buscarSoloPorTipo(Mascota mascota) {
        List<SugerenciaAlimentacion> sugerencias = sugerenciaAlimentacionDAO.obtenerPorCriterios(
            mascota.getTipo(),
            null,
            null,
            null
        );
        if (!sugerencias.isEmpty()) System.out.println("✓ Sugerencias básicas encontradas para " + mascota.getTipo());
        else System.out.println("⚠ No se encontraron sugerencias de alimentación");
        return sugerencias;
    }


    /**REFACTORIZACIÓN 5: EXTRACT METHOD
     Extraemos la logica de busqueda a un metodo aparte para mejorar la legibilidad y mantenimiento del codigo
     **/
    // Estrategia 1: Búsqueda específica por raza
    List<SugerenciaEjercicio> buscarPorRazaEjercicio(Mascota mascota) {
        if (mascota.getRaza() != null && !mascota.getRaza().trim().isEmpty()) {
            List<SugerenciaEjercicio> sugerencias = sugerenciaEjercicioDAO.obtenerPorCriterios(
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
        return new ArrayList<>();
    }

    // Estrategia 2: Búsqueda genérica por tipo (sin raza)
    List<SugerenciaEjercicio> buscarPorTipoEdadPesoEjercicio(Mascota mascota) {
        List<SugerenciaEjercicio> sugerencias = sugerenciaEjercicioDAO.obtenerPorCriterios(
                mascota.getTipo(),
                null,
                mascota.getEdad(),
                mascota.getPeso()
        );

        if (!sugerencias.isEmpty()) {
            System.out.println("✓ Sugerencias de ejercicio genéricas para " + mascota.getTipo());
            return sugerencias;
        }
        return new ArrayList<>();
    }

    // Estrategia 3: Búsqueda solo por tipo (sin edad ni peso)
    List<SugerenciaEjercicio> buscarSoloPorTipoEjercicio(Mascota mascota) {
        List<SugerenciaEjercicio> sugerencias = sugerenciaEjercicioDAO.obtenerPorCriterios(
                mascota.getTipo(),
                null,
                null,
                null
        );

        if (!sugerencias.isEmpty()) {
            System.out.println("✓ Sugerencias de ejercicio básicas para " + mascota.getTipo());
            return sugerencias;
        } else {
            System.out.println("⚠ No se encontraron sugerencias de ejercicio");
            return new ArrayList<>();
        }
    }
}