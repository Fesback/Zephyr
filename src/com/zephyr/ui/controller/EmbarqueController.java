package com.zephyr.ui.controller;

import com.zephyr.domain.PasajeroPorVuelo;
import com.zephyr.domain.Vuelo;
import com.zephyr.repository.BoletoRepository;
import com.zephyr.repository.VueloRepository;
import com.zephyr.repository.impl.BoletoRepositoryJDBCImpl;
import com.zephyr.repository.impl.VueloRepositoryJDBCImpl;
import com.zephyr.service.BoletoService;
import com.zephyr.service.ReportService;
import com.zephyr.service.VueloService;
import com.zephyr.service.impl.BoletoServiceImpl;
import com.zephyr.service.impl.ReportServiceImpl;
import com.zephyr.service.impl.VueloServiceImpl;
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

public class EmbarqueController implements Initializable {

    @FXML private ComboBox<Vuelo> vueloComboBox;
    @FXML private TableView<PasajeroPorVuelo> tablaPasajeros;
    @FXML private TableColumn<PasajeroPorVuelo, String> colNombre;
    @FXML private TableColumn<PasajeroPorVuelo, String> colAsiento;
    @FXML private TableColumn<PasajeroPorVuelo, String> colEstado;
    @FXML private TextField boletoField;
    @FXML private Button verificarButton;
    @FXML private Label contadorLabel;

    @FXML private Label labelManifiesto;
    @FXML private Button btnGenerarPdf;

    //serivces
    private final VueloService vueloService;
    private final BoletoService boletoService;
    private final ReportService  reportService;

    private ObservableList<Vuelo> listaVuelosObservable;
    private ObservableList<PasajeroPorVuelo> listaPasajerosObservable;

    public EmbarqueController() {
        VueloRepository vueloRepository = new VueloRepositoryJDBCImpl();
        this.vueloService = new VueloServiceImpl(vueloRepository);

        BoletoRepository boletoRepository = new BoletoRepositoryJDBCImpl();
        this.boletoService = new BoletoServiceImpl(boletoRepository);

        this.reportService = new ReportServiceImpl();

        this.listaPasajerosObservable = FXCollections.observableArrayList();
        this.listaVuelosObservable = FXCollections.observableArrayList();
    }



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombres"));
        colAsiento.setCellValueFactory(new PropertyValueFactory<>("asiento"));
        colEstado.setCellValueFactory(new PropertyValueFactory<>("estadoEmbarque"));
        tablaPasajeros.setItems(listaPasajerosObservable);

        vueloComboBox.setItems(listaVuelosObservable);
        configurarVueloComboBoxConverter();

        vueloComboBox.setOnAction(event -> handleSeleccionarVuelo());
        verificarButton.setOnAction(event -> handleVerificarBoleto());
        btnGenerarPdf.setOnAction(event -> handleGenerarPdf());

        tablaPasajeros.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            btnGenerarPdf.setDisable(newVal == null);
        });

        cargarVuelosParaSeleccionar();

    }

    private void cargarVuelosParaSeleccionar() {
        List<Vuelo> vuelos = vueloService.getVuelosProgramadosHoy();
        listaVuelosObservable.clear();
        listaVuelosObservable.addAll(vuelos);
    }

    private void handleSeleccionarVuelo() {
        Vuelo vueloSeleccionado = vueloComboBox.getSelectionModel().getSelectedItem();
        if (vueloSeleccionado != null) {
            labelManifiesto.setText("Manifiesto de Pasajeros (Vuelo: " + vueloSeleccionado.getCodigoVuelo() + ")");
            cargarManifiesto(vueloSeleccionado.getIdVuelo());
        } else  {
            labelManifiesto.setText("Manifiesto de Pasajeros");
            listaVuelosObservable.clear();
        }
    }

    private void cargarManifiesto(int idVuelo) {
        List<PasajeroPorVuelo> pasajeros = boletoService.getPasajerosPorVuelo(idVuelo);
        listaPasajerosObservable.clear();
        listaPasajerosObservable.addAll(pasajeros);
        actualizarContador();
    }

    private void handleVerificarBoleto() {
        String codigoBoleto = boletoField.getText().trim();
        if (codigoBoleto.isEmpty()) {
            mostrarAlerta(Alert.AlertType.WARNING, "Campo vacio", "Por favor ingrese un codigo de boleto");
            return;
        }

        String resultado = boletoService.verificarBoleto(codigoBoleto);

        if (resultado.startsWith("EXITO")) {
            mostrarAlerta(Alert.AlertType.INFORMATION, "Exito", resultado);

            Vuelo vueloSeleccionado = vueloComboBox.getSelectionModel().getSelectedItem();
            if (vueloSeleccionado != null) {
                cargarManifiesto(vueloSeleccionado.getIdVuelo());
            }
            boletoField.clear();
        } else {
            mostrarAlerta(Alert.AlertType.ERROR, "Resultado", resultado);
        }
    }

    private void handleGenerarPdf() {
        PasajeroPorVuelo pasajeroSel = tablaPasajeros.getSelectionModel().getSelectedItem();

        if (pasajeroSel == null) {
            mostrarAlerta(Alert.AlertType.WARNING, "Sin seleccion", "Por favor seleccione un pasajero d ela tabla");
            return;
        }

        System.out.println("Generando PDF para el boleto ID: " + pasajeroSel.getIdBoleto());
        String rutaArchivo = reportService.generarBoardingPass(pasajeroSel.getIdBoleto());

        if (rutaArchivo != null) {
            mostrarAlerta(Alert.AlertType.INFORMATION, "Exito", "PDF generado exitosamente.\n\nGuardado en: " + rutaArchivo);
        }  else {
            mostrarAlerta(Alert.AlertType.ERROR, "Error", "No se pudo generar el PDF. Revisar consola");
        }
    }

    private void actualizarContador() {
        long abordo = listaPasajerosObservable.stream().filter(p -> p.getEstadoEmbarque().equals("Verificado en puerta") ||
               p.getEstadoEmbarque().equals("Abordo")).count();

        int total = listaPasajerosObservable.size();
        contadorLabel.setText("Pasajeros Abordo: " + abordo + " / " + total);
    }

    private void configurarVueloComboBoxConverter() {
        vueloComboBox.setConverter(new StringConverter<Vuelo>() {

            @Override
            public String toString(Vuelo vuelo) {
                return (vuelo == null) ? null :
                        vuelo.getCodigoVuelo() + " (" + vuelo.getOrigenIata() + " -> " + vuelo.getDestinoIata() + ")";
            }
            @Override
            public Vuelo fromString(String string) {
                return null;
            }
        });
    }

    private void mostrarAlerta(Alert.AlertType tipo, String titulo, String contenido) {
        Alert alerta = new Alert(tipo);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(contenido);
        alerta.showAndWait();
    }
}
