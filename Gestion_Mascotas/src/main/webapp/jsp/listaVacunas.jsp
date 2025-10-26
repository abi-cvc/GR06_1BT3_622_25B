<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %> <%-- Necesario para formatear fechas --%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Vacunas - Gestión de Mascotas</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/dashboard.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <style>
        /* Estilo opcional para que las celdas vacías muestren un guión */
        td.optional:empty::before {
            content: "-";
            color: var(--text-light);
        }
    </style>
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
            <li><a href="${pageContext.request.contextPath}/vacuna" class="active"><i class="fas fa-syringe"></i> Vacunas</a></li>
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
        <section class="section-header">
            <h1><i class="fas fa-syringe"></i> Registro de Vacunas</h1>
            <button class="btn btn-primary" onclick="abrirModalRegistrarVacuna()">
                <i class="fas fa-plus-circle"></i> Registrar Vacuna
            </button>
        </section>

        <c:if test="${param.success == 'registrado'}">
            <div class="alert alert-success">
                <i class="fas fa-check-circle"></i>
                <span>¡Vacuna registrada exitosamente!</span>
            </div>
        </c:if>
        <c:if test="${param.success == 'eliminado'}">
            <div class="alert alert-success">
                <i class="fas fa-check-circle"></i>
                <span>¡Vacuna eliminada exitosamente!</span>
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
            <c:when test="${empty vacunas}">
                <div class="alert alert-info">
                    <i class="fas fa-info-circle"></i>
                    <span>No hay vacunas registradas aún. ¡Comienza añadiendo una!</span>
                </div>
            </c:when>
            <c:otherwise>
                <div style="background: white; border-radius: 12px; padding: 1.5rem; box-shadow: 0 4px 6px rgba(0,0,0,0.1); overflow-x: auto;">
                    <table style="width: 100%; border-collapse: collapse;">
                        <thead>
                        <tr style="background: linear-gradient(135deg, var(--primary-color), var(--primary-dark)); color: white;">
                            <th style="padding: 1rem; text-align: left; border-radius: 8px 0 0 0;">
                                <i class="fas fa-tag"></i> Nombre Vacuna
                            </th>
                            <th style="padding: 1rem; text-align: left;">
                                <i class="fas fa-paw"></i> Mascota
                            </th>
                            <th style="padding: 1rem; text-align: left;">
                                <i class="fas fa-calendar-alt"></i> Fecha Aplicación
                            </th>
                                <%-- Nueva Columna Veterinario --%>
                            <th style="padding: 1rem; text-align: left;">
                                <i class="fas fa-user-md"></i> Veterinario
                            </th>
                                <%-- Nueva Columna Próxima Dosis --%>
                            <th style="padding: 1rem; text-align: left;">
                                <i class="fas fa-calendar-check"></i> Próxima Dosis
                            </th>
                            <th style="padding: 1rem; text-align: center;">
                                <i class="fas fa-info-circle"></i> Estado
                            </th>
                            <th style="padding: 1rem; text-align: center; border-radius: 0 8px 0 0;">
                                <i class="fas fa-cog"></i> Acciones
                            </th>
                        </tr>
                        </thead>
                        <tbody>
                        <%@ page import="java.time.LocalDate" %>
                            <%-- <fmt:setLocale value="es_EC"/> --%> <%-- No longer needed for fmt:formatDate --%>
                        <c:forEach var="vacuna" items="${vacunas}">
                            <%
                                LocalDate fechaHoy = LocalDate.now();
                                request.setAttribute("fechaHoy", fechaHoy);
                            %>
                            <tr style="border-bottom: 1px solid #e5e7eb;">
                                <td style="padding: 1rem;">
                                    <strong style="color: var(--primary-color);">${vacuna.nombre}</strong>
                                </td>
                                <td style="padding: 1rem;">
                                    <i class="fas fa-<c:choose><c:when test='${vacuna.mascota.tipo == "PERRO"}'>dog</c:when><c:when test='${vacuna.mascota.tipo == "GATO"}'>cat</c:when><c:otherwise>paw</c:otherwise></c:choose>" style="color: var(--text-secondary); margin-right: 0.5rem;"></i>
                                        ${vacuna.mascota.nombre}
                                </td>
                                <td style="padding: 1rem;">
                                        <%-- CORRECTION HERE --%>
                                    <c:out value="${vacuna.fecha}"/>
                                </td>
                                    <%-- Nueva Celda Veterinario --%>
                                <td style="padding: 1rem;" class="optional">
                                    <c:out value="${vacuna.nombreVeterinario}"/>
                                </td>
                                    <%-- Nueva Celda Próxima Dosis --%>
                                <td style="padding: 1rem;" class="optional">
                                    <c:if test="${not empty vacuna.proximaDosis}">
                                        <%-- CORRECTION HERE --%>
                                        <c:out value="${vacuna.proximaDosis}"/>
                                    </c:if>
                                </td>
                                <td style="padding: 1rem; text-align: center;">
                                    <c:set var="fechaVacunaJSTL" value="${vacuna.fecha}"/>
                                    <c:choose>
                                        <c:when test="${fechaVacunaJSTL.isAfter(fechaHoy)}">
                                            <span class="badge badge-warning">
                                                <i class="fas fa-clock"></i> Próxima
                                            </span>
                                        </c:when>
                                        <c:otherwise>
                                            <span class="badge badge-success">
                                                <i class="fas fa-check-circle"></i> Aplicada
                                            </span>
                                        </c:otherwise>
                                    </c:choose>
                                </td>
                                <td style="padding: 1rem; text-align: center;">
                                    <button onclick="confirmarEliminarVacuna(${vacuna.id}, '${vacuna.nombre}')"
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

