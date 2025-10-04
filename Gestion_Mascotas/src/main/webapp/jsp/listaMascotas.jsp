<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="com.gestionmascotas.app.model.Mascota" %>
<html>
<head><title>Lista de Mascotas</title></head>
<body>
<h2>Lista de Mascotas</h2>
<a href="mascota?action=registrar">Registrar Nueva Mascota</a><br/><br/>

<table border="1" cellpadding="5">
    <tr>
        <th>ID</th>
        <th>Nombre</th>
        <th>Tipo</th>
        <th>Usuario</th>
        <th>Acciones</th>
    </tr>
    <%
        List<Mascota> mascotas = (List<Mascota>) request.getAttribute("mascotas");
        if (mascotas != null) {
            for (Mascota m : mascotas) {
    %>
    <tr>
        <td><%= m.getId() %></td>
        <td><%= m.getNombre() %></td>
        <td><%= m.getTipo() %></td>
        <td><%= m.getUsuario().getNombreUsuario() %></td>
        <td>
            <a href="mascota?action=eliminar&id=<%= m.getId() %>">Eliminar</a>
        </td>
    </tr>
    <%
            }
        }
    %>
</table>
</body>
</html>
