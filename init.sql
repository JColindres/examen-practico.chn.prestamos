-- 1. Crear la base de datos si no existe
CREATE DATABASE IF NOT EXISTS prestamos;
USE prestamos;

-- 2. Tabla Usuario
CREATE TABLE Usuario (
    id_usuario INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    rol VARCHAR(50) NOT NULL
);

-- 3. Tabla Cliente
CREATE TABLE Cliente (
    id_cliente INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    apellido VARCHAR(100) NOT NULL,
    numero_identificacion VARCHAR(50) UNIQUE NOT NULL,
    fecha_nacimiento DATE NOT NULL,
    direccion VARCHAR(255),
    correo VARCHAR(150),
    telefono VARCHAR(50),
    fecha_creacion DATETIME DEFAULT CURRENT_TIMESTAMP
);

-- 4. Tabla Solicitud_Prestamo
CREATE TABLE Solicitud_Prestamo (
    id_solicitud INT AUTO_INCREMENT PRIMARY KEY,
    id_cliente INT NOT NULL,
    monto_solicitado DECIMAL(18,2) NOT NULL,
    plazo_meses INT NOT NULL,
    estado VARCHAR(20) DEFAULT 'PENDIENTE',
    fecha_solicitud DATETIME DEFAULT CURRENT_TIMESTAMP,
    fecha_resolucion DATETIME,
    comentario_resolucion VARCHAR(255),
    CONSTRAINT fk_solicitud_cliente FOREIGN KEY (id_cliente) 
        REFERENCES Cliente(id_cliente) ON DELETE CASCADE
);

-- 5. Tabla Prestamo
CREATE TABLE Prestamo (
    id_prestamo INT AUTO_INCREMENT PRIMARY KEY,
    id_cliente INT NOT NULL,
    id_solicitud INT NOT NULL,
    monto_aprobado DECIMAL(18,2) NOT NULL,
    tasa_interes DECIMAL(5,2) NOT NULL,
    saldo_pendiente DECIMAL(18,2) NOT NULL,
    estado_pago VARCHAR(20) DEFAULT 'AL_DIA',
    fecha_inicio DATETIME DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_prestamo_cliente FOREIGN KEY (id_cliente) 
        REFERENCES Cliente(id_cliente) ON DELETE CASCADE,
    CONSTRAINT fk_prestamo_solicitud FOREIGN KEY (id_solicitud) 
        REFERENCES Solicitud_Prestamo(id_solicitud) ON DELETE CASCADE
);

-- 6. Tabla Pago
CREATE TABLE Pago (
    id_pago INT AUTO_INCREMENT PRIMARY KEY,
    id_prestamo INT NOT NULL,
    monto_pago DECIMAL(18,2) NOT NULL,
    fecha_pago DATETIME DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_pago_prestamo FOREIGN KEY (id_prestamo) 
        REFERENCES Prestamo(id_prestamo) ON DELETE CASCADE
);