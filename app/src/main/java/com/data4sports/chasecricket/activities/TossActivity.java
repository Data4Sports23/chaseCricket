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
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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
import com.data4sports.chasecricket.models.MatchNotes;
import com.data4sports.chasecricket.models.MatchOfficials;
import com.data4sports.chasecricket.models.MyApplicationClass;
import com.data4sports.chasecricket.models.Player;

import org.joda.time.DateTimeComparator;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;
import io.realm.Sort;
import io.realm.exceptions.RealmException;
import io.realm.exceptions.RealmPrimaryKeyConstraintException;

public class TossActivity extends AppCompatActivity implements  View.OnClickListener{

    RadioGroup winner_group, decision_group, toss_main_group, no_toss_group;
    RadioButton team1, team2, batting, fielding, made, not_made, unknown, u_team1, u_team2;
    TextView tv_sub2;
    LinearLayout ll_made, ll_not_made;
    Button next;
    boolean tossMade = false, noToss = false, unknownToss = false, matchnote = false,
            squad = false, squadA = false, squadB = false;

    AlertDialog.Builder addNoteBuilder;
    int matchid, twin = 0, userId = 0, d4s_userid = 0, gameid = 0, capAID = 0, capBID = 0,
            vcAID = 0, vcBID = 0, wkAID = 0, wkBID = 0, mid = 0;
    String matchID, teamA, teamB, capA, capB, vcA, vcB, wkA, wkB;
    String toss_winner = null, decision = null, dec = null, matchNote = "";

    SharedPreferences mPreferences;
    SharedPreferences.Editor mEditor;
    Realm realm;

