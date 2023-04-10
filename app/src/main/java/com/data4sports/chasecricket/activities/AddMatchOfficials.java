package com.data4sports.chasecricket.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.data4sports.chasecricket.R;
import com.data4sports.chasecricket.applicationConstants.AppConstants;
import com.data4sports.chasecricket.models.Constants;
import com.data4sports.chasecricket.models.Match;
import com.data4sports.chasecricket.models.MatchOfficials;
import com.data4sports.chasecricket.models.MyApplicationClass;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;
import io.realm.exceptions.RealmException;
import io.realm.exceptions.RealmPrimaryKeyConstraintException;

public class AddMatchOfficials extends AppCompatActivity {

    EditText et_umpire1, et_umpire2, et_umpire3, et_umpire4, et_scorer, et_referee;
    Button save;
    ImageButton back;

    Realm realm;
    int matchid, flag = 0, match_status = 0;
    String matchID, umpire1 = "", umpire2 = "", umpire3 = "", umpire4 = "", scorer = "", referee = "", type = "";
    String u1, u2, u3, u4, scr, mr;
    boolean score = false;

    RealmConfiguration config;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_match_officials);


        Log.d("officials", "onCreate");
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.custom_action_bar_layout);
        View view =getSupportActionBar().getCustomView();

        back = (ImageButton) view.findViewById(R.id.action_bar_back);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*finish();*/
                Log.e("scoring", "oncreate, back button pressd");

                onBackPressed();
                finish();
            }
        });

         config = new RealmConfiguration.Builder()
                .name(AppConstants.GAME_ID + ".realm")
                .deleteRealmIfMigrationNeeded()
                .build();
        realm = Realm.getInstance(config);

        et_umpire1 = findViewById(R.id.amo_umpire1);
        et_umpire2 = findViewById(R.id.amo_umpire2);
        et_umpire3 = findViewById(R.id.amo_umpire3);
        et_umpire4 = findViewById(R.id.amo_umpire4);
        et_scorer = findViewById(R.id.amo_scorer);
        et_referee = findViewById(R.id.amo_referee);
        save = findViewById(R.id.amo_save);


        Intent intent = getIntent();
        matchid = intent.getIntExtra("matchid", 0);
        matchID = intent.getStringExtra("matchID");
        score = intent.getBooleanExtra("score", false);

        // Added on 05/10/2021
        Match match = realm.where(Match.class).equalTo("matchid", matchid).findFirst();
        if (match != null) {
            match_status = match.getMatchSync();
        }
        if (match_status == 0)
            post();
        // till here

        checkValues();


        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                saveMatchOfficials();
            }
        });
    }



    public void checkValues() {

        int i = 0;

        RealmResults<MatchOfficials> results = realm.where(MatchOfficials.class).
                equalTo("matchID", matchID).findAll();

        if (results.isEmpty())
            Log.d("officials", "results : " +results);
        else {

            for (MatchOfficials officials : results) {

                Log.d("officials", "officials " + (i+1) + " : " + officials.toString());
                type = officials.getStatus();
                /*if (type.matches("u")) {  // commented on 14/09/2020
                    if (et_umpire1.getText().toString().matches(""))
                        et_umpire1.setText(officials.getOfficialName());
                    else
                            et_umpire2.setText(officials.getOfficialName());
                }*/

                // added on 14/09/2020
                if (type.matches("u1")) {
                    u1 = officials.getOfficialName();
                    et_umpire1.setText(u1);
                    
                } else if (type.matches("u2")) {
                    u2 = officials.getOfficialName();
                    et_umpire2.setText(u2);
                    
                } else if (type.matches("t")) {
                    u3 = officials.getOfficialName();
                    et_umpire3.setText(u3);
                    
                } else if (type.matches("f")) {
                    u4 = officials.getOfficialName();
                    et_umpire4.setText(u4);
                    
                } else if (type.matches("r")) {
                    mr = officials.getOfficialName();
                    et_referee.setText(mr);
                } else if (type.matches("s")) {
                    scr = officials.getOfficialName();
                    et_scorer.setText(scr);
                }
            }
        }
    }


    // updated on 16/11/2020
    public void saveMatchOfficials() {

        umpire1 = et_umpire1.getText().toString();
        umpire2 = et_umpire2.getText().toString();
        umpire3 = et_umpire3.getText().toString();
        umpire4 = et_umpire4.getText().toString();
        scorer = et_scorer.getText().toString();
        referee = et_referee.getText().toString();

        if (umpire1.matches("") && umpire2.matches("") && umpire3.matches("")
                && umpire4.matches("") && scorer.matches("") && referee.matches("")) {

            AlertDialog alertDialog = new AlertDialog.Builder(AddMatchOfficials.this).create();
            alertDialog.setIcon(R.drawable.ball);
            alertDialog.setCancelable(false);
            alertDialog.setTitle("Please enter at least one value before press save button");
            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            alertDialog.show();
        }

        else {

            // === till here
            Log.d("ChaseCricket", "u1 : " + u1);
            Log.d("ChaseCricket", "u2 : " + u2);
            Log.d("ChaseCricket", "u3 : " + u3);
            Log.d("ChaseCricket", "u4 : " + u4);
            Log.d("ChaseCricket", "scr : " + scr);
            Log.d("ChaseCricket", "mr : " + mr);
            Log.d("ChaseCricket", "umpire1 : " + umpire1);
            Log.d("ChaseCricket", "umpire2 : " + umpire2);
            Log.d("ChaseCricket", "umpire3 : " + umpire3);
            Log.d("ChaseCricket", "umpire4 : " + umpire4);
            Log.d("ChaseCricket", "scorer : " + scorer);
            Log.d("ChaseCricket", "referee : " + referee);

            if (!umpire1.matches("")) {
                if (u1 != null) {
                    if (!u1.matches(umpire1)) {
//                          saved = true;
                        ++flag;
                        saveOfficial(umpire1, "u1");
                    }
                }
                else {
                    ++flag;
                    saveOfficial(umpire1, "u1");
                }
            }

            if (!umpire2.matches("")) {
                if (u2 != null) {
                    if (!u2.matches(umpire2)) {
//                        saved = true;
                        ++flag;
                        saveOfficial(umpire2, "u2");
                    }
                }
                else {
                    ++flag;
                    saveOfficial(umpire2, "u2");
                }
            }

            if (!umpire3.matches("")) {
                if (u3 != null) {
                    if (!u3.matches(umpire3)) {
//                        saved = true;
                        ++flag;
                        saveOfficial(umpire3, "f");
                    }
                }
                else {
                    ++flag;
                    saveOfficial(umpire3, "f");
                }
            }

            if (!umpire4.matches("")) {
                if (u4 != null) {
                    if (!u4.matches(umpire4)) {
//                        saved = true;
                        ++flag;
                        saveOfficial(umpire4, "t");
                    }
                }
                else {
                    ++flag;
                    saveOfficial(umpire4, "t");
                }
            }

            if (!scorer.matches("")) {
                if (scr != null) {
                    if (!scr.matches(scorer)) {
//                        saved = true;
                        ++flag;
                        saveOfficial(scorer, "s");
                    }
                } else {
                    ++flag;
                    saveOfficial(scorer, "s");
                }
            }

            if (!referee.matches("")) {
                if (mr != null) {
                    if (!mr.matches(referee)) {
//                        saved = true;
                        ++flag;
                        saveOfficial(referee, "r");
                    }
                }
                else {
                    ++flag;
                    saveOfficial(referee, "r");
                }
            }

//            if (saved)
            Log.d("ChaseCricket", "flag : " + flag);
            if (flag > 0) {
                post(); // Added on 31/07/2021
//                postOfficialDetails();    // Commented on 31/07/2021
            }
        }
    }


    private void post() {

        // Added on 31/07/2021
        Match match = realm.where(Match.class).equalTo("matchid", matchid).findFirst();
        if (match != null) {
            if (match.getMatchSync() == 1) { // added on 31/07/23021
                postOfficialDetails();
            } else {

                // adding match details
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
//                    jsonfeed.put("AddMatchOfficials", jsonObject);    // commented on05/10/2021 (to split the postings
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
//                                        postOfficials();

                                        postOfficialDetails();

                                        JSONObject jsonMatch = response.getJSONObject("match");
                                        Log.d("create", "login(u,p), jsonMatch : " + jsonMatch);
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
                                                            match1.setPost(true);
                                                            match1.setMatchSync(1);
                                                            Log.d("matchSync", "create, match synced");
                                                            match1.setStatus("MC");
                                                            match1.setStatusId(1);
                                                            match1.setTeamA_sync(1);
                                                            match1.setTeamB_sync(1);

                                                            bgRealm.copyToRealm(match1);
                                                            Log.d("matchSync", "create, match : " + match1);
                                                        }

                                                      /* Commented on 05/10/2021
                                                        RealmResults<MatchOfficials> results = bgRealm.where(MatchOfficials.class).
                                                                equalTo("matchID",
                                                                        jsonMatch.getString("app_matchID")).
                                                                findAll();

                                                        if (results.size() > 0) {

                                                            for (MatchOfficials officials : results) {
                                                                officials.setSync(1);
                                                                bgRealm.copyToRealm(officials);
                                                            }
                                                        }*/
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

//
//                                  Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                                Log.d("CREATE", "Error Message is  : " + error);

                            }
                        });

                MyApplicationClass.getInstance(getApplicationContext()).
                        addToRequestQueue(jsonObjReq, "postRequest");
                Log.d("create", "jsonObjReq  : " + jsonObjReq);
                Log.d("create", "postparams  : " + postparams);

                startActivity(new Intent(AddMatchOfficials.this, UpdatedScoringActivity.class));    // 28/07/2021
                finish();
            }

        } else {
            Log.d("match", "AMO, match = " + match);
        }   // ==== tll here
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

                        // updated on 14/09/2020

                        MatchOfficials officials = bgRealm.where(MatchOfficials.class).
                                equalTo("matchid", matchid).
                                equalTo("status", type).findFirst();

                        if (officials == null) {

                            Number num = bgRealm.where(MatchOfficials.class).max("officialID");
                            int nextId = (num == null) ? 1 : num.intValue() + 1;

                            officials = bgRealm.createObject(MatchOfficials.class, nextId);
                            officials.setMatchid(matchid);
                            officials.setMatchID(matchID);
                            officials.setOfficialName(name);
                            officials.setStatus(type);
                            officials.setSync(0);
                            officials.setEdit(1);
                        }

                        else {

                            officials.setOfficialName(name);
                            officials.setSync(0);
                            officials.setEdit(2);
                        }

                        bgRealm.copyToRealmOrUpdate(officials);

                    } catch (RealmException e) {
                        Toast.makeText(getApplicationContext(),
                                " " + e, Toast.LENGTH_SHORT).show();
                    }
                }
            });
        /*} catch (RealmException e) {
            Log.d("test", "Exception : " + e);
        } finally {
            if (realm != null) {
                realm.close();
            }
        }*/




        int i = 0;

        RealmResults<MatchOfficials> result_officials = realm.where(MatchOfficials.class).
                equalTo("matchid", matchid).findAll();

        for (MatchOfficials officials : result_officials) {

            ++i;
            Log.d("OFFICILAS", "AdMatchOfficilas, " + i + " officials : " + officials.toString());
        }


        /*if (saved)
            postOfficialDetails();*/

    }



    // updated on 10/05/2021
    private void postOfficialDetails() {

         config = new RealmConfiguration.Builder()
                .name(AppConstants.GAME_ID + ".realm")
                .deleteRealmIfMigrationNeeded()
                .build();
        realm = Realm.getInstance(config);
//        if (isNetworkAvailable()) {

            RealmResults<MatchOfficials> results = realm.where(MatchOfficials.class).
                    equalTo("matchid", matchid).
                    equalTo("sync", 0).findAll();

            Log.d("ADD_OFFICIALS", "AddMatchOfficials, results 1 : " + results);
            if (results.isEmpty()) {
                Log.d("ADD_OFFICIALS", "AddMatchOfficials, results : " + results);
            }

            else {
                JSONArray arrayOfficialsAdd = new JSONArray();
                JSONArray arrayOfficialsEdit = new JSONArray();

                Log.d("ADD_OFFICIALS", "AddMatchOfficials, matchID : " + matchID);

                for (MatchOfficials officials : results) {

                    JSONObject jsonOfficialsAdd = new JSONObject();
                    JSONObject jsonOfficialsEdit = new JSONObject();

                    try {
                        if (officials.getEdit() == 1) { // added on 04/12/2020
                            if (!officials.getOfficialName().matches("")) {
                                jsonOfficialsAdd.put("name", officials.getOfficialName());

                                if (officials.getStatus().matches("u1") || officials.getStatus().matches("u2"))
                                    jsonOfficialsAdd.put("type", "u");
                                else
                                    jsonOfficialsAdd.put("type", officials.getStatus());

//                                if (officials.getD4s_id() == 0)
//                                    jsonOfficialsAdd.put("d4s_playerid", 0);
//                                else
                                    jsonOfficialsAdd.put("d4s_playerid", officials.getD4s_id());

                                arrayOfficialsAdd.put(jsonOfficialsAdd);
                            }
                        }

                        // added on 04/12/2020
                        if (officials.getEdit() == 2) {
                            if (!officials.getOfficialName().matches("")) {
                                jsonOfficialsEdit.put("name", officials.getOfficialName());

                                if (officials.getStatus().matches("u1") || officials.getStatus().matches("u2"))
                                    jsonOfficialsEdit.put("type", "u");
                                else
                                    jsonOfficialsEdit.put("type", officials.getStatus());

//                                if (officials.getD4s_id() == 0)
//                                    jsonOfficialsEdit.put("d4s_playerid", 0);
//                                else
                                    jsonOfficialsEdit.put("d4s_playerid", officials.getD4s_id());

                                arrayOfficialsEdit.put(jsonOfficialsEdit);
                            }
                        } // =========== till here

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                // Added on 10/05/2021
                if (arrayOfficialsEdit.length() > 0)
                    postEdit(arrayOfficialsEdit);
                if (arrayOfficialsAdd.length() > 0)
                    postAdd(arrayOfficialsAdd);

                // === till here


                /*JSONObject jsonObjectAdd = new JSONObject();
                try {
                    jsonObjectAdd.put("matchID", matchID);
                    jsonObjectAdd.put("officials", arrayOfficialsAdd);
                } catch (JSONException e) {
                    e.printStackTrace();
                }*/

               /* // added on 04/12/2020
                JSONObject jsonObjectEdit = new JSONObject();
                try {
                    jsonObjectEdit.put("matchID", matchID);
                    jsonObjectEdit.put("officials", arrayOfficialsEdit);
                } catch (JSONException e) {
                    e.printStackTrace();
                }// ==== till here*/

                /*JSONObject jsonfeed = new JSONObject();

                try {
                    jsonfeed.put("AddMatchOfficials", jsonObjectAdd);
                    jsonfeed.put("EditMatchOfficials", jsonObjectEdit);
                } catch (JSONException e) {
                    e.printStackTrace();
                }


                JSONObject postparams = new JSONObject();
                try {
                    postparams.put("title", "CHASE_POST");
                    postparams.put("feed", jsonfeed);
                } catch (JSONException e) {
                    e.printStackTrace();
                }*/

               /* JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                        Constants.CHASE_CRICKET_MATCH_API, postparams,
                        new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {

                                    try {

                                        Log.d("ADD_OFFICIALS", "AddMatchOfficials, response : " + response);
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
//                                  Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                                    Log.d("ADD_OFFICIALS", "AddMatchOfficials, Error Message is  : " + error);
                                }
                            });

                    MyApplicationClass.getInstance(getApplicationContext()).
                            addToRequestQueue(jsonObjReq, "postRequest");
                    Log.d("ADD_OFFICIALS", "AddMatchOfficials, jsonObjReq  : " + jsonObjReq);
                    Log.d("ADD_OFFICIALS", "AddMatchOfficials, postparams  : " + postparams);*/

                }
//            }
//        }


//        Toast.makeText(getApplicationContext(), "Match created", Toast.LENGTH_SHORT).show();
//        if (!pulled) {
//        startActivity(new Intent(AddMatchOfficials.this, ScoringActivity.class));     Commented on 28/07/2021
        startActivity(new Intent(AddMatchOfficials.this, UpdatedScoringActivity.class));    // 28/07/2021
        finish();
//        progress.dismiss();
    }


    private boolean isNetworkAvailable(){
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }


    // Added on 05/10/2021
    // to split the posting of match officials
/*
    public void postOfficials () {

        // Adding officials details
        RealmResults<MatchOfficials> result = realm.where(MatchOfficials.class).
                equalTo("matchID", matchID).findAll();

        JSONArray arrayOfficials = new JSONArray();
        Log.d("officials", "results 1 : " + result);
        if (result.isEmpty()) {
            Log.d("officials", "results : " + result);
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

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("matchID", matchID);
            jsonObject.put("officials", arrayOfficials);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
*/


    // added on 05/10/2021
    // updated on 07/10/2021
    // to post edit details
    public void postEdit(JSONArray arrayOfficialsEdit) {

        if (isNetworkAvailable()) {
            JSONObject jsonMID = new JSONObject();
            try {
                jsonMID.put("matchID", matchID);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            arrayOfficialsEdit.put(jsonMID);

            JSONObject jsonfeed = new JSONObject();
            try {
                jsonfeed.put("EditOfficials", arrayOfficialsEdit);
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

                            Log.d("ADD_OFFICIALS", "postEdit, response : " + response);

                            /*try {
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
                            }*/
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
//                                  Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                            Log.d("ADD_OFFICIALS", "postEdit, Error Message is  : " + error);
                        }
                    });

            MyApplicationClass.getInstance(getApplicationContext()).
                    addToRequestQueue(jsonObjReq, "postRequest");
            Log.d("ADD_OFFICIALS", "postEdit, jsonObjReq  : " + jsonObjReq);
            Log.d("ADD_OFFICIALS", "postEdit, postparams  : " + postparams);

        }/* else {
            startActivity(new Intent(AddMatchOfficials.this, UpdatedScoringActivity.class));
            finish();
        }*/
    }


    private void postAdd(JSONArray arrayOfficialsAdd) {
        if (isNetworkAvailable()) {

            JSONObject jsonObjectAdd = new JSONObject();
            try {
                jsonObjectAdd.put("matchID", matchID);
                jsonObjectAdd.put("officials", arrayOfficialsAdd);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            JSONObject jsonfeed = new JSONObject();

            try {
                jsonfeed.put("AddMatchOfficials", jsonObjectAdd);
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
                            Log.d("ADD_OFFICIALS", "postAdd, response : " + response);
                            try {
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
                                                            officials.setEdit(1);
                                                            bgRealm.copyToRealm(officials);
                                                        }
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
//                                  Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                            Log.d("ADD_OFFICIALS", "postAdd, Error Message is  : " + error);
                        }
                    });

            MyApplicationClass.getInstance(getApplicationContext()).
                    addToRequestQueue(jsonObjReq, "postRequest");

            Log.d("ADD_OFFICIALS", "postAdd, jsonObjReq  : " + jsonObjReq);
            Log.d("ADD_OFFICIALS", "postAdd, postparams  : " + postparams);

        } else {
            startActivity(new Intent(AddMatchOfficials.this, UpdatedScoringActivity.class));
            finish();
        }
    }
}
