package com.zephyr.ui.controller;

import com.zephyr.domain.Equipaje;
import com.zephyr.domain.PasajeroPorVuelo;
import com.zephyr.service.EquipajeService;
import com.zephyr.service.ReportService;
import com.zephyr.service.impl.EquipajeServiceImpl;
import com.zephyr.service.impl.ReportServiceImpl;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class EquipajeController implements Initializable {
    @FXML private Label lblTituloPasajero;
    @FXML private ComboBox<String> comboTipo;
    @FXML private TextField txtPeso;
    @FXML private TextField txtDimensiones;
    @FXML private Button btnRegistrar;
    @FXML private Button btnImprimirTag;

    @FXML private TableView<Equipaje> tablaEquipaje;
    @FXML private TableColumn<Equipaje, String> colCodigo;
    @FXML private TableColumn<Equipaje, String> colTipo;
    @FXML private TableColumn<Equipaje, Double> colPeso;
    @FXML private TableColumn<Equipaje, String> colDimensiones;
    @FXML private Button btnCerrar;

    // --- Backend ---
    private final EquipajeService equipajeService;
    private final ReportService reportService;
    private ObservableList<Equipaje> listaEquipaje;
    private int idBoletoActual;

    public EquipajeController() {
        this.equipajeService = new EquipajeServiceImpl();
        this.reportService = new ReportServiceImpl();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        colCodigo.setCellValueFactory(new PropertyValueFactory<>("codigoEquipaje"));
        colTipo.setCellValueFactory(new PropertyValueFactory<>("tipo"));
        colPeso.setCellValueFactory(new PropertyValueFactory<>("peso"));
        colDimensiones.setCellValueFactory(new PropertyValueFactory<>("dimensiones"));

        listaEquipaje = FXCollections.observableArrayList();
        tablaEquipaje.setItems(listaEquipaje);

        tablaEquipaje.getSelectionModel().selectedItemProperty().addListener((obs, o, n) -> {
            btnImprimirTag.setDisable(n == null);
        });

        btnImprimirTag.setOnAction(e -> imprimirTag());

        comboTipo.setItems(FXCollections.observableArrayList("Maleta (Bodega)", "Mochila", "Caja", "Instrumento", "Mascota"));

        btnRegistrar.setOnAction(e -> handleRegistrar());
        btnCerrar.setOnAction(e -> cerrarVentana());
    }

    public void setDatosPasajero(PasajeroPorVuelo pasajero) {
        if (pasajero != null) {
            this.idBoletoActual = pasajero.getIdBoleto();
            lblTituloPasajero.setText("Equipaje de: " + pasajero.getNombres() + " " + pasajero.getApellidos());
            cargarEquipaje();
        }
    }

    private void imprimirTag() {
        Equipaje seleccion = tablaEquipaje.getSelectionModel().getSelectedItem();
        if (seleccion == null) return;

        String ruta = reportService.generarEtiquetaEquipaje(seleccion.getIdEquipaje());

        if (ruta != null) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Etiqueta generada:\n" + ruta);
            alert.setHeaderText("Bag Tag Impreso");
            alert.showAndWait();
        }
    }

    private void cargarEquipaje() {
        var maletas = equipajeService.listarEquipajePorBoleto(idBoletoActual);
        listaEquipaje.setAll(maletas);
    }

    private void handleRegistrar() {
        if (comboTipo.getValue() == null || txtPeso.getText().isEmpty()) {
            mostrarAlerta(Alert.AlertType.WARNING, "Faltan Datos", "Seleccione tipo y peso.");
            return;
        }

        try {
            double peso = Double.parseDouble(txtPeso.getText());

            Equipaje nuevaMaleta = new Equipaje();
            nuevaMaleta.setTipo(comboTipo.getValue());
            nuevaMaleta.setPeso(peso);
            nuevaMaleta.setDimensiones(txtDimensiones.getText());
            nuevaMaleta.setIdBoleto(idBoletoActual);

            equipajeService.registrarEquipaje(nuevaMaleta);

            mostrarAlerta(Alert.AlertType.INFORMATION, "Éxito", "Equipaje registrado correctamente.");

            txtPeso.clear();
            txtDimensiones.clear();
            comboTipo.setValue(null);
            cargarEquipaje();

        } catch (NumberFormatException e) {
            mostrarAlerta(Alert.AlertType.ERROR, "Error", "El peso debe ser un número (ej. 23.5).");
        } catch (Exception e) {
            mostrarAlerta(Alert.AlertType.ERROR, "Error", "No se pudo registrar: " + e.getMessage());
        }
    }

    private void cerrarVentana() {
        Stage stage = (Stage) btnCerrar.getScene().getWindow();
        stage.close();
    }

    private void mostrarAlerta(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}