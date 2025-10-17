package com.gestion.mascotas.controlador;

import com.gestion.mascotas.modelo.RecordatorioAlimentacion;
import com.gestion.mascotas.modelo.Usuario;
import com.gestion.mascotas.service.RecordatorioAlimentacionService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;

@WebServlet("/recordatorioAlimentacion")
public class RecordatorioAlimentacionServlet extends HttpServlet {

    private RecordatorioAlimentacionService recordatorioService;

    @Override
    public void init() throws ServletException {
        recordatorioService = new RecordatorioAlimentacionService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Verificar autenticación
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("usuario") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        String action = request.getParameter("action");
        if (action == null) action = "listar";

        switch (action) {
            case "eliminar":
                eliminarRecordatorioAlimentacion(request, response);
                break;
            case "desactivar":
                desactivarRecordatorioAlimentacion(request, response);
                break;
            case "listar":
            default:
                listarRecordatoriosAlimentacion(request, response);
                break;
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Verificar autenticación
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("usuario") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        String action = request.getParameter("action");

        if ("registrar".equals(action)) {
            registrarRecordatorioAlimentacion(request, response);
        } else if ("actualizar".equals(action)) {
            actualizarRecordatorioAlimentacion(request, response);
        } else {
            response.sendRedirect(request.getContextPath() + "/mascota?error=accion_desconocida");
        }
    }

    /**
     * Registrar un nuevo recordatorio de alimentación
     */
    private void registrarRecordatorioAlimentacion(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        Usuario usuario = (Usuario) session.getAttribute("usuario");

        try {
            String mascotaIdStr = request.getParameter("mascotaId");
            String frecuencia = request.getParameter("frecuencia");
            String tipoAlimento = request.getParameter("tipoAlimento");
            String[] diasSeleccionados = request.getParameterValues("diasSemana");

            boolean guardado = recordatorioService.registrarRecordatorio(
                    mascotaIdStr, frecuencia, tipoAlimento, diasSeleccionados,
                    request::getParameter, usuario
            );

            if (guardado) {
                System.out.println("Recordatorio guardado exitosamente");
                response.sendRedirect(request.getContextPath() +
                        "/mascota?action=detalles&id=" + mascotaIdStr +
                        "&success=recordatorio_alimentacion_registrado");
            } else {
                System.err.println("Error al guardar el recordatorio");
                response.sendRedirect(request.getContextPath() +
                        "/mascota?action=detalles&id=" + mascotaIdStr +
                        "&error=error_guardar_recordatorio");
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
            System.err.println("Error de formato numérico: " + e.getMessage());
            response.sendRedirect(request.getContextPath() +
                    "/mascota?action=detalles&id=" + request.getParameter("mascotaId") +
                    "&error=formato_invalido");
        } catch (IllegalArgumentException e) {
            String mascotaIdStr = request.getParameter("mascotaId");
            response.sendRedirect(request.getContextPath() +
                    "/mascota?action=detalles&id=" + mascotaIdStr +
                    "&error=" + e.getMessage());

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Error general: " + e.getMessage());
            response.sendRedirect(request.getContextPath() +
                    "/mascota?action=detalles&id=" + request.getParameter("mascotaId") +
                    "&error=error_registro_recordatorio_alimentacion");
        }
    }

    /**
     * Actualizar un recordatorio de alimentación existente
     */
    private void actualizarRecordatorioAlimentacion(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        Usuario usuario = (Usuario) session.getAttribute("usuario");

        try {
            Long recordatorioId = Long.parseLong(request.getParameter("recordatorioId"));
            Long mascotaId = Long.parseLong(request.getParameter("mascotaId"));
            String frecuencia = request.getParameter("frecuencia");
            String tipoAlimento = request.getParameter("tipoAlimento");
            String[] diasSeleccionados = request.getParameterValues("diasSemana");

            boolean actualizado = recordatorioService.actualizarRecordatorio(
                    recordatorioId, frecuencia, tipoAlimento, diasSeleccionados,
                    request::getParameter, usuario
            );

            if (actualizado) {
                response.sendRedirect(request.getContextPath() +
                        "/mascota?action=detalles&id=" + mascotaId +
                        "&success=recordatorio_actualizado");
            } else {
                response.sendRedirect(request.getContextPath() +
                        "/mascota?action=detalles&id=" + mascotaId +
                        "&error=error_actualizar_recordatorio");
            }

        } catch (IllegalArgumentException e) {
            Long mascotaId = Long.parseLong(request.getParameter("mascotaId"));
            response.sendRedirect(request.getContextPath() +
                    "/mascota?action=detalles&id=" + mascotaId +
                    "&error=" + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() +
                    "/mascota?action=detalles&id=" + request.getParameter("mascotaId") +
                    "&error=error_actualizacion");
        }
    }

    /**
     * Eliminar un recordatorio de alimentación
     */
    private void eliminarRecordatorioAlimentacion(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        HttpSession session = request.getSession();
        Usuario usuario = (Usuario) session.getAttribute("usuario");

        try {
            String idStr = request.getParameter("id");
            String mascotaIdStr = request.getParameter("mascotaId");

            System.out.println("=== INICIO ELIMINACIÓN RECORDATORIO ALIMENTACIÓN ===");
            System.out.println("ID Recordatorio recibido: " + idStr);
            System.out.println("ID Mascota recibido: " + mascotaIdStr);

            if (mascotaIdStr == null || mascotaIdStr.trim().isEmpty()) {
                System.err.println("ERROR: ID de mascota es nulo o vacío");
                response.sendRedirect(request.getContextPath() +
                        "/mascota?action=listar&error=mascota_id_invalido");
                return;
            }

            boolean eliminado = recordatorioService.eliminarRecordatorio(idStr, usuario);

            if (eliminado) {
                response.sendRedirect(request.getContextPath() +
                        "/mascota?action=detalles&id=" + mascotaIdStr +
                        "&success=recordatorio_alimentacion_eliminado");
            } else {
                response.sendRedirect(request.getContextPath() +
                        "/mascota?action=detalles&id=" + mascotaIdStr +
                        "&error=error_eliminar_recordatorio");
            }
        } catch (NumberFormatException e) {
            System.err.println("ERROR: Formato de número inválido");
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() +
                    "/mascota?action=detalles&id=" + request.getParameter("mascotaId") +
                    "&error=formato_id_invalido");
        } catch (IllegalArgumentException e) {
            String mascotaIdStr = request.getParameter("mascotaId");
            response.sendRedirect(request.getContextPath() +
                    "/mascota?action=detalles&id=" + mascotaIdStr +
                    "&error=" + e.getMessage());

        } catch (Exception e) {
            System.err.println("ERROR GENERAL al eliminar recordatorio");
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() +
                    "/mascota?action=detalles&id=" + request.getParameter("mascotaId") +
                    "&error=error_eliminando_recordatorio_alimentacion");
        } finally {
            System.out.println("=== FIN ELIMINACIÓN RECORDATORIO ALIMENTACIÓN ===");
        }
    }

