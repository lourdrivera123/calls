package com.example.vbfc_bry07.calls.Controller;

import android.content.Context;

/**
 * Created by vbfc_bry07 on 2/29/2016.
 */
public class MaterialsByInstitutionDoctorMapsController extends DbHelper {

    DbHelper dbHelper;

    static String TBL_MaterialsByInstitutionDoctorMaps = "MaterialsByInstitutionDoctorMaps",
            MaterialsByInstitutionDoctorMaps_ID = "MaterialsByInstitutionDoctorMaps_ID",
            INST_DOC_ID_FK = "INST_DOC_ID_FK",
            DATE = "DATE",
            UPLOADED = "UPLOADED";

    public static final String CREATE_MaterialsByInstitutionDoctorMaps = String.format("CREATE TABLE %s (%s INTEGER PRIMARY KEY AUTOINCREMENT, %s INTEGER, %s INTEGER, %s TEXT, %s TEXT, %s TEXT, %s TEXT, %s TEXT)",
            TBL_MaterialsByInstitutionDoctorMaps, AI_ID, MaterialsByInstitutionDoctorMaps_ID, INST_DOC_ID_FK, DATE, UPLOADED, CREATED_AT, UPDATED_AT, DELETED_AT);

    public MaterialsByInstitutionDoctorMapsController(Context context) {
        super(context);
        dbHelper = new DbHelper(context);
    }
}
