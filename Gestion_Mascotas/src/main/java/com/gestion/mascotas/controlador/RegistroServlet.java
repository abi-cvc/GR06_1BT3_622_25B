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
 * Servlet para manejar el registro de nuevos usuarios
 */
@WebServlet("/registro")
public class RegistroServlet extends HttpServlet {

    private UsuarioService usuarioService;

    @Override
    public void init() {
        usuarioService = new UsuarioService();
    }

    /**
     * Mostrar formulario de registro
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

        request.getRequestDispatcher("/jsp/registro.jsp").forward(request, response);
    }

    /**
     * Procesar el registro de usuario
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Obtener parámetros del formulario
        String nombreUsuario = request.getParameter("nombreUsuario");
        String nombre = request.getParameter("nombre");
        String email = request.getParameter("email");
        String telefono = request.getParameter("telefono");
        String contrasena = request.getParameter("contrasena");
        String confirmarContrasena = request.getParameter("confirmarContrasena");

        try{
            Usuario nuevoUsaurio = usuarioService.crearPerfil(nombreUsuario,nombre,email,telefono,contrasena, confirmarContrasena);
            HttpSession session = request.getSession();
            session.setAttribute("registroExitoso", "¡Cuenta creada exitosamente! Ahora puedes iniciar sesión.");
            response.sendRedirect(request.getContextPath() + "/login");
        }catch (IllegalArgumentException e) {
            // Si ocurre una excepción de validación, se muestra el error
            request.setAttribute("error", e.getMessage());
            request.getRequestDispatcher("/jsp/registro.jsp").forward(request, response);
        } catch (Exception e) {
            // Otros errores inesperados
            request.setAttribute("error", "Ocurrió un error inesperado al crear la cuenta.");
            request.getRequestDispatcher("/jsp/registro.jsp").forward(request, response);
        }
    }
}