package com.gestion.mascotas.controlador;

import com.gestion.mascotas.modelo.entidades.Mascota;
import com.gestion.mascotas.modelo.entidades.Usuario;
import com.gestion.mascotas.modelo.entidades.VisitaVeterinaria;
import com.gestion.mascotas.servicio.MascotaService; // Necesario para listar mascotas
import com.gestion.mascotas.servicio.VisitaVeterinariaService; // Importar el nuevo servicio
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@WebServlet({"/visita", "/visitas"})
public class VisitaVeterinariaServlet extends HttpServlet {

    private VisitaVeterinariaService visitaService;
    private MascotaService mascotaService;

    public void init(){
        visitaService = new VisitaVeterinariaService();
        mascotaService = new MascotaService();
    }
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
                case "nueva": // Renombrado de "registrar" para claridad en GET
                    mostrarFormularioRegistro(request, response, usuario);
                    break;
                case "eliminar":
                    eliminarVisita(request, response, usuario);
                    break;
                case "listar":
                default:
                    listarVisitas(request, response, usuario);
                    break;
            }
        } catch (Exception e) {
            // Manejo genérico de errores
            e.printStackTrace(); // Loggear el error
            session.setAttribute("error", "Ocurrió un error inesperado: " + e.getMessage());
            response.sendRedirect(request.getContextPath() + "/visitas"); // Redirigir a la lista
        }
    }

    private void mostrarFormularioRegistro(HttpServletRequest request, HttpServletResponse response, Usuario usuario)
            throws ServletException, IOException {
        // Usar el servicio de mascota para obtener la lista
        List<Mascota> mascotas = mascotaService.listarMascotasPorUsuario(usuario.getId());
        request.setAttribute("mascotas", mascotas);
        request.getRequestDispatcher("/jsp/registrarVisita.jsp").forward(request, response);
    }

    private void listarVisitas(HttpServletRequest request, HttpServletResponse response, Usuario usuario)
            throws ServletException, IOException {
        // Usar los servicios para obtener visitas y mascotas
        List<VisitaVeterinaria> visitas = visitaService.consultarHistorialPorUsuario(usuario.getId());
        List<Mascota> mascotas = mascotaService.listarMascotasPorUsuario(usuario.getId());

        request.setAttribute("visitas", visitas);
        request.setAttribute("mascotas", mascotas); // Para el modal de registro
        request.getRequestDispatcher("/jsp/listaVisitas.jsp").forward(request, response);
    }

    private void eliminarVisita(HttpServletRequest request, HttpServletResponse response, Usuario usuario)
            throws IOException {
        try {
            Long id = Long.parseLong(request.getParameter("id"));
            visitaService.eliminarVisita(id, usuario); // Llamar al servicio
            response.sendRedirect(request.getContextPath() + "/visitas?success=eliminado");
        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/visitas?error=id_invalido");
        } catch (IllegalArgumentException | SecurityException e) {
            request.getSession().setAttribute("error", e.getMessage());
            response.sendRedirect(request.getContextPath() + "/visitas");
        } catch (Exception e) {
            e.printStackTrace();
            request.getSession().setAttribute("error", "Error interno al eliminar la visita.");
            response.sendRedirect(request.getContextPath() + "/visitas");
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
            registrarVisita(request, response, usuario);
        } else {
            response.sendRedirect(request.getContextPath() + "/visitas?error=accion_desconocida");
        }
    }

    private void registrarVisita(HttpServletRequest request, HttpServletResponse response, Usuario usuario)
            throws IOException, ServletException {
        try {
            String mascotaIdStr = request.getParameter("mascotaId");
            String fechaStr = request.getParameter("fecha");
            String motivo = request.getParameter("motivo");
            String diagnostico = request.getParameter("diagnostico");
            String tratamiento = request.getParameter("tratamiento");
            String observaciones = request.getParameter("observaciones");
            String nombreVeterinario = request.getParameter("nombreVeterinario");

            System.out.println("Este es el nombre: "+nombreVeterinario);

            // Validar que los strings no sean nulos o vacíos antes de parsear
            if (mascotaIdStr == null || mascotaIdStr.trim().isEmpty() || fechaStr == null || fechaStr.isEmpty() || motivo == null || motivo.trim().isEmpty()) {
                throw new IllegalArgumentException("Todos los campos son obligatorios (Mascota, Fecha, Motivo).");
            }

            Long mascotaId = Long.parseLong(mascotaIdStr);
            LocalDate fecha = LocalDate.parse(fechaStr);

            visitaService.registrarVisita(fecha, motivo, diagnostico,tratamiento,observaciones,nombreVeterinario, mascotaId, usuario);

            response.sendRedirect(request.getContextPath() + "/visitas?success=registrado");

        } catch (IllegalArgumentException | SecurityException e) {
            // Errores de validación o permisos del servicio
            request.setAttribute("error", e.getMessage());
            mostrarFormularioRegistro(request, response, usuario); // Volver al formulario con el error
        } catch (Exception e) {
            // Otros errores (parsing, base de datos, etc.)
            e.printStackTrace(); // Loggear
            request.setAttribute("error", "Error al procesar el registro: " + e.getMessage());
            mostrarFormularioRegistro(request, response, usuario); // Volver al formulario con el error
        }
    }
}