package com.ece.vbfc_bry07.calls.Controller;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.ece.vbfc_bry07.calls.Helpers;

import java.util.ArrayList;
import java.util.HashMap;

public class PlanDetailsController extends DbHelper {
    DbHelper dbHelper;
    PlansController pc;
    Helpers helpers;

    static String TBL_PlanDetails = "PlanDetails",
            PlanDetails_ID = "plan_details_id",
            PLAN_ID_FK = "plan_id",
            STATUS = "status",
            INST_DOC_ID_FK = "inst_doc_id",
            CYCLE_DAY = "cycle_day",
            CYCLE_DAY_LABEL = "label";

    public static final String CREATE_PlanDetails = String.format("CREATE TABLE %s (%s INTEGER PRIMARY KEY AUTOINCREMENT, %s INTEGER, %s INTEGER, %s INTEGER, %s INTEGER, %s TEXT, %s TEXT, %s TEXT, %s TEXT, %s TEXT)",
            TBL_PlanDetails, AI_ID, PlanDetails_ID, PLAN_ID_FK, STATUS, INST_DOC_ID_FK, CYCLE_DAY, CYCLE_DAY_LABEL, CREATED_AT, UPDATED_AT, DELETED_AT);

    public PlanDetailsController(Context context) {
        super(context);
        dbHelper = new DbHelper(context);
        helpers = new Helpers();
        pc = new PlansController(context);
    }

    ///////////////////////////////////////GET METHODS
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

    public ArrayList<HashMap<String, String>> getMonthDetails(int cycle_month) {
        String sql = "SELECT DISTINCT pd.cycle_day FROM Plans as p INNER JOIN PlanDetails as pd ON p.id = pd.plan_id WHERE p.cycle_number = " + cycle_month;
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cur = db.rawQuery(sql, null);
        ArrayList<HashMap<String, String>> array = new ArrayList<>();

        while (cur.moveToNext()) {
            String date = cur.getString(cur.getColumnIndex("cycle_day"));
            String sql2 = "SELECT c.id as call_id, * FROM PlanDetails as pd LEFT JOIN Calls as c ON pd.plan_details_id = c.plan_details_id WHERE pd.cycle_day = '" + date +
                    "' GROUP BY pd.id";
            Cursor cur2 = db.rawQuery(sql2, null);
            HashMap<String, String> map = new HashMap<>();
            map.put("date", date);
            int divisor = 0;
            int dividend = 0;

            while (cur2.moveToNext()) {
                divisor += 1;

                if (cur2.getString(cur2.getColumnIndex("call_id")) != null)
                    dividend += 1;
            }

            map.put("divisor", String.valueOf(divisor));
            map.put("dividend", String.valueOf(dividend));

            array.add(map);
            cur2.close();
        }

        db.close();
        cur.close();

        return array;
    }

    /////////////////////////////INSERT METHODS
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
                val.put(STATUS, 0);
                val.put(INST_DOC_ID_FK, calls_per_day.get(y).get("IDM_id"));
                val.put(CREATED_AT, helpers.getCurrentDate("timestamp"));

                id = db.insert(TBL_PlanDetails, null, val);
            }
        }

        db.close();

        return id > 0;
    }

    public int insertAdditionalCall(HashMap<String, String> map, int status_id) {
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
            val.put(STATUS, status_id);

            rowID = (int) db.insert(TBL_PlanDetails, null, val);

            db.close();
        } else if (planID == -1)
            rowID = -1;

        return rowID;
    }

    ///////////////////////////////CHECK METHODS
    public boolean IsDoctorExceedsMonthlyCalls(int inst_doc_id) {
        String sql = "SELECT COUNT(pd.id) as count, dc.max_visit FROM PlanDetails as pd INNER JOIN InstitutionDoctorMaps as idm ON " +
                "pd.inst_doc_id = idm.IDM_ID INNER JOIN DoctorClasses as dc ON idm.class_id = dc.doctor_classes_id WHERE inst_doc_id = " + inst_doc_id;
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cur = db.rawQuery(sql, null);
        boolean flag = false;

        if (cur.moveToNext()) {
            int actual_count = cur.getInt(cur.getColumnIndex("count"));
            int max_visit = cur.getString(cur.getColumnIndex("max_visit")) == null ? 0 : cur.getInt(cur.getColumnIndex("max_visit"));

            if (max_visit > 0) {
                if (actual_count >= max_visit)
                    flag = true;
            }
        }

        cur.close();
        db.close();

        return flag;
    }

    public String checkForMissedCall(int IDM_id, int cycle_month, String date_now) {
        String sql = "SELECT c.id as call_id, * FROM PlanDetails as pd INNER JOIN Plans as p ON pd.plan_id = p.id LEFT JOIN Calls as c " +
                "ON pd.plan_details_id = c.plan_details_id WHERE pd.inst_doc_id = " + IDM_id + " AND p.cycle_number = " + cycle_month + " AND pd.cycle_day < '" + date_now + "' ORDER BY pd.cycle_day ASC";
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cur = db.rawQuery(sql, null);
        String missedcall_date = "";

        if (cur.moveToNext()) {
            if (cur.getString(cur.getColumnIndex("call_id")) == null)
                missedcall_date = cur.getString(cur.getColumnIndex("cycle_day"));
        }

        cur.close();
        db.close();

        return missedcall_date;
    }

    public int checkTypeOfAdditionalCall(int temp_planDetails_id) {
        String sql = "SELECT * FROM PlanDetails as pd WHERE id = " + temp_planDetails_id;
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cur = db.rawQuery(sql, null);
        int status_id = 1;

        if (cur.moveToNext())
            status_id = cur.getInt(cur.getColumnIndex(STATUS));

        cur.close();

        db.close();
        return status_id;
    }
}