package com.ece.vbfc_bry07.calls.Fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.ece.vbfc_bry07.calls.Activity.ACPActivity;
import com.ece.vbfc_bry07.calls.Adapter.CallsFragmentAdapter;
import com.ece.vbfc_bry07.calls.Controller.PlanDetailsController;
import com.ece.vbfc_bry07.calls.R;

import java.util.ArrayList;
import java.util.HashMap;

public class NewCallsFragment extends Fragment {
    static ListView listview_calls;

    static PlanDetailsController pdc;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_calls, container, false);

        listview_calls = (ListView) v.findViewById(R.id.listview_calls);

        pdc = new PlanDetailsController(getActivity());

        return v;
    }

    public static void UpdateCallsTab(int IDM_id, int month) {
        ArrayList<HashMap<String, String>> objects = pdc.getMonthlyCallsByIDM_id(IDM_id, month);
        listview_calls.setAdapter(new CallsFragmentAdapter(ACPActivity.acp, objects));
    }
}
