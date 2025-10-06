# Sistema de Gestión de Mascotas 🐾

## Descripción del Proyecto
Sistema web desarrollado en Java para la gestión integral de mascotas, vacunas y visitas veterinarias. Implementado con tecnologías Jakarta EE, Hibernate y MySQL.

## 🔧 Tecnologías Utilizadas
- **Java**: 17
- **Jakarta EE**: 6.0
- **Hibernate ORM**: 6.4.4
- **PostgreSQL**: 14+
- **Maven**: Gestión de dependencias
- **JSTL**: 3.0.1
- **Servidor de aplicaciones**: Compatible con Tomcat 10+, Jetty, etc.

## 📁 Estructura del Proyecto

```
Gestion_Mascotas/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/
│   │   │       └── gestion/
│   │   │           └── mascotas/
│   │   │               ├── controlador/          # Servlets (Controladores)
│   │   │               │   ├── DashboardServlet.java
│   │   │               │   ├── LoginServlet.java
│   │   │               │   ├── LogoutServlet.java
│   │   │               │   ├── MascotaServlet.java
│   │   │               │   ├── UsuarioServlet.java
│   │   │               │   ├── VacunaServlet.java
│   │   │               │   ├── VisitaServlet.java
│   │   │               │   └── Incremento1/
│   │   │               │       └── CrearPerfilServlet.java
│   │   │               ├── dao/                   # Capa de Acceso a Datos
│   │   │               │   ├── MascotaDAO.java
│   │   │               │   ├── UsuarioDAO.java
│   │   │               │   ├── VacunaDAO.java
│   │   │               │   └── VisitaVeterinariaDAO.java
│   │   │               ├── modelo/                # Entidades JPA
│   │   │               │   ├── Mascota.java
│   │   │               │   ├── TipoMascota.java
│   │   │               │   ├── Usuario.java
│   │   │               │   ├── Vacuna.java
│   │   │               │   └── VisitaVeterinaria.java
│   │   │               └── util/                  # Utilidades
│   │   │                   ├── HibernateListener.java
│   │   │                   └── HibernateUtil.java
│   │   ├── resources/
│   │   │   ├── META-INF/
│   │   │   │   └── persistence.xml              # Configuración JPA
│   │   │   └── hibernate.cfg.xml                # Configuración Hibernate
│   │   └── webapp/
│   │       ├── css/                             # Estilos CSS
│   │       ├── js/                              # Scripts JavaScript
│   │       ├── jsp/                             # Páginas JSP
│   │       └── WEB-INF/
│   │           └── web.xml                      # Descriptor de despliegue
│   └── test/
├── pom.xml                                      # Configuración Maven
└── gestion_mascotas.sql                         # Script SQL de base de datos
```

## 🗄️ Modelo de Datos

### Entidades Principales

1. **Usuario**
   - ID (PK)
   - Nombre de Usuario (único)
   - Nombre
   - Email (único)
   - Teléfono
   - Contraseña
   - Lista de Mascotas (relación 1:N)

2. **Mascota**
   - ID (PK)
   - Nombre
   - Tipo (PERRO/GATO)
   - Usuario (FK)
   - Lista de Vacunas (relación 1:N)
   - Lista de Visitas (relación 1:N)

3. **Vacuna**
   - ID (PK)
   - Nombre
   - Fecha
   - Mascota (FK)

4. **VisitaVeterinaria**
   - ID (PK)
   - Fecha
   - Motivo
   - Mascota (FK)

## 🚀 Instalación y Configuración

### Prerequisitos
- JDK 17 o superior
- Maven 3.6+
- PostgreSQL 14+
- Servidor de aplicaciones compatible con Jakarta EE 6.0 (Tomcat 10+)

**Acceder a la aplicación**
   ```
   http://localhost:8080/gestion_mascotas
   ```

## 📋 Funcionalidades

### Gestión de Usuarios
- ✅ Registro de nuevos usuarios
- ✅ Inicio de sesión
- ✅ Cierre de sesión
- ✅ Validación de credenciales
- ✅ Actualización de perfil
- ✅ Eliminación de cuenta

### Gestión de Mascotas
- ✅ Registro de mascotas (PERRO/GATO)
- ✅ Listado de mascotas del usuario
- ✅ Eliminación de mascotas
- ✅ Asociación con usuario propietario

### Gestión de Vacunas
- ✅ Registro de vacunas para mascotas
- ✅ Listado de vacunas
- ✅ Eliminación de vacunas
- ✅ Seguimiento de fechas de vacunación

### Gestión de Visitas Veterinarias
- ✅ Registro de visitas al veterinario
- ✅ Listado de visitas
- ✅ Registro de motivos de consulta
- ✅ Eliminación de visitas

### Dashboard
- ✅ Vista centralizada de estadísticas
- ✅ Resumen de mascotas, vacunas y visitas

## 🛠️ Patrones de Diseño Implementados

1. **DAO (Data Access Object)**: Separación de la lógica de acceso a datos
2. **MVC (Model-View-Controller)**: Arquitectura de tres capas
3. **Singleton**: Para SessionFactory de Hibernate
4. **Front Controller**: Servlets como puntos de entrada

**Estado:** ✅ Completado y funcional

---

## 👥 Equipo

**Grupo:** GR06  
**Periodo:** 1BT3_622_25B  
**Asignatura:** Metodologías Ágiles
