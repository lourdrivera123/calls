package com.ece.vbfc_bry07.calls.controller;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.ece.vbfc_bry07.calls.Helpers;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;

public class CallsController extends DbHelper {
    Helpers helpers;
    DbHelper dbHelper;

    static String TBL_Calls = "Calls",
            Calls_ID = "calls_id",
            PLANDETAILS_ID = "plan_details_id",
            TEMP_PLANDETAILS_ID = "temp_planDetails_id",
            STATUS_ID_FK = "status_id",
            MAKEUP = "makeup",
            START_DATETIME = "start_datetime",
            END_DATETIME = "end_datetime",
            LATITUDE = "latitude",
            LONGITUDE = "longtitude",
            RESCHEDULE_DATE = "reschedule_date",
            SIGNED_DAY_ID = "signed_day",
            RETRY_COUNT = "retry_count",
            JOINT_CALL = "joint_call",
            QUICK_SIGN = "quick_sign";

    public static final String CREATE_Calls = String.format("CREATE TABLE %s (%s INTEGER PRIMARY KEY AUTOINCREMENT, %s INTEGER, %s INTEGER, %s INTEGER, %s INTEGER, %s INTEGER, %s TEXT, %s TEXT, %s DOUBLE, %s DOUBLE, %s TEXT, %s TEXT, %s LONG, %s INTEGER, %s INTEGER, %s TEXT, %s TEXT, %s TEXT)",
            TBL_Calls, AI_ID, Calls_ID, PLANDETAILS_ID, TEMP_PLANDETAILS_ID, STATUS_ID_FK, MAKEUP, START_DATETIME, END_DATETIME, LATITUDE, LONGITUDE, RESCHEDULE_DATE, SIGNED_DAY_ID, RETRY_COUNT, JOINT_CALL, QUICK_SIGN, CREATED_AT, UPDATED_AT, DELETED_AT);

    public CallsController(Context context) {
        super(context);
        dbHelper = new DbHelper(context);
        helpers = new Helpers();
    }

    ///////////////////////////////////GET METHODS
    public String callRate(int cycle_month) {
        String sql = "SELECT  c._id as call_id, * FROM Plans as p INNER JOIN PlanDetails as pd ON p._id = pd.plan_id LEFT JOIN Calls as c ON pd.plan_details_id = c.plan_details_id " +
                "WHERE (pd.plan_details_id > 0 OR c.temp_planDetails_id = pd._id) AND p.cycle_number = " + cycle_month;
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cur = db.rawQuery(sql, null);
        int total = cur.getCount(), calls = 0;
        float percentage = 0;

        if (cur.getCount() > 0) {
            while (cur.moveToNext()) {
                if (cur.getString(cur.getColumnIndex("call_id")) != null)
                    calls += 1;
            }

            percentage = calls * 100f / total;
        }

        String callRate = new BigDecimal(percentage).setScale(2, RoundingMode.HALF_UP) + "% (" + calls + "/" + total + ")";
        cur.close();
        db.close();

        return callRate;
    }

    public String callReach(int cycle_month) {
        String sql = "SELECT COUNT(c._id) as count_call_id, * FROM PlanDetails as pd INNER JOIN Plans as p ON pd.plan_id = p._id " +
                "INNER JOIN InstitutionDoctorMaps as idm ON pd.inst_doc_id = idm.IDM_ID INNER JOIN DoctorClasses as dc ON idm.class_id = dc.doctor_classes_id " +
                "LEFT JOIN Calls as c ON pd.plan_details_id = c.plan_details_id WHERE p.cycle_number = " + cycle_month + " AND " +
                "(c.plan_details_id > 0 OR c.plan_details_id IS NULL OR c.temp_planDetails_id = pd._id) GROUP BY idm.IDM_id";
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cur = db.rawQuery(sql, null);
        int total_doctor = cur.getCount();
        int total = 0;
        float percentage = 0;

        if (cur.getCount() > 0) {
            while (cur.moveToNext()) {
                int count_call_id = cur.getInt(cur.getColumnIndex("count_call_id"));

                if (count_call_id >= 1)
                    total += 1;
            }

            percentage = total * 100f / total_doctor;
        }

        db.close();
        cur.close();

        return new BigDecimal(percentage).setScale(2, RoundingMode.HALF_UP) + "% (" + total + "/" + total_doctor + ")";
    }

