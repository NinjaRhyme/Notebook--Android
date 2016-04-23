package ninja.taskbook.model.database;

import java.sql.ResultSet;
import java.sql.SQLException;

import ninja.taskbook.model.entity.GroupEntity;

public class GroupTable extends TableBase<GroupEntity> {

    //----------------------------------------------------------------------------------------------------
    public static final String TABLE_NAME = "'group'";
    public static final String[] COLUMNS = {
            "(group_id INTEGER PRIMARY KEY AUTOINCREMENT,",
            "group_name VARCHAR(255) DEFAULT \"\");",
    };

    //----------------------------------------------------------------------------------------------------
    public GroupTable() {

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
    public GroupEntity resultSetToEntity(ResultSet rs) {
        try {
            GroupEntity entity = new GroupEntity();
            entity.groupId = rs.getInt("group_id");
            entity.groupName = rs.getString("group_name");

            return entity;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    //----------------------------------------------------------------------------------------------------
    @Override
    public String entityToString(GroupEntity entity) {
        String result = "";
        result += (entity.groupId <= 0? "null" : "'" + entity.groupId + "'") + ",";
        result += "'" + entity.groupName + "'";

        return result;
    }

}
