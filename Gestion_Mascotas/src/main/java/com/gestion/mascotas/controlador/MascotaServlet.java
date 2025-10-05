package com.gestion.mascotas.controlador;

import com.gestion.mascotas.dao.MascotaDAO;
import com.gestion.mascotas.dao.UsuarioDAO;
import com.gestion.mascotas.modelo.Mascota;
import com.gestion.mascotas.modelo.TipoMascota;
import com.gestion.mascotas.modelo.Usuario;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.List;

@WebServlet("/mascota")
public class MascotaServlet extends HttpServlet {

    private MascotaDAO mascotaDAO = new MascotaDAO();
    private UsuarioDAO usuarioDAO = new UsuarioDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");
        if (action == null) action = "listar";

        switch (action) {
            case "registrar":
                mostrarFormularioRegistro(request, response);
                break;
            case "eliminar":
                eliminarMascota(request, response);
                break;
            default:
                listarMascotas(request, response);
                break;
        }
    }

    private void mostrarFormularioRegistro(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Puedes pasar el ID del usuario logueado
        request.getRequestDispatcher("mascota.jsp").forward(request, response);
    }

    private void listarMascotas(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<Mascota> mascotas = mascotaDAO.obtenerTodas();
        request.setAttribute("mascotas", mascotas);
        request.getRequestDispatcher("listaMascotas.jsp").forward(request, response);
    }

    private void eliminarMascota(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        Long id = Long.parseLong(request.getParameter("id"));
        mascotaDAO.eliminar(id);
        response.sendRedirect("mascota?action=listar");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");
        if ("registrar".equals(action)) {
            registrarMascota(request, response);
        }
    }

    private void registrarMascota(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        String nombre = request.getParameter("nombre");
        String tipo = request.getParameter("tipo");
        Long usuarioId = Long.parseLong(request.getParameter("usuarioId")); // lo pasas en el form

        Usuario usuario = usuarioDAO.obtenerPorId(usuarioId);
        if (usuario == null) {
            request.setAttribute("error", "Usuario no encontrado");
            request.getRequestDispatcher("mascota.jsp").forward(request, response);
            return;
        }

        Mascota mascota = new Mascota();
        mascota.setNombre(nombre);
        mascota.setTipo(TipoMascota.valueOf(tipo));
        mascota.setUsuario(usuario);

        mascotaDAO.guardar(mascota);

        response.sendRedirect("mascota?action=listar");
    }
}
