package Java.Intermedio.model;

public class Empresarial implements CuentaBancaria{
    private float montoEmpleados;
    private float montoEmpresa;
    private Cliente cliente;
    public void setMontoEmpleados(int montoEmpleados) {
        this.montoEmpleados = montoEmpleados;
    }
    public void setMontoEmpresa(float montoEmpresa) {
        this.montoEmpresa = montoEmpresa;
    }
    public float getMontoEmpleados() {
        return montoEmpleados;
    }
    public float getMontoEmpresa() {
        return montoEmpresa;
    }
    @Override
    public void depositar(float monto) {
        this.montoEmpresa += monto;
    }
    @Override
    public boolean retirar(float monto) {
        if(this.montoEmpresa >= monto){
            this.montoEmpresa -= monto;
            return true;
        }
        return false;
    }
}
