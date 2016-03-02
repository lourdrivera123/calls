package com.example.vbfc_bry07.calls.Activity;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.vbfc_bry07.calls.Adapter.DoctorListAdapter;
import com.example.vbfc_bry07.calls.Controller.DbHelper;
import com.example.vbfc_bry07.calls.Controller.DoctorsController;
import com.example.vbfc_bry07.calls.Controller.SpecializationsController;
import com.example.vbfc_bry07.calls.R;

import java.util.ArrayList;
import java.util.HashMap;

public class DoctorsInfoActivity extends AppCompatActivity implements View.OnClickListener {

    ListView DoctorsListView;
    DbHelper dbHelper;
    DoctorsController DC;

    ArrayList<HashMap<String, String>> all_doctors;

    TextView TxtDoctorName, TxtDoctorSpecialty, TxtDoctorNumber, TxtDoctorClass, TxtDoctorBirthDate;
    String DoctorName, DoctorSpecialty, DoctorNumber, DoctorClass, DoctorBirthDate;
    SearchView BtnSearch;

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
        BtnSearch = (SearchView) findViewById(R.id.BtnSearch);

        DoctorsListView = (ListView) findViewById(R.id.doctorsList);
        all_doctors = DC.SelectAllDoctors();
        ListAdapter doctorAdapter = new DoctorListAdapter(this, R.layout.adapter_doctors_list, all_doctors);
        DoctorsListView.setAdapter(doctorAdapter);

        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Doctors");
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#A25063")));

        DoctorsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // String selectedFromList = (DoctorsListView.getItemAtPosition(position).toString());
                DoctorName = all_doctors.get(position).get(DoctorsController.DOCTORS_DOC_NAME);
                DoctorSpecialty = all_doctors.get(position).get(SpecializationsController.SPECIALIZATION_NAME);
                DoctorNumber = all_doctors.get(position).get(DoctorsController.DOCTORS_CONTACT_NUMBER);
                DoctorBirthDate = all_doctors.get(position).get(DoctorsController.DOCTORS_BIRTHDAY);
                TxtDoctorName.setText(DoctorName);
                TxtDoctorSpecialty.setText(DoctorSpecialty);
                if(DoctorNumber.equals("")) {
                    TxtDoctorNumber.setText("No mobile # to display");
                } else {
                    TxtDoctorNumber.setText(DoctorNumber);
                }
                if(DoctorNumber.equals("")) {
                    TxtDoctorBirthDate.setText("No Birthdate to display");
                } else {
                    TxtDoctorBirthDate.setText(DoctorBirthDate);
                }
            }
        });

        BtnSearch.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_help, menu);
        return true;
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
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.BtnSearch:
                BtnSearch.onActionViewExpanded();
                break;
        }
    }
}
