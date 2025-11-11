package com.zephyr.ui.controller;

import com.zephyr.domain.Boleto;
import com.zephyr.domain.Pasajero;
import com.zephyr.repository.BoletoRepository;
import com.zephyr.repository.PasajeroRepository;
import com.zephyr.repository.impl.BoletoRepositoryJDBCImpl;
import com.zephyr.repository.impl.PasajeroRepositoryJDBCImpl;
import com.zephyr.service.EmailService;
import com.zephyr.service.ReportService;
import com.zephyr.service.impl.EmailServiceImpl;
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
    @FXML private Button btnGenerarEnviar;
    @FXML private TextField emailField;

    private final ReportService reportService;
    private final BoletoRepository boletoRepository;
    private final EmailService emailService;
    private final PasajeroRepository pasajeroRepository;

    public ReportesController() {
        this.reportService = new ReportServiceImpl();
        this.emailService = new EmailServiceImpl();
        this.boletoRepository = new BoletoRepositoryJDBCImpl();
        this.pasajeroRepository = new PasajeroRepositoryJDBCImpl();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        btnGenerarEnviar.setOnAction(event -> handleGenerarYEnviar());
        boletoField.setOnAction(event -> autocompletarEmail());
    }

    private void autocompletarEmail() {
        String codigoBoleto = boletoField.getText().trim();
        if (codigoBoleto.isEmpty()) return;

        Optional<Boleto> boletoOpt = boletoRepository.findBoletoByCodigo(codigoBoleto);
        if (boletoOpt.isPresent()) {
            Optional<Pasajero> pasajeroOpt = pasajeroRepository.findById(boletoOpt.get().getIdPasajero());
            if (pasajeroOpt.isPresent() && pasajeroOpt.get().getCorreo() != null) {
                emailField.setText(pasajeroOpt.get().getCorreo());
            } else {
                emailField.clear();
                emailField.setPromptText("Pasajero no tiene email registrado");
            }
        } else {
            emailField.clear();
            emailField.setPromptText("Email del destinatario");
        }
    }

    private void handleGenerarYEnviar() {
        String codigoBoleto = boletoField.getText().trim();
        String emailDestinatario = emailField.getText().trim();

        if (codigoBoleto.isEmpty() || emailDestinatario.isEmpty()) {
            mostrarAlerta(Alert.AlertType.WARNING, "Campos Vacíos", "Por favor ingrese un código de boleto Y un email.");
            return;
        }

        Optional<Boleto> boletoOpt = boletoRepository.findBoletoByCodigo(codigoBoleto);
        if (boletoOpt.isEmpty()) {
            mostrarAlerta(Alert.AlertType.ERROR, "Error", "No se encontró ningún boleto con el código: " + codigoBoleto);
            return;
        }
        int idBoleto = boletoOpt.get().getIdBoleto();

        System.out.println("Generando PDF para el boleto ID: " + idBoleto);
        String rutaArchivo = reportService.generarBoardingPass(idBoleto);

        if (rutaArchivo == null) {
            mostrarAlerta(Alert.AlertType.ERROR, "Error de PDF", "No se pudo generar el PDF. Revise la consola.");
            return;
        }

        System.out.println("Enviando PDF a: " + emailDestinatario);
        String asunto = "Su Tarjeta de Embarque - Vuelo " + boletoOpt.get().getCodigoBoleto();
        String cuerpo = "¡Gracias por volar con Zephyr!\n\nAdjuntamos su tarjeta de embarque (Boarding Pass) para su próximo vuelo.\n\nBuen viaje.";

        try {
            emailService.enviarEmailConAdjunto(emailDestinatario, asunto, cuerpo, rutaArchivo);
            mostrarAlerta(Alert.AlertType.INFORMATION, "Éxito", "PDF generado y enviado exitosamente a:\n" + emailDestinatario);
            boletoField.clear();
            emailField.clear();
        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlerta(Alert.AlertType.ERROR, "Error de Email", "Se generó el PDF, pero no se pudo enviar el correo. Revise la consola.");
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