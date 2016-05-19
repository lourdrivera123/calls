package com.ece.vbfc_bry07.calls.Controller;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.ece.vbfc_bry07.calls.Helpers;

import java.util.ArrayList;

public class BroadcastsController extends DbHelper {
    DbHelper dbHelper;
    Helpers helpers;

    static String TBL_Broadcasts = "Broadcasts",
            Broadcasts_ID = "broadcasts_id",
            MESSAGE = "message",
            STATUS_ID_FK = "status_id",
            END_DATE = "end_date",
            AUTHOR = "author";

    public static final String CREATE_Broadcasts = String.format("CREATE TABLE %s (%s INTEGER PRIMARY KEY AUTOINCREMENT, %s INTEGER, %s TEXT, %s INTEGER, %s TEXT, %s TEXT, %s TEXT, %s TEXT, %s TEXT)",
            TBL_Broadcasts, AI_ID, Broadcasts_ID, MESSAGE, STATUS_ID_FK, END_DATE, AUTHOR, CREATED_AT, UPDATED_AT, DELETED_AT);

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
