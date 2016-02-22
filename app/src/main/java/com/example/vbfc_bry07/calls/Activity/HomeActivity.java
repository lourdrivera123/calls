package com.example.vbfc_bry07.calls.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.vbfc_bry07.calls.R;

import java.util.Calendar;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener {
    LinearLayout quick_sign, actual_coverage_plan, master_coverage_plan, doctors_information, call_report, sales_report, material_monitoring, status_summary;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_new);

        quick_sign = (LinearLayout) findViewById(R.id.quick_sign);
        actual_coverage_plan = (LinearLayout) findViewById(R.id.actual_coverage_plan);
        master_coverage_plan = (LinearLayout) findViewById(R.id.master_coverage_plan);
        doctors_information = (LinearLayout) findViewById(R.id.doctors_information);
        call_report = (LinearLayout) findViewById(R.id.call_report);
        sales_report = (LinearLayout) findViewById(R.id.sales_report);
        material_monitoring = (LinearLayout) findViewById(R.id.material_monitoring);
        status_summary = (LinearLayout) findViewById(R.id.status_summary);

        quick_sign.setOnClickListener(this);
        actual_coverage_plan.setOnClickListener(this);
        master_coverage_plan.setOnClickListener(this);
        doctors_information.setOnClickListener(this);
        call_report.setOnClickListener(this);
        sales_report.setOnClickListener(this);
        material_monitoring.setOnClickListener(this);
        status_summary.setOnClickListener(this);
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
                Toast.makeText(this, "Material Monetoring", Toast.LENGTH_SHORT).show();
                break;
            case R.id.status_summary:
                Toast.makeText(this, "Status Summary", Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
