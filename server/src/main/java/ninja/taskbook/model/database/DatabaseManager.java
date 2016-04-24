package ninja.taskbook.model.database;

import java.sql.*;

//----------------------------------------------------------------------------------------------------
public class DatabaseManager {

    //----------------------------------------------------------------------------------------------------
    public static final String NAME = "jdbc:sqlite:data.sqlite";

    //----------------------------------------------------------------------------------------------------
    public DatabaseManager() {

    }

    //----------------------------------------------------------------------------------------------------
    protected void finalize() {

    }

    //----------------------------------------------------------------------------------------------------
    public Connection getConnection() {
        try {
            Class.forName("org.sqlite.JDBC");
            return DriverManager.getConnection(NAME);
        } catch(Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    //----------------------------------------------------------------------------------------------------
    public synchronized TableBase getTable(Class<?> className) {
        try {
            TableBase table;
            table = (TableBase)className.newInstance();
            table.setConnection(getConnection());
            table.create();
            return table;
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    //----------------------------------------------------------------------------------------------------
    public synchronized void dropTable(Class<?> className) {
        try {
            TableBase table;
            table = (TableBase)className.newInstance();
            table.setConnection(getConnection());
            table.drop();
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
