package com.ece.vbfc_bry07.calls.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ece.vbfc_bry07.calls.R;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;

public class CallReportAdapter extends BaseAdapter {
    Context context;
    ArrayList<HashMap<String, String>> objects, previous_objects, current_objects;

    TextView doctor_name, cycle_before, cycle_now, average;

    public CallReportAdapter(Context context, ArrayList<HashMap<String, String>> objects, ArrayList<HashMap<String, String>> previous_objects,
                             ArrayList<HashMap<String, String>> current_objects) {
        this.context = context;
        this.objects = objects;
        this.previous_objects = previous_objects;
        this.current_objects = current_objects;
    }

    @Override
    public int getCount() {
        return objects.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        int prev_call = 0, prev_total = 0, current_call = 0, current_total = 0;

        if (convertView == null) {
            LayoutInflater vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(R.layout.adapter_call_report, parent, false);
        }

        doctor_name = (TextView) v.findViewById(R.id.doctor_name);
        cycle_before = (TextView) v.findViewById(R.id.cycle_before);
        cycle_now = (TextView) v.findViewById(R.id.cycle_now);
        average = (TextView) v.findViewById(R.id.average);

        doctor_name.setText(objects.get(position).get("doc_name"));

        for (int x = 0; x < previous_objects.size(); x++) {
            if (objects.get(position).get("IDM_id").equals(previous_objects.get(x).get("IDM_id"))) {
                prev_call = Integer.parseInt(previous_objects.get(x).get("calls"));
                prev_total = Integer.parseInt(previous_objects.get(x).get("total"));
                cycle_before.setText(prev_call + "/" + prev_total);
                break;
            }
        }

        for (int x = 0; x < current_objects.size(); x++) {
            if (objects.get(position).get("IDM_id").equals(current_objects.get(x).get("IDM_id"))) {
                current_call = Integer.parseInt(current_objects.get(x).get("calls"));
                current_total = Integer.parseInt(current_objects.get(x).get("total"));
                cycle_now.setText(current_call + "/" + current_total);
                break;
            }
        }

        int total_total = prev_total + current_total;

        if (total_total > 0) {
            float percentage = (prev_call + current_call) * 100f / total_total;
            String callRate = new BigDecimal(percentage).setScale(2, RoundingMode.HALF_UP) + "%";
            average.setText(callRate);
        }

        return v;
    }
}