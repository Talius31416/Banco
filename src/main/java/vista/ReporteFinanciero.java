package vista;

import Java.Intermedio.model.Banco;
import Java.Intermedio.model.Cliente;
import Java.Intermedio.model.CuentaBancaria;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;

import java.util.List;

public class ReporteFinanciero {

    @FXML
    private Button btnGenerarReporte;

    @FXML
    private Button btnCancelarReporte;

    private final Banco banco = new Banco();

    @FXML
    public void initialize() {
        btnGenerarReporte.setOnAction(e -> generarReporteFinanciero());
        btnCancelarReporte.setOnAction(e -> cancelarReporte());
    }

    private void generarReporteFinanciero() {
        try {
            List<Cliente> clientes = banco.listarClientes();

            int totalClientes = clientes.size();
            int totalCuentas = 0;
            double saldoTotal = 0.0;

            for (Cliente cliente : clientes) {
                for (CuentaBancaria cuenta : cliente.getCuentas()) {
                    totalCuentas++;
                    saldoTotal += cuenta.getMonto();
                }
            }

            String reporte = String.format("""
                    📊 REPORTE FINANCIERO 📊

                    Total de clientes registrados: %d
                    Total de cuentas activas: %d
                    Suma total de saldos: $%.2f
                    """, totalClientes, totalCuentas, saldoTotal);

            mostrarAlerta(Alert.AlertType.INFORMATION, "Reporte Generado", reporte);

        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlerta(Alert.AlertType.ERROR, "Error", "No se pudo generar el reporte.");
        }
    }

    private void cancelarReporte() {
        mostrarAlerta(Alert.AlertType.INFORMATION, "Cancelado", "Reporte cancelado.");
    }

    private void mostrarAlerta(Alert.AlertType tipo, String titulo, String contenido) {
        Alert alerta = new Alert(tipo);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(contenido);
        alerta.showAndWait();
    }
}
