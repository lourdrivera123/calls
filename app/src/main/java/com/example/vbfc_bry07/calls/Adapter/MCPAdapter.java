package com.example.vbfc_bry07.calls.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.vbfc_bry07.calls.R;

public class MCPAdapter extends BaseAdapter {
    TextView doctor_name, doctor_hospital, doctor_number, doctor_class;
    Context context;

    public MCPAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return 0;
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

        if (convertView == null) {
            LayoutInflater vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(R.layout.item_mcp, parent, false);
        }

        doctor_name = (TextView) v.findViewById(R.id.doctor_name);
        doctor_hospital = (TextView) v.findViewById(R.id.doctor_hospital);
        doctor_number = (TextView) v.findViewById(R.id.doctor_number);
        doctor_class = (TextView) v.findViewById(R.id.doctor_class);

        return v;
    }
}
