package com.zephyr.ui.controller;

import com.zephyr.domain.Personal;
import com.zephyr.domain.Rol;
import com.zephyr.repository.PersonalRepository;
import com.zephyr.repository.RolRepository;
import com.zephyr.repository.impl.PersonalRepositoryJDBCImpl;
import com.zephyr.repository.impl.RolRepositoryJDBCImpl;
import com.zephyr.service.PersonalService;
import com.zephyr.service.RolService;
import com.zephyr.service.impl.PersonalServiceImpl;
import com.zephyr.service.impl.RolServiceImpl;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class RegistrarPersonalController implements Initializable {

    @FXML
    private TextField txtNombres, txtApellidos, txtDni, txtCorreo, txtTelefono, txtIdAeropuerto;
    @FXML private PasswordField txtContrasena;
    @FXML private ComboBox<String> comboTurno;
    @FXML private ComboBox<Rol> comboRol;
    @FXML private Button btnGuardarNuevo;
    @FXML private Button btnCancelar;

    private final PersonalService personalService;
    private final RolService rolService;
    private ObservableList<Rol> listaRolesObservable;

    private PersonalController personalControllerPadre;

    // Constructor: Arma el backend
    public RegistrarPersonalController() {
        PersonalRepository personalRepository = new PersonalRepositoryJDBCImpl();
        this.personalService = new PersonalServiceImpl(personalRepository);

        RolRepository rolRepository = new RolRepositoryJDBCImpl();
        this.rolService = new RolServiceImpl(rolRepository);
        this.listaRolesObservable = FXCollections.observableArrayList();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        configurarComboBoxRol();
        comboTurno.setItems(FXCollections.observableArrayList("Mañana", "Tarde", "Noche"));

        cargarRoles();

        btnGuardarNuevo.setOnAction(event -> handleGuardarNuevo());
        btnCancelar.setOnAction(event -> cerrarVentana());
    }

    private void configurarComboBoxRol() {
        comboRol.setItems(listaRolesObservable);
        comboRol.setConverter(new StringConverter<Rol>() {
            @Override public String toString(Rol rol) { return (rol == null) ? null : rol.getNombreRol(); }
            @Override public Rol fromString(String string) { return null; }
        });
    }

    private void cargarRoles() {
        List<Rol> roles = rolService.getRoles();
        listaRolesObservable.clear();
        listaRolesObservable.addAll(roles);
    }


    private void handleGuardarNuevo() {
        if (txtNombres.getText().isEmpty() || txtCorreo.getText().isEmpty() || comboRol.getValue() == null) {
            mostrarAlerta(Alert.AlertType.ERROR, "Campos Vacíos", "Nombres, Correo y Rol son obligatorios.");
            return;
        }

        String nombres = txtNombres.getText();
        String apellidos = txtApellidos.getText();
        String dni = txtDni.getText();
        String correo = txtCorreo.getText();
        String contrasena = txtContrasena.getText();
        String telefono = txtTelefono.getText();
        String turno = comboTurno.getValue();
        int idAeropuerto = Integer.parseInt(txtIdAeropuerto.getText());
        Rol rol = comboRol.getValue();

        Personal nuevoAgente = new Personal(
                0, nombres, apellidos, dni, correo,
                contrasena, telefono, turno, idAeropuerto, rol
        );

        personalService.registrarPersonal(nuevoAgente);

        if (personalControllerPadre != null) {
            personalControllerPadre.refrescarTablaPersonal();
        }

        mostrarAlerta(Alert.AlertType.INFORMATION, "Éxito", "Nuevo agente registrado correctamente.");
        cerrarVentana();
    }

    public void setControladorPadre(PersonalController personalController) {
        this.personalControllerPadre = personalController;
    }

    private void cerrarVentana() {
        Stage stage = (Stage) btnCancelar.getScene().getWindow();
        stage.close();
    }

    private void mostrarAlerta(Alert.AlertType tipo, String titulo, String contenido) {
        Alert alerta = new Alert(tipo);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(contenido);
        alerta.showAndWait();
    }
}
