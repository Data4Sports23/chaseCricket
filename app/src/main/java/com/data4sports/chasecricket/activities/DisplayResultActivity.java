package com.data4sports.chasecricket.activities;

import android.arch.lifecycle.LifecycleOwner;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.data4sports.chasecricket.R;
import com.data4sports.chasecricket.applicationConstants.AppConstants;
import com.data4sports.chasecricket.models.Constants;
import com.data4sports.chasecricket.models.Events;
import com.data4sports.chasecricket.models.Match;
import com.data4sports.chasecricket.models.MyApplicationClass;
import com.data4sports.chasecricket.models.Player;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;
import io.realm.exceptions.RealmException;
import io.realm.exceptions.RealmPrimaryKeyConstraintException;

/**
 * Updated on 16/06/2021
 */
public class DisplayResultActivity extends AppCompatActivity implements View.OnClickListener {

    TextView tv_teamA, tv_teamB, tv_edit_result, tv_man_of_the_match, tv_server_sync;
    Button btn_edit_result, btn_man_of_the_match, btn_scorecard, btn_server_sync;

    Realm realm;

    int matchid, checkedItem = -1, win_team = 0, win_runs = 0, win_wickets = 0, team = 0,
            totalInnings = 0, value = 0, mom_pid = 0, mom_d4sID = 0, innings = 0;

    float serverSync = 0f;

    String matchID, matchResult = "", type = "", mmTeam = "", manOfTheMatch, winner = "",
            teamA, teamB, rain_rule = null, win_team_name;

    // Added on 14/06/2021
    String result_type = null, title = "";
    int result_win_team = -1, result_margin = 0, pos = -1;

    ImageButton back;
    boolean wbr = false, wbw = false, overReduced = false, wbi = false;
    String[] result;

    int events_sync = 0;

    // Added on 03/09/2021
    LinearLayout ll_mom, ll_md;
    int abandoned_flag = 0, abandoned_after_toss_flag = 0;

    RealmConfiguration config;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_display_result);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.custom_action_bar_layout);
        View view =getSupportActionBar().getCustomView();

        back = (ImageButton) view.findViewById(R.id.action_bar_back);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*finish();*/
                Log.e("scoring", "oncreate, back button pressed");

                onBackPressed();
            }
        });

        getFromIntent();
        assignViews();
         config = new RealmConfiguration.Builder()
                .name(AppConstants.GAME_ID + ".realm")
                .deleteRealmIfMigrationNeeded()
                .build();
        realm = Realm.getInstance(config);

        Match match = realm.where(Match.class).equalTo("matchid", matchid).findFirst();
        matchResult = match.getResult();
        teamA = match.getTeamA();
        teamB = match.getTeamB();
        totalInnings = match.getTotalInnings();
        tv_teamA.setText(teamA);
        tv_teamB.setText(teamB);
        overReduced = match.isReducedOver();
        result_type = match.getType();// Added on 14/06/2021
        result_win_team = match.getWinner_team();// Added on 14/06/2021
        result_margin = match.getMargin();// Added on 14/06/2021
        // Added on 03/09/2021
        abandoned_flag = match.getAbandoned_match_flag();
        abandoned_after_toss_flag = match.getAbandoned_after_toss_match_flag();
        if (abandoned_flag == 1/* || abandoned_after_toss_flag == 1*/) {
            ll_mom.setVisibility(View.GONE);
            ll_md.setVisibility(View.VISIBLE);
        }
        else {
//            ll_mom.setVisibility(View.VISIBLE);
            if (abandoned_after_toss_flag == 1) {
                ll_mom.setVisibility(View.GONE);
            }
            else {
                ll_mom.setVisibility(View.VISIBLE);
            }
            ll_md.setVisibility(View.GONE);
        }



        // === till here
        if (overReduced)
            rain_rule = match.getAppliedRainRule();
        if (/*(match.getMatch_result().matches("")) || */(match.getResult() == null))
            tv_edit_result.setText("");
        else {
//            if (match.getWinner_team() == 0)
                tv_edit_result.setText(match.getResult());
           /* else {
                if (match.getWinner_team() == 1)
                    temp_team = teamA;
                else
                    if (match.getWinner_team() == 2)
                        temp_team = teamB;

                    if (match.getType().matches())*/



//                tv_edit_result.setText(match.getWinner_team() + " won");
//            }
        }

        if (match.getMan_of_the_match() == null)
            tv_man_of_the_match.setText("");
        else
            tv_man_of_the_match.setText(match.getMan_of_the_match());

        serverSyncResult();

