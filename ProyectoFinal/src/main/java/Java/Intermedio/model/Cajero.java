package Java.Intermedio.model;

import java.sql.*;
import java.time.LocalDate;

public class Cajero extends Persona {
    public Cajero(String nombre, String apellido, String correo, String telefono, String CC, LocalDate fechaNacimiento, String usuario, int contrasenia) {
        super(nombre, apellido, correo, telefono, CC, fechaNacimiento, usuario, contrasenia);
    }
    public String almacenarUsuario(Cliente cliente, String nombre, String apellido, String correo, String telefono, String CC, LocalDate fechaNacimiento, String direccion, TipoCuenta tipoCuenta, String usuario, int contrasenia){
        String texto = "";
        if(!existeCliente(cliente.getCC())){
            Connection connection = null;
            PreparedStatement preparedStatement = null;
            try{
                connection = Banco.getConnection();
                String sql = "INSERT INTO clientes (nombre, apellido, correo, telefono, CC, fechaNacimiento, usuario, contraseña, direccion, tipoCuenta) VALUES (?,?,?,?,?,?,?,?,?,?)";
                preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setString(1, nombre);
                preparedStatement.setString(2, apellido);
                preparedStatement.setString(3, correo);
                preparedStatement.setString(4, telefono);
                preparedStatement.setString(5, CC);
                preparedStatement.setDate(6, Date.valueOf(fechaNacimiento));
                preparedStatement.setString(7, usuario);
                preparedStatement.setInt(8, contrasenia);
                preparedStatement.setString(9, direccion);
                preparedStatement.setString(10, tipoCuenta.name());
                int filas = preparedStatement.executeUpdate();
                if(filas > 0){
                   texto = "El cliente se ha registrado con exito";
                }else{
                    texto = "El cliente no se pudo registrar";
                }
            }catch (SQLException e){
                e.printStackTrace();
            }finally {
                try{
                    if(preparedStatement != null){
                        preparedStatement.close();
                    }if(connection != null){
                        connection.close();
                    }
                }catch (SQLException e){
                    e.printStackTrace();
                }
            }
        }else{
            texto = "El cliente ya existe en la base de datos";
        }
        return texto;
    }

    private Cliente crearCliente(String nombre, String apellido, String correo, String telefono, String CC, LocalDate fechaNacimiento, String direccion, TipoCuenta tipoCuenta, String usuario, int contrasenia) {
        Cliente nuevoCliente = null;
        boolean existe = existeCliente(CC);
        if(!existe){
            nuevoCliente = new Cliente(nombre,apellido,correo,telefono,CC,fechaNacimiento,direccion,tipoCuenta,usuario,contrasenia);
        }
        return nuevoCliente;
    }

    private boolean existeCliente(String CC){
        boolean existe = false;
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try{
            connection = Banco.getConnection();
            String sql = "SELECT COUNT(*) FROM clientes WHERE CC = ?";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, CC);
            resultSet = preparedStatement.executeQuery();

            if(resultSet.next()){
                existe = true;
            }
        }catch(SQLException e){
            e.printStackTrace();
        }finally{
            try{
                if(resultSet != null){
                    resultSet.close();
                }if(preparedStatement != null){
                    preparedStatement.close();
                }if(connection != null){
                    connection.close();
                }
            }catch(SQLException e){
                e.printStackTrace();
            }
        }
        return existe;
    }
}
