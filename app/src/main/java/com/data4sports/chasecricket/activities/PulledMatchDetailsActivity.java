package com.data4sports.chasecricket.activities;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.data4sports.chasecricket.R;
import com.data4sports.chasecricket.applicationConstants.AppConstants;
import com.data4sports.chasecricket.models.Constants;
import com.data4sports.chasecricket.models.Match;
import com.data4sports.chasecricket.models.MatchOfficials;
import com.data4sports.chasecricket.models.MidGen;
import com.data4sports.chasecricket.models.MyApplicationClass;
import com.data4sports.chasecricket.models.Player;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;
import io.realm.exceptions.RealmException;
import io.realm.exceptions.RealmPrimaryKeyConstraintException;

public class PulledMatchDetailsActivity extends AppCompatActivity {

    ImageButton back;
    JSONObject jsonMatch;
    JSONArray arraySquadA, arraySquadB;
    String token = null, jsonString = null;
    boolean flag = false, checkFlag = true, midScoringFlag = false, playersFlag = false, squadA = false,
            squadB = false, squad = false, mid_scoring = false, substitute = false, HUNDRED = false;

    String matchID = null, teamA = null, teamB = null, venue = null, end1 = null, end2 = null, phase = null,
            event = null, matchType = null, innings = null, umpire1 = "", umpire2 = "", umpire3 = "",
            umpire4 = "", scorer = "", matchReferee = "", name = null, matchDate = null, captainA = null,
            captainB = null, viceCaptainA = null, viceCaptainB = null, wkA = null, wkB = null,
            toss_winner = null, decision = null;

    int matchid = 0, playerA = 0, playerB = 0, over = 0, balls = 0, widerun = 1, noballrun = 1,
            penaltyrun = 5, userId = 0, position = 0, status = -1, player_id, gameid = 0, totalInnings = 0,
            teamAId = 0, teamBId = 0, eventId = 0, venueId = 0, total_player_count = 0, captainAID = 0,
            captainBID = 0, viceCaptainAID = 0, viceCaptainBID = 0, wkAID = 0, wkBID = 0, d4s_userid = 0,
            squadAccount = 0, max_bpb = 0, max_opb = 0;

    TextView tv_gameid, tv_teamA, tv_teamB, tv_event, tv_phase, tv_venue, tv_matchDate, tv_matchType,
            tv_over, tv_bpo, tv_opb, tv_playersA, tv_playersB;
    //tv_umpire1, tv_umpire2, tv_umpire3, tv_umpire4, tv_scorer, tv_matchReferee, tv_max_opb;
    Button btn_pull_squad, btn_save;

//    LinearLayout ll_max_opb;

    boolean hundred = false, limited_overs = false;
    final MidGen random = new MidGen();

    SharedPreferences mPreferences;
    SharedPreferences.Editor mEditor;
    Realm realm;
    private ProgressDialog progress;

    RealmConfiguration config;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pulled_match_details);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.custom_action_bar_layout);
        View view = getSupportActionBar().getCustomView();

        back = (ImageButton) view.findViewById(R.id.action_bar_back);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*finish();*/
                Log.e("pulledMatch", "oncreate, back button pressed");

                onBackPressed();
            }
        });

        assignValues();

        Realm.init(this);
        Realm.getDefaultConfiguration();
        displayProgress();

        mPreferences = PreferenceManager
                .getDefaultSharedPreferences(this);
        mPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        token = mPreferences.getString("user_token", null);
        userId = mPreferences.getInt("sp_user_id", 0);
        d4s_userid = mPreferences.getInt("d4s_userid", 0);
        Log.d("pulled", "userid : " + userId);
        Log.d("pulled", "d4s_userid : " + d4s_userid);
//        getFromSP();

        jsonMatch = new JSONObject();
        arraySquadA = new JSONArray();
        arraySquadB = new JSONArray();


        Intent i = getIntent();
        position = i.getIntExtra("position", 0);
        Log.d("pulled", "position : " + position);
        if (position >= 0) {
            viewObject(position);
        }

       /* jsonString = i.getStringExtra("jsonmatch");
        try {
            jsonMatch = new JSONObject(jsonString);
        } catch (JSONException e) {
            e.printStackTrace();
        }*/

      /* moved inside another method
        try {
            gameid = Integer.parseInt(jsonMatch.getString("gameid"));
//            mid_scoring = jsonMatch.getBoolean("midscoring");
            teamA = jsonMatch.getString("teamA");
            teamAId = Integer.parseInt(jsonMatch.getString("teamA_id"));
            Log.d("teamAId", "oncreate, : " + teamAId);
            teamB = jsonMatch.getString("teamB");
            teamBId = Integer.parseInt(jsonMatch.getString("teamB_id"));
            event = jsonMatch.getString("event");
            eventId = Integer.parseInt(jsonMatch.getString("event_id"));
            phase = jsonMatch.getString("phase");
            venue = jsonMatch.getString("venue");
            venueId = Integer.parseInt(jsonMatch.getString("venue_id"));
            if (jsonMatch.has("end1"))
                end1 = jsonMatch.getString("end1");
            else
                end1 = null;
            if (jsonMatch.has("end2"))
                end2 = jsonMatch.getString("end2");
            else
                end2 = null;
//            URLEncoder.encode("-", "UTF8");
//            URLEncoder.encode("\t", "UTF8");
            matchDate = jsonMatch.getString("date");
            Log.d("matchObject", "viewDetails : " + jsonMatch);
            Log.d("date", "viewDetails : " + jsonMatch.getString("date"));
//            final StringBuilder builder = matchObject.ge("date");
            matchType = jsonMatch.getString("match_type");
            // Added on 08/12/2021
            if ((jsonMatch.getString("teamAplayers") != null) &&
                    (jsonMatch.getString("teamBplayers") != null)) {
                playerA = Integer.parseInt(jsonMatch.getString("teamAplayers"));
                playerB = Integer.parseInt(jsonMatch.getString("teamBplayers"));
            }
            else
                Toast.makeText(getApplicationContext(),
                        "Invalid player count", Toast.LENGTH_SHORT).show();
            if (jsonMatch.getString("scheduled_overs") == null)
                over = 0;
            else
                over = Integer.parseInt(jsonMatch.getString("scheduled_overs"));
            balls = Integer.parseInt(jsonMatch.getString("balls_per_over"));
            Log.d("PMD", "max_overs_per_bowler = " + jsonMatch.getString("max_overs_per_bowler"));
            if (jsonMatch.getString("max_overs_per_bowler") == null)
                max_opb = 0;
            else
                if (jsonMatch.getString("max_overs_per_bowler").matches("null"))
                    max_opb = 0;
            else
                max_opb = Integer.parseInt(jsonMatch.getString("max_overs_per_bowler"));
            innings = jsonMatch.getString("innings");

            if (over > 0)
                limited_overs = true;

            if (innings.matches("multi") && (over == 0) && (max_opb == 0)) {
                over = 1000;
                // if so remove the visibility of overs and max overs/bowler
            } //else {
//
//            }
            // === till here



           *//* Don't need it
           if (jsonMatch.has("umpire1"))
                umpire1 = jsonMatch.getString("umpire1");
            else
                umpire1 = "";
            if (jsonMatch.has("umpire2"))
                umpire2 = jsonMatch.getString("umpire2");
            else
                umpire2 = "";
            if (jsonMatch.has("umpire3"))
                umpire3 = jsonMatch.getString("umpire3");
            else
                umpire3 = "";
            if (jsonMatch.has("umpire4"))
                umpire4 = jsonMatch.getString("umpire4");
            else
                umpire4 = "";
            if (jsonMatch.has("scorer"))
                scorer = jsonMatch.getString("scorer");
            else
                scorer = "";
            if (jsonMatch.has("match_referee"))
                matchReferee = jsonMatch.getString("match_referee");
            else
                matchReferee = "";*//*

        } catch (JSONException e) {
            e.printStackTrace();
        }*//* catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }*//*

        Log.d("PMDA", "gameid = " + gameid);
        Log.d("PMDA", "teamA = " + teamA);
        Log.d("PMDA", "teamB = " + teamB);
        Log.d("PMDA", "event = " + event);
        Log.d("PMDA", "phase = " + phase);
        Log.d("PMDA", "venue = " + venue);
        Log.d("PMDA", "matchDate = " + matchDate);
        Log.d("PMDA", "matchType = " + matchType);
        Log.d("PMDA", "over = " + over);
        Log.d("PMDA", "balls = " + balls);
        Log.d("PMDA", "max_opb = " + max_opb);
        Log.d("PMDA", "playerA = " + playerA);
        Log.d("PMDA", "playerB = " + playerB);

        tv_gameid.setText(String.valueOf(gameid));
        tv_teamA.setText(teamA);
        tv_teamB.setText(teamB);
        tv_event.setText(event);
        tv_phase.setText(phase);
        tv_venue.setText(venue);
        tv_matchDate.setText(matchDate);
        tv_matchType.setText(matchType);

        if (over == 1000)
            tv_over.setText("");
        else
            tv_over.setText(String.valueOf(over));
        tv_bpo.setText(String.valueOf(balls));
        tv_opb.setText(String.valueOf(max_opb));
        tv_playersA.setText(String.valueOf(playerA));
        tv_playersB.setText(String.valueOf(playerB));*/

       /* // Added on 29/07/2021
        if (matchType.matches("Test"))
            ll_max_opb.setVisibility(View.GONE);
        else {
            ll_max_opb.setVisibility(View.VISIBLE);
            if (matchType.matches("100s"))
            tv_max_opb.setText(max_opb);
        }
        // == till here*/
        /*tv_umpire1.setText(umpire1);
        tv_umpire2.setText(umpire2);
        tv_umpire3.setText(umpire3);
        tv_umpire4.setText(umpire4);
        tv_scorer.setText(scorer);
        tv_matchReferee.setText(matchReferee);*/


        btn_pull_squad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                displayProgress();
                pullSquad();
//                click = true;
                btn_pull_squad.setEnabled(false);
                btn_save.setEnabled(true);
            }
        });


        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (matchType.matches("Custom")) {
                    new AlertDialog.Builder(PulledMatchDetailsActivity.this)
                            .setIcon(R.drawable.ball)
                            .setTitle("Warning")
                            .setMessage("This is a custom match")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
//                                    flag = true;
                                    saveToDevice();
                                    saveToDeviceCurrentMatch();
                                }
                            })
                            .show();
                } else {
//                    flag = true;
                    saveToDevice();
                    saveToDeviceCurrentMatch();
                }

