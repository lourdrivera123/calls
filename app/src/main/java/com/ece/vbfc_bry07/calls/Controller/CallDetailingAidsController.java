package com.ece.vbfc_bry07.calls.controller;

import android.content.Context;

public class CallDetailingAidsController extends DbHelper {

    DbHelper dbHelper;

    static String TBL_CallDetailingAids = "CallDetailingAids",
            CallDetailingAids_ID = "calldetailingads_id",
            CALL_ID_FK = "call_id",
            DETAILING_AID_ID_FK = "detailing_aid_id",
            START_TIME = "start_time",
            END_TIME = "end_time";

    public static final String CREATE_CallDetailingAids = String.format("CREATE TABLE %s (%s INTEGER PRIMARY KEY AUTOINCREMENT, %s INTEGER, %s INTEGER, %s INTEGER, %s TEXT, %s TEXT, %s TEXT, %s TEXT, %s TEXT)",
            TBL_CallDetailingAids, AI_ID, CallDetailingAids_ID, CALL_ID_FK, DETAILING_AID_ID_FK, START_TIME, END_TIME, CREATED_AT, UPDATED_AT, DELETED_AT);

    public CallDetailingAidsController(Context context) {
        super(context);
        dbHelper = new DbHelper(context);
    }
}
