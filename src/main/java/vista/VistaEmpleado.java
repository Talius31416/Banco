package vista;

import Java.Intermedio.model.Banco;
import Java.Intermedio.model.Cliente;
import Java.Intermedio.model.TipoCuenta;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;

public class VistaEmpleado {

    @FXML
    private Button btnGestionarClientes;

    @FXML
    private Button btnHomeCliente;

    @FXML
    private Label txtNombreEmpleado;

    private String nombreEmpleado;

    @FXML
    public void initialize() {
        btnGestionarClientes.setOnAction(e -> abrirGestionClientes());
        btnHomeCliente.setOnAction(e -> volverHome());
    }

    public void setNombreEmpleado(String nombre) {
        this.nombreEmpleado = nombre;
        txtNombreEmpleado.setText(nombreEmpleado);
    }

    private void abrirGestionClientes() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Java/Intermedio/gestionUsuario.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) btnGestionarClientes.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Gestión de Clientes");
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
            mostrarAlerta(Alert.AlertType.ERROR, "Error", "No se pudo abrir la gestión de clientes.");
        }
    }

    private void volverHome() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Java/Intermedio/VentanaInicio.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) btnHomeCliente.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Inicio");
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
            mostrarAlerta(Alert.AlertType.ERROR, "Error", "No se pudo volver al inicio.");
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