    RealmConfiguration config;
    private ProgressDialog progress;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.toss);

         config = new RealmConfiguration.Builder()
                .name(AppConstants.GAME_ID + ".realm")
                .deleteRealmIfMigrationNeeded()
                .build();
        realm = Realm.getInstance(config);

        mPreferences = PreferenceManager
                .getDefaultSharedPreferences(this);

        assignVariables();

        getFromSP();
        displayProgress();
        // Added on 10/12/2021
        Match match = realm.where(Match.class)
                .equalTo("matchid", matchid)
                .findFirst();

        if (match != null) {

            Log.d("TA", "match = " + match);
            Log.d("TA", "match.isPulled_squad() = " + match.isPulled_squad());

            if (match.isPulled()) {

//                MyApplicationClass.getInstance(this)
//                        .getRequestQueue().
//                        getCache().
//                        remove(Constants.CHASE_CRICKET_PULL_MATCH_API_TEST + d4s_userid);
                MyApplicationClass.getInstance(this)
                        .getRequestQueue()
                        .getCache()
                        .clear();
                getUpdateMatchDetails();
            }
//            else
//                progress.dismiss();
        }
        progress.dismiss();
        // === till here

        /*// Added on 02/08/2021
        Intent intent= new Intent(getBaseContext(), ScheduledService.class);
        getBaseContext().startService(intent);
        // === till here*/

        team1.setText(teamA);
        team2.setText(teamB);
        u_team1.setText(teamA);
        u_team2.setText(teamB);

        checkAlreadySelected();
        next.setOnClickListener(this);
    }


    void assignVariables(){

        toss_main_group = findViewById(R.id.rg_toss_main);
        winner_group = findViewById(R.id.toss_rg);
        decision_group = findViewById(R.id.toss_decision_rg);
        no_toss_group = findViewById(R.id.no_toss_rg);

        made = findViewById(R.id.rb_toss_made);
        not_made = findViewById(R.id.rb_toss_not_made);
        unknown = findViewById(R.id.rb_unknown);
        team1 = findViewById(R.id.toss_teamA_rb);
        team2 = findViewById(R.id.toss_teamB_rb);
        batting = findViewById(R.id.toss_batting);
        fielding = findViewById(R.id.toss_fielding);
        u_team1 = findViewById(R.id.no_toss_teamA_rb);
        u_team2 = findViewById(R.id.no_toss_teamB_rb);

        tv_sub2 = findViewById(R.id.tv_no_toss_head);

        ll_made = findViewById(R.id.ll_toss_details);
        ll_not_made = findViewById(R.id.ll_no_toss_details);

        next = findViewById(R.id.toss_next_btn);

        toss_winner = null;
        decision = null;
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.rb_toss_made:
                ll_made.setVisibility(View.VISIBLE);
                ll_not_made.setVisibility(View.GONE);
                tossMade = true;
                noToss = false;
                unknownToss = false;
                decision = null;
                toss_winner = null;
                break;

            case R.id.rb_toss_not_made:
                ll_made.setVisibility(View.GONE);
                ll_not_made.setVisibility(View.VISIBLE);
                tv_sub2.setText("Toss Not Made");
                tossMade = false;
                noToss = true;
                unknownToss = false;
                decision = null;
                toss_winner = null;
                break;

            case R.id.rb_unknown:
                ll_made.setVisibility(View.GONE);
                ll_not_made.setVisibility(View.VISIBLE);
                tv_sub2.setText("Toss Unknown");
                tossMade = false;
                noToss = false;
                unknownToss = true;
                decision = null;
                toss_winner = null;
                break;

            case R.id.toss_teamA_rb:    // Toss Made
                toss_winner = teamA;
                twin = 1;
                tossMade = true;
                noToss = false;
                unknownToss = false;
//                decision = null;  Commented on 23/09/2021
                break;

            case R.id.toss_teamB_rb:    // Toss Made
                toss_winner = teamB;
                twin = 2;
                tossMade = true;
                noToss = false;
                unknownToss = false;
//                decision = null;  // Commented on 23/09/2021
                break;

            case R.id.toss_batting:     // Toss Made
                if (toss_winner != null) {  // condition added on 27/07/2021
                    decision = "Batting";
                    dec = "batting";
                    tossMade = true;
                    noToss = false;
                    unknownToss = false;
                }
                else
                    decision = null;
                break;

            case R.id.toss_fielding:    // Toss Made
                if (toss_winner != null) {  // condition added on 27/07/2021
                    decision = "Fielding";
                    dec = "fielding";
                    tossMade = true;
                    noToss = false;
                    unknownToss = false;
                }
                else
                    decision = null;
                break;

            case R.id.no_toss_teamA_rb:
                toss_winner = teamA;
                twin = 1;
                if (noToss)
                    dec = "notmade";
                else if (unknownToss)
                    dec = "unknown";
                decision = "Batting";
//                dec = "batting";
//                noToss = true;
                break;

            case R.id.no_toss_teamB_rb:
                toss_winner = teamB;
                twin = 2;
                if (noToss)
                    dec = "notmade";
                else if (unknownToss)
                    dec = "unknown";
                decision = "Batting";
//                dec = "batting";
//                noToss = true;
                break;

            case R.id.toss_next_btn:
                Log.e("toss", "onClick matchID : "+matchID);
                Log.e("toss", "onClick noToss : "+noToss);
                saveToDB();
                break;

            default:
                displayErrorMessage("Please select Required fields");
        }
    }




    void getFromSP(){

        mPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        matchid = mPreferences.getInt("sp_match_id", 0);
        matchID = mPreferences.getString("sp_match_ID", null);
        teamA = mPreferences.getString("sp_teamA",null);
        teamB = mPreferences.getString("sp_teamB", null);

        userId = mPreferences.getInt("sp_user_id", 0);
        d4s_userid = mPreferences.getInt("d4s_userid", 0);
        gameid = mPreferences.getInt("sp_game_id", 0);

//        Toast.makeText(getApplicationContext(), " from Sp == team A: "+ teamA + ", team B: "+teamB, Toast.LENGTH_SHORT).show();
    }




    void saveToSP(){

        mEditor = mPreferences.edit();

        mEditor.putString("sp_toss_winner", toss_winner);
        mEditor.putString("sp_decision", decision);
        mEditor.putInt("toss_winner", twin);

        // Added on 02/08/2021
        mEditor.putInt("sp_post", 4);

        // Added on 10/12/2021
        mEditor.putString("sp_captainA", capA);
        mEditor.putInt("sp_captainA_id", capAID);
        mEditor.putString("sp_captainB", capB);
        mEditor.putInt("sp_captainB_id", capBID);
        mEditor.putString("sp_vcA", vcA);
        mEditor.putInt("sp_vcA_id", vcAID);
        mEditor.putString("sp_vcB", vcB);
        mEditor.putInt("sp_vcB_id", vcBID);
        mEditor.putString("sp_wkA", wkA);
        mEditor.putInt("sp_wkA_id", wkAID);
        mEditor.putString("sp_wkB", wkB);
        mEditor.putInt("sp_wkB_id", wkBID);
        // === till here
        mEditor.apply();
    }



    @Override
    protected void onStop() {
        super.onStop();
    }



    @Override
    protected void onPause() {

        super.onPause();

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
                        match.setAddPlayersB(false);
                        match.setToss(true);
//                        match.setOpeners(false);
//                        match.setScoring(false);
                        realm.copyToRealmOrUpdate(match);
                    } catch (RealmPrimaryKeyConstraintException e) {
                        Toast.makeText(getApplicationContext(), "Primary Key exists, Press Update instead",
                                Toast.LENGTH_SHORT).show();
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



    private void saveToDB(){

//        displayProgress();

        Log.e("toss", "saveToDB matchID : "+matchID);
        Log.e("toss", "saveToDB toss_winner : "+toss_winner);
        Log.e("toss", "saveToDB decision : "+decision);

       /* if ((toss_winner == null) || (toss_winner.matches("")))
            Log.d("toss", "saveToDB 3 toss_winner : "+toss_winner);

        if ((decision == null) || (decision.matches("")))
            Log.d("toss", "saveToDB 3 decision : "+decision);


        if (((toss_winner == null) || (toss_winner.matches("")))
                && ((decision == null) || (decision.matches("")))){
            displayErrorMessage("Please select required fields");
//            Toast.makeText(getApplicationContext(),
//                    "Required Fields are not selected", Toast.LENGTH_SHORT).show();
        }
        else {
            Log.e("toss", "saveToDB 2 matchID : "+matchID);
            Log.e("toss", "saveToDB 2 toss_winner : "+toss_winner);
            Log.e("toss", "saveToDB 2 decision : "+decision);
        }*/

        // updated on 27/07/2021
        if (((toss_winner != null)/* || (!toss_winner.matches(""))*/)
                && ((decision != null)/* || (!decision.matches("")))*/)) {

            Log.d("toss", "saveToDB 4 toss_winner : "+toss_winner);
            Log.d("toss", "saveToDB 4 decision : "+decision);

//        }
//            {

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
                            Log.d("Toss", " onclick, winner  : "+ toss_winner);
                            Log.d("Toss", "onclick,  decision  : "+ decision);
                            Log.d("Toss", "onclick,  noToss  : "+ noToss);
                            match.setTossMade(tossMade);
                            match.setNoToss(noToss);
                            match.setUnknownToss(unknownToss);
                            match.setToss_winner(toss_winner);
                            match.setDecision(decision);
                            realm.copyToRealmOrUpdate(match);
                        } catch (RealmPrimaryKeyConstraintException e) {
//                            progress.dismiss();
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
            saveToSP();

            //Added on 19/11/2021
            startActivity(new Intent(TossActivity.this, SelectTeamAXIActivity.class));
            finish();

            if (isNetworkAvailable())
                postJSON(matchID, matchid, twin, dec, noToss, unknownToss);
            /*// Added on 03/08/2021
            startActivity(new Intent(TossActivity.this, OpenersActivity.class));
            finish();*/







//            startActivity(new Intent(TossActivity.this, OpenersActivity.class));

        }

        else
            displayErrorMessage("Please select required fields");
    }


    private void postJSON(String matchID, int matchid, int tossWinner, String decision, boolean noToss, boolean unknownToss){

        Log.e("toss", "1st matchID : "+matchID);
        Log.e("toss", "1st noToss : "+noToss);

        int msync = 0;

        Match match = realm.where(Match.class).equalTo("matchid", matchid).findFirst();

        msync = match.getMatchSync();
        // Added on 03/08/2021
        if (msync == 0) {
            postMatchDetails(matchID, matchid);
        }
        else {

            RealmResults<MatchOfficials> result_officials = realm.where(MatchOfficials.class).
                    equalTo("matchid", matchid).
                    equalTo("sync", 0).findAll();
            if (result_officials.size() > 0) {
                postOfficialDetails();
            }

            // Added on 27/11/2021
            if ((match.getTeamAId() != 0) && (match.getTeamAId() != 0)) {

                Player player = realm.where(Player.class)
                        .equalTo("matchid", matchid)
                        .equalTo("team", 1)
//                        .equalTo("pulled", 0)
                        .findFirst();
                if ((player != null ) && (player.getD4s_playerid() == 0) )
                    postPlayers(1, match.getMatchID(), match.getTeamAId());

                player = realm.where(Player.class)
                        .equalTo("matchid", matchid)
                        .equalTo("team", 2)
//                        .equalTo("pulled", 0)
                        .findFirst();
                if ((player != null ) && (player.getD4s_playerid() == 0) )
                    postPlayers(2, match.getMatchID(), match.getTeamBId());

            }

            // === till here

                if (isNetworkAvailable()) {

                    JSONObject jsonmid = new JSONObject();
                    try {
                        jsonmid.put("matchID", matchID);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    Log.e("toss", "2nd matchID : " + matchID);

                    // adding toss winner and their decision
                    JSONObject jsonToss = new JSONObject();
                    try {
                        jsonToss.put("matchID", matchID);
                        if (noToss || unknownToss) {
                            jsonToss.put("winner", -1);
                            jsonToss.put("decision", decision);
                            jsonToss.put("battingteam", tossWinner);
                        } else {
                            jsonToss.put("winner", tossWinner);
                            jsonToss.put("decision", decision);
                            jsonToss.put("battingteam", 0);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    Log.e("toss", "jsonToss : " + jsonToss);

                    JSONArray arrayToss = new JSONArray();
                    arrayToss.put(jsonToss);
                    Log.e("toss", "arrayToss : " + arrayToss);


                    // adding values into feed
                    JSONObject jsonFeed = new JSONObject();
                    try {

                        jsonFeed.put("AddToss", arrayToss);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    Log.e("toss", "jsonFeed : " + jsonFeed);

                    //adding values to postparams
                    JSONObject postparams = new JSONObject();
                    try {
                        postparams.put("title", "CHASE_POST");
                        postparams.put("feed", jsonFeed);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    Log.e("captain", "postparams : " + postparams);

                    JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                            Constants.CHASE_CRICKET_MATCH_API, postparams,
                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {


                                    Log.e("captain", "response : " + response);

                                    try {

                                        Log.e("captain", "response : " + response);
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

                                                            Match match1 = bgRealm.where(Match.class).
                                                                    equalTo("matchID",
                                                                            jsonMatch.getString("app_matchID")).
                                                                    findFirst();

                                                            if (match1 != null) {

                                                                match1.setStatus("TC");
                                                                match1.setStatusId(4);
                                                                match1.setTossSync(1);

                                                                bgRealm.copyToRealm(match1);

                                                                Log.d("matchSync", "toss, match1 : " + match1);
                                                            }

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
                                    Log.e("captain", "Error Message is  : " + error.getMessage());
                                }
                            });

                    MyApplicationClass.getInstance(getApplicationContext()).addToRequestQueue(jsonObjReq, "postRequest");
                    Log.d("toss", "jsonObjReq  : " + jsonObjReq);
                    Log.d("toss", "postparams  : " + postparams);
                }
//                startActivity(new Intent(TossActivity.this, OpenersActivity.class));
                /* Commented on 19/11/2021
                startActivity(new Intent(TossActivity.this, SelectTeamAXIActivity.class));
                finish();*/
//        progress.dismiss();
//            }
        }
    }


    private boolean isNetworkAvailable(){
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }



    public void displayErrorMessage(String message){

//        progress.dismiss();
        AlertDialog messageAlert = new AlertDialog.
                Builder(TossActivity.this).create();
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



    // Updated on 15/06/2021
    private void checkAlreadySelected() {

        Match match = realm.where(Match.class).equalTo("matchid", matchid).findFirst();
        if (match != null) {
            Log.d("TOSS", "CAS, match = " + match);
            Log.d("TOSS", "CAS, match.isToss() = " + match.isToss());
            Log.d("TOSS", "CAS, match.isTossMade() = " + match.isTossMade());
            Log.d("TOSS", "CAS, match.isNoToss() = " + match.isNoToss());
            Log.d("TOSS", "CAS, match.isUnknownToss() = " + match.isUnknownToss());
            Log.d("TOSS", "CAS, match.getDecision() = " + match.getDecision());
            if (match.isToss()) {

                if (match.isTossMade()) {
                    tossMade = match.isTossMade();
                    made.setChecked(true);
                    not_made.setChecked(false);
                    unknown.setChecked(false);
                    ll_made.setVisibility(View.VISIBLE);
                    ll_not_made.setVisibility(View.GONE);
//                    tossMade = true;
                    noToss = false;
                    unknownToss = false;
                } else if (match.isNoToss()) {
                    noToss = match.isNoToss();
                    made.setChecked(false);
                    not_made.setChecked(true);
                    unknown.setChecked(false);
                    ll_made.setVisibility(View.GONE);
                    ll_not_made.setVisibility(View.VISIBLE);
                    tv_sub2.setText("Toss Not Made");
                    tossMade = false;
//                    noToss = true;
                    unknownToss = false;
                } else if (match.isUnknownToss()) {
                    unknownToss = match.isUnknownToss();
                    made.setChecked(false);
                    not_made.setChecked(false);
                    unknown.setChecked(true);
                    ll_made.setVisibility(View.GONE);
                    ll_not_made.setVisibility(View.VISIBLE);
                    tv_sub2.setText("Toss Unknown");
                    tossMade = false;
                    noToss = false;
//                    unknownToss = true;
                }

                if (tossMade || noToss || unknownToss) {
                    toss_winner = match.getToss_winner();
                    Log.d("TOSS", "CAS, toss_winner = " + toss_winner);
                    Log.d("TOSS", "CAS, teamA = " + teamA);
                    Log.d("TOSS", "CAS, teamB = " + teamB);
                    if (toss_winner.matches(teamA)) {
                        twin = 1;
                        if (tossMade) {
                            team1.setChecked(true);
                            team2.setChecked(false);
                        } else {
                            u_team1.setChecked(true);
                            u_team2.setChecked(false);
                            if (noToss)
                                dec = "notmade";
                            else if (unknownToss)
                                dec = "unknown";
                            decision = "Batting";
                        }
                    } else if (toss_winner.matches(teamB)) {
                        twin = 1;
                        if (tossMade) {
                            team1.setChecked(false);
                            team2.setChecked(true);
                        } else {
                            u_team1.setChecked(false);
                            u_team2.setChecked(true);
                            if (noToss)
                                dec = "notmade";
                            else if (unknownToss)
                                dec = "unknown";
                            decision = "Batting";
                        }
                    }

                    decision = match.getDecision();
                    if (decision.matches("Batting")) {
                        if (tossMade) {
                            dec = "batting";
                            batting.setChecked(true);
                            fielding.setChecked(false);
                        }
                    } else if (decision.matches("Fielding")) {
                        if (tossMade) {
                            dec = "fielding";
                            batting.setChecked(false);
                            fielding.setChecked(true);
                        }
                    }
                }

                else {
                    if (made.isChecked())
                        made.setChecked(false);
                    else if (not_made.isChecked())
                        not_made.setChecked(false);
                    else if (unknown.isChecked())
                        unknown.setChecked(false);
                }
            }
        }
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.match_note_menu, menu);//Menu Resource, Menu
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.add_match_note:
                addMatchNote();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }




    private void addMatchNote(){

        // added on 19/10/2020
        Intent intent = new Intent(TossActivity.this, MatchNoteListActivity.class);
        intent.putExtra("matchid", matchid);
        intent.putExtra("matchID", matchID);
        intent.putExtra("innings", 0);
        intent.putExtra("over", 0);
        startActivity(intent);
    }



    public void displayProgress(){

        progress = new ProgressDialog(this);
        progress.setMessage("Please wait...");
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setIndeterminate(true);
        progress.setProgress(0);
        progress.show();
    }


    // Added on 03/08/2021
    public void postMatchDetails(String matchID, int matchid) {

        Log.d("TAG", "postMatchDetails: ");
         config = new RealmConfiguration.Builder()
                .name(AppConstants.GAME_ID + ".realm")
                .deleteRealmIfMigrationNeeded()
                .build();
        realm = Realm.getInstance(config);
        if (isNetworkAvailable()) {

            Match match = realm.where(Match.class).equalTo("matchid", matchid).findFirst();
            Log.d("create", "match : " + match);

            JSONObject matchObject = new JSONObject();
            try {
                if (match != null) {
                    matchObject.put("d4s_gameid", match.getD4s_matchid());
                    matchObject.put("d4s_userid", match.getD4s_userid());
                    matchObject.put("matchID", match.getMatchID());
                    matchObject.put("teamA", match.getTeamA());
                    matchObject.put("d4s_teamA_id", match.getTeamAId());
                    matchObject.put("teamB", match.getTeamB());
                    matchObject.put("d4s_teamB_id", match.getTeamBId());
                    matchObject.put("venue", match.getVenue());
                    matchObject.put("d4s_venue_id", match.getVenueId());
                    matchObject.put("end1", match.getEnd1());
                    matchObject.put("end2", match.getEnd2());
                    matchObject.put("event", match.getEvent());
                    matchObject.put("d4s_event_id", match.getEventId());
                    matchObject.put("phase", match.getPhase());
                    matchObject.put("match_type", match.getMatchType());
                    matchObject.put("innings", match.getInnings());
                    matchObject.put("max_balls_per_over", match.getMax_opb());
                    matchObject.put("date", match.getDate());
                    matchObject.put("teamAplayers", match.getPlayerA());
                    matchObject.put("teamBplayers", match.getPlayerB());
//                    matchObject.put("players", match.getPlayer());
//                matchObject.put("players", match.getPlayerA());
//                matchObject.put("players", match.getPlayerB());
//                    matchObject.put("substitute_players", match.getSubst());
//                matchObject.put("substitute_players", match.getSubstA());
                    matchObject.put("over", match.getOver());
                    matchObject.put("balls_per_over", match.getBalls());
                    matchObject.put("wide_value", match.getWiderun());
                    matchObject.put("noball_value", match.getNoballrun());
                    matchObject.put("penalty_value", match.getPenaltyrun());
                    matchObject.put("rainruleused", "n");
                    matchObject.put("max_overs_per_bowler", match.getMax_opb());
                    matchObject.put("max_balls_per_bowler", match.getMax_bpb());
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("matchID", matchID);
//                jsonObject.put("officials", arrayOfficials);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            JSONObject jsonfeed = new JSONObject();
            try {
                jsonfeed.put("AddMatch", matchObject);
//                jsonfeed.put("AddMatchOfficials", jsonObject);
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

            Log.d("create", "postparams : " + postparams);

            JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                    Constants.CHASE_CRICKET_MATCH_API, postparams,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {

                            try {

                                Log.d("request", "response : " + response);

                                //if no error in response

                                if (!response.getBoolean("error") && response.getInt("status") == 200) {

                                    JSONObject jsonMatch = response.getJSONObject("match");
                                    Log.d("create", "login(u,p), jsonMatch : " + jsonMatch);
                                    String MATCHID = jsonMatch.getString("app_matchID");
                                    int teamA_id = jsonMatch.getInt("team1_id");
                                    int teamB_id = jsonMatch.getInt("team2_id");
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
                                                            equalTo("matchID", MATCHID).
                                                            /*jsonMatch.getString("app_matchID")).*/
                                                                    findFirst();

                                                    if (match1 != null) {
                                                        match1.setPost(true);
                                                        match1.setMatchSync(1);
                                                        Log.d("matchSync", "create, match synced");
                                                        match1.setStatus("MC");
                                                        match1.setStatusId(1);
                                                        match1.setTeamA_sync(1);
                                                        match1.setTeamB_sync(1);

                                                        // Adding team ids
                                                        match1.setTeamAId(teamA_id);
                                                        match1.setTeamBId(teamB_id);

                                                        bgRealm.copyToRealm(match1);
                                                        Log.d("matchSync", "create, match : " + match1);
//                                                        postJSON(matchID, matchid, twin, dec, noToss, unknownToss);
                                                    }

                                                } catch (RealmPrimaryKeyConstraintException e) {
                                                    progress.dismiss();
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
                                progress.dismiss();

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                        }
                    });

            MyApplicationClass.getInstance(getApplicationContext()).
                    addToRequestQueue(jsonObjReq, "postRequest");
            Log.d("create", "jsonObjReq  : " + jsonObjReq);
            Log.d("create", "postparams  : " + postparams);

        }
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
            }

            else {

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
                                        Log.d("Create", "pod, jsonMatch = " + jsonMatch);

                                        // Added on 10/11/2021
                                        JSONArray array = jsonMatch.getJSONArray("officials");
                                        Log.d("Create", "pod, array = " + array);

                                        if (array.length() > 0) {

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

                                                            for (int i = 0; i < array.length(); i++) {
                                                                JSONObject object = array.getJSONObject(i);
                                                                Log.d("Create", "pod, object = " + object);

                                                                if (object.getString("officialtype").matches("u")) {

                                                                    Log.d("Create", "pod, officialtype = " + object.getString("officialtype"));
                                                                    Log.d("Create", "pod, officialname = " + object.getString("officialname"));
                                                                    Log.d("Create", "pod, officialid = " + object.getString("officialid"));

                                                                    MatchOfficials officials_u = bgRealm.where(MatchOfficials.class).
                                                                            equalTo("matchID", jsonMatch.getString("app_matchID")).
                                                                            equalTo("officialName", object.getString("officialname")).
                                                                                    findFirst();

                                                                    Log.d("Create", "pod, officials_u = " + officials_u);

                                                                    if (officials_u != null) {

                                                                        officials_u.setD4s_id(object.getInt("officialid"));
                                                                        officials_u.setSync(1);
                                                                        bgRealm.copyToRealm(officials_u);
                                                                        Log.d("Create", "pod, officials_u = " + officials_u);
                                                                    }

                                                                } else {

                                                                    MatchOfficials officials = bgRealm.where(MatchOfficials.class).
                                                                            equalTo("matchID", jsonMatch.getString("app_matchID")).
                                                                            equalTo("status", object.getString("officialtype")).
                                                                            findFirst();

                                                                    if (officials != null) {

                                                                        officials.setD4s_id(object.getInt("officialid"));
                                                                        officials.setSync(1);
                                                                        bgRealm.copyToRealm(officials);
                                                                        Log.d("Create", "pod, officials = " + officials);
                                                                    }
                                                                }
                                                            }
                                                        } catch (JSONException e) {
                                                            e.printStackTrace();
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
                                        // till here

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


    void postPlayers(int team, String matchID, int d4s_team_id){

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


    // Added on 10/12/2021
    private void getUpdateMatchDetails() {

        if (isNetworkAvailable()) {

            // it is a GET methoid,  string request
            StringRequest stringRequest = new StringRequest(Request.Method.GET,
                    Constants.CHASE_CRICKET_PULL_MATCH_API_TEST + d4s_userid,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            try {
                                Log.d("response", "AssignedMatchList, " + response);
                                JSONArray array = new JSONArray(response);
                                Log.d("array", "AssignedMatchList, " + array);
                                if (array.length() > 0) {

                                    for (int i = 0; i < array.length(); i++) {

                                        JSONObject jsonMatch = array.getJSONObject(i);
                                        int id = Integer.parseInt(jsonMatch.getString("gameid"));
                                        if (id == gameid) {
                                            Match match = realm.where(Match.class)
                                                    .equalTo("d4s_matchid", gameid)
                                                    .findFirst();
                                            if (match != null) {
                                                Log.d("TA", "jsonMatch 1 = " + jsonMatch);
                                                updateMatchDetails(jsonMatch, match);
                                            }
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
        }
    }


    private void pullSquad(int teamAId, int teamBId) {

        Log.d("internet", "pulled, pullSquad, isNetworkAvailable() : " + isNetworkAvailable());

        if (isNetworkAvailable()) {

            StringRequest stringRequest = new StringRequest(Request.Method.GET,
                    Constants.CHASE_CRICKET_PULL_SQUAD_API_TEST + "" + gameid,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            try {

                                Log.d("response", "TOSS, : " + response);
                                JSONArray array = new JSONArray(response);
                                Log.d("array", "" + array);

                                JSONObject jsonSquad = array.getJSONObject(0);
                                Log.d("jsonSquad", "" + jsonSquad);

                                if (jsonSquad.getString("teamA_id") != null &&
                                        jsonSquad.getString("teamB_id") != null) {

                                    int AID = Integer.parseInt(jsonSquad.getString("teamA_id"));
                                    int BID = Integer.parseInt(jsonSquad.getString("teamB_id"));

                                    if ((AID > 0) && (BID > 0) && (AID == teamAId) && (BID == teamBId)) {

                                        JSONArray arraySquadA = jsonSquad.getJSONArray("squadA");
                                        JSONArray arraySquadB = jsonSquad.getJSONArray("squadB");

                                        if (arraySquadA.length() > 0) {
                                            squadA = true;
                                            deletePlayer(teamAId);
                                            updateSquad(teamAId, gameid, arraySquadA);
                                        }

                                        if (arraySquadB.length() > 0) {
                                            squadB = true;
                                            deletePlayer(teamBId);
                                            updateSquad(teamBId, gameid, arraySquadB);
                                        }

                                        progress.dismiss();
                                        updateMatch(squadA, squadB);
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
                            progress.dismiss();
                            Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });

            //creating a request queue
            RequestQueue requestQueue = Volley.newRequestQueue(this);

            //adding the string request to request queue
            requestQueue.add(stringRequest);
            Log.d("PMDA", "squad, stringRequest  : " + stringRequest);

        }

        else {

            progress.dismiss();
            AlertDialog alertDialog = new AlertDialog.Builder(TossActivity.this).create();
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


    private void deletePlayer(int tID) {

        RealmResults<Player> results = realm.where(Player.class)
                .equalTo("gameid", gameid)
                .equalTo("d4s_teamID", tID)
                .findAll();
        if (results.size() > 0) {
            for (Player player : results) {

                if (!realm.isInTransaction()) {
                    realm.beginTransaction();
                }

                player.deleteFromRealm();
                realm.commitTransaction();
            }
        }
    }




    private void updateSquad(int tID, int gameid, JSONArray arraySquad) {

        if (arraySquad.length() > 0) {

            for (int i = 0; i < arraySquad.length(); i++) {

                try {
                    JSONObject jsonTemp = arraySquad.getJSONObject(i);

                    saveSquad(1,
                            jsonTemp.getString("name"),
                            jsonTemp.getString("captain"),
                            jsonTemp.getString("vice_captain"),
                            jsonTemp.getString("wicketkeeper"),
                            jsonTemp.getInt("d4s_playerid"),
                            tID,
                            gameid);        // added on 05/06/2020
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    private void saveSquad(int team, String name, String cap, String vc, String wk, int d4s_pid, int d4s_tid, int gameid) {


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
                        int player_id = (num == null) ? 1 : num.intValue() + 1;
                        Player player = new Player();
                        player.setPlayerID(player_id);
                        player.setMatchid(matchid);
                        player.setMatchID(matchID);


                        player.setTeam(team);
                        player.setPulled(1);
                        player.setPlayerName(name);

                        player.setGameid(gameid);
                        player.setD4s_teamID(d4s_tid);


                        if (cap.matches("yes")) {

                            Log.d("polled", "captain : " + name + ", team : " + team);
                            player.setCaptain(true);

                            if (team == 1) {
                                capA = name;
                                capAID = player_id;
                            }

                            else if (team == 2) {
                                capB = name;
                                capBID = player_id;
                            }
                        }
                        else
                            player.setCaptain(false);


                        if (vc.matches("yes")) {
                            Log.d("polled", "vc : " + name + ", team : " + team);
                            player.setViceCaptain(true);

                            if (team == 1) {
                                vcA = name;
                                vcAID = player_id;
                            }

                            else if (team == 2) {
                                vcB = name;
                                vcBID = player_id;
                            }
                        }
                        else
                            player.setViceCaptain(false);


                        if (wk.matches("yes")) {
                            Log.d("polled", "wk : " + name + ", team : " + team);
                            player.setWicketKeeper(true);

                            if (team == 1) {
                                wkA = name;
                                wkAID = player_id;
                            }

                            else if (team == 2) {
                                wkB = name;
                                wkBID = player_id;
                            }

                        }
                        else
                            player.setWicketKeeper(false);


                        player.setD4s_playerid(d4s_pid);

                        realm.copyToRealm(player);

//                        postPlayerDetails(d4s_pid, player_id);
                        Log.d("player", "savePlayers, : " + player.getPlayerName() + ", " + player.getD4s_playerid() + ", " +
                                player.getMatchid() + ", " + player.getMatchID() + ", " + player.isSubstitute() + ", " +
                                player.getTeam() + ", " + player.isCaptain() + ", " + player.isViceCaptain() + ", " +
                                player.isWicketKeeper() + ", " + player_id + ", " + player);
                    }
                    catch (RealmPrimaryKeyConstraintException e) {
                        Toast.makeText(getApplicationContext(), "Primary Key exists, Press Update instead",
                                Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
        catch (RealmException e){
            Toast.makeText(getApplicationContext(), "Exception : "+ e, Toast.LENGTH_SHORT).show();
        }
    }

    private void updateMatch(boolean squadA, boolean squadB) {

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

                    Match match = realm.where(Match.class)
                            .equalTo("d4s_matchid", gameid)
                            .findFirst();

                    if (match != null) {

                        match.setPulled_squadA(squadA);
                        match.setPulled_squadB(squadB);
                        realm.copyToRealmOrUpdate(match);
                    }
                }
            });
        }
        catch (RealmException e){
            Toast.makeText(getApplicationContext(), "Exception : "+ e, Toast.LENGTH_SHORT).show();
        }
    }



    // Added on 11/12/2021
    private void updateMatchDetails(JSONObject jsonMatch, Match match) {

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

                    if (match != null) {

                        mid = match.getMatchid();

                        try {
                            if (jsonMatch.getString("venue") != null)
                                match.setVenue(jsonMatch.getString("venue"));
                            if (jsonMatch.getString("venue_id") != null)
                                match.setVenueId(Integer.parseInt(jsonMatch.getString("venue_id")));
                            if (jsonMatch.has("end1"))
                                match.setEnd1(jsonMatch.getString("end1"));
                            if (jsonMatch.has("end2"))
                                match.setEnd2(jsonMatch.getString("end2"));

                            if ((jsonMatch.getString("teamAplayers") != null) &&
                                    (jsonMatch.getString("teamBplayers") != null)) {
                                match.setPlayerA(Integer.parseInt(jsonMatch.getString("teamAplayers")));
                                match.setPlayerB(Integer.parseInt(jsonMatch.getString("teamBplayers")));
                            }

                            if (jsonMatch.getString("date") != null)
                                match.setDate(jsonMatch.getString("date"));

                            realm.copyToRealmOrUpdate(match);
                            Log.d("TA", "jsonMatch 2 = " + jsonMatch);
                            Log.d("TA", "match = " + match);

                            compareDate(jsonMatch.getString("date"));

                            /*// check squad
                            if (match.isPulled_squad()) {
                                pullSquad(match.getTeamAId(), match.getTeamBId());
                            }*/
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }
            });
        }
        catch (RealmException e){
            Toast.makeText(getApplicationContext(), "Exception : "+ e, Toast.LENGTH_SHORT).show();
        }
    }


    private void compareDate(String mDate) {

        String myFormat = "yyyy-MM-dd";//"dd/MM/yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.UK);
        String cDate= sdf.format(Calendar.getInstance().getTime());
        Log.d("TA", "mDate = " + mDate);
        Log.d("TA", "cDate3 = " + cDate);

        DateTimeComparator dateTimeComparator = DateTimeComparator.getDateOnlyInstance();
        int compareValues = dateTimeComparator.compare(cDate, mDate);

        // compareValues = 0 ==> cDate and mDate are same
        // compareValues < 0 ==> mDate is a future date
        // compareValues > 0 ==> mDate is a previous date
        if(compareValues < 0) {
            // match date is future Date
            displayError();
        }


    }


    private void displayError() {
        new AlertDialog.Builder(TossActivity.this)
                .setIcon(R.drawable.ball)
                .setTitle("Warning")
                .setMessage("Assigned match date is not met yet")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        startActivity(new Intent(TossActivity.this, HomeActivity.class));
                        finish();
                    }
                })
                .show();
    }
}
