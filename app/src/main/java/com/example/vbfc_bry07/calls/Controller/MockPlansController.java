package com.example.vbfc_bry07.calls.Controller;

import android.content.Context;

/**
 * Created by vbfc_bry07 on 2/29/2016.
 */
public class MockPlansController extends DbHelper {

    DbHelper dbHelper;

    static String TBL_MockPlans = "MockPlans",
            MockPlans_ID = "MockPlans_ID",
            DEVICE_ID = "DEVICE_ID",
            CYCLE_SET_ID = "CYCLE_SET_ID",
            CYCLE_NUMBER = "CYCLE_NUMBER",
            STATUS_DATE = "STATUS_DATE";

    public static final String CREATE_MockPlans = String.format("CREATE TABLE %s (%s INTEGER PRIMARY KEY AUTOINCREMENT, %s INTEGER, %s TEXT, %s TEXT, %s INTEGER, %s TEXT, %s TEXT, %s TEXT, %s TEXT)",
            TBL_MockPlans, AI_ID, MockPlans_ID, DEVICE_ID, CYCLE_SET_ID, CYCLE_NUMBER, STATUS_DATE, CREATED_AT, UPDATED_AT, DELETED_AT);

    public MockPlansController(Context context) {
        super(context);
        dbHelper = new DbHelper(context);
    }
}
