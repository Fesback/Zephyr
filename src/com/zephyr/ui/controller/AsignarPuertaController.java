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
        try {
            var puertas = puertaService.obtenerPuertasDisponibles(1);
            comboPuertas.setItems(FXCollections.observableArrayList(puertas));
        } catch (Exception e) {
            System.err.println("Error al cargar puertas: " + e.getMessage());
        }

        comboPuertas.setConverter(new StringConverter<PuertaEmbarque>() {
            @Override public String toString(PuertaEmbarque p) {
                return p == null ? null : p.getCodigoPuerta() + " (" + p.getUbicacionTerminal() + ")";
            }
            @Override public PuertaEmbarque fromString(String s) { return null; }
        });

        btnGuardar.setOnAction(e -> handleGuardar());
        btnCancelar.setOnAction(e -> cerrar());
    }

    public void setDatos(Vuelo vuelo, DashboardController parent) {
        this.vueloSeleccionado = vuelo;
        this.dashboardController = parent;

        if (vuelo != null) {
            lblVueloInfo.setText("Vuelo: " + vuelo.getCodigoVuelo() + " | Destino: " + vuelo.getDestinoIata());
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
                System.out.println("Puerta asignada. Refrescando...");
            }

            // Mostramos éxito y cerramos
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Puerta " + comboPuertas.getValue().getCodigoPuerta() + " asignada correctamente.");
            alert.showAndWait();
            cerrar();
        }
    }

    private void cerrar() {
        Stage stage = (Stage) btnCancelar.getScene().getWindow();
        stage.close();
    }

    private void mostrarAlerta(String msg) {
        new Alert(Alert.AlertType.WARNING, msg).show();
    }
}
