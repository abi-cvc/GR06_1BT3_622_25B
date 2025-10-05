<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="es">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Login - Sistema de Gestión de Mascotas</title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
<div class="login-container">
  <div class="login-box">
    <h1>🐾 Gestión de Mascotas</h1>

    <!-- Mensajes de error y éxito -->
    <c:if test="${not empty error}">
      <div class="alert alert-error">
          ${error}
      </div>
    </c:if>

    <c:if test="${not empty success}">
      <div class="alert alert-success">
          ${success}
      </div>
    </c:if>

    <!-- Tabs para Login y Registro -->
    <div class="tabs">
      <button class="tab-btn active" onclick="showTab('login')">Iniciar Sesión</button>
      <button class="tab-btn" onclick="showTab('register')">Registrarse</button>
    </div>

    <!-- Formulario de Login -->
    <div id="login-tab" class="tab-content active">
      <form action="${pageContext.request.contextPath}/login" method="post">
        <input type="hidden" name="action" value="login">

        <div class="form-group">
          <label for="login-usuario">Usuario:</label>
          <input type="text" id="login-usuario" name="nombreUsuario"
                 placeholder="Ingresa tu usuario" required>
        </div>

        <div class="form-group">
          <label for="login-password">Contraseña:</label>
          <input type="password" id="login-password" name="contrasena"
                 placeholder="Ingresa tu contraseña" required>
        </div>

        <button type="submit" class="btn btn-primary">Iniciar Sesión</button>
      </form>
    </div>

    <!-- Formulario de Registro -->
    <div id="register-tab" class="tab-content">
      <form action="${pageContext.request.contextPath}/login" method="post" onsubmit="return validarRegistro()">
        <input type="hidden" name="action" value="register">

        <div class="form-group">
          <label for="reg-usuario">Usuario: *</label>
          <input type="text" id="reg-usuario" name="nombreUsuario"
                 placeholder="Elige un nombre de usuario" required>
        </div>

        <div class="form-group">
          <label for="reg-nombre">Nombre completo: *</label>
          <input type="text" id="reg-nombre" name="nombre"
                 placeholder="Tu nombre completo" required>
        </div>

        <div class="form-group">
          <label for="reg-email">Email: *</label>
          <input type="email" id="reg-email" name="email"
                 placeholder="tu@email.com" required>
        </div>

        <div class="form-group">
          <label for="reg-telefono">Teléfono:</label>
          <input type="tel" id="reg-telefono" name="telefono"
                 placeholder="0999999999">
        </div>

        <div class="form-group">
          <label for="reg-password">Contraseña: *</label>
          <input type="password" id="reg-password" name="contrasena"
                 placeholder="Mínimo 6 caracteres" required>
        </div>

        <div class="form-group">
          <label for="reg-confirm">Confirmar contraseña: *</label>
          <input type="password" id="reg-confirm" name="confirmarContrasena"
                 placeholder="Repite tu contraseña" required>
        </div>

        <button type="submit" class="btn btn-primary">Registrarse</button>
      </form>
    </div>
  </div>
</div>

<script src="${pageContext.request.contextPath}/js/main.js"></script>
</body>
</html>