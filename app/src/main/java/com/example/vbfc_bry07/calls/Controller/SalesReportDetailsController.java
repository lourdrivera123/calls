package com.example.vbfc_bry07.calls.Controller;

import android.content.Context;

/**
 * Created by vbfc_bry07 on 2/29/2016.
 */
public class SalesReportDetailsController extends DbHelper {

    DbHelper dbHelper;

    static String TBL_SalesReportDetails = "SalesReportDetails",
            SalesReportDetails_ID = "SalesReportDetails_ID",
            SALES_TYPE = "SALES_TYPE",
            PRODUCT_ID_FK = "PRODUCT_ID_FK",
            CHANNEL = "CHANNEL",
            YEAR = "YEAR",
            MONTH = "MONTH",
            SALES_AMOUNT = "SALES_AMOUNT";

    public static final String CREATE_SalesReportDetails = String.format("CREATE TABLE %s (%s INTEGER PRIMARY KEY AUTOINCREMENT, %s INTEGER, %s TEXT, %s INTEGER, %s TEXT, %s INTEGER, %s INTEGER, %s DOUBLE, %s TEXT, %s TEXT, %s TEXT)",
            TBL_SalesReportDetails, AI_ID, SalesReportDetails_ID, SALES_TYPE, PRODUCT_ID_FK, CHANNEL, YEAR, MONTH, SALES_AMOUNT, CREATED_AT, UPDATED_AT, DELETED_AT);

    public SalesReportDetailsController(Context context) {
        super(context);
        dbHelper = new DbHelper(context);
    }
}
