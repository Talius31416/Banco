package Java.Intermedio.model;

import java.time.LocalDate;
import java.util.ArrayList;

public class Administrador extends Persona {
    private ArrayList<Cajero> cajeros;
    public Administrador(String nombre, String apellido, String correo, String telefono, String CC, LocalDate fechaNacimiento, String usuario, int contrasenia) {
        super(nombre, apellido, correo, telefono, CC, fechaNacimiento, usuario, contrasenia);
        this.cajeros = new ArrayList<>();
    }
}
