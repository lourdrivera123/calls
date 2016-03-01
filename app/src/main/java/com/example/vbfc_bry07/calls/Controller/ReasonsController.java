package com.example.vbfc_bry07.calls.Controller;

import android.content.Context;

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
}
