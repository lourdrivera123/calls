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
import com.ece.vbfc_bry07.calls.Controller.CallNotesController;
import com.ece.vbfc_bry07.calls.Controller.CallsController;
import com.ece.vbfc_bry07.calls.Controller.PlanDetailsController;
import com.ece.vbfc_bry07.calls.Controller.ReasonsController;
import com.ece.vbfc_bry07.calls.Controller.RescheduledCallsController;
import com.ece.vbfc_bry07.calls.Helpers;
import com.ece.vbfc_bry07.calls.R;
import com.ece.vbfc_bry07.calls.Signature;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class SignatureFormActivity extends AppCompatActivity {
    Toolbar toolbar;
    ListView list_of_products;
    LinearLayout signature_layout;
    RelativeLayout root;
    TextView timestamp_now, doctor_name, last_visited;

    Helpers helpers;
    CallsController cc;
    Signature signature;
    ReasonsController rc;
    CallNotesController cnc;
    PlanDetailsController pdc;
    CallMaterialsController cmc;
    SignatureFormAdapter adapter;
    RescheduledCallsController rcc;

    int retry_count;

    View mView;

    ArrayList<HashMap<String, String>> products, notes;
    HashMap<String, String> details, additional_call;

    @SuppressWarnings("unchecked")
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

        helpers = new Helpers();
        cc = new CallsController(this);
        rc = new ReasonsController(this);
        cnc = new CallNotesController(this);
        pdc = new PlanDetailsController(this);
        cmc = new CallMaterialsController(this);
        rcc = new RescheduledCallsController(this);

        Intent intent = getIntent();
        details = (HashMap<String, String>) intent.getSerializableExtra("call_details");
        products = (ArrayList<HashMap<String, String>>) intent.getSerializableExtra("call_products");
        additional_call = (HashMap<String, String>) intent.getSerializableExtra("calls_additional_call");
        notes = (ArrayList<HashMap<String, String>>) intent.getSerializableExtra("call_notes");

        details.putAll(pdc.getCallDetails(Integer.parseInt(details.get("calls_IDM_id"))));

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
                    mView.setDrawingCacheEnabled(true);
                    signature.save(mView);

                    details.put("calls_retry_count", String.valueOf(retry_count));
                    details.put("calls_status", "1");

                    if (additional_call != null && additional_call.size() > 0) {
                        details.put("calls_status", "2"); //INCIDENTAL CALLS
                        details.put("calls_makeup", "0");
                        details.put("calls_temp_planDetails_id", String.valueOf(pdc.insertAdditionalCall(additional_call)));
                    } else if (!ACPActivity.missed_call_date.equals("")) {
                        Date makeup_date = helpers.convertStringToDate(ACPActivity.missed_call_date);
                        Date current_date = helpers.convertStringToDate(helpers.getCurrentDate("date"));
                        details.put("calls_status", "2");

                        if (current_date.after(makeup_date))
                            details.put("calls_makeup", "1"); //MAKE UP CALLS
                        else if (current_date.before(makeup_date))
                            details.put("calls_makeup", "0"); //ADVANCE CALLS
                    }

                    long callID = cc.insertCall(details);

                    if (callID > 0) {
                        if (products.size() > 0) {
                            if (!cmc.insertCallMaterials(products, callID))
                                Snackbar.make(root, "Error occurred while saving materials", Snackbar.LENGTH_SHORT).show();
                        }

                        if (notes.size() > 0) {
                            if (!cnc.insertCallNotes(notes, callID))
                                Snackbar.make(root, "Error occurred while saving notes", Snackbar.LENGTH_SHORT).show();
                        }

                        if (details.get("calls_status").equals("2") && !ACPActivity.missed_call_date.equals("")) {
                            int reason_id = 0;

                            if (!ACPActivity.selected_reason.equals(""))
                                reason_id = rc.getReasonID(ACPActivity.selected_reason);

                            HashMap<String, String> map = new HashMap<>();
                            map.put("call_id", String.valueOf(callID));
                            map.put("reschedule_date", helpers.getCurrentDate("date"));
                            map.put("reason_id", String.valueOf(reason_id));
                            map.put("remarks", "");

                            if (!rcc.insertRescheduledCalls(map))
                                Snackbar.make(root, "Error saving call", Snackbar.LENGTH_SHORT).show();
                        }

                        ACPActivity.acp.finish();
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