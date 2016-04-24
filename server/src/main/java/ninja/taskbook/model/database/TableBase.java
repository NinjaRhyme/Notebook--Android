package ninja.taskbook.model.database;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

// Todo: rollback
//----------------------------------------------------------------------------------------------------
public abstract class TableBase<T> {

    //----------------------------------------------------------------------------------------------------
    Connection mConnection;

    //----------------------------------------------------------------------------------------------------
    public TableBase() {

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
    public void setConnection(Connection connection) {
        mConnection = connection;
    }

    //----------------------------------------------------------------------------------------------------
    public abstract String getTableName();
    public abstract String getCreationSQL();
    public abstract T resultSetToEntity(ResultSet rs);
    public abstract String entityToString(T entity);

    //----------------------------------------------------------------------------------------------------
    public boolean executeUpdate(String sql) {
        try {
            Statement stat = mConnection.createStatement();
            stat.executeUpdate(sql);
            stat.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
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
    public int queryLastId() {
        try{
            String sql = "select last_insert_rowid() as last from " + getTableName() + ";";
            Statement stat = mConnection.createStatement();
            ResultSet resultSet = stat.executeQuery(sql);
            int id = resultSet.getInt(1);
            System.out.println(id);
            stat.close();
            return id;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    //----------------------------------------------------------------------------------------------------
    public void create() {
        try {
            Statement stat = mConnection.createStatement();
            stat.executeUpdate(getCreationSQL());
            stat.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //----------------------------------------------------------------------------------------------------
    public void drop() {
        String sql = "drop table if exists " + getTableName()  + ";";
        executeUpdate(sql);
    }

    //----------------------------------------------------------------------------------------------------
    public int insert(T entity) {
        String sql = "insert into " + getTableName()  + " values(" + entityToString(entity) + ");";
        if (executeUpdate(sql)) {
            return queryLastId();
        }
        return 0;
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
