package com.example.vbfc_bry07.calls.Activity;

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

import com.example.vbfc_bry07.calls.Adapter.ACPListAdapter;
import com.example.vbfc_bry07.calls.Adapter.ACPTabsAdapter;
import com.example.vbfc_bry07.calls.Controller.CallsController;
import com.example.vbfc_bry07.calls.Controller.InstitutionDoctorMapsController;
import com.example.vbfc_bry07.calls.Controller.PlanDetailsController;
import com.example.vbfc_bry07.calls.Fragment.CallsFragment;
import com.example.vbfc_bry07.calls.Fragment.NotesFragment;
import com.example.vbfc_bry07.calls.Fragment.ProductsFragment;
import com.example.vbfc_bry07.calls.Helpers;
import com.example.vbfc_bry07.calls.R;

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

    ExpandableListView HospitalListView;
    PlanDetailsController pdc;
    CallsController cc;
    InstitutionDoctorMapsController idmc;
    ACPTabsAdapter fragment_adapter;
    ACPListAdapter hospital_adapter;
    Helpers helpers;

    List<String> listDataHeader;
    HashMap<Integer, ArrayList<HashMap<String, String>>> listDataChild;
    HashMap<String, String> incidental_call = new HashMap<>();

    boolean ongoing_call = false;
    String start_dateTime = "";
    public static String current_date;
    int plan_details_id = 0, incidental_plandetails_id = 0, menu_check = 0;
    public static int check_adapter_acp = 0;

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
        pdc = new PlanDetailsController(this);
        idmc = new InstitutionDoctorMapsController(this);

        current_date = helpers.getCurrentDate("date");
        LblDate.setText(helpers.convertToAlphabetDate(current_date));
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
            HospitalListView.setVisibility(View.VISIBLE);
            no_calls.setVisibility(View.GONE);
            incidental_plandetails_id = pdc.insertIncidentalCall(ShowListOfDoctorsDialog.child_clicked);

            if (incidental_plandetails_id == 0)
                Snackbar.make(root, "You have to plot a plan for this month first", Snackbar.LENGTH_SHORT).show();
            else {
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
                menu_check = 23;
                ongoing_call = true;
                start_dateTime = helpers.getCurrentDate("timestamp");
                invalidateOptionsMenu();
                break;

            case R.id.end_call:
                String end_dateTime = helpers.getCurrentDate("timestamp");

                HashMap<String, String> calls = new HashMap<>();
                calls.put("start_time", start_dateTime);
                calls.put("calls_end", end_dateTime);
                calls.put("calls_date", helpers.getCurrentDate("date"));
                calls.put("calls_plan_details_id", String.valueOf(plan_details_id));
                calls.put("calls_incidentals_pd_id", String.valueOf(incidental_plandetails_id));

                Intent intent = new Intent(this, SignatureFormActivity.class);
                intent.putExtra("call_details", calls);
                intent.putExtra("call_products", ProductsFragment.getNotEmptyProducts());
                startActivity(intent);
                this.finish();

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

                break;

            case R.id.add_incidental_call:
                check_adapter_acp = 20;
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
        } else
            super.onBackPressed();
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
            int hascalled = cc.hasCalled(plan_details_id, incidental_plandetails_id);
            if (hascalled > 0) {
                menu_check = 1;
                image.setVisibility(View.VISIBLE);

                if (hascalled == 2)
                    image.setImageResource(R.mipmap.signed_not_sync);
            } else
                menu_check = 20;

            doctor_name.setText(selected_doc);
            invalidateOptionsMenu();
        }

        return true;
    }
}
