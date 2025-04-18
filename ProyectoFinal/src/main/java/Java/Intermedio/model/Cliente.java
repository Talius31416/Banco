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
    public String getDireccion() {
        return direccion;
    }
    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }
    public TipoCuenta getTipoCuenta() {
        return tipoCuenta;
    }
    public void setTipoCuenta(TipoCuenta tipoCuenta) {
        this.tipoCuenta = tipoCuenta;
    }
    public ArrayList<Transaccion> getTransacciones() {
        return transacciones;
    }
    public void setTransacciones(ArrayList<Transaccion> transacciones) {
        this.transacciones = transacciones;

    }

}
