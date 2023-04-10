package com.data4sports.chasecricket.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.data4sports.chasecricket.R;
import com.data4sports.chasecricket.applicationConstants.AppConstants;
import com.data4sports.chasecricket.models.Batsman;
import com.data4sports.chasecricket.models.Bowler;
import com.data4sports.chasecricket.models.Constants;
import com.data4sports.chasecricket.models.Events;
import com.data4sports.chasecricket.models.ExtraCard;
import com.data4sports.chasecricket.models.FOW;
import com.data4sports.chasecricket.models.Match;
import com.data4sports.chasecricket.models.MyApplicationClass;
import com.data4sports.chasecricket.models.Player;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;
import io.realm.Sort;
import io.realm.exceptions.RealmException;
import io.realm.exceptions.RealmPrimaryKeyConstraintException;

/**
 * Updated on 16/06/2021
 */
public class ScoreCardActivity extends AppCompatActivity {

    TextView tv_teamA, tv_teamB, tv_type, tv_batTeam, tv_fieldTeam, tv_run,tv_div, tv_wicket, tv_over,
            tv_innings, tv_braces_open, tv_braces_closed, tv_message;
    TableLayout batting_table, fielding_table, extra_table, fow_table;
    LinearLayout ll_previous_innings, ll_end_of_match, ll_fieldingTeam;
    Button first, second, third, forth, enterResult, nextInnings /*so1, so2*/;
    HorizontalScrollView fow_hsv;
    Realm realm;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor mEditor;
    String matchID, teamA, teamB, battingTeam, fieldingTeam, type, matchResult = "", tosswinner, decision,
            rrmethod = "", otype, b_name, bowler = null, fielder = null, tBat, tField, winner = "",
            message = null, win_team_name = "", rain_rule = null, bowler_details;
    ArrayList<String> fielderIDList, fielderNameList;
    ArrayList<Batsman> batsmanList;
    int matchid, c_innings, battingTeamNo, fieldingTeamNo, tBatNo, tFieldNo, run, wicket, totalover,
            ot, rrid = 0, total_innings = 0, forfeit_team = 0, forfeit_innings = 0;
    boolean endOfInnings = false, endOfMatch = false, inningsnotstarted = false, wk = false, c = false,
            SUPER_OVER = false, declare = false, continueInnings = false, match_finished = false,
            followon = false, view_result = false, interval = false, session = false, wbi = false, wbr = false,
            wbw = false, wbir = false, wbiw = false, allout = false, overReduced = false, forfeit = false, concede = false, wk_flag = false;
    float over;
    int t = 0, b = 0, lb = 0, wd = 0, nb = 0, p = 0, matchStatus = 0, temp = 0, super_over_innings = 0,
            tp = 0, tpA = 0, tpB = 0, batting_table_count = 0, bowler_table_count = 0, fow_table_count = 0, back_type = -1,
            click = 0, win_runs = 0, win_wickets = 0, win_team = 0, checkedItem = -1, value = 0,
            noballRun = 0, wideRun = 0, penaltyRun = 0, conceded_team = 0, conceded_innings = 0,
            forceEndingType = 0, balls = 0, abandoned_match_flag = 0, abandoned_after_toss = 0,
            fid = 0, bid = 0, sub_flag = 0;


    ImageButton back;
    String[] result;

    // Added on 16/06/2021
    String title = "";
    int events_sync = 0, pos = -1;

    // Added on 28/07/2021
    public static boolean HUNDRED = false;


    RealmConfiguration config;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_score_card);

         config = new RealmConfiguration.Builder()
                .name(AppConstants.GAME_ID + ".realm")
                .deleteRealmIfMigrationNeeded()
                .build();
        realm = Realm.getInstance(config);

//        getFromSP();
        getFromIntent();
        saveToSP();
//        getFromScoring();

       /* Intent i = getIntent();
        matchid = i.getIntExtra("matchid",0);
        matchID = i.getStringExtra("matchID");
        endOfInnings = i.getBooleanExtra("endofinnings", false);
        continueInnings = i.getBooleanExtra("continue_match", false);
        declare = i.getBooleanExtra("declare", false);
        c_innings = i.getIntExtra("innings", 0);
//        matchStatus = i.getIntExtra("matchstatus", 0);
        endOfMatch = i.getBooleanExtra("endofmatch", false);
        inningsnotstarted = i.getBooleanExtra("inningsnotstarted", false);
        SUPER_OVER = i.getBooleanExtra("SUPER_OVER", false);

        match_finished = i.getBooleanExtra("match_finished", false);

        back_type = i.getIntExtra("back_type", -1);

        Log.d("ScoreCard", "onCreate, status :"+matchStatus);
        Log.d("ScoreCard", "onCreate, currentInnings :"+c_innings);*/



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

        // Added on 20/04/2021
        Intent intent= new Intent(getBaseContext(), ScheduledService.class);
        getBaseContext().startService(intent);


        Log.d("ScoreCard", "oncreate, matchid :"+matchid);
//        Log.d("display", " matchID :"+matchID);

        ll_previous_innings = findViewById(R.id.previousinnings);
        ll_end_of_match = findViewById(R.id.ll_endofmatch);
        tv_teamA = findViewById(R.id.txt_teamA_sc);
        tv_teamB = findViewById(R.id.txt_teamB_sc);
        tv_batTeam = findViewById(R.id.battingTeam);
        tv_fieldTeam = findViewById(R.id.fieldingTeam);
        tv_type = findViewById(R.id.txt_matchType_sc);
        tv_innings = findViewById(R.id.txt_innings_sc);
        tv_run = findViewById(R.id.txt_display_run);
        tv_div = findViewById(R.id.sc_div);
        tv_wicket = findViewById(R.id.txt_display_wicket);
        tv_over = findViewById(R.id.txt_display_over);
        tv_braces_open = findViewById(R.id.braces_open);
        tv_braces_closed = findViewById(R.id.braces_close);
        first = findViewById(R.id.first_innings);
        second = findViewById(R.id.second_innings);
        third = findViewById(R.id.third_innings);
        forth = findViewById(R.id.forth_innings);
        enterResult = findViewById(R.id.btn_enter_result);
        nextInnings = findViewById(R.id.btn_next_innings);
        ll_fieldingTeam = findViewById(R.id.ll_fieldingTeam);
        tv_message = findViewById(R.id.txt_message);
        tv_message.setVisibility(View.GONE);
        /*so1 = findViewById(R.id.so1_innings);
        so2 = findViewById(R.id.so2_innings);*/

        if (match_finished) {
            enterResult.setText("Back to Result");
        }
        else if (view_result){
            enterResult.setText("Display Result");
        }
        else {
            enterResult.setText("Enter Result");
        }

        fielderIDList = new ArrayList<>();
        fielderNameList = new ArrayList<>();
        batsmanList = new ArrayList<Batsman>();


        Match match = realm.where(Match.class).
                equalTo("matchid", matchid).findFirst();
        Log.d("Scorecard" , "oncreate, match : "+match);
        teamA = match.getTeamA();
        teamB = match.getTeamB();
        type = match.getMatchType();
        tosswinner = match.getToss_winner();
        decision = match.getDecision();
        totalover = match.getActual_over();
        matchStatus = match.getMatchStatus();
        total_innings = match.getTotalInnings();
//        tp = match.getPlayer();
        tpA = match.getPlayerA();
        tpB = match.getPlayerB();
        followon = match.isFollowon();
        SUPER_OVER = match.isSUPER_OVER();
        wideRun = match.getWiderun();
        noballRun = match.getNoballrun();
        penaltyRun = match.getPenaltyrun();
        overReduced = match.isReducedOver();
        HUNDRED = match.isHundred();    // Added on 28/07/2021
        abandoned_match_flag = match.getAbandoned_match_flag();    // Added on 02/09/2021
        abandoned_after_toss = match.getAbandoned_after_toss_match_flag();    // Added on 03/09/2021

        if (overReduced)
            rain_rule = match.getAppliedRainRule();

        if (SUPER_OVER && c_innings == 99)
            super_over_innings = 1;

        Log.d("Scorecard" , "oncreate, teamA : " + teamA + ", teamB : " + teamB + ", type : " + type +
                ", tosswinner : " + tosswinner + ", decision : " + decision + ", totalover : " + totalover +
                ", matchStatus : " + matchStatus + ", total_innings : " + total_innings + ", battingTeamNo : " +
                battingTeamNo + ", tpa : " + tpA + ", tpB : " + tpB +/* ", " +
                "match.getPlayerB() : " + match.getPlayerB() + */", followon : " + followon);

        click = 0;

        if (SUPER_OVER) {
            third.setText("SO 1");
            forth.setText("SO 2");
        }
        else {
            third.setText("3rd");
            forth.setText("4th");
        }



        if (c_innings % 2 != 0) {

            if (tosswinner.matches(teamA)) {
                if (decision.matches("Batting")) {
                    battingTeam = teamA;
                    fieldingTeam = teamB;
                    battingTeamNo = 1;
                    fieldingTeamNo = 2;
                } else {
                    battingTeam = teamB;
                    fieldingTeam = teamA;
                    battingTeamNo = 2;
                    fieldingTeamNo = 1;
                }
            } else {

                if (decision.matches("Batting")) {

                    battingTeam = teamB;
                    fieldingTeam = teamA;
                    battingTeamNo = 2;
                    fieldingTeamNo = 1;

                } else {

                    battingTeam = teamA;
                    fieldingTeam = teamB;
                    battingTeamNo = 1;
                    fieldingTeamNo = 2;
                }
            }
        }

        else {

                if (tosswinner.matches(teamA)) {
                    if (decision.matches("Batting")) {
                        battingTeam = teamB;
                        fieldingTeam = teamA;
                        battingTeamNo = 2;
                        fieldingTeamNo = 1;

                    } else {
                        battingTeam = teamA;
                        fieldingTeam = teamB;
                        battingTeamNo = 1;
                        fieldingTeamNo = 2;
                    }
                } else {

                    if (decision.matches("Batting")) {

                        battingTeam = teamA;
                        fieldingTeam = teamB;
                        battingTeamNo = 1;
                        fieldingTeamNo = 2;

                    } else {

                        battingTeam = teamB;
                        fieldingTeam = teamA;
                        battingTeamNo = 2;
                        fieldingTeamNo = 1;
                    }
                }
//            }
        }


        /* COMMNETE DON 01/02/2021
        if (battingTeamNo == 1)
            tp = match.getPlayerA();
        else if (battingTeamNo == 2)
            tp = match.getPlayerB();*/

        Log.d("ScoreCard", "oncreate, batting team :"+battingTeam);
        Log.d("ScoreCard", "oncreate,tpA :"+tpA);
        Log.d("ScoreCard", "oncreate,tpB :"+tpB);
//        Log.d("ScoreCard", "oncreate, match.getPlayerA() :"+match.getPlayerA());
//        Log.d("ScoreCard", "oncreate, match.getPlayerB() :"+match.getPlayerB());
        Log.d("ScoreCard", "oncreate, fielding team :"+fieldingTeam);
        Log.d("ScoreCard", "oncreate, innings :"+c_innings);
        Log.d("ScoreCard", "oncreate, batting team No :"+battingTeamNo);
        Log.d("ScoreCard", "oncreate, fielding team No :"+fieldingTeamNo);
        Log.d("ScoreCard", "oncreate, tosswinner :"+tosswinner);
        Log.d("ScoreCard", "oncreate, decision :"+decision);
        Log.d("ScoreCard", "oncreate, teamA :"+teamA);
        Log.d("ScoreCard", "oncreate, teamB :"+teamB);
        Log.d("ScoreCard", "oncreate, endOfInnings :"+endOfInnings);


        tv_teamA.setText(teamA);
        tv_teamB.setText(teamB);
        /*if (abandoned_match_flag == 1)
            setInnings(0);
        else*/

        setInnings(c_innings);
        checkMatchType(type);

        /*if (abandoned_match_flag == 1)
            displayScoreCard(matchid, 0);
        else*/
        displayScoreCard(matchid, c_innings);

        // added on 06/09/2021
        if (abandoned_after_toss == 1)
            displayPlayers(1);

        if (c_innings == 1 && matchStatus == 1) {
            ll_end_of_match.setVisibility(View.GONE);
//            nextInnings.setVisibility(View.GONE);
        }

        if (endOfInnings && !endOfMatch) {//c_innings != total_innings){

            continueInnings = true;

            ll_end_of_match.setVisibility(View.VISIBLE);
            nextInnings.setVisibility(View.VISIBLE);
            enterResult.setVisibility(View.GONE);
        }



        if (matchStatus == 1 &&  (/*(c_innings == total_innings) || */endOfMatch)){     // updated on 11/09/20

            ll_end_of_match.setVisibility(View.VISIBLE);
            nextInnings.setVisibility(View.GONE);
            enterResult.setVisibility(View.VISIBLE);
            ll_previous_innings.setVisibility(View.VISIBLE);
        }

