<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="es">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Registrar Recordatorio de Alimentación</title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
<jsp:include page="/jsp/header.jsp" />

<div class="container">
  <h1>Registrar Recordatorio de Alimentación</h1>

  <c:if test="${not empty error}">
    <p class="error-message">${error}</p>
  </c:if>
  <c:if test="${not empty mensaje}">
    <p class="success-message">${mensaje}</p>
  </c:if>

  <form action="${pageContext.request.contextPath}/recordatorio" method="post">
    <input type="hidden" name="action" value="registrar">
    <input type="hidden" name="tipoRecordatorio" value="alimentacion">

    <div class="form-group">
      <label for="mascotaId">Mascota:</label>
      <select id="mascotaId" name="mascotaId" required>
        <option value="">Seleccione una mascota</option>
        <c:forEach var="mascota" items="${mascotas}">
          <option value="${mascota.id}">${mascota.nombre} (${mascota.tipo})</option>
        </c:forEach>
      </select>
    </div>

    <div class="form-group">
      <label for="descripcion">Descripción:</label>
      <input type="text" id="descripcion" name="descripcion" required>
    </div>

    <div class="form-group">
      <label for="fechaHora">Fecha y Hora (YYYY-MM-DDTHH:MM):</label>
      <input type="datetime-local" id="fechaHora" name="fechaHora" required>
    </div>

    <div class="form-group">
      <label for="frecuencia">Frecuencia:</label>
      <select id="frecuencia" name="frecuencia" required>
        <option value="Diario">Diario</option>
        <option value="Semanal">Semanal</option>
        <option value="Cada 2 días">Cada 2 días</option>
        <option value="Cada 3 días">Cada 3 días</option>
      </select>
    </div>

    <div class="form-group">
      <label for="diasSemana">Días de la Semana (ej: Lunes,Miércoles,Viernes):</label>
      <input type="text" id="diasSemana" name="diasSemana" placeholder="Lunes,Miércoles,Viernes">
    </div>

    <div class="form-group">
      <label for="horarios">Horarios (ej: 08:00,13:00,19:00):</label>
      <input type="text" id="horarios" name="horarios" required placeholder="08:00,13:00,19:00">
    </div>

    <div class="form-group">
      <label for="tipoAlimento">Tipo de Alimento:</label>
      <input type="text" id="tipoAlimento" name="tipoAlimento" required>
    </div>

    <button type="submit" class="btn">Registrar Recordatorio</button>
  </form>
</div>
</body>
</html>