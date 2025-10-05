package com.gestion.mascotas.controlador;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet("/logout")
public class LogoutServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        procesarLogout(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        procesarLogout(request, response);
    }

    /**
     * Procesa el cierre de sesión
     */
    private void procesarLogout(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        // Obtener la sesión actual
        HttpSession session = request.getSession(false);

        if (session != null) {
            // Obtener el nombre de usuario antes de invalidar
            String nombreUsuario = (String) session.getAttribute("nombreUsuario");

            // Log del logout
            if (nombreUsuario != null) {
                System.out.println("Usuario '" + nombreUsuario + "' ha cerrado sesión");
            }

            // Invalidar la sesión
            session.invalidate();
        }

        // Eliminar cookies de "recordar usuario" si existen
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("recordarUsuario".equals(cookie.getName())) {
                    cookie.setValue("");
                    cookie.setMaxAge(0);
                    cookie.setPath(request.getContextPath());
                    response.addCookie(cookie);
                }
            }
        }

        // Redirigir al login con mensaje
        response.sendRedirect(request.getContextPath() + "/login?logout=true");
    }
}