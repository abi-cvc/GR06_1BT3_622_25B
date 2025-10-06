<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="es">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Detalles de ${mascota.nombre} - Gestión de Mascotas</title>
  <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css">
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/dashboard.css">
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
  <style>
    /* Estilos específicos para la sección de detalles de mascota */
    .mascota-details-card {
      background: var(--bg-primary);
      border-radius: var(--radius-xl);
      padding: 2.5rem;
      margin-bottom: 2rem;
      box-shadow: var(--shadow-xl);
      display: flex;
      flex-direction: column;
      align-items: center;
      text-align: center;
    }

    .mascota-details-card .icon-large {
      font-size: 6rem;
      color: var(--primary-color);
      margin-bottom: 1.5rem;
      filter: drop-shadow(0 4px 8px rgba(0, 0, 0, 0.1));
    }

    .mascota-details-card h1 {
      font-size: 2.5rem;
      font-weight: 700;
      color: var(--text-primary);
      margin-bottom: 1rem;
    }

    .mascota-details-card .info-grid {
      display: grid;
      grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
      gap: 1rem;
      margin-top: 1.5rem;
      width: 100%;
      max-width: 800px;
    }

    .mascota-details-card .info-item {
      background: var(--bg-tertiary);
      border-radius: var(--radius-md);
      padding: 1rem;
      display: flex;
      align-items: center;
      gap: 0.75rem;
      text-align: left;
    }

    .mascota-details-card .info-item i {
      color: var(--primary-color);
      font-size: 1.25rem;
    }

    .mascota-details-card .info-item span {
      font-size: 0.95rem;
      color: var(--text-secondary);
    }

    .mascota-details-card .info-item strong {
      color: var(--text-primary);
      font-weight: 600;
    }

    /* Estilos para la sección de acciones rápidas */
    .quick-actions-details {
      margin-top: 3rem;
    }

    .quick-actions-details h2 {
      font-size: 1.875rem;
      color: var(--text-primary);
      margin-bottom: 1.5rem;
      text-align: center;
    }

    .actions-grid-details {
      display: grid;
      grid-template-columns: repeat(auto-fit, minmax(280px, 1fr));
      gap: 1.5rem;
    }

    .action-card-details {
      background: var(--bg-primary);
      border-radius: var(--radius-lg);
      padding: 2rem;
      box-shadow: var(--shadow-md);
      text-decoration: none;
      color: var(--text-primary);
      transition: var(--transition);
      text-align: center;
      display: flex;
      flex-direction: column;
      justify-content: center;
      align-items: center;
    }

    .action-card-details:hover {
      transform: translateY(-5px);
      box-shadow: var(--shadow-lg);
      background: linear-gradient(135deg, var(--primary-light), var(--primary-color));
      color: white;
    }

    .action-card-details .action-icon {
      font-size: 3rem;
      margin-bottom: 1rem;
    }

    .action-card-details h3 {
      font-size: 1.25rem;
      margin-bottom: 0.5rem;
    }

    .action-card-details p {
      font-size: 0.875rem;
      opacity: 0.9;
    }

    /* Estilos para el modal */
    .modal {
      display: none;
      position: fixed;
      z-index: 1000;
      left: 0;
      top: 0;
      width: 100%;
      height: 100%;
      overflow: auto;
      background-color: rgba(0, 0, 0, 0.5);
    }

    .modal-content {
      background-color: var(--bg-primary);
      margin: 5% auto;
      padding: 2rem;
      border-radius: var(--radius-lg);
      width: 90%;
      max-width: 500px;
      box-shadow: var(--shadow-xl);
      position: relative;
      max-height: 80vh;
      overflow-y: auto;
    }

    .close-button {
      color: var(--text-secondary);
      float: right;
      font-size: 28px;
      font-weight: bold;
      cursor: pointer;
      line-height: 1;
    }

    .close-button:hover,
    .close-button:focus {
      color: var(--primary-color);
    }

    .modal h2 {
      color: var(--text-primary);
      margin-top: 0;
      margin-bottom: 1.5rem;
      text-align: center;
    }

    .form-group {
      margin-bottom: 1.5rem;
    }

    .form-group label {
      display: block;
      margin-bottom: 0.5rem;
      font-weight: 600;
      color: var(--text-primary);
    }

    .form-group input[type="text"],
    .form-group input[type="time"],
    .form-group select {
      width: 100%;
      padding: 0.75rem;
      border: 1px solid var(--border-color);
      border-radius: var(--radius-sm);
      background: var(--bg-secondary);
      color: var(--text-primary);
      box-sizing: border-box;
    }

    .form-group input[type="checkbox"] {
      margin-right: 0.5rem;
    }

    .dias-semana-group {
      display: flex;
      flex-wrap: wrap;
      gap: 1rem;
    }

    .dias-semana-group label {
      display: flex;
      align-items: center;
      font-size: 0.95rem;
      color: var(--text-secondary);
      cursor: pointer;
    }

    .btn-primary {
      background: var(--primary-color);
      color: white;
      padding: 0.75rem 1.5rem;
      border: none;
      border-radius: var(--radius-sm);
      cursor: pointer;
      width: 100%;
      font-size: 1rem;
      transition: var(--transition);
    }

    .btn-primary:hover {
      background: var(--primary-dark);
    }

    /* Responsive */
    @media (max-width: 768px) {
      .mascota-details-card {
        padding: 1.5rem;
      }
      .mascota-details-card .icon-large {
        font-size: 4rem;
      }
      .mascota-details-card h1 {
        font-size: 2rem;
      }
      .mascota-details-card .info-grid {
        grid-template-columns: 1fr;
      }
      .quick-actions-details h2 {
        font-size: 1.5rem;
      }
      .actions-grid-details {
        grid-template-columns: 1fr;
      }
      .modal-content {
        width: 95%;
        margin: 10% auto;
        padding: 1.5rem;
      }
    }
  </style>
