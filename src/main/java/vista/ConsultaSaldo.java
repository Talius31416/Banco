package vista;

import Java.Intermedio.model.Banco;
import Java.Intermedio.model.Cliente;
import Java.Intermedio.model.TipoCuenta;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.event.ActionEvent;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ConsultaSaldo {

    @FXML
    private Button btnConsultaSaldo;

    @FXML
    private Button btnCancelarConsultaSaldo;

    @FXML
    private Label lblMensaje;

    // Suponemos que ya tienes esta variable seteada desde otro controlador
    private Cliente clienteActual;

    public void setClienteActual(Cliente cliente) {
        this.clienteActual = cliente;
    }

    @FXML
    private void realizarConsulta(ActionEvent event) {
        if (clienteActual == null) {
            lblMensaje.setText("Error: No se ha identificado el cliente.");
            return;
        }

        String CC = clienteActual.getCC();
        TipoCuenta tipo = clienteActual.getTipoCuenta();
        String tabla = "";
        String campoMonto = "";

        switch (tipo) {
            case AHORRO:
                tabla = "ahorro";
                campoMonto = "monto";
                break;
            case CORRIENTE:
                tabla = "corriente";
                campoMonto = "monto";
                break;
            case EMPRESARIAL:
                tabla = "empresarial";
                campoMonto = "montoEmpresa";
                break;
        }

        String sql = "SELECT " + campoMonto + " FROM " + tabla + " WHERE CC = ?";
        try (Connection con = Banco.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, CC);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    float saldo = rs.getFloat(campoMonto);
                    lblMensaje.setText("Saldo actual: $" + saldo);
                } else {
                    lblMensaje.setText("No se encontró saldo para este cliente.");
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            lblMensaje.setText("Error al consultar el saldo.");
        }
    }

    @FXML
    private void cancelarConsulta(ActionEvent event) {
        lblMensaje.setText("Consulta cancelada.");
    }
}

