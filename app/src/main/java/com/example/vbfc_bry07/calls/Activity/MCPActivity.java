package com.example.vbfc_bry07.calls.Activity;

import android.app.Dialog;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.vbfc_bry07.calls.Adapter.CalendarAdapter;
import com.example.vbfc_bry07.calls.Adapter.ExpandableListAdapter;
import com.example.vbfc_bry07.calls.CalendarCollection;
import com.example.vbfc_bry07.calls.Controller.PlansController;
import com.example.vbfc_bry07.calls.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.TimeZone;

public class MCPActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener, TextWatcher {
    GregorianCalendar cal_month, cal_month_copy;
    CalendarAdapter cal_adapter;

    TextView tv_month, no_plans;
    public static TextView picked_day, picked_date;
    ImageButton prev, next, add_call;
    GridView gv_calendar;
    Toolbar toolbar;
    EditText search_doctor;
    ExpandableListView list_of_doctors;
    LinearLayout root;

    CallsController cc;
    ExpandableListAdapter listAdapter;

    List<String> listDataHeader;
    HashMap<Integer, ArrayList<HashMap<String, String>>> listDataChild;
    PlansController cc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_mcp);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        tv_month = (TextView) findViewById(R.id.tv_month);
        no_plans = (TextView) findViewById(R.id.no_plans);
        picked_day = (TextView) findViewById(R.id.picked_day);
        picked_date = (TextView) findViewById(R.id.picked_date);
        next = (ImageButton) findViewById(R.id.next);
        prev = (ImageButton) findViewById(R.id.prev);
        add_call = (ImageButton) findViewById(R.id.add_call);
        gv_calendar = (GridView) findViewById(R.id.gv_calendar);
        root = (LinearLayout) findViewById(R.id.root);

        prepareListData();
        cc = new PlansController(this);

        setSupportActionBar(toolbar);
        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Master Coverage Plan");

        cal_month = (GregorianCalendar) GregorianCalendar.getInstance();
        cal_month.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        cal_month_copy = (GregorianCalendar) cal_month.clone();
        cal_adapter = new CalendarAdapter(this, cal_month, CalendarCollection.date_collection_arr);
        gv_calendar.setAdapter(cal_adapter);

        tv_month.setText(android.text.format.DateFormat.format("MMMM yyyy", cal_month));
        prev.setOnClickListener(this);
        next.setOnClickListener(this);
        add_call.setOnClickListener(this);
        gv_calendar.setOnItemClickListener(this);
    }

    void setCalls() {
        CalendarCollection.date_collection_arr = new ArrayList<>();
        CalendarCollection.date_collection_arr.add(new CalendarCollection("2016-02-19", "John Birthday"));
        CalendarCollection.date_collection_arr.add(new CalendarCollection("2016-02-02", "Live Event and Concert of sonu"));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.save_menu, menu);

        if (!cc.checkIfHasPlan(cal_month.get(Calendar.MONTH), cal_month.get(Calendar.YEAR))) {
            add_call.setVisibility(View.GONE);
            no_plans.setText("No plotted plans for this month. Tap the \"+\" icon to start plotting.");
            getMenuInflater().inflate(R.menu.add_menu, menu);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                break;

            case R.id.add:
                add_call.setVisibility(View.VISIBLE);
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.prev:
                if (cal_month.get(Calendar.YEAR) >= 2016 && cal_month.get(Calendar.MONTH) >= 1) {
                    setPreviousMonth();
                    refreshCalendar();
                }
                break;

            case R.id.next:
                setNextMonth();
                refreshCalendar();
                break;

            case R.id.add_call:
                Dialog dialog = new Dialog(this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.dialog_choose_doctor);
                dialog.show();

                search_doctor = (EditText) dialog.findViewById(R.id.search_doctor);
                list_of_doctors = (ExpandableListView) dialog.findViewById(R.id.list_of_doctors);

                list_of_doctors.setAdapter(listAdapter);
                search_doctor.addTextChangedListener(this);
                break;
        }
    }

    protected void setNextMonth() {
        if (cal_month.get(GregorianCalendar.MONTH) == cal_month.getActualMaximum(GregorianCalendar.MONTH))
            cal_month.set((cal_month.get(GregorianCalendar.YEAR) + 1), cal_month.getActualMinimum(GregorianCalendar.MONTH), 1);
        else
            cal_month.set(GregorianCalendar.MONTH, cal_month.get(GregorianCalendar.MONTH) + 1);

        invalidateOptionsMenu();
    }

    protected void setPreviousMonth() {
        if (cal_month.get(GregorianCalendar.MONTH) == cal_month.getActualMinimum(GregorianCalendar.MONTH))
            cal_month.set((cal_month.get(GregorianCalendar.YEAR) - 1), cal_month.getActualMaximum(GregorianCalendar.MONTH), 1);
        else
            cal_month.set(GregorianCalendar.MONTH, cal_month.get(GregorianCalendar.MONTH) - 1);
        invalidateOptionsMenu();
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
        int new_pos;

        String[] separatedTime = selectedGridDate.split("-");
        String gridvalueString = separatedTime[2].replaceFirst("^0*", "");
        int gridvalue = Integer.parseInt(gridvalueString);

        if ((gridvalue > 10) && (position < 8)) {
            if (cal_month.get(Calendar.YEAR) >= 2016 && cal_month.get(Calendar.MONTH) >= 1) {
                setPreviousMonth();
                refreshCalendar();
            }
        } else if ((gridvalue < 7) && (position > 28)) {
            setNextMonth();
            refreshCalendar();
            new_pos = position - 28;

            ((CalendarAdapter) parent.getAdapter()).setSelected(gv_calendar.getChildAt(new_pos), new_pos);
        }

        ((CalendarAdapter) parent.getAdapter()).getPositionList(selectedGridDate);
    }

    private void prepareListData() {
        listDataHeader = new ArrayList<>();
        listDataChild = new HashMap<>();

        listDataHeader.add("Davao Doctors Hospital");
        listDataHeader.add("SPMC");
        listDataHeader.add("San Pedro Hospital");

        // Adding child data
        ArrayList<HashMap<String, String>> array = new ArrayList<>();
        HashMap<String, String> davaoDoc = new HashMap<>();
        davaoDoc.put("doc_id", "1");
        davaoDoc.put("doc_name", "Camahalan, Royette");
        array.add(davaoDoc);

        ArrayList<HashMap<String, String>> array1 = new ArrayList<>();
        HashMap<String, String> spmc = new HashMap<>();
        spmc.put("doc_id", "2");
        spmc.put("doc_name", "Villarel, Mary Joy");
        array1.add(spmc);

        ArrayList<HashMap<String, String>> array2 = new ArrayList<>();
        HashMap<String, String> sanpedro = new HashMap<>();
        sanpedro.put("doc_id", "3");
        sanpedro.put("doc_name", "Barnes, Jared Madison");
        array2.add(sanpedro);

        HashMap<String, String> sanpedro2 = new HashMap<>();
        sanpedro2.put("doc_id", "4");
        sanpedro2.put("doc_name", "Valencia, Almira");
        array2.add(sanpedro2);

        listDataChild.put(0, array); // Header, Child data
        listDataChild.put(1, array1);
        listDataChild.put(2, array2);

        listAdapter = new ExpandableListAdapter(this, listDataHeader, listDataChild);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        String search = String.valueOf(s);
        ArrayList<String> names = new ArrayList<>();

        for (int x = 0; x < listDataChild.size(); x++) {
            ArrayList<HashMap<String, String>> new_array = listDataChild.get(x);

            for (int y = 0; y < new_array.size(); y++) {
                HashMap<String, String> hash = new_array.get(y);
                names.add(hash.get("doc_name"));
            }
        }

//        for(s : names) {
//
//        }
    }
}