<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="com.gestionmascotas.app.model.VisitaVeterinaria" %>
<html>
<head><title>Lista de Visitas Veterinarias</title></head>
<body>
<h2>Lista de Visitas Veterinarias</h2>
<a href="visita?action=registrar">Registrar Nueva Visita</a><br/><br/>

<table border="1" cellpadding="5">
    <tr>
        <th>ID</th>
        <th>Motivo</th>
        <th>Fecha</th>
        <th>Mascota</th>
        <th>Acciones</th>
    </tr>
    <%
        List<VisitaVeterinaria> visitas = (List<VisitaVeterinaria>) request.getAttribute("visitas");
        if (visitas != null) {
            for (VisitaVeterinaria v : visitas) {
    %>
    <tr>
        <td><%= v.getId() %></td>
        <td><%= v.getMotivo() %></td>
        <td><%= v.getFecha() %></td>
        <td><%= v.getMascota().getNombre() %></td>
        <td>
            <a href="visita?action=eliminar&id=<%= v.getId() %>">Eliminar</a>
        </td>
    </tr>
    <%
            }
        }
    %>
</table>
</body>
</html>
