package com.ece.vbfc_bry07.calls.Activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.ece.vbfc_bry07.calls.R;
import com.ece.vbfc_bry07.calls.Signature;

public class QuickSignActivity extends AppCompatActivity {
    Toolbar toolbar;
    LinearLayout hide_layout, signature_layout;

    Signature signature;

    View mView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signature_form);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        hide_layout = (LinearLayout) findViewById(R.id.hide_layout);
        signature_layout = (LinearLayout) findViewById(R.id.signature_layout);

        hide_layout.setVisibility(View.GONE);

        setSupportActionBar(toolbar);
        assert getSupportActionBar() != null;
        getSupportActionBar().setTitle("Quick Sign");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        signature = new Signature(this, null, signature_layout);
        signature.setBackgroundColor(Color.WHITE);
        signature_layout.addView(signature, ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT);
        mView = signature_layout;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.save_clear_cancel_menu, menu);

        MenuItem item = menu.findItem(R.id.cancel);
        item.setVisible(false);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                break;

            case R.id.clear:
                signature.clear();
                break;

            case R.id.save:

                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
