package com.zephyr.repository.impl;

import com.zephyr.domain.Aerolinea;
import com.zephyr.repository.AerolineaRepository;
import com.zephyr.util.DatabaseConnector;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AerolineaJDBCImpl implements AerolineaRepository {

    private Connection conn;

    public AerolineaJDBCImpl() {
        this.conn = DatabaseConnector.getInstance().getConnection();
    }

    @Override
    public void save(Aerolinea aerolinea) {
        String sql = "INSERT INTO Aerolinea (nombre, codigo_iata, pais_origen, telefono_contacto) VALUES (?, ?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)){
            pstmt.setString(1, aerolinea.getNombre());
            pstmt.setString(2, aerolinea.getCodigoIata());
            pstmt.setString(3, aerolinea.getPaisOrigen());
            pstmt.setString(4, aerolinea.getTelefonoContacto());
            pstmt.executeUpdate();
            System.out.println("Aerolinea guardada: " + aerolinea.getNombre());
        } catch (SQLException e) {
            System.err.println("Error al guardar aerolinea: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public List<Aerolinea> findAll() {
        List<Aerolinea> lista = new ArrayList<>();
        String sql = "SELECT * FROM Aerolinea ORDER BY nombre";
        try (Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                lista.add(mapResultSetToAerolinea(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error al consultar aerolinea: " + e.getMessage());
            e.printStackTrace();
        }
        return lista;
    }

    @Override
    public Optional<Aerolinea> findById(int id) {
        String sql = "SELECT * FROM Aerolinea WHERE id_aerolinea = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToAerolinea(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al consultar aerolinea por ID: " + e.getMessage());
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public void update(Aerolinea aerolinea) {
        String sql = "UPDATE Aerolinea SET nombre=?, codigo_iata=?, pais_origen=?, telefono_contacto=? WHERE id_aerolinea=?";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, aerolinea.getNombre());
            pstmt.setString(2, aerolinea.getCodigoIata());
            pstmt.setString(3, aerolinea.getPaisOrigen());
            pstmt.setString(4, aerolinea.getTelefonoContacto());
            pstmt.setInt(5, aerolinea.getIdAerolinea());

            pstmt.executeUpdate();
            System.out.println("Aerolínea actualizada: " + aerolinea.getNombre());
        } catch (SQLException e) {
            System.err.println("Error al actualizar aerolínea: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void delete(int id) {
        String sql = "DELETE FROM Aerolinea WHERE id_aerolinea=?";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
            System.out.println("Aerolínea eliminada ID: " + id);
        } catch (SQLException e) {
            System.err.println("Error al eliminar aerolínea (posiblemente en uso): " + e.getMessage());
            e.printStackTrace();
        }
    }

    private Aerolinea mapResultSetToAerolinea(ResultSet rs) throws SQLException {
        return new Aerolinea(
                rs.getInt("id_aerolinea"),
                rs.getString("nombre"),
                rs.getString("codigo_iata"),
                rs.getString("pais_origen"),
                rs.getString("telefono_contacto")
        );
    }
}
