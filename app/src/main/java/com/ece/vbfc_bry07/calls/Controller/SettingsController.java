package com.ece.vbfc_bry07.calls.Controller;

import android.content.Context;

public class SettingsController extends DbHelper {
    DbHelper dbHelper;

    static String TBL_Settings = "Settings",
            Settings_ID = "settings_id",
            CODE = "code",
            VALUE = "value";

    public SettingsController(Context context) {
        super(context);
        dbHelper = new DbHelper(context);
    }
}
