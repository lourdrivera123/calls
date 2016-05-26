package com.ece.vbfc_bry07.calls.controller;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.HashMap;

public class CallNotesController extends DbHelper {

    DbHelper dbHelper;

    static String TBL_CallNotes = "CallNotes",
            CallNotes_ID = "callnotes_id",
            CALL_ID_FK = "call_id",
            CALL_NOTE_TYPE_ID_FK = "call_note_type_id",
            NOTES = "notes",
            DATETIME = "datetime";

    public static final String CREATE_CallNotes = String.format("CREATE TABLE %s (%s INTEGER PRIMARY KEY AUTOINCREMENT, %s INTEGER, %s INTEGER, %s INTEGER, %s TEXT, %s TEXT, %s TEXT, %s TEXT, %s TEXT)",
            TBL_CallNotes, AI_ID, CallNotes_ID, CALL_ID_FK, CALL_NOTE_TYPE_ID_FK, NOTES, DATETIME, CREATED_AT, UPDATED_AT, DELETED_AT);

    public CallNotesController(Context context) {
        super(context);
        dbHelper = new DbHelper(context);
    }

    //////////////////////////////INSERT METHODS
    public boolean insertCallNotes(ArrayList<HashMap<String, String>> array, long call_id) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        long id = 0;

        for (int x = 0; x < array.size(); x++) {
            ContentValues val = new ContentValues();
            val.put(CALL_ID_FK, call_id);
            val.put(CALL_NOTE_TYPE_ID_FK, 0);
            val.put(NOTES, array.get(x).get("note"));
            val.put(DATETIME, array.get(x).get("date"));

            id = db.insert(TBL_CallNotes, null, val);
        }

        db.close();

        return id > 0;
    }

    //////////////////////GET METHODS
    public ArrayList<HashMap<String, String>> listOfNotesByIDM_id(int IDM_id, String date) {
        String sql = "SELECT cn._id as call_note_id, * FROM CallNotes as cn INNER JOIN Calls as c ON cn.call_id = c._id " +
                "INNER JOIN PlanDetails as pd ON c.plan_details_id = pd.plan_details_id LEFT JOIN RescheduledCalls as rc ON c._id = rc.call_id " +
                "WHERE (c.plan_details_id > 0 OR c.temp_planDetails_id = pd._id) AND (pd.cycle_day = '" + date + "' OR rc.cycle_day = '" + date + "') AND pd.inst_doc_id = " + IDM_id;
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cur = db.rawQuery(sql, null);
        ArrayList<HashMap<String, String>> array = new ArrayList<>();

        while (cur.moveToNext()) {
            HashMap<String, String> map = new HashMap<>();
            map.put("note", cur.getString(cur.getColumnIndex("notes")));
            map.put("date", cur.getString(cur.getColumnIndex("datetime")));
            map.put("call_note_id", cur.getString(cur.getColumnIndex("call_note_id")));
            array.add(map);
        }

        cur.close();
        db.close();

        return array;
    }
}
