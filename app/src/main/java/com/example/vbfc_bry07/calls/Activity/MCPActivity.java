package com.example.vbfc_bry07.calls.Activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.vbfc_bry07.calls.Adapter.CalendarAdapter;
import com.example.vbfc_bry07.calls.CalendarCollection;
import com.example.vbfc_bry07.calls.R;

import java.util.ArrayList;
import java.util.GregorianCalendar;

public class MCPActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener {
    GregorianCalendar cal_month, cal_month_copy;
    CalendarAdapter cal_adapter;

    TextView tv_month;
    ImageButton prev, next;
    GridView gv_calendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_mcp);

        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Master Coverage Plan");

        tv_month = (TextView) findViewById(R.id.tv_month);
        next = (ImageButton) findViewById(R.id.next);
        prev = (ImageButton) findViewById(R.id.prev);
        gv_calendar = (GridView) findViewById(R.id.gv_calendar);

        setCalls();

        cal_month = (GregorianCalendar) GregorianCalendar.getInstance();
        cal_month_copy = (GregorianCalendar) cal_month.clone();
        cal_adapter = new CalendarAdapter(this, cal_month, CalendarCollection.date_collection_arr);
        gv_calendar.setAdapter(cal_adapter);

        tv_month.setText(android.text.format.DateFormat.format("MMMM yyyy", cal_month));
        prev.setOnClickListener(this);
        next.setOnClickListener(this);
        gv_calendar.setOnItemClickListener(this);
    }

    void setCalls() {
        CalendarCollection.date_collection_arr = new ArrayList<>();
        CalendarCollection.date_collection_arr.add(new CalendarCollection("2016-02-19", "John Birthday"));
        CalendarCollection.date_collection_arr.add(new CalendarCollection("2016-02-02", "Live Event and Concert of sonu"));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.save_menu, menu);
//        getMenuInflater().inflate(R.menu.add_menu, menu);
        return true;
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
        switch (v.getId()) {
            case R.id.prev:
                setPreviousMonth();
                refreshCalendar();
                break;

            case R.id.next:
                setNextMonth();
                refreshCalendar();
                break;
        }
    }

    protected void setNextMonth() {
        if (cal_month.get(GregorianCalendar.MONTH) == cal_month.getActualMaximum(GregorianCalendar.MONTH))
            cal_month.set((cal_month.get(GregorianCalendar.YEAR) + 1), cal_month.getActualMinimum(GregorianCalendar.MONTH), 1);
        else
            cal_month.set(GregorianCalendar.MONTH, cal_month.get(GregorianCalendar.MONTH) + 1);
    }

    protected void setPreviousMonth() {
        if (cal_month.get(GregorianCalendar.MONTH) == cal_month.getActualMinimum(GregorianCalendar.MONTH))
            cal_month.set((cal_month.get(GregorianCalendar.YEAR) - 1), cal_month.getActualMaximum(GregorianCalendar.MONTH), 1);
        else
            cal_month.set(GregorianCalendar.MONTH, cal_month.get(GregorianCalendar.MONTH) - 1);
    }

    public void refreshCalendar() {
        cal_adapter.refreshDays();
        cal_adapter.notifyDataSetChanged();
        tv_month.setText(android.text.format.DateFormat.format("MMMM yyyy", cal_month));
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        ((CalendarAdapter) parent.getAdapter()).setSelected(view, position);
        String selectedGridDate = CalendarAdapter.day_string.get(position);

        String[] separatedTime = selectedGridDate.split("-");
        String gridvalueString = separatedTime[2].replaceFirst("^0*", "");
        int gridvalue = Integer.parseInt(gridvalueString);

        if ((gridvalue > 10) && (position < 8)) {
            setPreviousMonth();
            refreshCalendar();
        } else if ((gridvalue < 7) && (position > 28)) {
            setNextMonth();
            refreshCalendar();
        }

        ((CalendarAdapter) parent.getAdapter()).setSelected(view, position);
        ((CalendarAdapter) parent.getAdapter()).getPositionList(selectedGridDate);
    }
}