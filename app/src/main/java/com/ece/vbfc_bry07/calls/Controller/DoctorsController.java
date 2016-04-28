package com.ece.vbfc_bry07.calls.Controller;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.HashMap;

public class DoctorsController extends DbHelper {

    DbHelper dbHelper;

    public static String TBL_DOCTORS = "Doctors",
            DOCTORS_DOC_ID = "doc_id",
            DOCTORS_DOC_CODE = "doc_code",
            DOCTORS_DOC_NAME = "doc_name",
            DOCTORS_SPECIALIZATION_ID = "specialization_id",
            DOCTORS_BIRTHDAY = "birthday",
            DOCTORS_SPOUSE = "spouse",
            DOCTORS_CONTACT_NUMBER = "contact_number",
            DOCTORS_PRC_LICENSE = "prc_license",
            DOCTORS_EMAIL_ADDRESS = "email_address";

    public static final String CREATE_DOCTORS = String.format("CREATE TABLE %s (%s INTEGER PRIMARY KEY AUTOINCREMENT, %s INTEGER, %s TEXT, %s TEXT, %s INTEGER, %s TEXT, %s TEXT, %s TEXT, %s TEXT, %s TEXT, %s TEXT, %s TEXT, %s TEXT)",
            TBL_DOCTORS, AI_ID, DOCTORS_DOC_ID, DOCTORS_DOC_CODE, DOCTORS_DOC_NAME, DOCTORS_SPECIALIZATION_ID, DOCTORS_BIRTHDAY, DOCTORS_SPOUSE, DOCTORS_CONTACT_NUMBER, DOCTORS_PRC_LICENSE, DOCTORS_EMAIL_ADDRESS, CREATED_AT, UPDATED_AT, DELETED_AT);

    public DoctorsController(Context context) {
        super(context);
        dbHelper = new DbHelper(context);
    }

    public ArrayList<HashMap<String, String>> SelectAllBirthdaysThisMonthYear() {
        ArrayList<HashMap<String, String>> doctorsBirthday = new ArrayList<>();
        String sql = "SELECT id, doc_id, doc_name, birthday, " +
                "   strftime('%m', birthday) as month, " +
                "   strftime('%d', birthday) as day, " +
                "   strftime('%Y', birthday) as year " +
                "FROM Doctors " +
                "where month = strftime('%m', date()) " +
                "   and year = strftime('%Y', date()) " +
                "   and day = strftime('%d', date())" +
                "ORDER by doc_name ASC";
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cur = db.rawQuery(sql, null);

        while (cur.moveToNext()) {
            HashMap<String, String> map = new HashMap<>();
            map.put(DOCTORS_DOC_NAME, cur.getString(cur.getColumnIndex(DOCTORS_DOC_NAME)));
            map.put(DOCTORS_BIRTHDAY, cur.getString(cur.getColumnIndex(DOCTORS_BIRTHDAY)));

            doctorsBirthday.add(map);
        }

        cur.close();
        db.close();

        return doctorsBirthday;

    }
}
