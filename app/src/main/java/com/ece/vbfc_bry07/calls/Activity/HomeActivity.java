package com.ece.vbfc_bry07.calls.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.ece.vbfc_bry07.calls.Adapter.BirthdayListAdapter;
import com.ece.vbfc_bry07.calls.Controller.CallsController;
import com.ece.vbfc_bry07.calls.Controller.DbHelper;
import com.ece.vbfc_bry07.calls.Controller.DoctorsController;
import com.ece.vbfc_bry07.calls.Helpers;
import com.ece.vbfc_bry07.calls.R;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener {
    LinearLayout quick_sign, actual_coverage_plan, master_coverage_plan, doctors_information, call_report, sales_report, material_monitoring, status_summary;
    Toolbar toolbar;
    TextView username;
    ListView listBroadcastMessage;

    ArrayList<HashMap<String, String>> all_birthays;
    ListAdapter birthdaysAdapter;
    ArrayList<HashMap<String, String>> birthdays_array = new ArrayList<>();

    String current_cycle_month = "strftime('%m', date())";
    String current_cycle_year = "strftime('%Y', date())";

    SharedPreferences sharedpref;
    PieChart chart;

    Helpers helpers;
    DbHelper dbHelper;
    CallsController CC;
    DoctorsController DC;

    private float[] yData;
    private String[] xData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        dbHelper = new DbHelper(this);
        CC = new CallsController(this);
        DC = new DoctorsController(this);
        helpers = new Helpers();

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
        chart = (PieChart) findViewById(R.id.chart);
        listBroadcastMessage = (ListView) findViewById(R.id.listBroadcastMessage);

        all_birthays = DC.SelectAllBirthdaysThisMonthYear();
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

    private void addData() {
        ArrayList<Entry> yVals1 = new ArrayList<>();

        for (int i = 0; i < yData.length; i++)
            yVals1.add(new Entry(yData[i], i));

        ArrayList<String> xVals1 = new ArrayList<>();

        Collections.addAll(xVals1, xData);

        // Create pie data set
        PieDataSet dataset = new PieDataSet(yVals1, "");
        dataset.setSliceSpace(3);
        dataset.setSelectionShift(5);

        // Add many colors
        ArrayList<Integer> colors = new ArrayList<>();

        for (int c : ColorTemplate.VORDIPLOM_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.JOYFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.COLORFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.LIBERTY_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.PASTEL_COLORS)
            colors.add(c);

        colors.add(ColorTemplate.getHoloBlue());
        dataset.setColors(colors);

        // Instantiate pie data object now
        PieData data = new PieData(xVals1, dataset);
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(11f);
        data.setValueTextColor(Color.GRAY);

        chart.setData(data);

        // Undo all highlights
        chart.highlightValue(null);

        // Update pie chart
        chart.invalidate();

    }

    @Override
    protected void onResume() {
        if (!sharedpref.contains("Username")) {
            startActivity(new Intent(this, LoginActivity.class));
            this.finish();
        } else {
            // data for Pie Chart
            float Planned_Calls = CC.fetchPlannedCalls(current_cycle_month, current_cycle_year);
            final float IncidentalCalls = CC.IncidentalCalls(helpers.convertDateToCycleMonth(helpers.getCurrentDate("")));
            float RecoveredCalls = CC.RecoveredCalls(helpers.convertDateToCycleMonth(helpers.getCurrentDate("")));
            float DeclaredMissedCalls = CC.DeclaredMissedCalls(current_cycle_month, current_cycle_year);
            float ActualCoveredCalls = CC.ActualCoveredCalls(current_cycle_month);
            float UnprocessedCalls = Planned_Calls - (ActualCoveredCalls + IncidentalCalls);
            yData = new float[]{IncidentalCalls, RecoveredCalls, DeclaredMissedCalls, UnprocessedCalls, ActualCoveredCalls};
            String labelIC = "Incidental Calls " + (int) IncidentalCalls + "/" + (int) Planned_Calls;
            String labelRC = "Recovered Calls " + (int) RecoveredCalls + "/" + (int) Planned_Calls;
            String labelDMC = "Declared Missed Calls " + (int) DeclaredMissedCalls + "/" + (int) Planned_Calls;
            String labelUC = "Unprocessed Calls " + (int) UnprocessedCalls + "/" + (int) Planned_Calls;
            String labelSC = "Actual Covered Calls " + (int) ActualCoveredCalls + "/" + (int) Planned_Calls;
            xData = new String[]{labelIC, labelRC, labelDMC, labelUC, labelSC};

            // Configure Pie Chart
            chart.setUsePercentValues(true);
            chart.setDescription("Planned Calls: " + (int) Planned_Calls);

            // Enable hole and configure
            chart.setDrawHoleEnabled(true);
            chart.setHoleRadius(0);
            chart.setTransparentCircleRadius(10);

            // Enable rotation of the chart by touch
            chart.setRotationAngle(0);
            chart.setRotationEnabled(true);

            // add data
            addData();

            // Customize legends
            Legend l = chart.getLegend();
            l.setPosition(Legend.LegendPosition.BELOW_CHART_LEFT);
            l.setXEntrySpace(7);
            l.setYEntrySpace(5);
            chart.getLegend().setWordWrapEnabled(true);

            // remove labels inside the Pie Chart
            chart.setDrawSliceText(false);
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
