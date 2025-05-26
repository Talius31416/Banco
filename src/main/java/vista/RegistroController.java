package vista;

import Java.Intermedio.model.Banco;
import Java.Intermedio.model.Cliente;
import Java.Intermedio.model.TipoCuenta;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;

import java.io.File;
import java.time.LocalDate;

public class RegistroController {

    @FXML
    private TextField txtNombreUsuarioReal;;
    @FXML private TextField txtApellidoUsuario;
    @FXML private TextField txtCorreoUsuario;
    @FXML private TextField txtTelefonoUsuario;
    @FXML private TextField txtCCUsuario;
    @FXML private TextField txtDireccionUsuario;
    @FXML private TextField txtNombreUsuario;
    @FXML private PasswordField txtClavedeUsuario;
    @FXML private DatePicker dateFechaNacimientoUsuario;
    @FXML private ComboBox<TipoCuenta> BoxSeleccionarTipoCuenta;
    @FXML private Button btnRegistrarUsuario;
    @FXML private Button btnCancelarRegistroUsuario;
    @FXML private Button btnAdjuntarImagen;
    @FXML private ImageView imgUsuario;

    private File archivoImagen; // Para guardar la imagen seleccionada
    private final Banco banco = new Banco();

    @FXML
    public void initialize() {
        // Cargar tipos de cuenta en el ComboBox
        BoxSeleccionarTipoCuenta.setItems(FXCollections.observableArrayList(TipoCuenta.values()));

        btnRegistrarUsuario.setOnAction(e -> registrarCliente());
        btnCancelarRegistroUsuario.setOnAction(e -> cancelarRegistro());
        btnAdjuntarImagen.setOnAction(e -> seleccionarImagen());
    }

    private void registrarCliente() {
        try {
            String nombre = txtNombreUsuarioReal.getText().trim();
            String apellido = txtApellidoUsuario.getText().trim();
            String correo = txtCorreoUsuario.getText().trim();
            String telefono = txtTelefonoUsuario.getText().trim();
            String cc = txtCCUsuario.getText().trim();
            String direccion = txtDireccionUsuario.getText().trim();
            String usuario = txtNombreUsuario.getText().trim();
            String contrasenia = txtClavedeUsuario.getText().trim();
            LocalDate fechaNacimiento = dateFechaNacimientoUsuario.getValue();
            TipoCuenta tipoCuenta = BoxSeleccionarTipoCuenta.getValue();

            if (nombre.isEmpty() || apellido.isEmpty() || correo.isEmpty() || telefono.isEmpty() ||
                    cc.isEmpty() || direccion.isEmpty() || usuario.isEmpty() || contrasenia.isEmpty() ||
                    fechaNacimiento == null || tipoCuenta == null) {
                mostrarAlerta(Alert.AlertType.WARNING, "Campos incompletos", "Por favor completa todos los campos.");
                return;
            }

            // Crear objeto Cliente
            Cliente nuevoCliente = new Cliente(
                    nombre, apellido, correo, telefono, cc, fechaNacimiento,
                    direccion, tipoCuenta, usuario, contrasenia, false
            );

            // Registrar cliente en base de datos
            String resultadoRegistro = banco.almacenarUsuario(nuevoCliente, nombre, apellido, correo, telefono, cc,
                    fechaNacimiento, direccion, tipoCuenta, usuario, contrasenia, false);

            // Abrir cuenta bancaria en tabla correspondiente
            String resultadoCuenta = nuevoCliente.abrirCuentaBancaria(tipoCuenta);

            mostrarAlerta(Alert.AlertType.INFORMATION, "Registro completo",
                    resultadoRegistro + "\n" + resultadoCuenta);

            limpiarFormulario();

        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlerta(Alert.AlertType.ERROR, "Error", "Ocurrió un error al registrar el usuario.");
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
            imgUsuario.setImage(new Image(archivoImagen.toURI().toString()));
        }
    }

    private void cancelarRegistro() {
        limpiarFormulario();
        mostrarAlerta(Alert.AlertType.INFORMATION, "Cancelado", "Registro cancelado.");
    }

    private void limpiarFormulario() {
        txtNombreUsuarioReal.clear();
        txtApellidoUsuario.clear();
        txtCorreoUsuario.clear();
        txtTelefonoUsuario.clear();
        txtCCUsuario.clear();
        txtDireccionUsuario.clear();
        txtNombreUsuario.clear();
        txtClavedeUsuario.clear();
        dateFechaNacimientoUsuario.setValue(null);
        BoxSeleccionarTipoCuenta.setValue(null);
        imgUsuario.setImage(new Image("/img/PerfilDef.png")); // Reset a imagen por defecto
        archivoImagen = null;
    }

    private void mostrarAlerta(Alert.AlertType tipo, String titulo, String mensaje) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
    private Cliente clienteAEditar;

    public void setClienteAEditar(Cliente cliente) {
        this.clienteAEditar = cliente;
        cargarDatosCliente(); // Esta línea da error si no existe este método
    }

    private void cargarDatosCliente() {
        if (clienteAEditar != null) {
            txtNombreUsuarioReal.setText(clienteAEditar.getNombre());
            txtCCUsuario.setText(clienteAEditar.getCC());
            txtCorreoUsuario.setText(clienteAEditar.getCorreo());
            txtCCUsuario.setDisable(true); // Deshabilitar la edición de la cédula si lo deseas
        }
    }


}


