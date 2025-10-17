package com.gestion.mascotas.controlador;

import com.gestion.mascotas.dao.UsuarioDAO;
import com.gestion.mascotas.modelo.entidades.Usuario;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.regex.Pattern;

/**
 * Servlet para manejar actualizaciones y eliminación del perfil de usuario
 */
@WebServlet("/perfil")
public class PerfilServlet extends HttpServlet {

    private UsuarioDAO usuarioDAO;

    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
    private static final Pattern PHONE_PATTERN =
            Pattern.compile("^[0-9]{10}$");

    @Override
    public void init() {
        usuarioDAO = new UsuarioDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Verificar autenticación
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("usuario") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        // Redirigir al dashboard (el perfil se edita desde el dashboard)
        response.sendRedirect(request.getContextPath() + "/dashboard");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Verificar autenticación
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("usuario") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        String action = request.getParameter("action");

        if ("actualizar".equals(action)) {
            actualizarPerfil(request, response);
        } else if ("eliminar".equals(action)) {
            eliminarPerfil(request, response);
        } else {
            response.sendRedirect(request.getContextPath() + "/dashboard");
        }
    }

    /**
     * Actualizar información del perfil del usuario
     */
    private void actualizarPerfil(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        Usuario usuario = (Usuario) session.getAttribute("usuario");

        // Obtener datos del formulario
        String nombre = request.getParameter("nombre");
        String email = request.getParameter("email");
        String telefono = request.getParameter("telefono");
        String contrasenaActual = request.getParameter("contrasenaActual");
        String nuevaContrasena = request.getParameter("nuevaContrasena");
        String confirmarNuevaContrasena = request.getParameter("confirmarNuevaContrasena");

        // Validar datos básicos
        String errorValidacion = validarDatosActualizacion(nombre, email, telefono,
                contrasenaActual, nuevaContrasena,
                confirmarNuevaContrasena);

        if (errorValidacion != null) {
            session.setAttribute("error", errorValidacion);
            response.sendRedirect(request.getContextPath() + "/dashboard");
            return;
        }

        try {
            // Verificar contraseña actual
            Usuario usuarioVerificado = usuarioDAO.validarLogin(usuario.getNombreUsuario(), contrasenaActual);

            if (usuarioVerificado == null) {
                session.setAttribute("error", "La contraseña actual es incorrecta.");
                response.sendRedirect(request.getContextPath() + "/dashboard");
                return;
            }

            // Normalizar datos
            nombre = nombre.trim();
            email = email.trim().toLowerCase();
            telefono = (telefono != null && !telefono.trim().isEmpty()) ? telefono.trim() : null;

            // Verificar si el email cambió y ya está en uso por otro usuario
            if (!email.equals(usuario.getEmail())) {
                Usuario usuarioConEmail = usuarioDAO.buscarPorEmail(email);
                if (usuarioConEmail != null && !usuarioConEmail.getId().equals(usuario.getId())) {
                    session.setAttribute("error",
                            "El correo electrónico '" + email + "' ya está en uso por otro usuario.");
                    response.sendRedirect(request.getContextPath() + "/dashboard");
                    return;
                }
            }

            // Actualizar datos del usuario
            usuario.setNombre(nombre);
            usuario.setEmail(email);
            usuario.setTelefono(telefono);

            // Cambiar contraseña si se solicitó
            if (nuevaContrasena != null && !nuevaContrasena.trim().isEmpty()) {
                usuario.setContrasena(nuevaContrasena);
            }

            // Guardar en base de datos
            boolean actualizado = usuarioDAO.actualizarUsuario(usuario);

            if (actualizado) {
                // Actualizar datos en sesión
                session.setAttribute("usuario", usuario);
                session.setAttribute("nombreCompleto", usuario.getNombre());
                session.setAttribute("email", usuario.getEmail());

                session.setAttribute("success",
                        "✅ Perfil actualizado exitosamente.");
            } else {
                session.setAttribute("error",
                        "Error al actualizar el perfil. Por favor intenta nuevamente.");
            }

        } catch (Exception e) {
            e.printStackTrace();
            session.setAttribute("error",
                    "Error en el servidor: " + e.getMessage());
        }

        response.sendRedirect(request.getContextPath() + "/dashboard");
    }

