package com.example.vbfc_bry07.calls.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.vbfc_bry07.calls.R;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener {
    LinearLayout quick_sign, actual_coverage_plan, master_coverage_plan, doctors_information, call_report, sales_report, material_monitoring, status_summary;
    Toolbar toolbar;
    TextView username;

    SharedPreferences sharedpref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        quick_sign = (LinearLayout) findViewById(R.id.quick_sign);
        actual_coverage_plan = (LinearLayout) findViewById(R.id.actual_coverage_plan);
        master_coverage_plan = (LinearLayout) findViewById(R.id.master_coverage_plan);
        doctors_information = (LinearLayout) findViewById(R.id.doctors_information);
        call_report = (LinearLayout) findViewById(R.id.call_report);
        sales_report = (LinearLayout) findViewById(R.id.sales_report);
        material_monitoring = (LinearLayout) findViewById(R.id.material_monitoring);
        status_summary = (LinearLayout) findViewById(R.id.status_summary);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        username = (TextView) findViewById(R.id.username);

        sharedpref = getSharedPreferences("ECECalls", Context.MODE_PRIVATE);

        setSupportActionBar(toolbar);
        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        quick_sign.setOnClickListener(this);
        actual_coverage_plan.setOnClickListener(this);
        master_coverage_plan.setOnClickListener(this);
        doctors_information.setOnClickListener(this);
        call_report.setOnClickListener(this);
        sales_report.setOnClickListener(this);
        material_monitoring.setOnClickListener(this);
        status_summary.setOnClickListener(this);

        username.setText(getUsername());
    }

    @Override
    protected void onResume() {
        if (!sharedpref.contains("Username")) {
            startActivity(new Intent(this, LoginActivity.class));
            this.finish();
        }

        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.logout, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.logout:
                SharedPreferences.Editor editor = sharedpref.edit();
                editor.clear();
                editor.apply();
                this.finish();
                startActivity(new Intent(this, LoginActivity.class));
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.quick_sign:
                Toast.makeText(this, "Quick Sign", Toast.LENGTH_SHORT).show();
                break;
            case R.id.actual_coverage_plan:
                startActivity(new Intent(this, ACPActivity.class));
                break;
            case R.id.master_coverage_plan:
                startActivity(new Intent(this, MCPActivity.class));
                break;
            case R.id.doctors_information:
                startActivity(new Intent(this, ListViewActivity.class));
                break;
            case R.id.call_report:

                Toast.makeText(this, "Call Report", Toast.LENGTH_SHORT).show();
                break;
            case R.id.sales_report:
                Toast.makeText(this, "Sales Report", Toast.LENGTH_SHORT).show();
                break;
            case R.id.material_monitoring:
                Toast.makeText(this, "Material Monitoring", Toast.LENGTH_SHORT).show();
                break;
            case R.id.status_summary:
                Toast.makeText(this, "Status Summary", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    public String getUsername() {
        return sharedpref.getString("Username", "");
    }
}
