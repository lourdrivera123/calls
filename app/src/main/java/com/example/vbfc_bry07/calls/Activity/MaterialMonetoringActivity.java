package com.example.vbfc_bry07.calls.Activity;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.example.vbfc_bry07.calls.Adapter.ProductListAdapter;
import com.example.vbfc_bry07.calls.Controller.DbHelper;
import com.example.vbfc_bry07.calls.Controller.MaterialMonetoringController;
import com.example.vbfc_bry07.calls.R;

import java.util.ArrayList;
import java.util.HashMap;

public class MaterialMonetoringActivity extends AppCompatActivity {

    ListView productsListView;
    DbHelper dbHelper;
    MaterialMonetoringController MMC;

    ArrayList<HashMap<String, String>> all_products;
    ListAdapter doctorAdapter;
    ArrayList<HashMap<String, String>> products_array = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_material_monetoring);

        dbHelper = new DbHelper(this);
        MMC = new MaterialMonetoringController(this);

        productsListView = (ListView) findViewById(R.id.productsListView);
        all_products = MMC.SelectAllProductsPerUser();
        products_array.addAll(all_products);
        doctorAdapter = new ProductListAdapter(this, R.layout.adapter_material_monetoring, all_products);
        productsListView.setAdapter(doctorAdapter);

        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Material Monetoring");
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#A25063")));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
