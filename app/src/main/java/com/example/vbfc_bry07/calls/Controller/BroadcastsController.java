package com.example.vbfc_bry07.calls.Controller;

import android.content.Context;

/**
 * Created by vbfc_bry07 on 2/29/2016.
 */
public class BroadcastsController extends DbHelper {

    DbHelper dbHelper;

    static String TBL_Broadcasts = "Broadcasts",
            Broadcasts_ID = "broadcasts_id",
            MESSAGE = "message",
            STATUS_ID_FK = "status_id_fk",
            END_DATE = "end_date",
            AUTHOR = "author";

    public static final String CREATE_Broadcasts = String.format("CREATE TABLE %s (%s INTEGER PRIMARY KEY AUTOINCREMENT, %s INTEGER, %s TEXT, %s INTEGER, %s TEXT, %s TEXT, %s TEXT, %s TEXT, %s TEXT)",
            TBL_Broadcasts, AI_ID, Broadcasts_ID, MESSAGE, STATUS_ID_FK, END_DATE, AUTHOR, CREATED_AT, UPDATED_AT, DELETED_AT);

    public BroadcastsController(Context context) {
        super(context);
        dbHelper = new DbHelper(context);
    }
}
