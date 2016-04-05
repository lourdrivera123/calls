package com.ece.vbfc_bry07.calls.Controller;

import android.content.Context;

/**
 * Created by vbfc_bry07 on 2/29/2016.
 */
public class MaterialAllocationsController extends DbHelper {
    DbHelper dbHelper;

    static String TBL_MaterialAllocations = " MaterialAllocations",
            MaterialAllocations_ID = "material_allocations_id",
            MATERIAL_MAP_ID = "material_map_id",
            PRODUCT_ID_FK = "product_id_fk",
            MATERIAL_ID_FK = "material_id_fk",
            MATERIAL_COUNT = "material_count";

    public static final String CREATE_MaterialAllocation = String.format("CREATE TABLE %s (%s INTEGER PRIMARY KEY AUTOINCREMENT, %s INTEGER, %s INTEGER, %s INTEGER, %s INTEGER, %s INTEGER, %s TEXT, %s TEXT, %s TEXT)",
            TBL_MaterialAllocations, AI_ID, MaterialAllocations_ID, MATERIAL_MAP_ID, PRODUCT_ID_FK, MATERIAL_ID_FK, MATERIAL_COUNT, CREATED_AT, UPDATED_AT, DELETED_AT);

    public MaterialAllocationsController(Context context) {
        super(context);
        dbHelper = new DbHelper(context);
    }
}
