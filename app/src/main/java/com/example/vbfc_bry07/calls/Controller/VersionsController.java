package com.example.vbfc_bry07.calls.Controller;

import android.content.Context;

/**
 * Created by vbfc_bry07 on 2/29/2016.
 */
public class VersionsController extends DbHelper {

    DbHelper dbHelper;

    static String TBL_Versions = "Versions",
            Versions_ID = "versions_id",
            CODE = "code",
            NAME = "name",
            VALUE = "value";

    public static final String CREATE_Versions = String.format("CREATE TABLE %s (%s INTEGER PRIMARY KEY AUTOINCREMENT, %s INTEGER, %s TEXT, %s TEXT, %s DOUBLE, %s TEXT, %s TEXT, %s TEXT)",
            TBL_Versions, AI_ID, Versions_ID, CODE, NAME, VALUE, CREATED_AT, UPDATED_AT, DELETED_AT);

    public VersionsController(Context context) {
        super(context);
        dbHelper = new DbHelper(context);
    }
}
