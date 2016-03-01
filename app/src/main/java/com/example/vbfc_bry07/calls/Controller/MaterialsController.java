package com.example.vbfc_bry07.calls.Controller;

import android.content.Context;

/**
 * Created by vbfc_bry07 on 2/29/2016.
 */
public class MaterialsController extends DbHelper {

    DbHelper dbHelper;

    static String TBL_Materials = "Materials",
            Materials_ID = "materials_id",
            CODE = "code",
            NAME = "name";

    public static final String CREATE_Materials = String.format("CREATE TABLE %s (%s INTEGER PRIMARY KEY AUTOINCREMENT, %s INTEGER, %s TEXT, %s TEXT, %s TEXT, %s TEXT, %s TEXT)",
            TBL_Materials, AI_ID, Materials_ID, CODE, NAME, CREATED_AT, UPDATED_AT, DELETED_AT);

    public MaterialsController(Context context) {
        super(context);
        dbHelper = new DbHelper(context);
    }
}
