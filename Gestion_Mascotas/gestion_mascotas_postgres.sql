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

CREATE TABLE IF NOT EXISTS mascotas (
    id BIGSERIAL PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    tipo VARCHAR(20) NOT NULL,
    raza VARCHAR(50),
    edad INTEGER,
    peso DOUBLE PRECISION,
    color VARCHAR(30),
    usuario_id BIGINT NOT NULL,
    CONSTRAINT fk_mascota_usuario FOREIGN KEY (usuario_id)
    REFERENCES usuarios(id) ON DELETE CASCADE,
    CONSTRAINT chk_tipo CHECK (tipo IN ('PERRO', 'GATO'))
    );

-- Tabla de Vacunas (Corregida)
CREATE TABLE IF NOT EXISTS vacunas (
    id BIGSERIAL PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL, -- Tipo de vacuna
    fecha DATE NOT NULL, -- Fecha de aplicación
    nombre_veterinario VARCHAR(100), -- Añadido
    proxima_dosis DATE, -- Añadido
    mascota_id BIGINT NOT NULL,
    CONSTRAINT fk_vacuna_mascota FOREIGN KEY (mascota_id)
    REFERENCES mascotas(id) ON DELETE CASCADE
);

-- Tabla de Visitas Veterinarias
CREATE TABLE IF NOT EXISTS visitas (
    id BIGSERIAL PRIMARY KEY,
    fecha DATE NOT NULL,
    motivo TEXT NOT NULL,
    diagnostico TEXT,
    tratamiento TEXT,
    observaciones TEXT,
    nombre_veterinario VARCHAR(100),
    mascota_id BIGINT NOT NULL,
    CONSTRAINT fk_visita_mascota FOREIGN KEY (mascota_id)
    REFERENCES mascotas(id) ON DELETE CASCADE
    );

-- ========================================
-- TABLAS PARA HERENCIA (JOINED STRATEGY)
-- ========================================

-- Tabla Base de Recordatorios (basada en Recordatorio.java)
CREATE TABLE IF NOT EXISTS recordatorios (
    id BIGSERIAL PRIMARY KEY,
    descripcion VARCHAR(255) NOT NULL,
    fecha_hora_recordatorio TIMESTAMP,
    activo BOOLEAN NOT NULL DEFAULT TRUE,
    mascota_id BIGINT NOT NULL,
    CONSTRAINT fk_recordatorio_mascota FOREIGN KEY (mascota_id)
        REFERENCES mascotas(id) ON DELETE CASCADE
);
COMMENT ON TABLE recordatorios IS 'Tabla base común para todos los tipos de recordatorios';

-- Tabla de Recordatorios de Alimentación (basada en RecordatorioAlimentacion.java)
CREATE TABLE IF NOT EXISTS recordatorios_alimentacion (
    id BIGINT PRIMARY KEY,
    frecuencia VARCHAR(255) NOT NULL,
    horarios VARCHAR(500) NOT NULL,
    tipo_alimento VARCHAR(100),
    dias_semana VARCHAR(200),
    CONSTRAINT fk_recordatorio_alimentacion_base FOREIGN KEY (id)
        REFERENCES recordatorios(id) ON DELETE CASCADE
);
COMMENT ON TABLE recordatorios_alimentacion IS 'Detalles específicos para recordatorios de alimentación';

-- Tabla de Recordatorios de Paseo (basada en RecordatorioPaseo.java)
CREATE TABLE IF NOT EXISTS recordatorios_paseo (
    id BIGINT PRIMARY KEY,
    dias_semana VARCHAR(200),
    horarios VARCHAR(500) NOT NULL,
    duracion_minutos INTEGER,
    CONSTRAINT fk_recordatorio_paseo_base FOREIGN KEY (id)
        REFERENCES recordatorios(id) ON DELETE CASCADE
);
COMMENT ON TABLE recordatorios_paseo IS 'Detalles específicos para recordatorios de paseo';

-- Tabla Base de Sugerencias
CREATE TABLE IF NOT EXISTS sugerencias (
    id BIGSERIAL PRIMARY KEY,
    tipo_mascota VARCHAR(20) NOT NULL,
    raza VARCHAR(100),
    edad_min INTEGER,
    edad_max INTEGER,
    peso_min DOUBLE PRECISION,
    peso_max DOUBLE PRECISION,
    descripcion TEXT NOT NULL,
    fuente VARCHAR(255)
);
COMMENT ON TABLE sugerencias IS 'Tabla base para almacenar criterios y descripciones de sugerencias';

-- Tabla de Sugerencias de Alimentación
CREATE TABLE IF NOT EXISTS sugerencias_alimentacion (
    id BIGINT PRIMARY KEY,
    ipo_comida VARCHAR(100),
    frecuencia VARCHAR(100),
    calorias_recomendadas INTEGER,
    CONSTRAINT fk_sugerencia_alimentacion_base FOREIGN KEY (id)
        REFERENCES sugerencias(id) ON DELETE CASCADE
);
COMMENT ON TABLE sugerencias_alimentacion IS 'Detalles específicos para sugerencias de alimentación';

-- Tabla de Sugerencias de Ejercicio
CREATE TABLE IF NOT EXISTS sugerencias_ejercicio (
    id BIGINT PRIMARY KEY,
    tipo_ejercicio VARCHAR(100),
    duracion_minutos INTEGER,
    nivel_actividad VARCHAR(30),
    CONSTRAINT fk_sugerencia_ejercicio_base FOREIGN KEY (id)
        REFERENCES sugerencias(id) ON DELETE CASCADE
);
COMMENT ON TABLE sugerencias_ejercicio IS 'Detalles específicos para sugerencias de ejercicio';

-- ========================================
-- ÍNDICES (Opcional pero recomendado para rendimiento)
-- ========================================
CREATE INDEX IF NOT EXISTS idx_mascota_usuario ON mascotas(usuario_id);
CREATE INDEX IF NOT EXISTS idx_vacuna_mascota ON vacunas(mascota_id);
CREATE INDEX IF NOT EXISTS idx_visita_mascota ON visitas(mascota_id);
CREATE INDEX IF NOT EXISTS idx_recordatorio_mascota ON recordatorios(mascota_id);
CREATE INDEX IF NOT EXISTS idx_usuario_email ON usuarios(email);
CREATE INDEX IF NOT EXISTS idx_usuario_nombre ON usuarios(nombre_usuario);
CREATE INDEX IF NOT EXISTS idx_sugerencia_tipo_raza ON sugerencias(tipo_mascota, lower(raza));

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
