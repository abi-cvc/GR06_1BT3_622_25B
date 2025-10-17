package com.gestion.mascotas.servicio;

import com.gestion.mascotas.dao.MascotaDAO;
import com.gestion.mascotas.dao.UsuarioDAO;
import com.gestion.mascotas.modelo.entidades.Mascota;
import com.gestion.mascotas.modelo.entidades.Usuario;
import com.gestion.mascotas.modelo.enums.TipoMascota;

import java.util.List;

public class MascotaService {

    private final MascotaDAO mascotaDAO = new MascotaDAO();
    private final UsuarioDAO usuarioDAO = new UsuarioDAO();

    /**
     * Válida los datos de una mascota antes de registrarla o actualizarla.
     */
    private void validarDatosMascota(String nombre, TipoMascota tipo, String raza, Integer edad, Double peso) {
        if (nombre == null || nombre.trim().isEmpty() || raza == null || raza.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre, tipo y raza de la mascota son obligatorios.");
        }
        if (edad == null || edad < 0) {
            throw new IllegalArgumentException("La edad debe ser un número positivo.");
        }
        if (peso == null || peso <= 0) {
            throw new IllegalArgumentException("El peso debe ser un valor mayor a cero.");
        }
    }

    /**
     * Registra una nueva mascota para un usuario. (registrarMascota)
     */
    public Mascota registrarMascota(String nombre, TipoMascota tipo, String raza, Integer edad, Double peso, String color, Long usuarioId) {
        validarDatosMascota(nombre, tipo, raza, edad, peso);

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
        validarDatosMascota(nombre, tipo, raza, edad, peso);

        Mascota mascota = mascotaDAO.obtenerPorId(mascotaId);
        if (mascota == null) {
            throw new IllegalArgumentException("No se encontró la mascota a actualizar.");
        }

        // Verificación de permiso: la mascota debe pertenecer al usuario que la edita.
        if (!mascota.getUsuario().getId().equals(usuarioActual.getId())) {
            throw new SecurityException("No tienes permiso para editar esta mascota.");
        }

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
        if (!mascota.getUsuario().getId().equals(usuarioActual.getId())) {
            throw new SecurityException("No tienes permiso para eliminar esta mascota.");
        }

        mascotaDAO.eliminar(mascotaId);
    }

    /**
     * Lista todas las mascotas de un usuario.
     */
    public List<Mascota> listarMascotasPorUsuario(Long usuarioId) {
        return mascotaDAO.obtenerMascotasPorUsuario(usuarioId);
    }
}
