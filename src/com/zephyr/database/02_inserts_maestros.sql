-- =====================================================
-- SCRIPT 2: INSERCIÓN DE DATOS MAESTROS (CATÁLOGOS)
-- =====================================================

SET session_replication_role = 'replica';

-- -----------------------------------------------------
-- 1. GEOGRAFÍA (Países y Ciudades)
-- -----------------------------------------------------

INSERT INTO Pais (nombre, codigo_iso) VALUES
        ('Estados Unidos', 'USA'),
        ('Canadá', 'CAN'),
        ('México', 'MEX'),
        ('Reino Unido', 'GBR'),
        ('Japón', 'JPN');

INSERT INTO Ciudad (nombre, codigo_ciudad, id_pais) VALUES
        ('New York', 'NYC', 1),
        ('Los Angeles', 'LAX', 1),
        ('Chicago', 'CHI', 1),
        ('Atlanta', 'ATL', 1),
        ('Dallas', 'DFW', 1),
        ('Miami', 'MIA', 1);


-- -----------------------------------------------------
-- 2. INFRAESTRUCTURA (Aeropuertos y Puertas)
-- -----------------------------------------------------

INSERT INTO Aeropuerto (nombre, codigo_iata, direccion, id_ciudad) VALUES
        ('John F. Kennedy International Airport', 'JFK', 'Queens, NY 11430', 1),
        ('Los Angeles International Airport', 'LAX', '1 World Way, Los Angeles, CA 90045', 2),
        ('O''Hare International Airport', 'ORD', '10000 W O''Hare Ave, Chicago, IL 60666', 3),
        ('Hartsfield-Jackson Atlanta International Airport', 'ATL', '6000 N Terminal Pkwy, Atlanta, GA 30320', 4),
        ('Dallas/Fort Worth International Airport', 'DFW', '2400 Aviation Dr, DFW Airport, TX 75261', 5),
        ('Miami International Airport', 'MIA', '2100 NW 42nd Ave, Miami, FL 33126', 6);

INSERT INTO PuertaDeEmbarque (codigo_puerta, ubicacion_terminal, id_aeropuerto) VALUES
        ('B20', 'Terminal 4', 1),
        ('B22', 'Terminal 4', 1),
        ('A10', 'Terminal 8', 1),
        ('C30', 'Terminal 1', 3),
        ('D25', 'Terminal D', 5),
        ('T1', 'Terminal T', 4),
        ('T3', 'Terminal T'


-- -----------------------------------------------------
-- 3. CATÁLOGOS DE NEGOCIO (Tipos y Estados)
-- -----------------------------------------------------

INSERT INTO TipoDocumento (descripcion, abreviatura) VALUES
        ('Pasaporte', 'PASS'),
        ('Licencia de Conducir', 'DL'),
        ('Tarjeta de Residente', 'GC');

INSERT INTO ClaseVuelo (nombre, descripcion) VALUES
        ('Económica', 'Clase turista estándar'),
        ('Economy Plus', 'Económica con espacio adicional para piernas'),
        ('Business', 'Clase ejecutiva con asientos reclinables'),
        ('Primera Clase', 'Servicio de lujo con asientos cama');

INSERT INTO EstadoVuelo (descripcion) VALUES
        ('Programado'),
        ('Embarcando'),
        ('En Vuelo'),
        ('Aterrizado'),
        ('Retrasado'),
        ('Cancelado');

INSERT INTO EstadoEmbarque (descripcion) VALUES
        ('Pendiente'),
        ('Check-In Realizado'),
        ('Verificado en Puerta'),
        ('Abordo');


-- -----------------------------------------------------
-- 4. FLOTA Y AEROLÍNEAS
-- -----------------------------------------------------

INSERT INTO Aerolinea (nombre, codigo_iata, pais_origen) VALUES
        ('American Airlines', 'AA', 'Estados Unidos'),
        ('Delta Air Lines', 'DL', 'Estados Unidos'),
        ('United Airlines', 'UA', 'Estados Unidos'),
        ('Southwest Airlines', 'WN', 'Estados Unidos'),
        ('Spirit Airlines', 'NK', 'Estados Unidos'),
        ('Copa Airlines', 'CM', 'Panamá');

INSERT INTO ModeloAvion (nombre_modelo, capacidad_pasajeros, autonomia_km) VALUES
        ('Airbus A320', 180, 6100),
        ('Boeing 737-800', 189, 5665),
        ('Boeing 777', 396, 15840),
        ('Boeing 787 Dreamliner', 290, 14140),
        ('Embraer E175', 88, 3700);

INSERT INTO Avion (matricula, anio_fabricacion, estado_operativo, id_modelo_avion, id_aerolinea) VALUES
        ('N101AA', 2019, 'Activo', 2, 1),
        ('N301DL', 2020, 'Activo', 1, 2),
        ('N201UA', 2018, 'Activo', 2, 3),
        ('N401WN', 2017, 'Activo', 2, 4),
        ('N501NK', 2021, 'Activo', 1, 5),
        ('N770AA', 2016, 'Activo', 3, 1),
        ('N800DL', 2019, 'Activo', 4, 2);


-- -----------------------------------------------------
-- 5. USUARIOS BASE (Roles y Personal Inicial)
-- ------------------------------------
INSERT INTO Rol (nombre_rol, descripcion) VALUES
        ('Administrador', 'Acceso total al sistema y configuración'),
        ('Supervisor de Operaciones', 'Gestión de vuelos y recursos'),
        ('Agente de Puerta', 'Operaciones de embarque y pasajeros');

INSERT INTO Personal (nombres, apellidos, dni, correo, turno, id_aeropuerto, contrasena_hash, id_rol) VALUES
        ('Admin', 'Zephyr', '000000001', 'admin@zephyr.com', 'Mañana', 1, 'admin123', 1),
        ('Michael', 'Scott', '100200300', 'm.scott@zephyr.com', 'Mañana', 1, 'agente123', 3),
        ('Sarah', 'Connor', '100200301', 's.connor@zephyr.com', 'Mañana', 1, 'super123', 2),
        ('John', 'Smith', '100200302', 'j.smith@zephyr.com', 'Tarde', 4, 'agente456', 3),
        ('Emily', 'Jones', '100200303', 'e.jones@zephyr.com', 'Noche', 4, 'agente789', 3);


SET session_replication_role = 'origin';

SELECT 'Script 02: Datos Maestros insertados correctamente.' AS "Estado";