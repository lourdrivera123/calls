package com.example.vbfc_bry07.calls.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.vbfc_bry07.calls.R;

/**
 * Created by vbfc_bry07 on 2/29/2016.
 */
public class DoctorListAdapter extends ArrayAdapter<String> {
    public DoctorListAdapter(Context context, String[] doctors) {
        super(context, R.layout.adapter_doctors_list, doctors);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View customView = inflater.inflate(R.layout.adapter_doctors_list, parent, false);

        String singleDoctorItem = getItem(position);
        TextView doctor = (TextView) customView.findViewById(R.id.doctor);

        doctor.setText(singleDoctorItem);

        return customView;
    }
}
