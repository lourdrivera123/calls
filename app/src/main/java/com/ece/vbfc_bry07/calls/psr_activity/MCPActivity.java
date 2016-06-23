package com.ece.vbfc_bry07.calls.psr_activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDialog;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.ece.vbfc_bry07.calls.psr_adapter.ExpandableListAdapter;
import com.ece.vbfc_bry07.calls.psr_adapter.MCPAdapter;
import com.ece.vbfc_bry07.calls.psr_adapter.MCPCalendarAdapter;
import com.ece.vbfc_bry07.calls.controller.InstitutionDoctorMapsController;
import com.ece.vbfc_bry07.calls.controller.PlanDetailsController;
import com.ece.vbfc_bry07.calls.controller.PlansController;
import com.ece.vbfc_bry07.calls.Helpers;
import com.ece.vbfc_bry07.calls.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.TimeZone;

public class MCPActivity extends AppCompatActivity implements ExpandableListView.OnChildClickListener, TextWatcher, View.OnClickListener {
    Toolbar toolbar;
    GridView gv_calendar;
    EditText search_doctor;
    ImageButton prev, next;
    static ListView calls_per_day;
    ExpandableListView list_of_doctors;
    static LinearLayout doc_details, root;
    static TextView tv_month, number_of_calls;
    public static TextView picked_date, picked_day;
    public static LinearLayout change_view, all_doctors;
    TextView no_records, no_plans, class_code, specialization;

    View _lastColored;
    static Calendar cal_month;

    static boolean flag;
    public static int isVisible;

    PlansController pc;
    static Helpers helpers;
    PlanDetailsController pdc;
    static MCPActivity context;
    static MCPAdapter plans_adapter;
    InstitutionDoctorMapsController idmc;
    ExpandableListAdapter doctor_adapter;
    static MCPCalendarAdapter cal_adapter;

