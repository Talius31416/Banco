package Java.Intermedio.model;

import java.time.LocalDate;

public abstract class Persona {
    private String nombre;
    private String apellido;
    private String correo;
    private String telefono;
    private String CC;
    private LocalDate fechaNacimiento;
    private String usuario;
    private int contrasenia;
    public Persona(String nombre, String apellido, String correo, String telefono, String CC, LocalDate fechaNacimiento, String usuario, int contrasenia) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.correo = correo;
        this.telefono = telefono;
        this.CC = CC;
        this.fechaNacimiento = fechaNacimiento;
        this.usuario = usuario;
        this.contrasenia = contrasenia;
    }
}
