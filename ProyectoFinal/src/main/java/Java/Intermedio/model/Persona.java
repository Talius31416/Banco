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
    public String getNombre() {
        return nombre;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    public String getApellido() {
        return apellido;
    }
    public void setApellido(String apellido) {
        this.apellido = apellido;
    }
    public String getCorreo() {
        return correo;
    }
    public void setCorreo(String correo) {
        this.correo = correo;
    }
    public String getTelefono() {
        return telefono;
    }
    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }
    public String getCC() {
        return CC;
    }
    public void setCC(String CC) {
        this.CC = CC;
    }
    public LocalDate getFechaNacimiento() {
        return fechaNacimiento;
    }
    public void setFechaNacimiento(LocalDate fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }
    public String getUsuario() {
        return usuario;
    }
    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }
    public int getContrasenia() {
        return contrasenia;
    }
    public void setContrasenia(int contrasenia) {
        this.contrasenia = contrasenia;
    }
}
