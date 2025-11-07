/*----------------------------------*/
-- Vuelos Programados VW
/*---------------------------------*/

create or replace view v_vuelos_programados as
select

	v.id_vuelo,
	v.codigo_vuelo,
	v.fecha_salida,
	v.fecha_llegada,
	
	a.nombre as aerolinea,
	a.codigo_iata as aerolinea_iata,
	
	av.matricula as avion_matricula,
	ma.nombre_modelo as avion_modelo,
	ma.capacidad_pasajeros,
	
	ao.nombre as aeropuerto_origen,
	ao.codigo_iata as origen_iata,
	co.nombre as ciudad_origen,
	
	ad.nombre as aeropuerto_destino,
	ad.codigo_iata as destino_iata,
	cd.nombre as ciudad_destino,
	
	pde.codigo_puerta,
	pde.ubicacion_terminal,
	
	ev.descripcion as estado_vuelo
	
from 
	Vuelo v
	
	join Aerolinea a on v.id_aerolinea = a.id_aerolinea
	JOIN EstadoVuelo ev ON v.id_estado_vuelo = ev.id_estado_vuelo
    JOIN Avion av ON v.id_avion = av.id_avion
    JOIN ModeloAvion ma ON av.id_modelo_avion = ma.id_modelo_avion
    
    
    LEFT JOIN PuertaDeEmbarque pde ON v.id_puerta_asignada = pde.id_puerta_embarque
    
    JOIN Aeropuerto ao ON v.id_aeropuerto_origen = ao.id_aeropuerto
    JOIN Ciudad co ON ao.id_ciudad = co.id_ciudad
    
    JOIN Aeropuerto ad ON v.id_aeropuerto_destino = ad.id_aeropuerto
    JOIN Ciudad cd ON ad.id_ciudad = cd.id_ciudad;
    
    
/*----------------------------------*/
-- Pasajeros por Vuelo VW
/*---------------------------------*/
    
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

FROM
    Boleto b
    JOIN Pasajero p ON b.id_pasajero = p.id_pasajero
    JOIN TipoDocumento td ON p.id_tipo_documento = td.id_tipo_documento
    JOIN ClaseVuelo cv ON b.id_clase_vuelo = cv.id_clase_vuelo
    JOIN EstadoEmbarque ee ON b.id_estado_embarque = ee.id_estado_embarque
ORDER BY
    p.apellidos, p.nombres;
    

/*----------------------------------*/
-- Equpaje por vuelo VW
/*---------------------------------*/

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

FROM
    Equipaje e
    JOIN Boleto b ON e.id_boleto = b.id_boleto
    JOIN Pasajero p ON b.id_pasajero = p.id_pasajero
ORDER BY
    b.id_vuelo, p.apellidos;

/*----------------------------------*/
-- Asignacion de Personal VW
/*---------------------------------*/

CREATE OR REPLACE VIEW v_personal_asignado AS
SELECT
    
    ap.id_asignacion,
    ap.fecha_asignacion,
    ap.rol_en_asignacion,

    -- Información del Personal
    p.id_personal,
    p.nombres AS personal_nombres,
    p.apellidos AS personal_apellidos,
    
    -- LA LÍNEA CORREGIDA:
    r.nombre_rol AS personal_rol_general, -- Reemplaza a p.cargo

    -- Información del Vuelo (para referencia rápida)
    v.id_vuelo,
    v.codigo_vuelo,
    v.fecha_salida,
    
    -- Aerolínea y Destino
    a.nombre AS aerolinea,
    aer_dest.codigo_iata AS destino_iata

FROM
    AsignacionPersonal ap
    -- Uniones (JOINs)
    JOIN Personal p ON ap.id_personal = p.id_personal
    
    -- EL JOIN ADICIONAL:
    JOIN Rol r ON p.id_rol = r.id_rol -- Conectamos con la nueva tabla Rol
    
    JOIN Vuelo v ON ap.id_vuelo = v.id_vuelo
    JOIN Aerolinea a ON v.id_aerolinea = a.id_aerolinea
    JOIN Aeropuerto aer_dest ON v.id_aeropuerto_destino = aer_dest.id_aeropuerto
ORDER BY
    ap.fecha_asignacion, v.fecha_salida, p.apellidos;
    
    
    -- PROBANDO LAS VISTAS

    SELECT * FROM v_vuelos_programados;
    
    SELECT * FROM v_pasajeros_por_vuelo;
    
    SELECT * FROM v_equipaje_por_vuelo;
    
    SELECT * FROM v_personal_asignado;
    
    
    
    
    
/*-------------------------------------*/
-- CREACION PROCEDIMIENTOS ALMACENADOS
/*-------------------------------------*/
    
    
/*-------------------------------------*/
-- Actualizar estado de Vuelo SP
/*-------------------------------------*/
    
