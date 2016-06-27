package com.ece.vbfc_bry07.calls.bdm_activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;

import com.ece.vbfc_bry07.calls.Helpers;
import com.ece.vbfc_bry07.calls.R;
import com.ece.vbfc_bry07.calls.bdm_adapter.CalendarCallAdapter;

import java.util.Calendar;
import java.util.TimeZone;

public class CalendarCallActivity extends AppCompatActivity implements View.OnClickListener {
    Toolbar toolbar;
    GridView calendar;
    Button btn_joint_call, btn_service_call;

    Helpers helpers;
    CalendarCallAdapter calendar_adapter;

    Calendar cal_month;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar_call);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        calendar = (GridView) findViewById(R.id.calendar);
        btn_joint_call = (Button) findViewById(R.id.btn_joint_call);
        btn_service_call = (Button) findViewById(R.id.btn_service_call);

        setSupportActionBar(toolbar);
        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Call Activity");

        TimeZone zone = TimeZone.getTimeZone("GMT+8");
        cal_month = Calendar.getInstance(zone);

        helpers = new Helpers();
        calendar_adapter = new CalendarCallAdapter(this, cal_month);

        calendar.setAdapter(calendar_adapter);
        btn_joint_call.setOnClickListener(this);
        btn_service_call.setOnClickListener(this);
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
            case R.id.btn_joint_call:

                break;

            case R.id.btn_service_call:

                break;
        }
    }
}
