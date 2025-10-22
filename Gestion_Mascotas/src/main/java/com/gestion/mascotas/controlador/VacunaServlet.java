package com.gestion.mascotas.controlador;

import com.gestion.mascotas.modelo.entidades.Mascota;
import com.gestion.mascotas.modelo.entidades.Usuario;
import com.gestion.mascotas.modelo.entidades.Vacuna;
import com.gestion.mascotas.servicio.MascotaService; // Necesario para listar mascotas en el form
import com.gestion.mascotas.servicio.VacunaService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@WebServlet({"/vacunas", "/vacuna"})
public class VacunaServlet extends HttpServlet {

    private VacunaService vacunaService = new VacunaService();
    private MascotaService mascotaService = new MascotaService(); // Para obtener la lista de mascotas

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("usuario") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        Usuario usuario = (Usuario) session.getAttribute("usuario");
        String action = request.getParameter("action");
        if (action == null) action = "listar";

        try {
            switch (action) {
                case "eliminar":
                    eliminarVacuna(request, response, usuario);
                    break;
                case "listar":
                default:
                    listarVacunas(request, response, usuario);
                    break;
            }
        } catch (Exception e) {
            // Manejo genérico de errores
            e.printStackTrace(); // Loggear el error
            session.setAttribute("error", "Ocurrió un error inesperado: " + e.getMessage());
            response.sendRedirect(request.getContextPath() + "/vacunas"); // Redirigir a la lista
        }
    }

    private void listarVacunas(HttpServletRequest request, HttpServletResponse response, Usuario usuario)
            throws ServletException, IOException {
        // Obtener vacunas y mascotas usando los servicios
        List<Vacuna> vacunas = vacunaService.consultarVacunasPorUsuario(usuario.getId());
        List<Mascota> mascotas = mascotaService.listarMascotasPorUsuario(usuario.getId());

        request.setAttribute("vacunas", vacunas);
        request.setAttribute("mascotas", mascotas); // Para el modal de registro
        request.getRequestDispatcher("/jsp/listaVacunas.jsp").forward(request, response);
    }

    private void eliminarVacuna(HttpServletRequest request, HttpServletResponse response, Usuario usuario)
            throws IOException {
        try {
            Long id = Long.parseLong(request.getParameter("id"));
            vacunaService.eliminarVacuna(id, usuario);
            response.sendRedirect(request.getContextPath() + "/vacunas?success=eliminado");
        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/vacunas?error=id_invalido");
        } catch (IllegalArgumentException | SecurityException e) {
            request.getSession().setAttribute("error", e.getMessage());
            response.sendRedirect(request.getContextPath() + "/vacunas");
        } catch (Exception e) {
            e.printStackTrace();
            request.getSession().setAttribute("error", "Error interno al eliminar la vacuna.");
            response.sendRedirect(request.getContextPath() + "/vacunas");
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
        Usuario usuario = (Usuario) session.getAttribute("usuario");

        String action = request.getParameter("action");
        if ("registrar".equals(action)) {
            registrarVacuna(request, response, usuario);
        } else {
            response.sendRedirect(request.getContextPath() + "/vacunas?error=accion_desconocida");
        }
    }

    private void registrarVacuna(HttpServletRequest request, HttpServletResponse response, Usuario usuario)
            throws IOException, ServletException {
        try {
            String nombre = request.getParameter("nombre");
            String fechaStr = request.getParameter("fecha");
            String mascotaIdStr = request.getParameter("mascotaId");

            // Validar que los strings no sean nulos o vacíos antes de parsear
            if (nombre == null || nombre.trim().isEmpty() || fechaStr == null || fechaStr.isEmpty() || mascotaIdStr == null || mascotaIdStr.isEmpty()) {
                throw new IllegalArgumentException("Todos los campos son obligatorios.");
            }

            LocalDate fecha = LocalDate.parse(fechaStr);
            Long mascotaId = Long.parseLong(mascotaIdStr);

            vacunaService.registrarVacuna(nombre, fecha, mascotaId, usuario);

            response.sendRedirect(request.getContextPath() + "/vacunas?success=registrado");

        } catch (IllegalArgumentException | SecurityException e) {
            // Errores de validación o permisos del servicio
            request.setAttribute("error", e.getMessage());
            // Recargar mascotas para el formulario
            List<Mascota> mascotas = mascotaService.listarMascotasPorUsuario(usuario.getId());
            request.setAttribute("mascotas", mascotas);
            request.getRequestDispatcher("/jsp/listaVacunas.jsp").forward(request, response); // Mostrar error en la misma página
        } catch (Exception e) {
            // Otros errores (parsing, base de datos, etc.)
            e.printStackTrace(); // Loggear
            request.setAttribute("error", "Error al procesar el registro: " + e.getMessage());
            // Recargar mascotas para el formulario
            List<Mascota> mascotas = mascotaService.listarMascotasPorUsuario(usuario.getId());
            request.setAttribute("mascotas", mascotas);
            request.getRequestDispatcher("/jsp/listaVacunas.jsp").forward(request, response); // Mostrar error en la misma página
        }
    }
}