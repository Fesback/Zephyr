-- -----------------------------------------------------
-- SCRIPT 1: ESTRUCTURA DE BASE DE DATOS (SCHEMA)
-- -----------------------------------------------------
DROP SCHEMA public CASCADE;
CREATE SCHEMA public;
SET session_replication_role = 'replica';

-- 1. Tablas Maestras (Catálogos)
CREATE TABLE Pais (
        id_pais SERIAL PRIMARY KEY,
        nombre VARCHAR(100) NOT NULL,
        codigo_iso VARCHAR(3)
);

CREATE TABLE Ciudad (
        id_ciudad SERIAL PRIMARY KEY,
        nombre VARCHAR(100) NOT NULL,
        codigo_ciudad VARCHAR(5),
        id_pais INT NOT NULL
);

CREATE TABLE TipoDocumento (
                id_tipo_documento SERIAL PRIMARY KEY,
                descripcion VARCHAR(50) NOT NULL,
                abreviatura VARCHAR(10)
);

CREATE TABLE ClaseVuelo (
            id_clase_vuelo SERIAL PRIMARY KEY,
            nombre VARCHAR(50) NOT NULL,
            descripcion VARCHAR(255)
);

CREATE TABLE EstadoVuelo (
                id_estado_vuelo SERIAL PRIMARY KEY,
                descripcion VARCHAR(50) NOT NULL
);

CREATE TABLE EstadoEmbarque (
                id_estado_embarque SERIAL PRIMARY KEY,
                descripcion VARCHAR(50) NOT NULL
);

CREATE TABLE Rol (
        id_rol SERIAL PRIMARY KEY,
        nombre_rol VARCHAR(50) NOT NULL UNIQUE,
        descripcion TEXT
);

-- 2. Tablas de Recursos
CREATE TABLE Aerolinea (
            id_aerolinea SERIAL PRIMARY KEY,
            nombre VARCHAR(100) NOT NULL,
            codigo_iata VARCHAR(3) NOT NULL,
            pais_origen VARCHAR(100),
            telefono_contacto VARCHAR(20)
);

CREATE TABLE ModeloAvion (
                id_modelo_avion SERIAL PRIMARY KEY,
                nombre_modelo VARCHAR(50) NOT NULL,
                capacidad_pasajeros INT NOT NULL,
                autonomia_km INT
);

CREATE TABLE Aeropuerto (
            id_aeropuerto SERIAL PRIMARY KEY,
            nombre VARCHAR(100) NOT NULL,
            codigo_iata VARCHAR(3) NOT NULL UNIQUE,
            direccion VARCHAR(255),
            id_ciudad INT NOT NULL
);

CREATE TABLE Avion (
        id_avion SERIAL PRIMARY KEY,
        matricula VARCHAR(20) NOT NULL UNIQUE,
        anio_fabricacion INT,
        estado_operativo VARCHAR(50),
        id_modelo_avion INT NOT NULL,
        id_aerolinea INT NOT NULL
);

CREATE TABLE PuertaDeEmbarque (
        id_puerta_embarque SERIAL PRIMARY KEY,
        codigo_puerta VARCHAR(10) NOT NULL,
        ubicacion_terminal VARCHAR(20),
        id_aeropuerto INT NOT NULL,
        estado_puerta VARCHAR(20) DEFAULT 'Disponible'
);

-- 3. Tablas Transaccionales y Usuarios
CREATE TABLE Personal (
            id_personal SERIAL PRIMARY KEY,
            nombres VARCHAR(100) NOT NULL,
            apellidos VARCHAR(100) NOT NULL,
            dni VARCHAR(20) UNIQUE,
            correo VARCHAR(100) UNIQUE,
            telefono VARCHAR(20),
            turno VARCHAR(20),
            id_aeropuerto INT NOT NULL,
            contrasena_hash VARCHAR(255),
            id_rol INT
);

CREATE TABLE Pasajero (
            id_pasajero SERIAL PRIMARY KEY,
            nombres VARCHAR(100) NOT NULL,
            apellidos VARCHAR(100) NOT NULL,
            fecha_nacimiento DATE,
            numero_documento VARCHAR(30) NOT NULL,
            nacionalidad VARCHAR(100),
            correo VARCHAR(100),
            telefono VARCHAR(20),
            id_tipo_documento INT NOT NULL
);

CREATE TABLE Vuelo (
        id_vuelo SERIAL PRIMARY KEY,
        codigo_vuelo VARCHAR(10) NOT NULL,
        fecha_salida TIMESTAMP NOT NULL,
        fecha_llegada TIMESTAMP NOT NULL,
        id_aerolinea INT NOT NULL,
        id_avion INT NOT NULL,
        id_aeropuerto_origen INT NOT NULL,
        id_aeropuerto_destino INT NOT NULL,
        id_estado_vuelo INT NOT NULL,
        id_puerta_asignada INT
);

