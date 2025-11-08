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
import javafx.util.StringConverter;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class PersonalController implements Initializable {

    @FXML private TableView<Personal> tablaPersonal;
    @FXML private TableColumn<Personal, Integer> colId;
    @FXML private TableColumn<Personal, String> colNombres;
    @FXML private TableColumn<Personal, String> colApellidos;
    @FXML private TableColumn<Personal, String> colCorreo;
    @FXML private TableColumn<Personal, Rol> colRol;

    @FXML private Label labelTituloFormulario;
    @FXML private TextField txtNombres, txtApellidos, txtDni, txtCorreo, txtTelefono, txtIdAeropuerto;
    @FXML private ComboBox<String> comboTurno;
    @FXML private PasswordField txtContrasena;
    @FXML private ComboBox<Rol> comboRol;
    @FXML private Button btnGuardar, btnEliminar, btnLimpiar;

    private final PersonalService personalService;
    private final RolService rolService;
    private ObservableList<Personal> listaPersonalObservable;
    private ObservableList<Rol> listaRolesObservable;
    private Personal personalSeleccionado;

    public PersonalController() {
        PersonalRepository personalRepository = new PersonalRepositoryJDBCImpl();
        this.personalService = new PersonalServiceImpl(personalRepository);

        RolRepository rolRepository = new RolRepositoryJDBCImpl();
        this.rolService = new RolServiceImpl(rolRepository);

        this.listaPersonalObservable = FXCollections.observableArrayList();
        this.listaRolesObservable = FXCollections.observableArrayList();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        configurarTabla();
        configurarComboBoxRol();

        tablaPersonal.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> seleccionarPersonal(newVal));

        comboTurno.setItems(FXCollections.observableArrayList("Mañana", "Tarde", "Noche"));
        btnGuardar.setOnAction(event -> handleGuardar());
        btnEliminar.setOnAction(event -> handleEliminar());
        btnLimpiar.setOnAction(event -> limpiarFormulario());

        cargarPersonal();
        cargarRoles();

        limpiarFormulario();
    }

    private void configurarTabla() {
        colId.setCellValueFactory(new PropertyValueFactory<>("idPersonal"));
        colNombres.setCellValueFactory(new PropertyValueFactory<>("nombres"));
        colApellidos.setCellValueFactory(new PropertyValueFactory<>("apellidos"));
        colCorreo.setCellValueFactory(new PropertyValueFactory<>("correo"));
        colRol.setCellValueFactory(new PropertyValueFactory<>("rol"));

        colRol.setCellFactory(column -> new TableCell<Personal, Rol>() {
            @Override
            protected void updateItem(Rol item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.getNombreRol());
            }
        });

        tablaPersonal.setItems(listaPersonalObservable);
    }

    private void configurarComboBoxRol() {
        comboRol.setItems(listaRolesObservable);
        comboRol.setConverter(new StringConverter<Rol>() {
            @Override
            public String toString(Rol rol) {
                return (rol == null) ? null : rol.getNombreRol();
            }
            @Override
            public Rol fromString(String string) {
                return null;
            }
        });
    }

    private void cargarPersonal() {
        List<Personal> personal = personalService.listarTodoElPersonal();
        listaPersonalObservable.clear();
        listaPersonalObservable.addAll(personal);
    }

    private void cargarRoles() {
        List<Rol> roles = rolService.getRoles();
        listaRolesObservable.clear();
        listaRolesObservable.addAll(roles);
    }

    private void handleGuardar() {
        String nombres = txtNombres.getText();
        String apellidos = txtApellidos.getText();
        String dni = txtDni.getText();
        String correo = txtCorreo.getText();
        String contrasena = txtContrasena.getText();
        String telefono = txtTelefono.getText();
        String turno = comboTurno.getValue();
        int idAeropuerto = Integer.parseInt(txtIdAeropuerto.getText());
        Rol rol = comboRol.getValue();

        if (personalSeleccionado == null) {
            Personal nuevo = new Personal(0, nombres, apellidos, dni, correo, contrasena, telefono, turno, idAeropuerto, rol);
            personalService.registrarPersonal(nuevo);

            mostrarAlerta(Alert.AlertType.INFORMATION, "Exito", "Nuevo agente registrado");
        } else {
            personalSeleccionado.setNombres(nombres);
            personalSeleccionado.setApellidos(apellidos);
            personalSeleccionado.setDni(dni);
            personalSeleccionado.setCorreo(correo);
            personalSeleccionado.setTelefono(telefono);
            personalSeleccionado.setTurno(turno);
            personalSeleccionado.setIdAeropuerto(idAeropuerto);
            personalSeleccionado.setRol(rol);

            personalService.actualizarPersonal(personalSeleccionado);
            mostrarAlerta(Alert.AlertType.INFORMATION, "Exito" , "Agente actualizado correctamente");
        }

        cargarPersonal();
        limpiarFormulario();
    }

    private void handleEliminar() {
        if (personalSeleccionado == null) {
            mostrarAlerta(Alert.AlertType.INFORMATION, "Sin seleccion", "Por favor, seleccione un agente de la tabla para eliminar");
            return;
        }

        personalService.eliminarAgente(personalSeleccionado.getIdPersonal());
        mostrarAlerta(Alert.AlertType.INFORMATION, "Exito", "Agente eliminado correctamente");

        cargarPersonal();
        limpiarFormulario();
    }

    private void seleccionarPersonal(Personal personal) {
        if (personal != null) {
            this.personalSeleccionado = personal;
            labelTituloFormulario.setText("Actualizar Agente (ID :" + personal.getIdPersonal() + ")");
            txtNombres.setText(personal.getNombres());
            txtApellidos.setText(personal.getApellidos());
            txtDni.setText(personal.getDni());
            txtCorreo.setText(personal.getCorreo());
            txtContrasena.setDisable(true);
            txtTelefono.setText(personal.getTelefono());
            comboTurno.setValue(personal.getTurno());
            txtIdAeropuerto.setText(String.valueOf(personal.getIdAeropuerto()));
            comboRol.setValue(personal.getRol());

            btnEliminar.setDisable(false);
        } else {
            limpiarFormulario();
        }
    }

    private void limpiarFormulario() {
        this.personalSeleccionado = null;
        labelTituloFormulario.setText("Registrar un nuevo Agente");

        txtNombres.clear();
        txtApellidos.clear();
        txtDni.clear();
        txtCorreo.clear();
        txtContrasena.clear();
        txtContrasena.setDisable(false);
        txtTelefono.clear();
        comboTurno.setValue(null);
        txtIdAeropuerto.clear();
        comboRol.setValue(null);

        btnEliminar.setDisable(true);
        tablaPersonal.getSelectionModel().clearSelection();
    }

    private void mostrarAlerta(Alert.AlertType tipo, String titulo, String contenido) {
        Alert alerta = new Alert(tipo);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(contenido);
        alerta.showAndWait();
    }
}
