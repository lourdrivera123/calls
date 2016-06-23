package com.ece.vbfc_bry07.calls.bdm_adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ece.vbfc_bry07.calls.R;

public class StatusSummaryAdapter extends BaseAdapter {
    Context context;

    TextView first_column, second_column, third_column;

    public StatusSummaryAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return 10;
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
            v = vi.inflate(R.layout.adapter_bdm_status_summary, parent, false);
        }

        first_column = (TextView) v.findViewById(R.id.first_column);
        second_column = (TextView) v.findViewById(R.id.second_column);
        third_column = (TextView) v.findViewById(R.id.third_column);

        return v;
    }
}
