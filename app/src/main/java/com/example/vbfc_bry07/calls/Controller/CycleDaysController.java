package com.example.vbfc_bry07.calls.Controller;

import android.content.Context;

/**
 * Created by vbfc_bry07 on 3/4/2016.
 */
public class CycleDaysController extends DbHelper {

    DbHelper dbHelper;

    static String TBL_CycleDays = "CycleDays",
            Cycle_Days_ID = "cycle_days_id",
            Cycle_Set_ID_FK = "cycle_set_id_fk",
            Cycle_Number = "cycle_number",
            Day_Number = "day_number",
            Date = "date",
            Day_Type_ID_FK = "day_type_id_fk",
            Label = "label";

    public static final String CREATE_CycleDays = String.format("CREATE TABLE %s (%s INTEGER PRIMARY KEY AUTOINCREMENT, %s INTEGER, %s INTEGER, %s INTEGER, %s INTEGER, %s TEXT, %s INTEGER, %s TEXT, %s TEXT, %s TEXT, %s TEXT)",
            TBL_CycleDays, AI_ID, Cycle_Days_ID, Cycle_Set_ID_FK, Cycle_Number, Day_Number, Date, Day_Type_ID_FK, Label, CREATED_AT, UPDATED_AT, DELETED_AT);

    public CycleDaysController(Context context) {
        super(context);
        dbHelper = new DbHelper(context);
    }
}
