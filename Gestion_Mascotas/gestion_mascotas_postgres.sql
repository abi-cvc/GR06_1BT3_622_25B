-- Script de Base de Datos para PostgreSQL
-- Sistema de Gestión de Mascotas
-- Versión: 1.0

-- Crear la base de datos (ejecutar como superusuario)
-- CREATE DATABASE gestion_mascotas;
-- \c gestion_mascotas;

-- Crear secuencias para IDs (opcional, PostgreSQL usa SERIAL automáticamente)

-- Tabla de Usuarios
CREATE TABLE IF NOT EXISTS usuarios (
    id BIGSERIAL PRIMARY KEY,
    nombre_usuario VARCHAR(50) NOT NULL UNIQUE,
    nombre VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    telefono VARCHAR(20),
    contrasena VARCHAR(255) NOT NULL,
    CONSTRAINT chk_email CHECK (email ~* '^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,}$')
);

-- Tabla de Mascotas
CREATE TABLE IF NOT EXISTS mascotas (
    id BIGSERIAL PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    tipo VARCHAR(20) NOT NULL,
    usuario_id BIGINT NOT NULL,
    CONSTRAINT fk_mascota_usuario FOREIGN KEY (usuario_id) 
        REFERENCES usuarios(id) ON DELETE CASCADE,
    CONSTRAINT chk_tipo CHECK (tipo IN ('PERRO', 'GATO'))
);

-- Tabla de Vacunas
CREATE TABLE IF NOT EXISTS vacunas (
    id BIGSERIAL PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    fecha DATE NOT NULL,
    mascota_id BIGINT NOT NULL,
    CONSTRAINT fk_vacuna_mascota FOREIGN KEY (mascota_id) 
        REFERENCES mascotas(id) ON DELETE CASCADE
);

-- Tabla de Visitas Veterinarias
CREATE TABLE IF NOT EXISTS visitas (
    id BIGSERIAL PRIMARY KEY,
    fecha DATE NOT NULL,
    motivo TEXT NOT NULL,
    mascota_id BIGINT NOT NULL,
    CONSTRAINT fk_visita_mascota FOREIGN KEY (mascota_id) 
        REFERENCES mascotas(id) ON DELETE CASCADE
);

-- Índices para mejorar el rendimiento
CREATE INDEX IF NOT EXISTS idx_mascota_usuario ON mascotas(usuario_id);
CREATE INDEX IF NOT EXISTS idx_vacuna_mascota ON vacunas(mascota_id);
CREATE INDEX IF NOT EXISTS idx_visita_mascota ON visitas(mascota_id);
CREATE INDEX IF NOT EXISTS idx_usuario_email ON usuarios(email);
CREATE INDEX IF NOT EXISTS idx_usuario_nombre ON usuarios(nombre_usuario);

-- Comentarios en las tablas
COMMENT ON TABLE usuarios IS 'Tabla de usuarios del sistema';
COMMENT ON TABLE mascotas IS 'Tabla de mascotas registradas';
COMMENT ON TABLE vacunas IS 'Tabla de vacunas aplicadas a las mascotas';
COMMENT ON TABLE visitas IS 'Tabla de visitas veterinarias';

-- Datos de prueba (opcional)
-- Descomentar para insertar datos de ejemplo

/*
-- Usuario de prueba
INSERT INTO usuarios (nombre_usuario, nombre, email, telefono, contrasena) 
VALUES ('admin', 'Administrador', 'admin@test.com', '0999999999', 'admin123');

-- Mascota de prueba
INSERT INTO mascotas (nombre, tipo, usuario_id) 
VALUES ('Max', 'PERRO', 1);

-- Vacuna de prueba
INSERT INTO vacunas (nombre, fecha, mascota_id) 
VALUES ('Rabia', CURRENT_DATE, 1);

-- Visita de prueba
INSERT INTO visitas (fecha, motivo, mascota_id) 
VALUES (CURRENT_DATE, 'Chequeo general', 1);
*/

-- Verificar las tablas creadas
SELECT table_name 
FROM information_schema.tables 
WHERE table_schema = 'public' 
ORDER BY table_name;
