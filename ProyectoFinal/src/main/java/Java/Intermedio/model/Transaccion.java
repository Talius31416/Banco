package Java.Intermedio.model;

import java.time.LocalDate;

public record Transaccion(Cliente cliente, TipoCuenta tipoCuenta, LocalDate fechaTransaccion, int montoTransaccion) {
}
