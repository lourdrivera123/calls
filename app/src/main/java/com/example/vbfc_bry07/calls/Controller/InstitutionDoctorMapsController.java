package com.example.vbfc_bry07.calls.Controller;

import android.content.Context;

/**
 * Created by vbfc_bry07 on 2/29/2016.
 */
public class InstitutionDoctorMapsController extends DbHelper {

    DbHelper dbHelper;

    static String TBL_InstitutionDoctorMaps = "InstitutionDoctorMapsController",
            InstitutionDoctorMaps_ID = "InstitutionDoctorMaps",
            INSTITUTION_ID_FK = "INSTITUTION_ID_FK",
            DOCTOR_ID_FK = "DOCTOR_ID_FK",
            CLASS_ID_FK = "CLASS_ID_FK",
            BEST_TIME_TO_CALL = "BEST_TIME_TO_CALL",
            ROOM_NUMBER = "ROOM_NUMBER",
            DEFAULT_PRODUCTS = "DEFAULT_PRODUCTS",
            STAGE_ID_FK = "STAGE_ID_FK";

    public static final String CREATE_InstitutionDoctorMaps = String.format("CREATE TABLE %s (%s INTEGER PRIMARY KEY AUTOINCREMENT, %s INTEGER, %s INTEGER, %s INTEGER, %s INTEGER, %s TEXT, %s TEXT, %s TEXT, %s INTEGER, %s TEXT, %s TEXT, %s TEXT)",
            TBL_InstitutionDoctorMaps, AI_ID, InstitutionDoctorMaps_ID, INSTITUTION_ID_FK, DOCTOR_ID_FK, CLASS_ID_FK, BEST_TIME_TO_CALL, ROOM_NUMBER, DEFAULT_PRODUCTS, STAGE_ID_FK, CREATED_AT, UPDATED_AT, DELETED_AT);

    public InstitutionDoctorMapsController(Context context) {
        super(context);
        dbHelper = new DbHelper(context);
    }
}
