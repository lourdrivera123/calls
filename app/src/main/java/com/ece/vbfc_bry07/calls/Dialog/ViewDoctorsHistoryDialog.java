package com.ece.vbfc_bry07.calls.Dialog;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.ece.vbfc_bry07.calls.Activity.ACPActivity;
import com.ece.vbfc_bry07.calls.Adapter.DoctorsHistoryAdapter;
import com.ece.vbfc_bry07.calls.Adapter.ExpandableListAdapter;
import com.ece.vbfc_bry07.calls.Controller.DoctorClassesController;
import com.ece.vbfc_bry07.calls.Controller.InstitutionDoctorMapsController;
import com.ece.vbfc_bry07.calls.Controller.PlanDetailsController;
import com.ece.vbfc_bry07.calls.Helpers;
import com.ece.vbfc_bry07.calls.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class ViewDoctorsHistoryDialog extends AppCompatActivity implements TextWatcher, ExpandableListView.OnChildClickListener, View.OnClickListener {
    View _lastColored;
    EditText search_doctor;
    ListView list_of_history;
    ExpandableListView list_of_doctors;
    TextView no_records, add_incidental_call;

    Helpers helpers;
    PlanDetailsController pdc;
    DoctorClassesController dcc;
    ExpandableListAdapter listAdapter;
    DoctorsHistoryAdapter child_adapter;
    InstitutionDoctorMapsController idmc;
    public static ViewDoctorsHistoryDialog activity;

    List<String> listDataHeader;
    ArrayList<HashMap<String, String>> doctors;
    static ArrayList<HashMap<String, String>> history;
    public static HashMap<String, String> child_clicked;
    HashMap<Integer, ArrayList<HashMap<String, String>>> listDataChild, duplicate_list_child;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        DisplayMetrics metrics = getResources().getDisplayMetrics();
        int screenWidth = (int) (metrics.widthPixels * 0.80);

        setContentView(R.layout.dialog_view_doctor_history);
        getWindow().setLayout(screenWidth, LinearLayout.LayoutParams.WRAP_CONTENT);

        no_records = (TextView) findViewById(R.id.no_records);
        search_doctor = (EditText) findViewById(R.id.search_doctor);
        list_of_history = (ListView) findViewById(R.id.list_of_history);
        add_incidental_call = (TextView) findViewById(R.id.add_incidental_call);
        list_of_doctors = (ExpandableListView) findViewById(R.id.list_of_doctors);

        activity = this;
        helpers = new Helpers();
        pdc = new PlanDetailsController(this);
        dcc = new DoctorClassesController(this);
        idmc = new InstitutionDoctorMapsController(this);

        prepareListData();

        search_doctor.addTextChangedListener(this);
        list_of_doctors.setOnChildClickListener(this);
        add_incidental_call.setOnClickListener(this);
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
        if (_lastColored != null) {
            _lastColored.setBackgroundColor(Color.TRANSPARENT);
            _lastColored.invalidate();
        }

        _lastColored = v;
        v.setBackgroundColor(Color.parseColor("#80A9A9A9"));

        int cycle_month = helpers.convertDateToCycleMonth(helpers.getCurrentDate("date"));
        int IDM_id = Integer.parseInt(listDataChild.get(groupPosition).get(childPosition).get("IDM_id"));
        child_clicked = listDataChild.get(groupPosition).get(childPosition);
        child_clicked.put("temp_plandetails_id", "0");

        history = pdc.getMonthlyCallsByIDM_id(IDM_id, cycle_month);

        if (history.size() > 0) {
            int max_visit = dcc.getMaxVisit(IDM_id);

            if (Integer.parseInt(listDataChild.get(groupPosition).get(childPosition).get("plan_details_id")) == 0) {
                if (max_visit > history.size()) {
                    add_incidental_call.setVisibility(View.VISIBLE);
                } else if (max_visit == history.size())
                    add_incidental_call.setVisibility(View.GONE);
            }

            list_of_history.setVisibility(View.VISIBLE);
            child_adapter = new DoctorsHistoryAdapter(this, history);
            list_of_history.setAdapter(child_adapter);
        } else {
            add_incidental_call.setVisibility(View.VISIBLE);
            list_of_history.setVisibility(View.GONE);
        }

        return true;
    }

    private void prepareListData() {
        listDataChild = new HashMap<>();
        listDataHeader = new ArrayList<>();
        duplicate_list_child = new HashMap<>();
        Set<String> uniqueInstitutions = new LinkedHashSet<>();
        doctors = idmc.getDoctorsNotIncludedInMCP(ACPActivity.current_date);

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
        listAdapter = new ExpandableListAdapter(this, listDataHeader, listDataChild);
        list_of_doctors.setAdapter(listAdapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add_incidental_call:
                ACPActivity.check_adapter_acp = 30;
                this.finish();
                break;
        }
    }
}
