package com.ece.vbfc_bry07.calls.bdm_activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.ece.vbfc_bry07.calls.R;
import com.ece.vbfc_bry07.calls.bdm_adapter.MCPApprovalAdapter;

public class BDMMCPApprovalActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
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
        list_of_mcp_approval.setOnItemClickListener(this);
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

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(this, MCPApprovalSelectedActivity.class);
        startActivity(intent);
    }
}
