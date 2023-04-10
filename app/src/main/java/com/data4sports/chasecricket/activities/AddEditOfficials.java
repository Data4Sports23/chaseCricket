package com.data4sports.chasecricket.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
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
import com.data4sports.chasecricket.models.Constants;
import com.data4sports.chasecricket.models.Match;
import com.data4sports.chasecricket.models.MatchOfficials;
import com.data4sports.chasecricket.models.MyApplicationClass;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;
import io.realm.exceptions.RealmException;
import io.realm.exceptions.RealmPrimaryKeyConstraintException;

/**
 * Added on 08/11/2021
 * for separating the add and edit in officials
 */
public class AddEditOfficials extends AppCompatActivity {

    LinearLayout ll_u1, ll_u2, ll_u3, ll_u4, ll_sc, ll_mr;
    TextView tv_u1, tv_u2, tv_u3, tv_u4, tv_sc, tv_mr;
    ImageView edit_u1, edit_u2, edit_u3, edit_u4, edit_sc, edit_mr;
    ImageView delete_u1, delete_u2, delete_u3, delete_u4, delete_sc, delete_mr;

    ImageButton back;

    Realm realm;
    int matchid, flag = 0, match_status = 0;
    String matchID, umpire1 = "", umpire2 = "", umpire3 = "", umpire4 = "", scorer = "", referee = "", type = "";
    String u1, u2, u3, u4, scr, mr;
    boolean score = false;
    String name = "", o_type = "", t = "", temp = "";
    int position = -1;

    ArrayList<String> ot_array;

    RealmConfiguration config;

