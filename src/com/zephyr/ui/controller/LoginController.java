package com.zephyr.ui.controller;

import com.zephyr.domain.Personal;
import com.zephyr.repository.PersonalRepository;
import com.zephyr.repository.impl.PersonalRepositoryJDBCImpl;
import com.zephyr.service.AuthService;
import com.zephyr.service.impl.AuthServiceImpl;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.stage.Stage;


import java.io.IOException;
import java.net.URL;
import java.util.Optional;

public class LoginController {

    @FXML
    private TextField usuarioField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Button loginButton;


    private final AuthService authService;
    private final PersonalRepository personalRepository;

    public LoginController() {
        this.personalRepository = new PersonalRepositoryJDBCImpl();
        this.authService = new AuthServiceImpl(this.personalRepository);
    }

    @FXML
    private void initialize() {
        loginButton.setOnAction(event -> handleLogin());
        // para que inicie sesion con enter
        loginButton.setDefaultButton(true);

    }

    private void handleLogin() {
        String correo = usuarioField.getText().trim();
        String contrasena = passwordField.getText().trim();

        if (correo.isEmpty() || contrasena.isEmpty()) {
            mostrarAlerta(Alert.AlertType.WARNING, "Campos Vacíos", "Por favor, ingrese su correo y contraseña.");
            return;
        }

        Optional<Personal> personalLogueado = authService.login(correo, contrasena);

        if (personalLogueado.isPresent()) {
            Personal personal = personalLogueado.get();

            // abrimos el dashboard
            abrirDashboard(personal);

            //cerramos el login
            Stage stageActual = (Stage) loginButton.getScene().getWindow();
            stageActual.close();

        } else {
            mostrarAlerta(Alert.AlertType.ERROR, "Error de Login", "Correo o contraseña incorrectos. Por favor, intente de nuevo.");
        }
    }

    //Dashboard
    private void abrirDashboard(Personal personal) {
        try {
            FXMLLoader loader = new FXMLLoader();
            URL fxmlUrl = getClass().getResource("/com/zephyr/ui/fxml/Dashboard.fxml");
            if (fxmlUrl == null) {
                mostrarAlerta(Alert.AlertType.ERROR, "ERROR DE UI", "Fichero no encontrado.");
                return;
            }

            loader.setLocation(fxmlUrl);
            Parent root = loader.load();
            DashboardController dashboardController = loader.getController();

            dashboardController.setUsuarioLogueado(personal);

            Stage dashboardStage = new Stage();
            dashboardStage.setTitle("Zephyr - Dashboard de Operaciones");
            dashboardStage.getIcons().add(new Image(getClass().getResourceAsStream("/com/zephyr/ui/images/zephyr-icon.png")));
            dashboardStage.setScene(new Scene(root, 1280, 720));
            dashboardStage.setMinWidth(1024);
            dashboardStage.setMinHeight(768);
            dashboardStage.show();

        } catch (IOException e) {
            e.printStackTrace();
            mostrarAlerta(Alert.AlertType.ERROR, "ERROR", "No se pudo abrir el dashboard");

        }
    }

    private void mostrarAlerta(Alert.AlertType tipo, String titulo, String contenido) {
        Alert alerta = new Alert(tipo);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(contenido);
        alerta.showAndWait();
    }
}
