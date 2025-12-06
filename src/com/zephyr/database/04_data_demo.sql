-- =====================================================
-- SCRIPT 4: CARGA DE DATOS DE PRUEBA (MASSIVE DEMO V8)
-- =====================================================
-- Proposito: Llenar el sistema con datos transaccionales
-- para demostracion xd
-- =====================================================

TRUNCATE Pasajero, Vuelo, Boleto, Equipaje, AsignacionPersonal RESTART IDENTITY CASCADE;

-- -----------------------------------------------------
-- 1. GENERAR 400 PASAJEROS (Truco Matemático SQL)
-- -----------------------------------------------------
INSERT INTO Pasajero (nombres, apellidos, numero_documento, nacionalidad, correo, id_tipo_documento)
SELECT
    n.nombre, a.apellido,
    'DNI-' || floor(random() * 1000000)::text || '-' || n.id || a.id,
    'Varias',
    lower(n.nombre) || '.' || lower(a.apellido) || '@email.com',
    1
FROM
    (VALUES (1, 'James'), (2, 'John'), (3, 'Robert'), (4, 'Michael'), (5, 'William'), (6, 'David'), (7, 'Richard'), (8, 'Joseph'), (9, 'Thomas'), (10, 'Charles'), (11, 'Mary'), (12, 'Patricia'), (13, 'Jennifer'), (14, 'Linda'), (15, 'Elizabeth'), (16, 'Barbara'), (17, 'Susan'), (18, 'Jessica'), (19, 'Sarah'), (20, 'Karen')) AS n(id, nombre)
        CROSS JOIN
    (VALUES (1, 'Smith'), (2, 'Johnson'), (3, 'Williams'), (4, 'Brown'), (5, 'Jones'), (6, 'Garcia'), (7, 'Miller'), (8, 'Davis'), (9, 'Rodriguez'), (10, 'Martinez'), (11, 'Hernandez'), (12, 'Lopez'), (13, 'Gonzalez'), (14, 'Wilson'), (15, 'Anderson'), (16, 'Thomas'), (17, 'Taylor'), (18, 'Moore'), (19, 'Jackson'), (20, 'Martin')) AS a(id, apellido);


-- -----------------------------------------------------
-- 2. GENERAR VUELOS (Pasado, Presente, Futuro)
-- -----------------------------------------------------

-- A. Vuelos para HOY (Dashboard)
INSERT INTO Vuelo (codigo_vuelo, fecha_salida, fecha_llegada, id_aerolinea, id_avion, id_aeropuerto_origen, id_aeropuerto_destino, id_estado_vuelo) VALUES
    ('AA100', NOW() + interval '1 hour', NOW() + interval '5 hours', 1, 1, 1, 2, 1),
    ('DL200', NOW() + interval '1.5 hours', NOW() + interval '6 hours', 2, 2, 3, 4, 1),
    ('UA300', NOW() + interval '2 hours', NOW() + interval '7 hours', 3, 3, 5, 6, 1),
    ('WN400', NOW() + interval '2.5 hours', NOW() + interval '6 hours', 4, 4, 4, 3, 1),
    ('NK500', NOW() + interval '3 hours', NOW() + interval '8 hours', 5, 5, 1, 6, 1),
    ('CM600', NOW() + interval '3.5 hours', NOW() + interval '7 hours', 6, 6, 2, 1, 2), -- Embarcando
    ('AA101', NOW() + interval '4 hours', NOW() + interval '9 hours', 1, 6, 5, 1, 1),
    ('DL201', NOW() + interval '4.5 hours', NOW() + interval '8 hours', 2, 7, 1, 4, 1),
    ('UA301', NOW() + interval '5 hours', NOW() + interval '10 hours', 3, 3, 2, 3, 1),
    ('WN401', NOW() + interval '5.5 hours', NOW() + interval '9 hours', 4, 4, 5, 3, 1),
    ('NK501', NOW() + interval '6 hours', NOW() + interval '11 hours', 5, 5, 3, 1, 1),
    ('CM601', NOW() + interval '6.5 hours', NOW() + interval '12 hours', 6, 6, 6, 2, 1),
    ('AA102', NOW() + interval '7 hours', NOW() + interval '12 hours', 1, 1, 1, 2, 1),
    ('DL202', NOW() + interval '7.5 hours', NOW() + interval '11 hours', 2, 2, 3, 4, 1),
    ('UA302', NOW() + interval '8 hours', NOW() + interval '13 hours', 3, 3, 5, 6, 1),
    ('WN402', NOW() + interval '8.5 hours', NOW() + interval '12 hours', 4, 4, 4, 3, 1),
    ('NK502', NOW() + interval '9 hours', NOW() + interval '14 hours', 5, 5, 1, 6, 1),
    ('CM602', NOW() + interval '9.5 hours', NOW() + interval '15 hours', 6, 6, 2, 1, 5), -- Retrasado
    ('AA103', NOW() + interval '10 hours', NOW() + interval '16 hours', 1, 6, 5, 1, 1),
    ('DL203', NOW() + interval '10.5 hours', NOW() + interval '14 hours', 2, 7, 1, 4, 6); -- Cancelado

-- B. Vuelos FUTUROS y PASADOS (Relleno)
INSERT INTO Vuelo (codigo_vuelo, fecha_salida, fecha_llegada, id_aerolinea, id_avion, id_aeropuerto_origen, id_aeropuerto_destino, id_estado_vuelo) VALUES
    ('FUTURE1', NOW() + interval '2 days', NOW() + interval '2 days 4h', 1, 1, 1, 2, 1),
    ('PAST1', NOW() - interval '1 day', NOW() - interval '1 day - 4h', 1, 1, 1, 2, 4);


-- -----------------------------------------------------
-- 3. BOLETOS MASIVOS
-- -----------------------------------------------------
INSERT INTO Boleto (codigo_boleto, fecha_emision, asiento, precio_total, id_pasajero, id_vuelo, id_clase_vuelo, id_estado_embarque)
SELECT
    'TKT-' || p.id_pasajero,
    NOW(),
    (p.id_pasajero % 30 + 1) || CASE WHEN p.id_pasajero % 6 = 0 THEN 'A' ELSE 'B' END,
    random() * 200 + 100,
    p.id_pasajero,
    (p.id_pasajero % 5) + 1,
    1,
    CASE WHEN p.id_pasajero % 8 = 0 THEN 1 ELSE 2 END
FROM Pasajero p
WHERE p.id_pasajero <= 400;


-- -----------------------------------------------------
-- 4. DATOS EXTRA
-- -----------------------------------------------------
INSERT INTO Equipaje (codigo_equipaje, peso, dimensiones, tipo, id_boleto) VALUES
        ('EQ1001', 22.5, '60x40x30', 'Bodega', 1), ('EQ1002', 21.0, '60x40x30', 'Bodega', 2);

INSERT INTO AsignacionPersonal (rol_en_asignacion, fecha_asignacion, id_personal, id_vuelo) VALUES
        ('Agente Principal', CURRENT_DATE, 2, 1), ('Supervisor', CURRENT_DATE, 3, 1);

SELECT 'Script 04: Datos de Prueba (V8) cargados exitosamente.' AS "Estado";