</head>
<body>
<!-- Navbar -->
<nav class="navbar">
  <div class="navbar-container">
    <div class="navbar-brand">
      <i class="fas fa-paw"></i>
      <span>Gestión de Mascotas</span>
    </div>
    <ul class="navbar-menu">
      <li><a href="${pageContext.request.contextPath}/dashboard"><i class="fas fa-home"></i> Inicio</a></li>
      <li><a href="${pageContext.request.contextPath}/mascotas" class="active"><i class="fas fa-dog"></i> Mis Mascotas</a></li>
      <li><a href="${pageContext.request.contextPath}/vacunas"><i class="fas fa-syringe"></i> Vacunas</a></li>
      <li><a href="${pageContext.request.contextPath}/visitas"><i class="fas fa-stethoscope"></i> Visitas</a></li>
    </ul>
    <div class="navbar-user">
      <button class="btn-logout-mobile" onclick="confirmarLogout()">
        <i class="fas fa-sign-out-alt"></i>
      </button>
    </div>
  </div>
</nav>

<main class="main-content">
  <div class="container">
    <a href="${pageContext.request.contextPath}/mascota?action=listar" class="btn btn-secondary" style="margin-bottom: 1.5rem;">
      <i class="fas fa-arrow-left"></i> Volver a Mis Mascotas
    </a>

    <c:if test="${not empty success}">
      <div class="alert alert-success">
        <i class="fas fa-check-circle"></i>
        <span>${success}</span>
      </div>
    </c:if>
    <c:if test="${not empty error}">
      <div class="alert alert-error">
        <i class="fas fa-exclamation-circle"></i>
        <span>${error}</span>
      </div>
    </c:if>

    <c:if test="${mascota != null}">
      <div class="mascota-details-card">
        <i class="icon-large fas <c:if test='${mascota.tipo == "PERRO"}'>fa-dog</c:if><c:if test='${mascota.tipo == "GATO"}'>fa-cat</c:if>"></i>
        <h1>${mascota.nombre}</h1>
        <p style="color: var(--text-secondary); font-size: 1.1rem; margin-top: -0.5rem;">¡Tu compañero de vida!</p>

        <div class="info-grid">
          <div class="info-item">
            <i class="fas fa-tag"></i>
            <span>Tipo: <strong>${mascota.tipo}</strong></span>
          </div>
          <div class="info-item">
            <i class="fas fa-dna"></i>
            <span>Raza: <strong>${mascota.raza}</strong></span>
          </div>
          <div class="info-item">
            <i class="fas fa-birthday-cake"></i>
            <span>Edad: <strong>${mascota.edad} años</strong></span>
          </div>
          <div class="info-item">
            <i class="fas fa-weight-hanging"></i>
            <span>Peso: <strong>${mascota.peso} kg</strong></span>
          </div>
          <div class="info-item">
            <i class="fas fa-palette"></i>
            <span>Color: <strong>${mascota.color}</strong></span>
          </div>
          <div class="info-item">
            <i class="fas fa-user"></i>
            <span>Dueño: <strong>${mascota.usuario.nombre}</strong></span>
          </div>
        </div>
      </div>

      <section class="quick-actions-details">
        <h2><i class="fas fa-bolt"></i> Acciones Rápidas para ${mascota.nombre}</h2>
        <div class="actions-grid-details">
          <a href="#" class="action-card-details" id="openRecordatorioAlimentacionModal">
            <i class="action-icon fas fa-utensils"></i>
            <h3>Configurar Recordatorio de Alimento</h3>
            <p>Establece horarios y tipo de comida para tu mascota.</p>
          </a>

          <a href="${pageContext.request.contextPath}/jsp/registrarRecordatorioPaseo.jsp?mascotaId=${mascota.id}" class="action-card-details">
            <i class="action-icon fas fa-running"></i>
            <h3>Configurar Recordatorio de Ejercicio</h3>
            <p>Planifica paseos y actividades para mantenerla activa.</p>
          </a>

          <a href="${pageContext.request.contextPath}/sugerencia?action=generar&mascotaId=${mascota.id}" class="action-card-details">
            <i class="action-icon fas fa-lightbulb"></i>
            <h3>Ver Recomendaciones</h3>
            <p>Obtén sugerencias personalizadas de alimentación y ejercicio.</p>
          </a>
        </div>
      </section>
    </c:if>
    <c:if test="${mascota == null}">
      <div class="alert alert-error">
        <i class="fas fa-exclamation-circle"></i>
        <span>No se pudo encontrar la información de la mascota.</span>
      </div>
    </c:if>
  </div>
