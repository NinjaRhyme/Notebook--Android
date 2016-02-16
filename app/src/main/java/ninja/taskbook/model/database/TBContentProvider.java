package ninja.taskbook.model.database;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.support.annotation.NonNull;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;


public class TBContentProvider extends ContentProvider {

    //----------------------------------------------------------------------------------------------------
    private final Map<String, SQLiteDatabase> m_databaseMap;
    private final Map<String, ReentrantLock> m_databaseLocks;

    // init
    //----------------------------------------------------------------------------------------------------
    public TBContentProvider() {
        m_databaseMap = new HashMap<>();
        m_databaseLocks = new HashMap<>();
    }

    @Override
    public boolean onCreate() {
        initialize();

        return true;
    }

    private void initialize() {

    }

    //----------------------------------------------------------------------------------------------------
    @Override
    public synchronized String getType(@NonNull Uri uri) {

        return null;
    }

    //----------------------------------------------------------------------------------------------------
    @Override
    public Uri insert(@NonNull Uri uri, ContentValues values) {

        return null;
    }

    //----------------------------------------------------------------------------------------------------
    @Override
    public int delete(@NonNull Uri uri, String whereClause, String[] whereArgs) {

        return 0;
    }

    //----------------------------------------------------------------------------------------------------
    @Override
    public int update(@NonNull Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        try {
            List<String> segments = uri.getPathSegments();
            final String dbName = segments.get(0);
            final String table = segments.get(1);
            SQLiteDatabase db = getDatabase(dbName);
            ReentrantLock lock = getDatabaseLock(dbName);

            try {
                lock.lock();
                return db.update(table, values, selection, selectionArgs);
            } finally {
                lock.unlock();
            }
        } catch (Exception e) {
            return 0;
        }
    }

    //----------------------------------------------------------------------------------------------------
    @Override
    public Cursor query(@NonNull Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

        return null;
    }

    //----------------------------------------------------------------------------------------------------
    private synchronized SQLiteDatabase getDatabase(final String dbName) {
        SQLiteDatabase db;
        db = m_databaseMap.get(dbName);
        if (db == null) {
            final String absPath = new File("database", dbName).getAbsolutePath();

            SQLiteOpenHelper helper = new UserDatabase(getContext(), absPath, null, 0);
            db = helper.getWritableDatabase();

            m_databaseMap.put(dbName, db);
        }

        return db;
    }

    private synchronized ReentrantLock getDatabaseLock(String dbName) {
        ReentrantLock lock = m_databaseLocks.get(dbName);
        if (lock == null) {
            lock = new ReentrantLock();
            m_databaseLocks.put(dbName, lock);
        }

        return lock;
    }
}