//        btn_edit_result.setOnClickListener(this);
//        btn_man_of_the_match.setOnClickListener(this);
//        btn_scorecard.setOnClickListener(this);
//        btn_server_sync.setOnClickListener(this);
    }



    void getFromIntent(){

        Intent i = getIntent();
        matchid = i.getIntExtra("matchid",0);
        matchID = i.getStringExtra("matchID");
    }



    void assignViews(){

        tv_teamA = findViewById(R.id.result_teamA);
        tv_teamB = findViewById(R.id.result_teamB);
        tv_edit_result = findViewById(R.id.txt_edit_result);
        tv_man_of_the_match = findViewById(R.id.txt_man_of_match);
        tv_server_sync = findViewById(R.id.txt_server_sync);
        btn_edit_result = findViewById(R.id.result_edit_result);
        btn_man_of_the_match = findViewById(R.id.result_man_of_match);
        btn_scorecard = findViewById(R.id.result_score_card);
        btn_server_sync = findViewById(R.id.result_server_sync);

        ll_mom = findViewById(R.id.result_ll_man_of_match);
        ll_md = findViewById(R.id.result_ll_match_details);
        ll_mom.setVisibility(View.GONE);
        ll_md.setVisibility(View.GONE);
    }

    public void onClick(View view) {
        switch (view.getId()){

            case R.id.result_edit_result:
//                editResult();
                readResult();
//                readWinnerTeam();
                break;

            case R.id.result_man_of_match:
                readTeam();
//                manOfTheMatch();
                break;

            case R.id.result_score_card:
                if (abandoned_flag == 1)
                    displayAlert("No scorecard available.");
                else
                    displayScoreCard();
                break;

            case R.id.result_server_sync:
                serversync();
                break;

            case R.id.result_match_details:
                displayMatchDetails();
                break;

                default:
                    break;
        }

    }




    void editResult(){

        AlertDialog.Builder comBuilder = new AlertDialog.Builder(DisplayResultActivity.this);
        comBuilder.setIcon(R.drawable.ball);
        comBuilder.setCancelable(false);
        comBuilder.setTitle("Enter Match Result");

        final EditText input = new EditText(this);

        input.setInputType(InputType.TYPE_CLASS_TEXT);
        input.setText(matchResult);
        comBuilder.setView(input);
//        Log.d("Test", "inside setCommentry()");
        comBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

            //            @Override
            public void onClick(DialogInterface dialog, int which) {


                if (matchResult.equals("") || matchResult == null){
                    Log.d("editResult", "no input ");
                }

                else {

                    matchResult = input.getText().toString();
                    Log.d("Test", "BuilderText : " + matchResult);


//                    if (!matchResult.matches("")){

                        tv_edit_result.setText(matchResult);
//                        updateResult(matchResult, manOfTheMatch, 1);

//                    }
//                    else {
//                        Log.d("Test","buildertext = null");
//                    }

                }
            }
        });

        comBuilder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        AlertDialog alert = comBuilder.create();
        alert.show();

