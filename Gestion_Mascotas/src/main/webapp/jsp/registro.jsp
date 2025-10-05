<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Registro - Sistema de Gestión de Mascotas</title>
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

        <div class="auth-card register-card">
            <!-- Logo y Título -->
            <div class="auth-header">
                <div class="logo">
                    <i class="fas fa-paw"></i>
                </div>
                <h1>Crear una cuenta</h1>
                <p class="subtitle">Únete para gestionar el cuidado de tus mascotas</p>
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

            <!-- Formulario de Registro -->
            <form action="${pageContext.request.contextPath}/registro" 
                  method="post" 
                  class="auth-form"
                  id="registroForm">
                <input type="hidden" name="action" value="register">

                <div class="form-row">
                    <div class="form-group">
                        <label for="nombreUsuario">
                            <i class="fas fa-user"></i>
                            Usuario <span class="required">*</span>
                        </label>
                        <input type="text" 
                               id="nombreUsuario" 
                               name="nombreUsuario"
                               placeholder="Elige un nombre de usuario" 
                               required 
                               autofocus
                               minlength="3"
                               maxlength="50"
                               value="${param.nombreUsuario}">
                        <small class="help-text">Mínimo 3 caracteres, sin espacios</small>
                    </div>

                    <div class="form-group">
                        <label for="nombre">
                            <i class="fas fa-id-card"></i>
                            Nombre completo <span class="required">*</span>
                        </label>
                        <input type="text" 
                               id="nombre" 
                               name="nombre"
                               placeholder="Tu nombre completo" 
                               required
                               maxlength="100"
                               value="${param.nombre}">
                    </div>
                </div>

                <div class="form-row">
                    <div class="form-group">
                        <label for="email">
                            <i class="fas fa-envelope"></i>
                            Correo electrónico <span class="required">*</span>
                        </label>
                        <input type="email" 
                               id="email" 
                               name="email"
                               placeholder="tu@email.com" 
                               required
                               maxlength="100"
                               value="${param.email}">
                    </div>

                    <div class="form-group">
                        <label for="telefono">
                            <i class="fas fa-phone"></i>
                            Teléfono
                        </label>
                        <input type="tel" 
                               id="telefono" 
                               name="telefono"
                               placeholder="0999999999"
                               pattern="[0-9]{10}"
                               maxlength="10"
                               value="${param.telefono}">
                        <small class="help-text">10 dígitos (opcional)</small>
                    </div>
                </div>

                <div class="form-row">
                    <div class="form-group">
                        <label for="contrasena">
                            <i class="fas fa-lock"></i>
                            Contraseña <span class="required">*</span>
                        </label>
                        <div class="password-input">
                            <input type="password" 
                                   id="contrasena" 
                                   name="contrasena"
                                   placeholder="Mínimo 6 caracteres" 
                                   required
                                   minlength="6"
                                   maxlength="100">
                            <button type="button" class="toggle-password" onclick="togglePassword('contrasena')">
                                <i class="fas fa-eye" id="toggle-icon-contrasena"></i>
                            </button>
                        </div>
                        <div class="password-strength" id="password-strength">
                            <div class="strength-bar"></div>
                        </div>
                        <small class="help-text" id="password-help">Mínimo 6 caracteres</small>
                    </div>

                    <div class="form-group">
                        <label for="confirmarContrasena">
                            <i class="fas fa-lock"></i>
                            Confirmar contraseña <span class="required">*</span>
                        </label>
                        <div class="password-input">
                            <input type="password" 
                                   id="confirmarContrasena" 
                                   name="confirmarContrasena"
                                   placeholder="Repite tu contraseña" 
                                   required
                                   minlength="6"
                                   maxlength="100">
                            <button type="button" class="toggle-password" onclick="togglePassword('confirmarContrasena')">
                                <i class="fas fa-eye" id="toggle-icon-confirmarContrasena"></i>
                            </button>
                        </div>
                        <small class="help-text" id="confirm-help"></small>
                    </div>
                </div>

                <div class="form-group">
                    <label class="checkbox-label">
                        <input type="checkbox" name="aceptarTerminos" required>
                        <span>Acepto los <a href="#" class="link-secondary">términos y condiciones</a> 
                              y la <a href="#" class="link-secondary">política de privacidad</a>
                        </span>
                    </label>
                </div>

                <button type="submit" class="btn btn-primary">
                    <i class="fas fa-user-plus"></i>
                    Crear Cuenta
                </button>
            </form>

            <!-- Link a Login -->
            <div class="auth-footer">
                <p>¿Ya tienes una cuenta? 
                    <a href="${pageContext.request.contextPath}/login" class="link-primary">
                        Inicia sesión aquí
                    </a>
                </p>
            </div>
        </div>

        <!-- Información adicional -->
        <div class="info-section">
            <div class="info-card">
                <i class="fas fa-gift"></i>
                <h3>100% Gratis</h3>
                <p>Sin costo alguno</p>
            </div>
            <div class="info-card">
                <i class="fas fa-cloud"></i>
                <h3>En la nube</h3>
                <p>Accede desde cualquier lugar</p>
            </div>
            <div class="info-card">
                <i class="fas fa-lock"></i>
                <h3>Privado</h3>
                <p>Solo tú ves tu información</p>
            </div>
        </div>
    </div>

    <script src="${pageContext.request.contextPath}/js/registro.js"></script>
</body>
</html>
