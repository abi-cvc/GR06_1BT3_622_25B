-- ========================================
-- SCRIPT PARA CORREGIR ENCODING UTF-8
-- Base de Datos: gestion_mascotas
-- ========================================

-- 1. VERIFICAR ENCODING ACTUAL DE LA BASE DE DATOS
-- Ejecuta esto primero para ver qué encoding tiene
SELECT 
    datname AS base_datos,
    pg_encoding_to_char(encoding) AS codificacion,
    datcollate AS collate,
    datctype AS ctype
FROM pg_database 
WHERE datname = 'gestion_mascotas';

-- ========================================
-- SI LA CODIFICACIÓN NO ES UTF8, SIGUE ESTOS PASOS:
-- ========================================

-- 2. TERMINAR TODAS LAS CONEXIONES ACTIVAS A LA BASE DE DATOS
-- ⚠️ IMPORTANTE: Cierra el servidor Tomcat antes de ejecutar esto
SELECT pg_terminate_backend(pg_stat_activity.pid)
FROM pg_stat_activity
WHERE pg_stat_activity.datname = 'gestion_mascotas'
  AND pid <> pg_backend_pid();

-- 3. RECREAR LA BASE DE DATOS CON UTF8
-- ⚠️ IMPORTANTE: Esto borrará todos los datos
-- Haz un backup primero si tienes datos importantes

-- Opción A: DESDE CERO (Si no tienes datos importantes)
DROP DATABASE IF EXISTS gestion_mascotas;
CREATE DATABASE gestion_mascotas
    WITH 
    OWNER = postgres
    ENCODING = 'UTF8'
    LC_COLLATE = 'Spanish_Ecuador.1252'
    LC_CTYPE = 'Spanish_Ecuador.1252'
    TABLESPACE = pg_default
    CONNECTION LIMIT = -1;

-- Opción B: BACKUP Y RESTORE (Si tienes datos importantes)
-- Primero haz backup desde terminal:
-- pg_dump -U postgres -d gestion_mascotas -f backup_mascotas.sql
-- 
-- Luego drop y create:
-- DROP DATABASE gestion_mascotas;
-- CREATE DATABASE gestion_mascotas WITH ENCODING 'UTF8' LC_COLLATE = 'Spanish_Ecuador.1252' LC_CTYPE = 'Spanish_Ecuador.1252';
--
-- Finalmente restaura:
-- psql -U postgres -d gestion_mascotas -f backup_mascotas.sql

-- ========================================
-- 4. VERIFICAR QUE AHORA SEA UTF8
-- ========================================
SELECT 
    datname AS base_datos,
    pg_encoding_to_char(encoding) AS codificacion,
    datcollate AS collate,
    datctype AS ctype
FROM pg_database 
WHERE datname = 'gestion_mascotas';

-- El resultado debe mostrar:
-- base_datos        | codificacion | collate                   | ctype
-- gestion_mascotas  | UTF8         | Spanish_Ecuador.1252      | Spanish_Ecuador.1252

-- ========================================
-- 5. VERIFICAR ENCODING DE LAS TABLAS
-- ========================================
-- Conecta a la base de datos gestion_mascotas primero:
-- \c gestion_mascotas

-- Luego verifica las tablas:
SELECT 
    table_schema,
    table_name,
    column_name,
    data_type,
    character_maximum_length,
    character_set_name
FROM information_schema.columns
WHERE table_schema = 'public'
  AND table_name IN ('usuarios', 'mascotas', 'vacunas', 'visitas_veterinarias')
  AND data_type LIKE '%char%'
ORDER BY table_name, ordinal_position;

-- ========================================
-- 6. INSERTAR DATOS DE PRUEBA CON TILDES
-- ========================================
-- Una vez que Hibernate cree las tablas automáticamente,
-- prueba insertar datos con tildes y ñ:

-- Ejemplo de usuario con tildes:
-- INSERT INTO usuarios (nombre_usuario, nombre, email, telefono, contrasena)
-- VALUES ('prueba', 'José Martínez', 'jose@email.com', '0999999999', 'password123');

-- Ejemplo de mascota con ñ:
-- INSERT INTO mascotas (nombre, tipo, raza, fecha_nacimiento, color, peso, observaciones, usuario_id)
-- VALUES ('Toño', 'PERRO', 'Pastor Alemán', '2020-01-15', 'Marrón', 25.5, 'Muy cariñoso', 1);

-- ========================================
-- 7. VERIFICAR QUE SE GUARDARON CORRECTAMENTE
-- ========================================
-- SELECT * FROM usuarios;
-- SELECT * FROM mascotas;

-- Si ves los caracteres correctamente (José, Martínez, Toño, Alemán, Marrón)
-- entonces el problema está resuelto ✅
