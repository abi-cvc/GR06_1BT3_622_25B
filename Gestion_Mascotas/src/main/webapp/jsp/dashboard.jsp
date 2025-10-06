<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Dashboard - Gestión de Mascotas</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/dashboard.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
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
                <li><a href="${pageContext.request.contextPath}/dashboard" class="active"><i class="fas fa-home"></i> Inicio</a></li>
                <li><a href="${pageContext.request.contextPath}/mascotas"><i class="fas fa-dog"></i> Mis Mascotas</a></li>
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

    <!-- Main Content -->
    <main class="main-content">
        <div class="container">
            <!-- Panel de Usuario -->
            <section class="user-panel">
                <div class="user-panel-header">
                    <div class="user-avatar">
                        <i class="fas fa-user-circle"></i>
                    </div>
                    <div class="user-info">
                        <h2 class="user-name">${sessionScope.nombreCompleto}</h2>
                        <p class="user-email"><i class="fas fa-envelope"></i> ${sessionScope.email}</p>
                        <p class="user-username"><i class="fas fa-at"></i> ${sessionScope.nombreUsuario}</p>
                        <c:if test="${not empty sessionScope.usuario.telefono}">
                            <p class="user-phone"><i class="fas fa-phone"></i> ${sessionScope.usuario.telefono}</p>
                        </c:if>
                    </div>
                    <div class="user-actions">
                        <button class="btn btn-primary" onclick="abrirModalEditar()">
                            <i class="fas fa-edit"></i> Editar Perfil
                        </button>
                        <button class="btn btn-danger" onclick="confirmarEliminarPerfil()">
                            <i class="fas fa-trash-alt"></i> Eliminar Perfil
                        </button>
                        <button class="btn btn-secondary" onclick="confirmarLogout()">
                            <i class="fas fa-sign-out-alt"></i> Cerrar Sesión
                        </button>
                    </div>
                </div>
            </section>

            <!-- Mensajes de éxito/error -->
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

            <!-- Sección de Bienvenida -->
            <section class="welcome-section">
                <h1>¡Bienvenido de nuevo! 👋</h1>
                <p>Gestiona toda la información de tus mascotas en un solo lugar</p>
            </section>

            <!-- Estadísticas -->
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

            <!-- Acciones Rápidas -->
            <section class="quick-actions">
                <h2>Acciones Rápidas</h2>
                <div class="actions-grid">
                    <a href="#" onclick="abrirModalRegistrarMascota(); return false;" class="action-card">
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

                    <a href="#" onclick="abrirModalEditar(); return false;" class="action-card">
                        <div class="action-icon">⚙️</div>
                        <h3>Editar Perfil</h3>
                        <p>Actualiza tu información personal</p>
                    </a>
                </div>
            </section>
        </div>
    </main>

    <!-- Modal de Editar Perfil -->
    <div id="modalEditarPerfil" class="modal">
        <div class="modal-content">
            <div class="modal-header">
                <h2><i class="fas fa-user-edit"></i> Editar Perfil</h2>
                <button class="modal-close" onclick="cerrarModalEditar()">&times;</button>
            </div>
            <form action="${pageContext.request.contextPath}/perfil" method="post" id="formEditarPerfil">
                <input type="hidden" name="action" value="actualizar">

                <div class="form-group">
                    <label for="nombre">
                        <i class="fas fa-id-card"></i> Nombre Completo
                    </label>
                    <input type="text"
                           id="nombre"
                           name="nombre"
                           value="${sessionScope.nombreCompleto}"
                           required
                           maxlength="100">
                </div>

                <div class="form-group">
                    <label for="email">
                        <i class="fas fa-envelope"></i> Correo Electrónico
                    </label>
                    <input type="email"
                           id="email"
                           name="email"
                           value="${sessionScope.email}"
                           required
                           maxlength="100">
                </div>

                <div class="form-group">
                    <label for="telefono">
                        <i class="fas fa-phone"></i> Teléfono
                    </label>
                    <input type="tel"
                           id="telefono"
                           name="telefono"
                           value="${sessionScope.usuario.telefono}"
                           pattern="[0-9]{10}"
                           maxlength="10"
                           placeholder="0999999999">
                    <small>Opcional, 10 dígitos</small>
                </div>

                <div class="form-group">
                    <label for="contrasenaActual">
                        <i class="fas fa-lock"></i> Contraseña Actual
                    </label>
                    <div class="password-input">
                        <input type="password"
                               id="contrasenaActual"
                               name="contrasenaActual"
                               placeholder="Ingresa tu contraseña actual para confirmar">
                        <button type="button" class="toggle-password" onclick="togglePassword('contrasenaActual')">
                            <i class="fas fa-eye" id="toggle-icon-contrasenaActual"></i>
                        </button>
                    </div>
                    <small>Requerida para guardar cambios</small>
                </div>

                <div class="form-group">
                    <label>
                        <input type="checkbox" id="cambiarContrasena" onchange="toggleCambiarContrasena()">
                        ¿Deseas cambiar tu contraseña?
                    </label>
                </div>

                <div id="camposNuevaContrasena" style="display: none;">
                    <div class="form-group">
                        <label for="nuevaContrasena">
                            <i class="fas fa-key"></i> Nueva Contraseña
                        </label>
                        <div class="password-input">
                            <input type="password"
                                   id="nuevaContrasena"
                                   name="nuevaContrasena"
                                   placeholder="Mínimo 6 caracteres"
                                   minlength="6">
                            <button type="button" class="toggle-password" onclick="togglePassword('nuevaContrasena')">
                                <i class="fas fa-eye" id="toggle-icon-nuevaContrasena"></i>
                            </button>
                        </div>
                    </div>

                    <div class="form-group">
                        <label for="confirmarNuevaContrasena">
                            <i class="fas fa-key"></i> Confirmar Nueva Contraseña
                        </label>
                        <div class="password-input">
                            <input type="password"
                                   id="confirmarNuevaContrasena"
                                   name="confirmarNuevaContrasena"
                                   placeholder="Repite la nueva contraseña"
                                   minlength="6">
                            <button type="button" class="toggle-password" onclick="togglePassword('confirmarNuevaContrasena')">
                                <i class="fas fa-eye" id="toggle-icon-confirmarNuevaContrasena"></i>
                            </button>
                        </div>
                    </div>
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

    <!-- Modal de Confirmación de Eliminación -->
    <div id="modalEliminar" class="modal">
        <div class="modal-content modal-confirm">
            <div class="modal-header danger">
                <i class="fas fa-exclamation-triangle"></i>
                <h2>Confirmar Eliminación de Perfil</h2>
            </div>
            <div class="modal-body">
                <p><strong>⚠️ Esta acción es permanente e irreversible</strong></p>
                <p>Se eliminará:</p>
                <ul>
                    <li>Tu perfil de usuario</li>
                    <li>Todas tus mascotas registradas</li>
                    <li>Historial de visitas veterinarias</li>
                    <li>Registro de vacunas</li>
                </ul>
                <form action="${pageContext.request.contextPath}/perfil" method="post" id="formEliminarPerfil">
                    <input type="hidden" name="action" value="eliminar">

                    <div class="form-group">
                        <label for="contrasenaEliminar">
                            <i class="fas fa-lock"></i> Confirma tu contraseña para continuar
                        </label>
                        <input type="password"
                               id="contrasenaEliminar"
                               name="contrasena"
                               placeholder="Ingresa tu contraseña"
                               required>
                    </div>

                    <div class="form-group">
                        <label>
                            <input type="checkbox" id="confirmarEliminacion" required>
                            Entiendo que esta acción no se puede deshacer
                        </label>
                    </div>

                    <div class="modal-actions">
                        <button type="button" class="btn btn-secondary" onclick="cerrarModalEliminar()">
                            <i class="fas fa-times"></i> Cancelar
                        </button>
                        <button type="submit" class="btn btn-danger">
                            <i class="fas fa-trash-alt"></i> Eliminar Mi Perfil
                        </button>
                    </div>
                </form>
            </div>
        </div>
    </div>

    <!-- Modal de Confirmación de Logout -->
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

    <script src="${pageContext.request.contextPath}/js/dashboard.js"></script>
</body>
</html>