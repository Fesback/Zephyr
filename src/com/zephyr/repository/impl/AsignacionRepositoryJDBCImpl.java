package com.zephyr.repository.impl;

import com.zephyr.domain.AsignacionPersonal;
import com.zephyr.repository.AsignacionRepository;
import com.zephyr.util.DatabaseConnector;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static com.itextpdf.kernel.pdf.PdfName.a;

public class AsignacionRepositoryJDBCImpl implements AsignacionRepository {

    private Connection conn;

    public AsignacionRepositoryJDBCImpl() {
        this.conn = DatabaseConnector.getInstance().getConnection();
    }



    @Override
    public void asignarPersonal(AsignacionPersonal a) {
        String sql = "INSERT INTO AsignacionPersonal (rol_en_asignacion, fecha_asignacion, id_personal, id_vuelo) VALUES (?, ?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, a.getRolEnAsignacion());
            pstmt.setDate(2, java.sql.Date.valueOf(a.getFechaAsignacion()));
            pstmt.setInt(3, a.getIdPersonal());
            pstmt.setInt(4, a.getIdVuelo());
            pstmt.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }

    @Override
    public List<AsignacionPersonal> listarPorVuelo(int idVuelo) {
        List<AsignacionPersonal> lista = new ArrayList<>();
        String sql = "SELECT * FROM AsignacionPersonal WHERE id_vuelo = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idVuelo);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    lista.add(new AsignacionPersonal(
                            rs.getInt("id_asignacion"),
                            rs.getString("rol_en_asignacion"),
                            rs.getDate("fecha_asignacion").toLocalDate(),
                            rs.getInt("id_personal"),
                            rs.getInt("id_vuelo")
                    ));
                }
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return lista;
    }
}
