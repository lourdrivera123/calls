package com.ece.vbfc_bry07.calls.controller;

import android.content.Context;

public class VersionsController extends DbHelper {

    DbHelper dbHelper;

    static String TBL_Versions = "Versions",
            Versions_ID = "versions_id",
            CODE = "code",
            NAME = "name",
            VALUE = "value";

    public VersionsController(Context context) {
        super(context);
        dbHelper = new DbHelper(context);
    }
}
