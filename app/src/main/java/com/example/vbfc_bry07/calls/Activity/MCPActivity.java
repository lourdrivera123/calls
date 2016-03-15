package com.example.vbfc_bry07.calls.Activity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
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
import android.widget.ListView;
import android.widget.TextView;

import com.example.vbfc_bry07.calls.Adapter.CalendarAdapter;
import com.example.vbfc_bry07.calls.Adapter.ExpandableListAdapter;
import com.example.vbfc_bry07.calls.Adapter.MCPAdapter;
import com.example.vbfc_bry07.calls.Controller.CycleSetsController;
import com.example.vbfc_bry07.calls.Controller.InstitutionDoctorMapsController;
import com.example.vbfc_bry07.calls.Controller.PlanDetailsController;
import com.example.vbfc_bry07.calls.Controller.PlansController;
import com.example.vbfc_bry07.calls.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.TimeZone;

public class MCPActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener, TextWatcher, ExpandableListView.OnChildClickListener {
    Calendar cal_month;
    Dialog dialog;

    TextView tv_month, no_plans, no_records, number_of_calls;
    ImageButton prev, next, add_call;
    Toolbar toolbar;
    EditText search_doctor;
    ExpandableListView list_of_doctors;
    LinearLayout root;
    ListView list_of_calls;
    GridView gv_calendar;
    public static TextView picked_day, picked_date;

    PlansController pc;
    PlanDetailsController pdc;
    CycleSetsController csc;
    ExpandableListAdapter listAdapter;
    CalendarAdapter cal_adapter;
    MCPAdapter mcp_adapter;
    InstitutionDoctorMapsController idmc;

    List<String> listDataHeader;
    HashMap<Integer, ArrayList<HashMap<String, String>>> listDataChild, duplicate_list_child;
    HashMap<String, ArrayList<HashMap<String, String>>> hash_plans_per_day = new HashMap<>();
    ArrayList<HashMap<String, String>> institutions, doctors;
    static ArrayList<HashMap<String, String>> new_plan_details = new ArrayList<>();
    public static ArrayList<HashMap<String, ArrayList<HashMap<String, String>>>> list_of_plans;

