package vista;

import Java.Intermedio.model.Banco;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import Java.Intermedio.model.Cliente;

import java.io.IOException;
import java.sql.SQLException;

public class TransaccionController {

    @FXML
    private ComboBox<String> comboTipoCuenta;

    @FXML
    private TextField txtDestinatario;

    @FXML
    private TextField txtMonto;

    private Cliente clienteActual;
    @FXML
    private void initialize() {
        comboTipoCuenta.getItems().addAll("ahorro", "corriente", "empresarial");
    }
    public void setClienteActual(Cliente cliente) {
        this.clienteActual = cliente;
    }

    @FXML
    private void realizarTransaccion(ActionEvent event) {
        String destinatarioCC = txtDestinatario.getText();
        String montoTexto = txtMonto.getText();
        String tipoCuenta = comboTipoCuenta.getValue();

        if (destinatarioCC.isEmpty() || montoTexto.isEmpty() || tipoCuenta == null) {
            mostrarAlerta("Error", "Todos los campos son obligatorios, incluyendo el tipo de cuenta.");
            return;
        }

        try {
            float monto = Float.parseFloat(montoTexto);

            if (clienteActual == null) {
                mostrarAlerta("Error", "No hay cliente autenticado.");
                return;
            }

            String resultado = Banco.transferirDinero(clienteActual, destinatarioCC, tipoCuenta, monto);
            mostrarAlerta("Resultado de transacción", resultado);

        } catch (NumberFormatException e) {
            mostrarAlerta("Error", "El monto debe ser un número válido.");
        } catch (SQLException e) {
            mostrarAlerta("Error", "Error en la base de datos: " + e.getMessage());
        }
    }





    @FXML
    private void cancelarTransaccion(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Java/Intermedio/VistaUsuario.fxml"));
            Parent root = loader.load();

            // PASAR EL CLIENTE al nuevo controlador de VistaUsuario
            VistaUsuario controlador = loader.getController();
            controlador.setClienteActual(clienteActual); // Esto es obligatorio

            // Mostrar la escena
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Usuario");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alerta = new Alert(Alert.AlertType.INFORMATION);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }
}

