package com.example.vbfc_bry07.calls.Activity;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.vbfc_bry07.calls.Adapter.ACPTabsAdapter;
import com.example.vbfc_bry07.calls.Fragment.CallsFragment;
import com.example.vbfc_bry07.calls.Fragment.NotesFragment;
import com.example.vbfc_bry07.calls.Fragment.ProductsFragment;
import com.example.vbfc_bry07.calls.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ACPActivity extends AppCompatActivity implements TabLayout.OnTabSelectedListener, View.OnClickListener {
    TextView LblDate;
    ImageView view_acp;
    TabLayout tab_layout;
    ViewPager pager;

    HashMap<String, List<String>> Hospital_Category;
    List<String> Hospital_List;
    ExpandableListView HospitalListView;
    HospitalAdapter adapter;
    ACPTabsAdapter fragment_adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acp);

        LblDate = (TextView) findViewById(R.id.LblDate);
        view_acp = (ImageView) findViewById(R.id.view_acp);
        HospitalListView = (ExpandableListView) findViewById(R.id.HospitalListView);
        tab_layout = (TabLayout) findViewById(R.id.tab_layout);
        pager = (ViewPager) findViewById(R.id.pager);

        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Actual Coverage Plan");

        setupViewPager(pager);
        tab_layout.setupWithViewPager(pager);
        tab_layout.setOnTabSelectedListener(this);

        Hospital_Category = DataHospital.getInfo(); // Get data from DataHospital class
        Hospital_List = new ArrayList<>(Hospital_Category.keySet());
        adapter = new HospitalAdapter(this, Hospital_Category, Hospital_List);
        HospitalListView.setAdapter(adapter);

        ShowFragment(0);
        android.app.FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().commit();

        view_acp.setOnClickListener(this);
    }

    void ShowFragment(int position) {
        Fragment fragment = null;

        switch (position) {
            case 0:
                fragment = new ProductsFragment();
                break;

            case 1:
                Toast.makeText(this, "1", Toast.LENGTH_SHORT).show();
                break;

            case 2:
                Toast.makeText(this, "2", Toast.LENGTH_SHORT).show();
                break;
        }

        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.pager, fragment).commit();
            fragment_adapter.notifyDataSetChanged();
        }
    }

    private void setupViewPager(ViewPager viewPager) {
        fragment_adapter = new ACPTabsAdapter(getSupportFragmentManager());
        fragment_adapter.addFragment(new ProductsFragment(), "Products");
        fragment_adapter.addFragment(new NotesFragment(), "Notes");
        fragment_adapter.addFragment(new CallsFragment(), "Calls");
        viewPager.setAdapter(fragment_adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.start_call_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                break;

            case R.id.end_call:
                startActivity(new Intent(this, SignatureFormActivity.class));
                this.finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        pager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {
    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.view_acp:

                break;
        }
    }
}
