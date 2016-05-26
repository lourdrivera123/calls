package com.ece.vbfc_bry07.calls.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ece.vbfc_bry07.calls.R;

import java.util.ArrayList;
import java.util.HashMap;

public class MCPAdapter extends BaseAdapter {
    TextView doctor_name, doctor_hospital, doctor_number, doctor_class;
    ArrayList<HashMap<String, String>> objects;
    Context context;

    public MCPAdapter(Context context, ArrayList<HashMap<String, String>> objects) {
        this.context = context;
        this.objects = objects;
    }

    public void remove(int position) {
        objects.remove(position);
        this.notifyDataSetChanged();
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

        if (convertView == null) {
            LayoutInflater vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(R.layout.item_mcp, parent, false);
        }

        doctor_name = (TextView) v.findViewById(R.id.doctor_name);
        doctor_hospital = (TextView) v.findViewById(R.id.doctor_hospital);
        doctor_number = (TextView) v.findViewById(R.id.doctor_number);
        doctor_class = (TextView) v.findViewById(R.id.doctor_class);

        String name = (1 + position) + ". " + objects.get(position).get("doc_name");

        if (objects.get(position).get("contact_number").equals(""))
            doctor_number.setVisibility(View.GONE);
        else
            doctor_number.setText(objects.get(position).get("contact_number"));

        String class_code = objects.get(position).get("class_name") + " (" + objects.get(position).get("class_code") + "x)";

        doctor_name.setText(name);
        doctor_hospital.setText(objects.get(position).get("inst_name"));
        doctor_class.setText(class_code);
        return v;
    }
}
