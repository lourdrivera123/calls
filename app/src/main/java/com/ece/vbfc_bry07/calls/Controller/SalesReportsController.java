package com.ece.vbfc_bry07.calls.Controller;

import android.content.Context;

/**
 * Created by vbfc_bry07 on 2/29/2016.
 */
public class SalesReportsController extends DbHelper {

    DbHelper dbHelper;

    static String TBL_SalesReports = "SalesReports",
            SalesReports_ID = "sales_reports_id",
            LAST_UPDATED = "last_updated",
            TERRITORY_CODE = "territory_code",
            SALES_TYPE = "sales_type";

    public static final String CREATE_SalesReports = String.format("CREATE TABLE %s (%s INTEGER PRIMARY KEY AUTOINCREMENT, %s INTEGER, %s TEXT, %s TEXT, %s TEXT, %s TEXT, %s TEXT, %s TEXT)",
            TBL_SalesReports, AI_ID, SalesReports_ID, LAST_UPDATED, TERRITORY_CODE, SALES_TYPE, CREATED_AT, UPDATED_AT, DELETED_AT);

    public SalesReportsController(Context context) {
        super(context);
        dbHelper = new DbHelper(context);
    }
}
