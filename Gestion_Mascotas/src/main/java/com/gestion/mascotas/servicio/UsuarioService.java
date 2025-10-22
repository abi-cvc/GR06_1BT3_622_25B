package com.gestion.mascotas.servicio;

import com.gestion.mascotas.dao.UsuarioDAO;
import com.gestion.mascotas.modelo.entidades.Usuario;
import com.gestion.mascotas.util.NormalizadorDatos;

import java.util.regex.Pattern;

public class UsuarioService {

    private final UsuarioDAO usuarioDAO = new UsuarioDAO();
    private final NormalizadorDatos normalizador =  new NormalizadorDatos();
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
    private static final Pattern USERNAME_PATTERN = Pattern.compile("^[a-zA-Z0-9_-]{3,50}$");
    private static final Pattern PHONE_PATTERN = Pattern.compile("^[0-9]{10}$");

    /**
     * Crea un nuevo perfil de usuario después de validar los datos.
     * @return El usuario creado si los datos son válidos y la creación es exitosa.
     * @throws IllegalArgumentException si los datos de entrada no son válidos.
     */
    public Usuario crearPerfil(String nombreUsuario, String nombre, String email, String telefono, String contrasena, String confirmarContrasena) {
        String error = validarDatosRegistro(nombreUsuario, nombre, email, telefono, contrasena, confirmarContrasena);
        if (error != null) {
            throw new IllegalArgumentException(error);
        }

        String nombreUsuarioNorm = nombreUsuario.trim().toLowerCase();
        String nombreNorm = NormalizadorDatos.normalizarNombre(nombre);
        String emailNorm = NormalizadorDatos.normalizarEmail(email);
        String telefonoNorm = NormalizadorDatos.normalizarTelefono(telefono);


        Usuario nuevoUsuario = new Usuario(nombreUsuarioNorm, nombreNorm, emailNorm, telefonoNorm, contrasena);

        if (usuarioDAO.crearUsuario(nuevoUsuario)) {
            return nuevoUsuario;
        }
        return null;
    }

    /**
     * Inicia la sesión de un usuario.
     * @param nombreUsuario El nombre de usuario.
     * @param contrasena La contraseña del usuario.
     * @return El usuario si las credenciales son correctas, de lo contrario null.
     */
    public Usuario iniciarSesion(String nombreUsuario, String contrasena) {
        return usuarioDAO.validarLogin(nombreUsuario, contrasena);
    }

    /**
     * Edita el perfil de un usuario existente después de validar los datos.
     * @throws IllegalArgumentException si los datos de entrada no son válidos o la contraseña actual es incorrecta.
     * @throws IllegalStateException si ocurre un error al guardar en la base de datos.
     */
    public void editarPerfil(Usuario usuario, String nombre, String email, String telefono, String contrasenaActual, String nuevaContrasena, String confirmarNuevaContrasena) {
        // 1. Validar datos del formulario
        String errorValidacion = validarDatosActualizacion(nombre, email, telefono, contrasenaActual, nuevaContrasena, confirmarNuevaContrasena);
        if (errorValidacion != null) {
            throw new IllegalArgumentException(errorValidacion);
        }

        // 2. Verificar contraseña actual
        if (usuarioDAO.validarLogin(usuario.getNombreUsuario(), contrasenaActual) == null) {
            throw new IllegalArgumentException("La contraseña actual es incorrecta.");
        }

        // 3. Verificar si el nuevo email ya está en uso por otro usuario
        if (!email.trim().equalsIgnoreCase(usuario.getEmail())) {
            Usuario otroUsuario = usuarioDAO.buscarPorEmail(email.trim().toLowerCase());
            if (otroUsuario != null && !otroUsuario.getId().equals(usuario.getId())) {
                throw new IllegalArgumentException("El correo electrónico ya está en uso por otra cuenta.");
            }
        }

        // 4. Actualizar los datos del objeto usuario
        usuario.setNombre(nombre.trim());
        usuario.setEmail(email.trim().toLowerCase());
        usuario.setTelefono((telefono != null && !telefono.trim().isEmpty()) ? telefono.trim() : null);
        if (nuevaContrasena != null && !nuevaContrasena.isEmpty()) {
            usuario.setContrasena(nuevaContrasena);
        }

        // 5. Guardar en la base de datos
        if (!usuarioDAO.actualizarUsuario(usuario)) {
            throw new IllegalStateException("Error al actualizar el perfil en la base de datos.");
        }
    }

