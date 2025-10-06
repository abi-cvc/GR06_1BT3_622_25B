<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Visitas Veterinarias - Gestión de Mascotas</title>
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
        <div class="navbar-user">
            <button class="btn-logout-mobile" onclick="confirmarLogout()">
                <i class="fas fa-sign-out-alt"></i>
            </button>
        </div>
    </div>
</nav>

<main class="main-content">
    <div class="container">
        <section class="section-header">
            <h1><i class="fas fa-stethoscope"></i> Visitas Veterinarias</h1>
            <button class="btn btn-primary" onclick="abrirModalRegistrarVisita()">
                <i class="fas fa-plus-circle"></i> Registrar Visita
            </button>
        </section>

        <c:if test="${param.success == 'registrado'}">
            <div class="alert alert-success">
                <i class="fas fa-check-circle"></i>
                <span>¡Visita registrada exitosamente!</span>
            </div>
        </c:if>

        <c:if test="${param.success == 'eliminado'}">
            <div class="alert alert-success">
                <i class="fas fa-check-circle"></i>
                <span>¡Visita eliminada exitosamente!</span>
            </div>
        </c:if>

        <c:if test="${param.error == 'mascota_no_encontrada'}">
            <div class="alert alert-error">
                <i class="fas fa-exclamation-circle"></i>
                <span>Mascota no encontrada. Por favor selecciona una mascota válida.</span>
            </div>
        </c:if>

        <c:if test="${not empty error}">
            <div class="alert alert-error">
                <i class="fas fa-exclamation-circle"></i>
                <span>${error}</span>
            </div>
        </c:if>

        <c:choose>
            <c:when test="${empty visitas}">
                <div class="alert alert-info">
                    <i class="fas fa-info-circle"></i>
                    <span>No hay visitas veterinarias registradas aún. ¡Comienza añadiendo una!</span>
                </div>
            </c:when>
            <c:otherwise>
                <div style="background: white; border-radius: 12px; padding: 1.5rem; box-shadow: 0 4px 6px rgba(0,0,0,0.1); overflow-x: auto;">
                    <table style="width: 100%; border-collapse: collapse;">
                        <thead>
                        <tr style="background: linear-gradient(135deg, var(--primary-color), var(--primary-dark)); color: white;">
                            <th style="padding: 1rem; text-align: left; border-radius: 8px 0 0 0;">
                                <i class="fas fa-calendar"></i> Fecha
                            </th>
                            <th style="padding: 1rem; text-align: left;">
                                <i class="fas fa-paw"></i> Mascota
                            </th>
                            <th style="padding: 1rem; text-align: left;">
                                <i class="fas fa-notes-medical"></i> Motivo
                            </th>
                            <th style="padding: 1rem; text-align: center; border-radius: 0 8px 0 0;">
                                <i class="fas fa-cog"></i> Acciones
                            </th>
                        </tr>
                        </thead>
                        <tbody>
                        <c:forEach var="visita" items="${visitas}">
                            <tr style="border-bottom: 1px solid #e5e7eb;">
                                <td style="padding: 1rem;">
                                    <strong style="color: var(--primary-color);">${visita.fecha}</strong>
                                </td>
                                <td style="padding: 1rem;">
                                    <i class="fas fa-dog" style="color: var(--text-secondary); margin-right: 0.5rem;"></i>
                                        ${visita.mascota.nombre}
                                </td>
                                <td style="padding: 1rem;">
                                        ${visita.motivo}
                                </td>
                                <td style="padding: 1rem; text-align: center;">
                                    <button onclick="confirmarEliminarVisita(${visita.id}, '${visita.mascota.nombre}', '${visita.fecha}')"
                                            class="btn btn-sm btn-danger"
                                            title="Eliminar">
                                        <i class="fas fa-trash-alt"></i>
                                    </button>
                                </td>
                            </tr>
                        </c:forEach>
                        </tbody>
                    </table>
                </div>
            </c:otherwise>
        </c:choose>
    </div>
</main>

<!-- Modal Registrar Visita -->
<div id="modalRegistrarVisita" class="modal">
    <div class="modal-content">
        <div class="modal-header">
            <h2><i class="fas fa-stethoscope"></i> Registrar Nueva Visita Veterinaria</h2>
            <button class="modal-close" onclick="cerrarModalRegistrarVisita()">&times;</button>
        </div>
        <form action="${pageContext.request.contextPath}/visita" method="post" id="formRegistrarVisita">
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
                <input type="date"
                       id="fecha"
                       name="fecha"
                       required
                       max="<%= java.time.LocalDate.now() %>">
                <small class="help-text">Fecha en que se realizó la visita veterinaria</small>
            </div>

            <div class="form-group">
                <label for="motivo">
                    <i class="fas fa-notes-medical"></i> Motivo de la Visita <span class="required">*</span>
                </label>
                <textarea id="motivo"
                          name="motivo"
                          rows="4"
                          required
                          placeholder="Ej: Control de rutina, vacunación, consulta por síntomas..."></textarea>
                <small class="help-text">Describe el motivo de la visita veterinaria</small>
            </div>

            <div class="modal-actions">
                <button type="button" class="btn btn-secondary" onclick="cerrarModalRegistrarVisita()">
                    <i class="fas fa-times"></i> Cancelar
                </button>
                <button type="submit" class="btn btn-primary">
                    <i class="fas fa-plus-circle"></i> Registrar Visita
                </button>
            </div>
        </form>
    </div>
</div>

<!-- Modal Eliminar Visita -->
<div id="modalEliminarVisita" class="modal">
    <div class="modal-content modal-confirm">
        <div class="modal-header danger">
            <i class="fas fa-exclamation-triangle"></i>
            <h2>Confirmar Eliminación</h2>
        </div>
        <div class="modal-body">
            <p><strong>¿Estás seguro de eliminar el registro de esta visita veterinaria?</strong></p>
            <p><strong>Mascota:</strong> <span id="eliminarMascotaVisita"></span></p>
            <p><strong>Fecha:</strong> <span id="eliminarFechaVisita"></span></p>
            <p style="color: #ef4444; margin-top: 1rem;">Esta acción es irreversible.</p>
            <form id="formEliminarVisita" method="get" action="${pageContext.request.contextPath}/visita">
                <input type="hidden" name="action" value="eliminar">
                <input type="hidden" id="eliminarIdVisita" name="id">

                <div class="modal-actions">
                    <button type="button" class="btn btn-secondary" onclick="cerrarModalEliminarVisita()">
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

<!-- Modal Logout -->
<div id="modalLogout" class="modal">
    <div class="modal-content modal-confirm">
        <div class="modal-header">
            <i class="fas fa-sign-out-alt"></i>
            <h2>Cerrar Sesión</h2>
        </div>
        <div class="modal-body">
            <p>¿Estás seguro que deseas cerrar sesión?</p>
            <div class="modal-actions">
                <button type="button" class="btn btn-secondary" onclick="cerrarModalLogout()">
                    <i class="fas fa-times"></i> Cancelar
                </button>
                <form action="${pageContext.request.contextPath}/login" method="post" style="display: inline;">
                    <input type="hidden" name="action" value="logout">
                    <button type="submit" class="btn btn-primary">
                        <i class="fas fa-check"></i> Sí, cerrar sesión
                    </button>
                </form>
            </div>
        </div>
    </div>
</div>

<script src="${pageContext.request.contextPath}/js/visitas.js"></script>
<script src="${pageContext.request.contextPath}/js/dashboard.js"></script>
</body>
</html>