//        Intent i = new Intent(ScoreCardActivity.this, DisplayResultActivity.class);
//        i.putExtra("matchid", matchid);
//        i.putExtra("matchID", matchID);
//        startActivity(i);
//        startActivity(new Intent(ScoreCardActivity.this, DisplayResultActivity.class));


    }




    private void setMOM(int team, String manOfTheMatch, int pid, int d4sID) {

        Log.d("mom", "updateResult1, team : " + team + ", manOfTheMatch : " + manOfTheMatch
                + ", pid : " + pid + ", d4sID : " + d4sID);

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

                        Match match = realm.where(Match.class).equalTo("matchid", matchid).findFirst();
                        if (match != null) {

                            if (match.getMom_pid() > 0)
                                match.setEditMOM(1);
                            match.setMom_team(team);
                            match.setMom_pid(pid);
                            match.setMom_d4sID(d4sID);
                            match.setMom_sync(0);

                            match.setMan_of_the_match(manOfTheMatch);
                            realm.copyToRealmOrUpdate(match);
                            Log.d("mom", "setMOM 2, match : " + match);
                        }
                    } catch (RealmPrimaryKeyConstraintException e) {

                        Log.d("mom", " Exception : "+e);
                    }
                }
            });
        }

        catch (RealmException e) {
            Log.d("mom", "Exception : " + e);
        }

        finally {
            if (realm != null) {
                realm.close();
            }
        }

        /*ScoreCardActivity scoreCard = new ScoreCardActivity();
        scoreCard.*/
//        postJSON(2);
        post(2);
    }


    // Updated the method on 08/10/2021
    // for result
    private void postJSONResult() {

        if (isNetworkAvailable()) {

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
                    if (match.getEditResult() == 0)
                        jsonFeed.put("MatchResult", jsonObject);
                    else
                        jsonFeed.put("EditMatchResult", jsonObject);
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
                Log.d("scoring", "jsonObjReq  : " + jsonObjReq);
                Log.d("scoring", "postparams  : " + postparams);


            }

        }
    }


    // Updated the method on 08/10/2021
    // for adding man of the match
    private void postJSONMOM() {

        if (isNetworkAvailable()) {

            Match match = realm.where(Match.class).equalTo("matchid", matchid).findFirst();

            if (match != null) {

                Log.d("result", "postJSON 1, match : " + match);

                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("matchID", match.getMatchID());
                    jsonObject.put("d4s_playerid", match.getMom_d4sID());
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                JSONArray array = new JSONArray();
                array.put(jsonObject);

                JSONObject jsonFeed = new JSONObject();
                try {
                    if (match.getEditMOM() == 0)
                        jsonFeed.put("AddManOfTheMatch", array);
//                    else
//                        jsonFeed.put("EditManOfTheMatch", array);
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
                                // update match details
                                try {
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
                                                                    jsonMatch.getString("matchid")).
                                                            findFirst();

                                                    if (match1 != null) {

                                                        match1.setMom_sync(1);
                                                        match1.setEditMOM(0);
                                                        bgRealm.copyToRealm(match1);
                                                        Log.d("matchSync", "captain, match : " + match1);
                                                    }

                                                    else
                                                        Log.e("RESPONSE", "Captain, match not found");

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
                Log.d("scoring", "jsonObjReq  : " + jsonObjReq);
                Log.d("scoring", "postparams  : " + postparams);


            }

        }
    }





    private void readManOfTheMatch(int team) {

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(DisplayResultActivity.this,
                android.R.layout.select_dialog_singlechoice);
        final ArrayList<Integer> pidList = new ArrayList<>();
        final ArrayList<Integer> d4sList = new ArrayList<>();

        RealmResults<Player> results = realm.where(Player.class).
                equalTo("matchid", matchid).
                equalTo("team", team).
                equalTo("substitute", false).findAll();

        results.load();
        for (Player player : results) {
                    arrayAdapter.add(player.getPlayerName());
                    pidList.add(player.getPlayerID());
                    d4sList.add(player.getD4s_playerid());
        }

        AlertDialog.Builder mmBuilder = new AlertDialog.Builder(DisplayResultActivity.this);
        mmBuilder.setIcon(R.drawable.ball);
        mmBuilder.setCancelable(false);
        mmBuilder.setTitle("Select man of the match");
        mmBuilder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        mmBuilder.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                manOfTheMatch = arrayAdapter.getItem(which);
                mom_pid = pidList.get(which);
                mom_d4sID = d4sList.get(which);
//                dialog.dismiss();
                AlertDialog.Builder builderInner = new AlertDialog.Builder(DisplayResultActivity.this);
                builderInner.setIcon(R.drawable.ball);
                builderInner.setCancelable(false);
                dialog.dismiss();
                builderInner.setMessage(manOfTheMatch);
                builderInner.setTitle("Selected player is");

                builderInner.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();
                        tv_man_of_the_match.setText(manOfTheMatch);
//                        manOfTheMatch(manOfTheMatch);
                        setMOM(team, manOfTheMatch, mom_pid, mom_d4sID);


                    }
                });

                builderInner.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
