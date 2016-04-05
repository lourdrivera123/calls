package com.ece.vbfc_bry07.calls.Controller;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.ece.vbfc_bry07.calls.Helpers;

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
    public float fetchPlannedCalls(String cycle_month, String cycle_year) {
        String sql = "Select count(id) as planned_calls, " +
                "    strftime('%m', cycle_day) as cycle_month, " +
                "    strftime('%Y', cycle_day) as cycle_year " +
                "from PlanDetails " +
                "where cycle_month = " + cycle_month + " and cycle_year = " + cycle_year + " ";
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cur = db.rawQuery(sql, null);
        float planned_calls = 0;

        while (cur.moveToNext()) {
            planned_calls = cur.getFloat(cur.getColumnIndex("planned_calls"));
        }

        cur.close();
        db.close();

        return planned_calls;
    }

    public float IncidentalCalls(int cycle_month) {
        String sql = "SELECT COUNT(c.id) as incidental_calls FROM Plans as p INNER JOIN PlanDetails as pd ON p.plans_id = pd.plan_id INNER JOIN Calls as c " +
                "ON pd.id = c.temp_planDetails_id WHERE cycle_number = " + cycle_month + " AND c.status_id = 1";
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cur = db.rawQuery(sql, null);
        float incidental_calls = 0;

        while (cur.moveToNext()) {
            incidental_calls = Float.parseFloat(cur.getString(cur.getColumnIndex("incidental_calls")));
        }

        cur.close();
        db.close();

        return incidental_calls;
    }

    public float RecoveredCalls(String cycle_month, String cycle_year) {
        String sql = "Select count(C.id) as recovered_calls, " +
                "    strftime('%m', PD.cycle_day) as cycle_month, " +
                "    strftime('%Y', PD.cycle_day) as cycle_year " +
                "from Calls C " +
                "    left join PlanDetails PD on C.plan_details_id = PD.plan_id " +
                "    inner join MissedCalls MC on MC.call_id_fk != C.calls_id " +
                "where makeup = '0' " +
                "    and reschedule_date > 0 " +
                "    and cycle_month = " + cycle_month + " " +
                "    and cycle_year = " + cycle_year + " ";
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cur = db.rawQuery(sql, null);
        float recovered_calls = 0;

        while (cur.moveToNext()) {
            recovered_calls = Float.parseFloat(cur.getString(cur.getColumnIndex("recovered_calls")));
        }

        cur.close();
        db.close();

        return recovered_calls;
    }

    public float UnprocessedCalls(String cycle_month, String cycle_year) {
        String sql = "Select count(C.id) as unprocessed_calls, " +
                "    strftime('%m', PD.cycle_day) as cycle_month, " +
                "    strftime('%Y', PD.cycle_day) as cycle_year " +
                "from Calls C " +
                "    left join PlanDetails PD on C.plan_details_id = PD.plan_id " +
                "    inner join MissedCalls MC on MC.call_id_fk != C.calls_id " +
                "where start_datetime = '' " +
                "    and end_datetime = '' " +
                "    and cycle_month = " + cycle_month + " " +
                "    and cycle_year = " + cycle_year + " ";
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cur = db.rawQuery(sql, null);
        float unprocessed_calls = 0;

        while (cur.moveToNext()) {
            unprocessed_calls = Float.parseFloat(cur.getString(cur.getColumnIndex("unprocessed_calls")));
        }

        cur.close();
        db.close();

        return unprocessed_calls;
    }

    public float DeclaredMissedCalls(String cycle_month, String cycle_year) {
        String sql = "Select count(MC.id) as missed_calls, " +
                "    strftime('%m', PD.cycle_day) as cycle_month, " +
                "    strftime('%Y', PD.cycle_day) as cycle_year " +
                "from MissedCalls MC " +
                "    left join Calls C on MC.call_id_fk = C.calls_id " +
                "    left join PlanDetails PD on C.plan_details_id = PD.plan_id " +
                "where cycle_month = " + cycle_month + " " +
                "    and cycle_year = " + cycle_year + " ";
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cur = db.rawQuery(sql, null);
        float missed_calls = 0;

        while (cur.moveToNext()) {
            missed_calls = Float.parseFloat(cur.getString(cur.getColumnIndex("missed_calls")));
        }

        cur.close();
        db.close();

        return missed_calls;
    }

    public HashMap<String, String> getLastVisited(int plandetails_id, int temp_plandetails_id) {
        String sql = "";
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String last_visited;
        HashMap<String, String> map = new HashMap<>();
        map.put("last_visited", "");
        map.put("count_visits", "");

        if (plandetails_id > 0) {
            sql = "SELECT c.created_at FROM Calls as c INNER JOIN PlanDetails as pd ON c.plan_details_id = pd.plan_details_id WHERE pd.plan_id = " +
                    "(SELECT plan_id FROM PlanDetails WHERE plan_details_id = " + plandetails_id + ")";
        } else if (temp_plandetails_id > 0) {
            sql = "";
        }

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

    /////////////////////////////////////////////INSERT METHODS//////////////////////////////
    public long insertCall(HashMap<String, String> map) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues val = new ContentValues();

        val.put(PLANDETAILS_ID, map.get("calls_plan_details_id"));
        val.put(TEMP_PLANDETAILS_ID, map.get("calls_incidentals_pd_id"));
        val.put(STATUS_ID_FK, map.get("status_id"));
        val.put(MAKEUP, 0);
        val.put(START_DATETIME, map.get("start_time"));
        val.put(END_DATETIME, map.get("calls_end"));
        val.put(RESCHEDULE_DATE, "0");
        val.put(RETRY_COUNT, map.get("calls_retry_count"));
        val.put(JOINT_CALL, map.get("calls_joint_call"));
        val.put(QUICK_SIGN, 0);
        val.put(CREATED_AT, helpers.getCurrentDate("timestamp"));

        long rowID = db.insert(TBL_Calls, null, val);

        db.close();

        return rowID;
    }

    ///////////////////////////////////////CHECK METHODS////////////////////////////////
    public int hasCalled(int plan_details_ID, int temp_plandetails_id) {
        String sql = "";
        int check = 0;

        if (plan_details_ID > 0) {
            sql = "SELECT * FROM Calls as c WHERE plan_details_id = " + plan_details_ID;
        } else if (temp_plandetails_id > 0) {
            sql = "SELECT * FROM Calls as c  WHERE temp_planDetails_id = " + temp_plandetails_id;
        }

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cur = db.rawQuery(sql, null);

        if (cur.moveToNext()) {
            if (cur.getString(cur.getColumnIndex("calls_id")) == null)
                check = 2;
            else
                check = 1;
        }

        cur.close();
        db.close();

        return check;
    }
}
