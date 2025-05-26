package vista;

import Java.Intermedio.model.Administrador;
import Java.Intermedio.model.Banco;
import Java.Intermedio.model.Cajero;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;

public class GestionarEmpleados {

    @FXML
    private TableView<Cajero> TablaDatosEmpleados;

    @FXML
    private Button btnCancelarGestion;

    @FXML
    private Button btnEliminarEmpleado;

    @FXML
    private Button btnEditarEmpleado;

    @FXML
    private Button btnAgregarNuevoEmpleado;
    @FXML
    private TableColumn<Cajero, String> colNombre;
    @FXML
    private TableColumn<Cajero, String> colIdentificacion;
    @FXML
    private TableColumn<Cajero, String> colCorreo;

    private Administrador administradorActual;

    public void setAdministradorActual(Administrador admin) {
        this.administradorActual = admin;
        cargarTabla();
    }

    @FXML
    private void initialize() {
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colIdentificacion.setCellValueFactory(new PropertyValueFactory<>("CC")); // o "cedula" según tu clase
        colCorreo.setCellValueFactory(new PropertyValueFactory<>("correo"));

        // No llames cargarTabla aquí si aún no tienes administradorActual
    }

    private void cargarTabla() {
        if (administradorActual == null) return;
        ArrayList<Cajero> listaCajeros = administradorActual.listarCajeros();
        ObservableList<Cajero> cajeros = FXCollections.observableArrayList(listaCajeros);
        TablaDatosEmpleados.setItems(cajeros);
    }



    @FXML
    private void eliminarEmpleado() {
        Cajero seleccionado = TablaDatosEmpleados.getSelectionModel().getSelectedItem();
        if (seleccionado == null) {
            mostrarAlerta("Seleccione un cajero para eliminar.");
            return;
        }

        Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacion.setTitle("Confirmar eliminación");
        confirmacion.setContentText("¿Está seguro de eliminar al cajero " + seleccionado.getNombre() + "?");
        confirmacion.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                String resultado = new Banco().eliminarCliente(seleccionado.getCC());
                mostrarAlerta(resultado);
                cargarTabla();
            }
        });
    }

    @FXML
    private void editarEmpleado() {
        Cajero seleccionado = TablaDatosEmpleados.getSelectionModel().getSelectedItem();
        if (seleccionado == null) {
            mostrarAlerta("Seleccione un cajero para editar.");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Java/Intermedio/editarEmpleado.fxml"));
            Parent root = loader.load();
            EditarEmpleado controller = loader.getController();
            controller.setAdministradorActual(administradorActual);
            controller.cargarCajero(seleccionado);

            Stage stage = new Stage();
            stage.setTitle("Editar Empleado");
            stage.setScene(new Scene(root));
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
            mostrarAlerta("No se pudo abrir la ventana de edición.");
        }
    }

    @FXML
    private void agregarNuevoEmpleado() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Java/Intermedio/registroEmpleado.fxml"));
            Parent root = loader.load();
            // Si deseas pasar el administrador actual:
            // RegistrarEmpleado controller = loader.getController();
            // controller.setAdministradorActual(administradorActual);

            Stage stage = new Stage();
            stage.setTitle("Registrar Nuevo Empleado");
            stage.setScene(new Scene(root));
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
            mostrarAlerta("No se pudo abrir la ventana de registro.");
        }
    }

    @FXML
    private void cancelarGestion() {
        try {
            // Cerrar la ventana actual
            Stage stageActual = (Stage) btnCancelarGestion.getScene().getWindow();
            stageActual.close();

            // Cargar la ventana inicial (Inicio.fxml)
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Java/Intermedio/ventanaInicio.fxml"));
            Parent root = loader.load();

            Stage stageInicio = new Stage();
            stageInicio.setTitle("Inicio");
            stageInicio.setScene(new Scene(root));
            stageInicio.show();

        } catch (IOException e) {
            e.printStackTrace();
            mostrarAlerta("No se pudo cargar la ventana de inicio.");
        }
    }

    private void mostrarAlerta(String mensaje) {
        Alert alerta = new Alert(Alert.AlertType.INFORMATION);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }
}