    /**
     * Eliminar el perfil del usuario y todos sus datos
     */
    private void eliminarPerfil(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        Usuario usuario = (Usuario) session.getAttribute("usuario");

        String contrasena = request.getParameter("contrasena");

        // Validar contraseña
        if (contrasena == null || contrasena.trim().isEmpty()) {
            session.setAttribute("error", "Debes ingresar tu contraseña para eliminar tu perfil.");
            response.sendRedirect(request.getContextPath() + "/dashboard");
            return;
        }

        try {
            // Verificar contraseña
            Usuario usuarioVerificado = usuarioDAO.validarLogin(usuario.getNombreUsuario(), contrasena);

            if (usuarioVerificado == null) {
                session.setAttribute("error", "La contraseña es incorrecta.");
                response.sendRedirect(request.getContextPath() + "/dashboard");
                return;
            }

            // Eliminar usuario (cascade eliminará mascotas, vacunas, visitas)
            boolean eliminado = usuarioDAO.eliminarUsuario(usuario.getId());

            if (eliminado) {
                // Invalidar sesión
                session.invalidate();

                // Crear nueva sesión con mensaje
                HttpSession nuevaSesion = request.getSession();
                nuevaSesion.setAttribute("eliminacionExitosa", true);
                nuevaSesion.setAttribute("mensajeEliminacion",
                        "Tu perfil ha sido eliminado exitosamente. ¡Esperamos verte de nuevo!");

                response.sendRedirect(request.getContextPath() + "/login");
            } else {
                session.setAttribute("error",
                        "Error al eliminar el perfil. Por favor intenta nuevamente.");
                response.sendRedirect(request.getContextPath() + "/dashboard");
            }

        } catch (Exception e) {
            e.printStackTrace();
            session.setAttribute("error",
                    "Error en el servidor: " + e.getMessage());
            response.sendRedirect(request.getContextPath() + "/dashboard");
        }
    }

    /**
     * Validar datos de actualización
     */
    private String validarDatosActualizacion(String nombre, String email, String telefono,
                                             String contrasenaActual, String nuevaContrasena,
                                             String confirmarNuevaContrasena) {

        // Validar campos obligatorios
        if (nombre == null || nombre.trim().isEmpty()) {
            return "El nombre completo es obligatorio.";
        }

        if (email == null || email.trim().isEmpty()) {
            return "El correo electrónico es obligatorio.";
        }

        if (contrasenaActual == null || contrasenaActual.isEmpty()) {
            return "Debes ingresar tu contraseña actual para guardar cambios.";
        }

        // Validar longitud del nombre
        if (nombre.trim().length() < 2 || nombre.trim().length() > 100) {
            return "El nombre debe tener entre 2 y 100 caracteres.";
        }

        // Validar formato de email
        if (!EMAIL_PATTERN.matcher(email.trim()).matches()) {
            return "El formato del correo electrónico no es válido.";
        }

        // Validar teléfono (si se proporciona)
        if (telefono != null && !telefono.trim().isEmpty()) {
            if (!PHONE_PATTERN.matcher(telefono.trim()).matches()) {
                return "El teléfono debe contener exactamente 10 dígitos numéricos.";
            }
        }

        // Validar cambio de contraseña (si se solicitó)
        if (nuevaContrasena != null && !nuevaContrasena.trim().isEmpty()) {
            if (nuevaContrasena.length() < 6) {
                return "La nueva contraseña debe tener al menos 6 caracteres.";
            }

            if (nuevaContrasena.length() > 100) {
                return "La nueva contraseña no puede tener más de 100 caracteres.";
            }

            if (confirmarNuevaContrasena == null || !nuevaContrasena.equals(confirmarNuevaContrasena)) {
                return "Las contraseñas nuevas no coinciden.";
            }
        }

        return null; // Sin errores
    }
}