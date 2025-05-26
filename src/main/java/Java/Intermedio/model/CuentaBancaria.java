package Java.Intermedio.model;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;

public abstract class CuentaBancaria {
    protected float monto;
    protected String CC;

    public CuentaBancaria(float monto, String CC) {
        this.monto = monto;
        this.CC = CC;
    }
    public float getMonto() {
        return monto;
    }
    public boolean hacerTransaccion(float monto, String tipo) {
        if (monto <= 0) return false;

        switch (tipo.toLowerCase()) {
            case "retiro":
                if (this.monto >= monto) {
                    this.monto -= monto;
                    return true;
                } else {
                    return false;
                }

            case "deposito":
                this.monto += monto;
                return true;

            default:
                return false;
        }
    }



    public abstract void depositar(float monto);
    public abstract boolean retirar(float monto);

    public String getCC() {
        return CC;
    }
}

