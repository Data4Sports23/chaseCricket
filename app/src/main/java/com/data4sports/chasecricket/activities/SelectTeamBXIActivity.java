package com.data4sports.chasecricket.activities;

import android.app.ProgressDialog;
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
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
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

public class SelectTeamBXIActivity extends AppCompatActivity {

    TextView tv_head, popup_head, tv_playerB;
    TableLayout teamB_table, confirm_teamB_table;
    Button btn_previewB, btn_confirmB;

    Realm realm;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor mEditor;

    int matchid, playerA, playerB, squad_count = 0, i, capBID = 0, vcBID = 0, wkBID = 0,
            total_player_count = 0, gameid = 0, squadBcount = 0, count = 0;
    String matchID, teamA, teamB,confirm_head = "- SELECTED PLAYERS", preview_head = "Select Players for ", capB, vcB, wkB;
    boolean[] selectedPlayers;
    ArrayList<String> playerList;
    ArrayList<Player> playerDetailsList;
    ArrayList<Player> selectedPlayerDetailsList;
    ArrayList<Integer> selectedPID;

    PopupWindow popupTeamBXI;
    LinearLayout linearLayout;

    ImageButton back, popup_back;

    JSONArray arrayB_playerID, arraySquadB;
    JSONObject jsonTemp;

    private ProgressDialog progress;

    RealmConfiguration config;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_select_team_bxi);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.custom_action_bar_layout);
        View view =getSupportActionBar().getCustomView();

        back = (ImageButton) view.findViewById(R.id.action_bar_back);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*finish();*/
                Log.e("selectbxi", "oncreate, back button pressed");

//                onBackPressed();
                startActivity(new Intent(SelectTeamBXIActivity.this, HomeActivity.class));
                finish();
            }
        });

        Realm.init(this);
        Realm.getDefaultConfiguration();
         config = new RealmConfiguration.Builder()
                .name(AppConstants.GAME_ID + ".realm")
                .deleteRealmIfMigrationNeeded()
                .build();
        realm = Realm.getInstance(config);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        linearLayout = findViewById(R.id.ll_team_bxi);

        tv_head = findViewById(R.id.tv_teamB_head);
        tv_playerB = findViewById(R.id.tv_playersB);
//        back = findViewById(R.id.squad_b_back);

        btn_previewB = findViewById(R.id.btn_previewB);

        playerList = new ArrayList<String>();
        playerDetailsList = new ArrayList<Player>();
        selectedPlayerDetailsList = new ArrayList<Player>();
        selectedPID = new ArrayList<Integer>();
        getFromSP();

        displayTeamBList();

