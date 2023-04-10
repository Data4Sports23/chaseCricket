package com.data4sports.chasecricket.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.InputFilter;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.data4sports.chasecricket.R;
import com.data4sports.chasecricket.applicationConstants.AppConstants;
import com.data4sports.chasecricket.models.Constants;
import com.data4sports.chasecricket.models.Match;
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

public class SelectTeamAXIActivity extends AppCompatActivity {

    TextView tv_head, popup_head, tv_playerA;
    TableLayout teamA_table, confirm_teamA_table;
    Button btn_previewA, btn_confirmA;

    Realm realm;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor mEditor;

    RealmConfiguration config;

    int matchid, playerA, playerB, squad_count = 0, i, captainAID = 0, vcAID = 0, wkAID = 0,
            captainBID = 0, vcBID = 0, wkBID = 0,
            total_player_count = 0, gameid = 0, squadAcount = 0, squadBcount = 0, count = 0,
            userId = 0, d4s_userid = 0;
    String matchID, teamA, teamB, confirm_head = "- Selected Players", preview_head = "Select Players for ",
            cap, vc, wk, capB, vcB, wkB;
    boolean[] selectedPlayers;
    boolean squadA = false, squadB = false;
    ArrayList<String> playerList;
    ArrayList<Player> playerDetailsList;
    ArrayList<Player> selectedPlayerDetailsList;
    ArrayList<Integer> selectedPID;

    PopupWindow popupTeamAXI;
    LinearLayout linearLayout;

    ImageButton back, popup_back;
    ImageButton back1;

    JSONArray arrayA_playerID, arraySquadA;
    JSONObject jsonTemp;

    private ProgressDialog progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_select_team_axi);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.custom_action_bar_layout);
        View view =getSupportActionBar().getCustomView();

        back1 = (ImageButton) view.findViewById(R.id.action_bar_back);

        back1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*finish();*/
                Log.e("selectaxi", "oncreate, back button pressed");

//                onBackPressed();
                startActivity(new Intent(SelectTeamAXIActivity.this, HomeActivity.class));
                finish();
            }
        });

        //Realm.init(this);
        //Realm.getDefaultConfiguration();
        Log.d("TAG", "onCreate: Game Id For particular match " + AppConstants.GAME_ID);
         config = new RealmConfiguration.Builder()
                .name(AppConstants.GAME_ID + ".realm")
                .deleteRealmIfMigrationNeeded()
                .build();
        realm = Realm.getInstance(config);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        linearLayout = findViewById(R.id.ll_team_axi);

        tv_head = findViewById(R.id.tv_teamA_head);
        tv_playerA = findViewById(R.id.tv_playersA);
//        back = findViewById(R.id.squad_a_back);

        btn_previewA = findViewById(R.id.btn_previewA);

        playerList = new ArrayList<String>();
        playerDetailsList = new ArrayList<Player>();
        selectedPlayerDetailsList = new ArrayList<Player>();
        selectedPID = new ArrayList<Integer>();
        getFromSP();

        // Added on 10/12/2021
        Match match = realm.where(Match.class)
                .equalTo("matchid", matchid)
                .findFirst();

        if (match != null) {
            if (match.isPulled()) {
//                displayProgress();
                getUpdatedPlayerCount();
            }
        }
        // === till here

//        displayProgress();
        displayTeamAList();

