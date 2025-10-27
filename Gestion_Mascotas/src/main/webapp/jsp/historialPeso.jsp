<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Historial de Peso - ${mascota.nombre}</title>
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
            <li><a href="${pageContext.request.contextPath}/mascota?action=listar" class="active"><i class="fas fa-dog"></i> Mis Mascotas</a></li>
            <li><a href="${pageContext.request.contextPath}/vacuna"><i class="fas fa-syringe"></i> Vacunas</a></li>
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
        <a href="${pageContext.request.contextPath}/mascota?action=detalles&id=${mascota.id}" class="btn btn-secondary" style="margin-bottom: 1.5rem;">
            <i class="fas fa-arrow-left"></i> Volver a Detalles
        </a>

        <section class="section-header">
            <h1>
                <i class="fas fa-weight"></i> Historial de Peso de ${mascota.nombre}
            </h1>
            <button class="btn btn-primary" onclick="abrirModalRegistrarPeso()">
                <i class="fas fa-plus-circle"></i> Registrar Peso
            </button>
        </section>

        <!-- Mensajes de éxito/error -->
        <c:if test="${param.success == 'registrado'}">
            <div class="alert alert-success">
                <i class="fas fa-check-circle"></i>
                <span>¡Nuevo registro de peso guardado exitosamente!</span>
            </div>
        </c:if>
        <c:if test="${param.error == 'mascota_no_encontrada'}">
            <div class="alert alert-error">
                <i class="fas fa-exclamation-circle"></i>
                <span>Mascota no encontrada.</span>
            </div>
        </c:if>
        <c:if test="${param.error == 'acceso_denegado'}">
            <div class="alert alert-error">
                <i class="fas fa-exclamation-circle"></i>
                <span>No tienes permiso para acceder a esta mascota.</span>
            </div>
        </c:if>
        <c:if test="${param.error == 'formato_invalido'}">
            <div class="alert alert-error">
                <i class="fas fa-exclamation-circle"></i>
                <span>Formato de datos inválido. Por favor verifica los valores ingresados.</span>
            </div>
        </c:if>
        <c:if test="${param.error == 'error_registro'}">
            <div class="alert alert-error">
                <i class="fas fa-exclamation-circle"></i>
                <span>Error al registrar el peso. Intenta nuevamente.</span>
            </div>
        </c:if>
        <c:if test="${not empty param.error && param.error != 'mascota_no_encontrada' && param.error != 'acceso_denegado' && param.error != 'formato_invalido' && param.error != 'error_registro'}">
            <div class="alert alert-error">
                <i class="fas fa-exclamation-circle"></i>
                <span>${param.error}</span>
            </div>
        </c:if>

        <!-- Peso actual de la mascota -->
        <div style="background: linear-gradient(135deg, var(--primary-color), var(--primary-dark)); border-radius: 12px; padding: 1.5rem; margin-bottom: 2rem; color: white; text-align: center;">
            <i class="fas fa-weight-hanging" style="font-size: 2.5rem; margin-bottom: 0.5rem;"></i>
            <h2 style="margin: 0; font-size: 1.5rem;">Peso Actual</h2>
            <p style="font-size: 2.5rem; font-weight: 700; margin: 0.5rem 0 0 0;">${mascota.peso} kg</p>
        </div>

        <!-- Lista de historial de peso -->
        <c:choose>
            <c:when test="${empty historialPeso}">
                <div class="alert alert-info">
                    <i class="fas fa-info-circle"></i>
                    <span>No hay registros de peso aún. ¡Comienza añadiendo el primer registro!</span>
                </div>
            </c:when>
            <c:otherwise>
                <div style="background: white; border-radius: 12px; padding: 1.5rem; box-shadow: 0 4px 6px rgba(0,0,0,0.1); overflow-x: auto;">
                    <table style="width: 100%; border-collapse: collapse;">
                        <thead>
                        <tr style="background: linear-gradient(135deg, var(--primary-color), var(--primary-dark)); color: white;">
                            <th style="padding: 1rem; text-align: left; border-radius: 8px 0 0 0;">
                                <i class="fas fa-calendar-alt"></i> Fecha
                            </th>
                            <th style="padding: 1rem; text-align: center;">
                                <i class="fas fa-weight"></i> Peso (kg)
                            </th>
                            <th style="padding: 1rem; text-align: left; border-radius: 0 8px 0 0;">
                                <i class="fas fa-comment"></i> Comentarios
                            </th>
                        </tr>
                        </thead>
                        <tbody>
                        <%@ page import="java.time.format.DateTimeFormatter" %>
                        <%@ page import="com.gestion.mascotas.modelo.entidades.HistorialPeso" %>
                        <c:forEach var="registro" items="${historialPeso}">
                            <c:set var="fechaFormateada" value="${registro.fechaRegistro.format(DateTimeFormatter.ofPattern('dd/MM/yyyy HH:mm'))}" />
                            <tr style="border-bottom: 1px solid #e5e7eb;">
                                <td style="padding: 1rem;">
                                    <i class="fas fa-clock" style="color: var(--text-secondary); margin-right: 0.5rem;"></i>
                                    <c:out value="${registro.fechaRegistro.toString().substring(0,10)} ${registro.fechaRegistro.toString().substring(11,16)}" />
                                </td>
                                <td style="padding: 1rem; text-align: center;">
                                    <strong style="color: var(--primary-color); font-size: 1.25rem;">${registro.peso} kg</strong>
                                </td>
                                <td style="padding: 1rem; color: var(--text-secondary);">
                                    <c:choose>
                                        <c:when test="${not empty registro.comentarios}">
                                            ${registro.comentarios}
                                        </c:when>
                                        <c:otherwise>
                                            <span style="font-style: italic;">Sin comentarios</span>
                                        </c:otherwise>
                                    </c:choose>
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

