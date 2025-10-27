package com.gestion.mascotas.controlador;

import com.gestion.mascotas.dao.HistorialPesoDAO;
import com.gestion.mascotas.dao.MascotaDAO;
import com.gestion.mascotas.modelo.entidades.HistorialPeso;
import com.gestion.mascotas.modelo.entidades.Mascota;
import com.gestion.mascotas.modelo.entidades.Usuario;
import com.gestion.mascotas.servicio.HistorialPesoService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@WebServlet("/historialPeso")
public class HistorialPesoServlet extends HttpServlet {

    private HistorialPesoService historialPesoService;
    private MascotaDAO mascotaDAO;

    @Override
    public void init() {
        this.historialPesoService = new HistorialPesoService();
        this.mascotaDAO = new MascotaDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("usuario") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        String action = request.getParameter("action");
        Usuario usuario = (Usuario) session.getAttribute("usuario");

        try {
            if ("listar".equals(action)) {
                listarHistorial(request, response, usuario);
            } else if ("eliminar".equals(action)) {
                eliminarRegistro(request, response, usuario);
            } else {
                listarHistorial(request, response, usuario);
            }
        } catch (Exception e) {
            request.getSession().setAttribute("error", "Error inesperado: " + e.getMessage());
            response.sendRedirect(request.getContextPath() + "/dashboard");
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
        Usuario usuario = (Usuario) session.getAttribute("usuario");

        try {
            if ("registrar".equals(action)) {
                registrarPeso(request, response, usuario);
            } else {
                response.sendRedirect(request.getContextPath() + "/historialPeso?action=listar&error=accion_desconocida");
            }
        } catch (Exception e) {
            request.getSession().setAttribute("error", "Error inesperado: " + e.getMessage());
            response.sendRedirect(request.getContextPath() + "/dashboard");
        }
    }

    private void listarHistorial(HttpServletRequest request, HttpServletResponse response, Usuario usuario)
            throws ServletException, IOException {
        Long mascotaId = Long.parseLong(request.getParameter("mascotaId"));

        // Verificar que la mascota pertenece al usuario
        Mascota mascota = mascotaDAO.obtenerPorId(mascotaId);
        if (mascota == null) {
            response.sendRedirect(request.getContextPath() + "/dashboard?error=mascota_no_encontrada");
            return;
        }

        if (!mascota.getUsuario().getId().equals(usuario.getId())) {
            response.sendRedirect(request.getContextPath() + "/dashboard?error=acceso_denegado");
            return;
        }

        // Obtener historial ordenado por fecha descendente
        List<HistorialPeso> historial = historialPesoService.obtenerHistorial(mascotaId);

        request.setAttribute("mascota", mascota);
        request.setAttribute("historialPeso", historial);
        request.getRequestDispatcher("/jsp/historialPeso.jsp").forward(request, response);
    }

    private void registrarPeso(HttpServletRequest request, HttpServletResponse response, Usuario usuario)
            throws IOException {
        try {
            Long mascotaId = Long.parseLong(request.getParameter("mascotaId"));
            double peso = Double.parseDouble(request.getParameter("peso"));
            String fechaStr = request.getParameter("fechaRegistro");
            String comentarios = request.getParameter("comentarios");

            // Verificar que la mascota pertenece al usuario
            Mascota mascota = mascotaDAO.obtenerPorId(mascotaId);
            if (mascota == null) {
                response.sendRedirect(request.getContextPath() + "/historialPeso?action=listar&mascotaId=" + mascotaId + "&error=mascota_no_encontrada");
                return;
            }

            if (!mascota.getUsuario().getId().equals(usuario.getId())) {
                response.sendRedirect(request.getContextPath() + "/historialPeso?action=listar&mascotaId=" + mascotaId + "&error=acceso_denegado");
                return;
            }

            // Parsear fecha
            LocalDateTime fechaRegistro;
            if (fechaStr != null && !fechaStr.isEmpty()) {
                // Si se proporciona fecha, agregarle la hora actual
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                fechaRegistro = LocalDateTime.parse(fechaStr + " " + LocalDateTime.now().toLocalTime().format(DateTimeFormatter.ofPattern("HH:mm:ss")),
                        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            } else {
                fechaRegistro = LocalDateTime.now();
            }

            // Registrar peso usando el servicio
            String error = historialPesoService.registrarPeso(mascotaId, peso, fechaRegistro, comentarios);

            if (error != null) {
                response.sendRedirect(request.getContextPath() + "/historialPeso?action=listar&mascotaId=" + mascotaId + "&error=" + error);
            } else {
                response.sendRedirect(request.getContextPath() + "/historialPeso?action=listar&mascotaId=" + mascotaId + "&success=registrado");
            }

        } catch (NumberFormatException e) {
            Long mascotaId = Long.parseLong(request.getParameter("mascotaId"));
            response.sendRedirect(request.getContextPath() + "/historialPeso?action=listar&mascotaId=" + mascotaId + "&error=formato_invalido");
        } catch (Exception e) {
            Long mascotaId = Long.parseLong(request.getParameter("mascotaId"));
            response.sendRedirect(request.getContextPath() + "/historialPeso?action=listar&mascotaId=" + mascotaId + "&error=error_registro");
        }
    }

    private void eliminarRegistro(HttpServletRequest request, HttpServletResponse response, Usuario usuario)
            throws IOException {
        // Implementación opcional para eliminar registros
        response.sendRedirect(request.getContextPath() + "/historialPeso?action=listar&error=funcion_no_implementada");
    }
}