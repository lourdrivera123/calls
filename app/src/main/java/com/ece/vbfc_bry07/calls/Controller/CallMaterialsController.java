package com.ece.vbfc_bry07.calls.Controller;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.ece.vbfc_bry07.calls.Helpers;

import java.util.ArrayList;
import java.util.HashMap;

public class CallMaterialsController extends DbHelper {
    Helpers helpers;
    DbHelper dbHelper;

    static String TBL_CallMaterials = "CallMaterials",
            CallMaterials_ID = "callmaterials_id",
            CALL_ID_FK = "call_id",
            PRODUCT_ID_FK = "product_id",
            SAMPLE = "sample",
            LITERATURE = "literature",
            PROMATERIALS = "promaterials";

    public static final String CREATE_CallMaterials = String.format("CREATE TABLE %s (%s INTEGER PRIMARY KEY AUTOINCREMENT, %s INTEGER, %s INTEGER, %s TEXT, %s INTEGER, %s INTEGER, %s INTEGER, %s TEXT, %s TEXT, %s TEXT)",
            TBL_CallMaterials, AI_ID, CallMaterials_ID, CALL_ID_FK, PRODUCT_ID_FK, SAMPLE, LITERATURE, PROMATERIALS, CREATED_AT, UPDATED_AT, DELETED_AT);

    public CallMaterialsController(Context context) {
        super(context);
        dbHelper = new DbHelper(context);
        helpers = new Helpers();
    }

    public boolean insertCallMaterials(ArrayList<HashMap<String, String>> materials, long callID) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        long rowID = 0;

        for (int x = 0; x < materials.size(); x++) {
            int sample = materials.get(x).get("sample").equals("") ? 0 : Integer.parseInt(materials.get(x).get("sample"));
            int literature = materials.get(x).get("literature").equals("") ? 0 : Integer.parseInt(materials.get(x).get("literature"));
            int promaterials = materials.get(x).get("promaterials").equals("") ? 0 : Integer.parseInt(materials.get(x).get("promaterials"));

            ContentValues val = new ContentValues();
            val.put(CALL_ID_FK, callID);
            val.put(PRODUCT_ID_FK, materials.get(x).get("product_id"));
            val.put(SAMPLE, sample);
            val.put(LITERATURE, literature);
            val.put(PROMATERIALS, promaterials);
            val.put(CREATED_AT, helpers.getCurrentDate("timestamp"));

            rowID = db.insert(TBL_CallMaterials, null, val);
        }

        db.close();

        return rowID > 0;
    }
}