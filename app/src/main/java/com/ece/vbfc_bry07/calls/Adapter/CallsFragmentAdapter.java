package com.ece.vbfc_bry07.calls.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ece.vbfc_bry07.calls.Helpers;
import com.ece.vbfc_bry07.calls.R;

import java.util.ArrayList;
import java.util.HashMap;

public class CallsFragmentAdapter extends BaseAdapter {
    Context context;
    ArrayList<HashMap<String, String>> objects;

    TextView date;
    ImageView image;

    Helpers helpers;

    public CallsFragmentAdapter(Context context, ArrayList<HashMap<String, String>> objects) {
        this.objects = objects;
        this.context = context;
        helpers = new Helpers();
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
        View v = convertView;

        if (convertView == null) {
            LayoutInflater vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(R.layout.adapter_calls_fragment, parent, false);
        }

        date = (TextView) v.findViewById(R.id.date);
        image = (ImageView) v.findViewById(R.id.image);

        date.setText(helpers.convertToAlphabetDate(objects.get(position).get("date"), ""));

        if (!objects.get(position).get("server_id").equals(""))
            image.setImageResource(R.mipmap.ic_actual_covered_call);
        else if (objects.get(position).get("status").equals("1"))
            image.setImageResource(R.mipmap.ic_signed_call);
        else if (objects.get(position).get("status").equals("2"))
            image.setImageResource(R.mipmap.ic_recovered_call);
        else if (objects.get(position).get("status").equals("3"))
            image.setImageResource(R.mipmap.ic_incidental_call);
        else if (objects.get(position).get("status").equals(""))
            image.setImageResource(R.mipmap.ic_missed_call);

        return v;
    }
}
