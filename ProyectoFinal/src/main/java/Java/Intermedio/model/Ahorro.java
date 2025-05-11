package Java.Intermedio.model;

public class Ahorro implements CuentaBancaria{
    private float monto;
    private float interesesPorMes;
    private Cliente cliente;
     public void setMonto(int monto) {
         this.monto = monto;
     }
     public void setInteresesPorMes(float interesesPorMes) {
         this.interesesPorMes = interesesPorMes;
     }
     public float getMonto() {
         return monto;
     }
     public float getInteresesPorMes() {
         return interesesPorMes;
     }

    @Override
    public void depositar(float monto) {
         this.monto += monto;
    }
    @Override
    public boolean retirar(float monto) {
         if(this.monto >= monto){
             this.monto -= monto;
             return true;
         }
         return false;
    }
}
