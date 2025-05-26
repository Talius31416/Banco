package Java.Intermedio.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Ahorro extends CuentaBancaria{
    private final float interesesPorMes = 0.10f;
     public Ahorro(float monto, String CC) {
         super(monto, CC);

     }

     public float getInteresesPorMes() {
         return interesesPorMes*100;
     }
    @Override
    public void depositar(float monto) {
        if (monto <= 0) return;

        Connection con = null;
        PreparedStatement ps1 = null;
        PreparedStatement ps2 = null;
        ResultSet rs = null;

        try {
            con = Banco.getConnection();

            String sql = "SELECT monto FROM ahorro WHERE CC = " + CC;
            ps1 = con.prepareStatement(sql);
            rs = ps1.executeQuery();

            float saldoActual;
            if (rs.next()) {
                saldoActual = rs.getFloat("monto") + monto;
            } else {
                return;
            }

            sql = "UPDATE ahorro SET monto = " + saldoActual + " WHERE CC = " + CC;
            ps2 = con.prepareStatement(sql);
            ps2.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null){
                    rs.close();
                }
                if (ps1 != null){
                    ps1.close();
                }
                if (ps2 != null){
                    ps2.close();
                }
                if (con != null){
                    con.close();
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
         try{
             con = Banco.getConnection();
             String sql = "SELECT monto FROM ahorro WHERE CC = " + CC;
             ps1 = con.prepareStatement(sql);
             rs = ps1.executeQuery();
             if(!rs.next()){
                 return false;
             }
             float saldoActual = rs.getFloat("monto");
             if (saldoActual < monto){
                 return false;
             }
             sql = "UPDATE ahorro SET monto = " + (saldoActual-monto) + " WHERE CC = " + CC;
             ps2 = con.prepareStatement(sql);
             ps2.executeUpdate();
         }catch (SQLException e){
             e.printStackTrace();
         }finally {
             try{
                 if (rs != null){
                     rs.close();
                 }
                 if(ps1 != null){
                     ps1.close();
                 }
                 if (ps2 != null){
                     ps2.close();
                 }
                 if (con != null){
                     con.close();
                 }
             }catch (SQLException e){
                 e.printStackTrace();
             }
         }
         return true;
    }


}
