package com.ece.vbfc_bry07.calls.Controller;

import android.content.Context;

public class MaterialInventoriesController extends DbHelper {
    DbHelper dbHelper;

    static String TBL_MaterialInventories = "MaterialInventories",
            MaterialInventories_ID = "material_inventories_id",
            CYCLE_SET_ID_FK = "cycle_set_id_fk",
            CYCLE_NUMBER = "cycle_number",
            PRODUCT_ID_FK = "product_id_fk",
            MATERIAL_ID_FK = "material_id_fk",
            MATERIAL_COUNT = "material_count",
            BEGINNING_BALANCE = "beginning_balance";

    public MaterialInventoriesController(Context context) {
        super(context);
        dbHelper = new DbHelper(context);
    }
}
