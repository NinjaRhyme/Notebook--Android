package ninja.taskbook.model.database;

import android.net.Uri;
import android.util.Log;

//----------------------------------------------------------------------------------------------------
public class DatabaseInfo {

    //----------------------------------------------------------------------------------------------------
    public static final String AUTHORITY = "database";
    public static final Uri BASE_URI = new Uri.Builder().scheme("content").authority(AUTHORITY).build();

    // Databases
    //----------------------------------------------------------------------------------------------------
    public class Databases {
        public static final String TASK_BOOK = "task_book.sqlite";
    }

    // Tables
    //----------------------------------------------------------------------------------------------------
    public static class UserTable {
        public static final String TABLE_NAME = "user";
        public static final Uri CONTENT_URI = getTableContentUri(Databases.TASK_BOOK, TABLE_NAME);
        public static final String[] COLUMNS = {
                "(user_id INTEGER PRIMARY KEY AUTOINCREMENT,",
                "user_name VARCHAR(255) DEFAULT \"\",",
                "user_nickname VARCHAR(255) DEFAULT \"\");",
        };
    }

    //----------------------------------------------------------------------------------------------------
    public static Uri getTableContentUri(final String database, final String table) {
        Log.d("Database", "path:" + BASE_URI.buildUpon().appendPath(database).appendPath(table).build().toString());
        return BASE_URI.buildUpon().appendPath(database).appendPath(table).build();
    }
}
