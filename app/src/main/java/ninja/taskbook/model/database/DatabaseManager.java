package ninja.taskbook.model.database;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;

import ninja.taskbook.model.entity.UserEntity;

//----------------------------------------------------------------------------------------------------
public class DatabaseManager {

    //----------------------------------------------------------------------------------------------------
    private static ContentResolver sResolver;

    //----------------------------------------------------------------------------------------------------
    public static void init(ContentResolver resolver) {
        sResolver = resolver;
    }

    //----------------------------------------------------------------------------------------------------
    public static void setUserId(int userId) {
        if (sResolver != null) {
            ContentValues values = new ContentValues();
            values.put("user_id", userId);
            sResolver.insert(DatabaseInfo.UserTable.CONTENT_URI, values);
        }
    }

    public static UserEntity getUserEntity() {
        UserEntity entity = null;
        if (sResolver != null) {
            Cursor cursor = sResolver.query(DatabaseInfo.UserTable.CONTENT_URI, null, null, null, null);
            if (cursor != null && cursor.moveToNext()) {
                int userId = cursor.getInt(0);
                entity = new UserEntity();
                entity.userId = userId;
                cursor.close();
            }
        }
        return entity;
    }

    public static void clear() {
        if (sResolver != null) {
            sResolver.delete(DatabaseInfo.UserTable.CONTENT_URI, null, null);
        }
    }
}
