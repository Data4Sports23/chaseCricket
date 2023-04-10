package com.data4sports.chasecricket.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.data4sports.chasecricket.R;
import com.data4sports.chasecricket.models.SharedPrefManager;

/**
 * Created on 17/05/2021
 */

public class UserProfile extends AppCompatActivity {

    TextView tv_name, tv_email, tv_matches;
    String name, email, matches;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_profile);

        getDetails();
        findViewById(R.id.up_tv_change_password).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(UserProfile.this, ChangePassword.class));
            }
        });

        findViewById(R.id.up_ll_signout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPrefManager.getInstance(UserProfile.this).logout();
            }
        });
    }

    private void getDetails() {

        tv_name = findViewById(R.id.up_tv_name);
        tv_email = findViewById(R.id.up_tv_email);
        tv_matches = findViewById(R.id.up_tv_matches);
        tv_name.setText(name);
        tv_email.setText(email);
        tv_matches.setText(matches);

    }
}