package com.zephyr.repository.impl;

import com.zephyr.domain.Ciudad;
import com.zephyr.repository.CiudadRepository;
import com.zephyr.util.DatabaseConnector;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CiudadRepositoryJDBCImpl implements CiudadRepository {
    private Connection conn;

    public CiudadRepositoryJDBCImpl() {
        this.conn = DatabaseConnector.getInstance().getConnection();
    }

    @Override
    public void save(Ciudad ciudad) {
        String sql = "INSERT INTO Ciudad (nombre, codigo_ciudad, id_pais) VALUES (?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, ciudad.getNombre());
            pstmt.setString(2, ciudad.getCodigoCiudad());
            pstmt.setInt(3, ciudad.getIdPais());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Ciudad> findAll() {
        List<Ciudad> lista = new ArrayList<>();
        String sql = "SELECT * FROM Ciudad ORDER BY nombre";

        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                lista.add(mapResultSetToCiudad(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    @Override
    public Optional<Ciudad> findById(int id) {
        String sql = "SELECT * FROM Ciudad WHERE id_ciudad = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToCiudad(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public void update(Ciudad ciudad) {
        String sql = "UPDATE Ciudad SET nombre=?, codigo_ciudad=?, id_pais=? WHERE id_ciudad=?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, ciudad.getNombre());
            pstmt.setString(2, ciudad.getCodigoCiudad());
            pstmt.setInt(3, ciudad.getIdPais());
            pstmt.setInt(4, ciudad.getIdCiudad());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(int id) {
        String sql = "DELETE FROM Ciudad WHERE id_ciudad=?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private Ciudad mapResultSetToCiudad(ResultSet rs) throws SQLException {
        return new Ciudad(
                rs.getInt("id_ciudad"),
                rs.getString("nombre"),
                rs.getString("codigo_ciudad"),
                rs.getInt("id_pais")
        );
    }
}
