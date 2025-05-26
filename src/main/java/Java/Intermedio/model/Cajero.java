package Java.Intermedio.model;

import java.sql.*;
import java.time.LocalDate;

public class Cajero extends Persona {
    public Cajero(String nombre, String apellido, String correo, String telefono, String CC, LocalDate fechaNacimiento, String usuario, String contrasenia) {
        super(nombre, apellido, correo, telefono, CC, fechaNacimiento, usuario, contrasenia);
    }

}
