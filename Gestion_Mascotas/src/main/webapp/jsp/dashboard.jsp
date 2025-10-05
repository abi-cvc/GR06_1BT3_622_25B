<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!-- Verificar que el usuario esté logueado -->
<%
    if (session.getAttribute("usuario") == null) {
        response.sendRedirect(request.getContextPath() + "/login.jsp");
        return;
    }
%>

<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Dashboard - Gestión de Mascotas</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/dashboard.css">
</head>
<body>
<header class="main-header">
    <div class="header-content">
        <div class="logo">
            <h2>🐾 Gestión de Mascotas</h2>
        </div>
        <nav class="main-nav">
            <ul>
                <li><a href="${pageContext.request.contextPath}/dashboard" class="active">Inicio</a></li>
                <li><a href="${pageContext.request.contextPath}/mascotas">Mis Mascotas</a></li>
                <li><a href="${pageContext.request.contextPath}/visitas">Visitas</a></li>
                <li><a href="${pageContext.request.contextPath}/vacunas">Vacunas</a></li>
            </ul>
        </nav>
        <div class="user-menu">
            <span class="user-name">👤 ${sessionScope.usuario.nombreCompleto}</span>
            <a href="${pageContext.request.contextPath}/perfil" class="btn-secondary">Mi Perfil</a>
            <a href="${pageContext.request.contextPath}/logout" class="btn-logout">Cerrar Sesión</a>
        </div>
    </div>
</header>

<main class="main-content">
    <div class="container">
        <section class="welcome-section">
            <h1>¡Bienvenido, ${sessionScope.usuario.nombreCompleto}! 👋</h1>
            <p>Gestiona toda la información de tus mascotas en un solo lugar</p>
        </section>

        <section class="stats-grid">
            <div class="stat-card">
                <div class="stat-icon">🐕</div>
                <div class="stat-info">
                    <h3><c:out value="${totalMascotas != null ? totalMascotas : 0}"/></h3>
                    <p>Mascotas Registradas</p>
                </div>
                <a href="${pageContext.request.contextPath}/mascotas" class="stat-link">Ver todas →</a>
            </div>

            <div class="stat-card">
                <div class="stat-icon">🏥</div>
                <div class="stat-info">
                    <h3><c:out value="${totalVisitas != null ? totalVisitas : 0}"/></h3>
                    <p>Visitas Veterinarias</p>
                </div>
                <a href="${pageContext.request.contextPath}/visitas" class="stat-link">Ver historial →</a>
            </div>

            <div class="stat-card">
                <div class="stat-icon">💉</div>
                <div class="stat-info">
                    <h3><c:out value="${totalVacunas != null ? totalVacunas : 0}"/></h3>
                    <p>Vacunas Aplicadas</p>
                </div>
                <a href="${pageContext.request.contextPath}/vacunas" class="stat-link">Ver registro →</a>
            </div>

            <div class="stat-card">
                <div class="stat-icon">📅</div>
                <div class="stat-info">
                    <h3><c:out value="${proximasVacunas != null ? proximasVacunas : 0}"/></h3>
                    <p>Vacunas Próximas</p>
                </div>
                <a href="${pageContext.request.contextPath}/vacunas?proximas=true" class="stat-link">Ver pendientes →</a>
            </div>
        </section>

        <section class="quick-actions">
            <h2>Acciones Rápidas</h2>
            <div class="actions-grid">
                <a href="${pageContext.request.contextPath}/mascotas?action=nueva" class="action-card">
                    <div class="action-icon">➕</div>
                    <h3>Registrar Mascota</h3>
                    <p>Añade una nueva mascota a tu perfil</p>
                </a>

                <a href="${pageContext.request.contextPath}/visitas?action=nueva" class="action-card">
                    <div class="action-icon">📝</div>
                    <h3>Registrar Visita</h3>
                    <p>Documenta una visita veterinaria</p>
                </a>

                <a href="${pageContext.request.contextPath}/vacunas?action=nueva" class="action-card">
                    <div class="action-icon">💊</div>
                    <h3>Registrar Vacuna</h3>
                    <p>Agrega una nueva vacuna aplicada</p>
                </a>

                <a href="${pageContext.request.contextPath}/perfil" class="action-card">
                    <div class="action-icon">⚙️</div>
                    <h3>Editar Perfil</h3>
                    <p>Actualiza tu información personal</p>
                </a>
            </div>
        </section>

        <section class="recent-activity">
            <h2>Actividad Reciente</h2>
            <div class="activity-list">
                <c:choose>
                    <c:when test="${not empty actividadReciente}">
                        <c:forEach items="${actividadReciente}" var="actividad">
                            <div class="activity-item">
                                <span class="activity-icon"><c:out value="${actividad.icono}"/></span>
                                <div class="activity-details">
                                    <p class="activity-text"><c:out value="${actividad.descripcion}"/></p>
                                    <span class="activity-date"><c:out value="${actividad.fecha}"/></span>
                                </div>
                            </div>
                        </c:forEach>
                    </c:when>
                    <c:otherwise>
                        <div class="empty-state">
                            <p>🎉 ¡Comienza registrando tu primera mascota!</p>
                        </div>
                    </c:otherwise>
                </c:choose>
            </div>
        </section>
    </div>
</main>

<footer class="main-footer">
    <p>&copy; 2025 Sistema de Gestión de Mascotas. Todos los derechos reservados.</p>
</footer>
</body>
</html>