//        back.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////                Toast.makeText(SelectTeamBXIActivity.this, "Need to complete", Toast.LENGTH_SHORT).show();
//                onBackPressed();
//            }
//        });

        btn_previewB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.d("squad", "count, " + squad_count);
                playerList.clear();
                selectedPlayerDetailsList.clear();

                for (int j = 0; j < squad_count; j++) {
//            while (teamA_table.getChildCount() > 0) {
                    TableRow tableRow = (TableRow) teamB_table.getChildAt(j);
                    CheckBox cb = (CheckBox) tableRow.getChildAt(0);
                    if (cb.isChecked()) {
                        playerList.add(cb.getText().toString());
                        selectedPlayerDetailsList.add(playerDetailsList.get(j));
                        selectedPID.add(playerDetailsList.get(j).getPlayerID());
                        Log.d("checked", "setPreview, " + playerList);
                        Log.d("checked", "setPreview, " + selectedPlayerDetailsList);
                    }

//            }
                }

                if (playerList.size() == playerB) {
//                    viewPreview();
                    Intent intent = new Intent(SelectTeamBXIActivity.this, ConfirmBXI.class);
                    intent.putExtra("mylist", selectedPID);
                    startActivity(intent);
                } else
                    displayError("You've selected " + selectedPlayerDetailsList.size() +
                            ", please select " + playerB + " to continue");
            }
        });
    }


    void getFromSP(){

        matchid = sharedPreferences.getInt("sp_match_id", 0);
        gameid = sharedPreferences.getInt("sp_game_id", 0);
        matchID = sharedPreferences.getString("sp_match_ID", null);
//        teamA = sharedPreferences.getString("sp_teamA",null);
//        teamB = sharedPreferences.getString("sp_teamB", null);
//        playerA = sharedPreferences.getInt("sp_playerA", 0);
//        playerB = sharedPreferences.getInt("sp_playerB", 0);
//        player_count = sharedPreferences.getInt("sp_player_count", 0);
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



    void displayTeamBList() {

        i = -1;
//        String str, str1;

        tv_head.setText(preview_head + teamB);
        tv_playerB.setText("Team A players/side  :  " + playerB);

        Log.d("matchid", "displayTeamAList : " + matchid);

        RealmResults<Player> results = realm.where(Player.class).
                equalTo("matchid", matchid).
                equalTo("team", 2).
                sort("playerID", Sort.ASCENDING).
                findAll();

        results.load();
        Log.d("results", "displayTeamAList : " + results);

        squad_count = results.size();
        selectedPlayers = new boolean[squad_count];


        teamB_table = findViewById(R.id.teamB_table);

        /*TableRow rowh = new TableRow(this);
        rowh.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, 50));
        rowh.setPadding(10, 25, 10, 25);

        CheckBox cbh = new CheckBox(this);
        cbh.setText("");
        cbh.setId(i);
        rowh.addView(cbh);
*/
        /*TextView tv = new TextView(this);
        tv.setText("");
        tv.setTextColor(Color.BLACK);
        rowh.addView(tv);
        */

        for (Player player : results) {

            ++count;

            /*str = "";
            str1 = "";*/
            playerDetailsList.add(player);
            /*if (player.isCaptain())
                str = "  (c)";
            else if (player.isViceCaptain())
                str = "  (vc)";

            if (player.isWicketKeeper())
                str1 = "  (wk)";*/


            TableRow row = new TableRow(this);
            row.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, 50));
            row.setPadding(10, 25, 10, 25);

            CheckBox cb = new CheckBox(this);
            cb.setText(player.getPlayerName());// + str + str1);
            cb.setId(i);
            row.addView(cb);

           /* TextView tv = new TextView(this);
            tv.setText(player.getPlayerName());
            tv.setTextColor(Color.BLACK);
            row.addView(tv);*/

            teamB_table.addView(row);

            ++i;
            selectedPlayers[i] = false;
        }
    }



    void viewPreview() {




        LayoutInflater layoutInflater = (LayoutInflater)
                SelectTeamBXIActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View teamBDetails = layoutInflater.inflate(R.layout.confirm_teamb_xi,null);

        popup_back = teamBDetails.findViewById(R.id.confirm_bxi_back);
        popup_head = teamBDetails.findViewById(R.id.head_confirm_bxi);
        confirm_teamB_table = teamBDetails.findViewById(R.id.confirm_teamB_table);
        btn_confirmB = teamBDetails.findViewById(R.id.btn_confirmB);

        //instantiate OUT popup window
        popupTeamBXI = new PopupWindow(teamBDetails, LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        popupTeamBXI.setFocusable(true);
        popupTeamBXI.setOutsideTouchable(isRestricted());
        popupTeamBXI.update();


        //display the OUT popup window
        popupTeamBXI.showAtLocation(linearLayout, Gravity.CENTER, 500, 500);

        popup_head.setText(teamB + confirm_head);

        popup_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupTeamBXI.dismiss();
            }
        });


        for (int i = 0; i < selectedPlayerDetailsList.size(); i++) {
            TableRow row_c = new TableRow(this);
            row_c.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, 50));
            row_c.setPadding(10, 25, 10, 25);
            row_c.setOrientation(LinearLayout.VERTICAL);


            TextView tv = new TextView(this);
            tv.setText(playerList.get(i));
//            tv.setText(selectedPlayerDetailsList.get(i).getPlayerName());
            tv.setTextColor(Color.BLACK);
            row_c.addView(tv);

            View v = new View(this);
            v.setLayoutParams(new TableRow.LayoutParams(3, TableRow.LayoutParams.MATCH_PARENT));
            v.setBackgroundColor(Color.LTGRAY);
            row_c.addView(v);

            confirm_teamB_table.addView(row_c);
        }



        btn_confirmB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                displayProgress();

                arrayB_playerID = new JSONArray();



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
                                        capBID = playerID;
                                        capB = player.getPlayerName();
                                    }
                                    else if (player.isViceCaptain()) {
                                        vcBID = playerID;
                                        vcB = player.getPlayerName();
                                    }
                                    if (player.isWicketKeeper()) {
                                        wkBID = playerID;
                                        wkB = player.getPlayerName();
                                    }

                                    arrayB_playerID.put(playerID);
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

//                postJSON();


                saveToSP();
