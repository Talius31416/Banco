package Java.Intermedio.model;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class Cliente extends Persona {
    private String direccion;
    private TipoCuenta tipoCuenta;
    private boolean cuentaBloqueada;

    public Cliente(String nombre, String apellido, String correo, String telefono, String CC, LocalDate fechaNacimiento, String direccion, TipoCuenta tipoCuenta, String usuario, String contrasenia, boolean cuentaBloqueada) {
        super(nombre, apellido, correo, telefono, CC, fechaNacimiento, usuario, contrasenia);
        this.direccion = direccion;
        this.tipoCuenta = tipoCuenta;
        this.cuentaBloqueada = cuentaBloqueada;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public TipoCuenta getTipoCuenta() {
        return tipoCuenta;
    }

    public void setTipoCuenta(TipoCuenta tipoCuenta) {
        this.tipoCuenta = tipoCuenta;
    }
    public boolean isCuentaBloqueada() {
        return cuentaBloqueada;
    }
    public String abrirCuentaBancaria(TipoCuenta tipoCuenta) {
        Connection conn = null;
        PreparedStatement preparedStatement = null;

        try {
            conn = Banco.getConnection();
            String sql = "";

            switch (tipoCuenta) {
                case CORRIENTE:
                    sql = "INSERT INTO corriente (monto, montoTarjetaCredito, tarjetaCredito, CC) VALUES (?, ?, ?, ?)";
                    preparedStatement = conn.prepareStatement(sql);
                    preparedStatement.setFloat(1, 0f);
                    preparedStatement.setFloat(2, 0f);
                    preparedStatement.setBoolean(3, false);
                    preparedStatement.setString(4, this.getCC());
                    break;

                case AHORRO:
                    sql = "INSERT INTO ahorro (monto, intereses, CC) VALUES (?, ?, ?)";
                    preparedStatement = conn.prepareStatement(sql);
                    preparedStatement.setFloat(1, 0f);
                    preparedStatement.setFloat(2, 0.10f);
                    preparedStatement.setString(3, this.getCC());
                    break;

                case EMPRESARIAL:
                    sql = "INSERT INTO empresarial (montoEmpresa, CC) VALUES (?, ?)";
                    preparedStatement = conn.prepareStatement(sql);
                    preparedStatement.setFloat(1, 0f);
                    preparedStatement.setString(2, this.getCC());
                    break;
            }

            if (preparedStatement != null) {
                int filas = preparedStatement.executeUpdate();
                if (filas > 0) {
                    return "Cuenta bancaria creada con éxito";
                } else {
                    return "No se pudo crear la cuenta bancaria";
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return "Error al crear la cuenta bancaria";
        } finally {
            try {
                if (preparedStatement != null) preparedStatement.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return "Error desconocido";
    }
    public ArrayList<CuentaBancaria> getCuentas() {
        ArrayList<CuentaBancaria> cuentas = new ArrayList<>();
        Connection conn = null;
        PreparedStatement ps1 = null;
        PreparedStatement ps2 = null;
        PreparedStatement ps3 = null;
        ResultSet rs = null;
        ResultSet rs2 = null;
        ResultSet rs3 = null;
        try{
            conn = Banco.getConnection();
            String sql = "SELECT * FROM ahorro WHERE CC = "+CC;
            ps1 = conn.prepareStatement(sql);
            rs = ps1.executeQuery();
            while(rs.next()){
                cuentas.add(new Ahorro(rs.getFloat("monto"),
                        rs.getString("CC")));
            }
            sql = "SELECT * FROM empresarial WHERE CC = "+CC;
            ps2= conn.prepareStatement(sql);
            rs2 = ps2.executeQuery();
            while(rs2.next()){
                cuentas.add(new Empresarial(rs2.getFloat("montoEmpresa"),
                        rs2.getString("CC")));
            }
            sql = "SELECT * FROM corriente WHERE CC = "+CC;
            ps3 = conn.prepareStatement(sql);
            rs3 = ps3.executeQuery();
            while(rs3.next()){
                cuentas.add(new Corriente(rs3.getFloat("monto"),
                        rs3.getString("CC"),
                        rs3.getBoolean("TarjetaCredito"),
                        rs3.getFloat("montoTarjetaCredito")));
            }
        }catch (SQLException e){
            e.printStackTrace();
        }finally {
            try{
                if(conn != null){
                    conn.close();
                }
                if(ps1 != null){
                    ps1.close();
                }
                if(ps2 != null){
                    ps2.close();
                }
                if(ps3 != null){
                    ps3.close();
                }
                if(rs != null){
                    rs.close();
                }
                if(rs2 != null){
                    rs2.close();
                }
                if(rs3 != null){
                    rs3.close();
                }
            }catch(SQLException e){
                e.printStackTrace();
            }
        }
        return cuentas;
    }
}


