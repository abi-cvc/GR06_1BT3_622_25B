<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Mis Mascotas - Gestión de Mascotas</title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/dashboard.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
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
        <section class="section-header">
            <h1><i class="fas fa-dog"></i> Mis Mascotas</h1>
            <a href="#" onclick="abrirModalRegistrarMascota(); return false;" class="btn btn-primary">
                <i class="fas fa-plus-circle"></i> Registrar Mascota
            </a>
        </section>

        <!-- Mensajes de éxito/error -->
        <c:if test="${param.success == 'registrado'}">
            <div class="alert alert-success">
                <i class="fas fa-check-circle"></i>
                <span>¡Mascota registrada exitosamente!</span>
            </div>
        </c:if>

        <c:if test="${param.success == 'actualizado'}">
            <div class="alert alert-success">
                <i class="fas fa-check-circle"></i>
                <span>¡Mascota actualizada exitosamente!</span>
            </div>
        </c:if>

        <c:if test="${param.success == 'mascota_eliminada'}">
            <div class="alert alert-success">
                <i class="fas fa-check-circle"></i>
                <span>¡Mascota eliminada exitosamente!</span>
            </div>
        </c:if>

        <c:if test="${param.error != null}">
            <div class="alert alert-error">
                <i class="fas fa-exclamation-circle"></i>
                <span>Ha ocurrido un error. Por favor, intenta nuevamente.</span>
            </div>
        </c:if>

        <c:if test="${empty mascotas}">
            <div class="alert alert-info">
                <i class="fas fa-info-circle"></i>
                <span>Aún no tienes mascotas registradas. ¡Anímate a añadir una!</span>
            </div>
        </c:if>

        <!-- Grid de mascotas -->
        <div class="mascotas-grid">
            <c:forEach var="mascota" items="${mascotas}">
                <div class="mascota-card">
                    <div class="mascota-icon">
                        <c:choose>
                            <c:when test="${mascota.tipo == 'PERRO'}">
                                <i class="fas fa-dog"></i>
                            </c:when>
                            <c:when test="${mascota.tipo == 'GATO'}">
                                <i class="fas fa-cat"></i>
                            </c:when>
                            <c:otherwise>
                                <i class="fas fa-paw"></i>
                            </c:otherwise>
                        </c:choose>
                    </div>
                    <h3 class="mascota-name">${mascota.nombre}</h3>
                    <p class="mascota-type">${mascota.tipo}</p>
                    <div class="mascota-actions">
                        <a href="${pageContext.request.contextPath}/mascota?action=detalles&id=${mascota.id}" class="btn btn-sm btn-info" title="Ver Detalles">
                            <i class="fas fa-eye"></i>
                        </a>
                        <button onclick="editarMascota(${mascota.id}, '${mascota.nombre}', '${mascota.tipo}', '${mascota.raza}', ${mascota.edad}, ${mascota.peso}, '${mascota.color}')"
                                class="btn btn-sm btn-secondary" title="Editar">
                            <i class="fas fa-edit"></i>
                        </button>
                        <button onclick="confirmarEliminarMascota(${mascota.id}, '${mascota.nombre}')"
                                class="btn btn-sm btn-danger" title="Eliminar">
                            <i class="fas fa-trash-alt"></i>
                        </button>
                    </div>
                </div>
            </c:forEach>
        </div>
    </div>
</main>

<!-- Modal de Editar Mascota -->
<div id="modalEditarMascota" class="modal">
    <div class="modal-content">
        <div class="modal-header">
            <h2><i class="fas fa-edit"></i> Editar Mascota</h2>
            <button class="modal-close" onclick="cerrarModalEditar()">&times;</button>
        </div>
        <form action="${pageContext.request.contextPath}/mascota" method="post" id="formEditarMascota">
            <input type="hidden" name="action" value="actualizar">
            <input type="hidden" id="editarId" name="id">

            <div class="form-group">
                <label for="editarNombre"><i class="fas fa-tag"></i> Nombre</label>
                <input type="text" id="editarNombre" name="nombre" required maxlength="50">
            </div>

            <div class="form-group">
                <label for="editarTipo"><i class="fas fa-cat"></i> Tipo de Mascota</label>
                <select id="editarTipo" name="tipo" required class="form-control">
                    <option value="" disabled>Seleccione un tipo</option>
                    <c:forEach var="tipo" items="${tiposMascota}">
                        <option value="${tipo}">${tipo}</option>
                    </c:forEach>
                </select>
            </div>

            <div class="form-group">
                <label for="editarRaza"><i class="fas fa-dog"></i> Raza</label>
                <input type="text" id="editarRaza" name="raza" required maxlength="50">
            </div>

            <div class="form-row">
                <div class="form-group">
                    <label for="editarEdad"><i class="fas fa-birthday-cake"></i> Edad (años)</label>
                    <input type="number" id="editarEdad" name="edad" min="0" required>
                </div>
                <div class="form-group">
                    <label for="editarPeso"><i class="fas fa-weight"></i> Peso (kg)</label>
                    <input type="number" id="editarPeso" name="peso" step="0.1" min="0" required>
                </div>
            </div>

            <div class="form-group">
                <label for="editarColor"><i class="fas fa-palette"></i> Color</label>
                <input type="text" id="editarColor" name="color" maxlength="30">
            </div>

            <div class="modal-actions">
                <button type="button" class="btn btn-secondary" onclick="cerrarModalEditar()">
                    <i class="fas fa-times"></i> Cancelar
                </button>
                <button type="submit" class="btn btn-primary">
                    <i class="fas fa-save"></i> Guardar Cambios
                </button>
            </div>
        </form>
    </div>
