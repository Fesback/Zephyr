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

    @Override
    public void save(Boleto b) {
        String sql = "INSERT INTO Boleto (codigo_boleto, fecha_emision, asiento, precio_total, id_pasajero, id_vuelo, id_clase_vuelo, id_estado_embarque) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, b.getCodigoBoleto());
            pstmt.setTimestamp(2, java.sql.Timestamp.valueOf(b.getFechaEEmision()));
            pstmt.setString(3, b.getAsiento());
            pstmt.setBigDecimal(4, b.getPrecioTotal());
            pstmt.setInt(5, b.getIdPasajero());
            pstmt.setInt(6, b.getIdVuelo());
            pstmt.setInt(7, b.getIdClaseVuelo());
            pstmt.setInt(8, b.getIdEstadoEmbarque());

            pstmt.executeUpdate();
            System.out.println("Boleto emitido: " + b.getCodigoBoleto());
        } catch (SQLException e) {
            System.err.println("Error al guardar boleto: " + e.getMessage());
            e.printStackTrace();
        }
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
