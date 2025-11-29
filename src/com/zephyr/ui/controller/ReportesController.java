package com.zephyr.ui.controller;

import com.zephyr.domain.Boleto;
import com.zephyr.domain.Pasajero;
import com.zephyr.domain.Vuelo;
import com.zephyr.repository.BoletoRepository;
import com.zephyr.repository.PasajeroRepository;
import com.zephyr.repository.VueloRepository;
import com.zephyr.repository.impl.BoletoRepositoryJDBCImpl;
import com.zephyr.repository.impl.PasajeroRepositoryJDBCImpl;
import com.zephyr.repository.impl.VueloRepositoryJDBCImpl;
import com.zephyr.service.EmailService;
import com.zephyr.service.ReportService;
import com.zephyr.service.VueloService;
import com.zephyr.service.impl.EmailServiceImpl;
import com.zephyr.service.impl.ReportServiceImpl;
import com.zephyr.service.impl.VueloServiceImpl;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.util.StringConverter;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class ReportesController implements Initializable {

    @FXML private TextField boletoField;
    @FXML private Button btnGenerarEnviar;
    @FXML private TextField emailField;

    @FXML private ComboBox<Vuelo> comboVuelosReporte;
    @FXML private Button btnGenerarManifiesto;

    private final ReportService reportService;
    private final VueloService vueloService;
    private final BoletoRepository boletoRepository;
    private final EmailService emailService;
    private final PasajeroRepository pasajeroRepository;

    public ReportesController() {
        this.reportService = new ReportServiceImpl();
        this.emailService = new EmailServiceImpl();
        this.boletoRepository = new BoletoRepositoryJDBCImpl();
        this.pasajeroRepository = new PasajeroRepositoryJDBCImpl();
        VueloRepository vueloRepo = new VueloRepositoryJDBCImpl();
        this.vueloService = new VueloServiceImpl(vueloRepo);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        btnGenerarEnviar.setOnAction(event -> handleGenerarYEnviar());
        boletoField.setOnAction(event -> autocompletarEmail());

        setupComboVuelos();
        btnGenerarManifiesto.setOnAction(event -> handleGenerarManifiesto());

        comboVuelosReporte.setOnAction(e -> {
            btnGenerarManifiesto.setDisable(comboVuelosReporte.getValue() == null);
        });
    }

    private void setupComboVuelos() {
        var vuelos = vueloService.getVuelosProgramadosHoy();
        comboVuelosReporte.setItems(FXCollections.observableArrayList(vuelos));
        comboVuelosReporte.setConverter(new StringConverter<Vuelo>() {
            @Override
            public String toString(Vuelo v) {
                return (v == null) ? null : v.getCodigoVuelo() + " (" + v.getOrigenIata() + " -> " + v.getDestinoIata() + ")";
            }
            @Override
            public Vuelo fromString(String s) { return null; }
        });
    }

    private void handleGenerarManifiesto() {
        Vuelo vueloSel = comboVuelosReporte.getValue();
        if (vueloSel == null) return;
        System.out.println("Generando Manifiesto para vuelo ID: " + vueloSel.getIdVuelo());

        String ruta = reportService.generarManifiestoVuelo(vueloSel.getIdVuelo());

        if (ruta != null) {
            mostrarAlerta(Alert.AlertType.INFORMATION, "Reporte Generado", "Manifiesto guardado en:\n" + ruta);
        } else {
            mostrarAlerta(Alert.AlertType.ERROR, "Error", "No se pudo generar el reporte.");
        }
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