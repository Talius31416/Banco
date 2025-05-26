package vista;

import Java.Intermedio.model.Banco;
import Java.Intermedio.model.Cliente;
import Java.Intermedio.model.CuentaBancaria;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import java.io.IOException;

import java.io.IOException;

public class Retiro {

    @FXML
    private TextField fltMontoRetiro;

    @FXML
    private Button btmRealizarRetiro;

    @FXML
    private Button btnCancelarRetiro;

    private Cliente clienteActual;
    private final Banco banco = new Banco();

    @FXML
    public void initialize() {
        btmRealizarRetiro.setOnAction(e -> realizarRetiro());
        btnCancelarRetiro.setOnAction(e -> {
            try {
                cancelarRetiro();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
    }

    public void setClienteActual(Cliente cliente) {
        this.clienteActual = cliente;
    }

    private void realizarRetiro() {
        if (clienteActual == null) {
            mostrarAlerta(Alert.AlertType.ERROR, "Error", "No se ha establecido el cliente.");
            return;
        }

        String textoMonto = fltMontoRetiro.getText().trim();
        if (textoMonto.isEmpty()) {
            mostrarAlerta(Alert.AlertType.WARNING, "Campo vacío", "Por favor, ingrese un monto.");
            return;
        }

        float monto;
        try {
            monto = Float.parseFloat(textoMonto);
            if (monto <= 0) {
                mostrarAlerta(Alert.AlertType.WARNING, "Monto inválido", "El monto debe ser mayor que cero.");
                return;
            }
        } catch (NumberFormatException e) {
            mostrarAlerta(Alert.AlertType.ERROR, "Formato inválido", "Ingrese un número válido.");
            return;
        }

        CuentaBancaria cuenta = clienteActual.getCuentas().stream().findFirst().orElse(null);
        if (cuenta == null) {
            mostrarAlerta(Alert.AlertType.ERROR, "Sin cuenta", "El cliente no tiene cuentas asociadas.");
            return;
        }

        if (!cuenta.retirar(monto)) {
            mostrarAlerta(Alert.AlertType.WARNING, "Fondos insuficientes", "No hay suficiente saldo.");
            return;
        }

        mostrarAlerta(Alert.AlertType.INFORMATION, "Éxito", "Retiro exitoso. Saldo actual: $" + cuenta.getMonto());
        fltMontoRetiro.clear();
    }


    @FXML
    private void cancelarRetiro() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Java/Intermedio/vistaUsuario.fxml"));
        Parent root = loader.load();

        // Stage directamente del botón
        Stage stage = (Stage) btnCancelarRetiro.getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }



    private void mostrarAlerta(Alert.AlertType tipo, String titulo, String mensaje) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}
