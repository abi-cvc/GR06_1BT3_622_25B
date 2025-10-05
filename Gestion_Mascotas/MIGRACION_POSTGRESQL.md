# 🔄 Guía de Migración: MySQL → PostgreSQL

## Resumen de Cambios Realizados

El proyecto ha sido actualizado para usar **PostgreSQL** en lugar de MySQL. Este documento detalla todos los cambios realizados.

---

## 📋 Cambios Implementados

### 1. Dependencias Maven (pom.xml)

**Antes (MySQL)**:
```xml
<!-- MySQL Connector -->
<dependency>
    <groupId>com.mysql</groupId>
    <artifactId>mysql-connector-j</artifactId>
    <version>8.3.0</version>
</dependency>
```

**Después (PostgreSQL)**:
```xml
<!-- PostgreSQL Driver -->
<dependency>
    <groupId>org.postgresql</groupId>
    <artifactId>postgresql</artifactId>
    <version>42.7.3</version>
</dependency>
```

---

### 2. Configuración JPA (persistence.xml)

**Antes (MySQL)**:
```xml
<property name="jakarta.persistence.jdbc.driver" value="com.mysql.cj.jdbc.Driver"/>
<property name="jakarta.persistence.jdbc.url" value="jdbc:mysql://localhost:3306/gestion_mascotas?useSSL=false&amp;serverTimezone=UTC"/>
<property name="jakarta.persistence.jdbc.user" value="root"/>
<property name="jakarta.persistence.jdbc.password" value="tu_password"/>
<property name="hibernate.dialect" value="org.hibernate.dialect.MySQLDialect"/>
```

**Después (PostgreSQL)**:
```xml
<property name="jakarta.persistence.jdbc.driver" value="org.postgresql.Driver"/>
<property name="jakarta.persistence.jdbc.url" value="jdbc:postgresql://localhost:5432/gestion_mascotas"/>
<property name="jakarta.persistence.jdbc.user" value="postgres"/>
<property name="jakarta.persistence.jdbc.password" value="tu_password"/>
<property name="hibernate.dialect" value="org.hibernate.dialect.PostgreSQLDialect"/>
```

---

### 3. Configuración Hibernate (hibernate.cfg.xml)

**Antes (MySQL)**:
```xml
<property name="hibernate.connection.driver_class">com.mysql.cj.jdbc.Driver</property>
<property name="hibernate.connection.url">jdbc:mysql://localhost:3306/gestion_mascotas?useSSL=false&amp;serverTimezone=UTC</property>
<property name="hibernate.connection.username">root</property>
<property name="hibernate.connection.password">tu_password</property>
<property name="hibernate.dialect">org.hibernate.dialect.MySQLDialect</property>
```

**Después (PostgreSQL)**:
```xml
<property name="hibernate.connection.driver_class">org.postgresql.Driver</property>
<property name="hibernate.connection.url">jdbc:postgresql://localhost:5432/gestion_mascotas</property>
<property name="hibernate.connection.username">postgres</property>
<property name="hibernate.connection.password">tu_password</property>
<property name="hibernate.dialect">org.hibernate.dialect.PostgreSQLDialect</property>
```

---

### 4. Script SQL de Base de Datos

Se creó un nuevo archivo **`gestion_mascotas_postgres.sql`** con sintaxis de PostgreSQL.

**Diferencias principales**:

| Aspecto | MySQL | PostgreSQL |
|---------|-------|------------|
| Auto-increment | `AUTO_INCREMENT` | `BIGSERIAL` |
| Motor de BD | `ENGINE=InnoDB` | No requerido |
| Tipos de datos | `BIGINT`, `VARCHAR` | `BIGINT`, `VARCHAR` (similar) |
| Verificar tablas | `SHOW TABLES;` | `\dt` |
| Describir tabla | `DESCRIBE tabla;` | `\d tabla` |
| Comentarios | `COMMENT=''` | `COMMENT ON TABLE` |

