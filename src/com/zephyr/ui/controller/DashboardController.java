package com.zephyr.ui.controller;

import com.sun.javafx.fxml.FXMLLoaderHelper;
import com.zephyr.domain.Personal;
import com.zephyr.domain.Vuelo;
import com.zephyr.repository.VueloRepository;
import com.zephyr.repository.impl.VueloRepositoryJDBCImpl;
import com.zephyr.service.VueloService;
import com.zephyr.service.impl.VueloServiceImpl;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;

import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.List;
import java.util.ResourceBundle;

public class DashboardController implements Initializable {

    private Personal usuarioLogueado;

    private Vuelo vueloSeleccionado;

    @FXML private BorderPane rootPane;

    @FXML private Button btnDashboard, btnGestionVuelos, btnEmbarque,btnGestionPersonal, btnReportes, btnConfiguracion;

    //@FXML private Label labelBienvenida;
    @FXML private MenuButton menuUsuario;
    @FXML private MenuItem menuItemLogout;
    @FXML private MenuItem menuItemPerfil;

    @FXML private TableView<Vuelo> tablaVuelos;
    @FXML private TableColumn<Vuelo,String> colCodigoVuelo;
    @FXML private TableColumn<Vuelo,String> colAerolinea;
    @FXML private TableColumn<Vuelo,String> colOrigen;
    @FXML private TableColumn<Vuelo,String> colDestino;
    @FXML private TableColumn<Vuelo,String> colEstado;
    @FXML private TableColumn<Vuelo, LocalDateTime> colSalida;

    @FXML private VBox panelDeAcciones;
    @FXML private VBox seccionAccionesSupervisor;
    @FXML private Label detalleCodigo;
    @FXML private Label detalleEstado;
    @FXML private Label detallePuerta;
    @FXML private Button btnIniciarEmbarque;
    @FXML private Button btnRetrasarVuelo;
    @FXML private Button btnAsignarPuerta;


    private final VueloService vueloService;
    private ObservableList<Vuelo> listaDeVuelosObservable;

    //nodos
    private Node dashboardCenterNode;
    private Node dashboardRightNode;

    public DashboardController() {
        VueloRepository vueloRepository = new VueloRepositoryJDBCImpl();
        this.vueloService = new VueloServiceImpl(vueloRepository);
        this.listaDeVuelosObservable = FXCollections.observableArrayList();
    }

    public void setUsuarioLogueado(Personal personal) {
        this.usuarioLogueado = personal;

        if (usuarioLogueado != null) {
            menuUsuario.setText("Hola, " + usuarioLogueado.getNombres());
            configurarVistaPorRol(usuarioLogueado.getRol().getNombreRol());
        }
    }

    // refrescar popups
    public void refrescarDashboard() {
        cargarVuelosDelDia();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        colCodigoVuelo.setCellValueFactory(new PropertyValueFactory<>("codigoVuelo"));
        colAerolinea.setCellValueFactory(new PropertyValueFactory<>("aerolinea"));
        colOrigen.setCellValueFactory(new PropertyValueFactory<>("origenIata"));
        colDestino.setCellValueFactory(new PropertyValueFactory<>("destinoIata"));
        colEstado.setCellValueFactory(new PropertyValueFactory<>("estadoVuelo"));
        colSalida.setCellValueFactory(new PropertyValueFactory<>("fechaSalida"));

        tablaVuelos.setItems(listaDeVuelosObservable);
        cargarVuelosDelDia();

        tablaVuelos.getSelectionModel().selectedItemProperty().addListener((observable,
                                                                            oldValue, newValue) -> mostrarDetallesVuelo(newValue));

        btnIniciarEmbarque.setOnAction(event -> handleActualizarEstado(2, "Embarcando"));
        btnRetrasarVuelo.setOnAction(event -> handleActualizarEstado(5,"Retrasado"));

        //logout
        menuItemLogout.setOnAction(event -> handleLogout());

        // guardar panel original del dash
        this.dashboardCenterNode = rootPane.getCenter();
        this.dashboardRightNode = rootPane.getRight();

        // botones
        btnDashboard.setOnAction(event -> {
            rootPane.setCenter(this.dashboardCenterNode);
            rootPane.setRight(this.dashboardRightNode);
        });

        btnEmbarque.setOnAction(event -> {
           cargarVistaEnCentro("/com/zephyr/ui/fxml/embarque.fxml");
        });

        btnGestionPersonal.setOnAction(event -> {
            cargarVistaEnCentro("/com/zephyr/ui/fxml/personal.fxml");
        });

        btnReportes.setOnAction(event -> {
            cargarVistaEnCentro("/com/zephyr/ui/fxml/reportes.fxml");
        });

        btnConfiguracion.setOnAction(event -> {
            cargarVistaEnCentro("/com/zephyr/ui/fxml/configuracion.fxml");
        });

        btnGestionVuelos.setOnAction(event -> {
            cargarVistaEnCentro("/com/zephyr/ui/fxml/vuelos.fxml");
        });

        btnAsignarPuerta.setOnAction(event -> {
           abrirPopupAsignarPuerta();
        });
    }