<!-- Modal para Registrar Peso -->
<div id="modalRegistrarPeso" class="modal">
    <div class="modal-content" style="margin: 5% auto; max-width: 500px;">
        <div class="modal-header">
            <h2><i class="fas fa-weight"></i> Registrar Peso de ${mascota.nombre}</h2>
            <button class="modal-close" onclick="cerrarModalRegistrarPeso()">&times;</button>
        </div>
        <form action="${pageContext.request.contextPath}/historialPeso" method="post" id="formRegistrarPeso">
            <input type="hidden" name="action" value="registrar">
            <input type="hidden" name="mascotaId" value="${mascota.id}">

            <div class="form-group">
                <label for="peso">
                    <i class="fas fa-weight"></i> Peso (kg) <span class="required">*</span>
                </label>
                <input type="number"
                       id="peso"
                       name="peso"
                       step="0.01"
                       min="0.01"
                       max="250"
                       placeholder="Ej: 15.5"
                       required
                       class="form-control">
                <small class="help-text">Ingresa el peso en kilogramos (0.01 - 250 kg)</small>
            </div>

            <div class="form-group">
                <label for="fechaRegistro">
                    <i class="fas fa-calendar"></i> Fecha del Registro <span class="required">*</span>
                </label>
                <input type="date"
                       id="fechaRegistro"
                       name="fechaRegistro"
                       max="<%= java.time.LocalDate.now() %>"
                       required
                       class="form-control">
                <small class="help-text">Fecha en que se registró el peso</small>
            </div>

            <div class="form-group">
                <label for="comentarios">
                    <i class="fas fa-comment"></i> Comentarios (Opcional)
                </label>
                <textarea id="comentarios"
                          name="comentarios"
                          rows="3"
                          maxlength="255"
                          placeholder="Ej: Pesaje post-vacunación, cambio de dieta, etc."
                          class="form-control"
                          style="resize: vertical;"></textarea>
                <small class="help-text">Notas adicionales sobre este registro</small>
            </div>

            <div class="modal-actions">
                <button type="button" class="btn btn-secondary" onclick="cerrarModalRegistrarPeso()">
                    <i class="fas fa-times"></i> Cancelar
                </button>
                <button type="submit" class="btn btn-primary">
                    <i class="fas fa-save"></i> Guardar Registro
                </button>
            </div>
        </form>
    </div>
</div>

<script>
    function abrirModalRegistrarPeso() {
        // Establecer fecha actual por defecto
        document.getElementById('fechaRegistro').valueAsDate = new Date();
        document.getElementById('modalRegistrarPeso').style.display = 'block';
    }

    function cerrarModalRegistrarPeso() {
        document.getElementById('modalRegistrarPeso').style.display = 'none';
        document.getElementById('formRegistrarPeso').reset();
    }

    // Cerrar modal al hacer clic fuera
    window.onclick = function(event) {
        var modal = document.getElementById('modalRegistrarPeso');
        if (event.target == modal) {
            cerrarModalRegistrarPeso();
        }
    }

    // Validación del formulario
    document.getElementById('formRegistrarPeso').addEventListener('submit', function(e) {
        var peso = parseFloat(document.getElementById('peso').value);

        if (peso <= 0) {
            e.preventDefault();
            alert('El peso debe ser un valor positivo mayor que cero.');
            return false;
        }

        if (peso > 250) {
            e.preventDefault();
            alert('El peso no puede exceder los 250 kg.');
            return false;
        }
    });

    // Función para confirmar logout (solo si se necesita en esta página)
    function confirmarLogout() {
        if (confirm('¿Estás seguro que deseas cerrar sesión?')) {
            window.location.href = '${pageContext.request.contextPath}/login?action=logout';
        }
    }
</script>
</body>
</html>