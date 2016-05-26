package com.ece.vbfc_bry07.calls.controller;

import android.content.Context;

public class InstitutionsController extends DbHelper {
    DbHelper dbhelper;

    static String TBL_INSTITUTIONS = "Institutions",
            INST_ID = "inst_id",
            INST_CODE = "inst_code",
            INST_NAME = "inst_name",
            INST_LOCATION = "inst_location";

    public InstitutionsController(Context context) {
        super(context);
        dbhelper = new DbHelper(context);
    }
}
