package com.ece.vbfc_bry07.calls.bdm_adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ece.vbfc_bry07.calls.R;

public class MCPApprovalAdapter extends BaseAdapter {
    Context context;

    TextView cycle_number, name, target, md_count, status, message, date_updated;

    public MCPApprovalAdapter(Context context) {
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
            v = vi.inflate(R.layout.adapter_mcp_approval, parent, false);
        }

        name = (TextView) v.findViewById(R.id.name);
        target = (TextView) v.findViewById(R.id.target);
        status = (TextView) v.findViewById(R.id.status);
        message = (TextView) v.findViewById(R.id.message);
        md_count = (TextView) v.findViewById(R.id.md_count);
        cycle_number = (TextView) v.findViewById(R.id.cycle_number);
        date_updated = (TextView) v.findViewById(R.id.date_updated);

        return v;
    }
}
