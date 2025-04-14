package Java.Intermedio.model;

import java.time.LocalDate;
import java.util.ArrayList;

public class Cliente extends Persona {
    private String direccion;
    private TipoCuenta tipoCuenta;
    private ArrayList<Transaccion> transacciones;
    public Cliente(String nombre, String apellido, String correo, String telefono, String CC, LocalDate fechaNacimiento, String direccion, TipoCuenta tipoCuenta, String usuario, int contrasenia) {
        super(nombre,apellido,correo,telefono,CC,fechaNacimiento, usuario, contrasenia);
        this.direccion = direccion;
        this.tipoCuenta = tipoCuenta;
        this.transacciones = new ArrayList<>();
    }

}
