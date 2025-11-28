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

    @Override
    public Pasajero save(Pasajero p) {
        String sql = "INSERT INTO Pasajero (nombres, apellidos, numero_documento, nacionalidad, correo, telefono, id_tipo_documento)" +
                "VALUES (?, ?, ?, ?, ?, ?, ?) RETURNING id_pasajero";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, p.getNombres());
            pstmt.setString(2, p.getApellidos());
            pstmt.setString(3, p.getNumeroDocumento());
            pstmt.setString(4, p.getNacionalidad());
            pstmt.setString(5, p.getCorreo());
            pstmt.setString(6, p.getTelefono());
            pstmt.setInt(7, p.getIdTipoDocumento());

            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                p.setIdPasajero(rs.getInt(1)); // Asignamos el ID nuevo al objeto
                System.out.println("Pasajero registrado con ID: " + p.getIdPasajero());
            }
        } catch (SQLException e) {
            System.err.println("Error al registrar el pasajero: " + e.getMessage());
            e.printStackTrace();
        }

        return p;
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
