package Java.Intermedio.model;

import java.time.LocalDate;

public interface CuentaBancaria {
    void depositar(float monto);
    boolean retirar(float monto);

    default String hacerTransaccion(Cliente clienteInicial, float monto, Cliente clienteFinal) {
        LocalDate fecha = LocalDate.now();
        if(!this.retirar(monto)) {
            return "Transaccion fallida";
        }
        CuentaBancaria cuentaDestino = getCuentaBancaria(clienteInicial);
        cuentaDestino.depositar(monto);

        Transaccion transaccion = new Transaccion(
                clienteInicial,
                clienteFinal,
                clienteFinal.getTipoCuenta(),
                fecha,
                monto
        );
        return "Transaccion exitosa";
    }
    private CuentaBancaria getCuentaBancaria(Cliente cliente) {
        switch(cliente.getTipoCuenta()){
            case AHORRO: return cliente.getAhorro();
            case CORRIENTE: return cliente.getCorriente();
            case EMPRESARIAL: return cliente.getEmpresarial();
            default: throw new IllegalArgumentException("Tipo de cuenta no valido");
        }
    }
}
