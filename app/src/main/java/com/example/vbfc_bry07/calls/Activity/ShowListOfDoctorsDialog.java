package com.example.vbfc_bry07.calls.Activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.example.vbfc_bry07.calls.Adapter.ExpandableListAdapter;
import com.example.vbfc_bry07.calls.Controller.InstitutionDoctorMapsController;
import com.example.vbfc_bry07.calls.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class ShowListOfDoctorsDialog extends AppCompatActivity implements TextWatcher, ExpandableListView.OnChildClickListener {
    ExpandableListView list_of_doctors;
    TextView no_records;
    EditText search_doctor;

    List<String> listDataHeader;
    ArrayList<HashMap<String, String>> doctors;
    public static HashMap<String, String> child_clicked;
    HashMap<Integer, ArrayList<HashMap<String, String>>> listDataChild, duplicate_list_child;

    InstitutionDoctorMapsController idmc;
    ExpandableListAdapter listAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_choose_doctor);

        list_of_doctors = (ExpandableListView) findViewById(R.id.list_of_doctors);
        no_records = (TextView) findViewById(R.id.no_records);
        search_doctor = (EditText) findViewById(R.id.search_doctor);

        idmc = new InstitutionDoctorMapsController(this);
        prepareListData();

        search_doctor.addTextChangedListener(this);
        list_of_doctors.setOnChildClickListener(this);
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
        child_clicked = listDataChild.get(groupPosition).get(childPosition);

        if (MCPActivity.check_adapter_mcp == 23 && ACPActivity.check_adapter_acp == 0)
            MCPActivity.check_adapter_mcp = 10;
        else if (ACPActivity.check_adapter_acp == 20 && MCPActivity.check_adapter_mcp == 0)
            ACPActivity.check_adapter_acp = 30;

        this.finish();
        return true;
    }

    private void prepareListData() {
        listDataChild = new HashMap<>();
        listDataHeader = new ArrayList<>();
        duplicate_list_child = new HashMap<>();
        Set<String> uniqueInstitutions = new LinkedHashSet<>();

        if (MCPActivity.check_adapter_mcp == 23 && ACPActivity.check_adapter_acp == 0)
            doctors = idmc.getDoctorsWithInstitutions("");
        else if (ACPActivity.check_adapter_acp == 20 && MCPActivity.check_adapter_mcp == 0)
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
}
