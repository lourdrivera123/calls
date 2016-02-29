package com.example.vbfc_bry07.calls.Controller;

import android.content.Context;

/**
 * Created by vbfc_bry07 on 2/29/2016.
 */
public class MaterialAllocationsController extends DbHelper {
    DbHelper dbHelper;

    static String TBL_MaterialAllocations = " MaterialAllocations",
            MaterialAllocations_ID = "MaterialAllocations",
            MATERIAL_MAP_ID = "MATERIAL_MAP_ID",
            PRODUCT_ID_FK = "PRODUCT_ID_FK",
            MATERIAL_ID_FK = "MATERIAL_ID_FK",
            MATERIAL_COUNT = "MATERIAL_COUNT";

    public static final String CREATE_MaterialAllocation = String.format("CREATE TABLE %s (%s INTEGER PRIMARY KEY AUTOINCREMENT, %s INTEGER, %s INTEGER, %s INTEGER, %s INTEGER, %s INTEGER, %s TEXT, %s TEXT, %s TEXT)",
            TBL_MaterialAllocations, AI_ID, MaterialAllocations_ID, MATERIAL_MAP_ID, PRODUCT_ID_FK, MATERIAL_ID_FK, MATERIAL_COUNT, CREATED_AT, UPDATED_AT, DELETED_AT);

    public MaterialAllocationsController(Context context) {
        super(context);
        dbHelper = new DbHelper(context);
    }
}
