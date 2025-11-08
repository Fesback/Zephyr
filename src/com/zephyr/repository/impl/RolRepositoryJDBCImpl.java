package com.zephyr.repository.impl;

import com.zephyr.domain.Rol;
import com.zephyr.repository.RolRepository;
import com.zephyr.util.DatabaseConnector;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RolRepositoryJDBCImpl implements RolRepository {

    private Connection conn;

    public RolRepositoryJDBCImpl() {
        this.conn = DatabaseConnector.getInstance().getConnection();
    }

    @Override
    public List<Rol> findAll() {
        List<Rol> roles = new ArrayList<>();
        String sql = "SELECT * FROM Rol ORDER BY nombre_rol";

        try (PreparedStatement pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                roles.add(mapResultSetToRol(rs));
            }
        } catch (SQLException e) {
            System.out.println("Error al consultar los roles");
            e.printStackTrace();
        }
        return roles;
    }

    private Rol mapResultSetToRol(ResultSet rs) throws SQLException {
        return new Rol(
                rs.getInt("id_rol"),
                rs.getString("nombre_rol"),
                rs.getString("descripcion")
        );
    }
}
