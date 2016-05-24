package com.ece.vbfc_bry07.calls.Controller;

import android.content.Context;

public class ModulesController extends DbHelper {
    DbHelper dbHelper;

    static String TBL_Modules = "Modules",
            MODULES_ID = "modules_id",
            CODE = "code",
            NAME = "name",
            PRIORITY = "priority",
            ACTIVE = "active";

    public ModulesController(Context context) {
        super(context);
        dbHelper = new DbHelper(context);
    }
}
