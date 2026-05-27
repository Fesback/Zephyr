-- =====================================================
-- SCRIPT 3: LÓGICA DE NEGOCIO (VISTAS Y SPs)
-- =====================================================


/*-----------------------------------------------------*/
-- A. LIMPIEZA PREVIA (Para evitar conflictos)
/*-----------------------------------------------------*/
DROP VIEW IF EXISTS v_vuelos_programados CASCADE;
DROP VIEW IF EXISTS v_pasajeros_por_vuelo CASCADE;
DROP VIEW IF EXISTS v_equipaje_por_vuelo CASCADE;
DROP VIEW IF EXISTS v_personal_asignado CASCADE;

DROP FUNCTION IF EXISTS sp_actualizar_estado_vuelo(INT, INT);
DROP FUNCTION IF EXISTS sp_verificar_pasajero_en_puerta(INT);
DROP FUNCTION IF EXISTS sp_asignar_puerta_a_vuelo(INT, INT);
DROP FUNCTION IF EXISTS sp_asignar_personal_a_vuelo(INT, INT, VARCHAR);


/*-----------------------------------------------------*/
-- B. VISTAS (VIEWS) - Lectura Optimizada
/*-----------------------------------------------------*/

-- -----------------------------------------------------
-- v . vuelos programados
-- -----------------------------------------------------
CREATE OR REPLACE VIEW v_vuelos_programados AS
SELECT
    v.id_vuelo,
    v.codigo_vuelo,
    v.fecha_salida,
    v.fecha_llegada,
    v.id_aerolinea,
    v.id_avion,
    v.id_aeropuerto_origen,
    v.id_aeropuerto_destino,
    v.id_puerta_asignada,
    v.id_estado_vuelo,

    a.nombre AS aerolinea,
    a.codigo_iata AS aerolinea_iata,
    av.matricula AS avion_matricula,
    ma.nombre_modelo AS avion_modelo,
    ma.capacidad_pasajeros,
    ao.nombre AS aeropuerto_origen,
    ao.codigo_iata AS origen_iata,
    co.nombre AS ciudad_origen,
    ad.nombre AS aeropuerto_destino,
    ad.codigo_iata AS destino_iata,
    cd.nombre AS ciudad_destino,
    pde.codigo_puerta,
    pde.ubicacion_terminal,
    ev.descripcion AS estado_vuelo

FROM Vuelo v
         LEFT JOIN Aerolinea a ON v.id_aerolinea = a.id_aerolinea
         LEFT JOIN EstadoVuelo ev ON v.id_estado_vuelo = ev.id_estado_vuelo
         LEFT JOIN Avion av ON v.id_avion = av.id_avion
         LEFT JOIN ModeloAvion ma ON av.id_modelo_avion = ma.id_modelo_avion
         LEFT JOIN PuertaDeEmbarque pde ON v.id_puerta_asignada = pde.id_puerta_embarque
         LEFT JOIN Aeropuerto ao ON v.id_aeropuerto_origen = ao.id_aeropuerto
         LEFT JOIN Ciudad co ON ao.id_ciudad = co.id_ciudad
         LEFT JOIN Aeropuerto ad ON v.id_aeropuerto_destino = ad.id_aeropuerto
         LEFT JOIN Ciudad cd ON ad.id_ciudad = cd.id_ciudad;


-- -----------------------------------------------------
-- v . pasajeros por vuelo
-- -----------------------------------------------------
CREATE OR REPLACE VIEW v_pasajeros_por_vuelo AS
SELECT
    b.id_vuelo,
    b.id_pasajero,
    p.nombres,
    p.apellidos,
    p.numero_documento,
    td.descripcion AS tipo_documento,
    b.id_boleto,
    b.codigo_boleto,
    b.asiento,
    cv.nombre AS clase_vuelo,
    ee.descripcion AS estado_embarque
FROM Boleto b
         JOIN Pasajero p ON b.id_pasajero = p.id_pasajero
         JOIN TipoDocumento td ON p.id_tipo_documento = td.id_tipo_documento
         JOIN ClaseVuelo cv ON b.id_clase_vuelo = cv.id_clase_vuelo
         JOIN EstadoEmbarque ee ON b.id_estado_embarque = ee.id_estado_embarque
ORDER BY p.apellidos, p.nombres;


-- -----------------------------------------------------
-- v. Equipaje por vuelo
-- -----------------------------------------------------
CREATE OR REPLACE VIEW v_equipaje_por_vuelo AS
SELECT
    b.id_vuelo,
    b.id_pasajero,
    e.id_equipaje,
    p.nombres AS pasajero_nombres,
    p.apellidos AS pasajero_apellidos,
    e.codigo_equipaje,
    e.peso,
    e.dimensiones,
    e.tipo AS tipo_equipaje,
    b.asiento
