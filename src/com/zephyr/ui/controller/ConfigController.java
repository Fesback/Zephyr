package com.zephyr.ui.controller;

import com.zephyr.domain.*;
import com.zephyr.repository.AerolineaRepository;
import com.zephyr.repository.impl.AerolineaJDBCImpl;
import com.zephyr.service.*;
import com.zephyr.service.impl.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.StringConverter;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class ConfigController implements Initializable{
    @FXML private TableView<Aerolinea> tablaAerolineas;
    @FXML private TableColumn<Aerolinea, Integer> colId;
    @FXML private TableColumn<Aerolinea, String> colNombre, colIata, colPais, colTelefono;
    @FXML private Label labelTituloFormulario;
    @FXML private TextField txtNombre, txtIata, txtPais, txtTelefono;
    @FXML private Button btnGuardar, btnEliminar, btnLimpiar;

    // ==================== AEROPUERTOS  ====================
    @FXML private TableView<Aeropuerto> tablaAeropuertos;
    @FXML private TableColumn<Aeropuerto, Integer> colAeroId;
    @FXML private TableColumn<Aeropuerto, String> colAeroNombre, colAeroIata;
    @FXML private TableColumn<Aeropuerto, Integer> colAeroCiudad;
    @FXML private Label lblFormAeropuerto;
    @FXML private TextField txtAeroNombre, txtAeroIata, txtAeroDireccion;
    @FXML private ComboBox<Ciudad> comboAeroCiudad;
    @FXML private Button btnAeroGuardar, btnAeroEliminar, btnAeroLimpiar;

    // ==================== AVIONES ====================
    @FXML private TableView<Avion> tablaAviones;
    @FXML private TableColumn<Avion, Integer> colAvionId;
    @FXML private TableColumn<Avion, String> colAvionMatricula, colAvionEstado;
    @FXML private TableColumn<Avion, Integer> colAvionModelo, colAvionAerolinea;
    @FXML private Label lblFormAvion;
    @FXML private TextField txtAvionMatricula, txtAvionAnio;
    @FXML private ComboBox<String> comboAvionEstado;
    @FXML private ComboBox<ModeloAvion> comboAvionModelo;
    @FXML private ComboBox<Aerolinea> comboAvionAerolinea;
    @FXML private Button btnAvionGuardar, btnAvionEliminar, btnAvionLimpiar;


    // ==================== BACKEND  ====================
    private final AerolinaService aerolineaService;
    private final AeropuertoService aeropuertoService;
    private final CiudadService ciudadService;
    private final AvionService avionService;
    private final ModeloAvionService modeloAvionService;

    // ==================== LISTAS ====================
    private ObservableList<Aerolinea> listaAerolineas;
    private ObservableList<Aeropuerto> listaAeropuertos;
    private ObservableList<Ciudad> listaCiudades;
    private ObservableList<Avion> listaAviones;
    private ObservableList<ModeloAvion> listaModelos;

    // ==================== SELECCIONES ====================
    private Aerolinea aerolineaSel;
    private Aeropuerto aeropuertoSel;
    private Avion avionSel;

    public ConfigController() {
        this.aerolineaService = new AerolineaServiceImpl();
        this.aeropuertoService = new AeropuertoServiceImpl();
        this.ciudadService = new CiudadServiceImpl();
        this.avionService = new AvionServiceImpl();
        this.modeloAvionService = new ModeloAvionServiceImpl();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setupAerolineas();
        setupAeropuertos();
        setupAviones();
    }


    // ====================  ====================
    private void setupAerolineas() {
        colId.setCellValueFactory(new PropertyValueFactory<>("idAerolinea"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colIata.setCellValueFactory(new PropertyValueFactory<>("codigoIata"));
        colPais.setCellValueFactory(new PropertyValueFactory<>("paisOrigen"));
        colTelefono.setCellValueFactory(new PropertyValueFactory<>("telefonoContacto"));
        listaAerolineas = FXCollections.observableArrayList();
        tablaAerolineas.setItems(listaAerolineas);
        tablaAerolineas.getSelectionModel().selectedItemProperty().addListener((obs, o, n) -> selAerolinea(n));
        btnGuardar.setOnAction(e -> guardarAerolinea());
        btnEliminar.setOnAction(e -> eliminarAerolinea());
        btnLimpiar.setOnAction(e -> limpiarAerolinea());
        cargarAerolineas();
    }
    private void cargarAerolineas() { listaAerolineas.setAll(aerolineaService.listarTodas()); }
    private void selAerolinea(Aerolinea a) {
        if (a != null) {
            aerolineaSel = a;
            labelTituloFormulario.setText("Editar Aerolínea");
            txtNombre.setText(a.getNombre());
            txtIata.setText(a.getCodigoIata());
            txtPais.setText(a.getPaisOrigen());
            txtTelefono.setText(a.getTelefonoContacto());
            btnEliminar.setDisable(false);
            btnGuardar.setText("Actualizar");
        } else limpiarAerolinea();
    }
    private void guardarAerolinea() {
        if (txtNombre.getText().isEmpty()) return;
        if (aerolineaSel == null) {
            Aerolinea nueva = new Aerolinea(0, txtNombre.getText(), txtIata.getText(), txtPais.getText(), txtTelefono.getText());
            aerolineaService.registrarAerolinea(nueva);
        } else {
            aerolineaSel.setNombre(txtNombre.getText());
            aerolineaSel.setCodigoIata(txtIata.getText());
            aerolineaSel.setPaisOrigen(txtPais.getText());
            aerolineaSel.setTelefonoContacto(txtTelefono.getText());
            aerolineaService.actualizarAerolinea(aerolineaSel);
        }
        cargarAerolineas();
        limpiarAerolinea();
    }
    private void eliminarAerolinea() {
        if (aerolineaSel != null) aerolineaService.eliminarAerolinea(aerolineaSel.getIdAerolinea());
        cargarAerolineas();
        limpiarAerolinea();
    }
    private void limpiarAerolinea() {
        aerolineaSel = null;
        labelTituloFormulario.setText("Nueva Aerolínea");
        txtNombre.clear(); txtIata.clear(); txtPais.clear(); txtTelefono.clear();
        btnEliminar.setDisable(true);
        btnGuardar.setText("Guardar");
        tablaAerolineas.getSelectionModel().clearSelection();
    }

    // ====================  ====================
    private void setupAeropuertos() {
        colAeroId.setCellValueFactory(new PropertyValueFactory<>("idAeropuerto"));
        colAeroNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colAeroIata.setCellValueFactory(new PropertyValueFactory<>("codigoIata"));
        colAeroCiudad.setCellValueFactory(new PropertyValueFactory<>("idCiudad"));
        listaAeropuertos = FXCollections.observableArrayList();
        tablaAeropuertos.setItems(listaAeropuertos);

        listaCiudades = FXCollections.observableArrayList(ciudadService.listarTodas());
        comboAeroCiudad.setItems(listaCiudades);
        comboAeroCiudad.setConverter(new StringConverter<Ciudad>() {
            @Override public String toString(Ciudad c) {
                return c == null ? null : c.getNombre();
            }
            @Override public Ciudad fromString(String s) {
                return null;
            }
        });

        tablaAeropuertos.getSelectionModel().selectedItemProperty().addListener((obs, o, n) -> selAeropuerto(n));
        btnAeroGuardar.setOnAction(e -> guardarAeropuerto());
        btnAeroEliminar.setOnAction(e -> eliminarAeropuerto());
        btnAeroLimpiar.setOnAction(e -> limpiarAeropuerto());
        cargarAeropuertos();
    }
    private void cargarAeropuertos() { listaAeropuertos.setAll(aeropuertoService.listarTodos()); }
    private void selAeropuerto(Aeropuerto a) {
        if (a != null) {
            aeropuertoSel = a;
            lblFormAeropuerto.setText("Editar Aeropuerto");
            txtAeroNombre.setText(a.getNombre());
            txtAeroIata.setText(a.getCodigoIata());
            txtAeroDireccion.setText(a.getDireccion());
            listaCiudades.stream().filter(c -> c.getIdCiudad() == a.getIdCiudad()).findFirst().ifPresent(comboAeroCiudad::setValue);
            btnAeroEliminar.setDisable(false);
            btnAeroGuardar.setText("Actualizar");
        } else limpiarAeropuerto();
    }
    private void guardarAeropuerto() {
        if (txtAeroNombre.getText().isEmpty() || comboAeroCiudad.getValue() == null) return;
        if (aeropuertoSel == null) {
            Aeropuerto nuevo = new Aeropuerto(0, txtAeroNombre.getText(), txtAeroIata.getText(), txtAeroDireccion.getText(), comboAeroCiudad.getValue().getIdCiudad());
            aeropuertoService.registrarAeropuerto(nuevo);
        } else {
            aeropuertoSel.setNombre(txtAeroNombre.getText());
            aeropuertoSel.setCodigoIata(txtAeroIata.getText());
            aeropuertoSel.setDireccion(txtAeroDireccion.getText());
            aeropuertoSel.setIdCiudad(comboAeroCiudad.getValue().getIdCiudad());
            aeropuertoService.actualizarAeropuerto(aeropuertoSel);
        }
        cargarAeropuertos();
        limpiarAeropuerto();
    }
    private void eliminarAeropuerto() {
        if (aeropuertoSel != null) aeropuertoService.eliminarAeropuerto(aeropuertoSel.getIdAeropuerto());
        cargarAeropuertos();
        limpiarAeropuerto();
    }
    private void limpiarAeropuerto() {
        aeropuertoSel = null;
        lblFormAeropuerto.setText("Nuevo Aeropuerto");
        txtAeroNombre.clear(); txtAeroIata.clear(); txtAeroDireccion.clear();
        comboAeroCiudad.setValue(null);
        btnAeroEliminar.setDisable(true);
        btnAeroGuardar.setText("Guardar");
        tablaAeropuertos.getSelectionModel().clearSelection();
    }

    // ==================== ====================
    private void setupAviones() {
        colAvionId.setCellValueFactory(new PropertyValueFactory<>("idAvion"));
        colAvionMatricula.setCellValueFactory(new PropertyValueFactory<>("matricula"));
        colAvionEstado.setCellValueFactory(new PropertyValueFactory<>("estadoOperativo"));
        colAvionModelo.setCellValueFactory(new PropertyValueFactory<>("idModeloAvion"));
        colAvionAerolinea.setCellValueFactory(new PropertyValueFactory<>("idAerolinea"));

        listaAviones = FXCollections.observableArrayList();
        tablaAviones.setItems(listaAviones);

        comboAvionEstado.setItems(FXCollections.observableArrayList("Operativo", "Mantenimiento", "Fuera de Servicio"));

        listaModelos = FXCollections.observableArrayList(modeloAvionService.listarModelos());
        comboAvionModelo.setItems(listaModelos);
        comboAvionModelo.setConverter(new StringConverter<ModeloAvion>() {
            @Override public String toString(ModeloAvion m) {
                return m == null ? null : m.getNombreModelo();
            }
            @Override public ModeloAvion fromString(String s) {
                return null;
            }
        });

        if (listaAerolineas == null || listaAerolineas.isEmpty()) cargarAerolineas();
        comboAvionAerolinea.setItems(listaAerolineas);
        comboAvionAerolinea.setConverter(new StringConverter<Aerolinea>() {
            @Override public String toString(Aerolinea a) {
                return a == null ? null : a.getNombre();
            }
            @Override public Aerolinea fromString(String s) {
                return null;
            }
        });

        tablaAviones.getSelectionModel().selectedItemProperty().addListener((obs, o, n) -> selAvion(n));
        btnAvionGuardar.setOnAction(e -> guardarAvion());
        btnAvionEliminar.setOnAction(e -> eliminarAvion());
        btnAvionLimpiar.setOnAction(e -> limpiarAvion());

        cargarAviones();
    }

    private void cargarAviones() {
        listaAviones.setAll(avionService.listarTodos());
    }

    private void selAvion(Avion a) {
        if (a != null) {
            avionSel = a;
            lblFormAvion.setText("Editar Avión");
            txtAvionMatricula.setText(a.getMatricula());
            txtAvionAnio.setText(String.valueOf(a.getAnioFabricacion()));
            comboAvionEstado.setValue(a.getEstadoOperativo());

            listaModelos.stream()
                    .filter(m -> m.getIdModeloAvion() == a.getIdModeloAvion())
                    .findFirst().ifPresent(comboAvionModelo::setValue);

            listaAerolineas.stream()
                    .filter(ae -> ae.getIdAerolinea() == a.getIdAerolinea())
                    .findFirst().ifPresent(comboAvionAerolinea::setValue);

            btnAvionEliminar.setDisable(false);
            btnAvionGuardar.setText("Actualizar");
        } else limpiarAvion();
    }

    private void guardarAvion() {
        if (txtAvionMatricula.getText().isEmpty() || comboAvionModelo.getValue() == null || comboAvionAerolinea.getValue() == null) {
            return;
        }

        int anio = 0;
        try { anio = Integer.parseInt(txtAvionAnio.getText()); } catch (Exception e) { }

        if (avionSel == null) {
            Avion nuevo = new Avion(0,
                    txtAvionMatricula.getText(),
                    anio,
                    comboAvionEstado.getValue(),
                    comboAvionModelo.getValue().getIdModeloAvion(),
                    comboAvionAerolinea.getValue().getIdAerolinea()
            );
            avionService.registrarAvion(nuevo);
        } else {
            avionSel.setMatricula(txtAvionMatricula.getText());
            avionSel.setAnioFabricacion(anio);
            avionSel.setEstadoOperativo(comboAvionEstado.getValue());
            avionSel.setIdModeloAvion(comboAvionModelo.getValue().getIdModeloAvion());
            avionSel.setIdAerolinea(comboAvionAerolinea.getValue().getIdAerolinea());
            avionService.actualizarAvion(avionSel);
        }
        cargarAviones();
        limpiarAvion();
    }

    private void eliminarAvion() {
        if (avionSel != null) avionService.eliminarAvion(avionSel.getIdAvion());
        cargarAviones();
        limpiarAvion();
    }

    private void limpiarAvion() {
        avionSel = null;
        lblFormAvion.setText("Nuevo Avión");
        txtAvionMatricula.clear();
        txtAvionAnio.clear();
        comboAvionEstado.setValue(null);
        comboAvionModelo.setValue(null);
        comboAvionAerolinea.setValue(null);

        btnAvionEliminar.setDisable(true);
        btnAvionGuardar.setText("Guardar");
        tablaAviones.getSelectionModel().clearSelection();
    }
}