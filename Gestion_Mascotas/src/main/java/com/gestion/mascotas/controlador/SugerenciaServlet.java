package com.gestion.mascotas.controlador;

import com.gestion.mascotas.dao.MascotaDAO;
import com.gestion.mascotas.modelo.Mascota;
import com.gestion.mascotas.modelo.SugerenciaAlimentacion;
import com.gestion.mascotas.modelo.SugerenciaEjercicio;
import com.gestion.mascotas.modelo.Usuario;
import com.gestion.mascotas.modelo.AnalizadorDatos;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@WebServlet("/sugerencia")
public class SugerenciaServlet extends HttpServlet {

    private MascotaDAO mascotaDAO = new MascotaDAO();
    private AnalizadorDatos analizadorDatos = new AnalizadorDatos();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("usuario") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        String action = request.getParameter("action");
        if (action == null) action = "generar";

        if ("generar".equals(action)) {
            generarRecomendaciones(request, response);
        } else {
            response.sendRedirect(request.getContextPath() + "/dashboard");
        }
    }

    private void generarRecomendaciones(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String mascotaIdParam = request.getParameter("mascotaId");
        if (mascotaIdParam == null || mascotaIdParam.trim().isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/mascota?action=listar&error=mascota_no_especificada");
            return;
        }

        Long mascotaId = Long.parseLong(mascotaIdParam);
        Mascota mascota = mascotaDAO.obtenerPorId(mascotaId);

        if (mascota == null) {
            response.sendRedirect(request.getContextPath() + "/mascota?action=listar&error=mascota_no_encontrada");
            return;
        }

        // Verificar que la mascota pertenece al usuario logueado
        HttpSession session = request.getSession(false);
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        if (!mascota.getUsuario().getId().equals(usuario.getId())) {
            response.sendRedirect(request.getContextPath() + "/mascota?action=listar&error=acceso_denegado");
            return;
        }

        // Usar el AnalizadorDatos para obtener las sugerencias
        Map<String, List<?>> resultados = analizadorDatos.analizarMascota(mascota);

        @SuppressWarnings("unchecked")
        List<SugerenciaAlimentacion> sugerenciasAlimentacion =
                (List<SugerenciaAlimentacion>) resultados.get("alimentacion");

        @SuppressWarnings("unchecked")
        List<SugerenciaEjercicio> sugerenciasEjercicio =
                (List<SugerenciaEjercicio>) resultados.get("ejercicio");

        // Información adicional del análisis
        String nivelActividadRecomendado = analizadorDatos.calcularNivelActividadRecomendado(mascota);
        Integer caloriasRecomendadas = analizadorDatos.calcularCaloriasRecomendadas(mascota);
        boolean necesitaAtencionEspecial = analizadorDatos.necesitaAtencionEspecial(mascota);
        String reporteAnalisis = analizadorDatos.generarReporteAnalisis(mascota);

        // Imprimir reporte en consola
        System.out.println(reporteAnalisis);

        // Pasar datos a la vista
        request.setAttribute("mascota", mascota);
        request.setAttribute("sugerenciasAlimentacion", sugerenciasAlimentacion);
        request.setAttribute("sugerenciasEjercicio", sugerenciasEjercicio);
        request.setAttribute("nivelActividadRecomendado", nivelActividadRecomendado);
        request.setAttribute("caloriasRecomendadas", caloriasRecomendadas);
        request.setAttribute("necesitaAtencionEspecial", necesitaAtencionEspecial);

        request.getRequestDispatcher("/jsp/recomendaciones.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}