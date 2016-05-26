package com.ece.vbfc_bry07.calls.controller;

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

    public DetailingAidsController(Context context) {
        super(context);
        dbHelper = new DbHelper(context);
    }
}
