package com.example.vbfc_bry07.calls.Controller;

import android.content.Context;

public class ProductsController extends DbHelper {

    DbHelper dbHelper;

    static String TBL_Products = "Products",
            Products_ID = "products_id",
            CODE = "code",
            NAME = "name";

    public static final String CREATE_Products = String.format("CREATE TABLE %s (%s INTEGER PRIMARY KEY AUTOINCREMENT, %s INTEGER, %s TEXT, %s TEXT, %s TEXT, %s TEXT, %s TEXT)",
            TBL_Products, AI_ID, Products_ID, CODE, NAME, CREATED_AT, UPDATED_AT, DELETED_AT);

    public ProductsController(Context context) {
        super(context);
        dbHelper = new DbHelper(context);
    }
}