    /**
     * Elimina el perfil de un usuario.
     * @param idUsuario El ID del usuario a eliminar.
     * @return true si la eliminación fue exitosa, false en caso contrario.
     */
    public boolean eliminarPerfil(Long idUsuario) {
        return usuarioDAO.eliminarUsuario(idUsuario);
    }

    /**
     * Cierra la sesión del usuario.
     * Este método es manejado principalmente por el servlet invalidando la sesión.
     * Se puede agregar lógica adicional aquí si es necesario (ej. registrar logout).
     */
    public void cerrarSesion() {
        // Lógica de logout adicional sí se requiere, por ejemplo, registrar el evento.
        System.out.println("Cerrando sesión...");
    }


    /**
     * Válida los datos para el registro de un nuevo usuario.
     * @return Un String con el mensaje de error, o null si los datos son válidos.
     */
    private String validarDatosRegistro(String nombreUsuario, String nombre, String email, String telefono, String contrasena, String confirmarContrasena) {
        if (nombreUsuario == null || !USERNAME_PATTERN.matcher(nombreUsuario.trim()).matches()) {
            return "El nombre de usuario es inválido (3-50 caracteres alfanuméricos).";
        }
        if (nombre == null || nombre.trim().length() < 2) {
            return "El nombre completo es obligatorio.";
        }
        if (email == null || !EMAIL_PATTERN.matcher(email.trim()).matches()) {
            return "El formato del correo electrónico no es válido.";
        }
        if (telefono != null && !telefono.trim().isEmpty() && !PHONE_PATTERN.matcher(telefono.trim()).matches()) {
            return "El teléfono debe contener 10 dígitos.";
        }
        if (contrasena == null || contrasena.length() < 6) {
            return "La contraseña debe tener al menos 6 caracteres.";
        }
        if (!contrasena.equals(confirmarContrasena)) {
            return "Las contraseñas no coinciden.";
        }

        if (validarFortalezaContrasena(contrasena)) {
            return "Tu contraseña es muy débil. Intenta incluir mayúsculas, minúsculas, números o caracteres especiales.";
        }

        if (usuarioDAO.buscarPorNombreUsuario(nombreUsuario.trim().toLowerCase()) != null) {
            return "El nombre de usuario ya está en uso.";
        }
        if (usuarioDAO.buscarPorEmail(email.trim().toLowerCase()) != null) {
            return "El correo electrónico ya está registrado.";
        }
        return null; // No hay errores
    }

    private boolean validarFortalezaContrasena(String contrasena) {
        // Debe tener al menos una letra
        boolean tieneLetra = contrasena.matches(".*[a-zA-Z].*");

        // Debe tener al menos un número O un caracter especial
        boolean tieneNumeroOEspecial = contrasena.matches(".*[0-9].*") ||
                contrasena.matches(".*[!@#$%^&*(),.?\":{}|<>].*");

        return !tieneLetra || !tieneNumeroOEspecial;
    }

    /**
     * Valida los datos para la actualización de un perfil.
     * @return Un String con el mensaje de error, o null si los datos son válidos.
     */
    private String validarDatosActualizacion(String nombre, String email, String telefono, String contrasenaActual, String nuevaContrasena, String confirmarNuevaContrasena) {
        if (nombre == null || nombre.trim().isEmpty()) {
            return "El nombre completo es obligatorio.";
        }
        if (email == null || !EMAIL_PATTERN.matcher(email.trim()).matches()) {
            return "El formato del correo electrónico no es válido.";
        }
        if (contrasenaActual == null || contrasenaActual.isEmpty()) {
            return "Debes ingresar tu contraseña actual para guardar los cambios.";
        }
        if (telefono != null && !telefono.trim().isEmpty() && !PHONE_PATTERN.matcher(telefono.trim()).matches()) {
            return "El teléfono debe contener exactamente 10 dígitos.";
        }
        if (nuevaContrasena != null && !nuevaContrasena.isEmpty()) {
            if (nuevaContrasena.length() < 6) {
                return "La nueva contraseña debe tener al menos 6 caracteres.";
            }

            if (!nuevaContrasena.equals(confirmarNuevaContrasena)) {
                return "Las nuevas contraseñas no coinciden.";
            }

            if (validarFortalezaContrasena(nuevaContrasena)){
                return "Tu contraseña es muy débil. Intenta incluir mayúsculas, minúsculas, números o caracteres especiales.";
            }
        }
        return null; // No hay errores
    }
}