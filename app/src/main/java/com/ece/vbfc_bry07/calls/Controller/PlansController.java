package com.ece.vbfc_bry07.calls.controller;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.ece.vbfc_bry07.calls.Helpers;

import java.util.ArrayList;
import java.util.HashMap;

public class PlansController extends DbHelper {
    DbHelper dbhelper;
    Helpers helpers;

    static String TBL_PLANS = "Plans",
            PLANS_ID = "plans_id",
            PLANS_CYCLE_NUMBER = "cycle_number",
            PLANS_STATUS = "status";

    public PlansController(Context context) {
        super(context);
        dbhelper = new DbHelper(context);
        helpers = new Helpers();
    }

    //////////////////////////////CHECK METHODS///////////////////////////
    public long checkForPlanByMonthYear(int year, int month) {
        String sql1 = "SELECT * FROM Plans as p INNER JOIN CycleSets as cs ON p.cycle_set_id = cs.cycle_sets_id WHERE year = " + year;
        SQLiteDatabase db = dbhelper.getWritableDatabase();
        Cursor cur = db.rawQuery(sql1, null);
        int cycle_set_id = 0;
        long plan_id = 0;

        if (cur.moveToNext())
            cycle_set_id = cur.getInt(cur.getColumnIndex("cycle_set_id"));

        if (cycle_set_id > 0) {
            String sql2 = "SELECT * FROM Plans WHERE cycle_set_id = " + cycle_set_id + " AND cycle_number = " + month;
            Cursor cur2 = db.rawQuery(sql2, null);

            if (cur2.moveToNext()) {
                plan_id = cur.getInt(cur.getColumnIndex("plans_id"));
            }

            cur2.close();
        }

        cur.close();
        db.close();

        return plan_id;
    }

    ///////////////////////ISAHON LANG NI SILA
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

    public int checkPlanIfApproved(long plan_id) {
        String sql = "SELECT * FROM Plans WHERE plans_id = " + plan_id;
        SQLiteDatabase db = dbhelper.getWritableDatabase();
        Cursor cur = db.rawQuery(sql, null);
        int status_id = -1;

        if (cur.moveToNext())
            status_id = cur.getInt(cur.getColumnIndex("status"));

        cur.close();
        db.close();

        return status_id;
    }
    ///////////////////////////////

    public HashMap<String, String> checkForDisapprovedPlans() {
        String sql = "SELECT * FROM Plans WHERE status = 2";
        SQLiteDatabase db = dbhelper.getWritableDatabase();
        Cursor cur = db.rawQuery(sql, null);
        HashMap<String, String> map = new HashMap<>();
        map.put("cycle_number", "");
        map.put("date", "");

        if (cur.moveToNext()) {
            map.put("cycle_number", cur.getString(cur.getColumnIndex(PLANS_CYCLE_NUMBER)));
            map.put("date", cur.getString(cur.getColumnIndex("updated_at")));
        }

        cur.close();
        db.close();

        return map;
    }

    ///////////////////////////////////////////GET METHODS
    public ArrayList<HashMap<String, String>> getAllPlans() {
        String sql = "SELECT * FROM Plans as p INNER JOIN CycleSets as cs ON p.cycle_set_id = cs.cycle_sets_id";
        SQLiteDatabase db = dbhelper.getWritableDatabase();
        Cursor cur = db.rawQuery(sql, null);
        ArrayList<HashMap<String, String>> array = new ArrayList<>();

        while (cur.moveToNext()) {
            HashMap<String, String> map = new HashMap<>();
            String month = helpers.convertIntToStringMonth(cur.getInt(cur.getColumnIndex(PLANS_CYCLE_NUMBER)));
            int year = cur.getInt(cur.getColumnIndex("year"));
            map.put("name", month + " " + year);
            map.put("cycle_number", cur.getString(cur.getColumnIndex(PLANS_CYCLE_NUMBER)));
            map.put("year", cur.getString(cur.getColumnIndex("year")));

            array.add(map);
        }

        db.close();
        cur.close();

        return array;
    }

    public int getPlanID(int cycleMonth) {
        String sql = "SELECT * FROM Plans WHERE cycle_number = " + cycleMonth;
        SQLiteDatabase db = dbhelper.getWritableDatabase();
        Cursor cur = db.rawQuery(sql, null);
        int planID = 0;

        if (cur.moveToNext())
            planID = cur.getInt(cur.getColumnIndex(AI_ID));

        cur.close();
        db.close();

        return planID;
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
