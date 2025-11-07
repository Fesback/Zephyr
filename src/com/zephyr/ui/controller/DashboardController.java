package com.zephyr.ui.controller;

import com.zephyr.domain.Personal;
import com.zephyr.domain.Vuelo;
import com.zephyr.repository.VueloRepository;
import com.zephyr.repository.impl.VueloRepositoryJDBCImpl;
import com.zephyr.service.VueloService;
import com.zephyr.service.impl.VueloServiceImpl;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.time.LocalDateTime;
import java.util.List;
import java.util.ResourceBundle;

public class DashboardController implements Initializable {

    private Personal usuarioLogueado;

    @FXML
    private Label labelBienvenida;

    @FXML
    private TableView<Vuelo> tablaVuelos;

    @FXML
    private TableColumn<Vuelo,String> colCodigoVuelo;

    @FXML
    private TableColumn<Vuelo,String> colAerolinea;

    @FXML
    private TableColumn<Vuelo,String> colOrigen;

    @FXML
    private TableColumn<Vuelo,String> colDestino;

    @FXML
    private TableColumn<Vuelo,String> colEstado;

    @FXML
    private TableColumn<Vuelo, LocalDateTime> colSalida;

    private final VueloService vueloService;

    private ObservableList<Vuelo> listaDeVuelosObservable;

    public DashboardController() {
        VueloRepository vueloRepository = new VueloRepositoryJDBCImpl();
        this.vueloService = new VueloServiceImpl(vueloRepository);
        this.listaDeVuelosObservable = FXCollections.observableArrayList();
    }

    public void setUsuarioLogueado(Personal personal) {
        this.usuarioLogueado = personal;

        if (usuarioLogueado != null) {
            labelBienvenida.setText("Bienvenido, " + usuarioLogueado.getNombres() + "!");
            configurarVistaPorRol(usuarioLogueado.getRol().getNombreRol());
        }
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
    }

    private void cargarVuelosDelDia() {
        List<Vuelo> vuelos = vueloService.getVuelosProgramadosHoy();
        listaDeVuelosObservable.clear();
        listaDeVuelosObservable.addAll(vuelos);

        System.out.println("Dashboard: Se cargaron " + vuelos.size() + " vuelos");
    }

    private void configurarVistaPorRol(String rol) {
        System.out.println("COnfigurando vista para el rol: " + rol);
    }
}
