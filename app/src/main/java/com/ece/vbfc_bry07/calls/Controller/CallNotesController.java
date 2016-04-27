package com.ece.vbfc_bry07.calls.Controller;

import android.content.ContentValues;
import android.content.Context;
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
}