CREATE TABLE Boleto (
        id_boleto SERIAL PRIMARY KEY,
        codigo_boleto VARCHAR(20) NOT NULL UNIQUE,
        fecha_emision TIMESTAMP NOT NULL,
        asiento VARCHAR(5),
        precio_total DECIMAL(10, 2),
        id_pasajero INT NOT NULL,
        id_vuelo INT NOT NULL,
        id_clase_vuelo INT NOT NULL,
        id_estado_embarque INT NOT NULL
);

CREATE TABLE Equipaje (
            id_equipaje SERIAL PRIMARY KEY,
            codigo_equipaje VARCHAR(30) NOT NULL UNIQUE,
            peso DECIMAL(5, 2),
            dimensiones VARCHAR(50),
            tipo VARCHAR(30),
            id_boleto INT NOT NULL
);

CREATE TABLE AsignacionPersonal (
        id_asignacion SERIAL PRIMARY KEY,
        rol_en_asignacion VARCHAR(50),
        fecha_asignacion DATE,
        id_personal INT NOT NULL,
        id_vuelo INT NOT NULL
);

-- 4. Restricciones (Foreign Keys)
ALTER TABLE Ciudad ADD CONSTRAINT fk_ciudad_pais FOREIGN KEY (id_pais) REFERENCES Pais(id_pais);
ALTER TABLE Aeropuerto ADD CONSTRAINT fk_aeropuerto_ciudad FOREIGN KEY (id_ciudad) REFERENCES Ciudad(id_ciudad);
ALTER TABLE Avion ADD CONSTRAINT fk_avion_modelo FOREIGN KEY (id_modelo_avion) REFERENCES ModeloAvion(id_modelo_avion);
ALTER TABLE Avion ADD CONSTRAINT fk_avion_aerolinea FOREIGN KEY (id_aerolinea) REFERENCES Aerolinea(id_aerolinea);
ALTER TABLE PuertaDeEmbarque ADD CONSTRAINT fk_puerta_aeropuerto FOREIGN KEY (id_aeropuerto) REFERENCES Aeropuerto(id_aeropuerto);
ALTER TABLE Pasajero ADD CONSTRAINT fk_pasajero_tipodocumento FOREIGN KEY (id_tipo_documento) REFERENCES TipoDocumento(id_tipo_documento);
ALTER TABLE Pasajero ADD CONSTRAINT uq_pasajero_documento UNIQUE (id_tipo_documento, numero_documento);
ALTER TABLE Personal ADD CONSTRAINT fk_personal_aeropuerto FOREIGN KEY (id_aeropuerto) REFERENCES Aeropuerto(id_aeropuerto);
ALTER TABLE Personal ADD CONSTRAINT fk_personal_rol FOREIGN KEY (id_rol) REFERENCES Rol(id_rol);
ALTER TABLE Vuelo ADD CONSTRAINT fk_vuelo_aerolinea FOREIGN KEY (id_aerolinea) REFERENCES Aerolinea(id_aerolinea);
ALTER TABLE Vuelo ADD CONSTRAINT fk_vuelo_avion FOREIGN KEY (id_avion) REFERENCES Avion(id_avion);
ALTER TABLE Vuelo ADD CONSTRAINT fk_vuelo_aeropuerto_origen FOREIGN KEY (id_aeropuerto_origen) REFERENCES Aeropuerto(id_aeropuerto);
ALTER TABLE Vuelo ADD CONSTRAINT fk_vuelo_aeropuerto_destino FOREIGN KEY (id_aeropuerto_destino) REFERENCES Aeropuerto(id_aeropuerto);
ALTER TABLE Vuelo ADD CONSTRAINT fk_vuelo_estadovuelo FOREIGN KEY (id_estado_vuelo) REFERENCES EstadoVuelo(id_estado_vuelo);
ALTER TABLE Vuelo ADD CONSTRAINT fk_vuelo_puerta FOREIGN KEY (id_puerta_asignada) REFERENCES PuertaDeEmbarque(id_puerta_embarque);
ALTER TABLE Boleto ADD CONSTRAINT fk_boleto_pasajero FOREIGN KEY (id_pasajero) REFERENCES Pasajero(id_pasajero);
ALTER TABLE Boleto ADD CONSTRAINT fk_boleto_vuelo FOREIGN KEY (id_vuelo) REFERENCES Vuelo(id_vuelo);
ALTER TABLE Boleto ADD CONSTRAINT fk_boleto_clasevuelo FOREIGN KEY (id_clase_vuelo) REFERENCES ClaseVuelo(id_clase_vuelo);
ALTER TABLE Boleto ADD CONSTRAINT fk_boleto_estadoembarque FOREIGN KEY (id_estado_embarque) REFERENCES EstadoEmbarque(id_estado_embarque);
ALTER TABLE Equipaje ADD CONSTRAINT fk_equipaje_boleto FOREIGN KEY (id_boleto) REFERENCES Boleto(id_boleto);
ALTER TABLE AsignacionPersonal ADD CONSTRAINT fk_asignacion_personal FOREIGN KEY (id_personal) REFERENCES Personal(id_personal);
ALTER TABLE AsignacionPersonal ADD CONSTRAINT fk_asignacion_vuelo FOREIGN KEY (id_vuelo) REFERENCES Vuelo(id_vuelo);

SET session_replication_role = 'origin';

