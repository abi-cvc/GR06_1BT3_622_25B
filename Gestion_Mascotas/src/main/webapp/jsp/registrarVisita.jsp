<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="es">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Registrar Visita - Gestión de Mascotas</title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/dashboard.css">
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
  <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
</head>
<body>
<nav class="navbar">
  <div class="navbar-container">
    <div class="navbar-brand">
      <i class="fas fa-paw"></i>
      <span>Gestión de Mascotas</span>
    </div>
    <ul class="navbar-menu">
      <li><a href="${pageContext.request.contextPath}/dashboard"><i class="fas fa-home"></i> Inicio</a></li>
      <li><a href="${pageContext.request.contextPath}/mascotas"><i class="fas fa-dog"></i> Mis Mascotas</a></li>
      <li><a href="${pageContext.request.contextPath}/vacunas"><i class="fas fa-syringe"></i> Vacunas</a></li>
      <li><a href="${pageContext.request.contextPath}/visitas" class="active"><i class="fas fa-stethoscope"></i> Visitas</a></li>
    </ul>
  </div>
</nav>

<main class="main-content">
  <div class="container" style="max-width: 800px;">
    <section class="section-header">
      <h1><i class="fas fa-stethoscope"></i> Registrar Visita Veterinaria</h1>
    </section>

    <c:if test="${not empty error}">
      <div class="alert alert-error">
        <i class="fas fa-exclamation-circle"></i>
        <span>${error}</span>
      </div>
    </c:if>

    <div style="background: white; border-radius: 12px; padding: 2rem; box-shadow: 0 4px 6px rgba(0,0,0,0.1);">
      <form action="${pageContext.request.contextPath}/visita" method="post">
        <input type="hidden" name="action" value="registrar">

        <div class="form-group">
          <label for="mascotaId">
            <i class="fas fa-paw"></i> Seleccionar Mascota <span class="required">*</span>
          </label>
          <select id="mascotaId" name="mascotaId" required class="form-control">
            <option value="" disabled selected>Seleccione una mascota</option>
            <c:forEach var="mascota" items="${mascotas}">
              <option value="${mascota.id}">
                  ${mascota.nombre} - ${mascota.tipo}
              </option>
            </c:forEach>
          </select>
          <small class="help-text">Selecciona la mascota que visitó al veterinario</small>
        </div>

        <div class="form-group">
          <label for="fecha">
            <i class="fas fa-calendar"></i> Fecha de la Visita <span class="required">*</span>
          </label>
          <input type="date" id="fecha" name="fecha" required max="<%= java.time.LocalDate.now() %>">
          <small class="help-text">Fecha en que se realizó la visita</small>
        </div>

        <div class="form-group">
          <label for="motivo">
            <i class="fas fa-notes-medical"></i> Motivo de la Visita <span class="required">*</span>
          </label>
          <textarea id="motivo" name="motivo" rows="5" required
                    placeholder="Describe el motivo de la visita veterinaria..."></textarea>
          <small class="help-text">Ej: Control de rutina, vacunación, consulta por síntomas</small>
        </div>

        <div class="modal-actions">
          <a href="${pageContext.request.contextPath}/visitas" class="btn btn-secondary">
            <i class="fas fa-times"></i> Cancelar
          </a>
          <button type="submit" class="btn btn-primary">
            <i class="fas fa-plus-circle"></i> Registrar Visita
          </button>
        </div>
      </form>
    </div>
  </div>
</main>
</body>
</html>