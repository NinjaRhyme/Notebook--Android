package ninja.taskbook.model.database;

import java.sql.ResultSet;
import java.sql.SQLException;

import ninja.taskbook.model.entity.GroupEntity;
import ninja.taskbook.model.entity.UserGroupRelation;
import ninja.taskbook.util.pair.Pair;

public class UserGroupTable extends TableBase<UserGroupRelation> {

    //----------------------------------------------------------------------------------------------------
    public static final String TABLE_NAME = "user_group";
    public static final String[] COLUMNS = {
            "(user_group_id INTEGER PRIMARY KEY AUTOINCREMENT,",
            "user_id INTEGER DEFAULT 0,",
            "group_id INTEGER DEFAULT 0,",
            "user_role INTEGER DEFAULT 0);",
    };

    //----------------------------------------------------------------------------------------------------
    public UserGroupTable() {

    }

    //----------------------------------------------------------------------------------------------------
    @Override
    public String getTableName() {
        return TABLE_NAME;
    }

    //----------------------------------------------------------------------------------------------------
    @Override
    public String getRelationTableName() {
        return TABLE_NAME + " inner join " + GroupTable.TABLE_NAME;
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
    public UserGroupRelation resultSetToEntity(ResultSet rs) {
        try {
            UserGroupRelation entity = new UserGroupRelation();
            entity.userGroupId = rs.getInt("user_group_id");
            entity.userId = rs.getInt("user_id");
            entity.groupId = rs.getInt("group_id");
            entity.userRole = rs.getInt("user_role");

            return entity;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    //----------------------------------------------------------------------------------------------------
    @Override
    public Pair<?, ?> resultSetToRelationEntity(ResultSet rs) {
        try {
            UserGroupRelation relation = new UserGroupRelation();
            relation.userGroupId = rs.getInt("user_group_id");
            relation.userId = rs.getInt("user_id");
            relation.groupId = rs.getInt("group_id");
            relation.userRole = rs.getInt("user_role");
            GroupEntity entity = (new GroupTable()).resultSetToEntity(rs);

            return new Pair<>(relation, entity);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    //----------------------------------------------------------------------------------------------------
    @Override
    public String entityToString(UserGroupRelation entity) {
        String result = "";
        result += (entity.userGroupId <= 0 ? "null" : "'" + entity.userGroupId + "'") + ",";
        result += "'" + entity.userId + "',";
        result += "'" + entity.groupId + "',";
        result += "'" + entity.userRole + "'";

        return result;
    }
}