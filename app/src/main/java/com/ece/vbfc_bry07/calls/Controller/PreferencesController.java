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
    public int checkUserType(String uname, String pword) {
        SQLiteDatabase db = dbhelper.getReadableDatabase();
        String sql = "SELECT * FROM Preferences WHERE key = 'PASSWORD' OR key = 'USERNAME' OR key = 'USER_TYPE'";
        Cursor cur = db.rawQuery(sql, null);
        int user_type = 0;
        int count = 0;

        while (cur.moveToNext()) {
            if (cur.getString(cur.getColumnIndex("key")).equals("USERNAME") && cur.getString(cur.getColumnIndex("value")).equals(uname))
                count += 1;

            if (cur.getString(cur.getColumnIndex("key")).equals("PASSWORD") && cur.getString(cur.getColumnIndex("value")).equals(pword))
                count += 1;

            if (cur.getString(cur.getColumnIndex("key")).equals("USER_TYPE"))
                user_type = cur.getInt(cur.getColumnIndex("value"));
        }

        cur.close();
        db.close();

        if (count == 2)
            return user_type;
        else
            return 0;
    }
}
