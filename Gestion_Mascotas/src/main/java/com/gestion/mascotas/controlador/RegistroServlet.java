package com.gestion.mascotas.controlador;

import com.gestion.mascotas.dao.UsuarioDAO;
import com.gestion.mascotas.modelo.entidades.Usuario;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.regex.Pattern;

/**
 * Servlet para manejar el registro de nuevos usuarios
 */
@WebServlet("/registro")
public class RegistroServlet extends HttpServlet {

    private UsuarioDAO usuarioDAO;
    
    // Patrones de validación
    private static final Pattern EMAIL_PATTERN = 
        Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
    private static final Pattern USERNAME_PATTERN = 
        Pattern.compile("^[a-zA-Z0-9_-]{3,50}$");
    private static final Pattern PHONE_PATTERN = 
        Pattern.compile("^[0-9]{10}$");

    @Override
    public void init() {
        usuarioDAO = new UsuarioDAO();
    }

    /**
     * Mostrar formulario de registro
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Si ya está logueado, redirigir al dashboard
        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("usuario") != null) {
            response.sendRedirect(request.getContextPath() + "/dashboard");
            return;
        }

        request.getRequestDispatcher("/jsp/registro.jsp").forward(request, response);
    }

    /**
     * Procesar el registro de usuario
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Obtener parámetros del formulario
        String nombreUsuario = request.getParameter("nombreUsuario");
        String nombre = request.getParameter("nombre");
        String email = request.getParameter("email");
        String telefono = request.getParameter("telefono");
        String contrasena = request.getParameter("contrasena");
        String confirmarContrasena = request.getParameter("confirmarContrasena");

        // Validar datos
        String errorValidacion = validarDatos(nombreUsuario, nombre, email, telefono, 
                                               contrasena, confirmarContrasena);
        
        if (errorValidacion != null) {
            request.setAttribute("error", errorValidacion);
            request.getRequestDispatcher("/jsp/registro.jsp").forward(request, response);
            return;
        }

        // Normalizar datos
        nombreUsuario = nombreUsuario.trim().toLowerCase();
        nombre = nombre.trim();
        email = email.trim().toLowerCase();
        telefono = (telefono != null && !telefono.trim().isEmpty()) ? telefono.trim() : null;

        // Verificar si el usuario ya existe
        try {
            if (usuarioDAO.buscarPorNombreUsuario(nombreUsuario) != null) {
                request.setAttribute("error", 
                    "El nombre de usuario '" + nombreUsuario + "' ya está en uso. Por favor elige otro.");
                request.getRequestDispatcher("/jsp/registro.jsp").forward(request, response);
                return;
            }

            if (usuarioDAO.buscarPorEmail(email) != null) {
                request.setAttribute("error", 
                    "El correo electrónico '" + email + "' ya está registrado. ¿Deseas iniciar sesión?");
                request.getRequestDispatcher("/jsp/registro.jsp").forward(request, response);
                return;
            }

            // Crear nuevo usuario
            Usuario nuevoUsuario = new Usuario(nombreUsuario, nombre, email, telefono, contrasena);

            if (usuarioDAO.crearUsuario(nuevoUsuario)) {
                // Registro exitoso - redirigir al login con mensaje de éxito
                HttpSession session = request.getSession();
                session.setAttribute("registroExitoso", true);
                session.setAttribute("mensajeRegistro", 
                    "¡Cuenta creada exitosamente! Ahora puedes iniciar sesión con tus credenciales.");
                
                response.sendRedirect(request.getContextPath() + "/login");
            } else {
                request.setAttribute("error", 
                    "Ocurrió un error al crear tu cuenta. Por favor intenta nuevamente.");
                request.getRequestDispatcher("/jsp/registro.jsp").forward(request, response);
            }

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", 
                "Error en el servidor. Por favor intenta más tarde. Detalle: " + e.getMessage());
            request.getRequestDispatcher("/jsp/registro.jsp").forward(request, response);
        }
    }

    /**
     * Validar todos los datos del formulario
     */
    private String validarDatos(String nombreUsuario, String nombre, String email, 
                                 String telefono, String contrasena, String confirmarContrasena) {
        
        // Validar campos obligatorios
        if (nombreUsuario == null || nombreUsuario.trim().isEmpty()) {
            return "El nombre de usuario es obligatorio.";
        }
        
        if (nombre == null || nombre.trim().isEmpty()) {
            return "El nombre completo es obligatorio.";
        }
        
        if (email == null || email.trim().isEmpty()) {
            return "El correo electrónico es obligatorio.";
        }
        
        if (contrasena == null || contrasena.isEmpty()) {
            return "La contraseña es obligatoria.";
        }
        
        if (confirmarContrasena == null || confirmarContrasena.isEmpty()) {
            return "Debes confirmar tu contraseña.";
        }

        // Validar formato de nombre de usuario
        if (!USERNAME_PATTERN.matcher(nombreUsuario.trim()).matches()) {
            return "El nombre de usuario debe tener entre 3 y 50 caracteres y solo puede contener letras, números, guiones y guiones bajos.";
        }

        // Validar longitud del nombre
        if (nombre.trim().length() < 2 || nombre.trim().length() > 100) {
            return "El nombre debe tener entre 2 y 100 caracteres.";
        }

        // Validar formato de email
        if (!EMAIL_PATTERN.matcher(email.trim()).matches()) {
            return "El formato del correo electrónico no es válido.";
        }

        // Validar teléfono (si se proporciona)
        if (telefono != null && !telefono.trim().isEmpty()) {
            if (!PHONE_PATTERN.matcher(telefono.trim()).matches()) {
                return "El teléfono debe contener exactamente 10 dígitos numéricos.";
            }
        }

        // Validar longitud de contraseña
        if (contrasena.length() < 6) {
            return "La contraseña debe tener al menos 6 caracteres.";
        }
        
        if (contrasena.length() > 100) {
            return "La contraseña no puede tener más de 100 caracteres.";
        }

        // Validar coincidencia de contraseñas
        if (!contrasena.equals(confirmarContrasena)) {
            return "Las contraseñas no coinciden. Por favor verifica.";
        }

        // Validar fortaleza de contraseña (recomendaciones)
        if (!validarFortalezaContrasena(contrasena)) {
            return "Tu contraseña es muy débil. Intenta incluir mayúsculas, minúsculas, números o caracteres especiales.";
        }

        return null; // Sin errores
    }

    /**
     * Validar fortaleza básica de la contraseña
     */
    private boolean validarFortalezaContrasena(String contrasena) {
        // Debe tener al menos una letra
        boolean tieneLetra = contrasena.matches(".*[a-zA-Z].*");
        
        // Debe tener al menos un número O un caracter especial
        boolean tieneNumeroOEspecial = contrasena.matches(".*[0-9].*") || 
                                        contrasena.matches(".*[!@#$%^&*(),.?\":{}|<>].*");
        
        return tieneLetra && tieneNumeroOEspecial;
    }
}
