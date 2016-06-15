package com.ece.vbfc_bry07.calls.activity;

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

import com.ece.vbfc_bry07.calls.controller.DbHelper;
import com.ece.vbfc_bry07.calls.controller.PreferencesController;
import com.ece.vbfc_bry07.calls.R;

import java.io.IOException;

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

        try {
            db.createDataBase();
            db.openDataBase();
        } catch (IOException ioe) {
            throw new Error(ioe + "");
        }
    }

    @Override
    protected void onResume() {
        if (shared.contains("Username")) {
            if (shared.getInt("User_type", 0) == 1)
                startActivity(new Intent(this, PSRHomeActivity.class));
            else if (shared.getInt("User_type", 0) == 2)
                startActivity(new Intent(this, BDMHomeActivity.class));
            
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
            int user_type = PC.checkUserType(get_username, get_password);

            if (user_type > 0) {
                SharedPreferences.Editor editor = shared.edit();
                editor.putString("Username", get_username);
                Intent intent = null;

                if (user_type == 1) {
                    editor.putInt("User_type", 1);
                    intent = new Intent(this, PSRHomeActivity.class);
                } else if (user_type == 2) {
                    editor.putInt("User_type", 2);
                    intent = new Intent(this, BDMHomeActivity.class);
                }

                editor.apply();
                startActivity(intent);
                this.finish();

            } else
                Snackbar.make(root, "Invalid Login Credentials", Snackbar.LENGTH_SHORT).show();
        }
    }
}
