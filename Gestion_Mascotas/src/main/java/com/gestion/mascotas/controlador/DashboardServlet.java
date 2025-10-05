package com.gestion.mascotas.controlador;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet({"/dashboard", "/home", ""})
public class DashboardServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Verificar que el usuario esté logueado
        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("usuario") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        // TODO: Obtener usuario y cargar estadísticas reales usando DAOs
        // Usuario usuario = (Usuario) session.getAttribute("usuario");
        // Long usuarioId = usuario.getId();
        
        // Por ahora dejamos valores de ejemplo
        request.setAttribute("totalMascotas", 0);
        request.setAttribute("totalVisitas", 0);
        request.setAttribute("totalVacunas", 0);
        request.setAttribute("proximasVacunas", 0);

        // Mostrar el dashboard
        request.getRequestDispatcher("/dashboard.jsp").forward(request, response);
    }
}