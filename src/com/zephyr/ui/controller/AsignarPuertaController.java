package com.zephyr.ui.controller;

import com.zephyr.domain.PuertaEmbarque;
import com.zephyr.domain.Vuelo;
import com.zephyr.service.PuertaService;
import com.zephyr.service.impl.PuertaServiceImpl;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class AsignarPuertaController implements Initializable {

    @FXML private Label lblVueloInfo;
    @FXML private ComboBox<PuertaEmbarque> comboPuertas;
    @FXML private Button btnGuardar;
    @FXML private Button btnCancelar;

    private final PuertaService puertaService;
    private Vuelo vueloSeleccionado;
    private DashboardController dashboardController;

    public AsignarPuertaController() {
        this.puertaService = new PuertaServiceImpl();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        comboPuertas.setConverter(new StringConverter<PuertaEmbarque>() {
            @Override
            public String toString(PuertaEmbarque p) {
                return p == null ? null : p.getCodigoPuerta() + " (" + p.getUbicacionTerminal() + ")";
            }

            @Override
            public PuertaEmbarque fromString(String s) {
                return null;
            }
        });

        btnGuardar.setOnAction(e -> handleGuardar());
        btnCancelar.setOnAction(e -> cerrar());

    }

    public void setDatos(Vuelo vuelo, DashboardController parent) {
        this.vueloSeleccionado = vuelo;
        this.dashboardController = parent;

        if (vuelo != null) {
            lblVueloInfo.setText("Vuelo: " + vuelo.getCodigoVuelo() +
                    " | Origen: " + vuelo.getOrigenIata());

            cargarPuertasDelAeropuerto(vuelo.getIdAeropuertoOrigen());
        }
    }

    private void cargarPuertasDelAeropuerto(int idAeropuertoOrigen) {
        try {
            System.out.println("Buscando puertas para el Aeropuerto ID: " + idAeropuertoOrigen);

            List<PuertaEmbarque> puertasDisponibles = puertaService.obtenerPuertasDisponibles(idAeropuertoOrigen);

            comboPuertas.setItems(FXCollections.observableArrayList(puertasDisponibles));

            if (puertasDisponibles.isEmpty()) {
                comboPuertas.setPromptText("No hay puertas registradas para este aeropuerto");
            } else {
                comboPuertas.setPromptText("Seleccione una puerta...");
            }

        } catch (Exception e) {
            System.err.println("Error al cargar puertas: " + e.getMessage());
        }
    }

    private void handleGuardar() {
        if (comboPuertas.getValue() == null) {
            mostrarAlerta("Por favor seleccione una puerta.");
            return;
        }

        if (vueloSeleccionado != null) {
            puertaService.asignarPuertaAVuelo(vueloSeleccionado.getIdVuelo(), comboPuertas.getValue().getIdPuertaEmbarque());

            if (dashboardController != null) {
                dashboardController.refrescarDashboard();
            }

            mostrarInfo("Puerta " + comboPuertas.getValue().getCodigoPuerta() + " asignada correctamente.");
            cerrar();
        }
    }

    private void cerrar() {
        Stage stage = (Stage) btnCancelar.getScene().getWindow();
        stage.close();
    }

    private void mostrarAlerta(String msg) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Aviso");
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }

    private void mostrarInfo(String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Éxito");
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
