package com.zephyr.main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

public class MainApp extends Application {

    @Override
    public void start(Stage primaryStage){
        try {

            URL fxmlUrl = getClass().getResource("/com/zephyr/ui/fxml/login.fxml");

            if (fxmlUrl == null) {
                System.err.println("Error: No se pudo encontrar el archivo login.fxml.");
                System.err.println("Verifica la ruta en 'resources'.");
                return;
            }


            Parent root = FXMLLoader.load(fxmlUrl);

            //icon de la app
            primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/com/zephyr/ui/images/zephyr-icon.png")));

            primaryStage.setTitle("Zephyr - Login");

            //res al ejecutar
            primaryStage.setScene(new Scene(root, 1280, 720));
            primaryStage.setMinWidth(1024);
            primaryStage.setMinHeight(768);
            primaryStage.show();

        } catch (IOException e) {
            System.err.println("Error al cargar el FXML:");
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}