package com.ece.vbfc_bry07.calls.psr_activity;

import android.graphics.Color;

import java.util.HashMap;

import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.ece.vbfc_bry07.calls.psr_adapter.ProductListAdapter;
import com.ece.vbfc_bry07.calls.controller.DbHelper;
import com.ece.vbfc_bry07.calls.controller.MaterialMonitoringController;
import com.ece.vbfc_bry07.calls.Helpers;
import com.ece.vbfc_bry07.calls.R;

import java.util.ArrayList;

public class MaterialMonitoringActivity extends AppCompatActivity {
    DbHelper dbHelper;
    TextView cycle_month;

    Helpers helpers;
    ListView productsListView;
    ListAdapter doctorAdapter;
    MaterialMonitoringController mmc;

    ArrayList<HashMap<String, String>> all_products, call__materials;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_material_monitoring);

        cycle_month = (TextView) findViewById(R.id.cycle_month);
        productsListView = (ListView) findViewById(R.id.productsListView);

        helpers = new Helpers();
        dbHelper = new DbHelper(this);
        mmc = new MaterialMonitoringController(this);

        all_products = mmc.SelectAllProductsPerUser();
        call__materials = mmc.getCallMaterialsByMonth(helpers.convertDateToCycleMonth(helpers.getCurrentDate("")));

        doctorAdapter = new ProductListAdapter(this, all_products, call__materials);
        productsListView.setAdapter(doctorAdapter);

        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Material Monitoring");
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#A25063")));

        cycle_month.setText(helpers.getMonthYear());
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