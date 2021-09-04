package com.kmmi.notekas.preferences;

import android.content.Context;
import android.content.SharedPreferences;

public class AppPreferences {
    private final static String PREFS_NAME = "prefs_note";

    private final static String IS_LOGIN = "is_login";
    private final static String NEXT_PAGE = "next_page";

    private final static String ID_USER_PREFS = "id_user";

    private final static String TYPE = "type";

    private SharedPreferences preferences;

    public AppPreferences(Context context) {
        this.preferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    public void setIsLogin(boolean value) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(IS_LOGIN, value);
        editor.apply();
    }

    public boolean getIsLogin() {
        return preferences.getBoolean(IS_LOGIN, false);
    }

    public void setNextPage(boolean value) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(NEXT_PAGE, value);
        editor.apply();
    }

    public boolean getNextPage() {
        return preferences.getBoolean(NEXT_PAGE, false);
    }

    public void  setIdUserPrefs(int idUser) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(ID_USER_PREFS, idUser);
        editor.apply();
    }
    public int getIdUserPrefs() {
        return preferences.getInt(ID_USER_PREFS, 0);
    }

    public void setType(boolean value) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(TYPE,value);
        editor.apply();
    }

    public boolean getType() {
        return preferences.getBoolean(TYPE,false);
    }
}
