package com.gestion.mascotas.controlador;

import com.gestion.mascotas.modelo.entidades.Usuario;
import com.gestion.mascotas.servicio.RecordatorioPaseoService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Servlet para manejar operaciones sobre los recordatorios de paseo.
 */
@WebServlet("/recordatorioPaseo")
public class RecordatorioPaseoServlet extends HttpServlet {

    private RecordatorioPaseoService recordatorioService;

    @Override
    public void init() throws ServletException {
        recordatorioService = new RecordatorioPaseoService();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("usuario") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        Usuario usuario = (Usuario) session.getAttribute("usuario");
        String action = request.getParameter("action");

        try {
            switch (action) {
                case "registrar":
                    registrarRecordatorio(request, response, usuario);
                    break;
                case "actualizar":
                    actualizarRecordatorio(request, response, usuario);
                    break;
                case "eliminar":
                    eliminarRecordatorio(request, response, usuario);
                    break;
                case "desactivar":
                    desactivarRecordatorio(request, response, usuario);
                    break;
                default:
                    response.sendRedirect(request.getContextPath() + "/mascota?action=listar");
            }
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/mascota?action=listar&error=" + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/mascota?action=listar&error=Error inesperado.");
        }
    }

    private void registrarRecordatorio(HttpServletRequest request, HttpServletResponse response, Usuario usuario)
            throws IOException {
        Long mascotaId = Long.parseLong(request.getParameter("mascotaId"));
        int frecuencia = Integer.parseInt(request.getParameter("frecuenciaPaseo"));
        Integer duracionMinutos = Integer.valueOf(request.getParameter("duracionMinutos"));
        String[] diasSeleccionados = request.getParameterValues("diasSemanaPaseo");

        List<LocalTime> horarios = new ArrayList<>();
        for (int i = 1; i <= frecuencia; i++) {
            String horaStr = request.getParameter("horarioPaseo" + i);
            if (horaStr != null && !horaStr.isEmpty()) {
                horarios.add(LocalTime.parse(horaStr));
            }
        }

        boolean exito = recordatorioService.registrarRecordatorio(
                usuario, mascotaId, frecuencia, duracionMinutos, diasSeleccionados, horarios
        );

        String redirect = request.getContextPath() + "/mascota?action=detalles&id=" + mascotaId;
        response.sendRedirect(redirect + (exito ? "&success=Recordatorio guardado"
                                               : "&error=No se pudo guardar el recordatorio"));
    }

    private void actualizarRecordatorio(HttpServletRequest request, HttpServletResponse response, Usuario usuario)
            throws IOException {
        Long idRecordatorio = Long.parseLong(request.getParameter("idRecordatorio"));
        int frecuencia = Integer.parseInt(request.getParameter("frecuenciaPaseo"));
        Integer duracionMinutos = Integer.valueOf(request.getParameter("duracionMinutos"));
        String[] diasSeleccionados = request.getParameterValues("diasSemanaPaseo");

        List<LocalTime> horarios = new ArrayList<>();
        for (int i = 1; i <= frecuencia; i++) {
            String horaStr = request.getParameter("horarioPaseo" + i);
            if (horaStr != null && !horaStr.isEmpty()) {
                horarios.add(LocalTime.parse(horaStr));
            }
        }

        boolean exito = recordatorioService.actualizarRecordatorio(
                idRecordatorio, usuario, frecuencia, duracionMinutos, diasSeleccionados, horarios
        );

        String redirect = request.getContextPath() + "/mascota?action=listar";
        response.sendRedirect(redirect + (exito ? "&success=Recordatorio actualizado"
                                               : "&error=No se pudo actualizar el recordatorio"));
    }

    private void eliminarRecordatorio(HttpServletRequest request, HttpServletResponse response, Usuario usuario)
            throws IOException {
        Long idRecordatorio = Long.parseLong(request.getParameter("idRecordatorio"));
        boolean exito = recordatorioService.eliminarRecordatorio(idRecordatorio, usuario);

        String redirect = request.getContextPath() + "/mascota?action=listar";
        response.sendRedirect(redirect + (exito ? "&success=Recordatorio eliminado"
                                               : "&error=No se pudo eliminar el recordatorio"));
    }

    private void desactivarRecordatorio(HttpServletRequest request, HttpServletResponse response, Usuario usuario)
            throws IOException {
        Long idRecordatorio = Long.parseLong(request.getParameter("idRecordatorio"));
        boolean exito = recordatorioService.desactivarRecordatorio(idRecordatorio, usuario);

        String redirect = request.getContextPath() + "/mascota?action=listar";
        response.sendRedirect(redirect + (exito ? "&success=Recordatorio desactivado"
                                               : "&error=No se pudo desactivar el recordatorio"));
    }
}
