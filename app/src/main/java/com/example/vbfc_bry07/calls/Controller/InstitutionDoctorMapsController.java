package com.example.vbfc_bry07.calls.Controller;

import android.content.Context;

public class InstitutionDoctorMapsController extends DbHelper {

    DbHelper dbHelper;

    static String TBL_InstitutionDoctorMaps = "InstitutionDoctorMaps",
<<<<<<< HEAD
            InstitutionDoctorMaps_ID = "institution_doctor_maps_id",
            INSTITUTION_ID_FK = "institution_id_fk",
            DOCTOR_ID_FK = "doctor_id_fk",
            CLASS_ID_FK = "class_id_fk",
=======
            InstitutionDoctorMaps_ID = "IDM_ID",
            INSTITUTION_ID_FK = "institution_id",
            DOCTOR_ID_FK = "doctor_id",
            CLASS_ID_FK = "class_id",
>>>>>>> d7483abaad8cf30117053c7820fadc8c7bbf3fcc
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
}
