# 🚀 Guía Rápida de Despliegue

## Prerequisitos Verificados ✓
- [x] JDK 17 instalado
- [x] Maven 3.6+ instalado
- [x] PostgreSQL 14+ instalado y ejecutándose
- [x] Servidor de aplicaciones (Tomcat 10+, Jetty, etc.)

## 🔥 Despliegue en 5 Pasos

### Paso 1: Configurar Base de Datos
```sql
-- Conectarse a PostgreSQL (Windows)
psql -U postgres

-- O en Linux/Mac
sudo -u postgres psql

-- Crear base de datos
CREATE DATABASE gestion_mascotas;

-- Conectarse a la base de datos
\c gestion_mascotas

-- Ejecutar script SQL (desde terminal del sistema, no desde psql)
-- Windows:
psql -U postgres -d gestion_mascotas -f gestion_mascotas_postgres.sql

-- Linux/Mac:
psql -U postgres -d gestion_mascotas -f gestion_mascotas_postgres.sql

-- Verificar tablas creadas (desde psql)
\dt

-- Salir
\q
```

### Paso 2: Actualizar Credenciales
Editar estos 2 archivos con tus credenciales de PostgreSQL:

**`src/main/resources/persistence.xml`** (líneas 17-18):
```xml
<property name="jakarta.persistence.jdbc.user" value="postgres"/>
<property name="jakarta.persistence.jdbc.password" value="TU_PASSWORD_AQUI"/>
```

**`src/main/resources/hibernate.cfg.xml`** (líneas 9-10):
```xml
<property name="hibernate.connection.username">postgres</property>
<property name="hibernate.connection.password">TU_PASSWORD_AQUI</property>
```

### Paso 3: Compilar el Proyecto
```bash
cd Gestion_Mascotas
mvn clean package
```

**Resultado esperado**: 
```
BUILD SUCCESS
```
Archivo generado: `target/gestion_mascotas-1.0-SNAPSHOT.war`

### Paso 4: Desplegar en Servidor

#### Opción A: Tomcat (Manual)
```bash
# Copiar WAR al directorio de Tomcat
copy target\gestion_mascotas-1.0-SNAPSHOT.war C:\ruta\a\tomcat\webapps\

# Iniciar Tomcat
cd C:\ruta\a\tomcat\bin
startup.bat
```

#### Opción B: Tomcat (Maven Plugin)
Agregar al `pom.xml`:
```xml
<plugin>
    <groupId>org.apache.tomcat.maven</groupId>
    <artifactId>tomcat7-maven-plugin</artifactId>
    <version>2.2</version>
    <configuration>
        <url>http://localhost:8080/manager/text</url>
        <server>TomcatServer</server>
        <path>/gestion_mascotas</path>
    </configuration>
</plugin>
```

Luego ejecutar:
```bash
mvn tomcat7:deploy
```

#### Opción C: Desde IDE
1. Configurar servidor en IDE (Eclipse, IntelliJ, NetBeans)
2. Agregar proyecto al servidor
3. Iniciar servidor en modo debug

### Paso 5: Verificar Despliegue

**URL de acceso**:
```
http://localhost:8080/gestion_mascotas
```

**Verificaciones**:
- ✅ La página de inicio carga correctamente
- ✅ Se puede registrar un nuevo usuario
- ✅ Se puede iniciar sesión
- ✅ El dashboard es accesible

## 🔍 Verificación de Logs

### Verificar Inicialización de Hibernate
Buscar en los logs del servidor:
```
===========================================
Inicializando Sistema de Gestión de Mascotas
===========================================
✓ Hibernate inicializado correctamente
✓ Conexión a base de datos establecida
===========================================
```

### Verificar Creación de Tablas
En PostgreSQL:
```sql
-- Conectarse a la base de datos
psql -U postgres -d gestion_mascotas

-- Ver todas las tablas
\dt

-- Ver estructura de una tabla
\d usuarios
\d mascotas
\d vacunas
\d visitas

-- Salir
\q
```

**Resultado esperado**:
```
              List of relations
 Schema |   Name   | Type  |  Owner   
--------+----------+-------+----------
 public | mascotas | table | postgres
 public | usuarios | table | postgres
 public | vacunas  | table | postgres
 public | visitas  | table | postgres
```

