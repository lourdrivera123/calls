package com.ece.vbfc_bry07.calls.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.ece.vbfc_bry07.calls.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ACPListAdapter extends BaseExpandableListAdapter {
    Context context;
    List<String> listDataHeader;
    HashMap<Integer, ArrayList<HashMap<String, String>>> listDataChild;

    public ACPListAdapter(Context context, List<String> listDataHeader, HashMap<Integer, ArrayList<HashMap<String, String>>> listChildData) {
        this.context = context;
        this.listDataHeader = listDataHeader;
        this.listDataChild = listChildData;
    }

    @Override
    public int getGroupCount() {
        return listDataHeader.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return listDataChild.get(groupPosition) != null ? listDataChild.get(groupPosition).size() : 0;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return listDataHeader.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosititon) {
        HashMap<String, String> map = listDataChild.get(groupPosition).get(childPosititon);
        return map.get("doc_name");
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parentview) {
        String group_title = (String) getGroup(groupPosition);

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.parent_hospital, parentview, false);
        }

        TextView parent_text = (TextView) convertView.findViewById(R.id.parent_txt);
        parent_text.setText(group_title);

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int child, boolean isLastChild, View convertView, ViewGroup parentview) {
        String child_title = (String) getChild(groupPosition, child);

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.child_hospital, parentview, false);
        }

        TextView child_textview = (TextView) convertView.findViewById(R.id.child_txt);
        child_textview.setText(child_title);

        return convertView;
    }
}
