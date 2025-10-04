<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="com.gestionmascotas.app.model.Vacuna" %>
<html>
<head><title>Lista de Vacunas</title></head>
<body>
<h2>Lista de Vacunas</h2>
<a href="vacuna?action=registrar">Registrar Nueva Vacuna</a><br/><br/>

<table border="1" cellpadding="5">
    <tr>
        <th>ID</th>
        <th>Nombre</th>
        <th>Fecha</th>
        <th>Mascota</th>
        <th>Acciones</th>
    </tr>
    <%
        List<Vacuna> vacunas = (List<Vacuna>) request.getAttribute("vacunas");
        if (vacunas != null) {
            for (Vacuna v : vacunas) {
    %>
    <tr>
        <td><%= v.getId() %></td>
        <td><%= v.getNombre() %></td>
        <td><%= v.getFecha() %></td>
        <td><%= v.getMascota().getNombre() %></td>
        <td>
            <a href="vacuna?action=eliminar&id=<%= v.getId() %>">Eliminar</a>
        </td>
    </tr>
    <%
            }
        }
    %>
</table>
</body>
</html>
