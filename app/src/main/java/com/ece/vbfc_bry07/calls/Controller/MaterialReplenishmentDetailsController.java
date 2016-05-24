package com.ece.vbfc_bry07.calls.Controller;

import android.content.Context;

public class MaterialReplenishmentDetailsController extends DbHelper {
    DbHelper dbHelper;

    static String TBL_MaterialReplenishmentDetails = "MaterialReplenishmentDetails",
            MaterialReplenishmentDetails_ID = "material_replenishment_details_id",
            HEADER_ID_FK = "header_id_fk",
            PRODUCT_ID_FK = "product_id_fk",
            MATERIAL_ID_FK = "material_id_fk",
            MATERIAL_COUNT = "material_count";

    public MaterialReplenishmentDetailsController(Context context) {
        super(context);
        dbHelper = new DbHelper(context);
    }
}
