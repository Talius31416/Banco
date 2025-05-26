package vista;

import Java.Intermedio.model.Administrador;
import Java.Intermedio.model.ConexionFallidaConBaseDeDatosException;
import Java.Intermedio.model.Cajero;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;

public class EditarEmpleado {

    @FXML private TextField txtNombreEmpleado;
    @FXML private TextField txtApellidoEmpleado;
    @FXML private TextField txtCorreoEmpleado;
    @FXML private TextField txtTelefonoEmpleado;
    @FXML private TextField txtCCEmpleado;
    @FXML private TextField txtUsuarioEmpleado;
    @FXML private PasswordField txtClaveDeEmpleado;
    @FXML private TextField txtDireccionEmpleado;
    @FXML private DatePicker dateFechaNacimientoEmpleado;
    @FXML private Button btnEditarEmpleado;
    @FXML private Button btnCancelarEdicionEmpleado;
    @FXML private ImageView imgEmpleado;
    @FXML private Button btnAdjuntarImagen;

    private File archivoImagen = null;
    private Administrador administradorActual;
    private Cajero cajeroAEditar;

    public void setAdministradorActual(Administrador admin) {
        this.administradorActual = admin;
    }

    public void cargarCajero(Cajero cajero) {
        this.cajeroAEditar = cajero;
        txtNombreEmpleado.setText(cajero.getNombre());
        txtApellidoEmpleado.setText(cajero.getApellido());
        txtCorreoEmpleado.setText(cajero.getCorreo());
        txtTelefonoEmpleado.setText(cajero.getTelefono());
        txtCCEmpleado.setText(cajero.getCC());
        txtUsuarioEmpleado.setText(cajero.getUsuario());
        txtClaveDeEmpleado.setText(cajero.getContrasenia());
        txtDireccionEmpleado.setText(""); // Si tienes dirección, cámbialo
        dateFechaNacimientoEmpleado.setValue(cajero.getFechaNacimiento());
        // Puedes cargar una imagen por defecto si la tienes guardada
    }

    @FXML
    private void initialize() {
        // Opcional: configuración inicial
    }

    @FXML
    private void editarEmpleado() {
        if (administradorActual == null || cajeroAEditar == null) {
            mostrarAlerta("Error", "Faltan datos para editar el empleado.");
            return;
        }

        String nombre = txtNombreEmpleado.getText();
        String apellido = txtApellidoEmpleado.getText();
        String correo = txtCorreoEmpleado.getText();
        String telefono = txtTelefonoEmpleado.getText();
        String CC = txtCCEmpleado.getText();
        String usuario = txtUsuarioEmpleado.getText();
        String contrasenia = txtClaveDeEmpleado.getText();
        String direccion = txtDireccionEmpleado.getText();
        LocalDate fechaNacimiento = dateFechaNacimientoEmpleado.getValue();

        try {
            String resultado = administradorActual.actualizarCajero(
                    nombre, apellido, correo, telefono,
                    fechaNacimiento, usuario, contrasenia, CC
            );
            mostrarAlerta("Éxito", resultado);

        } catch (ConexionFallidaConBaseDeDatosException e) {
            mostrarAlerta("Error", "No se pudo actualizar el cajero.");
        } catch (NumberFormatException e) {
            mostrarAlerta("Error", "La contraseña debe ser numérica.");
        }
    }

    @FXML
    private void cancelarEdicion() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Java/Intermedio/gestionarEmpleados.fxml"));
            Parent root = loader.load();

            // Pasar el administrador actual a la vista administrador
            GestionarEmpleados controlador = loader.getController();
            controlador.setAdministradorActual(administradorActual);

            // Obtener la escena actual desde el botón
            Stage stage = (Stage) btnCancelarEdicionEmpleado.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            mostrarAlerta("Error", "No se pudo cargar la vista del administrador.");
        }
    }


    @FXML
    private void adjuntarImagen() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Seleccionar imagen");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Imágenes", "*.png", "*.jpg", "*.jpeg")
        );
        File selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null) {
            archivoImagen = selectedFile;
            imgEmpleado.setImage(new Image(selectedFile.toURI().toString()));
        }
    }

    private void limpiarCampos() {
        txtNombreEmpleado.clear();
        txtApellidoEmpleado.clear();
        txtCorreoEmpleado.clear();
        txtTelefonoEmpleado.clear();
        txtCCEmpleado.clear();
        txtUsuarioEmpleado.clear();
        txtClaveDeEmpleado.clear();
        txtDireccionEmpleado.clear();
        dateFechaNacimientoEmpleado.setValue(null);
    }

    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}
