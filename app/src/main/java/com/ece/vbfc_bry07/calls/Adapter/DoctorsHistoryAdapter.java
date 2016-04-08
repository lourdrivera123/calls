package com.ece.vbfc_bry07.calls.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ece.vbfc_bry07.calls.R;

import java.util.ArrayList;
import java.util.HashMap;

public class DoctorsHistoryAdapter extends BaseAdapter {
    ArrayList<HashMap<String, String>> objects;
    Context context;

    TextView date;
    ImageView image;

    //    public DoctorsHistoryAdapter(Context context, ArrayList<HashMap<String, String>> objects) {
//        this.objects = objects;
//        this.context = context;
//    }

    public DoctorsHistoryAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
//        return objects.size();
        return 3;
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
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.item_doctor_history, parent, false);
        }

        date = (TextView) convertView.findViewById(R.id.date);
        image = (ImageView) convertView.findViewById(R.id.image);

        return convertView;
    }
}
