package com.gestion.mascotas.controlador;

import com.gestion.mascotas.modelo.entidades.Usuario;

import com.gestion.mascotas.servicio.UsuarioService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Servlet para manejar actualizaciones y eliminación del perfil de usuario
 */
@WebServlet("/perfil")
public class PerfilServlet extends HttpServlet {

    private UsuarioService usuarioService;

    @Override
    public void init() {
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
}