# Sistema de Gestión de Mascotas 🐾

## Descripción del Proyecto
Sistema web desarrollado en Java para la gestión integral de mascotas, vacunas y visitas veterinarias. Implementado con tecnologías Jakarta EE, Hibernate y MySQL.

## 🔧 Tecnologías Utilizadas
- **Java**: 17
- **Jakarta EE**: 6.0
- **Hibernate ORM**: 6.4.4
- **MySQL**: 8.0
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
- MySQL 8.0+
- Servidor de aplicaciones compatible con Jakarta EE 6.0 (Tomcat 10+)

### Pasos de Instalación

1. **Clonar el repositorio**
   ```bash
   git clone <url-repositorio>
   cd Gestion_Mascotas
   ```

2. **Configurar la base de datos**
   - Crear la base de datos en MySQL:
     ```sql
     CREATE DATABASE gestion_mascotas;
     ```
   - Ejecutar el script SQL proporcionado:
     ```bash
     mysql -u root -p gestion_mascotas < gestion_mascotas.sql
     ```

3. **Configurar credenciales de la base de datos**
   
   Actualizar los archivos de configuración con tus credenciales de MySQL:
   
   **`src/main/resources/persistence.xml`**:
   ```xml
   <property name="jakarta.persistence.jdbc.user" value="TU_USUARIO"/>
   <property name="jakarta.persistence.jdbc.password" value="TU_CONTRASEÑA"/>
   ```
   
   **`src/main/resources/hibernate.cfg.xml`**:
   ```xml
   <property name="hibernate.connection.username">TU_USUARIO</property>
   <property name="hibernate.connection.password">TU_CONTRASEÑA</property>
   ```

4. **Compilar el proyecto**
   ```bash
   mvn clean install
   ```

5. **Desplegar en el servidor**
   - Copiar el archivo WAR generado en `target/gestion_mascotas-1.0-SNAPSHOT.war` al directorio de despliegue de tu servidor (ej: `webapps/` en Tomcat)
   - O usar Maven para desplegar:
     ```bash
     mvn tomcat7:deploy
     ```

6. **Acceder a la aplicación**
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

## 🔐 Seguridad
- Gestión de sesiones HTTP
- Validación de usuarios autenticados
- Timeout de sesión configurable (30 minutos)
- Redirección automática a login para usuarios no autenticados

## 🛠️ Patrones de Diseño Implementados

1. **DAO (Data Access Object)**: Separación de la lógica de acceso a datos
2. **MVC (Model-View-Controller)**: Arquitectura de tres capas
3. **Singleton**: Para SessionFactory de Hibernate
4. **Front Controller**: Servlets como puntos de entrada

## 📦 Dependencias Maven

```xml
<!-- Servlet API -->
<dependency>
    <groupId>jakarta.servlet</groupId>
    <artifactId>jakarta.servlet-api</artifactId>
    <version>6.0.0</version>
</dependency>

<!-- Hibernate ORM -->
<dependency>
    <groupId>org.hibernate.orm</groupId>
    <artifactId>hibernate-core</artifactId>
    <version>6.4.4.Final</version>
</dependency>

<!-- MySQL Connector -->
<dependency>
    <groupId>com.mysql</groupId>
    <artifactId>mysql-connector-j</artifactId>
    <version>8.3.0</version>
</dependency>

<!-- JSTL -->
<dependency>
    <groupId>org.glassfish.web</groupId>
    <artifactId>jakarta.servlet.jsp.jstl</artifactId>
    <version>3.0.1</version>
</dependency>
```

## 🐛 Solución de Problemas Comunes

### Error de conexión a la base de datos
- Verificar que MySQL esté ejecutándose
- Verificar credenciales en `persistence.xml` y `hibernate.cfg.xml`
- Verificar que la base de datos `gestion_mascotas` exista

### Error 404 al acceder a la aplicación
- Verificar que el servidor esté ejecutándose
- Verificar el contexto de despliegue
- Verificar la URL de acceso

### Errores de compilación
- Ejecutar `mvn clean install` para limpiar y recompilar
- Verificar que JDK 17 esté configurado
- Verificar que todas las dependencias se hayan descargado

## 📝 Notas de Desarrollo

### Convenciones de Código
- Nombres de paquetes en minúsculas: `com.gestion.mascotas.*`
- Nombres de clases en PascalCase: `MascotaServlet`, `UsuarioDAO`
- Nombres de métodos en camelCase: `validarLogin()`, `obtenerTodas()`
- Uso de annotations JPA/Hibernate para mapeo de entidades
- Uso de annotations de servlets (`@WebServlet`)

### Hibernate 6.x - Métodos Actualizados
Se ha actualizado el código para usar los métodos modernos de Hibernate 6:
- `session.persist()` en lugar de `session.save()`
- `session.merge()` en lugar de `session.update()`
- `session.remove()` en lugar de `session.delete()`

## 🔄 Versión Actual
**v1.0-SNAPSHOT**

## 👥 Contribuciones
Este proyecto fue desarrollado como parte de la asignatura de Metodologías Ágiles.

## 📄 Licencia
Proyecto académico - Todos los derechos reservados.

---

**Última actualización**: Octubre 2025
