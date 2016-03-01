package com.example.vbfc_bry07.calls.Controller;

import android.content.Context;

public class CycleSetsController extends DbHelper {

    DbHelper dbHelper;

    static String TBL_CycleSets = "CycleSets",
            CycleSets_ID = "cycle_sets_id",
            CODE = "code",
            YEAR = "year",
            UPLOADER = "uploader",
            UPLOADED = "uploaded";

    public static final String CREATE_CycleSets = String.format("CREATE TABLE %s (%s INTEGER PRIMARY KEY AUTOINCREMENT, %s INTEGER, %s TEXT, %s INTEGER, %s TEXT, %s TEXT, %s TEXT, %s TEXT, %s TEXT)",
            TBL_CycleSets, AI_ID, CycleSets_ID, CODE, YEAR, UPLOADER, UPLOADED, CREATED_AT, UPDATED_AT, DELETED_AT);

    public CycleSetsController(Context context) {
        super(context);
        dbHelper = new DbHelper(context);
    }
}
