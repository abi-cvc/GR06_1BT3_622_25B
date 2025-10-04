<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="com.gestionmascotas.app.model.Usuario" %>
<%
    Usuario usuario = (Usuario) session.getAttribute("usuario");
    if (usuario == null) {
        response.sendRedirect("login.jsp");
        return;
    }
%>
<html>
<head><title>Perfil</title></head>
<body>
<h2>Bienvenido, <%= usuario.getNombreUsuario() %></h2>
<p>Email: <%= usuario.getEmail() %></p>

<h3>Editar perfil</h3>
<form action="usuario" method="post">
    <input type="hidden" name="action" value="editar"/>
    Nuevo Email: <input type="email" name="email"/><br/>
    Nueva Contraseña: <input type="password" name="contrasena"/><br/>
    <button type="submit">Actualizar</button>
</form>

<h3>Eliminar cuenta</h3>
<form action="usuario" method="post">
    <input type="hidden" name="action" value="eliminar"/>
    <button type="submit" style="color:red">Eliminar mi cuenta</button>
</form>

<c:if test="${not empty mensaje}">
    <p style="color:green">${mensaje}</p>
</c:if>
<c:if test="${not empty error}">
    <p style="color:red">${error}</p>
</c:if>
</body>
</html>
