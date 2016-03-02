package com.example.vbfc_bry07.calls.Controller;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class PlansController extends DbHelper {
    DbHelper dbhelper;

    static String TBL_PLANS = "Plans",
            PLANS_CYCLE_SET = "cycle_set",
            PLANS_CYCLE_NUMBER = "cycle_number",
            PLANS_STATUS = "status",
            PLANS_STATUS_DATE = "status_date";

    public static final String CREATE_PLANS = String.format("CREATE TABLE %s (%s INTEGER PRIMARY KEY AUTOINCREMENT, %s INTEGER, %s INTEGER, %s TEXT, %s TEXT, %s TEXT, %s TEXT, %s TEXT)",
            TBL_PLANS, AI_ID, PLANS_CYCLE_SET, PLANS_CYCLE_NUMBER, PLANS_STATUS, PLANS_STATUS_DATE, CREATED_AT, UPDATED_AT, DELETED_AT);

    public PlansController(Context context) {
        super(context);
        dbhelper = new DbHelper(context);
    }

    public boolean checkIfHasPlan(int month, int year) {
        SQLiteDatabase db = dbhelper.getWritableDatabase();
        String sql = "SELECT * FROM " + TBL_PLANS + " WHERE " + PLANS_CYCLE_SET + " = " + year + " AND " + PLANS_CYCLE_NUMBER + " = " + month;
        Cursor cur = db.rawQuery(sql, null);

        boolean flag = cur.moveToNext();

        cur.close();
        db.close();

        return flag;
    }
}