    public int fetchPlannedCalls(int cycle_month) {
        String sql = "SELECT * FROM PlanDetails as pd INNER JOIN Plans as p ON pd.plan_id = p._id WHERE p.cycle_number = " + cycle_month + " AND plan_details_id > 0";
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cur = db.rawQuery(sql, null);

        int planned_calls = cur.getCount();

        cur.close();
        db.close();

        return planned_calls;
    }

    public HashMap<String, String> getLastVisited(int IDM_id, int cycle_month) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String last_visited;
        HashMap<String, String> map = new HashMap<>();
        map.put("last_visited", "");
        map.put("count_visits", "");

        String sql = "SELECT  c.created_at FROM Calls as c INNER JOIN PlanDetails as pd ON c.plan_details_id = pd.plan_details_id INNER JOIN Plans as p ON pd.plan_id = p._id " +
                "WHERE (c.plan_details_id > 0 OR c.temp_planDetails_id = pd._id) AND pd.inst_doc_id = " + IDM_id + " AND p.cycle_number = " + cycle_month +
                " GROUP BY c.created_at ORDER BY c.created_at DESC";

        Cursor cur = db.rawQuery(sql, null);

        if (cur.moveToNext()) {
            last_visited = helpers.convertToAlphabetDate(cur.getString(cur.getColumnIndex("created_at")), "complete");
            map.put("last_visited", last_visited);
            map.put("count_visits", String.valueOf(cur.getCount()));
        }

        cur.close();
        db.close();

