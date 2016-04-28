package com.ece.vbfc_bry07.calls.Activity;

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
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.ece.vbfc_bry07.calls.Adapter.BirthdayListAdapter;
import com.ece.vbfc_bry07.calls.Controller.CallsController;
import com.ece.vbfc_bry07.calls.Controller.DbHelper;
import com.ece.vbfc_bry07.calls.Controller.DoctorsController;
import com.ece.vbfc_bry07.calls.Controller.PlansController;
import com.ece.vbfc_bry07.calls.Helpers;
import com.ece.vbfc_bry07.calls.R;

import java.util.ArrayList;
import java.util.HashMap;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener {
    Toolbar toolbar;
    ScrollView statistics;
    ListView listBroadcastMessage;
    TextView username, no_data, call_rate, call_reach, planned_calls, incidental_calls, recovered_calls, declared_missed_calls, unprocessed_calls;
    LinearLayout quick_sign, actual_coverage_plan, master_coverage_plan, doctors_information, call_report, sales_report, material_monitoring, status_summary;

    ArrayList<HashMap<String, String>> all_birthays;
    ListAdapter birthdaysAdapter;
    ArrayList<HashMap<String, String>> birthdays_array = new ArrayList<>();

    SharedPreferences sharedpref;

    Helpers helpers;
    DbHelper dbHelper;
    CallsController cc;
    PlansController pc;
    DoctorsController dc;

    String current_cycle_month = "strftime('%m', date())";

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
        listBroadcastMessage = (ListView) findViewById(R.id.listBroadcastMessage);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        username = (TextView) findViewById(R.id.username);
        no_data = (TextView) findViewById(R.id.no_data);
        call_rate = (TextView) findViewById(R.id.call_rate);
        call_reach = (TextView) findViewById(R.id.call_reach);
        planned_calls = (TextView) findViewById(R.id.planned_calls);
        incidental_calls = (TextView) findViewById(R.id.incidental_calls);
        recovered_calls = (TextView) findViewById(R.id.recovered_calls);
        declared_missed_calls = (TextView) findViewById(R.id.declared_missed_calls);
        unprocessed_calls = (TextView) findViewById(R.id.unprocessed_calls);
        statistics = (ScrollView) findViewById(R.id.statistics);

        helpers = new Helpers();
        dbHelper = new DbHelper(this);
        cc = new CallsController(this);
        dc = new DoctorsController(this);
        pc = new PlansController(this);

        all_birthays = dc.SelectAllBirthdaysThisMonthYear();
        birthdays_array.addAll(all_birthays);
        birthdaysAdapter = new BirthdayListAdapter(this, R.layout.adapter_birthdays, all_birthays);
        listBroadcastMessage.setAdapter(birthdaysAdapter);

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
        } else {
            if (pc.checkIfHasPlan(helpers.convertDateToCycleMonth(helpers.getCurrentDate("date"))) == 0) {
                no_data.setVisibility(View.VISIBLE);
                statistics.setVisibility(View.GONE);
            } else {
                no_data.setVisibility(View.GONE);
                statistics.setVisibility(View.VISIBLE);

                int plannedCalls = cc.fetchPlannedCalls(helpers.convertDateToCycleMonth(helpers.getCurrentDate("")));
                int IncidentalCalls = cc.IncidentalCalls(helpers.convertDateToCycleMonth(helpers.getCurrentDate("")));
                int RecoveredCalls = cc.RecoveredCalls(helpers.convertDateToCycleMonth(helpers.getCurrentDate("")));
                int DeclaredMissedCalls = cc.DeclaredMissedCalls(current_cycle_month);
                int ActualCoveredCalls = cc.ActualCoveredCalls(current_cycle_month);
                int UnprocessedCalls = plannedCalls - (ActualCoveredCalls + IncidentalCalls);
                String callRate = cc.callRate(helpers.convertDateToCycleMonth(helpers.getCurrentDate("")));

                planned_calls.setText(String.valueOf(plannedCalls));
                incidental_calls.setText(String.valueOf(IncidentalCalls));
                recovered_calls.setText(String.valueOf(RecoveredCalls));
                declared_missed_calls.setText(String.valueOf(DeclaredMissedCalls));
                unprocessed_calls.setText(String.valueOf(UnprocessedCalls));
                call_rate.setText(callRate);
            }
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

            case R.id.sync:

                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.quick_sign:
                startActivity(new Intent(this, QuickSignActivity.class));
                break;
            case R.id.actual_coverage_plan:
                startActivity(new Intent(this, ACPActivity.class));
                break;
            case R.id.master_coverage_plan:
                startActivity(new Intent(this, MCPActivity.class));
                break;
            case R.id.doctors_information:
                startActivity(new Intent(this, DoctorsInfoActivity.class));
                break;
            case R.id.call_report:
                break;
            case R.id.sales_report:
                startActivity(new Intent(this, SalesReportActivity.class));
                break;
            case R.id.material_monitoring:
                startActivity(new Intent(this, MaterialMonitoringActivity.class));
                break;
            case R.id.status_summary:
                startActivity(new Intent(this, StatusSummaryActivity.class));
                break;
        }
    }

    public String getUsername() {
        return sharedpref.getString("Username", "");
    }

}
