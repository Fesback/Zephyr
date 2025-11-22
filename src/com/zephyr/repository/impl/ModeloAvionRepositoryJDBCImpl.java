package com.zephyr.repository.impl;

import com.zephyr.domain.ModeloAvion;
import com.zephyr.repository.ModeloAvionRepository;
import com.zephyr.util.DatabaseConnector;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ModeloAvionRepositoryJDBCImpl implements ModeloAvionRepository {

    private Connection conn;

    public ModeloAvionRepositoryJDBCImpl() {
        this.conn= DatabaseConnector.getInstance().getConnection();
    }

    @Override
    public List<ModeloAvion> findAll() {
        List<ModeloAvion> lista = new ArrayList<>();
        String sql = "SELECT * FROM ModeloAvion ORDER BY nombre_modelo";
        try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)){
            while (rs.next()) {
                lista.add(new ModeloAvion(
                   rs.getInt("id_modelo_avion"),
                   rs.getString("nombre_modelo"),
                   rs.getInt("capacidad_pasajeros"),
                   rs.getInt("autonomia_km")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }
}
