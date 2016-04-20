package ninja.taskbook.model.database;

import java.sql.ResultSet;
import java.sql.SQLException;

import ninja.taskbook.model.entity.UserEntity;

//----------------------------------------------------------------------------------------------------
public class UserTable extends TableBase<UserEntity> {

    //----------------------------------------------------------------------------------------------------
    public static final String TABLE_NAME = "user";
    public static final String[] COLUMNS = {
            "(user_id INTEGER PRIMARY KEY AUTOINCREMENT,",
            "user_name VARCHAR(255) DEFAULT \"\",",
            "user_password VARCHAR(255) DEFAULT \"\",",
            "user_nickname VARCHAR(255) DEFAULT \"\");",
    };

    //----------------------------------------------------------------------------------------------------
    public UserTable() {

    }

    //----------------------------------------------------------------------------------------------------
    @Override
    public String getTableName() {
        return TABLE_NAME;
    }

    //----------------------------------------------------------------------------------------------------
    @Override
    public String getCreationSQL() {
        StringBuilder builder = new StringBuilder("CREATE TABLE IF NOT EXISTS ");
        builder.append(TABLE_NAME);
        for (String column : COLUMNS) {
            builder.append(column);
        }

        return builder.toString();
    }

    //----------------------------------------------------------------------------------------------------
    @Override
    public UserEntity resultSetToEntity(ResultSet rs) {
        try {
            UserEntity entity = new UserEntity();
            entity.userId = rs.getInt("user_id");
            entity.userName = rs.getString("user_name");
            entity.userPassword = rs.getString("user_password");
            entity.userNickname = rs.getString("user_nickname");

            return entity;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    //----------------------------------------------------------------------------------------------------
    @Override
    public String entityToString(UserEntity entity) {
        String result = "";
        result += "'" + entity.userId + "',";
        result += "'" + entity.userName + "',";
        result += "'" + entity.userPassword + "',";
        result += "'" + entity.userNickname + "'";

        return result;
    }
}
