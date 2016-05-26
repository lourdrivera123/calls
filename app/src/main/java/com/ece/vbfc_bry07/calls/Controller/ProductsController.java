package com.ece.vbfc_bry07.calls.controller;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.HashMap;

public class ProductsController extends DbHelper {

    DbHelper dbHelper;

    static String TBL_Products = "Products",
            Products_ID = "products_id",
            CODE = "code",
            NAME = "name";

    public ProductsController(Context context) {
        super(context);
        dbHelper = new DbHelper(context);
    }

    public ArrayList<HashMap<String, String>> getAllProducts() {
        String sql = "SELECT * FROM " + TBL_Products + " ORDER BY " + NAME;
        SQLiteDatabase db = getWritableDatabase();
        Cursor cur = db.rawQuery(sql, null);
        ArrayList<HashMap<String, String>> array = new ArrayList<>();

        while (cur.moveToNext()) {
            HashMap<String, String> map = new HashMap<>();
            map.put("product_id", cur.getString(cur.getColumnIndex(Products_ID)));
            map.put("product_code", cur.getString(cur.getColumnIndex(CODE)));
            map.put("product_name", cur.getString(cur.getColumnIndex(NAME)));
            map.put("sample", "");
            map.put("literature", "");
            map.put("promaterials", "");
            array.add(map);
        }

        cur.close();
        db.close();

        return array;
    }
}
