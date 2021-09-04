package com.kmmi.notekas.db.userhelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.kmmi.notekas.db.DatabaseContract;
import com.kmmi.notekas.db.DatabaseHelper;

public class UserHelper {

    private final static String DATABASE_TABLE = DatabaseContract.TABLE_USER;

    private static DatabaseHelper databaseHelper;

    private static SQLiteDatabase database;

    private UserHelper(Context context) {
        databaseHelper = new DatabaseHelper(context);
    }

    private static UserHelper INSTANCE;

    public static UserHelper getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (SQLiteOpenHelper.class) {
                if (INSTANCE == null) {
                    INSTANCE = new UserHelper(context);
                }
            }
        }
        return INSTANCE;
    }

    public void close() {
        databaseHelper.close();
        if (database.isOpen()) database.close();
    }

    public boolean insert(String username, String email, String password) {
        database = databaseHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseContract.UserColumns.USERNAME, username);
        values.put(DatabaseContract.UserColumns.EMAIL, email);
        values.put(DatabaseContract.UserColumns.PASSWORD, password);
        long result = database.insert(DATABASE_TABLE, null, values);
        return result > 0;
    }

    public boolean checkUser(String username) {
        database = databaseHelper.getWritableDatabase();
        Cursor cursor = database.rawQuery("SELECT * FROM "
                + DATABASE_TABLE + " WHERE "
                + DatabaseContract.UserColumns.USERNAME +
                " = ?", new String[]{username});
        return cursor.getCount() > 0;
    }

    public boolean isLogin(String username, String password) {
        database = databaseHelper.getWritableDatabase();
        Cursor cursor = database.rawQuery("SELECT * FROM "
                + DATABASE_TABLE + " WHERE "
                + DatabaseContract.UserColumns.USERNAME + " = ? AND "
                + DatabaseContract.UserColumns.PASSWORD + " = ?", new String[]{username, password});
        return cursor.getCount() > 0;
    }

    public int getIdUser(String username, String password) {
        database = databaseHelper.getWritableDatabase();
        int idUser = 0;
        Cursor cursor = database.rawQuery("SELECT _id FROM "
                + DATABASE_TABLE + " WHERE "
                + DatabaseContract.UserColumns.USERNAME + " = ? AND "
                + DatabaseContract.UserColumns.PASSWORD + " = ?", new String[]{username, password});
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            idUser = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseContract.UserColumns._ID));
        }
        return idUser;
    }
}
