package com.ece.vbfc_bry07.calls.activity;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.ece.vbfc_bry07.calls.adapter.ExpandableListAdapter;
import com.ece.vbfc_bry07.calls.controller.DbHelper;
import com.ece.vbfc_bry07.calls.controller.DoctorsController;
import com.ece.vbfc_bry07.calls.controller.InstitutionDoctorMapsController;
import com.ece.vbfc_bry07.calls.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class DoctorsInfoActivity extends AppCompatActivity implements TextWatcher, ExpandableListView.OnChildClickListener {
    DbHelper dbHelper;
    DoctorsController DC;
    ExpandableListAdapter doctor_adapter;
    InstitutionDoctorMapsController idmc;

    List<String> listDataHeader;
    HashMap<Integer, ArrayList<HashMap<String, String>>> listDataChild, duplicate_list_child;

    EditText BtnSearch;
    ExpandableListView list_of_doctors;
    TextView TxtDoctorName, TxtDoctorNumber, TxtDoctorClass, TxtDoctorBirthDate, no_records, doctor_count, specialization, txt_email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doc_info);

        BtnSearch = (EditText) findViewById(R.id.BtnSearch);
        txt_email = (TextView) findViewById(R.id.txt_email);
        no_records = (TextView) findViewById(R.id.no_records);
        doctor_count = (TextView) findViewById(R.id.doctor_count);
        TxtDoctorName = (TextView) findViewById(R.id.TxtDoctorName);
        TxtDoctorClass = (TextView) findViewById(R.id.TxtDoctorClass);
        specialization = (TextView) findViewById(R.id.specialization);
        TxtDoctorNumber = (TextView) findViewById(R.id.TxtDoctorNumber);
        TxtDoctorBirthDate = (TextView) findViewById(R.id.TxtDoctorBirthDate);
        list_of_doctors = (ExpandableListView) findViewById(R.id.list_of_doctors);

        dbHelper = new DbHelper(this);
        DC = new DoctorsController(this);
        idmc = new InstitutionDoctorMapsController(this);

        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Doctors");
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#A25063")));

        prepareListData();

        BtnSearch.addTextChangedListener(this);
        list_of_doctors.setOnChildClickListener(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                break;
        }
        return super.onOptionsItemSelected(item);
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

    private void prepareListData() {
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
        doctor_count.setText("Doctor Count: " + doctors.size());
    }

    @Override
    public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
        String doc_number = listDataChild.get(groupPosition).get(childPosition).get("contact_number");
        String doc_birthdate = listDataChild.get(groupPosition).get(childPosition).get("birthday");
        String doc_email = listDataChild.get(groupPosition).get(childPosition).get("email_address");
        String class_name = listDataChild.get(groupPosition).get(childPosition).get("class_name") + " (" + listDataChild.get(groupPosition).get(childPosition).get("class_code") + "x)";

        TxtDoctorName.setText(listDataChild.get(groupPosition).get(childPosition).get("doc_name"));
        TxtDoctorClass.setText(class_name);
        specialization.setText(listDataChild.get(groupPosition).get(childPosition).get("specialization"));

        if (doc_number.equals(""))
            TxtDoctorNumber.setText("N/A");
        else
            TxtDoctorNumber.setText(doc_number);

        if (doc_birthdate.equals(""))
            TxtDoctorBirthDate.setText("N/A");
        else
            TxtDoctorBirthDate.setText(doc_birthdate);

        if (doc_email.equals(""))
            txt_email.setText("N/A");
        else
            txt_email.setText(doc_email);

        return false;
    }
}
