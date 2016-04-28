package com.ece.vbfc_bry07.calls.Controller;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.ece.vbfc_bry07.calls.Helpers;

import java.math.BigDecimal;
import java.math.RoundingMode;
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
        String sql = "SELECT  c.id as call_id, * FROM Plans as p INNER JOIN PlanDetails as pd ON p.id = pd.plan_id LEFT JOIN Calls as c ON pd.plan_details_id = c.plan_details_id " +
                "WHERE pd.plan_details_id > 0 OR c.temp_planDetails_id = pd.id AND p.cycle_number = " + cycle_month;
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cur = db.rawQuery(sql, null);
        int total = cur.getCount();
        int calls = 0;

        while (cur.moveToNext()) {
            if (cur.getString(cur.getColumnIndex("call_id")) != null)
                calls += 1;
        }

        float percentage = calls * 100f / total;

        String callRate = new BigDecimal(percentage).setScale(2, RoundingMode.HALF_UP) + "% \n (" + calls + " / " + total + ")";

        cur.close();
        db.close();

        return callRate;
    }

    public int fetchPlannedCalls(int cycle_month) {
        String sql = "SELECT * FROM PlanDetails as pd INNER JOIN Plans as p ON pd.plan_id = p.id WHERE p.cycle_number = " + cycle_month + " AND plan_details_id > 0";
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cur = db.rawQuery(sql, null);

        int planned_calls = cur.getCount();

        cur.close();
        db.close();

        return planned_calls;
    }

    public int IncidentalCalls(int cycle_month) {
        String sql = "SELECT COUNT(c.id) as incidental_calls FROM Plans as p INNER JOIN PlanDetails as pd ON p.id = pd.plan_id INNER JOIN Calls as c " +
                "ON pd.id = c.temp_planDetails_id WHERE cycle_number = " + cycle_month + " AND c.status_id = 2 AND c.makeup = 0";
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cur = db.rawQuery(sql, null);
        int incidental_calls = 0;

        while (cur.moveToNext())
            incidental_calls = cur.getInt(cur.getColumnIndex("incidental_calls"));

        cur.close();
        db.close();

        return incidental_calls;
    }

    public int RecoveredCalls(int cycle_month) {
        String sql = "SELECT * FROM RescheduledCalls as rc INNER JOIN Calls as c ON rc.call_id = c.id INNER JOIN PlanDetails as pd ON c.plan_details_id = pd.plan_details_id " +
                "INNER JOIN Plans as p ON pd.plan_id = p.id WHERE p.cycle_number = " + cycle_month;
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cur = db.rawQuery(sql, null);

        int recovered_calls = cur.getCount();

        cur.close();
        db.close();

        return recovered_calls;
    }

    public int ActualCoveredCalls(String cycle_month) {
        String sql = "SELECT * FROM Calls as c INNER JOIN PlanDetails as pd ON c.plan_details_id = pd.plan_details_id INNER JOIN Plans as p " +
                "on pd.plan_id = p.id WHERE p.cycle_number = " + cycle_month + "  AND c.status_id = 1 GROUP BY c.id";
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cur = db.rawQuery(sql, null);

        int covered_calls = cur.getCount();

        cur.close();
        db.close();

        return covered_calls;
    }

    public int DeclaredMissedCalls(String cycle_month) {
        String sql = "Select count(MC.id) as missed_calls,  strftime('%m', PD.cycle_day) as cycle_month from MissedCalls MC  left join Calls C on MC.call_id_fk = C.calls_id " +
                "left join PlanDetails PD on C.plan_details_id = PD.plan_id where cycle_month = " + cycle_month;
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cur = db.rawQuery(sql, null);
        int missed_calls = 0;

        while (cur.moveToNext())
            missed_calls = cur.getInt(cur.getColumnIndex("missed_calls"));

        cur.close();
        db.close();

        return missed_calls;
    }

    public HashMap<String, String> getLastVisited(int plandetails_id) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String last_visited;
        HashMap<String, String> map = new HashMap<>();
        map.put("last_visited", "");
        map.put("count_visits", "");

        String sql = "SELECT c.created_at FROM PlanDetails as pd INNER JOIN Calls as c ON pd.plan_details_id = c.plan_details_id WHERE pd.plan_details_id = " + plandetails_id + " AND c.temp_planDetails_id = 0";

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
            sql = "SELECT * FROM Calls as c WHERE plan_details_id = " + id;
        else if (type.equals("temp_planDetails"))
            sql = "SELECT * FROM calls as c WHERE temp_planDetails_id = " + id;

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cur = db.rawQuery(sql, null);

        if (cur.moveToNext()) {
            if (cur.getString(cur.getColumnIndex("calls_id")) == null)
                check = 2; //signed but not sync
            else {
                check = 1; //sync

                if (cur.getString(cur.getColumnIndex("status_id")).equals("2") && cur.getString(cur.getColumnIndex("makeup")).equals("1"))
                    check = 3; //missed call recovered - signed and sync
            }
        }

        cur.close();
        db.close();

        return check;
    }
}
