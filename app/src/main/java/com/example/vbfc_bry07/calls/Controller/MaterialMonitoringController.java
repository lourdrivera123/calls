package com.example.vbfc_bry07.calls.Controller;

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

    public ArrayList<HashMap<String,String>> SelectAllProductsPerUser() {
        ArrayList<HashMap<String, String>> products = new ArrayList<>();
        String sql = "SELECT " +
                "    product_name, " +
                "    sum(case when material_id = 1 then material_count end) Total_Sample, " +
                "    sum(case when material_id = 2 then material_count end) Total_Literature, " +
                "    sum(case when material_id = 3 then material_count end) Total_Promomaterials, " +
                "    sum(case when material_id = 1 then beginning_balance end) Content_Sample, " +
                "    sum(case when material_id = 2 then beginning_balance end) Content_Literature, " +
                "    sum(case when material_id = 3 then beginning_balance end) Content_Promomaterials " +
                "FROM " +
                "    (SELECT" +
                "        MI.id, MI.material_inventories_id, MI.cycle_set_id_fk, " +
                "        MI.cycle_number, P.name as product_name, M.id as material_id, M.name as material_name, " +
                "        sum(MI.material_count) as material_count, sum(MI.beginning_balance) as beginning_balance " +
                "    FROM MaterialInventories MI " +
                "    left join CycleSets CS on CS.id = MI.cycle_set_id_fk " +
                "    left join Products P on P.id = MI.product_id_fk " +
                "    left join Materials M on M.id = MI.material_id_fk " +
                "    group by product_name, material_id) " +
                "group by product_name " +
                "order by product_name";
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cur = db.rawQuery(sql, null);

        while (cur.moveToNext()) {
            HashMap<String, String> map = new HashMap<>();
            // map.put("id", cur.getString(cur.getColumnIndex("id")));
            // map.put("material_inventories_id", cur.getString(cur.getColumnIndex("material_inventories_id")));
            // map.put("cycle_set_id_fk", cur.getString(cur.getColumnIndex("cycle_set_id_fk")));
            // map.put("cycle_number", cur.getString(cur.getColumnIndex("cycle_number")));
            map.put("product_name", cur.getString(cur.getColumnIndex("product_name")));
            map.put("Total_Sample", cur.getString(cur.getColumnIndex("Total_Sample")));
            map.put("Total_Literature", cur.getString(cur.getColumnIndex("Total_Literature")));
            map.put("Total_Promomaterials", cur.getString(cur.getColumnIndex("Total_Promomaterials")));
            map.put("Content_Sample", cur.getString(cur.getColumnIndex("Content_Sample")));
            map.put("Content_Literature", cur.getString(cur.getColumnIndex("Content_Literature")));
            map.put("Content_Promomaterials", cur.getString(cur.getColumnIndex("Content_Promomaterials")));

            products.add(map);
        }

        cur.close();
        db.close();

        return products;

    }
}
