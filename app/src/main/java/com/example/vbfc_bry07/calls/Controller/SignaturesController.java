package com.example.vbfc_bry07.calls.Controller;

import android.content.Context;

/**
 * Created by vbfc_bry07 on 2/29/2016.
 */
public class SignaturesController extends DbHelper {

    DbHelper dbHelper;

    static String TBL_Signatures = "Signatures",
            Signatures_ID = "signatures_id",
            CALL_ID_FK = "call_id_fk",
            PATH = "path",
            STATUS_ID_FK = "status_id_fk";

    public static final String CREATE_Signatures = String.format("CREATE TABLE %s (%s INTEGER PRIMARY KEY AUTOINCREMENT, %s INTEGER, %s INTEGER, %s TEXT, %s INTEGER, %s TEXT, %s TEXT, %s TEXT)",
            TBL_Signatures, AI_ID, Signatures_ID, CALL_ID_FK, PATH, STATUS_ID_FK, CREATED_AT, UPDATED_AT, DELETED_AT);

    public SignaturesController(Context context) {
        super(context);
        dbHelper = new DbHelper(context);
    }
}
