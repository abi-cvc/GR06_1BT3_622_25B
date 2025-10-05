// FileName: MultipleFiles/RecordatorioServlet.java
package com.gestion.mascotas.controlador;

import com.gestion.mascotas.dao.MascotaDAO;
import com.gestion.mascotas.dao.RecordatorioAlimentacionDAO;
import com.gestion.mascotas.dao.RecordatorioDAO;
import com.gestion.mascotas.dao.RecordatorioPaseoDAO;
import com.gestion.mascotas.modelo.Mascota;
import com.gestion.mascotas.modelo.Recordatorio;
import com.gestion.mascotas.modelo.RecordatorioAlimentacion;
import com.gestion.mascotas.modelo.RecordatorioPaseo;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@WebServlet("/recordatorio")
public class RecordatorioServlet extends HttpServlet {

    private RecordatorioDAO recordatorioDAO;
    private RecordatorioAlimentacionDAO recordatorioAlimentacionDAO;
    private RecordatorioPaseoDAO recordatorioPaseoDAO;
    private MascotaDAO mascotaDAO;

    @Override
    public void init() throws ServletException {
        recordatorioDAO = new RecordatorioDAO();
        recordatorioAlimentacionDAO = new RecordatorioAlimentacionDAO();
        recordatorioPaseoDAO = new RecordatorioPaseoDAO();
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
            case "registrar":
                mostrarFormularioRegistro(request, response);
                break;
            case "editar":
                mostrarFormularioEdicion(request, response);
                break;
            case "eliminar":
                eliminarRecordatorio(request, response);
                break;
            case "listarPorMascota":
                listarRecordatoriosPorMascota(request, response);
                break;
            default:
                listarTodosRecordatorios(request, response);
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

        switch (action) {
            case "registrar":
                registrarRecordatorio(request, response);
                break;
            case "actualizar":
                actualizarRecordatorio(request, response);
                break;
            default:
                response.sendRedirect(request.getContextPath() + "/recordatorio?error=accion_desconocida");
                break;
        }
    }

    private void mostrarFormularioRegistro(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<Mascota> mascotas = mascotaDAO.obtenerTodas();
        request.setAttribute("mascotas", mascotas);
        request.getRequestDispatcher("/jsp/recordatorio/registrarRecordatorio.jsp").forward(request, response);
    }

    private void mostrarFormularioEdicion(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Long id = Long.parseLong(request.getParameter("id"));
        Recordatorio recordatorio = recordatorioDAO.obtenerPorId(id);

        if (recordatorio == null) {
            request.setAttribute("error", "Recordatorio no encontrado.");
            listarTodosRecordatorios(request, response);
            return;
        }

        request.setAttribute("recordatorio", recordatorio);
        List<Mascota> mascotas = mascotaDAO.obtenerTodas();
        request.setAttribute("mascotas", mascotas);

        if (recordatorio instanceof RecordatorioAlimentacion) {
            request.getRequestDispatcher("/jsp/recordatorio/editarRecordatorioAlimentacion.jsp").forward(request, response);
        } else if (recordatorio instanceof RecordatorioPaseo) {
            request.getRequestDispatcher("/jsp/recordatorio/editarRecordatorioPaseo.jsp").forward(request, response);
        } else {
            request.getRequestDispatcher("/jsp/recordatorio/editarRecordatorioGenerico.jsp").forward(request, response);
        }
    }

    private void registrarRecordatorio(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String tipoRecordatorio = request.getParameter("tipoRecordatorio");
        Long mascotaId = Long.parseLong(request.getParameter("mascotaId"));
        String descripcion = request.getParameter("descripcion");
        LocalDateTime fechaHora = LocalDateTime.parse(request.getParameter("fechaHora"));

        Mascota mascota = mascotaDAO.obtenerPorId(mascotaId);
        if (mascota == null) {
            request.setAttribute("error", "Mascota no encontrada.");
            mostrarFormularioRegistro(request, response);
            return;
        }

        try {
            if ("alimentacion".equals(tipoRecordatorio)) {
                RecordatorioAlimentacion ra = new RecordatorioAlimentacion();
                String frecuencia = request.getParameter("frecuencia");
                String diasSemana = request.getParameter("diasSemana");
                String horarios = request.getParameter("horarios");
                String tipoAlimento = request.getParameter("tipoAlimento");
                ra.configurarRecordatorio(mascota, descripcion, fechaHora, frecuencia, diasSemana, horarios, tipoAlimento);
                recordatorioAlimentacionDAO.guardar(ra);
            } else if ("paseo".equals(tipoRecordatorio)) {
                RecordatorioPaseo rp = new RecordatorioPaseo();
                String diasSemana = request.getParameter("diasSemana");
                String horarios = request.getParameter("horarios");
                Integer duracionMinutos = Integer.parseInt(request.getParameter("duracionMinutos"));
                rp.configurarRecordatorio(mascota, descripcion, fechaHora, diasSemana, horarios, duracionMinutos);
                recordatorioPaseoDAO.guardar(rp);
            } else {
                // Default or generic recordatorio if needed, though current model implies specific types
                request.setAttribute("error", "Tipo de recordatorio no válido.");
                mostrarFormularioRegistro(request, response);
                return;
            }
            response.sendRedirect(request.getContextPath() + "/recordatorio?success=true");
        } catch (Exception e) {
            request.setAttribute("error", "Error al registrar el recordatorio: " + e.getMessage());
            mostrarFormularioRegistro(request, response);
        }
    }

