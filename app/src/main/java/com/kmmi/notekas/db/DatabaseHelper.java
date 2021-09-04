package com.kmmi.notekas.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    protected final static String DATABASE_NAME = "notedb";
    protected final static int DATABASE_VERSION = 1;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    private final static String SQL_CREATE_TABLE_USER = String.format("CREATE TABLE %s"
            + " (%s INTEGER PRIMARY KEY AUTOINCREMENT," +
            " %s TEXT NOT NULL," +
            " %s TEXT NOT NULL," +
            " %s TEXT NOT NULL)",
            DatabaseContract.TABLE_USER,
            DatabaseContract.UserColumns._ID,
            DatabaseContract.UserColumns.USERNAME,
            DatabaseContract.UserColumns.EMAIL,
            DatabaseContract.UserColumns.PASSWORD);

    private final static String SQL_CREATE_TABLE_NOTEKAS = String.format("CREATE TABLE %s" +
            " (%s INTEGER PRIMARY KEY AUTOINCREMENT," +
            " %s INTEGER NOT NULL," +
            " %s TEXT NOT NULL," +
            " %s TEXT NOT NULL," +
            " %s TEXT NOT NULL," +
            " %s DOUBLE NOT NULL," +
            " %s TEXT NOT NULL," +
            " FOREIGN KEY(%s) REFERENCES %s(%s));",
            DatabaseContract.TABLE_NOTEKAS,
            DatabaseContract.NoteKasColumns._ID,
            DatabaseContract.NoteKasColumns.ID_USER,
            DatabaseContract.NoteKasColumns.TYPE_INPUT,
            DatabaseContract.NoteKasColumns.TITLE,
            DatabaseContract.NoteKasColumns.DESCRIPTION,
            DatabaseContract.NoteKasColumns.AMOUNT,
            DatabaseContract.NoteKasColumns.DATE,
            DatabaseContract.NoteKasColumns.ID_USER,
            DatabaseContract.TABLE_USER,
            DatabaseContract.UserColumns._ID);

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(SQL_CREATE_TABLE_USER);
        sqLiteDatabase.execSQL(SQL_CREATE_TABLE_NOTEKAS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + DatabaseContract.TABLE_USER);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + DatabaseContract.TABLE_NOTEKAS);
        onCreate(sqLiteDatabase);
    }
}
