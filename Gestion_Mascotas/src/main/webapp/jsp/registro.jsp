<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<html>
<head><title>Registro</title></head>
<body>
<h2>Crear cuenta</h2>
<form action="usuario" method="post">
    <input type="hidden" name="action" value="registrar"/>
    Usuario: <input type="text" name="nombreUsuario" required/><br/>
    Email: <input type="email" name="email" required/><br/>
    Contraseña: <input type="password" name="contrasena" required/><br/>
    <button type="submit">Registrarse</button>
</form>
<c:if test="${not empty error}">
    <p style="color:red">${error}</p>
</c:if>
</body>
</html>
