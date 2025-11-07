-- -----------------------------------------------------
-- SCRIPT 4: Inserción de Datos Transaccionales (V2 - IDs Corregidos)
-- -----------------------------------------------------

-- 1. Insertar Pasajeros
INSERT INTO Pasajero (nombres, apellidos, fecha_nacimiento, numero_documento, nacionalidad, correo, telefono, id_tipo_documento)
VALUES
('John', 'Doe', '1990-05-15', 'A12345678', 'Estadounidense', 'john.doe@email.com', '555-1234', 1),
('Jane', 'Smith', '1988-11-20', 'B87654321', 'Canadiense', 'jane.smith@email.com', '555-5678', 1),
('Robert', 'Brown', '1995-02-10', 'E45678901', 'Estadounidense', 'rob.brown@email.com', '555-9012', 2),
('Emily', 'Davis', '2001-07-30', 'P65432109', 'Mexicana', 'em.davis@email.com', '555-3456', 1);

-- 2. Insertar Vuelos
INSERT INTO Vuelo (codigo_vuelo, fecha_salida, fecha_llegada, id_aerolinea, id_avion, id_aeropuerto_origen, id_aeropuerto_destino, id_estado_vuelo)
VALUES
('AA100', '2025-11-06 08:30:00', '2025-11-06 11:45:00', 1, 1, 1, 2, 1),
('DL890', '2025-11-06 09:15:00', '2025-11-06 12:00:00', 2, 2, 4, 6, 1);

-- 3. Insertar Boletos
INSERT INTO Boleto (codigo_boleto, fecha_emision, asiento, precio_total, id_pasajero, id_vuelo, id_clase_vuelo, id_estado_embarque)
VALUES
('AXD456', NOW(), '22A', 350.00, 1, 1, 1, 2),
('AXD457', NOW(), '22B', 350.00, 2, 1, 1, 2),
('DLR789', NOW(), '10C', 950.00, 3, 2, 3, 1);

-- 4. Insertar Equipaje
INSERT INTO Equipaje (codigo_equipaje, peso, dimensiones, tipo, id_boleto)
VALUES
('AA123456', 22.5, '60x40x30', 'Bodega', 1),
('AA123457', 21.0, '60x40x30', 'Bodega', 2),
('AA123458', 10.0, '40x30x20', 'Mano', 2);

-- 5. Insertar AsignacionPersonal (IDs de Personal CORREGIDOS)
INSERT INTO AsignacionPersonal (rol_en_asignacion, fecha_asignacion, id_personal, id_vuelo)
VALUES
(
    'Agente de Puerta Principal', '2025-11-06',
    2,
    1
),
(
    'Supervisor de Operaciones', '2025-11-06',
    3,
    1
),
(
    'Agente de Puerta Principal', '2025-11-06',
    4,
    2
);

SELECT 'Script de inserción de prueba V2 (Consolidado) ejecutado exitosamente.' AS "Estado";