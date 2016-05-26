package com.ece.vbfc_bry07.calls.controller;

import android.content.Context;

public class SpecializationsController extends DbHelper {
    public static final String TBL_SPECIALIZATIONS = "Specializations",
            SPECIALIZATION_ID = "specialization_id",
            SPECIALIZATION_CODE = "code",
            SPECIALIZATION_NAME = "name";

    public SpecializationsController(Context context) {
        super(context);
    }
}