FROM Equipaje e
         JOIN Boleto b ON e.id_boleto = b.id_boleto
         JOIN Pasajero p ON b.id_pasajero = p.id_pasajero
ORDER BY b.id_vuelo, p.apellidos;


-- -----------------------------------------------------
-- v. asignar personal
-- -----------------------------------------------------
CREATE OR REPLACE VIEW v_personal_asignado AS
SELECT
    ap.id_asignacion,
    ap.fecha_asignacion,
    ap.rol_en_asignacion,
    p.id_personal,
    p.nombres AS personal_nombres,
    p.apellidos AS personal_apellidos,
    r.nombre_rol AS personal_rol_general,
    v.id_vuelo,
    v.codigo_vuelo,
    v.fecha_salida,
    a.nombre AS aerolinea,
    aer_dest.codigo_iata AS destino_iata
FROM AsignacionPersonal ap
         JOIN Personal p ON ap.id_personal = p.id_personal
         JOIN Rol r ON p.id_rol = r.id_rol
         JOIN Vuelo v ON ap.id_vuelo = v.id_vuelo
         JOIN Aerolinea a ON v.id_aerolinea = a.id_aerolinea
         JOIN Aeropuerto aer_dest ON v.id_aeropuerto_destino = aer_dest.id_aeropuerto
ORDER BY ap.fecha_asignacion, v.fecha_salida, p.apellidos;


/*-----------------------------------------------------*/
-- C. PROCEDIMIENTOS ALMACENADOS (STORED PROCEDURES)
/*-----------------------------------------------------*/

-- -----------------------------------------------------
-- SP actualizar estado del vuelo
-- -----------------------------------------------------
CREATE OR REPLACE FUNCTION sp_actualizar_estado_vuelo(
    p_id_vuelo INT,
    p_nuevo_estado_id INT
)
RETURNS VARCHAR AS $$
BEGIN
UPDATE Vuelo
SET id_estado_vuelo = p_nuevo_estado_id
WHERE id_vuelo = p_id_vuelo;

RETURN 'ÉXITO';
EXCEPTION
    WHEN others THEN RETURN 'ERROR: No se pudo actualizar el estado.';
END;
$$ LANGUAGE plpgsql;


-- -----------------------------------------------------
-- SP verificar pasajero
-- -----------------------------------------------------
CREATE OR REPLACE FUNCTION sp_verificar_pasajero_en_puerta(
    p_id_boleto INT
)
RETURNS VARCHAR AS $$
DECLARE
v_actual INT;
    v_verificado INT;
    v_abordo INT;
BEGIN
SELECT id_estado_embarque INTO v_verificado FROM EstadoEmbarque WHERE descripcion = 'Verificado en Puerta';
SELECT id_estado_embarque INTO v_abordo FROM EstadoEmbarque WHERE descripcion = 'Abordo';

SELECT id_estado_embarque INTO v_actual FROM Boleto WHERE id_boleto = p_id_boleto;

IF NOT FOUND THEN
        RETURN 'ERROR: El boleto no existe.';
END IF;

    IF v_actual = v_verificado THEN RETURN 'ADVERTENCIA: Pasajero ya verificado.'; END IF;
    IF v_actual = v_abordo THEN RETURN 'ADVERTENCIA: Pasajero ya abordo.'; END IF;

UPDATE Boleto SET id_estado_embarque = v_verificado WHERE id_boleto = p_id_boleto;

RETURN 'ÉXITO: Verificado correctamente.';

EXCEPTION
    WHEN others THEN RETURN 'ERROR: Fallo inesperado.';
END;
$$ LANGUAGE plpgsql;


-- -----------------------------------------------------
-- SP asignar puerta a vuelo
-- -----------------------------------------------------
CREATE OR REPLACE FUNCTION sp_asignar_puerta_a_vuelo(
    p_id_vuelo INT,
    p_id_puerta INT
)
RETURNS VARCHAR AS $$
DECLARE
v_estado VARCHAR(20);
    v_existe INT;
BEGIN
SELECT 1 INTO v_existe FROM Vuelo WHERE id_vuelo = p_id_vuelo;
IF NOT FOUND THEN RETURN 'ERROR: Vuelo no existe.'; END IF;

SELECT estado_puerta INTO v_estado FROM PuertaDeEmbarque WHERE id_puerta_embarque = p_id_puerta;
IF NOT FOUND THEN RETURN 'ERROR: Puerta no existe.'; END IF;

    IF COALESCE(v_estado, 'Disponible') <> 'Disponible' THEN
        RETURN 'ERROR: Puerta ocupada o en mantenimiento.';
