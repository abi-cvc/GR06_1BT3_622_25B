package com.gestion.mascotas.servicio;

import com.gestion.mascotas.dao.MascotaDAO;
import com.gestion.mascotas.dao.UsuarioDAO;
import com.gestion.mascotas.modelo.entidades.Mascota;
import com.gestion.mascotas.modelo.entidades.Usuario;
import com.gestion.mascotas.modelo.enums.TipoMascota;

import java.util.List;

public class MascotaService {

    private final MascotaDAO mascotaDAO;
    private final UsuarioDAO usuarioDAO;

    public MascotaService() {
        this.mascotaDAO = new MascotaDAO();
        this.usuarioDAO = new UsuarioDAO();
    }

    public MascotaService(MascotaDAO mascotaDAO, UsuarioDAO usuarioDAO) {
        this.mascotaDAO = mascotaDAO;
        this.usuarioDAO = usuarioDAO;
    }

    /**
     * Registra una nueva mascota para un usuario. (registrarMascota)
     */
    public Mascota registrarMascota(String nombre, TipoMascota tipo, String raza, Integer edad, Double peso, String color, Long usuarioId) {
        validarDatosMascotaDetallado(nombre, tipo, raza, edad, peso, color);

        Usuario propietario = usuarioDAO.obtenerPorId(usuarioId);
        if (propietario == null) {
            throw new IllegalArgumentException("El usuario propietario no existe.");
        }

        Mascota nuevaMascota = new Mascota();
        nuevaMascota.setNombre(nombre.trim());
        nuevaMascota.setTipo(tipo);
        nuevaMascota.setRaza(raza.trim());
        nuevaMascota.setEdad(edad);
        nuevaMascota.setPeso(peso);
        nuevaMascota.setColor(color != null ? color.trim() : null);
        nuevaMascota.setUsuario(propietario);

        mascotaDAO.guardar(nuevaMascota);
        return nuevaMascota;
    }

    /**
     * Consulta los datos de una mascota específica. (consultarDatos)
     */
    public Mascota consultarDatos(Long mascotaId) {
        return mascotaDAO.obtenerPorId(mascotaId);
    }

    /**
     * Actualiza los datos de una mascota existente. (actualizarDatos)
     */
    public void actualizarDatos(Long mascotaId, String nombre, TipoMascota tipo, String raza, Integer edad, Double peso, String color, Usuario usuarioActual) {
        validarDatosMascotaDetallado(nombre, tipo, raza, edad, peso, color);

        Mascota mascota = mascotaDAO.obtenerPorId(mascotaId);
        if (mascota == null) {
            throw new IllegalArgumentException("No se encontró la mascota a actualizar.");
        }

        // Verificación de permiso: la mascota debe pertenecer al usuario que la edita.
        validarPropietarioDeMascota(mascota, usuarioActual, "No tienes permiso para editar esta mascota.");

        mascota.setNombre(nombre.trim());
        mascota.setTipo(tipo);
        mascota.setRaza(raza.trim());
        mascota.setEdad(edad);
        mascota.setPeso(peso);
        mascota.setColor(color != null ? color.trim() : null);

        mascotaDAO.guardar(mascota);
    }

    /**
     * Elimina una mascota. (eliminarMascota)
     */
    public void eliminarMascota(Long mascotaId, Usuario usuarioActual) {
        Mascota mascota = mascotaDAO.obtenerPorId(mascotaId);
        if (mascota == null) {
            throw new IllegalArgumentException("No se encontró la mascota a eliminar.");
        }

        // Verificación de permiso
        validarPropietarioDeMascota(mascota, usuarioActual, "No tienes permiso para eliminar esta mascota.");

        mascotaDAO.eliminar(mascotaId);
    }

    private void validarPropietarioDeMascota(Mascota mascota, Usuario usuarioActual, String s) {
        if (!mascota.getUsuario().getId().equals(usuarioActual.getId())) {
            throw new SecurityException(s);
        }
    }

    /**
     * Lista todas las mascotas de un usuario.
     */
    public List<Mascota> listarMascotasPorUsuario(Long usuarioId) {
        return mascotaDAO.obtenerMascotasPorUsuario(usuarioId);
    }

    /**
     * Realiza validaciones detalladas sobre los datos de una mascota.
     * Define límites razonables para edad y peso.
     *
     * @param nombre Nombre de la mascota.
     * @param tipo Tipo de mascota (PERRO/GATO).
     * @param raza Raza de la mascota.
     * @param edad Edad en años (Integer).
     * @param peso Peso en kg (Double).
     * @param color Color de la mascota (opcional).
     * @return Un String con el primer mensaje de error encontrado, o null si todo es válido.
     */
    public String validarDatosMascotaDetallado(String nombre, TipoMascota tipo, String raza, Integer edad, Double peso, String color) {
        // Validación de Nombre
        if (nombre == null || nombre.trim().isEmpty()) {
            return "El nombre es obligatorio.";
        }
        if (nombre.trim().length() < 2 || nombre.trim().length() > 50) {
            return "El nombre debe tener entre 2 y 50 caracteres.";
        }

        // Validación de Tipo
        if (tipo == null) {
            return "El tipo de mascota es obligatorio.";
        }

        // Validación de Raza
        if (raza == null || raza.trim().isEmpty()) {
            return "La raza es obligatoria.";
        }
        if (raza.trim().length() < 2 || raza.trim().length() > 50) {
            return "La raza debe tener entre 2 y 50 caracteres.";
        }

        // Validación de Edad (según los tests)
        if (edad == null) {
            return "La edad es obligatoria."; // Mensaje esperado por el test @NullSource
        }
        if (edad < 0) {
            // Mensaje ajustado para pasar el test
            return "La edad debe ser positiva o cero.";
        }
        if (edad > 30) {
            // Mensaje ajustado para pasar el test
            return "La edad excede el límite razonable (30 años).";
        }

        // Validación de Peso (según los tests)
        if (peso == null) {
            return "El peso es obligatorio."; // Mensaje esperado por el test @NullSource
        }
        if (peso <= 0) {
            // Mensaje ajustado para pasar los tests (cero y negativo)
            return "El peso debe ser mayor a cero.";
        }
        if (peso > 150) {
            // Mensaje ajustado para pasar el test
            return "El peso excede el límite razonable (150 kg).";
        }

        // Validación de Color (opcional)
        if (color != null && color.trim().length() > 30) {
            return "El color no debe exceder los 30 caracteres.";
        }

        // Si todas las validaciones pasan
        return null;
    }
}