**Ejemplo de tabla en PostgreSQL**:
```sql
CREATE TABLE IF NOT EXISTS usuarios (
    id BIGSERIAL PRIMARY KEY,
    nombre_usuario VARCHAR(50) NOT NULL UNIQUE,
    nombre VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    telefono VARCHAR(20),
    contrasena VARCHAR(255) NOT NULL
);
```

---

## 🚀 Pasos para Migrar

### Opción A: Nueva Instalación (Recomendado)

1. **Instalar PostgreSQL**
   - Descargar de: https://www.postgresql.org/download/
   - Versión mínima: 14+
   - Puerto por defecto: 5432

2. **Crear la base de datos**
   ```bash
   # Conectarse a PostgreSQL
   psql -U postgres
   
   # Crear base de datos
   CREATE DATABASE gestion_mascotas;
   
   # Salir
   \q
   ```

3. **Ejecutar script de creación**
   ```bash
   psql -U postgres -d gestion_mascotas -f gestion_mascotas_postgres.sql
   ```

4. **Actualizar credenciales**
   - Editar `src/main/resources/persistence.xml`
   - Editar `src/main/resources/hibernate.cfg.xml`

5. **Recompilar el proyecto**
   ```bash
   mvn clean package
   ```

---

### Opción B: Migrar Datos Existentes de MySQL

Si ya tienes datos en MySQL y quieres migrarlos a PostgreSQL:

#### 1. Exportar Datos de MySQL

```bash
# Exportar solo datos (sin estructura)
mysqldump -u root -p --no-create-info --complete-insert gestion_mascotas > datos_mysql.sql
```

#### 2. Crear Estructura en PostgreSQL

```bash
psql -U postgres -d gestion_mascotas -f gestion_mascotas_postgres.sql
```

#### 3. Adaptar el SQL Exportado

Editar `datos_mysql.sql` y hacer estos cambios:

**Cambios necesarios**:
- Cambiar comillas invertidas por comillas dobles: `` `tabla` `` → `"tabla"`
- Cambiar `0` y `1` por `FALSE` y `TRUE` para booleanos
- Revisar fechas: MySQL puede usar `0000-00-00`, PostgreSQL no lo acepta

**Ejemplo de sed (Linux/Mac)**:
```bash
sed "s/\`/\"/g" datos_mysql.sql > datos_postgres.sql
```

#### 4. Importar a PostgreSQL

```bash
psql -U postgres -d gestion_mascotas -f datos_postgres.sql
```

---

## 🔍 Verificación Post-Migración

### 1. Verificar Conexión

```bash
psql -U postgres -d gestion_mascotas
```

### 2. Verificar Tablas

```sql
-- Listar tablas
\dt

-- Ver estructura
\d usuarios
\d mascotas
\d vacunas
\d visitas
```

### 3. Verificar Datos (si migró datos)

```sql
-- Contar registros
SELECT COUNT(*) FROM usuarios;
SELECT COUNT(*) FROM mascotas;
SELECT COUNT(*) FROM vacunas;
SELECT COUNT(*) FROM visitas;
```

### 4. Compilar y Probar

```bash
# Compilar
mvn clean package

# Desplegar en servidor
# ... según tu servidor

# Probar login/registro
```

---

## 🐛 Solución de Problemas Comunes

### Error: "FATAL: password authentication failed"

**Solución**:
1. Editar `pg_hba.conf` (ubicación varía según SO)
2. Cambiar método de autenticación:
   ```
   # IPv4 local connections:
   host    all             all             127.0.0.1/32            md5
   ```
3. Reiniciar PostgreSQL:
   ```bash
   # Windows
   net stop postgresql-x64-14
   net start postgresql-x64-14
   
   # Linux
   sudo systemctl restart postgresql
   ```

### Error: "Connection refused"

**Solución**:
1. Verificar que PostgreSQL está ejecutándose:
   ```bash
   # Windows
   sc query postgresql-x64-14
   
   # Linux
   sudo systemctl status postgresql
   ```

