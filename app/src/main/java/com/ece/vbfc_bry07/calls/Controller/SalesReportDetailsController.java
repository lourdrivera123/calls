package com.ece.vbfc_bry07.calls.Controller;

import android.content.Context;

public class SalesReportDetailsController extends DbHelper {

    DbHelper dbHelper;

    static String TBL_SalesReportDetails = "SalesReportDetails",
            SalesReportDetails_ID = "sales_report_details_id",
            SALES_TYPE = "sales_type",
            PRODUCT_ID_FK = "product_id_fk",
            CHANNEL = "channel",
            YEAR = "year",
            MONTH = "month",
            SALES_AMOUNT = "sales_amount";

    public static final String CREATE_SalesReportDetails = String.format("CREATE TABLE %s (%s INTEGER PRIMARY KEY AUTOINCREMENT, %s INTEGER, %s TEXT, %s INTEGER, %s TEXT, %s INTEGER, %s INTEGER, %s DOUBLE, %s TEXT, %s TEXT, %s TEXT)",
            TBL_SalesReportDetails, AI_ID, SalesReportDetails_ID, SALES_TYPE, PRODUCT_ID_FK, CHANNEL, YEAR, MONTH, SALES_AMOUNT, CREATED_AT, UPDATED_AT, DELETED_AT);

    public SalesReportDetailsController(Context context) {
        super(context);
        dbHelper = new DbHelper(context);
    }
}
