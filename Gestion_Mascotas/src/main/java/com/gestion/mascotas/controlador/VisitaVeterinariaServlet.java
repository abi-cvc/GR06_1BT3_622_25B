package com.gestion.mascotas.controlador;

import com.gestion.mascotas.dao.MascotaDAO;
import com.gestion.mascotas.dao.VisitaVeterinariaDAO;
import com.gestion.mascotas.modelo.Mascota;
import com.gestion.mascotas.modelo.Usuario;
import com.gestion.mascotas.modelo.VisitaVeterinaria;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@WebServlet({"/visita", "/visitas"})
public class VisitaVeterinariaServlet extends HttpServlet {

    private VisitaVeterinariaDAO visitaDAO = new VisitaVeterinariaDAO();
    private MascotaDAO mascotaDAO = new MascotaDAO();

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
            case "nueva":
            case "registrar":
                mostrarFormularioRegistro(request, response);
                break;
            case "eliminar":
                eliminarVisita(request, response);
                break;
            case "listar":
            default:
                listarVisitas(request, response);
                break;
        }
    }

    private void mostrarFormularioRegistro(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        Long usuarioId = (Long) session.getAttribute("usuarioId");

        List<Mascota> mascotas = mascotaDAO.obtenerMascotasPorUsuario(usuarioId);
        request.setAttribute("mascotas", mascotas);
        request.getRequestDispatcher("/jsp/registrarVisita.jsp").forward(request, response);
    }

    private void listarVisitas(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        Long usuarioId = (Long) session.getAttribute("usuarioId");

        List<VisitaVeterinaria> visitas = visitaDAO.obtenerPorUsuario(usuarioId);
        List<Mascota> mascotas = mascotaDAO.obtenerMascotasPorUsuario(usuarioId);

        request.setAttribute("visitas", visitas);
        request.setAttribute("mascotas", mascotas);
        request.getRequestDispatcher("/jsp/listaVisitas.jsp").forward(request, response);
    }

    private void eliminarVisita(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        Long id = Long.parseLong(request.getParameter("id"));

        HttpSession session = request.getSession(false);
        Usuario usuario = (Usuario) session.getAttribute("usuario");

        VisitaVeterinaria visita = visitaDAO.obtenerPorId(id);
        if (visita == null) {
            response.sendRedirect(request.getContextPath() + "/visitas?error=visita_no_encontrada");
            return;
        }

        if (!visita.getMascota().getUsuario().getId().equals(usuario.getId())) {
            response.sendRedirect(request.getContextPath() + "/visitas?error=acceso_denegado");
            return;
        }

        try {
            visitaDAO.eliminar(id);
            response.sendRedirect(request.getContextPath() + "/visitas?success=eliminado");
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/visitas?error=error_eliminando");
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
            registrarVisita(request, response);
        } else {
            response.sendRedirect(request.getContextPath() + "/visitas?error=accion_desconocida");
        }
    }

    private void registrarVisita(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        try {
            Long mascotaId = Long.parseLong(request.getParameter("mascotaId"));
            LocalDate fecha = LocalDate.parse(request.getParameter("fecha"));
            String motivo = request.getParameter("motivo");

            Mascota mascota = mascotaDAO.obtenerPorId(mascotaId);
            if (mascota == null) {
                request.setAttribute("error", "Mascota no encontrada.");
                mostrarFormularioRegistro(request, response);
                return;
            }

            HttpSession session = request.getSession(false);
            Usuario usuario = (Usuario) session.getAttribute("usuario");
            if (!mascota.getUsuario().getId().equals(usuario.getId())) {
                request.setAttribute("error", "No tienes permiso para registrar visitas para esta mascota.");
                mostrarFormularioRegistro(request, response);
                return;
            }

            VisitaVeterinaria visita = new VisitaVeterinaria();
            visita.setFecha(fecha);
            visita.setMotivo(motivo);
            visita.setMascota(mascota);

            visitaDAO.guardar(visita);
            response.sendRedirect(request.getContextPath() + "/visitas?success=registrado");

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Error al registrar la visita: " + e.getMessage());
            mostrarFormularioRegistro(request, response);
        }
    }
}