//        if (c_innings > 1) {  // commented on 24/09/2020


            if (c_innings == 2) {


                if (checkInnings(match.getMatchType(), match.getInnings())) {

                    if (SUPER_OVER) {

                        nextInnings.setText("START SUPER OVER");
//                        first.setVisibility(View.VISIBLE);
//                        second.setVisibility(View.VISIBLE);
//                        third.setVisibility(View.INVISIBLE);
//                        forth.setVisibility(View.INVISIBLE);
                    }
                }

//                else {
                ll_previous_innings.setVisibility(View.VISIBLE);
                first.setVisibility(View.VISIBLE);
                second.setVisibility(View.VISIBLE);
                third.setVisibility(View.INVISIBLE);
                forth.setVisibility(View.INVISIBLE);
                    /*so1.setVisibility(View.INVISIBLE);
                    so2.setVisibility(View.INVISIBLE);*/
//                }
            }

            else if (c_innings == 3) {

                first.setVisibility(View.VISIBLE);
                second.setVisibility(View.VISIBLE);
                if (total_innings == 4) {
                    third.setVisibility(View.VISIBLE);
                    /*so1.setVisibility(View.INVISIBLE);
                    so2.setVisibility(View.INVISIBLE);*/
                } else {
                    third.setVisibility(View.INVISIBLE);
                    /*so1.setVisibility(View.INVISIBLE);
                    so2.setVisibility(View.INVISIBLE);*/
                }
                forth.setVisibility(View.INVISIBLE);
                ll_previous_innings.setVisibility(View.VISIBLE);
            }

            else if (c_innings == 4) {

                first.setVisibility(View.VISIBLE);
                second.setVisibility(View.VISIBLE);
                third.setVisibility(View.VISIBLE);
                forth.setVisibility(View.VISIBLE);
                ll_previous_innings.setVisibility(View.VISIBLE);
                /*so1.setVisibility(View.INVISIBLE);
                so2.setVisibility(View.INVISIBLE);*/
            }

            else if (c_innings == 99 || c_innings == 100) {

                first.setVisibility(View.VISIBLE);
                second.setVisibility(View.VISIBLE);
                third.setVisibility(View.INVISIBLE);
                forth.setVisibility(View.INVISIBLE);
                ll_previous_innings.setVisibility(View.VISIBLE);

                if (c_innings == 99)
                    third.setVisibility(View.VISIBLE);
                else if (c_innings == 100) {
                    third.setVisibility(View.VISIBLE);
                    forth.setVisibility(View.VISIBLE);
                }

//            } // commented on 24/09/2020
//        }

            /*if (matchStatus == 1 && c_innings == total_innings){

                first.setVisibility(View.VISIBLE);
                second.setVisibility(View.VISIBLE);
                if (total_innings == 4) {
                    third.setVisibility(View.VISIBLE);
                    forth.setVisibility(View.VISIBLE);
                    *//*so1.setVisibility(View.INVISIBLE);
                    so2.setVisibility(View.INVISIBLE);*//*
                    nextInnings.setVisibility(View.GONE);
                    ll_end_of_match.setVisibility(View.VISIBLE);
                }

                else {
                    third.setVisibility(View.INVISIBLE);
                    forth.setVisibility(View.INVISIBLE);
                    *//*so1.setVisibility(View.GONE);
                    so2.setVisibility(View.GONE);*//*
                    nextInnings.setVisibility(View.GONE);
                    ll_end_of_match.setVisibility(View.VISIBLE);
                }
            }*/

            }   // updated on 11/09/2020

            if (matchStatus == 1) {

                Log.d("NEW", "oncreate, c_innings : " + c_innings);
                Log.d("NEW", "oncreate, matchStatus : " + matchStatus);

                ll_previous_innings.setVisibility(View.VISIBLE);

                if (/*c_innings == 0 || c_innings == 1 ||*/ c_innings == 2) {   // updated on 17/10/2020

                    Log.d("NEW", "oncreate 1, 1st condition");

                    first.setVisibility(View.VISIBLE);
                    second.setVisibility(View.VISIBLE);
                    third.setVisibility(View.INVISIBLE);
                    forth.setVisibility(View.INVISIBLE);
                    nextInnings.setVisibility(View.GONE);
                    ll_end_of_match.setVisibility(View.VISIBLE);

                    // if c_innings == 0 ot 1
                    // create new method to display all players
                }


                else if (c_innings == 3) {

                    first.setVisibility(View.VISIBLE);
                    second.setVisibility(View.VISIBLE);
                    third.setVisibility(View.VISIBLE);
                    forth.setVisibility(View.INVISIBLE);
                    nextInnings.setVisibility(View.GONE);
                    ll_end_of_match.setVisibility(View.VISIBLE);
                }

                else if (total_innings == 4) {

                        third.setVisibility(View.VISIBLE);
                        forth.setVisibility(View.VISIBLE);
                        /*so1.setVisibility(View.INVISIBLE);
                        so2.setVisibility(View.INVISIBLE);*/
                        nextInnings.setVisibility(View.GONE);
                        ll_end_of_match.setVisibility(View.VISIBLE);
                }

                else if (total_innings == 2 && SUPER_OVER) {

                    third.setVisibility(View.VISIBLE);
                    forth.setVisibility(View.VISIBLE);
                        /*so1.setVisibility(View.INVISIBLE);
                        so2.setVisibility(View.INVISIBLE);*/
                    nextInnings.setVisibility(View.GONE);
                    ll_end_of_match.setVisibility(View.VISIBLE);
                }

                //added on 20/10/2020
                else if (c_innings == 0 || c_innings == 1) {

                    if (c_innings == 0)
                        first.setText(teamA);
                    second.setText(teamB);

                    first.setVisibility(View.VISIBLE);
                    second.setVisibility(View.VISIBLE);
                    third.setVisibility(View.INVISIBLE);
                    forth.setVisibility(View.INVISIBLE);
                    nextInnings.setVisibility(View.GONE);
                    ll_end_of_match.setVisibility(View.VISIBLE);
                }

                /*commented on 20/10/2020
                // added on 11/09/2020
                else if (c_innings <= 1 && matchStatus == 1) {
                    Log.d("NEW", "oncreate 2, inside new condition");

                }*/

            }

//        }
        Log.d("ScoreCard", "oncreate, innings :"+c_innings);

        first.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ++click;
                // added on 20/10/2020
                Log.d("ScoreCard", "oncreate 1, innings :"+c_innings);
                Log.d("ScoreCard", "oncreate 1, matchStatus :"+matchStatus);
                if ((matchStatus == 1 && c_innings == 0 ) || abandoned_after_toss == 1)
                    displayPlayers(1);  // === till here
                else
                    displayScoreCard(matchid, 1);
            }
        });

        second.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ++click;
                // added on 20/10/2020
                Log.d("ScoreCard", "oncreate 1, innings :"+c_innings);
                Log.d("ScoreCard", "oncreate 1, matchStatus :"+matchStatus);
                if ((matchStatus == 1 && (c_innings == 0 || c_innings == 1)) || abandoned_after_toss == 1)
                    displayPlayers(2);  // === till here
                else
                    displayScoreCard(matchid, 2);
            }
        });

        third.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ++click;
                if (SUPER_OVER)
                    displayScoreCard(matchid, 99);
                else
                    displayScoreCard(matchid, 3);
            }
        });


        forth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ++click;
                if (SUPER_OVER)
                    displayScoreCard(matchid, 100);
                else
                    displayScoreCard(matchid, 4);
            }
        });

        enterResult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent= new Intent(getBaseContext(), ScheduledService.class);
                getBaseContext().startService(intent);

                if (match_finished) {

                    Intent i = new Intent(ScoreCardActivity.this, DisplayResultActivity.class);
                    i.putExtra("matchid", matchid);
                    i.putExtra("matchID", matchID);
                    startActivity(i);
                    freeMemory();
                    finish();
                }
                else {
                    readResult();
//                    readWinnerTeam();
                }
            }
        });

        nextInnings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.d("ScoreCard", "nextInnings1, batting team :"+battingTeam);
                Log.d("ScoreCard", "nextInnings1, fielding team :"+fieldingTeam);
                Log.d("c_innings", ""+c_innings);
                Log.d("ScoreCard", "nextInnings1, batting team No :"+battingTeamNo);
                Log.d("ScoreCard", "nextInnings1, fielding team No :"+fieldingTeamNo);
                Log.d("ScoreCard", "nextInnings1, super_over_innings :"+super_over_innings);

                if (!SUPER_OVER){

                    saveToSP();
                }


                if (total_innings == 4 ) {

                    if (c_innings == 2)
                        callFollowOn();

                    else if (c_innings == 1 || (c_innings == 3 && !followon)) {

                        Log.d("nextInnings", "nextInnings2, batting team :"+battingTeam);
                        Log.d("nextInnings", "nextInnings2, fielding team :"+fieldingTeam);
                        Log.d("nextInnings", "nextInnings2, innings :"+c_innings);
                        Log.d("nextInnings", "nextInnings2, batting team No :"+battingTeamNo);
                        Log.d("nextInnings", "nextInnings2, fielding team No :"+fieldingTeamNo);

                        Log.d("nextInnings", "for nextInnings3, batting team :"+fieldingTeam);
                        Log.d("nextInnings", "for nextInnings3, fielding team :"+battingTeam);
                        Log.d("nextInnings", "for nextInnings3, innings :"+c_innings);
                        Log.d("nextInnings", "for nextInnings3, batting team No :"+fieldingTeamNo);
                        Log.d("nextInnings", "for nextInnings3, fielding team No :"+battingTeamNo);

                        Intent intent = new Intent(ScoreCardActivity.this, OpenersActivity.class);
//                      intent.putExtra("SUPER_OVER", SUPER_OVER);
                        intent.putExtra("batting_team_no", fieldingTeamNo);
                        intent.putExtra("batting_team", fieldingTeam);
                        intent.putExtra("fielding_team_no", battingTeamNo);
                        intent.putExtra("fielding_team", battingTeam);
                        intent.putExtra("super_over_innings", super_over_innings);
                        intent.putExtra("continue_innings", continueInnings);
                        startActivity(intent);
                    }

                    else if (c_innings == 3 && followon) {

                        Log.d("nextInnings", "nextInnings5, batting team :"+battingTeam);
                        Log.d("nextInnings", "nextInnings5, fielding team :"+fieldingTeam);
                        Log.d("nextInnings", "nextInnings5, innings :"+c_innings);
                        Log.d("nextInnings", "nextInnings5, batting team No :"+battingTeamNo);
                        Log.d("nextInnings", "nextInnings5, fielding team No :"+fieldingTeamNo);

                        Intent intent = new Intent(ScoreCardActivity.this, OpenersActivity.class);
//                      intent.putExtra("SUPER_OVER", SUPER_OVER);
                        intent.putExtra("batting_team_no", battingTeamNo);
                        intent.putExtra("batting_team", battingTeam);
                        intent.putExtra("fielding_team_no", fieldingTeamNo);
                        intent.putExtra("fielding_team", fieldingTeam);
                        intent.putExtra("super_over_innings", super_over_innings);
                        intent.putExtra("continue_innings", continueInnings);
                        startActivity(intent);
                    }
                }

                else {

                    Log.d("ScoreCard", "nextInnings4, batting team : "+battingTeam);
                    Log.d("ScoreCard", "nextInnings4, fielding team : "+fieldingTeam);
                    Log.d("ScoreCard", "nextInnings4, innings : "+c_innings);
                    Log.d("ScoreCard", "nextInnings4, batting team No : "+battingTeamNo);
                    Log.d("ScoreCard", "nextInnings4, fielding team No : "+fieldingTeamNo);
                    Log.d("ScoreCard", "nextInnings4, super_over_innings : "+super_over_innings);
                    Log.d("ScoreCard", "nextInnings4, continueInnings : "+continueInnings);



                    if (total_innings == 2 && SUPER_OVER && (c_innings == total_innings || c_innings == 99)) {

                        Log.d("ScoreCard", "nextInnings7, batting team : "+battingTeam);
                        Log.d("ScoreCard", "nextInnings7, fielding team : "+fieldingTeam);
                        Log.d("ScoreCard", "nextInnings7, innings : "+c_innings);
                        Log.d("ScoreCard", "nextInnings7, batting team No : "+battingTeamNo);
                        Log.d("ScoreCard", "nextInnings7, fielding team No : "+fieldingTeamNo);
                        Log.d("ScoreCard", "nextInnings7, super_over_innings : "+super_over_innings);
                        Log.d("ScoreCard", "nextInnings7, continueInnings : "+continueInnings);

                        Intent intent = new Intent(ScoreCardActivity.this, OpenersActivity.class);
                        intent.putExtra("batting_team_no", battingTeamNo);
                        intent.putExtra("batting_team", battingTeam);
                        intent.putExtra("fielding_team_no", fieldingTeamNo);
                        intent.putExtra("fielding_team", fieldingTeam);
                        intent.putExtra("super_over_innings", super_over_innings);
                        intent.putExtra("continue_innings", continueInnings);
                        startActivity(intent);
                    }

/*
                    else if (SUPER_OVER && c_innings == 99) {

                        Log.d("ScoreCard", "nextInnings6, batting team :"+fieldingTeam);
                        Log.d("ScoreCard", "nextInnings6, fielding team :"+battingTeam);
                        Log.d("ScoreCard", "nextInnings6, innings :"+c_innings);
                        Log.d("ScoreCard", "nextInnings6, batting team No :"+fieldingTeamNo);
                        Log.d("ScoreCard", "nextInnings6, fielding team No :"+battingTeamNo);
                        Log.d("ScoreCard", "nextInnings6, super_over_innings :"+super_over_innings);

                        Intent intent = new Intent(ScoreCardActivity.this, OpenersActivity.class);
                        intent.putExtra("batting_team_no", fieldingTeamNo);
                        intent.putExtra("batting_team", fieldingTeam);
                        intent.putExtra("fielding_team_no", battingTeamNo);
                        intent.putExtra("fielding_team", battingTeam);
                        intent.putExtra("super_over_innings", super_over_innings);
                        intent.putExtra("continue_innings", continueInnings);
                        startActivity(intent);
                    }
*/

                    else {

                        Log.d("ScoreCard", "nextInnings8, batting team :"+fieldingTeam);
                        Log.d("ScoreCard", "nextInnings8, fielding team :"+battingTeam);
                        Log.d("ScoreCard", "nextInnings8, innings :"+c_innings);
                        Log.d("ScoreCard", "nextInnings8, batting team No :"+fieldingTeamNo);
                        Log.d("ScoreCard", "nextInnings8, fielding team No :"+battingTeamNo);
                        Log.d("ScoreCard", "nextInnings8 , super_over_innings :"+super_over_innings);

                        Intent intent = new Intent(ScoreCardActivity.this, OpenersActivity.class);
                        intent.putExtra("SUPER_OVER", SUPER_OVER);
                        intent.putExtra("batting_team_no", fieldingTeamNo);
                        intent.putExtra("batting_team", fieldingTeam);
                        intent.putExtra("fielding_team_no", battingTeamNo);
                        intent.putExtra("fielding_team", battingTeam);
                        startActivity(intent);
                    }
                }




//                Intent i = new Intent(ScoreCardActivity.this, OpenersActivity.class);
//                i.putExtra("matchid", matchid);
//                i.putExtra("endofinnings", endOfInnings);
//                i.putExtra("innings", currentInnings);
//                i.putExtra("matchstatus", status);
//                startActivity(i);
            }
        });
