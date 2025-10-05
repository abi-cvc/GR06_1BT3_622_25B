<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="es">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Header</title>
  <style>
    /* Basic CSS for the header - you might want to move this to style.css */
    header {
      background-color: #4CAF50;
      color: white;
      padding: 1em 0;
      text-align: center;
      box-shadow: 0 2px 5px rgba(0,0,0,0.2);
    }
    header nav ul {
      list-style-type: none;
      padding: 0;
      margin: 0;
      display: flex;
      justify-content: center;
      flex-wrap: wrap;
    }
    header nav ul li {
      margin: 0 15px;
    }
    header nav ul li a {
      color: white;
      text-decoration: none;
      font-weight: bold;
      padding: 5px 10px;
      border-radius: 3px;
      transition: background-color 0.3s ease;
    }
    header nav ul li a:hover {
      background-color: #45a049;
    }
    .user-info {
      margin-top: 10px;
      font-size: 0.9em;
    }
    .user-info a {
      color: white;
      text-decoration: underline;
    }
  </style>
</head>
<body>
<header>
  <nav>
    <ul>
      <li><a href="${pageContext.request.contextPath}/dashboard">Dashboard</a></li>
      <li><a href="${pageContext.request.contextPath}/mascota?action=listar">Mis Mascotas</a></li>
      <li><a href="${pageContext.request.contextPath}/recordatorio?action=registrar">Registrar Recordatorio</a></li>
      <li><a href="${pageContext.request.contextPath}/sugerencia?action=generar">Generar Sugerencia</a></li>
      <li><a href="${pageContext.request.contextPath}/vacuna?action=listar">Vacunas</a></li>
      <li><a href="${pageContext.request.contextPath}/visita?action=listar">Visitas Veterinarias</a></li>
      <c:if test="${sessionScope.usuario != null}">
        <li><a href="${pageContext.request.contextPath}/logout">Cerrar Sesión</a></li>
      </c:if>
      <c:if test="${sessionScope.usuario == null}">
        <li><a href="${pageContext.request.contextPath}/login">Iniciar Sesión</a></li>
      </c:if>
    </ul>
  </nav>
  <c:if test="${sessionScope.nombreUsuario != null}">
    <div class="user-info">
      Bienvenido, ${sessionScope.nombreUsuario}!
    </div>
  </c:if>
</header>
</body>
</html>