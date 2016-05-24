package com.ece.vbfc_bry07.calls.Controller;

import android.content.Context;

public class CycleSetsController extends DbHelper {
    DbHelper dbHelper;

    static String TBL_CycleSets = "CycleSets",
            CycleSets_ID = "cycle_sets_id",
            CODE = "code",
            YEAR = "year",
            UPLOADER = "uploader",
            UPLOADED = "uploaded";


    public CycleSetsController(Context context) {
        super(context);
        dbHelper = new DbHelper(context);
    }
}
