package ninja.taskbook.model.database;

import java.sql.ResultSet;
import java.sql.SQLException;

import ninja.taskbook.model.entity.NotificationEntity;

//----------------------------------------------------------------------------------------------------
public class NotificationTable extends TableBase<NotificationEntity> {

    //----------------------------------------------------------------------------------------------------
    public static final String TABLE_NAME = "notification";
    public static final String[] COLUMNS = {
            "(notification_id INTEGER PRIMARY KEY AUTOINCREMENT,",
            "notification_owner_id INTEGER DEFAULT 0,",
            "notification_receiver_id INTEGER DEFAULT 0,",
            "notification_type VARCHAR(255) DEFAULT \"\",",
            "notification_data VARCHAR(255) DEFAULT \"\";",
    };

    //----------------------------------------------------------------------------------------------------
    public NotificationTable() {

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
    public NotificationEntity resultSetToEntity(ResultSet rs) {
        try {
            NotificationEntity entity = new NotificationEntity();
            entity.notificationId = rs.getInt("notification_id");
            entity.notificationOwnerId = rs.getInt("notification_owner_id");
            entity.notificationReceiverId = rs.getInt("notification_receiver_id");
            entity.notificationType = rs.getInt("notification_type");
            entity.notificationData = rs.getString("notification_data");

            return entity;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    //----------------------------------------------------------------------------------------------------
    @Override
    public String entityToString(NotificationEntity entity) {
        String result = "";
        result += (entity.notificationId <= 0? "null" : "'" + entity.notificationId + "'") + ",";
        result += "'" + entity.notificationOwnerId + "',";
        result += "'" + entity.notificationReceiverId + "',";
        result += "'" + entity.notificationType + "',";
        result += "'" + entity.notificationData + "'";

        return result;
    }
}