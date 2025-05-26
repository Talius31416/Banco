package Java.Intermedio.model;

import java.time.LocalDateTime;

public record Transaccion(Cliente clienteInicial, Cliente clienteFinal, TipoCuenta tipoCuenta, LocalDateTime fechaTransaccion, float montoTransaccion) {

    public String transaccionToString() {
        return "Cliente origen: "+clienteInicial.getNombre()+"\nCliente destino: "+clienteFinal.getNombre()+"Tipo de cuenta: "+tipoCuenta.name()+"Fecha: "+fechaTransaccion.toString()+"Monto: "+montoTransaccion;
    }
}