<div id="modalRegistrarVacuna" class="modal">
    <div class="modal-content">
        <div class="modal-header">
            <h2><i class="fas fa-syringe"></i> Registrar Nueva Vacuna</h2>
            <button class="modal-close" onclick="cerrarModalRegistrarVacuna()">&times;</button>
        </div>
        <form action="${pageContext.request.contextPath}/vacuna" method="post" id="formRegistrarVacuna">
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
                <small class="help-text">Selecciona la mascota que recibirá la vacuna</small>
            </div>

            <div class="form-group">
                <label for="nombreVacuna">
                    <i class="fas fa-tag"></i> Tipo de Vacuna <span class="required">*</span>
                </label>
                <select id="nombreVacuna" name="nombre" required class="form-control">
                    <option value="" disabled selected>Seleccione el tipo de vacuna</option>
                    <optgroup label="Vacunas Comunes">
                        <option value="Rabia">Rabia</option>
                        <option value="Parvovirus">Parvovirus</option>
                        <option value="Moquillo">Moquillo</option>
                        <option value="Hepatitis">Hepatitis</option>
                        <option value="Leptospirosis">Leptospirosis</option>
                        <option value="Triple Felina">Triple Felina</option>
                        <option value="Leucemia Felina">Leucemia Felina</option>
                    </optgroup>
                    <optgroup label="Otras">
                        <option value="Tos de las Perreras">Tos de las Perreras</option>
                        <option value="Séxtuple">Séxtuple</option>
                        <option value="Óctuple">Óctuple</option>
                        <option value="Panleucopenia">Panleucopenia</option>
                        <option value="Rinotraqueítis">Rinotraqueítis</option>
                        <option value="Otra">Otra</option>
                    </optgroup>
                </select>
                <small class="help-text">Selecciona el tipo de vacuna aplicada</small>
            </div>

            <div class="form-group">
                <label for="fechaVacuna">
                    <i class="fas fa-calendar"></i> Fecha de Aplicación/Programación <span class="required">*</span>
                </label>
                <input type="date"
                       id="fechaVacuna"
                       name="fecha"
                       required>
                <small class="help-text">Fecha en que se aplicó o se aplicará la vacuna</small>
            </div>

            <%-- Nuevo Campo: Nombre Veterinario --%>
            <div class="form-group">
                <label for="nombreVeterinarioVacuna">
                    <i class="fas fa-user-md"></i> Nombre del Veterinario (Opcional)
                </label>
                <input type="text"
                       id="nombreVeterinarioVacuna"
                       name="nombreVeterinario" <%-- Coincide con el parámetro del servlet --%>
                       placeholder="Nombre del profesional"
                       maxlength="100"
                       class="form-control">
                <small class="help-text">Quién aplicó la vacuna</small>
            </div>

            <%-- Nuevo Campo: Próxima Dosis --%>
            <div class="form-group">
                <label for="proximaDosisVacuna">
                    <i class="fas fa-calendar-check"></i> Fecha Próxima Dosis (Opcional)
                </label>
                <input type="date"
                       id="proximaDosisVacuna"
                       name="proximaDosis" <%-- Coincide con el parámetro del servlet --%>
                       min="<%= java.time.LocalDate.now().plusDays(1) %>"> <%-- Solo fechas futuras --%>
                <small class="help-text">Si aplica, fecha de la siguiente dosis</small>
            </div>

            <div class="modal-actions">
                <button type="button" class="btn btn-secondary" onclick="cerrarModalRegistrarVacuna()">
                    <i class="fas fa-times"></i> Cancelar
                </button>
                <button type="submit" class="btn btn-primary">
                    <i class="fas fa-plus-circle"></i> Registrar Vacuna
                </button>
            </div>
        </form>
    </div>
</div>

<div id="modalEliminarVacuna" class="modal">
    <div class="modal-content modal-confirm">
        <div class="modal-header danger">
            <i class="fas fa-exclamation-triangle"></i>
            <h2>Confirmar Eliminación</h2>
        </div>
        <div class="modal-body">
            <p><strong>¿Estás seguro de eliminar el registro de la vacuna "<span id="eliminarNombreVacuna"></span>"?</strong></p>
            <p>Esta acción es irreversible.</p>
            <form id="formEliminarVacuna" method="get" action="${pageContext.request.contextPath}/vacuna">
                <input type="hidden" name="action" value="eliminar">
                <input type="hidden" id="eliminarIdVacuna" name="id">
                <div class="modal-actions">
                    <button type="button" class="btn btn-secondary" onclick="cerrarModalEliminarVacuna()">
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

<script src="${pageContext.request.contextPath}/js/vacunas.js"></script>
<script src="${pageContext.request.contextPath}/js/dashboard.js"></script>
</body>
</html>