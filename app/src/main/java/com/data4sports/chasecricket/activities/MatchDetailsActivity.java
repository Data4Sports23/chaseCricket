package com.data4sports.chasecricket.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.data4sports.chasecricket.R;
import com.data4sports.chasecricket.applicationConstants.AppConstants;
import com.data4sports.chasecricket.models.Match;
import com.data4sports.chasecricket.models.MatchOfficials;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;

/**
 * Created on 21/10/2020
 */

public class MatchDetailsActivity extends AppCompatActivity {

    TextView tv_teamA, tv_teamB, tv_event, tv_phase, tv_venue, tv_end1, tv_end2, tv_matchtype,
            tv_over, tv_bpo, tv_opb, tv_playersA, tv_playersB,
            tv_matchdate, tv_umpire1, tv_umpire2, tv_umpire3, tv_umpire4, tv_matchreferee;
    ImageButton back;

    int matchid, u1 = 0;
    Realm realm;

    RealmConfiguration config;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match_details);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.custom_action_bar_layout);
        View view =getSupportActionBar().getCustomView();

        back = (ImageButton) view.findViewById(R.id.action_bar_back);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*finish();*/
                Log.e("scorecard", "oncreate, back button pressd");

                onBackPressed();
            }
        });


         config = new RealmConfiguration.Builder()
                .name(AppConstants.GAME_ID + ".realm")
                .deleteRealmIfMigrationNeeded()
                .build();
        realm = Realm.getInstance(config);
        Intent intent = getIntent();
        matchid = intent.getIntExtra("matchid", 0);

        assignViews();

        if (matchid == 0)
            displayAlert();
        else
            displayMatchDetails();
    }


    public void assignViews() {

        tv_teamA = findViewById(R.id.md_tv_teamA);
        tv_teamB = findViewById(R.id.md_tv_teamB);
        tv_event = findViewById(R.id.md_tv_event);
        tv_phase = findViewById(R.id.md_tv_phase);
        tv_venue = findViewById(R.id.md_tv_venue);
        tv_end1 = findViewById(R.id.md_tv_end1);
        tv_end2 = findViewById(R.id.md_tv_end2);
        tv_matchtype = findViewById(R.id.md_tv_match_type);

        tv_over = findViewById(R.id.md_tv_over);
        tv_bpo = findViewById(R.id.md_tv_bpo);
        tv_opb = findViewById(R.id.md_tv_opb);
        tv_playersA = findViewById(R.id.md_tv_playersA);
        tv_playersB = findViewById(R.id.md_tv_plauersB);

        tv_matchdate = findViewById(R.id.md_tv_match_date);
        tv_umpire1 = findViewById(R.id.md_tv_umpire1);
        tv_umpire2 = findViewById(R.id.md_tv_umpire2);
        tv_umpire3 = findViewById(R.id.md_tv_umpire3);
        tv_umpire4 = findViewById(R.id.md_tv_umpire4);
        tv_matchreferee = findViewById(R.id.md_tv_match_referee);
    }


    public void displayMatchDetails() {

        Match match = realm.where(Match.class).equalTo("matchid", matchid).findFirst();
        if (match != null) {

            tv_teamA.setText(match.getTeamA());
            tv_teamB.setText(match.getTeamB());
            tv_event.setText(match.getEvent());
            tv_phase.setText(match.getPhase());
            tv_venue.setText(match.getVenue());
            tv_end1.setText(match.getEnd1());
            tv_end2.setText(match.getEnd2());
            if (match.getMatchType().matches("Custom")) {
                if (match.getActual_over() == 1000) {
                    tv_matchtype.setText(match.getMatchType());
                } else {
                    tv_matchtype.setText(match.getMatchType() +
                            "  (" + (int) match.getActual_over() + "over, " +
                            match.getBalls() + "bpo)");
                }
            }
            else
                tv_matchtype.setText(match.getMatchType());

            if (match.getActual_over() == 1000)
                tv_over.setText("");
            else
                tv_over.setText("" + match.getActual_over());
            tv_bpo.setText("" + match.getBalls());
            tv_opb.setText("" + match.getMax_opb());
            tv_playersA.setText("" + match.getPlayerA());
            tv_playersB.setText("" + match.getPlayerB());

            tv_matchdate.setText(match.getDate());
        }

        RealmResults<MatchOfficials> officials_result = realm.where(MatchOfficials.class).
                equalTo("matchid", matchid).findAll();
        if (officials_result.size() > 0) {
            for (MatchOfficials officials : officials_result) {
                if ((u1 == 0) && (officials.getStatus().matches("u1") || officials.getStatus().matches("u2"))) {
                    ++u1;
                    tv_umpire1.setText(officials.getOfficialName());
                }
                else if ((u1 == 1) && (officials.getStatus().matches("u1") || officials.getStatus().matches("u2"))) {
                    ++u1;
                    tv_umpire2.setText(officials.getOfficialName());
                }
                else if (officials.getStatus().matches("t")) {
                    tv_umpire3.setText(officials.getOfficialName());
                }
                else if (officials.getStatus().matches("f")) {
                    tv_umpire4.setText(officials.getOfficialName());
                }
                else if (officials.getStatus().matches("r")) {
                    tv_matchreferee.setText(officials.getOfficialName());
                }
            }
        }
    }


    public void displayAlert() {

        AlertDialog alertDialog = new AlertDialog.Builder(MatchDetailsActivity.this).create();
        alertDialog.setIcon(R.drawable.ball);
        alertDialog.setCancelable(false);
        alertDialog.setTitle("Couldn't find match details");
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK",
                new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alertDialog.show();
    }
}