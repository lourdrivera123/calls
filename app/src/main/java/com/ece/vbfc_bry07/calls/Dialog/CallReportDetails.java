package com.ece.vbfc_bry07.calls.dialog;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;

import com.ece.vbfc_bry07.calls.adapter.ExpandableListAdapter;
import com.ece.vbfc_bry07.calls.controller.CallsController;
import com.ece.vbfc_bry07.calls.Helpers;
import com.ece.vbfc_bry07.calls.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CallReportDetails extends AppCompatActivity {
    ExpandableListView call_report_details;

    Helpers helpers;
    CallsController cc;

    HashMap<Integer, ArrayList<HashMap<String, String>>> listDataChild;
    List<String> listDataHeader;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        DisplayMetrics metrics = getResources().getDisplayMetrics();
        int screenSize = (int) (metrics.widthPixels * 0.70);

        setContentView(R.layout.dialog_expandable);
        getWindow().setLayout(screenSize, LinearLayout.LayoutParams.WRAP_CONTENT);

        call_report_details = (ExpandableListView) findViewById(R.id.call_report_details);

        helpers = new Helpers();
        cc = new CallsController(this);

        prepareData();
    }

    void prepareData() {
        listDataHeader = new ArrayList<>();
        listDataChild = new HashMap<>();
        int cycle_month = helpers.convertDateToCycleMonth(helpers.getCurrentDate(""));

        listDataHeader.add("Actual Covered Calls");
        listDataChild.put(0, cc.getCallReportDetails("actual_covered_call", cycle_month));
        listDataHeader.add("Declared Missed Calls");
        listDataChild.put(1, cc.getCallReportDetails("declared_missed_call", cycle_month));
        listDataHeader.add("Recovered Calls");
        listDataChild.put(2, cc.getCallReportDetails("recovered_call", cycle_month));
        listDataHeader.add("Incidental Calls");
        listDataChild.put(3, cc.getCallReportDetails("incidental_call", cycle_month));

        call_report_details.setAdapter(new ExpandableListAdapter(this, listDataHeader, listDataChild));
    }
}
