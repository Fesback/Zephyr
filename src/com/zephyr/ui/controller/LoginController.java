package com.zephyr.ui.controller;

import com.zephyr.domain.Personal;
import com.zephyr.repository.PersonalRepository;
import com.zephyr.repository.impl.PersonalRepositoryJDBCImpl;
import com.zephyr.service.AuthService;
import com.zephyr.service.impl.AuthServiceImpl;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;


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
            mostrarAlerta(Alert.AlertType.INFORMATION, "Login Exitoso", "¡Bienvenido, " + personal.getNombres() + "!\nTu rol es: " + personal.getRol().getNombreRol());

        } else {
            mostrarAlerta(Alert.AlertType.ERROR, "Error de Login", "Correo o contraseña incorrectos. Por favor, intente de nuevo.");
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
