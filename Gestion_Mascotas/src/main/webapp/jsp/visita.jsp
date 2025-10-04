<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="com.gestionmascotas.app.model.Mascota" %>
<html>
<head><title>Registrar Visita Veterinaria</title></head>
<body>
<h2>Registrar Visita Veterinaria</h2>
<form action="visita" method="post">
    <input type="hidden" name="action" value="registrar"/>
    Motivo: <input type="text" name="motivo" required/><br/>
    Fecha: <input type="date" name="fecha" required/><br/>
    Mascota:
    <select name="mascotaId" required>
        <%
            List<Mascota> mascotas = (List<Mascota>) request.getAttribute("mascotas");
            if (mascotas != null) {
                for (Mascota m : mascotas) {
        %>
        <option value="<%= m.getId() %>"><%= m.getNombre() %> - <%= m.getTipo() %></option>
        <%
                }
            }
        %>
    </select><br/>
    <button type="submit">Guardar</button>
</form>
</body>
</html>
