package com.zephyr.repository.impl;

import com.zephyr.domain.Boleto;
import com.zephyr.domain.PasajeroPorVuelo;
import com.zephyr.repository.BoletoRepository;
import com.zephyr.util.DatabaseConnector;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BoletoRepositoryJDBCImpl implements BoletoRepository {

    private Connection conn;

    public BoletoRepositoryJDBCImpl() {
        this.conn = DatabaseConnector.getInstance().getConnection();
        if (this.conn == null) {
            System.err.println("ERROR: boletoReepository no pudo conectarse a la BD");
        }
    }

    @Override
    public List<PasajeroPorVuelo> findPasajerosByVueloId(int idVuelo) {
        List<PasajeroPorVuelo> manifiesto = new ArrayList<>();
        String sql = "SELECT * FROM v_pasajeros_por_vuelo WHERE id_vuelo = ? ORDER BY apellidos, nombres";

        try(PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idVuelo);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    manifiesto.add(mapResultSetToPasajeroPorVuelo(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al llamar al SP");
            e.printStackTrace();
        }
        return manifiesto;
    }

    @Override
    public String verificarPasajeroEnPuerta(int idBoleto) {
        String sql = "SELECT sp_verificar_pasajero_en_puerta(?);";

        try (CallableStatement cstmt = conn.prepareCall(sql)) {
            cstmt.setInt(1, idBoleto);
            ResultSet  rs = cstmt.executeQuery();

            if(rs.next()) {
                return rs.getString(1);
            }
        } catch (SQLException e) {
            System.err.println("Error al llamar al SP");
            e.printStackTrace();
            return "ERROR: Falla de base de datos";
        }
        return "ERROR: No se recibio respueesta del SP";
    }

    @Override
    public Optional<Boleto> findBoletoByCodigo(String codigoBoleto) {
        String sql = "SELECT * FROM Boleto WHERE codigo_boleto = ?";

        try(PreparedStatement pstmt = conn.prepareStatement(sql)){
            pstmt.setString(1, codigoBoleto);

            try(ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToBoleto(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar boleto por codigo: ");
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public Optional<Boleto> findById(int idBoleto) {
        String sql = "SELECT * FROM Boleto WHERE id_boleto = ?";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idBoleto);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToBoleto(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar boleto por codigo: ");
            e.printStackTrace();
        }
        return Optional.empty();
    }

    private PasajeroPorVuelo mapResultSetToPasajeroPorVuelo(ResultSet rs) throws SQLException {
        return  new PasajeroPorVuelo(
                rs.getInt("id_vuelo"),
                rs.getInt("id_pasajero"),
                rs.getString("nombres"),
                rs.getString("apellidos"),
                rs.getString("numero_documento"),
                rs.getString("tipo_documento"),
                rs.getInt("id_boleto"),
                rs.getString("codigo_boleto"),
                rs.getString("asiento"),
                rs.getString("clase_vuelo"),
                rs.getString("estado_embarque")
        );
    }

    private Boleto mapResultSetToBoleto(ResultSet rs) throws SQLException {
        return  new Boleto(
                rs.getInt("id_Boleto"),
                rs.getString("codigo_boleto"),
                rs.getTimestamp("fecha_emision").toLocalDateTime(),
                rs.getString("asiento"),
                rs.getBigDecimal("precio_total"),
                rs.getInt("id_pasajero"),
                rs.getInt("id_vuelo"),
                rs.getInt("id_clase_vuelo"),
                rs.getInt("id_estado_embarque")
        );
    }
}
