package com.ece.vbfc_bry07.calls.Activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.ece.vbfc_bry07.calls.Adapter.BirthdayAdapter;
import com.ece.vbfc_bry07.calls.Controller.BroadcastsController;
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
    TextView username, no_data, call_rate, call_reach, planned_calls, incidental_calls, recovered_calls,
            declared_missed_calls, unprocessed_calls, cycle_number, count_birthday, count_broadcast;
    LinearLayout root, birthday, quick_sign, actual_coverage_plan, master_coverage_plan, doctors_information,
            call_report, sales_report, material_monitoring, status_summary, broadcast_msg;

    SharedPreferences sharedpref;

    Helpers helpers;
    DbHelper dbHelper;
    CallsController cc;
    PlansController pc;
    DoctorsController dc;
    BroadcastsController bc;

    int cycle_month;

    ArrayList<String> broadcasts;
    ArrayList<HashMap<String, String>> birthdays;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        root = (LinearLayout) findViewById(R.id.root);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        no_data = (TextView) findViewById(R.id.no_data);
        username = (TextView) findViewById(R.id.username);
        call_rate = (TextView) findViewById(R.id.call_rate);
        call_reach = (TextView) findViewById(R.id.call_reach);
        statistics = (ScrollView) findViewById(R.id.statistics);
        birthday = (LinearLayout) findViewById(R.id.birthday);
        quick_sign = (LinearLayout) findViewById(R.id.quick_sign);
        cycle_number = (TextView) findViewById(R.id.cycle_number);
        call_report = (LinearLayout) findViewById(R.id.call_report);
        planned_calls = (TextView) findViewById(R.id.planned_calls);
        sales_report = (LinearLayout) findViewById(R.id.sales_report);
        count_birthday = (TextView) findViewById(R.id.count_birthday);
        broadcast_msg = (LinearLayout) findViewById(R.id.broadcast_msg);
        count_broadcast = (TextView) findViewById(R.id.count_broadcast);
        recovered_calls = (TextView) findViewById(R.id.recovered_calls);
        incidental_calls = (TextView) findViewById(R.id.incidental_calls);
        status_summary = (LinearLayout) findViewById(R.id.status_summary);
        unprocessed_calls = (TextView) findViewById(R.id.unprocessed_calls);
        doctors_information = (LinearLayout) findViewById(R.id.doctors_information);
        material_monitoring = (LinearLayout) findViewById(R.id.material_monitoring);
        declared_missed_calls = (TextView) findViewById(R.id.declared_missed_calls);
        actual_coverage_plan = (LinearLayout) findViewById(R.id.actual_coverage_plan);
        master_coverage_plan = (LinearLayout) findViewById(R.id.master_coverage_plan);

        helpers = new Helpers();
        dbHelper = new DbHelper(this);
        cc = new CallsController(this);
        dc = new DoctorsController(this);
        pc = new PlansController(this);
        bc = new BroadcastsController(this);

        sharedpref = getSharedPreferences("ECECalls", Context.MODE_PRIVATE);
        cycle_month = helpers.convertDateToCycleMonth(helpers.getCurrentDate(""));

        setSupportActionBar(toolbar);
        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        birthday.setOnClickListener(this);
        quick_sign.setOnClickListener(this);
        call_report.setOnClickListener(this);
        sales_report.setOnClickListener(this);
        broadcast_msg.setOnClickListener(this);
        status_summary.setOnClickListener(this);
        actual_coverage_plan.setOnClickListener(this);
        master_coverage_plan.setOnClickListener(this);
        doctors_information.setOnClickListener(this);
        material_monitoring.setOnClickListener(this);

        username.setText(getUsername());
        cycle_number.setText("Cycle Statistics (Cycle " + cycle_month + ")");

        birthdays = dc.getBirthdayByDate(helpers.convertToAlphabetDate(helpers.getCurrentDate(""), "without_year"));

        if (birthdays.size() > 0) {
            count_birthday.setVisibility(View.VISIBLE);
            count_birthday.setText(birthdays.size() + "");
        } else
            count_birthday.setVisibility(View.GONE);
    }

    @Override
    protected void onResume() {
        broadcasts = bc.getBroadcastMessages();

        if (!sharedpref.contains("Username")) {
            startActivity(new Intent(this, LoginActivity.class));
            this.finish();
        } else {
            if (pc.checkIfHasPlan(cycle_month) == 0) {
                no_data.setVisibility(View.VISIBLE);
                statistics.setVisibility(View.GONE);
            } else {
                no_data.setVisibility(View.GONE);
                statistics.setVisibility(View.VISIBLE);

                int plannedCalls = cc.fetchPlannedCalls(cycle_month);
                int IncidentalCalls = cc.getCallReportDetails("incidental_call", cycle_month).size();
                int RecoveredCalls = cc.getCallReportDetails("recovered_call", cycle_month).size();
                int DeclaredMissedCalls = cc.getCallReportDetails("declared_missed_call", cycle_month).size();
                int ActualCoveredCalls = cc.getCallReportDetails("actual_covered_call", cycle_month).size();
                int UnprocessedCalls = plannedCalls - (ActualCoveredCalls + IncidentalCalls);
                String callRate = cc.callRate(cycle_month);
                String callReach = cc.callReach(cycle_month);

                planned_calls.setText(String.valueOf(plannedCalls));
                incidental_calls.setText(String.valueOf(IncidentalCalls));
                recovered_calls.setText(String.valueOf(RecoveredCalls));
                declared_missed_calls.setText(String.valueOf(DeclaredMissedCalls));
                unprocessed_calls.setText(String.valueOf(UnprocessedCalls));
                call_rate.setText(callRate);
                call_reach.setText(callReach);
            }

            if (pc.checkForDisapprovedPlans() > 0)
                broadcasts.add("* Plan has been disapproved. Tap the MCP tab to edit");

            if (broadcasts.size() > 0) {
                count_broadcast.setVisibility(View.VISIBLE);
                count_broadcast.setText(broadcasts.size() + "");
            } else
                count_broadcast.setVisibility(View.GONE);
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
                startActivity(new Intent(this, CallReportActivity.class));
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

            case R.id.birthday:
                if (birthdays.size() > 0) {
                    Dialog dialog = new Dialog(this);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setContentView(R.layout.layout_listview_only);
                    dialog.show();

                    ListView listview = (ListView) dialog.findViewById(R.id.listview);
                    listview.setAdapter(new BirthdayAdapter(this, birthdays));
                }
                break;

            case R.id.broadcast_msg:
                if (broadcasts.size() > 0) {
                    Dialog dialog = new Dialog(this);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setContentView(R.layout.layout_listview_only);
                    dialog.getWindow().setLayout(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT);
                    dialog.show();

                    ListView listview = (ListView) dialog.findViewById(R.id.listview);
                    listview.setAdapter(new ArrayAdapter<>(this, R.layout.item_plain_textview, broadcasts));
                }
                break;
        }
    }

    public String getUsername() {
        return sharedpref.getString("Username", "");
    }
}