//        displayScoreCard(matchid, innings);


    }



    void callFollowOn(){

        Log.d("ScoreCard", "callFollowOn, batting team :"+battingTeam);
        Log.d("ScoreCard", "callFollowOn, fielding team :"+fieldingTeam);
        Log.d("ScoreCard", "callFollowOn, innings :"+c_innings);
        Log.d("ScoreCard", "callFollowOn, batting team No :"+battingTeamNo);
        Log.d("ScoreCard", "callFollowOn, fielding team No :"+fieldingTeamNo);

        AlertDialog followOnAlert = new AlertDialog.
                Builder(ScoreCardActivity.this).create();
        followOnAlert.setIcon(R.drawable.ball);
        followOnAlert.setCancelable(false);
        followOnAlert.setTitle("Do you want to Follow on");
//        superOverAlert.setMessage("Allotted innings completed");
        followOnAlert.setButton(AlertDialog.BUTTON_POSITIVE, "Normal",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();

                        checkFollowOn("Normal",false);


                                   /* SUPER_OVER = true;
                                    displayScoreCard(false);
//                    callSuperOver();*/

                    }

                });


        followOnAlert.setButton(AlertDialog.BUTTON_NEGATIVE, "Follow On",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();

                        checkFollowOn("Follow On",true);
                       /* Intent intent = new Intent(ScoreCardActivity.this, OpenersActivity.class);
//                intent.putExtra("SUPER_OVER", SUPER_OVER);
                        intent.putExtra("batting_team_no", battingTeamNo);
                        intent.putExtra("batting_team", battingTeam);
                        intent.putExtra("fielding_team_no", fieldingTeamNo);
                        intent.putExtra("fielding_team", fieldingTeam);
                        intent.putExtra("super_over_innings", super_over_innings);
                        intent.putExtra("continue_innings", continueInnings);
                        startActivity(intent);
                                   *//* endOfMatch = true;
                                    endOfMatch();*/

                    }

                });


        followOnAlert.show();
    }



    void checkFollowOn(String message, boolean choice) {

        Log.d("ScoreCard", "checkFollowOn, batting team :"+battingTeam);
        Log.d("ScoreCard", "checkFollowOn, fielding team :"+fieldingTeam);
        Log.d("ScoreCard", "checkFollowOn, innings :"+c_innings);
        Log.d("ScoreCard", "checkFollowOn, batting team No :"+battingTeamNo);
        Log.d("ScoreCard", "checkFollowOn, fielding team No :"+fieldingTeamNo);
        Log.d("ScoreCard", "checkFollowOn, choice :"+choice);
        Log.d("ScoreCard", "checkFollowOn, message :"+message);

        AlertDialog followOnAlert = new AlertDialog.
                Builder(ScoreCardActivity.this).create();
        followOnAlert.setIcon(R.drawable.ball);
        followOnAlert.setCancelable(false);
        followOnAlert.setTitle(message);
//        superOverAlert.setMessage("Allotted innings completed");
        followOnAlert.setButton(AlertDialog.BUTTON_POSITIVE, "CONFIRM",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();

                        if (choice) {

                            saveFollowOn(choice);

                            Intent intent = new Intent(ScoreCardActivity.this, OpenersActivity.class);
//                intent.putExtra("SUPER_OVER", SUPER_OVER);
                            intent.putExtra("batting_team_no", battingTeamNo);
                            intent.putExtra("batting_team", battingTeam);
                            intent.putExtra("fielding_team_no", fieldingTeamNo);
                            intent.putExtra("fielding_team", fieldingTeam);
                            intent.putExtra("super_over_innings", super_over_innings);
                            intent.putExtra("continue_innings", continueInnings);
                            intent.putExtra("follow_on", true);
                            startActivity(intent);
                        }

                        else if (!choice) {

                            Intent intent = new Intent(ScoreCardActivity.this, OpenersActivity.class);
//                intent.putExtra("SUPER_OVER", SUPER_OVER);
                            intent.putExtra("batting_team_no", fieldingTeamNo);
                            intent.putExtra("batting_team", fieldingTeam);
                            intent.putExtra("fielding_team_no", battingTeamNo);
                            intent.putExtra("fielding_team", battingTeam);
                            intent.putExtra("super_over_innings", super_over_innings);
                            intent.putExtra("continue_innings", continueInnings);
                            intent.putExtra("follow_on", false);
                            startActivity(intent);
                        }



                                   /* SUPER_OVER = true;
                                    displayScoreCard(false);
//                    callSuperOver();*/

                    }

                });


        followOnAlert.setButton(AlertDialog.BUTTON_NEGATIVE, "CANCEL",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();

                        callFollowOn();

                                   /* endOfMatch = true;
                                    endOfMatch();*/

                    }

                });


        followOnAlert.show();



    }



    void saveFollowOn(boolean followon) {


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

                            if (match != null) {
                                match.setFollowon(followon);

                                realm.copyToRealmOrUpdate(match);
                            }

                            Log.d("endOfMatch", "endOfMatch, match : " + match);
                        } catch (RealmPrimaryKeyConstraintException e) {
                            Toast.makeText(getApplicationContext(),
                                    "Primary Key exists, Press Update instead", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }

            catch (RealmException e) {
                Log.d("test", "Exception : " + e);
            }

            finally {
                if (realm != null) {
                    realm.close();
                }
            }

    }


    void refreshScoreCard(){

        batting_table_count = realm.where(Batsman.class).
                equalTo("matchid", matchid).
                equalTo("innings", c_innings).
                equalTo("team", battingTeamNo).findAll().size();

        while (batting_table.getChildCount() > 0){
            TableRow tableRow = (TableRow) batting_table.getChildAt(0);
            batting_table.removeView(tableRow);
        }

        if (abandoned_after_toss == 0) {

            while (extra_table.getChildCount() > 0) {
                TableRow tableRow = (TableRow) extra_table.getChildAt(0);
                extra_table.removeView(tableRow);
            }

            while (fow_table.getChildCount() > 0) {
                TableRow tableRow = (TableRow) fow_table.getChildAt(0);
                fow_table.removeView(tableRow);
            }

            while (fielding_table.getChildCount() > 0) {
                TableRow tableRow = (TableRow) fielding_table.getChildAt(0);
                fielding_table.removeView(tableRow);
            }
        }
    }

    // Added on 02/03/2021
    private void getFromEventTable(int matchid, int innings) {


        Events events = realm.where(Events.class).
                equalTo("matchid", matchid).
                equalTo("innings", innings).
                notEqualTo("ballType", 13).findAll().last();

        if (events != null) {

            declare = events.isDeclared();
            forfeit = events.isForfeit();
            forfeit_team = events.getForfeit_team();
            forfeit_innings = events.getForfeit_innings();

            forceEndingType = events.getForceEndingType();

            /*i.putExtra("declare", events.isDeclared());
            i.putExtra("forfeit", events.isForfeit());
            i.putExtra("forfeit_team", events.getForfeit_team());
            i.putExtra("forfeit_innings", events.getForfeit_innings());
            i.putExtra("innings", events.getInnings());
            i.putExtra("matchstatus", match.getStatus());*/
        }
    }


    private void getConcedeInfo(int matchid) {

        Events events = realm.where(Events.class).
                equalTo("matchid", matchid).
                notEqualTo("ballType", 13).findAll().last();

        if (events != null) {
            concede = events.isCONCEDE();
            conceded_team = events.getConceded_team();
            conceded_innings = events.getInnings();
        }
    }
    // ======= till here-------



    void displayScoreCard(int matchid, int innings){

        if (abandoned_after_toss == 0) {
            getConcedeInfo(matchid);
//        if (abandoned_match_flag == 0)
            getFromEventTable(matchid, innings);
        }


        Log.d("ScoreCard", "displayScoreCard, click :"+click);
        Log.d("ScoreCard", "displayScoreCard starting, batting team :"+battingTeam);
        Log.d("ScoreCard", "displayScoreCard starting, fielding team :"+fieldingTeam);
        Log.d("ScoreCard", "displayScoreCard starting, innings :"+c_innings);
        Log.d("ScoreCard", "displayScoreCard starting, batting team No :"+battingTeamNo);
        Log.d("ScoreCard", "displayScoreCard starting, fielding team No :"+fieldingTeamNo);


        if (click > 0)
            refreshScoreCard();

//
//        if (batting_table.getChildCount() > 0)
//            batting_table.removeAllViews();
//
//        if (extra_table.getChildCount() > 0)
//            extra_table.removeAllViews();
//
//        if (fow_table.getChildCount() > 0)
//            fow_table.removeAllViews();
//
//        if (fielding_table.getChildCount() > 0)
//            fielding_table.removeAllViews();

        Log.d("ScoreCard", "displayScoreCard, matchid :"+matchid);
        Log.d("ScoreCard", "displayScoreCard, innings :"+innings);

//        if (c_innings > 1){       // commented on 11/09/2020

            if (innings == 1){
                first.setEnabled(false);
                second.setEnabled(true);
                third.setEnabled(true);
                forth.setEnabled(true);

            } else if (innings == 2){
                first.setEnabled(true);
                second.setEnabled(false);
                third.setEnabled(true);
                forth.setEnabled(true);

            } else if (innings == 3){
                first.setEnabled(true);
                second.setEnabled(true);
                third.setEnabled(false);
                forth.setEnabled(true);

            } else if (innings == 4){
                first.setEnabled(true);
                second.setEnabled(true);
                third.setEnabled(true);
                forth.setEnabled(false);

            } else if (innings == 99){
                first.setEnabled(true);
                second.setEnabled(true);
                third.setEnabled(false);
                forth.setEnabled(true);

            } else if (innings == 100){
                first.setEnabled(true);
                second.setEnabled(true);
                third.setEnabled(true);
                forth.setEnabled(false);

            } else  if (abandoned_after_toss == 1) {    // Added new condition on 06/09/2021
                first.setEnabled(true);
                second.setEnabled(true);
                third.setEnabled(false);
                forth.setEnabled(true);

            } else {  // Added on 02/09/2021

                first.setEnabled(false);
                second.setEnabled(false);
                third.setEnabled(false);
                forth.setEnabled(false);
            }
//        }     // commented on 11/09/2020

//        if (innings % 2 != 0){
//
//            if (battingTeamNo == 1){
//                battingTeam = teamB;
//                fieldingTeam = teamA;
//                battingTeamNo = 2;
//                fieldingTeamNo = 1;
//            }
//            else {
//                battingTeam = teamA;
//                fieldingTeam = teamB;
//                battingTeamNo = 1;
//                fieldingTeamNo = 2;
//            }
//        }

        Log.d("ScoreCard", "displayScoreCard, teamA :"+teamA);
        Log.d("ScoreCard", "displayScoreCard, teamB :"+teamB);

        // added on 11/09/2020
       RealmResults<Events> events_result = realm.where(Events.class).
                equalTo("matchid", matchid).
                equalTo("innings", innings).findAll();

       if (events_result.size() > 0) {

           Events event = events_result.last();

       /* Events event = realm.where(Events.class).     // commented on 11/09/2020
                equalTo("matchid", matchid).
                equalTo("innings", innings).findAll().last();*/

           // commented on 31/07/2020
        /*if (inningsnotstarted && c_innings == innings){

            run = 0;
            wicket = 0;
            over = 0;
            Log.d("ScoreCard", "displayScoreCard, inningsnotstarted : " +inningsnotstarted);
        }

        else {*/

//        Log.d("ScoreCard", "displayScoreCard, event :"+event);


//            tv_innings.setText(String.valueOf(innings));
           setInnings(innings);
           run = event.getTotalRuns();
           wicket = event.getWicket();
           over = event.getOvers();
           balls = event.getBalls();
           Log.d("ScoreCard", "displayScoreCard, last event : " + event);
//        } // commented on 31/07/2020

           // added on 20/10/2020
           tv_braces_open.setVisibility(View.VISIBLE);
           tv_braces_closed.setVisibility(View.VISIBLE);

           tBatNo = event.getBattingTeamNo();
           tFieldNo = event.getFieldingTeamNo();

           if (tBatNo == 1 && tFieldNo == 2) {
               tBat = teamA;
               tField = teamB;
               tp = tpA;
           } else if (tBatNo == 2 && tFieldNo == 1) {
               tBat = teamB;
               tField = teamA;
               tp = tpB;
           }
           tv_batTeam.setText(tBat);
           tv_fieldTeam.setText(tField);


           tv_run.setText(String.valueOf(run));

           if (wicket >= (tp - 1)) {

               tv_div.setVisibility(View.GONE);
               tv_wicket.setVisibility(View.GONE);
           }
           else if (forceEndingType == 2) {
               tv_div.setVisibility(View.GONE);
               tv_wicket.setVisibility(View.GONE);
           }
           else {
               tv_div.setVisibility(View.VISIBLE);
               tv_wicket.setVisibility(View.VISIBLE);
               tv_wicket.setText(String.valueOf(wicket));
           }

           if (HUNDRED)
               tv_over.setText(String.valueOf(balls));
           else
               tv_over.setText(String.valueOf(new DecimalFormat("##.#").format(over)));


           // for batting team

           batting_table = findViewById(R.id.batting_table);
           TableRow rowbh = new TableRow(this);
           rowbh.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, 50));
//        rowbh.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT, 1));
           rowbh.setPadding(10, 25, 10, 25);

           TextView tvbh12 = new TextView(this);
           tvbh12.setTextSize(13);
           tvbh12.setText(" ");
           tvbh12.setTextColor(Color.BLACK);
           rowbh.addView(tvbh12);

           TextView tvbh13 = new TextView(this);
           tvbh13.setTextSize(13);
           tvbh13.setText("");
           tvbh13.setTextColor(Color.BLACK);
           rowbh.addView(tvbh13);

           TextView tvbh1 = new TextView(this);
           tvbh1.setTextSize(13);
           tvbh1.setTextColor(Color.BLACK);
           tvbh1.setText(" Player");
           rowbh.addView(tvbh1);


           TextView tvbh11 = new TextView(this);
           tvbh11.setTextSize(13);
           tvbh11.setTextColor(Color.BLACK);
           tvbh11.setText(" \t\t");
           rowbh.addView(tvbh11);

          /* // Added on 06/03/2021
           TextView tvbh14 = new TextView(this);
           tvbh14.setTextSize(13);
           tvbh14.setTextColor(Color.BLACK);
           tvbh14.setText(" \t\t");
           rowbh.addView(tvbh14);
           // ========== till here*/

        /*TextView tvbh14 = new TextView(this);
//            tvb11.setTextSize(16);
        tvbh14.setTextColor(Color.BLACK);
        tvbh14.setText(" \t\t");
        rowbh.addView(tvbh14);


        TextView tvbh15 = new TextView(this);
//            tvb11.setTextSize(16);
        tvbh15.setTextColor(Color.BLACK);
        tvbh15.setText(" \t\t");
        rowbh.addView(tvbh15);*/

