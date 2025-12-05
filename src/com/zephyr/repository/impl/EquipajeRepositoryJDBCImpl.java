package com.zephyr.repository.impl;

import com.zephyr.domain.Equipaje;
import com.zephyr.repository.EquipajeRepository;
import com.zephyr.util.DatabaseConnector;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class EquipajeRepositoryJDBCImpl implements EquipajeRepository {

    private Connection conn;

    public EquipajeRepositoryJDBCImpl() {
        this.conn = DatabaseConnector.getInstance().getConnection();
    }

    @Override
    public void save(Equipaje e) {
        String sql = "INSERT INTO Equipaje (codigo_equipaje, peso, dimensiones, tipo, id_boleto) VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, e.getCodigoEquipaje());
            pstmt.setDouble(2, e.getPeso());
            pstmt.setString(3, e.getDimensiones());
            pstmt.setString(4, e.getTipo());
            pstmt.setInt(5, e.getIdBoleto());

            pstmt.executeUpdate();
            System.out.println("Equipaje registrado: " + e.getCodigoEquipaje());

        } catch (SQLException ex) {
            System.err.println("Error al guardar equipaje: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    @Override
    public List<Equipaje> findByBoletoId(int idBoleto) {
        List<Equipaje> lista = new ArrayList<>();
        String sql = "SELECT * FROM Equipaje WHERE id_boleto = ?";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idBoleto);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    lista.add(new Equipaje(
                            rs.getInt("id_equipaje"),
                            rs.getString("codigo_equipaje"),
                            rs.getDouble("peso"),
                            rs.getString("dimensiones"),
                            rs.getString("tipo"),
                            rs.getInt("id_boleto")
                    ));
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return lista;
    }

    @Override
    public Optional<Equipaje> findById(int id) {
        String sql = "SELECT * FROM Equipaje WHERE id_equipaje = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(new Equipaje(
                            rs.getInt("id_equipaje"),
                            rs.getString("codigo_equipaje"),
                            rs.getDouble("peso"),
                            rs.getString("dimensiones"),
                            rs.getString("tipo"),
                            rs.getInt("id_boleto")
                    ));
                }
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return Optional.empty();
    }
}
