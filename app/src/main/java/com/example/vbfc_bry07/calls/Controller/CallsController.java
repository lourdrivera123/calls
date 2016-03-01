package com.example.vbfc_bry07.calls.Controller;

import android.content.Context;

public class CallsController extends DbHelper {

    DbHelper dbHelper;

    static String TBL_Calls = "Calls",
            Calls_ID = "calls_id",
            INST_DOC_ID_FK = "inst_doc_id",
            CYCLE_DAY_ID_FK = "cycle_day_id",
            STATUS_ID_FK = "status_id",
            PLANNED = "planned",
            MAKEUP = "makeup",
            START_DATETIME = "start_datetime",
            END_DATETIME = "end_datetime",
            LATITUDE = "latitude",
            LONGITUDE = "longtitude",
            RESCHEDULE_DATE = "reschedule_date",
            SIGNED_DAY_ID = "signed_day_id",
            RETRY_COUNT = "retry_count",
            JOINT_CALL = "joinr_call",
            QUICK_SIGN = "quick_sign";

    public static final String CREATE_Calls = String.format("CREATE TABLE %s (%s INTEGER PRIMARY KEY AUTOINCREMENT, %s INTEGER, %s INTEGER, %s INTEGER, %s INTEGER, %s INTEGER, %s INTEGER, %s TEXT, %s TEXT, %s DOUBLE, %s DOUBLE, %s INTEGER, %s INTEGER, %s LONG, %s INTEGER, %s INTEGER, %s TEXT, %s TEXT, %s TEXT)",
            TBL_Calls, AI_ID, Calls_ID, INST_DOC_ID_FK, CYCLE_DAY_ID_FK, STATUS_ID_FK, PLANNED, MAKEUP, START_DATETIME, END_DATETIME, LATITUDE, LONGITUDE, RESCHEDULE_DATE, SIGNED_DAY_ID, RETRY_COUNT, JOINT_CALL, QUICK_SIGN, CREATED_AT, UPDATED_AT, DELETED_AT);

    public CallsController(Context context) {
        super(context);
        dbHelper = new DbHelper(context);
    }
}
