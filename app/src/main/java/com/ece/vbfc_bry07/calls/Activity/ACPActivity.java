package com.ece.vbfc_bry07.calls.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.ece.vbfc_bry07.calls.adapter.ACPListAdapter;
import com.ece.vbfc_bry07.calls.adapter.ACPTabsAdapter;
import com.ece.vbfc_bry07.calls.controller.CallsController;
import com.ece.vbfc_bry07.calls.controller.InstitutionDoctorMapsController;
import com.ece.vbfc_bry07.calls.controller.MissedCallsController;
import com.ece.vbfc_bry07.calls.controller.PlanDetailsController;
import com.ece.vbfc_bry07.calls.controller.PlansController;
import com.ece.vbfc_bry07.calls.controller.ReasonsController;
import com.ece.vbfc_bry07.calls.dialog.HelpDialog;
import com.ece.vbfc_bry07.calls.dialog.ViewCycleMonth;
import com.ece.vbfc_bry07.calls.dialog.ViewDoctorsHistoryDialog;
import com.ece.vbfc_bry07.calls.fragment.NewCallsFragment;
import com.ece.vbfc_bry07.calls.fragment.NotesFragment;
import com.ece.vbfc_bry07.calls.fragment.ProductsFragment;
import com.ece.vbfc_bry07.calls.Helpers;
import com.ece.vbfc_bry07.calls.R;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class ACPActivity extends AppCompatActivity implements TabLayout.OnTabSelectedListener, View.OnClickListener, ExpandableListView.OnChildClickListener {
    LinearLayout root;
    EditText remarks;
    ImageView image;
    ViewPager pager;
    Toolbar toolbar;
    TabLayout tab_layout;
    Spinner spinner_of_reasons;
    ImageView view_acp, add_incidental_call, declared_as_missed;
    TextView LblDate, no_calls, doctor_name, proceed, title;

    Helpers helpers;
    CallsController cc;
    PlansController pc;
    ReasonsController rcc;
    PlanDetailsController pdc;
    MissedCallsController mcc;
    ACPTabsAdapter fragment_adapter;
    ACPListAdapter hospital_adapter;
    ExpandableListView HospitalListView;
    InstitutionDoctorMapsController idmc;

    List<String> listDataHeader;
    HashMap<String, String> additional_call;
    HashMap<Integer, ArrayList<HashMap<String, String>>> listDataChild;

    boolean ongoing_call = false;
    int plan_details_id = 0, joint_call = 0, cycle_month;
    String start_dateTime = "";
    public static int check_adapter_acp = 0, menu_check = 0, tab_position = 0;
    public static String current_date = "", viewotheracp = "", IDM_id = "", missed_call_date = "", selected_reason = "";
    public static Activity acp;
    Dialog dialog_reasons;

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
        declared_as_missed = (ImageView) findViewById(R.id.declared_as_missed);
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
        mcc = new MissedCallsController(this);
        idmc = new InstitutionDoctorMapsController(this);

        acp = this;
        selected_reason = "";
        missed_call_date = "";
        additional_call = new HashMap<>();
        current_date = helpers.getCurrentDate("");
        cycle_month = helpers.convertDateToCycleMonth(current_date);

        LblDate.setText(helpers.convertToAlphabetDate(current_date, ""));
        prepareListData();

        ShowFragment();
        android.app.FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().commit();

        view_acp.setOnClickListener(this);
        HospitalListView.setOnChildClickListener(this);
        add_incidental_call.setOnClickListener(this);
        declared_as_missed.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        image.setVisibility(View.INVISIBLE);

        if (check_adapter_acp == 30 || check_adapter_acp >= 50) {
            int checkPlanDetails = pdc.checkPlanDetails(cycle_month);

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
                    showDialog(true);

                    proceed.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            selected_reason = String.valueOf(spinner_of_reasons.getSelectedItem());
                            dialog_reasons.dismiss();
                        }
                    });
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
                declared_as_missed.setVisibility(View.VISIBLE);
                add_incidental_call.setVisibility(View.VISIBLE);
            } else {
                current_date = viewotheracp;
                declared_as_missed.setVisibility(View.INVISIBLE);
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
        fragment_adapter.addFragment(new NewCallsFragment(), "Calls");
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
                if (pc.checkIfPlanIsApproved(cycle_month) == 1) {
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
                String last_visited = cc.getLastVisited(Integer.parseInt(IDM_id), cycle_month).get("last_visited");
                String count_visits = cc.getLastVisited(Integer.parseInt(IDM_id), cycle_month).get("count_visits");

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

            case R.id.brochure:
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("content://media/external/images/media")));
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        pager.setCurrentItem(tab.getPosition());
        tab_position = tab.getPosition();

        if (tab_position == 2 && !IDM_id.equals(""))
            NewCallsFragment.UpdateCallsTab(Integer.parseInt(IDM_id), cycle_month);
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

                case R.id.declared_as_missed:
                    if (listDataHeader.size() > 0) {
                        AlertDialog.Builder alert = new AlertDialog.Builder(this);
                        alert.setMessage("Are you sure you want to declare all unprocessed calls as missed calls?");
                        alert.setNegativeButton("No", null);
                        alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                prepareListData();
                                showDialog(false);

                                title.setText(R.string.header);
                                proceed.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        String get_remarks = remarks.getText().toString();
                                        int reason_id = rcc.getReasonID(spinner_of_reasons.getSelectedItem().toString());

                                        if (mcc.insertMissedCalls(reason_id, get_remarks, listDataChild))
                                            Snackbar.make(root, "All calls have been declared as missed", Snackbar.LENGTH_SHORT).show();

                                        dialog_reasons.dismiss();
                                    }
                                });
                            }
                        });
                        alert.show();
                    } else
                        Snackbar.make(root, "No calls to declare as missed", Snackbar.LENGTH_SHORT).show();
                    break;
            }
        } else
            Snackbar.make(root, "There is an ongoing call. You are not allowed to do this action", Snackbar.LENGTH_SHORT).show();
    }

    void showDialog(boolean hidden) {
        dialog_reasons = new Dialog(this);
        dialog_reasons.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog_reasons.setContentView(R.layout.dialog_reasons);
        dialog_reasons.show();

        title = (TextView) dialog_reasons.findViewById(R.id.title);
        spinner_of_reasons = (Spinner) dialog_reasons.findViewById(R.id.spinner_of_reasons);
        proceed = (TextView) dialog_reasons.findViewById(R.id.proceed);
        remarks = (EditText) dialog_reasons.findViewById(R.id.remarks);

        if (hidden)
            remarks.setVisibility(View.GONE);
        else
            remarks.setVisibility(View.VISIBLE);

        spinner_of_reasons.setAdapter(new ArrayAdapter<>(ACPActivity.this, R.layout.item_plain_textview, rcc.getEnabledReasons()));
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
        if (ongoing_call)
            Snackbar.make(root, "There is an ongoing call. You are not allowed to do this action", Snackbar.LENGTH_SHORT).show();
        else {
            String selected_doc = listDataChild.get(groupPosition).get(childPosition).get("doc_name");
            plan_details_id = Integer.parseInt(listDataChild.get(groupPosition).get(childPosition).get("plan_details_id"));
            IDM_id = listDataChild.get(groupPosition).get(childPosition).get("IDM_id");
            int pd_id = Integer.parseInt(listDataChild.get(groupPosition).get(childPosition).get("plan_details_id"));
            int temp_pd_id = Integer.parseInt(listDataChild.get(groupPosition).get(childPosition).get("temp_plandetails_id"));

            menu_check = 20;
            image.setVisibility(View.INVISIBLE);

            if (pd_id > 0) {
                int hascalled = cc.hasCalled(plan_details_id, "planDetails");

                if (hascalled == 2) {
                    menu_check = 1;
                    image.setVisibility(View.VISIBLE);
                    image.setImageResource(R.mipmap.ic_signed_call);
                } else if (hascalled == 3) {
                    menu_check = 1;
                    image.setVisibility(View.VISIBLE);
                    image.setImageResource(R.mipmap.ic_recovered_call);
                } else if (hascalled == 5) {
                    menu_check = 1;
                    image.setVisibility(View.VISIBLE);
                    image.setImageResource(R.mipmap.ic_missed_call);
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
                int hascalled = cc.hasCalled(temp_pd_id, "temp_planDetails");
                menu_check = 1;
                image.setVisibility(View.VISIBLE);

                if (hascalled == 4)
                    image.setImageResource(R.mipmap.ic_incidental_call);
            }

            doctor_name.setText(selected_doc);
            invalidateOptionsMenu();

            NotesFragment.callNotesFragment();
            ProductsFragment.setCallMaterials(Integer.parseInt(IDM_id), current_date);

            if (tab_position == 2)
                NewCallsFragment.UpdateCallsTab(Integer.parseInt(IDM_id), cycle_month);
        }

        return true;
    }
}