//        back.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////                Toast.makeText(SelectTeamAXIActivity.this, "Need to complete", Toast.LENGTH_SHORT).show();
//                onBackPressed();
//            }
//        });

        btn_previewA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                playerList.clear();
                selectedPlayerDetailsList.clear();
                selectedPID.clear();

                for (int j = 0; j < squad_count; j++) {
//            while (teamA_table.getChildCount() > 0) {
                    TableRow tableRow = (TableRow) teamA_table.getChildAt(j);
                    CheckBox cb = (CheckBox) tableRow.getChildAt(0);
                    if (cb.isChecked()) {
                        playerList.add(cb.getText().toString());
                        selectedPlayerDetailsList.add(playerDetailsList.get(j));
                        selectedPID.add(playerDetailsList.get(j).getPlayerID());
                        Log.d("checked", "playerList = " + playerList);
                        Log.d("checked", "selectedPlayerDetailsList = " + selectedPlayerDetailsList);
                        Log.d("checked", "selectedPID = " + selectedPID);
                    }

//            }
                }

                Log.d("checked", "playerList.size() = " + playerList.size());
                Log.d("checked", "playerA = " + playerA);

                if (playerList.size() == playerA) {
//                    preview();
                    Intent intent = new Intent(SelectTeamAXIActivity.this, ConfirmAXI.class);
                    intent.putExtra("mylist", selectedPID);
                    startActivity(intent);
                } else {
                    displayError("You've selected " + selectedPlayerDetailsList.size() +
                            ", please select " + playerA + " to continue");
//                    displayError();
                }


            }
        });

    }



    void getFromSP(){

        matchid = sharedPreferences.getInt("sp_match_id", 0);
        gameid = sharedPreferences.getInt("sp_game_id", 0);
        matchID = sharedPreferences.getString("sp_match_ID", null);

        userId = sharedPreferences.getInt("sp_user_id", 0);
        d4s_userid = sharedPreferences.getInt("d4s_userid", 0);

//        teamA = sharedPreferences.getString("sp_teamA",null);
//        teamB = sharedPreferences.getString("sp_teamB", null);
//        player_count = sharedPreferences.getInt("sp_player_count", 0);
//        playerA = sharedPreferences.getInt("sp_playerA", 0);
//        playerB = sharedPreferences.getInt("sp_playerB", 0);
//        sub = sharedPreferences.getInt("sp_sub_seq", 0);

        Match match = realm.where(Match.class)
                .equalTo("matchid", matchid)
                .findFirst();
        if (match != null) {
            teamA = match.getTeamA();
            teamB = match.getTeamB();
            playerA = match.getPlayerA();
            playerB = match.getPlayerB();
        }
    }



    void displayTeamAList() {

        i = -1;
//        String str = "", str1 = "";

        tv_head.setText(preview_head + teamA);
        tv_playerA.setText("Team A players/side  :  " + playerA);

        Log.d("matchid", "select 11, displayTeamAList : " + matchid);

//        progress.dismiss();

        RealmResults<Player> results = realm.where(Player.class).
                equalTo("matchid", matchid).
                equalTo("team", 1).
                sort("playerID", Sort.ASCENDING).
                findAll();

        results.load();
        Log.d("results1", "select 11, displayTeamAList : " + results);

        squad_count = results.size();
        selectedPlayers = new boolean[squad_count];

        teamA_table = findViewById(R.id.teamA_table);

        for (Player player : results) {

            ++count;

            playerDetailsList.add(player);

            TableRow row = new TableRow(this);
            row.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, 50));
            row.setPadding(10, 25, 10, 25);
//            row.setShowDividers(2);

            CheckBox cb = new CheckBox(this);
            cb.setText(player.getPlayerName());// + str + str1);
            cb.setId(i);
            row.addView(cb);

            teamA_table.addView(row);

            ++i;
            selectedPlayers[i] = false;
        }

    }



    void preview() {

        LayoutInflater layoutInflater = (LayoutInflater)
                SelectTeamAXIActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View teamADetails = layoutInflater.inflate(R.layout.confirm_teama_xi,null);

        popup_back = teamADetails.findViewById(R.id.confirm_axi_back);
        popup_head = teamADetails.findViewById(R.id.head_confirm_axi);
        confirm_teamA_table = teamADetails.findViewById(R.id.confirm_teamA_table);
        btn_confirmA = teamADetails.findViewById(R.id.btn_confirmA);

        //instantiate OUT popup window
        popupTeamAXI = new PopupWindow(teamADetails, LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
//        TypedValue tval = new TypedValue();
//        if (getTheme().resolveAttribute(android.R.attr.actionBarSize, tval, true))
//        {
//            int actionBarHeight = TypedValue.complexToDimensionPixelSize(tval.data,getResources().getDisplayMetrics());
//            popupTeamAXI = new PopupWindow(teamADetails, (WindowManager.LayoutParams.WRAP_CONTENT - actionBarHeight),
//                    LinearLayout.LayoutParams.MATCH_PARENT);
//        }

        popupTeamAXI.setFocusable(true);
        popupTeamAXI.setOutsideTouchable(isRestricted());
        popupTeamAXI.update();


        // display the popup window
//        popupTeamAXI.showAtLocation(linearLayout, Gravity.CENTER, 500, 500);
        popupTeamAXI.showAtLocation(linearLayout, Gravity.CENTER, 500, 500);

        popup_head.setText(teamA + confirm_head);

        popup_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupTeamAXI.dismiss();
            }
        });


        for (int i = 0; i < selectedPlayerDetailsList.size(); i++) {
            TableRow row_c = new TableRow(this);
            row_c.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, 50));
            row_c.setPadding(10, 25, 10, 25);
            row_c.setOrientation(LinearLayout.VERTICAL);
            row_c.setDividerPadding(3);
