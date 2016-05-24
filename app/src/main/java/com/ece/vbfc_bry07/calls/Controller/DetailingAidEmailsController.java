package com.ece.vbfc_bry07.calls.Controller;

import android.content.Context;

public class DetailingAidEmailsController extends DbHelper {

    DbHelper dbHelper;

    public DetailingAidEmailsController(Context context) {
        super(context);
        dbHelper = new DbHelper(context);
    }
}
