<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head><title>Registrar Mascota</title></head>
<body>
<h2>Registrar Mascota</h2>
<form action="mascota" method="post">
    <input type="hidden" name="action" value="registrar"/>
    Nombre: <input type="text" name="nombre" required/><br/>
    Tipo:
    <select name="tipo" required>
        <option value="PERRO">Perro</option>
        <option value="GATO">Gato</option>
    </select><br/>
    <button type="submit">Guardar</button>
</form>
</body>
</html>