END IF;

UPDATE Vuelo SET id_puerta_asignada = p_id_puerta WHERE id_vuelo = p_id_vuelo;
UPDATE PuertaDeEmbarque SET estado_puerta = 'Operando' WHERE id_puerta_embarque = p_id_puerta;

RETURN 'EXITO';

EXCEPTION
    WHEN OTHERS THEN RETURN 'ERROR SQL: ' || SQLERRM;
END;
$$ LANGUAGE plpgsql;

-- -----------------------------------------------------
-- SP 4 Asignar Personal
-- -----------------------------------------------------
CREATE OR REPLACE FUNCTION sp_asignar_personal_a_vuelo(
    p_id_personal INT,
    p_id_vuelo INT,
    p_rol_asignado VARCHAR(50)
)
RETURNS VARCHAR AS $$
BEGIN
INSERT INTO AsignacionPersonal (id_personal, id_vuelo, rol_en_asignacion, fecha_asignacion)
VALUES (p_id_personal, p_id_vuelo, p_rol_asignado, CURRENT_DATE);

RETURN 'ÉXITO';

EXCEPTION
    WHEN unique_violation THEN RETURN 'ERROR: Personal ya asignado.';
WHEN foreign_key_violation THEN RETURN 'ERROR: ID inválido.';
WHEN others THEN RETURN 'ERROR: Fallo inesperado.';
END;
$$ LANGUAGE plpgsql;

SELECT 'Script 03: Lógica de Negocio (Vistas y SPs) creada correctamente.' AS "Estado";



CREATE OR REPLACE PROCEDURE simular_vuelos_del_dia()
LANGUAGE plpgsql
AS $$
BEGIN
    -- Limpiamos las tablas transaccionales viejas para no acumular basura
    TRUNCATE Pasajero, Vuelo, Boleto, Equipaje, AsignacionPersonal RESTART IDENTITY CASCADE;

    -- 1. Generamos los 400 pasajeros dinámicos
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

    -- 2. Insertamos los vuelos para el momento EXACTO actual (2026)
    INSERT INTO Vuelo (codigo_vuelo, fecha_salida, fecha_llegada, id_aerolinea, id_avion, id_aeropuerto_origen, id_aeropuerto_destino, id_estado_vuelo) VALUES
        ('AA100', NOW() + interval '1 hour', NOW() + interval '5 hours', 1, 1, 1, 2, 1),
        ('DL200', NOW() + interval '1.5 hours', NOW() + interval '6 hours', 2, 2, 3, 4, 1),
        ('UA300', NOW() + interval '2 hours', NOW() + interval '7 hours', 3, 3, 5, 6, 1),
        ('WN400', NOW() + interval '2.5 hours', NOW() + interval '6 hours', 4, 4, 4, 3, 1),
        ('NK500', NOW() + interval '3 hours', NOW() + interval '8 hours', 5, 5, 1, 6, 1),
        ('CM600', NOW() + interval '3.5 hours', NOW() + interval '7 hours', 6, 6, 2, 1, 2),
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
        ('CM602', NOW() + interval '9.5 hours', NOW() + interval '15 hours', 6, 6, 2, 1, 5),
        ('AA103', NOW() + interval '10 hours', NOW() + interval '16 hours', 1, 6, 5, 1, 1),
        ('DL203', NOW() + interval '10.5 hours', NOW() + interval '14 hours', 2, 7, 1, 4, 6),
        ('FUTURE1', NOW() + interval '2 days', NOW() + interval '2 days 4h', 1, 1, 1, 2, 1),
        ('PAST1', NOW() - interval '1 day', NOW() - interval '1 day - 4h', 1, 1, 1, 2, 4);

    -- 3. Boletos Masivos asignados a los nuevos IDs de vuelo
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

    -- 4. Datos extra
    INSERT INTO Equipaje (codigo_equipaje, peso, dimensions, tipo, id_boleto) VALUES -- Ojo: verifica si en tu tabla es 'dimensiones' o 'dimensions'
        ('EQ1001', 22.5, '60x40x30', 'Bodega', 1),
        ('EQ1002', 21.0, '60x40x30', 'Bodega', 2);

    INSERT INTO AsignacionPersonal (rol_en_asignacion, fecha_asignacion, id_personal, id_vuelo) VALUES
        ('Agente Principal', CURRENT_DATE, 2, 1),
        ('Supervisor', CURRENT_DATE, 3, 1);
END;
$$;