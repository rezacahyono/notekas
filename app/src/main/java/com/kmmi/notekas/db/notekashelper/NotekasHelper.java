package com.kmmi.notekas.db.notekashelper;

import static android.provider.BaseColumns._ID;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.kmmi.notekas.db.DatabaseContract;
import com.kmmi.notekas.db.DatabaseHelper;

public class NotekasHelper {

    private final static String DATABASE_TABLE = DatabaseContract.TABLE_NOTEKAS;

    private static DatabaseHelper databaseHelper;
    private static SQLiteDatabase database;

    private NotekasHelper(Context context) {
        databaseHelper = new DatabaseHelper(context);
    }

    private static NotekasHelper INSTANCE;
    public static NotekasHelper getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (SQLiteOpenHelper.class) {
                if (INSTANCE == null) {
                    INSTANCE = new NotekasHelper(context);
                }
            }
        }
        return INSTANCE;
    }

    public void close() {
        databaseHelper.close();
        if (database.isOpen()) database.close();
    }

    public void open() {
        database = databaseHelper.getWritableDatabase();
    }

    public Cursor queryByIdUser(String idUser) {
        return database.query(DATABASE_TABLE, null
                , DatabaseContract.NoteKasColumns.ID_USER + " = ?"
                , new String[]{idUser}
                , null
                , null
                , null
                , null);
    }

    public Cursor getAmount(String idUser) {
        database = databaseHelper.getWritableDatabase();
        return database.rawQuery("SELECT SUM(amount) AS total, " +
                "(SELECT SUM(amount) FROM notekas WHERE type_input = 'Pemasukan') AS masuk, " +
                "(SELECT SUM(amount) FROM notekas WHERE type_input = 'Pengeluaran') AS keluar " +
                "FROM notekas WHERE "+DatabaseContract.NoteKasColumns.ID_USER+" = ? ",new String[]{idUser});
    }

    public Cursor getIncome(String idUser) {
        database = databaseHelper.getWritableDatabase();
        return database.query(DATABASE_TABLE,
                null,
                DatabaseContract.NoteKasColumns.TYPE_INPUT +" = 'Pemasukan' AND " +
                        DatabaseContract.NoteKasColumns.ID_USER+ " = ?" ,
                new String[] {idUser},
                null,
                null,
                null,
                null);
    }

    public Cursor getExpenditure(String idUser) {
        database = databaseHelper.getWritableDatabase();
        return database.query(DATABASE_TABLE,
                null,
                DatabaseContract.NoteKasColumns.TYPE_INPUT +" = 'Pengeluaran' AND " +
                        DatabaseContract.NoteKasColumns.ID_USER+ " = ?" ,
                new String[] {idUser},
                null,
                null,
                null,
                null);
    }

    public Long insert(ContentValues values) {
        return database.insert(DATABASE_TABLE, null, values);
    }

    public int update(String id, ContentValues values) {
        return database.update(DATABASE_TABLE, values, _ID + " = ?", new String[]{id});
    }

    public int deleteById(String id) {
        return database.delete(DATABASE_TABLE, _ID + " = ?", new String[]{id});
    }
}
