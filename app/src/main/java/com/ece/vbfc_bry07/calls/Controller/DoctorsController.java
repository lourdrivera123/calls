package com.ece.vbfc_bry07.calls.controller;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.HashMap;

public class DoctorsController extends DbHelper {

    DbHelper dbHelper;

    public static String DOCTORS_DOC_NAME = "doc_name",
            DOCTORS_BIRTHDAY = "birthday";

    public DoctorsController(Context context) {
        super(context);
        dbHelper = new DbHelper(context);
    }

    ////////////////////////GET METHODS
    public ArrayList<HashMap<String, String>> getBirthdayByDate(String date) {
        String sql = "SELECT * FROM Doctors as d INNER JOIN InstitutionDoctorMaps as idm ON d.doc_id = idm.doctor_id INNER JOIN Institutions as i ON idm.institution_id = i.inst_id" +
                " WHERE d.birthday LIKE '%" + date + "%'";
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cur = db.rawQuery(sql, null);
        ArrayList<HashMap<String, String>> array = new ArrayList<>();

        while (cur.moveToNext()) {
            HashMap<String, String> map = new HashMap<>();
            map.put("doctor_name", cur.getString(cur.getColumnIndex(DOCTORS_DOC_NAME)));
            map.put("birthday", cur.getString(cur.getColumnIndex(DOCTORS_BIRTHDAY)));
            map.put("institution", cur.getString(cur.getColumnIndex("inst_name")));
            array.add(map);
        }

        cur.close();
        db.close();

        return array;
    }
}
