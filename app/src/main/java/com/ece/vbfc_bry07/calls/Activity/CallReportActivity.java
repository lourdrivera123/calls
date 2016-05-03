package com.ece.vbfc_bry07.calls.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;

import com.ece.vbfc_bry07.calls.Adapter.CallReportAdapter;
import com.ece.vbfc_bry07.calls.Controller.CallReportsController;
import com.ece.vbfc_bry07.calls.Controller.CallsController;
import com.ece.vbfc_bry07.calls.Controller.InstitutionDoctorMapsController;
import com.ece.vbfc_bry07.calls.Helpers;
import com.ece.vbfc_bry07.calls.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.TimeZone;

public class CallReportActivity extends AppCompatActivity {
    Toolbar toolbar;
    ListView list_of_call_reports;
    TextView incidental_call, declared_missed_call, call_reach, call_rate, doctor_header, prev_cycle, current_cycle;

    Helpers helpers;
    CallsController cc;
    CallReportsController crc;
    CallReportAdapter adapter;
    InstitutionDoctorMapsController idmc;

    Calendar cal;

    ArrayList<HashMap<String, String>> list_prev_month, list_current_month;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call_report);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        call_rate = (TextView) findViewById(R.id.call_rate);
        call_reach = (TextView) findViewById(R.id.call_reach);
        prev_cycle = (TextView) findViewById(R.id.prev_cycle);
        doctor_header = (TextView) findViewById(R.id.doctor_header);
        current_cycle = (TextView) findViewById(R.id.current_cycle);
        incidental_call = (TextView) findViewById(R.id.incidental_call);
        list_of_call_reports = (ListView) findViewById(R.id.list_of_call_reports);
        declared_missed_call = (TextView) findViewById(R.id.declared_missed_calls);

        helpers = new Helpers();
        cc = new CallsController(this);
        crc = new CallReportsController(this);
        idmc = new InstitutionDoctorMapsController(this);

        setSupportActionBar(toolbar);
        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Call Report as of " + helpers.convertToAlphabetDate(helpers.getCurrentDate(""), "complete"));

        prepareData();
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

    public void prepareData() {
        TimeZone zone = TimeZone.getTimeZone("GMT+8");
        cal = Calendar.getInstance(zone);
        int previous_month = (cal.get(Calendar.MONTH) + 1) - 1;
        int current_month = helpers.convertDateToCycleMonth(helpers.getCurrentDate(""));
        int IncidentalCalls = cc.IncidentalCalls(helpers.convertDateToCycleMonth(helpers.getCurrentDate("")));
        String callRate = cc.callRate(helpers.convertDateToCycleMonth(helpers.getCurrentDate("")));
        list_prev_month = crc.getMonthReport(previous_month);
        list_current_month = crc.getMonthReport(current_month);
        int prev_total = 0, prev_calls = 0, current_total = 0, current_calls = 0;

        for (int x = 0; x < list_prev_month.size(); x++) {
            prev_calls = prev_calls + Integer.parseInt(list_prev_month.get(x).get("calls"));
            prev_total = prev_total + Integer.parseInt(list_prev_month.get(x).get("total"));
        }

        for (int x = 0; x < list_current_month.size(); x++) {
            current_calls = current_calls + Integer.parseInt(list_current_month.get(x).get("calls"));
            current_total = current_total + Integer.parseInt(list_current_month.get(x).get("total"));
        }

        call_rate.setText(callRate);
        incidental_call.setText(String.valueOf(IncidentalCalls));
        doctor_header.setText("Doctors \n (" + idmc.getDoctorsWithInstitutions("").size() + ")");
        prev_cycle.setText("Cycle " + previous_month + " \n(" + prev_calls + "/" + prev_total + ")");
        current_cycle.setText("Cycle " + current_month + "\n (" + current_calls + "/" + current_total + ")");

        adapter = new CallReportAdapter(this, idmc.getDoctorsWithInstitutions(""), list_prev_month, list_current_month);
        list_of_call_reports.setAdapter(adapter);
    }
}