//            row_c.setShowDividers(LinearLayout.SHOW_DIVIDER_END);

            TextView tv = new TextView(this);
            tv.setText(playerList.get(i));
//            tv.setText(selectedPlayerDetailsList.get(i).getPlayerName());
            tv.setTextColor(Color.BLACK);
            row_c.addView(tv);

            View v = new View(this);
            v.setLayoutParams(new TableRow.LayoutParams(3, TableRow.LayoutParams.MATCH_PARENT));
            v.setBackgroundColor(Color.LTGRAY);
            row_c.addView(v);

            confirm_teamA_table.addView(row_c);
        }



        btn_confirmA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                displayProgress();
                arrayA_playerID = new JSONArray();

                for (int i = 0; i < selectedPlayerDetailsList.size(); i++) {

                    final int playerID = selectedPlayerDetailsList.get(i).getPlayerID();

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

//                                try {

                                    Player player = bgRealm.where(Player.class).
                                            equalTo("matchID", matchID).
                                            equalTo("playerID", playerID).findFirst();

                                    if (player != null) {
                                        player.setSubstitute(false);

                                        if (player.isCaptain()) {

                                            captainAID = playerID;
                                            cap = player.getPlayerName();
                                            Log.d("axi", "preview, captainAID : " + captainAID + ", cap : " + cap);
                                        }
                                        else if (player.isViceCaptain()) {
                                            vcAID = playerID;
                                            vc = player.getPlayerName();
                                            Log.d("axi", "preview, vcAID : " + vcAID + ", vc : " + vc);
                                        }
                                        if (player.isWicketKeeper()) {
                                            wkAID = playerID;
                                            wk = player.getPlayerName();
                                            Log.d("axi", "preview, wkAID : " + wkAID + ", wk : " + wk);
                                        }
                                        arrayA_playerID.put(playerID);
//                                        postJSON(playerID);
                                        bgRealm.copyToRealm(player);
                                    }

//                                } catch (JSONException e) {
//                                    e.printStackTrace();
//                                } catch (RealmPrimaryKeyConstraintException e) {
//                                    Toast.makeText(getApplicationContext(),
//                                            "Primary Key exists, Press Update instead",
//                                            Toast.LENGTH_SHORT).show();
//                                }
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

                saveToSP();
                startActivity(new Intent(SelectTeamAXIActivity.this, SelectTeamBXIActivity.class));
//                startActivity(new Intent(SelectTeamAXIActivity.this, AddSquad.class));
                finish();

                popupTeamAXI.dismiss();
//                progress.dismiss();

                /*squadBcount = realm.where(Player.class).
                        equalTo("matchid", matchid).
                        equalTo("team", 2).findAll().size();
                Log.d("squad", "pulled, squadBcount : " + squadBcount);*/


//                postJSON();

            }
        });




        /*AlertDialog alertDialog = new AlertDialog.Builder(SelectTeamAXIActivity.this).create();
        alertDialog.setIcon(R.drawable.ball);
        alertDialog.setCancelable(false);
        alertDialog.setTitle("Selected Players are");
        alertDialog.setMessage( "" +playerList);
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        alertDialog.show();*/

        Log.d("checked", "setPreview, playerList : " + playerList);
        Log.d("checked", "setPreview, selectedPlayerDetailsList : " + selectedPlayerDetailsList);
    }


    void saveToSP(){

        Log.d("axi", "saveToSP, captainAID : " + captainAID + ", cap : " + cap);
        Log.d("axi", "saveToSP, vcAID : " + vcAID + ", vc : " + vc);
        Log.d("axi", "saveToSP, wkAID : " + wkAID + ", wk : " + wk);
        mEditor = sharedPreferences.edit();
//        mEditor.putString("sp_team", teamB);
//        mEditor.putInt("sp_team_id", 2);
        mEditor.putInt("sp_captainA_id", captainAID);
        mEditor.putString("sp_captainA", cap);
        mEditor.putString("sp_vcA", vc);
        mEditor.putInt("sp_vcA_id", vcAID);
        mEditor.putString("sp_wkA", wk);
        mEditor.putInt("sp_wkA_id", wkAID);
        mEditor.apply();

    }



    void displayError() {

        AlertDialog alertDialog = new AlertDialog.Builder(SelectTeamAXIActivity.this).create();
        alertDialog.setIcon(R.drawable.ball);
        alertDialog.setCancelable(false);
        alertDialog.setTitle("Invalid player count");
//        alertDialog.setMessage( "" +playerList);
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        alertDialog.show();
    }


 /*   private void postJSON() {

        if (arrayA_playerID.length() > 0) {

            JSONObject jsonPlayerID = new JSONObject();
            JSONObject jsonTeamA = new JSONObject();
            JSONObject jsonMatchID = new JSONObject();
            try {
                jsonPlayerID.put("players", arrayA_playerID);
                jsonMatchID.put("matchID", matchID);
                jsonTeamA.put("TeamA", jsonPlayerID);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            JSONArray arrayAddPlayers = new JSONArray();
            arrayAddPlayers.put(jsonMatchID);
            arrayAddPlayers.put(jsonTeamA);

            JSONObject jsonAddPlayers = new JSONObject();
            JSONObject postparams = new JSONObject();
            try {
                jsonAddPlayers.put("AddPlayers", arrayAddPlayers);
                postparams.put("title", "CHASE_POST");
                postparams.put("feed", jsonAddPlayers);

            } catch (JSONException e) {
                e.printStackTrace();
            }

            if (isNetworkAvailable()) {

                JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                        Constants.CHASE_CRICKET_MATCH_API, postparams,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {

//                                try {

                                    Log.e("captain", "response : " + response);

//                                } catch (JSONException e) {
//                                    e.printStackTrace();
//                                }

                            }

                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {

                                Log.e("captain", "Error Message is  : " + error.getMessage());

                            }
                        });

                MyApplicationClass.getInstance(getApplicationContext()).addToRequestQueue(jsonObjReq, "postRequest");
                Log.d("captain", "jsonObjReq  : " + jsonObjReq);
                Log.d("captain", "postparams  : " + postparams);


            }

            startActivity(new Intent(SelectTeamAXIActivity.this, SelectTeamBXIActivity.class));
            finish();
        }

    }*/


    private boolean isNetworkAvailable(){
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
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
//                        match.setCaptain(false);
                        match.setToss(false);
//                        match.setOpeners(false);
//                        match.setScoring(false);
//                        match.setScoreCard(false);
//                        match.setEndOfInnings(false);
//                        match.setPulledMatch(false);
//                        match.setAddSquad(false);
                        match.setSelectAXI(true);
//                        match.setSelectBXI(false);
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


    @Override
    public void onBackPressed() {
        super.onBackPressed();

//        mEditor = sharedPreferences.edit();
//        mEditor.putInt("sp_match_id", matchid);
//        mEditor.putString("sp_match_ID", matchID);
//        mEditor.putString("sp_team", teamA);
//        mEditor.putInt("sp_team_id", 1);
//        mEditor.putInt("sp_player_count", player_count);
//        mEditor.putInt("sp_sub_seq", sub);
//        mEditor.apply();
        /*squadAcount = realm.where(Player.class).
                equalTo("matchid", matchid).
                equalTo("team", 1).findAll().size();
        Log.d("squad", "pulled, squadAcount : " + squadAcount);
        startActivity(new Intent(SelectTeamAXIActivity.this, AddSquad.class));
        finish();*/
            startActivity(new Intent(this, HomeActivity.class));
            finish();

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.refreshteamaxi, menu);//Menu Resource, Menu
        getMenuInflater().inflate(R.menu.playercount, menu);//Menu Resource, Menu
        getMenuInflater().inflate(R.menu.refresh_pulled, menu);//Menu Resource, Menu
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

/*            case R.id.refreshaxi:
                refresh();
                return true;*/

            case R.id.addplayer_axi:
                Intent i = new Intent(SelectTeamAXIActivity.this, AddSquad.class);
                i.putExtra("team_id", 1);
                i.putExtra("team", teamA);
                i.putExtra("player_count", count);
                startActivity(i);
                finish();
                return true;

            case R.id.playercount:
                updateCount();
                return true;

            case R.id.refresh_pulled:
                pullSquad();

            default:
                return super.onOptionsItemSelected(item);
        }

    }


    // Added on 08/12/2021
    void updateCount() {

        Match match = realm.where(Match.class)
                .equalTo("matchid", matchid)
                .findFirst();

        if (match != null) {
            if (match.isPulled()) {
//                displayProgress();
                getUpdatedPlayerCount();
            } else {

                View intervalView = View.inflate(this, R.layout.playercount, null);
                TextView tv_teamA = (TextView) intervalView.findViewById(R.id.pc_tv_teamA);
                TextView tv_teamB = (TextView) intervalView.findViewById(R.id.pc_tv_teamB);
                EditText et_playerA = (EditText) intervalView.findViewById(R.id.pc_et_playerA);
                EditText et_playerB = (EditText) intervalView.findViewById(R.id.pc_et_playerB);

                tv_teamA.setText(teamA);
                tv_teamB.setText(teamB);
                et_playerA.setInputType(InputType.TYPE_CLASS_NUMBER);
                et_playerB.setInputType(InputType.TYPE_CLASS_NUMBER);
                int maxLength = 2;
                et_playerA.setFilters(new InputFilter[] {new InputFilter.LengthFilter(maxLength)});
                et_playerB.setFilters(new InputFilter[] {new InputFilter.LengthFilter(maxLength)});
                et_playerA.setText("" + playerA);
                et_playerB.setText("" + playerB);

                new AlertDialog.Builder(SelectTeamAXIActivity.this)
                        .setIcon(R.drawable.ball)
                        .setTitle("Update Player Count/team")
                        .setView(intervalView)
//                        .setMessage("test")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
//                                displayProgress();

                                String str1 = et_playerA.getText().toString();
                                String str2 = et_playerB.getText().toString();

                                if ((str1.matches("")) || (str2.matches(""))) {
//                                    progress.dismiss();
                                    Toast.makeText(getApplicationContext(),
                                            "Invalid player input", Toast.LENGTH_SHORT).show();

                                } else {
                                    playerA = Integer.parseInt(str1);
                                    playerB = Integer.parseInt(str2);

                                    if ((playerA == 0) || (playerB == 0)) {
//                                        progress.dismiss();
                                        Toast.makeText(getApplicationContext(),
                                                "Invalid player count", Toast.LENGTH_SHORT).show();
                                    } else {
                                        updateMatch(playerA, playerB);
                                    }
                                }
                            }
                        })
                        .setNegativeButton("Cancel", null)
                        .show();
            }
        }
    }


    void updateMatch(int playerA, int playerB) {

         config = new RealmConfiguration.Builder()
                .name(AppConstants.GAME_ID + ".realm")
                .deleteRealmIfMigrationNeeded()
                .build();
        realm = Realm.getInstance(config);
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
                        Match match = bgRealm.where(Match.class)
                                .equalTo("matchid", matchid)
                                .findFirst();
                        if (match != null) {
                            match.setPlayerA(playerA);
                            match.setPlayerB(playerB);
                            bgRealm.copyToRealmOrUpdate(match);
//                            progress.dismiss();
                            tv_playerA.setText("Team A players/side  :  " + playerA);
                        }
                    } catch (RealmPrimaryKeyConstraintException e) {
                        Toast.makeText(getApplicationContext(),
                                "Primary Key exists, Press Update instead", Toast.LENGTH_SHORT).show();
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


    private void getUpdatedPlayerCount() {

        if (isNetworkAvailable()) {

            // it is a GET methoid,  string request
            StringRequest stringRequest = new StringRequest(Request.Method.GET,
                    Constants.CHASE_CRICKET_PULL_MATCH_API_TEST + d4s_userid,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

//                            progress.dismiss();
                            try {

                                Log.d("response", "AssignedMatchList, " + response);
                                JSONArray array = new JSONArray(response);
                                Log.d("array", "AssignedMatchList, " + array);
                                if (array.length() > 0) {

                                    for (int i = 0; i < array.length(); i++) {

                                        JSONObject object = array.getJSONObject(i);
                                        int id = Integer.parseInt(object.getString("gameid"));
                                        if (id == gameid) {
                                            if ((object.getString("teamAplayers") != null) &&
                                                    (object.getString("teamBplayers") != null)) {
                                                playerA = Integer.parseInt(object.getString("teamAplayers"));
                                                playerB = Integer.parseInt(object.getString("teamAplayers"));
                                                if (playerA > 0 && playerB > 0) {
//                                                    progress.dismiss();
                                                    updateMatch(playerA, playerB);
                                                }
                                                else {
//                                                    progress.dismiss();
                                                    displayError();
                                                }
                                            } else {
//                                                progress.dismiss();
                                                displayError();
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
//                            progress.dismiss();
                            Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
            //creating a request queue
            RequestQueue requestQueue = Volley.newRequestQueue(this);

            //adding the string request to request queue
            requestQueue.add(stringRequest);
        }
    }
    // === till here



/*
    private void refresh() {

        jsonTemp = new JSONObject();
        PulledMatchDetailsActivity pulledMatch = new PulledMatchDetailsActivity();

        if (isNetworkAvailable()) {

            StringRequest stringRequest = new StringRequest(Request.Method.GET,
                    Constants.CHASE_CRICKET_PULL_SQUAD_API_TEST + "" + gameid,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {


                            try {

                                JSONArray array = new JSONArray(response);
                                Log.d("array", "" + array);

                                JSONObject jsonSquad = array.getJSONObject(0);
                                Log.d("jsonSquad", "" + jsonSquad);

                                arraySquadA = jsonSquad.getJSONArray("squadA");

                                if (arraySquadA.length() >= playerA) {
                                    total_player_count = arraySquadA.length();

                                    for (int i = 0; i < arraySquadA.length(); i++) {

                                        try {
                                            jsonTemp = arraySquadA.getJSONObject(i);
                                            pulledMatch.savePlayers(1,
                                                    jsonTemp.getString("name"),
                                                    jsonTemp.getString("captain"),
                                                    jsonTemp.getString("vice_captain"),
                                                    jsonTemp.getString("wicketkeeper"),
                                                    jsonTemp.getInt("d4s_playerid"),
                                                    true);
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }



                                    }

                                    displayTeamAList();
                                    */
/*startActivity(new Intent(SelectTeamAXIActivity.this, SelectTeamAXIActivity.class));
                                    finish();*//*



                                }

                                else {

                                    displayError();
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

//           tempObject = matchObject;
//           aveToDevice();

        }


        else {

            progress.dismiss();
            AlertDialog alertDialog = new AlertDialog.Builder(SelectTeamAXIActivity.this).create();
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
*/



    public void displayProgress(){

        progress = new ProgressDialog(this);
        progress.setMessage("Please wait...");
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setIndeterminate(true);
        progress.setProgress(0);
        progress.show();
    }



    void displayError(String message) {

//        progress.dismiss();
        AlertDialog alertDialog = new AlertDialog.Builder(SelectTeamAXIActivity.this).create();
        alertDialog.setIcon(R.drawable.ball);
        alertDialog.setCancelable(false);
//        alertDialog.setTitle("Values Missing");
        alertDialog.setMessage(message);
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        alertDialog.show();
    }



    // Added on 15/12/2021
    private void pullSquad() {

        displayProgress();

        Match match = realm.where(Match.class)
                .equalTo("matchid", matchid)
                .findFirst();
        if (match != null) {
            if (match.isPulled_squad()) {
                refreshPlayerList();
                pullSquad(match.getTeamAId(), match.getTeamBId());
                displayTeamAList();
            }
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

                            progress.dismiss();
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
                                        updateMatchSquad(squadA, squadB);
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
            AlertDialog alertDialog = new AlertDialog.Builder(SelectTeamAXIActivity.this).create();
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



    private void saveSquad(int team, String name, String capt, String vca, String wka, int d4s_pid, int d4s_tid, int gameid) {


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


                        if (capt.matches("yes")) {

                            Log.d("polled", "captain : " + name + ", team : " + team);
                            player.setCaptain(true);

                            if (team == 1) {
                                cap = name;
                                captainAID = player_id;
                            }

                            else if (team == 2) {
                                capB = name;
                                captainBID = player_id;
                            }
                        }
                        else
                            player.setCaptain(false);


                        if (vca.matches("yes")) {
                            Log.d("polled", "vc : " + name + ", team : " + team);
                            player.setViceCaptain(true);

                            if (team == 1) {
                                vc = name;
                                vcAID = player_id;
                            }

                            else if (team == 2) {
                                vcB = name;
                                vcBID = player_id;
                            }
                        }
                        else
                            player.setViceCaptain(false);


                        if (wka.matches("yes")) {
                            Log.d("polled", "wk : " + name + ", team : " + team);
                            player.setWicketKeeper(true);

                            if (team == 1) {
                                wk = name;
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



    private void updateMatchSquad(boolean squadA, boolean squadB) {

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



    void refreshPlayerList(){
        Log.d("SAXI", "teamA_table.getChildCount() = " + teamA_table.getChildCount());
        while (teamA_table.getChildCount() > 0){
            TableRow tableRow = (TableRow) teamA_table.getChildAt(0);
            teamA_table.removeView(tableRow);
        }
    }

}
