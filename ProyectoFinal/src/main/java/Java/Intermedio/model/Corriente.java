package Java.Intermedio.model;

public class Corriente implements CuentaBancaria{
    private float monto;
    private Cliente cliente;
    private boolean tarjetaCredito;
    public void setMonto(float monto) {
        this.monto = monto;
    }
    public float getMonto() {
        return monto;
    }
    @Override
    public void depositar(float monto) {
        this.monto += monto;
    }
    @Override
    public boolean retirar(float monto) {
        if(this.monto >= monto || (tarjetaCredito && this.monto >= monto-1000000)) {
            this.monto -= monto;
            return true;
        }
        return false;
    }
}
