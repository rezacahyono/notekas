package com.kmmi.notekas.db;

import android.provider.BaseColumns;

public class DatabaseContract {
    public final static String TABLE_USER = "user";
    public final static String TABLE_NOTEKAS = "notekas";

    public final static class UserColumns implements BaseColumns {
        public static final String USERNAME = "username";
        public static final String EMAIL = "email";
        public static final String PASSWORD = "password";
    }

    public final static class NoteKasColumns implements BaseColumns {
        public static final String ID_USER = "id_user";
        public static final String TYPE_INPUT = "type_input";
        public static final String TITLE = "title";
        public static final String DESCRIPTION = "description";
        public static final String AMOUNT = "amount";
        public static final String DATE = "date";
    }
}
