package com.gestion.mascotas.servicio;

import com.gestion.mascotas.dao.MascotaDAO;
import com.gestion.mascotas.dao.VacunaDAO;
import com.gestion.mascotas.modelo.entidades.Mascota;
import com.gestion.mascotas.modelo.entidades.Usuario;
import com.gestion.mascotas.modelo.entidades.Vacuna;

import java.time.LocalDate;
import java.util.List;

public class VacunaService {

    private final VacunaDAO vacunaDAO = new VacunaDAO();
    private final MascotaDAO mascotaDAO = new MascotaDAO();

    /**
     * Válida los datos de entrada para registrar una vacuna.
     * @return Mensaje de error o null si es válido.
     */
    private String validarDatosVacuna(String nombre, LocalDate fecha, Mascota mascota, String nombreVeterinario, LocalDate proximaDosis) {
        if (nombre == null || nombre.trim().isEmpty()) {
            return "El nombre o tipo de vacuna es obligatorio.";
        }
        if (fecha == null) {
            return "La fecha de aplicación/programación es obligatoria.";
        }
        if (mascota == null) {
            return "La mascota asociada no fue encontrada.";
        }

        if (nombreVeterinario != null && nombreVeterinario.length() > 100) {
            return "El nombre del veterinario no puede exceder los 100 caracteres.";
        }

        if (proximaDosis != null && proximaDosis.isBefore(fecha)) {
            return "La fecha de la próxima dosis no puede ser anterior a la fecha de aplicación.";
        }
        return null;
    }

    /**
     * Registra una nueva vacuna para una mascota. (Corresponde a registrarVacuna)
     * @throws IllegalArgumentException si los datos no son válidos o la mascota no existe.
     * @throws SecurityException si el usuario no tiene permiso sobre la mascota.
     * @throws RuntimeException si ocurre un error al guardar.
     */
    public void registrarVacuna(String nombre, LocalDate fecha, String nombreVeterinario, LocalDate proximaDosis, Long mascotaId, Usuario usuarioActual) {
        Mascota mascota = mascotaDAO.obtenerPorId(mascotaId);

        // Validar datos
        String errorValidacion = validarDatosVacuna(nombre, fecha, mascota, nombreVeterinario, proximaDosis);
        if (errorValidacion != null) {
            throw new IllegalArgumentException(errorValidacion);
        }

        // Verificar permiso: La mascota debe pertenecer al usuario actual
        if (!mascota.getUsuario().getId().equals(usuarioActual.getId())) {
            throw new SecurityException("No tienes permiso para registrar vacunas para esta mascota.");
        }

        Vacuna nuevaVacuna = new Vacuna();
        nuevaVacuna.setNombre(nombre.trim());
        nuevaVacuna.setFecha(fecha);
        nuevaVacuna.setMascota(mascota);
        nuevaVacuna.setNombreVeterinario(nombreVeterinario != null ? nombreVeterinario.trim() : null);
        nuevaVacuna.setProximaDosis(proximaDosis);


        try {
            vacunaDAO.guardar(nuevaVacuna);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error al guardar la vacuna en la base de datos.");
        }
    }

    /**
     * Consulta todas las vacunas registradas para las mascotas de un usuario. (Corresponde a consultarVacunas)
     */
    public List<Vacuna> consultarVacunasPorUsuario(Long usuarioId) {
        // Asumiendo que VacunaDAO tiene un método para esto (si no, hay que crearlo)
        // O podríamos obtener las mascotas del usuario y luego las vacunas de cada mascota.
        // Por simplicidad, asumimos que el DAO puede filtrar por usuario (a través de la mascota)
        return vacunaDAO.obtenerPorUsuario(usuarioId); // Necesitarás implementar este método en VacunaDAO
    }

    /**
     * Consulta las vacunas de una mascota específica.
     */
    public List<Vacuna> consultarVacunasPorMascota(Long mascotaId, Usuario usuarioActual) {
        Mascota mascota = mascotaDAO.obtenerPorId(mascotaId);
        if (mascota == null) {
            throw new IllegalArgumentException("Mascota no encontrada.");
        }
        // Verificar permiso
        if (!mascota.getUsuario().getId().equals(usuarioActual.getId())) {
            throw new SecurityException("No tienes permiso para ver las vacunas de esta mascota.");
        }
        return vacunaDAO.obtenerPorMascota(mascotaId); // Este método ya existe en VacunaDAO
    }


    /**
     * Elimina el registro de una vacuna.
     * @throws IllegalArgumentException si la vacuna no existe.
     * @throws SecurityException si el usuario no tiene permiso.
     * @throws RuntimeException si ocurre un error al eliminar.
     */
    public void eliminarVacuna(Long vacunaId, Usuario usuarioActual) {
        Vacuna vacuna = vacunaDAO.obtenerPorId(vacunaId);
        if (vacuna == null) {
            throw new IllegalArgumentException("Registro de vacuna no encontrado.");
        }

        // Verificar permiso
        if (!vacuna.getMascota().getUsuario().getId().equals(usuarioActual.getId())) {
            throw new SecurityException("No tienes permiso para eliminar este registro de vacuna.");
        }

        try {
            vacunaDAO.eliminar(vacunaId);
        } catch (Exception e) {
            // Loggear el error e.printStackTrace();
            throw new RuntimeException("Error al eliminar la vacuna de la base de datos.");
        }
    }

    public boolean validarTipo(String tipoVacuna) {
        // Aquí podrías tener una lista de tipos válidos o alguna lógica más compleja
        return tipoVacuna != null && !tipoVacuna.trim().isEmpty();
    }

    public boolean validarFecha(LocalDate fecha) {
        // Podrías validar que la fecha no sea extremadamente antigua o futura, si aplica
        return fecha != null;
    }
}