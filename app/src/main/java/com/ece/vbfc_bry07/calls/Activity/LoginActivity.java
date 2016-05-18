package com.ece.vbfc_bry07.calls.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.ece.vbfc_bry07.calls.Controller.DbHelper;
import com.ece.vbfc_bry07.calls.Controller.PreferencesController;
import com.ece.vbfc_bry07.calls.R;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    Button BtnLogin;
    LinearLayout root;
    EditText username, password;

    SharedPreferences shared;

    DbHelper db;
    PreferencesController PC;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);

        BtnLogin = (Button) findViewById(R.id.BtnLogin);
        root = (LinearLayout) findViewById(R.id.root);
        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);

        db = new DbHelper(this);
        PC = new PreferencesController(this);
        shared = getSharedPreferences("ECECalls", Context.MODE_PRIVATE);

        BtnLogin.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        if (shared.contains("Username")) {
            startActivity(new Intent(this, HomeActivity.class));
            this.finish();
        }

        super.onResume();
    }

    @Override
    public void onClick(View v) {
        String get_username, get_password;
        int success = 2;

        get_username = username.getText().toString();
        get_password = password.getText().toString();

        if (get_username.isEmpty()) {
            success -= 1;
            username.setError("Field required");
        }

        if (get_password.isEmpty()) {
            success -= 1;
            password.setError("Field required");
        }

        if (success == 2) {
            if (PC.checkUser(get_username, get_password) > 1) {
                SharedPreferences.Editor editor = shared.edit();
                editor.putString("Username", get_username);
                editor.apply();
                startActivity(new Intent(this, HomeActivity.class));
                this.finish();
            } else
                Snackbar.make(root, "Invalid Login Credentials", Snackbar.LENGTH_SHORT).show();
        }
    }
}
