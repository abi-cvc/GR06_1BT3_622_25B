package com.gestion.mascotas.servicio;

import com.gestion.mascotas.dao.MascotaDAO;
import com.gestion.mascotas.modelo.entidades.Mascota;
import com.gestion.mascotas.modelo.entidades.Usuario;
import com.gestion.mascotas.util.ValidadorRecordatorio;

public abstract class RecordatorioService {

    protected MascotaDAO mascotaDAO;

    public RecordatorioService() {
        this.mascotaDAO = new MascotaDAO();
    }

    // Constructor para Inyección de Dependencias / Tests
    public RecordatorioService(MascotaDAO mascotaDAO) {
        this.mascotaDAO = mascotaDAO;
    }
    protected Mascota validarYObtenerMascota(String mascotaIdStr, Usuario usuario) {
        // Validar ID de mascota
        String error = ValidadorRecordatorio.validarMascotaId(mascotaIdStr);
        if (error != null) {
            throw new IllegalArgumentException(error);
        }

        Long mascotaId = Long.parseLong(mascotaIdStr);

        // Obtener mascota
        Mascota mascota = mascotaDAO.obtenerPorId(mascotaId);

        // Validar que existe
        error = ValidadorRecordatorio.validarMascotaExiste(mascota);
        if (error != null) {
            throw new IllegalArgumentException(error);
        }

        // Validar que pertenece al usuario
        error = ValidadorRecordatorio.validarPerteneceUsuario(mascota, usuario);
        if (error != null) {
            throw new IllegalArgumentException(error);
        }

        return mascota;
    }

    protected void validarPermisos(Object recordatorio, Usuario usuario,
                                   java.util.function.Function<Object, Usuario> getUsuario) {
        String error = ValidadorRecordatorio.validarRecordatorioExiste(recordatorio);
        if (error != null) {
            throw new IllegalArgumentException(error);
        }

        Usuario propietario = getUsuario.apply(recordatorio);
        if (!propietario.getId().equals(usuario.getId())) {
            throw new IllegalArgumentException("acceso_denegado");
        }
    }
}