package com.ece.vbfc_bry07.calls.Controller;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

public class ReasonsController extends DbHelper {

    DbHelper dbHelper;

    static String Reasons_ID = "reasons_id",
            NAME = "name";

    public ReasonsController(Context context) {
        super(context);
        dbHelper = new DbHelper(context);
    }

    ///////////////////////////////GET METHODS
    public ArrayList<String> getEnabledReasons() {
        String sql = "SELECT * FROM Reasons WHERE remarks_enabled = 1";
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cur = db.rawQuery(sql, null);
        ArrayList<String> array = new ArrayList<>();

        while (cur.moveToNext())
            array.add(cur.getString(cur.getColumnIndex(NAME)));

        cur.close();
        db.close();

        return array;
    }

    public int getReasonID(String name) {
        String sql = "SELECT * FROM Reasons WHERE name = '" + name + "'";
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cur = db.rawQuery(sql, null);
        int reason_ID = 0;

        if (cur.moveToNext())
            reason_ID = cur.getInt(cur.getColumnIndex(Reasons_ID));

        cur.close();
        db.close();

        return reason_ID;
    }
}
