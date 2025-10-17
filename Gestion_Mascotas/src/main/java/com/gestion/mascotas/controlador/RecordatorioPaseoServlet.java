package com.gestion.mascotas.controlador;

import com.gestion.mascotas.modelo.RecordatorioPaseo;
import com.gestion.mascotas.modelo.Usuario;
import com.gestion.mascotas.service.RecordatorioPaseoService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;

@WebServlet("/recordatorioPaseo")
public class RecordatorioPaseoServlet extends HttpServlet {

    private RecordatorioPaseoService recordatorioService;

    @Override
    public void init() throws ServletException {
        recordatorioService = new RecordatorioPaseoService();
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
                eliminarRecordatorioPaseo(request, response);
                break;
            case "desactivar":
                desactivarRecordatorioPaseo(request, response);
                break;
            case "listar":
            default:
                listarRecordatoriosPaseo(request, response);
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
            registrarRecordatorioPaseo(request, response);
        } else if ("actualizar".equals(action)) {
            actualizarRecordatorioPaseo(request, response);
        } else {
            response.sendRedirect(request.getContextPath() + "/mascota?error=accion_desconocida");
        }
    }

    private void registrarRecordatorioPaseo(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        Usuario usuario = (Usuario) session.getAttribute("usuario");

        try {
            String mascotaIdStr = request.getParameter("mascotaId");
            String frecuenciaStr = request.getParameter("frecuenciaPaseo");
            String duracionMinutosStr = request.getParameter("duracionMinutos");
            String[] diasSeleccionados = request.getParameterValues("diasSemanaPaseo");

            boolean guardado = recordatorioService.registrarRecordatorio(
                    mascotaIdStr, frecuenciaStr, duracionMinutosStr, diasSeleccionados,
                    request::getParameter, usuario
            );

            if (guardado) {
                System.out.println("Recordatorio de paseo guardado exitosamente");
                response.sendRedirect(request.getContextPath() +
                        "/mascota?action=detalles&id=" + mascotaIdStr +
                        "&success=recordatorio_paseo_registrado");
            } else {
                System.err.println("Error al guardar el recordatorio de paseo");
                response.sendRedirect(request.getContextPath() +
                        "/mascota?action=detalles&id=" + mascotaIdStr +
                        "&error=error_guardar_recordatorio_paseo");
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
                    "&error=error_registro_recordatorio_paseo");
        }
    }

    private void actualizarRecordatorioPaseo(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        Usuario usuario = (Usuario) session.getAttribute("usuario");

        try {
            Long recordatorioId = Long.parseLong(request.getParameter("recordatorioId"));
            Long mascotaId = Long.parseLong(request.getParameter("mascotaId"));
            String frecuenciaStr = request.getParameter("frecuenciaPaseo");
            String duracionMinutosStr = request.getParameter("duracionMinutos");
            String[] diasSeleccionados = request.getParameterValues("diasSemanaPaseo");

            boolean actualizado = recordatorioService.actualizarRecordatorio(
                    recordatorioId, frecuenciaStr, duracionMinutosStr, diasSeleccionados,
                    request::getParameter, usuario
            );

            if (actualizado) {
                response.sendRedirect(request.getContextPath() +
                        "/mascota?action=detalles&id=" + mascotaId +
                        "&success=recordatorio_paseo_actualizado");
            } else {
                response.sendRedirect(request.getContextPath() +
                        "/mascota?action=detalles&id=" + mascotaId +
                        "&error=error_actualizar_recordatorio_paseo");
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
                    "&error=error_actualizacion_paseo");
        }
    }

    private void eliminarRecordatorioPaseo(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        HttpSession session = request.getSession();
        Usuario usuario = (Usuario) session.getAttribute("usuario");

        try {
            String idStr = request.getParameter("id");
            String mascotaIdStr = request.getParameter("mascotaId");

            System.out.println("=== INICIO ELIMINACIÓN RECORDATORIO PASEO ===");
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
                        "&success=recordatorio_paseo_eliminado");
            } else {
                response.sendRedirect(request.getContextPath() +
                        "/mascota?action=detalles&id=" + mascotaIdStr +
                        "&error=error_eliminar_recordatorio_paseo");
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
            System.err.println("ERROR GENERAL al eliminar recordatorio de paseo");
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() +
                    "/mascota?action=detalles&id=" + request.getParameter("mascotaId") +
                    "&error=error_eliminando_recordatorio_paseo");
        } finally {
            System.out.println("=== FIN ELIMINACIÓN RECORDATORIO PASEO ===");
        }
    }

    private void desactivarRecordatorioPaseo(HttpServletRequest request, HttpServletResponse response)
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
                        "&success=recordatorio_paseo_desactivado");
            } else {
                response.sendRedirect(request.getContextPath() +
                        "/mascota?action=detalles&id=" + mascotaId +
                        "&error=error_desactivar_recordatorio_paseo");
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
                    "&error=error_desactivando_recordatorio_paseo");
        }
    }

    private void listarRecordatoriosPaseo(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            Long mascotaId = Long.parseLong(request.getParameter("mascotaId"));
            List<RecordatorioPaseo> recordatorios =
                    recordatorioService.obtenerRecordatoriosPorMascota(mascotaId);

            request.setAttribute("recordatoriosPaseo", recordatorios);
            request.setAttribute("mascotaId", mascotaId);
            request.getRequestDispatcher("/jsp/listaRecordatoriosPaseo.jsp").forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/mascota?error=error_listando_recordatorios_paseo");
        }
    }
}