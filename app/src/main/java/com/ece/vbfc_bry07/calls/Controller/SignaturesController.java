package com.ece.vbfc_bry07.calls.Controller;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.ece.vbfc_bry07.calls.Helpers;

public class SignaturesController extends DbHelper {
    DbHelper dbHelper;
    Helpers helpers;

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
        helpers = new Helpers();
    }

    ///////////////////////////INSERT METHODS
    public boolean insertSignature(long call_id, String path) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues val = new ContentValues();
        val.put(CALL_ID_FK, call_id);
        val.put(PATH, path);
        val.put(STATUS_ID_FK, 1);
        val.put(CREATED_AT, helpers.getCurrentDate("timestamp"));

        long id = db.insert(TBL_Signatures, null, val);

        db.close();

        return id > 0;
    }
}
