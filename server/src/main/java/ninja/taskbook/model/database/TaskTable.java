package ninja.taskbook.model.database;

import java.sql.ResultSet;
import java.sql.SQLException;

import ninja.taskbook.model.entity.TaskEntity;

//----------------------------------------------------------------------------------------------------
public class TaskTable extends TableBase<TaskEntity> {

    //----------------------------------------------------------------------------------------------------
    public static final String TABLE_NAME = "task";
    public static final String[] COLUMNS = {
            "(task_id INTEGER PRIMARY KEY AUTOINCREMENT,",
            "task_group_id INTEGER DEFAULT 0,",
            "task_author VARCHAR(255) DEFAULT \"\",",
            "task_name VARCHAR(255) DEFAULT \"\",",
            "task_content VARCHAR(1023) DEFAULT \"\",",
            "task_beginning VARCHAR(255) DEFAULT \"\",",
            "task_deadline VARCHAR(255) DEFAULT \"\",",
            "task_progress FLOAT DEFAULT 0);",
    };

    //----------------------------------------------------------------------------------------------------
    public TaskTable() {

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
    public TaskEntity resultSetToEntity(ResultSet rs) {
        try {
            TaskEntity entity = new TaskEntity();
            entity.taskId = rs.getInt("task_id");
            entity.taskGroupId = rs.getInt("task_group_id");
            entity.taskAuthor = rs.getString("task_author");
            entity.taskName = rs.getString("task_name");
            entity.taskContent = rs.getString("task_content");
            entity.taskBeginning = rs.getString("task_beginning");
            entity.taskDeadline = rs.getString("task_deadline");
            entity.taskProgress = rs.getFloat("task_progress");

            return entity;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    //----------------------------------------------------------------------------------------------------
    @Override
    public String entityToString(TaskEntity entity) {
        String result = "";
        result += (entity.taskId <= 0? "null" : "'" + entity.taskId + "'") + ",";
        result += "'" + entity.taskGroupId + "',";
        result += "'" + entity.taskAuthor + "',";
        result += "'" + entity.taskName + "',";
        result += "'" + entity.taskContent + "',";
        result += "'" + entity.taskBeginning + "',";
        result += "'" + entity.taskDeadline + "',";
        result += "'" + entity.taskProgress + "'";

        return result;
    }
}