2. Verificar puerto:
   ```bash
   psql -U postgres -p 5432
   ```

### Error: "database does not exist"

**Solución**:
```bash
psql -U postgres
CREATE DATABASE gestion_mascotas;
\q
```

### Error: "Driver not found"

**Solución**:
1. Limpiar proyecto: `mvn clean`
2. Verificar dependencia en pom.xml
3. Recompilar: `mvn compile`

---

## 📊 Comparación de Características

| Característica | MySQL | PostgreSQL | Notas |
|----------------|-------|------------|-------|
| ACID Compliant | ✅ | ✅ | Ambos son ACID compliant |
| Licencia | GPL/Commercial | PostgreSQL License | PostgreSQL más permisivo |
| Rendimiento en lectura | Excelente | Muy Bueno | Similar para este proyecto |
| Rendimiento en escritura | Muy Bueno | Excelente | PostgreSQL mejor en concurrencia |
| Tipos de datos | Extenso | Muy Extenso | PostgreSQL tiene más tipos (JSON, Arrays, etc.) |
| Full Text Search | Básico | Avanzado | PostgreSQL superior |
| Extensibilidad | Limitada | Excelente | PostgreSQL permite extensiones |
| Conformidad SQL | Buena | Excelente | PostgreSQL más estricto con estándares |

---

## 🎯 Ventajas de PostgreSQL para Este Proyecto

1. **Mejor conformidad con estándares SQL**
2. **Mayor extensibilidad** (útil para futuras mejoras)
3. **Mejor manejo de concurrencia** (MVCC)
4. **Tipos de datos avanzados** (JSON, Arrays)
5. **Licencia más permisiva**
6. **Excelente para aplicaciones empresariales**

---

## 📝 Comandos Útiles PostgreSQL

### Conexión
```bash
# Conectarse
psql -U postgres -d gestion_mascotas

# Conectarse a otra base de datos
\c otra_base_datos
```

### Consultas de Sistema
```sql
-- Listar bases de datos
\l

-- Listar tablas
\dt

-- Describir tabla
\d nombre_tabla

-- Ver índices
\di

-- Ver secuencias
\ds

-- Listar usuarios
\du

-- Ver tamaño de base de datos
\l+

-- Salir
\q
```

### Mantenimiento
```sql
-- Analizar tabla (actualizar estadísticas)
ANALYZE usuarios;

-- Vacuum (limpiar)
VACUUM usuarios;

-- Reindexar
REINDEX TABLE usuarios;
```

---

## ✅ Checklist de Migración

- [ ] PostgreSQL instalado y ejecutándose
- [ ] Base de datos `gestion_mascotas` creada
- [ ] Script `gestion_mascotas_postgres.sql` ejecutado
- [ ] Tablas creadas correctamente
- [ ] Dependencia PostgreSQL en `pom.xml`
- [ ] `persistence.xml` actualizado
- [ ] `hibernate.cfg.xml` actualizado
- [ ] Credenciales configuradas
- [ ] Proyecto compila sin errores
- [ ] Datos migrados (si aplica)
- [ ] Aplicación desplegada y probada
- [ ] Login funciona correctamente
- [ ] CRUD de mascotas funciona
- [ ] CRUD de vacunas funciona
- [ ] CRUD de visitas funciona

---

## 🔗 Referencias

- [Documentación PostgreSQL](https://www.postgresql.org/docs/)
- [Hibernate PostgreSQL Dialect](https://docs.jboss.org/hibernate/orm/6.4/javadocs/org/hibernate/dialect/PostgreSQLDialect.html)
- [PostgreSQL JDBC Driver](https://jdbc.postgresql.org/)
- [Migración MySQL a PostgreSQL](https://www.postgresql.org/docs/current/migration.html)

---

**Fecha de migración**: 4 de octubre de 2025  
**Versión**: 1.0-SNAPSHOT  
**Estado**: ✅ Completado
