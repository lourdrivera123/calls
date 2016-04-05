package com.ece.vbfc_bry07.calls.Controller;

import android.content.Context;

public class ClientsController extends DbHelper {

    DbHelper dbhelper;

    static String TBL_Clients = "Clients",
            Clients_ID = "clients_id",
            CODE = "code",
            NAME = "name",
            CONFIG_URL = "config_url",
            CONFIG = "config";

    public static final String CREATE_Clients = String.format("CREATE TABLE %s (%s INTEGER PRIMARY KEY AUTOINCREMENT, %s INTEGER, %s TEXT, %s TEXT, %s TEXT, %s TEXT, %s TEXT, %s TEXT, %s TEXT)",
            TBL_Clients, AI_ID, Clients_ID, CODE, NAME, CONFIG_URL, CONFIG, CREATED_AT, UPDATED_AT, DELETED_AT);

    public ClientsController(Context context) {
        super(context);
        dbhelper = new DbHelper(context);
    }
}
