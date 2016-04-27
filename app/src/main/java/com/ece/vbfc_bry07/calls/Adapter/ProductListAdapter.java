package com.ece.vbfc_bry07.calls.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ece.vbfc_bry07.calls.R;

import java.util.ArrayList;
import java.util.HashMap;

public class ProductListAdapter extends BaseAdapter {
    TextView TxtProductName, TotalS, TotalL, TotalP, ContentS, ContentL, ContentP;

    ArrayList<HashMap<String, String>> objects, call_materials;
    Context context;

    public ProductListAdapter(Context context, ArrayList<HashMap<String, String>> objects, ArrayList<HashMap<String, String>> call_materials) {
        this.objects = objects;
        this.context = context;
        this.call_materials = call_materials;
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
            convertView = inflater.inflate(R.layout.adapter_material_monetoring, parent, false);
        }

        TxtProductName = (TextView) convertView.findViewById(R.id.TxtProductName);
        TotalS = (TextView) convertView.findViewById(R.id.TotalS);
        TotalL = (TextView) convertView.findViewById(R.id.TotalL);
        TotalP = (TextView) convertView.findViewById(R.id.TotalP);
        ContentS = (TextView) convertView.findViewById(R.id.ContentS);
        ContentL = (TextView) convertView.findViewById(R.id.ContentL);
        ContentP = (TextView) convertView.findViewById(R.id.ContentP);

        TxtProductName.setText(objects.get(position).get("product_name"));

        for (int x = 0; x < call_materials.size(); x++) {
            if (call_materials.get(x).get("product_id").equals(objects.get(position).get("product_id"))) {
                TotalS.setText(call_materials.get(x).get("sample"));
                TotalL.setText(call_materials.get(x).get("literature"));
                TotalP.setText(call_materials.get(x).get("promaterials"));
                break;
            }
        }

//        ContentS.setText(objects.get(position).get("Content_Sample"));
//        ContentL.setText(objects.get(position).get("Content_Literature"));
//        ContentP.setText(objects.get(position).get("Content_Promomaterials"));

        return convertView;
    }

}
