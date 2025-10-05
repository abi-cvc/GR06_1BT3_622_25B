package com.gestion.mascotas.controlador;

import com.gestion.mascotas.dao.MascotaDAO;
import com.gestion.mascotas.dao.VacunaDAO;
import com.gestion.mascotas.modelo.Mascota;
import com.gestion.mascotas.modelo.Vacuna;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@WebServlet("/vacuna")
public class VacunaServlet extends HttpServlet {

    private VacunaDAO vacunaDAO = new VacunaDAO();
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
                eliminarVacuna(request, response);
                break;
            default:
                listarVacunas(request, response);
                break;
        }
    }

    private void mostrarFormularioRegistro(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<Mascota> mascotas = mascotaDAO.obtenerTodas();
        request.setAttribute("mascotas", mascotas);
        request.getRequestDispatcher("vacuna.jsp").forward(request, response);
    }

    private void listarVacunas(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<Vacuna> vacunas = vacunaDAO.obtenerTodas();
        request.setAttribute("vacunas", vacunas);
        request.getRequestDispatcher("listaVacunas.jsp").forward(request, response);
    }

    private void eliminarVacuna(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        Long id = Long.parseLong(request.getParameter("id"));
        vacunaDAO.eliminar(id);
        response.sendRedirect("vacuna?action=listar");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        if ("registrar".equals(action)) {
            registrarVacuna(request, response);
        }
    }

    private void registrarVacuna(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        String nombre = request.getParameter("nombre");
        LocalDate fecha = LocalDate.parse(request.getParameter("fecha"));
        Long mascotaId = Long.parseLong(request.getParameter("mascotaId"));

        Mascota mascota = mascotaDAO.obtenerPorId(mascotaId);
        if (mascota == null) {
            request.setAttribute("error", "Mascota no encontrada");
            request.getRequestDispatcher("vacuna.jsp").forward(request, response);
            return;
        }

        Vacuna vacuna = new Vacuna();
        vacuna.setNombre(nombre);
        vacuna.setFecha(fecha);
        vacuna.setMascota(mascota);

        vacunaDAO.guardar(vacuna);

        response.sendRedirect("vacuna?action=listar");
    }
}
