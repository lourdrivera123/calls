package com.ece.vbfc_bry07.calls.controller;

import android.content.Context;

public class DayTypesController extends DbHelper {

    DbHelper dbHelper;

    static String TBL_DayTypes = "DayTypes",
            DayTypes_ID = "day_types_id",
            CODE = "code",
            NAME = "name",
            ENABLED = "enabled";

    public static final String CREATE_DayTypes = String.format("CREATE TABLE %s (%s INTEGER PRIMARY KEY AUTOINCREMENT, %s INTEGER, %s TEXT, %s TEXT, %s INTEGER, %s TEXT, %s TEXT, %s TEXT)",
            TBL_DayTypes, AI_ID, DayTypes_ID, CODE, NAME, ENABLED, CREATED_AT, UPDATED_AT, DELETED_AT);

    public DayTypesController(Context context) {
        super(context);
        dbHelper = new DbHelper(context);
    }
}
