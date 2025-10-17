package com.gestion.mascotas.controlador;

import com.gestion.mascotas.dao.MascotaDAO;
import com.gestion.mascotas.dao.RecordatorioPaseoDAO;
import com.gestion.mascotas.modelo.entidades.Mascota;
import com.gestion.mascotas.modelo.entidades.RecordatorioPaseo;
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

@WebServlet("/recordatorioPaseo")
public class RecordatorioPaseoServlet extends HttpServlet {

    private RecordatorioPaseoDAO recordatorioPaseoDAO;
    private MascotaDAO mascotaDAO;

    @Override
    public void init() throws ServletException {
        recordatorioPaseoDAO = new RecordatorioPaseoDAO();
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

    /**
     * Registrar un nuevo recordatorio de paseo
     */
    private void registrarRecordatorioPaseo(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        Usuario usuario = (Usuario) session.getAttribute("usuario");

        try {
            // Obtener parámetros del formulario
            String mascotaIdStr = request.getParameter("mascotaId");
            String frecuenciaStr = request.getParameter("frecuenciaPaseo");
            String duracionMinutosStr = request.getParameter("duracionMinutos");
            String[] diasSeleccionados = request.getParameterValues("diasSemanaPaseo");

            // Validaciones básicas
            if (mascotaIdStr == null || mascotaIdStr.trim().isEmpty()) {
                response.sendRedirect(request.getContextPath() +
                        "/mascota?action=listar&error=mascota_id_invalido");
                return;
            }

            if (frecuenciaStr == null || frecuenciaStr.trim().isEmpty()) {
                response.sendRedirect(request.getContextPath() +
                        "/mascota?action=detalles&id=" + mascotaIdStr +
                        "&error=frecuencia_paseo_vacia");
                return;
            }

            Long mascotaId = Long.parseLong(mascotaIdStr);
            int frecuencia = Integer.parseInt(frecuenciaStr);

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

            System.out.println("Numero de horarios esperados para paseo: " + frecuencia);

            for (int i = 1; i <= frecuencia; i++) {
                String paramName = "horarioPaseo" + i;
                String horaStr = request.getParameter(paramName);

                System.out.println("Parametro " + paramName + ": " + horaStr);

                if (horaStr != null && !horaStr.trim().isEmpty()) {
                    try {
                        LocalTime hora = LocalTime.parse(horaStr);
                        horarios.add(hora);
                        System.out.println("Hora de paseo agregada: " + hora);
                    } catch (Exception e) {
                        System.err.println("Error parseando hora de paseo: " + horaStr);
                        e.printStackTrace();
                        response.sendRedirect(request.getContextPath() +
                                "/mascota?action=detalles&id=" + mascotaId +
                                "&error=formato_hora_invalido");
                        return;
                    }
                }
            }

            System.out.println("Total horarios de paseo capturados: " + horarios.size());

            // Validar que se ingresaron horarios
            if (horarios.isEmpty()) {
                System.err.println("ERROR: No se recibieron horarios de paseo");
                response.sendRedirect(request.getContextPath() +
                        "/mascota?action=detalles&id=" + mascotaId +
                        "&error=horarios_paseo_vacios");
                return;
            }

            // Validar duración
            Integer duracionMinutos = null;
            if (duracionMinutosStr != null && !duracionMinutosStr.trim().isEmpty()) {
                duracionMinutos = Integer.parseInt(duracionMinutosStr);
                if (duracionMinutos <= 0) {
                    response.sendRedirect(request.getContextPath() +
                            "/mascota?action=detalles&id=" + mascotaId +
                            "&error=duracion_invalida");
                    return;
                }
            }

            // Construir string de días de la semana
            String diasSemana = "";
            if (diasSeleccionados != null && diasSeleccionados.length > 0) {
                diasSemana = String.join(",", diasSeleccionados);
                System.out.println("Dias seleccionados para paseo: " + diasSemana);
            } else {
                System.out.println("Sin dias especificos - activo todos los dias");
            }

            // Crear el recordatorio de paseo
            RecordatorioPaseo recordatorio = new RecordatorioPaseo();
            recordatorio.setMascota(mascota);
            recordatorio.setActivo(true);

            String descripcion = "Recordatorio de paseo: " + frecuencia + " vez/veces al día";
            if (duracionMinutos != null) {
                descripcion += " - " + duracionMinutos + " minutos";
            }
            recordatorio.setDescripcion(descripcion);

            String horariosStr = horarios.stream()
                    .map(LocalTime::toString)
                    .collect(Collectors.joining(","));

            recordatorio.setHorarios(horariosStr);
            recordatorio.setDiasSemana(diasSemana);
            recordatorio.setDuracionMinutos(duracionMinutos);
            recordatorio.setFechaHoraRecordatorio(LocalDateTime.now());

            System.out.println("Guardando recordatorio de paseo: " + recordatorio);

            // Guardar en la base de datos
            boolean guardado = recordatorioPaseoDAO.guardar(recordatorio);

            if (guardado) {
                System.out.println("Recordatorio de paseo guardado exitosamente");
                response.sendRedirect(request.getContextPath() +
                        "/mascota?action=detalles&id=" + mascotaId +
                        "&success=recordatorio_paseo_registrado");
            } else {
                System.err.println("Error al guardar el recordatorio de paseo");
                response.sendRedirect(request.getContextPath() +
                        "/mascota?action=detalles&id=" + mascotaId +
                        "&error=error_guardar_recordatorio_paseo");
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
                    "&error=error_registro_recordatorio_paseo");
        }
    }

    /**
     * Actualizar un recordatorio de paseo existente
     */
    private void actualizarRecordatorioPaseo(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        Usuario usuario = (Usuario) session.getAttribute("usuario");

        try {
            Long recordatorioId = Long.parseLong(request.getParameter("recordatorioId"));
            Long mascotaId = Long.parseLong(request.getParameter("mascotaId"));

            RecordatorioPaseo recordatorio = recordatorioPaseoDAO.obtenerPorId(recordatorioId);

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
            String frecuenciaStr = request.getParameter("frecuenciaPaseo");
            String duracionMinutosStr = request.getParameter("duracionMinutos");
            String[] diasSeleccionados = request.getParameterValues("diasSemanaPaseo");

            int frecuencia = Integer.parseInt(frecuenciaStr);
            List<LocalTime> horarios = new ArrayList<>();

            for (int i = 1; i <= frecuencia; i++) {
                String horaStr = request.getParameter("horarioPaseo" + i);
                if (horaStr != null && !horaStr.trim().isEmpty()) {
                    horarios.add(LocalTime.parse(horaStr));
                }
            }

            Integer duracionMinutos = null;
            if (duracionMinutosStr != null && !duracionMinutosStr.trim().isEmpty()) {
                duracionMinutos = Integer.parseInt(duracionMinutosStr);
            }

            String diasSemana = "";
            if (diasSeleccionados != null && diasSeleccionados.length > 0) {
                diasSemana = String.join(",", diasSeleccionados);
            }

            recordatorio.setHorarios(horarios.stream()
                    .map(LocalTime::toString)
                    .collect(Collectors.joining(",")));
            recordatorio.setDiasSemana(diasSemana);
            recordatorio.setDuracionMinutos(duracionMinutos);

            String descripcion = "Recordatorio de paseo: " + frecuencia + " vez/veces al día";
            if (duracionMinutos != null) {
                descripcion += " - " + duracionMinutos + " minutos";
            }
            recordatorio.setDescripcion(descripcion);

            boolean actualizado = recordatorioPaseoDAO.actualizar(recordatorio);

            if (actualizado) {
                response.sendRedirect(request.getContextPath() +
                        "/mascota?action=detalles&id=" + mascotaId +
                        "&success=recordatorio_paseo_actualizado");
            } else {
                response.sendRedirect(request.getContextPath() +
                        "/mascota?action=detalles&id=" + mascotaId +
                        "&error=error_actualizar_recordatorio_paseo");
            }

        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() +
                    "/mascota?action=detalles&id=" + request.getParameter("mascotaId") +
                    "&error=error_actualizacion_paseo");
        }
    }

