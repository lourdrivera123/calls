package com.ece.vbfc_bry07.calls.psr_activity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.ece.vbfc_bry07.calls.R;

import java.util.HashMap;
import java.util.List;

public class HospitalAdapter extends BaseExpandableListAdapter {
    private Context ctx;
    private HashMap<String, List<String>> Hospital_Category;
    private List<String> Hospital_List;

    public HospitalAdapter(Context ctx, HashMap<String, List<String>> Hospital_Category, List<String> Hospital_List) {
        this.ctx = ctx;
        this.Hospital_Category = Hospital_Category;
        this.Hospital_List = Hospital_List;
    }

    @Override
    public Object getChild(int parent, int child) {
        return Hospital_Category.get(Hospital_List.get(parent)).get(child);
    }

    @Override
    public long getChildId(int parent, int child) {
        return child;
    }

    @Override
    public View getChildView(int groupPosition, int child, boolean isLastChild, View convertView, ViewGroup parentview) {
        String child_title = (String) getChild(groupPosition, child);

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.child_hospital, parentview, false);
        }

        TextView child_textview = (TextView) convertView.findViewById(R.id.child_txt);
        child_textview.setText(child_title);

        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return Hospital_Category.get(Hospital_List.get(groupPosition)).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return Hospital_List.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return Hospital_List.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parentview) {
        String group_title = (String) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.parent_hospital, parentview, false);
        }
        TextView parent_text = (TextView) convertView.findViewById(R.id.parent_txt);
        parent_text.setText(group_title);
        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }
}
