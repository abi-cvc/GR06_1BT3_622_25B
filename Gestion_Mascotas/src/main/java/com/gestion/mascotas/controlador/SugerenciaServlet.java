// FileName: MultipleFiles/SugerenciaServlet.java
package com.gestion.mascotas.controlador;

import com.gestion.mascotas.dao.MascotaDAO;
import com.gestion.mascotas.dao.SugerenciaAlimentacionDAO;
import com.gestion.mascotas.dao.SugerenciaDAO;
import com.gestion.mascotas.dao.SugerenciaEjercicioDAO;
import com.gestion.mascotas.modelo.AnalizadorDatos;
import com.gestion.mascotas.modelo.Mascota;
import com.gestion.mascotas.modelo.Sugerencia;
import com.gestion.mascotas.modelo.SugerenciaAlimentacion;
import com.gestion.mascotas.modelo.SugerenciaEjercicio;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;

@WebServlet("/sugerencia")
public class SugerenciaServlet extends HttpServlet {

    private SugerenciaDAO sugerenciaDAO;
    private SugerenciaAlimentacionDAO sugerenciaAlimentacionDAO;
    private SugerenciaEjercicioDAO sugerenciaEjercicioDAO;
    private MascotaDAO mascotaDAO;
    private AnalizadorDatos analizadorDatos;

    @Override
    public void init() throws ServletException {
        sugerenciaDAO = new SugerenciaDAO();
        sugerenciaAlimentacionDAO = new SugerenciaAlimentacionDAO();
        sugerenciaEjercicioDAO = new SugerenciaEjercicioDAO();
        mascotaDAO = new MascotaDAO();
        analizadorDatos = new AnalizadorDatos();
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
            case "generar":
                mostrarFormularioGeneracion(request, response);
                break;
            case "eliminar":
                eliminarSugerencia(request, response);
                break;
            case "listarPorMascota":
                listarSugerenciasPorMascota(request, response);
                break;
            default:
                listarTodasSugerencias(request, response);
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

        if ("generar".equals(action)) {
            generarSugerencia(request, response);
        } else {
            response.sendRedirect(request.getContextPath() + "/sugerencia?error=accion_desconocida");
        }
    }

    private void mostrarFormularioGeneracion(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<Mascota> mascotas = mascotaDAO.obtenerTodas();
        request.setAttribute("mascotas", mascotas);
        request.getRequestDispatcher("/jsp/sugerencia/generarSugerencia.jsp").forward(request, response);
    }

    private void generarSugerencia(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Long mascotaId = Long.parseLong(request.getParameter("mascotaId"));
        String tipoSugerencia = request.getParameter("tipoSugerencia"); // "alimentacion" o "ejercicio"

        Mascota mascota = mascotaDAO.obtenerPorId(mascotaId);
        if (mascota == null) {
            request.setAttribute("error", "Mascota no encontrada.");
            mostrarFormularioGeneracion(request, response);
            return;
        }

        try {
            Sugerencia sugerenciaGenerada = analizadorDatos.analizarPerfilMascota(mascota, tipoSugerencia);

            if (sugerenciaGenerada != null) {
                if (sugerenciaGenerada instanceof SugerenciaAlimentacion) {
                    sugerenciaAlimentacionDAO.guardar((SugerenciaAlimentacion) sugerenciaGenerada);
                } else if (sugerenciaGenerada instanceof SugerenciaEjercicio) {
                    sugerenciaEjercicioDAO.guardar((SugerenciaEjercicio) sugerenciaGenerada);
                } else {
                    sugerenciaDAO.guardar(sugerenciaGenerada); // Fallback for generic if applicable
                }
                response.sendRedirect(request.getContextPath() + "/sugerencia?success=true");
            } else {
                request.setAttribute("error", "No se pudo generar la sugerencia para el tipo: " + tipoSugerencia);
                mostrarFormularioGeneracion(request, response);
            }
        } catch (Exception e) {
            request.setAttribute("error", "Error al generar la sugerencia: " + e.getMessage());
            mostrarFormularioGeneracion(request, response);
        }
    }

    private void eliminarSugerencia(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        Long id = Long.parseLong(request.getParameter("id"));
        try {
            sugerenciaDAO.eliminar(id); // This will handle specific types via polymorphism if needed
            response.sendRedirect(request.getContextPath() + "/sugerencia?success=eliminado");
        } catch (Exception e) {
            request.setAttribute("error", "Error al eliminar la sugerencia: " + e.getMessage());
            listarTodasSugerencias(request, response);
        }
    }

    private void listarTodasSugerencias(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<Sugerencia> sugerencias = sugerenciaDAO.obtenerTodos();
        request.setAttribute("sugerencias", sugerencias);
        request.getRequestDispatcher("/jsp/sugerencia/listaSugerencias.jsp").forward(request, response);
    }

    private void listarSugerenciasPorMascota(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Long mascotaId = Long.parseLong(request.getParameter("mascotaId"));
        List<Sugerencia> sugerencias = sugerenciaDAO.obtenerSugerenciasPorMascota(mascotaId);
        request.setAttribute("sugerencias", sugerencias);
        request.getRequestDispatcher("/jsp/sugerencia/listaSugerencias.jsp").forward(request, response);
    }
}