//        TextView tvbh11 = new TextView(this);
//        tvbh11.setTextSize(16);
//        tvbh11.setText("");
//        rowbh.addView(tvbh11);
//        TextView tvbh12 = new TextView(this);
//        tvbh12.setTextSize(16);
//        tvbh12.setText("");
//        rowbh.addView(tvbh12);
//        TextView tvbh13 = new TextView(this);
//        tvbh13.setTextSize(16);
//        tvbh13.setText("");
//        rowbh.addView(tvbh13);
           TextView tvbh2 = new TextView(this);
           tvbh2.setTextSize(13);
           tvbh2.setTextColor(Color.BLACK);
           tvbh2.setText("\t\t\t\t\tRuns");
           rowbh.addView(tvbh2);

           TextView tvbh3 = new TextView(this);
           tvbh3.setTextSize(13);
           tvbh3.setTextColor(Color.BLACK);
           tvbh3.setText("\t\tBalls");
           rowbh.addView(tvbh3);

           TextView tvbh4 = new TextView(this);
           tvbh4.setTextSize(13);
           tvbh4.setTextColor(Color.BLACK);
           tvbh4.setText("\t\t\t4s");
           rowbh.addView(tvbh4);

           TextView tvbh5 = new TextView(this);
           tvbh5.setTextSize(13);
           tvbh5.setTextColor(Color.BLACK);
           tvbh5.setText("\t\t\t6s");
           rowbh.addView(tvbh5);

           TextView tvbh6 = new TextView(this);
           tvbh6.setTextSize(13);
           tvbh6.setTextColor(Color.BLACK);
           tvbh6.setText("\t\t\t      ");
           rowbh.addView(tvbh6);

           batting_table.addView(rowbh);


//        Log.d("ScoreCard", "displayScoreCard, matchid :"+matchid+", battingTeamNo : "+battingTeamNo+
//                ", innings : "+innings);

     /*   RealmResults<Batsman> resultb = realm.where(Batsman.class).
                equalTo("matchid", matchid).
                equalTo("team", tBat).
                equalTo("innings", innings).findAll();*/


           /*==============================================================================================================================================*/

           // Added on 06/12/2021
           boolean flag = false;
           if (SUPER_OVER) {
               if (innings == 1 || innings == 2 || innings == 3 || innings == 4)
                   flag = false;
               else
                   flag = true;
           }
           // === till here

           RealmResults<Batsman> resultt = realm.where(Batsman.class).
                   equalTo("matchid", matchid).
                   equalTo("team", tBatNo).
                   equalTo("innings", innings).
                   equalTo("SUPER_OVER", flag).//SUPER_OVER).
                   sort("battingOrder", Sort.ASCENDING).
                   findAll();



           /*==============================================================================================================================================*/

//        resultb.load();

//        Log.d("ScoreCard", "displayScoreCard, resulttb : "+resultb);


           Log.d("ScoreCardActivity", "displayScoreCard, matchid : " + matchid);
           Log.d("ScoreCardActivity", "displayScoreCard, battingTeamNo : " + battingTeamNo);
           Log.d("ScoreCardActivity", "displayScoreCard, tBatNo : " + tBatNo);
           Log.d("ScoreCardActivity", "displayScoreCard, innings : " + innings);
           Log.d("ScoreCardActivity", "displayScoreCard, SUPER_OVER : " + SUPER_OVER);
//        Log.d("ScoreCardActivity", "displayScoreCard, batsman : "+resultb);
           Log.d("ScoreCardActivity", "displayScoreCard, sorted batsman : " + resultt);
           for (Batsman batsman : resultt) { //resultb) {

               Player player = realm.where(Player.class).
                       equalTo("matchid", matchid).
                       equalTo("team", tBatNo).
                       equalTo("playerID", batsman.getBatsman_pID()).findFirst();

               Log.d("ScoreCardActivity", "displayScoreCard, batsman.getBatsman_pID()) : " +
                       batsman.getBatsman_pID());

               if (player != null) {
                   c = player.isCaptain();
                   wk = player.isWicketKeeper();
                   b_name = player.getPlayerName();
               } else
                   Log.d("ScoreCardActivity", "displayScoreCard, Player : " + player);

               Log.d("ScoreCard", "displayScoreCard, batsman : " + batsman);

//            Log.d("scorecard", "displayScoreCard, playerName : "+batsman.getBatsmanName()+", captain : "+c+", wk : "+wk);

               temp++;

               TableRow rowb = new TableRow(this);
               rowb.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, 50));
               rowb.setPadding(10, 25, 10, 25);


               if (temp % 2 != 0) {
                   rowb.setBackgroundResource(R.drawable.row_border);
               }

               if (batsman.isOut()) {
                   ot = batsman.getOutType();
                   wk_flag = batsman.isWk_fielder();
                   if ((ot == 0) || (ot == 1) || (ot == 3) || (ot == 4) || (ot == 5) || (ot == 6)) {
                       bowler = realm.where(Player.class).
                               equalTo("matchid", matchid).
                               equalTo("team", tFieldNo).
                               equalTo("playerID", batsman.getBowler_pID()).findFirst().getPlayerName();
                       if (ot == 1)
                           bid = batsman.getBowler_pID();
                       else
                           bid = 0;
                   } else
                       bowler = "";

                   Log.d("Scorecad", "if (batsman.isOut()), ot : " + ot);
                   Log.d("Scorecad", "if (batsman.isToBeBatted()), ot : " + batsman.isToBeBatted());
                   Log.d("Scorecad", "if (batsman.isRetired()), ot : " + batsman.isRetired());
                   Log.d("Scorecad", "if (batsman.isPlaying()), ot : " + batsman.isPlaying());

                   if (ot > 0 && ot <= 3) {
                       Log.d("Scorecad", "if (ot > 0 && ot <= 3), ot : " + ot);

                       if (ot == 2) {// || batsman.getFielder_pID().contains(", ")) {       // if not present it returns -1
                           // else it returns value >= 0
                           Log.d("Scorecad", "if (ot == 2), ot : " + ot);

                           String[] arrSplit = batsman.getFielder_pID().split(", ");
                           for (int i = 0; i < arrSplit.length; i++) {

                               fielderIDList.add(arrSplit[i]);
                           }

                           if (fielderIDList != null) {
                               Log.d("Scorecad", "fielderIDList : " + fielderIDList);
                               Log.d("Scorecad", "fielderIDList.get(0) : " + fielderIDList.get(0));

                               for (int t = 0; t < fielderIDList.size(); t++) {

                            /*fielderNameList.add(realm.where(Player.class).
                                    equalTo("matchid", matchid).
                                    equalTo("team", fieldingTeamNo).
                                    equalTo("playerID", Integer.parseInt(fielderIDList.get(t)))
                                    .findFirst().getPlayerName());*/

                                   if (t == 0) {

                                       /* Commented on 23/09/2021
                                       fielder = realm.where(Player.class).
                                               equalTo("matchid", matchid).
                                               equalTo("team", fieldingTeamNo).
                                               equalTo("playerID", Integer.parseInt(fielderIDList.get(t)))
                                               .findFirst().getPlayerName();*/

                                       // Updated on 23/09/2021
                                       Player plyr = realm.where(Player.class).
                                               equalTo("matchid", matchid).
                                               equalTo("team", fieldingTeamNo).
                                               equalTo("playerID", Integer.parseInt(fielderIDList.get(t)))
                                               .findFirst();
                                       if (plyr != null) {
                                           fielder = plyr.getPlayerName();
                                           /*if (player.isSubstitute())
                                               sub_flag = 1;*/
                                       }
                                       else
                                           fielder = "";

                                   } else if (t > 0) {

                                       /* Commented on 23/09/2021
                                       fielder = fielder + ", " + realm.where(Player.class).
                                               equalTo("matchid", matchid).
                                               equalTo("team", fieldingTeamNo).
                                               equalTo("playerID", Integer.parseInt(fielderIDList.get(t)))
                                               .findFirst().getPlayerName();*/

                                       // Updated on 23/09/2021
                                       Player plyr = realm.where(Player.class).
                                               equalTo("matchid", matchid).
                                               equalTo("team", fieldingTeamNo).
                                               equalTo("playerID", Integer.parseInt(fielderIDList.get(t)))
                                               .findFirst();
                                       if (plyr != null) {
                                           fielder = fielder.concat(", " + plyr.getPlayerName());
                                       }
                                      /* else
                                           fielder = fielder;*/
                                   }

                               }

                            /*fielder = realm.where(Player.class).
                                    equalTo("matchid", matchid).
                                    equalTo("team", tFieldNo).
                                    equalTo("playerID", Integer.parseInt(fielderIDList.get(0))).findFirst().getPlayerName();*/

                           }
                       } else if (ot == 1 || ot == 3) {

                           Log.d("Scorecad", "else if (ot == 1 || ot == 3), ot : " + ot);

                           Player p = realm.where(Player.class).
                                   equalTo("matchid", matchid).
                                   equalTo("team", tFieldNo).
                                   equalTo("playerID", Integer.parseInt(batsman.getFielder_pID())).findFirst();
                           if (p != null) {
                               fielder = p.getPlayerName();
                               if (p.isSubstitute())
                                   sub_flag = 1;
                               else
                                   sub_flag = 0;
                               if (ot == 1)
                                   fid = Integer.parseInt(batsman.getFielder_pID());
                               else
                                   fid = 0;
                           }
                       }

                    /*fielder = realm.where(Player.class).
                            equalTo("matchid", matchid).
                            equalTo("team", fieldingTeamNo).
                            equalTo("playerID", batsman.getFielder_pID()).findFirst().getPlayerName();*/
                   } else {
                       Log.d("Scorecad", "else , ot : " + ot);
                       fielder = null;
                   }

                   displayOut(ot, fielder, bowler, wk_flag, fid, bid, sub_flag);
               } else if (batsman.isPlaying()) {
                   ot = -1;
                   otype = "not out";
                   Log.d("Scorecad", "batsman.isPlaying(), batsman : " + batsman);
               } else if (batsman.isRetired()) {
                   otype = "retired";
               } else if (batsman.isToBeBatted()) {
                   ot = 100;
               }


               TextView tvb12 = new TextView(this);
//            tvb12.setTextSize(10);
               tvb12.setTextColor(Color.BLACK);
               if (c) {
                   tvb12.setText(" *");
               } else {
                   tvb12.setVisibility(View.INVISIBLE);
               }
               rowb.addView(tvb12);

               TextView tvb13 = new TextView(this);
//            tvb13.setTextSize(10);
               tvb13.setTextColor(Color.BLACK);
               if (wk) {
                   tvb13.setText("+");
               } else {
                   tvb13.setVisibility(View.INVISIBLE);
               }
               rowb.addView(tvb13);

               TextView tvb1 = new TextView(this);
//            tvb1.setTextSize(16);
               tvb1.setTextColor(Color.BLACK);
               Log.e("ScoreCardActivity", "displayScoreCard, b_name : " + b_name);
               tvb1.setText(" " + b_name);
               rowb.addView(tvb1);


               TextView tvb11 = new TextView(this);
//            tvb11.setTextSize(16);
               tvb11.setTextColor(Color.BLACK);
               Log.d("ScoreCardActivity", "displayScoreCard, batsman.isPlaying() : " + batsman.isPlaying());
               Log.e("ScoreCardActivity", "displayScoreCard, otype : " + otype);
               tvb11.setText(" \t\t" + otype);
//            tvb11.setText(" \t\tnot out");
               rowb.addView(tvb11);



           /* TextView tvb14 = new TextView(this);
//            tvb11.setTextSize(16);
            tvb14.setTextColor(Color.BLACK);
            if (ot > -1){
                if (batsman.getFielder_pID() != null)
                    tvb14.setText(" \tc " + fielder);
                else {
                    tvb14.setVisibility(View.INVISIBLE);
                }
            }
            else {
                tvb14.setVisibility(View.INVISIBLE);
            }
            rowb.addView(tvb14);
            fielder = null;
            fielderIDList.clear();


            TextView tvb15 = new TextView(this);
//            tvb11.setTextSize(16);
            tvb15.setTextColor(Color.BLACK);
            if (ot > -1){
                if (batsman.getBowler_pID() != 0)
                    tvb15.setText(" \tb " + bowler);
                else {
                    tvb15.setVisibility(View.INVISIBLE);
                }
            }
            else {
                tvb15.setVisibility(View.INVISIBLE);
            }
            rowb.addView(tvb15);*/

               fielder = null;
               bowler = null;
               fielderIDList.clear();


               TextView tvb2 = new TextView(this);
//            tvb2.setTextSize(16);
               tvb2.setTextColor(Color.BLACK);
               Log.e("ScoreCardActivity", "displayScoreCard, batsman.getRuns() : " + batsman.getRuns());
               tvb2.setText("\t\t\t\t\t\t" + batsman.getRuns());
               rowb.addView(tvb2);

               TextView tvb3 = new TextView(this);
//            tvb3.setTextSize(16);
               tvb3.setTextColor(Color.BLACK);
               Log.e("ScoreCardActivity", "displayScoreCard, batsman.getBalls() : " + batsman.getBalls());
               tvb3.setText("\t\t\t" + batsman.getBalls());
               rowb.addView(tvb3);

               TextView tvb4 = new TextView(this);
//            tvb4.setBackgroundColor(Color.RED);
//            tvb4.setTextSize(16);
               tvb4.setTextColor(Color.BLACK);
               Log.e("ScoreCardActivity", "displayScoreCard, batsman.getF4s() : " + batsman.getF4s());
               tvb4.setText("\t\t\t" + batsman.getF4s());
               rowb.addView(tvb4);

               TextView tvb5 = new TextView(this);
