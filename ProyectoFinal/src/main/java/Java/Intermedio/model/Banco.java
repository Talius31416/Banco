package Java.Intermedio.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Banco {
    private static final String URL = "jdbc:mysql://localhost:3306/banco";
    private static final String USUARIO = "devuser";
    private static final String contrasena = "1077723953";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USUARIO, contrasena);
    }
}
