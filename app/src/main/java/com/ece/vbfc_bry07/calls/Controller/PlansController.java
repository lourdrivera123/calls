package com.ece.vbfc_bry07.calls.controller;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.ece.vbfc_bry07.calls.Helpers;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class PlansController extends DbHelper {
    DbHelper dbhelper;
    Helpers helpers;

    static String TBL_PLANS = "Plans",
            PLANS_ID = "plans_id",
            PLANS_CYCLE_SET = "cycle_set_id",
            PLANS_CYCLE_NUMBER = "cycle_number",
            PLANS_STATUS = "status",
            PLANS_STATUS_DATE = "status_date";

    public PlansController(Context context) {
        super(context);
        dbhelper = new DbHelper(context);
        helpers = new Helpers();
    }

    //////////////////////////////CHECK METHODS///////////////////////////
    public int checkIfHasPlan(int month) {
        SQLiteDatabase db = dbhelper.getWritableDatabase();
        String sql = "SELECT * FROM " + TBL_PLANS + " WHERE " + PLANS_CYCLE_NUMBER + " = " + month;
        Cursor cur = db.rawQuery(sql, null);
        int plan_id = 0;

        if (cur.moveToNext())
            plan_id = cur.getInt(cur.getColumnIndex(AI_ID));

        cur.close();
        db.close();

        return plan_id;
    }

    public int checkIfPlanIsApproved(int cycle_number) {
        String sql = "SELECT * FROM Plans WHERE cycle_number = " + cycle_number;
        SQLiteDatabase db = dbhelper.getWritableDatabase();
        Cursor cur = db.rawQuery(sql, null);
        int status_id = 0;

        if (cur.moveToNext())
            status_id = cur.getInt(cur.getColumnIndex(PLANS_STATUS));

        cur.close();
        db.close();

        return status_id;
    }

    public HashMap<String, String> checkForDisapprovedPlans() {
        String sql = "SELECT * FROM Plans WHERE status = 2";
        SQLiteDatabase db = dbhelper.getWritableDatabase();
        Cursor cur = db.rawQuery(sql, null);
        HashMap<String, String> map = new HashMap<>();
        map.put("cycle_number", "");
        map.put("date", "");

        if (cur.moveToNext()) {
            map.put("cycle_number", cur.getString(cur.getColumnIndex(PLANS_CYCLE_NUMBER)));
            map.put("date", helpers.convertToAlphabetDate(cur.getString(cur.getColumnIndex("updated_at")), ""));
        }

        cur.close();
        db.close();

        return map;
    }

    ///////////////////////////////////////////GET METHODS
    public ArrayList<HashMap<String, String>> getAllPlans() {
        String sql = "SELECT * FROM Plans as p INNER JOIN CycleSets as cs ON p.cycle_set_id = cs.cycle_sets_id WHERE status = 1";
        SQLiteDatabase db = dbhelper.getWritableDatabase();
        Cursor cur = db.rawQuery(sql, null);
        ArrayList<HashMap<String, String>> array = new ArrayList<>();

        while (cur.moveToNext()) {
            HashMap<String, String> map = new HashMap<>();
            String month = helpers.convertIntToStringMonth(cur.getInt(cur.getColumnIndex(PLANS_CYCLE_NUMBER)));
            int year = cur.getInt(cur.getColumnIndex("year"));
            map.put("name", month + " " + year);
            map.put("cycle_number", cur.getString(cur.getColumnIndex(PLANS_CYCLE_NUMBER)));

            array.add(map);
        }

        db.close();
        cur.close();

        return array;
    }

    public int getPlanID(int cycleMonth, String from) {
        String sql = "SELECT * FROM Plans WHERE cycle_number = " + cycleMonth;
        SQLiteDatabase db = dbhelper.getWritableDatabase();
        Cursor cur = db.rawQuery(sql, null);
        int planID = 0;

        if (cur.moveToNext()) {
            planID = cur.getInt(cur.getColumnIndex(AI_ID));

            if (from.equals("PlanDetails")) {
                if (cur.getInt(cur.getColumnIndex(PLANS_STATUS)) == 0)
                    planID = -1;
            }
        }

        cur.close();
        db.close();

        return planID;
    }

    ///////////////////////////////INSERT METHODS/////////////////////////
    public long insertPlans(int year, int month) {
        ContentValues val = new ContentValues();
        SQLiteDatabase db = getWritableDatabase();
        String sql = "SELECT * FROM CycleSets WHERE year = " + year;
        Cursor cur = db.rawQuery(sql, null);
        int cycle_set_id = 0;
        long id;

        if (cur.moveToNext())
            cycle_set_id = cur.getInt(cur.getColumnIndex("cycle_sets_id"));

        String check_sql = "SELECT * FROM " + TBL_PLANS + " WHERE " + PLANS_CYCLE_NUMBER + " = " + month + " AND " + PLANS_CYCLE_SET + " = " + cycle_set_id;
        Cursor cur1 = db.rawQuery(check_sql, null);

        if (cur1.moveToNext()) {
            return -1;
        } else {
            Date datenow = new Date();
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());

            val.put(PLANS_CYCLE_SET, cycle_set_id);
            val.put(PLANS_CYCLE_NUMBER, month);
            val.put(PLANS_STATUS, 0);
            val.put(PLANS_STATUS_DATE, formatter.format(datenow));

            id = db.insert(TBL_PLANS, null, val);
        }

        cur.close();
        cur1.close();
        db.close();

        return id;
    }

    /////////////////////UPDATE METHODS
    public boolean updatePlanStatus(long plan_id) {
        SQLiteDatabase db = dbhelper.getWritableDatabase();
        ContentValues val = new ContentValues();

        val.put(PLANS_STATUS, 0);

        long id = db.update(TBL_PLANS, val, PLANS_ID + " = " + plan_id, null);

        return id > 0;
    }
}