//            tvb5.setTextSize(16);
               tvb5.setTextColor(Color.BLACK);
               Log.e("ScoreCardActivity", "displayScoreCard, batsman.getS6s() : " + batsman.getS6s());
               tvb5.setText("\t\t\t" + batsman.getS6s());
               rowb.addView(tvb5);

               batting_table.addView(rowb);

               Log.e("ScoreCardActivity", "displayScoreCard, ot : " + ot);

               if (ot == 100) {

                   tvb11.setVisibility(View.INVISIBLE);
                /*tvb14.setVisibility(View.INVISIBLE);
                tvb15.setVisibility(View.INVISIBLE);*/
                   tvb2.setVisibility(View.INVISIBLE);
                   tvb3.setVisibility(View.INVISIBLE);
                   tvb4.setVisibility(View.INVISIBLE);
                   tvb5.setVisibility(View.INVISIBLE);
               }

               if (ot != 100) {

                   tvb11.setVisibility(View.VISIBLE);
                /*tvb14.setVisibility(View.VISIBLE);
                tvb15.setVisibility(View.VISIBLE);*/
                   tvb2.setVisibility(View.VISIBLE);
                   tvb3.setVisibility(View.VISIBLE);
                   tvb4.setVisibility(View.VISIBLE);
                   tvb5.setVisibility(View.VISIBLE);
               }

               if (ot == 11) {

                   tvb2.setVisibility(View.INVISIBLE);
                   tvb3.setVisibility(View.INVISIBLE);
                   tvb4.setVisibility(View.INVISIBLE);
                   tvb5.setVisibility(View.INVISIBLE);
               }
           }


           extra_table = findViewById(R.id.extracard);
//        TableRow rowe = new TableRow(this);
//        rowbh.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,50));
//        rowbh.setPadding(10, 25,10, 25);

           // for adding extras in extra_table in score card display
           temp++;
           TableRow rowbe = new TableRow(this);
           rowbe.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, 50));
           rowbe.setPadding(10, 25, 10, 25);

           if (temp % 2 != 0)
               rowbe.setBackgroundResource(R.drawable.row_border);

           TextView tvbe1 = new TextView(this);
//        tvbe1.setTextSize(16);
           tvbe1.setTextColor(Color.BLACK);
           tvbe1.setText(" \t\t\t Extras");
           rowbe.addView(tvbe1);

//        TextView tvbe2 = new TextView(this);
////        tvbe2.setTextSize(16);
//        tvbe2.setTextColor(Color.BLACK);
//        tvbe2.setText(" \t\t\t\t");
//        rowbe.addView(tvbe2);

           TextView tvbe3 = new TextView(this);
//        tvbe3.setTextSize(16);
           tvbe3.setTextColor(Color.BLACK);

           ExtraCard extraCard = realm.where(ExtraCard.class).
                   equalTo("matchid", matchid).
                   equalTo("innings", innings).findFirst();
           Log.d("ScoreCard", "onCreate, extracard : " + extraCard);
           if (extraCard != null) {

               b = extraCard.getByes();
               lb = extraCard.getLb();
               wd = extraCard.getWide();
               nb = extraCard.getNoBall();
               p = extraCard.getPenalty();

           } else {
               b = 0;
               lb = 0;
               wd = 0;
               nb = 0;
               p = 0;
           }

           tvbe3.setText(" \t\t( " + b + "b, " + lb + "lb, " + (nb * noballRun) + "nb, " +
                   (wd * wideRun) + "wd, " + (p * penaltyRun) + "p)");

           rowbe.addView(tvbe3);

           TextView tvbe4 = new TextView(this);
//        tvbe4.setTextSize(16);
           tvbe4.setTextColor(Color.BLACK);
           tvbe4.setText(" \t");
           rowbe.addView(tvbe4);

           TextView tvbe5 = new TextView(this);
           tvbe5.setTextColor(Color.BLACK);
//        tve5.setTextSize(16);
//        if (extraCard != null)
//            t = extraCard.getByes() + extraCard.getLb() + extraCard.getWide() +
//            extraCard.getNoBall() + extraCard.getPenalty();
           tvbe5.setText(" \t\t" + (b + lb + (wd * wideRun) + (nb * noballRun) + (p * penaltyRun)) + " \t\t\t\t\t\t");
           rowbe.addView(tvbe5);
           extra_table.addView(rowbe);


           // for adding total runs and wickets in extra_table in scores card
           temp++;
           TableRow rowbt = new TableRow(this);
           rowbt.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, 50));
           rowbt.setPadding(10, 25, 10, 25);

           if (temp % 2 != 0)
               rowbt.setBackgroundResource(R.drawable.row_border);

           TextView tvbt1 = new TextView(this);
//        tvbe1.setTextSize(16);
           tvbt1.setTextColor(Color.BLACK);
           tvbt1.setText(" \t\t\t Total");
           rowbt.addView(tvbt1);

//        TextView tvbt2 = new TextView(this);
////        tvbe2.setTextSize(16);
//        tvbt2.setTextColor(Color.BLACK);
//        tvbt2.setText(" \t\t\t\t");
//        rowbt.addView(tvbt2);

           TextView tvbt3 = new TextView(this);
//        tvbe3.setTextSize(16);
           tvbt3.setTextColor(Color.BLACK);
           if (declare)
               tvbt3.setText(" \t\t( " + wicket + " wkts, declared, " +
                       (new DecimalFormat("##.#").format(over)) + " overs)");
           else {
               if (wicket == (tp - 1)) {
                   if (HUNDRED) {
                       tvbt3.setText(" \t\t( " + balls + " balls)");
                   } else {
                       tvbt3.setText(" \t\t( " + (new DecimalFormat("##.#").format(over)) + " overs)");
                   }
               } else {
                   if (HUNDRED) {
                       tvbt3.setText(" \t\t( " + wicket + " wkts, " + balls + " balls)");
                   } else {
                       tvbt3.setText(" \t\t( " + wicket + " wkts, " +
                               (new DecimalFormat("##.#").format(over)) + " overs)");
                   }
               }
           }
           rowbt.addView(tvbt3);

           TextView tvbt4 = new TextView(this);
//        tvbe4.setTextSize(16);
           tvbt4.setTextColor(Color.BLACK);
           tvbt4.setText(" \t\t\t\t");
           rowbt.addView(tvbt4);

           TextView tvbt5 = new TextView(this);
           tvbt5.setTextColor(Color.BLACK);
           tvbt5.setText(" \t\t" + run + " \t\t\t\t\t\t");
           rowbt.addView(tvbt5);
           extra_table.addView(rowbt);


           // to display Fall Of Wickets of the batting team

           fow_table = findViewById(R.id.fow);

           fow_hsv = findViewById(R.id.fow_hsv);

           RealmResults<FOW> results = realm.where(FOW.class).
                   equalTo("matchid", matchid).
                   equalTo("innings", innings).findAll();


           if (wicket > 0)
               fow_hsv.setVisibility(View.VISIBLE);

           else
               fow_hsv.setVisibility(View.GONE);

           if (results != null) {

               results.load();
               temp++;
               TableRow rowfwh = new TableRow(this);
               rowfwh.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, 50));
               rowfwh.setPadding(10, 25, 10, 25);

               if (temp % 2 != 0)
                   rowfwh.setBackgroundResource(R.drawable.row_border);

               TextView tvfwh1 = new TextView(this);
//          tvfw1.setTextSize(16);
               tvfwh1.setTextColor(Color.BLACK);
               tvfwh1.setText(" \t\t");
               rowfwh.addView(tvfwh1);

               TextView tvfwh2 = new TextView(this);
//            tvfw1.setTextSize(16);
               tvfwh2.setTextColor(Color.BLACK);
               tvfwh2.setText(" \t");
               rowfwh.addView(tvfwh2);

               TextView tvfwh3 = new TextView(this);
//            tvfw1.setTextSize(16);
               tvfwh3.setTextColor(Color.BLACK);
               tvfwh3.setText("Fall Of Wickets  :\t\t\t");
               rowfwh.addView(tvfwh3);

               /* Commented on 28/07/2021
               TextView tvfwh4 = new TextView(this);
               //        tvfw1.setTextSize(16);
               tvfwh4.setTextColor(Color.BLACK);
               tvfwh4.setText(" ");
               rowfwh.addView(tvfwh4);

               TextView tvfwh5 = new TextView(this);
//          tvfw1.setTextSize(16);
               tvfwh5.setTextColor(Color.BLACK);
               tvfwh5.setText(" \t\t\t\t\t\t\t\t\t");
               rowfwh.addView(tvfwh5);

               TextView tvfwh6 = new TextView(this);
//          tvfw1.setTextSize(16);
               tvfwh6.setTextColor(Color.BLACK);
               tvfwh6.setText(" \t\t\t\t\t\t");
               rowfwh.addView(tvfwh6);*/

               fow_table.addView(rowfwh);


               for (FOW fow : results) {

                   Log.e("ScoreCardActivity", "displayScoreCard, fow : " + fow);

//            TextView tvfw = new TextView(this);
//            tvfw.setTextColor(Color.BLACK);
////            tvfw.setTextSize(16);
//            tvfw.setText("\t" + fow.getWicket() + "-" + fow.getRun());
//            rowfw.addView(tvfw);
////        }

//            temp++;
                   TableRow rowfw = new TableRow(this);
                   rowfw.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, 35));
                   rowfw.setPadding(10, 25, 10, 25);

//            if (temp % 2 != 0)
//                rowfw.setBackgroundResource(R.drawable.row_border);

                   TextView tvfw1 = new TextView(this);
//        tvfw1.setTextSize(16);
                   tvfw1.setTextColor(Color.BLACK);
                   tvfw1.setText(" \t\t " + fow.getWicket() + " - " + fow.getRun());    //  Updated on 28/07/2021
                   rowfw.addView(tvfw1);

                   /*  Commented on 28/07/2021
                   TextView tvfw2 = new TextView(this);
//        tvfw2.setTextSize(16);
                   tvfw2.setTextColor(Color.BLACK);
                   tvfw2.setText(" " + fow.getRun() + " ");
                   rowfw.addView(tvfw2);*/

                   String name = "";
                   Player player = realm.where(Player.class).
                           equalTo("matchid", matchid).
                           equalTo("team", tBatNo).
                           equalTo("playerID", fow.getDismissedPlayerID()).findFirst();
                   if (player != null)
                       name = player.getPlayerName();
                   Log.e("ScoreCardActivity", "displayScoreCard, player : " + player);

                   TextView tvfw3 = new TextView(this);
//        tvfw3.setTextSize(16);
                   tvfw3.setTextColor(Color.BLACK);
                   tvfw3.setText("( " + name + " , ");
                   rowfw.addView(tvfw3);

                   TextView tvfw4 = new TextView(this);
//          tvfw4.setTextSize(16);
                   tvfw4.setTextColor(Color.BLACK);
//                tvfw4.setText((new DecimalFormat("##.#").format(fow.getOver())) + " ov )");
                   if (HUNDRED)
                       tvfw4.setText(fow.getBalls() + " balls )");
                   else
                       tvfw4.setText(fow.getOver() + " ov )");
                   rowfw.addView(tvfw4);

                   TextView tvfw5 = new TextView(this);
//        tvfw4.setTextSize(16);
                   tvfw5.setTextColor(Color.BLACK);
                   tvfw5.setText("\t\t\t\t\t\t\t");
                   rowfw.addView(tvfw5);

                   fow_table.addView(rowfw);

               }
           } else
               fow_hsv.setVisibility(View.GONE);


           // for fielding team

           fielding_table = findViewById(R.id.fielding_table);
           TableRow rowfh = new TableRow(this);
           rowfh.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, 50));
//        TableRow.LayoutParams lprfh = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, 50);
//        rowfh.setLayoutParams(lp);
//        rowfh.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT, 1));
//        temp++;
           rowfh.setPadding(10, 25, 10, 25);
//        if (temp % 2 != 0)
//            rowfh.setBackgroundResource(R.drawable.row_border);
//        rowfh.setBackgroundColor(Color.rgb(51, 51, 51));

           TextView tvfh1 = new TextView(this);
//        tvfh1.setTextSize(16);
           tvfh1.setTextColor(Color.BLACK);
           tvfh1.setText("\tBowler");
           rowfh.addView(tvfh1);

           TextView tvfh2 = new TextView(this);
//        tvfh2.setTextSize(16);
           tvfh2.setTextColor(Color.BLACK);
           if (HUNDRED)
               tvfh2.setText("\t\t\tBalls");
           else
               tvfh2.setText("\t\t\tOver");
           rowfh.addView(tvfh2);

           TextView tvfh3 = new TextView(this);
//        tvfh3.setTextSize(16);
           tvfh3.setTextColor(Color.BLACK);
           if (HUNDRED)
               tvfh3.setText("\t\tDots");
           else
               tvfh3.setText("\t\tM.O");
           rowfh.addView(tvfh3);

           TextView tvfh4 = new TextView(this);
//        tvfh4.setTextSize(16);
           tvfh4.setTextColor(Color.BLACK);
           tvfh4.setText("\t\tRuns");
           rowfh.addView(tvfh4);

           TextView tvfh5 = new TextView(this);
//        tvfh5.setTextSize(16);
           tvfh5.setTextColor(Color.BLACK);
           tvfh5.setText("\t\tWkts");
           rowfh.addView(tvfh5);

           TextView tvfh6 = new TextView(this);
//        tvfh6.setTextSize(16);
           tvfh6.setTextColor(Color.BLACK);
           tvfh6.setText("\t\tWide");
           rowfh.addView(tvfh6);

           TextView tvfh7 = new TextView(this);
//        tvfh7.setTextSize(16);
           tvfh7.setTextColor(Color.BLACK);
           tvfh7.setText("\t\tNoball");
           rowfh.addView(tvfh7);
           fielding_table.addView(rowfh);
           temp = 0;


           RealmResults<Bowler> resultf = realm.where(Bowler.class).
                   equalTo("matchid", matchid).
                   equalTo("team", tFieldNo).
                   equalTo("innings", innings).findAll();

           resultf.load();
           for (Bowler bowler : resultf) {

               TableRow rowf = new TableRow(this);
               rowf.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, 50));
