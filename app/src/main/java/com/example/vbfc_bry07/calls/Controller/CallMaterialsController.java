package com.example.vbfc_bry07.calls.Controller;

import android.content.Context;

public class CallMaterialsController extends DbHelper {

    DbHelper dbHelper;

    static String TBL_CallMaterials = "CallMaterials",
            CallMaterials_ID = "callmaterials_id",
            CALL_ID_FK = "call_id",
            PRODUCT_ID_FK = "product_id",
            MATERIAL_ID_FK = "material_id",
            MATERIAL_COUNT = "material_count";

    public static final String CREATE_CallMaterials = String.format("CREATE TABLE %s (%s INTEGER PRIMARY KEY AUTOINCREMENT, %s INTEGER, %s INTEGER, %s INTEGER, %s INTEGER, %s INTEGER, %s TEXT, %s TEXT, %s TEXT)",
            TBL_CallMaterials, AI_ID, CallMaterials_ID, CALL_ID_FK, PRODUCT_ID_FK, MATERIAL_ID_FK, MATERIAL_COUNT, CREATED_AT, UPDATED_AT, DELETED_AT);

    public CallMaterialsController(Context context) {
        super(context);
        dbHelper = new DbHelper(context);
    }
}
