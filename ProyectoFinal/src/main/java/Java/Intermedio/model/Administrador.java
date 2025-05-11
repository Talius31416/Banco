package Java.Intermedio.model;

import javax.swing.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;
import java.time.Duration;
import java.time.LocalDate;
import java.util.ArrayList;

public class Administrador extends Persona {
    private ArrayList<Cajero> cajeros;
    public Administrador(String nombre, String apellido, String correo, String telefono, String CC, LocalDate fechaNacimiento, String usuario, int contrasenia) {
        super(nombre, apellido, correo, telefono, CC, fechaNacimiento, usuario, contrasenia);
        this.cajeros = new ArrayList<>();
    }
    public String almacenarCajero(Cajero cajero, String nombre, String apellido, String correo, String telefono, String CC, LocalDate fechaNacimiento, String usuario, int contrasenia){
        String texto = "";
        if(!existeCajero(cajero.getCC())){
            Connection connection = null;
            PreparedStatement preparedStatement = null;
            try{
                connection = Banco.getConnection();
                String sql = "INSERT INTO cajeros (nombre, apellido, correo, telefono, CC, fechaNacimiento, usuario, contraseña) VALUES (?,?,?,?,?,?,?,?)";
                preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setString(1, nombre);
                preparedStatement.setString(2, apellido);
                preparedStatement.setString(3, correo);
                preparedStatement.setString(4, telefono);
                preparedStatement.setString(5, CC);
                preparedStatement.setDate(6, Date.valueOf(fechaNacimiento));
                preparedStatement.setString(7, usuario);
                preparedStatement.setInt(8, contrasenia);
                int filas = preparedStatement.executeUpdate();
                if(filas > 0){
                    texto = "El cajero se ha registrado con exito";
                }else{
                    texto = "El cajero no se pudo registrar";
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
            texto = "El cajero ya existe en la base de datos";
        }
        return texto;
    }

    private Cajero crearCajero(String nombre, String apellido, String correo, String telefono, String CC, LocalDate fechaNacimiento, String usuario, int contrasenia) {
        Cajero nuevoCajero = null;
        boolean existe = existeCajero(CC);
        if(!existe){
            nuevoCajero = new Cajero(nombre, apellido, correo, telefono, CC, fechaNacimiento, usuario, contrasenia);
        }
        return nuevoCajero;
    }

    public static boolean existeCajero(String CC){
        boolean existe = false;
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try{
            connection = Banco.getConnection();
            String sql = "SELECT COUNT(*) FROM cajeros WHERE CC = ?";
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
    public Cajero buscarCajero(String CC){
        Cajero cajero = null;
        try(Connection connection = Banco.getConnection()){
            String sql = "SELECT * FROM cajeros WHERE CC = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, CC);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                cajero = new Cajero(
                        resultSet.getString("nombre"),
                        resultSet.getString("apellido"),
                        resultSet.getString("correo"),
                        resultSet.getString("telefono"),
                        resultSet.getString("CC"),
                        resultSet.getDate("fechaNacimiento").toLocalDate(),
                        resultSet.getString("usuario"),
                        resultSet.getInt("contraseña")
                );
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        return cajero;
    }
    public String eliminarCajero(String CC){
        String texto = "";
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try{
            connection = Banco.getConnection();
            String sql = "DELETE FROM cajeros WHERE CC = ?";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, CC);
            int filasAlteradas = preparedStatement.executeUpdate();
            if(filasAlteradas > 0){
                texto = "El cajero se ha eliminado con exito";
            }else{
                texto = "no se encontro ningun cajero con ese id";
            }
        }catch(SQLException e){
            e.printStackTrace();
            texto = "Error al eliminar el cajero";
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
    public String actualizarCajero(String nombre, String apellido, String correo, String telefono, LocalDate fechaNacimiento, String usuario, int contrasenia, String CC){
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try{
            String sql = "UPDATE cajeros set nombre = '"+nombre+"', set apellido = '"+apellido+"', set correo = '"+correo+"', set telefono = '"+telefono+"', set fechaNacimiento = '"+fechaNacimiento+"', set usuario = '"+usuario+"', set contraseña = '"+contrasenia+"' where CC = "+CC;
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.executeUpdate();
        }catch(SQLException e){
            throw new ConexionFallidaConBaseDeDatosException("Error al actualizar el cajero");
        }
        return "El cajero se ha editado con exito";
    }
    public static String crearArchivoTxt(String texto){
        String mensaje = "";
        JFileChooser archivo = new JFileChooser();
        archivo.setDialogTitle("Exportar reporte a txt.");
        int resultado = archivo.showSaveDialog(null);
        if(resultado == JFileChooser.APPROVE_OPTION){
            File file = archivo.getSelectedFile();
            if(!file.getAbsolutePath().endsWith(".txt")){
                file = new File(file.getAbsolutePath() + ".txt");
            }
            try{
                FileWriter fw = new FileWriter(file);
                fw.write(texto);
                mensaje = "Archivo guardado en: " + file.getAbsolutePath();
            } catch (IOException e) {
                mensaje = "El usuario cancelo la operacion";
            }
        }
        return mensaje;
    }
    public static String revisarTransaccionesSospechosas(ArrayList<Transaccion> transacciones, LocalDate fechaAComparar, Cliente clienteDeTransaccion) {
        int contador = 0;
        String mensaje = "";
        ArrayList<Transaccion> transaccionesMismoDia = new ArrayList<>();
        if(!transaccionesEnFecha(transacciones, fechaAComparar)){
            return "No hay transacciones para esa fecha";
        }
        for(Transaccion transaccion : transacciones){
            if(transaccion.fechaTransaccion().toLocalDate().equals(fechaAComparar)){
                transaccionesMismoDia.add(transaccion);
            }
        }
        Transaccion arreglotransacciones[] = transaccionesMismoDia.toArray(new Transaccion[transaccionesMismoDia.size()]);
        for(int i=0; i<arreglotransacciones.length; i++){
            if(i+1<arreglotransacciones.length){
                Duration diferencia = Duration.between(arreglotransacciones[i].fechaTransaccion(), arreglotransacciones[i+1].fechaTransaccion());
                if(diferencia.getSeconds()/3600 <=1){
                    contador++;
                }
            }
        }
        mensaje = "El Cliente "+ clienteDeTransaccion.getNombre()+" ha tenido "+contador+" transacciones sospechosas para la fecha" + fechaAComparar;
        return mensaje;
    }
    private static boolean transaccionesEnFecha(ArrayList<Transaccion> transacciones, LocalDate fecha){
        int contador = 0;
        for(Transaccion transaccion : transacciones){
            if(transaccion.fechaTransaccion().toLocalDate().equals(fecha)){
                contador++;
            }
        }
        return contador>0;
    }

}
