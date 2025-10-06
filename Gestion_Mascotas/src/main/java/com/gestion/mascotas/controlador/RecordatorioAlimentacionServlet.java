package com.gestion.mascotas.controlador;

import com.gestion.mascotas.dao.MascotaDAO;
import com.gestion.mascotas.dao.RecordatorioAlimentacionDAO;
import com.gestion.mascotas.modelo.Mascota;
import com.gestion.mascotas.modelo.RecordatorioAlimentacion;

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
import java.util.stream.Collectors;

@WebServlet("/recordatorioAlimentacion")
public class RecordatorioAlimentacionServlet extends HttpServlet {

    private RecordatorioAlimentacionDAO recordatorioAlimentacionDAO;
    private MascotaDAO mascotaDAO;

    @Override
    public void init() throws ServletException {
        recordatorioAlimentacionDAO = new RecordatorioAlimentacionDAO();
        mascotaDAO = new MascotaDAO();
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
        if (action == null) action = "listar";

        switch (action) {
            case "eliminar":
                eliminarRecordatorioAlimentacion(request, response);
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
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("usuario") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        String action = request.getParameter("action");
        if ("registrar".equals(action)) {
            registrarRecordatorioAlimentacion(request, response);
        } else {
            response.sendRedirect(request.getContextPath() + "/recordatorioAlimentacion?error=accion_desconocida");
        }
    }

    private void registrarRecordatorioAlimentacion(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            Long mascotaId = Long.parseLong(request.getParameter("mascotaId"));
            String frecuencia = request.getParameter("frecuencia"); // "1", "2", "3"
            String tipoAlimento = request.getParameter("tipoAlimento");
            String[] diasSeleccionados = request.getParameterValues("diasSemana");

            Mascota mascota = mascotaDAO.obtenerPorId(mascotaId);
            if (mascota == null) {
                response.sendRedirect(request.getContextPath() + "/mascota?action=detalles&id=" + mascotaId + "&error=mascota_no_encontrada");
                return;
            }

            List<LocalTime> horarios = new ArrayList<>();
            for (int i = 1; i <= Integer.parseInt(frecuencia); i++) {
                String horaStr = request.getParameter("horario" + i);
                if (horaStr != null && !horaStr.isEmpty()) {
                    horarios.add(LocalTime.parse(horaStr));
                }
            }

            if (horarios.isEmpty()) {
                response.sendRedirect(request.getContextPath() + "/mascota?action=detalles&id=" + mascotaId + "&error=horarios_vacios");
                return;
            }

            String diasSemana = (diasSeleccionados != null && diasSeleccionados.length > 0) ?
                    String.join(",", diasSeleccionados) : "";

            RecordatorioAlimentacion recordatorio = new RecordatorioAlimentacion();
            recordatorio.setMascota(mascota);
            recordatorio.setActivo(true); // Por defecto activo
            recordatorio.setDescripcion("Recordatorio de alimentación para " + mascota.getNombre());
            recordatorio.setFrecuencia(frecuencia + " veces al día"); // Guardar como texto descriptivo
            recordatorio.setHorarios(horarios.stream().map(LocalTime::toString).collect(Collectors.joining(",")));
            recordatorio.setTipoAlimento(tipoAlimento);
            recordatorio.setDiasSemana(diasSemana);

            recordatorioAlimentacionDAO.guardar(recordatorio);

            response.sendRedirect(request.getContextPath() + "/mascota?action=detalles&id=" + mascotaId + "&success=recordatorio_alimentacion_registrado");

        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/mascota?action=detalles&id=" + request.getParameter("mascotaId") + "&error=formato_invalido");
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/mascota?action=detalles&id=" + request.getParameter("mascotaId") + "&error=error_registro_recordatorio_alimentacion");
        }
    }

    private void eliminarRecordatorioAlimentacion(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        Long recordatorioId = Long.parseLong(request.getParameter("id"));
        Long mascotaId = Long.parseLong(request.getParameter("mascotaId")); // Para redirigir de vuelta a los detalles de la mascota

        try {
            recordatorioAlimentacionDAO.eliminar(recordatorioId);
            response.sendRedirect(request.getContextPath() + "/mascota?action=detalles&id=" + mascotaId + "&success=recordatorio_alimentacion_eliminado");
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/mascota?action=detalles&id=" + mascotaId + "&error=error_eliminando_recordatorio_alimentacion");
        }
    }

    private void listarRecordatoriosAlimentacion(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Este método podría ser útil si se quiere una página dedicada a listar todos los recordatorios de alimentación
        // Por ahora, la lista se mostrará en los detalles de la mascota.
        Long mascotaId = Long.parseLong(request.getParameter("mascotaId"));
        List<RecordatorioAlimentacion> recordatorios = recordatorioAlimentacionDAO.obtenerRecordatoriosAlimentacionPorMascota(mascotaId);
        request.setAttribute("recordatoriosAlimentacion", recordatorios);
        request.getRequestDispatcher("/jsp/listaRecordatoriosAlimentacion.jsp").forward(request, response);
    }
}