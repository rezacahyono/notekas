package com.kmmi.notekas.utils;

import android.database.Cursor;

import com.kmmi.notekas.db.DatabaseContract;
import com.kmmi.notekas.model.Notekas;

import java.util.ArrayList;

public class MappingHelper {
    public static ArrayList<Notekas> mapCursorToArrayList(Cursor notekasCursor) {
        ArrayList<Notekas> notekasList = new ArrayList<>();
        while (notekasCursor.moveToNext()) {
            int id = notekasCursor.getInt(notekasCursor.getColumnIndexOrThrow(DatabaseContract.NoteKasColumns._ID));
            int idUser = notekasCursor.getInt(notekasCursor.getColumnIndexOrThrow(DatabaseContract.NoteKasColumns.ID_USER));
            String typeInput = notekasCursor.getString(notekasCursor.getColumnIndexOrThrow(DatabaseContract.NoteKasColumns.TYPE_INPUT));
            String title = notekasCursor.getString(notekasCursor.getColumnIndexOrThrow(DatabaseContract.NoteKasColumns.TITLE));
            String description = notekasCursor.getString(notekasCursor.getColumnIndexOrThrow(DatabaseContract.NoteKasColumns.DESCRIPTION));
            double amount = notekasCursor.getDouble(notekasCursor.getColumnIndexOrThrow(DatabaseContract.NoteKasColumns.AMOUNT));
            String date = notekasCursor.getString(notekasCursor.getColumnIndexOrThrow(DatabaseContract.NoteKasColumns.DATE));
            notekasList.add(new Notekas(id,idUser,typeInput, title, description, amount,date));
        }
        return notekasList;
    }
}
