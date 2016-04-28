package com.ece.vbfc_bry07.calls.Dialog;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ece.vbfc_bry07.calls.Adapter.ACPCalendarAdapter;
import com.ece.vbfc_bry07.calls.Controller.PlanDetailsController;
import com.ece.vbfc_bry07.calls.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.TimeZone;

public class ViewCycleMonth extends AppCompatActivity {
    GridView acp_gridview;
    TextView cycle_number;

    Calendar calendar;

    ACPCalendarAdapter adapter;
    PlanDetailsController pdc;

    ArrayList<HashMap<String, String>> list_of_calls;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        DisplayMetrics metrics = getResources().getDisplayMetrics();
        int screenWidth = (int) (metrics.widthPixels * 0.80);

        setContentView(R.layout.activity_view_cyle_month);
        getWindow().setLayout(screenWidth, LinearLayout.LayoutParams.WRAP_CONTENT);

        acp_gridview = (GridView) findViewById(R.id.acp_gridview);
        cycle_number = (TextView) findViewById(R.id.cycle_number);

        TimeZone zone = TimeZone.getTimeZone("GMT+8");
        calendar = Calendar.getInstance(zone);

        list_of_calls = new ArrayList<>();
        pdc = new PlanDetailsController(this);

        list_of_calls = pdc.getMonthDetails(calendar.get(Calendar.MONTH) + 1);

        adapter = new ACPCalendarAdapter(this, calendar, list_of_calls);
        acp_gridview.setAdapter(adapter);

        cycle_number.setText(android.text.format.DateFormat.format("MMMM yyyy", calendar));
    }
}