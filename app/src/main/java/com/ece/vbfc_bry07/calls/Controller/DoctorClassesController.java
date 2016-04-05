package com.ece.vbfc_bry07.calls.Controller;

import android.content.Context;

public class DoctorClassesController extends DbHelper {

    DbHelper dbHelper;

    static String TBL_DoctorClasses = "DoctorClasses",
            DoctorClasses_ID = "doctor_classes_id",
            CODE = "code",
            NAME = "name",
            MAX_VISIT = "max_visit";

    public static final String CREATE_DoctorClasses = String.format("CREATE TABLE %s (%s INTEGER PRIMARY KEY AUTOINCREMENT, %s INTEGER, %s TEXT, %s TEXT, %s INTEGER, %s TEXT, %s TEXT, %s TEXT)",
            TBL_DoctorClasses, AI_ID, DoctorClasses_ID, CODE, NAME, MAX_VISIT, CREATED_AT, UPDATED_AT, DELETED_AT);

    public DoctorClassesController(Context context) {
        super(context);
        dbHelper = new DbHelper(context);
    }
}
