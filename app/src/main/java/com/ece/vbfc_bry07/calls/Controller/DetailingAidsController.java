package com.ece.vbfc_bry07.calls.Controller;

import android.content.Context;

public class DetailingAidsController extends DbHelper {

    DbHelper dbHelper;

    static String TBL_DetailingAids = "DetailingAids",
            DetailingAids_ID = "detailing_aids_id",
            FILENAME = "cycle_number",
            FILETYPE = "file_type",
            FILESIZE = "file_size",
            PRODUCTS = "products",
            DOWNLOAD_PATH = "download_path",
            STATUS_ID_FK = "status_id_fk",
            DOWNLOADED = "downloaded",
            DOWNLOADED_DATE = "downloaded_date";

    public static final String CREATE_DetailingAids = String.format("CREATE TABLE %s (%s INTEGER PRIMARY KEY AUTOINCREMENT, %s INTEGER, %s TEXT, %s TEXT, %s TEXT, %s TEXT, %s TEXT, %s INTEGER, %s INTEGER, %s TEXT, %s TEXT, %s TEXT, %s TEXT)",
            TBL_DetailingAids, AI_ID, DetailingAids_ID, FILENAME, FILETYPE, FILESIZE, PRODUCTS, DOWNLOAD_PATH, STATUS_ID_FK, DOWNLOADED, DOWNLOADED_DATE, CREATED_AT, UPDATED_AT, DELETED_AT);

    public DetailingAidsController(Context context) {
        super(context);
        dbHelper = new DbHelper(context);
    }
}
