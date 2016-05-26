package com.ece.vbfc_bry07.calls.controller;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class PreferencesController extends DbHelper {
    DbHelper dbhelper;

    public PreferencesController(Context context) {
        super(context);
        dbhelper = new DbHelper(context);
    }

    // Validate user upon login
    public long checkUser(String username, String password) {
        SQLiteDatabase db = dbhelper.getReadableDatabase();
        String sql0 = "SELECT * FROM Preferences WHERE (key = 'PASSWORD' AND value = '" + password + "') OR (key = 'USERNAME' AND value = '"+username+"')";
        Cursor cur = db.rawQuery(sql0, null);
        long count = cur.getCount();

        db.close();
        cur.close();
        return count;
    }
}
