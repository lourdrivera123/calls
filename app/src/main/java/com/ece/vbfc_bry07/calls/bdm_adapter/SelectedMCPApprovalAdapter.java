package com.ece.vbfc_bry07.calls.bdm_adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ece.vbfc_bry07.calls.R;

public class SelectedMCPApprovalAdapter extends BaseAdapter {
    Context context;

    TextView date, md_name, specialization, clas_code, institution;

    public SelectedMCPApprovalAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return 15;
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
            v = vi.inflate(R.layout.adapter_selected_mcp_approval, parent, false);
        }

        date = (TextView) v.findViewById(R.id.date);
        md_name = (TextView) v.findViewById(R.id.md_name);
        specialization = (TextView) v.findViewById(R.id.specialization);
        clas_code = (TextView) v.findViewById(R.id.class_code);
        institution = (TextView) v.findViewById(R.id.institution);

        return v;
    }
}