## 🐛 Solución de Problemas Comunes

### Error: "No suitable driver found"
**Problema**: PostgreSQL driver no encontrado

**Solución**:
1. Verificar que postgresql esté en las dependencias del pom.xml
2. Limpiar y recompilar: `mvn clean package`

### Error: "Access denied for user" o "authentication failed"
**Problema**: Credenciales incorrectas

**Solución**:
1. Verificar usuario y contraseña en PostgreSQL
2. Actualizar persistence.xml y hibernate.cfg.xml
3. Verificar el método de autenticación en pg_hba.conf
4. Recompilar el proyecto

### Error: "Unknown database 'gestion_mascotas'"
**Problema**: Base de datos no creada

**Solución**:
```sql
CREATE DATABASE gestion_mascotas;
```

### Error 404: "Page not found"
**Problema**: Contexto incorrecto

**Solución**:
- Verificar URL: debe ser `/gestion_mascotas` no solo `/`
- Verificar que el WAR se haya desplegado correctamente

### Error: "Session factory is null"
**Problema**: Hibernate no se inicializó

**Solución**:
1. Verificar logs de servidor para ver errores de Hibernate
2. Verificar configuración de hibernate.cfg.xml
3. Verificar que PostgreSQL esté ejecutándose
4. Verificar conectividad: `psql -U postgres -d gestion_mascotas`

## 🧪 Pruebas Básicas

### Test 1: Registro de Usuario
1. Ir a http://localhost:8080/gestion_mascotas
2. Registrar usuario con datos de prueba
3. Verificar redirección a login

### Test 2: Login
1. Iniciar sesión con usuario creado
2. Verificar redirección a dashboard

### Test 3: Crear Mascota
1. Desde dashboard, ir a "Mascotas"
2. Registrar nueva mascota
3. Verificar que aparece en la lista

### Test 4: Registrar Vacuna
1. Ir a sección de vacunas
2. Seleccionar mascota
3. Registrar vacuna
4. Verificar que aparece en la lista

## 📊 Monitoreo

### Ver SQL Generado
En los logs del servidor podrás ver:
```sql
Hibernate: 
    select
        u1_0.id,
        u1_0.contrasena,
        u1_0.email,
        u1_0.nombre,
        u1_0.nombre_usuario,
        u1_0.telefono 
    from
        usuarios u1_0 
    where
        u1_0.nombre_usuario=?
```

### Verificar Pool de Conexiones
Buscar en logs:
```
C3P0 pool initialized
min_size: 5
max_size: 20
```

## 🔄 Actualización de Código

Después de hacer cambios en el código:
```bash
# 1. Detener servidor
# 2. Recompilar
mvn clean package

# 3. Redesplegar
# (Copiar WAR nuevamente o usar hot-reload del servidor)

# 4. Reiniciar servidor
```

## ✅ Checklist de Despliegue Exitoso

- [ ] Base de datos creada
- [ ] Credenciales actualizadas en archivos de configuración
- [ ] Proyecto compila sin errores (`mvn clean package`)
- [ ] WAR desplegado en servidor
- [ ] Servidor iniciado correctamente
- [ ] Logs muestran inicialización exitosa de Hibernate
- [ ] Aplicación accesible en navegador
- [ ] Registro de usuario funciona
- [ ] Login funciona
- [ ] Dashboard carga correctamente

## 🎯 URLs Importantes

| Función | URL |
|---------|-----|
| Página Principal | http://localhost:8080/gestion_mascotas/ |
| Login | http://localhost:8080/gestion_mascotas/login |
| Dashboard | http://localhost:8080/gestion_mascotas/dashboard |
| Mascotas | http://localhost:8080/gestion_mascotas/mascota |
| Vacunas | http://localhost:8080/gestion_mascotas/vacuna |
| Visitas | http://localhost:8080/gestion_mascotas/visita |
| Logout | http://localhost:8080/gestion_mascotas/logout |

---

**¿Necesitas ayuda?** Revisa el archivo `CHANGELOG.md` para ver todos los cambios realizados y el `README.md` para documentación completa.
