package com.zephyr.ui.controller;

import com.zephyr.domain.Aerolinea;
import com.zephyr.domain.Aeropuerto;
import com.zephyr.domain.Avion;
import com.zephyr.domain.VueloRegistro;
import com.zephyr.repository.impl.VueloRepositoryJDBCImpl;
import com.zephyr.service.AerolinaService;
import com.zephyr.service.AeropuertoService;
import com.zephyr.service.AvionService;
import com.zephyr.service.VueloService;
import com.zephyr.service.impl.AerolineaServiceImpl;
import com.zephyr.service.impl.AeropuertoServiceImpl;
import com.zephyr.service.impl.AvionServiceImpl;
import com.zephyr.service.impl.VueloServiceImpl;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.ResourceBundle;

public class RegistrarVueloController implements Initializable {
    @FXML
    private TextField txtCodigo;

    @FXML private ComboBox<Aerolinea> comboAerolinea;
    @FXML private ComboBox<Avion> comboAvion;
    @FXML private ComboBox<Aeropuerto> comboOrigen;
    @FXML private ComboBox<Aeropuerto> comboDestino;

    @FXML private DatePicker dateSalida;
    @FXML private TextField timeSalida;

    @FXML private DatePicker dateLlegada;
    @FXML private TextField timeLlegada;
    @FXML private Button btnGuardar;
    @FXML private Button btnCancelar;

    private final VueloService vueloService;
    private final AerolinaService aerolineaService;
    private final AvionService avionService;
    private final AeropuertoService aeropuertoService;

    public RegistrarVueloController() {
        this.vueloService = new VueloServiceImpl(new VueloRepositoryJDBCImpl());
        this.aerolineaService = new AerolineaServiceImpl();
        this.avionService = new AvionServiceImpl();
        this.aeropuertoService = new AeropuertoServiceImpl();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        cargarCatalogos();
        configurarConvertidores();

        btnGuardar.setOnAction(event -> handleGuardarVuelo());
        btnCancelar.setOnAction(event -> cerrarVentana());
    }

    private void cargarCatalogos() {
        comboAerolinea.setItems(FXCollections.observableArrayList(aerolineaService.listarTodas()));
        comboAvion.setItems(FXCollections.observableArrayList(avionService.listarTodos()));

        var aeropuertos = FXCollections.observableArrayList(aeropuertoService.listarTodos());
        comboOrigen.setItems(aeropuertos);
        comboDestino.setItems(FXCollections.observableArrayList(aeropuertos));
    }

    private void configurarConvertidores() {
        comboAerolinea.setConverter(new StringConverter<Aerolinea>() {
            @Override
            public String toString(Aerolinea a) {
                return a == null ? null : a.getNombre();
            }

            @Override
            public Aerolinea fromString(String s) {
                return null;
            }
        });

        comboAvion.setConverter(new StringConverter<Avion>() {
            @Override
            public String toString(Avion a) {
                return a == null ? null : a.getMatricula();
            }

            @Override
            public Avion fromString(String s) {
                return null;
            }
        });

        StringConverter<Aeropuerto> aeroConverter = new StringConverter<Aeropuerto>() {
            @Override
            public String toString(Aeropuerto a) {
                return a == null ? null : a.getCodigoIata() + " - " + a.getNombre();
            }
            @Override
            public Aeropuerto fromString(String s) {
                return null;
            }
        };
        comboOrigen.setConverter(aeroConverter);
        comboDestino.setConverter(aeroConverter);
    }

    private void handleGuardarVuelo() {
        try {
            if (txtCodigo.getText().isEmpty() || comboAerolinea.getValue() == null ||
                    comboAvion.getValue() == null || comboOrigen.getValue() == null ||
                    comboDestino.getValue() == null || dateSalida.getValue() == null ||
                    timeSalida.getText().isEmpty() || dateLlegada.getValue() == null ||
                    timeLlegada.getText().isEmpty()) {

                mostrarAlerta(Alert.AlertType.WARNING, "Campos Incompletos", "Por favor llene todos los campos.");
                return;
            }

            LocalDateTime fechaHoraSalida = parsearFechaHora(dateSalida.getValue(), timeSalida.getText());
            LocalDateTime fechaHoraLlegada = parsearFechaHora(dateLlegada.getValue(), timeLlegada.getText());

            VueloRegistro nuevoVuelo = new VueloRegistro(
                    txtCodigo.getText().toUpperCase(),
                    fechaHoraSalida,
                    fechaHoraLlegada,
                    comboAerolinea.getValue().getIdAerolinea(),
                    comboAvion.getValue().getIdAvion(),
                    comboOrigen.getValue().getIdAeropuerto(),
                    comboDestino.getValue().getIdAeropuerto(),
                    1
            );

            vueloService.registrarVuelo(nuevoVuelo);

            mostrarAlerta(Alert.AlertType.INFORMATION, "Éxito", "Vuelo programado correctamente.");
            cerrarVentana();

        } catch (DateTimeParseException e) {
            mostrarAlerta(Alert.AlertType.ERROR, "Formato de Hora Incorrecto", "Use el formato HH:mm (ej. 14:30)");
        } catch (IllegalArgumentException e) {
            mostrarAlerta(Alert.AlertType.ERROR, "Error de Validación", e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlerta(Alert.AlertType.ERROR, "Error Interno", "No se pudo guardar el vuelo.");
        }
    }

    private LocalDateTime parsearFechaHora(LocalDate fecha, String horaStr) {
        LocalTime hora = LocalTime.parse(horaStr);
        return LocalDateTime.of(fecha, hora);
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

