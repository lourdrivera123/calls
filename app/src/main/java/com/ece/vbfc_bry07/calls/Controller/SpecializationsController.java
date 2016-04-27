package com.ece.vbfc_bry07.calls.Controller;

import android.content.Context;

public class SpecializationsController extends DbHelper {
    public static final String TBL_SPECIALIZATIONS = "Specializations",
            SPECIALIZATION_ID = "specialization_id",
            SPECIALIZATION_CODE = "code",
            SPECIALIZATION_NAME = "name";

    public static final String CREATE_SPECIALIZATIONS = String.format("CREATE TABLE %s (%s INTEGER PRIMARY KEY AUTOINCREMENT, %s INTEGER, %s TEXT, %s TEXT, %s TEXT, %s TEXT, %s TEXT)",
            TBL_SPECIALIZATIONS, AI_ID, SPECIALIZATION_ID, SPECIALIZATION_CODE, SPECIALIZATION_NAME, CREATED_AT, UPDATED_AT, DELETED_AT);

    public SpecializationsController(Context context) {
        super(context);
    }
}