//                if (flag)
//                    saveToDevice();
            }
        });
    }


    public void assignValues() {

        tv_gameid = findViewById(R.id.gameid);
        tv_teamA = findViewById(R.id.teamA);
        tv_teamB = findViewById(R.id.teamB);
        tv_event = findViewById(R.id.event);
        tv_phase = findViewById(R.id.phase);
        tv_venue = findViewById(R.id.venue);
        tv_matchDate = findViewById(R.id.match_date);
        tv_matchType = findViewById(R.id.match_type);

//        tv_max_opb = findViewById(R.id.opb); // Added on 29/07/2021
//        ll_max_opb = findViewById(R.id.ll_opb); // Added on 29/07/2021

        tv_over = findViewById(R.id.over);
        tv_bpo = findViewById(R.id.bpo);
        tv_opb = findViewById(R.id.opb);
        tv_playersA = findViewById(R.id.playersA);
        tv_playersB = findViewById(R.id.playersB);

//        tv_umpire1 = findViewById(R.id.umpire1);
//        tv_umpire2 = findViewById(R.id.umpire2);
//        tv_umpire3 = findViewById(R.id.umpire3);
//        tv_umpire4 = findViewById(R.id.umpire4);
//        tv_scorer = findViewById(R.id.scorer);
//        tv_matchReferee = findViewById(R.id.match_referee);
        btn_pull_squad = findViewById(R.id.pull_squad);
        btn_save = findViewById(R.id.save);
        btn_save.setEnabled(false);
        btn_pull_squad.setEnabled(true);
//        ll_max_opb.setVisibility(View.VISIBLE);
    }


    private void pullSquad() {

        Log.d("internet", "pulled, pullSquad, isNetworkAvailable() : " + isNetworkAvailable());

        if (isNetworkAvailable()) {

            StringRequest stringRequest = new StringRequest(Request.Method.GET,
                    Constants.CHASE_CRICKET_PULL_SQUAD_API_TEST + "" + gameid,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            try {

                                Log.d("response", "PulledMatchDetails, : " + response);
                                JSONArray array = new JSONArray(response);
                                Log.d("array", "" + array);

                                JSONObject jsonSquad = array.getJSONObject(0);
                                Log.d("jsonSquad", "" + jsonSquad);

                                /*arraySquadA = jsonSquad.getJSONArray("squadA");
                                arraySquadB = json1Squad.getJSONArray("squadB");

                                if ((arraySquadA.length() > 0) && (arraySquadB.length() > 0)) {
                                    squad = true;
                                    Toast.makeText(PulledMatchDetailsActivity.this,
                                            "Squad details are pulled", Toast.LENGTH_SHORT).show();
                                    if (arraySquadA.length() == arraySquadB.length())
                                        total_player_count = arraySquadA.length();
                                    else if (arraySquadA.length() > arraySquadB.length())
                                        total_player_count = arraySquadB.length();
                                    else
                                        total_player_count = arraySquadA.length();
                                    progress.dismiss();
                                } else {
                                    squad = false;
                                    progress.dismiss();
                                    Toast.makeText(PulledMatchDetailsActivity.this,
                                            "No squad details are available", Toast.LENGTH_SHORT).show();
                                }*/

                                /*// code added on 13/04/2020 for not getting any team Id in response also no squad details
//                                String teamA_Id = jsonSquad.getString("teamA_id");
//                                Log.d("teamA_Id", " : " + teamA_Id);
                                if (jsonSquad.getString("teamA_id").matches("") || jsonSquad.getString("teamB_id").matches("")) {
                                    progress.dismiss();
                                    Toast.makeText(PulledMatchDetailsActivity.this, " Squad not available", Toast.LENGTH_SHORT).show();
                                    squadA = false;
                                }

                                else {
                                    arraySquadA = jsonSquad.getJSONArray("squadA");
                                    arraySquadB = jsonSquad.getJSONArray("squadB");

                                    if ((arraySquadA.length() > 0) && (arraySquadB.length() > 0)) {
                                         squad = true;
                                        Toast.makeText(PulledMatchDetailsActivity.this,
                                                "Squad details are pulled", Toast.LENGTH_SHORT).show();
                                        if (arraySquadA.length() == arraySquadB.length())
                                            total_player_count = arraySquadA.length();
                                        else if (arraySquadA.length() > arraySquadB.length())
                                            total_player_count = arraySquadB.length();
                                        else
                                            total_player_count = arraySquadA.length();
                                        progress.dismiss();
                                    } else {
                                        squad = false;
                                        progress.dismiss();
                                        Toast.makeText(PulledMatchDetailsActivity.this,
                                                "No squad details are available", Toast.LENGTH_SHORT).show();
                                    }
                                }

                                if (jsonSquad.getString("teamA_id").matches("")) {
                                    progress.dismiss();
                                    Toast.makeText(PulledMatchDetailsActivity.this, "No squad available", Toast.LENGTH_SHORT).show();
                                    squad = false;
                                }

                                else {*/
                                arraySquadA = jsonSquad.getJSONArray("squadA");
                                arraySquadB = jsonSquad.getJSONArray("squadB");

                                if ((arraySquadA.length() == 0) && (arraySquadB.length() == 0)) {
                                    squad = false;
                                    squadA = false;
                                    squadB = false;
                                    progress.dismiss();
                                    Toast.makeText(PulledMatchDetailsActivity.this,
                                            "No squad details are available", Toast.LENGTH_SHORT).show();
                                } else {
                                    squad = true;
                                    Toast.makeText(PulledMatchDetailsActivity.this,
                                            "Squad details are pulled", Toast.LENGTH_SHORT).show();
                                        /*if (arraySquadA.length() == arraySquadB.length())
                                            total_player_count = arraySquadA.length();
                                        else if (arraySquadA.length() > arraySquadB.length())
                                            total_player_count = arraySquadB.length();
                                        else
                                            total_player_count = arraySquadA.length();*/
                                    if ((arraySquadA.length() > 0) && (arraySquadB.length() > 0)) {
                                        squadA = true;
                                        squadB = true;
                                    } else if ((arraySquadA.length() > 0) && (arraySquadB.length() == 0)) {
                                        squadA = true;
                                        squadB = false;
                                    } else {
                                        squadA = false;
                                        squadB = true;
                                    }
                                    progress.dismiss();
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            //displaying the error in toast if occurrs
                            Log.d("TAG", "onErrorResponse: Exception " + error.getMessage());
                            progress.dismiss();
                            Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });


            //creating a request queue
            RequestQueue requestQueue = Volley.newRequestQueue(this);

            //adding the string request to request queue
            requestQueue.add(stringRequest);

            Log.d("PMDA", "squad, stringRequest  : " + stringRequest);

//            tempObject = matchObject;
//            saveToDevice();

        } else {

            progress.dismiss();
            AlertDialog alertDialog = new AlertDialog.Builder(PulledMatchDetailsActivity.this).create();
            alertDialog.setIcon(R.drawable.ball);
            alertDialog.setCancelable(false);
            alertDialog.setTitle("Network Error");
            alertDialog.setMessage("Please check your INTERNET connection");
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();

                        }
                    });
            alertDialog.show();

        }


    }


    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }


    @SuppressLint("SuspiciousIndentation")
    private void saveToDevice() {


        displayProgress();

        CreateMatchActivity createMatch = new CreateMatchActivity();

        Log.d("saveToDevice", "gameid = " + gameid);
        Log.d("saveToDevice", "teamA = " + teamA);
        Log.d("saveToDevice", "teamB = " + teamB);
        Log.d("saveToDevice", "venue = " + venue);
        Log.d("saveToDevice", "event = " + event);
        Log.d("saveToDevice", "matchDate = " + matchDate);
        Log.d("saveToDevice", "matchType = " + matchType);
        Log.d("saveToDevice", "innings = " + innings);
        Log.d("saveToDevice", "playerA = " + playerA);
        Log.d("saveToDevice", "playerB = " + playerB);
        Log.d("saveToDevice", "overs = " + over);
        Log.d("saveToDevice", "balls = " + balls);
        Log.d("saveToDevice", "max opb = " + max_opb);

        if (gameid <= 0) {
            checkFlag = false;
        } else if (teamA.matches("") || teamA == null) {
            checkFlag = false;
        } else if (teamB.matches("") || teamB == null) {
            checkFlag = false;
        } else if (venue.matches("") || venue == null) {
            checkFlag = false;
        } else if (event.matches("") || event == null) {
            checkFlag = false;
        } else if (matchDate.matches("") || matchDate == null) {
            checkFlag = false;
        } else if (matchType.matches("") || matchType == null) {
            checkFlag = false;
        } else if (innings.matches("") || innings == null) {
            checkFlag = false;
        } else if (playerA == 0 || playerB == 0 || balls == 0) {
            checkFlag = false;
        } else {
            checkFlag = true;
        }

/*      COMMENTED ON 09/12/2021
        else if (!matchType.matches("")) {

          */
/* Commented on 09/12/2021
            if (matchType.matches("T20")) {
                innings = "single";
                totalInnings = 2;
                playerA = 11;
                playerB = 11;
//                subst = 0;
                *//*
         */
/*commented on 13/10/2020
                if (total_player_count > 0 && total_player_count > player)
                    subst = total_player_count - player;
                else
                    subst = 5;*//*
         */
/*
                over = 20;
                balls = 6;
                widerun = 1;
                noballrun = 1;
                penaltyrun = 5;
                hundred = false;    // Added on 29/07/2021
                max_opb = 4;        // Added on 29/07/2021
                max_bpb = 0;        // Added on 29/07/2021
                limited_overs = true;   // Added on 07/09/2021
            }

            else if (matchType.matches("ODI")) {
                innings = "single";
                totalInnings = 2;
                playerA = 11;
                playerB = 11;
//                subst = 0;
                *//*
         */
/*commented on 13/10/2020
                if (total_player_count > 0 && total_player_count > player)
                    subst = total_player_count - player;
                else
                    subst = 5;*//*
         */
/*
                over = 50;
                balls = 6;
                widerun = 1;
                noballrun = 1;
                penaltyrun = 5;
                hundred = false;    // Added on 29/07/2021
                max_opb = 10;        // Added on 29/07/2021
                max_bpb = 0;        // Added on 29/07/2021
                limited_overs = true;   // Added on 07/09/2021
            }

            else if (matchType.matches("Test")) {
                innings = "multi";
                totalInnings = 4;
                playerA = 11;
                playerB = 11;
//                subst = 0;
                *//*
         */
/*commented on 13/10/2020
                if (total_player_count > 0 && total_player_count > player)
                    subst = total_player_count - player;
                else
                    subst = 5;*//*
         */
/*
                over = 1000;
                balls = 6;
                widerun = 1;
                noballrun = 1;
                penaltyrun = 5;
                hundred = false;    // Added on 29/07/2021
                max_opb = 0;        // Added on 29/07/2021
                max_bpb = 0;        // Added on 29/07/2021
                limited_overs = false;   // Added on 07/09/2021
            }

            // Added on 29/07/2021
            if (matchType.matches("100s")) {
                innings = "single";
                totalInnings = 2;
                playerA = 11;
                playerB = 11;
//                subst = 0;

                over = 20;
                balls = 5;
                widerun = 1;
                noballrun = 1;
                penaltyrun = 5;
                hundred = true;     // Added on 29/07/2021
                max_opb = 0;        // Added on 29/07/2021
                max_bpb = 20;       // Added on 29/07/2021
                limited_overs = true;   // Added on 07/09/2021
            }*//*

            // === till here

//            playersFlag = false;


//            added on 132/04/2020
            */
/*playerA = 11;
            playerB = 11;*//*

//            userId = matchObject.getInt("userid");
            Log.d("checkFlag", "saveToDevice, " + checkFlag);
            Log.d("totalInnings", "saveToDevice, " + totalInnings);
            Log.d("userId", "saveToDevice, " + userId);
        }
*/
//        else {

//            matchID = random.genId();
//        Realm realm;
////        config = new RealmConfiguration.Builder()
////                .name(AppConstants.GAME_ID + ".realm")
////                .deleteRealmIfMigrationNeeded()
////                .build();
//        realm = Realm.getDefaultInstance();
        matchID = checkID(random.genId());

        if (checkFlag) {

            if (innings.matches("single"))
                totalInnings = 2;
            else if (innings.matches("multi"))
                totalInnings = 4;

            Realm realm1 = null;
            try {
//                config = new RealmConfiguration.Builder()
//                        .name(AppConstants.GAME_ID + ".realm")
//                        .deleteRealmIfMigrationNeeded()
//                        .build();
                realm1 = Realm.getDefaultInstance();
                realm1.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm bgRealm) {

                        try {

                            Number num = bgRealm.where(Match.class).max("matchid");
                            int nextId = (num == null) ? 1 : num.intValue() + 1;

                            Match match = bgRealm.createObject(Match.class, nextId);
                            matchid = nextId;

                            match.setD4s_matchid(gameid);
                            match.setMatchID(matchID);
                            match.setUserId(userId);
                            match.setD4s_userid(d4s_userid);

                            match.setTeamA(teamA);
                            match.setTeamAId(teamAId);
                            match.setTeamB(teamB);
                            match.setTeamBId(teamBId);

                            match.setVenue(venue);
                            match.setVenueId(venueId);
                            match.setEnd1(end1);
                            match.setEnd2(end2);
                            match.setEvent(event);
                            match.setEventId(eventId);
                            match.setPhase(phase);

                            match.setMatchType(matchType);
                            match.setMax_opb(max_opb);
                            match.setInnings(innings);
                            match.setTotalInnings(totalInnings);

                            match.setPlayerA(playerA);
                            match.setPlayerB(playerB);
//                                match.setSubst(subst);
                            match.setOver(over);
                            match.setActual_over(over);
                            match.setBalls(balls);

                            match.setWiderun(widerun);
                            match.setNoballrun(noballrun);
                            match.setPenaltyrun(penaltyrun);

                            match.setDate(matchDate);

                            match.setPulled(true);
                            match.setPulled_squad(squad);
                            match.setPulled_squadA(squadA); // Added on 19/11/2021
                            match.setPulled_squadB(squadB); // Added on 19/11/2021
                            match.setHundred(hundred);    // Added on 29/07/2021
                            match.setMax_bpb(max_bpb);    // Added on 29/07/2021


                            match.setLimited_overs(limited_overs);    // Added on 29/07/2021

                             /*   match.setUmpire1(umpire1);
                                match.setUmpire2(umpire2);
                                match.setUmpire3(umpire3);
                                match.setUmpire4(umpire4);
                                match.setMatchReferee(matchReferee);*/

//                                match.setMatchSync(1);
//                            match.setScorer(scorer);

//                                                    realm.commitTransaction();
                            bgRealm.copyToRealm(match);

                            // posting match details on CreateMatch table

                            Log.d("match", "saveToDevice, " + match);
                            Log.d("squad", "saveToDevice, " + squad);
//                                postMatchDetails();
//                                if (squad)
//                                    saveSquad();
                            Toast.makeText(PulledMatchDetailsActivity.this,
                                    "Match saved on device", Toast.LENGTH_SHORT).show();


                            saveToSP();

                            //check thecondition that whether it has squad details or not, or whether it is a mid scoring assignment
                                /*if (midScoringFlag)
                                    pullDetails();
                                else
                                    deleteFromList();*/
//                                                    saveToServer();
                        } catch (RealmPrimaryKeyConstraintException e) {
                            progress.dismiss();
                            Toast.makeText(getApplicationContext(), "Primary Key exists, Press Update instead",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });


            } catch (RealmException e) {
                Log.d("test", "Exception : " + e);
            } finally {
                if (realm != null) {
                    realm.close();
                }
            }

//                if (!umpire1.matches(""))
//                    saveOfficial(umpire1, "u1");
//                if (!umpire2.matches(""))
//                    saveOfficial(umpire2, "u2");
//                if (!umpire3.matches(""))
//                    saveOfficial(umpire3, "t");
//                if (!umpire4.matches(""))
//                    saveOfficial(umpire4, "f");
//                if (!scorer.matches(""))
//                    saveOfficial(scorer, "s");
//                if (!matchReferee.matches(""))
//                    saveOfficial(matchReferee, "r");

               /* if (mid_scoring) {     not currently working on mid scoring

                    saveSquad();
                    saveDetails();
                }

                else {*/
            if (squad)
               // saveSquad();

            postMatchDetailsLocal(matchID, squad);
//                    postOfficialDetails();// added on 03/08/2021

            //}

        } else {

            progress.dismiss();
            new AlertDialog.Builder(PulledMatchDetailsActivity.this)
                    .setIcon(R.drawable.ball)
                    .setTitle("Save Match Failed")
                    .setMessage("Required fields are missing. Report to admin.")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    })
                    .show();
