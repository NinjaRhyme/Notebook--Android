package ninja.taskbook.model.database;

import java.sql.*;

//----------------------------------------------------------------------------------------------------
public class DatabaseManager {

    //----------------------------------------------------------------------------------------------------
    public static final String NAME = "jdbc:sqlite:data.sqlite";

    //----------------------------------------------------------------------------------------------------
    Connection mConnection;

    //----------------------------------------------------------------------------------------------------
    public DatabaseManager() {

    }

    //----------------------------------------------------------------------------------------------------
    protected void finalize() {
        try {
            super.finalize();
            if (mConnection != null) {
                mConnection.close();
            }
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }

    //----------------------------------------------------------------------------------------------------
    public Connection getConnection() {
        try {
            if (mConnection == null) {
                Class.forName("org.sqlite.JDBC");
                mConnection = DriverManager.getConnection(NAME);
            }
            return mConnection;
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
