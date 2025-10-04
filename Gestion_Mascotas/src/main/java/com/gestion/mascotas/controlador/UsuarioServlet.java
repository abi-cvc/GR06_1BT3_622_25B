package com.gestionmascotas.app.controller;

import com.gestionmascotas.app.dao.UsuarioDAO;
import com.gestionmascotas.app.model.Usuario;
import org.mindrot.jbcrypt.BCrypt;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
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
                response.sendRedirect("index.jsp");
        }
    }

    private void registrarUsuario(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        String nombreUsuario = request.getParameter("nombreUsuario");
        String email = request.getParameter("email");
        String contrasena = request.getParameter("contrasena");

        // Verificar si ya existe
        if (usuarioDAO.buscarPorNombreUsuario(nombreUsuario) != null ||
                usuarioDAO.buscarPorEmail(email) != null) {
            request.setAttribute("error", "El usuario o email ya existe");
            request.getRequestDispatcher("registro.jsp").forward(request, response);
            return;
        }

        // Hashear contraseña
        String hashedPassword = BCrypt.hashpw(contrasena, BCrypt.gensalt());

        Usuario usuario = new Usuario();
        usuario.setNombreUsuario(nombreUsuario);
        usuario.setEmail(email);
        usuario.setContrasena(hashedPassword);

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

        Usuario usuario = usuarioDAO.buscarPorNombreUsuario(nombreUsuario);

        if (usuario != null && BCrypt.checkpw(contrasena, usuario.getContrasena())) {
            HttpSession session = request.getSession();
            session.setAttribute("usuario", usuario);
            session.setAttribute("nombreCompleto", usuario.getNombreUsuario()); // <-- importante
            response.sendRedirect(request.getContextPath() + "/dashboard"); // ir al dashboard
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
            usuario.setContrasena(BCrypt.hashpw(nuevaContrasena, BCrypt.gensalt()));
        }

        if (usuarioDAO.actualizarUsuario(usuario)) {
            session.setAttribute("usuario", usuario);
            session.setAttribute("nombreCompleto", usuario.getNombreUsuario()); // mantener nombre actualizado
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
