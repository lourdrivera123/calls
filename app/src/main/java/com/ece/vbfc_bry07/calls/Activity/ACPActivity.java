package com.ece.vbfc_bry07.calls.Activity;

import android.app.Activity;
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
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ece.vbfc_bry07.calls.Adapter.ACPListAdapter;
import com.ece.vbfc_bry07.calls.Adapter.ACPTabsAdapter;
import com.ece.vbfc_bry07.calls.Controller.CallsController;
import com.ece.vbfc_bry07.calls.Controller.InstitutionDoctorMapsController;
import com.ece.vbfc_bry07.calls.Controller.PlanDetailsController;
import com.ece.vbfc_bry07.calls.Controller.PlansController;
import com.ece.vbfc_bry07.calls.Dialog.ShowListOfDoctorsDialog;
import com.ece.vbfc_bry07.calls.Dialog.ViewCycleMonth;
import com.ece.vbfc_bry07.calls.Fragment.CallsFragment;
import com.ece.vbfc_bry07.calls.Fragment.NotesFragment;
import com.ece.vbfc_bry07.calls.Fragment.ProductsFragment;
import com.ece.vbfc_bry07.calls.Helpers;
import com.ece.vbfc_bry07.calls.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ACPActivity extends AppCompatActivity implements TabLayout.OnTabSelectedListener, View.OnClickListener, ExpandableListView.OnChildClickListener {
    TextView LblDate, no_calls, doctor_name;
    ImageView view_acp, add_incidental_call;
    TabLayout tab_layout;
    LinearLayout root;
    ImageView image;
    ViewPager pager;
    Toolbar toolbar;

    ACPTabsAdapter fragment_adapter;
    ACPListAdapter hospital_adapter;
    CallsController cc;
    ExpandableListView HospitalListView;
    Helpers helpers;
    InstitutionDoctorMapsController idmc;
    PlanDetailsController pdc;
    PlansController pc;

    List<String> listDataHeader;
    HashMap<Integer, ArrayList<HashMap<String, String>>> listDataChild;
    HashMap<String, String> incidental_call = new HashMap<>();

    boolean ongoing_call = false;
    String start_dateTime = "";
    public static String current_date;
    int plan_details_id = 0, incidental_plandetails_id = 0, menu_check = 0, joint_call = 0;
    public static int check_adapter_acp = 0;
    public static String viewotheracp = "";
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

        acp = this;
        helpers = new Helpers();
        cc = new CallsController(this);
        pc = new PlansController(this);
        pdc = new PlanDetailsController(this);
        idmc = new InstitutionDoctorMapsController(this);

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
        if (check_adapter_acp == 30) {
            incidental_plandetails_id = pdc.insertIncidentalCall(ShowListOfDoctorsDialog.child_clicked);

            if (incidental_plandetails_id == 0)
                Snackbar.make(root, "You have to plot a plan for this month first", Snackbar.LENGTH_LONG).show();
            else if (incidental_plandetails_id == -1)
                Snackbar.make(root, "Current plan hasn't been approved. You are not yet allowed to make any transaction.", Snackbar.LENGTH_LONG).show();
            else {
                HospitalListView.setVisibility(View.VISIBLE);
                no_calls.setVisibility(View.GONE);

                incidental_call = ShowListOfDoctorsDialog.child_clicked;
                incidental_call.put("plan_details_id", String.valueOf(0));
                incidental_call.put("temp_plandetails_id", String.valueOf(incidental_plandetails_id));
                ArrayList<HashMap<String, String>> temp_array = new ArrayList<>();

                if (listDataHeader.contains(ShowListOfDoctorsDialog.child_clicked.get("inst_name"))) {
                    for (int x = 0; x < listDataHeader.size(); x++) {
                        if (listDataHeader.get(x).equals(incidental_call.get("inst_name"))) {
                            temp_array = listDataChild.get(x);
                            temp_array.add(incidental_call);
                            break;
                        }
                    }

                } else {
                    int index = listDataHeader.size();
                    listDataHeader.add(ShowListOfDoctorsDialog.child_clicked.get("inst_name"));
                    temp_array.add(incidental_call);
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
                menu_check = 1;
                add_incidental_call.setVisibility(View.INVISIBLE);
            }

            doctor_name.setText("");
            LblDate.setText(helpers.convertToAlphabetDate(current_date, ""));
            prepareListData();
            invalidateOptionsMenu();
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
                    menu_check = 23;
                    ongoing_call = true;
                    start_dateTime = helpers.getCurrentDate("timestamp");
                    invalidateOptionsMenu();
                } else
                    Snackbar.make(root, "Current plan hasn't been approved. You are not yet allowed to make any transaction.", Snackbar.LENGTH_LONG).show();

                break;

            case R.id.end_call:
                String end_dateTime = helpers.getCurrentDate("timestamp");
                String last_visited = cc.getLastVisited(plan_details_id, incidental_plandetails_id).get("last_visited");
                String count_visits = cc.getLastVisited(plan_details_id, incidental_plandetails_id).get("count_visits");

                HashMap<String, String> calls = new HashMap<>();
                calls.put("start_time", start_dateTime);
                calls.put("calls_end", end_dateTime);
                calls.put("calls_joint_call", String.valueOf(joint_call));
                calls.put("calls_last_visited", last_visited);
                calls.put("calls_count_visits", count_visits);
                calls.put("calls_plan_details_id", String.valueOf(plan_details_id));
                calls.put("calls_incidentals_pd_id", String.valueOf(incidental_plandetails_id));

                Intent intent = new Intent(this, SignatureFormActivity.class);
                intent.putExtra("call_details", calls);
                intent.putExtra("call_products", ProductsFragment.getNotEmptyProducts());
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
        switch (v.getId()) {
            case R.id.view_acp:
                startActivity(new Intent(this, ViewCycleMonth.class));
                break;

            case R.id.add_incidental_call:
                check_adapter_acp = 20;
                viewotheracp = "";
                MCPActivity.check_adapter_mcp = 0;
                startActivity(new Intent(this, ShowListOfDoctorsDialog.class));

                break;
        }
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
        ArrayList<HashMap<String, String>> institutions = idmc.getInstitutions(current_date);
        ArrayList<HashMap<String, String>> doctors = idmc.getDoctorsWithInstitutions(current_date);
        listDataHeader = new ArrayList<>();
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
        incidental_plandetails_id = Integer.parseInt(listDataChild.get(groupPosition).get(childPosition).get("temp_plandetails_id"));

        if (ongoing_call)
            Snackbar.make(root, "There is an ongoing call. You are not allowed to do this action", Snackbar.LENGTH_SHORT).show();
        else {
            if (viewotheracp.equals("")) {
                int hascalled = cc.hasCalled(plan_details_id, incidental_plandetails_id);

                if (hascalled > 0) {
                    menu_check = 1;
                    image.setVisibility(View.VISIBLE);

                    if (hascalled == 2)
                        image.setImageResource(R.mipmap.signed_not_sync);
                } else {
                    image.setVisibility(View.INVISIBLE);
                    menu_check = 20;
                }
            }

            doctor_name.setText(selected_doc);
            invalidateOptionsMenu();
        }

        return true;
    }
}