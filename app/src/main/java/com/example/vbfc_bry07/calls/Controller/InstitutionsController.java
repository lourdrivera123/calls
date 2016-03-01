package com.example.vbfc_bry07.calls.Controller;

import android.content.Context;

public class InstitutionsController extends DbHelper {

    DbHelper dbhelper;

    static String TBL_INSTITUTIONS = "Institutions",
            INST_ID = "INST_ID",
            INST_CODE = "INST_CODE",
            INST_NAME = "INST_NAME",
            INST_LOCATION = "INST_LOCATION";

    public static final String CREATE_Institutions = String.format("CREATE TABLE %s (%s INTEGER PRIMARY KEY AUTOINCREMENT, %s INTEGER, %s TEXT, %s TEXT, %s TEXT, %s TEXT, %s TEXT, %s TEXT)",
            TBL_INSTITUTIONS, AI_ID, INST_ID, INST_CODE, INST_NAME, INST_LOCATION, CREATED_AT, UPDATED_AT, DELETED_AT);

    public InstitutionsController(Context context) {
        super(context);
        dbhelper = new DbHelper(context);
    }
}
