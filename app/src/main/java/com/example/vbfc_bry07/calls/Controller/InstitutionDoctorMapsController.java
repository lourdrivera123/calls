package com.example.vbfc_bry07.calls.Controller;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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

    public ArrayList<HashMap<String, String>> getInstitutions() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String sql = "SELECT DISTINCT idm.institution_id, i.inst_name from InstitutionDoctorMaps AS idm INNER JOIN Institutions AS i ON idm.institution_id = i.inst_id";
        Cursor cur = db.rawQuery(sql, null);
        ArrayList<HashMap<String, String>> array = new ArrayList<>();

        while (cur.moveToNext()) {
            HashMap<String, String> map = new HashMap<>();
            map.put("institution_id", cur.getString(cur.getColumnIndex(INSTITUTION_ID_FK)));
            map.put("institution_name", cur.getString(cur.getColumnIndex("inst_name")));
            array.add(map);
        }

        cur.close();
        db.close();

        return array;
    }

    public ArrayList<HashMap<String, String>> getDoctorsWithInstitutions() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String sql = "SELECT idm.IDM_ID, idm.doctor_id, idm.institution_id, i.inst_name, d.doc_name, d.contact_number, dc.name, dc.max_visit from Institutions as i " +
                "INNER JOIN InstitutionDoctorMaps as idm on i.inst_id = idm.institution_id INNER JOIN Doctors as d on idm.doctor_id = d.doc_id " +
                "INNER JOIN DoctorClasses as dc on idm.class_id = dc.doctor_classes_id ORDER BY idm.institution_id";
        Cursor cur = db.rawQuery(sql, null);
        ArrayList<HashMap<String, String>> array = new ArrayList<>();

        while (cur.moveToNext()) {
            String class_code = cur.getString(cur.getColumnIndex("name")) + " (" + cur.getString(cur.getColumnIndex("max_visit")) + "x)";

            HashMap<String, String> map = new HashMap<>();
            map.put("doctor_id", cur.getString(cur.getColumnIndex(DOCTOR_ID_FK)));
            map.put("IDM_id", cur.getString(cur.getColumnIndex("IDM_ID")));
            map.put("doctor_inst_id", cur.getString(cur.getColumnIndex(INSTITUTION_ID_FK)));
            map.put("doc_name", cur.getString(cur.getColumnIndex("doc_name")));
            map.put("contact_number", cur.getString(cur.getColumnIndex("contact_number")));
            map.put("inst_name", cur.getString(cur.getColumnIndex("inst_name")));
            map.put("class_code", class_code);
            array.add(map);
        }

        cur.close();
        db.close();

        return array;
    }

    public String getInstitutionByID(int id) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String sql = "SELECT DISTINCT idm.institution_id, i.inst_name from InstitutionDoctorMaps AS idm INNER JOIN Institutions AS i ON idm.institution_id = i.inst_id WHERE idm.institution_id = " + id;
        Cursor cur = db.rawQuery(sql, null);
        String institution = "";

        if (cur.moveToNext())
            institution = cur.getString(cur.getColumnIndex("inst_name"));

        cur.close();
        db.close();

        return institution;
    }
}