//
                builderInner.show();
//                dialog.dismiss();
            }
        });



        mmBuilder.show();
    }



    private void readTeam() {

        String[] teams = {teamA, teamB};

        AlertDialog.Builder winnerBuilder = new AlertDialog.Builder(this);
        winnerBuilder.setIcon(R.drawable.ball);
        winnerBuilder.setCancelable(false);
        winnerBuilder.setTitle("Select Team");
        winnerBuilder.setSingleChoiceItems(teams, checkedItem, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int position) {

                dialog.dismiss();
                mmTeam = Arrays.asList(teams).get(position);
                team = position + 1;
                readManOfTheMatch(team);
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




    private void manOfTheMatch(String manOfTheMatch) {


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

                        Match match = realm.where(Match.class).equalTo("matchid", matchid).findFirst();
                        match.setMan_of_the_match(manOfTheMatch);
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
    }





    void displayScoreCard(){

       /* int status = realm.where(Match.class).equalTo("matchid", matchid).findFirst().getMatchStatus();
        Intent i = new Intent(MatchListActivity.this, ScoreCardActivity.class);
        i.putExtra("matchid", matchid);
        i.putExtra("endofinnings", false);
        i.putExtra("innings", events.getInnings());
        i.putExtra("matchstatus", status);
        startActivity(i);*/

        // added on 20/10/2020
        RealmResults<Events> events_result = realm.where(Events.class).
                equalTo("matchid", matchid).findAll();
        if (events_result.size() > 0) {
            Events events = events_result.last();
            innings = events.getInnings();
        } // === till here

        int status = realm.where(Match.class).equalTo("matchid", matchid).findFirst().getMatchStatus();
        Intent i = new Intent(DisplayResultActivity.this, ScoreCardActivity.class);
        i.putExtra("matchid", matchid);
//        i.putExtra("endofinnings", endOfInnings);
        i.putExtra("innings", innings);//totalInnings);
        i.putExtra("matchstatus", status);
        i.putExtra("match_finished", true);
        freeMemory();
        startActivity(i);
    }




    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, HomeActivity.class));
        finish();
    }



    void readRunOrWicket(String title) {

        String team = "";

        AlertDialog.Builder builder = new AlertDialog.Builder(DisplayResultActivity.this);
        builder.setIcon(R.drawable.ball);
        builder.setCancelable(false);
        /* Commented on 16/06/2021
        if (wbr || wbir)
            builder.setTitle("by runs");
        else if (wbw || wbiw)
            builder.setTitle("by wickets");*/
        builder.setTitle(title);    // added on 16/06/2021

        final EditText input = new EditText(this);

        input.setInputType(InputType.TYPE_CLASS_NUMBER);
//        input.setText("" + result_margin); commented on 30/11/2021
        builder.setView(input);
//        Log.d("Test", "inside AddRuns()");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

            //            @Override
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

                    else if (wbi){//wbir) {
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
                    Log.d("RESULT", "matchResult = " + matchResult);

                    tv_edit_result.setText(matchResult);
                    saveMatchResult(win_team, type, value, matchResult);


//        startActivity(new Intent(ScoreCardActivity.this, DisplayResultActivity.class));

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



    void readWonBy() {

        String[] wonby = {"Runs", "Wickets"};

        AlertDialog.Builder wonByBuilder = new AlertDialog.Builder(this);
        wonByBuilder.setIcon(R.drawable.ball);
        wonByBuilder.setCancelable(false);
        wonByBuilder.setTitle("Won by ");
        int position = -1;                         // Added on 14/06/2021
//        if (result_type.matches("wr"))       // Added on 14/06/2021, commented on 30/11/2021
//            position = 0;                          // Added on 14/06/2021, commented on 30/11/2021
//        else if (result_type.matches("ww"))  // Added on 14/06/2021, commented on 30/11/2021
//            position = 1;                          // Added on 14/06/2021, commented on 30/11/2021
        wonByBuilder.setSingleChoiceItems(wonby, position, new DialogInterface.OnClickListener() {

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

//                readRunOrWicket(title);    // updated on 16/06/2021
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



    void readResult() {
        String str[] = new String[5];


        String[] singleInningsMatchResult = {"Match Won", "Match Tied", "Match Abandoned", "No Result", "Match Concession"};
        String[] multiInningsMatchResult = {"Match Won", "Match Tied", "Match Drawn", "Match Abandoned"};
        String[] singleInningsMatchResult1 = {"w", "td", "ab", "nr", "con"};
        String[] multiInningsMatchResult1 = {"w", "td", "dr", "ab"};

        if (totalInnings == 2) {
//            str = new String[5];
            result = new String[singleInningsMatchResult.length];
            for (int i = 0; i < singleInningsMatchResult.length; i++) {
                result[i] = singleInningsMatchResult[i];
                str[i] = singleInningsMatchResult1[i];
            }
        }

        else
            if (totalInnings == 4) {
//                str = new String[4];
                result = new String[multiInningsMatchResult.length];
                for (int i = 0; i < multiInningsMatchResult.length; i++) {
                    result[i] = multiInningsMatchResult[i];
                    str[i] = multiInningsMatchResult1[i];
                }
            }

        AlertDialog.Builder winnerBuilder = new AlertDialog.Builder(this);
        winnerBuilder.setIcon(R.drawable.ball);
        winnerBuilder.setCancelable(false);
        winnerBuilder.setTitle("Select Result");
        Log.d("DISPLAY", "totalInnings : " + totalInnings);
        Log.d("DISPLAY", "result : " + Arrays.toString(result));
        Log.d("DISPLAY", "str : " + Arrays.toString(str));
        Log.d("result", "result_type : " + result_type);
        Log.d("result", "indexOfString(result_type, result) : " + indexOfString(result_type, result));
//        if (totalInnings == 2)
//        int position = indexOfString(result_type, str); added on 14/06/2021, commented on 30/11/2021
        int position = -1;  // Added on 30/11/2021
        winnerBuilder.setSingleChoiceItems(result, position, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int position) {


                Log.d("DISPLAY", "readResult, position : " + position);
                pos = position;
                matchResult = Arrays.asList(result).get(position);
                Log.d("DISPLAY", "matchResult = " + matchResult);
                Log.d("DISPLAY", "result = " + result);
              if (position == 0) {
                    readWinnerTeam();
                  dialog.dismiss();
              }
                /* Commented on 16/06/2021
                else {

                    if (totalInnings == 2) {
                        switch (position) {

                            case 1:
                                type = "td";    // Tied
                                break;

                            case 2:
                                type = "ab";    // Abandoned
                                break;

                            case 3:
                                type = "nr";    // No Result
                                break;

                            case 4:
                                type = "con";   // Concession
                                break;

                        }
                    }

                    else
                    if (totalInnings == 4) {
                        switch (position) {

                            case 1:
                                type = "td";    // Tied
                                break;

                            case 2:
                                type = "dr";    // Drawn
                                break;

                            case 3:
                                type = "ab";    // Abandoned
                                break;

                        }
                    }

                    saveMatchResult(0,type,0, matchResult);   // Commented on 16/06/2021
//                    updateResult(0,"",0, matchResult, manOfTheMatch, 1);
                }*/
            }

        });
        winnerBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();
                Log.d("DISPLAY", "readResult, which : " + which);
                Log.d("DISPLAY", "readResult, pos : " + pos);
                // added on 16/06/2021
                if (pos > 0) {
                    if (totalInnings == 2) {
                        switch (pos) {

                            case 1:
                                type = "td";    // Tied
                                break;

                            case 2:
                                type = "ab";    // Abandoned
                                break;

                            case 3:
                                type = "nr";    // No Result
                                break;

                            case 4:
                                type = "con";   // Concession
                                break;

                        }
                    }

                    else
                    if (totalInnings == 4) {
                        switch (pos) {

                            case 1:
                                type = "td";    // Tied
                                break;

                            case 2:
                                type = "dr";    // Drawn
                                break;

                            case 3:
                                type = "ab";    // Abandoned
                                break;

                        }
                    }

                    saveMatchResult(0,type,0, matchResult);   // Commented on 16/06/2021
                } else if (pos < 0)
                    Toast.makeText(getApplicationContext(),
                            "Invalid result selection", Toast.LENGTH_SHORT).show();
            }
        });
        winnerBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
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
        // added on 14/06/2021
        int position = -1;                         // Added on 14/06/2021
//        if (result_win_team == 1)                  // Added on 14/06/2021, commented on 30/11/2021
//            position = 0;                          // Added on 14/06/2021, commented on 30/11/2021
//        else if (result_win_team == 2)             // Added on 14/06/2021, commented on 30/11/2021
//            position = 1;                          // Added on 14/06/2021, commented on 30/11/2021
        winnerBuilder.setSingleChoiceItems(teams, position, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int position) {

                dialog.dismiss();
                winner = Arrays.asList(teams).get(position);
                win_team = position + 1;
                if (win_team == 1)
                    win_team_name = teamA;
                else if(win_team == 2)
                    win_team_name = teamB;
//                readWonBy();
//                 Commented on 16/06/2021
                if (totalInnings == 2)
                    readWonBy();
                else if (totalInnings == 4)
                    multiInningsWonBy();
            }
        });
        winnerBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();
                // Added on 16/06/2021
               /* if (totalInnings == 2)
                    readWonBy();
                else if (totalInnings == 4)
                    multiInningsWonBy();*/
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
                            match.setResult_sync(0);
                            match.setEditResult(1);
                            realm.copyToRealmOrUpdate(match);
                            tv_edit_result.setText(matchResult);

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

        /*ScoreCardActivity scoreCard = new ScoreCardActivity();
        scoreCard.*/
