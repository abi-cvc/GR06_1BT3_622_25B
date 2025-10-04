<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Iniciar Sesión - Gestión de Mascotas</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
<div class="login-container">
    <div class="login-box">
        <h1>🐾 Gestión de Mascotas</h1>
        <p class="subtitle">Inicia sesión para gestionar tus mascotas</p>

        <!-- Mensaje de registro exitoso -->
        <c:if test="${not empty sessionScope.mensajeRegistro}">
            <div class="alert alert-success">
                ✅ ${sessionScope.mensajeRegistro}
            </div>
            <c:remove var="mensajeRegistro" scope="session"/>
        </c:if>

        <!-- Mensajes de error -->
        <c:if test="${not empty error}">
            <div class="alert alert-error">
                ❌ ${error}
            </div>
        </c:if>

        <!-- Mensaje de logout -->
        <c:if test="${not empty param.logout}">
            <div class="alert alert-success">
                ✅ Has cerrado sesión exitosamente.
            </div>
        </c:if>

        <!-- Formulario de Login -->
        <form action="${pageContext.request.contextPath}/login" method="post" onsubmit="return validarLogin()">

            <div class="form-group">
                <label for="nombreUsuario">Usuario:</label>
                <input type="text"
                       id="nombreUsuario"
                       name="nombreUsuario"
                       placeholder="Ingresa tu nombre de usuario"
                       value="${param.nombreUsuario}"
                       required
                       autofocus>
            </div>

            <div class="form-group">
                <label for="contrasena">Contraseña:</label>
                <div class="password-wrapper">
                    <input type="password"
                           id="contrasena"
                           name="contrasena"
                           placeholder="Ingresa tu contraseña"
                           required>
                    <button type="button" class="toggle-password" onclick="togglePassword('contrasena')">
                        👁️
                    </button>
                </div>
            </div>

            <div class="form-options">
                <label class="remember-me">
                    <input type="checkbox" name="recordar" value="true">
                    <span>Recordarme</span>
                </label>
            </div>

            <div class="form-actions">
                <button type="submit" class="btn btn-primary">
                    Iniciar Sesión
                </button>
            </div>
        </form>

        <div class="form-footer">
            <p>¿No tienes una cuenta?
                <a href="${pageContext.request.contextPath}/registro">Créate una aquí</a>
            </p>
        </div>
    </div>
</div>

<script src="${pageContext.request.contextPath}/js/login.js"></script>
</body>
</html>