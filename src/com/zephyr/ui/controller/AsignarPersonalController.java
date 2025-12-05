package com.zephyr.ui.controller;

import com.zephyr.domain.AsignacionPersonal;
import com.zephyr.domain.Personal;
import com.zephyr.domain.Vuelo;
import com.zephyr.repository.impl.PersonalRepositoryJDBCImpl;
import com.zephyr.service.AsignacionService;
import com.zephyr.service.PersonalService;
import com.zephyr.service.impl.AsignacionServiceImpl;
import com.zephyr.service.impl.PersonalServiceImpl;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;

public class AsignarPersonalController implements Initializable {
    @FXML private Label lblVueloInfo;
    @FXML private ComboBox<Personal> comboEmpleado;
    @FXML private ComboBox<String> comboRolVuelo;
    @FXML private Button btnAsignar, btnCerrar;
    @FXML private TableView<AsignacionPersonal> tablaAsignaciones;
    @FXML private TableColumn<AsignacionPersonal, String> colEmpleado, colRol;

    private final AsignacionService asignacionService;
    private final PersonalService personalService;
    private Vuelo vueloSeleccionado;
    private List<Personal> todosLosEmpleados;

    public AsignarPersonalController() {
        this.asignacionService = new AsignacionServiceImpl();
        var personalRepo = new PersonalRepositoryJDBCImpl();
        this.personalService = new PersonalServiceImpl(personalRepo);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        todosLosEmpleados = personalService.listarTodoElPersonal();
        comboEmpleado.setItems(FXCollections.observableArrayList(todosLosEmpleados));
        comboEmpleado.setConverter(new StringConverter<Personal>() {
            @Override public String toString(Personal p) { return p == null ? null : p.getNombres() + " " + p.getApellidos(); }
            @Override public Personal fromString(String s) { return null; }
        });

        comboRolVuelo.setItems(FXCollections.observableArrayList("Agente Principal", "Agente de Puerta", "Supervisor de Turno", "Encargado de Equipaje"));

        colEmpleado.setCellValueFactory(cellData -> {
            int idP = cellData.getValue().getIdPersonal();
            String nombre = todosLosEmpleados.stream()
                    .filter(p -> p.getIdPersonal() == idP).findFirst()
                    .map(p -> p.getNombres() + " " + p.getApellidos()).orElse("Desconocido");
            return new SimpleStringProperty(nombre);
        });
        colRol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getRolEnAsignacion()));

        btnAsignar.setOnAction(e -> handleAsignar());
        btnCerrar.setOnAction(e -> cerrar());
    }

    public void setDatos(Vuelo vuelo) {
        this.vueloSeleccionado = vuelo;
        if (vuelo != null) {
            lblVueloInfo.setText("Vuelo: " + vuelo.getCodigoVuelo());
            cargarTabla();
        }
    }

    private void cargarTabla() {
        var lista = asignacionService.listarAsignacionesDeVuelo(vueloSeleccionado.getIdVuelo());
        tablaAsignaciones.setItems(FXCollections.observableArrayList(lista));
    }

    private void handleAsignar() {
        if (comboEmpleado.getValue() == null || comboRolVuelo.getValue() == null) return;

        AsignacionPersonal asignacion = new AsignacionPersonal();
        asignacion.setIdPersonal(comboEmpleado.getValue().getIdPersonal());
        asignacion.setIdVuelo(vueloSeleccionado.getIdVuelo());
        asignacion.setRolEnAsignacion(comboRolVuelo.getValue());
        asignacion.setFechaAsignacion(LocalDate.now());

        asignacionService.asignarPersonal(asignacion);

        comboEmpleado.setValue(null);
        comboRolVuelo.setValue(null);
        cargarTabla();
    }

    private void cerrar() {
        ((Stage) btnCerrar.getScene().getWindow()).close();
    }
}
