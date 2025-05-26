package Java.Intermedio.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Empresarial extends CuentaBancaria{

    public Empresarial(float monto, String CC) {
        super(monto, CC);
    }
    @Override
    public void depositar(float monto) {
        Connection connection = null;
        PreparedStatement preparedStatement1 = null;
        PreparedStatement preparedStatement2 = null;
        ResultSet resultSet = null;
        try{
            connection = Banco.getConnection();
            String sql = "SELECT montoEmpresa FROM empresarial WHERE CC = "+CC;
            preparedStatement1 = connection.prepareStatement(sql);
            resultSet = preparedStatement1.executeQuery();
            float montoEmpresa;
            if(resultSet.next()) {
                 montoEmpresa = resultSet.getFloat("montoEmpresa") + monto;
            }else{
                return;
            }
            sql = "UPDATE empresarial SET montoEmpresa = " + montoEmpresa+" WHERE CC = "+CC;
            preparedStatement2 = connection.prepareStatement(sql);
            preparedStatement2.executeUpdate();
        }catch(SQLException e){
            e.printStackTrace();
        }finally{
            try{
                if(connection != null){
                    connection.close();
                }
                if(preparedStatement1 != null){
                    preparedStatement1.close();
                }
                if(preparedStatement2 != null){
                    preparedStatement2.close();
                }
                if(resultSet != null) {
                    resultSet.close();
                }
            }catch(SQLException e){
                e.printStackTrace();
            }
        }
    }
    @Override
    public boolean retirar(float monto) {
            Connection connection = null;
            PreparedStatement preparedStatement1 = null;
            PreparedStatement preparedStatement2 = null;
            ResultSet resultSet = null;
            try {
                connection = Banco.getConnection();
                String sql = "SELECT montoEmpresa FROM empresarial WHERE CC = " + CC;
                preparedStatement1 = connection.prepareStatement(sql);
                resultSet = preparedStatement1.executeQuery();
                float montoEmpresa = 0;
                if(resultSet.next()) {
                    montoEmpresa = resultSet.getFloat("montoEmpresa") - monto;
                    if(montoEmpresa < 0){
                        return false;
                    }
                }else{
                    return false;
                }
                sql = "UPDATE empresarial SET montoEmpresa = " + montoEmpresa+" WHERE CC = " + CC;
                preparedStatement2 = connection.prepareStatement(sql);
                preparedStatement2.executeUpdate();

                return true;
            } catch (SQLException e) {
                e.printStackTrace();
            }finally{
                try{
                    if(connection != null){
                        connection.close();
                    }
                    if(preparedStatement1 != null){
                        preparedStatement1.close();
                    }
                    if(preparedStatement2 != null){
                        preparedStatement2.close();
                    }
                    if(resultSet != null){
                        resultSet.close();
                    }
                }catch(SQLException e){
                    e.printStackTrace();
                }
            }

        return true;
    }
}
