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
 * Created by vbfc_bry07 on 08/03/2016.
 */
public class BirthdayListAdapter extends ArrayAdapter {

    LayoutInflater inflater;
    TextView TxtDoctorName, TxtDoctorBirthDate;

    ArrayList<HashMap<String, String>> objects;

    public BirthdayListAdapter(Context context, int resource, ArrayList<HashMap<String, String>> objects) {
        super(context, resource, objects);
        inflater = LayoutInflater.from(context);

        this.objects = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // return super.getView(position, convertView, parent);
        convertView = inflater.inflate(R.layout.adapter_birthdays, parent, false);

        TxtDoctorName = (TextView) convertView.findViewById(R.id.TxtDoctorName);

        TxtDoctorName.setText(objects.get(position).get(DoctorsController.DOCTORS_DOC_NAME));

        return convertView;
    }
}
