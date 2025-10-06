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
          <a href="${pageContext.request.contextPath}/jsp/recordatorio/registrarRecordatorioAlimentacion.jsp?mascotaId=${mascota.id}" class="action-card-details">
            <i class="action-icon fas fa-utensils"></i>
            <h3>Configurar Recordatorio de Alimento</h3>
            <p>Establece horarios y tipo de comida para tu mascota.</p>
          </a>

          <a href="${pageContext.request.contextPath}/jsp/recordatorio/registrarRecordatorioPaseo.jsp?mascotaId=${mascota.id}" class="action-card-details">
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

<script src="${pageContext.request.contextPath}/js/dashboard.js"></script>
</body>
</html>