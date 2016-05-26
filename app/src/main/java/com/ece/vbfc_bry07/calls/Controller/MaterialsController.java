package com.ece.vbfc_bry07.calls.controller;

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
}
