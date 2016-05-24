package com.ece.vbfc_bry07.calls.Controller;

import android.content.Context;

public class MockPlansController extends DbHelper {
    DbHelper dbHelper;

    static String TBL_MockPlans = "MockPlans",
            MockPlans_ID = "mock_plans_id",
            DEVICE_ID = "device_id",
            CYCLE_SET_ID = "cycle_set_id",
            CYCLE_NUMBER = "cycle_number",
            STATUS_DATE = "status_date";

    public MockPlansController(Context context) {
        super(context);
        dbHelper = new DbHelper(context);
    }
}