//                startActivity(new Intent(SelectTeamBXIActivity.this, CaptainActivity.class));
                startActivity(new Intent(SelectTeamBXIActivity.this, CaptainActivity.class));
                finish();
                popupTeamBXI.dismiss();

                progress.dismiss();
//                finish();

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

        Log.d("saveToSP", "selectedPlayerDetailsList : " + selectedPlayerDetailsList);
        Log.d("saveToSP", "playerB : " + playerB);
        Log.d("saveToSP", "capB : " + capB);
        Log.d("saveToSP", "capBID : " + capBID);
        Log.d("saveToSP", "vcB : " + vcB);
        Log.d("saveToSP", "vcBID : " + vcBID);
        Log.d("saveToSP", "wkB : " + capB);
        Log.d("saveToSP", "wkBID : " + wkBID);

        mEditor = sharedPreferences.edit();
        mEditor.putString("sp_captainB", capB);
        mEditor.putInt("sp_captainB_id", capBID);
        mEditor.putString("sp_vcB", vcB);
        mEditor.putInt("sp_vcB_id", vcBID);
        mEditor.putString("sp_wkB", wkB);
        mEditor.putInt("sp_wkB_id", wkBID);
        mEditor.apply();

    }



    void displayError(String message) {

        AlertDialog alertDialog = new AlertDialog.Builder(SelectTeamBXIActivity.this).create();
        alertDialog.setIcon(R.drawable.ball);
        alertDialog.setCancelable(false);
        alertDialog.setTitle(message);
//        alertDialog.setMessage( "" +playerList);
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        alertDialog.show();
    }



   /* private void postJSON() {

        if (arrayB_playerID.length() > 0) {

            JSONObject jsonPlayerID = new JSONObject();
            JSONObject jsonTeamA = new JSONObject();
            JSONObject jsonMatchID = new JSONObject();
            try {
                jsonPlayerID.put("players", arrayB_playerID);
                jsonMatchID.put("matchID", matchID);
                jsonTeamA.put("TeamB", jsonPlayerID);
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

            startActivity(new Intent(SelectTeamBXIActivity.this, CaptainActivity.class));
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
//                        match.setToss(false);
//                        match.setOpeners(false);
//                        match.setScoring(false);
//                        match.setScoreCard(false);
//                        match.setEndOfInnings(false);
//                        match.setPulledMatch(false);
                        match.setConfirmAXI(false);
                        match.setSelectBXI(true);
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
            startActivity(new Intent(this, HomeActivity.class));
            finish();

        /*squadBcount = realm.where(Player.class).
                equalTo("matchid", matchid).
                equalTo("team", 2).findAll().size();
        Log.d("squad", "pulled, squadBcount : " + squadBcount);
        startActivity(new Intent(SelectTeamBXIActivity.this, AddSquad.class));
        finish();*/

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.refreshteamaxi, menu);//Menu Resource, Menu
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

           /* case R.id.add_match_note:
                refresh();
                return true;*/

            case R.id.addplayer_axi:
                Intent i = new Intent(SelectTeamBXIActivity.this, AddSquad.class);
                i.putExtra("team_id", 2);
                i.putExtra("team", teamB);
                i.putExtra("player_count", count);
                startActivity(i);
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }

    }



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

                                arraySquadB = jsonSquad.getJSONArray("squadB");

                                if (arraySquadB.length() >= playerB) {
                                    total_player_count = arraySquadB.length();

                                    for (int i = 0; i < arraySquadB.length(); i++) {

                                        try {
                                            jsonTemp = arraySquadB.getJSONObject(i);
                                            pulledMatch.savePlayers(2,
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

                                    displayTeamBList();
                                    */
/*startActivity(new Intent(SelectTeamAXIActivity.this, SelectTeamBXIActivity.class));
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
            AlertDialog alertDialog = new AlertDialog.Builder(SelectTeamBXIActivity.this).create();
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



    void displayError() {

        progress.dismiss();
        AlertDialog alertDialog = new AlertDialog.Builder(SelectTeamBXIActivity.this).create();
        alertDialog.setIcon(R.drawable.ball);
        alertDialog.setCancelable(false);
        alertDialog.setTitle("Not enough player details");
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        //commented on 29/04/20
                        /*startActivity(new Intent(SelectTeamBXIActivity.this,
                                AddPlayers.class));*/
                        startActivity(new Intent(SelectTeamBXIActivity.this,
                                AddPlayersA.class));
                        finish();

                    }
                });

        alertDialog.show();
    }


}
