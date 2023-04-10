package com.data4sports.chasecricket.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.data4sports.chasecricket.R;

/**
 * Created on 17/05/2021
 */
public class PlayerProfile extends AppCompatActivity {

    ImageView iv_player;
    TextView tv_name, tv_country, tv_age, tv_dob, tv_matches,
            tv_ODI, tv_T20, tv_Test, tv_total_run, tv_total_wickets;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.player_profile);

        assignViews();
    }

    public void assignViews() {
        iv_player = findViewById(R.id.pp_iv_player);
        tv_name = findViewById(R.id.pp_tv_name);
        tv_country = findViewById(R.id.pp_tv_country);
        tv_age = findViewById(R.id.pp_tv_age);
        tv_dob = findViewById(R.id.pp_tv_dob);
        tv_matches = findViewById(R.id.pp_tv_matches);
        tv_ODI = findViewById(R.id.pp_tv_ODI);
        tv_T20 = findViewById(R.id.pp_tv_T20);
        tv_Test = findViewById(R.id.pp_tv_Test);
        tv_total_run = findViewById(R.id.pp_tv_total_run);
        tv_total_wickets = findViewById(R.id.pp_tv_total_wicket);
    }
}