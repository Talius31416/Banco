package vista;

import Java.Intermedio.model.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;

public class InicioDeSesion {

    @FXML
    private TextField txtNombreUsuario;

    @FXML
    private PasswordField txtClaveAcceso;

    @FXML
    private Button btnRegistrarse;

    @FXML
    private Button btnRegistrarse1; // Botón "Ingresar"

    private Banco banco = new Banco();
    private Administrador administrador;
    private Cajero cajero;
    private Cliente cliente;

    @FXML
    public void initialize() {
        btnRegistrarse1.setOnAction(e -> iniciarSesion());
    }

    private void iniciarSesion() {
        String usuario = txtNombreUsuario.getText().trim();
        String contrasenia = txtClaveAcceso.getText().trim();

        if (usuario.isEmpty() || contrasenia.isEmpty()) {
            mostrarAlerta(Alert.AlertType.WARNING, "Campos vacíos", "Por favor, complete todos los campos.");
            return;
        }

        // Buscar en las 3 categorías (Cliente, Cajero, Administrador)
        Cliente cliente = buscarCliente(usuario, contrasenia);


        Cajero cajero = new Administrador("", "", "", "", "", null, "", "").buscarCajeroPorCredenciales(usuario, contrasenia);
        Administrador admin = buscarAdministrador(usuario, contrasenia);

        if (cliente != null) {
            mostrarAlerta(Alert.AlertType.INFORMATION, "Bienvenido", "Cliente autenticado correctamente.");
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/Java/Intermedio/VistaUsuario.fxml"));
                Parent root = loader.load();

                VistaUsuario controller = loader.getController();
                controller.setClienteActual(cliente);

                Stage stage = new Stage();
                stage.setTitle("Panel del Cliente");
                stage.setScene(new Scene(root));
                stage.show();

                Stage ventanaActual = (Stage) txtNombreUsuario.getScene().getWindow();
                ventanaActual.close();
            } catch (IOException e) {
                e.printStackTrace();
                mostrarAlerta(Alert.AlertType.ERROR, "Error", "No se pudo abrir la ventana del cliente.");
            }
        }
        else if (cajero != null) {
            mostrarAlerta(Alert.AlertType.INFORMATION, "Bienvenido", "Cajero autenticado correctamente.");
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/Java/Intermedio/VistaEmpleado.fxml"));
                Parent root = loader.load();

                // Obtener el controlador y pasarle el nombre del empleado
                VistaEmpleado controller = loader.getController();
                controller.setNombreEmpleado(cajero.getNombre());

                Stage stage = new Stage();
                stage.setTitle("Panel del Empleado");
                stage.setScene(new Scene(root));
                stage.show();

                // Cierra la ventana actual
                Stage ventanaActual = (Stage) txtNombreUsuario.getScene().getWindow();
                ventanaActual.close();

            } catch (IOException e) {
                e.printStackTrace();
                mostrarAlerta(Alert.AlertType.ERROR, "Error", "No se pudo abrir la ventana del cajero.");
            }
        }


        else if(admin !=null)

    {
        mostrarAlerta(Alert.AlertType.INFORMATION, "Bienvenido", "Administrador autenticado correctamente.");
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Java/Intermedio/GestionarEmpleados.fxml"));
            Parent root = loader.load();

            // Pasar el administrador al controlador
            vista.GestionarEmpleados controller = loader.getController();
            controller.setAdministradorActual(admin);

            Stage stage = new Stage();
            stage.setTitle("Gestión de Empleados");
            stage.setScene(new Scene(root));
            stage.show();

            // (Opcional) cerrar la ventana de login
            Stage ventanaActual = (Stage) txtNombreUsuario.getScene().getWindow();
            ventanaActual.close();

        } catch (IOException e) {
            e.printStackTrace();
            mostrarAlerta(Alert.AlertType.ERROR, "Error", "No se pudo abrir la ventana de gestión.");
        }
    }else{
        mostrarAlerta(Alert.AlertType.ERROR, "Error de autenticación", "Usuario o contraseña incorrectos.");
    }

    }

    private Administrador buscarAdministrador(String usuario, String contrasenia) {
        Administrador admin = null;

        String url = "jdbc:mysql://localhost:3306/banco"; // Asegúrate de que la base de datos se llama "banco"
        String user = "root"; // Cambia esto si usas otro usuario
        String password = ""; // Cambia esto si tu usuario tiene contraseña

        String query = "SELECT * FROM administrador WHERE usuario = ? AND contrasena = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        try  {
            conn = Banco.getConnection();
            stmt = conn.prepareStatement(query);

            stmt.setString(1, usuario);
            stmt.setString(2, contrasenia);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    admin = new Administrador(
                            rs.getString("nombre"),
                            rs.getString("apellido"),
                            rs.getString("correo"),
                            rs.getString("telefono"),
                            rs.getString("CC"),
                            rs.getDate("fechaNacimiento").toLocalDate(),
                            rs.getString("usuario"),
                            rs.getString("contrasena")
                            // Puedes añadir `fotoPerfil` si tu clase lo necesita
                    );
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return admin;
    }

    private Cajero buscarCajero(String usuario, String contrasenia) {
        Cajero cajero = null;

        String query = "SELECT * FROM cajeros WHERE usuario = ? AND contrasena = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        try  {
            conn = Banco.getConnection();
            stmt = conn.prepareStatement(query);

            stmt.setString(1, usuario);
            stmt.setString(2, contrasenia);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                cajero = new Cajero(
                        rs.getString("nombre"),
                        rs.getString("apellido"),
                        rs.getString("correo"),
                        rs.getString("telefono"),
                        rs.getString("CC"),
                        rs.getDate("fechaNacimiento").toLocalDate(),
                        rs.getString("usuario"),
                        rs.getString("contrasena")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return cajero;
    }
    private Cliente buscarCliente(String usuario, String contrasenia) {
        Cliente cliente = null;

        String query = "SELECT * FROM clientes WHERE usuario = ? AND contrasena = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        try  {
            conn = Banco.getConnection();
            stmt = conn.prepareStatement(query);
            stmt.setString(1, usuario);
            stmt.setString(2, contrasenia);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                cliente = new Cliente(
                        rs.getString("nombre"),
                        rs.getString("apellido"),
                        rs.getString("correo"),
                        rs.getString("telefono"),
                        rs.getString("CC"),
                        rs.getDate("fechaNacimiento").toLocalDate(),
                        rs.getString("direccion"),
                        TipoCuenta.valueOf(rs.getString("tipoCuenta").toUpperCase()),
                        rs.getString("usuario"),
                        rs.getString("contrasena"),
                        rs.getBoolean("cuentaBloqueada")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            System.err.println("TipoCuenta inválido: " + e.getMessage());
        }

        return cliente;
    }


    private void mostrarAlerta(Alert.AlertType tipo, String titulo, String contenido) {
        Alert alerta = new Alert(tipo);
        alerta.setTitle(titulo);
        alerta.setContentText(contenido);
        alerta.showAndWait();
    }
}
