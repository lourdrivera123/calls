package com.ece.vbfc_bry07.calls.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.ece.vbfc_bry07.calls.activity.ACPActivity;
import com.ece.vbfc_bry07.calls.adapter.CallsFragmentAdapter;
import com.ece.vbfc_bry07.calls.controller.PlanDetailsController;
import com.ece.vbfc_bry07.calls.R;

import java.util.ArrayList;
import java.util.HashMap;

public class NewCallsFragment extends Fragment {
    static ListView listview;

    static PlanDetailsController pdc;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.layout_listview_only, container, false);

        listview = (ListView) v.findViewById(R.id.listview);

        pdc = new PlanDetailsController(getActivity());

        return v;
    }

    public static void UpdateCallsTab(int IDM_id, int month) {
        ArrayList<HashMap<String, String>> objects = pdc.getMonthlyCallsByIDM_id(IDM_id, month);
        listview.setAdapter(new CallsFragmentAdapter(ACPActivity.acp, objects));
    }
}
