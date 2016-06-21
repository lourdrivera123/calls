package com.ece.vbfc_bry07.calls.dialog;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.ece.vbfc_bry07.calls.R;
import com.ece.vbfc_bry07.calls.controller.CallsController;
import com.ece.vbfc_bry07.calls.controller.MissedCallsController;
import com.ece.vbfc_bry07.calls.controller.ReasonsController;

import java.util.ArrayList;
import java.util.HashMap;

public class DeclareMissedCalls extends AppCompatActivity implements View.OnClickListener {
    TextView previous, next, doctor_name;
    EditText remarks;
    Spinner spinner_reasons;
    LinearLayout root;

    ArrayList<String> reasons;
    ArrayList<HashMap<String, String>> unprocessed_calls_per_day;

    CallsController cc;
    ReasonsController rc;
    MissedCallsController mcc;

    String date;
    int index = 0;

    @SuppressWarnings("unchecked")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        DisplayMetrics metrics = getResources().getDisplayMetrics();
        int screenWidth = (int) (metrics.widthPixels * 0.60);

        setContentView(R.layout.dialog_declare_as_missed);
        getWindow().setLayout(screenWidth, LinearLayout.LayoutParams.WRAP_CONTENT);

        next = (TextView) findViewById(R.id.next);
        root = (LinearLayout) findViewById(R.id.root);
        remarks = (EditText) findViewById(R.id.remarks);
        previous = (TextView) findViewById(R.id.previous);
        doctor_name = (TextView) findViewById(R.id.doctor_name);
        spinner_reasons = (Spinner) findViewById(R.id.spinner_reasons);

        date = getIntent().getStringExtra("date");

        cc = new CallsController(this);
        rc = new ReasonsController(this);
        mcc = new MissedCallsController(this);
        reasons = rc.getEnabledReasons();
        unprocessed_calls_per_day = cc.getUnprocessedCallsPerDay(date);

        ArrayAdapter adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, reasons);
        spinner_reasons.setAdapter(adapter);
        doctor_name.setText(unprocessed_calls_per_day.get(index).get("doc_name"));

        previous.setEnabled(false);
        previous.setOnClickListener(this);
        next.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.previous:
                if (previous.isEnabled()) {
                    next.setText("Next");
                    index -= 1;
                    String current_name = unprocessed_calls_per_day.get(index).get("doc_name");
                    doctor_name.setText(current_name);

                    if (current_name.equals(unprocessed_calls_per_day.get(0).get("doc_name")))
                        previous.setEnabled(false);
                }

                break;

            case R.id.next:
                previous.setEnabled(true);
                HashMap<String, String> map = unprocessed_calls_per_day.get(index);
                map.put("remarks", remarks.getText().toString());
                map.put("reason", String.valueOf(rc.getReasonID(spinner_reasons.getSelectedItem().toString())));
                unprocessed_calls_per_day.set(index, map);

                if (next.getText().toString().equals("Next")) {
                    index += 1;
                    String current = unprocessed_calls_per_day.get(index).get("doc_name");
                    doctor_name.setText(current);

                    if (current.equals(unprocessed_calls_per_day.get(unprocessed_calls_per_day.size() - 1).get("doc_name")))
                        next.setText("Save");
                } else {
                    if (mcc.insertMissedCalls(unprocessed_calls_per_day)) {
                        this.finish();
                        Snackbar.make(root, "Saved successfully", Snackbar.LENGTH_SHORT).show();
                    } else
                        Snackbar.make(root, "Error occurred", Snackbar.LENGTH_SHORT).show();
                }

                break;
        }
    }
}
