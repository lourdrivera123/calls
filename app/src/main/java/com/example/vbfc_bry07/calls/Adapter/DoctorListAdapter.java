package com.example.vbfc_bry07.calls.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.vbfc_bry07.calls.Controller.DoctorsController;
import com.example.vbfc_bry07.calls.R;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by vbfc_bry07 on 2/29/2016.
 */
public class DoctorListAdapter extends ArrayAdapter {

    LayoutInflater inflater;
    TextView doctor_name;

    ArrayList<HashMap<String, String>> objects;

    public DoctorListAdapter(Context context, int resource, ArrayList<HashMap<String, String>> objects) {
        super(context, resource, objects);
        inflater = LayoutInflater.from(context);

        this.objects = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // return super.getView(position, convertView, parent);
        convertView = inflater.inflate(R.layout.adapter_doctors_list, parent, false);

        doctor_name = (TextView) convertView.findViewById(R.id.doctor_name);


        doctor_name.setText(objects.get(position).get(DoctorsController.DOCTORS_DOC_NAME));

        return convertView;
    }

}
