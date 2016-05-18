package com.ece.vbfc_bry07.calls.Controller;

import android.content.Context;

public class MaterialReplenishmentsController extends DbHelper {

    DbHelper dbHelper;

    static String TBL_MaterialReplenishments = "MaterialReplenishments",
            MaterialReplenishments_ID = "material_replenishments_id",
            CYCLE_SET_ID_FK = "cycle_set_id_fk",
            CYCLE_NUMBER = "cycle_number",
            NAME = "name",
            UPLOADED = "uploaded",
            ACKNOWLEDGED = "acknowledged";

    public static final String CREATE_MaterialReplenishments = String.format("CREATE TABLE %s (%s INTEGER PRIMARY KEY AUTOINCREMENT, %s INTEGER, %s INTEGER, %s INTEGER, %s TEXT, %s TEXT, %s INTEGER, %s TEXT, %s TEXT, %s TEXT)",
            TBL_MaterialReplenishments, AI_ID, MaterialReplenishments_ID, CYCLE_SET_ID_FK, CYCLE_NUMBER, NAME, UPLOADED, ACKNOWLEDGED, CREATED_AT, UPDATED_AT, DELETED_AT);

    public MaterialReplenishmentsController(Context context) {
        super(context);
        dbHelper = new DbHelper(context);
    }
}
