package com.gestion.mascotas.controlador;

import com.gestion.mascotas.dao.UsuarioDAO;
import com.gestion.mascotas.modelo.entidades.Usuario;

import com.gestion.mascotas.servicio.UsuarioService;
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
    private UsuarioService usuarioService;

    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
    private static final Pattern PHONE_PATTERN =
            Pattern.compile("^[0-9]{10}$");

    @Override
    public void init() {
        usuarioDAO = new UsuarioDAO();
        usuarioService = new UsuarioService();
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

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("usuario") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        String action = request.getParameter("action");
        Usuario usuarioActual = (Usuario) session.getAttribute("usuario");

        if ("actualizar".equals(action)) {
            actualizarPerfil(request, response, usuarioActual);
        } else if ("eliminar".equals(action)) {
            boolean exito = usuarioService.eliminarPerfil(usuarioActual.getId());

            if (exito) {
                session.invalidate();
                response.sendRedirect(request.getContextPath() + "/login?success=perfil_eliminado");
            } else {
                response.sendRedirect(request.getContextPath() + "/dashboard?error=eliminacion_fallida");
            }
        }
    }

    private void actualizarPerfil(HttpServletRequest request, HttpServletResponse response, Usuario usuarioActual) throws IOException {
        String nombre = request.getParameter("nombre");
        String email = request.getParameter("email");
        String telefono = request.getParameter("telefono");
        String contrasenaActual = request.getParameter("contrasenaActual");
        String nuevaContrasena = request.getParameter("nuevaContrasena");
        String confirmarNuevaContrasena = request.getParameter("confirmarNuevaContrasena");

        try {
            usuarioService.editarPerfil(usuarioActual, nombre, email, telefono, contrasenaActual, nuevaContrasena, confirmarNuevaContrasena);

            // Actualizar la sesión y redirigir con mensaje de éxito
            request.getSession().setAttribute("usuario", usuarioActual);
            request.getSession().setAttribute("success", "✅ Perfil actualizado exitosamente.");

        } catch (IllegalArgumentException e) {
            // Error de validación o contraseña incorrecta
            request.getSession().setAttribute("error", e.getMessage());
        } catch (Exception e) {
            // Otro error inesperado
            request.getSession().setAttribute("error", "Error en el servidor al actualizar el perfil.");
            e.printStackTrace();
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
}