//                Toast.makeText(getApplicationContext(), "Can not save match due to some required fields are empty",
//                        Toast.LENGTH_LONG).show();
        }
//        }
    }

    private void saveToDeviceCurrentMatch() {


        displayProgress();
        if (gameid <= 0) {
            checkFlag = false;
        } else if (teamA.matches("") || teamA == null) {
            checkFlag = false;
        } else if (teamB.matches("") || teamB == null) {
            checkFlag = false;
        } else if (venue.matches("") || venue == null) {
            checkFlag = false;
        } else if (event.matches("") || event == null) {
            checkFlag = false;
        } else if (matchDate.matches("") || matchDate == null) {
            checkFlag = false;
        } else if (matchType.matches("") || matchType == null) {
            checkFlag = false;
        } else if (innings.matches("") || innings == null) {
            checkFlag = false;
        } else if (playerA == 0 || playerB == 0 || balls == 0) {
            checkFlag = false;
        } else {
            checkFlag = true;
        }

        config = new RealmConfiguration.Builder()
                .name(AppConstants.GAME_ID + ".realm")
                .deleteRealmIfMigrationNeeded()
                .build();
        realm = Realm.getInstance(config);
        matchID = checkIDName(random.genId());

        if (checkFlag) {

            if (innings.matches("single"))
                totalInnings = 2;
            else if (innings.matches("multi"))
                totalInnings = 4;

            Realm realm = null;
            try {
                config = new RealmConfiguration.Builder()
                        .name(AppConstants.GAME_ID + ".realm")
                        .deleteRealmIfMigrationNeeded()
                        .build();
                realm = Realm.getInstance(config);
                realm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm bgRealm) {

                        try {

                            Number num = bgRealm.where(Match.class).max("matchid");
                            int nextId = (num == null) ? 1 : num.intValue() + 1;

                            Match match = bgRealm.createObject(Match.class, nextId);
                            matchid = nextId;

                            match.setD4s_matchid(gameid);
                            match.setMatchID(matchID);
                            match.setUserId(userId);
                            match.setD4s_userid(d4s_userid);

                            match.setTeamA(teamA);
                            match.setTeamAId(teamAId);
                            match.setTeamB(teamB);
                            match.setTeamBId(teamBId);

                            match.setVenue(venue);
                            match.setVenueId(venueId);
                            match.setEnd1(end1);
                            match.setEnd2(end2);
                            match.setEvent(event);
                            match.setEventId(eventId);
                            match.setPhase(phase);

                            match.setMatchType(matchType);
                            match.setMax_opb(max_opb);
                            match.setInnings(innings);
                            match.setTotalInnings(totalInnings);

                            match.setPlayerA(playerA);
                            match.setPlayerB(playerB);
//                                match.setSubst(subst);
                            match.setOver(over);
                            match.setActual_over(over);
                            match.setBalls(balls);

                            match.setWiderun(widerun);
                            match.setNoballrun(noballrun);
                            match.setPenaltyrun(penaltyrun);

                            match.setDate(matchDate);

                            match.setPulled(true);
                            match.setPulled_squad(squad);
                            match.setPulled_squadA(squadA); // Added on 19/11/2021
                            match.setPulled_squadB(squadB); // Added on 19/11/2021
                            match.setHundred(hundred);    // Added on 29/07/2021
                            match.setMax_bpb(max_bpb);    // Added on 29/07/2021


                            match.setLimited_overs(limited_overs);    // Added on 29/07/2021

                             /*   match.setUmpire1(umpire1);
                                match.setUmpire2(umpire2);
                                match.setUmpire3(umpire3);
                                match.setUmpire4(umpire4);
                                match.setMatchReferee(matchReferee);*/

//                                match.setMatchSync(1);
//                            match.setScorer(scorer);

//                                                    realm.commitTransaction();
                            bgRealm.copyToRealm(match);

                            // posting match details on CreateMatch table

                            Log.d("match", "saveToDevice, " + match);
                            Log.d("squad", "saveToDevice, " + squad);
//                                postMatchDetails();
//                                if (squad)
//                                    saveSquad();
                            Toast.makeText(PulledMatchDetailsActivity.this,
                                    "Match saved on device", Toast.LENGTH_SHORT).show();


                            saveToSP();

                            //check thecondition that whether it has squad details or not, or whether it is a mid scoring assignment
                                /*if (midScoringFlag)
                                    pullDetails();
                                else
                                    deleteFromList();*/
//                                                    saveToServer();
                        } catch (RealmPrimaryKeyConstraintException e) {
                            progress.dismiss();
                            Toast.makeText(getApplicationContext(), "Primary Key exists, Press Update instead",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });


            } catch (RealmException e) {
                Log.d("test", "Exception : " + e);
            } finally {
                if (realm != null) {
                    realm.close();
                }
            }

//                if (!umpire1.matches(""))
//                    saveOfficial(umpire1, "u1");
//                if (!umpire2.matches(""))
//                    saveOfficial(umpire2, "u2");
//                if (!umpire3.matches(""))
//                    saveOfficial(umpire3, "t");
//                if (!umpire4.matches(""))
//                    saveOfficial(umpire4, "f");
//                if (!scorer.matches(""))
//                    saveOfficial(scorer, "s");
//                if (!matchReferee.matches(""))
//                    saveOfficial(matchReferee, "r");

               /* if (mid_scoring) {     not currently working on mid scoring

                    saveSquad();
                    saveDetails();
                }

                else {*/
            if (squad)
                saveSquad();

            postMatchDetails(matchID, squad);
//                    postOfficialDetails();// added on 03/08/2021

            //}

        } else {

            progress.dismiss();
            new AlertDialog.Builder(PulledMatchDetailsActivity.this)
                    .setIcon(R.drawable.ball)
                    .setTitle("Save Match Failed")
                    .setMessage("Required fields are missing. Report to admin.")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    })
                    .show();
