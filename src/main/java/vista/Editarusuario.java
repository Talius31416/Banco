package vista;

import Java.Intermedio.model.Cliente;
import Java.Intermedio.model.TipoCuenta;
import Java.Intermedio.model.Banco;
import Java.Intermedio.model.ConexionFallidaConBaseDeDatosException;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;

import java.io.File;
import java.time.LocalDate;

public class Editarusuario {

    @FXML private TextField txtNombre;
    @FXML private TextField txtApellido;
    @FXML private TextField txtCorreo;
    @FXML private TextField txtTelefono;
    @FXML private TextField txtCC;
    @FXML private TextField txtUsuario;
    @FXML private PasswordField txtClavedeUsuario;
    @FXML private TextField txtDireccion;
    @FXML private DatePicker dateFechaNacimiento;
    @FXML private ComboBox<TipoCuenta> BoxSeleccionarTipoCuenta;

    @FXML private ImageView imgUsuario;
    @FXML private Button btnEditar;
    @FXML private Button btnCancelarEdicion;
    @FXML private Button btnAdjuntarImagen;

    private File archivoImagen = null;
    private Cliente clienteActual;

    public void setClienteActual(Cliente cliente) {
        this.clienteActual = cliente;
        cargarDatosDelCliente();
    }

    @FXML
    private void initialize() {
        BoxSeleccionarTipoCuenta.getItems().setAll(TipoCuenta.values());
    }

    private void cargarDatosDelCliente() {
        if (clienteActual == null) return;

        txtNombre.setText(clienteActual.getNombre());
        txtApellido.setText(clienteActual.getApellido());
        txtCorreo.setText(clienteActual.getCorreo());
        txtTelefono.setText(clienteActual.getTelefono());
        txtCC.setText(clienteActual.getCC());
        txtUsuario.setText(clienteActual.getUsuario());
        txtClavedeUsuario.setText(clienteActual.getContrasenia());
        txtDireccion.setText(clienteActual.getDireccion());
        dateFechaNacimiento.setValue(clienteActual.getFechaNacimiento());
        BoxSeleccionarTipoCuenta.setValue(clienteActual.getTipoCuenta());
        // Si tienes imagen del cliente, podrías cargarla aquí
    }

    @FXML
    private void editarUsuario() {
        if (clienteActual == null) {
            mostrarAlerta("Error", "No hay cliente cargado para editar.");
            return;
        }

        try {
            String nombre = txtNombre.getText();
            String apellido = txtApellido.getText();
            String correo = txtCorreo.getText();
            String telefono = txtTelefono.getText();
            String cc = txtCC.getText();
            String usuario = txtUsuario.getText();
            String contrasena = txtClavedeUsuario.getText();
            String direccion = txtDireccion.getText();
            LocalDate fechaNacimiento = dateFechaNacimiento.getValue();
            TipoCuenta tipoCuenta = BoxSeleccionarTipoCuenta.getValue();
            boolean cuentaBloqueada = clienteActual.isCuentaBloqueada(); // si tienes esa info

            String resultado = new Banco().actualizarCliente(
                    nombre, apellido, correo, telefono,
                    fechaNacimiento, usuario, contrasena,
                    direccion, cc, tipoCuenta, cuentaBloqueada
            );

            mostrarAlerta("Éxito", resultado);

        } catch (ConexionFallidaConBaseDeDatosException e) {
            mostrarAlerta("Error", "No se pudo actualizar el usuario.");
        } catch (Exception e) {
            mostrarAlerta("Error", "Verifique los datos ingresados.");
        }
    }

    @FXML
    private void cancelarEdicion() {
        mostrarAlerta("Cancelado", "La edición fue cancelada.");
        limpiarCampos();
    }

    @FXML
    private void adjuntarImagen() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Seleccionar imagen de perfil");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Imágenes", "*.jpg", "*.png", "*.jpeg")
        );
        File archivo = fileChooser.showOpenDialog(null);
        if (archivo != null) {
            archivoImagen = archivo;
            imgUsuario.setImage(new Image(archivo.toURI().toString()));
        }
    }

    private void limpiarCampos() {
        txtNombre.clear();
        txtApellido.clear();
        txtCorreo.clear();
        txtTelefono.clear();
        txtCC.clear();
        txtUsuario.clear();
        txtClavedeUsuario.clear();
        txtDireccion.clear();
        dateFechaNacimiento.setValue(null);
        BoxSeleccionarTipoCuenta.setValue(null);
    }

    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alerta = new Alert(Alert.AlertType.INFORMATION);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }
    private Cliente clienteAEditar;

    public void setClienteAEditar(Cliente cliente) {
        this.clienteAEditar = cliente;
        cargarDatosCliente(); // Esta línea da error si no existe este método
    }

    private void cargarDatosCliente() {
        if (clienteAEditar != null) {
            txtNombre.setText(clienteAEditar.getNombre());
            txtCC.setText(clienteAEditar.getCC());
            txtCorreo.setText(clienteAEditar.getCorreo());
            txtCC.setDisable(true); // Deshabilitar la edición de la cédula si lo deseas
        }
    }

}
