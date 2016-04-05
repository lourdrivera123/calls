package com.ece.vbfc_bry07.calls.Activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.ece.vbfc_bry07.calls.Adapter.CalendarAdapter;
import com.ece.vbfc_bry07.calls.Adapter.MCPAdapter;
import com.ece.vbfc_bry07.calls.Controller.CycleSetsController;
import com.ece.vbfc_bry07.calls.Controller.InstitutionDoctorMapsController;
import com.ece.vbfc_bry07.calls.Controller.PlanDetailsController;
import com.ece.vbfc_bry07.calls.Controller.PlansController;
import com.ece.vbfc_bry07.calls.Dialog.ShowListOfDoctorsDialog;
import com.ece.vbfc_bry07.calls.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.TimeZone;

public class MCPActivity extends AppCompatActivity implements View.OnClickListener {
    static Calendar cal_month;
    static Context context;

    ImageButton prev, next, add_call;
    Toolbar toolbar;
    LinearLayout root;
    GridView gv_calendar;
    static ListView list_of_calls;
    public static TextView picked_day, picked_date, number_of_calls, tv_month, no_plans;

    PlansController pc;
    PlanDetailsController pdc;
    CycleSetsController csc;
    static CalendarAdapter cal_adapter;
    static MCPAdapter mcp_adapter;
    InstitutionDoctorMapsController idmc;

    public static ArrayList<HashMap<String, String>> new_plan_details = new ArrayList<>();
    public static ArrayList<HashMap<String, ArrayList<HashMap<String, String>>>> list_of_plans;
    static HashMap<String, ArrayList<HashMap<String, String>>> hash_plans_per_day = new HashMap<>();

    boolean flag = false;
    public static String date;
    public static boolean setFirstdaySelected = false, isVisible = false;
    public static int check_adapter_mcp = 0, check_count;

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

        check_count = 0;
        context = this;
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
        list_of_calls.setOnCreateContextMenuListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        list_of_plans = new ArrayList<>();
        hash_plans_per_day = new HashMap<>();

        if (flag) { //on going na plotting
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
                checkIfHasUnsaved("");
                break;

            case R.id.add:
                if (picked_date.getText().toString().equals("")) {
                    Snackbar.make(root, "Pick a date to start plotting", Snackbar.LENGTH_SHORT).show();
                } else {
                    add_call.setVisibility(View.VISIBLE);
                    no_plans.setVisibility(View.GONE);
                    list_of_calls.setVisibility(View.VISIBLE);

                    new_plan_details = new ArrayList<>();

                    mcp_adapter = new MCPAdapter(this, new_plan_details);
                    list_of_calls.setAdapter(mcp_adapter);
                    flag = true;
                    invalidateOptionsMenu();
                }

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
                            flag = false;
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

            case R.id.cancel:
                addToListOfPlans();

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
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        if (flag)
            getMenuInflater().inflate(R.menu.delete_context, menu);

        super.onCreateContextMenu(menu, v, menuInfo);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int position = info.position;
        String doc_id_toBeRemoved = new_plan_details.get(position).get("doctor_id");
        String hash_keyset = "";

        if (hash_plans_per_day.size() > 0)
            hash_keyset = hash_plans_per_day.keySet().toString().replace("[", "").replace("]", "");

        for (int x = 0; x < new_plan_details.size(); x++) {
            if (new_plan_details.get(x).get("doctor_id").equals(doc_id_toBeRemoved)) {
                if (!hash_keyset.equals("")) {
                    hash_plans_per_day.get(hash_keyset).remove(x);
                    mcp_adapter.notifyDataSetChanged();
                } else {
                    for (int y = 0; y < list_of_plans.size(); y++) {
                        if (list_of_plans.get(y).get(date) != null) {
                            ArrayList<HashMap<String, String>> array = list_of_plans.get(y).get(date);

                            for (int z = 0; z < array.size(); z++) {
                                if (array.get(z).get("doctor_id").equals(doc_id_toBeRemoved)) {
                                    list_of_plans.get(y).get(date).remove(z);

                                    if (array.size() == 0)
                                        list_of_plans.remove(y);

                                    break;
                                }
                            }
                        }
                    }

                    mcp_adapter.remove(position);
                }
                break;
            }
        }

        if (new_plan_details.size() > 0) {
            number_of_calls.setText(new_plan_details.size() + " call/s for this day");
        } else {
            number_of_calls.setText("");
            cal_adapter.notifyDataSetChanged();
            isVisible = false;
        }
        return super.onContextItemSelected(item);
    }

    @Override
    protected void onResume() {
        if (check_adapter_mcp == 10) {
            new_plan_details.add(ShowListOfDoctorsDialog.child_clicked);
            hash_plans_per_day.put(date, new_plan_details);

            int number = hash_plans_per_day.get(date).size();

            if (number > 0) {
                no_plans.setVisibility(View.GONE);
                number_of_calls.setText(number + " call/s for this day");
            }

            mcp_adapter.notifyDataSetChanged();
            cal_adapter.notifyDataSetChanged();
        } else if (check_adapter_mcp == 11)
            Snackbar.make(root, "Doctor exceeds the maximum number of call for this month", Snackbar.LENGTH_SHORT).show();

        check_adapter_mcp = 0;
        super.onResume();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.prev:
                if (cal_month.get(Calendar.YEAR) >= 2016 && cal_month.get(Calendar.MONTH) >= 1) {
                    checkIfHasUnsaved("Previous");
                    invalidateOptionsMenu();
                }
                break;

            case R.id.next:
                checkIfHasUnsaved("Next");
                invalidateOptionsMenu();
                break;

            case R.id.add_call:
                flag = true;
                check_adapter_mcp = 23;
                ACPActivity.check_adapter_acp = 0;
                startActivity(new Intent(this, ShowListOfDoctorsDialog.class));
                break;
        }
    }

    public static void updateDuringOnItemClick(String selectedDate) {
        number_of_calls.setText("");
        new_plan_details = new ArrayList<>();
        date = selectedDate;

        if (hash_plans_per_day.size() > 0) {
            if (list_of_plans.size() > 0)
                addToListOfPlans();
            else
                list_of_plans.add(hash_plans_per_day);
        }

        setPerDayPlans(selectedDate);
        hash_plans_per_day = new HashMap<>();
    }

    static void setPerDayPlans(String date) {
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

        mcp_adapter = new MCPAdapter(context, new_plan_details);
        list_of_calls.setAdapter(mcp_adapter);
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
                        switch (where) {
                            case "Next":
                                setNextMonth();
                                break;
                            case "Previous":
                                setPreviousMonth();
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
            } else
                check += 1;
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
        setFirstdaySelected = true;
        picked_date.setText("");
        picked_day.setText("");
        number_of_calls.setText("");

        new_plan_details = new ArrayList<>();
        list_of_plans = new ArrayList<>();
        hash_plans_per_day = new HashMap<>();

        cal_adapter.refreshDays();
        mcp_adapter.notifyDataSetChanged();

        tv_month.setText(android.text.format.DateFormat.format("MMMM yyyy", cal_month));
    }

    static void addToListOfPlans() {
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