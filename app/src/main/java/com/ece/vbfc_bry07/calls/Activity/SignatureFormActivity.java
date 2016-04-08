package com.ece.vbfc_bry07.calls.Activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ece.vbfc_bry07.calls.Adapter.SignatureFormAdapter;
import com.ece.vbfc_bry07.calls.Controller.CallMaterialsController;
import com.ece.vbfc_bry07.calls.Controller.CallsController;
import com.ece.vbfc_bry07.calls.Controller.PlanDetailsController;
import com.ece.vbfc_bry07.calls.Helpers;
import com.ece.vbfc_bry07.calls.R;
import com.ece.vbfc_bry07.calls.Signature;

import java.util.ArrayList;
import java.util.HashMap;

public class SignatureFormActivity extends AppCompatActivity {
    Toolbar toolbar;
    ListView list_of_products;
    LinearLayout signature_layout;
    RelativeLayout root;
    TextView timestamp_now, doctor_name, last_visited;

    PlanDetailsController pdc;
    SignatureFormAdapter adapter;
    CallsController cc;
    CallMaterialsController cmc;
    Helpers helpers;
    Signature signature;

    int status_id = 0, retry_count;

    View mView;

    ArrayList<HashMap<String, String>> products;
    HashMap<String, String> details;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signature_form);

        toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setTitle("");

        timestamp_now = (TextView) findViewById(R.id.timestamp_now);
        doctor_name = (TextView) findViewById(R.id.doctor_name);
        last_visited = (TextView) findViewById(R.id.last_visited);
        list_of_products = (ListView) findViewById(R.id.list_of_products);
        signature_layout = (LinearLayout) findViewById(R.id.signature_layout);
        root = (RelativeLayout) findViewById(R.id.root);

        pdc = new PlanDetailsController(this);
        cc = new CallsController(this);
        cmc = new CallMaterialsController(this);
        helpers = new Helpers();

        Intent intent = getIntent();
        details = (HashMap<String, String>) intent.getSerializableExtra("call_details");
        products = (ArrayList<HashMap<String, String>>) intent.getSerializableExtra("call_products");

        if (Integer.parseInt(details.get("calls_plan_details_id")) == 0)
            status_id = 1;
        else
            status_id = 2;

        details.put("status_id", String.valueOf(status_id));
        details.putAll(pdc.getCallDetails(Integer.parseInt(details.get("calls_plan_details_id")), Integer.parseInt(details.get("calls_incidentals_pd_id"))));

        adapter = new SignatureFormAdapter(this, products);
        list_of_products.setAdapter(adapter);

        signature = new Signature(this, null, signature_layout);
        signature.setBackgroundColor(Color.WHITE);
        signature_layout.addView(signature, ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT);
        mView = signature_layout;

        if (!details.get("calls_last_visited").equals(""))
            last_visited.setText("Last visited: " + details.get("calls_last_visited"));
        else
            last_visited.setVisibility(View.GONE);

        int count_visits = details.get("calls_count_visits").equals("") ? 1 : Integer.parseInt(details.get("calls_count_visits")) + 1;
        doctor_name.setText(details.get("doc_name") + " (" + count_visits + " / " + details.get("max_visit") + ")");
        timestamp_now.setText(details.get("calls_end"));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.save_clear_cancel_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save:
                if (signature.hasSigned()) {
                    ACPActivity.acp.finish();
                    mView.setDrawingCacheEnabled(true);
                    signature.save(mView);

                    details.put("calls_retry_count", String.valueOf(retry_count));

                    long callID = cc.insertCall(details);

                    if (callID > 0) {
                        if (products.size() > 0) {
                            if (!cmc.insertCallMaterials(products, callID))
                                Snackbar.make(root, "Error saving materials", Snackbar.LENGTH_SHORT).show();
                        }

                        startActivity(new Intent(this, ACPActivity.class));
                        finish();
                    } else
                        Snackbar.make(root, "Error occurred", Snackbar.LENGTH_SHORT).show();
                } else
                    Snackbar.make(root, "Signature is required", Snackbar.LENGTH_SHORT).show();
                break;

            case R.id.clear:
                signature.clear();
                retry_count += 1;
                break;

            case R.id.cancel:
                this.finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}