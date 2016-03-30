package com.example.vbfc_bry07.calls.Controller;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.vbfc_bry07.calls.Helpers;

import java.util.ArrayList;
import java.util.HashMap;

public class PlanDetailsController extends DbHelper {
    DbHelper dbHelper;
    PlansController pc;
    Helpers helpers;

    static String TBL_PlanDetails = "PlanDetails",
            PlanDetails_ID = "plan_details_id",
            PLAN_ID_FK = "plan_id",
            INST_DOC_ID_FK = "inst_doc_id",
            CYCLE_DAY = "cycle_day",
            CYCLE_DAY_LABEL = "label";

    public static final String CREATE_PlanDetails = String.format("CREATE TABLE %s (%s INTEGER PRIMARY KEY AUTOINCREMENT, %s INTEGER, %s INTEGER, %s INTEGER, %s TEXT, %s TEXT, %s TEXT, %s TEXT, %s TEXT)",
            TBL_PlanDetails, AI_ID, PlanDetails_ID, PLAN_ID_FK, INST_DOC_ID_FK, CYCLE_DAY, CYCLE_DAY_LABEL, CREATED_AT, UPDATED_AT, DELETED_AT);

    public PlanDetailsController(Context context) {
        super(context);
        dbHelper = new DbHelper(context);
        helpers = new Helpers();
        pc = new PlansController(context);
    }

    //GET METHODS
    public ArrayList<HashMap<String, ArrayList<HashMap<String, String>>>> getPlanDetailsByPlanID(int plan_id) {
        String sql1 = "SELECT DISTINCT cycle_day FROM PlanDetails as pd INNER JOIN Plans as p ON pd.plan_id = p.id WHERE pd.plan_id = " + plan_id;
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cur = db.rawQuery(sql1, null);
        ArrayList<HashMap<String, ArrayList<HashMap<String, String>>>> array = new ArrayList<>();

        while (cur.moveToNext()) {
            String date = cur.getString(cur.getColumnIndex("cycle_day"));
            HashMap<String, ArrayList<HashMap<String, String>>> map_day = new HashMap<>();
            ArrayList<HashMap<String, String>> array_day = new ArrayList<>();

            String sql2 = "SELECT  pd.*, i.inst_name, d.doc_id, d.doc_name, d.contact_number, dc.name, dc.max_visit FROM PlanDetails as pd INNER JOIN InstitutionDoctorMaps as idm " +
                    "on pd.inst_doc_id = idm.IDM_ID INNER JOIN Doctors as d on idm.doctor_id = d.doc_id INNER JOIN Institutions as i " +
                    "on idm.institution_id = i.inst_id INNER JOIN DoctorClasses as dc on idm.class_id = dc.doctor_classes_id where pd.cycle_day = '" + date + "'";
            Cursor cur2 = db.rawQuery(sql2, null);

            while (cur2.moveToNext()) {
                HashMap<String, String> map = new HashMap<>();
                map.put("plan_details_id", cur2.getString(cur2.getColumnIndex("plan_details_id")));
                map.put("cycle_day", cur2.getString(cur2.getColumnIndex("cycle_day")));
                map.put("cycle_label", cur2.getString(cur2.getColumnIndex("label")));
                map.put("doc_id", cur2.getString(cur2.getColumnIndex("doc_id")));
                map.put("doc_name", cur2.getString(cur2.getColumnIndex("doc_name")));
                map.put("contact_number", cur2.getString(cur2.getColumnIndex("contact_number")));
                map.put("class_code", cur2.getString(cur2.getColumnIndex("max_visit")));
                map.put("class_name", cur2.getString(cur2.getColumnIndex("name")));
                map.put("inst_name", cur2.getString(cur2.getColumnIndex("inst_name")));
                map.put("inst_doc_id", cur2.getString(cur2.getColumnIndex("inst_doc_id")));
                array_day.add(map);
            }

            map_day.put(date, array_day);
            array.add(map_day);

            cur2.close();
        }

        cur.close();
        db.close();

        return array;
    }

    public HashMap<String, String> getCallDetails(int plandetails_id, int temp_plandetails_id) {
        String sql = null;
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        HashMap<String, String> map = new HashMap<>();

        if (plandetails_id > 0) {
            sql = "SELECT * FROM PlanDetails as pd INNER JOIN InstitutionDoctorMaps as idm ON pd.inst_doc_id = idm.IDM_ID INNER JOIN Doctors as d " +
                    "ON idm.doctor_id = d.doc_id INNER JOIN DoctorClasses as dc ON idm.class_id = dc.doctor_classes_id WHERE pd.plan_details_id = " + plandetails_id;
        } else if (temp_plandetails_id > 0) {
            sql = "SELECT * FROM PlanDetails as pd INNER JOIN InstitutionDoctorMaps as idm ON pd.inst_doc_id = idm.IDM_ID INNER JOIN Doctors as d " +
                    "ON idm.doctor_id = d.doc_id INNER JOIN DoctorClasses as dc ON idm.class_id = dc.doctor_classes_id WHERE pd.id = " + temp_plandetails_id;
        }

        Cursor cur = db.rawQuery(sql, null);

        if (cur.moveToNext()) {
            map.put("doc_name", cur.getString(cur.getColumnIndex("doc_name")));
            map.put("max_visit", cur.getString(cur.getColumnIndex("max_visit")));
        }

        cur.close();
        db.close();

        return map;
    }

    //INSERT METHODS
    public boolean insertPlanDetails(long plan_id, ArrayList<HashMap<String, ArrayList<HashMap<String, String>>>> details) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        long id = 0;

        for (int x = 0; x < details.size(); x++) {
            HashMap<String, ArrayList<HashMap<String, String>>> per_day = details.get(x);
            String date = per_day.keySet().toString().replace("[", "").replace("]", "");
            ArrayList<HashMap<String, String>> calls_per_day = per_day.get(date);

            for (int y = 0; y < calls_per_day.size(); y++) {
                ContentValues val = new ContentValues();
                val.put(PLAN_ID_FK, plan_id);
                val.put(PlanDetails_ID, 0);
                val.put(CYCLE_DAY, date);
                val.put(INST_DOC_ID_FK, calls_per_day.get(y).get("IDM_id"));
                val.put(CREATED_AT, helpers.getCurrentDate("timestamp"));

                id = db.insert(TBL_PlanDetails, null, val);
            }
        }

        db.close();

        return id > 0;
    }

    public int insertIncidentalCall(HashMap<String, String> map) {
        String date = helpers.getCurrentDate("");
        int cycleMonth = helpers.convertDateToCycleMonth(date);
        int planID = pc.getPlanID(cycleMonth);
        int rowID = 0;

        if (planID > 0) {
            SQLiteDatabase db = dbHelper.getWritableDatabase();

            ContentValues val = new ContentValues();
            val.put(INST_DOC_ID_FK, map.get("IDM_id"));
            val.put(PlanDetails_ID, map.get("plan_details_id"));
            val.put(CYCLE_DAY, date);
            val.put(CREATED_AT, helpers.getCurrentDate("timestamp"));
            val.put(PLAN_ID_FK, planID);

            rowID = (int) db.insert(TBL_PlanDetails, null, val);

            db.close();
        } else if (planID == -1)
            rowID = -1;

        return rowID;
    }
}