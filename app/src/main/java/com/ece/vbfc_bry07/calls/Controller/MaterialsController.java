package com.ece.vbfc_bry07.calls.Controller;

import android.content.Context;

public class MaterialsController extends DbHelper {
    DbHelper dbHelper;

    static String TBL_MATERIALS = "Materials",
            MATERIALS_ID = "materials_id",
            MATERIALS_CODE = "code",
            MATERIALS_NAME = "name";

    public MaterialsController(Context context) {
        super(context);
        dbHelper = new DbHelper(context);
    }

    public static final String CREATE_MATERIALS = String.format("CREATE TABLE %s (%s INTEGER PRIMARY KEY AUTOINCREMENT, %s INTEGER, %s TEXT, %s TEXT, %s TEXT, %s TEXT, %s TEXT)",
            TBL_MATERIALS, AI_ID, MATERIALS_ID, MATERIALS_CODE, MATERIALS_NAME, CREATED_AT, UPDATED_AT, DELETED_AT);
}
