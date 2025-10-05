package com.gestion.mascotas.controlador;

import com.gestion.mascotas.dao.UsuarioDAO;
import com.gestion.mascotas.modelo.Usuario;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet("/usuario")
public class UsuarioServlet extends HttpServlet {
    private UsuarioDAO usuarioDAO;

    @Override
    public void init() throws ServletException {
        usuarioDAO = new UsuarioDAO();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");

        switch (action) {
            case "registrar":
                registrarUsuario(request, response);
                break;
            case "login":
                loginUsuario(request, response);
                break;
            case "editar":
                editarUsuario(request, response);
                break;
            case "eliminar":
                eliminarUsuario(request, response);
                break;
            default:
                response.sendRedirect("jsp/index.jsp");
        }
    }

    private void registrarUsuario(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        String nombreUsuario = request.getParameter("nombreUsuario");
        String nombre = request.getParameter("nombre");
        String email = request.getParameter("email");
        String telefono = request.getParameter("telefono");
        String contrasena = request.getParameter("contrasena");

        // Verificar si ya existe
        if (usuarioDAO.buscarPorNombreUsuario(nombreUsuario) != null ||
                usuarioDAO.buscarPorEmail(email) != null) {
            request.setAttribute("error", "El usuario o email ya existe");
            request.getRequestDispatcher("registro.jsp").forward(request, response);
            return;
        }

        Usuario usuario = new Usuario(nombreUsuario, nombre, email, telefono, contrasena);

        if (usuarioDAO.crearUsuario(usuario)) {
            response.sendRedirect("login.jsp");
        } else {
            request.setAttribute("error", "Error al registrar usuario");
            request.getRequestDispatcher("registro.jsp").forward(request, response);
        }
    }

    private void loginUsuario(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        String nombreUsuario = request.getParameter("nombreUsuario");
        String contrasena = request.getParameter("contrasena");

        Usuario usuario = usuarioDAO.validarLogin(nombreUsuario, contrasena);

        if (usuario != null) {
            HttpSession session = request.getSession();
            session.setAttribute("usuario", usuario);
            session.setAttribute("nombreCompleto", usuario.getNombreUsuario());
            response.sendRedirect(request.getContextPath() + "/dashboard");
        } else {
            request.setAttribute("error", "Credenciales inválidas");
            request.getRequestDispatcher("/login.jsp").forward(request, response);
        }
    }


    private void editarUsuario(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("usuario") == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        Usuario usuario = (Usuario) session.getAttribute("usuario");

        String nuevoEmail = request.getParameter("email");
        String nuevaContrasena = request.getParameter("contrasena");

        if (nuevoEmail != null && !nuevoEmail.isEmpty()) {
            usuario.setEmail(nuevoEmail);
        }
        if (nuevaContrasena != null && !nuevaContrasena.isEmpty()) {
            usuario.setContrasena(nuevaContrasena);
        }

        if (usuarioDAO.actualizarUsuario(usuario)) {
            session.setAttribute("usuario", usuario);
            session.setAttribute("nombreCompleto", usuario.getNombreUsuario());
            request.setAttribute("mensaje", "Perfil actualizado correctamente");
        } else {
            request.setAttribute("error", "Error al actualizar perfil");
        }
        request.getRequestDispatcher("perfil.jsp").forward(request, response);
    }

    private void eliminarUsuario(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("usuario") == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        Usuario usuario = (Usuario) session.getAttribute("usuario");
        if (usuarioDAO.eliminarUsuario(usuario.getId())) {
            session.invalidate();
            response.sendRedirect("index.jsp");
        } else {
            response.sendRedirect("perfil.jsp?error=1");
        }
    }
}
