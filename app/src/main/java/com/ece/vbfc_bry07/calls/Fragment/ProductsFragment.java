package com.ece.vbfc_bry07.calls.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.ece.vbfc_bry07.calls.psr_activity.ACPActivity;
import com.ece.vbfc_bry07.calls.psr_adapter.CallMaterialsFragmentAdapter;
import com.ece.vbfc_bry07.calls.psr_adapter.ProductsFragmentAdapter;
import com.ece.vbfc_bry07.calls.controller.CallMaterialsController;
import com.ece.vbfc_bry07.calls.controller.ProductsController;
import com.ece.vbfc_bry07.calls.R;

import java.util.ArrayList;
import java.util.HashMap;

public class ProductsFragment extends Fragment {
    static ListView list_of_products;

    public static ArrayList<HashMap<String, String>> array_of_products = new ArrayList<>(), call_materials;

    ProductsController pc;
    static CallMaterialsController cm;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_products, container, false);

        list_of_products = (ListView) v.findViewById(R.id.list_of_products);

        pc = new ProductsController(getActivity());
        cm = new CallMaterialsController(getActivity());

        if (array_of_products.size() == 0)
            array_of_products = pc.getAllProducts();

        call_materials = new ArrayList<>();

        list_of_products.setAdapter(new ProductsFragmentAdapter(getActivity(), array_of_products));

        return v;
    }

    public static ArrayList<HashMap<String, String>> getNotEmptyProducts() {
        ArrayList<HashMap<String, String>> array = new ArrayList<>();

        for (int x = 0; x < array_of_products.size(); x++) {
            String promats = array_of_products.get(x).get("promaterials");
            String literature = array_of_products.get(x).get("literature");
            String sample = array_of_products.get(x).get("sample");

            if (!promats.equals("") || !literature.equals("") || !sample.equals("")) {
                HashMap<String, String> map = array_of_products.get(x);
                array.add(map);
            }
        }

        return array;
    }

    public static void setCallMaterials(int IDM_id, String date) {
        call_materials = cm.getCallMaterialsByIDM_id(IDM_id, date);

        if (call_materials.size() > 0) {
            list_of_products.setAdapter(new CallMaterialsFragmentAdapter(ACPActivity.acp, call_materials));
        } else
            list_of_products.setAdapter(new ProductsFragmentAdapter(ACPActivity.acp, array_of_products));
    }
}
