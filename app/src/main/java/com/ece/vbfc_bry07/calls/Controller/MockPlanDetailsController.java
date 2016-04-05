package com.ece.vbfc_bry07.calls.Controller;

import android.content.Context;

/**
 * Created by vbfc_bry07 on 2/29/2016.
 */
public class MockPlanDetailsController extends DbHelper {

    DbHelper dbHelper;

    static String TBL_MockPlanDetails = "MockPlanDetails",
            MockPlanDetails_ID = "mock_plan_details_id",
            MOCK_PLAN_ID_FK = "mock_plan_id_fk",
            INST_DOC_ID_FK = "inst_doc_id_fk",
            CYCLE_DAY_ID_FK = "cycle_day_id_fk";

    public static final String CREATE_MockPlanDetails = String.format("CREATE TABLE %s (%s INTEGER PRIMARY KEY AUTOINCREMENT, %s INTEGER, %s TEXT, %s TEXT, %s INTEGER, %s TEXT, %s TEXT, %s TEXT)",
            TBL_MockPlanDetails, AI_ID, MockPlanDetails_ID, MOCK_PLAN_ID_FK, INST_DOC_ID_FK, CYCLE_DAY_ID_FK, CREATED_AT, UPDATED_AT, DELETED_AT);

    public MockPlanDetailsController(Context context) {
        super(context);
        dbHelper = new DbHelper(context);
    }
}
