package com.example.vbfc_bry07.calls.Controller;

import android.content.Context;

/**
 * Created by vbfc_bry07 on 2/29/2016.
 */
public class CallReportsController extends DbHelper {

    DbHelper dbHelper;

    static String TBL_CallReports = "CallReports",
            CallReports_ID = "callreports_id",
            INST_DOC_ID_FK = "inst_doc_id_fk",
            CYCLE_SET_ID_FK = "cycle_set_id_fk",
            CYCLE_NUMBER = "cycle_number",
            CALL_COUNT = "call_count",
            PLAN_COUNT = "plan_count";

    public static final String CREATE_CallReports = String.format("CREATE TABLE %s (%s INTEGER PRIMARY KEY AUTOINCREMENT, %s INTEGER, %s INTEGER, %s INTEGER, %s INTEGER, %s INTEGER, %s INTEGER, %s TEXT, %s TEXT, %s TEXT)",
            TBL_CallReports, AI_ID, CallReports_ID, INST_DOC_ID_FK, CYCLE_SET_ID_FK, CYCLE_NUMBER, CALL_COUNT, PLAN_COUNT, CREATED_AT, UPDATED_AT, DELETED_AT);

    public CallReportsController(Context context) {
        super(context);
        dbHelper = new DbHelper(context);
    }
}
