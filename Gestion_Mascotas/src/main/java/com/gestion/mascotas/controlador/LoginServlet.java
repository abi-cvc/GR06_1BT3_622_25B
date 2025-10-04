package com.gestionmascotas.app.controller;

import com.gestionmascotas.app.dao.UsuarioDAO;
import com.gestionmascotas.app.model.Usuario;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    private UsuarioDAO usuarioDAO;

    @Override
    public void init() {
        usuarioDAO = new UsuarioDAO();
    }

    // Mostrar página de login
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Si ya está logueado, redirigir al dashboard
        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("usuario") != null) {
            response.sendRedirect(request.getContextPath() + "/dashboard");
            return;
        }

        request.getRequestDispatcher("/index.jsp").forward(request, response);
    }

    // Procesar login
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");

        if ("login".equals(action)) {
            procesarLogin(request, response);
        } else if ("register".equals(action)) {
            procesarRegistro(request, response);
        } else if ("logout".equals(action)) {
            procesarLogout(request, response);
        }
    }

    private void procesarLogin(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String nombreUsuario = request.getParameter("nombreUsuario");
        String contrasena = request.getParameter("contrasena");

        // Validación básica
        if (nombreUsuario == null || nombreUsuario.trim().isEmpty() ||
                contrasena == null || contrasena.trim().isEmpty()) {
            request.setAttribute("error", "Usuario y contraseña son obligatorios");
            request.getRequestDispatcher("/index.jsp").forward(request, response);
            return;
        }

        // Validar credenciales
        Usuario usuario = usuarioDAO.validarLogin(nombreUsuario, contrasena);

        if (usuario != null) {
            // Login exitoso
            HttpSession session = request.getSession();
            session.setAttribute("usuario", usuario);
            session.setAttribute("usuarioId", usuario.getId());
            session.setAttribute("nombreUsuario", usuario.getNombreUsuario());

            response.sendRedirect(request.getContextPath() + "/dashboard");
        } else {
            // Login fallido
            request.setAttribute("error", "Usuario o contraseña incorrectos");
            request.getRequestDispatcher("/index.jsp").forward(request, response);
        }
    }

    private void procesarRegistro(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String nombreUsuario = request.getParameter("nombreUsuario");
        String nombre = request.getParameter("nombre");
        String email = request.getParameter("email");
        String telefono = request.getParameter("telefono");
        String contrasena = request.getParameter("contrasena");
        String confirmarContrasena = request.getParameter("confirmarContrasena");

        // Validaciones
        if (nombreUsuario == null || nombreUsuario.trim().isEmpty() ||
                nombre == null || nombre.trim().isEmpty() ||
                email == null || email.trim().isEmpty() ||
                contrasena == null || contrasena.trim().isEmpty()) {

            request.setAttribute("error", "Todos los campos obligatorios deben ser completados");
            request.getRequestDispatcher("/index.jsp").forward(request, response);
            return;
        }

        if (!contrasena.equals(confirmarContrasena)) {
            request.setAttribute("error", "Las contraseñas no coinciden");
            request.getRequestDispatcher("/index.jsp").forward(request, response);
            return;
        }

        // Verificar si el usuario ya existe
        if (usuarioDAO.buscarPorNombreUsuario(nombreUsuario) != null) {
            request.setAttribute("error", "El nombre de usuario ya existe");
            request.getRequestDispatcher("/index.jsp").forward(request, response);
            return;
        }

        if (usuarioDAO.buscarPorEmail(email) != null) {
            request.setAttribute("error", "El email ya está registrado");
            request.getRequestDispatcher("/index.jsp").forward(request, response);
            return;
        }

        // Crear nuevo usuario
        Usuario nuevoUsuario = new Usuario(nombreUsuario, nombre, email, telefono, contrasena);

        if (usuarioDAO.crearUsuario(nuevoUsuario)) {
            request.setAttribute("success", "Usuario registrado exitosamente. Por favor inicia sesión.");
            request.getRequestDispatcher("/index.jsp").forward(request, response);
        } else {
            request.setAttribute("error", "Error al registrar el usuario. Intenta nuevamente.");
            request.getRequestDispatcher("/index.jsp").forward(request, response);
        }
    }

    private void procesarLogout(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }

        response.sendRedirect(request.getContextPath() + "/login");
    }
}