<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
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
      cursor: pointer;
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

    .modal-content.modal-confirm {
      max-width: 450px;
    }

    .modal-header {
      display: flex;
      align-items: center;
      gap: 1rem;
      margin-bottom: 1.5rem;
      padding-bottom: 1rem;
      border-bottom: 2px solid var(--border-color);
    }

    .modal-header.danger {
      color: #dc3545;
      border-bottom-color: #dc3545;
    }

    .modal-header i {
      font-size: 2rem;
    }

    .modal-header h2 {
      margin: 0;
      font-size: 1.5rem;
    }

    .modal-body {
      margin-bottom: 1.5rem;
    }

    .modal-body p {
      margin-bottom: 1rem;
      line-height: 1.6;
      color: var(--text-secondary);
    }

    .modal-actions {
      display: flex;
      gap: 1rem;
      justify-content: flex-end;
      margin-top: 1.5rem;
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
    .form-group input[type="number"],
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

    /* Sección de recordatorios existentes */
    .recordatorios-section {
      margin-top: 3rem;
      background: var(--bg-primary);
      border-radius: var(--radius-lg);
      padding: 2rem;
      box-shadow: var(--shadow-md);
    }

    .recordatorios-section h2 {
      font-size: 1.875rem;
      color: var(--text-primary);
      margin-bottom: 1.5rem;
      text-align: center;
    }

    .recordatorio-card {
      background: var(--bg-secondary);
      border-radius: var(--radius-md);
      padding: 1.5rem;
      margin-bottom: 1rem;
      box-shadow: var(--shadow-sm);
    }

    .recordatorio-card-header {
      display: flex;
      justify-content: space-between;
      align-items: center;
      margin-bottom: 1rem;
    }

    .recordatorio-card h3 {
      font-size: 1.25rem;
      color: var(--text-primary);
      margin: 0;
    }

    .recordatorio-card-body {
      display: grid;
      gap: 0.75rem;
    }

    .recordatorio-info {
      display: flex;
      align-items: center;
      gap: 0.5rem;
      color: var(--text-secondary);
    }

    .recordatorio-info i {
      color: var(--primary-color);
    }

    .recordatorio-actions {
      display: flex;
      gap: 0.5rem;
      margin-top: 1rem;
    }

    .btn-sm {
      padding: 0.5rem 1rem;
      font-size: 0.875rem;
      border-radius: var(--radius-sm);
      border: none;
      cursor: pointer;
      transition: var(--transition);
    }

    .btn-danger {
      background: #dc3545;
      color: white;
    }

    .btn-danger:hover {
      background: #c82333;
    }

    .btn-secondary {
      background: #6c757d;
      color: white;
    }

    .btn-secondary:hover {
      background: #5a6268;
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
      .modal-actions {
        flex-direction: column;
      }
      .modal-actions button {
        width: 100%;
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
      <li><a href="${pageContext.request.contextPath}/mascota?action=listar" class="active"><i class="fas fa-dog"></i> Mis Mascotas</a></li>
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

    <!-- Mensajes de éxito/error -->
    <c:if test="${not empty param.success}">
      <div class="alert alert-success">
        <i class="fas fa-check-circle"></i>
        <span>
          <c:choose>
            <c:when test="${param.success == 'recordatorio_alimentacion_registrado'}">
              ✅ Recordatorio de alimentación registrado exitosamente
            </c:when>
            <c:when test="${param.success == 'recordatorio_alimentacion_eliminado'}">
              ✅ Recordatorio de alimentación eliminado exitosamente
            </c:when>
            <c:when test="${param.success == 'recordatorio_paseo_registrado'}">
              ✅ Recordatorio de paseo registrado exitosamente
            </c:when>
            <c:when test="${param.success == 'recordatorio_paseo_eliminado'}">
              ✅ Recordatorio de paseo eliminado exitosamente
            </c:when>
            <c:when test="${param.success == 'recordatorio_actualizado'}">
              ✅ Recordatorio actualizado exitosamente
            </c:when>
            <c:when test="${param.success == 'recordatorio_desactivado'}">
              ✅ Recordatorio desactivado exitosamente
            </c:when>
          </c:choose>
        </span>
      </div>
    </c:if>

    <c:if test="${not empty param.error}">
      <div class="alert alert-error">
        <i class="fas fa-exclamation-circle"></i>
        <span>
          <c:choose>
            <c:when test="${param.error == 'mascota_no_encontrada'}">
              ❌ Mascota no encontrada
            </c:when>
            <c:when test="${param.error == 'horarios_vacios'}">
              ❌ Debes especificar al menos un horario
            </c:when>
            <c:when test="${param.error == 'horarios_paseo_vacios'}">
              ❌ Debes especificar al menos un horario de paseo
            </c:when>
            <c:when test="${param.error == 'formato_invalido'}">
              ❌ Formato de datos inválido
            </c:when>
            <c:when test="${param.error == 'tipo_alimento_vacio'}">
              ❌ El tipo de alimento es obligatorio
            </c:when>
            <c:when test="${param.error == 'formato_hora_invalido'}">
              ❌ Formato de hora inválido
            </c:when>
            <c:when test="${param.error == 'duracion_invalida'}">
              ❌ La duración del paseo debe ser mayor a 0
            </c:when>
            <c:when test="${param.error == 'acceso_denegado'}">
              ❌ No tienes permiso para realizar esta acción
            </c:when>
            <c:when test="${param.error == 'error_guardar_recordatorio'}">
              ❌ Error al guardar el recordatorio
            </c:when>
            <c:when test="${param.error == 'error_registro_recordatorio_alimentacion'}">
              ❌ Error al registrar el recordatorio de alimentación
            </c:when>
            <c:when test="${param.error == 'error_registro_recordatorio_paseo'}">
              ❌ Error al registrar el recordatorio de paseo
            </c:when>
            <c:when test="${param.error == 'error_eliminando_recordatorio_alimentacion'}">
              ❌ Error al eliminar el recordatorio de alimentación
            </c:when>
            <c:when test="${param.error == 'error_eliminando_recordatorio_paseo'}">
              ❌ Error al eliminar el recordatorio de paseo
            </c:when>
          </c:choose>
        </span>
      </div>
    </c:if>

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
      <!-- Detalles de la mascota -->
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

      <!-- Acciones Rápidas -->
      <section class="quick-actions-details">
        <h2><i class="fas fa-bolt"></i> Acciones Rápidas para ${mascota.nombre}</h2>
        <div class="actions-grid-details">
          <a href="#" class="action-card-details" id="openRecordatorioAlimentacionModal">
            <i class="action-icon fas fa-utensils"></i>
            <h3>Configurar Recordatorio de Alimento</h3>
            <p>Establece horarios y tipo de comida para tu mascota.</p>
          </a>

          <a href="#" class="action-card-details" id="openRecordatorioPaseoModal">
            <i class="action-icon fas fa-running"></i>
            <h3>Configurar Recordatorio de Paseo</h3>
            <p>Planifica paseos y actividades para mantenerla activa.</p>
          </a>

          <a href="${pageContext.request.contextPath}/sugerencia?action=generar&mascotaId=${mascota.id}" class="action-card-details">
            <i class="action-icon fas fa-lightbulb"></i>
            <h3>Ver Recomendaciones</h3>
            <p>Obtén sugerencias personalizadas de alimentación y ejercicio.</p>
          </a>

            <a href="${pageContext.request.contextPath}/historialPeso?action=listar&mascotaId=${mascota.id}" class="action-card-details">
                <i class="action-icon fas fa-weight"></i>
                <h3>Historial de Peso</h3>
                <p>Registra y consulta la evolución del peso de tu mascota.</p>
            </a>

        </div>
      </section>



      <!-- Sección de Recordatorios de Alimentación Existentes -->
      <c:if test="${not empty recordatoriosAlimentacion}">
        <section class="recordatorios-section">
          <h2><i class="fas fa-bell"></i> Recordatorios de Alimentación Activos</h2>

          <c:forEach var="recordatorio" items="${recordatoriosAlimentacion}">
            <div class="recordatorio-card">
              <div class="recordatorio-card-header">
                <h3><i class="fas fa-utensils"></i> ${recordatorio.tipoAlimento}</h3>
              </div>

              <div class="recordatorio-card-body">
                <div class="recordatorio-info">
                  <i class="fas fa-bell"></i>
                  <span><strong>Horarios:</strong>
                    <c:forEach var="hora" items="${recordatorio.listaHorarios}" varStatus="status">
                      ${hora}<c:if test="${!status.last}">, </c:if>
                    </c:forEach>
                  </span>
                </div>

                <c:if test="${not empty recordatorio.diasSemana}">
                  <div class="recordatorio-info">
                    <i class="fas fa-calendar-alt"></i>
                    <span><strong>Días:</strong> ${recordatorio.diasSemana}</span>
                  </div>
                </c:if>

                <div class="recordatorio-info">
                  <i class="fas fa-info-circle"></i>
                  <span><strong>Estado:</strong>
                    <c:choose>
                      <c:when test="${recordatorio.activo}">
                        <span style="color: #28a745;">Activo</span>
                      </c:when>
                      <c:otherwise>
                        <span style="color: #dc3545;">Inactivo</span>
                      </c:otherwise>
                    </c:choose>
                  </span>
                </div>
              </div>

              <div class="recordatorio-actions">
                <button type="button"
                        class="btn-sm btn-danger"
                        onclick="confirmarEliminarRecordatorioAlimentacion(${recordatorio.id}, '${recordatorio.tipoAlimento}')">
                  <i class="fas fa-trash-alt"></i> Eliminar
                </button>
              </div>
            </div>
          </c:forEach>
        </section>
      </c:if>

      <!-- Sección de Recordatorios de Paseo Existentes -->
      <c:if test="${not empty recordatoriosPaseo}">
        <section class="recordatorios-section">
          <h2><i class="fas fa-running"></i> Recordatorios de Paseo Activos</h2>

          <c:forEach var="recordatorio" items="${recordatoriosPaseo}">
            <div class="recordatorio-card">
              <div class="recordatorio-card-header">
                <h3><i class="fas fa-walking"></i> Recordatorio de Paseo</h3>
              </div>

              <div class="recordatorio-card-body">
                <div class="recordatorio-info">
                  <i class="fas fa-bell"></i>
                  <span><strong>Horarios:</strong>
                    <c:forEach var="hora" items="${recordatorio.listaHorarios}" varStatus="status">
                      ${hora}<c:if test="${!status.last}">, </c:if>
                    </c:forEach>
                  </span>
                </div>

                <c:if test="${not empty recordatorio.duracionMinutos}">
                  <div class="recordatorio-info">
                    <i class="fas fa-stopwatch"></i>
                    <span><strong>Duración:</strong> ${recordatorio.duracionMinutos} minutos</span>
                  </div>
                </c:if>

                <c:if test="${not empty recordatorio.diasSemana}">
                  <div class="recordatorio-info">
                    <i class="fas fa-calendar-alt"></i>
                    <span><strong>Días:</strong> ${recordatorio.diasSemana}</span>
                  </div>
                </c:if>

                <div class="recordatorio-info">
                  <i class="fas fa-info-circle"></i>
                  <span><strong>Estado:</strong>
                    <c:choose>
                      <c:when test="${recordatorio.activo}">
                        <span style="color: #28a745;">Activo</span>
                      </c:when>
                      <c:otherwise>
                        <span style="color: #dc3545;">Inactivo</span>
                      </c:otherwise>
                    </c:choose>
                  </span>
                </div>
              </div>

              <div class="recordatorio-actions">
                <button type="button"
                        class="btn-sm btn-danger"
                        onclick="confirmarEliminarRecordatorioPaseo(${recordatorio.id})">
                  <i class="fas fa-trash-alt"></i> Eliminar
                </button>
              </div>
            </div>
          </c:forEach>
        </section>
      </c:if>
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
    <span class="close-button" id="closeAlimentacion">&times;</span>
    <h2>Configurar Recordatorio de Alimentación para ${mascota.nombre}</h2>

    <form action="${pageContext.request.contextPath}/recordatorioAlimentacion" method="post" id="formRecordatorioAlimentacion">
      <input type="hidden" name="action" value="registrar">
      <input type="hidden" name="mascotaId" value="${mascota.id}">

      <div class="form-group">
        <label for="frecuencia"><i class="fas fa-clock"></i> Frecuencia de alimentación:</label>
        <select id="frecuencia" name="frecuencia" required onchange="mostrarSelectoresHoraAlimentacion()" class="form-control">
          <option value="">-- Selecciona la frecuencia --</option>
          <option value="1">1 vez al día</option>
          <option value="2">2 veces al día</option>
          <option value="3">3 veces al día</option>
        </select>
      </div>

      <div id="horariosContainer">
        <!-- Los selectores de hora se insertarán aquí dinámicamente -->
      </div>

      <div class="form-group">
        <label for="tipoAlimento"><i class="fas fa-utensils"></i> Tipo de Alimento: *</label>
        <input type="text"
               id="tipoAlimento"
               name="tipoAlimento"
               placeholder="Ej: Pienso seco, comida húmeda, alimento balanceado"
               required
               maxlength="100"
               class="form-control">
      </div>

      <div class="form-group">
        <label><i class="fas fa-calendar-week"></i> Días para el recordatorio: (opcional)</label>
        <div class="dias-semana-group">
          <label><input type="checkbox" name="diasSemana" value="Lunes"> Lunes</label>
          <label><input type="checkbox" name="diasSemana" value="Martes"> Martes</label>
          <label><input type="checkbox" name="diasSemana" value="Miércoles"> Miércoles</label>
          <label><input type="checkbox" name="diasSemana" value="Jueves"> Jueves</label>
          <label><input type="checkbox" name="diasSemana" value="Viernes"> Viernes</label>
          <label><input type="checkbox" name="diasSemana" value="Sábado"> Sábado</label>
          <label><input type="checkbox" name="diasSemana" value="Domingo"> Domingo</label>
        </div>
        <small style="color: var(--text-secondary);">Si no seleccionas días, el recordatorio estará activo todos los días</small>
      </div>

      <button type="submit" class="btn-primary">
        <i class="fas fa-save"></i> Guardar Recordatorio
      </button>
    </form>
  </div>
</div>

<!-- Modal para Configurar Recordatorio de Paseo -->
<div id="recordatorioPaseoModal" class="modal">
  <div class="modal-content">
    <span class="close-button" id="closePaseo">&times;</span>
    <h2>Configurar Recordatorio de Paseo para ${mascota.nombre}</h2>

    <form action="${pageContext.request.contextPath}/recordatorioPaseo" method="post" id="formRecordatorioPaseo">
      <input type="hidden" name="action" value="registrar">
      <input type="hidden" name="mascotaId" value="${mascota.id}">

      <div class="form-group">
        <label for="frecuenciaPaseo"><i class="fas fa-clock"></i> Frecuencia de paseos:</label>
        <select id="frecuenciaPaseo" name="frecuenciaPaseo" required onchange="mostrarSelectoresHoraPaseo()" class="form-control">
          <option value="">-- Selecciona la frecuencia --</option>
          <option value="1">1 vez al día</option>
          <option value="2">2 veces al día</option>
          <option value="3">3 veces al día</option>
          <option value="4">4 veces al día</option>
        </select>
      </div>

      <div id="horariosPaseoContainer">
        <!-- Los selectores de hora se insertarán aquí dinámicamente -->
      </div>

      <div class="form-group">
        <label for="duracionMinutos"><i class="fas fa-stopwatch"></i> Duración del paseo (minutos):</label>
        <input type="number"
               id="duracionMinutos"
               name="duracionMinutos"
               placeholder="Ej: 30"
               min="1"
               max="300"
               class="form-control">
        <small style="color: var(--text-secondary);">Duración estimada en minutos (opcional)</small>
      </div>

      <div class="form-group">
        <label><i class="fas fa-calendar-week"></i> Días para el recordatorio: (opcional)</label>
        <div class="dias-semana-group">
          <label><input type="checkbox" name="diasSemanaPaseo" value="Lunes"> Lunes</label>
          <label><input type="checkbox" name="diasSemanaPaseo" value="Martes"> Martes</label>
          <label><input type="checkbox" name="diasSemanaPaseo" value="Miércoles"> Miércoles</label>
          <label><input type="checkbox" name="diasSemanaPaseo" value="Jueves"> Jueves</label>
          <label><input type="checkbox" name="diasSemanaPaseo" value="Viernes"> Viernes</label>
          <label><input type="checkbox" name="diasSemanaPaseo" value="Sábado"> Sábado</label>
          <label><input type="checkbox" name="diasSemanaPaseo" value="Domingo"> Domingo</label>
        </div>
        <small style="color: var(--text-secondary);">Si no seleccionas días, el recordatorio estará activo todos los días</small>
      </div>

      <button type="submit" class="btn-primary">
        <i class="fas fa-save"></i> Guardar Recordatorio
      </button>
    </form>
  </div>
</div>

<!-- Modal Eliminar Recordatorio de Alimentación -->
<div id="modalEliminarRecordatorioAlimentacion" class="modal">
  <div class="modal-content modal-confirm">
    <div class="modal-header danger">
      <i class="fas fa-exclamation-triangle"></i>
      <h2>Confirmar Eliminación</h2>
    </div>
    <div class="modal-body">
      <p><strong>¿Estás seguro de eliminar el recordatorio de alimentación "<span id="eliminarTipoAlimento"></span>"?</strong></p>
      <p>Esta acción es irreversible.</p>
      <form id="formEliminarRecordatorioAlimentacion" method="get" action="${pageContext.request.contextPath}/recordatorioAlimentacion">
        <input type="hidden" name="action" value="eliminar">
        <input type="hidden" id="eliminarIdRecordatorioAlimentacion" name="id">
        <input type="hidden" name="mascotaId" value="${mascota.id}">

        <div class="modal-actions">
          <button type="button" class="btn btn-secondary" onclick="cerrarModalEliminarRecordatorioAlimentacion()">
            <i class="fas fa-times"></i> Cancelar
          </button>
          <button type="submit" class="btn btn-danger">
            <i class="fas fa-trash-alt"></i> Sí, eliminar
          </button>
        </div>
      </form>
    </div>
  </div>
</div>

<!-- Modal Eliminar Recordatorio de Paseo -->
<div id="modalEliminarRecordatorioPaseo" class="modal">
  <div class="modal-content modal-confirm">
    <div class="modal-header danger">
      <i class="fas fa-exclamation-triangle"></i>
      <h2>Confirmar Eliminación</h2>
    </div>
    <div class="modal-body">
      <p><strong>¿Estás seguro de eliminar este recordatorio de paseo?</strong></p>
      <p>Esta acción es irreversible.</p>
      <form id="formEliminarRecordatorioPaseo" method="get" action="${pageContext.request.contextPath}/recordatorioPaseo">
        <input type="hidden" name="action" value="eliminar">
        <input type="hidden" id="eliminarIdRecordatorioPaseo" name="id">
        <input type="hidden" name="mascotaId" value="${mascota.id}">

        <div class="modal-actions">
          <button type="button" class="btn btn-secondary" onclick="cerrarModalEliminarRecordatorioPaseo()">
            <i class="fas fa-times"></i> Cancelar
          </button>
          <button type="submit" class="btn btn-danger">
            <i class="fas fa-trash-alt"></i> Sí, eliminar
          </button>
        </div>
      </form>
    </div>
  </div>
</div>

<script>
  document.addEventListener('DOMContentLoaded', function() {
    // Modal de Alimentación
    var modalAlimentacion = document.getElementById("recordatorioAlimentacionModal");
    var btnAlimentacion = document.getElementById("openRecordatorioAlimentacionModal");
    var spanCloseAlimentacion = document.getElementById("closeAlimentacion");

    // Modal de Paseo
    var modalPaseo = document.getElementById("recordatorioPaseoModal");
    var btnPaseo = document.getElementById("openRecordatorioPaseoModal");
    var spanClosePaseo = document.getElementById("closePaseo");

    // Abrir modal de alimentación
    if (btnAlimentacion) {
      btnAlimentacion.onclick = function(event) {
        event.preventDefault();
        modalAlimentacion.style.display = "block";
        document.getElementById('formRecordatorioAlimentacion').reset();
        document.getElementById('horariosContainer').innerHTML = '';
      }
    }

    // Cerrar modal de alimentación
    if (spanCloseAlimentacion) {
      spanCloseAlimentacion.onclick = function() {
        modalAlimentacion.style.display = "none";
      }
    }

    // Abrir modal de paseo
    if (btnPaseo) {
      btnPaseo.onclick = function(event) {
        event.preventDefault();
        modalPaseo.style.display = "block";
        document.getElementById('formRecordatorioPaseo').reset();
        document.getElementById('horariosPaseoContainer').innerHTML = '';
      }
    }

    // Cerrar modal de paseo
    if (spanClosePaseo) {
      spanClosePaseo.onclick = function() {
        modalPaseo.style.display = "none";
      }
    }

    // Cerrar modales al hacer clic fuera
    window.onclick = function(event) {
      if (event.target == modalAlimentacion) {
        modalAlimentacion.style.display = "none";
      }
      if (event.target == modalPaseo) {
        modalPaseo.style.display = "none";
      }

      var modalEliminarAlimentacion = document.getElementById('modalEliminarRecordatorioAlimentacion');
      if (event.target == modalEliminarAlimentacion) {
        modalEliminarAlimentacion.style.display = "none";
      }

      var modalEliminarPaseo = document.getElementById('modalEliminarRecordatorioPaseo');
      if (event.target == modalEliminarPaseo) {
        modalEliminarPaseo.style.display = "none";
      }
    }

    // Validación formulario de alimentación
    var formAlimentacion = document.getElementById('formRecordatorioAlimentacion');
    if (formAlimentacion) {
      formAlimentacion.addEventListener('submit', function(e) {
        var frecuencia = document.getElementById('frecuencia').value;
        if (!frecuencia) {
          e.preventDefault();
          alert('Debes seleccionar una frecuencia');
          return false;
        }

        var numHorarios = parseInt(frecuencia);
        var horariosCompletos = true;

        for (var i = 1; i <= numHorarios; i++) {
          var horarioInput = document.getElementById('horario' + i);
          if (!horarioInput || !horarioInput.value) {
            e.preventDefault();
            alert('Debes completar todos los horarios de alimentación');
            horariosCompletos = false;
            break;
          }
        }

        var tipoAlimento = document.getElementById('tipoAlimento').value;
        if (!tipoAlimento || tipoAlimento.trim() === '') {
          e.preventDefault();
          alert('Debes especificar el tipo de alimento');
          return false;
        }
      });
    }

    // Validación formulario de paseo
    var formPaseo = document.getElementById('formRecordatorioPaseo');
    if (formPaseo) {
      formPaseo.addEventListener('submit', function(e) {
        var frecuencia = document.getElementById('frecuenciaPaseo').value;
        if (!frecuencia) {
          e.preventDefault();
          alert('Debes seleccionar una frecuencia de paseos');
          return false;
        }

        var numHorarios = parseInt(frecuencia);

        for (var i = 1; i <= numHorarios; i++) {
          var horarioInput = document.getElementById('horarioPaseo' + i);
          if (!horarioInput || !horarioInput.value) {
            e.preventDefault();
            alert('Debes completar todos los horarios de paseo');
            return false;
          }
        }
      });
    }
  });

  // Función para mostrar selectores de hora de alimentación
  function mostrarSelectoresHoraAlimentacion() {
    var frecuencia = document.getElementById("frecuencia").value;
    var horariosContainer = document.getElementById("horariosContainer");

    horariosContainer.innerHTML = '';

    if (frecuencia && frecuencia !== '') {
      var numHorarios = parseInt(frecuencia);

      for (var i = 1; i <= numHorarios; i++) {
        var divGroup = document.createElement('div');
        divGroup.className = 'form-group';

        var label = document.createElement('label');
        label.setAttribute('for', 'horario' + i);
        label.innerHTML = '<i class="fas fa-bell"></i> Hora de Alimentación ' + i + ':';

        var input = document.createElement('input');
        input.type = 'time';
        input.id = 'horario' + i;
        input.name = 'horario' + i;
        input.required = true;
        input.className = 'form-control';

        divGroup.appendChild(label);
        divGroup.appendChild(input);
        horariosContainer.appendChild(divGroup);
      }
    }
  }

  // Función para mostrar selectores de hora de paseo
  function mostrarSelectoresHoraPaseo() {
    var frecuencia = document.getElementById("frecuenciaPaseo").value;
    var horariosContainer = document.getElementById("horariosPaseoContainer");

    horariosContainer.innerHTML = '';

    if (frecuencia && frecuencia !== '') {
      var numHorarios = parseInt(frecuencia);

      for (var i = 1; i <= numHorarios; i++) {
        var divGroup = document.createElement('div');
        divGroup.className = 'form-group';

        var label = document.createElement('label');
        label.setAttribute('for', 'horarioPaseo' + i);
        label.innerHTML = '<i class="fas fa-bell"></i> Hora de Paseo ' + i + ':';

        var input = document.createElement('input');
        input.type = 'time';
        input.id = 'horarioPaseo' + i;
        input.name = 'horarioPaseo' + i;
        input.required = true;
        input.className = 'form-control';

        divGroup.appendChild(label);
        divGroup.appendChild(input);
        horariosContainer.appendChild(divGroup);
      }
    }
  }

  // Función para confirmar eliminación de recordatorio de alimentación
  function confirmarEliminarRecordatorioAlimentacion(id, tipoAlimento) {
    document.getElementById('eliminarIdRecordatorioAlimentacion').value = id;
    document.getElementById('eliminarTipoAlimento').textContent = tipoAlimento;
    document.getElementById('modalEliminarRecordatorioAlimentacion').style.display = 'block';
  }

  // Función para cerrar modal de eliminación de recordatorio de alimentación
  function cerrarModalEliminarRecordatorioAlimentacion() {
    document.getElementById('modalEliminarRecordatorioAlimentacion').style.display = 'none';
  }

  // Función para confirmar eliminación de recordatorio de paseo
  function confirmarEliminarRecordatorioPaseo(id) {
    document.getElementById('eliminarIdRecordatorioPaseo').value = id;
    document.getElementById('modalEliminarRecordatorioPaseo').style.display = 'block';
  }

  // Función para cerrar modal de eliminación de recordatorio de paseo
  function cerrarModalEliminarRecordatorioPaseo() {
    document.getElementById('modalEliminarRecordatorioPaseo').style.display = 'none';
  }
</script>

</body>
</html>