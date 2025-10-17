package com.gestion.mascotas.controlador;

import com.gestion.mascotas.dao.UsuarioDAO;
import com.gestion.mascotas.modelo.entidades.Usuario;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/crearPerfil")
public class CrearPerfilServlet extends HttpServlet {

    private UsuarioDAO usuarioDAO = new UsuarioDAO();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // 1. Obtener datos del formulario
        String nombreUsuario = request.getParameter("nombreUsuario");
        String nombre = request.getParameter("nombre");
        String email = request.getParameter("email");
        String telefono = request.getParameter("telefono");
        String contrasena = request.getParameter("contrasena");
        String confirmarContrasena = request.getParameter("confirmarContrasena");

        // 2. Validar que las contraseñas coincidan
        if (!contrasena.equals(confirmarContrasena)) {
            request.setAttribute("error", "Las contraseñas no coinciden");
            request.getRequestDispatcher("/jsp/incremento1/crearPerfil.jsp")
                    .forward(request, response);
            return;
        }

        // 3. Verificar si el correo ya existe (Diagrama de Actividades)
        if (usuarioDAO.existeEmail(email)) {
            request.setAttribute("error", "El correo ya está registrado");
            request.getRequestDispatcher("/jsp/incremento1/crearPerfil.jsp")
                    .forward(request, response);
            return;
        }

        // 4. Crear objeto Usuario
        Usuario nuevoUsuario = new Usuario();
        nuevoUsuario.setNombreUsuario(nombreUsuario);
        nuevoUsuario.setNombre(nombre);
        nuevoUsuario.setEmail(email);
        nuevoUsuario.setTelefono(telefono);
        nuevoUsuario.setContrasena(contrasena);

        // 5. Guardar en la base de datos
        try {
            usuarioDAO.guardar(nuevoUsuario);

            // 6. Redirigir a inicio de sesión con mensaje de éxito
            request.setAttribute("mensaje", "Perfil creado exitosamente. Por favor, inicia sesión.");
            request.getRequestDispatcher("/jsp/incremento1/iniciarSesion.jsp")
                    .forward(request, response);

        } catch (Exception e) {
            request.setAttribute("error", "Error al crear el perfil: " + e.getMessage());
            request.getRequestDispatcher("/jsp/incremento1/crearPerfil.jsp")
                    .forward(request, response);
        }
    }
}