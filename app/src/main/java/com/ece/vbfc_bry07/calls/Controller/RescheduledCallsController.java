package com.ece.vbfc_bry07.calls.controller;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.ece.vbfc_bry07.calls.Helpers;

import java.util.HashMap;

public class RescheduledCallsController extends DbHelper {
    Helpers helpers;
    DbHelper dbHelper;

    static String TBL_RescheduledCalls = "RescheduledCalls",
            RescheduledCalls_ID = "rescheduledcalls_id",
            CALL_ID_FK = "call_id",
            CYCLE_DAY = "cycle_day",
            REASON_ID_FK = "reason_id",
            REMARKS = "remarks",
            SENT = "sent";

    public static final String CREATE_RescheduledCalls = String.format("CREATE TABLE %s (%s INTEGER PRIMARY KEY AUTOINCREMENT, %s INTEGER, %s INTEGER, %s INTEGER, %s INTEGER, %s TEXT, %s INTEGER, %s TEXT, %s TEXT, %s TEXT)",
            TBL_RescheduledCalls, AI_ID, RescheduledCalls_ID, CALL_ID_FK, CYCLE_DAY, REASON_ID_FK, REMARKS, SENT, CREATED_AT, UPDATED_AT, DELETED_AT);

    public RescheduledCallsController(Context context) {
        super(context);
        dbHelper = new DbHelper(context);
        helpers = new Helpers();
    }

    public boolean insertRescheduledCalls(HashMap<String, String> map) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues val = new ContentValues();
        val.put(CALL_ID_FK, map.get("call_id"));
        val.put(CYCLE_DAY, map.get("reschedule_date"));
        val.put(REASON_ID_FK, map.get("reason_id"));
        val.put(REMARKS, map.get("remarks"));
        val.put(SENT, 0);
        val.put(CREATED_AT, helpers.getCurrentDate("timestamp"));

        long id = db.insert(TBL_RescheduledCalls, null, val);

        db.close();

        return id > 0;
    }
}
