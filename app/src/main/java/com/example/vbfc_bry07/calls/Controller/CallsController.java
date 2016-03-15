package com.example.vbfc_bry07.calls.Controller;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.HashMap;

public class CallsController extends DbHelper {

    DbHelper dbHelper;

    static String TBL_Calls = "Calls",
            Calls_ID = "calls_id",
            PLANDETAILS_ID = "plan_details_id",
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

    public static final String CREATE_Calls = String.format("CREATE TABLE %s (%s INTEGER PRIMARY KEY AUTOINCREMENT, %s INTEGER, %s INTEGER, %s INTEGER, %s INTEGER, %s TEXT, %s TEXT, %s DOUBLE, %s DOUBLE, %s INTEGER, %s TEXT, %s LONG, %s INTEGER, %s INTEGER, %s TEXT, %s TEXT, %s TEXT)",
            TBL_Calls, AI_ID, Calls_ID, PLANDETAILS_ID, STATUS_ID_FK, MAKEUP, START_DATETIME, END_DATETIME, LATITUDE, LONGITUDE, RESCHEDULE_DATE, SIGNED_DAY_ID, RETRY_COUNT, JOINT_CALL, QUICK_SIGN, CREATED_AT, UPDATED_AT, DELETED_AT);

    public CallsController(Context context) {
        super(context);
        dbHelper = new DbHelper(context);
    }

    //GET METHODS
    public float fetchPlannedCalls(String cycle_month, String cycle_year) {
        String sql = "Select strftime('%m', start_datetime) as cycle_month, strftime('%Y', start_datetime) as cycle_year, count(*) as planned_calls FROM Calls " +
                "where cycle_month = " + cycle_month + " and cycle_year = " + cycle_year + " " +
                "group by cycle_month, cycle_year";
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

    public float IncidentalCalls(String cycle_month, String cycle_year) {
        String sql = "Select strftime('%m', start_datetime) as cycle_month, strftime('%Y', start_datetime) as cycle_year, count(*) as planned_calls FROM Calls " +
                "where planned = 0 and cycle_month = " + cycle_month + " and cycle_year = " + cycle_year + " " +
                "group by cycle_month, cycle_year";
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cur = db.rawQuery(sql, null);
        float planned_calls = 0;

        while (cur.moveToNext()) {
            planned_calls = Float.parseFloat(cur.getString(cur.getColumnIndex("planned_calls")));
        }

        cur.close();
        db.close();

        return planned_calls;
    }

    public float RecoveredCalls(String cycle_month, String cycle_year) {
        String sql = "Select strftime('%m', start_datetime) as cycle_month, strftime('%Y', start_datetime) as cycle_year, count(*) as recovered_calls FROM Calls " +
                "where makeup = 0 and reschedule_date > 0 and cycle_month = " + cycle_month + " and cycle_year = " + cycle_year + " " +
                "group by cycle_month, cycle_year";
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
        String sql = "SELECT C.id, C.calls_id, C.start_datetime, C.end_datetime, count(*) as unprocessed_calls, " +
                "strftime('%m', C.cycle_day) as cycle_month, strftime('%Y', C.cycle_day) as cycle_year " +
                "FROM Calls as C inner join MissedCalls as MC on MC.call_id_fk != C.calls_id where C.start_datetime = '' and C.end_datetime = '' " +
                "and cycle_month = " + cycle_month + " and cycle_year = " + cycle_year + " group by C.cycle_day";
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
        String sql = "SELECT count(MC.id) as missed_calls, MC.call_id_fk, C.cycle_day, strftime('%m', C.cycle_day) as cycle_month, strftime('%Y', C.cycle_day) " +
                "as cycle_year FROM MissedCalls MC inner join Calls C on MC.call_id_fk = C.calls_id where cycle_month = " + cycle_month + " and cycle_year = " + cycle_year;
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

    //SAVE METHODS
//    public boolean saveDoctorCall(HashMap<String, String> data) {
//        SQLiteDatabase db = dbHelper.getWritableDatabase();
//
//        ContentValues val = new ContentValues();
//        val.put(PLANDETAILS_ID, data.get("calls_plan_details_id"));
//        val.put(START_DATETIME, data.get("start_time"));
//        val.put(END_DATETIME, data.get("calls_end"));
//    }
}