    List<String> listDataHeader;
    public static ArrayList<String> list_of_dates;
    public static HashMap<String, String> child_clicked;
    public static ArrayList<HashMap<String, ArrayList<String>>> list_of_plans;
    HashMap<Integer, ArrayList<HashMap<String, String>>> listDataChild, duplicate_list_child;
    public static ArrayList<HashMap<String, ArrayList<HashMap<String, String>>>> list_of_plans_per_day;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mcp);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        tv_month = (TextView) findViewById(R.id.tv_month);
        no_plans = (TextView) findViewById(R.id.no_plans);
        picked_day = (TextView) findViewById(R.id.picked_day);
        picked_date = (TextView) findViewById(R.id.picked_date);
        no_records = (TextView) findViewById(R.id.no_records);
        specialization = (TextView) findViewById(R.id.specialization);
        class_code = (TextView) findViewById(R.id.class_code);
        number_of_calls = (TextView) findViewById(R.id.number_of_calls);
        next = (ImageButton) findViewById(R.id.next);
        prev = (ImageButton) findViewById(R.id.prev);
        calls_per_day = (ListView) findViewById(R.id.calls_per_day);
        search_doctor = (EditText) findViewById(R.id.search_doctor);
        gv_calendar = (GridView) findViewById(R.id.gv_calendar);
        root = (LinearLayout) findViewById(R.id.root);
        all_doctors = (LinearLayout) findViewById(R.id.all_doctors);
        change_view = (LinearLayout) findViewById(R.id.change_view);
        doc_details = (LinearLayout) findViewById(R.id.doc_details);
        list_of_doctors = (ExpandableListView) findViewById(R.id.list_of_doctors);

        setSupportActionBar(toolbar);
        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Master Coverage Plan");

        TimeZone zone = TimeZone.getTimeZone("GMT+8");
        cal_month = Calendar.getInstance(zone);

        flag = false;
        isVisible = 0;
        context = this;
        list_of_plans = new ArrayList<>();
        list_of_dates = new ArrayList<>();

        helpers = new Helpers();
        pc = new PlansController(this);
        pdc = new PlanDetailsController(this);
        idmc = new InstitutionDoctorMapsController(this);

        cal_adapter = new MCPCalendarAdapter(this, cal_month);
        gv_calendar.setAdapter(cal_adapter);

        tv_month.setText(android.text.format.DateFormat.format("MMMM yyyy", cal_month));
        list_of_doctors.setOnChildClickListener(this);
        search_doctor.addTextChangedListener(this);
        next.setOnClickListener(this);
        prev.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        int year = cal_month.get(Calendar.YEAR);
        int month = cal_month.get(Calendar.MONTH) + 1;
        long plan_id = pc.checkForPlanByMonthYear(year, month);

        if (flag) {
            no_plans.setVisibility(View.GONE);
            getMenuInflater().inflate(R.menu.save_menu, menu);

            if (plan_id > 0 && pc.checkPlanIfApproved(plan_id) == 2) {
                all_doctors.setVisibility(View.VISIBLE);
                prepareListData();
                list_of_plans = pdc.getPlanDetailsByPID(plan_id);
            }
        } else {
            no_plans.setVisibility(View.VISIBLE);

            if (plan_id == 0) { //WALA PAY NA RECEIVE NA PERMISSION GIKAN SA SERVER
                no_plans.setText("You are not yet allowed to plot plan for this month");
                no_plans.setTextColor(Color.parseColor("#808080"));
                all_doctors.setVisibility(View.GONE);
                doc_details.setVisibility(View.GONE);
            } else { //ALLOWED NA MAG PLOT UG PLAN OR GI DISAPPROVED ANG PLAN
                if (pc.checkPlanIfApproved(plan_id) == 2) {
                    getMenuInflater().inflate(R.menu.edit_white_menu, menu);
                    no_plans.setText("Plan has been disapproved. Tap the edit icon to update plan");
                    no_plans.setTextColor(Color.RED);
                } else if (pc.checkPlanIfApproved(plan_id) == 1 || pc.checkPlanIfApproved(plan_id) == 0) {
                    if (pdc.checkPlanDetailsByPlanID(plan_id)) {
                        no_plans.setVisibility(View.GONE);
                        all_doctors.setVisibility(View.VISIBLE);
                        getMenuInflater().inflate(R.menu.view_all, menu);
                        prepareListData();
                        list_of_plans = pdc.getPlanDetailsByPID(plan_id);
                    } else {
                        no_plans.setText("No plotted plan for this month. Tap the \"+\" icon to start plotting.");
                        no_plans.setTextColor(Color.parseColor("#236B8E"));
                        getMenuInflater().inflate(R.menu.add_menu, menu);
                    }
                }
            }

            cal_adapter = new MCPCalendarAdapter(this, cal_month);
            gv_calendar.setAdapter(cal_adapter);
        }

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int year = cal_month.get(Calendar.YEAR);
        int month = cal_month.get(Calendar.MONTH) + 1;

        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                break;

            case R.id.add:
                no_plans.setVisibility(View.GONE);
                all_doctors.setVisibility(View.VISIBLE);
                prepareListData();
                flag = true;
                invalidateOptionsMenu();
                break;

            case R.id.save:
                if (list_of_plans.size() > 0) {
                    AppCompatDialog dialog = new AppCompatDialog(this);
                    dialog.setCancelable(false);
                    dialog.show();
                    long plan_id = pc.checkForPlanByMonthYear(year, month);

                    if (plan_id > 0) {
                        if (pc.checkPlanIfApproved(plan_id) == 2) {
                            pdc.deletePlanDetails(plan_id);
                            pc.updatePlanStatus(plan_id);
                        }

                        if (pdc.savePlanDetails(plan_id, list_of_plans)) {
                            Snackbar.make(root, "Successfully saved", Snackbar.LENGTH_SHORT).show();
                            flag = false;
                            cal_adapter = new MCPCalendarAdapter(this, cal_month);
                            gv_calendar.setAdapter(cal_adapter);
                            invalidateOptionsMenu();
                        } else
                            Snackbar.make(root, "Error occurred", Snackbar.LENGTH_SHORT).show();

                        dialog.dismiss();
                    } else if (plan_id == 0)
                        Snackbar.make(root, "Error occurred", Snackbar.LENGTH_SHORT).show();
                } else
                    Snackbar.make(root, "Unable to add schedule with no content", Snackbar.LENGTH_SHORT).show();

                break;

            case R.id.cancel:
                if (list_of_plans.size() > 0) {
                    AlertDialog.Builder alert = new AlertDialog.Builder(this);
                    alert.setMessage("You have made changes. Are you sure you want to navigate away and lose your changes?");
                    alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            MCPActivity.this.finish();
                        }
                    });
                    alert.setNegativeButton("No", null);
                    alert.show();
                } else
                    this.finish();

                break;

            case R.id.view_all:
                if (all_doctors.getVisibility() == View.VISIBLE) {
                    change_view.setVisibility(View.VISIBLE);
                    all_doctors.setVisibility(View.GONE);
                    doc_details.setVisibility(View.GONE);
                    int planID = pc.getPlanID(month);
                    String first_date = helpers.getFirstDateOfMonth(month - 1);

                    picked_date.setText(helpers.convertToAlphabetDate(first_date, ""));
                    picked_day.setText(helpers.convertToDayOfWeek(first_date));
                    list_of_plans_per_day = pdc.getPlanDetailsByPlanID(planID);

                    changeArrayContent(first_date);
                    cal_adapter.notifyDataSetChanged();
                } else {
                    change_view.setVisibility(View.GONE);
                    all_doctors.setVisibility(View.VISIBLE);
                }
                break;

            case R.id.copy_from:
                int previous_month = (cal_month.get(Calendar.MONTH) + 1) - 1;
                int plan_id = pc.getPlanID(previous_month);
                ArrayList<HashMap<String, ArrayList<String>>> previous_mcp = pdc.getPlanDetailsByPID(plan_id), new_mcp = new ArrayList<>();

                if (previous_mcp.size() > 0) {
                    for (int x = 0; x < previous_mcp.size(); x++) {
                        String keyset = previous_mcp.get(x).keySet().toString().replace("[", "").replace("]", "");
                        HashMap<String, ArrayList<String>> per_IDM_id = new HashMap<>();
                        ArrayList<String> array_date = new ArrayList<>();

                        for (int y = 0; y < previous_mcp.get(x).get(keyset).size(); y++) {
                            String date = helpers.Add1MonthToDate(previous_mcp.get(x).get(keyset).get(y));
                            array_date.add(date);
                        }

                        per_IDM_id.put(keyset, array_date);
                        new_mcp.add(per_IDM_id);
                    }
                } else
                    Snackbar.make(root, "No data available for previous month", Snackbar.LENGTH_SHORT).show();

                list_of_plans = new ArrayList<>();
                list_of_plans.addAll(new_mcp);

                cal_adapter = new MCPCalendarAdapter(this, cal_month);
                gv_calendar.setAdapter(cal_adapter);
                break;

            case R.id.edit:
                flag = true;
                invalidateOptionsMenu();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onChildClick(ExpandableListView parent, View v, int groupPosition,
                                int childPosition, long id) {
        doc_details.setVisibility(View.VISIBLE);
        child_clicked = listDataChild.get(groupPosition).get(childPosition);
        String IDM_id = child_clicked.get("IDM_id");
        list_of_dates = new ArrayList<>();
        isVisible = 0;
        cal_adapter.notifyDataSetChanged();

        class_code.setText("CLASS: " + child_clicked.get("class_name") + " (" + child_clicked.get("class_code") + "x)");
        specialization.setText("SPECIALIZATION: " + child_clicked.get("specialization"));

        if (_lastColored != null) {
            _lastColored.setBackgroundColor(Color.TRANSPARENT);
            _lastColored.invalidate();
        }

        _lastColored = v;
        v.setBackgroundColor(Color.parseColor("#e8dece"));

        for (int x = 0; x < list_of_plans.size(); x++) {
            String keyset = list_of_plans.get(x).keySet().toString().replace("[", "").replace("]", "");

            if (keyset.equals(IDM_id)) {
                list_of_dates.addAll(list_of_plans.get(x).get(keyset));
                cal_adapter.notifyDataSetChanged();
                break;
            }
        }

        return false;
    }

    private void prepareListData() {
        ProgressDialog progress = new ProgressDialog(this);
        progress.setMessage("Please wait...");
        progress.setCancelable(false);
        progress.show();

        listDataChild = new HashMap<>();
        listDataHeader = new ArrayList<>();
        duplicate_list_child = new HashMap<>();
        Set<String> uniqueInstitutions = new LinkedHashSet<>();
        ArrayList<HashMap<String, String>> doctors = idmc.getDoctorsWithInstitutions("");

        for (int x = 0; x < doctors.size(); x++)
            uniqueInstitutions.add(doctors.get(x).get("inst_name"));

        listDataHeader.addAll(uniqueInstitutions);

        for (int x = 0; x < listDataHeader.size(); x++) {
            ArrayList<HashMap<String, String>> array = new ArrayList<>();

            for (int y = 0; y < doctors.size(); y++) {
                if (doctors.get(y).get("inst_name").equals(listDataHeader.get(x)))
                    array.add(doctors.get(y));
            }
            listDataChild.put(x, array);
        }

        duplicate_list_child.putAll(listDataChild);
        doctor_adapter = new ExpandableListAdapter(this, listDataHeader, listDataChild);
        list_of_doctors.setAdapter(doctor_adapter);
        progress.dismiss();
    }

    public static void duringOnClick(String selected_date) {
        if (flag && MCPActivity.doc_details.getVisibility() == View.VISIBLE) {
            String IDM_id = child_clicked.get("IDM_id");
            HashMap<String, ArrayList<String>> map = new HashMap<>();
            ArrayList<String> date = new ArrayList<>();
            date.add(selected_date);
            map.put(IDM_id, date);

            if (list_of_plans.size() > 0) {
                for (int x = 0; x < list_of_plans.size(); x++) {
                    String keyset = list_of_plans.get(x).keySet().toString().replace("[", "").replace("]", "");

                    if (keyset.equals(IDM_id)) {
                        if (list_of_plans.get(x).get(IDM_id).size() < Integer.parseInt(child_clicked.get("class_code"))) {
                            if (!list_of_plans.get(x).get(IDM_id).contains(selected_date)) {
                                list_of_plans.get(x).get(IDM_id).add(selected_date);
                                isVisible = 1;
                            }
                        } else {
                            if (!list_of_plans.get(x).get(IDM_id).contains(selected_date))
                                Snackbar.make(root, "Doctor exceeds the maximum number of call for this month", Snackbar.LENGTH_SHORT).show();
                            isVisible = 2;
                        }

                        break;
                    } else {
                        if (x == list_of_plans.size() - 1) {
                            list_of_plans.add(map);
                            isVisible = 1;
                        }
                    }
                }
            } else {
                MCPActivity.list_of_plans.add(map);
                isVisible = 1;
            }

            cal_adapter.notifyDataSetChanged();
        } else if (change_view.getVisibility() == View.VISIBLE)
            changeArrayContent(selected_date);
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
                doctor_adapter = new ExpandableListAdapter(this, listDataHeader, listDataChild);
                list_of_doctors.setAdapter(doctor_adapter);
            } else {
                no_records.setVisibility(View.VISIBLE);
                list_of_doctors.setVisibility(View.GONE);
            }
        } else
            prepareListData();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.next:
                checkIfHasUnsaved("Next");
                invalidateOptionsMenu();
                break;

            case R.id.prev:
                if (cal_month.get(Calendar.YEAR) >= 2016 && cal_month.get(Calendar.MONTH) >= 1) {
                    checkIfHasUnsaved("Previous");
                    invalidateOptionsMenu();
                }
                break;
        }
    }

    void checkIfHasUnsaved(final String where) {
        int check = 0;

        if (flag) {
            if (list_of_plans.size() > 0) {
                AlertDialog.Builder alert = new AlertDialog.Builder(this);
                alert.setMessage("You have made changes. Are you sure you want to navigate away and lose your changes?");
                alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (where) {
                            case "Next":
                                setNextMonth();
                                flag = false;
                                break;
                            case "Previous":
                                setPreviousMonth();
                                flag = false;
                                break;
                            case "":
                                MCPActivity.this.finish();
                                break;
                        }
                        refreshCalendar();
                        invalidateOptionsMenu();
                    }
                });
                alert.setNegativeButton("No", null);
                alert.show();
            } else {
                check += 1;
                flag = false;
            }
        } else
            check += 1;

        if (check > 0) {
            switch (where) {
                case "Next":
                    setNextMonth();
                    break;
                case "Previous":
                    setPreviousMonth();
                    break;
                case "":
                    this.finish();
                    break;
            }

            refreshCalendar();
            invalidateOptionsMenu();
        }
    }

    public static void setNextMonth() {
        if (cal_month.get(Calendar.MONTH) == cal_month.getActualMaximum(Calendar.MONTH))
            cal_month.set((cal_month.get(Calendar.YEAR) + 1), cal_month.getActualMinimum(Calendar.MONTH), 1);
        else
            cal_month.set(Calendar.MONTH, cal_month.get(Calendar.MONTH) + 1);

        refreshCalendar();
    }

    public static void setPreviousMonth() {
        if (cal_month.get(Calendar.MONTH) == cal_month.getActualMinimum(Calendar.MONTH))
            cal_month.set((cal_month.get(Calendar.YEAR) - 1), cal_month.getActualMaximum(Calendar.MONTH), 1);
        else
            cal_month.set(Calendar.MONTH, cal_month.get(Calendar.MONTH) - 1);

        refreshCalendar();
    }

    static void refreshCalendar() {
        list_of_plans = new ArrayList<>();
        list_of_dates = new ArrayList<>();

        cal_adapter.refreshDays();

        tv_month.setText(android.text.format.DateFormat.format("MMMM yyyy", cal_month));
    }

    static void changeArrayContent(String date) {
        ArrayList<HashMap<String, String>> plan_details = new ArrayList<>();
        number_of_calls.setText("");

        for (int x = 0; x < list_of_plans_per_day.size(); x++) {
            String keyset = list_of_plans_per_day.get(x).keySet().toString().replace("[", "").replace("]", "");

            if (keyset.equals(date)) {
                plan_details = list_of_plans_per_day.get(x).get(keyset);
                number_of_calls.setText(plan_details.size() + " call/s for this day");
                break;
            }
        }


        plans_adapter = new MCPAdapter(context, plan_details);
        calls_per_day.setAdapter(plans_adapter);
    }
}
