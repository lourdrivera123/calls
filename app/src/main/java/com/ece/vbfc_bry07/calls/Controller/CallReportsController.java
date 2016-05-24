package com.ece.vbfc_bry07.calls.Controller;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.HashMap;

public class CallReportsController extends DbHelper {
    DbHelper dbHelper;

    public CallReportsController(Context context) {
        super(context);
        dbHelper = new DbHelper(context);
    }

    //////////////////////////GET METHODS
    public ArrayList<HashMap<String, String>> getMonthReport(int month) {
        String sql = "SELECT max_visit as total, COUNT(c._id) as calls, * FROM PlanDetails as pd INNER JOIN Plans as p ON pd.plan_id = p._id " +
                "LEFT JOIN Calls as c ON c.plan_details_id = pd.plan_details_id INNER JOIN InstitutionDoctorMaps as idm ON pd.inst_doc_id = idm.IDM_id " +
                "INNER JOIN DoctorClasses as dc ON idm.class_id = dc.doctor_classes_id WHERE p.cycle_number = " + month + " AND p.status = 1 AND " +
                "(c.plan_details_id IS NULL OR c.plan_details_id > 0 OR c.temp_planDetails_id = pd._id) GROUP BY pd.inst_doc_id";
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cur = db.rawQuery(sql, null);
        ArrayList<HashMap<String, String>> array = new ArrayList<>();

        while (cur.moveToNext()) {
            HashMap<String, String> map = new HashMap<>();
            map.put("IDM_id", cur.getString(cur.getColumnIndex("inst_doc_id")));
            map.put("calls", cur.getString(cur.getColumnIndex("calls")));
            map.put("total", cur.getString(cur.getColumnIndex("total")));

            array.add(map);
        }

        cur.close();
        db.close();

        return array;
    }
}
