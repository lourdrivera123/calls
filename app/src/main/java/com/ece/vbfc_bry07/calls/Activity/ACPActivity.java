package com.ece.vbfc_bry07.calls.Activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.ece.vbfc_bry07.calls.Adapter.ACPListAdapter;
import com.ece.vbfc_bry07.calls.Adapter.ACPTabsAdapter;
import com.ece.vbfc_bry07.calls.Controller.CallsController;
import com.ece.vbfc_bry07.calls.Controller.InstitutionDoctorMapsController;
import com.ece.vbfc_bry07.calls.Controller.PlanDetailsController;
import com.ece.vbfc_bry07.calls.Controller.PlansController;
import com.ece.vbfc_bry07.calls.Controller.ReasonsController;
import com.ece.vbfc_bry07.calls.Dialog.HelpDialog;
import com.ece.vbfc_bry07.calls.Dialog.ViewCycleMonth;
import com.ece.vbfc_bry07.calls.Dialog.ViewDoctorsHistoryDialog;
import com.ece.vbfc_bry07.calls.Fragment.CallsFragment;
import com.ece.vbfc_bry07.calls.Fragment.NotesFragment;
import com.ece.vbfc_bry07.calls.Fragment.ProductsFragment;
import com.ece.vbfc_bry07.calls.Helpers;
import com.ece.vbfc_bry07.calls.R;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class ACPActivity extends AppCompatActivity implements TabLayout.OnTabSelectedListener, View.OnClickListener, ExpandableListView.OnChildClickListener {
    TextView LblDate, no_calls, doctor_name, proceed;
    ImageView view_acp, add_incidental_call;
    Spinner spinner_of_reasons;
    TabLayout tab_layout;
    LinearLayout root;
    ImageView image;
    ViewPager pager;
    Toolbar toolbar;

    Dialog dialog;

    Helpers helpers;
    CallsController cc;
    PlansController pc;
    ReasonsController rcc;
    PlanDetailsController pdc;
    ACPTabsAdapter fragment_adapter;
    ACPListAdapter hospital_adapter;
    ExpandableListView HospitalListView;
    InstitutionDoctorMapsController idmc;

    List<String> listDataHeader;
    HashMap<String, String> additional_call;
    HashMap<Integer, ArrayList<HashMap<String, String>>> listDataChild;

    boolean ongoing_call = false;
    int plan_details_id = 0, menu_check = 0, joint_call = 0;
    String start_dateTime = "";
    public static int check_adapter_acp = 0;
    public static String current_date = "", viewotheracp = "", IDM_id = "", missed_call_date = "", selected_reason = "";
    public static Activity acp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acp);

        LblDate = (TextView) findViewById(R.id.LblDate);
        no_calls = (TextView) findViewById(R.id.no_calls);
        doctor_name = (TextView) findViewById(R.id.doctor_name);
        tab_layout = (TabLayout) findViewById(R.id.tab_layout);
        pager = (ViewPager) findViewById(R.id.pager);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        root = (LinearLayout) findViewById(R.id.root);
        image = (ImageView) findViewById(R.id.image);
        view_acp = (ImageView) findViewById(R.id.view_acp);
        add_incidental_call = (ImageView) findViewById(R.id.add_incidental_call);
        HospitalListView = (ExpandableListView) findViewById(R.id.HospitalListView);

        setSupportActionBar(toolbar);
        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Actual Coverage Plan");

        setupViewPager(pager);
        tab_layout.setupWithViewPager(pager);
        tab_layout.setOnTabSelectedListener(this);

        helpers = new Helpers();
        cc = new CallsController(this);
        pc = new PlansController(this);
        rcc = new ReasonsController(this);
        pdc = new PlanDetailsController(this);
        idmc = new InstitutionDoctorMapsController(this);

        acp = this;
        selected_reason = "";
        missed_call_date = "";
        additional_call = new HashMap<>();

        current_date = helpers.getCurrentDate("date");
        LblDate.setText(helpers.convertToAlphabetDate(current_date, ""));
        prepareListData();

        ShowFragment();
        android.app.FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().commit();

        view_acp.setOnClickListener(this);
        HospitalListView.setOnChildClickListener(this);
        add_incidental_call.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        image.setVisibility(View.INVISIBLE);

        if (check_adapter_acp == 30 || check_adapter_acp >= 50) {
            int checkPlanDetails = pdc.checkPlanDetails(helpers.convertDateToCycleMonth(helpers.getCurrentDate("date")));

            if (checkPlanDetails == 0)
                Snackbar.make(root, "You have to plot a plan for this month first", Snackbar.LENGTH_LONG).show();
            else if (checkPlanDetails == -1)
                Snackbar.make(root, "Current plan hasn't been approved. You are not yet allowed to make any transaction.", Snackbar.LENGTH_LONG).show();
            else {
                HospitalListView.setVisibility(View.VISIBLE);
                no_calls.setVisibility(View.GONE);

                if (check_adapter_acp == 50) {
                    AlertDialog.Builder alert = new AlertDialog.Builder(this);
                    alert.setMessage("You have missed a  call from this Doctor on " + helpers.convertToAlphabetDate(missed_call_date, "complete")
                            + ". This will count as a recovered call");
                    alert.setPositiveButton("Ok", null);
                    alert.show();
                } else if (check_adapter_acp == 55) {
                    dialog = new Dialog(this);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setContentView(R.layout.dialog_reasons);
                    dialog.show();

                    spinner_of_reasons = (Spinner) dialog.findViewById(R.id.spinner_of_reasons);
                    proceed = (TextView) dialog.findViewById(R.id.proceed);

                    ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.item_products, rcc.getEnabledReasons());
                    spinner_of_reasons.setAdapter(adapter);
                    proceed.setOnClickListener(this);
                }

                if (ViewDoctorsHistoryDialog.child_clicked.get("plan_details_id").equals("0")) {
                    missed_call_date = "";
                    additional_call = ViewDoctorsHistoryDialog.child_clicked;
                    additional_call.put("plan_details_id", additional_call.get("plan_details_id"));
                    additional_call.put("temp_plandetails_id", "0");
                }

                ArrayList<HashMap<String, String>> temp_array = new ArrayList<>();

                if (listDataHeader.contains(ViewDoctorsHistoryDialog.child_clicked.get("inst_name"))) {
                    for (int x = 0; x < listDataHeader.size(); x++) {
                        if (listDataHeader.get(x).equals(ViewDoctorsHistoryDialog.child_clicked.get("inst_name"))) {
                            temp_array = listDataChild.get(x);
                            temp_array.add(ViewDoctorsHistoryDialog.child_clicked);
                            break;
                        }
                    }

                } else {
                    int index = listDataHeader.size();
                    listDataHeader.add(ViewDoctorsHistoryDialog.child_clicked.get("inst_name"));
                    temp_array.add(ViewDoctorsHistoryDialog.child_clicked);
                    listDataChild.put(index, temp_array);
                }

                hospital_adapter = new ACPListAdapter(this, listDataHeader, listDataChild);
                HospitalListView.setAdapter(hospital_adapter);
            }
        } else if (!viewotheracp.equals("") && check_adapter_acp == 0) {
            if (viewotheracp.equals(helpers.getCurrentDate("date"))) {
                current_date = helpers.getCurrentDate("date");
                menu_check = 20;
                add_incidental_call.setVisibility(View.VISIBLE);
            } else {
                current_date = viewotheracp;
                add_incidental_call.setVisibility(View.INVISIBLE);
                menu_check = 8;
                invalidateOptionsMenu();
            }

            doctor_name.setText("");
            LblDate.setText(helpers.convertToAlphabetDate(current_date, ""));
            prepareListData();
        }

        check_adapter_acp = 0;
        super.onResume();
    }

    void ShowFragment() {
        Fragment fragment = new ProductsFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.pager, fragment).commit();
        fragment_adapter.notifyDataSetChanged();
    }

    private void setupViewPager(ViewPager viewPager) {
        fragment_adapter = new ACPTabsAdapter(getSupportFragmentManager());
        fragment_adapter.addFragment(new ProductsFragment(), "Products");
        fragment_adapter.addFragment(new NotesFragment(), "Notes");
        fragment_adapter.addFragment(new CallsFragment(), "Calls");
        viewPager.setAdapter(fragment_adapter);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (menu_check == 20)
            getMenuInflater().inflate(R.menu.start_call_menu, menu);
        else if (menu_check == 23)
            getMenuInflater().inflate(R.menu.end_call, menu);
        else
            getMenuInflater().inflate(R.menu.help, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;

            case R.id.start_call:
                int cycleMonth = helpers.convertDateToCycleMonth(current_date);
                int cycleSet = helpers.convertDateToCycleSet(current_date);

                if (pc.checkIfPlanIsApproved(cycleSet, cycleMonth)) {
                    if (ViewDoctorsHistoryDialog.child_clicked != null && !IDM_id.equals(ViewDoctorsHistoryDialog.child_clicked.get("IDM_id")))
                        missed_call_date = "";

                    menu_check = 23;
                    ongoing_call = true;
                    start_dateTime = helpers.getCurrentDate("timestamp");
                    invalidateOptionsMenu();
                } else
                    Snackbar.make(root, "Current plan hasn't been approved. You are not yet allowed to make any transaction.", Snackbar.LENGTH_LONG).show();

                break;

            case R.id.end_call:
                String end_dateTime = helpers.getCurrentDate("timestamp");
                String last_visited = cc.getLastVisited(plan_details_id).get("last_visited");
                String count_visits = cc.getLastVisited(plan_details_id).get("count_visits");

                HashMap<String, String> calls = new HashMap<>();
                calls.put("start_time", start_dateTime);
                calls.put("calls_end", end_dateTime);
                calls.put("calls_joint_call", String.valueOf(joint_call));
                calls.put("calls_last_visited", last_visited);
                calls.put("calls_count_visits", count_visits);
                calls.put("calls_IDM_id", IDM_id);
                calls.put("calls_status", "1");
                calls.put("calls_plan_details_id", String.valueOf(plan_details_id));

                Intent intent = new Intent(this, SignatureFormActivity.class);
                intent.putExtra("call_details", calls);
                intent.putExtra("call_products", ProductsFragment.getNotEmptyProducts());
                intent.putExtra("call_notes", NotesFragment.array_of_notes);

                if (additional_call != null && additional_call.size() > 0)
                    intent.putExtra("calls_additional_call", additional_call);

                startActivity(intent);

                break;

            case R.id.joint:
                if (item.isChecked()) {
                    joint_call = 0;
                    item.setChecked(false);
                } else {
                    joint_call = 1;
                    item.setChecked(true);
                }

                break;

            case R.id.help:
                Intent intent1 = new Intent(this, HelpDialog.class);
                intent1.putExtra("array_name", "color_coded_circles");
                startActivity(intent1);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        pager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {
    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {
    }

    @Override
    public void onClick(View v) {
        if (!ongoing_call) {
            switch (v.getId()) {
                case R.id.view_acp:
                    startActivity(new Intent(this, ViewCycleMonth.class));
                    break;

                case R.id.add_incidental_call:
                    check_adapter_acp = 20;
                    viewotheracp = "";
                    startActivity(new Intent(this, ViewDoctorsHistoryDialog.class));

                    break;

                case R.id.proceed:
                    selected_reason = String.valueOf(spinner_of_reasons.getSelectedItem());
                    dialog.dismiss();

                    break;
            }
        } else
            Snackbar.make(root, "There is an ongoing call. You are not allowed to do this action", Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed() {
        if (ongoing_call) {
            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setMessage("There is an ongoing call, Are you sure you want to leave this page?");
            alert.setNegativeButton("No", null);
            alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    ACPActivity.this.finish();
                }
            });
            alert.show();
        } else {
            viewotheracp = "";
            this.finish();
        }
    }

    private void prepareListData() {
        listDataChild = new HashMap<>();
        listDataHeader = new ArrayList<>();
        Set<String> uniqueInstitutions = new LinkedHashSet<>();
        ArrayList<HashMap<String, String>> doctors = idmc.getDoctorsWithInstitutions(current_date);

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

        if (listDataHeader.size() > 0) {
            HospitalListView.setVisibility(View.VISIBLE);
            no_calls.setVisibility(View.GONE);
            hospital_adapter = new ACPListAdapter(this, listDataHeader, listDataChild);
            HospitalListView.setAdapter(hospital_adapter);
        } else {
            no_calls.setVisibility(View.VISIBLE);
            HospitalListView.setVisibility(View.GONE);
        }
    }

    @Override
    public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
        String selected_doc = listDataChild.get(groupPosition).get(childPosition).get("doc_name");
        plan_details_id = Integer.parseInt(listDataChild.get(groupPosition).get(childPosition).get("plan_details_id"));
        IDM_id = listDataChild.get(groupPosition).get(childPosition).get("IDM_id");
        int pd_id = Integer.parseInt(listDataChild.get(groupPosition).get(childPosition).get("plan_details_id"));
        int temp_pd_id = Integer.parseInt(listDataChild.get(groupPosition).get(childPosition).get("temp_plandetails_id"));

        if (ongoing_call)
            Snackbar.make(root, "There is an ongoing call. You are not allowed to do this action", Snackbar.LENGTH_SHORT).show();
        else {
            menu_check = 20;
            image.setVisibility(View.INVISIBLE);

            if (pd_id > 0) {
                int hascalled = cc.hasCalled(plan_details_id, "planDetails");

                if (hascalled == 2) {
                    menu_check = 1;
                    image.setVisibility(View.VISIBLE);
                    image.setImageResource(R.mipmap.ic_signed_call);
                } else if (!viewotheracp.equals("") && !viewotheracp.equals(helpers.getCurrentDate("date"))) {
                    Date dateNow = helpers.convertStringToDate(helpers.getCurrentDate("date"));
                    Date date1 = helpers.convertStringToDate(viewotheracp);
                    menu_check = 1;

                    if (dateNow.after(date1)) {
                        image.setVisibility(View.VISIBLE);
                        image.setImageResource(R.mipmap.ic_missed_call);
                    }
                }
            } else if (temp_pd_id > 0) {
                int hascalled = cc.hasCalled(plan_details_id, "temp_planDetails");
                menu_check = 1;
                image.setVisibility(View.VISIBLE);

                if (hascalled == 3)
                    image.setImageResource(R.mipmap.ic_recovered_call);
                else if (hascalled == 2)
                    image.setImageResource(R.mipmap.ic_signed_call);
            }

            doctor_name.setText(selected_doc);
            invalidateOptionsMenu();
        }

        return true;
    }
}