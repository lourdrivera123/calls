package com.ece.vbfc_bry07.calls.Activity;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatDialog;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.ece.vbfc_bry07.calls.Controller.CallsController;
import com.ece.vbfc_bry07.calls.Controller.DbHelper;
import com.ece.vbfc_bry07.calls.Controller.PlansController;
import com.ece.vbfc_bry07.calls.Helpers;
import com.ece.vbfc_bry07.calls.R;

import java.util.ArrayList;
import java.util.HashMap;

public class StatusSummaryActivity extends AppCompatActivity implements View.OnClickListener {
    TextView tv_month, call_rate, call_reach, planned_calls, incidental_calls, recovered_calls, declared_missed_calls, unprocessed_calls, no_data;
    ScrollView statistics;

    Helpers helpers;
    DbHelper dbHelper;
    CallsController cc;
    PlansController pc;

    String current_cycle_month = "strftime('%m', date())";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status_summary);

        tv_month = (TextView) findViewById(R.id.tv_month);
        call_rate = (TextView) findViewById(R.id.call_rate);
        call_reach = (TextView) findViewById(R.id.call_reach);
        planned_calls = (TextView) findViewById(R.id.planned_calls);
        incidental_calls = (TextView) findViewById(R.id.incidental_calls);
        recovered_calls = (TextView) findViewById(R.id.recovered_calls);
        declared_missed_calls = (TextView) findViewById(R.id.declared_missed_calls);
        unprocessed_calls = (TextView) findViewById(R.id.unprocessed_calls);
        no_data = (TextView) findViewById(R.id.no_data);
        statistics = (ScrollView) findViewById(R.id.statistics);

        helpers = new Helpers();
        dbHelper = new DbHelper(this);
        cc = new CallsController(this);
        pc = new PlansController(this);

        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Status Summary");
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#A25063")));

        tv_month.setText(helpers.getMonthYear());
        tv_month.setPaintFlags(tv_month.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        AddData(helpers.convertDateToCycleMonth(helpers.getCurrentDate("")));

        tv_month.setOnClickListener(this);
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

    void AddData(int month) {
        if (pc.checkIfHasPlan(month) == 0) {
            no_data.setVisibility(View.VISIBLE);
            statistics.setVisibility(View.GONE);
        } else {
            no_data.setVisibility(View.GONE);
            statistics.setVisibility(View.VISIBLE);

            int plannedCalls = cc.fetchPlannedCalls(month);
            int IncidentalCalls = cc.IncidentalCalls(month);
            int RecoveredCalls = cc.RecoveredCalls(month);
            int DeclaredMissedCalls = cc.DeclaredMissedCalls(month);
            int ActualCoveredCalls = cc.ActualCoveredCalls(current_cycle_month);
            int UnprocessedCalls = plannedCalls - (ActualCoveredCalls + IncidentalCalls);
            String callRate = cc.callRate(month);
            String callReach = cc.callReach(month);

            planned_calls.setText(String.valueOf(plannedCalls));
            incidental_calls.setText(String.valueOf(IncidentalCalls));
            recovered_calls.setText(String.valueOf(RecoveredCalls));
            declared_missed_calls.setText(String.valueOf(DeclaredMissedCalls));
            unprocessed_calls.setText(String.valueOf(UnprocessedCalls));
            call_rate.setText(callRate);
            call_reach.setText(callReach);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_month:
                AlertDialog.Builder alert = new AlertDialog.Builder(this);
                View view = LayoutInflater.from(this).inflate(R.layout.fragment_calls, null);
                alert.setView(view);
                final AppCompatDialog pdialog = alert.create();
                pdialog.show();

                final ArrayList<HashMap<String, String>> all_plans = pc.getAllPlans();
                ArrayList<String> names = new ArrayList<>();

                for (int x = 0; x < all_plans.size(); x++)
                    names.add(all_plans.get(x).get("name"));

                ListView listview_calls = (ListView) view.findViewById(R.id.listview_calls);
                listview_calls.setAdapter(new ArrayAdapter<>(this, R.layout.item_small_textview_colored, names));
                listview_calls.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        AddData(Integer.parseInt(all_plans.get(position).get("cycle_number")));
                        pdialog.dismiss();
                    }
                });
                break;
        }
    }
}
