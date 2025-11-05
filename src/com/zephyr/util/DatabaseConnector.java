package com.zephyr.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnector {

    private static final String DB_URL = "jdbc:postgresql://localhost:5432/zephyr";
    private static final String DB_USER = "postgres";
    private static final String DB_PASSWORD = "2104";

    private static DatabaseConnector instance;

    private Connection connection;

    private DatabaseConnector() {
        try {
            Class.forName("org.postgresql.Driver");
            this.connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            System.out.println("La conexion a la base de datos Zephyr fue exitosa");

        } catch (ClassNotFoundException e) {
            System.err.println("Error: No se encontro el driver JDBC de PostgreSQL");
            e.printStackTrace();

        } catch (SQLException e) {
            System.err.println("Error: No se pudo conectar a la basse de datos");
            e.printStackTrace();
        }
    }

    public static DatabaseConnector getInstance() {
        if (instance == null) {
            instance = new DatabaseConnector();
        }

        return instance;
    }

    public Connection getConnection() {
        return this.connection;
    }

}

// Lass pruebas de conexion a la BD fueron exitosas
/*--
public static void main(String[] args) {
    System.out.println("Iniciando prueba de conexión a 'zephyr'...");


    DatabaseConnector.getInstance();

    System.out.println("Prueba de conexión finalizada.");
}
--*/
