package com.example.vbfc_bry07.calls.Controller;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DbHelper extends SQLiteOpenHelper {

    static String TBL_PREFERENCES = "Preferences",
            PREFERENCE_ID = "id",
            PREFERENCE_KEY = "key",
            PREFERENCE_VALUE = "value",
            PREFERENCE_TYPE = "type";

    public DbHelper(Context context) {
        super(context, "ECE_calls", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_PREFERENCES = String.format("CREATE TABLE %s (%s INTEGER PRIMARY KEY AUTOINCREMENT, %s TEXT UNIQUE, %s TEXT, %s INTEGER)",
                TBL_PREFERENCES, PREFERENCE_ID, PREFERENCE_KEY, PREFERENCE_VALUE, PREFERENCE_TYPE);

        db.execSQL(CREATE_PREFERENCES);
        db.execSQL(CallsController.CREATE_PLANS);

        insertToTablePreferences(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String sql = "DROP TABLE IF EXISTS ECE_calls";
        db.execSQL(sql);
    }

    //INSERT METHODS
    public String insertToTablePreferences(SQLiteDatabase db) {
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

    //CHECK METHODS
    public long checkUser(String username, String password) {
        SQLiteDatabase db = getWritableDatabase();
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
