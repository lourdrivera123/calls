package com.ece.vbfc_bry07.calls.Controller;

import android.content.Context;

public class MaterialsByInstitutionDoctorMapsController extends DbHelper {
    DbHelper dbHelper;

    static String TBL_MaterialsByInstitutionDoctorMaps = "MaterialsByInstitutionDoctorMaps",
            MaterialsByInstitutionDoctorMaps_ID = "materials_by_institution_doctor_maps_id",
            INST_DOC_ID_FK = "inst_doc_id_fk",
            DATE = "date",
            UPLOADED = "uploaded";

    public MaterialsByInstitutionDoctorMapsController(Context context) {
        super(context);
        dbHelper = new DbHelper(context);
    }
}
