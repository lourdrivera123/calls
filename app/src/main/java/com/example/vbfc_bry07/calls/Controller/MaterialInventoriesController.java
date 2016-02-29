package com.example.vbfc_bry07.calls.Controller;

import android.content.Context;

/**
 * Created by vbfc_bry07 on 2/29/2016.
 */
public class MaterialInventoriesController extends DbHelper {

    DbHelper dbHelper;

    static String TBL_MaterialInventories = "MaterialInventories",
            MaterialInventories_ID = "MaterialInventories_ID",
            CYCLE_SET_ID_FK = "CYCLE_SET_ID_FK",
            CYCLE_NUMBER = "CYCLE_NUMBER",
            PRODUCT_ID_FK = "PRODUCT_ID_FK",
            MATERIAL_ID_FK = "MATERIAL_ID_FK",
            MATERIAL_COUNT = "MATERIAL_COUNT",
            BEGINNING_BALANCE = "BEGINNING_BALANCE";

    public static final String CREATE_MaterialInventories = String.format("CREATE TABLE %s (%s INTEGER PRIMARY KEY AUTOINCREMENT, %s INTEGER, %s INTEGER, %s INTEGER, %s INTEGER, %s INTEGER, %s INTEGER, %s INTEGER, %s TEXT, %s TEXT, %s TEXT)",
            TBL_MaterialInventories, AI_ID, MaterialInventories_ID, CYCLE_SET_ID_FK, CYCLE_NUMBER, PRODUCT_ID_FK, MATERIAL_ID_FK, MATERIAL_COUNT, BEGINNING_BALANCE, CREATED_AT, UPDATED_AT, DELETED_AT);

    public MaterialInventoriesController(Context context) {
        super(context);
        dbHelper = new DbHelper(context);
    }
}
