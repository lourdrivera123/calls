package com.ece.vbfc_bry07.calls.Controller;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.HashMap;

public class InstitutionDoctorMapsController extends DbHelper {

    DbHelper dbHelper;

    static String TBL_InstitutionDoctorMaps = "InstitutionDoctorMaps",
            InstitutionDoctorMaps_ID = "IDM_ID",
            INSTITUTION_ID_FK = "institution_id",
            DOCTOR_ID_FK = "doctor_id",
            CLASS_ID_FK = "class_id",
            BEST_TIME_TO_CALL = "best_time_to_call",
            ROOM_NUMBER = "room_number",
            DEFAULT_PRODUCTS = "default_products",
            STAGE_ID_FK = "stage_id";

    public static final String CREATE_InstitutionDoctorMaps = String.format("CREATE TABLE %s (%s INTEGER PRIMARY KEY AUTOINCREMENT, %s INTEGER, %s INTEGER, %s INTEGER, %s INTEGER, %s TEXT, %s TEXT, %s TEXT, %s INTEGER, %s TEXT, %s TEXT, %s TEXT)",
            TBL_InstitutionDoctorMaps, AI_ID, InstitutionDoctorMaps_ID, INSTITUTION_ID_FK, DOCTOR_ID_FK, CLASS_ID_FK, BEST_TIME_TO_CALL, ROOM_NUMBER, DEFAULT_PRODUCTS, STAGE_ID_FK, CREATED_AT, UPDATED_AT, DELETED_AT);

    public InstitutionDoctorMapsController(Context context) {
        super(context);
        dbHelper = new DbHelper(context);
    }

    //GET METHODS
    public ArrayList<HashMap<String, String>> getDoctorsWithInstitutions(String date) {
        String sql;

        if (!date.equals("")) {
            sql = "SELECT  pd.id as temp_plandetails_id, pd.plan_details_id as pd_id, dc.name as class_name, * FROM Doctors as d INNER JOIN InstitutionDoctorMaps as idm ON idm.doctor_id = d.doc_id " +
                    "INNER JOIN DoctorClasses as dc ON idm.class_id = dc.doctor_classes_id INNER JOIN Institutions as i on idm.institution_id = i.inst_id " +
                    "INNER JOIN PlanDetails as pd ON idm.IDM_ID = pd.inst_doc_id LEFT JOIN Calls as c ON pd.plan_details_id = c.plan_details_id " +
                    "LEFT JOIN RescheduledCalls as rc ON c.id = rc.call_id WHERE pd.cycle_day = '" + date + "' OR rc.cycle_day = '" + date + "'";
        } else
            sql = "SELECT *, dc.name as class_name, s.name as specialization_name from InstitutionDoctorMaps as idm INNER JOIN Doctors as d on idm.doctor_id = d.doc_id " +
                    "INNER JOIN DoctorClasses as dc on idm.class_id = dc.doctor_classes_id INNER JOIN Institutions as i on idm.institution_id = i.inst_id " +
                    "INNER JOIN Specializations as s on d.specialization_id = s.specialization_id ORDER BY idm.institution_id";

        SQLiteDatabase db = dbHelper.getWritableDatabase();

        Cursor cur = db.rawQuery(sql, null);
        ArrayList<HashMap<String, String>> array = new ArrayList<>();

        while (cur.moveToNext()) {
            HashMap<String, String> map = new HashMap<>();
            map.put("doctor_id", cur.getString(cur.getColumnIndex("doctor_id")));
            map.put("IDM_id", cur.getString(cur.getColumnIndex("IDM_ID")));
            map.put("doctor_inst_id", cur.getString(cur.getColumnIndex("institution_id")));
            map.put("inst_name", cur.getString(cur.getColumnIndex("inst_name")));
            map.put("doc_name", cur.getString(cur.getColumnIndex("doc_name")));
            map.put("contact_number", cur.getString(cur.getColumnIndex("contact_number")));
            map.put("class_code", cur.getString(cur.getColumnIndex("max_visit")));
            map.put("class_name", cur.getString(cur.getColumnIndex("class_name")));

            if (!date.equals("")) {
                map.put("plan_details_id", cur.getString(cur.getColumnIndex("pd_id")));
                map.put("temp_plandetails_id", cur.getString(cur.getColumnIndex("temp_plandetails_id")));
            } else
                map.put("specialization", cur.getString(cur.getColumnIndex("specialization_name")));

            array.add(map);
        }

        cur.close();
        db.close();

        return array;
    }

    public String getInstitutionByID(int id) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String sql = "SELECT DISTINCT idm.institution_id, i.inst_name from InstitutionDoctorMaps AS idm INNER JOIN Institutions AS i ON idm.institution_id = i.inst_id " +
                "WHERE idm.institution_id = " + id;
        Cursor cur = db.rawQuery(sql, null);
        String institution = "";

        if (cur.moveToNext())
            institution = cur.getString(cur.getColumnIndex("inst_name"));

        cur.close();
        db.close();

        return institution;
    }

    public ArrayList<HashMap<String, String>> getDoctorsNotIncludedInMCP(String date) {
        String sql = "SELECT * FROM Doctors as d INNER JOIN InstitutionDoctorMaps as idm ON d.doc_id = idm.doctor_id INNER JOIN Institutions as i ON idm.institution_id = i.inst_id " +
                "LEFT JOIN PlanDetails as pd ON idm.IDM_ID = pd.inst_doc_id WHERE idm.IDM_ID NOT IN (SELECT pd.inst_doc_id FROM PlanDetails as pd " +
                "LEFT JOIN Calls as c ON c.plan_details_id = pd.plan_details_id " +
                "LEFT JOIN RescheduledCalls as rc ON c.id = rc.call_id WHERE pd.cycle_day = '" + date + "' OR rc.cycle_day = '" + date + "') GROUP BY d.id ORDER BY institution_id";
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cur = db.rawQuery(sql, null);
        ArrayList<HashMap<String, String>> array = new ArrayList<>();

        while (cur.moveToNext()) {
            String planDetails_id = cur.getString(cur.getColumnIndex("plan_details_id")) == null ? "0" : cur.getString(cur.getColumnIndex("plan_details_id"));

            HashMap<String, String> map = new HashMap<>();
            map.put("doctor_id", cur.getString(cur.getColumnIndex("doctor_id")));
            map.put("IDM_id", cur.getString(cur.getColumnIndex("IDM_ID")));
            map.put("doctor_inst_id", cur.getString(cur.getColumnIndex("institution_id")));
            map.put("inst_name", cur.getString(cur.getColumnIndex("inst_name")));
            map.put("doc_name", cur.getString(cur.getColumnIndex("doc_name")));
            map.put("contact_number", cur.getString(cur.getColumnIndex("contact_number")));
            map.put("plan_details_id", planDetails_id);
            array.add(map);
        }

        cur.close();
        db.close();

        return array;
    }
}