    /**
     * Desactivar un recordatorio sin eliminarlo
     */
    private void desactivarRecordatorioAlimentacion(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        HttpSession session = request.getSession();
        Usuario usuario = (Usuario) session.getAttribute("usuario");

        try {
            Long recordatorioId = Long.parseLong(request.getParameter("id"));
            Long mascotaId = Long.parseLong(request.getParameter("mascotaId"));

            boolean desactivado = recordatorioService.desactivarRecordatorio(recordatorioId, usuario);

            if (desactivado) {
                response.sendRedirect(request.getContextPath() +
                        "/mascota?action=detalles&id=" + mascotaId +
                        "&success=recordatorio_desactivado");
            } else {
                response.sendRedirect(request.getContextPath() +
                        "/mascota?action=detalles&id=" + mascotaId +
                        "&error=error_desactivar_recordatorio");
            }

        } catch (IllegalArgumentException e) {
            Long mascotaId = Long.parseLong(request.getParameter("mascotaId"));
            response.sendRedirect(request.getContextPath() +
                    "/mascota?action=detalles&id=" + mascotaId +
                    "&error=" + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() +
                    "/mascota?action=detalles&id=" + request.getParameter("mascotaId") +
                    "&error=error_desactivando_recordatorio");
        }
    }

    /**
     * Listar recordatorios de alimentación de una mascota
     */
    private void listarRecordatoriosAlimentacion(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            Long mascotaId = Long.parseLong(request.getParameter("mascotaId"));
            List<RecordatorioAlimentacion> recordatorios =
                    recordatorioService.obtenerRecordatoriosPorMascota(mascotaId);

            request.setAttribute("recordatoriosAlimentacion", recordatorios);
            request.setAttribute("mascotaId", mascotaId);
            request.getRequestDispatcher("/jsp/listaRecordatoriosAlimentacion.jsp").forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/mascota?error=error_listando_recordatorios");
        }
    }
}