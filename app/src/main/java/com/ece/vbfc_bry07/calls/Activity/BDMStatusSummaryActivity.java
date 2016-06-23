
package com.ece.vbfc_bry07.calls.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.ece.vbfc_bry07.calls.Helpers;
import com.ece.vbfc_bry07.calls.R;
import com.ece.vbfc_bry07.calls.bdm_adapter.StatusSummaryDistrictCallAdapter;

public class BDMStatusSummaryActivity extends AppCompatActivity implements View.OnClickListener {
    Toolbar toolbar;
    TextView cycle_number;
    ImageView arrow_one, arrow_two, arrow_three;
    ListView list_of_names, list_of_broadcasts, list_of_developmental_plan;
    LinearLayout for_district_call_performance, for_broadcast_message, for_developmental_plan_due;

    StatusSummaryDistrictCallAdapter dist_call_adapter;

    Helpers helpers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status_summary_bdm);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        arrow_one = (ImageView) findViewById(R.id.arrow_one);
        arrow_two = (ImageView) findViewById(R.id.arrow_two);
        arrow_three = (ImageView) findViewById(R.id.arrow_three);
        cycle_number = (TextView) findViewById(R.id.cycle_number);
        list_of_names = (ListView) findViewById(R.id.list_of_names);
        list_of_broadcasts = (ListView) findViewById(R.id.list_of_broadcasts);
        list_of_developmental_plan = (ListView) findViewById(R.id.list_of_developmental_plan);
        for_broadcast_message = (LinearLayout) findViewById(R.id.for_broadcast_message);
        for_developmental_plan_due = (LinearLayout) findViewById(R.id.for_developmental_plan_due);
        for_district_call_performance = (LinearLayout) findViewById(R.id.for_district_call_performance);

        setSupportActionBar(toolbar);
        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Status Summary");

        helpers = new Helpers();
        dist_call_adapter = new StatusSummaryDistrictCallAdapter(this);

        cycle_number.setText("Cycle " + helpers.convertDateToCycleMonth(helpers.getCurrentDate("")));

        list_of_names.setAdapter(dist_call_adapter);
        arrow_one.setOnClickListener(this);
        arrow_two.setOnClickListener(this);
        arrow_three.setOnClickListener(this);
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
            case R.id.arrow_one:
                if (arrow_one.getDrawable().getConstantState() == getResources().getDrawable(R.mipmap.arrow_down).getConstantState()) {
                    for_district_call_performance.setVisibility(View.VISIBLE);
                    arrow_one.setImageDrawable(getResources().getDrawable(R.mipmap.arrow_up));
                } else {
                    for_district_call_performance.setVisibility(View.GONE);
                    arrow_one.setImageDrawable(getResources().getDrawable(R.mipmap.arrow_down));
                }

                break;

            case R.id.arrow_two:
                if (arrow_two.getDrawable().getConstantState() == getResources().getDrawable(R.mipmap.arrow_down).getConstantState()) {
                    for_broadcast_message.setVisibility(View.VISIBLE);
                    arrow_two.setImageDrawable(getResources().getDrawable(R.mipmap.arrow_up));
                } else {
                    for_broadcast_message.setVisibility(View.GONE);
                    arrow_two.setImageDrawable(getResources().getDrawable(R.mipmap.arrow_down));
                }

                break;

            case R.id.arrow_three:
                if (arrow_three.getDrawable().getConstantState() == getResources().getDrawable(R.mipmap.arrow_down).getConstantState()) {
                    for_developmental_plan_due.setVisibility(View.VISIBLE);
                    arrow_three.setImageDrawable(getResources().getDrawable(R.mipmap.arrow_up));
                } else {
                    for_developmental_plan_due.setVisibility(View.GONE);
                    arrow_three.setImageDrawable(getResources().getDrawable(R.mipmap.arrow_down));
                }

                break;
        }
    }
}
