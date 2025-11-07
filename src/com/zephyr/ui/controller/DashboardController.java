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
import javafx.scene.control.TableView;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class DashboardController implements Initializable {

    private Personal usuarioLogueado;

    @FXML
    private Label labelBienvenida;

    @FXML
    private TableView<Vuelo> tablaVuelos;

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
