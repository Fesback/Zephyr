package com.zephyr.ui.controller;

import com.zephyr.domain.Aerolinea;
import com.zephyr.repository.AerolineaRepository;
import com.zephyr.repository.impl.AerolineaJDBCImpl;
import com.zephyr.service.AerolinaService;
import com.zephyr.service.impl.AerolineaServiceImpl;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class ConfigController implements Initializable{
        @FXML private TableView<Aerolinea> tablaAerolineas;
        @FXML private TableColumn<Aerolinea, Integer> colId;
        @FXML private TableColumn<Aerolinea, String> colNombre;
        @FXML private TableColumn<Aerolinea, String> colIata;
        @FXML private TableColumn<Aerolinea, String> colPais;
        @FXML private TableColumn<Aerolinea, String> colTelefono;

        @FXML private Label labelTituloFormulario;
        @FXML private TextField txtNombre;
        @FXML private TextField txtIata;
        @FXML private TextField txtPais;
        @FXML private TextField txtTelefono;
        @FXML private Button btnGuardar;
        @FXML private Button btnEliminar;
        @FXML private Button btnLimpiar;

        private final AerolinaService aerolineaService;
        private ObservableList<Aerolinea> listaAerolineasObservable;
        private Aerolinea aerolineaSeleccionada;


        public ConfigController() {
            this.aerolineaService = new AerolineaServiceImpl();
        }

        @Override
        public void initialize(URL url, ResourceBundle resourceBundle) {
            configurarTabla();

            tablaAerolineas.getSelectionModel().selectedItemProperty().addListener(
                    (obs, oldVal, newVal) -> seleccionarAerolinea(newVal)
            );

            btnGuardar.setOnAction(event -> handleGuardar());
            btnEliminar.setOnAction(event -> handleEliminar());
            btnLimpiar.setOnAction(event -> limpiarFormulario());

            cargarDatos();
        }

        private void configurarTabla() {
            colId.setCellValueFactory(new PropertyValueFactory<>("idAerolinea"));
            colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
            colIata.setCellValueFactory(new PropertyValueFactory<>("codigoIata"));
            colPais.setCellValueFactory(new PropertyValueFactory<>("paisOrigen"));
            colTelefono.setCellValueFactory(new PropertyValueFactory<>("telefonoContacto"));

            listaAerolineasObservable = FXCollections.observableArrayList();
            tablaAerolineas.setItems(listaAerolineasObservable);
        }

        private void cargarDatos() {
            List<Aerolinea> aerolineas = aerolineaService.listarTodas();
            listaAerolineasObservable.clear();
            listaAerolineasObservable.addAll(aerolineas);
        }

        private void handleGuardar() {
            String nombre = txtNombre.getText();
            String iata = txtIata.getText();
            String pais = txtPais.getText();
            String telefono = txtTelefono.getText();

            if (nombre.isEmpty() || iata.isEmpty()) {
                mostrarAlerta(Alert.AlertType.WARNING, "Campos Vacíos", "Nombre y Código IATA son obligatorios.");
                return;
            }

            if (aerolineaSeleccionada == null) {
                Aerolinea nueva = new Aerolinea(0, nombre, iata, pais, telefono);
                aerolineaService.registrarAerolinea(nueva);
                mostrarAlerta(Alert.AlertType.INFORMATION, "Éxito", "Aerolínea registrada.");
            } else {
                aerolineaSeleccionada.setNombre(nombre);
                aerolineaSeleccionada.setCodigoIata(iata);
                aerolineaSeleccionada.setPaisOrigen(pais);
                aerolineaSeleccionada.setTelefonoContacto(telefono);

                aerolineaService.actualizarAerolinea(aerolineaSeleccionada);
                mostrarAlerta(Alert.AlertType.INFORMATION, "Éxito", "Aerolínea actualizada.");
            }
            cargarDatos();
            limpiarFormulario();
        }

        private void handleEliminar() {
            if (aerolineaSeleccionada == null) return;

            aerolineaService.eliminarAerolinea(aerolineaSeleccionada.getIdAerolinea());
            mostrarAlerta(Alert.AlertType.INFORMATION, "Éxito", "Aerolínea eliminada.");
            cargarDatos();
            limpiarFormulario();
        }

        private void seleccionarAerolinea(Aerolinea aerolinea) {
            if (aerolinea != null) {
                this.aerolineaSeleccionada = aerolinea;
                labelTituloFormulario.setText("Editar Aerolínea");
                txtNombre.setText(aerolinea.getNombre());
                txtIata.setText(aerolinea.getCodigoIata());
                txtPais.setText(aerolinea.getPaisOrigen());
                txtTelefono.setText(aerolinea.getTelefonoContacto());

                btnEliminar.setDisable(false);
                btnGuardar.setText("Actualizar");
            } else {
                limpiarFormulario();
            }
        }

        private void limpiarFormulario() {
            this.aerolineaSeleccionada = null;
            labelTituloFormulario.setText("Nueva Aerolínea");
            txtNombre.clear();
            txtIata.clear();
            txtPais.clear();
            txtTelefono.clear();

            btnEliminar.setDisable(true);
            btnGuardar.setText("Guardar");
            tablaAerolineas.getSelectionModel().clearSelection();
        }

        private void mostrarAlerta(Alert.AlertType tipo, String titulo, String contenido) {
            Alert alerta = new Alert(tipo);
            alerta.setTitle(titulo);
            alerta.setHeaderText(null);
            alerta.setContentText(contenido);
            alerta.showAndWait();
        }
    }