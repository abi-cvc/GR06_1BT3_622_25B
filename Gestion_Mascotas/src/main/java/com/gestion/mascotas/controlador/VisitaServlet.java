package com.gestionmascotas.app.controller;

import com.gestionmascotas.app.dao.MascotaDAO;
import com.gestionmascotas.app.dao.VisitaVeterinariaDAO;
import com.gestionmascotas.app.model.Mascota;
import com.gestionmascotas.app.model.VisitaVeterinaria;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@WebServlet("/visita")
public class VisitaVeterinariaServlet extends HttpServlet {

    private VisitaVeterinariaDAO visitaDAO = new VisitaVeterinariaDAO();
    private MascotaDAO mascotaDAO = new MascotaDAO();

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
                eliminarVisita(request, response);
                break;
            default:
                listarVisitas(request, response);
                break;
        }
    }

    private void mostrarFormularioRegistro(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<Mascota> mascotas = mascotaDAO.obtenerTodas();
        request.setAttribute("mascotas", mascotas);
        request.getRequestDispatcher("visita.jsp").forward(request, response);
    }

    private void listarVisitas(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<VisitaVeterinaria> visitas = visitaDAO.obtenerTodas();
        request.setAttribute("visitas", visitas);
        request.getRequestDispatcher("listaVisitas.jsp").forward(request, response);
    }

    private void eliminarVisita(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        Long id = Long.parseLong(request.getParameter("id"));
        visitaDAO.eliminar(id);
        response.sendRedirect("visita?action=listar");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        if ("registrar".equals(action)) {
            registrarVisita(request, response);
        }
    }

    private void registrarVisita(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        String motivo = request.getParameter("motivo");
        LocalDate fecha = LocalDate.parse(request.getParameter("fecha"));
        Long mascotaId = Long.parseLong(request.getParameter("mascotaId"));

        Mascota mascota = mascotaDAO.obtenerPorId(mascotaId);
        if (mascota == null) {
            request.setAttribute("error", "Mascota no encontrada");
            request.getRequestDispatcher("visita.jsp").forward(request, response);
            return;
        }

        VisitaVeterinaria visita = new VisitaVeterinaria();
        visita.setMotivo(motivo);
        visita.setFecha(fecha);
        visita.setMascota(mascota);

        visitaDAO.guardar(visita);

        response.sendRedirect("visita?action=listar");
    }
}
