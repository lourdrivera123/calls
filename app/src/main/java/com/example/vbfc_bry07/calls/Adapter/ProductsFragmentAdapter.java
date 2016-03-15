package com.example.vbfc_bry07.calls.Adapter;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.TextView;

import com.example.vbfc_bry07.calls.R;

import java.util.ArrayList;
import java.util.HashMap;

public class ProductsFragmentAdapter extends BaseAdapter {
    Context context;
    ArrayList<HashMap<String, String>> objects;

    public ProductsFragmentAdapter(Context context, ArrayList<HashMap<String, String>> array) {
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

    static class ViewHolder {
        EditText sample, literature, promaterials;
        TextView product_name;
        int ref;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.adapter_products_fragment, parent, false);

            holder = new ViewHolder();

            holder.product_name = (TextView) convertView.findViewById(R.id.product_name);
            holder.sample = (EditText) convertView.findViewById(R.id.sample);
            holder.literature = (EditText) convertView.findViewById(R.id.literature);
            holder.promaterials = (EditText) convertView.findViewById(R.id.promaterials);

            convertView.setTag(holder);
        } else
            holder = (ViewHolder) convertView.getTag();

        holder.ref = position;

        holder.product_name.setText(objects.get(position).get("product_name"));

        holder.sample.setText(objects.get(position).get("sample"));
        holder.sample.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                HashMap<String, String> map = new HashMap<>();
                map.putAll(objects.get(holder.ref));
                map.put("sample", s.toString());

                objects.set(holder.ref, map);
            }
        });

        holder.literature.setText(objects.get(position).get("literature"));
        holder.literature.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                HashMap<String, String> map = new HashMap<>();
                map.putAll(objects.get(holder.ref));
                map.put("literature", s.toString());

                objects.set(holder.ref, map);
            }
        });

        holder.promaterials.setText(objects.get(position).get("promaterials"));
        holder.promaterials.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                HashMap<String, String> map = new HashMap<>();
                map.putAll(objects.get(holder.ref));
                map.put("promaterials", s.toString());

                objects.set(holder.ref, map);
            }
        });

        return convertView;
    }
}