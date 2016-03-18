package com.example.vbfc_bry07.calls.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.widget.TextView;

import com.example.vbfc_bry07.calls.Controller.PlanDetailsController;
import com.example.vbfc_bry07.calls.R;

import java.util.Date;
import java.util.HashMap;

public class SignatureFormActivity extends AppCompatActivity {
    Toolbar toolbar;
    TextView timestamp_now, doctor_name;

    PlanDetailsController pdc;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signature_form);

        toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setTitle("");

        timestamp_now = (TextView) findViewById(R.id.timestamp_now);
        doctor_name = (TextView) findViewById(R.id.doctor_name);

        pdc = new PlanDetailsController(this);

        Intent intent = getIntent();
        HashMap<String, String> details = (HashMap<String, String>) intent.getSerializableExtra("call_details");

        timestamp_now.setText(details.get("calls_end"));
        doctor_name.setText(pdc.getDoctorByPlanDetailsID(Integer.parseInt(details.get("calls_plan_details_id")), details.get("calls_date")));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.save_clear_cancel_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
    }
}
