package Java.Intermedio.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Corriente extends CuentaBancaria {
    private float tarjetaCreditoMonto;
    private boolean tarjetaCredito;

    public Corriente(float monto, String CC, boolean tarjetaCredito, float tarjetaCreditoMonto) {
        super(monto, CC);
        this.tarjetaCredito = tarjetaCredito;
        this.tarjetaCreditoMonto = tarjetaCreditoMonto;
    }

    public void setTarjetaCredito(boolean tarjetaCredito) {
        this.tarjetaCredito = tarjetaCredito;
    }

    public boolean isTarjetaCredito() {
        return tarjetaCredito;
    }

    public void setTarjetaCreditoMonto(float tarjetaCreditoMonto) {
        this.tarjetaCreditoMonto = tarjetaCreditoMonto;
    }

    public float getTarjetaCreditoMonto() {
        return tarjetaCreditoMonto;
    }
    @Override
    public void depositar(float monto) {
        Connection con = null;
        PreparedStatement ps1 = null;
        PreparedStatement ps2 = null;
        ResultSet rs = null;
        try {
            con = Banco.getConnection();
            String sql = "SELECT monto FROM corriente WHERE CC = " + CC;
            ps1 = con.prepareStatement(sql);
            rs = ps1.executeQuery();
            float montoCorriente = 0.0f;
            if (rs.next()) {
                montoCorriente = rs.getFloat("monto") + monto;
            } else {
                return;
            }
            sql = "UPDATE corriente SET monto = " + montoCorriente + " WHERE CC = " + CC;
            ps2 = con.prepareStatement(sql);
            ps2.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (ps1 != null) {
                    ps1.close();
                }
                if (ps2 != null) {
                    ps2.close();
                }
                if (con != null) {
                    con.close();
                }
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    @Override
    public boolean retirar(float monto) {
        Connection con = null;
        PreparedStatement ps1 = null;
        PreparedStatement ps2 = null;
        ResultSet rs = null;

        try {
            con = Banco.getConnection();

            // Consulta monto y tarjetaCredito
            String sql = "SELECT monto, tarjetaCredito FROM corriente WHERE CC = " + CC;
            ps1 = con.prepareStatement(sql);
            rs = ps1.executeQuery();

            if (!rs.next()) {
                return false;
            }

            float saldoCuenta = rs.getFloat("monto");
            boolean tieneTarjetaCredito = rs.getBoolean("tarjetaCredito");

            if (saldoCuenta >= monto) {
                // Puede retirar directamente
                float nuevoSaldo = saldoCuenta - monto;
                sql = "UPDATE corriente SET monto = " + nuevoSaldo + " WHERE CC = " + CC;
                ps2 = con.prepareStatement(sql);
                ps2.executeUpdate();
                return true;
            } else if (tieneTarjetaCredito) {
                // Permitir sobregiro hasta 1,000,000
                float limiteSobregiro = 1000000;
                if (saldoCuenta + limiteSobregiro >= monto) {
                    // Retiro con sobregiro permitido
                    float nuevoSaldo = saldoCuenta - monto;
                    sql = "UPDATE corriente SET monto = " + nuevoSaldo + " WHERE CC = " + CC;
                    ps2 = con.prepareStatement(sql);
                    ps2.executeUpdate();
                    return true;
                } else {
                    return false;
                }
            } else {
                return false;
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (rs != null) rs.close();
                if (ps1 != null) ps1.close();
                if (ps2 != null) ps2.close();
                if (con != null) con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}

