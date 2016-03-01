package com.example.vbfc_bry07.calls.Controller;

import android.content.Context;

/**
 * Created by vbfc_bry07 on 3/1/2016.
 */
public class DetailingAidEmailsController extends DbHelper {

    DbHelper dbHelper;

    static String TBL_DetailingAidEmails = "DetailingAidEmails",
            DetailingAidEmails_ID = "detailing_aid_emails_ID",
            DETAILING_IDS = "detailling_ids",
            EMAILS = "email",
            DOCTOR_ID_FK = "doctor_id_fk",
            ADVERSE_EFFECT_INCLUDED = "adverse_effect_included";

    public static final String CREATE_DetailingAidEmails = String.format("CREATE TABLE %s (%s INTEGER PRIMARY KEY AUTOINCREMENT, %s INTEGER, %s TEXT, %s TEXT, %s INTEGER, %s INTEGER, %s TEXT, %s TEXT, %s TEXT)",
            TBL_DetailingAidEmails, AI_ID, DetailingAidEmails_ID, DETAILING_IDS, EMAILS, DOCTOR_ID_FK, ADVERSE_EFFECT_INCLUDED, CREATED_AT, UPDATED_AT, DELETED_AT);

    public DetailingAidEmailsController(Context context) {
        super(context);
        dbHelper = new DbHelper(context);
    }
}
