package com.ece.vbfc_bry07.calls.controller;

import android.content.Context;

public class StagesController extends DbHelper {

    DbHelper dbHelper;

    static String TBL_Stages = "Stages",
            Stages_ID = "stages_id",
            CODE = "code",
            NAME = "name";

    public StagesController(Context context) {
        super(context);
        dbHelper = new DbHelper(context);
    }
}
