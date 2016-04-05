package com.ece.vbfc_bry07.calls.Fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.ece.vbfc_bry07.calls.Adapter.ProductsFragmentAdapter;
import com.ece.vbfc_bry07.calls.Controller.ProductsController;
import com.ece.vbfc_bry07.calls.R;

import java.util.ArrayList;
import java.util.HashMap;

public class ProductsFragment extends Fragment {
    ListView list_of_products;

    public static ArrayList<HashMap<String, String>> array_of_products;

    ProductsController pc;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_products, container, false);

        list_of_products = (ListView) v.findViewById(R.id.list_of_products);

        pc = new ProductsController(getActivity());
        array_of_products = pc.getAllProducts();

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
}
