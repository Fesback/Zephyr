package com.zephyr.repository.impl;

import com.itextpdf.kernel.pdf.DestinationResolverCopyFilter;
import com.zephyr.domain.Vuelo;
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

    private Vuelo mapResultSetToVuelo(ResultSet rs) throws SQLException {
        return new Vuelo(
                rs.getInt("id_vuelo"),
                rs.getString("codigo_vuelo"),
                rs.getTimestamp("fecha_salida").toLocalDateTime(),
                rs.getTimestamp("fecha_llegada").toLocalDateTime(),
                rs.getString("estado_vuelo"),
                rs.getString("aerolinea"),
                rs.getString("aerolinea_iata"),
                rs.getString("avion_matricula"),
                rs.getString("avion_modelo"),
                rs.getInt("capacidad_pasajeros"),
                rs.getString("aeropuerto_origen"),
                rs.getString("origen_iata"),
                rs.getString("ciudad_origen"),
                rs.getString("aeropuerto_destino"),
                rs.getString("destino_iata"),
                rs.getString("ciudad_destino"),
                rs.getString("codigo_puerta"),
                rs.getString("ubicacion_terminal")
        );
    }
}
