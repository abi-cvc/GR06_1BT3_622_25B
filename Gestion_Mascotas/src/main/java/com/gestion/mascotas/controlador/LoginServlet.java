package com.gestion.mascotas.controlador;

import com.gestion.mascotas.dao.UsuarioDAO;
import com.gestion.mascotas.modelo.Usuario;

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

    private UsuarioDAO usuarioDAO;

    @Override
    public void init() {
        usuarioDAO = new UsuarioDAO();
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
            procesarLogin(request, response);
        } else if ("logout".equals(action)) {
            procesarLogout(request, response);
        } else {
            response.sendRedirect(request.getContextPath() + "/login");
        }
    }

    /**
     * Procesar el inicio de sesión
     */
    private void procesarLogin(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String nombreUsuario = request.getParameter("nombreUsuario");
        String contrasena = request.getParameter("contrasena");
        String recordar = request.getParameter("recordar");

        // Validación básica
        if (nombreUsuario == null || nombreUsuario.trim().isEmpty() ||
                contrasena == null || contrasena.isEmpty()) {
            request.setAttribute("error", "Por favor ingresa tu usuario y contraseña.");
            request.getRequestDispatcher("/jsp/login.jsp").forward(request, response);
            return;
        }

        // Normalizar nombre de usuario (convertir a minúsculas)
        nombreUsuario = nombreUsuario.trim().toLowerCase();

        try {
            // Validar credenciales
            Usuario usuario = usuarioDAO.validarLogin(nombreUsuario, contrasena);

            if (usuario != null) {
                // Login exitoso - crear sesión
                HttpSession session = request.getSession();
                session.setAttribute("usuario", usuario);
                session.setAttribute("usuarioId", usuario.getId());
                session.setAttribute("nombreUsuario", usuario.getNombreUsuario());
                session.setAttribute("nombreCompleto", usuario.getNombre());
                session.setAttribute("email", usuario.getEmail());
                
                // Configurar tiempo de sesión
                if ("on".equals(recordar)) {
                    // Recordar por 30 días
                    session.setMaxInactiveInterval(30 * 24 * 60 * 60);
                } else {
                    // Sesión estándar de 2 horas
                    session.setMaxInactiveInterval(2 * 60 * 60);
                }

                // Redirigir al dashboard
                response.sendRedirect(request.getContextPath() + "/dashboard");
            } else {
                // Login fallido
                request.setAttribute("error", 
                    "Usuario o contraseña incorrectos. Por favor verifica tus credenciales.");
                request.getRequestDispatcher("/jsp/login.jsp").forward(request, response);
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", 
                "Error en el servidor. Por favor intenta más tarde.");
            request.getRequestDispatcher("/jsp/login.jsp").forward(request, response);
        }
    }

    /**
     * Procesar el cierre de sesión
     */
    private void procesarLogout(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        HttpSession session = request.getSession(false);
        if (session != null) {
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