    public static String date;
    public static int global_position;
    boolean flag = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_mcp);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        tv_month = (TextView) findViewById(R.id.tv_month);
        no_plans = (TextView) findViewById(R.id.no_plans);
        picked_day = (TextView) findViewById(R.id.picked_day);
        picked_date = (TextView) findViewById(R.id.picked_date);
        number_of_calls = (TextView) findViewById(R.id.number_of_calls);
        next = (ImageButton) findViewById(R.id.next);
        prev = (ImageButton) findViewById(R.id.prev);
        add_call = (ImageButton) findViewById(R.id.add_call);
        gv_calendar = (GridView) findViewById(R.id.gv_calendar);
        root = (LinearLayout) findViewById(R.id.root);
        list_of_calls = (ListView) findViewById(R.id.list_of_calls);

        list_of_plans = new ArrayList<>();
        new_plan_details = new ArrayList<>();
        pc = new PlansController(this);
        pdc = new PlanDetailsController(this);
        csc = new CycleSetsController(this);
        idmc = new InstitutionDoctorMapsController(this);

        setSupportActionBar(toolbar);
        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Master Coverage Plan");

        TimeZone zone = TimeZone.getTimeZone("GMT+8");
        cal_month = Calendar.getInstance(zone);

        cal_adapter = new CalendarAdapter(this, cal_month, list_of_plans);
        gv_calendar.setAdapter(cal_adapter);

        mcp_adapter = new MCPAdapter(this, new_plan_details);
        list_of_calls.setAdapter(mcp_adapter);

        tv_month.setText(android.text.format.DateFormat.format("MMMM yyyy", cal_month));
        prev.setOnClickListener(this);
        next.setOnClickListener(this);
        add_call.setOnClickListener(this);
        gv_calendar.setOnItemClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        list_of_plans = new ArrayList<>();
        hash_plans_per_day = new HashMap<>();

        if (flag) {
            getMenuInflater().inflate(R.menu.save_menu, menu);
            flag = false;
        } else {
            int month = cal_month.get(Calendar.MONTH) + 1;
            int cycle_set_id = csc.getCycleSetID(cal_month.get(Calendar.YEAR));

            int plan_id = pc.checkIfHasPlan(month, cycle_set_id);

            if (plan_id == 0) {
                add_call.setVisibility(View.GONE);
                no_plans.setVisibility(View.VISIBLE);
                no_plans.setText("No plotted plans for this month. Tap the \"+\" icon to start plotting.");
                getMenuInflater().inflate(R.menu.add_menu, menu);
            } else {
                list_of_calls.setVisibility(View.VISIBLE);
                no_plans.setVisibility(View.GONE);
                list_of_plans = pdc.getPlanDetailsByPlanID(plan_id);
                setPerDayPlans(date);
            }
            cal_adapter = new CalendarAdapter(this, cal_month, list_of_plans);
            gv_calendar.setAdapter(cal_adapter);
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
                no_plans.setVisibility(View.GONE);
                list_of_calls.setVisibility(View.VISIBLE);

                new_plan_details = new ArrayList<>();

                mcp_adapter = new MCPAdapter(this, new_plan_details);
                list_of_calls.setAdapter(mcp_adapter);
                number_of_calls.setVisibility(View.VISIBLE);
                flag = true;
                invalidateOptionsMenu();
                break;

            case R.id.save:
                addToListOfPlans();

                if (list_of_plans.size() > 0) {
                    int year = cal_month.get(Calendar.YEAR);
                    int month = cal_month.get(Calendar.MONTH) + 1;
                    long plan_id = pc.insertPlans(year, month);

                    if (plan_id > 0) {
                        if (pdc.insertPlanDetails(plan_id, list_of_plans)) {
                            Snackbar.make(root, "Successfully saved", Snackbar.LENGTH_SHORT).show();

                            cal_adapter = new CalendarAdapter(this, cal_month, list_of_plans);
                            gv_calendar.setAdapter(cal_adapter);
                        } else
                            Snackbar.make(root, "Error occurred", Snackbar.LENGTH_SHORT).show();
                    } else if (plan_id == 0)
                        Snackbar.make(root, "Error occurred", Snackbar.LENGTH_SHORT).show();
                    else
                        Snackbar.make(root, "You already have schedule for this month.", Snackbar.LENGTH_SHORT).show();
                } else
                    Snackbar.make(root, "Unable to add schedule with no content", Snackbar.LENGTH_SHORT).show();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.prev:
                if (cal_month.get(Calendar.YEAR) >= 2016 && cal_month.get(Calendar.MONTH) >= 1)
                    checkIfHasUnsaved("Previous");
                break;

            case R.id.next:
                checkIfHasUnsaved("Next");
                break;

            case R.id.add_call:
                dialog = new Dialog(this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.getWindow().setLayout(600, 600);
                dialog.setContentView(R.layout.dialog_choose_doctor);
                dialog.show();

                search_doctor = (EditText) dialog.findViewById(R.id.search_doctor);
                list_of_doctors = (ExpandableListView) dialog.findViewById(R.id.list_of_doctors);
                no_records = (TextView) dialog.findViewById(R.id.no_records);

                prepareListData();
                search_doctor.addTextChangedListener(this);
                list_of_doctors.setOnChildClickListener(this);
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        ((CalendarAdapter) parent.getAdapter()).setSelected(view, position);
        String selectedGridDate = CalendarAdapter.day_string.get(position);
        date = selectedGridDate;
        int new_pos = 0;
        number_of_calls.setText("");
        new_plan_details = new ArrayList<>();

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

        if (hash_plans_per_day.size() > 0) {
            if (list_of_plans.size() > 0)
                addToListOfPlans();
            else
                list_of_plans.add(hash_plans_per_day);
        }

        setPerDayPlans(selectedGridDate);

        hash_plans_per_day = new HashMap<>();
        global_position = new_pos;
    }

    private void setPerDayPlans(String date) {
        if (list_of_plans.size() > 0) {
            int count = 0;

            for (int x = 0; x < list_of_plans.size(); x++) {
                ArrayList<HashMap<String, String>> array_per_day = list_of_plans.get(x).get(date);

                if (array_per_day != null) {
                    for (int y = 0; y < array_per_day.size(); y++) {
                        new_plan_details.add(array_per_day.get(y));
                        count += 1;
                    }
                }
            }
            if (count > 0)
                number_of_calls.setText(count + " call/s for this day");
        }

        mcp_adapter = new MCPAdapter(this, new_plan_details);
        list_of_calls.setAdapter(mcp_adapter);
    }

    private void prepareListData() {
        institutions = idmc.getInstitutions("");
        doctors = idmc.getDoctorsWithInstitutions("");
        listDataHeader = new ArrayList<>();
        duplicate_list_child = new HashMap<>();
        listDataChild = new HashMap<>();

        for (int x = 0; x < institutions.size(); x++) {
            listDataHeader.add(institutions.get(x).get("institution_name"));
            ArrayList<HashMap<String, String>> array = new ArrayList<>();

            for (int y = 0; y < doctors.size(); y++) {
                if (doctors.get(y).get("doctor_inst_id").equals(institutions.get(x).get("institution_id")))
                    array.add(doctors.get(y));
            }
            listDataChild.put(x, array);
        }

        duplicate_list_child.putAll(listDataChild);
        listAdapter = new ExpandableListAdapter(this, listDataHeader, listDataChild);
        list_of_doctors.setAdapter(listAdapter);
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
        int counter = 0;

        if (!search.equals("")) {
            HashMap<Integer, ArrayList<HashMap<String, String>>> new_child = new HashMap<>();
            new_child.putAll(duplicate_list_child);
            listDataHeader.clear();
            listDataChild.clear();

            for (int x = 0; x < new_child.size(); x++) {
                ArrayList<HashMap<String, String>> new_array = new_child.get(x);
                ArrayList<HashMap<String, String>> array_for_child = new ArrayList<>();

                for (int y = 0; y < new_array.size(); y++) {
                    HashMap<String, String> hash = new_array.get(y);

                    if (hash.get("doc_name").toLowerCase().contains(search.toLowerCase()))
                        array_for_child.add(hash);
                }

                if (array_for_child.size() > 0) {
                    listDataChild.put(counter, array_for_child);
                    counter += 1;
                }
            }

            for (int x = 0; x < listDataChild.size(); x++) {
                int inst_id = Integer.parseInt(listDataChild.get(x).get(0).get("doctor_inst_id"));
                listDataHeader.add(idmc.getInstitutionByID(inst_id));
            }

            if (listDataHeader.size() > 0) {
                no_records.setVisibility(View.GONE);
                list_of_doctors.setVisibility(View.VISIBLE);
                listAdapter = new ExpandableListAdapter(this, listDataHeader, listDataChild);
                list_of_doctors.setAdapter(listAdapter);
            } else {
                no_records.setVisibility(View.VISIBLE);
                list_of_doctors.setVisibility(View.GONE);
            }
        } else
            prepareListData();
    }

    @Override
    public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
        HashMap<String, String> map = listDataChild.get(groupPosition).get(childPosition);
        new_plan_details.add(map);
        hash_plans_per_day.put(date, new_plan_details);

        number_of_calls.setText(hash_plans_per_day.get(date).size() + " call/s for this day");
        mcp_adapter.notifyDataSetChanged();
        dialog.dismiss();
        return true;
    }

    void checkIfHasUnsaved(final String where) {
        int check = 0;

        if (flag) {
            if (list_of_plans.size() > 0 || hash_plans_per_day.size() > 0) {
                AlertDialog.Builder alert = new AlertDialog.Builder(this);
                alert.setMessage("You have made changes. Are you sure you want to navigate away and lose your changes?");
                alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (where.equals("Next"))
                            setNextMonth();
                        else
                            setPreviousMonth();
                        refreshCalendar();
                    }
                });
                alert.setNegativeButton("No", null);
                alert.show();
            } else
                check += 1;
        } else
            check += 1;

        if (check > 0) {
            if (where.equals("Next"))
                setNextMonth();
            else
                setPreviousMonth();
            refreshCalendar();
        }
    }

    protected void setNextMonth() {
        list_of_plans = new ArrayList<>();
        hash_plans_per_day = new HashMap<>();

        if (cal_month.get(Calendar.MONTH) == cal_month.getActualMaximum(Calendar.MONTH))
            cal_month.set((cal_month.get(Calendar.YEAR) + 1), cal_month.getActualMinimum(Calendar.MONTH), 1);
        else
            cal_month.set(Calendar.MONTH, cal_month.get(Calendar.MONTH) + 1);
    }

    protected void setPreviousMonth() {
        list_of_plans = new ArrayList<>();
        hash_plans_per_day = new HashMap<>();

        if (cal_month.get(Calendar.MONTH) == cal_month.getActualMinimum(Calendar.MONTH))
            cal_month.set((cal_month.get(Calendar.YEAR) - 1), cal_month.getActualMaximum(Calendar.MONTH), 1);
        else
            cal_month.set(Calendar.MONTH, cal_month.get(Calendar.MONTH) - 1);
    }

    public void refreshCalendar() {
        setPerDayPlans(date);

        invalidateOptionsMenu();
        cal_adapter.refreshDays();
        cal_adapter.notifyDataSetChanged();
        tv_month.setText(android.text.format.DateFormat.format("MMMM yyyy", cal_month));
    }

    void addToListOfPlans() {
        int count = 0;

        for (int x = 0; x < list_of_plans.size(); x++) {
            String hash_keyset = hash_plans_per_day.keySet().toString().replace("[", "").replace("]", "");

            if (list_of_plans.get(x).get(hash_keyset) != null) {
                count += 1;
                list_of_plans.set(x, hash_plans_per_day);
                break;
            }
        }

        if (hash_plans_per_day.size() > 0) {
            if (count == 0)
                list_of_plans.add(hash_plans_per_day);
        }
    }
}