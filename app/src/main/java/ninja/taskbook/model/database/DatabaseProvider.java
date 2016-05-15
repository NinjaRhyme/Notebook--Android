package ninja.taskbook.model.database;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.support.annotation.NonNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

import ninja.taskbook.model.entity.UserEntity;

//----------------------------------------------------------------------------------------------------
public class DatabaseProvider extends ContentProvider {

    //----------------------------------------------------------------------------------------------------
    private final Map<String, SQLiteDatabase> mDatabaseMap;
    private final Map<String, ReentrantLock> mDatabaseLocks;

    //----------------------------------------------------------------------------------------------------
    public DatabaseProvider() {
        mDatabaseMap = new HashMap<>();
        mDatabaseLocks = new HashMap<>();
    }

    @Override
    public boolean onCreate() {

        return true;
    }

    //----------------------------------------------------------------------------------------------------
    @Override
    public synchronized String getType(@NonNull Uri uri) {

        return null;
    }

    //----------------------------------------------------------------------------------------------------
    @Override
    public Uri insert(@NonNull Uri uri, ContentValues values) {
        List<String> segments = uri.getPathSegments();
        final String dbName = segments.get(0);
        final String table = segments.get(1);

        try {
            SQLiteDatabase db = getDatabase(dbName);
            ReentrantLock lock = getDatabaseLock(dbName);

            try {
                lock.lock();
                db.insertWithOnConflict(table, null, values, SQLiteDatabase.CONFLICT_REPLACE);
                return uri;
            } finally {
                lock.unlock();
            }
        } catch (Exception e) {
            return null;
        }
    }

    //----------------------------------------------------------------------------------------------------
    @Override
    public int delete(@NonNull Uri uri, String whereClause, String[] whereArgs) {
        List<String> segments = uri.getPathSegments();
        final String dbName = segments.get(0);
        final String table = segments.get(1);

        try {
            SQLiteDatabase db = getDatabase(dbName);
            ReentrantLock lock = getDatabaseLock(dbName);

            try {
                lock.lock();
                return db.delete(table, whereClause, whereArgs);
            } finally {
                lock.unlock();
            }
        } catch (Exception e) {
            return 0;
        }
    }

    //----------------------------------------------------------------------------------------------------
    @Override
    public int update(@NonNull Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        List<String> segments = uri.getPathSegments();
        final String dbName = segments.get(0);
        final String table = segments.get(1);

        try {
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
        List<String> segments = uri.getPathSegments();
        final String dbName = segments.get(0);
        final String table = segments.get(1);

        try {
            SQLiteDatabase db = getDatabase(dbName);
            ReentrantLock lock = getDatabaseLock(dbName);

            try {
                lock.lock();
                return db.query(table, projection, selection, selectionArgs, null, null, sortOrder);
            } finally {
                lock.unlock();
            }
        } catch (Exception e) {
            return null;
        }
    }

    //----------------------------------------------------------------------------------------------------
    private synchronized SQLiteDatabase getDatabase(final String dbName) {
        SQLiteDatabase db;
        db = mDatabaseMap.get(dbName);
        if (db == null) {
            SQLiteOpenHelper helper = new UserTable(getContext(), dbName, null, 3);
            db = helper.getWritableDatabase();

            mDatabaseMap.put(dbName, db);
        }

        return db;
    }

    private synchronized ReentrantLock getDatabaseLock(String dbName) {
        ReentrantLock lock = mDatabaseLocks.get(dbName);
        if (lock == null) {
            lock = new ReentrantLock();
            mDatabaseLocks.put(dbName, lock);
        }

        return lock;
    }

    //----------------------------------------------------------------------------------------------------
}
