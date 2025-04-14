package Java.Intermedio.model;

public interface CuentaBancaria {
    default String HacerTransaccion(Cliente cliente, int monto){
        String texto = "Transaccion incompleta, Intente mas tarde.";

        return texto;
    }

}
