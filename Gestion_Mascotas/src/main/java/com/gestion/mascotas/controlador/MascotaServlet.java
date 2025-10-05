// FileName: MultipleFiles/MascotaServlet.java (Updated)
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

@WebServlet({"/mascota", "/mascotas"})
public class MascotaServlet extends HttpServlet {

    private MascotaDAO mascotaDAO = new MascotaDAO();
    private UsuarioDAO usuarioDAO = new UsuarioDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("usuario") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        String action = request.getParameter("action");
        if (action == null) action = "listar";

        switch (action) {
            case "registrar":
                mostrarFormularioRegistro(request, response);
                break;
            case "editar":
                mostrarFormularioEdicion(request, response);
                break;
            case "eliminar":
                eliminarMascota(request, response);
                break;
            case "detalles":
                mostrarDetallesMascota(request, response);
                break;
            default:
                listarMascotas(request, response);
                break;
        }
    }

    private void mostrarFormularioRegistro(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Pass the logged-in user's ID to pre-fill or associate
        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("usuarioId") != null) {
            request.setAttribute("usuarioId", session.getAttribute("usuarioId"));
        }
        request.setAttribute("tiposMascota", TipoMascota.values());
        request.getRequestDispatcher("/jsp/mascota/registrarMascota.jsp").forward(request, response);
    }

    private void mostrarFormularioEdicion(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Long id = Long.parseLong(request.getParameter("id"));
        Mascota mascota = mascotaDAO.obtenerPorId(id);

        if (mascota == null) {
            request.setAttribute("error", "Mascota no encontrada para edición.");
            listarMascotas(request, response);
            return;
        }

        request.setAttribute("mascota", mascota);
        request.setAttribute("tiposMascota", TipoMascota.values());
        request.getRequestDispatcher("/jsp/mascota/editarMascota.jsp").forward(request, response);
    }

    private void listarMascotas(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Optionally filter by logged-in user
        HttpSession session = request.getSession(false);
        Long usuarioId = (Long) session.getAttribute("usuarioId");
        List<Mascota> mascotas = usuarioDAO.obtenerPorId(usuarioId).getMascotas(); // Assuming UsuarioDAO has a method to get pets by user

        request.setAttribute("mascotas", mascotas);
        request.getRequestDispatcher("/jsp/mascota/listaMascotas.jsp").forward(request, response);
    }

    private void mostrarDetallesMascota(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Long id = Long.parseLong(request.getParameter("id"));
        Mascota mascota = mascotaDAO.obtenerPorId(id);

        if (mascota == null) {
            request.setAttribute("error", "Mascota no encontrada.");
            listarMascotas(request, response);
            return;
        }
        request.setAttribute("mascota", mascota);
        request.getRequestDispatcher("/jsp/mascota/detallesMascota.jsp").forward(request, response);
    }

    private void eliminarMascota(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        Long id = Long.parseLong(request.getParameter("id"));
        
        HttpSession session = request.getSession(false);
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        
        // Verificar que la mascota pertenezca al usuario
        Mascota mascota = mascotaDAO.obtenerPorId(id);
        if (mascota == null) {
            response.sendRedirect(request.getContextPath() + "/dashboard?error=mascota_no_encontrada");
            return;
        }
        
        if (!mascota.getUsuario().getId().equals(usuario.getId())) {
            response.sendRedirect(request.getContextPath() + "/dashboard?error=acceso_denegado");
            return;
        }
        
        try {
            mascotaDAO.eliminar(id);
            response.sendRedirect(request.getContextPath() + "/dashboard?success=mascota_eliminada");
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/dashboard?error=error_eliminando");
        }
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
        if ("registrar".equals(action)) {
            registrarMascota(request, response);
        } else if ("actualizar".equals(action)) {
            actualizarMascota(request, response);
        } else {
            response.sendRedirect(request.getContextPath() + "/mascota?error=accion_desconocida");
        }
    }

    private void registrarMascota(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        String nombre = request.getParameter("nombre");
        TipoMascota tipo = TipoMascota.valueOf(request.getParameter("tipo"));
        String raza = request.getParameter("raza");
        Integer edad = Integer.parseInt(request.getParameter("edad"));
        Double peso = Double.parseDouble(request.getParameter("peso"));
        String color = request.getParameter("color");
        Long usuarioId = Long.parseLong(request.getParameter("usuarioId"));

        Usuario usuario = usuarioDAO.obtenerPorId(usuarioId);
        if (usuario == null) {
            request.setAttribute("error", "Usuario no encontrado.");
            mostrarFormularioRegistro(request, response);
            return;
        }

        Mascota mascota = new Mascota();
        mascota.registrarMascota(nombre, tipo, raza, edad, peso, color, usuario); // Using the model method

        try {
            mascotaDAO.guardar(mascota);
            response.sendRedirect(request.getContextPath() + "/mascota?action=listar&success=registrado");
        } catch (Exception e) {
            request.setAttribute("error", "Error al registrar la mascota: " + e.getMessage());
            mostrarFormularioRegistro(request, response);
        }
    }

    private void actualizarMascota(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        Long id = Long.parseLong(request.getParameter("id"));
        String nombre = request.getParameter("nombre");
        TipoMascota tipo = TipoMascota.valueOf(request.getParameter("tipo")); // Type might not be editable, but including for completeness
        String raza = request.getParameter("raza");
        Integer edad = Integer.parseInt(request.getParameter("edad"));
        Double peso = Double.parseDouble(request.getParameter("peso"));
        String color = request.getParameter("color");
        // Assuming usuarioId is not changed during pet update, or handled separately

        Mascota mascota = mascotaDAO.obtenerPorId(id);
        if (mascota == null) {
            request.setAttribute("error", "Mascota no encontrada para actualizar.");
            mostrarFormularioEdicion(request, response);
            return;
        }

        mascota.actualizarDatos(nombre, raza, edad, peso, color); // Using the model method
        mascota.setTipo(tipo); // Update type directly if it's editable

        try {
            mascotaDAO.guardar(mascota); // save or merge
            response.sendRedirect(request.getContextPath() + "/mascota?action=listar&success=actualizado");
        } catch (Exception e) {
            request.setAttribute("error", "Error al actualizar la mascota: " + e.getMessage());
            mostrarFormularioEdicion(request, response);
        }
    }
}