    // logout
    private void handleLogout() {
        try {
            Stage currentStage = (Stage) menuUsuario.getScene().getWindow();
            currentStage.close();

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/zephyr/ui/fxml/login.fxml"));
            Parent root = loader.load();
            Stage loginStage = new Stage();

            loginStage.getIcons().add(new Image(getClass().getResourceAsStream("/com/zephyr/ui/images/zephyr-icon.png")));
            loginStage.setTitle("Zephyr - Login");
            loginStage.setScene(new Scene(root, 1280, 720));
            loginStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void cargarVistaEnCentro(String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent vista = loader.load();

            rootPane.setCenter(vista);
            rootPane.setRight(null);
        } catch (IOException e) {
            e.printStackTrace();
            mostrarAlerta(Alert.AlertType.ERROR, "Error de navegacion" , "No se pudo cargar la vista: " + fxmlPath);
        }
    }

    private void mostrarDetallesVuelo(Vuelo vuelo) {
        if (vuelo != null) {
            this.vueloSeleccionado = vuelo;

            detalleCodigo.setText(vuelo.getCodigoVuelo());
            detalleEstado.setText(vuelo.getEstadoVuelo());
            detallePuerta.setText(vuelo.getCodigoPuerta() != null ? vuelo.getCodigoPuerta() : "Sin asignar");

            panelDeAcciones.setVisible(true);
        } else {
            panelDeAcciones.setVisible(false);
        }
    }

    private void cargarVuelosDelDia() {
        List<Vuelo> vuelos = vueloService.getVuelosProgramadosHoy();
        listaDeVuelosObservable.clear();
        listaDeVuelosObservable.addAll(vuelos);

        System.out.println("Dashboard: Se cargaron " + vuelos.size() + " vuelos");
    }

    private void handleActualizarEstado(int idNuevoEstado, String nombreEstado) {
        Vuelo vueloClicado = tablaVuelos.getSelectionModel().getSelectedItem();
        if (vueloClicado == null) {
            mostrarAlerta(Alert.AlertType.WARNING, "Sin seleccion", "Por favor seleciione un vuelo de la tabla primero");
            return;
        }

        System.out.println("Intentando cambiar estado del Vuelo ID: " + vueloClicado.getIdVuelo() + " a '"  + nombreEstado + "'");
        vueloService.actualizarEstadoVuelo(vueloClicado.getIdVuelo(), idNuevoEstado);
        mostrarAlerta(Alert.AlertType.INFORMATION, "Exito", "El vuelo " + vueloClicado.getCodigoVuelo() + " ha sido actualizado a: " + nombreEstado);
        cargarVuelosDelDia();

        tablaVuelos.getSelectionModel().clearSelection();
        panelDeAcciones.setVisible(false);
    }

    private void configurarVistaPorRol(String rol) {
        System.out.println("COnfigurando vista para el rol: " + rol);

        if (rol.equals("Agente de Puerta")) {
            // un  agente no puede ver la seccion de acciones del supervisor
            seccionAccionesSupervisor.setVisible(false);
            seccionAccionesSupervisor.setManaged(false);

            // entonces oculto los botones del menu lateral
            btnGestionPersonal.setVisible(false);
            btnConfiguracion.setVisible(false);

        }
    }

    // Asignar puerta
    @FXML
    private void abrirPopupAsignarPuerta() {
        if (vueloSeleccionado == null) {
            mostrarAlerta(Alert.AlertType.WARNING, "Sin Selección", "Seleccione un vuelo primero.");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/zephyr/ui/fxml/asignar-puerta.fxml"));
            Parent root = loader.load();

            AsignarPuertaController controller = loader.getController();
            controller.setDatos(vueloSeleccionado, this);

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Asignar Puerta");
            stage.showAndWait();

            cargarVuelosDelDia();

        } catch (Exception e) {
            e.printStackTrace();
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
