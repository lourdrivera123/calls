package com.example.vbfc_bry07.calls.Activity;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.vbfc_bry07.calls.Adapter.DoctorListAdapter;
import com.example.vbfc_bry07.calls.Controller.DbHelper;
import com.example.vbfc_bry07.calls.Controller.DoctorsController;
import com.example.vbfc_bry07.calls.Controller.SpecializationsController;
import com.example.vbfc_bry07.calls.R;

import java.util.ArrayList;
import java.util.HashMap;

public class DoctorsInfoActivity extends AppCompatActivity implements TextWatcher {

    ListView DoctorsListView;
    DbHelper dbHelper;
    DoctorsController DC;

    ArrayList<HashMap<String, String>> all_doctors;
    ListAdapter doctorAdapter;
    // HashMap<Integer, ArrayList<HashMap<String, String>>> listDataChild;
    ArrayList<HashMap<String, String>> doctors_array = new ArrayList<>();
    ArrayList<HashMap<String, Object>> searchResults;

    TextView TxtDoctorName, TxtDoctorSpecialty, TxtDoctorNumber, TxtDoctorClass, TxtDoctorBirthDate, no_records;
    String DoctorName, DoctorSpecialty, DoctorNumber, DoctorClass, DoctorBirthDate;
    EditText BtnSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doc_info);

        dbHelper = new DbHelper(this);
        DC = new DoctorsController(this);

        TxtDoctorName = (TextView) findViewById(R.id.TxtDoctorName);
        TxtDoctorSpecialty = (TextView) findViewById(R.id.TxtDoctorSpecialty);
        TxtDoctorNumber = (TextView) findViewById(R.id.TxtDoctorNumber);
        TxtDoctorClass = (TextView) findViewById(R.id.TxtDoctorClass);
        TxtDoctorBirthDate = (TextView) findViewById(R.id.TxtDoctorBirthDate);
        BtnSearch = (EditText) findViewById(R.id.BtnSearch);
        no_records = (TextView) findViewById(R.id.no_records);

        DoctorsListView = (ListView) findViewById(R.id.doctorsList);
        all_doctors = DC.SelectAllDoctors();
        doctors_array.addAll(all_doctors);
        doctorAdapter = new DoctorListAdapter(this, R.layout.adapter_doctors_list, all_doctors);
        DoctorsListView.setAdapter(doctorAdapter);

        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Doctors");
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#A25063")));

        DoctorsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                DoctorName = all_doctors.get(position).get(DoctorsController.DOCTORS_DOC_NAME);
                DoctorSpecialty = all_doctors.get(position).get(SpecializationsController.SPECIALIZATION_NAME);
                DoctorNumber = all_doctors.get(position).get(DoctorsController.DOCTORS_CONTACT_NUMBER);
                DoctorBirthDate = all_doctors.get(position).get(DoctorsController.DOCTORS_BIRTHDAY);
                TxtDoctorName.setText(DoctorName);
                if(DoctorSpecialty.equals("")) {
                    TxtDoctorSpecialty.setText("No Specialty to display");
                } else {
                    TxtDoctorSpecialty.setText(DoctorSpecialty);
                }
                if (DoctorNumber.equals("")) {
                    TxtDoctorNumber.setText("No mobile # to display");
                } else {
                    TxtDoctorNumber.setText(DoctorNumber);
                }
                if (DoctorBirthDate.equals("")) {
                    TxtDoctorBirthDate.setText("No Birthdate to display");
                } else {
                    TxtDoctorBirthDate.setText(DoctorBirthDate);
                }
            }
        });

        BtnSearch.addTextChangedListener(this);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.help, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                break;
            case R.id.BtnHelp:
                Toast.makeText(this, "HELP!", Toast.LENGTH_SHORT).show();
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
        if(!search.equals("")) {
            all_doctors.clear();

            for (int x = 0; x < doctors_array.size(); x++) {
                HashMap<String, String> hash = doctors_array.get(x);

                if (hash.get(DoctorsController.DOCTORS_DOC_NAME).toLowerCase().contains(search.toLowerCase())) {
                    all_doctors.add(hash);
                }
            }

            if (all_doctors.size() > 0) {
                no_records.setVisibility(View.GONE);
                DoctorsListView.setVisibility(View.VISIBLE);

                doctorAdapter = new DoctorListAdapter(this, R.layout.adapter_doctors_list, all_doctors);
                DoctorsListView.setAdapter(doctorAdapter);
            } else {
                no_records.setVisibility(View.VISIBLE);
                DoctorsListView.setVisibility(View.GONE);
            }

        } else {
            all_doctors = DC.SelectAllDoctors();
            doctorAdapter = new DoctorListAdapter(this, R.layout.adapter_doctors_list, all_doctors);
            DoctorsListView.setAdapter(doctorAdapter);
        }

    }
}
