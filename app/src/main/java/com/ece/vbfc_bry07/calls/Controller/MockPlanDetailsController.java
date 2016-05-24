package com.ece.vbfc_bry07.calls.Controller;

import android.content.Context;

public class MockPlanDetailsController extends DbHelper {
    DbHelper dbHelper;

    static String TBL_MockPlanDetails = "MockPlanDetails",
            MockPlanDetails_ID = "mock_plan_details_id",
            MOCK_PLAN_ID_FK = "mock_plan_id_fk",
            INST_DOC_ID_FK = "inst_doc_id_fk",
            CYCLE_DAY_ID_FK = "cycle_day_id_fk";

    public MockPlanDetailsController(Context context) {
        super(context);
        dbHelper = new DbHelper(context);
    }
}
