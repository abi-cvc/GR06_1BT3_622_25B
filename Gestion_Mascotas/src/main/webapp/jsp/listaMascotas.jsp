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

<!-- Navbar idéntico al dashboard.jsp -->
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
            <a href="${pageContext.request.contextPath}/mascota?action=registrar" class="btn btn-primary">
                <i class="fas fa-plus-circle"></i> Registrar Mascota
            </a>
        </section>

        <c:if test="${empty mascotas}">
            <div class="alert alert-info">
                <i class="fas fa-info-circle"></i>
                <span>Aún no tienes mascotas registradas. ¡Anímate a añadir una!</span>
            </div>
        </c:if>

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
                        <a href="${pageContext.request.contextPath}/mascota?action=detalles&id=${mascota.id}" class="btn btn-sm btn-info" title="Ver Detalles"><i class="fas fa-eye"></i></a>
                        <a href="${pageContext.request.contextPath}/mascota?action=editar&id=${mascota.id}" class="btn btn-sm btn-secondary" title="Editar"><i class="fas fa-edit"></i></a>
                        <a href="${pageContext.request.contextPath}/mascota?action=eliminar&id=${mascota.id}" class="btn btn-sm btn-danger" title="Eliminar" onclick="return confirm('¿Estás seguro de eliminar a ${mascota.nombre}? Esta acción es irreversible.');"><i class="fas fa-trash-alt"></i></a>
                    </div>
                </div>
            </c:forEach>
        </div>
    </div>
</main>

<!-- Modal de Logout igual al dashboard -->
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
</body>
</html>
