package com.gestion.mascotas.controlador;

import com.gestion.mascotas.dao.MascotaDAO;
import com.gestion.mascotas.dao.VacunaDAO;
import com.gestion.mascotas.dao.VisitaVeterinariaDAO;
import com.gestion.mascotas.modelo.Mascota;
import com.gestion.mascotas.modelo.Usuario;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.gestion.mascotas.modelo.TipoMascota;

@WebServlet({"/dashboard", "/home", ""})
public class DashboardServlet extends HttpServlet {

    private MascotaDAO mascotaDAO = new MascotaDAO();
    private VacunaDAO vacunaDAO = new VacunaDAO();
    private VisitaVeterinariaDAO visitaDAO = new VisitaVeterinariaDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Verificar que el usuario esté logueado
        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("usuario") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        // Obtener usuario y cargar estadísticas reales
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        Long usuarioId = usuario.getId();
        
        // Obtener todas las mascotas del usuario
        List<Mascota> mascotas = mascotaDAO.obtenerMascotasPorUsuario(usuarioId);
        
        // Crear mapas para almacenar las estadísticas por mascota
        Map<Long, Long> visitasPorMascota = new HashMap<>();
        Map<Long, Long> vacunasPorMascota = new HashMap<>();
        Map<Long, Long> vacunasProximasPorMascota = new HashMap<>();
        
        // Calcular estadísticas para cada mascota
        for (Mascota mascota : mascotas) {
            Long mascotaId = mascota.getId();
            visitasPorMascota.put(mascotaId, visitaDAO.contarVisitasPorMascota(mascotaId));
            vacunasPorMascota.put(mascotaId, vacunaDAO.contarVacunasPorMascota(mascotaId));
            vacunasProximasPorMascota.put(mascotaId, vacunaDAO.contarVacunasProximasPorMascota(mascotaId));
        }
        
        // Enviar datos a la vista
        request.setAttribute("mascotas", mascotas);
        request.setAttribute("visitasPorMascota", visitasPorMascota);
        request.setAttribute("vacunasPorMascota", vacunasPorMascota);
        request.setAttribute("vacunasProximasPorMascota", vacunasProximasPorMascota);
        request.setAttribute("tiposMascota", TipoMascota.values());

        // Mostrar el dashboard
        request.getRequestDispatcher("/jsp/dashboard.jsp").forward(request, response);
    }
}