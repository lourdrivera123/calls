package com.example.vbfc_bry07.calls.Controller;

import android.content.Context;

/**
 * Created by vbfc_bry07 on 2/29/2016.
 */
public class MissedCallsController extends DbHelper {

    DbHelper dbHelper;

    static String TBL_MissedCalls = "MissedCalls",
            MissedCalls_ID = "MissedCalls_ID",
            CALL_ID_FK = "CALL_ID_FK",
            REASON_ID_FK = "REASON_ID_FK",
            REMARKS = "REMARKS",
            SENT = "SENT",
            STATUS_APPROVED = "STATUS_APPROVED";

    public static final String CREATE_MissedCalls = String.format("CREATE TABLE %s (%s INTEGER PRIMARY KEY AUTOINCREMENT, %s INTEGER, %s INTEGER, %s INTEGER, %s TEXT, %s INTEGER, %s INTEGER, %s TEXT, %s TEXT, %s TEXT)",
            TBL_MissedCalls, AI_ID, MissedCalls_ID, CALL_ID_FK, REASON_ID_FK, REMARKS, SENT, STATUS_APPROVED, CREATED_AT, UPDATED_AT, DELETED_AT);

    public MissedCallsController(Context context) {
        super(context);
        dbHelper = new DbHelper(context);
    }
}