</main>

<!-- Modal para Configurar Recordatorio de Alimentación -->
<div id="recordatorioAlimentacionModal" class="modal">
  <div class="modal-content">
    <span class="close-button">&times;</span>
    <h2>Configurar Recordatorio de Alimentación para ${mascota.nombre}</h2>
    <form action="${pageContext.request.contextPath}/recordatorioAlimentacion" method="post">
      <input type="hidden" name="action" value="registrar">
      <input type="hidden" name="mascotaId" value="${mascota.id}">

      <div class="form-group">
        <label for="frecuencia">Frecuencia:</label>
        <select id="frecuencia" name="frecuencia" required onchange="mostrarSelectoresHora()">
          <option value="">Selecciona la frecuencia</option>
          <option value="1">1 vez al día</option>
          <option value="2">2 veces al día</option>
          <option value="3">3 veces al día</option>
        </select>
      </div>

      <div id="horariosContainer">
        <!-- Los selectores de hora se insertarán aquí dinámicamente -->
      </div>

      <div class="form-group">
        <label for="tipoAlimento">Tipo de Alimento:</label>
        <input type="text" id="tipoAlimento" name="tipoAlimento" placeholder="Ej: Pienso seco, comida húmeda" required>
      </div>

      <div class="form-group">
        <label>Días para el recordatorio:</label>
        <div class="dias-semana-group">
          <label><input type="checkbox" name="diasSemana" value="Lunes"> Lunes</label>
          <label><input type="checkbox" name="diasSemana" value="Martes"> Martes</label>
          <label><input type="checkbox" name="diasSemana" value="Miércoles"> Miércoles</label>
          <label><input type="checkbox" name="diasSemana" value="Jueves"> Jueves</label>
          <label><input type="checkbox" name="diasSemana" value="Viernes"> Viernes</label>
          <label><input type="checkbox" name="diasSemana" value="Sábado"> Sábado</label>
          <label><input type="checkbox" name="diasSemana" value="Domingo"> Domingo</label>
        </div>
      </div>

      <button type="submit" class="btn-primary">Guardar Recordatorio</button>
    </form>
  </div>
</div>

<script src="${pageContext.request.contextPath}/js/dashboard.js"></script>
<script>
  document.addEventListener('DOMContentLoaded', function() {
    var modal = document.getElementById("recordatorioAlimentacionModal");
    var btn = document.getElementById("openRecordatorioAlimentacionModal");
    var span = document.getElementsByClassName("close-button")[0];

    if (btn) {
      btn.onclick = function(event) {
        event.preventDefault(); // Evita que el enlace navegue
        modal.style.display = "block";
        mostrarSelectoresHora(); // Asegura que los selectores se muestren al abrir el modal
      }
    }

    if (span) {
      span.onclick = function() {
        modal.style.display = "none";
      }
    }

    window.onclick = function(event) {
      if (event.target == modal) {
        modal.style.display = "none";
      }
    }
  });

  function mostrarSelectoresHora() {
    var frecuencia = document.getElementById("frecuencia").value;
    var horariosContainer = document.getElementById("horariosContainer");
    horariosContainer.innerHTML = ''; // Limpiar selectores anteriores

    if (frecuencia) {
      var numHorarios = parseInt(frecuencia);
      for (var i = 1; i <= numHorarios; i++) {
        var div = document.createElement('div');
        div.className = 'form-group';
        div.innerHTML = `
          <label for="horario${i}">Hora de Alimentación ${i}:</label>
          <input type="time" id="horario${i}" name="horario${i}" required>
        `;
        horariosContainer.appendChild(div);
      }
    }
  }
</script>
</body>
</html>