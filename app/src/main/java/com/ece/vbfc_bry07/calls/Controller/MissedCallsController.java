package com.ece.vbfc_bry07.calls.Controller;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.ece.vbfc_bry07.calls.Helpers;

import java.util.ArrayList;
import java.util.HashMap;

public class MissedCallsController extends DbHelper {
    Helpers helpers;
    CallsController cc;
    DbHelper dbHelper;

    static String TBL_MissedCalls = "MissedCalls",
            MissedCalls_ID = "missed_calls_id",
            PLAN_DETAILS_ID = "plan_details_id",
            REASON_ID = "reason_id",
            REMARKS = "remarks",
            SENT = "sent",
            STATUS_APPROVED = "status_approved";

    public static final String CREATE_MissedCalls = String.format("CREATE TABLE %s (%s INTEGER PRIMARY KEY AUTOINCREMENT, %s INTEGER, %s INTEGER, %s INTEGER, %s TEXT, %s INTEGER, %s INTEGER, %s TEXT, %s TEXT, %s TEXT)",
            TBL_MissedCalls, AI_ID, MissedCalls_ID, PLAN_DETAILS_ID, REASON_ID, REMARKS, SENT, STATUS_APPROVED, CREATED_AT, UPDATED_AT, DELETED_AT);

    public MissedCallsController(Context context) {
        super(context);
        helpers = new Helpers();
        cc = new CallsController(context);
        dbHelper = new DbHelper(context);
    }


    public boolean insertMissedCalls(int reason_id, String remarks, HashMap<Integer, ArrayList<HashMap<String, String>>> planDetails) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        long check = 0;

        for (int x = 0; x < planDetails.size(); x++) {
            for (int y = 0; y < planDetails.get(x).size(); y++) {
                ContentValues val = new ContentValues();
                val.put(REASON_ID, reason_id);
                val.put(REMARKS, remarks);

                int plan_details_id = Integer.parseInt(planDetails.get(x).get(y).get("plan_details_id"));
                int hascalled = cc.hasCalled(plan_details_id, "planDetails");

                if (hascalled == 0) {
                    val.put(PLAN_DETAILS_ID, planDetails.get(x).get(y).get("plan_details_id"));
                    val.put(CREATED_AT, helpers.getCurrentDate("timestamp"));

                    check = db.insert(TBL_MissedCalls, null, val);
                }
            }
        }

        db.close();
        return check > 0;
    }
}
