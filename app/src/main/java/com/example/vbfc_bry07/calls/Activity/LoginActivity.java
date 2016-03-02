package com.example.vbfc_bry07.calls.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.example.vbfc_bry07.calls.Controller.DbHelper;
import com.example.vbfc_bry07.calls.Controller.PreferencesController;
import com.example.vbfc_bry07.calls.R;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    Button BtnLogin;
    LinearLayout root;
    EditText username_txtfield, password_txtfield;

    SharedPreferences shared;

    DbHelper db;
    PreferencesController PC;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        BtnLogin = (Button) findViewById(R.id.BtnLogin);
        root = (LinearLayout) findViewById(R.id.root);
        username_txtfield = (EditText) findViewById(R.id.username_txtfield);
        password_txtfield = (EditText) findViewById(R.id.password_txtfield);

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
        String username, password;
        int success = 2;

        username = username_txtfield.getText().toString();
        password = password_txtfield.getText().toString();

        if (username.equals("")) {
            success -= 1;
            username_txtfield.setError("Field required");
        }

        if (password.equals("")) {
            success -= 1;
            password_txtfield.setError("Fields required");
        }

        if (success == 2) {
            if (PC.checkUser(username, password) > 1) {
                SharedPreferences.Editor editor = shared.edit();
                editor.putString("Username", username);
                editor.apply();
                startActivity(new Intent(this, HomeActivity.class));
                this.finish();
            } else
                Snackbar.make(root, "Invalid Login Credentials", Snackbar.LENGTH_SHORT).show();
        }
    }
}
