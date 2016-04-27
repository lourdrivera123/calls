package com.ece.vbfc_bry07.calls.Controller;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.HashMap;

public class ReasonsController extends DbHelper {

    DbHelper dbHelper;

    static String TBL_Reasons = "Reasons",
            Reasons_ID = "reasons_id",
            CODE = "code",
            NAME = "name",
            REMARKS_ENABLED = "remarks_enabled";

    public static final String CREATE_Reasons = String.format("CREATE TABLE %s (%s INTEGER PRIMARY KEY AUTOINCREMENT, %s INTEGER, %s TEXT, %s TEXT, %s INTEGER, %s TEXT, %s TEXT, %s TEXT)",
            TBL_Reasons, AI_ID, Reasons_ID, CODE, NAME, REMARKS_ENABLED, CREATED_AT, UPDATED_AT, DELETED_AT);

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
