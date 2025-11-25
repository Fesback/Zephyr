package com.zephyr.repository.impl;

import com.zephyr.domain.PuertaEmbarque;
import com.zephyr.repository.PuertaRepository;
import com.zephyr.util.DatabaseConnector;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PuertaRepositoryJDBCImpl  implements PuertaRepository {

    private Connection conn;

    public PuertaRepositoryJDBCImpl() {
        this.conn = DatabaseConnector.getInstance().getConnection();
    }

    @Override
    public List<PuertaEmbarque> listarPorAeropuerto(int idAeropuerto) {
        List<PuertaEmbarque> lista = new ArrayList<>();
        String sql = "SELECT * FROM PuertaDeEmbarque WHERE id_aeropuerto = ? ORDER BY codigo_puerta";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idAeropuerto);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    lista.add(new PuertaEmbarque(
                            rs.getInt("id_puerta_embarque"),
                            rs.getString("codigo_puerta"),
                            rs.getString("ubicacion_terminal"),
                            rs.getInt("id_aeropuerto")
                    ));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al listar puertas: " + e.getMessage());
            e.printStackTrace();
        }
        return lista;
    }

    @Override
    public void asignarPuerta(int idVuelo, int idPuerta) {
        String sql = "SELECT sp_asignar_puerta_a_vuelo(?, ?)";
        try (CallableStatement cstmt = conn.prepareCall(sql)) {
            cstmt.setInt(1, idVuelo);
            cstmt.setInt(2, idPuerta);

            ResultSet rs = cstmt.executeQuery();
            if (rs.next()) {
                System.out.println("Resultado de la asignacion: " + rs.getString(1));
            }
        } catch (SQLException e) {
            System.err.println("Error al asignar puerta: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
