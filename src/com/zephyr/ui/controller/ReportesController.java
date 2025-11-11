package com.zephyr.ui.controller;

import com.zephyr.domain.Boleto;
import com.zephyr.repository.BoletoRepository;
import com.zephyr.repository.impl.BoletoRepositoryJDBCImpl;
import com.zephyr.service.ReportService;
import com.zephyr.service.impl.ReportServiceImpl;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class ReportesController implements Initializable {

    @FXML private TextField boletoField;
    @FXML private Button btnGenerarPdf;

    private final ReportService reportService;
    private final BoletoRepository boletoRepository;

    public ReportesController() {
        this.reportService = new ReportServiceImpl();
        this.boletoRepository = new BoletoRepositoryJDBCImpl();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        btnGenerarPdf.setOnAction(event -> handleGenerarPdf());
    }

    private void handleGenerarPdf() {
        String codigoBoleto = boletoField.getText().trim();

        if (codigoBoleto.isEmpty()) {
            mostrarAlerta(Alert.AlertType.WARNING, "Campo vacio", "Por favor ingresar un codigo de boleto (ej: AXD456)");
            return;
        }

        Optional<Boleto> boletoOpt =  boletoRepository.findBoletoByCodigo(codigoBoleto);

        if (boletoOpt.isEmpty()) {
            mostrarAlerta(Alert.AlertType.ERROR, "Error", "no se encontro ningun boleto con el codigo: " + codigoBoleto);
            return;
        }

        int idBoleto  = boletoOpt.get().getIdBoleto();

        System.out.println("Generando PDF para el boleto ID: "  + idBoleto);
        String rutaArchivo = reportService.generarBoardingPass(idBoleto);

        if (rutaArchivo != null) {
            mostrarAlerta(Alert.AlertType.INFORMATION, "Exito", "PDF generado exitosamente");
            boletoField.clear();
        } else {
            mostrarAlerta(Alert.AlertType.ERROR, "Error", "No se pudo generar PDF");
        }
    }

    private  void mostrarAlerta(Alert.AlertType type, String titulo, String contenido) {
        Alert alerta = new Alert(type);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(contenido);
        alerta.showAndWait();
    }
}
