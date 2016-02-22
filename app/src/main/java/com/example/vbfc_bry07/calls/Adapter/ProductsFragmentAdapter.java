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

public class ProductsFragmentAdapter extends BaseAdapter {
    Context context;
    ArrayList<String> objects;

    TextView product_name;

    public ProductsFragmentAdapter(Context context, ArrayList<String> array) {
        this.context = context;
        this.objects = array;
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
        convertView = LayoutInflater.from(context).inflate(R.layout.adapter_products_fragment, parent, false);

        product_name = (TextView) convertView.findViewById(R.id.product_name);

        product_name.setText(objects.get(position));

        return convertView;
    }
}
