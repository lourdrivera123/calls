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

public class BirthdayAdapter extends BaseAdapter {
    Context context;
    ArrayList<HashMap<String, String>> objects;

    TextView doctor_name, institution, birthday;

    public BirthdayAdapter(Context context, ArrayList<HashMap<String, String>> objects) {
        this.context = context;
        this.objects = objects;
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
            v = vi.inflate(R.layout.adapter_birthday, parent, false);
        }

        doctor_name = (TextView) v.findViewById(R.id.doctor_name);
        institution = (TextView) v.findViewById(R.id.institution);
        birthday = (TextView) v.findViewById(R.id.birthday);

        doctor_name.setText(objects.get(position).get("doctor_name"));
        institution.setText(objects.get(position).get("institution"));
        birthday.setText("Birthday: " + objects.get(position).get("birthday"));

        return v;
    }
}
