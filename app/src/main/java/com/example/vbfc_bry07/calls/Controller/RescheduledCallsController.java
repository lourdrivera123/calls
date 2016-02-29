package com.example.vbfc_bry07.calls.Controller;

import android.content.Context;

/**
 * Created by vbfc_bry07 on 2/29/2016.
 */
public class RescheduledCallsController extends DbHelper {

    DbHelper dbHelper;

    static String TBL_RescheduledCalls = "RescheduledCalls",
            RescheduledCalls_ID = "RescheduledCalls",
            CALL_ID_FK = "CALL_ID_FK",
            CYCLE_DAY_ID = "CYCLE_DAY_ID",
            REASON_ID_FK = "REASON_ID_FK",
            REMARKS = "REMARKS",
            SENT = "SENT";

    public static final String CREATE_RescheduledCalls = String.format("CREATE TABLE %s (%s INTEGER PRIMARY KEY AUTOINCREMENT, %s INTEGER, %s INTEGER, %s INTEGER, %s INTEGER, %s TEXT, %s INTEGER, %s TEXT, %s TEXT, %s TEXT)",
            TBL_RescheduledCalls, AI_ID, RescheduledCalls_ID, CALL_ID_FK, CYCLE_DAY_ID, REASON_ID_FK, REMARKS, SENT, CREATED_AT, UPDATED_AT, DELETED_AT);

    public RescheduledCallsController(Context context) {
        super(context);
        dbHelper = new DbHelper(context);
    }
}
