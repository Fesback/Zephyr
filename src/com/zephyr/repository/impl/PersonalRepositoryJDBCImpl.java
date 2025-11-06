package com.zephyr.repository.impl;

import com.zephyr.domain.Personal;
import com.zephyr.domain.Rol;
import com.zephyr.repository.PersonalRepository;
import com.zephyr.util.DatabaseConnector;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class PersonalRepositoryJDBCImpl implements PersonalRepository {

    private Connection conn;

    public PersonalRepositoryJDBCImpl(){
        this.conn = DatabaseConnector.getInstance().getConnection();
        if (this.conn == null) {
            System.err.println("No connection established");
        }
    }

    @Override
    public Optional<Personal> findbyCorreo(String correo) {
        String sql = "SELECT p.*, r.nombre_rol, r.descripcion " +
                     "FROM Personal p " +
                     "JOIN Rol r ON p.id_rol = r.id_rol " +
                     "WHERE LOWER(p.correo) = LOWER(?)";

        try(PreparedStatement pstmt = conn.prepareStatement(sql)){
            pstmt.setString(1, correo.toLowerCase());

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    Rol rol = new Rol(
                            rs.getInt("id_rol"),
                            rs.getString("nombre_rol"),
                            rs.getString("descripcion")
                    );

                    Personal personal = new Personal(
                            rs.getInt("id_personal"),
                            rs.getString("nombres"),
                            rs.getString("apellidos"),
                            rs.getString("correo"),
                            rs.getString("contrasena_hash"),
                            rol
                    );

                    return Optional.of(personal);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al consultar la BD");
            e.printStackTrace();
        }

        return Optional.empty();
    }
}
