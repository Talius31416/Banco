package vista;

import Java.Intermedio.model.Cliente;
import Java.Intermedio.model.TipoCuenta;
import Java.Intermedio.model.Banco;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import javafx.event.ActionEvent;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Deposito {

    @FXML
    private TextField fltMontoDeposito;

    @FXML
    private Button btmRealizarDeposito;

    @FXML
    private Button btnCancelarDeposito;

    @FXML
    private Label lblMensaje; // Agrega este en tu FXML si quieres ver mensajes

    private Cliente clienteActual;

    public void setClienteActual(Cliente cliente) {
        this.clienteActual = cliente;
    }

    @FXML
    private void realizarDeposito(ActionEvent event) {
        if (clienteActual == null) {
            mostrarMensaje("Error: cliente no definido.");
            return;
        }

        String input = fltMontoDeposito.getText();
        if (input == null || input.isEmpty()) {
            mostrarMensaje("Ingrese un monto válido.");
            return;
        }

        try {
            float monto = Float.parseFloat(input);
            if (monto <= 0) {
                mostrarMensaje("El monto debe ser mayor a cero.");
                return;
            }

            String tabla = "";
            String campo = "";

            TipoCuenta tipo = clienteActual.getTipoCuenta();
            switch (tipo) {
                case AHORRO -> {
                    tabla = "ahorro";
                    campo = "monto";
                }
                case CORRIENTE -> {
                    tabla = "corriente";
                    campo = "monto";
                }
                case EMPRESARIAL -> {
                    tabla = "empresarial";
                    campo = "montoEmpresa";
                }
            }

            String sql = "UPDATE " + tabla + " SET " + campo + " = " + campo + " + ? WHERE CC = ?";

            try (Connection con = Banco.getConnection();
                 PreparedStatement ps = con.prepareStatement(sql)) {

                ps.setFloat(1, monto);
                ps.setString(2, clienteActual.getCC());
                int filas = ps.executeUpdate();

                if (filas > 0) {
                    mostrarMensaje("Depósito realizado con éxito.");
                } else {
                    mostrarMensaje("No se encontró la cuenta.");
                }

            } catch (SQLException e) {
                e.printStackTrace();
                mostrarMensaje("Error al realizar el depósito.");
            }

        } catch (NumberFormatException e) {
            mostrarMensaje("Formato inválido: debe ingresar un número.");
        }
    }

    @FXML
    private void cancelarDeposito(ActionEvent event) {
        fltMontoDeposito.clear();
        mostrarMensaje("Depósito cancelado.");
    }

    private void mostrarMensaje(String mensaje) {
        if (lblMensaje != null) {
            lblMensaje.setText(mensaje);
        } else {
            System.out.println(mensaje); // Por si no agregaste el label
        }
    }
}

