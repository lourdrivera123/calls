package com.ece.vbfc_bry07.calls.bdm_activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ListView;

import com.ece.vbfc_bry07.calls.R;
import com.ece.vbfc_bry07.calls.bdm_adapter.MCPApprovalAdapter;

public class BDMMCPApprovalActivity extends AppCompatActivity {
    Toolbar toolbar;
    ListView list_of_mcp_approval;

    MCPApprovalAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mcp_approval_bdm);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        list_of_mcp_approval = (ListView) findViewById(R.id.list_of_mcp_approval);

        setSupportActionBar(toolbar);
        assert getSupportActionBar() != null;
        getSupportActionBar().setTitle("MCP Approval");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        adapter = new MCPApprovalAdapter(this);

        list_of_mcp_approval.setAdapter(adapter);
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
