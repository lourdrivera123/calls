package com.ece.vbfc_bry07.calls.Controller;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.HashMap;

public class MaterialMonitoringController extends DbHelper {

    DbHelper dbHelper;

    public MaterialMonitoringController(Context context) {
        super(context);
        dbHelper = new DbHelper(context);
    }

    public ArrayList<HashMap<String, String>> SelectAllProductsPerUser() {
        ArrayList<HashMap<String, String>> products = new ArrayList<>();
        String sql = "SELECT * FROM Products";
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cur = db.rawQuery(sql, null);

        while (cur.moveToNext()) {
            HashMap<String, String> map = new HashMap<>();
            map.put("product_name", cur.getString(cur.getColumnIndex("name")));
            map.put("product_id", cur.getString(cur.getColumnIndex("products_id")));
            products.add(map);
        }

        cur.close();
        db.close();

        return products;

    }

    public ArrayList<HashMap<String, String>> getCallMaterialsByMonth(int cycleMonth) {
        String sql = "SELECT cm.product_id, sum(sample) as sample, sum(literature) as literature, sum(promaterials) as promaterials  FROM Calls as c " +
                "INNER JOIN PlanDetails as pd ON pd.plan_details_id = c.plan_details_id INNER JOIN Plans as p ON p.id = pd.plan_id " +
                "INNER JOIN CallMaterials as cm ON c.id = cm.call_id WHERE (c.temp_planDetails_id = pd.id OR c.plan_details_id > 0) AND p.cycle_number = " + cycleMonth + " GROUP BY cm.product_id";
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cur = db.rawQuery(sql, null);
        ArrayList<HashMap<String, String>> array = new ArrayList<>();

        while (cur.moveToNext()) {
            HashMap<String, String> map = new HashMap<>();
            map.put("product_id", cur.getString(cur.getColumnIndex("product_id")));
            map.put("sample", cur.getString(cur.getColumnIndex("sample")));
            map.put("literature", cur.getString(cur.getColumnIndex("literature")));
            map.put("promaterials", cur.getString(cur.getColumnIndex("promaterials")));
            array.add(map);
        }

        cur.close();
        db.close();

        return array;
    }
}