</div>

<!-- Modal de Confirmar Eliminación -->
<div id="modalEliminarMascota" class="modal">
    <div class="modal-content modal-confirm">
        <div class="modal-header danger">
            <i class="fas fa-exclamation-triangle"></i>
            <h2>Confirmar Eliminación</h2>
        </div>
        <div class="modal-body">
            <p><strong>¿Estás seguro de eliminar a <span id="eliminarNombre"></span>?</strong></p>
            <p>Esta acción es irreversible y se eliminarán:</p>
            <ul>
                <li>Todos los datos de la mascota</li>
                <li>Historial de visitas veterinarias</li>
                <li>Registro de vacunas</li>
                <li>Recordatorios configurados</li>
            </ul>
            <form id="formEliminarMascota" method="get" action="${pageContext.request.contextPath}/mascota">
                <input type="hidden" name="action" value="eliminar">
                <input type="hidden" id="eliminarId" name="id">

                <div class="modal-actions">
                    <button type="button" class="btn btn-secondary" onclick="cerrarModalEliminar()">
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

<!-- Modal de Registrar Mascota -->
<div id="modalRegistrarMascota" class="modal">
    <div class="modal-content">
        <div class="modal-header">
            <h2><i class="fas fa-paw"></i> Registrar Nueva Mascota</h2>
            <button class="modal-close" onclick="cerrarModalRegistrarMascota()">&times;</button>
        </div>
        <form action="${pageContext.request.contextPath}/mascota" method="post" id="formRegistrarMascota">
            <input type="hidden" name="action" value="registrar">
            <input type="hidden" id="usuarioId" name="usuarioId" value="${sessionScope.usuario.id}" readonly required>

            <div class="form-group">
                <label for="nombreMascota"><i class="fas fa-tag"></i> Nombre</label>
                <input type="text" id="nombreMascota" name="nombre" required maxlength="50">
            </div>

            <div class="form-group">
                <label for="tipoMascota"><i class="fas fa-cat"></i> Tipo de Mascota</label>
                <select id="tipoMascota" name="tipo" required class="form-control">
                    <option value="" disabled selected>Seleccione un tipo</option>
                    <c:forEach var="tipo" items="${tiposMascota}">
                        <option value="${tipo}">${tipo}</option>
                    </c:forEach>
                </select>
            </div>

            <div class="form-group">
                <label for="razaMascota"><i class="fas fa-dog"></i> Raza</label>
                <input type="text" id="razaMascota" name="raza" required maxlength="50">
            </div>

            <div class="form-row">
                <div class="form-group">
                    <label for="edadMascota"><i class="fas fa-birthday-cake"></i> Edad (años)</label>
                    <input type="number" id="edadMascota" name="edad" min="0" required>
                </div>
                <div class="form-group">
                    <label for="pesoMascota"><i class="fas fa-weight"></i> Peso (kg)</label>
                    <input type="number" id="pesoMascota" name="peso" step="0.1" min="0" required>
                </div>
            </div>

            <div class="form-group">
                <label for="colorMascota"><i class="fas fa-palette"></i> Color</label>
                <input type="text" id="colorMascota" name="color" maxlength="30">
            </div>

            <div class="modal-actions">
                <button type="button" class="btn btn-secondary" onclick="cerrarModalRegistrarMascota()">
                    <i class="fas fa-times"></i> Cancelar
                </button>
                <button type="submit" class="btn btn-primary">
                    <i class="fas fa-plus-circle"></i> Registrar Mascota
                </button>
            </div>
        </form>
    </div>
</div>

<!-- Modal de Logout -->
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

<script src="${pageContext.request.contextPath}/js/dashboard.js"></script>
<script src="${pageContext.request.contextPath}/js/mascotas.js"></script>
</body>
</html>