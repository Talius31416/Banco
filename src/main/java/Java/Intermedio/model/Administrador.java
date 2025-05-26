package Java.Intermedio.model;

import javax.swing.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;
import java.time.Duration;
import java.time.LocalDate;
import java.util.ArrayList;

public class Administrador extends Persona {

    public Administrador(String nombre, String apellido, String correo, String telefono, String CC,
                         LocalDate fechaNacimiento, String usuario, String contrasenia) {
        super(nombre, apellido, correo, telefono, CC, fechaNacimiento, usuario, contrasenia);
    }

    public String almacenarCajero(String nombre, String apellido, String correo, String telefono,
                                  String CC, LocalDate fechaNacimiento, String usuario, String contrasenia) {
        String texto = "";

        if (!existeCajero(CC)) {
            try (Connection connection = Banco.getConnection()) {
                String sql = "INSERT INTO cajeros (nombre, apellido, correo, telefono, CC, fechaNacimiento, usuario, contrasena) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
                try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                    preparedStatement.setString(1, nombre);
                    preparedStatement.setString(2, apellido);
                    preparedStatement.setString(3, correo);
                    preparedStatement.setString(4, telefono);
                    preparedStatement.setString(5, CC);
                    preparedStatement.setDate(6, Date.valueOf(fechaNacimiento));
                    preparedStatement.setString(7, usuario);
                    preparedStatement.setString(8, contrasenia);
                    int filas = preparedStatement.executeUpdate();
                    texto = filas > 0 ? "El cajero se ha registrado con éxito" : "El cajero no se pudo registrar";
                }
            } catch (SQLException e) {
                e.printStackTrace();
                texto = "Error al registrar el cajero";
            }
        } else {
            texto = "El cajero ya existe en la base de datos";
        }

        return texto;
    }

    public static boolean existeCajero(String CC) {
        boolean existe = false;
        String sql = "SELECT COUNT(*) FROM cajeros WHERE CC = ?";
        try (Connection connection = Banco.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, CC);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    existe = resultSet.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return existe;
    }

    public Cajero buscarCajero(String CC) {
        Cajero cajero = null;
        String sql = "SELECT * FROM cajeros WHERE CC = ?";
        try (Connection connection = Banco.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, CC);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    cajero = new Cajero(
                            resultSet.getString("nombre"),
                            resultSet.getString("apellido"),
                            resultSet.getString("correo"),
                            resultSet.getString("telefono"),
                            resultSet.getString("CC"),
                            resultSet.getDate("fechaNacimiento").toLocalDate(),
                            resultSet.getString("usuario"),
                            resultSet.getString("contrasena")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cajero;
    }

    public String eliminarCajero(String CC) {
        String texto = "";
        String sql = "DELETE FROM cajeros WHERE CC = ?";
        try (Connection connection = Banco.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, CC);
            int filasAlteradas = preparedStatement.executeUpdate();
            texto = filasAlteradas > 0
                    ? "El cajero se ha eliminado con éxito"
                    : "No se encontró ningún cajero con ese CC";
        } catch (SQLException e) {
            e.printStackTrace();
            texto = "Error al eliminar el cajero";
        }
        return texto;
    }

    public String actualizarCajero(String nombre, String apellido, String correo, String telefono,
                                   LocalDate fechaNacimiento, String usuario, String contrasenia, String CC) {
        String sql = "UPDATE cajeros SET nombre = ?, apellido = ?, correo = ?, telefono = ?, " +
                "fechaNacimiento = ?, usuario = ?, contrasena = ? WHERE CC = ?";
        try (Connection connection = Banco.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, nombre);
            preparedStatement.setString(2, apellido);
            preparedStatement.setString(3, correo);
            preparedStatement.setString(4, telefono);
            preparedStatement.setDate(5, Date.valueOf(fechaNacimiento));
            preparedStatement.setString(6, usuario);
            preparedStatement.setString(7, contrasenia);
            preparedStatement.setString(8, CC);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new ConexionFallidaConBaseDeDatosException("Error al actualizar el cajero");
        }
        return "El cajero se ha editado con éxito";
    }

    public static File crearArchivoTxt(String texto) {
        JFileChooser archivo = new JFileChooser();
        archivo.setDialogTitle("Exportar reporte a txt.");
        int resultado = archivo.showSaveDialog(null);

        if (resultado == JFileChooser.APPROVE_OPTION) {
            File file = archivo.getSelectedFile();
            if (!file.getAbsolutePath().endsWith(".txt")) {
                file = new File(file.getAbsolutePath() + ".txt");
            }

            try (FileWriter fw = new FileWriter(file)) {
                fw.write(texto);
                return file;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        return null;
    }

    public static String revisarTransaccionesSospechosas(ArrayList<Transaccion> transacciones,
                                                         LocalDate fechaAComparar,
                                                         Cliente clienteDeTransaccion) {
        int contador = 0;
        String mensaje;
        ArrayList<Transaccion> transaccionesMismoDia = new ArrayList<>();

        if (!transaccionesEnFecha(transacciones, fechaAComparar)) {
            return "No hay transacciones para esa fecha";
        }

        for (Transaccion t : transacciones) {
            if (t.fechaTransaccion().toLocalDate().equals(fechaAComparar)) {
                transaccionesMismoDia.add(t);
            }
        }

        Transaccion[] arreglo = transaccionesMismoDia.toArray(new Transaccion[0]);

        for (int i = 0; i < arreglo.length - 1; i++) {
            Duration diferencia = Duration.between(arreglo[i].fechaTransaccion(), arreglo[i + 1].fechaTransaccion());
            if (diferencia.toHours() <= 1) {
                contador++;
            }
        }

        mensaje = "El Cliente " + clienteDeTransaccion.getNombre() +
                " ha tenido " + contador + " transacciones sospechosas para la fecha " + fechaAComparar;
        return mensaje;
    }

    private static boolean transaccionesEnFecha(ArrayList<Transaccion> transacciones, LocalDate fecha) {
        for (Transaccion t : transacciones) {
            if (t.fechaTransaccion().toLocalDate().equals(fecha)) {
                return true;
            }
        }
        return false;
    }
    public ArrayList<Cajero> listarCajeros(){
        ArrayList<Cajero> cajeros = new ArrayList<>();
        Connection connection = null;
        try {
            connection = Banco.getConnection();
            String sql = "SELECT * FROM cajeros";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while(resultSet.next()) {
                    cajeros.add(new Cajero(
                            resultSet.getString("nombre"),
                            resultSet.getString("apellido"),
                            resultSet.getString("correo"),
                            resultSet.getString("telefono"),
                            resultSet.getString("CC"),
                            resultSet.getDate("fechaNacimiento").toLocalDate(),
                            resultSet.getString("usuario"),
                            resultSet.getString("contrasena")
                    ));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cajeros;
    }
    public Cajero buscarCajeroPorCredenciales(String usuario, String contrasenia) {
        String sql = "SELECT * FROM cajeros WHERE usuario = ? AND contrasena = ?";
        try (Connection connection = Banco.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, usuario);
            preparedStatement.setString(2, contrasenia);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return new Cajero(
                        resultSet.getString("nombre"),
                        resultSet.getString("apellido"),
                        resultSet.getString("correo"),
                        resultSet.getString("telefono"),
                        resultSet.getString("CC"),
                        resultSet.getDate("fechaNacimiento").toLocalDate(),
                        resultSet.getString("usuario"),
                        resultSet.getString("contrasena")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

}

