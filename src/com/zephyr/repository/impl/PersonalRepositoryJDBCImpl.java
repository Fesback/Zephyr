package com.zephyr.repository.impl;

import com.zephyr.domain.Personal;
import com.zephyr.domain.Rol;
import com.zephyr.repository.PersonalRepository;
import com.zephyr.util.DatabaseConnector;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
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

    // guardar
    @Override
    public void save(Personal personal) {
        String sql = "INSERT INTO Personal (nombres, apellidos, dni, correo, telefono, turno, id_aeropuerto, contrasena_hash, id_rol) " +
                     " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?) RETURNING id_personal;";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, personal.getNombres());
            pstmt.setString(2, personal.getApellidos());
            pstmt.setString(3, personal.getDni());
            pstmt.setString(4, personal.getCorreo());
            pstmt.setString(5, personal.getTelefono());
            pstmt.setString(6, personal.getTurno());
            pstmt.setInt(7, personal.getIdAeropuerto());
            pstmt.setString(8, personal.getContrasenaHash());
            pstmt.setInt(9, personal.getRol().getIdRol());

            ResultSet rs = pstmt.executeQuery();
            if(rs.next()) {
                System.out.println("PErsonal guardado con ID: " + rs.getInt(1));
            }
        } catch (SQLException e) {
            System.err.println("Error al guardar la BD");
            e.printStackTrace();
        }
    }

    // buscar por id
    @Override
    public Optional<Personal> findbyId(int id) {
        String sql = "SELECT p.*, r.nombre_rol, r.descripcion " +
                    "FROM Personal p " +
                    "JOIN Rol r ON p.id_rol = r.id_rol " +
                    "WHERE p.id_personal = ?";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToPersonal(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar al personal por ID: ");
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public List<Personal> findAll() {
        List<Personal> personalLista = new ArrayList<>();
        String sql = "SELECT p.*, r.nombre_rol, r.descripcion " +
                "FROM Personal p " +
                "JOIN Rol r ON p.id_rol = r.id_rol " +
                "ORDER BY p.apellidos, p.nombres";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    personalLista.add(mapResultSetToPersonal(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al consultar todo el personal:");
            e.printStackTrace();
        }
        return personalLista;
    }

    @Override
    public void update(Personal personal) {
        String sql = "UPDATE Personal SET " +
                "nombres = ?, apellidos = ?, dni = ?, correo = ?, " +
                "telefono = ?, turno = ?, id_aeropuerto = ?, id_rol = ? " +
                "WHERE id_personal = ?;";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, personal.getNombres());
            pstmt.setString(2, personal.getApellidos());
            pstmt.setString(3, personal.getDni());
            pstmt.setString(4, personal.getCorreo());
            pstmt.setString(5, personal.getTelefono());
            pstmt.setString(6, personal.getTurno());
            pstmt.setInt(7, personal.getIdAeropuerto());
            pstmt.setInt(8, personal.getRol().getIdRol());
            pstmt.setInt(9, personal.getIdPersonal());

            int filasAfectadas = pstmt.executeUpdate();
            if (filasAfectadas > 0) {
                System.out.println("Personal ID " + personal.getIdPersonal() + " actualizado.");
            }
        } catch (SQLException e) {
            System.err.println("Error al actualizar personal en la BD:");
            e.printStackTrace();
        }
    }

    @Override
    public void delete(int id) {
        String sql = "DELETE FROM Personal WHERE id_personal = ?;";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);

            int filasAfectadas = pstmt.executeUpdate();
            if (filasAfectadas > 0) {
                System.out.println("Personal ID " + id + " eliminado.");
            }
        } catch (SQLException e) {
            System.err.println("Error al eliminar personal en la BD (podría estar en uso):");
            e.printStackTrace();
        }
    }

    private Personal mapResultSetToPersonal(ResultSet rs) throws SQLException {
        Rol rol = new Rol (
                rs.getInt("id_rol"),
                rs.getString("nombre_rol"),
                rs.getString("descripcion")
        );

        return new Personal (
                rs.getInt("id_personal"),
                rs.getString("nombres"),
                rs.getString("apellidos"),
                rs.getString("dni"),
                rs.getString("correo"),
                rs.getString("contrasena_hash"),
                rs.getString("telefono"),
                rs.getString("turno"),
                rs.getInt("id_aeropuerto"),
                rol
        );
    }
}
