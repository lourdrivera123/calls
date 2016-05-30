package com.ece.vbfc_bry07.calls.controller;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.ece.vbfc_bry07.calls.Helpers;

import java.util.ArrayList;
import java.util.HashMap;

public class BroadcastsController extends DbHelper {
    DbHelper dbHelper;
    Helpers helpers;

    public BroadcastsController(Context context) {
        super(context);
        helpers = new Helpers();
        dbHelper = new DbHelper(context);
    }

    ////////////////////////GET METHODS
    public ArrayList<HashMap<String, String>> getBroadcastMessages() {
        String sql = "SELECT * FROM Broadcasts WHERE end_date >= '" + helpers.getCurrentDate("") + "'";
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cur = db.rawQuery(sql, null);
        ArrayList<HashMap<String, String>> array = new ArrayList<>();

        while (cur.moveToNext()) {
            HashMap<String, String> map = new HashMap<>();
            map.put("date", helpers.convertToAlphabetDate(cur.getString(cur.getColumnIndex("created_at")), ""));
            map.put("message", "* " + cur.getString(cur.getColumnIndex("message")));
            array.add(map);
        }

        cur.close();
        db.close();

        return array;
    }
}
