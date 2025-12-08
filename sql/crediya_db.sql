CREATE DATABASE IF NOT EXISTS crediya_db;
USE crediya_db;

CREATE TABLE IF NOT EXISTS empleados (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(80) NOT NULL,
    documento VARCHAR(30) NOT NULL,
    rol VARCHAR(50) NOT NULL,
    correo VARCHAR(80),
    salario DECIMAL(10,2) NOT NULL
);

CREATE TABLE IF NOT EXISTS clientes (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(80) NOT NULL,
    documento VARCHAR(30) NOT NULL,
    correo VARCHAR(80),
    telefono VARCHAR(20)
);

CREATE TABLE IF NOT EXISTS prestamos (
    id INT AUTO_INCREMENT PRIMARY KEY,
    cliente_id INT NOT NULL,
    empleado_id INT NOT NULL,
    monto DECIMAL(12,2) NOT NULL,
    interes DECIMAL(5,2) NOT NULL,
    cuotas INT NOT NULL,
    fecha_inicio DATE NOT NULL,
    estado VARCHAR(20) NOT NULL,
    FOREIGN KEY (cliente_id) REFERENCES clientes(id),
    FOREIGN KEY (empleado_id) REFERENCES empleados(id)
);

CREATE TABLE IF NOT EXISTS pagos (
    id INT AUTO_INCREMENT PRIMARY KEY,
    prestamo_id INT NOT NULL,
    fecha_pago DATE NOT NULL,
    monto DECIMAL(10,2) NOT NULL,
    FOREIGN KEY (prestamo_id) REFERENCES prestamos(id)
);


CREATE VIEW prestamos_pendientes AS
SELECT p.*, c.nombre AS cliente_nombre
FROM prestamos p
INNER JOIN clientes c ON p.cliente_id = c.id
WHERE p.estado = 'PENDIENTE';


CREATE INDEX idx_prestamos_cliente ON prestamos(cliente_id);
CREATE INDEX idx_prestamos_empleado ON prestamos(empleado_id);
CREATE INDEX idx_pagos_prestamo ON pagos(prestamo_id);
