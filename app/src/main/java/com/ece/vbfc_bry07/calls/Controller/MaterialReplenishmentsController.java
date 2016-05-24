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

    public MaterialReplenishmentsController(Context context) {
        super(context);
        dbHelper = new DbHelper(context);
    }
}