//                Toast.makeText(getApplicationContext(), "Can not save match due to some required fields are empty",
//                        Toast.LENGTH_LONG).show();
        }
//        }
    }


    private void saveOfficial(String name, String type) {

        Realm realm = null;
//        try {
        config = new RealmConfiguration.Builder()
                .name(AppConstants.GAME_ID + ".realm")
                .deleteRealmIfMigrationNeeded()
                .build();
        realm = Realm.getInstance(config);
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm bgRealm) {

                try {

                    Number num = bgRealm.where(MatchOfficials.class).max("officialID");
                    int nextId = (num == null) ? 1 : num.intValue() + 1;

                    MatchOfficials officials = bgRealm.createObject(MatchOfficials.class, nextId);
                    officials.setMatchid(matchid);
                    officials.setMatchID(matchID);
                    officials.setOfficialName(name);
                    officials.setStatus(type);

                    bgRealm.copyToRealm(officials);

                } catch (RealmException e) {
                    Toast.makeText(getApplicationContext(),
                            " " + e, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


//    public void postMatchDetails(String matchID, boolean pulled) {
//
//
//         config = new RealmConfiguration.Builder()
//                .name(AppConstants.GAME_ID + ".realm")
//                .deleteRealmIfMigrationNeeded()
//                .build();
//        realm = Realm.getInstance(config);
//        if (isNetworkAvailable()) {
//
////                Log.e("creatematch", "matchID : "+matchID);
////                sync = save.post(matchID);
////                Log.e("creatematch", "matchsync : "+sync);
//
//            Match match = realm.where(Match.class).equalTo("matchID", matchID).findFirst();
//
//
//            JSONObject matchObject = new JSONObject();
//            try {
//
//                matchObject.put("gameid", match.getD4s_matchid());
//                matchObject.put("matchID", matchID);
//                matchObject.put("teamA", match.getTeamA());
//                matchObject.put("teamA_id", match.getTeamAId());
//                matchObject.put("teamB", match.getTeamB());
//                matchObject.put("teamB_id", match.getTeamBId());
//                matchObject.put("venue", match.getVenue());
//                matchObject.put("venue_id", match.getVenueId());
//                matchObject.put("end1", match.getEnd1());
//                matchObject.put("end2", match.getEnd2());
//                matchObject.put("event", match.getEvent());
//                matchObject.put("event_id", match.getEventId());
//                matchObject.put("phase", match.getPhase());
//                matchObject.put("match_type", match.getMatchType());
//                matchObject.put("innings", match.getInnings());
//                matchObject.put("players", match.getPlayer());
//                matchObject.put("substitute_players", match.getSubst());
//                matchObject.put("over", match.getOver());
//                matchObject.put("balls_per_over", match.getBalls());
//                matchObject.put("wide_value", match.getWiderun());
//                matchObject.put("noball_value", match.getNoballrun());
//                matchObject.put("penalty_value", match.getPenaltyrun());
//                matchObject.put("umpire1", match.getUmpire1());
//                matchObject.put("umpire2", match.getUmpire2());
//                matchObject.put("umpire3", match.getUmpire3());
//                matchObject.put("umpire4", match.getUmpire4());
//                matchObject.put("match_referee", match.getMatchReferee());
//
/////
////                postparams.put("token", token);
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//
//
//            JSONObject jsonfeed = new JSONObject();
//            try {
//                jsonfeed.put("AddMatch", matchObject);
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//
//
//            JSONObject postparams = new JSONObject();
//            try {
//                postparams.put("title", "CHASE_POST");
//                postparams.put("feed", jsonfeed);
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//
//            Log.d("postparams", "pulledmatch, : " + postparams);
//
//            JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
//                    Constants.CHASE_CRICKET_MATCH_API, postparams,
//                    new Response.Listener<JSONObject>() {
//                        @Override
//                        public void onResponse(JSONObject response) {
//
//                            try {
//
//                                Log.d("response", "pulledmatch : " + response);
//
//                                //if no error in response
//
//                                if (!response.getBoolean("error") && response.getInt("status") == 200) {
//
//                                    JSONObject jsonMatch = response.getJSONObject("match");
//                                    Log.d("create", "pulledmatch, jsonMatch : " + jsonMatch);
//                                    Realm realm = null;
//                                    try {
//                                         config = new RealmConfiguration.Builder()
//                .name(AppConstants.GAME_ID + ".realm")
//                .deleteRealmIfMigrationNeeded()
//                .build();
//        realm = Realm.getInstance(config);
//                                        realm.executeTransaction(new Realm.Transaction() {
//                                            @Override
//                                            public void execute(Realm bgRealm) {
//
//                                                try {
//
//                                                    Match match1 = bgRealm.where(Match.class).
//                                                            equalTo("matchID",
//                                                                    jsonMatch.getString("app_matchID")).
//                                                            findFirst();
//
//                                                    match1.setMatchSync(1);
//                                                    match1.setStatus("MC");
//                                                    match1.setStatusId(1);
//                                                    bgRealm.copyToRealm(match1);
//
//                                                } catch (JSONException e) {
//                                                    e.printStackTrace();
//                                                } catch (RealmPrimaryKeyConstraintException e) {
//                                                    Toast.makeText(getApplicationContext(),
//                                                            "Primary Key exists, Press Update instead",
//                                                            Toast.LENGTH_SHORT).show();
//                                                }
//                                            }
//                                        });
//                                    } catch (RealmException e) {
//                                        Log.d("pulledmatch", "Exception : " + e);
//                                    } finally {
//                                        if (realm != null) {
//                                            realm.close();
//                                        }
//                                    }
//
//
//                                    Toast.makeText(getApplicationContext(),
//                                            response.getString("message"), Toast.LENGTH_SHORT).show();
//
//
//                                    //starting the profile activity
//
////                                        startActivity(new Intent(CreateMatchActivity.this, AddPlayers.class));
////                                        finish();*/
//
//                                } else {
//                                    Toast.makeText(getApplicationContext(),
//                                            response.getString("message"), Toast.LENGTH_SHORT).show();
//                                }
//
//                            } catch (JSONException e) {
//                                e.printStackTrace();
//                            }
//                        }
//                    },
//                    new Response.ErrorListener() {
//                        @Override
//                        public void onErrorResponse(VolleyError error) {
//
////
////                                  Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
//                            Log.d("pulledmatch)", "Error Message is  : " + error);
//
//                        }
//                    });
//
//            MyApplicationClass.getInstance(getApplicationContext()).
//                    addToRequestQueue(jsonObjReq, "postRequest");
//            Log.d("pulledmatch", "postMatchDetails, jsonObjReq  : " + jsonObjReq);
//            Log.d("pulledmatch", "postMatchDetails, postparams  : " + postparams);
//
//        }
//
//        Toast.makeText(getApplicationContext(), "Match created", Toast.LENGTH_SHORT).show();
//        if (!pulled) {
//            startActivity(new Intent(PulledMatchDetailsActivity.this, AddPlayers.class));
//            finish();
//        }
//    }


    public void postMatchDetailsLocal(String matchID, boolean pulled) {

//        config = new RealmConfiguration.Builder()
//                .name(AppConstants.GAME_ID + ".realm")
//                .deleteRealmIfMigrationNeeded()
//                .build();
//        realm = Realm.getInstance(config);

        Realm realm;
        realm = Realm.getDefaultInstance();
        Log.d("internet", "pulled, postMatchDetails, isNetworkAvailable() : " + isNetworkAvailable());

        if (isNetworkAvailable()) {

//                Log.e("creatematch", "matchID : "+matchID);
//                sync = save.post(matchID);
//                Log.e("creatematch", "matchsync : "+sync);

            Match match = realm.where(Match.class).equalTo("matchID", matchID).findFirst();
            Log.d("match", "pulledmatch , " + match);

            JSONObject matchObject = new JSONObject();

            try {

                matchObject.put("d4s_gameid", match.getD4s_matchid());
                matchObject.put("d4s_userid", match.getD4s_userid());

                matchObject.put("matchID", matchID);
                matchObject.put("teamA", match.getTeamA());
                matchObject.put("d4s_teamA_id", match.getTeamAId());
                matchObject.put("teamB", match.getTeamB());
                matchObject.put("d4s_teamB_id", match.getTeamBId());
                matchObject.put("venue", match.getVenue());
                matchObject.put("d4s_venue_id", match.getVenueId());
                if (match.getEnd1() == null)
                    matchObject.put("end1", "");
                else
                    matchObject.put("end1", match.getEnd1());
                if (match.getEnd2() == null)
                    matchObject.put("end2", "");
                else
                    matchObject.put("end2", match.getEnd2());
                matchObject.put("event", match.getEvent());
                matchObject.put("d4s_event_id", match.getEventId());
                if (match.getPhase() == null)
                    matchObject.put("phase", "");
                else
                    matchObject.put("phase", match.getPhase());
                matchObject.put("match_type", match.getMatchType());
                matchObject.put("innings", match.getInnings());
                matchObject.put("teamAplayers", match.getPlayerA());
                matchObject.put("teamBplayers", match.getPlayerB());
                matchObject.put("over", match.getOver());
                matchObject.put("balls_per_over", match.getBalls());
                matchObject.put("wide_value", match.getWiderun());
                matchObject.put("noball_value", match.getNoballrun());
                matchObject.put("penalty_value", match.getPenaltyrun());
                matchObject.put("rainruleused", "n");
                matchObject.put("max_overs_per_bowler", match.getMax_opb());
                matchObject.put("max_balls_per_bowler", match.getMax_bpb());

            } catch (JSONException e) {
                e.printStackTrace();
            }


            JSONObject jsonfeed = new JSONObject();
            try {
                jsonfeed.put("AddMatch", matchObject);
            } catch (JSONException e) {
                e.printStackTrace();
            }


            JSONObject postparams = new JSONObject();
            try {
                postparams.put("title", "CHASE_POST");
                postparams.put("feed", jsonfeed);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            Log.d("pulledmatch", "postparams : " + postparams);

            JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                    Constants.CHASE_CRICKET_MATCH_API, postparams,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {

                            try {

                                Log.d("pulledmatch", "response : " + response);

                                //if no error in response

                                if (!response.getBoolean("error") && response.getInt("status") == 200) {

                                    JSONObject jsonMatch = response.getJSONObject("match");
                                    // Added on 03/08/2021
                                    String MATCHID = jsonMatch.getString("app_matchID");
                                    int teamA_id = jsonMatch.getInt("team1_id");
                                    int teamB_id = jsonMatch.getInt("team2_id");
                                    Log.d("pulledmatch", " jsonMatch : " + jsonMatch);
                                    Realm realm = null;
                                    try {
                                       realm = Realm.getDefaultInstance();
                                        realm.executeTransaction(new Realm.Transaction() {
                                            @Override
                                            public void execute(Realm bgRealm) {

                                                try {

                                                    Match match1 = bgRealm.where(Match.class).
                                                            equalTo("matchID",
                                                                    jsonMatch.getString("app_matchID")).
                                                            findFirst();

                                                    if (match1 != null) {
                                                        match1.setMatchSync(1);
                                                        match1.setPost(true);
                                                        Log.d("matchSync", "pulled, match synced");
                                                        match1.setStatus("MC");
                                                        match1.setStatusId(1);
                                                        match1.setTeamA_sync(1);
                                                        match1.setTeamB_sync(1);

                                                        // Adding team ids
                                                        match1.setTeamAId(teamA_id);
                                                        match1.setTeamBId(teamB_id);

                                                        bgRealm.copyToRealm(match1);
                                                        Log.d("matchSync", "pulled, match : " + match1);

                                                        postSquads();   // Added on 14/12/2021
                                                    }

                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                } catch (RealmPrimaryKeyConstraintException e) {
                                                    Toast.makeText(getApplicationContext(),
                                                            "Primary Key exists, Press Update instead",
                                                            Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                                    } catch (RealmException e) {
                                        Log.d("test", "Exception : " + e);
                                    } finally {
                                        if (realm != null) {
                                            realm.close();
                                        }
                                    }


//                                    progress.dismiss();
//                                    Toast.makeText(getApplicationContext(),
//                                            response.getString("message"), Toast.LENGTH_SHORT).show();


                                    //starting the profile activity

//                                        startActivity(new Intent(CreateMatchActivity.this, AddPlayers.class));
//                                        finish();*/

                                } else {
                                    Toast.makeText(getApplicationContext(),
                                            response.getString("message"), Toast.LENGTH_SHORT).show();
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

//
//                                  Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                            Log.d("pulledmatch", "Error Message is  : " + error);

                        }
                    });

            MyApplicationClass.getInstance(getApplicationContext()).addToRequestQueue(jsonObjReq, "postRequest");
            Log.d("pulledmatch", "jsonObjReq  : " + jsonObjReq);
            Log.d("pulledmatch)", "postparams  : " + postparams);

        }

//        Toast.makeText(getApplicationContext(), "Match created", Toast.LENGTH_SHORT).show();

        // commented for checking AddSquad page
//        if (pulled) {

//        saveToSP();   Commented on 14/12/2021


//        if (squad) {
//            //SelectTeamAXIActivity.class));
//            // Added on 19/11/2021
//            if (!squadA)
//                startActivity(new Intent(PulledMatchDetailsActivity.this, AddPlayersA.class));
//            else if (!squadB)
//                startActivity(new Intent(PulledMatchDetailsActivity.this, AddPlayersB.class));
//            else
//                // till here
//                startActivity(new Intent(PulledMatchDetailsActivity.this, TossActivity.class));
//        } else {
//            //commented on 29/04/2020
////                  startActivity(new Intent(PulledMatchDetailsActivity.this, AddPlayers.class));
//            startActivity(new Intent(PulledMatchDetailsActivity.this, AddPlayersA.class));
//        }
//        finish();
//        progress.dismiss();
///*        }
//        else {
//            saveToSP();
//            squadAccount = realm.where(Player.class).
//                    equalTo("matchid", matchid).
//                    equalTo("team", 1).findAll().size();
//            Log.d("squad", "pulled, squadAccount : " + squadAccount);
////            startActivity(new Intent(PulledMatchDetailsActivity.this, AddSquad.class));
//            startActivity(new Intent(PulledMatchDetailsActivity.this, AddSquad.class));
//            finish();
//            progress.dismiss();
//        }*/
    }

    public void postMatchDetails(String matchID, boolean pulled) {

        config = new RealmConfiguration.Builder()
                .name(AppConstants.GAME_ID + ".realm")
                .deleteRealmIfMigrationNeeded()
                .build();
        realm = Realm.getInstance(config);
        Log.d("internet", "pulled, postMatchDetails, isNetworkAvailable() : " + isNetworkAvailable());

        if (isNetworkAvailable()) {

//                Log.e("creatematch", "matchID : "+matchID);
//                sync = save.post(matchID);
//                Log.e("creatematch", "matchsync : "+sync);

            Match match = realm.where(Match.class).equalTo("matchID", matchID).findFirst();
            Log.d("match", "pulledmatch , " + match);

            JSONObject matchObject = new JSONObject();

            try {

                matchObject.put("d4s_gameid", match.getD4s_matchid());
                matchObject.put("d4s_userid", match.getD4s_userid());

                matchObject.put("matchID", matchID);
                matchObject.put("teamA", match.getTeamA());
                matchObject.put("d4s_teamA_id", match.getTeamAId());
                matchObject.put("teamB", match.getTeamB());
                matchObject.put("d4s_teamB_id", match.getTeamBId());
                matchObject.put("venue", match.getVenue());
                matchObject.put("d4s_venue_id", match.getVenueId());
                if (match.getEnd1() == null)
                    matchObject.put("end1", "");
                else
                    matchObject.put("end1", match.getEnd1());
                if (match.getEnd2() == null)
                    matchObject.put("end2", "");
                else
                    matchObject.put("end2", match.getEnd2());
                matchObject.put("event", match.getEvent());
                matchObject.put("d4s_event_id", match.getEventId());
                if (match.getPhase() == null)
                    matchObject.put("phase", "");
                else
                    matchObject.put("phase", match.getPhase());
                matchObject.put("match_type", match.getMatchType());
                matchObject.put("innings", match.getInnings());
                matchObject.put("teamAplayers", match.getPlayerA());
                matchObject.put("teamBplayers", match.getPlayerB());
                matchObject.put("over", match.getOver());
                matchObject.put("balls_per_over", match.getBalls());
                matchObject.put("wide_value", match.getWiderun());
                matchObject.put("noball_value", match.getNoballrun());
                matchObject.put("penalty_value", match.getPenaltyrun());
                matchObject.put("rainruleused", "n");
                matchObject.put("max_overs_per_bowler", match.getMax_opb());
                matchObject.put("max_balls_per_bowler", match.getMax_bpb());

            } catch (JSONException e) {
                e.printStackTrace();
            }


            JSONObject jsonfeed = new JSONObject();
            try {
                jsonfeed.put("AddMatch", matchObject);
            } catch (JSONException e) {
                e.printStackTrace();
            }


            JSONObject postparams = new JSONObject();
            try {
                postparams.put("title", "CHASE_POST");
                postparams.put("feed", jsonfeed);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            Log.d("pulledmatch", "postparams : " + postparams);

            JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                    Constants.CHASE_CRICKET_MATCH_API, postparams,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {

                            try {

                                Log.d("pulledmatch", "response : " + response);

                                //if no error in response

                                if (!response.getBoolean("error") && response.getInt("status") == 200) {

                                    JSONObject jsonMatch = response.getJSONObject("match");
                                    // Added on 03/08/2021
                                    String MATCHID = jsonMatch.getString("app_matchID");
                                    int teamA_id = jsonMatch.getInt("team1_id");
                                    int teamB_id = jsonMatch.getInt("team2_id");
                                    Log.d("pulledmatch", " jsonMatch : " + jsonMatch);
                                    Realm realm = null;
                                    try {
                                        config = new RealmConfiguration.Builder()
                                                .name(AppConstants.GAME_ID + ".realm")
                                                .deleteRealmIfMigrationNeeded()
                                                .build();
                                        realm = Realm.getInstance(config);
                                        realm.executeTransaction(new Realm.Transaction() {
                                            @Override
                                            public void execute(Realm bgRealm) {

                                                try {

                                                    Match match1 = bgRealm.where(Match.class).
                                                            equalTo("matchID",
                                                                    jsonMatch.getString("app_matchID")).
                                                            findFirst();

                                                    if (match1 != null) {
                                                        match1.setMatchSync(1);
                                                        match1.setPost(true);
                                                        Log.d("matchSync", "pulled, match synced");
                                                        match1.setStatus("MC");
                                                        match1.setStatusId(1);
                                                        match1.setTeamA_sync(1);
                                                        match1.setTeamB_sync(1);

                                                        // Adding team ids
                                                        match1.setTeamAId(teamA_id);
                                                        match1.setTeamBId(teamB_id);

                                                        bgRealm.copyToRealm(match1);
                                                        Log.d("matchSync", "pulled, match : " + match1);

                                                        postSquads();   // Added on 14/12/2021
                                                    }

                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                } catch (RealmPrimaryKeyConstraintException e) {
                                                    Toast.makeText(getApplicationContext(),
                                                            "Primary Key exists, Press Update instead",
                                                            Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                                    } catch (RealmException e) {
                                        Log.d("test", "Exception : " + e);
                                    } finally {
                                        if (realm != null) {
                                            realm.close();
                                        }
                                    }


//                                    progress.dismiss();
//                                    Toast.makeText(getApplicationContext(),
//                                            response.getString("message"), Toast.LENGTH_SHORT).show();


                                    //starting the profile activity

//                                        startActivity(new Intent(CreateMatchActivity.this, AddPlayers.class));
//                                        finish();*/

                                } else {
                                    Toast.makeText(getApplicationContext(),
                                            response.getString("message"), Toast.LENGTH_SHORT).show();
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

//
//                                  Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                            Log.d("pulledmatch", "Error Message is  : " + error);

                        }
                    });

            MyApplicationClass.getInstance(getApplicationContext()).addToRequestQueue(jsonObjReq, "postRequest");
            Log.d("pulledmatch", "jsonObjReq  : " + jsonObjReq);
            Log.d("pulledmatch)", "postparams  : " + postparams);

        }

//        Toast.makeText(getApplicationContext(), "Match created", Toast.LENGTH_SHORT).show();

        // commented for checking AddSquad page
//        if (pulled) {

//        saveToSP();   Commented on 14/12/2021


        if (squad) {
            //SelectTeamAXIActivity.class));
            // Added on 19/11/2021
            if (!squadA)
                startActivity(new Intent(PulledMatchDetailsActivity.this, AddPlayersA.class));
            else if (!squadB)
                startActivity(new Intent(PulledMatchDetailsActivity.this, AddPlayersB.class));
            else
                // till here
                startActivity(new Intent(PulledMatchDetailsActivity.this, TossActivity.class));
        } else {
            //commented on 29/04/2020
//                  startActivity(new Intent(PulledMatchDetailsActivity.this, AddPlayers.class));
            startActivity(new Intent(PulledMatchDetailsActivity.this, AddPlayersA.class));
        }
        finish();
        progress.dismiss();
/*        }
        else {
            saveToSP();
            squadAccount = realm.where(Player.class).
                    equalTo("matchid", matchid).
                    equalTo("team", 1).findAll().size();
            Log.d("squad", "pulled, squadAccount : " + squadAccount);
//            startActivity(new Intent(PulledMatchDetailsActivity.this, AddSquad.class));
            startActivity(new Intent(PulledMatchDetailsActivity.this, AddSquad.class));
            finish();
            progress.dismiss();
        }*/
    }


    private void postPlayerDetails(int d4s_player_id, int player_id) {

        if (isNetworkAvailable()) {

            JSONObject playerObject = new JSONObject();
            try {


                playerObject.put("gameid", gameid);
                playerObject.put("d4s_playerid", d4s_player_id);
                playerObject.put("playerid", player_id);
//                matchObject.put("matchID", matchID);

            } catch (JSONException e) {
                e.printStackTrace();
            }

            JSONObject postparams = new JSONObject();
            try {
                postparams.put("title", "CHASE_POST");
                postparams.put("d4s_pull", playerObject);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                    Constants.CHASE_CRICKET_PULL_SQUAD_API_TEST + "" + gameid, postparams,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {

//                            try {

                            Log.d("postPlayerDetails", "response : " + response);

                            //if no error in response

                                /*if (!response.getBoolean("error") && response.getInt("status") == 200) {

                                    JSONObject jsonMatch = response.getJSONObject("match");
                                    Log.d("create", "login(u,p), jsonMatch : " + jsonMatch);



                                    Toast.makeText(getApplicationContext(),
                                            response.getString("message"), Toast.LENGTH_SHORT).show();


                                    //starting the profile activity

//                                        startActivity(new Intent(CreateMatchActivity.this, AddPlayers.class));
//                                        finish();

                                } else {
                                    Toast.makeText(getApplicationContext(),
                                            response.getString("message"), Toast.LENGTH_SHORT).show();
                                }*/

//                            } catch (JSONException e) {
//                                e.printStackTrace();
//                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

//
//                                  Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                            Log.d("postPlayerDetails", "Error Message is  : " + error);

                        }
                    });

            MyApplicationClass.getInstance(getApplicationContext()).addToRequestQueue(jsonObjReq, "postRequest");
            Log.d("postPlayerDetails", "postPlayerDetails, jsonObjReq  : " + jsonObjReq);
            Log.d("postPlayerDetails", "postPlayerDetails, postparams  : " + postparams);

        }
    }


    public void saveToSP() {

        mEditor = mPreferences.edit();
        mEditor.putInt("sp_match_id", matchid);
        mEditor.putInt("sp_game_id", gameid);
        mEditor.putString("sp_match_ID", matchID);
//        mEditor.putString("sp_token", token);
//        mEditor.putString("sp_team", teamA);
//        mEditor.putInt("sp_team_id", 1);
        mEditor.putString("sp_teamA", teamA);
        mEditor.putString("sp_teamB", teamB);
        mEditor.putInt("sp_playerA", playerA);
        mEditor.putInt("sp_playerB", playerB);
//        mEditor.putInt("sp_player_count", 11);
//        mEditor.putInt("sp_sub_seq", subst);
        mEditor.putString("sp_innings", innings);
        mEditor.putInt("sp_over", over);
        mEditor.putInt("sp_balls", balls);
        mEditor.putString("sp_match_type", matchType);
        mEditor.putInt("sp_wide_value", widerun);
        mEditor.putInt("sp_noball_value", noballrun);
        mEditor.putInt("sp_penalty_value", penaltyrun);
        mEditor.putInt("sp_current_innings", 0);
        mEditor.putBoolean("sp_hundred", HUNDRED);

        mEditor.putBoolean("squad", squad);

        Log.d("pulled", "saveToSP, captainA  : " + captainA + ", captainAID : " + captainAID);
        Log.d("pulled", "saveToSP, captainAB  : " + captainB + ", captainBID : " + captainBID);
        Log.d("pulled", "saveToSP, viceCaptainA  : " + viceCaptainA + ", viceCaptainAID : " + viceCaptainAID);
        Log.d("pulled", "saveToSP, viceCaptainB  : " + viceCaptainB + ", viceCaptainBID : " + viceCaptainBID);
        Log.d("pulled", "saveToSP, wkA  : " + wkA + ", wkAID : " + wkAID);
        Log.d("pulled", "saveToSP, wkB  : " + wkB + ", wkBID : " + wkBID);
        mEditor.putString("sp_captainA", captainA);
        mEditor.putInt("sp_captainA_id", captainAID);
        mEditor.putString("sp_captainB", captainB);
        mEditor.putInt("sp_captainB_id", captainBID);
        mEditor.putString("sp_vcA", viceCaptainA);
        mEditor.putInt("sp_vcA_id", viceCaptainAID);
        mEditor.putString("sp_vcB", viceCaptainB);
        mEditor.putInt("sp_vcB_id", viceCaptainBID);
        mEditor.putString("sp_wkA", wkA);
        mEditor.putInt("sp_wkA_id", wkAID);
        mEditor.putString("sp_wkB", wkB);
        mEditor.putInt("sp_wkB_id", wkBID);
        mEditor.putInt("sp_team1", 1);
        mEditor.putInt("sp_team2", 2);

        mEditor.apply();
    }


    private void saveSquad() {

        JSONObject jsonTemp = new JSONObject();

        Log.d("arraySquadA", "saveSquad, " + arraySquadA);
        Log.d("arraySquadB", "saveSquad, " + arraySquadB);

        if (arraySquadA.length() > 0/* && arraySquadB.length() > 0*/) {

            for (int i = 0; i < arraySquadA.length(); i++) {

                try {
                    jsonTemp = arraySquadA.getJSONObject(i);

                    savePlayers(1,
                            jsonTemp.getString("name"),
                            jsonTemp.getString("captain"),
                            jsonTemp.getString("vice_captain"),
                            jsonTemp.getString("wicketkeeper"),
                            jsonTemp.getInt("d4s_playerid"),
                            teamAId,
                            gameid);        // added on 05/06/2020
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }


        if (arraySquadB.length() > 0) {

            for (int i = 0; i < arraySquadB.length(); i++) {

                try {
                    jsonTemp = arraySquadB.getJSONObject(i);

                    savePlayers(2,
                            jsonTemp.getString("name"),
                            jsonTemp.getString("captain"),
                            jsonTemp.getString("vice_captain"),
                            jsonTemp.getString("wicketkeeper"),
                            jsonTemp.getInt("d4s_playerid"),
                            teamBId,
                            gameid);        // added on 05/06/2020
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

    }


    public void savePlayers(int team, String name, String cap, String vc, String wk, int d4s_pid, int d4s_tid, int gameid) {

        Realm realm = null;
        try {
//            Toast.makeText(getApplicationContext(), "Inside savePlayerDetails", Toast.LENGTH_SHORT).show();
            config = new RealmConfiguration.Builder()
                    .name(AppConstants.GAME_ID + ".realm")
                    .deleteRealmIfMigrationNeeded()
                    .build();
            realm = Realm.getInstance(config);
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {


                    try {

                        Number num = realm.where(Player.class).max("playerID");
                        player_id = (num == null) ? 1 : num.intValue() + 1;
                        Player player = new Player();
                        player.setPlayerID(player_id);
                        player.setMatchid(matchid);
                        player.setMatchID(matchID);
//                        if (mid_scoring)
                        player.setSubstitute(substitute);
                        /*else
                            player.setSubstitute(true);*/

                        player.setTeam(team);
                        player.setPulled(1);
                        player.setPlayerName(name);

                        player.setGameid(gameid);
                        player.setD4s_teamID(d4s_tid);


                        if (cap.matches("yes")) {

                            Log.d("polled", "captain : " + name + ", team : " + team);
                            player.setCaptain(true);

                            if (team == 1) {
                                captainA = name;
                                captainAID = player_id;
                            } else if (team == 2) {
                                captainB = name;
                                captainBID = player_id;
                            }
                        } else
                            player.setCaptain(false);


                        if (vc.matches("yes")) {
                            Log.d("polled", "vc : " + name + ", team : " + team);
                            player.setViceCaptain(true);

                            if (team == 1) {
                                viceCaptainA = name;
                                viceCaptainAID = player_id;
                            } else if (team == 2) {
                                viceCaptainB = name;
                                viceCaptainBID = player_id;
                            }
                        } else
                            player.setViceCaptain(false);


                        if (wk.matches("yes")) {
                            Log.d("polled", "wk : " + name + ", team : " + team);
                            player.setWicketKeeper(true);

                            if (team == 1) {
                                wkA = name;
                                wkAID = player_id;
                            } else if (team == 2) {
                                wkB = name;
                                wkBID = player_id;
                            }

                        } else
                            player.setWicketKeeper(false);


                        player.setD4s_playerid(d4s_pid);

                        realm.copyToRealm(player);

//                        postPlayerDetails(d4s_pid, player_id);
                        Log.d("player", "savePlayers, : " + player.getPlayerName() + ", " + player.getD4s_playerid() + ", " +
                                player.getMatchid() + ", " + player.getMatchID() + ", " + player.isSubstitute() + ", " +
                                player.getTeam() + ", " + player.isCaptain() + ", " + player.isViceCaptain() + ", " +
                                player.isWicketKeeper() + ", " + player_id + ", " + player);
                    } catch (RealmPrimaryKeyConstraintException e) {
                        Toast.makeText(getApplicationContext(), "Primary Key exists, Press Update instead",
                                Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } catch (RealmException e) {
            Toast.makeText(getApplicationContext(), "Exception : " + e, Toast.LENGTH_SHORT).show();
        }
        /*finally {

            if (realm != null) {
                realm.close();
            }
        }*/
    }


    /*@Override
    protected void onPause() {

        super.onPause();
//        Toast.makeText(getApplicationContext(), "onPause called 1", Toast.LENGTH_LONG).show();

        Realm realm = null;
        try {
             config = new RealmConfiguration.Builder()
                .name(AppConstants.GAME_ID + ".realm")
                .deleteRealmIfMigrationNeeded()
                .build();
        realm = Realm.getInstance(config);
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {

                    try {

                        Match match = realm.where(Match.class).
                                equalTo("matchid", matchid).findFirst();
                        match.setAddPlayers(false);
                        match.setCaptain(false);
                        match.setToss(false);
                        match.setOpeners(false);
                        match.setScoring(false);
                        match.setScoreCard(false);
                        match.setEndOfInnings(false);
                        match.setPulledMatch(true);
                        match.setSelectAXI(false);
                        match.setSelectBXI(false);
                        realm.copyToRealmOrUpdate(match);
                    } catch (RealmPrimaryKeyConstraintException e) {
                        Toast.makeText(getApplicationContext(),
                                "Primary Key exists, Press Update instead", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

        catch (RealmException e) {
            Log.d("toss", "onclick, Exception : " + e);
        }

        finally {
            if (realm != null) {
                realm.close();
            }
        }
    }
*/

    @Override
    public void onBackPressed() {

        startActivity(new Intent(this, HomeActivity.class));
        finish();

    }


    public void displayProgress() {

        progress = new ProgressDialog(this);
        progress.setMessage("Please wait...");
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setIndeterminate(true);
        progress.setProgress(0);
        progress.show();
    }


    private void deleteFromList() {


    }


    String checkID(String matchID) {

        Realm realm;
        realm = Realm.getDefaultInstance();
        Match match_test = realm.where(Match.class).equalTo("matchID", matchID).findFirst();
        if (match_test != null)
            return checkID(random.genId());
        else
            return matchID;
    }

    String checkIDName(String matchID) {

        config = new RealmConfiguration.Builder()
                .name(AppConstants.GAME_ID + ".realm")
                .deleteRealmIfMigrationNeeded()
                .build();
        realm = Realm.getInstance(config);
        Match match_test = realm.where(Match.class).equalTo("matchID", matchID).findFirst();
        if (match_test != null)
            return checkIDName(random.genId());
        else
            return matchID;
    }


    // added on 06/06/2020
    public void saveDetails() {
        // save toss winner and decision on Match table
        Realm realm = null;
        try {
            config = new RealmConfiguration.Builder()
                    .name(AppConstants.GAME_ID + ".realm")
                    .deleteRealmIfMigrationNeeded()
                    .build();
            realm = Realm.getInstance(config);
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {

                    try {

                        Match match = realm.where(Match.class).
                                equalTo("matchid", matchid).findFirst();
                        match.setAddPlayers(false);
                        match.setCaptain(false);
                        match.setToss(false);
                        match.setOpeners(false);
                        match.setScoring(false);
                        match.setScoreCard(false);
                        match.setEndOfInnings(false);
                        match.setPulledMatch(true);
                        match.setSelectAXI(false);
                        match.setSelectBXI(false);
                        realm.copyToRealmOrUpdate(match);
                    } catch (RealmPrimaryKeyConstraintException e) {
                        Toast.makeText(getApplicationContext(),
                                "Primary Key exists, Press Update instead", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } catch (RealmException e) {
            Log.d("toss", "onclick, Exception : " + e);
        } finally {
            if (realm != null) {
                realm.close();
            }
        }
        // set sync true fro both match and players
        // set scoring as true , if the last ball type not equal to 9 or 10 or 11
        // set scorecard as true if ball type =9 or 10 or 11

    }


    private void postOfficialDetails() {

        config = new RealmConfiguration.Builder()
                .name(AppConstants.GAME_ID + ".realm")
                .deleteRealmIfMigrationNeeded()
                .build();
        realm = Realm.getInstance(config);
        if (isNetworkAvailable()) {

            RealmResults<MatchOfficials> results = realm.where(MatchOfficials.class).
                    equalTo("matchid", matchid).
                    equalTo("sync", 0).findAll();

            Log.d("ADD_OFFICIALS", "Scoring, results 1 : " + results);
            if (results.isEmpty()) {

                Log.d("ADD_OFFICIALS", "Scoring, results : " + results);
            } else {

                JSONArray arrayOfficials = new JSONArray();

                Log.d("ADD_OFFICIALS", "Scoring, matchID : " + matchID);

                for (MatchOfficials officials : results) {

                    JSONObject jsonOfficials = new JSONObject();

                    try {
                        if (!officials.getOfficialName().matches("")) {
                            jsonOfficials.put("name", officials.getOfficialName());

                            if (officials.getStatus().matches("u1") || officials.getStatus().matches("u2"))
                                jsonOfficials.put("type", "u");
                            else
                                jsonOfficials.put("type", officials.getStatus());

                            if (officials.getD4s_id() == 0)
                                jsonOfficials.put("d4s_playerid", 0);
                            else
                                jsonOfficials.put("d4s_playerid", officials.getD4s_id());

                            arrayOfficials.put(jsonOfficials);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("matchID", matchID);
                    jsonObject.put("officials", arrayOfficials);
                } catch (JSONException e) {
                    e.printStackTrace();
                }


                JSONObject jsonfeed = new JSONObject();

                try {
                    jsonfeed.put("AddMatchOfficials", jsonObject);
                } catch (JSONException e) {
                    e.printStackTrace();
                }


                JSONObject postparams = new JSONObject();
                try {
                    postparams.put("title", "CHASE_POST");
                    postparams.put("feed", jsonfeed);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                        Constants.CHASE_CRICKET_MATCH_API, postparams,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {

                                try {

                                    Log.d("ADD_OFFICIALS", "Scoring, response : " + response);
                                    // added on 14/09/2020
                                    if (!response.getBoolean("error") && response.getInt("status") == 200) {

                                        JSONObject jsonMatch = response.getJSONObject("match");

                                        RealmResults<MatchOfficials> results1 = realm.where(MatchOfficials.class).
                                                equalTo("matchid", matchid).
                                                equalTo("sync", 0).findAll();

                                        try {
                                            config = new RealmConfiguration.Builder()
                                                    .name(AppConstants.GAME_ID + ".realm")
                                                    .deleteRealmIfMigrationNeeded()
                                                    .build();
                                            realm = Realm.getInstance(config);
                                            realm.executeTransaction(new Realm.Transaction() {
                                                @Override
                                                public void execute(Realm bgRealm) {

                                                    try {

                                                        RealmResults<MatchOfficials> result_officials1 = bgRealm.where(MatchOfficials.class).
                                                                equalTo("matchID",
                                                                        jsonMatch.getString("app_matchID")).
                                                                equalTo("sync", 0).findAll();

                                                        if (result_officials1.size() > 0) {
                                                            for (MatchOfficials officials : result_officials1) {
                                                                officials.setSync(1);
                                                                bgRealm.copyToRealm(officials);
                                                            }
                                                        }

                                                        //Added on 02/08/2021
                                                        Match match1 = bgRealm.where(Match.class).
                                                                equalTo("matchID",
                                                                        jsonMatch.getString("app_matchID")).
                                                                findFirst();
                                                        if (match1 != null) {
                                                            match1.setAddPOfficials(1);
                                                            bgRealm.copyToRealm(match1);
                                                        }

                                                    } catch (JSONException e) {
                                                        e.printStackTrace();
                                                    } catch (RealmPrimaryKeyConstraintException e) {
                                                        Toast.makeText(getApplicationContext(),
                                                                "Primary Key exists, Press Update instead",
                                                                Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            });
                                        } catch (RealmException e) {
                                            Log.d("test", "Exception : " + e);
                                        } finally {
                                            if (realm != null) {
                                                realm.close();
                                            }
                                        }
                                    } else {
                                        Toast.makeText(getApplicationContext(),
                                                response.getString("message"), Toast.LENGTH_SHORT).show();
                                    }

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {

//
//                                  Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                                Log.d("ADD_OFFICIALS", "Scoring, Error Message is  : " + error);

                            }
                        });

                MyApplicationClass.getInstance(getApplicationContext()).
                        addToRequestQueue(jsonObjReq, "postRequest");
                Log.d("ADD_OFFICIALS", "Scoring, jsonObjReq  : " + jsonObjReq);
                Log.d("ADD_OFFICIALS", "Scoring, postparams  : " + postparams);

            }
        }
    }


    // Added on 11/12/2021
    private void viewObject(int position) {

        Log.d("viewObject", "position = " + position);

        if (isNetworkAvailable()) {

            StringRequest stringRequest = new StringRequest(Request.Method.GET,
                    Constants.CHASE_CRICKET_PULL_MATCH_API_TEST + d4s_userid,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            try {

                                Log.d("response", "AssignedMatchList, " + response);

                                // based on gib=ven dummy values
                                JSONArray array = new JSONArray(response);
                                Log.d("array", "AssignedMatchList, " + array);


                                if (array.length() > 0) {

                                    for (int i = 0; i < array.length(); i++) {
                                        //getting the json object of the particular index inside the array

                                        if (i == position) {

                                            JSONObject matchObject = array.getJSONObject(i);
                                            displayDetails(matchObject);
                                            break;
                                        }
                                    }
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            //displaying the error in toast if occurrs
                            Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });


            //creating a request queue
            RequestQueue requestQueue = Volley.newRequestQueue(this);

            //adding the string request to request queue
            requestQueue.add(stringRequest);

            Log.d("AMLA", "pulled match, stringRequest  : " + stringRequest);

        } else {

            AlertDialog alertDialog = new AlertDialog.Builder(PulledMatchDetailsActivity.this).create();
            alertDialog.setIcon(R.drawable.ball);
            alertDialog.setCancelable(false);
            alertDialog.setTitle("Network Error");
            alertDialog.setMessage("Please check your INTERNET connection");
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            alertDialog.show();

        }
    }


    private void displayDetails(JSONObject jsonMatch) {

        try {
            gameid = Integer.parseInt(jsonMatch.getString("gameid"));
            AppConstants.GAME_ID = gameid;
//            mid_scoring = jsonMatch.getBoolean("midscoring");
            teamA = jsonMatch.getString("teamA");
            teamAId = Integer.parseInt(jsonMatch.getString("teamA_id"));
            Log.d("teamAId", "oncreate, : " + teamAId);
            teamB = jsonMatch.getString("teamB");
            teamBId = Integer.parseInt(jsonMatch.getString("teamB_id"));
            event = jsonMatch.getString("event");
            eventId = Integer.parseInt(jsonMatch.getString("event_id"));
            phase = jsonMatch.getString("phase");
            venue = jsonMatch.getString("venue");
            venueId = Integer.parseInt(jsonMatch.getString("venue_id"));
            if (jsonMatch.has("end1"))
                end1 = jsonMatch.getString("end1");
            else
                end1 = null;
            if (jsonMatch.has("end2"))
                end2 = jsonMatch.getString("end2");
            else
                end2 = null;
//            URLEncoder.encode("-", "UTF8");
//            URLEncoder.encode("\t", "UTF8");
            matchDate = jsonMatch.getString("date");
            Log.d("matchObject", "viewDetails : " + jsonMatch);
            Log.d("date", "viewDetails : " + jsonMatch.getString("date"));
//            final StringBuilder builder = matchObject.ge("date");
            matchType = jsonMatch.getString("match_type");
            // Added on 08/12/2021
            if ((jsonMatch.getString("teamAplayers") != null) &&
                    (jsonMatch.getString("teamBplayers") != null)) {
                playerA = Integer.parseInt(jsonMatch.getString("teamAplayers"));
                playerB = Integer.parseInt(jsonMatch.getString("teamBplayers"));
            } else
                Toast.makeText(getApplicationContext(),
                        "Invalid player count", Toast.LENGTH_SHORT).show();
            if (jsonMatch.getString("scheduled_overs") == null)
                over = 0;
            else
                over = Integer.parseInt(jsonMatch.getString("scheduled_overs"));
            balls = Integer.parseInt(jsonMatch.getString("balls_per_over"));
            Log.d("PMD", "max_overs_per_bowler = " + jsonMatch.getString("max_overs_per_bowler"));
            if (jsonMatch.getString("max_overs_per_bowler") == null)
                max_opb = 0;
            else if (jsonMatch.getString("max_overs_per_bowler").matches("null"))
                max_opb = 0;
            else
                max_opb = Integer.parseInt(jsonMatch.getString("max_overs_per_bowler"));
            innings = jsonMatch.getString("innings");

            if (over > 0)
                limited_overs = true;

            if (innings.matches("multi") && (over == 0) && (max_opb == 0)) {
                over = 1000;
                // if so remove the visibility of overs and max overs/bowler
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.d("PMDA", "gameid = " + gameid);
        Log.d("PMDA", "teamA = " + teamA);
        Log.d("PMDA", "teamB = " + teamB);
        Log.d("PMDA", "event = " + event);
        Log.d("PMDA", "phase = " + phase);
        Log.d("PMDA", "venue = " + venue);
        Log.d("PMDA", "matchDate = " + matchDate);
        Log.d("PMDA", "matchType = " + matchType);
        Log.d("PMDA", "over = " + over);
        Log.d("PMDA", "balls = " + balls);
        Log.d("PMDA", "max_opb = " + max_opb);
        Log.d("PMDA", "playerA = " + playerA);
        Log.d("PMDA", "playerB = " + playerB);

        tv_gameid.setText(String.valueOf(gameid));
        tv_teamA.setText(teamA);
        tv_teamB.setText(teamB);
        tv_event.setText(event);
        tv_phase.setText(phase);
        tv_venue.setText(venue);
        tv_matchDate.setText(matchDate);
        tv_matchType.setText(matchType);

        if (over == 1000)
            tv_over.setText("");
        else
            tv_over.setText(String.valueOf(over));
        tv_bpo.setText(String.valueOf(balls));
        tv_opb.setText(String.valueOf(max_opb));
        tv_playersA.setText(String.valueOf(playerA));
        tv_playersB.setText(String.valueOf(playerB));

        progress.dismiss();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.refresh_pulled, menu);//Menu Resource, Menu
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.refresh_pulled:
                displayProgress();
                viewObject(position);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }

    }


    // Added on 14/12/2021
    private void postSquads() {

        Match match = realm.where(Match.class)
                .equalTo("matchid", matchid)
                .findFirst();

        if ((match.getTeamAId() != 0) && (match.getTeamAId() != 0)) {
            Player player = realm.where(Player.class)
                    .equalTo("matchid", matchid)
                    .equalTo("team", 1)
//                    .equalTo("pulled", 0)
                    .findFirst();
            if (player != null)// && (player.getD4s_playerid() == 0) )
                postPlayers(1, match.getMatchID(), match.getTeamAId());

            player = realm.where(Player.class)
                    .equalTo("matchid", matchid)
                    .equalTo("team", 2)
//                    .equalTo("pulled", 0)
                    .findFirst();
            if (player != null)// && (player.getD4s_playerid() == 0) )
                postPlayers(2, match.getMatchID(), match.getTeamBId());
        }
    }

    void postPlayers(int team, String matchID, int d4s_team_id) {

        config = new RealmConfiguration.Builder()
                .name(AppConstants.GAME_ID + ".realm")
                .deleteRealmIfMigrationNeeded()
                .build();
        realm = Realm.getInstance(config);
        if (isNetworkAvailable()) {
            JSONArray array = new JSONArray();
            RealmResults<Player> results = realm.where(Player.class)
                    .equalTo("matchid", matchid)
                    .equalTo("team", team)
                    .findAll();
            if (results.size() > 0) {

                for (Player player : results) {

                    JSONObject object = new JSONObject();
                    try {
                        object.put("name", player.getPlayerName());
                        object.put("player_id", player.getPlayerID());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    array.put(object);
                }
            }

            JSONObject object1 = new JSONObject();
            try {
                object1.put("TeamID", d4s_team_id);
                object1.put("Players", array);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            JSONObject object2 = new JSONObject();
            try {
                object2.put("matchID", matchID);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            JSONArray array2 = new JSONArray();
            array2.put(object2);
            array2.put(object1);

            JSONObject jsonfeed = new JSONObject();
            try {
                jsonfeed.put("PlayersAdd", array2);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            JSONObject postparams = new JSONObject();
            try {
                postparams.put("title", "CHASE_POST");
                postparams.put("feed", jsonfeed);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                    Constants.CHASE_CRICKET_MATCH_API, postparams,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Log.d("APA", "response = " + response);
                            try {
                                JSONArray array = response.getJSONArray("playerdet");
                                for (int i = 0; i < array.length(); i++) {
                                    syncPlayer(array.getJSONObject(i));
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.d("APA", "VolleyError = " + error);
                        }
                    });

            MyApplicationClass.getInstance(getApplicationContext()).
                    addToRequestQueue(jsonObjReq, "postRequest");
            Log.d("APA", "jsonObjReq = " + jsonObjReq);
            Log.d("APA", "postparams = " + postparams);
        }
    }


    private void syncPlayer(JSONObject object) {

        Realm realm = null;
        try {
            config = new RealmConfiguration.Builder()
                    .name(AppConstants.GAME_ID + ".realm")
                    .deleteRealmIfMigrationNeeded()
                    .build();
            realm = Realm.getInstance(config);
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {

                    try {

                        try {
                            Player player = realm.where(Player.class)
                                    .equalTo("matchID", object.getString("matchid"))
                                    .equalTo("playerID", object.getInt("app_player_id"))
                                    .findFirst();

                            if (player != null) {
                                player.setD4s_playerid(object.getInt("d4splayerid"));
                                realm.copyToRealmOrUpdate(player);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    } catch (RealmPrimaryKeyConstraintException e) {
                        Toast.makeText(getApplicationContext(), "Primary Key exists, Press Update instead",
                                Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } catch (RealmException e) {
            Log.d("toss", "onclick, Exception : " + e);
        } finally {
            if (realm != null) {
                realm.close();
            }
        }
    }

}
