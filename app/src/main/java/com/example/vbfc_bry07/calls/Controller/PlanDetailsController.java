package com.example.vbfc_bry07.calls.Controller;

import android.content.Context;

public class PlanDetailsController extends DbHelper {
    DbHelper dbHelper;

    static String TBL_PlanDetails = "PlanDetails",
            PlanDetails_ID = "plan_details_id",
            PLAN_ID_FK = "plan_id",
            INST_DOC_ID_FK = "inst_doc_id",
            CYCLE_DAY_ID_FK = "cycle_day",
            CYCLE_DAY_LABEL = "label";

    public static final String CREATE_PlanDetails = String.format("CREATE TABLE %s (%s INTEGER PRIMARY KEY AUTOINCREMENT, %s INTEGER, %s TEXT, %s INTEGER, %s TEXT, %s TEXT, %s TEXT, %s TEXT, %s TEXT)",
            TBL_PlanDetails, AI_ID, PlanDetails_ID, PLAN_ID_FK, INST_DOC_ID_FK, CYCLE_DAY_ID_FK, CYCLE_DAY_LABEL, CREATED_AT, UPDATED_AT, DELETED_AT);

    public PlanDetailsController(Context context) {
        super(context);
        dbHelper = new DbHelper(context);
    }
}
