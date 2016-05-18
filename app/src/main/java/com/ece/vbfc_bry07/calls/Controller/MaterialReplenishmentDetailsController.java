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

    public static final String CREATE_MaterialReplenishmentDetails = String.format("CREATE TABLE %s (%s INTEGER PRIMARY KEY AUTOINCREMENT, %s INTEGER, %s INTEGER, %s INTEGER, %s INTEGER, %s INTEGER, %s TEXT, %s TEXT, %s TEXT)",
            TBL_MaterialReplenishmentDetails, AI_ID, MaterialReplenishmentDetails_ID, HEADER_ID_FK, PRODUCT_ID_FK, MATERIAL_ID_FK, MATERIAL_COUNT, CREATED_AT, UPDATED_AT, DELETED_AT);

    public MaterialReplenishmentDetailsController(Context context) {
        super(context);
        dbHelper = new DbHelper(context);
    }
}
