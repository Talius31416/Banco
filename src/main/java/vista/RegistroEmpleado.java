package vista;

import Java.Intermedio.model.Administrador;
import Java.Intermedio.model.Banco;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;

import java.io.File;
import java.time.LocalDate;

public class RegistroEmpleado {

    @FXML private TextField txtNombreEmpleado;
    @FXML private TextField txtApellidoEmpleado;
    @FXML private TextField txtCorreoEmpleado;
    @FXML private TextField txtTelefonoEmpleado;
    @FXML private TextField txtCCEmpleado;
    @FXML private TextField txtDireccionEmpleado;
    @FXML private TextField txtUsuarioEmpleado;
    @FXML private PasswordField txtClaveDeEmpleado;
    @FXML private DatePicker dateFechaNacimientoEmpleado;
    @FXML private Button btnRegistrarEmpleado;
    @FXML private Button btnCancelarRegistroEmpleado;
    @FXML private Button btnAdjuntarImagen;
    @FXML private ImageView imgEmpleado;

    private File archivoImagen;
    private final Administrador administrador = new Administrador("", "", "", "", "", null, "", "");

    @FXML
    public void initialize() {
        btnRegistrarEmpleado.setOnAction(e -> registrarCajero());
        btnCancelarRegistroEmpleado.setOnAction(e -> cancelarRegistro());
        btnAdjuntarImagen.setOnAction(e -> seleccionarImagen());
    }

    private void registrarCajero() {
        try {
            String nombre = txtNombreEmpleado.getText().trim();
            String apellido = txtApellidoEmpleado.getText().trim();
            String correo = txtCorreoEmpleado.getText().trim();
            String telefono = txtTelefonoEmpleado.getText().trim();
            String cc = txtCCEmpleado.getText().trim();
            String direccion = txtDireccionEmpleado.getText().trim(); // No se almacena en BD actualmente
            String usuario = txtUsuarioEmpleado.getText().trim();
            String contrasenia = txtClaveDeEmpleado.getText().trim();
            LocalDate fechaNacimiento = dateFechaNacimientoEmpleado.getValue();

            if (nombre.isEmpty() || apellido.isEmpty() || correo.isEmpty() || telefono.isEmpty() ||
                    cc.isEmpty() || usuario.isEmpty() || contrasenia.isEmpty() || fechaNacimiento == null) {
                mostrarAlerta(Alert.AlertType.WARNING, "Campos incompletos", "Por favor, completa todos los campos.");
                return;
            }

            String resultado = administrador.almacenarCajero(
                    nombre, apellido, correo, telefono, cc, fechaNacimiento, usuario, contrasenia
            );

            mostrarAlerta(Alert.AlertType.INFORMATION, "Resultado", resultado);
            limpiarFormulario();

        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlerta(Alert.AlertType.ERROR, "Error", "Ocurrió un error al registrar el empleado.");
        }
    }

    @FXML
    private void seleccionarImagen() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Seleccionar imagen de perfil");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Imágenes", "*.png", "*.jpg", "*.jpeg")
        );
        archivoImagen = fileChooser.showOpenDialog(null);
        if (archivoImagen != null) {
            imgEmpleado.setImage(new Image(archivoImagen.toURI().toString()));
        }
    }

    private void cancelarRegistro() {
        limpiarFormulario();
        mostrarAlerta(Alert.AlertType.INFORMATION, "Cancelado", "Registro cancelado.");
    }

    private void limpiarFormulario() {
        txtNombreEmpleado.clear();
        txtApellidoEmpleado.clear();
        txtCorreoEmpleado.clear();
        txtTelefonoEmpleado.clear();
        txtCCEmpleado.clear();
        txtDireccionEmpleado.clear();
        txtUsuarioEmpleado.clear();
        txtClaveDeEmpleado.clear();
        dateFechaNacimientoEmpleado.setValue(null);
        imgEmpleado.setImage(new Image("/img/PerfilDef.png"));
        archivoImagen = null;
    }

    private void mostrarAlerta(Alert.AlertType tipo, String titulo, String mensaje) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}
