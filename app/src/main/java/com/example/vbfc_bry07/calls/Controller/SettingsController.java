package com.example.vbfc_bry07.calls.Controller;

import android.content.Context;

/**
 * Created by vbfc_bry07 on 2/29/2016.
 */
public class SettingsController extends DbHelper {

    DbHelper dbHelper;

    static String TBL_Settings = "Settings",
            Settings_ID = "Settings_ID",
            CODE = "CODE",
            VALUE = "VALUE";

    public static final String CREATE_Settings = String.format("CREATE TABLE %s (%s INTEGER PRIMARY KEY AUTOINCREMENT, %s INTEGER, %s TEXT, %s TEXT, %s TEXT, %s TEXT, %s TEXT)",
            TBL_Settings, AI_ID, Settings_ID, CODE, VALUE, CREATED_AT, UPDATED_AT, DELETED_AT);

    public SettingsController(Context context) {
        super(context);
        dbHelper = new DbHelper(context);
    }
}
