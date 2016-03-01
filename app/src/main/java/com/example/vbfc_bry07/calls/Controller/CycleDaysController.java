package com.example.vbfc_bry07.calls.Controller;

import android.content.Context;

public class CycleDaysController extends DbHelper {

    DbHelper dbHelper;

    static String TBL_PLANS = "CycleDays",
            CycleDays_ID = "cycledays_id",
            CYCLE_SET_ID_FK = "cycle_set",
            CYCLE_NUMBER = "cycle_number",
            DAY_NUMBER = "day_number",
            DATE = "date",
            DAY_TYPE_ID_FK = "day_type",
            LABEL = "label";

    public static final String CREATE_CycleDays = String.format("CREATE TABLE %s (%s INTEGER PRIMARY KEY AUTOINCREMENT, %s INTEGER, %s INTEGER, %s INTEGER, %s INTEGER, %s TEXT, %s INTEGER, %s TEXT, %s TEXT, %s TEXT, %s TEXT)",
            TBL_PLANS, AI_ID, CycleDays_ID, CYCLE_SET_ID_FK, CYCLE_NUMBER, DAY_NUMBER, DATE, DAY_TYPE_ID_FK, LABEL, CREATED_AT, UPDATED_AT, DELETED_AT);

    public CycleDaysController(Context context) {
        super(context);
        dbHelper = new DbHelper(context);
    }
}
