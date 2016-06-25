package com.ece.vbfc_bry07.calls.bdm_activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;

import com.ece.vbfc_bry07.calls.R;
import com.ece.vbfc_bry07.calls.bdm_adapter.SelectedMCPApprovalAdapter;

public class MCPApprovalSelectedActivity extends AppCompatActivity {
    Toolbar toolbar;
    TextView name;
    ListView list_of_plan_details;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mcp_approval_selected);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        name = (TextView) findViewById(R.id.name);
        list_of_plan_details = (ListView) findViewById(R.id.list_of_plan_details);

        setSupportActionBar(toolbar);
        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("MCP Approval");

        list_of_plan_details.setAdapter(new SelectedMCPApprovalAdapter(this));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}