CREATE OR REPLACE FUNCTION sp_actualizar_estado_vuelo(
    p_id_vuelo INT,
    p_nuevo_estado_id INT
)
RETURNS VARCHAR AS $$
BEGIN
    
    UPDATE Vuelo
    SET id_estado_vuelo = p_nuevo_estado_id
    WHERE id_vuelo = p_id_vuelo;

    RETURN 'ÉXITO: Estado del vuelo ' || p_id_vuelo || ' actualizado.';
    
EXCEPTION
    WHEN others THEN
        RETURN 'ERROR: No se pudo actualizar el estado del vuelo.';
END;
$$ LANGUAGE plpgsql;
    

/*-------------------------------------*/
-- Verificar pasajero en puerta SP
/*-------------------------------------*/

CREATE OR REPLACE FUNCTION sp_verificar_pasajero_en_puerta(
    p_id_boleto INT
)
RETURNS VARCHAR AS $$
DECLARE
    v_estado_actual_id INT;
    v_id_estado_verificado INT;
    v_id_estado_abordo INT;
BEGIN
  
    SELECT id_estado_embarque INTO v_id_estado_verificado FROM EstadoEmbarque WHERE descripcion = 'Verificado en Puerta';
    SELECT id_estado_embarque INTO v_id_estado_abordo FROM EstadoEmbarque WHERE descripcion = 'Abordo';
    
    SELECT id_estado_embarque INTO v_estado_actual_id FROM Boleto
    WHERE id_boleto = p_id_boleto;

    IF NOT FOUND THEN
        RETURN 'ERROR: El boleto ' || p_id_boleto || ' no existe.';
    
    ELSIF v_estado_actual_id = v_id_estado_verificado THEN
        RETURN 'ADVERTENCIA: Pasajero ya fue verificado en puerta.';
        
    ELSIF v_estado_actual_id = v_id_estado_abordo THEN
        RETURN 'ADVERTENCIA: Pasajero ya se encuentra Abordo.';
        
    ELSE
        
        UPDATE Boleto
        SET id_estado_embarque = v_id_estado_verificado
        WHERE id_boleto = p_id_boleto;
        
        RETURN 'ÉXITO: Pasajero ' || p_id_boleto || ' verificado correctamente.';
    END IF;

EXCEPTION
    WHEN others THEN
        RETURN 'ERROR: Ocurrió un error inesperado al verificar el boleto.';
END;
$$ LANGUAGE plpgsql;
    
   
/*-------------------------------------*/
-- Asignar puerta a vuelo SP
/*-------------------------------------*/

CREATE OR REPLACE FUNCTION sp_asignar_puerta_a_vuelo(
    p_id_vuelo INT,
    p_id_puerta INT
)
RETURNS VARCHAR AS $$
DECLARE
    v_estado_puerta VARCHAR(50);
BEGIN

    SELECT estado_puerta INTO v_estado_puerta FROM PuertaDeEmbarque
    WHERE id_puerta_embarque = p_id_puerta;

    IF NOT FOUND THEN
        RETURN 'ERROR: La puerta ' || p_id_puerta || ' no existe.';
    END IF;

    IF v_estado_puerta = 'Operando' THEN
        RETURN 'ERROR: La puerta ' || p_id_puerta || ' ya está en uso.';
    END IF;
    
    IF v_estado_puerta = 'Mantenimiento' THEN
        RETURN 'ERROR: La puerta ' || p_id_puerta || ' está en mantenimiento.';
    END IF;

    UPDATE Vuelo
    SET id_puerta_asignada = p_id_puerta
    WHERE id_vuelo = p_id_vuelo;

    UPDATE PuertaDeEmbarque
    SET estado_puerta = 'Operando'
    WHERE id_puerta_embarque = p_id_puerta;

    RETURN 'ÉXITO: Puerta ' || p_id_puerta || ' asignada al vuelo ' || p_id_vuelo || '.';

EXCEPTION
    WHEN others THEN
        RETURN 'ERROR: Ocurrió un error inesperado al asignar la puerta.';
END;
$$ LANGUAGE plpgsql;

/*-------------------------------------*/
-- Asignar Personal al Vuelo SP
/*-------------------------------------*/
    
CREATE OR REPLACE FUNCTION sp_asignar_personal_a_vuelo(
    p_id_personal INT,
    p_id_vuelo INT,
    p_rol_asignado VARCHAR(50)
)
RETURNS VARCHAR AS $$
BEGIN
    INSERT INTO AsignacionPersonal (id_personal, id_vuelo, rol_en_asignacion, fecha_asignacion)
    VALUES (p_id_personal, p_id_vuelo, p_rol_asignado, CURRENT_DATE);

    RETURN 'ÉXITO: Personal ' || p_id_personal || ' asignado al vuelo ' || p_id_vuelo;

EXCEPTION

    WHEN unique_violation THEN
        RETURN 'ERROR: Esta persona ya está asignada a este vuelo.';
    WHEN foreign_key_violation THEN
        RETURN 'ERROR: El personal o el vuelo no existen.';
    WHEN others THEN
        RETURN 'ERROR: Ocurrió un error inesperado.';
END;
$$ LANGUAGE plpgsql;