    private void actualizarRecordatorio(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Long id = Long.parseLong(request.getParameter("id"));
        String descripcion = request.getParameter("descripcion");
        LocalDateTime fechaHora = LocalDateTime.parse(request.getParameter("fechaHora"));
        boolean activo = Boolean.parseBoolean(request.getParameter("activo"));
        Long mascotaId = Long.parseLong(request.getParameter("mascotaId"));

        Recordatorio recordatorio = recordatorioDAO.obtenerPorId(id);
        if (recordatorio == null) {
            request.setAttribute("error", "Recordatorio no encontrado para actualizar.");
            mostrarFormularioEdicion(request, response);
            return;
        }

        Mascota mascota = mascotaDAO.obtenerPorId(mascotaId);
        if (mascota == null) {
            request.setAttribute("error", "Mascota no encontrada.");
            mostrarFormularioEdicion(request, response);
            return;
        }
        recordatorio.setMascota(mascota); // Update mascota if changed

        try {
            recordatorio.modificarRecordatorio(descripcion, fechaHora, activo);

            if (recordatorio instanceof RecordatorioAlimentacion) {
                RecordatorioAlimentacion ra = (RecordatorioAlimentacion) recordatorio;
                String frecuencia = request.getParameter("frecuencia");
                String diasSemana = request.getParameter("diasSemana");
                String horarios = request.getParameter("horarios");
                String tipoAlimento = request.getParameter("tipoAlimento");
                ra.modificarDetallesAlimentacion(frecuencia, diasSemana, horarios, tipoAlimento);
                recordatorioAlimentacionDAO.actualizar(ra);
            } else if (recordatorio instanceof RecordatorioPaseo) {
                RecordatorioPaseo rp = (RecordatorioPaseo) recordatorio;
                String diasSemana = request.getParameter("diasSemana");
                String horarios = request.getParameter("horarios");
                Integer duracionMinutos = Integer.parseInt(request.getParameter("duracionMinutos"));
                rp.modificarDetallesPaseo(diasSemana, horarios, duracionMinutos);
                recordatorioPaseoDAO.actualizar(rp);
            } else {
                recordatorioDAO.actualizar(recordatorio); // For generic recordatorios if they existed
            }
            response.sendRedirect(request.getContextPath() + "/recordatorio?success=true");
        } catch (Exception e) {
            request.setAttribute("error", "Error al actualizar el recordatorio: " + e.getMessage());
            mostrarFormularioEdicion(request, response);
        }
    }

    private void eliminarRecordatorio(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        Long id = Long.parseLong(request.getParameter("id"));
        try {
            recordatorioDAO.eliminar(id); // This will handle specific types via polymorphism if needed, or just delete the base record
            response.sendRedirect(request.getContextPath() + "/recordatorio?success=eliminado");
        } catch (Exception e) {
            request.setAttribute("error", "Error al eliminar el recordatorio: " + e.getMessage());
            listarTodosRecordatorios(request, response);
        }
    }

    private void listarTodosRecordatorios(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<Recordatorio> recordatorios = recordatorioDAO.obtenerTodos();
        request.setAttribute("recordatorios", recordatorios);
        request.getRequestDispatcher("/jsp/recordatorio/listaRecordatorios.jsp").forward(request, response);
    }

    private void listarRecordatoriosPorMascota(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Long mascotaId = Long.parseLong(request.getParameter("mascotaId"));
        List<Recordatorio> recordatorios = recordatorioDAO.obtenerRecordatoriosPorMascota(mascotaId);
        request.setAttribute("recordatorios", recordatorios);
        request.getRequestDispatcher("/jsp/recordatorio/listaRecordatorios.jsp").forward(request, response);
    }
}