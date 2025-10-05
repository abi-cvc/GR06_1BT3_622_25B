<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="es">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Generar Sugerencia de Alimentación</title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
<jsp:include page="/jsp/header.jsp" />

<div class="container">
  <h1>Generar Sugerencia de Alimentación</h1>

  <c:if test="${not empty error}">
    <p class="error-message">${error}</p>
  </c:if>
  <c:if test="${not empty success}">
    <p class="success-message">Sugerencia de alimentación generada exitosamente.</p>
  </c:if>

  <form action="${pageContext.request.contextPath}/sugerencia" method="post">
    <input type="hidden" name="action" value="generar">
    <input type="hidden" name="tipoSugerencia" value="alimentacion">

    <div class="form-group">
      <label for="mascotaId">Mascota:</label>
      <select id="mascotaId" name="mascotaId" required>
        <option value="">Seleccione una mascota</option>
        <c:forEach var="mascota" items="${mascotas}">
          <option value="${mascota.id}">${mascota.nombre} (${mascota.tipo})</option>
        </c:forEach>
      </select>
    </div>

    <button type="submit" class="btn">Generar Sugerencia</button>
  </form>

  <c:if test="${not empty sugerencias}">
    <h2>Sugerencias de Alimentación Generadas</h2>
    <div class="list-container">
      <c:forEach var="sugerencia" items="${sugerencias}">
        <div class="list-item">
          <h3>${sugerencia.titulo}</h3>
          <p><strong>Mascota:</strong> ${sugerencia.mascota.nombre}</p>
          <p><strong>Fecha de Generación:</strong> ${sugerencia.fechaGeneracion}</p>
          <p><strong>Tipo de Alimento Sugerido:</strong> ${sugerencia.tipoAlimentoSugerido}</p>
          <p><strong>Cantidad Diaria:</strong> ${sugerencia.cantidadDiariaGramos} gramos</p>
          <p><strong>Frecuencia:</strong> ${sugerencia.frecuencia}</p>
          <p><strong>Consideraciones:</strong> ${sugerencia.consideraciones}</p>
          <p>${sugerencia.contenido}</p>
          <a href="${pageContext.request.contextPath}/sugerencia?action=eliminar&id=${sugerencia.id}" class="btn btn-danger">Eliminar</a>
        </div>
      </c:forEach>
    </div>
  </c:if>
</div>
</body>
</html>