package Java.Intermedio.model;



import java.io.File;
import java.io.FileInputStream;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;

public class Banco {
    private static final String URL = "jdbc:mysql://localhost:3306/banco";
    private static final String USUARIO = "devuser";
    private static final String contrasena = "1077723953";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USUARIO, contrasena);
    }

    public String almacenarUsuario(Cliente cliente, String nombre, String apellido, String correo, String telefono, String CC, LocalDate fechaNacimiento, String direccion, TipoCuenta tipoCuenta, String usuario, String contrasenia, boolean cuentaBloqueada){
        String texto = "";
        if(!existeCliente(cliente.getCC())){
            Connection connection = null;
            PreparedStatement preparedStatement = null;
            try{
                connection = Banco.getConnection();
                String sql = "INSERT INTO clientes (nombre, apellido, correo, telefono, CC, fechaNacimiento, usuario, contrasena, direccion, tipoCuenta, cuentabloqueada) VALUES (?,?,?,?,?,?,?,?,?,?,?)";
                preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setString(1, nombre);
                preparedStatement.setString(2, apellido);
                preparedStatement.setString(3, correo);
                preparedStatement.setString(4, telefono);
                preparedStatement.setString(5, CC);
                preparedStatement.setDate(6, Date.valueOf(fechaNacimiento));
                preparedStatement.setString(7, usuario);
                preparedStatement.setString(8, contrasenia);
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

    private Cliente crearCliente(String nombre, String apellido, String correo, String telefono, String CC, LocalDate fechaNacimiento, String direccion, TipoCuenta tipoCuenta, String usuario, String contrasenia, boolean cuentaBoqueada) {
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
                        resultSet.getString("contrasena"),
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
    public String actualizarCliente(String nombre, String apellido, String correo, String telefono,
                                    LocalDate fechaNacimiento, String usuario, String contrasenia,
                                    String direccion, String CC, TipoCuenta tipoCuenta, boolean cuentaBloqueada) {
        String sql = "UPDATE clientes SET nombre = ?, apellido = ?, correo = ?, telefono = ?, fechaNacimiento = ?, " +
                "usuario = ?, contrasena = ?, direccion = ?, tipoCuenta = ?, cuentaBloqueada = ? WHERE CC = ?";
        try (Connection connection = Banco.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, nombre);
            preparedStatement.setString(2, apellido);
            preparedStatement.setString(3, correo);
            preparedStatement.setString(4, telefono);
            preparedStatement.setDate(5, Date.valueOf(fechaNacimiento));
            preparedStatement.setString(6, usuario);
            preparedStatement.setString(7, contrasenia);
            preparedStatement.setString(8, direccion);
            preparedStatement.setString(9, tipoCuenta.name());
            preparedStatement.setBoolean(10, cuentaBloqueada);
            preparedStatement.setString(11, CC);  // WHERE CC = ?

            int filasAfectadas = preparedStatement.executeUpdate();

            if (filasAfectadas == 0) {
                return "No se encontró ningún cliente con esa cédula.";
            }

            return "El cliente se ha editado con éxito.";

        } catch (SQLException e) {
            e.printStackTrace();
            throw new ConexionFallidaConBaseDeDatosException("Error al actualizar el cliente.");
        }
    }

    public ArrayList<Cliente> listarClientes(){
        ArrayList<Cliente> clientes = new ArrayList<>();
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            connection = Banco.getConnection();
            String sql = "SELECT * FROM clientes";
            preparedStatement = connection.prepareStatement(sql);
            resultSet = preparedStatement.executeQuery();
            while(resultSet.next()){
                clientes.add(new Cliente(
                        resultSet.getString("nombre"),
                        resultSet.getString("apellido"),
                        resultSet.getString("correo"),
                        resultSet.getString("telefono"),
                        resultSet.getString("CC"),
                        resultSet.getDate("fechaNacimiento").toLocalDate(),
                        resultSet.getString("direccion"),
                        TipoCuenta.valueOf(resultSet.getString("tipoCuenta").toUpperCase()),
                        resultSet.getString("usuario"),
                        resultSet.getString("contrasena"),
                        resultSet.getBoolean("cuentaBloqueada")
                ));
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        return clientes;
    }
    public static CuentaBancaria obtenerCuentaPorTipo(Cliente cliente, TipoCuenta tipoCuenta) {
        switch (tipoCuenta) {
            case AHORRO:
                return new Ahorro(getSaldoAhorro(cliente.getCC()), cliente.getCC());
            case EMPRESARIAL:
                return new Empresarial(getSaldoEmpresarial(cliente.getCC()), cliente.getCC());
            case CORRIENTE:
                return getCuentaCorriente(cliente.getCC());
            default:
                return null;
        }
    }

    private static float getSaldoAhorro(String cc) {
        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement("SELECT monto FROM ahorro WHERE CC = ?")) {
            ps.setString(1, cc);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getFloat("monto");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0f;
    }

    private static float getSaldoEmpresarial(String cc) {
        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement("SELECT montoEmpresa FROM empresarial WHERE CC = ?")) {
            ps.setString(1, cc);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getFloat("montoEmpresa");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0f;
    }

    private static Corriente getCuentaCorriente(String cc) {
        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement("SELECT * FROM corriente WHERE CC = ?")) {
            ps.setString(1, cc);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new Corriente(
                        rs.getFloat("monto"),
                        cc,
                        rs.getBoolean("tarjetaCredito"),
                        rs.getFloat("montoTarjetaCredito")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    public static String transferirDinero(Cliente clienteInicial, String cedulaDestino, String tipoCuenta, float monto) throws SQLException {
        Connection connection = null;
        PreparedStatement stmt = null;

        try {
            connection = getConnection();
            connection.setAutoCommit(false);

            // Obtener cuenta de origen
            CuentaBancaria cuentaOrigen = obtenerCuenta(clienteInicial, clienteInicial.getTipoCuenta().toString().toUpperCase());
            if (cuentaOrigen == null) {
                throw new SQLException("Cuenta de origen no encontrada.");
            }

            Cliente clienteFinal = buscarCliente(cedulaDestino);
            if (clienteFinal == null) {
                throw new SQLException("Cliente de destino no encontrado.");
            }

            CuentaBancaria cuentaDestino = obtenerCuenta(clienteFinal, tipoCuenta);
            if (cuentaDestino == null) {
                throw new SQLException("Cuenta de destino no encontrada.");
            }

            if (cuentaOrigen.getMonto() < monto) {
                throw new SQLException("Fondos insuficientes en la cuenta de origen.");
            }

            cuentaOrigen.retirar(monto);
            cuentaDestino.depositar(monto);

            // Registrar la transacción
            String query = "INSERT INTO transacciones (CC, ClienteInicial, CCclienteFinal, clienteFinal, tipoCuenta, fechaTransaccion, monto) VALUES (?, ?, ?, ?, ?, ?, ?)";
            stmt = connection.prepareStatement(query);
            stmt.setString(1, clienteInicial.getCC());
            stmt.setString(2, clienteInicial.getNombre());
            stmt.setString(3, clienteFinal.getCC());
            stmt.setString(4, clienteFinal.getNombre());
            stmt.setString(5, tipoCuenta);
            stmt.setDate(6, Date.valueOf(LocalDate.now()));
            stmt.setFloat(7, monto);
            stmt.executeUpdate();

            // Confirmar la transacción
            connection.commit();
            System.out.println("Transferencia realizada y transacción registrada correctamente.");

        } catch (SQLException e) {
            if (connection != null) {
                connection.rollback(); // Deshacer cambios en caso de error
            }
            throw e;
        } finally {
            if (stmt != null) stmt.close();
            if (connection != null) connection.close();
        }
        return "Transaccion exitosa";
    }
    public static void actualizarSaldoEnBaseDeDatos(Connection connection, CuentaBancaria cuenta, String cedula, String tipoCuentaOrigen, String tipoCuentaDestino, float monto) throws SQLException {
        String columnaOrigen;
        switch (tipoCuentaOrigen.toLowerCase()) {
            case "ahorro":
            case "corriente":
                columnaOrigen = "monto";
                break;
            case "empresarial":
                columnaOrigen = "montoEmpresa";
                break;
            default:
                throw new IllegalArgumentException("Tipo de cuenta origen inválido: " + tipoCuentaOrigen);
        }

        String columnaDestino;
        switch (tipoCuentaDestino.toLowerCase()) {
            case "ahorro":
            case "corriente":
                columnaDestino = "monto";
                break;
            case "empresarial":
                columnaDestino = "montoEmpresa";
                break;
            default:
                throw new IllegalArgumentException("Tipo de cuenta destino inválido: " + tipoCuentaDestino);
        }

        float saldoOrigen = 0;
        float saldoDestino = 0;

        String sqlOrigen = "SELECT " + columnaOrigen + " FROM " + tipoCuentaOrigen + " WHERE CC = ?";
        try (PreparedStatement psOrigen = connection.prepareStatement(sqlOrigen)) {
            psOrigen.setString(1, cuenta.getCC());
            try (ResultSet rs = psOrigen.executeQuery()) {
                if (rs.next()) {
                    saldoOrigen = rs.getFloat(1);
                } else {
                    throw new SQLException("No se encontró cuenta de origen con CC: " + cedula);
                }
            }
        }

        String sqlDestino = "SELECT " + columnaDestino + " FROM " + tipoCuentaDestino + " WHERE CC = ?";
        try (PreparedStatement psDestino = connection.prepareStatement(sqlDestino)) {
            psDestino.setString(1, cedula);
            try (ResultSet rs = psDestino.executeQuery()) {
                if (rs.next()) {
                    saldoDestino = rs.getFloat(1);
                } else {
                    throw new SQLException("No se encontró cuenta de destino con CC: " + cedula);
                }
            }
        }

        String sqlUpdateDestino = "UPDATE " + tipoCuentaDestino + " SET " + columnaDestino + " = ? WHERE CC = ?";
        try (PreparedStatement ps = connection.prepareStatement(sqlUpdateDestino)) {
            ps.setFloat(1, saldoDestino + monto);
            ps.setString(2, cedula);
            int rows = ps.executeUpdate();
            if (rows == 0) throw new SQLException("No se actualizó saldo destino");
        }

        String sqlUpdateOrigen = "UPDATE " + tipoCuentaOrigen + " SET " + columnaOrigen + " = ? WHERE CC = ?";
        try (PreparedStatement ps = connection.prepareStatement(sqlUpdateOrigen)) {
            ps.setFloat(1, saldoOrigen - monto);
            ps.setString(2, cedula);
            int rows = ps.executeUpdate();
            if (rows == 0) throw new SQLException("No se actualizó saldo origen");
        }
    }



    public static CuentaBancaria obtenerCuenta(Cliente cliente, String tipoCuenta) throws SQLException {
        String cedula = cliente.getCC();
        String tabla = tipoCuenta.toLowerCase(); // ¡Usar el parámetro correctamente!
        String query = "SELECT * FROM " + tabla + " WHERE CC = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, cedula);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                float monto;
                switch (tipoCuenta.toLowerCase()) {
                    case "empresarial":
                        monto = rs.getFloat("montoEmpresa"); // asegúrate de que esta columna exista
                        return new Empresarial(monto, cedula);
                    case "ahorro":
                        monto = rs.getFloat("monto");
                        return new Ahorro(monto, cedula);
                    case "corriente":
                        monto = rs.getFloat("monto");
                        boolean tarjetaCredito = rs.getBoolean("tarjetaCredito");
                        float montoTarjeta = rs.getFloat("montoTarjetaCredito");
                        return new Corriente(monto, cedula, tarjetaCredito, montoTarjeta);
                }
            }
        }
        return null;
    }

    public static ArrayList<Transaccion> obtenerTransacciones(String CC) {
        ArrayList<Transaccion> transacciones = new ArrayList<>();

        String sql = "SELECT * FROM transacciones WHERE CC = ?";

        try (Connection conexion = Banco.getConnection();
             PreparedStatement preparedStatement = conexion.prepareStatement(sql)) {

            preparedStatement.setString(1, CC);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    transacciones.add(new Transaccion(
                            Banco.buscarCliente(resultSet.getString("CC")),
                            Banco.buscarCliente(resultSet.getString("CCclienteFinal")),
                            TipoCuenta.valueOf(resultSet.getString("tipoCuenta").toUpperCase()),
                            resultSet.getTimestamp("fechaTransaccion").toLocalDateTime(),
                            resultSet.getFloat("monto")
                    ));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return transacciones;
    }
    public String actualizarImagenCliente(String cc, File archivoImagen) throws ConexionFallidaConBaseDeDatosException {
        String sql = "UPDATE clientes SET fotoPerfil = ? WHERE CC = ?";

        try (Connection conexion = getConnection();
             FileInputStream fis = new FileInputStream(archivoImagen);
             PreparedStatement stmt = conexion.prepareStatement(sql)) {

            stmt.setBinaryStream(1, fis, (int) archivoImagen.length());
            stmt.setString(2, cc);

            int filas = stmt.executeUpdate();
            if (filas > 0) {
                return "Imagen de perfil actualizada correctamente.";
            } else {
                return "No se encontró el cliente con esa cédula.";
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw new ConexionFallidaConBaseDeDatosException("No se pudo conectar a la base de datos.");
        }
    }

}
