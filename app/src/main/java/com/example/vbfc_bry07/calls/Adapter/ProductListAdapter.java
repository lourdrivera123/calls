package com.example.vbfc_bry07.calls.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.vbfc_bry07.calls.R;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by vbfc_bry07 on 3/3/2016.
 */
public class ProductListAdapter extends ArrayAdapter {

    LayoutInflater inflater;
    TextView TxtProductName, TotalS, TotalL, TotalP, ContentS, ContentL, ContentP;

    ArrayList<HashMap<String, String>> objects;

    public ProductListAdapter(Context context, int resource, ArrayList<HashMap<String, String>> objects) {
        super(context, resource, objects);
        inflater = LayoutInflater.from(context);

        this.objects = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // return super.getView(position, convertView, parent);
        convertView = inflater.inflate(R.layout.adapter_material_monetoring, parent, false);

        TxtProductName = (TextView) convertView.findViewById(R.id.TxtProductName);
        TotalS = (TextView) convertView.findViewById(R.id.TotalS);
        TotalL = (TextView) convertView.findViewById(R.id.TotalL);
        TotalP = (TextView) convertView.findViewById(R.id.TotalP);
        ContentS = (TextView) convertView.findViewById(R.id.ContentS);
        ContentL = (TextView) convertView.findViewById(R.id.ContentL);
        ContentP = (TextView) convertView.findViewById(R.id.ContentP);


        TxtProductName.setText(objects.get(position).get("product_name"));
        TotalS.setText(objects.get(position).get("Total_Sample"));
        TotalL.setText(objects.get(position).get("Total_Literature"));
        TotalP.setText(objects.get(position).get("Total_Promomaterials"));
        ContentS.setText(objects.get(position).get("Content_Sample"));
        ContentL.setText(objects.get(position).get("Content_Literature"));
        ContentP.setText(objects.get(position).get("Content_Promomaterials"));

        return convertView;
    }

}
