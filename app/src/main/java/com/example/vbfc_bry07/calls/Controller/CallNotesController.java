package com.example.vbfc_bry07.calls.Controller;

import android.content.Context;

/**
 * Created by vbfc_bry07 on 2/29/2016.
 */
public class CallNotesController extends DbHelper {

    DbHelper dbHelper;

    static String TBL_CallNotes = "CallNotes",
            CallNotes_ID = "call_notes_id",
            CALL_ID_FK = "call_id_fk",
            CALL_NOTE_TYPE_ID_FK = "call_note_type_id_fk",
            NOTES = "notes",
            DATETIME = "datetime";

    public static final String CREATE_CallNotes = String.format("CREATE TABLE %s (%s INTEGER PRIMARY KEY AUTOINCREMENT, %s INTEGER, %s INTEGER, %s INTEGER, %s TEXT, %s TEXT, %s TEXT, %s TEXT, %s TEXT)",
            TBL_CallNotes, AI_ID, CallNotes_ID, CALL_ID_FK, CALL_NOTE_TYPE_ID_FK, NOTES, DATETIME, CREATED_AT, UPDATED_AT, DELETED_AT);

    public CallNotesController(Context context) {
        super(context);
        dbHelper = new DbHelper(context);
    }
}
