package com.example.vbfc_bry07.calls.Controller;

import android.content.Context;

/**
 * Created by vbfc_bry07 on 2/29/2016.
 */
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

    public static final String CREATE_MaterialInventories = String.format("CREATE TABLE %s (%s INTEGER PRIMARY KEY AUTOINCREMENT, %s INTEGER, %s INTEGER, %s INTEGER, %s INTEGER, %s INTEGER, %s INTEGER, %s INTEGER, %s TEXT, %s TEXT, %s TEXT)",
            TBL_MaterialInventories, AI_ID, MaterialInventories_ID, CYCLE_SET_ID_FK, CYCLE_NUMBER, PRODUCT_ID_FK, MATERIAL_ID_FK, MATERIAL_COUNT, BEGINNING_BALANCE, CREATED_AT, UPDATED_AT, DELETED_AT);

    public MaterialInventoriesController(Context context) {
        super(context);
        dbHelper = new DbHelper(context);
    }
}
