package Java.Intermedio.model;


import com.mysql.cj.jdbc.MysqlDataSource;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.sql.SQLException;
import java.util.Properties;

public class Banco {
    public static void main(String[] args) {
        Properties props = new Properties();
        try{
            props.load(Files.newInputStream(Path.of("banco.properties"), StandardOpenOption.READ));
        }catch(IOException e){
            throw new RuntimeException(e);
        }
        var dataSource = new MysqlDataSource();
        dataSource.setServerName(props.getProperty("serverName"));
        dataSource.setPort(Integer.parseInt(props.getProperty("port")));
        dataSource.setDatabaseName(props.getProperty("databaseName"));

        try(var connection = dataSource.getConnection(props.getProperty("user"), System.getenv("MYSQL_PASS"))) {
            System.out.println("Success!");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
