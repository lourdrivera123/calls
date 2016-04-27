package com.ece.vbfc_bry07.calls.Activity;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;

import com.ece.vbfc_bry07.calls.Controller.CallsController;
import com.ece.vbfc_bry07.calls.Controller.DbHelper;
import com.ece.vbfc_bry07.calls.Helpers;
import com.ece.vbfc_bry07.calls.R;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;

public class StatusSummaryActivity extends AppCompatActivity implements View.OnClickListener, DatePickerDialog.OnDateSetListener {
    TextView CycleDate;

    String current_cycle_month = "strftime('%m', date())";

    PieChart chart;

    DbHelper dbHelper;
    CallsController CC;
    Helpers helpers;

    private float[] yData;
    private String[] xData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status_summary);

        dbHelper = new DbHelper(this);
        CC = new CallsController(this);
        helpers = new Helpers();

        chart = (PieChart) findViewById(R.id.chart);
        CycleDate = (TextView) findViewById(R.id.CycleDate);

        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Status Summary");
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#A25063")));

        this.CycleDate.setOnClickListener(this);

        // data for Pie Chart
        int Planned_Calls = CC.fetchPlannedCalls(helpers.convertDateToCycleMonth(helpers.getCurrentDate("")));
        int IncidentalCalls = CC.IncidentalCalls(helpers.convertDateToCycleMonth(helpers.getCurrentDate("")));
        int RecoveredCalls = CC.RecoveredCalls(helpers.convertDateToCycleMonth(helpers.getCurrentDate("")));
        int DeclaredMissedCalls = CC.DeclaredMissedCalls(current_cycle_month);
        int ActualCoveredCalls = CC.ActualCoveredCalls(current_cycle_month);
        int UnprocessedCalls = Planned_Calls - (ActualCoveredCalls + IncidentalCalls);
        yData = new float[]{IncidentalCalls, RecoveredCalls, DeclaredMissedCalls, UnprocessedCalls, ActualCoveredCalls};
        String labelIC = "Incidental Calls " + IncidentalCalls + "/" + Planned_Calls;
        String labelRC = "Recovered Calls " + RecoveredCalls + "/" + Planned_Calls;
        String labelDMC = "Declared Missed Calls " + DeclaredMissedCalls + "/" + Planned_Calls;
        String labelUC = "Unprocessed Calls " + UnprocessedCalls + "/" + Planned_Calls;
        String labelSC = "Actual Covered Calls " + ActualCoveredCalls + "/" + Planned_Calls;
        xData = new String[]{labelIC, labelRC, labelDMC, labelUC, labelSC};

        // Configure Pie Chart
        chart.setUsePercentValues(true);
        chart.setDescription("Planned Calls: " + Planned_Calls);

        // Enable hole and configure
        chart.setDrawHoleEnabled(true);
        chart.setHoleRadius(7);
        chart.setTransparentCircleRadius(10);

        // Enable rotation of the chart by touch
        chart.setRotationAngle(0);
        chart.setRotationEnabled(true);

        // add data
        addData();

        // Customize legends
        Legend l = chart.getLegend();
        l.setPosition(Legend.LegendPosition.RIGHT_OF_CHART_CENTER);
        l.setXEntrySpace(7);
        l.setYEntrySpace(5);

        // remove labels inside the Pie Chart
        chart.setDrawSliceText(false);
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
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        // createDialogWithoutDateField().show();
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);
        DatePickerDialog dialog = new DatePickerDialog(this, this, year, month, day);
        dialog.show();
    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        String selected_cycle_month;

        if (monthOfYear < 9) {
            selected_cycle_month = "0" + String.valueOf(monthOfYear + 1);
        } else {
            selected_cycle_month = String.valueOf(monthOfYear + 1);
        }

        CycleDate.setText("Cycle " + (monthOfYear + 1) + " of Year " + year);

        /* --------- CHANGE VALUE OF PIE CHART ON CHANGE OF DATE --------- */
        // data for Pie Chart
        int Planned_Calls = CC.fetchPlannedCalls(helpers.convertDateToCycleMonth(helpers.getCurrentDate("")));
        int IncidentalCalls = CC.IncidentalCalls(helpers.convertDateToCycleMonth(helpers.getCurrentDate("")));
        int RecoveredCalls = CC.RecoveredCalls(helpers.convertDateToCycleMonth(helpers.getCurrentDate("")));
        int DeclaredMissedCalls = CC.DeclaredMissedCalls(current_cycle_month);
        int ActualCoveredCalls = CC.ActualCoveredCalls(current_cycle_month);
        int UnprocessedCalls = Planned_Calls - (ActualCoveredCalls + IncidentalCalls);
        yData = new float[]{IncidentalCalls, RecoveredCalls, DeclaredMissedCalls, UnprocessedCalls, ActualCoveredCalls};
        String labelIC = "Incidental Calls " + IncidentalCalls + "/" + Planned_Calls;
        String labelRC = "Recovered Calls " + RecoveredCalls + "/" + Planned_Calls;
        String labelDMC = "Declared Missed Calls " + DeclaredMissedCalls + "/" + Planned_Calls;
        String labelUC = "Unprocessed Calls " + UnprocessedCalls + "/" + Planned_Calls;
        String labelSC = "Actual Covered Calls " + ActualCoveredCalls + "/" + Planned_Calls;
        xData = new String[]{labelIC, labelRC, labelDMC, labelUC, labelSC};

        // Configure Pie Chart
        chart.setUsePercentValues(true);
        chart.setDescription("Planned Calls: " + Planned_Calls);

        // Enable hole and configure
        chart.setDrawHoleEnabled(true);
        chart.setHoleRadius(20);
        chart.setTransparentCircleRadius(10);

        // Enable rotation of the chart by touch
        chart.setRotationAngle(0);
        chart.setRotationEnabled(true);

        // set a Chart value Listener
        chart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {

            @Override
            public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {
                // display msg when value selected
                if (e == null) {
                    return;
                }
                // Toast.makeText(StatusSummaryActivity.this, xData[e.getXIndex()] + "=" + e.getVal() + "%", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected() {

            }
        });

        // add data
        addData();

        // Customize legends
        Legend l = chart.getLegend();
        l.setPosition(Legend.LegendPosition.RIGHT_OF_CHART_CENTER);
        l.setXEntrySpace(7);
        l.setYEntrySpace(5);

        // remove labels inside the Pie Chart
        chart.setDrawSliceText(false);
        /* ---------                END                          --------- */
    }
}