//            TableRow.LayoutParams lprf = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, 50);
//            rowf.setLayoutParams(lp);
//            rowf.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT, 1));
//            rowf.setBackgroundColor(Color.rgb(51, 51, 51));

               String f_name = realm.where(Player.class).
                       equalTo("matchid", matchid).
                       equalTo("team", tFieldNo).
                       equalTo("playerID", bowler.getPlayerID()).findFirst().getPlayerName();
               temp++;
               rowf.setPadding(10, 25, 10, 25);
               if (temp % 2 != 0)
                   rowf.setBackgroundResource(R.drawable.row_border);
               TextView tvf1 = new TextView(this);
//            tvf1.setTextSize(16);
               tvf1.setTextColor(Color.BLACK);
               tvf1.setText("\t" + f_name);
               rowf.addView(tvf1);

               TextView tvf2 = new TextView(this);
//            tvf2.setTextSize(16);
               tvf2.setTextColor(Color.BLACK);
               if (HUNDRED) {
                   tvf2.setText(" \t\t\t\t" + bowler.getTotalBalls());
               } else {
                   tvf2.setText(" \t\t\t\t" + (new
                           DecimalFormat("##.#").format(setOverDisplay(bowler.getOver(), bowler.getBalls()))));
               }
//            tvf2.setText("\t\t\t\t"+bowler.getOver());
               rowf.addView(tvf2);

               TextView tvf3 = new TextView(this);
//            tvf3.setTextSize(16);
               tvf3.setTextColor(Color.BLACK);
               if (HUNDRED)
                   tvf3.setText("\t\t\t" + bowler.getDots());
               else
                   tvf3.setText("\t\t\t" + bowler.getMaidenOver());
               rowf.addView(tvf3);

               TextView tvf4 = new TextView(this);
//            tvf4.setTextSize(16);
               tvf4.setTextColor(Color.BLACK);
               tvf4.setText("  \t\t\t" + bowler.getRuns());
               rowf.addView(tvf4);

               TextView tvf5 = new TextView(this);
//            tvf5.setTextSize(16);
               tvf5.setTextColor(Color.BLACK);
               tvf5.setText("  \t\t\t" + bowler.getWicket());
               rowf.addView(tvf5);

               TextView tvf6 = new TextView(this);
//            tvf6.setTextSize(16);
               tvf6.setTextColor(Color.BLACK);
               tvf6.setText("  \t\t\t" + bowler.getWides());
               rowf.addView(tvf6);

               TextView tvf7 = new TextView(this);
//            tvf7.setTextSize(16);
               tvf7.setTextColor(Color.BLACK);
               tvf7.setText("\t\t\t\t" + bowler.getNoBalls());
               rowf.addView(tvf7);

               fielding_table.addView(rowf);
           }


           Log.d("ScoreCard", "displayScoreCard end, batting team :" + battingTeam);
           Log.d("ScoreCard", "displayScoreCard end, fielding team :" + fieldingTeam);
           Log.d("ScoreCard", "displayScoreCard end, innings :" + c_innings);
           Log.d("ScoreCard", "displayScoreCard end, batting team No :" + battingTeamNo);
           Log.d("ScoreCard", "displayScoreCard end, fielding team No :" + fieldingTeamNo);

       }        // added } and else on 11/09/2020

       else {
           tv_batTeam.setText(tBat);
           tv_fieldTeam.setText(tField);

           batting_table = findViewById(R.id.batting_table);
           TableRow rowbh = new TableRow(this);
           rowbh.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, 50));
//        rowbh.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT, 1));
           rowbh.setPadding(10, 25, 10, 25);

           TextView tvbh12 = new TextView(this);
//        tvbh12.setTextSize(16);
           tvbh12.setText(" ");
           tvbh12.setTextColor(Color.BLACK);
           rowbh.addView(tvbh12);

           TextView tvbh13 = new TextView(this);
//        tvbh13.setTextSize(16);
           tvbh13.setText("");
           tvbh13.setTextColor(Color.BLACK);
           rowbh.addView(tvbh13);

           TextView tvbh1 = new TextView(this);
//        tvbh1.setTextSize(16);
           tvbh1.setTextColor(Color.BLACK);
           tvbh1.setText(" Player");
           rowbh.addView(tvbh1);


           TextView tvbh11 = new TextView(this);
//        tvbh11.setTextSize(16);
           tvbh11.setTextColor(Color.BLACK);
           tvbh11.setText(" \t\t");
           rowbh.addView(tvbh11);

//           if (abandoned_match_flag == 0) {

               RealmResults<Player> resultt = realm.where(Player.class).
                       equalTo("matchid", matchid).
                       equalTo("team", tBatNo).
                       findAll();

               if (resultt.size() > 0) {

                   for (Player players : resultt) { //resultb) {

              /* Player player = realm.where(Player.class).
                       equalTo("matchid", matchid).
                       equalTo("team", tBatNo).
                       equalTo("playerID", batsman.getBatsman_pID()).findFirst();

               Log.d("ScoreCardActivity", "displayScoreCard, batsman.getBatsman_pID()) : " +
                       batsman.getBatsman_pID());*/

//                   if (player != null) {
                       c = players.isCaptain();

                       wk = players.isWicketKeeper();

                       b_name = players.getPlayerName();
//                   } else
//                       Log.d("ScoreCardActivity", "displayScoreCard, Player : " + player);

//                   Log.d("ScoreCard", "displayScoreCard, batsman : " + batsman);

//            Log.d("scorecard", "displayScoreCard, playerName : "+batsman.getBatsmanName()+", captain : "+c+", wk : "+wk);

                       temp++;

                       TableRow rowb = new TableRow(this);
                       rowb.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, 50));
                       rowb.setPadding(10, 25, 10, 25);

                       TextView tvb12 = new TextView(this);
//            tvb12.setTextSize(10);
                       tvb12.setTextColor(Color.BLACK);
                       if (c) {
                           tvb12.setText(" *");
                       } else {
                           tvb12.setVisibility(View.INVISIBLE);
                       }
                       rowb.addView(tvb12);

                       TextView tvb13 = new TextView(this);
//            tvb13.setTextSize(10);
                       tvb13.setTextColor(Color.BLACK);
                       if (wk) {
                           tvb13.setText("+");
                       } else {
                           tvb13.setVisibility(View.INVISIBLE);
                       }
                       rowb.addView(tvb13);

                       TextView tvb1 = new TextView(this);
//            tvb1.setTextSize(16);
                       tvb1.setTextColor(Color.BLACK);
                       Log.e("ScoreCardActivity", "displayScoreCard, b_name : " + b_name);
                       tvb1.setText(" " + b_name);
                       rowb.addView(tvb1);
                   }
               }
           /*}

           else {

           }*/
       }
    }




//    void getFromSP() {
//
//        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
//        matchid = sharedPreferences.getInt("sp_match_id", 0);
//        matchID = sharedPreferences.getString("sp_match_ID", null);
////        teamA = sharedPreferences.getString("sp_teamA", null);
////        teamB = sharedPreferences.getString("sp_teamB", null);
////        type = sharedPreferences.getString("sp_match_type", null);
////        team1 = sharedPreferences.getInt("sp_team1", 0);
////        team2 = sharedPreferences.getInt("sp_team2", 0);
////
////        if (battingTeamNo == team1){
////            captain = sharedPreferences.getString("sp_captainA", null);
////            wicketkeeper = sharedPreferences.getString("sp_wkA", null);
////        }
////
////        if (battingTeamNo == team2){
////            captain = sharedPreferences.getString("sp_captainB", null);
////            wicketkeeper = sharedPreferences.getString("sp_wkB", null);
////        }
//
//    }

    void getFromIntent (){

        Intent i = getIntent();
        matchid = i.getIntExtra("matchid",0);
        matchID = i.getStringExtra("matchID");
        endOfInnings = i.getBooleanExtra("endofinnings", false);
        continueInnings = i.getBooleanExtra("continue_match", false);
        declare = i.getBooleanExtra("declare", false);
        forfeit = i.getBooleanExtra("forfeit", false);
        allout = i.getBooleanExtra("allout", false);
        c_innings = i.getIntExtra("innings", 0);
        forfeit_team = i.getIntExtra("forfeit_team", 0);
        forfeit_innings = i.getIntExtra("forfeit_innings", 0);
        Log.d("FORFEIT", "ScoreCard, getFromIntent 1, forfeit : " + forfeit);
        Log.d("FORFEIT", "ScoreCard, getFromIntent 2, forfeit_team : " + forfeit_team);
        Log.d("FORFEIT", "ScoreCard, getFromIntent 3, forfeit_innings : " + forfeit_innings);

//        matchStatus = i.getIntExtra("matchstatus", 0);
        endOfMatch = i.getBooleanExtra("endofmatch", false);
        inningsnotstarted = i.getBooleanExtra("inningsnotstarted", false);
//        SUPER_OVER = i.getBooleanExtra("SUPER_OVER", false);

        match_finished = i.getBooleanExtra("match_finished", false);

        back_type = i.getIntExtra("back_type", -1);
        view_result = i.getBooleanExtra("view", false);
        interval = i.getBooleanExtra("interval", false);
        session = i.getBooleanExtra("session", false);



        Log.d("ScoreCard", "getFromIntent, status :"+matchStatus);
        Log.d("ScoreCard", "getFromIntent, currentInnings :"+c_innings);
        Log.d("ScoreCard", "getFromIntent, match_finished :"+match_finished);
        Log.d("ScoreCard", "getFromIntent, SUPER_OVER :"+SUPER_OVER);

    }

//    void getFromScoring(){
//
//        Intent i = getIntent();
//        run = i.getIntExtra("runs", 0);
//        over = i.getFloatExtra("over", 0f);
//        battingTeam =i.getStringExtra("battingTeam");
//        fieldingTeam =i.getStringExtra("fieldingTeam");
//        battingTeamNo = i.getIntExtra("battingTeamNo", 0);
//        fieldingTeamNo = i.getIntExtra("fieldingTeamNo", 0);
//        innings = i.getIntExtra("innings", 0);
//        matchtype = i.getStringExtra("matchtype");
//        totalover = i.getIntExtra("totalover", 0);
//        striker = i.getStringExtra("striker");
//        nonStriker = i.getStringExtra("nonstriker");
//    }



    void setInnings(int c_innings){

        String string = "", string2 = "";
        if (forfeit_team == 1)
            string = teamA;
        else if (forfeit_team == 2)
            string = teamB;



        if (concede) {
            if (c_innings == conceded_innings) {
                if (conceded_team == 1)
                    string2 = teamA;
                else if (conceded_team == 2)
                    string2 = teamB;
                tv_message.setVisibility(View.VISIBLE);
                tv_message.setText("Match conceded by " + string2);
            }

            else
                tv_message.setVisibility(View.GONE);
        }

        Log.d("FORFEIT", "ScoreCard, setInnings 4, forfeit_team : " + forfeit_team);
        Log.d("FORFEIT", "ScoreCard, setInnings 5, string : " + string);
        Log.d("FORFEIT", "ScoreCard, setInnings 6, c_innings : " + c_innings);
        Log.d("FORFEIT", "ScoreCard, setInnings 7, forfeit_innings : " + forfeit_innings);
        Log.d("FORFEIT", "ScoreCard, setInnings 8, forfeit : " + forfeit);

        if (c_innings == 1) {
            if (forfeit) {
                if (c_innings == forfeit_innings) {
                    Log.d("FORFEIT", "ScoreCard, setInnings 9, c_innings == forfeit_innings");
                    tv_innings.setText("1st innings forfeit by " + string + " \t");
                }
                else {
                    Log.d("FORFEIT", "ScoreCard, setInnings 10, c_innings != forfeit_innings");
                    tv_innings.setText("1st innings\t");
                }
            }
            else
                tv_innings.setText("1st innings\t");
        }

        else if (c_innings == 2) {
            if (forfeit) {
                if (c_innings == forfeit_innings) {
                    Log.d("FORFEIT", "ScoreCard, setInnings 11, c_innings == forfeit_innings");
                    tv_innings.setText("2nd innings forfeit by " + string + "\t");
                }
                else {
                    Log.d("FORFEIT", "ScoreCard, setInnings 12, c_innings != forfeit_innings");
                    tv_innings.setText("2nd innings\t");
                }
            }
            else
                tv_innings.setText("2nd innings\t");
        }

        else if (c_innings == 3) {
            if (forfeit) {
                if (c_innings == forfeit_innings) {
                    Log.d("FORFEIT", "ScoreCard, setInnings 13, c_innings == forfeit_innings");
                    tv_innings.setText("3rd innings forfeit by " + string + "\t");
                }
                else {
                    Log.d("FORFEIT", "ScoreCard, setInnings 7, c_innings != forfeit_innings");
                    tv_innings.setText("3rd innings\t");
                }
            }
            else
                tv_innings.setText("3rd innings\t");
        }

        else if (c_innings == 4)
            tv_innings.setText("4th innings");
        else if (c_innings == 99)
            tv_innings.setText("SUPER OVER 1st innings");
        else if (c_innings == 100)
            tv_innings.setText("SUPER OVER 2nd innings");
        else
            tv_innings.setText("");
    }


    void checkMatchType(String matchtype){

        if(matchtype != null){

            if (matchtype.matches("Custom Type")){
                if (totalover == 1000)
                {
                    matchtype = "Custom ";
                }

                else {
                    matchtype = totalover + " over";
                }
            }

            else if (matchtype.matches("100")) {
                matchtype = "100 Balls";
            }

            tv_type.setText(matchtype);

        }

    }



//    void readRunOrWicket() {
//
//        AlertDialog.Builder builder = new AlertDialog.Builder(ScoreCardActivity.this);
//        builder.setIcon(R.drawable.ball);
//        builder.setCancelable(false);
//        if (wbr)
//            builder.setTitle("by runs");
//        else if (wbw)
//            builder.setTitle("by wickets");
//
//        final EditText input = new EditText(this);
//
//        input.setInputType(InputType.TYPE_CLASS_NUMBER);
//        builder.setView(input);
////        Log.d("Test", "inside AddRuns()");
//        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
//
//            //            @Override
//            public void onClick(DialogInterface dialog, int which) {
//
//                if (input.getText().toString().matches("")){
//                    Log.d("readRunOrWicket", "no input ");
//                }
//
//                else {
//
//                    if (wbr) {
//                        win_runs = Integer.parseInt(input.getText().toString());
//                        win_wickets = 0;
//                    }
//
//                    else if (wbw) {
//                        win_wickets = Integer.parseInt(input.getText().toString());
//                        win_runs = 0;
//                    }
//
//                    saveMatchResult(win_team, win_runs, win_wickets);
//
//
////        startActivity(new Intent(ScoreCardActivity.this, DisplayResultActivity.class));
//
//                }
//            }
//        });
//
//        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                dialog.cancel();
//            }
//        });
//
//        AlertDialog alert = builder.create();
//        alert.show();
//
//        builderText = "";
//    }



