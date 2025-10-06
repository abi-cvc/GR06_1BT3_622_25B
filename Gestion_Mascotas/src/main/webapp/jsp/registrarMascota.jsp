<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <title>Registrar Mascota</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
<div class="container">
    <h2>Registrar Nueva Mascota</h2>

    <!-- Mostrar errores si el servlet los envía -->
    <c:if test="${not empty error}">
        <div class="error-message" style="color: red; margin-bottom: 10px;">
                ${error}
        </div>
    </c:if>

    <form action="${pageContext.request.contextPath}/mascota" method="post">
        <input type="hidden" name="action" value="registrar">

        <div class="form-group">
            <label for="nombre">Nombre:</label>
            <input type="text" id="nombre" name="nombre" value="${param.nombre}" required>
        </div>

        <div class="form-group">
            <label for="tipo">Tipo de Mascota:</label>
            <select id="tipo" name="tipo" required>
                <option value="">Seleccione un tipo</option>
                <c:forEach var="tipo" items="${tiposMascota}">
                    <option value="${tipo}" ${param.tipo == tipo ? 'selected' : ''}>${tipo}</option>
                </c:forEach>
            </select>
        </div>

        <div class="form-group">
            <label for="raza">Raza:</label>
            <input type="text" id="raza" name="raza" value="${param.raza}" required>
        </div>

        <div class="form-group">
            <label for="edad">Edad (años):</label>
            <input type="number" id="edad" name="edad" value="${param.edad}" min="0" required>
        </div>

        <div class="form-group">
            <label for="peso">Peso (kg):</label>
            <input type="number" id="peso" name="peso" value="${param.peso}" step="0.1" min="0" required>
        </div>

        <div class="form-group">
            <label for="color">Color:</label>
            <input type="text" id="color" name="color" value="${param.color}">
        </div>

        <div class="form-group">
            <label for="usuarioId">ID del Usuario Propietario:</label>
            <input type="number" id="usuarioId" name="usuarioId" value="${usuarioId}" readonly required>
            <small>(Pre-llenado desde su sesión)</small>
        </div>

        <button type="submit">Registrar Mascota</button>
    </form>

    <a href="${pageContext.request.contextPath}/mascota?action=listar">Volver a Lista de Mascotas</a>
    <a href="${pageContext.request.contextPath}/dashboard">Volver al Dashboard</a>
</div>
</body>
</html>