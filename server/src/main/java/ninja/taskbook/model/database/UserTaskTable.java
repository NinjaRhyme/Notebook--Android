package ninja.taskbook.model.database;

import java.sql.ResultSet;
import java.sql.SQLException;

import ninja.taskbook.model.entity.TaskEntity;
import ninja.taskbook.model.entity.UserTaskRelation;
import ninja.taskbook.util.pair.Pair;

//----------------------------------------------------------------------------------------------------
public class UserTaskTable extends TableBase<UserTaskRelation> {

    //----------------------------------------------------------------------------------------------------
    public static final String TABLE_NAME = "user_task";
    public static final String[] COLUMNS = {
            "(user_task_id INTEGER PRIMARY KEY AUTOINCREMENT,",
            "user_id INTEGER DEFAULT 0,",
            "task_id INTEGER DEFAULT 0,",
            "user_role INTEGER DEFAULT 0);",
    };

    //----------------------------------------------------------------------------------------------------
    public UserTaskTable() {

    }

    //----------------------------------------------------------------------------------------------------
    @Override
    public String getTableName() {
        return TABLE_NAME;
    }

    //----------------------------------------------------------------------------------------------------
    @Override
    public String getRelationTableName() {
        return TABLE_NAME + " inner join " + TaskTable.TABLE_NAME;
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
    public UserTaskRelation resultSetToEntity(ResultSet rs) {
        try {
            UserTaskRelation entity = new UserTaskRelation();
            entity.userTaskId = rs.getInt("user_task_id");
            entity.userId = rs.getInt("user_id");
            entity.taskId = rs.getInt("task_id");
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
            UserTaskRelation relation = new UserTaskRelation();
            relation.userTaskId = rs.getInt("user_task_id");
            relation.userId = rs.getInt("user_id");
            relation.taskId = rs.getInt("task_id");
            relation.userRole = rs.getInt("user_role");
            TaskEntity entity = (new TaskTable()).resultSetToEntity(rs);

            return new Pair<>(relation, entity);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    //----------------------------------------------------------------------------------------------------
    @Override
    public String entityToString(UserTaskRelation entity) {
        String result = "";
        result += (entity.userTaskId <= 0 ? "null" : "'" + entity.userTaskId + "'") + ",";
        result += "'" + entity.userId + "',";
        result += "'" + entity.taskId + "',";
        result += "'" + entity.userRole + "'";

        return result;
    }
}
