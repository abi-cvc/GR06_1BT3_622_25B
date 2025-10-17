package com.gestion.mascotas.controlador;

import com.gestion.mascotas.dao.MascotaDAO;
import com.gestion.mascotas.dao.RecordatorioAlimentacionDAO;
import com.gestion.mascotas.dao.RecordatorioPaseoDAO;
import com.gestion.mascotas.dao.UsuarioDAO;
import com.gestion.mascotas.modelo.entidades.Mascota;
import com.gestion.mascotas.modelo.entidades.RecordatorioAlimentacion;
import com.gestion.mascotas.modelo.entidades.RecordatorioPaseo;
import com.gestion.mascotas.modelo.enums.TipoMascota;
import com.gestion.mascotas.modelo.entidades.Usuario;

import com.gestion.mascotas.servicio.MascotaService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.List;

@WebServlet({"/mascota", "/mascotas"})
public class MascotaServlet extends HttpServlet {

    private MascotaService mascotaService;
    private MascotaDAO mascotaDAO = new MascotaDAO();
    private UsuarioDAO usuarioDAO = new UsuarioDAO();
    private RecordatorioAlimentacionDAO recordatorioAlimentacionDAO = new RecordatorioAlimentacionDAO();
    private RecordatorioPaseoDAO recordatorioPaseoDAO = new RecordatorioPaseoDAO();

    public MascotaServlet() {
        this.mascotaService = new MascotaService();
    }

    public void init (){
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

        String action = request.getParameter("action");
        if (action == null) action = "listar";
        Usuario usuario = (Usuario) session.getAttribute("usuario");

        try {
            switch (action) {
                case "detalles":
                    mostrarDetalles(request, response);
                    break;
                case "eliminar":
                    eliminarMascota(request, response, usuario);
                    break;
                case "listar":
                default:
                    listarMascotas(request, response, usuario);
                    break;
            }
        } catch (Exception e) {
            request.getSession().setAttribute("error", "Error inesperado: " + e.getMessage());
            response.sendRedirect(request.getContextPath() + "/dashboard");
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
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        if ("registrar".equals(action)) {
            registrarMascota(request, response);
        } else if ("actualizar".equals(action)) {
            actualizarMascota(request, response, usuario);
        } else {
            response.sendRedirect(request.getContextPath() + "/mascota?action=listar&error=accion_desconocida");
        }
    }

//    private void mostrarFormularioRegistro(HttpServletRequest request, HttpServletResponse response)
//            throws ServletException, IOException {
//        HttpSession session = request.getSession(false);
//        if (session != null && session.getAttribute("usuarioId") != null) {
//            request.setAttribute("usuarioId", session.getAttribute("usuarioId"));
//        }
//        request.setAttribute("tiposMascota", TipoMascota.values());
//        request.getRequestDispatcher("/jsp/registrarMascota.jsp").forward(request, response);
//    }

//    private void mostrarFormularioEdicion(HttpServletRequest request, HttpServletResponse response)
//            throws ServletException, IOException {
//        Long id = Long.parseLong(request.getParameter("id"));
//        Mascota mascota = mascotaDAO.obtenerPorId(id);
//
//        if (mascota == null) {
//            request.setAttribute("error", "Mascota no encontrada para edición.");
//            listarMascotas(request, response);
//            return;
//        }
//
//        request.setAttribute("mascota", mascota);
//        request.setAttribute("tiposMascota", TipoMascota.values());
//        request.getRequestDispatcher("/jsp/mascota/editarMascota.jsp").forward(request, response);
//    }

    private void listarMascotas(HttpServletRequest request, HttpServletResponse response, Usuario usuario) throws ServletException, IOException {
        request.setAttribute("mascotas", mascotaService.listarMascotasPorUsuario(usuario.getId()));
        request.setAttribute("tiposMascota", TipoMascota.values());
        request.getRequestDispatcher("/jsp/listaMascotas.jsp").forward(request, response);
    }

    private void mostrarDetalles(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Long mascotaId = Long.parseLong(request.getParameter("id"));
        request.setAttribute("mascota", mascotaService.consultarDatos(mascotaId));
        // Lógica para cargar recordatorios iría aquí, llamando a sus respectivos servicios
        request.getRequestDispatcher("/jsp/detallesMascota.jsp").forward(request, response);
    }

    private void registrarMascota(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        try {
            String nombre = request.getParameter("nombre");
            TipoMascota tipo = TipoMascota.valueOf(request.getParameter("tipo"));
            String raza = request.getParameter("raza");
            Integer edad = Integer.parseInt(request.getParameter("edad"));
            Double peso = Double.parseDouble(request.getParameter("peso"));
            String color = request.getParameter("color");
            Long usuarioId = Long.parseLong(request.getParameter("usuarioId"));

            mascotaService.registrarMascota(nombre, tipo, raza, edad, peso, color, usuarioId);
            response.sendRedirect(request.getContextPath() + "/mascota?action=listar&success=registrado");
        } catch (IllegalArgumentException e) {
            request.setAttribute("error", e.getMessage());
            request.setAttribute("tiposMascota", TipoMascota.values());
            request.getRequestDispatcher("/jsp/registrarMascota.jsp").forward(request, response);
        }
    }

    private void actualizarMascota(HttpServletRequest request, HttpServletResponse response, Usuario usuario) throws IOException, ServletException {
        try {
            Long mascotaId = Long.parseLong(request.getParameter("id"));
            String nombre = request.getParameter("nombre");
            TipoMascota tipo = TipoMascota.valueOf(request.getParameter("tipo"));
            String raza = request.getParameter("raza");
            Integer edad = Integer.parseInt(request.getParameter("edad"));
            Double peso = Double.parseDouble(request.getParameter("peso"));
            String color = request.getParameter("color");

            mascotaService.actualizarDatos(mascotaId, nombre, tipo, raza, edad, peso, color, usuario);
            response.sendRedirect(request.getContextPath() + "/mascota?action=listar&success=actualizado");
        } catch (IllegalArgumentException | SecurityException e) {
            request.getSession().setAttribute("error", e.getMessage());
            response.sendRedirect(request.getContextPath() + "/mascota?action=listar");
        }
    }

    private void eliminarMascota(HttpServletRequest request, HttpServletResponse response, Usuario usuario) throws IOException {
        try {
            Long mascotaId = Long.parseLong(request.getParameter("id"));
            mascotaService.eliminarMascota(mascotaId, usuario);
            response.sendRedirect(request.getContextPath() + "/dashboard?success=mascota_eliminada");
        } catch (IllegalArgumentException | SecurityException e) {
            request.getSession().setAttribute("error", e.getMessage());
            response.sendRedirect(request.getContextPath() + "/dashboard");
        }
    }

    /**
     * Obtiene todas las mascotas de un usuario específico
     * @param usuarioId ID del usuario
     * @return Lista de mascotas del usuario
     */
    public List<Mascota> obtenerPorUsuario(Long usuarioId) {
        return mascotaDAO.obtenerMascotasPorUsuario(usuarioId);
    }
}