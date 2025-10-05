<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Mis Mascotas - Gestión de Mascotas</title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css">
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
        <div class="section-header">
            <h1><i class="fas fa-paw"></i> Mis Mascotas</h1>
            <a href="${pageContext.request.contextPath}/jsp/mascota/registrarMascota.jsp" class="btn btn-primary">
                <i class="fas fa-plus-circle"></i> Registrar Nueva Mascota
            </a>
        </div>

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

        <div class="mascotas-grid">
            <c:choose>
                <c:when test="${not empty mascotas}">
                    <c:forEach var="mascota" items="${mascotas}">
                        <div class="mascota-card">
                            <i class="mascota-icon fas <c:if test='${mascota.tipo == "PERRO"}'>fa-dog</c:if><c:if test='${mascota.tipo == "GATO"}'>fa-cat</c:if>"></i>
                            <h3 class="mascota-name">${mascota.nombre}</h3>
                            <p class="mascota-type">${mascota.tipo}</p>
                            <div class="mascota-actions">
                                <a href="${pageContext.request.contextPath}/mascota?action=detalles&id=${mascota.id}" class="btn btn-sm btn-info" title="Ver Detalles"><i class="fas fa-eye"></i></a>
                                <a href="${pageContext.request.contextPath}/mascota?action=editar&id=${mascota.id}" class="btn btn-sm btn-primary" title="Editar"><i class="fas fa-edit"></i></a>
                                <a href="${pageContext.request.contextPath}/mascota?action=eliminar&id=${mascota.id}" class="btn btn-sm btn-danger" title="Eliminar" onclick="return confirm('¿Estás seguro de que quieres eliminar a ${mascota.nombre}? Esta acción es irreversible.');"><i class="fas fa-trash-alt"></i></a>
                            </div>
                        </div>
                    </c:forEach>
                </c:when>
                <c:otherwise>
                    <div class="alert alert-info" style="grid-column: 1 / -1;">
                        <i class="fas fa-info-circle"></i>
                        <span>Aún no tienes mascotas registradas. ¡Anímate a añadir la primera!</span>
                    </div>
                </c:otherwise>
            </c:choose>
        </div>
    </div>
</main>

<script src="${pageContext.request.contextPath}/js/dashboard.js"></script>
</body>
</html>