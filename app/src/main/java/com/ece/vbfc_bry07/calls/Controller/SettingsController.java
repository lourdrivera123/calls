package com.ece.vbfc_bry07.calls.Controller;

import android.content.Context;

public class SettingsController extends DbHelper {

    DbHelper dbHelper;

    static String TBL_Settings = "Settings",
            Settings_ID = "settings_id",
            CODE = "code",
            VALUE = "value";

    public static final String CREATE_Settings = String.format("CREATE TABLE %s (%s INTEGER PRIMARY KEY AUTOINCREMENT, %s INTEGER, %s TEXT, %s TEXT, %s TEXT, %s TEXT, %s TEXT)",
            TBL_Settings, AI_ID, Settings_ID, CODE, VALUE, CREATED_AT, UPDATED_AT, DELETED_AT);

    public SettingsController(Context context) {
        super(context);
        dbHelper = new DbHelper(context);
    }
}
