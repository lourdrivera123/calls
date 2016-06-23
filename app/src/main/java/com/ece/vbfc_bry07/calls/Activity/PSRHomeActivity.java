package com.ece.vbfc_bry07.calls.activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.ece.vbfc_bry07.calls.psr_adapter.BirthdayAdapter;
import com.ece.vbfc_bry07.calls.psr_adapter.BroadcastMessagesAdapter;
import com.ece.vbfc_bry07.calls.controller.BroadcastsController;
import com.ece.vbfc_bry07.calls.controller.CallsController;
import com.ece.vbfc_bry07.calls.controller.DbHelper;
import com.ece.vbfc_bry07.calls.controller.DoctorsController;
import com.ece.vbfc_bry07.calls.controller.PlanDetailsController;
import com.ece.vbfc_bry07.calls.controller.PlansController;
import com.ece.vbfc_bry07.calls.Helpers;
import com.ece.vbfc_bry07.calls.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.HashMap;

public class PSRHomeActivity extends AppCompatActivity implements View.OnClickListener {
    Toolbar toolbar;
    ScrollView statistics;
    TextView username, no_data, call_rate, call_reach, planned_calls, incidental_calls, recovered_calls,
            declared_missed_calls, unprocessed_calls, cycle_number, count_birthday, count_broadcast, notif_mcp;
    LinearLayout root, birthday, quick_sign, actual_coverage_plan, master_coverage_plan, doctors_information,
            call_report, sales_report, material_monitoring, status_summary, broadcast_msg, export_database;

    SharedPreferences sharedpref;

    Helpers helpers;
    DbHelper dbHelper;
    CallsController cc;
    PlansController pc;
    DoctorsController dc;
    BroadcastsController bc;
    PlanDetailsController pdc;

    int cycle_month, current_year;

