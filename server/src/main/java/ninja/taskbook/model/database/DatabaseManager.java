package ninja.taskbook.model.database;

import java.sql.*;

//----------------------------------------------------------------------------------------------------
public class DatabaseManager {

    //----------------------------------------------------------------------------------------------------
    public DatabaseManager() {

    }

    //----------------------------------------------------------------------------------------------------
    public Connection getConnection() {
        try {
            Class.forName("org.sqlite.JDBC");
            Connection connection = DriverManager.getConnection("jdbc:sqlite:data.db");
            return connection;
        }
        catch(Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
