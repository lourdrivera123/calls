package com.ece.vbfc_bry07.calls.controller;

import android.content.Context;

public class MaterialsByClassSpecializationMapsController extends DbHelper {
    DbHelper dbHelper;

    static String TBL_MaterialsByClassSpecializationMaps = "MaterialsByClassSpecializationMaps",
            MaterialsByClassSpecializationMaps_ID = "materials_by_class_specialization_maps_id",
            CLASS_ID_FK = "class_id_fk",
            SPECIALIZATION_ID_FK = "specialization_id_fk",
            UPLOADED = "uploaded";

    public MaterialsByClassSpecializationMapsController(Context context) {
        super(context);
        dbHelper = new DbHelper(context);
    }
}
