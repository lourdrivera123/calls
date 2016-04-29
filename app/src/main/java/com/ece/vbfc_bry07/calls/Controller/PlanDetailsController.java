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

    public ArrayList<HashMap<String, ArrayList<String>>> getPlanDetailsByPID(int plan_ID) {
        String sql1 = "SELECT DISTINCT inst_doc_id FROM PlanDetails as pd INNER JOIN Plans as p ON pd.plan_id = p.id WHERE plan_id = " + plan_ID;
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cur = db.rawQuery(sql1, null);
        ArrayList<HashMap<String, ArrayList<String>>> array = new ArrayList<>();

        while (cur.moveToNext()) {
            String sql2 = "SELECT cycle_day FROM PlanDetails as pd INNER JOIN InstitutionDoctorMaps as idm on pd.inst_doc_id = idm.IDM_ID " +
                    "INNER JOIN Doctors as d on idm.doctor_id = d.doc_id INNER JOIN Institutions as i on idm.institution_id = i.inst_id " +
                    "INNER JOIN DoctorClasses as dc on idm.class_id = dc.doctor_classes_id where IDM_id = " + cur.getInt(cur.getColumnIndex("inst_doc_id"));
            Cursor cur2 = db.rawQuery(sql2, null);
            ArrayList<String> date = new ArrayList<>();
            HashMap<String, ArrayList<String>> map = new HashMap<>();

            while (cur2.moveToNext()) {
                date.add(cur2.getString(cur2.getColumnIndex("cycle_day")));
            }

            map.put(cur.getString(cur.getColumnIndex("inst_doc_id")), date);
            array.add(map);

            cur2.close();
        }

        cur.close();
        db.close();

        return array;
    }

    public HashMap<String, String> getCallDetails(int IDM_id) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        HashMap<String, String> map = new HashMap<>();
        String sql = "SELECT * FROM InstitutionDoctorMaps as idm INNER JOIN Doctors as d ON idm.doctor_id = d.doc_id INNER JOIN DoctorClasses as dc " +
                "ON idm.class_id = dc.doctor_classes_id WHERE idm.IDM_id = " + IDM_id;

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
            String sql2 = "SELECT c.id as call_id_final, * FROM PlanDetails as pd LEFT JOIN Calls as c ON pd.plan_details_id = c.plan_details_id " +
                    "LEFT JOIN RescheduledCalls as rc ON c.id = rc.call_id WHERE pd.cycle_day = '" + date + "' OR rc.cycle_day = '" + date + "' GROUP BY pd.id";
            Cursor cur2 = db.rawQuery(sql2, null);
            HashMap<String, String> map = new HashMap<>();
            map.put("date", date);
            int divisor = 0;
            int dividend = 0;


            while (cur2.moveToNext()) {
                divisor += 1;

                if (cur2.getString(cur2.getColumnIndex("call_id_final")) != null)
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

    public ArrayList<HashMap<String, String>> getMonthlyHistoryByIDM_id(int IDM_id, int cycle_month) {
        String sql = "SELECT c.calls_id as server_id, c.status_id as call_status,  pd.plan_details_id as pd_id, * FROM PlanDetails as pd LEFT JOIN Calls as c ON c.plan_details_id = pd.plan_details_id " +
                "WHERE pd.inst_doc_id = " + IDM_id + " AND plan_id = (SELECT id FROM plans WHERE cycle_number = " + cycle_month + ") AND pd.plan_details_id > 0";
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cur = db.rawQuery(sql, null);
        Cursor cur1 = null;
        ArrayList<HashMap<String, String>> array = new ArrayList<>();

        if (cur.getCount() > 0) {
            while (cur.moveToNext()) {
                String day = cur.getString(cur.getColumnIndex("cycle_day"));
                String sql1 = "SELECT c.calls_id as server_id, c.status_id as call_status, * FROM Calls as c INNER JOIN PlanDetails as pd ON c.temp_planDetails_id = pd.id " +
                        "WHERE pd.inst_doc_id = " + IDM_id + " AND reschedule_date = '" + day + "'";
                cur1 = db.rawQuery(sql1, null);
                String call_status, server_id;
                HashMap<String, String> map = new HashMap<>();
                map.put("date", day);

                if (cur1.moveToNext()) {
                    call_status = cur1.getString(cur1.getColumnIndex("call_status")) == null ? "" : cur1.getString(cur1.getColumnIndex("call_status"));
                    server_id = cur1.getString(cur1.getColumnIndex("server_id")) == null ? "" : cur1.getString(cur1.getColumnIndex("server_id"));
                } else {
                    call_status = cur.getString(cur.getColumnIndex("call_status")) == null ? "" : cur.getString(cur.getColumnIndex("call_status"));
                    server_id = cur.getString(cur.getColumnIndex("server_id")) == null ? "" : cur.getString(cur.getColumnIndex("server_id"));
                }

                map.put("status", call_status);
                map.put("server_id", server_id);
                map.put("plan_details_id", cur.getString(cur.getColumnIndex("pd_id")));
                array.add(map);
            }
        } else { //FOR INCIDENTAL CALLS
            String sql2 = "SELECT c.calls_id as server_id, c.status_id as call_status, * FROM PlanDetails as pd LEFT JOIN Calls as c ON c.plan_details_id = pd.plan_details_id " +
                    "WHERE pd.inst_doc_id = " + IDM_id + " AND plan_id = (SELECT id FROM plans WHERE cycle_number = " + cycle_month + ") AND pd.plan_details_id == 0 GROUP BY pd.id";
            Cursor cur2 = db.rawQuery(sql2, null);

            while (cur2.moveToNext()) {
                String status_id = cur2.getString(cur2.getColumnIndex("call_status")) == null ? "" : cur2.getString(cur2.getColumnIndex("call_status"));
                String server_id = cur2.getString(cur2.getColumnIndex("server_id")) == null ? "" : cur2.getString(cur2.getColumnIndex("server_id"));
                String makeup = cur2.getString(cur2.getColumnIndex("makeup"));
                String call_status = "";

                if (status_id.equals("2") && makeup.equals("1"))
                    call_status = "2";
                else if (status_id.equals("2") && makeup.equals("0"))
                    call_status = "3";

                HashMap<String, String> map = new HashMap<>();
                map.put("date", cur2.getString(cur2.getColumnIndex("cycle_day")));
                map.put("status", call_status);
                map.put("server_id", server_id);
                array.add(map);
            }

            cur2.close();
        }

        if (cur1 != null)
            cur1.close();

        cur.close();
        db.close();

        return array;
    }

    /////////////////////////////INSERT METHODS
    public boolean insertPlanDetails(long plan_id, ArrayList<HashMap<String, ArrayList<String>>> planDetails) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        long id = 0;

        for (int x = 0; x < planDetails.size(); x++) {
            HashMap<String, ArrayList<String>> IDM_id_with_date = planDetails.get(x);
            String IDM_id = IDM_id_with_date.keySet().toString().replace("[", "").replace("]", "");
            ArrayList<String> date = IDM_id_with_date.get(IDM_id);

            for (int y = 0; y < date.size(); y++) {
                ContentValues val = new ContentValues();
                val.put(PLAN_ID_FK, plan_id);
                val.put(PlanDetails_ID, 0);
                val.put(CYCLE_DAY, date.get(y));
                val.put(INST_DOC_ID_FK, IDM_id);
                val.put(CREATED_AT, helpers.getCurrentDate("timestamp"));

                id = db.insert(TBL_PlanDetails, null, val);
            }
        }

        db.close();

        return id > 0;
    }

    public long insertAdditionalCall(HashMap<String, String> map) {
        int planID = pc.getPlanID(helpers.convertDateToCycleMonth(helpers.getCurrentDate("date")), "");
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues val = new ContentValues();

        val.put(PlanDetails_ID, 0);
        val.put(PLAN_ID_FK, planID);
        val.put(INST_DOC_ID_FK, map.get("IDM_id"));
        val.put(CYCLE_DAY, helpers.getCurrentDate("date"));
        val.put(CREATED_AT, helpers.getCurrentDate("timestamp"));

        long temp_planDetails_id = db.insert(TBL_PlanDetails, null, val);

        db.close();

        return temp_planDetails_id;
    }

    ///////////////////////////////CHECK METHODS
    public int checkPlanDetails(int cycleMonth) {
        int planID = pc.getPlanID(cycleMonth, "PlanDetails");
        int rowID = 0;

        if (planID > 0) {
            rowID = 1;
        } else if (planID == -1)
            rowID = -1;

        return rowID;
    }
}