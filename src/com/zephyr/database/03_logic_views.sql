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