    ArrayList<HashMap<String, String>> broadcasts, birthdays;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_psr_home);

        root = (LinearLayout) findViewById(R.id.root);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        no_data = (TextView) findViewById(R.id.no_data);
        username = (TextView) findViewById(R.id.username);
        call_rate = (TextView) findViewById(R.id.call_rate);
        notif_mcp = (TextView) findViewById(R.id.notif_mcp);
        call_reach = (TextView) findViewById(R.id.call_reach);
        statistics = (ScrollView) findViewById(R.id.statistics);
        birthday = (LinearLayout) findViewById(R.id.birthday);
        quick_sign = (LinearLayout) findViewById(R.id.quick_sign);
        cycle_number = (TextView) findViewById(R.id.cycle_number);
        call_report = (LinearLayout) findViewById(R.id.call_report);
        planned_calls = (TextView) findViewById(R.id.planned_calls);
        sales_report = (LinearLayout) findViewById(R.id.sales_report);
        count_birthday = (TextView) findViewById(R.id.count_birthday);
        broadcast_msg = (LinearLayout) findViewById(R.id.broadcast_msg);
        count_broadcast = (TextView) findViewById(R.id.count_broadcast);
        recovered_calls = (TextView) findViewById(R.id.recovered_calls);
        export_database = (LinearLayout) findViewById(R.id.export_database);
        incidental_calls = (TextView) findViewById(R.id.incidental_calls);
        status_summary = (LinearLayout) findViewById(R.id.status_summary);
        unprocessed_calls = (TextView) findViewById(R.id.unprocessed_calls);
        doctors_information = (LinearLayout) findViewById(R.id.doctors_information);
        material_monitoring = (LinearLayout) findViewById(R.id.material_monitoring);
        declared_missed_calls = (TextView) findViewById(R.id.declared_missed_calls);
        actual_coverage_plan = (LinearLayout) findViewById(R.id.actual_coverage_plan);
        master_coverage_plan = (LinearLayout) findViewById(R.id.master_coverage_plan);

        helpers = new Helpers();
        dbHelper = new DbHelper(this);
        cc = new CallsController(this);
        dc = new DoctorsController(this);
        pc = new PlansController(this);
        bc = new BroadcastsController(this);
        pdc = new PlanDetailsController(this);

        sharedpref = getSharedPreferences("ECECalls", Context.MODE_PRIVATE);
        cycle_month = helpers.convertDateToCycleMonth(helpers.getCurrentDate(""));
        current_year = helpers.getCurrentYear();

        setSupportActionBar(toolbar);
        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        birthday.setOnClickListener(this);
        quick_sign.setOnClickListener(this);
        call_report.setOnClickListener(this);
        sales_report.setOnClickListener(this);
        broadcast_msg.setOnClickListener(this);
        status_summary.setOnClickListener(this);
        export_database.setOnClickListener(this);
        actual_coverage_plan.setOnClickListener(this);
        master_coverage_plan.setOnClickListener(this);
        doctors_information.setOnClickListener(this);
        material_monitoring.setOnClickListener(this);

        username.setText(getUsername());
        cycle_number.setText("Cycle Statistics (Cycle " + cycle_month + ")");

        birthdays = dc.getBirthdayByDate(helpers.convertToAlphabetDate(helpers.getCurrentDate(""), "without_year"));

        if (birthdays.size() > 0) {
            count_birthday.setVisibility(View.VISIBLE);
            count_birthday.setText(birthdays.size() + "");
        } else
            count_birthday.setVisibility(View.GONE);
    }

    @Override
    protected void onResume() {
        broadcasts = bc.getBroadcastMessages();

        if (!sharedpref.contains("Username")) {
            startActivity(new Intent(this, LoginActivity.class));
            this.finish();
        } else {
            if (!pdc.checkPlanDetailsByPlanID(pc.getPlanID(cycle_month))) {
                no_data.setVisibility(View.VISIBLE);
                statistics.setVisibility(View.GONE);
            } else {
                no_data.setVisibility(View.GONE);
                statistics.setVisibility(View.VISIBLE);

                int plannedCalls = cc.fetchPlannedCalls(cycle_month);
                int IncidentalCalls = cc.getCallReportDetails("incidental_call", cycle_month).size();
                int RecoveredCalls = cc.getCallReportDetails("recovered_call", cycle_month).size();
                int DeclaredMissedCalls = cc.getCallReportDetails("declared_missed_call", cycle_month).size();
                int ActualCoveredCalls = cc.getCallReportDetails("actual_covered_call", cycle_month).size();
                int UnprocessedCalls = plannedCalls - (ActualCoveredCalls + IncidentalCalls);
                String callRate = cc.callRate(cycle_month);
                String callReach = cc.callReach(cycle_month);

                planned_calls.setText(String.valueOf(plannedCalls));
                incidental_calls.setText(String.valueOf(IncidentalCalls));
                recovered_calls.setText(String.valueOf(RecoveredCalls));
                declared_missed_calls.setText(String.valueOf(DeclaredMissedCalls));
                unprocessed_calls.setText(String.valueOf(UnprocessedCalls));
                call_rate.setText(callRate);
                call_reach.setText(callReach);
            }

            if (pdc.checkForMCPNotif())
                notif_mcp.setVisibility(View.VISIBLE);
            else
                notif_mcp.setVisibility(View.GONE);

            if (broadcasts.size() > 0) {
                count_broadcast.setVisibility(View.VISIBLE);
                count_broadcast.setText(broadcasts.size() + "");
            } else
                count_broadcast.setVisibility(View.GONE);
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
            case R.id.logout:
                SharedPreferences.Editor editor = sharedpref.edit();
                editor.clear();
                editor.apply();
                this.finish();
                startActivity(new Intent(this, LoginActivity.class));
                break;

            case R.id.sync:

                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.quick_sign:
                startActivity(new Intent(this, QuickSignActivity.class));
                break;
            case R.id.actual_coverage_plan:
                startActivity(new Intent(this, ACPActivity.class));
                break;
            case R.id.master_coverage_plan:
                startActivity(new Intent(this, MCPActivity.class));
                break;
            case R.id.doctors_information:
                startActivity(new Intent(this, DoctorsInfoActivity.class));
                break;
            case R.id.call_report:
                startActivity(new Intent(this, CallReportActivity.class));
                break;
            case R.id.sales_report:
                startActivity(new Intent(this, SalesReportActivity.class));
//                try {
//                    dbHelper.copyDatabase();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
                break;
            case R.id.material_monitoring:
                startActivity(new Intent(this, MaterialMonitoringActivity.class));
                break;
            case R.id.status_summary:
                startActivity(new Intent(this, PSRStatusSummaryActivity.class));
                break;

            case R.id.birthday:
                if (birthdays.size() > 0) {
                    Dialog dialog = new Dialog(this);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setContentView(R.layout.layout_listview_only);
                    dialog.show();

                    ListView listview = (ListView) dialog.findViewById(R.id.listview);
                    listview.setAdapter(new BirthdayAdapter(this, birthdays));
                }
                break;

            case R.id.broadcast_msg:
                if (broadcasts.size() > 0) {
                    Dialog dialog = new Dialog(this);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setContentView(R.layout.layout_listview_only);
                    dialog.getWindow().setLayout(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT);
                    dialog.show();

                    ListView listview = (ListView) dialog.findViewById(R.id.listview);
                    listview.setAdapter(new BroadcastMessagesAdapter(this, broadcasts));
                }
                break;

            case R.id.export_database:
                new ExportDatabaseFileTask().execute();
                break;
        }
    }

    public String getUsername() {
        return sharedpref.getString("Username", "");
    }

    /////////////////////////EXPORTING DATABASE
    private class ExportDatabaseFileTask extends AsyncTask<String, Void, Boolean> {
        private final ProgressDialog dialog = new ProgressDialog(PSRHomeActivity.this);

        // can use UI thread here
        protected void onPreExecute() {
            this.dialog.setMessage("Exporting database...");
            this.dialog.show();
        }

        // automatically done on worker thread (separate from UI thread)
        protected Boolean doInBackground(final String... args) {
            File dbFile = new File(Environment.getDataDirectory() + "/data/" + getPackageName() + "/databases/ECE_calls");

            File exportDir = new File(Environment.getExternalStorageDirectory(), "");
            if (!exportDir.exists())
                exportDir.mkdirs();

            File file = new File(exportDir, dbFile.getName());

            try {
                file.createNewFile();
                this.copyFile(dbFile, file);
                return true;
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }

        // can use UI thread here
        protected void onPostExecute(final Boolean success) {
            if (this.dialog.isShowing()) {
                this.dialog.dismiss();
            }
            if (success)
                Toast.makeText(PSRHomeActivity.this, "Export successful!", Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(PSRHomeActivity.this, "Export failed", Toast.LENGTH_SHORT).show();
        }

        void copyFile(File src, File dst) throws IOException {
            FileChannel inChannel = new FileInputStream(src).getChannel();
            FileChannel outChannel = new FileOutputStream(dst).getChannel();

            try {
                inChannel.transferTo(0, inChannel.size(), outChannel);
            } finally {
                if (inChannel != null)
                    inChannel.close();

                outChannel.close();
            }
        }
    }
}