        return map;
    }

    public ArrayList<HashMap<String, String>> getCallReportDetails(String type, int cycle_month) {
        String sql;
        SQLiteDatabase db = getWritableDatabase();
        ArrayList<HashMap<String, String>> array = new ArrayList<>();

        switch (type) {
            case "actual_covered_call":
                sql = "SELECT * FROM Calls as c INNER JOIN PlanDetails as pd ON c.plan_details_id = pd.plan_details_id INNER JOIN Plans as p ON pd.plan_id = p._id " +
                        "INNER JOIN InstitutionDoctorMaps as idm ON idm.IDM_ID = pd.inst_doc_id INNER JOIN Doctors as d ON d.doc_id = idm.doctor_id " +
                        "WHERE p.cycle_number = " + cycle_month + " AND c.status_id = 1 AND c.makeup = 0";
                break;
            case "declared_missed_call":
                sql = "SELECT * FROM MissedCalls as mc INNER JOIN PlanDetails as pd ON mc.plan_details_id = pd.plan_details_id INNER JOIN Plans as p ON pd.plan_id = p._id " +
                        "INNER JOIN InstitutionDoctorMaps as idm ON idm.IDM_ID = pd.inst_doc_id INNER JOIN Doctors as d ON idm.doctor_id = d.doc_id WHERE p.cycle_number = " + cycle_month;
                break;
            case "recovered_call":
                sql = "SELECT rc.cycle_day as rc_cycleday, * FROM RescheduledCalls as rc INNER JOIN Calls as c ON rc.call_id = c._id INNER JOIN PlanDetails as pd ON c.plan_details_id = pd.plan_details_id " +
                        "INNER JOIN Plans as p ON pd.plan_id = p._id INNER JOIN InstitutionDoctorMaps as idm ON pd.inst_doc_id = idm.IDM_ID " +
                        "INNER JOIN Doctors as d ON idm.doctor_id = d.doc_id WHERE p.cycle_number = " + cycle_month;
                break;
            default:
                sql = "SELECT * FROM Plans as p INNER JOIN PlanDetails as pd ON p._id = pd.plan_id INNER JOIN Calls as c ON pd._id = c.temp_planDetails_id " +
                        "INNER JOIN InstitutionDoctorMaps as idm ON pd.inst_doc_id = idm.IDM_ID INNER JOIN Doctors as d ON idm.doctor_id = d.doc_id " +
                        "WHERE cycle_number = " + cycle_month + " AND c.status_id = 2 AND c.makeup = 0";
                break;
        }

        Cursor cur = db.rawQuery(sql, null);

        while (cur.moveToNext()) {
            HashMap<String, String> map = new HashMap<>();
            String input;

            if (type.equals("recovered_call"))
                input = cur.getString(cur.getColumnIndex("doc_name")) + " (" + cur.getString(cur.getColumnIndex("rc_cycleday")) + ")";
            else
                input = cur.getString(cur.getColumnIndex("doc_name")) + " (" + cur.getString(cur.getColumnIndex("cycle_day")) + ")";

            map.put("doc_name", input);
            array.add(map);
        }

        db.close();
        cur.close();

        return array;
    }

    /////////////////////////////////////////////INSERT METHODS//////////////////////////////
    public long insertCall(HashMap<String, String> map) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues val = new ContentValues();
        val.put(MAKEUP, 0);
        val.put(RESCHEDULE_DATE, "");

        if (Integer.parseInt(map.get("calls_plan_details_id")) > 0) {
            val.put(TEMP_PLANDETAILS_ID, 0);
            val.put(PLANDETAILS_ID, map.get("calls_plan_details_id"));

            if (map.get("calls_status").equals("2")) {
                if (map.get("calls_makeup").equals("1"))
                    val.put(MAKEUP, 1);
            }
        } else if (Integer.parseInt(map.get("calls_temp_planDetails_id")) > 0) {
            val.put(TEMP_PLANDETAILS_ID, map.get("calls_temp_planDetails_id"));
            val.put(PLANDETAILS_ID, 0);
        }

        val.put(STATUS_ID_FK, map.get("calls_status"));
        val.put(START_DATETIME, map.get("start_time"));
        val.put(END_DATETIME, map.get("calls_end"));
        val.put(RETRY_COUNT, map.get("calls_retry_count"));
        val.put(JOINT_CALL, map.get("calls_joint_call"));
        val.put(QUICK_SIGN, 0);
        val.put(CREATED_AT, helpers.getCurrentDate("timestamp"));

        long rowID = db.insert(TBL_Calls, null, val);

        db.close();

        return rowID;
    }

    ///////////////////////////////////////CHECK METHODS////////////////////////////////
    public int hasCalled(int id, String type) {
        int check = 0;
        String sql = "";


        if (type.equals("planDetails"))
            sql = "SELECT mc._id as mc_id, * FROM PlanDetails as pd LEFT JOIN Calls as c ON pd.plan_details_id = c.plan_details_id " +
                    "LEFT JOIN MissedCalls as mc ON pd.plan_details_id = mc.plan_details_id WHERE pd.plan_details_id = " + id;
        else if (type.equals("temp_planDetails"))
            sql = "SELECT * FROM calls as c WHERE temp_planDetails_id = " + id;

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cur = db.rawQuery(sql, null);

        if (cur.moveToNext()) {
            if (cur.getString(cur.getColumnIndex("calls_id")) != null)
                check = 1; //signed and sync
            else {
                if (cur.getString(cur.getColumnIndex("status_id")) != null && cur.getString(cur.getColumnIndex("temp_planDetails_id")) != null) {
                    check = 2; //signed but not sync

                    if (cur.getString(cur.getColumnIndex("status_id")).equals("2") && cur.getInt(cur.getColumnIndex("temp_planDetails_id")) == 0)
                        check = 3; //recovered call (missed call or advanced call) - signed
                    if (cur.getInt(cur.getColumnIndex("temp_planDetails_id")) > 0 && cur.getString(cur.getColumnIndex("status_id")).equals("2"))
                        check = 4; //incidental call
                } else {
                    if (cur.getString(cur.getColumnIndex("mc_id")) != null)
                        check = 5; //declared missed call
                }
            }
        }

        cur.close();
        db.close();

        return check;
    }
}
