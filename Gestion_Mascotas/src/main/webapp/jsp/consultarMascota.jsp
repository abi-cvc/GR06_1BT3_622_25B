<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="es">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Detalles de Mascota</title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
<jsp:include page="/jsp/header.jsp" />

<div class="container">
  <h1>Detalles de Mascota</h1>

  <c:if test="${not empty error}">
    <p class="error-message">${error}</p>
  </c:if>

  <c:if test="${not empty mascota}">
    <div class="mascota-details">
      <h2>${mascota.nombre}</h2>
      <p><strong>ID:</strong> ${mascota.id}</p>
      <p><strong>Tipo:</strong> ${mascota.tipo}</p>
      <p><strong>Raza:</strong> ${mascota.raza}</p>
      <p><strong>Edad:</strong> ${mascota.edad} años</p>
      <p><strong>Peso:</strong> ${mascota.peso} kg</p>
      <p><strong>Color:</strong> ${mascota.color}</p>
      <p><strong>Dueño:</strong> ${mascota.usuario.nombreUsuario}</p>

      <h3>Vacunas</h3>
      <c:if test="${not empty mascota.vacunas}">
        <ul>
          <c:forEach var="vacuna" items="${mascota.vacunas}">
            <li>${vacuna.nombre} - ${vacuna.fecha}</li>
          </c:forEach>
        </ul>
      </c:if>
      <c:if test="${empty mascota.vacunas}">
        <p>No hay vacunas registradas para esta mascota.</p>
      </c:if>

      <h3>Visitas Veterinarias</h3>
      <c:if test="${not empty mascota.visitas}">
        <ul>
          <c:forEach var="visita" items="${mascota.visitas}">
            <li>${visita.fecha} - ${visita.motivo}</li>
          </c:forEach>
        </ul>
      </c:if>
      <c:if test="${empty mascota.visitas}">
        <p>No hay visitas veterinarias registradas para esta mascota.</p>
      </c:if>

      <h3>Recordatorios</h3>
      <c:if test="${not empty mascota.recordatorios}">
        <ul>
          <c:forEach var="recordatorio" items="${mascota.recordatorios}">
            <li>${recordatorio.descripcion} - ${recordatorio.fechaHoraRecordatorio} (Activo: ${recordatorio.activo ? 'Sí' : 'No'})</li>
          </c:forEach>
        </ul>
      </c:if>
      <c:if test="${empty mascota.recordatorios}">
        <p>No hay recordatorios registrados para esta mascota.</p>
      </c:if>

      <h3>Sugerencias</h3>
      <c:if test="${not empty mascota.sugerencias}">
        <ul>
          <c:forEach var="sugerencia" items="${mascota.sugerencias}">
            <li>${sugerencia.titulo} - ${sugerencia.fechaGeneracion}</li>
          </c:forEach>
        </ul>
      </c:if>
      <c:if test="${empty mascota.sugerencias}">
        <p>No hay sugerencias generadas para esta mascota.</p>
      </c:if>

      <a href="${pageContext.request.contextPath}/mascota?action=editar&id=${mascota.id}" class="btn">Editar Mascota</a>
      <a href="${pageContext.request.contextPath}/mascota?action=eliminar&id=${mascota.id}" class="btn btn-danger" onclick="return confirm('¿Está seguro de que desea eliminar esta mascota?');">Eliminar Mascota</a>
      <a href="${pageContext.request.contextPath}/mascota?action=listar" class="btn">Volver a la lista</a>
    </div>
  </c:if>
  <c:if test="${empty mascota}">
    <p>Mascota no encontrada o no especificada.</p>
    <a href="${pageContext.request.contextPath}/mascota?action=listar" class="btn">Volver a la lista de mascotas</a>
  </c:if>
</div>
</body>
</html>