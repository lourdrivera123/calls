package com.ece.vbfc_bry07.calls.Controller;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

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

    public static void insertStartUpYear(SQLiteDatabase db) {
        ContentValues val = new ContentValues();
        val.put(CycleSets_ID, 1);
        val.put(CODE, "CYCLE2016");
        val.put(YEAR, 2016);
        val.put(UPLOADER, "DUMMY");

        db.insert(TBL_CycleSets, null, val);
    }

    public int getCycleSetID(int year) {
        String sql = "SELECT * FROM " + TBL_CycleSets + " WHERE " + YEAR + " = " + year;
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cur = db.rawQuery(sql, null);
        int cycle_setID = 0;

        if (cur.moveToNext())
            cycle_setID = cur.getInt(cur.getColumnIndex(CycleSets_ID));

        cur.close();
        db.close();

        return cycle_setID;
    }
}
