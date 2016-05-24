package com.ece.vbfc_bry07.calls.Controller;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.ece.vbfc_bry07.calls.Helpers;

import java.util.ArrayList;

public class BroadcastsController extends DbHelper {
    DbHelper dbHelper;
    Helpers helpers;

    public BroadcastsController(Context context) {
        super(context);
        helpers = new Helpers();
        dbHelper = new DbHelper(context);
    }

    ////////////////////////GET METHODS
    public ArrayList<String> getBroadcastMessages() {
        String sql = "SELECT * FROM Broadcasts WHERE end_date >= '" + helpers.getCurrentDate("") + "'";
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cur = db.rawQuery(sql, null);
        ArrayList<String> array = new ArrayList<>();

        while (cur.moveToNext())
            array.add("* " + cur.getString(cur.getColumnIndex("message")));

        cur.close();
        db.close();

        return array;
    }
}
