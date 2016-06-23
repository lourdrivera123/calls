package com.ece.vbfc_bry07.calls.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.ece.vbfc_bry07.calls.R;

public class BDMHomeActivity extends AppCompatActivity implements View.OnClickListener {
    SharedPreferences sharedpref;

    Toolbar toolbar;
    LinearLayout actual_call, itinerary, developmental_plan, pmr, mcp_approval, status_summary, pmr_coverage, export_database;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bdm_home);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        actual_call = (LinearLayout) findViewById(R.id.actual_call);
        itinerary = (LinearLayout) findViewById(R.id.itinerary);
        developmental_plan = (LinearLayout) findViewById(R.id.developmental_plan);
        pmr = (LinearLayout) findViewById(R.id.pmr);
        mcp_approval = (LinearLayout) findViewById(R.id.mcp_approval);
        status_summary = (LinearLayout) findViewById(R.id.status_summary);
        pmr_coverage = (LinearLayout) findViewById(R.id.pmr_coverage);
        export_database = (LinearLayout) findViewById(R.id.export_database);

        sharedpref = getSharedPreferences("ECECalls", Context.MODE_PRIVATE);

        setSupportActionBar(toolbar);
        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setTitle("");

        actual_call.setOnClickListener(this);
        itinerary.setOnClickListener(this);
        developmental_plan.setOnClickListener(this);
        pmr.setOnClickListener(this);
        mcp_approval.setOnClickListener(this);
        status_summary.setOnClickListener(this);
        pmr_coverage.setOnClickListener(this);
        export_database.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        if (!sharedpref.contains("Username")) {
            startActivity(new Intent(this, LoginActivity.class));
            this.finish();
        }

        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.logout, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.sync:

                break;

            case R.id.logout:
                SharedPreferences.Editor editor = sharedpref.edit();
                editor.clear();
                editor.apply();
                this.finish();
                startActivity(new Intent(this, LoginActivity.class));

                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.actual_call:

                break;

            case R.id.itinerary:

                break;

            case R.id.developmental_plan:

                break;

            case R.id.pmr:

                break;

            case R.id.mcp_approval:

                break;

            case R.id.status_summary:
                startActivity(new Intent(this, BDMStatusSummaryActivity.class));
                break;

            case R.id.pmr_coverage:

                break;

            case R.id.export_database:

                break;
        }
    }
}
