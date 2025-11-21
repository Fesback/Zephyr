package com.zephyr.repository.impl;

import com.zephyr.domain.Aeropuerto;
import com.zephyr.repository.AeropuertoRepository;
import com.zephyr.util.DatabaseConnector;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AeropuertoRepositoryJDBCImpl implements AeropuertoRepository {
    private Connection conn;

    public AeropuertoRepositoryJDBCImpl() {
        this.conn = DatabaseConnector.getInstance().getConnection();
    }

    @Override
    public void save(Aeropuerto aeropuerto) {
        String sql = "INSERT INTO Aeropuerto (nombre, codigo_iata, direccion, id_ciudad) VALUES (?, ?, ?, ?)";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, aeropuerto.getNombre());
            pstmt.setString(2, aeropuerto.getCodigoIata());
            pstmt.setString(3, aeropuerto.getDireccion());
            pstmt.setInt(4, aeropuerto.getIdCiudad());

            pstmt.executeUpdate();
            System.out.println("Aeropuerto guardado: " + aeropuerto.getCodigoIata());

        } catch (SQLException e) {
            System.err.println("Error al guardar aeropuerto: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public List<Aeropuerto> findAll() {
        List<Aeropuerto> lista = new ArrayList<>();
        String sql = "SELECT * FROM Aeropuerto ORDER BY nombre";

        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                lista.add(mapResultSetToAeropuerto(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error al listar aeropuertos: " + e.getMessage());
            e.printStackTrace();
        }
        return lista;
    }

    @Override
    public Optional<Aeropuerto> findById(int id) {
        String sql = "SELECT * FROM Aeropuerto WHERE id_aeropuerto = ?";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToAeropuerto(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar aeropuerto por ID: " + e.getMessage());
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public void update(Aeropuerto aeropuerto) {
        String sql = "UPDATE Aeropuerto SET nombre=?, codigo_iata=?, direccion=?, id_ciudad=? WHERE id_aeropuerto=?";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, aeropuerto.getNombre());
            pstmt.setString(2, aeropuerto.getCodigoIata());
            pstmt.setString(3, aeropuerto.getDireccion());
            pstmt.setInt(4, aeropuerto.getIdCiudad());
            pstmt.setInt(5, aeropuerto.getIdAeropuerto());

            pstmt.executeUpdate();
            System.out.println("Aeropuerto actualizado: " + aeropuerto.getCodigoIata());

        } catch (SQLException e) {
            System.err.println("Error al actualizar aeropuerto: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void delete(int id) {
        String sql = "DELETE FROM Aeropuerto WHERE id_aeropuerto=?";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
            System.out.println("Aeropuerto eliminado ID: " + id);

        } catch (SQLException e) {
            System.err.println("Error al eliminar aeropuerto (puede estar en uso por vuelos): " + e.getMessage());
            e.printStackTrace();
        }
    }

    private Aeropuerto mapResultSetToAeropuerto(ResultSet rs) throws SQLException {
        return new Aeropuerto(
                rs.getInt("id_aeropuerto"),
                rs.getString("nombre"),
                rs.getString("codigo_iata"),
                rs.getString("direccion"),
                rs.getInt("id_ciudad")
        );
    }
}
