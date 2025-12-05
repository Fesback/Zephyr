package com.zephyr.ui.controller;

import com.zephyr.domain.Vuelo;
import com.zephyr.repository.impl.VueloRepositoryJDBCImpl;
import com.zephyr.service.VueloService;
import com.zephyr.service.impl.VueloServiceImpl;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class VuelosController implements Initializable {
    @FXML
    private TableView<Vuelo> tablaVuelos;
    @FXML private TableColumn<Vuelo, String> colCodigo, colAerolinea, colOrigen, colDestino, colFecha, colEstado;
    @FXML private Button btnNuevoVuelo;

    private final VueloService vueloService;
    private ObservableList<Vuelo> listaVuelos;

    public VuelosController() {
        this.vueloService = new VueloServiceImpl(new VueloRepositoryJDBCImpl());
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        configurarTabla();

        btnNuevoVuelo.setOnAction(event -> abrirPopupRegistroVuelo());

        cargarVuelos();
    }

    private void configurarTabla() {
        colCodigo.setCellValueFactory(new PropertyValueFactory<>("codigoVuelo"));
        colAerolinea.setCellValueFactory(new PropertyValueFactory<>("aerolinea"));
        colOrigen.setCellValueFactory(new PropertyValueFactory<>("origenIata"));
        colDestino.setCellValueFactory(new PropertyValueFactory<>("destinoIata"));
        colFecha.setCellValueFactory(new PropertyValueFactory<>("fechaSalida"));
        colEstado.setCellValueFactory(new PropertyValueFactory<>("estadoVuelo"));

        listaVuelos = FXCollections.observableArrayList();
        tablaVuelos.setItems(listaVuelos);
    }

    private void cargarVuelos() {
        listaVuelos.setAll(vueloService.listarTodosLosVuelos());
    }

    private void abrirPopupRegistroVuelo() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/zephyr/ui/fxml/registrar-vuelo.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Registrar Nuevo Vuelo");
            stage.setScene(new Scene(root));
            stage.setResizable(false);
            stage.getIcons().add(new Image(getClass().getResourceAsStream("/com/zephyr/ui/images/zephyr-icon.png")));
            stage.showAndWait();
            cargarVuelos();

        } catch (IOException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Error al abrir ventana de registro.").show();
        }
    }
}