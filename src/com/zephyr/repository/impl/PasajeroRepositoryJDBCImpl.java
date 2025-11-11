package com.zephyr.repository.impl;

import com.zephyr.domain.Pasajero;
import com.zephyr.repository.PasajeroRepository;
import com.zephyr.util.DatabaseConnector;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class PasajeroRepositoryJDBCImpl implements PasajeroRepository {

    private Connection conn;

    public PasajeroRepositoryJDBCImpl() {
        this.conn = DatabaseConnector.getInstance().getConnection();
        if (this.conn == null) {
            System.err.println("ERROR: PasajeroRepository  no pudo conectarse a la BD");
        }
    }

    @Override
    public Optional<Pasajero> findById(int idPasajero) {
        String sql = "SELECT * FROM Pasajero WHERE id_pasajero = ?";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idPasajero);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToPasajero(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar pasajero por ID");
            e.printStackTrace();
        }
        return Optional.empty();
    }

    private Pasajero mapResultSetToPasajero(ResultSet rs) throws SQLException {
        return new Pasajero(
                rs.getInt("id_pasajero"),
                rs.getString("nombres"),
                rs.getString("apellidos"),
                (rs.getDate("fecha_nacimiento") != null) ? rs.getDate("fecha_nacimiento").toLocalDate() : null,
                rs.getString("numero_documento"),
                rs.getString("nacionalidad"),
                rs.getString("correo"),
                rs.getString("telefono"),
                rs.getInt("id_tipo_documento")
        );
    }
}
