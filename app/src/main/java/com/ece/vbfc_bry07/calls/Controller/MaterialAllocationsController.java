package com.ece.vbfc_bry07.calls.controller;

import android.content.Context;

public class MaterialAllocationsController extends DbHelper {
    DbHelper dbHelper;

    static String TBL_MaterialAllocations = " MaterialAllocations",
            MaterialAllocations_ID = "material_allocations_id",
            MATERIAL_MAP_ID = "material_map_id",
            PRODUCT_ID_FK = "product_id_fk",
            MATERIAL_ID_FK = "material_id_fk",
            MATERIAL_COUNT = "material_count";

    public MaterialAllocationsController(Context context) {
        super(context);
        dbHelper = new DbHelper(context);
    }
}
