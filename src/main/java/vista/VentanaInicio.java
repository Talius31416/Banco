package vista;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;

import java.io.IOException;

public class VentanaInicio {

    @FXML
    private Button btnIniciarPrograma;

    @FXML
    private Button btnCerrarSesion;

    @FXML
    public void initialize() {
        btnIniciarPrograma.setOnAction(e -> iniciarPrograma());
        btnCerrarSesion.setOnAction(e -> cerrarSesion());
    }

    private void iniciarPrograma() {
        try {
            // Cargar la ventana de inicio de sesión
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Java/Intermedio/InicioDeSesion.fxml"));
            Parent root = loader.load();

            // Obtener el Stage actual y reemplazar escena
            Stage stage = (Stage) btnIniciarPrograma.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Inicio de Sesión");
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
            mostrarAlerta(Alert.AlertType.ERROR, "Error", "No se pudo cargar la ventana de inicio de sesión.");
        }
    }

    private void cerrarSesion() {
        Stage stage = (Stage) btnCerrarSesion.getScene().getWindow();
        stage.close(); // Cierra la ventana principal (termina la app)
    }

    private void mostrarAlerta(Alert.AlertType tipo, String titulo, String mensaje) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}

