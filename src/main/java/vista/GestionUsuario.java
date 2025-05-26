package vista;

import Java.Intermedio.model.Banco;
import Java.Intermedio.model.Cliente;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class GestionUsuario {

    @FXML
    private ImageView imgLogo;

    @FXML
    private Button btnCancelarGestion, btnEliminarUsuario, btnEditarUsuario, btnAgregaranuevoUsuario;

    @FXML
    private TableView<Cliente> TablaDatosUsuarios;

    @FXML
    private TableColumn<Cliente, String> nombreCol;

    @FXML
    private TableColumn<Cliente, String> ccCol;

    @FXML
    private TableColumn<Cliente, String> correoCol;

    private Banco banco = new Banco();
    private ObservableList<Cliente> listaClientes;

    @FXML
    public void initialize() {
        nombreCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getNombre()));
        ccCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getCC()));
        correoCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getCorreo()));

        cargarUsuarios();
    }


    private void cargarUsuarios() {
        List<Cliente> clientes = banco.listarClientes();
        listaClientes = FXCollections.observableArrayList(clientes);
        TablaDatosUsuarios.setItems(listaClientes);
    }

    @FXML
    private void eliminarUsuario() {
        Cliente clienteSeleccionado = TablaDatosUsuarios.getSelectionModel().getSelectedItem();
        if (clienteSeleccionado != null) {
            String resultado = banco.eliminarCliente(clienteSeleccionado.getCC());
            mostrarAlerta(Alert.AlertType.INFORMATION, "Resultado", resultado);
            cargarUsuarios();
        } else {
            mostrarAlerta(Alert.AlertType.WARNING, "Advertencia", "Debe seleccionar un usuario para eliminar.");
        }
    }

    @FXML
    private void editarUsuario() {
        Cliente clienteSeleccionado = TablaDatosUsuarios.getSelectionModel().getSelectedItem();
        if (clienteSeleccionado != null) {
            try {
                // Usar FXMLLoader con ruta al archivo FXML
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/Java/Intermedio/editarUsuario.fxml"));
                Parent root = loader.load();

                // Obtener el controlador
                Editarusuario controller = loader.getController();
                controller.setClienteActual(clienteSeleccionado);

                // Mostrar ventana
                Stage stage = new Stage();
                stage.setTitle("Editar Usuario");
                stage.setScene(new Scene(root));
                stage.show();

            } catch (IOException e) {
                e.printStackTrace();
                mostrarAlerta(Alert.AlertType.ERROR, "Error", "No se pudo abrir la ventana de edición.");
            }
        } else {
            mostrarAlerta(Alert.AlertType.WARNING, "Advertencia", "Debe seleccionar un usuario para editar.");
        }
    }




    @FXML
    private void agregarUsuarioNuevo() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Java/Intermedio/registro.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Registrar Nuevo Cliente");
            stage.setScene(new Scene(root));
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
            mostrarAlerta(Alert.AlertType.ERROR, "Error", "No se pudo abrir la ventana de registro.");
        }
    }

    @FXML
    private void cancelarGestion() {
        try {
            // Cerrar la ventana actual
            Stage stageActual = (Stage) btnCancelarGestion.getScene().getWindow();
            stageActual.close();

            // Cargar la ventana de inicio
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Java/Intermedio/ventanaInicio.fxml"));
            Parent root = loader.load();

            Stage stageInicio = new Stage();
            stageInicio.setTitle("Inicio");
            stageInicio.setScene(new Scene(root));
            stageInicio.show();

        } catch (IOException e) {
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
