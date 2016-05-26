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

public class CallMaterialsFragmentAdapter extends BaseAdapter {
    Context context;

    ArrayList<HashMap<String, String>> objects;

    public CallMaterialsFragmentAdapter(Context context, ArrayList<HashMap<String, String>> objects) {
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

    static class ViewHolder {
        TextView name, sample, literature, promaterials;
        int ref;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        final ViewHolder holder;

        if (convertView == null) {
            LayoutInflater vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(R.layout.adapter_call_materials_fragment, parent, false);

            holder = new ViewHolder();

            holder.name = (TextView) v.findViewById(R.id.name);
            holder.sample = (TextView) v.findViewById(R.id.sample);
            holder.literature = (TextView) v.findViewById(R.id.literature);
            holder.promaterials = (TextView) v.findViewById(R.id.promaterials);
        } else
            holder = (ViewHolder) v.getTag();

        holder.name.setText(objects.get(position).get("product_name"));
        holder.literature.setText(objects.get(position).get("literature"));
        holder.promaterials.setText(objects.get(position).get("promaterials"));
        holder.sample.setText(objects.get(position).get("sample"));

        return v;
    }
}
