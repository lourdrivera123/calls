package com.example.vbfc_bry07.calls.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.vbfc_bry07.calls.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class SignatureFormAdapter extends BaseAdapter {
    TextView product_name;

    Context context;
    ArrayList<HashMap<String, String>> objects;

    public SignatureFormAdapter(Context context, ArrayList<HashMap<String, String>> objects) {
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
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.item_products, parent, false);
        }

        product_name = (TextView) convertView.findViewById(R.id.product_name);
        String count = "";
        String sample = objects.get(position).get("sample");
        String literature = objects.get(position).get("literature");
        String promats = objects.get(position).get("promaterials");

        if (!sample.equals(""))
            count = count + "Sample : " + sample + ", ";

        if (!literature.equals(""))
            count = count + "Literature : " + literature + ", ";

        if (!promats.equals(""))
            count = count + "Promaterials : " + promats;

        count = count.replaceAll(", $", "");
        String content = objects.get(position).get("product_name") + " [" + count + "]";
        product_name.setText(content);

        return convertView;
    }
}