    final static String TAG = "AddEditOfficial";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_edit_officials);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.custom_action_bar_layout);
        View view = getSupportActionBar().getCustomView();

        back = (ImageButton) view.findViewById(R.id.action_bar_back);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e(TAG, "oncreate, back button pressd");

                onBackPressed();
                finish();
            }
        });

        assignViews();
        config = new RealmConfiguration.Builder()
                .name(AppConstants.GAME_ID + ".realm")
                .deleteRealmIfMigrationNeeded()
                .build();
        realm = Realm.getInstance(config);

        Intent intent = getIntent();
        matchid = intent.getIntExtra("matchid", 0);
        matchID = intent.getStringExtra("matchID");
        score = intent.getBooleanExtra("score", false);

        Match match = realm.where(Match.class).equalTo("matchid", matchid).findFirst();
        if (match != null) {
            match_status = match.getMatchSync();
        }
        if (match_status == 0)
            post();

        checkValues(1);

        edit_u1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MatchOfficials official = realm.where(MatchOfficials.class).
                        equalTo("matchID", matchID).
                        equalTo("status", "u1").
                        findFirst();
                if (official != null) {
                    editOfficial(official, "u1");
                }
            }
        });

        edit_u2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MatchOfficials official = realm.where(MatchOfficials.class).
                        equalTo("matchID", matchID).
                        equalTo("status", "u2").
                        findFirst();
                if (official != null) {
                    editOfficial(official, "u2");
                }
            }
        });

        edit_u3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MatchOfficials official = realm.where(MatchOfficials.class).
                        equalTo("matchID", matchID).
                        equalTo("status", "t").
                        findFirst();
                if (official != null) {
                    editOfficial(official, "t");
                }
            }
        });

        edit_u4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MatchOfficials official = realm.where(MatchOfficials.class).
                        equalTo("matchID", matchID).
                        equalTo("status", "f").
                        findFirst();
                if (official != null) {
                    editOfficial(official, "f");
                }
            }
        });

        edit_sc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                MatchOfficials official = realm.where(MatchOfficials.class).
                        equalTo("matchID", matchID).
                        equalTo("status", "s").
                        findFirst();
                if (official != null) {
                    editOfficial(official, "s");
                }
            }
        });

        edit_mr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                MatchOfficials official = realm.where(MatchOfficials.class).
                        equalTo("matchID", matchID).
                        equalTo("status", "r").
                        findFirst();
                if (official != null) {
                    editOfficial(official, "r");
                }
            }
        });

        delete_u1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MatchOfficials official = realm.where(MatchOfficials.class).
                        equalTo("matchID", matchID).
                        equalTo("status", "u1").
                        findFirst();
                if (official != null) {
                    deleteOfficial(official);
                }
            }
        });

        delete_u2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MatchOfficials official = realm.where(MatchOfficials.class).
                        equalTo("matchID", matchID).
                        equalTo("status", "u2").
                        findFirst();
                if (official != null) {
                    deleteOfficial(official);
                }
            }
        });

        delete_u3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MatchOfficials official = realm.where(MatchOfficials.class).
                        equalTo("matchID", matchID).
                        equalTo("status", "t").
                        findFirst();
                if (official != null) {
                    deleteOfficial(official);
                }
            }
        });

        delete_u4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MatchOfficials official = realm.where(MatchOfficials.class).
                        equalTo("matchID", matchID).
                        equalTo("status", "f").
                        findFirst();
                if (official != null) {
                    deleteOfficial(official);
                }
            }
        });

        delete_sc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MatchOfficials official = realm.where(MatchOfficials.class).
                        equalTo("matchID", matchID).
                        equalTo("status", "s").
                        findFirst();
                if (official != null) {
                    deleteOfficial(official);
                }
            }
        });

        delete_mr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MatchOfficials official = realm.where(MatchOfficials.class).
                        equalTo("matchID", matchID).
                        equalTo("status", "r").
                        findFirst();
                if (official != null) {
                    deleteOfficial(official);
                }
            }
        });

    }


    void assignViews() {

        ll_u1 = findViewById(R.id.aeo_ll_u1);
        ll_u2 = findViewById(R.id.aeo_ll_u2);
        ll_u3 = findViewById(R.id.aeo_ll_u3);
        ll_u4 = findViewById(R.id.aeo_ll_u4);
        ll_sc = findViewById(R.id.aeo_ll_sc);
        ll_mr = findViewById(R.id.aeo_ll_mr);

        tv_u1 = findViewById(R.id.aeo_tv_u1);
        tv_u2 = findViewById(R.id.aeo_tv_u2);
        tv_u3 = findViewById(R.id.aeo_tv_u3);
        tv_u4 = findViewById(R.id.aeo_tv_u4);
        tv_sc = findViewById(R.id.aeo_tv_sc);
        tv_mr = findViewById(R.id.aeo_tv_mr);

        edit_u1 = findViewById(R.id.aeo_edit_u1);
        edit_u2 = findViewById(R.id.aeo_edit_u2);
        edit_u3 = findViewById(R.id.aeo_edit_u3);
        edit_u4 = findViewById(R.id.aeo_edit_u4);
        edit_sc = findViewById(R.id.aeo_edit_sc);
        edit_mr = findViewById(R.id.aeo_edit_mr);

        delete_u1 = findViewById(R.id.aeo_delete_u1);
        delete_u2 = findViewById(R.id.aeo_delete_u2);
        delete_u3 = findViewById(R.id.aeo_delete_u3);
        delete_u4 = findViewById(R.id.aeo_delete_u4);
        delete_sc = findViewById(R.id.aeo_delete_sc);
        delete_mr = findViewById(R.id.aeo_delete_mr);

        ll_u1.setVisibility(View.GONE);
        ll_u2.setVisibility(View.GONE);
        ll_u3.setVisibility(View.GONE);
        ll_u4.setVisibility(View.GONE);
        ll_sc.setVisibility(View.GONE);
        ll_mr.setVisibility(View.GONE);
    }


    public void checkValues(int i) {


        Log.d(TAG, "checkValues i = " + i);

//        int i = 0;
        ot_array = new ArrayList<String>();
        type = "";

        ll_u1.setVisibility(View.GONE);
        ll_u2.setVisibility(View.GONE);
        ll_u3.setVisibility(View.GONE);
        ll_u4.setVisibility(View.GONE);
        ll_sc.setVisibility(View.GONE);
        ll_mr.setVisibility(View.GONE);

        RealmResults<MatchOfficials> results = realm.where(MatchOfficials.class).
                equalTo("matchID", matchID).findAll();

        if (results.isEmpty())
            Log.d(TAG, "results : " + results);
        else {

            for (MatchOfficials officials : results) {
                if (!officials.isDelete()) {

                    Log.d(TAG, "checkValues, officials : " + officials.toString());
                    type = officials.getStatus();

                    if (type.matches("u1")) {
                        u1 = officials.getOfficialName();
                        tv_u1.setText(u1);
                        ot_array.add("u1");
                        ll_u1.setVisibility(View.VISIBLE);

                    } else if (type.matches("u2")) {
                        u2 = officials.getOfficialName();
                        tv_u2.setText(u2);
                        ot_array.add("u2");
                        ll_u2.setVisibility(View.VISIBLE);

                    } else if (type.matches("t")) {
                        u3 = officials.getOfficialName();
                        tv_u3.setText(u3);
                        ot_array.add("t");
                        ll_u3.setVisibility(View.VISIBLE);

                    } else if (type.matches("f")) {
                        u4 = officials.getOfficialName();
                        tv_u4.setText(u4);
                        ot_array.add("f");
                        ll_u4.setVisibility(View.VISIBLE);

                    } else if (type.matches("r")) {
                        mr = officials.getOfficialName();
                        tv_mr.setText(mr);
                        ot_array.add("r");
                        ll_mr.setVisibility(View.VISIBLE);

                    } else if (type.matches("s")) {
                        scr = officials.getOfficialName();
                        tv_sc.setText(scr);
                        ot_array.add("s");
                        ll_sc.setVisibility(View.VISIBLE);

                    }
                }
            }
        }
    }


    private void post() {

        // Added on 31/07/2021
        Match match = realm.where(Match.class).equalTo("matchid", matchid).findFirst();
        if (match != null) {
            if (match.getMatchSync() == 1) { // added on 31/07/23021
//                postOfficialDetails();
                Log.d(TAG, "match.getMatchSync() = " + match.getMatchSync());
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

                Log.d(TAG, "postparams : " + postparams);

                JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                        Constants.CHASE_CRICKET_MATCH_API, postparams,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {

                                try {

                                    Log.d(TAG, "response : " + response);

                                    //if no error in response

                                    if (!response.getBoolean("error") && response.getInt("status") == 200) {
//                                        postOfficials();

//                                        postOfficialDetails();

                                        JSONObject jsonMatch = response.getJSONObject("match");
                                        Log.d(TAG, "login(u,p), jsonMatch : " + jsonMatch);
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
                                                            Log.d(TAG, "create, match synced");
                                                            match1.setStatus("MC");
                                                            match1.setStatusId(1);
                                                            match1.setTeamA_sync(1);
                                                            match1.setTeamB_sync(1);

                                                            bgRealm.copyToRealm(match1);
                                                            Log.d(TAG, "create, match : " + match1);
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
                                            Log.d(TAG, "Exception : " + e);
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
                                Log.d(TAG, "Error Message is  : " + error);

                            }
                        });

                MyApplicationClass.getInstance(getApplicationContext()).
                        addToRequestQueue(jsonObjReq, "postRequest");
                Log.d(TAG, "jsonObjReq  : " + jsonObjReq);
                Log.d(TAG, "postparams  : " + postparams);

                startActivity(new Intent(AddEditOfficials.this, UpdatedScoringActivity.class));    // 28/07/2021
                finish();
            }

        } else {
            Log.d(TAG, "AMO, match = " + match);
        }   // ==== tll here
    }


    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }


    private void editOfficial(MatchOfficials officials, String type) {

        AlertDialog.Builder editBuilder = new AlertDialog.Builder(AddEditOfficials.this);
        editBuilder.setIcon(R.drawable.ball);
        editBuilder.setCancelable(false);
        editBuilder.setTitle("Enter new name");

        final EditText input = new EditText(AddEditOfficials.this);

        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_WORDS);
        input.setPadding(20, 15, 20, 15);
        input.setText(officials.getOfficialName());
        editBuilder.setView(input);
        editBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (input.getText().toString().equals("") ||
                        input.getText().toString().equals("null") ||
                        input.getText().toString() == null) {
                    Toast.makeText(getApplicationContext(),
                            "Please enter a valid name", Toast.LENGTH_SHORT).show();
                } else {
                    String newName = input.getText().toString();
                    saveEditOfficial(newName, type, officials.getOfficialID());
                }
            }
        });
        editBuilder.setNegativeButton("Cancel", null);
        AlertDialog alert = editBuilder.create();
        alert.show();
    }


    private void saveEditOfficial(String name, String type, int officialID) {

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

                    MatchOfficials officials = bgRealm.where(MatchOfficials.class).
                            equalTo("matchid", matchid).
                            equalTo("officialID", officialID).findFirst();

                    if (officials != null) {
                        if (officials.getStatus().matches(type)) {
                            officials.setOfficialName(name);
                            officials.setSync(0);
                            officials.setEdit(2);
                        }

                        bgRealm.copyToRealmOrUpdate(officials);
                        checkValues(2);
                        postEditOfficialDetails(officials);
                    }

                } catch (RealmException e) {
                    Toast.makeText(getApplicationContext(),
                            " " + e, Toast.LENGTH_SHORT).show();
                }
            }
        });

    }


    private void postEditOfficialDetails(MatchOfficials officials) {

        if (isNetworkAvailable()) {

            JSONObject jsonDetails = new JSONObject();
            try {
                if (!officials.getOfficialName().matches("")) {
                    jsonDetails.put("name", officials.getOfficialName());

                    if (officials.getStatus().matches("u1") || officials.getStatus().matches("u2"))
                        jsonDetails.put("type", "u");
                    else
                        jsonDetails.put("type", officials.getStatus());

                    jsonDetails.put("d4s_playerid", officials.getD4s_id());
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            JSONArray array = new JSONArray();
            array.put(jsonDetails);

            JSONObject jsonEdit = new JSONObject();
            try {
                jsonEdit.put("matchID", matchID);
                jsonEdit.put("officials", array);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            JSONObject jsonfeed = new JSONObject();
            try {
                jsonfeed.put("EditMatchOfficials", jsonEdit);
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

            Log.d(TAG, "postparams = " + postparams);

            JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                    Constants.CHASE_CRICKET_MATCH_API, postparams,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {

                            Log.d(TAG, "postEdit, response : " + response);

                            try {
                                if (!response.getBoolean("error") && response.getInt("status") == 200) {

                                    JSONObject jsonMatch = response.getJSONObject("match");
                                    Log.d(TAG, "postEdit, jsonMatch = " + jsonMatch);

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

                                                    MatchOfficials officials = bgRealm.where(MatchOfficials.class).
                                                            equalTo("matchID", jsonMatch.getString("app_matchID")).
                                                            equalTo("d4s_id", jsonMatch.getInt("playerid")).
                                                            findFirst();

                                                    if (officials != null) {

                                                        officials.setSync(1);
                                                        bgRealm.copyToRealm(officials);
                                                        Log.d(TAG, "postEdit, officials = " + officials);
                                                    }

                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        });
                                    } catch (RealmException e) {
                                        Log.d(TAG, "Exception : " + e);
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
//                                  Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                            Log.d(TAG, "postEdit, Error Message is  : " + error);
                        }
                    });

            MyApplicationClass.getInstance(getApplicationContext()).
                    addToRequestQueue(jsonObjReq, "postRequest");
            Log.d(TAG, "postEdit = " + jsonObjReq);
            Log.d(TAG, "postEdit = " + postparams);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.playerlist_addplayer, menu);//Menu Resource, Menu
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.add_new_player:
                addOfficial();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }


    void addOfficial() {

        ArrayList<String> list;

        View intervalView = View.inflate(this, R.layout.add_new_official, null);
        Spinner spinner = (Spinner) intervalView.findViewById(R.id.ano_sp_type);
        EditText editText = (EditText) intervalView.findViewById(R.id.ano_et_name);

        list = new ArrayList<String>();
        list.add("--Select--");
        if (!ot_array.contains("u1"))
            list.add("Umpire 1");
        if (!ot_array.contains("u2"))
            list.add("Umpire 2");
        if (!ot_array.contains("t"))
            list.add("Umpire 3");
        if (!ot_array.contains("f"))
            list.add("Umpire 4");
        if (!ot_array.contains("s"))
            list.add("Scorer");
        if (!ot_array.contains("r"))
            list.add("Match Referee");

        ArrayAdapter<String> adapterIntervals = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, list);
        adapterIntervals.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view
        spinner.setAdapter(adapterIntervals);

        /*spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                temp = adapterView.getItemAtPosition(position).toString();
                if (temp.matches("Umpire 1"))
                    t = "u1";
                else  if (temp.matches("Umpire 2"))
                    t = "u1";
                if (temp.matches("Umpire 3"))
                    t = "t";
                if (temp.matches("Umpire 4"))
                    t = "f";
                if (temp.matches("Scorer"))
                    t = "s";
                if (temp.matches("Match Referee"))
                    t = "r";


            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });*/

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setIcon(R.drawable.ball).
                setTitle("Add New Official").
                setView(intervalView).
                setCancelable(false).
                setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                        o_type = (String) spinner.getSelectedItem();
                        position = spinner.getSelectedItemPosition();
                        name = editText.getText().toString();
                        t = "";

                        Log.d(TAG, "o_type = " + o_type);
                        Log.d(TAG, "position = " + position);
                        Log.d(TAG, "name = " + name);

                        if ((position > 0) && !(name.matches(""))) {
                            if (o_type.matches("Umpire 1"))
                                t = "u1";
                            else if (o_type.matches("Umpire 2"))
                                t = "u2";
                            if (o_type.matches("Umpire 3"))
                                t = "t";
                            if (o_type.matches("Umpire 4"))
                                t = "f";
                            if (o_type.matches("Scorer"))
                                t = "s";
                            if (o_type.matches("Match Referee"))
                                t = "r";
                            saveOfficial(name, t);
                        } else
                            displayError("Invalid selection or name");
                    }
                }).
                setNegativeButton("Cancel", null).
                show();
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

                    /*else {

                        officials.setOfficialName(name);
                        officials.setSync(0);
                        officials.setEdit(2);
                    }*/

                    bgRealm.copyToRealmOrUpdate(officials);
                    checkValues(3);
                    postAddOfficialDetails(officials);

                } catch (RealmException e) {
                    Toast.makeText(getApplicationContext(),
                            " " + e, Toast.LENGTH_SHORT).show();
                }
            }
        });

    }


    // Added on 10/11/2021
    private void postAddOfficialDetails(MatchOfficials officials) {

        if (isNetworkAvailable()) {

            JSONArray arrayOfficials = new JSONArray();

            Log.d("ADD_OFFICIALS", "Scoring, matchID : " + matchID);

            JSONObject jsonOfficials = new JSONObject();

            try {
                if (!officials.getOfficialName().matches("")) {
                    jsonOfficials.put("name", officials.getOfficialName());

                    if (officials.getStatus().matches("u1") || officials.getStatus().matches("u2"))
                        jsonOfficials.put("type", "u");
                    else
                        jsonOfficials.put("type", officials.getStatus());

//                    if (officials.getD4s_id() == 0)
//                        jsonOfficials.put("d4s_playerid", 0);
//                    else
                    jsonOfficials.put("d4s_playerid", officials.getD4s_id());

                    arrayOfficials.put(jsonOfficials);
                }

            } catch (JSONException e) {
                e.printStackTrace();
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
                jsonfeed.put("AddMatchOfficialsIndividual", jsonObject);
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

                                    // Added on 11/11/2021
                                    JSONArray array = jsonMatch.getJSONArray("officials");
                                    if (array.length() > 0) {

                                        JSONObject object = array.getJSONObject(0);
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

                                                        MatchOfficials official = bgRealm.where(MatchOfficials.class).
                                                                equalTo("matchID", jsonMatch.getString("app_matchID")).
                                                                equalTo("status", object.getString("officialtype")).
                                                                equalTo("officialName", object.getString("officialname")).
                                                                findFirst();

                                                        if (official != null) {

                                                            officials.setSync(1);
                                                            officials.setD4s_id(object.getInt("officialid"));
                                                            bgRealm.copyToRealm(officials);
                                                            Log.d("ADO", "Individuals, official : " + official);
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
//                              Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                            Log.d("ADD_OFFICIALS", "Scoring, Error Message is  : " + error);

                        }
                    });

            MyApplicationClass.getInstance(getApplicationContext()).
                    addToRequestQueue(jsonObjReq, "postRequest");
            Log.d("ADD_OFFICIALS", "Scoring, jsonObjReq  : " + jsonObjReq);
            Log.d("ADD_OFFICIALS", "Scoring, postparams  : " + postparams);

        }
    }

    // === till here


    private void deleteOfficial(MatchOfficials officials) {

        new AlertDialog.Builder(AddEditOfficials.this)
                .setIcon(R.drawable.ball)
                .setTitle("Delete Official")
                .setMessage("Do you want to delete official \"" + officials.getOfficialName() + "\"?")
                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        delete(officials);
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }


    private void delete(MatchOfficials o) {

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
//
                        MatchOfficials officials = bgRealm.where(MatchOfficials.class).
                                equalTo("matchid", matchid).
                                equalTo("officialID", o.getOfficialID()).findFirst();

                        if (officials != null) {
                            if (officials.getD4s_id() == 0) {
                                if (!bgRealm.isInTransaction()) {
                                    bgRealm.beginTransaction();
                                }
                                officials.deleteFromRealm();
                                bgRealm.commitTransaction();

                            } else {
                                officials.setDelete(true);
                                bgRealm.copyToRealmOrUpdate(officials);
                                Log.d(TAG, "Delete, officials  " + officials);
                                checkValues(4);
                                postOfficialDeletion(officials);
                            }
                        }

                    } catch (RealmException e) {
                        Toast.makeText(getApplicationContext(),
                                " " + e, Toast.LENGTH_SHORT).show();
                    }
                }
            });

        } catch (RealmException e) {
            Log.d(TAG, "Exception : " + e);
        } finally {
            if (realm != null) {
                realm.close();
            }
        }
    }


    private void postOfficialDeletion(MatchOfficials officials) {

        if (isNetworkAvailable()) {

            JSONObject jsonDetails = new JSONObject();
            try {
                if (!officials.getOfficialName().matches("")) {
                    jsonDetails.put("name", officials.getOfficialName());

                    if (officials.getStatus().matches("u1") || officials.getStatus().matches("u2"))
                        jsonDetails.put("type", "u");
                    else
                        jsonDetails.put("type", officials.getStatus());

                    jsonDetails.put("d4s_playerid", officials.getD4s_id());
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            JSONArray array = new JSONArray();
            array.put(jsonDetails);

            JSONObject jsonEdit = new JSONObject();
            try {
                jsonEdit.put("matchID", matchID);
                jsonEdit.put("officials", array);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            JSONObject jsonfeed = new JSONObject();
            try {
                jsonfeed.put("DeleteMatchOfficials", jsonEdit);
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

            Log.d(TAG, "postDelete, postparams = " + postparams);

            JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                    Constants.CHASE_CRICKET_MATCH_API, postparams,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {

                            Log.d(TAG, "postDelete, response : " + response);

                            try {
                                if (!response.getBoolean("error") && response.getInt("status") == 200) {

                                    JSONObject jsonMatch = response.getJSONObject("match");
                                    Log.d(TAG, "postDelete, jsonMatch = " + jsonMatch);

//                                    try {

                                    Realm bgRealm;
                                    config = new RealmConfiguration.Builder()
                                            .name(AppConstants.GAME_ID + ".realm")
                                            .deleteRealmIfMigrationNeeded()
                                            .build();
                                    bgRealm = Realm.getInstance(config);
//                                        realm.executeTransaction(new Realm.Transaction() {
//                                            @Override
//                                            public void execute(Realm bgRealm) {

                                    try {
                                        MatchOfficials official = bgRealm.where(MatchOfficials.class).
                                                equalTo("matchID", jsonMatch.getString("app_matchID")).
                                                equalTo("officialID", officials.getOfficialID()).
                                                findFirst();

                                        if (official != null) {
                                            if (!bgRealm.isInTransaction()) {
                                                bgRealm.beginTransaction();
                                            }

                                            official.deleteFromRealm();
                                            bgRealm.commitTransaction();
                                        }

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
//                                            }
//                                        });
//                                    } catch (RealmException e) {
//                                        Log.d(TAG, "Exception : " + e);
//                                    } finally {
//                                        if (realm != null) {
//                                            realm.close();
//                                        }
//                                    }
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
                            Log.d(TAG, "postDelete, Error Message is  : " + error);
                        }
                    });

            MyApplicationClass.getInstance(getApplicationContext()).
                    addToRequestQueue(jsonObjReq, "postRequest");
            Log.d(TAG, "postDelete = " + jsonObjReq);
//            Log.d(TAG, "postDelete = " + postparams);
        }
    }


    // Added on 10/11/2021
    void displayError(String message) {

        new AlertDialog.Builder(this)
                .setIcon(R.drawable.ball)
                .setTitle("Invalid entry")
                .setMessage(message)
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                })
                .show();
    }
}