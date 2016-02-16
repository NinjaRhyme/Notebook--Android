package ninja.taskbook.model.database;

import android.net.Uri;


public class DatabaseInfo {

    //----------------------------------------------------------------------------------------------------
    public static final String FILE_ROOT = "database";
    public static final String AUTHORITY = "provider";
    public static final Uri BASE_URI = new Uri.Builder().scheme("content").authority(AUTHORITY).build();

    // Databases
    //----------------------------------------------------------------------------------------------------
    public class Databases {
        public static final String USER = "user.sqlite";
    }

    // Tables
    //----------------------------------------------------------------------------------------------------
    public static class UserTable {
        public static final String TABLE_NAME = "User";
        public static final Uri CONTENT_URI = getTableContentUri(Databases.USER, TABLE_NAME);
        public static final String[] COLUMNS = {
                "(user_id integer,",
                "user_password varchar default \"\")",
        };
    }

    //----------------------------------------------------------------------------------------------------
    public static Uri getTableContentUri(final String database, final String table) {
        return BASE_URI.buildUpon().appendPath(database).appendPath(table).build();
    }
}
