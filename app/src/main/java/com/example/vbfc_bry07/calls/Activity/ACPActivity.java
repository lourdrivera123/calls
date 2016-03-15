package com.example.vbfc_bry07.calls.Activity;

import android.app.Dialog;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.vbfc_bry07.calls.Adapter.ACPListAdapter;
import com.example.vbfc_bry07.calls.Adapter.ACPTabsAdapter;
import com.example.vbfc_bry07.calls.Adapter.ExpandableListAdapter;
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
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class ACPActivity extends AppCompatActivity implements TabLayout.OnTabSelectedListener, View.OnClickListener, ExpandableListView.OnChildClickListener, TextWatcher {
    TextView LblDate, no_calls, doctor_name;
    ImageView view_acp, add_incidental_call;
    TabLayout tab_layout;
    LinearLayout root;
    ViewPager pager;
    EditText search_doctor;
    ExpandableListView list_of_doctors;
    Toolbar toolbar;

    ExpandableListView HospitalListView;
    PlanDetailsController pdc;
    InstitutionDoctorMapsController idmc;
    ACPTabsAdapter fragment_adapter;
    ACPListAdapter hospital_adapter;
    Helpers helpers;
    ExpandableListAdapter search_adapter;

    String current_date;

    List<String> listDataHeader;
    HashMap<Integer, ArrayList<HashMap<String, String>>> listDataChild;
    int menu_check = 0;
    boolean ongoing_call = false;

    int plan_details_id = 0;
    String start_dateTime = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acp);

        LblDate = (TextView) findViewById(R.id.LblDate);
        no_calls = (TextView) findViewById(R.id.no_calls);
        doctor_name = (TextView) findViewById(R.id.doctor_name);
        view_acp = (ImageView) findViewById(R.id.view_acp);
        add_incidental_call = (ImageView) findViewById(R.id.add_incidental_call);
        HospitalListView = (ExpandableListView) findViewById(R.id.HospitalListView);
        tab_layout = (TabLayout) findViewById(R.id.tab_layout);
        pager = (ViewPager) findViewById(R.id.pager);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        root = (LinearLayout) findViewById(R.id.root);

        setSupportActionBar(toolbar);
        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Actual Coverage Plan");

        setupViewPager(pager);
        tab_layout.setupWithViewPager(pager);
        tab_layout.setOnTabSelectedListener(this);

        helpers = new Helpers();
        pdc = new PlanDetailsController(this);
        idmc = new InstitutionDoctorMapsController(this);

        current_date = helpers.getCurrentDate("date");

        LblDate.setText(helpers.convertToAlphabetDate(current_date));
        prepareListData();

        ShowFragment(0);
        android.app.FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().commit();

        view_acp.setOnClickListener(this);
        add_incidental_call.setOnClickListener(this);
    }

    void ShowFragment(int position) {
        Fragment fragment = null;

        switch (position) {
            case 0:
                fragment = new ProductsFragment();
                break;

            case 1:
                Toast.makeText(this, "1", Toast.LENGTH_SHORT).show();
                break;

            case 2:
                Toast.makeText(this, "2", Toast.LENGTH_SHORT).show();
                break;
        }

        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.pager, fragment).commit();
            fragment_adapter.notifyDataSetChanged();
        }
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
                this.finish();
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

                Intent intent = new Intent(this, SignatureFormActivity.class);
                intent.putExtra("call_details", calls);
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
                Dialog dialog = new Dialog(this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.getWindow().setLayout(600, 600);
                dialog.setContentView(R.layout.dialog_choose_doctor);
                dialog.show();

                search_doctor = (EditText) dialog.findViewById(R.id.search_doctor);
                list_of_doctors = (ExpandableListView) dialog.findViewById(R.id.list_of_doctors);

                prepareSearchDoctors();

                search_doctor.addTextChangedListener(this);
                list_of_doctors.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
                    @Override
                    public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {

                        return true;
                    }
                });
                break;
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
            HospitalListView.setOnChildClickListener(this);
        } else {
            no_calls.setVisibility(View.VISIBLE);
            HospitalListView.setVisibility(View.GONE);
        }
    }

    void prepareSearchDoctors() {
        ArrayList<HashMap<String, String>> doctors = idmc.getDoctorsNotIncludedInMCP(current_date);
        Set<String> uniqueInstitutions = new LinkedHashSet<>();
        List<String> header = new ArrayList<>();
        HashMap<Integer, ArrayList<HashMap<String, String>>> child = new HashMap<>();

        for (int x = 0; x < doctors.size(); x++)
            uniqueInstitutions.add(doctors.get(x).get("inst_name"));

        header.addAll(uniqueInstitutions);

        for (int x = 0; x < header.size(); x++) {
            ArrayList<HashMap<String, String>> array = new ArrayList<>();

            for (int y = 0; y < doctors.size(); y++) {
                if (doctors.get(y).get("inst_name").equals(header.get(x)))
                    array.add(doctors.get(y));
            }
            child.put(x, array);
        }

        search_adapter = new ExpandableListAdapter(this, header, child);
        list_of_doctors.setAdapter(search_adapter);
    }

    @Override
    public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
        String selected_doc = listDataChild.get(groupPosition).get(childPosition).get("doc_name");
        plan_details_id = Integer.parseInt(listDataChild.get(groupPosition).get(childPosition).get("plan_details_id"));

        if (ongoing_call)
            Snackbar.make(root, "There is an ongoing call. You are not allowed to do this action", Snackbar.LENGTH_SHORT).show();
        else {
            menu_check = 20;
            doctor_name.setText(selected_doc);
            invalidateOptionsMenu();
        }

        return true;
    }

    @Override

    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}
