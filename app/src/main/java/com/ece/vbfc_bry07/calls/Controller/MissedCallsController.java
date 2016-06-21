package com.ece.vbfc_bry07.calls.controller;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.ece.vbfc_bry07.calls.Helpers;

import java.util.ArrayList;
import java.util.HashMap;

public class MissedCallsController extends DbHelper {
    Helpers helpers;
    CallsController cc;
    DbHelper dbHelper;

    public MissedCallsController(Context context) {
        super(context);
        helpers = new Helpers();
        cc = new CallsController(context);
        dbHelper = new DbHelper(context);
    }

    ///////////////////INSERT METHODS
    public boolean insertMissedCalls(ArrayList<HashMap<String, String>> unprocessed_calls) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        long id = 0;

        for (int x = 0; x < unprocessed_calls.size(); x++) {
            HashMap<String, String> map = unprocessed_calls.get(x);
            ContentValues val = new ContentValues();
            val.put("plan_details_id", map.get("plan_details_id"));
            val.put("reason_id", map.get("reason"));
            val.put("remarks", map.get("remarks"));
            val.put("created_at", helpers.getCurrentDate("timestamp"));

            id = db.insert("MissedCalls", null, val);
        }

        db.close();

        return id > 0;
    }
}
