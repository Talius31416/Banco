package Java.Intermedio.model;

import java.sql.*;
import java.time.LocalDate;

public class Cajero extends Persona {
    public Cajero(String nombre, String apellido, String correo, String telefono, String CC, LocalDate fechaNacimiento, String usuario, int contrasenia) {
        super(nombre, apellido, correo, telefono, CC, fechaNacimiento, usuario, contrasenia);
    }
    public String almacenarUsuario(Cliente cliente, String nombre, String apellido, String correo, String telefono, String CC, LocalDate fechaNacimiento, String direccion, TipoCuenta tipoCuenta, String usuario, int contrasenia, boolean cuentaBloqueada){
        String texto = "";
        if(!existeCliente(cliente.getCC())){
            Connection connection = null;
            PreparedStatement preparedStatement = null;
            try{
                connection = Banco.getConnection();
                String sql = "INSERT INTO clientes (nombre, apellido, correo, telefono, CC, fechaNacimiento, usuario, contraseña, direccion, tipoCuenta, cuentabloqueada) VALUES (?,?,?,?,?,?,?,?,?,?,?)";
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
                preparedStatement.setBoolean(11, cuentaBloqueada);
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

    private Cliente crearCliente(String nombre, String apellido, String correo, String telefono, String CC, LocalDate fechaNacimiento, String direccion, TipoCuenta tipoCuenta, String usuario, int contrasenia, boolean cuentaBoqueada) {
        Cliente nuevoCliente = null;
        boolean existe = existeCliente(CC);
        if(!existe){
            nuevoCliente = new Cliente(nombre,apellido,correo,telefono,CC,fechaNacimiento,direccion,tipoCuenta,usuario,contrasenia, cuentaBoqueada);
        }
        return nuevoCliente;
    }

    public static boolean existeCliente(String CC){
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
                int count = resultSet.getInt(1);
                existe = count > 0;
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
    public static Cliente buscarCliente(String CC){
        Cliente cliente = null;
        try(Connection connection = Banco.getConnection()){
            String sql = "SELECT * FROM clientes WHERE CC = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, CC);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                cliente = new Cliente(
                        resultSet.getString("nombre"),
                        resultSet.getString("apellido"),
                        resultSet.getString("correo"),
                        resultSet.getString("telefono"),
                        resultSet.getString("CC"),
                        resultSet.getDate("fechaNacimiento").toLocalDate(),
                        resultSet.getString("direccion"),
                        TipoCuenta.valueOf(resultSet.getString("tipoCuenta").toUpperCase()),
                        resultSet.getString("usuario"),
                        resultSet.getInt("contraseña"),
                        resultSet.getBoolean("cuentaBloqueada")
                );
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        return cliente;
    }
    public String eliminarCliente(String CC){
        String texto = "";
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try{
            connection = Banco.getConnection();
            String sql = "DELETE FROM clientes WHERE CC = ?";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, CC);
            int filasAlteradas = preparedStatement.executeUpdate();
            if(filasAlteradas > 0){
                texto = "El cliente se ha eliminado con exito";
            }else{
                texto = "no se encontro ningun cliente con ese id";
            }
        }catch(SQLException e){
            e.printStackTrace();
            texto = "Error al eliminar el cliente";
        }finally{
            try{
                if(preparedStatement != null){
                    preparedStatement.close();
                }if(connection != null){
                    connection.close();
                }
            }catch(SQLException e){
                e.printStackTrace();
            }
        }
        return texto;
    }
    public String actualizarCliente(String nombre, String apellido, String correo, String telefono, LocalDate fechaNacimiento, String usuario, int contrasenia,String direccion, String CC, TipoCuenta tipoCuenta, boolean cuentaBoqueada) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try{
            String sql = "UPDATE clientes set nombre = '"+nombre+"', set apellido = '"+apellido+"', set correo = '"+correo+"', set telefono = '"+telefono+"', set fechaNacimiento = '"+fechaNacimiento+"', set usuario = '"+usuario+"', set contraseña = '"+contrasenia+"',set direccion = '"+direccion+"' set tipoCuenta = '"+tipoCuenta.name()+"'set cuentaBloqueada = '"+cuentaBoqueada+"' where CC = "+CC;
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.executeUpdate();
        }catch(SQLException e){
            throw new ConexionFallidaConBaseDeDatosException("Error al actualizar el cliente");
        }
        return "El cliente se ha editado con exito";
    }


}
