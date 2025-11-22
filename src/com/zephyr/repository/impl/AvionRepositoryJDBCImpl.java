package com.zephyr.repository.impl;

import com.zephyr.domain.Avion;
import com.zephyr.repository.AvionRepository;
import com.zephyr.util.DatabaseConnector;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AvionRepositoryJDBCImpl implements AvionRepository {

    private Connection conn;

    public AvionRepositoryJDBCImpl() {
        this.conn = DatabaseConnector.getInstance().getConnection();
    }

    @Override
    public void save(Avion avion) {
        String sql = "INSERT INTO Avion (matricula, anio_fabricacion, estado_operativo, id_modelo_avion, id_aerolinea) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, avion.getMatricula());
            pstmt.setInt(2, avion.getAnioFabricacion());
            pstmt.setString(3, avion.getEstadoOperativo());
            pstmt.setInt(4, avion.getIdModeloAvion());
            pstmt.setInt(5, avion.getIdAerolinea());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Avion> findAll() {
        List<Avion> lista = new ArrayList<>();
        String sql = "SELECT * FROM Avion ORDER BY matricula";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)){

            while (rs.next()) {
                lista.add(mapResultSetToAvion(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }

    @Override
    public void update(Avion avion) {
        String sql = "UPDATE Avion SET matricula=?, anio_fabricacion=?, estado_operativo=?, id_modelo_avion=?, id_aerolinea=? WHERE id_avion=?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, avion.getMatricula());
            pstmt.setInt(2, avion.getAnioFabricacion());
            pstmt.setString(3, avion.getEstadoOperativo());
            pstmt.setInt(4, avion.getIdModeloAvion());
            pstmt.setInt(5, avion.getIdAerolinea());
            pstmt.setInt(6, avion.getIdAvion());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(int id) {
        String sql = "DELETE FROM Avion WHERE id_avion=?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private Avion mapResultSetToAvion(ResultSet rs) throws SQLException {
        return new Avion(
                rs.getInt("id_avion"),
                rs.getString("matricula"),
                rs.getInt("anio_fabricacion"),
                rs.getString("estado_operativo"),
                rs.getInt("id_modelo_avion"),
                rs.getInt("id_aerolinea")
        );
    }
}
