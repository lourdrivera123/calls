package com.example.vbfc_bry07.calls.Controller;

import android.content.Context;

/**
 * Created by vbfc_bry07 on 2/29/2016.
 */
public class MaterialsByClassSpecializationMapsController extends DbHelper {

    DbHelper dbHelper;

    static String TBL_MaterialsByClassSpecializationMaps = "MaterialsByClassSpecializationMaps",
            MaterialsByClassSpecializationMaps_ID = "materials_by_class_specialization_maps_id",
            CLASS_ID_FK = "class_id_fk",
            SPECIALIZATION_ID_FK = "specialization_id_fk",
            UPLOADED = "uploaded";

    public static final String CREATE_MaterialsByClassSpecializationMaps = String.format("CREATE TABLE %s (%s INTEGER PRIMARY KEY AUTOINCREMENT, %s INTEGER, %s INTEGER, %s INTEGER, %s TEXT, %s TEXT, %s TEXT, %s TEXT)",
            TBL_MaterialsByClassSpecializationMaps, AI_ID, MaterialsByClassSpecializationMaps_ID, CLASS_ID_FK, SPECIALIZATION_ID_FK, UPLOADED, CREATED_AT, UPDATED_AT, DELETED_AT);

    public MaterialsByClassSpecializationMapsController(Context context) {
        super(context);
        dbHelper = new DbHelper(context);
    }
}
