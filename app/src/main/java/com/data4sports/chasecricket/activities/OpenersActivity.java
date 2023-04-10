package com.data4sports.chasecricket.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
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
import com.data4sports.chasecricket.models.Match;
import com.data4sports.chasecricket.models.MatchNotes;
import com.data4sports.chasecricket.models.MatchOfficials;
import com.data4sports.chasecricket.models.MyApplicationClass;
import com.data4sports.chasecricket.models.Openers;
import com.data4sports.chasecricket.models.Player;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;
import io.realm.Sort;
import io.realm.exceptions.RealmException;
import io.realm.exceptions.RealmPrimaryKeyConstraintException;

public class OpenersActivity extends AppCompatActivity {

    TextView batting_team_textView, fielding_team_textView;
    Spinner spinner_striker, spinner_non_striker;
    Spinner spinner_bowler;// spinner_next_bowler;
    Button next;
    LinearLayout ll_next_bowler;

    int matchid, strID = 0, nstrID = 0, bowlerID = 0, nextBowlerID = 0, currentInnings = 0,
            bTeam = 0, fTeam = 0, super_over_innings = 0, twin = 0, sessionId = 0;
    String matchID, teamA, teamB, decision, toss_winner, battingTeam, fieldingTeam, status = null,
            matchNote = "", striker = null, non_striker = null, bowler = null, next_bowler = null,
            batting, fielding, capA, capB, vcA, vcB, wkA, wkB;
    boolean sflag = false, bflag = false, SUPER_OVER = false, RUN = false, continueInnings = false,
            followon = false, matchnote = false;

    AlertDialog.Builder addNoteBuilder;
    Realm realm;

    ArrayList<String> battingList, fieldingList;
    ArrayAdapter<String> adapterBattingTeam, adapterFieldingTeam;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor mEditor;
    private ProgressDialog progress;
    ImageButton back;

    RealmConfiguration config;

//    TextView test;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_openers);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.custom_action_bar_layout);
        View view = getSupportActionBar().getCustomView();

        back = (ImageButton) view.findViewById(R.id.action_bar_back);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*finish();*/
                Log.e("scoring", "oncreate, back button pressd");

                onBackPressed();
            }
        });

        sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(this);

        /*// Added on 02/08/2021
        Intent intent= new Intent(getBaseContext(), ScheduledService.class);
        getBaseContext().startService(intent);
        // === till here*/

        config = new RealmConfiguration.Builder()
                .name(AppConstants.GAME_ID + ".realm")
                .deleteRealmIfMigrationNeeded()
                .build();
        realm = Realm.getInstance(config);


        Log.d("Openers", "onCreate, currentInnings : " + currentInnings);


//        test = findViewById(R.id.test);


        assignVariables();

        getFromSP();
        getFromIntent();


        readFromRealm();

