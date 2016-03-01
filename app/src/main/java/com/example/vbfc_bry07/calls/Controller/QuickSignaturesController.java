package com.example.vbfc_bry07.calls.Controller;

import android.content.Context;

/**
 * Created by vbfc_bry07 on 2/29/2016.
 */
public class QuickSignaturesController extends DbHelper {

    DbHelper dbHelper;

    static String TBL_QuickSignatures = "QuickSignatures",
            QuickSignatures_ID = "quick_signatures_id",
            PATH = "path",
            MATCHED = "matched",
            LATITUDE = "latitude",
            LONGITUDE = "longtitude",
            SIGN_DATETIME = "sign_datetime",
            RETRY_COUNT = "retry_count";

    public static final String CREATE_QuickSignatures = String.format("CREATE TABLE %s (%s INTEGER PRIMARY KEY AUTOINCREMENT, %s INTEGER, %s TEXT, %s INTEGER, %s DOUBLE, %s DOUBLE, %s TEXT, %s LONG, %s TEXT, %s TEXT, %s TEXT)",
            TBL_QuickSignatures, AI_ID, QuickSignatures_ID, PATH, MATCHED, LATITUDE, LONGITUDE, SIGN_DATETIME, RETRY_COUNT, CREATED_AT, UPDATED_AT, DELETED_AT);

    public QuickSignaturesController(Context context) {
        super(context);
        dbHelper = new DbHelper(context);
    }
}
