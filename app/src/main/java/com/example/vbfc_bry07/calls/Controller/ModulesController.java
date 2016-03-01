package com.example.vbfc_bry07.calls.Controller;

import android.content.Context;

/**
 * Created by vbfc_bry07 on 2/29/2016.
 */
public class ModulesController extends DbHelper {

    DbHelper dbHelper;

    static String TBL_Modules = "Modules",
            MODULES_ID = "modules_id",
            CODE = "code",
            NAME = "name",
            PRIORITY = "priority",
            ACTIVE = "active";

    public static final String CREATE_Modules = String.format("CREATE TABLE %s (%s INTEGER PRIMARY KEY AUTOINCREMENT, %s INTEGER, %s TEXT, %s TEXT, %s INTEGER, %s INTEGER, %s TEXT, %s TEXT, %s TEXT)",
            TBL_Modules, AI_ID, MODULES_ID, CODE, NAME, PRIORITY, ACTIVE, CREATED_AT, UPDATED_AT, DELETED_AT);

    public ModulesController(Context context) {
        super(context);
        dbHelper = new DbHelper(context);
    }
}