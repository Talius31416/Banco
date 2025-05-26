package vista;
import Java.Intermedio.model.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.Scene;
import javafx.scene.Parent;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;

public class VistaUsuario {

    @FXML
    private Label txtNombreUsuario;

    @FXML
    private Button btnConsultarSaldo;
    @FXML
    private Button btnDepositar;
    @FXML
    private Button btnRetirar;
    @FXML
    private Button btnInformeTransacciones;
    @FXML
    private Button btnRealizarTransaccion;
    @FXML
    private Button btnHome;

    private Cliente clienteActual;

    @FXML
    public void initialize() {
        btnConsultarSaldo.setOnAction(e -> consultarSaldo());
        btnDepositar.setOnAction(e -> abrirDeposito());
        btnRetirar.setOnAction(e -> abrirRetiro());
        btnInformeTransacciones.setOnAction(e -> mostrarTransacciones());
        btnRealizarTransaccion.setOnAction(e -> abrirTransaccion());
        btnHome.setOnAction(e -> volverHome());
    }

    public void setClienteActual(Cliente cliente) {
        this.clienteActual = cliente;
        if (cliente != null) {
            txtNombreUsuario.setText(cliente.getNombre());

            // Configurar botones cuando el cliente ya está definido
            btnConsultarSaldo.setOnAction(e -> consultarSaldo());
            btnDepositar.setOnAction(e -> abrirDeposito());
            btnRetirar.setOnAction(e -> abrirRetiro());
            btnInformeTransacciones.setOnAction(e -> mostrarTransacciones());
            btnRealizarTransaccion.setOnAction(e -> abrirTransaccion());
            btnHome.setOnAction(e -> volverHome());
        } else {
            System.err.println("Cliente es null en setClienteActual (VistaUsuario)");
        }
    }




    private void consultarSaldo() {
        if (clienteActual == null) {
            mostrarAlerta(Alert.AlertType.ERROR, "Error", "Cliente no definido.");
            return;
        }

        Optional<CuentaBancaria> cuenta = clienteActual.getCuentas().stream().findFirst();
        if (cuenta.isPresent()) {
            double saldo = cuenta.get().getMonto();
            mostrarAlerta(Alert.AlertType.INFORMATION, "Saldo actual", "Su saldo es: $" + saldo);
        } else {
            mostrarAlerta(Alert.AlertType.WARNING, "Sin cuenta", "El cliente no tiene cuentas asociadas.");
        }
    }

    @FXML
    private void abrirDeposito() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Java/Intermedio/deposito.fxml")); // ✅ Ruta correcta
            Parent root = loader.load();

            // Si necesitas pasar el cliente al controlador, hazlo después de load()
            Deposito controller = loader.getController();
            controller.setClienteActual(clienteActual);

            Stage stage = new Stage();
            stage.setTitle("Depósito");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void abrirRetiro() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Java/Intermedio/retiro.fxml"));
            Parent root = loader.load();

            // Enviar cliente al controlador de Retiro
            Retiro controller = loader.getController();
            controller.setClienteActual(clienteActual);

            Stage stage = (Stage) btnRetirar.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Retiro");
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
            mostrarAlerta(Alert.AlertType.ERROR, "Error", "No se pudo abrir la ventana de retiro.");
        }
    }

    private void abrirTransaccion() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Java/Intermedio/transaccion.fxml"));
            Parent root = loader.load();

            TransaccionController controlador = loader.getController();

            controlador.setClienteActual(clienteActual);

            Stage stage = (Stage) btnRealizarTransaccion.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Transacción");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            mostrarAlerta(Alert.AlertType.ERROR, "Error", "No se pudo abrir la ventana de transacción.");
        }
    }




    private void mostrarTransacciones() {
        if (clienteActual == null) {
            mostrarAlerta(Alert.AlertType.ERROR, "Error", "Cliente no definido.");
            return;
        }

        ArrayList<Transaccion> transacciones = Banco.obtenerTransacciones(clienteActual.getCC());

        if (transacciones == null || transacciones.isEmpty()) {
            mostrarAlerta(Alert.AlertType.INFORMATION, "Transacciones", "No se encontraron transacciones para este cliente.");
            return;
        }

        StringBuilder cuerpoCorreo = new StringBuilder();
        cuerpoCorreo.append("Estimado/a ").append(clienteActual.getNombre()).append(",\n\n")
                .append("Aquí están sus transacciones recientes:\n\n");

        for (Transaccion t : transacciones) {
            cuerpoCorreo.append("ID: ").append(t.clienteInicial().getCC()).append("\n,")
                    .append(", Monto: ").append(t.montoTransaccion()).append("\n,")
                    .append(", Fecha: ").append(t.fechaTransaccion()).append("\n,")
                    .append(", Destinatario: ").append(t.clienteFinal().getNombre())
                    .append("\n");
        }

        cuerpoCorreo.append("\nAtentamente,\nCertus Bank.");

        // Crear archivo .txt
        String contenido = cuerpoCorreo.toString();
        String nombreArchivo = "Transacciones_" + clienteActual.getCC() + ".txt";
        File archivo = Administrador.crearArchivoTxt(contenido);

        try {
            new EnvioCorreo().enviarCorreoConAdjunto(
                    clienteActual.getCorreo(),              // destinatario
                    "Transacciones recientes",              // asunto
                    "Adjunto encontrará el historial de transacciones.\n\n" + contenido, // cuerpo
                    archivo                                 // archivo adjunto
            );
            mostrarAlerta(Alert.AlertType.INFORMATION, "Correo enviado", "Las transacciones fueron enviadas a su correo.");
        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlerta(Alert.AlertType.ERROR, "Error", "No se pudo enviar el correo.");
        }
    }



    @FXML
    public void volverHome() {
        System.out.println("Botón Home presionado"); // Para verificar en consola

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Java/Intermedio/VentanaInicio.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) btnHome.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Inicio");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            mostrarAlerta(Alert.AlertType.ERROR, "Error", "No se pudo cargar la ventana de inicio.");
        }
    }


    private void cargarVentana(String rutaFXML, String tituloVentana) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(rutaFXML));
            Parent root = loader.load();
            Stage stage = (Stage) btnHome.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle(tituloVentana);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            mostrarAlerta(Alert.AlertType.ERROR, "Error", "No se pudo cargar " + tituloVentana + ".");
        }
    }

    private void mostrarAlerta(Alert.AlertType tipo, String titulo, String contenido) {
        Alert alerta = new Alert(tipo);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(contenido);
        alerta.showAndWait();
    }
    @FXML
    private void abrirVistaTransaccion(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Java/Intermedio/transaccion.fxml"));
            Parent root = loader.load();

            // Obtener controlador y pasarle el cliente actual
            TransaccionController controlador = loader.getController();
            controlador.setClienteActual(clienteActual); // este paso es crucial

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Transacción");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
