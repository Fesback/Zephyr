package com.zephyr.repository.impl;

import com.itextpdf.kernel.pdf.DestinationResolverCopyFilter;
import com.zephyr.domain.Vuelo;
import com.zephyr.domain.VueloRegistro;
import com.zephyr.repository.VueloRepository;
import com.zephyr.util.DatabaseConnector;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class VueloRepositoryJDBCImpl implements VueloRepository {

    private Connection conn;

    public VueloRepositoryJDBCImpl() {
        this.conn = DatabaseConnector.getInstance().getConnection();
        if(this.conn == null){
            System.err.println("ERROR: VueloRepositoryJDBCImpl: No se pudo establecer la conexion");
        }
    }

    @Override
    public List<Vuelo> findVuelosDetalladosDelDia() {
        List<Vuelo> vuelos = new ArrayList<>();
        String sql = "SELECT * FROM v_vuelos_programados " +
                "WHERE fecha_salida::date = CURRENT_DATE " +
                "ORDER BY fecha_salida ASC";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Vuelo vuelo = mapResultSetToVuelo(rs);
                    vuelos.add(vuelo);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al consultar la vista de vuelos: ");
            e.printStackTrace();
        }

        return vuelos;
    }

    @Override
    public Optional<Vuelo> findVueloDetalladoById(int idVuelo) {
        String sql = "SELECT * FROM v_vuelos_programados WHERE id_vuelo = ?";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1,idVuelo);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToVuelo(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar vuelo detallado por ID: ");
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public void actualizarEstadoVuelo(int idVuelo, int idNuevoEstado) {
        String sql = "SELECT sp_actualizar_estado_vuelo(?,?);";
        try (CallableStatement cstmt = conn.prepareCall(sql)) {
            cstmt.setInt(1, idVuelo);
            cstmt.setInt(2, idNuevoEstado);
            ResultSet rs = cstmt.executeQuery();

            if (rs.next()) {
                System.out.println("Respuesta de la BD: " + rs.getString(1));
            }
        } catch (SQLException e) {
            System.err.println("Error al llamar al SP");
            e.printStackTrace();
        }
    }

    @Override
    public void registrarVuelo(VueloRegistro vuelo) {
        String sql = "INSERT INTO Vuelo (codigo_vuelo, fecha_salida, fecha_llegada, id_aerolinea, id_avion, id_aeropuerto_origen, id_aeropuerto_destino, id_estado_vuelo) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, vuelo.getCodigoVuelo());
            pstmt.setTimestamp(2, Timestamp.valueOf(vuelo.getFechaSalida()));
            pstmt.setTimestamp(3, Timestamp.valueOf(vuelo.getFechaLlegada()));
            pstmt.setInt(4, vuelo.getIdAerolinea());
            pstmt.setInt(5, vuelo.getIdAvion());
            pstmt.setInt(6, vuelo.getIdAeropuertoOrigen());
            pstmt.setInt(7, vuelo.getIdAeropuertoDestino());
            pstmt.setInt(8, vuelo.getIdEstadoVuelo());

            pstmt.executeUpdate();
            System.out.println("Vuelo registrado exitosamente: " + vuelo.getCodigoVuelo());

        } catch (SQLException e) {
            System.err.println("Error al registrar Vuelo: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public List<Vuelo> findAll() {
        List<Vuelo> vuelos = new ArrayList<>();
        String sql = "SELECT * FROM v_vuelos_programados ORDER BY fecha_salida DESC";

        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                try {
                    vuelos.add(mapResultSetToVuelo(rs));
                } catch (SQLException e) {  }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return vuelos;
    }

    private Vuelo mapResultSetToVuelo(ResultSet rs) throws SQLException {

        String codigoPuerta = rs.getString("codigo_puerta");
        String ubicacionTerminal = rs.getString("ubicacion_terminal");
        String avionMatricula = rs.getString("avion_matricula");
        String avionModelo = rs.getString("avion_modelo");

        return new Vuelo(
                rs.getInt("id_vuelo"),
                rs.getString("codigo_vuelo"),
                rs.getTimestamp("fecha_salida").toLocalDateTime(),
                rs.getTimestamp("fecha_llegada").toLocalDateTime(),
                rs.getInt("id_aerolinea"),
                rs.getInt("id_avion"),
                rs.getInt("id_aeropuerto_origen"),
                rs.getInt("id_aeropuerto_destino"),
                rs.getInt("id_puerta_asignada"),
                rs.getString("estado_vuelo"),
                rs.getString("aerolinea"),
                rs.getString("aerolinea_iata"),
                avionMatricula,
                avionModelo,
                rs.getInt("capacidad_pasajeros"),
                rs.getString("aeropuerto_origen"),
                rs.getString("origen_iata"),
                rs.getString("ciudad_origen"),
                rs.getString("aeropuerto_destino"),
                rs.getString("destino_iata"),
                rs.getString("ciudad_destino"),
                codigoPuerta,
                ubicacionTerminal
        );
    }
}
