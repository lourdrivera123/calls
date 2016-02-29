package com.example.vbfc_bry07.calls.Activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.vbfc_bry07.calls.Adapter.DoctorListAdapter;
import com.example.vbfc_bry07.calls.R;

public class DoctorsInfoActivity extends AppCompatActivity {

    ListView DoctorsListView;
    String[] doctors = {"Doctor 1", "Doctor 2", "Doctor 3", "Doctor 4", "Doctor 5", "Doctor 6", "Doctor 7"};

    TextView TxtDoctorName, TxtDoctorSpecialty, TxtDoctorNumber, TxtDoctorClass, TxtDoctorBirthDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doc_info);

        TxtDoctorName = (TextView) findViewById(R.id.TxtDoctorName);
        TxtDoctorSpecialty = (TextView) findViewById(R.id.TxtDoctorSpecialty);
        TxtDoctorNumber = (TextView) findViewById(R.id.TxtDoctorNumber);
        TxtDoctorClass = (TextView) findViewById(R.id.TxtDoctorClass);
        TxtDoctorBirthDate = (TextView) findViewById(R.id.TxtDoctorBirthDate);

        DoctorsListView = (ListView) findViewById(R.id.doctorsList);
        ListAdapter doctorAdapter = new DoctorListAdapter(this, doctors);
        DoctorsListView.setAdapter(doctorAdapter);

        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Doctors");

        DoctorsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedFromList = (DoctorsListView.getItemAtPosition(position).toString());
                TxtDoctorName.setText(selectedFromList);
            }
        });
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
}
