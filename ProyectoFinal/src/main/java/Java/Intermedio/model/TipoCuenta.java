package Java.Intermedio.model;

public enum TipoCuenta {
    AHORRO(new Ahorro()),
    CORRIENTE(new Corriente()),
    EMPRESARIAL(new Empresarial());

    TipoCuenta(Ahorro ahorro) {
    }
    TipoCuenta(Corriente corriente) {
    }
    TipoCuenta(Empresarial empresarial) {
    }
}
