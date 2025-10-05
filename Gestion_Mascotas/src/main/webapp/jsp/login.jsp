<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Iniciar Sesión - Sistema de Gestión de Mascotas</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/auth.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
</head>
<body>
    <div class="auth-container">
        <div class="auth-background">
            <div class="shape shape-1"></div>
            <div class="shape shape-2"></div>
            <div class="shape shape-3"></div>
        </div>

        <div class="auth-card">
            <!-- Logo y Título -->
            <div class="auth-header">
                <div class="logo">
                    <i class="fas fa-paw"></i>
                </div>
                <h1>Bienvenido de nuevo</h1>
                <p class="subtitle">Ingresa a tu cuenta para continuar</p>
            </div>

            <!-- Mensajes de error y éxito -->
            <c:if test="${not empty error}">
                <div class="alert alert-error">
                    <i class="fas fa-exclamation-circle"></i>
                    <span>${error}</span>
                </div>
            </c:if>

            <c:if test="${not empty success}">
                <div class="alert alert-success">
                    <i class="fas fa-check-circle"></i>
                    <span>${success}</span>
                </div>
            </c:if>

            <!-- Formulario de Login -->
            <form action="${pageContext.request.contextPath}/login" method="post" class="auth-form">
                <input type="hidden" name="action" value="login">

                <div class="form-group">
                    <label for="nombreUsuario">
                        <i class="fas fa-user"></i>
                        Usuario
                    </label>
                    <input type="text" 
                           id="nombreUsuario" 
                           name="nombreUsuario"
                           placeholder="Ingresa tu nombre de usuario" 
                           required 
                           autofocus
                           value="${param.nombreUsuario}">
                </div>

                <div class="form-group">
                    <label for="contrasena">
                        <i class="fas fa-lock"></i>
                        Contraseña
                    </label>
                    <div class="password-input">
                        <input type="password" 
                               id="contrasena" 
                               name="contrasena"
                               placeholder="Ingresa tu contraseña" 
                               required>
                        <button type="button" class="toggle-password" onclick="togglePassword('contrasena')">
                            <i class="fas fa-eye" id="toggle-icon-contrasena"></i>
                        </button>
                    </div>
                </div>

                <div class="form-options">
                    <label class="checkbox-label">
                        <input type="checkbox" name="recordar">
                        <span>Recordarme</span>
                    </label>
                    <a href="#" class="link-secondary">¿Olvidaste tu contraseña?</a>
                </div>

                <button type="submit" class="btn btn-primary">
                    <i class="fas fa-sign-in-alt"></i>
                    Iniciar Sesión
                </button>
            </form>

            <!-- Link a Registro -->
            <div class="auth-footer">
                <p>¿No tienes una cuenta? 
                    <a href="${pageContext.request.contextPath}/registro" class="link-primary">
                        Regístrate aquí
                    </a>
                </p>
            </div>
        </div>

        <!-- Información adicional -->
        <div class="info-section">
            <div class="info-card">
                <i class="fas fa-shield-alt"></i>
                <h3>Seguro</h3>
                <p>Tus datos están protegidos</p>
            </div>
            <div class="info-card">
                <i class="fas fa-heart"></i>
                <h3>Cuidado</h3>
                <p>Gestiona la salud de tus mascotas</p>
            </div>
            <div class="info-card">
                <i class="fas fa-calendar-check"></i>
                <h3>Organizado</h3>
                <p>Control de vacunas y visitas</p>
            </div>
        </div>
    </div>

    <script>
        function togglePassword(inputId) {
            const input = document.getElementById(inputId);
            const icon = document.getElementById('toggle-icon-' + inputId);
            
            if (input.type === 'password') {
                input.type = 'text';
                icon.classList.remove('fa-eye');
                icon.classList.add('fa-eye-slash');
            } else {
                input.type = 'password';
                icon.classList.remove('fa-eye-slash');
                icon.classList.add('fa-eye');
            }
        }

        // Auto-hide alerts after 5 seconds
        document.addEventListener('DOMContentLoaded', function() {
            const alerts = document.querySelectorAll('.alert');
            alerts.forEach(alert => {
                setTimeout(() => {
                    alert.style.animation = 'slideOut 0.3s ease-out';
                    setTimeout(() => alert.remove(), 300);
                }, 5000);
            });
        });
    </script>
</body>
</html>