    /**
     * Eliminar un recordatorio de paseo
     */
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
            RecordatorioPaseo recordatorio = recordatorioPaseoDAO.obtenerPorId(recordatorioId);

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

            System.out.println("Intentando eliminar recordatorio de paseo...");
            boolean eliminado = recordatorioPaseoDAO.eliminar(recordatorioId);

            if (eliminado) {
                System.out.println("✓ Recordatorio de paseo eliminado exitosamente");
                response.sendRedirect(request.getContextPath() +
                        "/mascota?action=detalles&id=" + mascotaId +
                        "&success=recordatorio_paseo_eliminado");
            } else {
                System.err.println("✗ Error al eliminar el recordatorio de paseo - el DAO retornó false");
                response.sendRedirect(request.getContextPath() +
                        "/mascota?action=detalles&id=" + mascotaId +
                        "&error=error_eliminar_recordatorio_paseo");
            }

        } catch (NumberFormatException e) {
            System.err.println("ERROR: Formato de número inválido");
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() +
                    "/mascota?action=detalles&id=" + request.getParameter("mascotaId") +
                    "&error=formato_id_invalido");
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

    /**
     * Desactivar un recordatorio sin eliminarlo
     */
    private void desactivarRecordatorioPaseo(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        HttpSession session = request.getSession();
        Usuario usuario = (Usuario) session.getAttribute("usuario");

        try {
            Long recordatorioId = Long.parseLong(request.getParameter("id"));
            Long mascotaId = Long.parseLong(request.getParameter("mascotaId"));

            RecordatorioPaseo recordatorio = recordatorioPaseoDAO.obtenerPorId(recordatorioId);

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

            boolean desactivado = recordatorioPaseoDAO.desactivarRecordatorio(recordatorioId);

            if (desactivado) {
                response.sendRedirect(request.getContextPath() +
                        "/mascota?action=detalles&id=" + mascotaId +
                        "&success=recordatorio_paseo_desactivado");
            } else {
                response.sendRedirect(request.getContextPath() +
                        "/mascota?action=detalles&id=" + mascotaId +
                        "&error=error_desactivar_recordatorio_paseo");
            }

        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() +
                    "/mascota?action=detalles&id=" + request.getParameter("mascotaId") +
                    "&error=error_desactivando_recordatorio_paseo");
        }
    }

    /**
     * Listar recordatorios de paseo de una mascota
     */
    private void listarRecordatoriosPaseo(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            Long mascotaId = Long.parseLong(request.getParameter("mascotaId"));
            List<RecordatorioPaseo> recordatorios =
                    recordatorioPaseoDAO.obtenerRecordatoriosPaseoPorMascota(mascotaId);

            request.setAttribute("recordatoriosPaseo", recordatorios);
            request.setAttribute("mascotaId", mascotaId);
            request.getRequestDispatcher("/jsp/listaRecordatoriosPaseo.jsp").forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/mascota?error=error_listando_recordatorios_paseo");
        }
    }
}