//        setTeamNames();
        setSpinnerValues();

        checkAlreadySelected();


        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                checkSpinnerValues();
            }
        });

    }


    void assignVariables() {

        Log.d("Openers", "assignVariables, currentInnings : " + currentInnings);

        batting_team_textView = findViewById(R.id.txt_batting_team);
        fielding_team_textView = findViewById(R.id.txt_fielding_team);
        spinner_striker = findViewById(R.id.strkr_teamA_spinner);
        spinner_non_striker = findViewById(R.id.nstrkr_teamA_spinner);
        spinner_bowler = findViewById(R.id.bwlr_teamB_spinner);
//        spinner_next_bowler = findViewById(R.id.nbwlr_teamB_spinner);
        next = findViewById(R.id.opener_next_btn);
//        ll_next_bowler = findViewById(R.id.ll_next_bowler);

        battingList = new ArrayList<String>();
        fieldingList = new ArrayList<String>();
    }


    void getFromSP() {


        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
//        teamA = sharedPreferences.getString("sp_teamA", null);
//        teamB = sharedPreferences.getString("sp_teamB", null);
//        toss_winner = sharedPreferences.getString("sp_toss_winner", null);
//        decision = sharedPreferences.getString("sp_decision", null);
        matchID = sharedPreferences.getString("sp_match_ID", null);
        matchid = sharedPreferences.getInt("sp_match_id", 0);
        super_over_innings = sharedPreferences.getInt("sp_super_over_innings", 0);
//        SUPER_OVER = sharedPreferences.getBoolean("SUPER_OVER", false);
        RUN = sharedPreferences.getBoolean("RUN", false);
        twin = sharedPreferences.getInt("toss_winner", 0);
        Log.d("matchid1", "" + matchid);
        Log.d("matchID1", "" + matchID);
//        Match match = realm.where(Match.class).equalTo("matchid", matchid).findFirst();
//        if (match != null)
//            SUPER_OVER = match.isSUPER_OVER();
//
//        if (SUPER_OVER) {
//
//            if (super_over_innings == 0) {
//                super_over_innings = 1;
//                currentInnings = 99;
//            }
//
//            else if (super_over_innings == 1)
//                ++currentInnings;
//
////            ll_next_bowler.setVisibility(View.INVISIBLE);
//
//        }
//
//        else {


        Match match = realm.where(Match.class).
                equalTo("matchid", matchid).findFirst();
        if (match != null) {

            if (match.isCaptain())//isToss())
                currentInnings = 1;
            else
                currentInnings = sharedPreferences.getInt("sp_current_innings", 0) + 1;

            /*sessionId = match.getStatusId();*/
        }
//            ll_next_bowler.setVisibility(View.VISIBLE);
//        }

        Log.v("openers", "getFromSP, currentInnings : " + currentInnings);

        Log.d("openers", "getFromSP, match : " + matchid);
//        Log.d("openers", "getFromSP, Team 1: "+teamA);
//        Log.d("openers", "getFromSP, Team 2: "+teamB);
//        Log.d("test", "toss: "+toss_winner);
//        Log.d("test", "decision: "+decision);
    }


    void getFromIntent() {

        Intent intent = getIntent();
        battingTeam = intent.getStringExtra("batting_team");
        bTeam = intent.getIntExtra("batting_team_no", 0);
        fieldingTeam = intent.getStringExtra("fielding_team");
        fTeam = intent.getIntExtra("fielding_team_no", 0);
        super_over_innings = intent.getIntExtra("super_over_innings", 0);
        continueInnings = intent.getBooleanExtra("continue_innings", false);
        followon = intent.getBooleanExtra("follow_on", false);
        batting_team_textView.setText(battingTeam);
        fielding_team_textView.setText(fieldingTeam);


        Log.d("openers", "getFromIntent, battingTeam : " + battingTeam);
        Log.d("openers", "getFromIntent, bTeam : " + bTeam);
        Log.d("openers", "getFromIntent, fieldingTeam : " + fieldingTeam);
        Log.d("openers", "getFromIntent, fTeam : " + fTeam);
        Log.d("openers", "getFromIntent, followon : " + followon);
        Log.d("openers", "getFromIntent, continueInnings : " + continueInnings);


//        batting_team_textView.setText(battingTeam);
//        fielding_team_textView.setText(fieldingTeam);
    }


    void setTeamNames() {

        Log.d("Openers", "setTeamNames, currentInnings : " + currentInnings);


        if (currentInnings % 2 != 0) {


            if (toss_winner.equals(teamA)) {

                if (decision.matches("Batting")) {

                    battingTeam = teamA;
                    fieldingTeam = teamB;
                    bTeam = 1;
                    fTeam = 2;

                } else {

                    fieldingTeam = teamA;
                    battingTeam = teamB;
                    fTeam = 1;
                    bTeam = 2;
                }
            } else {

                if (decision.matches("Batting")) {

                    battingTeam = teamB;
                    fieldingTeam = teamA;
                    bTeam = 2;
                    fTeam = 1;

                } else {

                    fieldingTeam = teamB;
                    battingTeam = teamA;
                    fTeam = 2;
                    bTeam = 1;
                }
            }
        } else {


            if (toss_winner.equals(teamA)) {

                if (decision.matches("Batting")) {

                    battingTeam = teamB;
                    fieldingTeam = teamA;
                    bTeam = 2;
                    fTeam = 1;

                } else {

                    fieldingTeam = teamB;
                    battingTeam = teamA;
                    fTeam = 2;
                    bTeam = 1;
                }
            } else {

                if (decision.matches("Batting")) {

                    battingTeam = teamA;
                    fieldingTeam = teamB;
                    bTeam = 1;
                    fTeam = 2;

                } else {

                    fieldingTeam = teamA;
                    battingTeam = teamB;
                    fTeam = 1;
                    bTeam = 2;
                }
            }
        }

//        Log.d("openers", "setTeamNames, toss: "+toss_winner);
//        Log.d("openers", "setTeamNames, decision: "+decision);
//        Toast.makeText(getApplicationContext(), "batting: "+ battingTeam, Toast.LENGTH_SHORT).show();
//        Toast.makeText(getApplicationContext(), "fielding: "+ fieldingTeam, Toast.LENGTH_SHORT).show();
        batting_team_textView.setText(battingTeam);
        fielding_team_textView.setText(fieldingTeam);

    }


    //Read Player details from database

    void readFromRealm() {

        Log.d("Openers", "readFromRealm, currentInnings : " + currentInnings);
        Log.d("Openers", "readFromRealm, super_over_innings  : " + super_over_innings);


        Match match = realm.where(Match.class).
                equalTo("matchid", matchid).findFirst();
        if (match != null) {

            matchID = match.getMatchID();
            teamA = match.getTeamA();
            teamB = match.getTeamB();
            toss_winner = match.getToss_winner();
            decision = match.getDecision();
            SUPER_OVER = match.isSUPER_OVER();

            if (SUPER_OVER) {

                if (super_over_innings == 0) {
                    super_over_innings = 1;
                    currentInnings = 99;
                } else if (super_over_innings == 1)
                    currentInnings = 100;

//            ll_next_bowler.setVisibility(View.INVISIBLE);

            }

            if (currentInnings == 1)
                setTeamNames();


//        toss_winner = realm.where(Match.class).equalTo("matchid", matchid).findFirst().getToss_winner();
//        decision = realm.where(Match.class).equalTo("matchid", matchid).findFirst().getDecision();
            Log.d("openers", "readFromRealm, team1: " + teamA);
            Log.d("openers", "readFromRealm, team2: " + teamB);
            Log.d("openers", "readFromRealm, toss_winner: " + toss_winner);
            Log.d("openers", "readFromRealm, decision: " + decision);
            Log.d("openers", "readFromRealm, battingTeam: " + battingTeam);
            Log.d("openers", "readFromRealm, fieldingTeam: " + fieldingTeam);
            Log.d("openers", "readFromRealm, battingTeam No.: " + bTeam);
            Log.d("openers", "readFromRealm, fieldingTeam No.: " + fTeam);
            Log.d("openers", "readFromRealm, SUPER_OVER: " + SUPER_OVER);
            Log.d("openers", "readFromRealm, currentInnings: " + currentInnings);
            Log.d("openers", "readFromRealm, super_over_innings: " + super_over_innings);

            battingList.add("--select--");
            fieldingList.add("--select--");


        } else {

            displayErrorMessage("Couldn't find match");
//
//                Toast.makeText(getApplicationContext(), "couldn't find match id ", Toast.LENGTH_SHORT).show();


        }


        if (bTeam != 0) {


            RealmResults<Player> battingRealmResults = realm.where(Player.class).
                    equalTo("matchid", matchid).
                    equalTo("team", bTeam).
                    equalTo("substitute", false).
                    equalTo("retired", false).
                    equalTo("retired_concussion", false).    // added on 27/02/2021
                            sort("playerID", Sort.ASCENDING).
                    findAll();
            battingRealmResults.load();
            for (Player player : battingRealmResults) {
                battingList.add(player.getPlayerName());

            }
        }

        if (fTeam != 0) {
            RealmResults<Player> fieldingRealmResults = realm.where(Player.class).
                    equalTo("matchid", matchid).
                    equalTo("team", fTeam).
                    equalTo("substitute", false).
                    equalTo("retired_concussion", false).    // added on 27/02/2021
                            equalTo("new_wk", false).                // added on 05/10/2021
                            equalTo("retired", false)
                    .sort("playerID", Sort.ASCENDING).findAll();
            fieldingRealmResults.load();
            for (Player player : fieldingRealmResults) {
                fieldingList.add(player.getPlayerName());
            }
        }
    }


    void setSpinnerValues() {

        Log.d("Openers", "setSpinnerValues, currentInnings : " + currentInnings);

        adapterBattingTeam = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, battingList);
        adapterBattingTeam.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view
        spinner_striker.setAdapter(adapterBattingTeam);
        spinner_non_striker.setAdapter(adapterBattingTeam);

        adapterFieldingTeam = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, fieldingList);
        adapterFieldingTeam.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view
        spinner_bowler.setAdapter(adapterFieldingTeam);
