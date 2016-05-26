package com.ece.vbfc_bry07.calls.controller;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class DoctorClassesController extends DbHelper {

    DbHelper dbHelper;

    static String MAX_VISIT = "max_visit";

    public DoctorClassesController(Context context) {
        super(context);
        dbHelper = new DbHelper(context);
    }

    public int getMaxVisit(int IDM_id) {
        String sql = "SELECT * FROM InstitutionDoctorMaps as IDM INNER JOIN DoctorClasses as dc ON IDM.class_id = dc.doctor_classes_id WHERE IDM_id = " + IDM_id;
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cur = db.rawQuery(sql, null);
        int max_visit = 0;

        if (cur.moveToNext())
            max_visit = cur.getInt(cur.getColumnIndex(MAX_VISIT));

        cur.close();
        db.close();

        return max_visit;
    }
}
