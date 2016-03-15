package com.example.vbfc_bry07.calls.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.widget.TextView;

import com.example.vbfc_bry07.calls.R;

import java.util.HashMap;

public class SignatureFormActivity extends AppCompatActivity {
    Toolbar toolbar;
    TextView timestamp_now;

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

        Intent intent = getIntent();
        HashMap<String, String> details = (HashMap<String, String>) intent.getSerializableExtra("call_details");

        Log.d("details", details + "");

        timestamp_now.setText(details.get("calls_end"));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.save_clear_cancel_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
    }
}