//        spinner_next_bowler.setAdapter(adapterFieldingTeam);

    }


    void checkSpinnerValues() {

        Log.d("Openers", "checkSpinnerValues, currentInnings : " + currentInnings);
        displayProgress();

        if (!spinner_striker.getSelectedItem().equals("--select--")) {

            striker = (String) spinner_striker.getSelectedItem();

//            Toast.makeText(getApplicationContext(), "Captain A = "+striker, Toast.LENGTH_SHORT).show();
            strID = realm.where(Player.class).
                    equalTo("matchid", matchid).
                    equalTo("team", bTeam).
                    equalTo("playerName", striker).findFirst().getPlayerID();

        }

        if (!spinner_non_striker.getSelectedItem().equals("--select--")) {

            non_striker = (String) spinner_non_striker.getSelectedItem();

            nstrID = realm.where(Player.class).
                    equalTo("matchid", matchid).
                    equalTo("team", bTeam).
                    equalTo("playerName", non_striker).findFirst().getPlayerID();
        }


        if (spinner_bowler != null &&
                !((spinner_bowler.getSelectedItem().toString()).matches("--select--"))) {

            bowler = (String) spinner_bowler.getSelectedItem();

            bowlerID = realm.where(Player.class).
                    equalTo("matchid", matchid).
                    equalTo("team", fTeam).
                    equalTo("playerName", bowler).findFirst().getPlayerID();
//            Toast.makeText(getApplicationContext(), "Captain B = "+bowler, Toast.LENGTH_SHORT).show();
        }


        if (nstrID == strID) {

            nstrID = 0;
            non_striker = "";
            sflag = false;
            displayErrorMessage("Invalid non striker selection");

        }


        if ((nstrID != strID) && (nstrID > 0) && bowlerID > 0) {

            sflag = true;
        }


        Log.d("Openers", "currentInnings : " + currentInnings);
        /*if (spinner_striker.getSelectedItem().equals("--select--"))
            displayErrorMessage("Invalid striker selection");

        if (spinner_non_striker.getSelectedItem().equals("--select--"))
            displayErrorMessage("Invalid non striker selection");

        if (spinner_bowler.getSelectedItem().equals("--select--"))
            displayErrorMessage("Invalid bowler selection");*/


        if (sflag) {

            savetoDB(strID, bTeam, true, false, currentInnings, 1, SUPER_OVER);
            savetoDB(nstrID, bTeam, true, false, currentInnings, 2, SUPER_OVER);
            savetoDB(bowlerID, fTeam, false, true, currentInnings, 0, SUPER_OVER);

//            if (!SUPER_OVER)
//                savetoDB(nextBowlerID, fTeam, false, true, currentInnings);

            status = "start";
            saveToSP();

//            postJSON();

            // added on 04/05/2020

            if (currentInnings == 1) {

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
                                        equalTo("matchid", matchid).
                                        findFirst();

//                        match1.setMatchSync(1);
                                match1.setStatus("MS");
                                match1.setStatusId(6);

                                bgRealm.copyToRealm(match1);
                                Log.d("setMatchStatus", " match1 : " + match1);

                            } catch (RealmPrimaryKeyConstraintException e) {
                                Toast.makeText(getApplicationContext(),
                                        "Primary Key exists, Press Update instead",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } catch (RealmException e) {
                    Log.d("openers", "Exception : " + e);
                } finally {
                    if (realm != null) {
                        realm.close();
                    }
                }
            }


            //=================== till here

            // ===== added on 19/05/2020

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

                            Number num = bgRealm.where(Openers.class).max("openersID");
                            int nextId = (num == null) ? 1 : num.intValue() + 1;

                            Openers openers = bgRealm.createObject(Openers.class, nextId);
                            openers.setMatchid(matchid);
                            openers.setMatchID(matchID);
                            openers.setInnings(currentInnings);
                            openers.setStrikerID(strID);
                            openers.setNonStrikerID(nstrID);
                            openers.setBowlerID(bowlerID);

                            bgRealm.copyToRealm(openers);
                            Log.d("openers", "checkSpinnerValues, openers : " + openers);

                        } catch (RealmPrimaryKeyConstraintException e) {
                            Toast.makeText(getApplicationContext(),
                                    "Primary Key exists, Press Update instead",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            } catch (RealmException e) {
                Log.d("openers", "Exception : " + e);
            } finally {
                if (realm != null) {
                    realm.close();
                }
            }


            //=========== till here


//            Intent i = new Intent(OpenersActivity.this, ScoringActivity.class);   Commented on 24/07/2021
            Intent i = new Intent(OpenersActivity.this, UpdatedScoringActivity.class); // Updated on 24/07/2021
            i.putExtra("status", "start");
            i.putExtra("runonce", true);
            i.putExtra("score", true);
            i.putExtra("inningsnotstarted", true);
            i.putExtra("initialize", true);
            i.putExtra("first_bowler", true);
            i.putExtra("first_batsman", true);
            i.putExtra("new_innings", true);


            Log.d("Openers", "checkSpinnerValues, Intent : " + i);
//            i.putExtra("just_started", true);

            startActivity(i);
            finish();
            progress.dismiss();

        } else {

            if (spinner_striker.getSelectedItem().equals("--select--"))
                displayErrorMessage("Invalid striker selection");

            else if (spinner_non_striker.getSelectedItem().equals("--select--"))
                displayErrorMessage("Invalid non striker selection");

            else if (spinner_bowler.getSelectedItem().equals("--select--"))
                displayErrorMessage("Invalid bowler selection");
        }
    }


    void savetoDB(int playerID, int team, boolean batsman, boolean bowler, int currentInnings,
                  int battingOrder, boolean SUPER_OVER) {

//        Log.e("Openers", "savetoDB, player : "+name+", batsman : "+batsman+", bowler : "+bowler);

        Realm realm = null;
        try {
            config = new RealmConfiguration.Builder()
                    .name(AppConstants.GAME_ID + ".realm")
                    .deleteRealmIfMigrationNeeded()
                    .build();
            realm = Realm.getInstance(config);
            realm.executeTransaction(new Realm.Transaction() {


                @Override
                public void execute(Realm bgrealm) {

                    try {

                        // save Batsman datails

                        if (batsman) {
                            Number num1 = bgrealm.where(Batsman.class).max("batsmanID");
                            Log.e("Openers", "savetoDB, previous - id : " + num1);
                            int nextId1 = (num1 == null) ? 1 : num1.intValue() + 1;
                            Batsman batsmann = bgrealm.createObject(Batsman.class, nextId1);
                            batsmann.setMatchid(matchid);
                            batsmann.setMatchID(matchID);
                            batsmann.setBatsman_pID(playerID);
                            batsmann.setTeam(team);
                            batsmann.setInnings(currentInnings);
                            batsmann.setSUPER_OVER(SUPER_OVER);
                            batsmann.setPlaying(true);
                            /*Number pos = bgrealm.where(Batsman.class).
                                    equalTo("matchid", matchid).
                                    equalTo("team", team).
                                    equalTo("innings", currentInnings).max("battingOrder");
                            int nextPos = (pos == null) ? 1 : pos.intValue() + 1;*/
                            batsmann.setBattingOrder(battingOrder);

                            bgrealm.insertOrUpdate(batsmann);
                            Log.e("Openers", "savetoDB, batsman : " + batsmann);
                        }


                        // save striker details to table Bowler

                        else if (bowler) {

                            Number num3 = bgrealm.where(Bowler.class).max("bowlerID");
                            Log.e("Openers", "savetoDB, previous - id : " + num3);
                            int nextId1 = (num3 == null) ? 1 : num3.intValue() + 1;
                            Bowler bowlerr = bgrealm.createObject(Bowler.class, nextId1);
                            bowlerr.setMatchid(matchid);
                            bowlerr.setMatchID(matchID);
                            bowlerr.setPlayerID(playerID);
                            bowlerr.setTeam(team);
                            bowlerr.setInnings(currentInnings);
                            bowlerr.setSUPER_OVER(SUPER_OVER);

                            bgrealm.insertOrUpdate(bowlerr);
                            Log.e("Openers", "savetoDB, batsman : " + bowlerr);
                        }

                    } catch (RealmPrimaryKeyConstraintException e) {
                        Toast.makeText(getApplicationContext(),
                                "Primary Key exists, Press Update instead", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } catch (RealmException e) {
            Log.d("openers", "checkSpinnerValues, Exception : " + e);
        } finally {
            if (realm != null) {
                realm.close();
            }
        }

    }


    void saveToSP() {

        Log.d("Openers", "saveToSP, currentInnings : " + currentInnings);
        Log.d("Openers", "saveToSP, matchID : " + matchID);
        Log.d("Openers", "saveToSP, matchid : " + matchid);
        Log.d("super_over_innings", "saveToSP, super_over_innings : " + super_over_innings);

        mEditor = sharedPreferences.edit();

        mEditor.putBoolean("SUPER_OVER", SUPER_OVER);
        mEditor.putString("sp_match_ID", matchID);
        mEditor.putInt("sp_match_id", matchid);
        mEditor.putString("sp_batting_team", battingTeam);
        mEditor.putString("sp_fielding_team", fieldingTeam);
        mEditor.putString("sp_striker", striker);
        mEditor.putInt("sp_striker_id", strID);
        mEditor.putString("sp_non_striker", non_striker);
        mEditor.putInt("sp_non_striker_id", nstrID);
        mEditor.putString("sp_bowler", bowler);
        mEditor.putInt("sp_bowler_id", bowlerID);
//        mEditor.putString("sp_next_bowler", next_bowler);
//        mEditor.putInt("sp_next_bowler_id", nextBowlerID);
        mEditor.putInt("sp_super_over_innings", super_over_innings);
        mEditor.putInt("sp_current_innings", currentInnings);

        mEditor.putInt("sp_batting_team_no", bTeam);
        mEditor.putInt("sp_fielding_team_no", fTeam);

        mEditor.putInt("sp_batting_order", 2);

        Log.d("Openers", "saveToSP, mEditor 1 : " + mEditor);

        mEditor.putBoolean("sp_just_started", true);
        mEditor.putString("sp_status", status);
        mEditor.putBoolean("follow_on", followon);


        if (currentInnings == 1) {

            mEditor.putInt("sp_session_id", 12);
            mEditor.putString("sp_session", "SS1");
        } else {
            if (sessionId == 7) {
                mEditor.putInt("sp_session_id", 8);
                mEditor.putString("sp_session", "SS2");
            } else if (sessionId == 9) {
                mEditor.putInt("sp_session_id", 10);
                mEditor.putString("sp_session", "SS3");
            }
        }


//        Log.d("Openers", "saveToSP, mEditor 1 : " + mEditor);


        if (SUPER_OVER) {

            mEditor.putInt("sp_over", 1);
            mEditor.putInt("sp_player_count", 2);
//            mEditor.putInt("sp_sub_seq", 0);
        }
        mEditor.putBoolean("midscoreTest", false);

        mEditor.apply();

        Log.d("Openers", "saveToSP, mEditor 2 : " + mEditor);


        Log.d("openers", "saveToSP, battingTeam : " + battingTeam);
        Log.d("openers", "saveToSP, fieldingTeam : " + fieldingTeam);
        Log.d("openers", "saveToSP, striker : " + striker);
        Log.d("openers", "saveToSP, strID : " + strID);
        Log.d("openers", "saveToSP, non_striker : " + non_striker);
        Log.d("openers", "saveToSP, nstrID : " + nstrID);
        Log.d("openers", "saveToSP, bowler : " + bowler);
        Log.d("openers", "saveToSP, bowlerID : " + bowlerID);
//        Log.d("openers", "saveToSP, next_bowler : " + next_bowler);

        Log.d("openers", "saveToSP, nextBowlerID : " + nextBowlerID);
        Log.d("openers", "saveToSP, currentInnings : " + currentInnings);
        Log.d("openers", "saveToSP, bTeam : " + bTeam);
        Log.d("openers", "saveToSP, fTeam : " + fTeam);


    }


    @Override
    protected void onStop() {

        super.onStop();
//        Toast.makeText(getApplicationContext(), "onStop called 2 ", Toast.LENGTH_LONG).show();


    }


    @Override
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
//                        match.setAddPlayers(false);
                        match.setCaptain(false);
//                        match.setToss(false);
                        if (match.getAbandoned_after_toss_match_flag() == 0)
                            match.setOpeners(true);
//                        match.setScoring(false);
//                        match.setScoreCard(false);
//                        match.setEndOfInnings(false);
//                        match.setPulledMatch(false);
//                        match.setSelectAXI(false);
//                        match.setSelectBXI(false);
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
    }


    @Override
    public void onBackPressed() {

//        if (continueInnings) {
        startActivity(new Intent(this, HomeActivity.class));
        finish();
//        }
//
//        else {
//            startActivity(new Intent(this, TossActivity.class));
//            finish();
//        }
    }




/*
    private void postJSON(){

        Log.e("toss", "1st matchID : "+matchID);

        int msync = 0, psync = 0, tsync = 0;
        String capA = null, vcA = null, wkA = null, capB = null, vcB = null, wkB = null,
                umpire1_v = "", umpire2_v = "", umpire3_v = "", umpire4_v = "" , match_referee_v = "";

        Match match = realm.where(Match.class).equalTo("matchid", matchid).findFirst();

        msync = match.getMatchSync();
        boolean post = match.isPost();
        boolean player_post = match.isPlayer_post();
        psync = match.getPlayerSync();
        tsync = match.getTossSync();

        if (isNetworkAvailable()) {

            JSONObject jsonMatch = new JSONObject();

            // adding officials
            RealmResults<MatchOfficials> result = realm.where(MatchOfficials.class).
                    equalTo("matchID", matchID).findAll();

            JSONArray arrayOfficials = new JSONArray();
            Log.d("officials", "results 1 : " + result);
            if (result.isEmpty()) {

                Log.d("officials", "results : " + result);
            }

            else {

                for (MatchOfficials officials : result) {

                    JSONObject jsonOfficials = new JSONObject();

                    try {
                        if (!officials.getOfficialName().matches(""))
                            jsonOfficials.put("name", officials.getOfficialName());
                        else
                            jsonOfficials.put("name", "");

                        if (officials.getStatus().matches("u1") || officials.getStatus().matches("u2"))
                            jsonOfficials.put("type", "u");
                        else
                            jsonOfficials.put("type", officials.getStatus());

                        if (officials.getD4s_id() == 0)
                            jsonOfficials.put("d4s_playerid", 0);
                        else
                            jsonOfficials.put("d4s_playerid", officials.getD4s_id());

                        arrayOfficials.put(jsonOfficials);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }



            if (!post) {//msync == 0) {

                try {

                    jsonMatch.put("d4s_gameid", match.getD4s_matchid());
                    jsonMatch.put("d4s_userid", match.getD4s_userid());

                    jsonMatch.put("matchID", matchID);
                    jsonMatch.put("teamA", match.getTeamA());
                    jsonMatch.put("d4s_teamA_id", match.getTeamAId());
                    jsonMatch.put("teamB", match.getTeamB());
                    jsonMatch.put("d4s_teamB_id", match.getTeamBId());
                    jsonMatch.put("venue", match.getVenue());
                    jsonMatch.put("d4s_venue_id", match.getVenueId());
                    if (match.getEnd1() == null)
                        jsonMatch.put("end1", "");
                    else
                        jsonMatch.put("end1", match.getEnd1());
                    if (match.getEnd2() == null)
                        jsonMatch.put("end2", "");
                    else
                        jsonMatch.put("end2", match.getEnd2());
                    jsonMatch.put("event", match.getEvent());
                    jsonMatch.put("d4s_event_id", match.getEventId());
                    jsonMatch.put("phase", match.getPhase());
                    jsonMatch.put("match_type", match.getMatchType());
                    jsonMatch.put("innings", match.getInnings());
                    jsonMatch.put("players", match.getPlayer());
                    jsonMatch.put("substitute_players", match.getSubst());
                    jsonMatch.put("over", match.getOver());
                    jsonMatch.put("balls_per_over", match.getBalls());
                    jsonMatch.put("wide_value", match.getWiderun());
                    jsonMatch.put("noball_value", match.getNoballrun());
                    jsonMatch.put("penalty_value", match.getPenaltyrun());
                    jsonMatch.put("rainruleused", "n");
                    */
/*if (match.getUmpire1() == null)
                        jsonMatch.put("umpire1", "");
                    else
                        jsonMatch.put("umpire1", match.getUmpire1());
                    if (match.getUmpire2() == null)
                        jsonMatch.put("umpire2", "");
                    else
                        jsonMatch.put("umpire2", match.getUmpire2());
                    if (match.getUmpire3() == null)
                        jsonMatch.put("umpire3", "");
                    else
                        jsonMatch.put("umpire3", match.getUmpire3());
                    if (match.getUmpire4() == null)
                        jsonMatch.put("umpire4", "");
                    else
                        jsonMatch.put("umpire4", match.getUmpire4());
                    if (match.getMatchReferee() == null)
                        jsonMatch.put("match_referee", "");
                    else
                        jsonMatch.put("match_referee", match.getMatchReferee());*//*

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            // starting of adding Team A players
            JSONObject jsonPlayerA;

            // for adding players

            RealmResults<Player> results = realm.where(Player.class).
                    equalTo("matchid", matchid).
                    equalTo("team", 1).
                    equalTo("substitute", false).findAll();
            JSONArray arrayPlayerA = new JSONArray();

            for (Player player : results) {

                jsonPlayerA = new JSONObject();

                try {
                    jsonPlayerA.put("name", player.getPlayerName());
                    jsonPlayerA.put("player_id", player.getPlayerID());
                    jsonPlayerA.put("d4s_playerid", player.getD4s_playerid());
                    if (player.isCaptain()) {
                        jsonPlayerA.put("captain", "y");
                        capA = player.getPlayerName();
                    }
                    else
                        jsonPlayerA.put("captain", "n");
                    if (player.isViceCaptain()) {
                        jsonPlayerA.put("vice_captain", "y");
                        vcA = player.getPlayerName();
                    }
                    else
                        jsonPlayerA.put("vice_captain", "n");
                    if (player.isWicketKeeper()) {
                        jsonPlayerA.put("wicketkeeper", "y");
                        wkA = player.getPlayerName();
                    }
                    else
                        jsonPlayerA.put("wicketkeeper", "n");
                    arrayPlayerA.put(jsonPlayerA);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            //for adding substitute players

            results = realm.where(Player.class).
                    equalTo("matchid", matchid).
                    equalTo("team", 1).
                    equalTo("substitute", true).findAll();
            JSONArray arraySubA = new JSONArray();

            for (Player player : results) {

                jsonPlayerA = new JSONObject();

                try {
                    jsonPlayerA.put("name", player.getPlayerName());
                    jsonPlayerA.put("player_id", player.getPlayerID());
                    jsonPlayerA.put("d4s_playerid", player.getD4s_playerid());
                    if (player.isCaptain())
                        jsonPlayerA.put("captain", "y");
                    else
                        jsonPlayerA.put("captain", "n");
                    if (player.isViceCaptain())
                        jsonPlayerA.put("vice_captain", "y");
                    else
                        jsonPlayerA.put("vice_captain", "n");
                    if (player.isWicketKeeper())
                        jsonPlayerA.put("wicketkeeper", "y");
                    else
                        jsonPlayerA.put("wicketkeeper", "n");
                    arraySubA.put(jsonPlayerA);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            JSONObject jsonTeamA = new JSONObject();
            try {
                jsonTeamA.put("players", arrayPlayerA);
                jsonTeamA.put("substitutes", arraySubA);
                jsonTeamA.put("captain", capA);
                jsonTeamA.put("vice captain", vcA);
                jsonTeamA.put("wicketkeeper", wkA);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            JSONObject jsonA = new JSONObject();
            try {
                jsonA.put("TeamA", jsonTeamA);
            } catch (JSONException e) {
                e.printStackTrace();
            }// end of adding Tam A players


            // starting of adding Team B players
            JSONObject jsonPlayerB;

            // for adding players

            results = realm.where(Player.class).
                    equalTo("matchid", matchid).
                    equalTo("team", 2).
                    equalTo("substitute", false).findAll();
            JSONArray arrayPlayerB = new JSONArray();

            for (Player player : results) {

                jsonPlayerB = new JSONObject();

                try {
                    jsonPlayerB.put("name", player.getPlayerName());
                    jsonPlayerB.put("player_id", player.getPlayerID());
                    jsonPlayerB.put("d4s_playerid", player.getD4s_playerid());
                    if (player.isCaptain()) {
                        jsonPlayerB.put("captain", "y");
                        capB = player.getPlayerName();
                    }
                    else
                        jsonPlayerB.put("captain", "n");
                    if (player.isViceCaptain()) {
                        jsonPlayerB.put("vice_captain", "y");
                        vcB = player.getPlayerName();
                    }
                    else
                        jsonPlayerB.put("vice_captain", "n");
                    if (player.isWicketKeeper()) {
                        jsonPlayerB.put("wicketkeeper", "y");
                        wkB = player.getPlayerName();
                    }
                    else
                        jsonPlayerB.put("wicketkeeper", "n");
                    arrayPlayerB.put(jsonPlayerB);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            //for adding substitute players

            results = realm.where(Player.class).
                    equalTo("matchid", matchid).
                    equalTo("team", 2).
                    equalTo("substitute", true).findAll();
            JSONArray arraySubB = new JSONArray();

            for (Player player : results) {

                jsonPlayerB = new JSONObject();

                try {
                    jsonPlayerB.put("name", player.getPlayerName());
                    jsonPlayerB.put("player_id", player.getPlayerID());
                    jsonPlayerB.put("d4s_playerid", player.getD4s_playerid());
                    if (player.isCaptain())
                        jsonPlayerB.put("captain", "y");
                    else
                        jsonPlayerB.put("captain", "n");
                    if (player.isViceCaptain())
                        jsonPlayerB.put("vice_captain", "y");
                    else
                        jsonPlayerB.put("vice_captain", "n");
                    if (player.isWicketKeeper())
                        jsonPlayerB.put("wicketkeeper", "y");
                    else
                        jsonPlayerB.put("wicketkeeper", "n");
                    arraySubB.put(jsonPlayerB);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            JSONObject jsonTeamB = new JSONObject();
            try {
                jsonTeamB.put("players", arrayPlayerB);
                jsonTeamB.put("substitutes", arraySubB);
                jsonTeamB.put("captain", capB);
                jsonTeamB.put("vice captain", vcB);
                jsonTeamB.put("wicketkeeper", wkB);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            JSONObject jsonB = new JSONObject();
            try {
                jsonB.put("TeamB", jsonTeamB);
            } catch (JSONException e) {
                e.printStackTrace();
            }//end of adding Team B players


            JSONObject jsonmid = new JSONObject();
            try {
                jsonmid.put("matchID", matchID);
            } catch (JSONException e) {
                e.printStackTrace();
            }


            //adding players to Players array
            JSONArray arrayPlayers = new JSONArray();
            arrayPlayers.put(jsonmid);
            arrayPlayers.put(jsonA);
            arrayPlayers.put(jsonB);

            Log.e("toss", "2nd matchID : "+matchID);


            // adding toss winner and their decision
            JSONObject jsonToss = new JSONObject();
            try {
                jsonToss.put("matchID", matchID);
                jsonToss.put("winner", twin);
                jsonToss.put("decision", decision);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            Log.e("toss", "jsonToss : "+jsonToss);

            JSONArray arrayToss = new JSONArray();
            arrayToss.put(jsonToss);
            Log.e("toss", "arrayToss : "+arrayToss);

            JSONObject jsonOpener = new JSONObject();
            try {
                jsonOpener.put("matchID", matchID);
                jsonOpener.put("innings", currentInnings);
                jsonOpener.put("batting_team", bTeam);
                jsonOpener.put("striker", striker);
                jsonOpener.put("non_striker", non_striker);
                jsonOpener.put("fielding_team", fTeam);
                jsonOpener.put("bowler", bowler);
//                jsonOpener.put("next_bowler", next_bowler);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            JSONArray arrayOpener = new JSONArray();
            arrayOpener.put(jsonOpener);
            Log.e("toss", "arrayOpener : "+arrayOpener);


            // adding values into feed
            JSONObject jsonFeed = new JSONObject();
            try {
                if (!post) {//msync == 0) {
                    jsonFeed.put("AddMatch", jsonMatch);
                    jsonFeed.put("AddMatchOfficials", arrayOfficials);
                }
                if (!player_post) //psync == 0)
                    jsonFeed.put("AddPlayers", arrayPlayers);
                if (tsync == 0)
                    jsonFeed.put("AddToss", arrayToss);
                jsonFeed.put("AddOpeners", arrayOpener);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Log.e("toss", "jsonFeed : "+jsonFeed);

            //adding values to postparams
            JSONObject postparams = new JSONObject();
            try {
                postparams.put("title", "CHASE_POST");
                postparams.put("feed", jsonFeed);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            Log.e("openers", "postparams : "+postparams);

            JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                    Constants.CHASE_CRICKET_MATCH_API, postparams,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {


                            Log.e("openers", "response : " + response);

//                            try {
//
//                                Log.e("captain", "response : " + response);
//                                //if no error in response
//
//                                if (!response.getBoolean("error") && response.getInt("status") == 200) {
//
//                                    JSONObject jsonMatch = response.getJSONObject("match");
//                                    Realm realm = null;
//                                    try {
//                                         config = new RealmConfiguration.Builder()
                .name(AppConstants.GAME_ID + ".realm")
                .deleteRealmIfMigrationNeeded()
                .build();
        realm = Realm.getInstance(config);
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
//                                                    if (match1.getMatchSync() != 1)
//                                                        match1.setMatchSync(1);
//                                                    if (match1.getPlayerSync() != 1)
//                                                        match1.setPlayerSync(1);
//                                                    match1.setStatus("OA");
//                                                    match1.setTossSync(1);
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
//                                        Log.d("test", "Exception : " + e);
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
//                                    startActivity(new Intent(TossActivity.this, OpenersActivity.class));
//                                    finish();
//
//                                } else {
//                                    Toast.makeText(getApplicationContext(),
//                                            response.getString("message"), Toast.LENGTH_SHORT).show();
//                                }
//                            } catch (JSONException e) {
//                                e.printStackTrace();
//                            }
//
////                        startActivity(new Intent(TossActivity.this, OpenersActivity.class));

                        }

                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                            Log.e("openers", "Error Message is  : " + error.getMessage());
//
//                            AlertDialog.Builder builderInner = new AlertDialog.Builder(OpenersActivity.this);
////                            builderInner.setIcon(R.drawable.ball);
//                            builderInner.setMessage("Server Error");
//                            builderInner.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialog, int which) {
//                                    dialog.dismiss();
//                                    startActivity(new Intent(OpenersActivity.this, ScoringActivity.class));
//                                }
//                            });
//                            builderInner.show();

                        }
                    });

            MyApplicationClass.getInstance(getApplicationContext()).addToRequestQueue(jsonObjReq, "postRequest");
            Log.d("openers", "jsonObjReq  : " + jsonObjReq);
            Log.d("openers", "postparams  : " + postparams);

        }

//        else
        */
/*startActivity(new Intent(OpenersActivity.this, ScoringActivity.class));
        finish();*//*


        Intent i = new Intent(OpenersActivity.this, ScoringActivity.class);
//            i.putExtra("status", "start");
        i.putExtra("runonce", true);
        i.putExtra("score", true);
        i.putExtra("inningsnotstarted", true);
        i.putExtra("initialize", true);
        i.putExtra("first_bowler", true);
        i.putExtra("first_batsman", true);


        Log.d("Openers", "checkSpinnerValues, Intent : " + i);
//            i.putExtra("just_started", true);

        startActivity(i);
        finish();
//        progress.dismiss();


    }
*/


    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }


    public void displayErrorMessage(String message) {

        progress.dismiss();
        AlertDialog messageAlert = new AlertDialog.
                Builder(OpenersActivity.this).create();
        messageAlert.setIcon(R.drawable.ball);
        messageAlert.setCancelable(false);
        messageAlert.setMessage(message);
        messageAlert.setButton(AlertDialog.BUTTON_POSITIVE, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }

                });

        messageAlert.show();
    }


    private void checkAlreadySelected() {

//        saveToSP();

        Match match = realm.where(Match.class).equalTo("matchid", matchid).findFirst();
        if (match.isOpeners()) {

            sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
            striker = sharedPreferences.getString("sp_striker", null);
            non_striker = sharedPreferences.getString("sp_non_striker", null);
            bowler = sharedPreferences.getString("sp_bowler", null);
//            next_bowler = sharedPreferences.getString("sp_next_bowler", null);

            spinner_striker.setSelection(adapterBattingTeam.getPosition(striker));
            spinner_non_striker.setSelection(adapterBattingTeam.getPosition(non_striker));
            spinner_bowler.setSelection(adapterFieldingTeam.getPosition(bowler));
//            spinner_next_bowler.setSelection(adapterFieldingTeam.getPosition(next_bowler));
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.match_note_menu, menu);//Menu Resource, Menu Commente don 03/09/2021
        getMenuInflater().inflate(R.menu.match_abandoned_menu, menu);//Menu Resource, Menu   // Added on 03/09/2021
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            /* Commente on 03/09/2021
            case R.id.add_match_note:
                addMatchNote();
                return true;*/

            case R.id.match_abandoned:  // Added on 03/09/2021
                matchAbandoned();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }

    }


    // Added on 03/09/2021
    private void matchAbandoned() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Match Abandoned").
                setIcon(R.drawable.ball).
                setMessage("Do you want to abandoned the match ?").
                setCancelable(false).
                setPositiveButton("CONFIRM", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        // display result page with result abandoned
                        updateMatchResult();
                    }
                }).
                setNeutralButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();
    }


    // Added on 02/09/2021
    private void updateMatchResult() {

        Realm realm = null;
        try {
            config = new RealmConfiguration.Builder()
                    .name(AppConstants.GAME_ID + ".realm")
                    .deleteRealmIfMigrationNeeded()
                    .build();
            realm = Realm.getInstance(config);
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm bgrealm) {

                    try {
                        Match match = bgrealm.where(Match.class).
                                equalTo("matchid", matchid).findFirst();
                        if (match != null) {
                            match.setResult("Match Abandoned");
                            match.setType("ab");
                            match.setOpeners(false);
                            match.setMatchStatus(1);
                            match.setAbandoned_after_toss_match_flag(1);

                            bgrealm.copyToRealmOrUpdate(match);
                            Log.d("AddPlayersA", "updateMatchResult, notes : " + match);

                            Intent intent = new Intent(getBaseContext(), ScheduledService.class);
                            getBaseContext().startService(intent);

                            postJSON();

                            Intent i = new Intent(OpenersActivity.this, DisplayResultActivity.class);
                            i.putExtra("matchid", matchid);
                            i.putExtra("matchID", matchID);
                            freeMemory();
                            startActivity(i);
                            finish();
                        }
                    } catch (RealmPrimaryKeyConstraintException e) {
                        Log.d("readResult", " Exception : " + e);
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

    private void postJSON() {

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
                try {
                    jsonObject.put("matchID", match.getMatchID());
                    jsonObject.put("type", match.getType());
                    jsonObject.put("margin", match.getMargin());
                    jsonObject.put("winner", match.getWinner_team());
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                JSONObject jsonFeed = new JSONObject();
                try {
                    jsonFeed.put("MatchResult", jsonObject);
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


    public void freeMemory() {
        System.runFinalization();
        Runtime.getRuntime().gc();
        System.gc();
    }


    // ==== till here


    private void addMatchNote() {

        // added on 19/10/2020
        Intent intent = new Intent(OpenersActivity.this, MatchNoteListActivity.class);
        intent.putExtra("matchid", matchid);
        intent.putExtra("matchID", matchID);
        intent.putExtra("innings", 0);
        intent.putExtra("over", 0);
        startActivity(intent);

     /* commented on 19/10/2020
        addNoteBuilder = new AlertDialog.Builder(OpenersActivity.this);
        addNoteBuilder.setIcon(R.drawable.ball);
        addNoteBuilder.setCancelable(false);
        addNoteBuilder.setTitle("Enter Match Note");

        final EditText input = new EditText(this);

        input.setInputType(InputType.TYPE_CLASS_TEXT);
        addNoteBuilder.setView(input);
//        Log.d("Test", "inside AddRuns()");
        addNoteBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

            //            @Override
            public void onClick(DialogInterface dialog, int which) {

//                displayProgress();
                if (input.getText().equals(null)){
//                    progress.dismiss();
                    Toast.makeText(OpenersActivity.this, "Empty match note", Toast.LENGTH_SHORT).show();
                }

                else {
                    matchNote = input.getText().toString();
                    matchnote = true;

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
                            public void execute(Realm bgrealm) {

                                try {

                                   *//* commented on 09/10/2020
                                   Match match = realm.where(Match.class).
                                            equalTo("matchid", matchid).findFirst();
                                    match.setMatch_note(matchNote);
                                    match.setPost_matchnote(true);
                                    realm.copyToRealmOrUpdate(match);*//*

                                    // added on 09/10/2020
                                    Number num = bgrealm.where(MatchNotes.class).max("note_id");
                                    int nextId = (num == null) ? 1 : num.intValue() + 1;

                                    Number num2 = bgrealm.where(MatchNotes.class).
                                            equalTo("matchid", matchid).max("sequence");
                                    int sequence = (num2 == null) ? 1 : num2.intValue() + 1;

                                    MatchNotes notes = bgrealm.createObject(MatchNotes.class, nextId);
                                    notes.setMatchid(matchid);
                                    notes.setMatchID(matchID);
                                    notes.setOver(0);
                                    notes.setSequence(sequence);
                                    notes.setNote(matchNote);
                                    bgrealm.copyToRealm(notes);

                                    postMatchNote();

//                                    progress.dismiss();
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

                    // post matchnote
                }
            }
        });

        addNoteBuilder.setNegativeButton("CANCEL", null);
        AlertDialog alert = addNoteBuilder.create();
        alert.show();*/
    }


    public void displayProgress() {

        progress = new ProgressDialog(this);
        progress.setMessage("Please wait...");
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setIndeterminate(true);
        progress.setProgress(0);
        progress.show();
    }


    // added on 19/10/2020
    void postMatchNote() {

        int sync, notes_size = 0;
        Match match = realm.where(Match.class).equalTo("matchid", matchid).findFirst();

        sync = match.getMatchSync();
        boolean post = match.isPost();
        boolean mpost = match.isPost();
        Log.e("openers", "sync : " + sync);

        if (isNetworkAvailable()) {

            JSONObject jsonMatch = new JSONObject();

            // adding officials

            RealmResults<MatchOfficials> result = realm.where(MatchOfficials.class).
                    equalTo("matchID", matchID).findAll();

            JSONArray arrayOfficials = new JSONArray();
            Log.d("officials", "openers, results 1 : " + result);
            if (result.isEmpty()) {

                Log.d("officials", "openers, results : " + result);
            } else {

                for (MatchOfficials officials : result) {

                    JSONObject jsonOfficials = new JSONObject();

                    try {
                        if (!officials.getOfficialName().matches(""))
                            jsonOfficials.put("name", officials.getOfficialName());
                        else
                            jsonOfficials.put("name", "");

                        if (officials.getStatus().matches("u1") || officials.getStatus().matches("u2"))
                            jsonOfficials.put("type", "u");
                        else
                            jsonOfficials.put("type", officials.getStatus());

                        if (officials.getD4s_id() == 0)
                            jsonOfficials.put("d4s_playerid", 0);
                        else
                            jsonOfficials.put("d4s_playerid", officials.getD4s_id());

                        arrayOfficials.put(jsonOfficials);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }


            if (sync == 1)//post)
                Log.d("cap", "match : " + match);
            else {//sync == 0) {

                try {

                    jsonMatch.put("d4s_gameid", match.getD4s_matchid());
                    jsonMatch.put("d4s_userid", match.getD4s_userid());

                    jsonMatch.put("matchID", matchID);
                    jsonMatch.put("teamA", match.getTeamA());
                    jsonMatch.put("d4s_teamA_id", match.getTeamAId());
                    jsonMatch.put("teamB", match.getTeamB());
                    jsonMatch.put("d4s_teamB_id", match.getTeamBId());
                    jsonMatch.put("venue", match.getVenue());
                    jsonMatch.put("d4s_venue_id", match.getVenueId());
                    if (match.getEnd1() == null)
                        jsonMatch.put("end1", "");
                    else
                        jsonMatch.put("end1", match.getEnd1());
                    if (match.getEnd2() == null)
                        jsonMatch.put("end2", "");
                    else
                        jsonMatch.put("end2", match.getEnd2());
                    jsonMatch.put("event", match.getEvent());
                    jsonMatch.put("d4s_event_id", match.getEventId());
                    jsonMatch.put("phase", match.getPhase());
                    jsonMatch.put("match_type", match.getMatchType());
                    jsonMatch.put("innings", match.getInnings());
                    jsonMatch.put("teamAplayers", match.getPlayerA());
                    jsonMatch.put("teamBplayers", match.getPlayerB());
                    jsonMatch.put("over", match.getOver());
                    jsonMatch.put("balls_per_over", match.getBalls());
                    jsonMatch.put("wide_value", match.getWiderun());
                    jsonMatch.put("noball_value", match.getNoballrun());
                    jsonMatch.put("penalty_value", match.getPenaltyrun());
                    jsonMatch.put("rainruleused", "n");

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            // starting of adding Team A players
            JSONObject jsonPlayerA;

            // for adding players

            RealmResults<Player> results = realm.where(Player.class).
                    equalTo("matchid", matchid).
                    equalTo("team", 1).
                    equalTo("sync", 0).
                    equalTo("substitute", false).findAll();
            Log.e("captain", "team1 players : " + results);
            JSONArray arrayPlayerA = new JSONArray();

            for (Player player : results) {

                jsonPlayerA = new JSONObject();

                try {
                    jsonPlayerA.put("name", player.getPlayerName());
                    jsonPlayerA.put("player_id", player.getPlayerID());
                    jsonPlayerA.put("d4s_playerid", player.getD4s_playerid());
                    if (player.isCaptain()) {
                        jsonPlayerA.put("captain", "y");
                        capA = player.getPlayerName();
                    } else
                        jsonPlayerA.put("captain", "n");
                    if (player.isViceCaptain()) {
                        jsonPlayerA.put("vice_captain", "y");
                        vcA = player.getPlayerName();
                    } else
                        jsonPlayerA.put("vice_captain", "n");
                    if (player.isWicketKeeper()) {
                        jsonPlayerA.put("wicketkeeper", "y");
                        wkA = player.getPlayerName();
                    } else
                        jsonPlayerA.put("wicketkeeper", "n");
                    arrayPlayerA.put(jsonPlayerA);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            //for adding substitute players

            results = realm.where(Player.class).
                    equalTo("matchid", matchid).
                    equalTo("team", 1).
                    equalTo("sync", 0).
                    equalTo("substitute", true).findAll();
            Log.e("captain", "team1 substitutes : " + results);
            JSONArray arraySubA = new JSONArray();

            for (Player player : results) {

                jsonPlayerA = new JSONObject();

                try {
                    jsonPlayerA.put("name", player.getPlayerName());
                    jsonPlayerA.put("player_id", player.getPlayerID());
                    jsonPlayerA.put("d4s_playerid", player.getD4s_playerid());
                    if (player.isCaptain()) {
                        jsonPlayerA.put("captain", "y");
                        capA = player.getPlayerName();
                    } else
                        jsonPlayerA.put("captain", "n");
                    if (player.isViceCaptain()) {
                        jsonPlayerA.put("vice_captain", "y");
                        vcA = player.getPlayerName();
                    } else
                        jsonPlayerA.put("vice_captain", "n");
                    if (player.isWicketKeeper()) {
                        jsonPlayerA.put("wicketkeeper", "y");
                        wkA = player.getPlayerName();
                    } else
                        jsonPlayerA.put("wicketkeeper", "n");
                    arraySubA.put(jsonPlayerA);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            JSONObject jsonTeamA = new JSONObject();
            try {
                jsonTeamA.put("players", arrayPlayerA);
                jsonTeamA.put("substitutes", arraySubA);
                jsonTeamA.put("captain", capA);
                jsonTeamA.put("vice captain", vcA);
                jsonTeamA.put("wicketkeeper", wkA);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            JSONObject jsonA = new JSONObject();
            try {
                jsonA.put("TeamA", jsonTeamA);
            } catch (JSONException e) {
                e.printStackTrace();
            }// end of adding Tam A players


            // starting of adding Team B players
            JSONObject jsonPlayerB;

            // for adding players

            results = realm.where(Player.class).
                    equalTo("matchid", matchid).
                    equalTo("team", 2).
                    equalTo("sync", 0).
                    equalTo("substitute", false).findAll();
            Log.e("captain", "team2 players : " + results);
            JSONArray arrayPlayerB = new JSONArray();

            for (Player player : results) {

                jsonPlayerB = new JSONObject();

                try {
                    jsonPlayerB.put("name", player.getPlayerName());
                    jsonPlayerB.put("player_id", player.getPlayerID());
                    jsonPlayerB.put("d4s_playerid", player.getD4s_playerid());
                    if (player.isCaptain()) {
                        jsonPlayerB.put("captain", "y");
                        capB = player.getPlayerName();
                    } else
                        jsonPlayerB.put("captain", "n");
                    if (player.isViceCaptain()) {
                        jsonPlayerB.put("vice_captain", "y");
                        vcB = player.getPlayerName();
                    } else
                        jsonPlayerB.put("vice_captain", "n");
                    if (player.isWicketKeeper()) {
                        jsonPlayerB.put("wicketkeeper", "y");
                        wkB = player.getPlayerName();
                    } else
                        jsonPlayerB.put("wicketkeeper", "n");
                    arrayPlayerB.put(jsonPlayerB);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            //for adding substitute players

            results = realm.where(Player.class).
                    equalTo("matchid", matchid).
                    equalTo("team", 2).
                    equalTo("sync", 0).
                    equalTo("substitute", true).findAll();
            Log.e("captain", "team2 substitutes : " + results);
            JSONArray arraySubB = new JSONArray();

            for (Player player : results) {

                jsonPlayerB = new JSONObject();

                try {
                    jsonPlayerB.put("name", player.getPlayerName());
                    jsonPlayerB.put("player_id", player.getPlayerID());
                    jsonPlayerB.put("d4s_playerid", player.getD4s_playerid());
                    if (player.isCaptain()) {
                        jsonPlayerB.put("captain", "y");
                        capB = player.getPlayerName();
                    } else
                        jsonPlayerB.put("captain", "n");
                    if (player.isViceCaptain()) {
                        jsonPlayerB.put("vice_captain", "y");
                        vcB = player.getPlayerName();
                    } else
                        jsonPlayerB.put("vice_captain", "n");
                    if (player.isWicketKeeper()) {
                        jsonPlayerB.put("wicketkeeper", "y");
                        wkB = player.getPlayerName();
                    } else
                        jsonPlayerB.put("wicketkeeper", "n");
                    arraySubB.put(jsonPlayerB);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            JSONObject jsonTeamB = new JSONObject();
            try {
                jsonTeamB.put("players", arrayPlayerB);
                jsonTeamB.put("substitutes", arraySubB);
                jsonTeamB.put("captain", capB);
                jsonTeamB.put("vice captain", vcB);
                jsonTeamB.put("wicketkeeper", wkB);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            JSONObject jsonB = new JSONObject();
            try {
                jsonB.put("TeamB", jsonTeamB);
            } catch (JSONException e) {
                e.printStackTrace();
            }//end of adding Team B players


            JSONObject jsonmid = new JSONObject();
            try {
                jsonmid.put("matchID", matchID);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            JSONArray arrayNote = new JSONArray();

            RealmResults<MatchNotes> results_notes = realm.where(MatchNotes.class).
                    equalTo("matchid", matchid).
                    equalTo("sync", 0).
                    sort("sequence", Sort.ASCENDING).findAll();

            if (results_notes.size() > 0) {
                for (MatchNotes notes : results_notes) {
                    ++notes_size;
                    JSONObject json = new JSONObject();
                    try {
                        json.put("sequence", notes.getSequence());
                        json.put("over", notes.getOver());
                        json.put("matchnote", notes.getNote());

                        arrayNote.put(json);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            JSONObject jsonNote = new JSONObject();
            try {
                jsonNote.put("matchID", matchID);
                jsonNote.put("notes", arrayNote);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            //adding players to Players array
            JSONArray arrayPlayers = new JSONArray();
            arrayPlayers.put(jsonmid);
            arrayPlayers.put(jsonA);
            arrayPlayers.put(jsonB);

            // adding values into feed
            JSONObject jsonFeed = new JSONObject();
            try {
                if (sync == 1)
//                if (post)
                    Log.d("openers", "match : " + match);
                else {
                    jsonFeed.put("AddMatch", jsonMatch);
                    jsonFeed.put("AddMatchOfficials", arrayOfficials);
                }
                jsonFeed.put("AddPlayers", arrayPlayers);

                if (notes_size > 0)                     // added on 09/10/2020
                    jsonFeed.put("AddMatchNote", jsonNote);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            //adding values to postparams
            JSONObject postparams = new JSONObject();
            try {
                postparams.put("title", "CHASE_POST");
                postparams.put("feed", jsonFeed);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            Log.e("openers", "postparams : " + postparams);

            JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                    Constants.CHASE_CRICKET_MATCH_API, postparams,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {

                            try {

                                Log.e("openers", "response : " + response);
                                //if no error in response

                                if (!response.getBoolean("error") && response.getInt("status") == 200) {

                                    JSONObject jsonMatch = response.getJSONObject("match");
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
                                                    matchnote = false;

                                                    Match match1 = bgRealm.where(Match.class).
                                                            equalTo("matchID",
                                                                    jsonMatch.getString("app_matchID")).
                                                            findFirst();

                                                    if (match1 != null) {//match1.getMatchSync() != 1) {
                                                        match1.setPost(true);
                                                        match1.setPost_matchnote(false);
                                                        match1.setMatchSync(1);
                                                        Log.d("matchSync", "openers, match synced");
                                                    }
                                                    match1.setStatus("CVW");
                                                    match1.setStatusId(3);
                                                    bgRealm.copyToRealm(match1);

                                                    Log.d("matchSync", "openers, match : " + match1);

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
                                        Log.d("openers", "Exception : " + e);
                                    } finally {
                                        if (realm != null) {
                                            realm.close();
                                        }
                                    }


//                                    progress.dismiss();
//                                    Toast.makeText(getApplicationContext(),
//                                            response.getString("message"), Toast.LENGTH_SHORT).show();


                                } else {
//                                    progress.dismiss();
//                                    Toast.makeText(getApplicationContext(),
//                                            response.getString("message"), Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }


                        }

                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
//                            progress.dismiss();
                            Log.e("openers", "Error Message is  : " + error.getMessage());
                        }
                    });
            MyApplicationClass.getInstance(getApplicationContext()).addToRequestQueue(jsonObjReq, "postRequest");
            Log.d("openers", "jsonObjReq  : " + jsonObjReq);
            Log.d("openers", "postparams  : " + postparams);
        }
    }


}
