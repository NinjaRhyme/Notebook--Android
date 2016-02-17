package ninja.taskbook.model.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

//----------------------------------------------------------------------------------------------------
public class UserDatabase extends SQLiteOpenHelper {

    //----------------------------------------------------------------------------------------------------
    public UserDatabase(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    //----------------------------------------------------------------------------------------------------
    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            StringBuilder builder = new StringBuilder("CREATE TABLE IF NOT EXISTS ");
            builder.append(DatabaseInfo.UserTable.TABLE_NAME);
            for (String column : DatabaseInfo.UserTable.COLUMNS)
            {
                builder.append(column);
            }

            db.execSQL(builder.toString());
        } catch (Exception e) {
            //
        }
    }

    //----------------------------------------------------------------------------------------------------
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    //----------------------------------------------------------------------------------------------------
    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}