//    void readWonBy() {
//
//        String[] wonby = {"Runs", "Wickets"};
//
//        AlertDialog.Builder wonByBuilder = new AlertDialog.Builder(this);
//        wonByBuilder.setIcon(R.drawable.ball);
//        wonByBuilder.setCancelable(false);
//        wonByBuilder.setTitle("Select Dismissed player");
//        wonByBuilder.setSingleChoiceItems(wonby, checkedItem, new DialogInterface.OnClickListener() {
//
//            @Override
//            public void onClick(DialogInterface dialog, int position) {
//
//                if (position == 0) {
//                    wbr = true;
//                    wbw = false;
//                }
//
//                else if (position == 1) {
//                    wbr = false;
//                    wbw = true;
//                }
//
//
//            }
//
//        });
//        wonByBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
//
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//
//                if ((wbr && !wbw) || (!wbr && wbw))
//                    readRunOrWicket();
//                dialog.dismiss();
//            }
//        });
//        wonByBuilder.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                dialog.dismiss();
//            }
//        });
//        AlertDialog alert = wonByBuilder.create();
//        alert.show();
//    }



//    void readWinnerTeam() {
//
//        String[] teams = {teamA, teamB};
//
//        AlertDialog.Builder winnerBuilder = new AlertDialog.Builder(this);
//        winnerBuilder.setIcon(R.drawable.ball);
//        winnerBuilder.setCancelable(false);
//        winnerBuilder.setTitle("Select Winner Team");
//        winnerBuilder.setSingleChoiceItems(teams, checkedItem, new DialogInterface.OnClickListener() {
//
//            @Override
//            public void onClick(DialogInterface dialog, int position) {
//
//                winner = Arrays.asList(teams).get(position);
//                win_team = position + 1;
//
//            }
//
//        });
//        winnerBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
//
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//
//                if ((winner != null/* || !winner.matches("")*/) && win_team > 0)
//                    readWonBy();
//                dialog.dismiss();
//            }
//        });
//        winnerBuilder.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                dialog.dismiss();
//            }
//        });
//        AlertDialog alert = winnerBuilder.create();
//        alert.show();
//    }


    // to save result


    void readResult() {

        String[] singleInningsMatchResult = {"Match Won", "Match Tied", "Match Abandoned", "No Result", "Match Concession"};
        String[] multiInningsMatchResult = {"Match Won", "Match Tied", "Match Drawn", "Match Abandoned"};

        if (total_innings == 2) {
            result = new String[singleInningsMatchResult.length];
            for (int i = 0; i < singleInningsMatchResult.length; i++) {
                result[i] = singleInningsMatchResult[i];
            }
        }

        else
        if (total_innings == 4) {
            result = new String[multiInningsMatchResult.length];
            for (int i = 0; i < multiInningsMatchResult.length; i++) {
                result[i] = multiInningsMatchResult[i];
            }
        }

        AlertDialog.Builder winnerBuilder = new AlertDialog.Builder(this);
        winnerBuilder.setIcon(R.drawable.ball);
        winnerBuilder.setCancelable(false);
        winnerBuilder.setTitle("Select Result");
//        if (totalInnings == 2)
        winnerBuilder.setSingleChoiceItems(result, checkedItem, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int position) {


                pos = position;
                matchResult = Arrays.asList(result).get(position);
                if (position == 0) {
                    readWinnerTeam();
                    dialog.dismiss();
                }
            }

        });
        winnerBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();
                // Added here on 16/06/2021
                if (pos != 0) {

                    if (total_innings == 2) {
                        switch (pos) {

                            case 1:
                                type = "td";
                                break;

                            case 2:
                                type = "ab";
                                break;

                            case 3:
                                type = "nr";
                                break;

                            case 4:
                                type = "con";
                                break;

                        }
                    }

                    else
                    if (total_innings == 4) {
                        switch (pos) {

                            case 1:
                                type = "td";
                                break;

                            case 2:
                                type = "dr";
                                break;

                            case 3:
                                type = "ab";
                                break;

                        }
                    }

                    saveMatchResult(0,type,0, matchResult);
                }

            }
        });
        winnerBuilder.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog alert = winnerBuilder.create();
        alert.show();
    }


    void readWinnerTeam() {

        String[] teams = {teamA, teamB};

        AlertDialog.Builder winnerBuilder = new AlertDialog.Builder(this);
        winnerBuilder.setIcon(R.drawable.ball);
        winnerBuilder.setCancelable(false);
        winnerBuilder.setTitle("Select Team");
        winnerBuilder.setSingleChoiceItems(teams, checkedItem, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int position) {

                dialog.dismiss();
                winner = Arrays.asList(teams).get(position);
                win_team = position + 1;
                if (win_team == 1)
                    win_team_name = teamA;
                else if(win_team == 2)
                    win_team_name = teamB;
                if (total_innings == 2)
                    readWonBy();
                else if (total_innings == 4)
                    multiInningsWonBy();
            }

        });
        winnerBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();
            }
        });
        winnerBuilder.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog alert = winnerBuilder.create();
        alert.show();
    }


    void readWonBy() {

        String[] wonby = {"Runs", "Wickets"};

        AlertDialog.Builder wonByBuilder = new AlertDialog.Builder(this);
        wonByBuilder.setIcon(R.drawable.ball);
        wonByBuilder.setCancelable(false);
        wonByBuilder.setTitle("Won by ");
        wonByBuilder.setSingleChoiceItems(wonby, checkedItem, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int position) {
                dialog.dismiss();

                if (position == 0) {
                    wbr = true;
                    wbw = false;
                    title = "by runs";    // added on 16/06/2021
                }

                else if (position == 1) {
                    wbr = false;
                    wbw = true;
                    title = "by wickets";    // added on 16/06/2021
                }

                readRunOrWicketSingle(title); // Updated on 16/06/2021
            }

        });
        wonByBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();
            }
        });
        wonByBuilder.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog alert = wonByBuilder.create();
        alert.show();
    }



    // setted only for multi innings on 11/12/2021
    void readRunOrWicket(String title) {

        AlertDialog.Builder builder = new AlertDialog.Builder(ScoreCardActivity.this);
        builder.setIcon(R.drawable.ball);
        builder.setCancelable(false);
        builder.setTitle(title);

        final EditText input = new EditText(this);

        input.setInputType(InputType.TYPE_CLASS_NUMBER);
        builder.setView(input);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                if (input.getText().toString().matches("")){
                    Log.d("readRunOrWicket", "no input ");
                }

                else {

                    value = Integer.parseInt(input.getText().toString());

                    if (wbr) {
                        type = "wr";
                        win_runs = Integer.parseInt(input.getText().toString());
                        win_wickets = 0;
                    }

                    else if (wbw) {
                        type = "ww";
                        win_wickets = Integer.parseInt(input.getText().toString());
                        win_runs = 0;
                    }

                    else if (wbi) {//r) {
                        type = "wi";
                        win_runs = Integer.parseInt(input.getText().toString());
                        win_wickets = 0;
                    }

                    if (type.matches("wr"))
                        matchResult = win_team_name + " won by " + value + " runs";
                    else if (type.matches("ww"))
                        matchResult = win_team_name + " won by " + value + " wickets";
                    else if (type.matches("wi"))
                        matchResult = win_team_name + " won by an innings and " + value + " runs";

                    if (overReduced)
                        matchResult = matchResult + "(" + rain_rule + ")";

                    saveMatchResult(win_team, type, value, matchResult);
                }
            }
        });

        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        AlertDialog alert = builder.create();
        alert.show();


    }




    void saveMatchResult(int team, String type, int value, String matchResult) {

        Log.d("result", "saveMatchResult 1, team : " + team +
                ", type : " + type + ", value : " + value + ", matchResult : " + matchResult);



        //saving match result to MATCH table
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

                        if (match != null) {
                            match.setResult(matchResult);
                            match.setWinner_team(team);
                            match.setType(type);
                            match.setMargin(value);
//                            match.setMatchResult(message);

                        /*match.setWin_by_run(run);
                        match.setWin_by_wicket(wickets);
                        if (run > 0)
                            match.setMatch_result(winner + " won by " + run);
                        else if (wickets > 0)
                            match.setMatch_result(winner + " won by " + wickets);*/
                            realm.copyToRealmOrUpdate(match);

                            Log.d("result", "saveMatchResult 2, match : " + match);
                        }
                    } catch (RealmPrimaryKeyConstraintException e) {

                        Log.d("readResult", " Exception : "+e);
                    }
                }
            });
        }

        catch (RealmException e) {
            Log.d("test", "Exception : " + e);
        }

        finally {
            if (realm != null) {
                realm.close();
            }
        }



        // Adding condition on 11/11/2021
        RealmResults<Events> results = realm.where(Events.class).
                equalTo("matchid", matchid).
                equalTo("syncstatus", 0).
                findAll();

        if (results.size() > 0) {
            // Added on 20/04/2021
            Intent intent= new Intent(getBaseContext(), ScheduledService.class);
            getBaseContext().startService(intent);
            // === till here
        } else {
            postJSON(/*1*/);
        }

        Intent i = new Intent(ScoreCardActivity.this, DisplayResultActivity.class);
        i.putExtra("matchid", matchid);
        i.putExtra("matchID", matchID);
        freeMemory();
        startActivity(i);
        finish();

    }



    private void postJSON(/*int flag*/) {

        Log.d("result", "postJSON  ");

        if (isNetworkAvailable()) {

             config = new RealmConfiguration.Builder()
                .name(AppConstants.GAME_ID + ".realm")
                .deleteRealmIfMigrationNeeded()
                .build();
        realm = Realm.getInstance(config);
            Match match = realm.where(Match.class).equalTo("matchid", matchid).findFirst();


            if (match != null) {

                Log.d("result", "postJSON 1, match : " + match);

                JSONObject jsonObject = new JSONObject();
                /*if (flag == 2) {

                    try {
                        jsonObject.put("matchID", match.getMatchID());
                        jsonObject.put("manofmatchID", match.getMom_pid());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else if (flag == 1) {*/

                    try {
                        jsonObject.put("matchID", match.getMatchID());
                        jsonObject.put("type", match.getType());
                        jsonObject.put("margin", match.getMargin());
                        jsonObject.put("winner", match.getWinner_team());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
//                }

                JSONObject jsonFeed = new JSONObject();
                try {
//                    if (flag == 1)
                        jsonFeed.put("MatchResult", jsonObject);
//                    else if (flag == 2)
//                        jsonFeed.put("ManoftheMatch", jsonObject);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                JSONObject postparams = new JSONObject();
                try {
                    postparams.put("title", "CHASE_POST");
                    postparams.put("feed", jsonFeed);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                        Constants.CHASE_CRICKET_MATCH_API,
                        postparams,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {

                                Log.e("result", "response : " + response);
                                // updated the match model
                                try {

                                    if (response.getInt("status") == 200 && !(response.getBoolean("error"))) {

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
                                                                        response.getString("match")).
                                                                findFirst();

                                                        if (match1 != null) {

                                                            match1.setResult_sync(1);
                                                            match1.setEditResult(0);
                                                            bgRealm.copyToRealm(match1);
                                                            Toast.makeText(getApplicationContext(),
                                                                    response.getString("message"),
                                                                    Toast.LENGTH_SHORT).show();
                                                            Log.d("matchSync", "captain, match : " + match1);
                                                        }

                                                        else
                                                            Log.e("RESPONSE", "Captain, match not found");

                                                    } catch (JSONException e) {
                                                        e.printStackTrace();
                                                    } catch (RealmPrimaryKeyConstraintException e) {
//                                                    progress.dismiss();
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

                                    }


                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }

                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {

                                Log.e("result", "volley, onErrorResponse  : " + error);
                            }
                        });

                MyApplicationClass.getInstance(getApplicationContext()).addToRequestQueue(jsonObjReq, "postRequest");
                Log.d("result", "jsonObjReq  : " + jsonObjReq);
                Log.d("result", "postparams  : " + postparams);

            }
        }
    }






    /*void saveMatchResult(int team, int run, int wickets) {

        //saving match result to MATCH table
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

                        match.setWinner_team(team);
                        match.setWin_by_run(run);
                        match.setWin_by_wicket(wickets);
                        if (run > 0)
                            match.setResult(winner + " won by " + run);
                        else if (wickets > 0)
                            match.setResult(winner + " won by " + wickets);
                        realm.copyToRealmOrUpdate(match);
                    } catch (RealmPrimaryKeyConstraintException e) {

                        Log.d("readResult", " Exception : "+e);
                    }
                }
            });
        }

        catch (RealmException e) {
            Log.d("test", "Exception : " + e);
        }

        finally {
            if (realm != null) {
                realm.close();
            }
        }


        Intent i = new Intent(ScoreCardActivity.this, DisplayResultActivity.class);
        i.putExtra("matchid", matchid);
        i.putExtra("matchID", matchID);
        startActivity(i);
        finish();
    }*/




    void saveToSP(){

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        mEditor = sharedPreferences.edit();

        mEditor.putInt("sp_match_id", matchid);
        mEditor.putString("sp_match_ID", matchID);
        mEditor.putInt("sp_current_innings", c_innings);
        mEditor.putString("sp_teamA", teamA);
        mEditor.putString("sp_teamB", teamB);
        mEditor.putBoolean("SUPER_OVER", SUPER_OVER);

        mEditor.apply();
    }




    void displayOut(int ot, String fielder, String bowler, boolean wk_fielder_flag, int fid, int bid, int flag){

        switch (ot){

            case 0:
                otype = "b " + bowler;
//                otype = "";
//                bowler_details = "b " + bowler;
                break;

            case 1:
                if (wk_fielder_flag)                                        // Added on 18/03/2021
                    otype = "c +" + fielder + "   b " + bowler;             // Added on 18/03/2021
                else                                                        // Added on 13/12/2021
                    if ((fid > 0) && (bid > 0) && (fid == bid)) {           // Added on 13/12/2021
                        otype = "c & b " + bowler;                          // Added on 13/12/2021
                } else
                    if (flag == 1) {                                        // Added on 14/12/2021
                        otype = "c sub(" + fielder + ")   b " + bowler;     // Added on 14/12/2021
                } else                                                      // Added on 18/03/2021
                    otype = "c " + fielder + "   b " + bowler;
//                otype = "c " + fielder;
//                bowler_details = " b " + bowler;
                break;

            case 2:
                otype = "run out   (" + fielder + ")";
                break;

            case 3:
//                otype = "s";
                if (wk_fielder_flag)                                       // Added on 18/03/2021
                    otype = "st +" + fielder + "   b " + bowler;           // Added on 18/03/2021
                else                                                       // Added on 14/12/2021
                    if (flag == 1) {                                       // Added on 14/12/2021
                        otype = "st sub(+" + fielder + ")   b " + bowler;  // Added on 14/12/2021
                } else                                               // Added on 18/03/2021
                    otype = "st " + fielder + "   b " + bowler;
                break;

            case 4:
                otype = "lbw   b " + bowler;
                break;

            case 5:
                otype = "hit wicket   b " + bowler;
                break;

            case 6:
                otype = "hit the ball twice";
                break;

            case 7:
//                otype = "of";
                otype = "obstructing the field";
                break;

            case 8:
                otype = "handled the ball";
                break;

            case 9:
                otype = "timed out";
                break;

            case 10:
                otype = "retired hurt";
                break;

            case 11:
//                otype = "rta";
                otype = "absent";
                break;

            case 12:
                otype = "retired out";
                break;
        }
    }




    public boolean checkInnings(String type, String inningsType){

        boolean flag = false;

        if (type.matches("ODI") || inningsType.matches("single"))
            flag = true;

        return flag;
    }


    public float setOverDisplay(int over, int balls){

        float result = 0f;

//        result = ((over * 10) + balls)/ 10;
        result = over + (balls / 10f);

        return result;
    }



    @Override
    public void onBackPressed() {

        if (back_type == 1 && !endOfInnings && !declare && !endOfMatch && !allout && !forfeit) {
//            Intent intent = new Intent(this, ScoringActivity.class);      Commented on 27/07/2021
            Intent intent = new Intent(this, UpdatedScoringActivity.class);    // Added on 27/07/2021
            intent.putExtra("interval", interval);
            intent.putExtra("midscore", true);
            intent.putExtra("session", session);
            startActivity(intent);
            freeMemory();
            finish();
        }

        else if (back_type == 2){
            startActivity(new Intent(this, HomeActivity.class));
            freeMemory();
            finish();
        }
        else if (match_finished) {
            /*startActivity(new Intent(this, DisplayResultActivity.class));
            finish();*/
            Intent i = new Intent(ScoreCardActivity.this, DisplayResultActivity.class);
            i.putExtra("matchid", matchid);
            i.putExtra("matchID", matchID);
            startActivity(i);
            freeMemory();
            finish();
        }

        else {
            startActivity(new Intent(this, MatchListActivity.class));
            freeMemory();
            finish();
        }
    }




    private boolean isNetworkAvailable(){
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }



    public void freeMemory() {
        System.runFinalization();
        Runtime.getRuntime().gc();
        System.gc();
    }


    // added on 20/10/2020
    public void displayPlayers(int team) {

        Log.d("displayplayers", "ScoreCard, team : " + team);
        Log.d("displayplayers", "ScoreCard, run : " + run);
        Log.d("displayplayers", "ScoreCard, wicket : " + wicket);
        Log.d("displayplayers", "ScoreCard, over : " + over);
        Log.d("displayplayers", "ScoreCard, c_innings : " + c_innings);
        Log.d("displayplayers", "ScoreCard, tBat : " + tBat);
        Log.d("displayplayers", "ScoreCard, c_innings : " + c_innings);

        if (click > 0) {
            refreshScoreCard();
        }

        if (team == 1){

            first.setEnabled(false);
            second.setEnabled(true);
//            third.setEnabled(true);
//            forth.setEnabled(true);
        }

        else if (team == 2){

            first.setEnabled(true);
            second.setEnabled(false);
           /* third.setEnabled(true);
            forth.setEnabled(true);*/
        }

        tv_innings.setText("");
        if (abandoned_after_toss == 1) {

            if (team == 1) {
                tv_batTeam.setText(teamA);
                tv_fieldTeam.setText("");
            } else {
                tv_batTeam.setText(teamB);
                tv_fieldTeam.setText("");
            }

            ll_fieldingTeam.setVisibility(View.GONE);

        } else {

            if (team == 1) {
                tv_batTeam.setText(teamA);
                tv_fieldTeam.setText("");
            } else {
                tv_batTeam.setText(teamB);
                tv_fieldTeam.setText("");
            }
        }

        tv_run.setText("");
        tv_wicket.setText("");
        tv_div.setVisibility(View.GONE);
        tv_wicket.setVisibility(View.GONE);
        tv_over.setText("");
        tv_innings.setText("");
        tv_braces_open.setVisibility(View.GONE);
        tv_braces_closed.setVisibility(View.GONE);

        // displaying table
        batting_table = findViewById(R.id.batting_table);
        TableRow rowbh = new TableRow(this);
        rowbh.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, 50));
