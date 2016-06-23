package com.ece.vbfc_bry07.calls.psr_adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ece.vbfc_bry07.calls.Helpers;
import com.ece.vbfc_bry07.calls.R;

import java.util.ArrayList;
import java.util.HashMap;

public class BroadcastMessagesAdapter extends BaseAdapter {
    Context context;
    ArrayList<HashMap<String, String>> objects;

    TextView message, month, date;

    Helpers helpers;

    public BroadcastMessagesAdapter(Context context, ArrayList<HashMap<String, String>> objects) {
        this.context = context;
        this.objects = objects;

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
            v = vi.inflate(R.layout.item_broadcast, parent, false);
        }

        message = (TextView) v.findViewById(R.id.message);
        month = (TextView) v.findViewById(R.id.month);
        date = (TextView) v.findViewById(R.id.date);

        message.setText(objects.get(position).get("message"));
        month.setText(helpers.divideDate(objects.get(position).get("date")).get("month"));
        date.setText(helpers.divideDate(objects.get(position).get("date")).get("date"));

        return v;
    }
}
