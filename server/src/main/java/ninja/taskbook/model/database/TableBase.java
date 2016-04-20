package ninja.taskbook.model.database;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

//----------------------------------------------------------------------------------------------------
public abstract class TableBase<T> {

    //----------------------------------------------------------------------------------------------------
    Connection mConnection;

    //----------------------------------------------------------------------------------------------------
    public TableBase() {

    }

    //----------------------------------------------------------------------------------------------------
    public void setConnection(Connection connection) {
        mConnection = connection;
        create(getCreationSQL());
    }

    //----------------------------------------------------------------------------------------------------
    public abstract String getTableName();
    public abstract String getCreationSQL();
    public abstract T resultSetToEntity(ResultSet rs);
    public abstract String entityToString(T entity);

    //----------------------------------------------------------------------------------------------------
    public void executeUpdate(String sql) {
        try {
            Statement stat = mConnection.createStatement();
            stat.executeUpdate(sql);
            stat.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //----------------------------------------------------------------------------------------------------
    public List<T> executeQuery(String sql) {
        try {
            List<T> result = new ArrayList<>();
            Statement stat = mConnection.createStatement();
            ResultSet resultSet = stat.executeQuery(sql);
            while (resultSet.next()) {
                result.add(resultSetToEntity(resultSet));
            }
            stat.close();
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    //----------------------------------------------------------------------------------------------------
    private void create(String sql) {
        try {
            Statement stat = mConnection.createStatement();
            stat.executeUpdate(sql);
            stat.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //----------------------------------------------------------------------------------------------------
    public void insert(T entity) {
        String sql = "insert into " + getTableName()  + " values(" + entityToString(entity) + ");";
        executeUpdate(sql);
    }

    //----------------------------------------------------------------------------------------------------
    public boolean delete(String where) {
        try{
            String sql = "delete from " + getTableName() + " where " + where + ";" ;
            executeUpdate(sql);
            return true ;
        }catch (Exception e) {
            e.printStackTrace();
            return false ;
        }
    }

    //----------------------------------------------------------------------------------------------------
    public boolean update(String set, String where) {
        try{
            String sql = "update " + getTableName() + " set " + set + " where " + where + ";";
            executeUpdate(sql);
            return true ;
        }catch (Exception e) {
            e.printStackTrace();
            return false ;
        }
    }

    //----------------------------------------------------------------------------------------------------
    public T queryEntity(String where) {
        try{
            String sql = "select * from " + getTableName() + " where " + where + ";";
            return executeQuery(sql).get(0);
        }catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    //----------------------------------------------------------------------------------------------------
    public List<T> queryEntities(String where) {
        try{
            String sql = "select * from " + getTableName() + " where " + where + ";";
            return executeQuery(sql);
        }catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