//        rowbh.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT, 1));
        rowbh.setPadding(10, 25, 10, 25);

        TextView tvbh12 = new TextView(this);
        tvbh12.setTextSize(13);
        tvbh12.setText(" ");
        tvbh12.setTextColor(Color.BLACK);
        rowbh.addView(tvbh12);

        TextView tvbh13 = new TextView(this);
        tvbh13.setTextSize(13);
        tvbh13.setText("");
        tvbh13.setTextColor(Color.BLACK);
        rowbh.addView(tvbh13);

        TextView tvbh1 = new TextView(this);
        tvbh1.setTextSize(13);
        tvbh1.setTextColor(Color.BLACK);
        tvbh1.setText(" Player");
        rowbh.addView(tvbh1);

        batting_table.addView(rowbh);

        //displaying players
        RealmResults<Player> resultt = realm.where(Player.class).
                equalTo("matchid", matchid).
                equalTo("team", team).
                equalTo("retired", false).findAll();

        if (resultt.size() > 0) {

            for (Player player : resultt) {

                if (player != null) {

                    c = player.isCaptain();
                    wk = player.isWicketKeeper();
                    b_name = player.getPlayerName();

                } else
                    Log.d("displayplayers", "ScoreCard, Player : " + player);

                temp++;

                TableRow rowb = new TableRow(this);
                rowb.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, 50));
                rowb.setPadding(10, 25, 10, 25);

                if (temp % 2 != 0) {
                    rowb.setBackgroundResource(R.drawable.row_border);
                }

                TextView tvb12 = new TextView(this);
//            tvb12.setTextSize(10);
                tvb12.setTextColor(Color.BLACK);
                if (c) {
                    tvb12.setText(" *");
                } else {
                    tvb12.setVisibility(View.INVISIBLE);
                }
                rowb.addView(tvb12);

                TextView tvb13 = new TextView(this);
//            tvb13.setTextSize(10);
                tvb13.setTextColor(Color.BLACK);
                if (wk) {
                    tvb13.setText("+");
                } else {
                    tvb13.setVisibility(View.INVISIBLE);
                }
                rowb.addView(tvb13);

                TextView tvb1 = new TextView(this);
//            tvb1.setTextSize(16);
                tvb1.setTextColor(Color.BLACK);
                Log.e("displayplayers", "ScoreCard, b_name : " + b_name);
                tvb1.setText(" " + b_name);
                rowb.addView(tvb1);

                TextView tvb2 = new TextView(this);
                tvb2.setTextSize(13);
                tvb2.setText("\t\t\t\t\t");
                tvb2.setTextColor(Color.BLACK);
                rowb.addView(tvb2);

                TextView tvb3 = new TextView(this);
                tvb3.setTextSize(13);
                tvb3.setText("\t\t\t\t\t");
                tvb3.setTextColor(Color.BLACK);
                rowb.addView(tvb3);

                TextView tvb4 = new TextView(this);
                tvb4.setTextSize(13);
                tvb4.setText("\t\t\t\t\t");
                tvb4.setTextColor(Color.BLACK);
                rowb.addView(tvb4);

                TextView tvb5 = new TextView(this);
                tvb5.setTextSize(13);
                tvb5.setText("\t\t\t\t\t");
                tvb5.setTextColor(Color.BLACK);
                rowb.addView(tvb5);

                TextView tvb6 = new TextView(this);
                tvb6.setTextSize(13);
                tvb6.setText("\t\t\t\t\t");
                tvb6.setTextColor(Color.BLACK);
                rowb.addView(tvb6);

                TextView tvb7 = new TextView(this);
                tvb7.setTextSize(13);
                tvb7.setText("\t\t\t\t\t");
                tvb7.setTextColor(Color.BLACK);
                rowb.addView(tvb7);

                TextView tvb8 = new TextView(this);
                tvb8.setTextSize(13);
                tvb8.setText("\t\t\t\t\t");
                tvb8.setTextColor(Color.BLACK);
                rowb.addView(tvb8);

                TextView tvb9 = new TextView(this);
                tvb9.setTextSize(13);
                tvb9.setText("\t\t\t\t\t");
                tvb9.setTextColor(Color.BLACK);
                rowb.addView(tvb9);

                batting_table.addView(rowb);
            }
        }
    }


    // added on 21/10/2020
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.score_card_menu, menu);//Menu Resource, Menu
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.display_match_details:
                displayDetails();

                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }



    public void displayDetails() {

        if (matchid == 0)
            displayAlert();
        else {
                Intent intent = new Intent(ScoreCardActivity.this, MatchDetailsActivity.class);
                intent.putExtra("matchid", matchid);
                startActivity(intent);
        }
    }


    public void displayAlert() {

        AlertDialog alertDialog = new AlertDialog.Builder(ScoreCardActivity.this).create();
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


    // Added on 16/04/2021
    public void multiInningsWonBy() {

//        readWonBy();
        String[] wonby = {"An Innings", "Runs", "Wickets"};

        AlertDialog.Builder wonByBuilder = new AlertDialog.Builder(this);
        wonByBuilder.setIcon(R.drawable.ball);
        wonByBuilder.setCancelable(false);
        wonByBuilder.setTitle("Won by ");
        wonByBuilder.setSingleChoiceItems(wonby, checkedItem, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int position) {
                dialog.dismiss();

                if (position == 0) {
                    wbi = true;
                    wbr = false;
                    wbw = false;
                    title = "by an innings and runs";    // added on 16/06/2021
                }

                else if (position == 1) {
                    wbi = false;
                    wbr = true;
                    wbw = false;
                    title = "by runs";    // added on 16/06/2021
                }
                else if (position == 2) {
                    wbi = false;
                    wbr = false;
                    wbw = true;
                    title = "by wickets";    // added on 16/06/2021
                }

                /* Commented on 16/06/2021
                if (wbi)
                    multiInningsRunOrWicket();

                else*/
                    readRunOrWicket(title);

//                multiInningsRunOrWicket();
            }

        });
        wonByBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();
            }
        });
        wonByBuilder.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog alert = wonByBuilder.create();
        alert.show();
    }

/*
    void multiInningsRunOrWicket() {

        String[] anInningsAnd = {"Runs", "Wickets"};

        AlertDialog.Builder wonByBuilder = new AlertDialog.Builder(this);
        wonByBuilder.setIcon(R.drawable.ball);
        wonByBuilder.setCancelable(false);
        wonByBuilder.setTitle("An Innings and ");
        wonByBuilder.setSingleChoiceItems(anInningsAnd, checkedItem, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int position) {
                dialog.dismiss();

                if (position == 0) {
                    wbir = true;
                    wbiw = false;
                }

                else if (position == 1) {
                    wbir = false;
                    wbiw = true;
                }

                readRunOrWicket();
            }

        });
        wonByBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();
            }
        });
        wonByBuilder.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog alert = wonByBuilder.create();
        alert.show();
    }
*/


    // Updated on 11/12/2021
    // for single innings
    void readRunOrWicketSingle(String title) {

        boolean checked = false;

        View resultView = View.inflate(this, R.layout.result, null);
        EditText editText = (EditText) resultView.findViewById(R.id.r_value);
        CheckBox cb_rr = (CheckBox) resultView.findViewById(R.id.r_rainrule);

        AlertDialog.Builder builder = new AlertDialog.Builder(ScoreCardActivity.this);
        builder.setIcon(R.drawable.ball);
        builder.setView(resultView);
        builder.setCancelable(false);
        builder.setTitle(title);

//        final EditText input = new EditText(this);
//
//        input.setInputType(InputType.TYPE_CLASS_NUMBER);
//        builder.setView(resultView);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                if (editText.getText().toString().matches("")){
                    Log.d("readRunOrWicket", "no input ");
                }

                else {

                    value = Integer.parseInt(editText.getText().toString());
                    boolean checked = cb_rr.isChecked();

                    if (wbr) {
                        type = "wr";
                        win_runs = Integer.parseInt(editText.getText().toString());
                        win_wickets = 0;
                    }

                    else if (wbw) {
                        type = "ww";
                        win_wickets = Integer.parseInt(editText.getText().toString());
                        win_runs = 0;
                    }

                    /* COMMENTED ON 13/12/2021
                    else if (wbi) {//r) {
                        type = "wi";
                        win_runs = Integer.parseInt(editText.getText().toString());
                        win_wickets = 0;
                    }*/

                    if (type.matches("wr"))
                        matchResult = win_team_name + " won by " + value + " runs";
                    else if (type.matches("ww"))
                        matchResult = win_team_name + " won by " + value + " wickets";
                    /* COMMENTED ON 13/12/2021
                    else if (type.matches("wi"))
                        matchResult = win_team_name + " won by an innings and " + value + " runs";*/

                    /* commented on 11/12/2021
                    if (overReduced)
                        matchResult = matchResult + "(" + rain_rule + ")";*/

                    if (checked)
                        matchResult = matchResult + "(rain rule)";

                    saveMatchResult(win_team, type, value, matchResult);

                }
            }
        });

        builder.setNegativeButton("CANCEL", null);
        AlertDialog alert = builder.create();
        alert.show();
    }



}
