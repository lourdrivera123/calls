package com.example.vbfc_bry07.calls.Controller;

import android.content.Context;

public class StagesController extends DbHelper {

    DbHelper dbHelper;

    static String TBL_Stages = "Stages",
            Stages_ID = "Stages_ID",
            CODE = "CODE",
            NAME = "NAME";

    public static final String CREATE_Stages = String.format("CREATE TABLE %s (%s INTEGER PRIMARY KEY AUTOINCREMENT, %s INTEGER, %s TEXT, %s TEXT, %s TEXT, %s TEXT, %s TEXT)",
            TBL_Stages, AI_ID, Stages_ID, CODE, NAME, CREATED_AT, UPDATED_AT, DELETED_AT);

    public StagesController(Context context) {
        super(context);
        dbHelper = new DbHelper(context);
    }
}
