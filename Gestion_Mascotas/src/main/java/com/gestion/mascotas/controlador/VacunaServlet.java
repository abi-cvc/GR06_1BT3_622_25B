package com.gestion.mascotas.controlador;

import com.gestion.mascotas.dao.MascotaDAO;
import com.gestion.mascotas.dao.VacunaDAO;
import com.gestion.mascotas.modelo.Mascota;
import com.gestion.mascotas.modelo.Vacuna;
import com.gestion.mascotas.modelo.Usuario;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@WebServlet({"/vacunas", "/vacuna"})
public class VacunaServlet extends HttpServlet {

    private VacunaDAO vacunaDAO = new VacunaDAO();
    private MascotaDAO mascotaDAO = new MascotaDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Verificar que el usuario esté logueado
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("usuario") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        Usuario usuario = (Usuario) session.getAttribute("usuario");
        String action = request.getParameter("action");

        if (action == null) {
            action = "listar";
        }

        switch (action) {
            case "eliminar":
                eliminarVacuna(request, response);
                break;
            case "listar":
            default:
                listarVacunas(request, response, usuario);
                break;
        }
    }

    private void listarVacunas(HttpServletRequest request, HttpServletResponse response, Usuario usuario)
            throws ServletException, IOException {
        try {
            // Obtener todas las vacunas del sistema (puedes filtrar por usuario si lo deseas)
            List<Vacuna> vacunas = vacunaDAO.obtenerTodas();

            // Obtener las mascotas del usuario para el modal de registro
            List<Mascota> mascotas = mascotaDAO.obtenerPorUsuario(usuario.getId());

            request.setAttribute("vacunas", vacunas);
            request.setAttribute("mascotas", mascotas);

            request.getRequestDispatcher("jsp/listaVacunas.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Error al cargar las vacunas: " + e.getMessage());
            request.getRequestDispatcher("jsp/listaVacunas.jsp").forward(request, response);
        }
    }

    private void eliminarVacuna(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        try {
            Long id = Long.parseLong(request.getParameter("id"));
            vacunaDAO.eliminar(id);
            response.sendRedirect(request.getContextPath() + "/vacuna?success=eliminado");
        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/vacuna?error=id_invalido");
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/vacuna?error=error_eliminando");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Verificar sesión
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("usuario") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        String action = request.getParameter("action");
        if ("registrar".equals(action)) {
            registrarVacuna(request, response);
        }
    }

    private void registrarVacuna(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        try {
            // Obtener parámetros del formulario
            String nombre = request.getParameter("nombre");
            String fechaStr = request.getParameter("fecha");
            String mascotaIdStr = request.getParameter("mascotaId");

            // Validar que todos los campos estén presentes
            if (nombre == null || nombre.trim().isEmpty() ||
                    fechaStr == null || fechaStr.trim().isEmpty() ||
                    mascotaIdStr == null || mascotaIdStr.trim().isEmpty()) {
                response.sendRedirect(request.getContextPath() + "/vacuna?error=campos_vacios");
                return;
            }

            // Parsear fecha y mascota ID
            LocalDate fecha = LocalDate.parse(fechaStr);
            Long mascotaId = Long.parseLong(mascotaIdStr);

            // Validar que la fecha no sea futura
            if (fecha.isAfter(LocalDate.now())) {
                response.sendRedirect(request.getContextPath() + "/vacuna?error=fecha_invalida");
                return;
            }

            // Obtener la mascota
            Mascota mascota = mascotaDAO.obtenerPorId(mascotaId);
            if (mascota == null) {
                response.sendRedirect(request.getContextPath() + "/vacuna?error=mascota_no_encontrada");
                return;
            }

            // Crear y guardar la vacuna
            Vacuna vacuna = new Vacuna();
            vacuna.setNombre(nombre.trim());
            vacuna.setFecha(fecha);
            vacuna.setMascota(mascota);

            vacunaDAO.guardar(vacuna);

            // Redirigir con mensaje de éxito
            response.sendRedirect(request.getContextPath() + "/vacuna?success=registrado");

        } catch (NumberFormatException e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/vacuna?error=formato_invalido");
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/vacuna?error=error_registro");
        }
    }
}