package com.gestion.mascotas.controlador;

import com.gestion.mascotas.dao.MascotaDAO;
import com.gestion.mascotas.dao.RecordatorioAlimentacionDAO;
import com.gestion.mascotas.modelo.entidades.Mascota;
import com.gestion.mascotas.modelo.entidades.RecordatorioAlimentacion;
import com.gestion.mascotas.modelo.entidades.Usuario;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.time.LocalDateTime;
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
            // Obtener parámetros del formulario
            String mascotaIdStr = request.getParameter("mascotaId");
            String frecuencia = request.getParameter("frecuencia");
            String tipoAlimento = request.getParameter("tipoAlimento");
            String[] diasSeleccionados = request.getParameterValues("diasSemana");


            // Validaciones básicas
            if (mascotaIdStr == null || mascotaIdStr.trim().isEmpty()) {
                response.sendRedirect(request.getContextPath() +
                        "/mascota?action=listar&error=mascota_id_invalido");
                return;
            }

            if (frecuencia == null || frecuencia.trim().isEmpty()) {
                response.sendRedirect(request.getContextPath() +
                        "/mascota?action=detalles&id=" + mascotaIdStr +
                        "&error=frecuencia_vacia");
                return;
            }

            Long mascotaId = Long.parseLong(mascotaIdStr);

            // Validar que la mascota existe y pertenece al usuario
            Mascota mascota = mascotaDAO.obtenerPorId(mascotaId);
            if (mascota == null) {
                response.sendRedirect(request.getContextPath() +
                        "/mascota?action=detalles&id=" + mascotaId +
                        "&error=mascota_no_encontrada");
                return;
            }

            // Verificar que la mascota pertenece al usuario
            if (!mascota.getUsuario().getId().equals(usuario.getId())) {
                response.sendRedirect(request.getContextPath() +
                        "/mascota?action=detalles&id=" + mascotaId +
                        "&error=acceso_denegado");
                return;
            }

            // Obtener los horarios del formulario
            List<LocalTime> horarios = new ArrayList<>();
            int numHorarios = Integer.parseInt(frecuencia);

            System.out.println("Numero de horarios esperados: " + numHorarios);

            for (int i = 1; i <= numHorarios; i++) {
                String paramName = "horario" + i;
                String horaStr = request.getParameter(paramName);

                System.out.println("Parametro " + paramName + ": " + horaStr);

                if (horaStr != null && !horaStr.trim().isEmpty()) {
                    try {
                        LocalTime hora = LocalTime.parse(horaStr);
                        horarios.add(hora);
                        System.out.println("Hora agregada: " + hora);
                    } catch (Exception e) {
                        System.err.println("Error parseando hora: " + horaStr);
                        e.printStackTrace();
                        response.sendRedirect(request.getContextPath() +
                                "/mascota?action=detalles&id=" + mascotaId +
                                "&error=formato_hora_invalido");
                        return;
                    }
                }
            }

            System.out.println("Total horarios capturados: " + horarios.size());

            // Validar que se ingresaron horarios
            if (horarios.isEmpty()) {
                System.err.println("ERROR: No se recibieron horarios");
                response.sendRedirect(request.getContextPath() +
                        "/mascota?action=detalles&id=" + mascotaId +
                        "&error=horarios_vacios");
                return;
            }

            // Validar tipo de alimento
            if (tipoAlimento == null || tipoAlimento.trim().isEmpty()) {
                response.sendRedirect(request.getContextPath() +
                        "/mascota?action=detalles&id=" + mascotaId +
                        "&error=tipo_alimento_vacio");
                return;
            }

            // Construir string de días de la semana
            String diasSemana = "";
            if (diasSeleccionados != null && diasSeleccionados.length > 0) {
                diasSemana = String.join(",", diasSeleccionados);
                System.out.println("Dias seleccionados: " + diasSemana);
            } else {
                System.out.println("Sin dias especificos - activo todos los dias");
            }

            // Crear el recordatorio de alimentación
            RecordatorioAlimentacion recordatorio = new RecordatorioAlimentacion();
            recordatorio.setMascota(mascota);
            recordatorio.setActivo(true);
            recordatorio.setDescripcion("Recordatorio de alimentación: " +
                    frecuencia + " vez/veces al día - " + tipoAlimento);
            recordatorio.setFrecuencia(frecuencia);

            String horariosStr = horarios.stream()
                    .map(LocalTime::toString)
                    .collect(Collectors.joining(","));

            recordatorio.setHorarios(horariosStr);
            recordatorio.setTipoAlimento(tipoAlimento.trim());
            recordatorio.setDiasSemana(diasSemana);
            recordatorio.setFechaHoraRecordatorio(LocalDateTime.now());

            System.out.println("Guardando recordatorio: " + recordatorio);

            // Guardar en la base de datos
            boolean guardado = recordatorioAlimentacionDAO.guardar(recordatorio);

            if (guardado) {
                System.out.println("Recordatorio guardado exitosamente");
                response.sendRedirect(request.getContextPath() +
                        "/mascota?action=detalles&id=" + mascotaId +
                        "&success=recordatorio_alimentacion_registrado");
            } else {
                System.err.println("Error al guardar el recordatorio");
                response.sendRedirect(request.getContextPath() +
                        "/mascota?action=detalles&id=" + mascotaId +
                        "&error=error_guardar_recordatorio");
            }

        } catch (NumberFormatException e) {
            e.printStackTrace();
            System.err.println("Error de formato numerico: " + e.getMessage());
            response.sendRedirect(request.getContextPath() +
                    "/mascota?action=detalles&id=" + request.getParameter("mascotaId") +
                    "&error=formato_invalido");
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

            RecordatorioAlimentacion recordatorio = recordatorioAlimentacionDAO.obtenerPorId(recordatorioId);

            if (recordatorio == null) {
                response.sendRedirect(request.getContextPath() +
                        "/mascota?action=detalles&id=" + mascotaId +
                        "&error=recordatorio_no_encontrado");
                return;
            }

            // Verificar permisos
            if (!recordatorio.getMascota().getUsuario().getId().equals(usuario.getId())) {
                response.sendRedirect(request.getContextPath() +
                        "/mascota?action=detalles&id=" + mascotaId +
                        "&error=acceso_denegado");
                return;
            }

            // Actualizar datos
            String frecuencia = request.getParameter("frecuencia");
            String tipoAlimento = request.getParameter("tipoAlimento");
            String[] diasSeleccionados = request.getParameterValues("diasSemana");

            List<LocalTime> horarios = new ArrayList<>();
            int numHorarios = Integer.parseInt(frecuencia);

            for (int i = 1; i <= numHorarios; i++) {
                String horaStr = request.getParameter("horario" + i);
                if (horaStr != null && !horaStr.trim().isEmpty()) {
                    horarios.add(LocalTime.parse(horaStr));
                }
            }

            String diasSemana = "";
            if (diasSeleccionados != null && diasSeleccionados.length > 0) {
                diasSemana = String.join(",", diasSeleccionados);
            }

            recordatorio.setFrecuencia(frecuencia);
            recordatorio.setHorarios(horarios.stream()
                    .map(LocalTime::toString)
                    .collect(Collectors.joining(",")));
            recordatorio.setTipoAlimento(tipoAlimento.trim());
            recordatorio.setDiasSemana(diasSemana);
            recordatorio.setDescripcion("Recordatorio de alimentación: " +
                    frecuencia + " vez/veces al día - " + tipoAlimento);

            boolean actualizado = recordatorioAlimentacionDAO.actualizar(recordatorio);

            if (actualizado) {
                response.sendRedirect(request.getContextPath() +
                        "/mascota?action=detalles&id=" + mascotaId +
                        "&success=recordatorio_actualizado");
            } else {
                response.sendRedirect(request.getContextPath() +
                        "/mascota?action=detalles&id=" + mascotaId +
                        "&error=error_actualizar_recordatorio");
            }

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

            if (idStr == null || idStr.trim().isEmpty()) {
                System.err.println("ERROR: ID de recordatorio es nulo o vacío");
                response.sendRedirect(request.getContextPath() +
                        "/mascota?action=detalles&id=" + mascotaIdStr +
                        "&error=id_recordatorio_invalido");
                return;
            }

            if (mascotaIdStr == null || mascotaIdStr.trim().isEmpty()) {
                System.err.println("ERROR: ID de mascota es nulo o vacío");
                response.sendRedirect(request.getContextPath() +
                        "/mascota?action=listar&error=mascota_id_invalido");
                return;
            }

            Long recordatorioId = Long.parseLong(idStr);
            Long mascotaId = Long.parseLong(mascotaIdStr);

            System.out.println("Buscando recordatorio con ID: " + recordatorioId);
            RecordatorioAlimentacion recordatorio = recordatorioAlimentacionDAO.obtenerPorId(recordatorioId);

            if (recordatorio == null) {
                System.err.println("ERROR: No se encontró el recordatorio con ID: " + recordatorioId);
                response.sendRedirect(request.getContextPath() +
                        "/mascota?action=detalles&id=" + mascotaId +
                        "&error=recordatorio_no_encontrado");
                return;
            }

            System.out.println("Recordatorio encontrado: " + recordatorio);
            System.out.println("Usuario del recordatorio: " + recordatorio.getMascota().getUsuario().getId());
            System.out.println("Usuario actual: " + usuario.getId());

            // Verificar permisos
            if (!recordatorio.getMascota().getUsuario().getId().equals(usuario.getId())) {
                System.err.println("ERROR: Usuario no tiene permisos para eliminar este recordatorio");
                response.sendRedirect(request.getContextPath() +
                        "/mascota?action=detalles&id=" + mascotaId +
                        "&error=acceso_denegado");
                return;
            }

            System.out.println("Intentando eliminar recordatorio...");
            boolean eliminado = recordatorioAlimentacionDAO.eliminar(recordatorioId);

            if (eliminado) {
                System.out.println("✓ Recordatorio eliminado exitosamente");
                response.sendRedirect(request.getContextPath() +
                        "/mascota?action=detalles&id=" + mascotaId +
                        "&success=recordatorio_alimentacion_eliminado");
            } else {
                System.err.println("✗ Error al eliminar el recordatorio - el DAO retornó false");
                response.sendRedirect(request.getContextPath() +
                        "/mascota?action=detalles&id=" + mascotaId +
                        "&error=error_eliminar_recordatorio");
            }

        } catch (NumberFormatException e) {
            System.err.println("ERROR: Formato de número inválido");
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() +
                    "/mascota?action=detalles&id=" + request.getParameter("mascotaId") +
                    "&error=formato_id_invalido");
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

            RecordatorioAlimentacion recordatorio = recordatorioAlimentacionDAO.obtenerPorId(recordatorioId);

            if (recordatorio == null) {
                response.sendRedirect(request.getContextPath() +
                        "/mascota?action=detalles&id=" + mascotaId +
                        "&error=recordatorio_no_encontrado");
                return;
            }

            // Verificar permisos
            if (!recordatorio.getMascota().getUsuario().getId().equals(usuario.getId())) {
                response.sendRedirect(request.getContextPath() +
                        "/mascota?action=detalles&id=" + mascotaId +
                        "&error=acceso_denegado");
                return;
            }

            boolean desactivado = recordatorioAlimentacionDAO.desactivarRecordatorio(recordatorioId);

            if (desactivado) {
                response.sendRedirect(request.getContextPath() +
                        "/mascota?action=detalles&id=" + mascotaId +
                        "&success=recordatorio_desactivado");
            } else {
                response.sendRedirect(request.getContextPath() +
                        "/mascota?action=detalles&id=" + mascotaId +
                        "&error=error_desactivar_recordatorio");
            }

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
                    recordatorioAlimentacionDAO.obtenerRecordatoriosAlimentacionPorMascota(mascotaId);

            request.setAttribute("recordatoriosAlimentacion", recordatorios);
            request.setAttribute("mascotaId", mascotaId);
            request.getRequestDispatcher("/jsp/listaRecordatoriosAlimentacion.jsp").forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/mascota?error=error_listando_recordatorios");
        }
    }
}