package ninja.taskbook.model.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class UserDatabase extends SQLiteOpenHelper {

    //----------------------------------------------------------------------------------------------------
    public UserDatabase(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    //----------------------------------------------------------------------------------------------------
    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            db.execSQL("create table if not exists User (" +
                    ")");
        } catch (Exception e) {
            //
        }

        //onUpgrade(db,0,0);
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
