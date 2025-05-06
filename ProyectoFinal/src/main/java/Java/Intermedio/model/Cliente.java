import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;

public class Cliente extends Persona {
    private String direccion;
    private TipoCuenta tipoCuenta;
    Ahorro ahorro = null;
    Corriente corriente = null;
    Empresarial empresarial = null;
    public Cliente(String nombre, String apellido, String correo, String telefono, String CC, LocalDate fechaNacimiento, String direccion, TipoCuenta tipoCuenta, String usuario, int contrasenia) {
        super(nombre,apellido,correo,telefono,CC,fechaNacimiento, usuario, contrasenia);
        this.direccion = direccion;
        this.tipoCuenta = tipoCuenta;
        if(tipoCuenta == TipoCuenta.AHORRO){
            ahorro = new Ahorro();
        } else if (tipoCuenta == TipoCuenta.CORRIENTE) {
            corriente = new Corriente();
        }else if(tipoCuenta == TipoCuenta.EMPRESARIAL){
            empresarial = new Empresarial();
        }

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
    public Ahorro getAhorro() {
        return ahorro;
    }
    public void setAhorro(Ahorro ahorro) {
        this.ahorro = ahorro;
    }
    public Corriente getCorriente() {
        return corriente;
    }
    public void setCorriente(Corriente corriente) {
        this.corriente = corriente;
    }
    public Empresarial getEmpresarial() {
        return empresarial;
    }
    public void setEmpresarial(Empresarial empresarial) {
        this.empresarial = empresarial;
    }
    public String añadirTransaccion(Transaccion transaccion) {
        String texto = "";
        Connection conexion = null;
        PreparedStatement preparedStatement = null;
        try{
            conexion = Banco.getConnection();
            String sql = "INSERT INTO transacciones VALUES(?,?,?,?,?,?,?,?)";
            preparedStatement = conexion.prepareStatement(sql);
            preparedStatement.setString(1, transaccion.clienteInicial().getCC());
            preparedStatement.setString(2, transaccion.clienteInicial().getNombre());
            preparedStatement.setString(3, transaccion.clienteFinal().getCC());
            preparedStatement.setString(4, transaccion.clienteFinal().getNombre());
            preparedStatement.setString(5, transaccion.tipoCuenta().name());
            preparedStatement.setDate(6, Date.valueOf(transaccion.fechaTransaccion()));
            preparedStatement.setDouble(7, transaccion.montoTransaccion());

            int filas = preparedStatement.executeUpdate();
            if(filas > 0){
                texto = "Transaccion registrado exitosamente";
            }else{
                texto = "No se pudo registrar el transaccion";
            }


        }catch(SQLException e){
            e.printStackTrace();
        }finally{
            try{
                if(preparedStatement != null){
                    preparedStatement.close();
                }
                if(conexion != null){
                    conexion.close();
                }
            }catch(SQLException e){
                e.printStackTrace();
            }
        }
        return texto;
    }
    public ArrayList<Transaccion> obtenerTransacciones(String CC){
        ArrayList<Transaccion> transacciones = new ArrayList<>();
        Connection conexion = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try{
            conexion = Banco.getConnection();
            String sql = "SELECT * FROM transacciones WHERE CC = (?)";
            preparedStatement = conexion.prepareStatement(sql);
            preparedStatement.setString(1, CC);
            resultSet = preparedStatement.executeQuery();
            while(resultSet.next()){
                transacciones.add(new Transaccion(
                    Cajero.buscarCliente(resultSet.getString("CC")),
                    Cajero.buscarCliente(resultSet.getString("CCclienteFinal")),
                    TipoCuenta.valueOf(resultSet.getString("tipoCuenta").toUpperCase()),
                    resultSet.getDate("fechaTransaccion").toLocalDate(),
                    resultSet.getFloat("monto")
                ));
            }
        }catch (SQLException e){
            e.printStackTrace();
        }finally{
            try{
                if(preparedStatement != null){
                    preparedStatement.close();
                }
                if(conexion != null){
                    conexion.close();
                }
                if(resultSet != null){
                    resultSet.close();
                }
            }catch (SQLException e){
                e.printStackTrace();
            }
        }
        return transacciones;
    }


}


