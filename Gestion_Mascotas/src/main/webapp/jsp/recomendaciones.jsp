<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Recomendaciones para ${mascota.nombre} - Gestión de Mascotas</title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/dashboard.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
    <style>
        .recomendaciones-header {
            text-align: center;
            margin-bottom: 3rem;
            padding: 2rem;
            background: linear-gradient(135deg, var(--primary-light), var(--primary-color));
            border-radius: var(--radius-xl);
            color: white;
            box-shadow: var(--shadow-xl);
        }

        .recomendaciones-header h1 {
            font-size: 2.5rem;
            font-weight: 700;
            margin-bottom: 0.5rem;
            text-transform: uppercase;
            letter-spacing: 1px;
        }

        .recomendaciones-header .mascota-info {
            font-size: 1.2rem;
            opacity: 0.95;
            margin-top: 1rem;
        }

        .recomendaciones-header .mascota-info i {
            margin-right: 0.5rem;
        }

        .recomendaciones-grid {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(450px, 1fr));
            gap: 2rem;
            margin-top: 2rem;
        }

        .recomendacion-card {
            background: var(--bg-primary);
            border-radius: var(--radius-xl);
            padding: 2rem;
            box-shadow: var(--shadow-lg);
            transition: var(--transition);
        }

        .recomendacion-card:hover {
            transform: translateY(-5px);
            box-shadow: var(--shadow-xl);
        }

        .recomendacion-card-header {
            display: flex;
            align-items: center;
            gap: 1rem;
            margin-bottom: 2rem;
            padding-bottom: 1rem;
            border-bottom: 3px solid var(--primary-color);
        }

        .recomendacion-card-header.alimentacion {
            border-bottom-color: #28a745;
        }

        .recomendacion-card-header.ejercicio {
            border-bottom-color: #ff6b6b;
        }

        .recomendacion-card-header i {
            font-size: 3rem;
            color: var(--primary-color);
        }

        .recomendacion-card-header.alimentacion i {
            color: #28a745;
        }

        .recomendacion-card-header.ejercicio i {
            color: #ff6b6b;
        }

        .recomendacion-card-header h2 {
            font-size: 1.8rem;
            color: var(--text-primary);
            margin: 0;
        }

        .sugerencia-item {
            background: var(--bg-secondary);
            border-radius: var(--radius-md);
            padding: 1.5rem;
            margin-bottom: 1.5rem;
            border-left: 4px solid var(--primary-color);
            transition: var(--transition);
        }

        .sugerencia-item.alimentacion {
            border-left-color: #28a745;
        }

        .sugerencia-item.ejercicio {
            border-left-color: #ff6b6b;
        }

        .sugerencia-item:hover {
            transform: translateX(5px);
            box-shadow: var(--shadow-md);
        }

        .sugerencia-item h3 {
            font-size: 1.3rem;
            color: var(--text-primary);
            margin-bottom: 1rem;
            display: flex;
            align-items: center;
            gap: 0.5rem;
        }

        .sugerencia-item h3 i {
            color: var(--primary-color);
            font-size: 1.1rem;
        }

        .sugerencia-item.alimentacion h3 i {
            color: #28a745;
        }

        .sugerencia-item.ejercicio h3 i {
            color: #ff6b6b;
        }

        .sugerencia-descripcion {
            color: var(--text-secondary);
            line-height: 1.6;
            margin-bottom: 1rem;
        }

        .sugerencia-detalles {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(150px, 1fr));
            gap: 1rem;
            margin-top: 1rem;
        }

        .detalle-item {
            background: var(--bg-tertiary);
            padding: 0.75rem;
            border-radius: var(--radius-sm);
            display: flex;
            align-items: center;
            gap: 0.5rem;
        }

        .detalle-item i {
            color: var(--primary-color);
            font-size: 1rem;
        }

        .detalle-item.alimentacion i {
            color: #28a745;
        }

        .detalle-item.ejercicio i {
            color: #ff6b6b;
        }

        .detalle-item span {
            font-size: 0.9rem;
            color: var(--text-secondary);
        }

        .detalle-item strong {
            color: var(--text-primary);
            font-weight: 600;
        }

        .fuente-info {
            margin-top: 1rem;
            padding-top: 1rem;
            border-top: 1px solid var(--border-color);
            font-size: 0.85rem;
            color: var(--text-secondary);
            font-style: italic;
        }

        .fuente-info i {
            margin-right: 0.3rem;
        }

        .no-sugerencias {
            text-align: center;
            padding: 3rem 2rem;
            background: var(--bg-secondary);
            border-radius: var(--radius-lg);
            color: var(--text-secondary);
        }

        .no-sugerencias i {
            font-size: 4rem;
            margin-bottom: 1rem;
            opacity: 0.5;
        }

        .no-sugerencias h3 {
            font-size: 1.5rem;
            margin-bottom: 0.5rem;
            color: var(--text-primary);
        }

        .badge {
            display: inline-block;
            padding: 0.3rem 0.8rem;
            border-radius: var(--radius-sm);
            font-size: 0.85rem;
            font-weight: 600;
            margin-left: 0.5rem;
        }

        .badge.baja {
            background: #ffc107;
            color: #000;
        }

        .badge.media {
            background: #ff9800;
            color: white;
        }

        .badge.alta {
            background: #dc3545;
            color: white;
        }

        .btn-volver {
            margin-bottom: 2rem;
            display: inline-flex;
            align-items: center;
            gap: 0.5rem;
            padding: 0.75rem 1.5rem;
            background: var(--bg-primary);
            color: var(--text-primary);
            text-decoration: none;
            border-radius: var(--radius-sm);
            transition: var(--transition);
            box-shadow: var(--shadow-sm);
        }

        .btn-volver:hover {
            background: var(--primary-color);
            color: white;
            transform: translateX(-3px);
        }

        @media (max-width: 992px) {
            .recomendaciones-grid {
                grid-template-columns: 1fr;
            }

            .recomendaciones-header h1 {
                font-size: 2rem;
            }
        }

        @media (max-width: 768px) {
            .recomendaciones-header {
                padding: 1.5rem;
            }

            .recomendaciones-header h1 {
                font-size: 1.5rem;
            }

            .recomendacion-card {
                padding: 1.5rem;
            }

            .recomendacion-card-header h2 {
                font-size: 1.5rem;
            }

            .sugerencia-detalles {
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
        <a href="${pageContext.request.contextPath}/mascota?action=detalles&id=${mascota.id}" class="btn-volver">
            <i class="fas fa-arrow-left"></i> Volver a ${mascota.nombre}
        </a>

        <!-- Header de Recomendaciones -->
        <div class="recomendaciones-header">
            <h1>🐾 CONSIENTE A TU ANIMALITO APLICANDO LAS SIGUIENTES RECOMENDACIONES 🐾</h1>
            <div class="mascota-info">
                <i class="fas <c:if test='${mascota.tipo == "PERRO"}'>fa-dog</c:if><c:if test='${mascota.tipo == "GATO"}'>fa-cat</c:if>"></i>
                <strong>${mascota.nombre}</strong> - ${mascota.raza} | ${mascota.edad} años | ${mascota.peso} kg
            </div>
        </div>

        <!-- Grid de Recomendaciones -->
        <div class="recomendaciones-grid">

            <!-- Columna de Alimentación -->
            <div class="recomendacion-card">
                <div class="recomendacion-card-header alimentacion">
                    <i class="fas fa-utensils"></i>
                    <h2>🍖 Alimentación</h2>
                </div>

                <c:choose>
                    <c:when test="${not empty sugerenciasAlimentacion}">
                        <c:forEach var="sugerencia" items="${sugerenciasAlimentacion}">
                            <div class="sugerencia-item alimentacion">
                                <h3>
                                    <i class="fas fa-bone"></i>
                                    <c:choose>
                                        <c:when test="${not empty sugerencia.raza}">
                                            Recomendación para ${sugerencia.raza}
                                        </c:when>
                                        <c:otherwise>
                                            Recomendación General
                                        </c:otherwise>
                                    </c:choose>
                                </h3>

                                <div class="sugerencia-descripcion">
                                        ${sugerencia.descripcion}
                                </div>

                                <div class="sugerencia-detalles">
                                    <c:if test="${not empty sugerencia.tipoComida}">
                                        <div class="detalle-item alimentacion">
                                            <i class="fas fa-hamburger"></i>
                                            <span><strong>Tipo:</strong> ${sugerencia.tipoComida}</span>
                                        </div>
                                    </c:if>

                                    <c:if test="${not empty sugerencia.frecuencia}">
                                        <div class="detalle-item alimentacion">
                                            <i class="fas fa-clock"></i>
                                            <span><strong>Frecuencia:</strong> ${sugerencia.frecuencia}</span>
                                        </div>
                                    </c:if>

                                    <c:if test="${not empty sugerencia.caloriasRecomendadas}">
                                        <div class="detalle-item alimentacion">
                                            <i class="fas fa-fire"></i>
                                            <span><strong>Calorías:</strong> ${sugerencia.caloriasRecomendadas} kcal/día</span>
                                        </div>
                                    </c:if>

                                    <c:if test="${not empty sugerencia.edadMin && not empty sugerencia.edadMax}">
                                        <div class="detalle-item alimentacion">
                                            <i class="fas fa-birthday-cake"></i>
                                            <span><strong>Edad:</strong> ${sugerencia.edadMin}-${sugerencia.edadMax} años</span>
                                        </div>
                                    </c:if>

                                    <c:if test="${not empty sugerencia.pesoMin && not empty sugerencia.pesoMax}">
                                        <div class="detalle-item alimentacion">
                                            <i class="fas fa-weight"></i>
                                            <span><strong>Peso:</strong> ${sugerencia.pesoMin}-${sugerencia.pesoMax} kg</span>
                                        </div>
                                    </c:if>
                                </div>

                                <c:if test="${not empty sugerencia.fuente}">
                                    <div class="fuente-info">
                                        <i class="fas fa-book"></i>
                                        Fuente: ${sugerencia.fuente}
                                    </div>
                                </c:if>
                            </div>
                        </c:forEach>
                    </c:when>
                    <c:otherwise>
                        <div class="no-sugerencias">
                            <i class="fas fa-utensils"></i>
                            <h3>No hay recomendaciones disponibles</h3>
                            <p>No se encontraron sugerencias de alimentación para ${mascota.nombre}.</p>
                        </div>
                    </c:otherwise>
                </c:choose>
            </div>

            <!-- Columna de Ejercicio -->
            <div class="recomendacion-card">
                <div class="recomendacion-card-header ejercicio">
                    <i class="fas fa-running"></i>
                    <h2>🏃 Ejercicio</h2>
                </div>

                <c:choose>
                    <c:when test="${not empty sugerenciasEjercicio}">
                        <c:forEach var="sugerencia" items="${sugerenciasEjercicio}">
                            <div class="sugerencia-item ejercicio">
                                <h3>
                                    <i class="fas fa-heartbeat"></i>
                                    <c:choose>
                                        <c:when test="${not empty sugerencia.raza}">
                                            Recomendación para ${sugerencia.raza}
                                        </c:when>
                                        <c:otherwise>
                                            Recomendación General
                                        </c:otherwise>
                                    </c:choose>
                                    <c:if test="${not empty sugerencia.nivelActividad}">
                    <span class="badge ${sugerencia.nivelActividad.toString().toLowerCase()}">
                            ${sugerencia.nivelActividad}
                    </span>
                                    </c:if>
                                </h3>

                                <div class="sugerencia-descripcion">
                                        ${sugerencia.descripcion}
                                </div>

                                <div class="sugerencia-detalles">
                                    <c:if test="${not empty sugerencia.tipoEjercicio}">
                                        <div class="detalle-item ejercicio">
                                            <i class="fas fa-walking"></i>
                                            <span><strong>Actividad:</strong> ${sugerencia.tipoEjercicio}</span>
                                        </div>
                                    </c:if>

                                    <c:if test="${not empty sugerencia.duracionMinutos}">
                                        <div class="detalle-item ejercicio">
                                            <i class="fas fa-stopwatch"></i>
                                            <span><strong>Duración:</strong> ${sugerencia.duracionMinutos} min</span>
                                        </div>
                                    </c:if>

                                    <c:if test="${not empty sugerencia.edadMin && not empty sugerencia.edadMax}">
                                        <div class="detalle-item ejercicio">
                                            <i class="fas fa-birthday-cake"></i>
                                            <span><strong>Edad:</strong> ${sugerencia.edadMin}-${sugerencia.edadMax} años</span>
                                        </div>
                                    </c:if>

                                    <c:if test="${not empty sugerencia.pesoMin && not empty sugerencia.pesoMax}">
                                        <div class="detalle-item ejercicio">
                                            <i class="fas fa-weight"></i>
                                            <span><strong>Peso:</strong> ${sugerencia.pesoMin}-${sugerencia.pesoMax} kg</span>
                                        </div>
                                    </c:if>
                                </div>

                                <c:if test="${not empty sugerencia.fuente}">
                                    <div class="fuente-info">
                                        <i class="fas fa-book"></i>
                                        Fuente: ${sugerencia.fuente}
                                    </div>
                                </c:if>
                            </div>
                        </c:forEach>
                    </c:when>
                    <c:otherwise>
                        <div class="no-sugerencias">
                            <i class="fas fa-running"></i>
                            <h3>No hay recomendaciones disponibles</h3>
                            <p>No se encontraron sugerencias de ejercicio para ${mascota.nombre}.</p>
                        </div>
                    </c:otherwise>
                </c:choose>
            </div>

        </div>
    </div>
</main>

<script src="${pageContext.request.contextPath}/js/dashboard.js"></script>
</body>
</html>