//        postJSON(1);
        post(1);
    }


    private boolean isNetworkAvailable(){
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }



    void serversync(){

        Intent i= new Intent(getBaseContext(), ScheduledService.class);
        getBaseContext().startService(i);

        serverSyncResult();
    }



    void serverSyncResult() {

        RealmResults<Events> results1 = realm.where(Events.class).
                equalTo("matchid", matchid).

                findAll();
        if (results1.isEmpty()){

        }

        else {

            RealmResults<Events> results2 = realm.where(Events.class).
                    equalTo("matchid", matchid).
                    equalTo("syncstatus", 1).
                    findAll();

            float total_events = (float) (results1.size());
            int synced = results2.size();

            if (total_events > 0) {
                serverSync = (synced * 100.0f)/ total_events;
            }

            /*if (results2.isEmpty())
                serverSync = 0f;
            else {

                int total_events = results1.size();
                int synced = results2.size();
                Log.d("sync", "serverSync, total_events : " + total_events);
                Log.d("sync", "serverSync, synced : " + synced);

                if (total_events > 0) {

                    serverSync = (synced / total_events) * 100;

                }

            }*/
        }

        tv_server_sync.setText(String.valueOf(new DecimalFormat("##.##").format(serverSync)) + " %");



    }


    public void freeMemory() {
        System.runFinalization();
        Runtime.getRuntime().gc();
        System.gc();
    }


    // Added on 19/04/2021
    public void multiInningsWonBy() {

//        readWonBy();
        String[] wonby = {"An Innings", "Runs", "Wickets"};

        AlertDialog.Builder wonByBuilder = new AlertDialog.Builder(this);
        wonByBuilder.setIcon(R.drawable.ball);
        wonByBuilder.setCancelable(false);
        wonByBuilder.setTitle("Won by ");
        int position = -1;                         // Added on 14/06/2021
//        if (result_type.matches("wi"))       // Added on 16/06/2021, commented on 30/11/2021
//            position = 0;                          // Added on 14/06/2021, commented on 30/11/2021
//        else if (result_type.matches("wr"))  // Added on 14/06/2021, commented on 30/11/2021
//            position = 1;                          // Added on 14/06/2021, commented on 30/11/2021
//        else if (result_type.matches("ww"))  // Added on 14/06/2021, commented on 30/11/2021
//            position = 2;                          // Added on 14/06/2021, commented on 30/11/2021
        wonByBuilder.setSingleChoiceItems(wonby, position, new DialogInterface.OnClickListener() {

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
                    readRunOrWicket(title);    // updated on 16/06/2021

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

/*  Commented on 16/06/2021
    void multiInningsRunOrWicket() {

        String[] anInningsAnd = {"Runs", "Wickets"};

        AlertDialog.Builder wonByBuilder = new AlertDialog.Builder(this);
        wonByBuilder.setIcon(R.drawable.ball);
        wonByBuilder.setCancelable(false);
        wonByBuilder.setTitle("An Innings and ");
        int position = -1;                         // Added on 14/06/2021
        if (result_type.matches("wir"))      // Added on 14/06/2021
            position = 0;                          // Added on 14/06/2021
        else if (result_type.matches("wiw")) // Added on 14/06/2021
            position = 1;                          // Added on 14/06/2021
        wonByBuilder.setSingleChoiceItems(anInningsAnd, position, new DialogInterface.OnClickListener() {

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


    // Added on 20/04/2021
    public void post(int x) {

        events_sync = 0;

        Log.d("DISPLAY", "post 1, events_sync : " + events_sync);

        // Added on 16/06/2021
        events_sync = realm.where(Events.class).
                equalTo("matchid", matchid).
                equalTo("syncstatus", 0).
                findAll().size();

        Log.d("DISPLAY", "post 2, events_sync : " + events_sync);

        if (events_sync == 0) {
            if (x == 1)
                postJSONResult();
            else if (x == 2)
                postJSONMOM();
        }
        else {
            Intent i = new Intent(getBaseContext(), ScheduledService.class);
            getBaseContext().startService(i);
        }
    }


    // Added on 14/06/2021
    public int indexOfString(String searchString, String[] domain)
    {
        for(int i = 0; i < domain.length; i++) {
            if (searchString.contains("w")) {
                if (domain[i].contains("w")) {
                    return i;
                }
            }
            if (searchString.equals(domain[i])) {
                return i;
            }
        }

        return -1;
    }


    // Added on 03/09/2021
    public void displayMatchDetails() {
        Intent intent = new Intent(DisplayResultActivity.this, MatchDetailsActivity.class);
        intent.putExtra("matchid", matchid);
        startActivity(intent);
    }


    public void displayAlert(String message) {

        AlertDialog alertDialog = new AlertDialog.Builder(DisplayResultActivity.this).create();
        alertDialog.setIcon(R.drawable.ball);
        alertDialog.setCancelable(false);
        alertDialog.setMessage(message);
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();
    }



    // Updated on 11/12/2021
    // for single innings
    void readRunOrWicketSingle(String title) {

        boolean checked = false;

        View resultView = View.inflate(this, R.layout.result, null);
        EditText editText = (EditText) resultView.findViewById(R.id.r_value);
        CheckBox cb_rr = (CheckBox) resultView.findViewById(R.id.r_rainrule);

        AlertDialog.Builder builder = new AlertDialog.Builder(DisplayResultActivity.this);
        builder.setIcon(R.drawable.ball);
        builder.setView(resultView);
        builder.setCancelable(false);
        builder.setTitle(title);

//        final EditText input = new EditText(this);
//
//        input.setInputType(InputType.TYPE_CLASS_NUMBER);
//        builder.setView(editText);
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
