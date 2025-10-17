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

/**
 * Servlet para manejar el inicio de sesión y cierre de sesión de usuarios
 */
@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    private UsuarioService usuarioService;


    @Override
    public void init() {
        usuarioService = new UsuarioService();
    }

    /**
     * Mostrar página de login
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Si ya está logueado, redirigir al dashboard
        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("usuario") != null) {
            response.sendRedirect(request.getContextPath() + "/dashboard");
            return;
        }

        // Verificar si viene de un registro exitoso
        session = request.getSession(false);
        if (session != null) {
            if (session.getAttribute("registroExitoso") != null) {
                request.setAttribute("success", session.getAttribute("mensajeRegistro"));
                session.removeAttribute("registroExitoso");
                session.removeAttribute("mensajeRegistro");
            }

            if (session.getAttribute("logoutExitoso") != null) {
                request.setAttribute("success", session.getAttribute("mensajeLogout"));
                session.removeAttribute("logoutExitoso");
                session.removeAttribute("mensajeLogout");
            }

            if (session.getAttribute("eliminacionExitosa") != null) {
                request.setAttribute("success", session.getAttribute("mensajeEliminacion"));
                session.removeAttribute("eliminacionExitosa");
                session.removeAttribute("mensajeEliminacion");
            }
        }

        request.getRequestDispatcher("/jsp/login.jsp").forward(request, response);
    }

    /**
     * Procesar login o logout
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");

        if ("login".equals(action)) {
            String nombreUsuario = request.getParameter("nombreUsuario");
            String contrasena = request.getParameter("contrasena");

            Usuario usuario = usuarioService.iniciarSesion(nombreUsuario, contrasena);
            if (usuario != null) {
                HttpSession session = request.getSession();
                session.setAttribute("usuario", usuario);
                response.sendRedirect(request.getContextPath() + "/dashboard");
            } else {
                request.setAttribute("error", "Usuario o contraseña incorrectos. Por favor, inténtalo de nuevo.");
                request.getRequestDispatcher("/jsp/login.jsp").forward(request, response);
            }
        } else if ("logout".equals(action)) {
            HttpSession session = request.getSession(false);
            if (session != null) {
                usuarioService.cerrarSesion();
                session.invalidate();
                // Crear nueva sesión para el mensaje
                HttpSession newSession = request.getSession();
                newSession.setAttribute("logoutExitoso", true);
                newSession.setAttribute("mensajeLogout",
                        "Has cerrado sesión exitosamente. ¡Hasta pronto!");
            }
            response.sendRedirect(request.getContextPath() + "/login");
        }
    }
}