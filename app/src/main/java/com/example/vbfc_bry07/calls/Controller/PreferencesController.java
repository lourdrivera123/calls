package com.example.vbfc_bry07.calls.Controller;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class PreferencesController extends DbHelper {

    DbHelper dbHelper;

    static String TBL_PREFERENCES = "Preferences",
            Preferences_ID = "preferences_id",
            PREFERENCE_KEY = "key",
            PREFERENCE_VALUE = "value",
            PREFERENCE_TYPE = "type";

    public static final String CREATE_PREFERENCES = String.format("CREATE TABLE %s (%s INTEGER PRIMARY KEY AUTOINCREMENT, %s INTEGER, %s TEXT UNIQUE, %s TEXT, %s INTEGER)",
            TBL_PREFERENCES, AI_ID, Preferences_ID, PREFERENCE_KEY, PREFERENCE_VALUE, PREFERENCE_TYPE);

    public PreferencesController(Context context) {
        super(context);
        dbHelper = new DbHelper(context);
    }

    public static String insertToTablePreferences(SQLiteDatabase db) {
        ContentValues preferences_value = new ContentValues();
        preferences_value.put(PREFERENCE_KEY, "USERNAME");
        preferences_value.put(PREFERENCE_VALUE, "esel");
        preferences_value.put(PREFERENCE_TYPE, "6");
        db.insert(TBL_PREFERENCES, null, preferences_value);

        preferences_value.put(PREFERENCE_KEY, "PASSWORD");
        preferences_value.put(PREFERENCE_VALUE, "esel");
        preferences_value.put(PREFERENCE_TYPE, "6");
        db.insert(TBL_PREFERENCES, null, preferences_value);

        return null;
    }

    // Validate user upon login
    public long checkUser(String username, String password) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String sql0 = "SELECT * FROM " + TBL_PREFERENCES +
                " WHERE (" + PREFERENCE_KEY + "='PASSWORD' AND " + PREFERENCE_VALUE + "='" + password + "')" +
                " OR (" + PREFERENCE_KEY + "='USERNAME' AND " + PREFERENCE_VALUE + "='" + username + "')";
        Cursor cur = db.rawQuery(sql0, null);
        long count = cur.getCount();

        db.close();
        cur.close();
        return count;
    }

}
