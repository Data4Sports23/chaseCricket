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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
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
import com.data4sports.chasecricket.models.MatchNotes;
import com.data4sports.chasecricket.models.MatchOfficials;
import com.data4sports.chasecricket.models.MyApplicationClass;
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

/**
 * Updated on 29/01/2021
 */
public class CaptainActivity extends AppCompatActivity  implements AdapterView.OnItemSelectedListener {

    TextView teamAName, teamBname;
    Spinner spinner_captain_A, spinner_captain_B,  spinner_vc_A, spinner_vc_B, spinner_wk_A, spinner_wk_B;
    Button next, delete;
    int matchid, team = 0;

    AlertDialog.Builder addNoteBuilder;
    String teamA, teamB, matchID, token, type;
    String captainA = null, captainB = null;
    int captainAID = 0, captainBID = 0, userId = 0;
    String viceCaptainA = null, viceCaptainB = null;
    int viceCaptainAID = 0, viceCaptainBID = 0;
    String wkA = null, wkB = null, matchNote = "";
    int wkAID = 0, wkBID = 0;

    SharedPreferences sharedPreferences;

    //String outputA[] = {}, outputB[] = {};
    ArrayList<String> teamAList, teamBList;
    ArrayAdapter<String> adapterTeamA, adapterTeamB;
    ArrayList<Integer> teamA_playerIDs, teamB_playerIDs;

    Realm realm;

    SharedPreferences.Editor mEditor;
    private ProgressDialog progress;

    boolean squad = false, matchnote = false;
    int flag_vcA = 0, flag_vcB = 0, flag_vc = 0;

    RealmConfiguration config;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_captain);

        sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(this);

        assignElements();

        getFromSP();

        setTeamNames();

         config = new RealmConfiguration.Builder()
                .name(AppConstants.GAME_ID + ".realm")
                .deleteRealmIfMigrationNeeded()
                .build();
        realm = Realm.getInstance(config);


        delete = findViewById(R.id.delete);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                realm.beginTransaction();
                RealmResults<Player> delete = realm.where(Player.class).findAll();
                delete.deleteAllFromRealm();
//                Toast.makeText(getApplicationContext(), "Deleted", Toast.LENGTH_SHORT).show();
//                try {
//                    if (delete.isLoaded()) {
//                        delete.deleteAllFromRealm();
//                        realm.commitTransaction();
//                        Toast.makeText(getApplicationContext(), "Loaded REalm", Toast.LENGTH_SHORT).show();
//                    }
//                    else {
//                        Toast.makeText(getApplicationContext(), "Empty Realm Result", Toast.LENGTH_SHORT).show();
//                    }
//                }
//                catch (RealmException e){
//                    Toast.makeText(getApplicationContext(), "Exception : "+e, Toast.LENGTH_SHORT).show();
//                }

            }
        });


        readFromRealm();

        setSpinnerValues();
        spinner_captain_A.setOnItemSelectedListener(this);
        spinner_vc_A.setOnItemSelectedListener(this);
        spinner_wk_A.setOnItemSelectedListener(this);
        spinner_captain_B.setOnItemSelectedListener(this);
        spinner_vc_B.setOnItemSelectedListener(this);
        spinner_wk_B.setOnItemSelectedListener(this);


        // if isCaptain
//        checkAlreadySelected();

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                checkSpinnerValues();
            }
        });
    }



    // assign elements to its corresponding UI Elements

    public void assignElements(){

        teamAName = findViewById(R.id.txt_cap_teamA);
        teamBname = findViewById(R.id.txt_cap_teamB);

        spinner_captain_A = findViewById(R.id.cap_teamA_spinner);
        spinner_captain_B = findViewById(R.id.cap_teamB_spinner);
        spinner_vc_A = findViewById(R.id.vc_teamA_spinner);
        spinner_vc_B = findViewById(R.id.vc_teamB_spinner);
        spinner_wk_A = findViewById(R.id.wk_teamA_spinner);
        spinner_wk_B = findViewById(R.id.wk_teamB_spinner);

        next = findViewById(R.id.cap_next_btn);

        teamAList = new ArrayList<String>();
        teamBList = new ArrayList<String>();
        teamA_playerIDs = new ArrayList<Integer>();
        teamB_playerIDs = new ArrayList<Integer>();
    }




    // get values from Sharedprefernces

    void getFromSP(){

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        matchid = sharedPreferences.getInt("sp_match_id", 0);
        teamA = sharedPreferences.getString("sp_teamA", null);
        teamB = sharedPreferences.getString("sp_teamB", null);
        matchID = sharedPreferences.getString("sp_match_ID", null);
        type = sharedPreferences.getString("sp_innings", null);
        token = sharedPreferences.getString("user_token", null);
        squad = sharedPreferences.getBoolean("squad", false);
    }




    // Set names of teams

    void setTeamNames() {

        if (teamA != null) {
            teamAName.setText(teamA);
        }
        if (teamB != null) {
            teamBname.setText(teamB);
        }

    }




    //Read Player details from database

    void readFromRealm(){

        // list of players that display on spinner

       teamAList.add("--select--");
       teamBList.add("--select--");

        RealmResults<Player> result = realm.where(Player.class).
                equalTo("matchid", matchid).
                equalTo("team", 1).
                equalTo("substitute", false).
                equalTo("retired", false).findAll();     //fetching the data

        result.load();
        for(Player player:result){
            Log.d("captain", "player, 1 : " + player);
            teamAList.add(player.getPlayerName());     // add player names to ArrayList (Team 1)
            teamA_playerIDs.add(player.getPlayerID()); // add player ids to ArrayList (Team 1)
        }


        result = realm.where(Player.class).
                equalTo("matchid", matchid).
                equalTo("team", 2).
                equalTo("substitute", false).
                equalTo("retired", false).findAll();    //fetching the data

        result.load();
        for(Player player:result){
            Log.d("captain", "player, 2 : " + player);
            teamBList.add(player.getPlayerName());      // add player names to ArrayList (Team 2)
            teamB_playerIDs.add(player.getPlayerID()); // add player ids to ArrayList (Team 2)
        }
    }




    // Updatedon 29/01/2021
    void setSpinnerValues(){

        adapterTeamA = new ArrayAdapter<String>(this,   android.R.layout.simple_spinner_item, teamAList);
        adapterTeamA.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view
        spinner_captain_A.setAdapter(adapterTeamA);
        spinner_vc_A.setAdapter(adapterTeamA);
        spinner_wk_A.setAdapter(adapterTeamA);

        adapterTeamB = new ArrayAdapter<String>(this,   android.R.layout.simple_spinner_item, teamBList);
        adapterTeamB.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view
        spinner_captain_B.setAdapter(adapterTeamB);
        spinner_vc_B.setAdapter(adapterTeamB);
        spinner_wk_B.setAdapter(adapterTeamB);

        // Addred on 29/01/2021
        // to check if already values are assigned or not
        RealmResults<Player> result_A = realm.where(Player.class).
                equalTo("matchid", matchid).
                equalTo("team", 1).
                equalTo("substitute", false).
                equalTo("retired", false)
                .sort("playerID", Sort.ASCENDING).
                findAll();

        for (Player playerA : result_A) {
            if (playerA.isCaptain()) {
                captainA = playerA.getPlayerName();
                captainAID = playerA.getPlayerID();
                int spinnerPosition = adapterTeamA.getPosition(captainA);//season_adapter.getPosition(season);
                spinner_captain_A.setSelection(spinnerPosition);//sesp_season.setSelection(spinnerPosition);
            }

            else if (playerA.isViceCaptain()) {
                viceCaptainA = playerA.getPlayerName();
                viceCaptainAID = playerA.getPlayerID();
                int spinnerPosition = adapterTeamA.getPosition(viceCaptainA);//season_adapter.getPosition(season);
                spinner_vc_A.setSelection(spinnerPosition);//sesp_season.setSelection(spinnerPosition);
            }

            if (playerA.isWicketKeeper()) {
                wkA = playerA.getPlayerName();
                wkAID = playerA.getPlayerID();
                int spinnerPosition = adapterTeamA.getPosition(wkA);//season_adapter.getPosition(season);
                spinner_wk_A.setSelection(spinnerPosition);//sesp_season.setSelection(spinnerPosition);
            }
        }

        RealmResults<Player> result_B = realm.where(Player.class).
                equalTo("matchid", matchid).
                equalTo("team", 2).
                equalTo("substitute", false).
                equalTo("retired", false)
                .sort("playerID", Sort.ASCENDING).findAll();

        for (Player playerB : result_B) {
            if (playerB.isCaptain()) {
                captainB = playerB.getPlayerName();
                captainBID = playerB.getPlayerID();
                int spinnerPosition = adapterTeamB.getPosition(captainB);//season_adapter.getPosition(season);
                spinner_captain_B.setSelection(spinnerPosition);//sesp_season.setSelection(spinnerPosition);
            }

            else if (playerB.isViceCaptain()) {
                viceCaptainB = playerB.getPlayerName();
                viceCaptainBID = playerB.getPlayerID();
                int spinnerPosition = adapterTeamB.getPosition(viceCaptainB);//season_adapter.getPosition(season);
                spinner_vc_B.setSelection(spinnerPosition);//sesp_season.setSelection(spinnerPosition);
            }

            if (playerB.isWicketKeeper()) {
                wkB = playerB.getPlayerName();
                wkBID = playerB.getPlayerID();
                int spinnerPosition = adapterTeamB.getPosition(wkB);//season_adapter.getPosition(season);
                spinner_wk_B.setSelection(spinnerPosition);//sesp_season.setSelection(spinnerPosition);
            }
        }

        ////////// ========= TILL HERE
    }




    // Updated on 29/01/2021
    void checkSpinnerValues() {

        displayProgress();

        Log.d("CAPTAIN 11", "captainA : " + captainA + ", captainAID : " + captainAID);
        Log.d("CAPTAIN 11", "viceCaptainA : " + viceCaptainA + ", viceCaptainAID : " + viceCaptainAID);
        Log.d("CAPTAIN 11", "wkA : " + wkA + ", wkAID : " + wkAID);
        Log.d("CAPTAIN 11", "captainB : " + captainB + ", captainBID : " + captainBID);
        Log.d("CAPTAIN 11", "viceCaptainB : " + viceCaptainB + ", viceCaptainBID : " + viceCaptainBID);
        Log.d("CAPTAIN 11", "wkB : " + wkB + ", wkBID : " + wkBID);

        flag_vc = 0;

        // Added on 29/01/2021
        if (captainA == null) {
            progress.dismiss();
            displayErrorMessage("Invalid captain");
        }


        else if (wkA == null) {
            progress.dismiss();
            displayErrorMessage("Invalid wicketkeeper");
        }

        else if (captainB == null) {
            progress.dismiss();
            displayErrorMessage("Invalid captain");
        }

        else if (wkB == null) {
            progress.dismiss();
            displayErrorMessage("Invalid wicketkeeper");
        }


        else {

            if (viceCaptainA != null && viceCaptainAID > 0) {
                if ((viceCaptainAID == captainAID)) {
//                    progress.dismiss();
                    flag_vcA = 1;
                    ++ flag_vc;
//                    displayErrorMessage("Invalid captain/vice captain");
                } else {
                    flag_vcA = 0;
                }
            }

            else {
                flag_vcA = 0;
            }

            if (viceCaptainB != null) { //viceCaptainBID > 0){
                if ((viceCaptainBID > 0) && (viceCaptainBID == captainBID)) {
//                    progress.dismiss();
                    flag_vcB = 1;
                    ++flag_vc;
//                    displayErrorMessage("Invalid captain/vice captain");
                }
                else {
                    flag_vcB = 0;
                }
            }

            else {
                flag_vcB = 0;
            }

            if (flag_vcA == 1 || flag_vcB == 1) {
                progress.dismiss();
                displayErrorMessage("Invalid captain/vice captain");
            }

            else {


                Log.d("CAPTAIN 2", "captainA : " + captainA + ", captainB : " + captainB);
                Log.d("CAPTAIN 2", "viceCaptainA : " + viceCaptainA + ", viceCaptainB : " + viceCaptainB);
                Log.d("CAPTAIN 2.1", "viceCaptainAID : " + viceCaptainAID + ", viceCaptainBID : " + viceCaptainBID);
                Log.d("CAPTAIN 2", "wkA : " + wkA + ", wkB : " + wkB);

                setDBValues();
            }
        }
    }





//    void savePlayerDetails(){
//
//        realm = null;
//        try {
//             config = new RealmConfiguration.Builder()
//                .name(AppConstants.GAME_ID + ".realm")
//                .deleteRealmIfMigrationNeeded()
//                .build();
//        realm = Realm.getInstance(config);
//            realm.executeTransaction(new Realm.Transaction() {
//                @Override
//                public void execute(Realm realm) {
//
//
//                    try {
//
//                        Player player = new Player();
//
////                        player.setCaptain("N");
////                        player.setWicketKeeper("N");
//
//
//                        realm.copyToRealm(player);
////                        saveToSP();
//                    }
//                    catch (RealmPrimaryKeyConstraintException e) {
//                        Toast.makeText(getApplicationContext(), "Primary Key exists, Press Update instead", Toast.LENGTH_SHORT).show();
//                    }
//                }
//            });
//        }
//        finally {
//
//            if (realm != null) {
//                realm.close();
//            }
//        }
//
//    }




    void setDBValues(){

        Log.d("CAPTAIN 1", "captainA : " + captainA + ", captainB : " + captainB);
        Log.d("CAPTAIN 1", "viceCaptainA : " + viceCaptainA + ", viceCaptainB : " + viceCaptainB);
        Log.d("CAPTAIN 1.1", "viceCaptainAID : " + viceCaptainAID + ", viceCaptainBID : " + viceCaptainBID);
        Log.d("CAPTAIN 1", "wkA : " + wkA + ", wkB : " + wkB);

        setTeamCaptain(captainAID, 1);
        setTeamCaptain(captainBID, 2);
        if (viceCaptainAID > 0)
            setTeamViceCaptain(viceCaptainAID, 1);
        if (viceCaptainBID > 0)
            setTeamViceCaptain(viceCaptainBID, 2);
        setTeamWicketKeeper(wkAID, 1);
        setTeamWicketKeeper(wkBID, 2);
        saveToSP();

       /* //Added on 03/08/2021
        startActivity(new Intent(CaptainActivity.this, TossActivity.class));
        finish();
        progress.dismiss();*/

//        if (!squad)
            postJSON(captainA, viceCaptainA, wkA, captainB, viceCaptainB, wkB);

    }


    void setTeamCaptain(int captainID, int team){

        try {

             config = new RealmConfiguration.Builder()
                .name(AppConstants.GAME_ID + ".realm")
                .deleteRealmIfMigrationNeeded()
                .build();
        realm = Realm.getInstance(config);
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    Player player = realm.where(Player.class).
                            equalTo("matchid", matchid).
                            equalTo("team", team).
                            equalTo("playerID", captainID).findFirst();
                if(player != null) {
                    player.setCaptain(true);
                    player.setViceCaptain(false);
//                    player.setWicketKeeper(false);    Commented on 20/09/2021
//                    player.setOld_wk(false);    Commented on 20/09/2021
//                    player.setNew_wk(false);    Commented on 20/09/2021
//                    player.setWicketkeeping_position(0);    Commented on 20/09/2021
                    realm.copyToRealmOrUpdate(player);
//                    player.setRole(1);
//                    Toast.makeText(getApplicationContext(), "You Selected Captain", Toast.LENGTH_SHORT).show();
                }
//                else {
//                    Toast.makeText(getApplicationContext(), "Selected player not on the Team", Toast.LENGTH_SHORT).show();
//                }

                }
            });
        }
        catch (NullPointerException e){

            Toast.makeText(getApplicationContext(), "Error : " + e, Toast.LENGTH_SHORT).show();
        }

    }


    void setTeamViceCaptain(int viceCaptainID, int team){

        try {

             config = new RealmConfiguration.Builder()
                .name(AppConstants.GAME_ID + ".realm")
                .deleteRealmIfMigrationNeeded()
                .build();
        realm = Realm.getInstance(config);
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    Player player = realm.where(Player.class).
                            equalTo("matchid", matchid).
                            equalTo("team", team).
                            equalTo("playerID", viceCaptainID).findFirst();
                    if(player != null) {
                        player.setCaptain(false);
                        player.setViceCaptain(true);
//                        player.setWicketKeeper(false);    Commented on 20/09/2021
//                        player.setOld_wk(false);    Commented on 20/09/2021
//                        player.setNew_wk(false);    Commented on 20/09/2021
//                        player.setWicketkeeping_position(0);    Commented on 20/09/2021
                        realm.copyToRealmOrUpdate(player);
//                    player.setRole(1);
//                        Toast.makeText(getApplicationContext(), "You Selected Captain", Toast.LENGTH_SHORT).show();
                    }
//                else {
//                    Toast.makeText(getApplicationContext(), "Selected player not on the Team", Toast.LENGTH_SHORT).show();
//                }

                }
            });
        }
        catch (NullPointerException e){

            Toast.makeText(getApplicationContext(), "Error : " + e, Toast.LENGTH_SHORT).show();
        }

    }



    void setTeamWicketKeeper(int wkID, int team){


         config = new RealmConfiguration.Builder()
                .name(AppConstants.GAME_ID + ".realm")
                .deleteRealmIfMigrationNeeded()
                .build();
        realm = Realm.getInstance(config);
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute (Realm realm) {
                Player player = realm.where(Player.class).
                        equalTo("matchid", matchid).
                        equalTo("team", team).
                        equalTo("playerID", wkID).findFirst();
                if(player != null) {
//                    player.setCaptain(false);    Commented on 20/09/2021
//                    player.setViceCaptain(false);    Commented on 20/09/2021
                    player.setWicketKeeper(true);
                    player.setOld_wk(false);
                    player.setNew_wk(true);
                    Number num1 = realm.where(Player.class).
                            equalTo("matchid", matchid).
                            equalTo("team", team).
                            max("wicketkeeping_position");
                    int nextId1 = (num1 == null) ? 1 : num1.intValue() + 1;
                    player.setWicketkeeping_position(nextId1);
                    realm.copyToRealmOrUpdate(player);
//                    player.setRole(2);
//                    Toast.makeText(getApplicationContext(), "You've selected WicketKeeper", Toast.LENGTH_SHORT).show();
                }
//                else {
////                    Toast.makeText(getApplicationContext(), "Please a player", Toast.LENGTH_SHORT).show();
//                    displayErrorMessage("Select a player for Wicketkeeper");
//                }

            }
        });

    }



    void saveToSP(){

        mEditor = sharedPreferences.edit();


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

        // Added on 02/08/2021
        mEditor.putInt("sp_post", 3);

        mEditor.apply();

    }




    private void postJSON(String captainA, String vcA, String wkA, String captainB, String vcB, String wkB) {

        Log.d("CAPTAIN", "captainA : " + captainA + ", captainB : " + captainB);
        Log.d("CAPTAIN", "vcA : " + vcA + ", vcB : " + vcB);
        Log.d("CAPTAIN", "wkA : " + wkA + ", wkB : " + wkB);

        int sync, p_status = 1;
        String cap = "n", vc = "n", wk = "n", umpire1_v = "", umpire2_v = "", umpire3_v = "" ,
                umpire4_v = "", match_referee_v = "";
        Match match = realm.where(Match.class).equalTo("matchid", matchid).findFirst();

        sync = match.getMatchSync();
        if (sync == 0) {
            postMatchDetails(matchid);
        } else {

            // Added on 03/08/2021
            RealmResults<MatchOfficials> result_officials = realm.where(MatchOfficials.class).
                    equalTo("matchid", matchid).
                    equalTo("sync", 0).findAll();
            if (result_officials.size() > 0) {
                postOfficialDetails();
            }

            // Added on 30/11/2021
            if ((match.getTeamAId() != 0) && (match.getTeamAId() != 0)) {
                Player player = realm.where(Player.class)
                        .equalTo("matchid", matchid)
                        .equalTo("team", 1)
                        .equalTo("pulled", 0)
                        .findFirst();
                if ((player != null ) && (player.getD4s_playerid() == 0) )
                    postPlayers(1, match.getMatchID(), match.getTeamAId());

                player = realm.where(Player.class)
                        .equalTo("matchid", matchid)
                        .equalTo("team", 2)
                        .equalTo("pulled", 0)
                        .findFirst();
                if ((player != null ) && (player.getD4s_playerid() == 0) )
                    postPlayers(2, match.getMatchID(), match.getTeamBId());
            }
            // === till here

            if (match.getTossSync() == 0)
                postToss();

            boolean post = match.isPost();
            boolean mpost = match.isPost();
            Log.e("captain", "sync : " + sync);


            if (isNetworkAvailable()) {

                // starting of adding Team A players
                JSONObject jsonPlayerA;

                // for adding players

                RealmResults<Player> results = realm.where(Player.class).
                        equalTo("matchid", matchid).
                        equalTo("team", 1).
                        equalTo("substitute", false).findAll();
                Log.e("captain", "team1 players : " + results);
                JSONArray arrayPlayerA = new JSONArray();

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
                        arrayPlayerA.put(jsonPlayerA);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                //for adding substitute players

               /* Commented on 14/12/2021
               results = realm.where(Player.class).
                        equalTo("matchid", matchid).
                        equalTo("team", 1).
                        equalTo("substitute", true).findAll();
                Log.e("captain", "team1 substitutes : " + results);
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
                }*/

                JSONObject jsonTeamA = new JSONObject();
                try {
//                if (!squad) {
                    jsonTeamA.put("players", arrayPlayerA);
//                    jsonTeamA.put("substitutes", arraySubA);  Commented on 14/12/2021
//                }
                    jsonTeamA.put("captain", captainA);
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
                Log.e("captain", "team2 players : " + results);
                JSONArray arrayPlayerB = new JSONArray();

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
                        arrayPlayerB.put(jsonPlayerB);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                //for adding substitute players

               /* Commented on 14/12/2021
                results = realm.where(Player.class).
                        equalTo("matchid", matchid).
                        equalTo("team", 2).
                        equalTo("substitute", true).findAll();
                Log.e("captain", "team2 substitutes : " + results);
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
                }*/

                JSONObject jsonTeamB = new JSONObject();
                try {
//                if (!squad) {
                    jsonTeamB.put("players", arrayPlayerB);
//                    jsonTeamB.put("substitutes", arraySubB);    Commented on 14/12/2021
//                }
                    jsonTeamB.put("captain", captainB);
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

                // adding values into feed
                JSONObject jsonFeed = new JSONObject();
                try {
                    jsonFeed.put("AddPlayers", arrayPlayers);
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

                Log.e("captain", "postparams : " + postparams);

                JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                        Constants.CHASE_CRICKET_MATCH_API, postparams,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {

                                try {

                                    Log.e("captain", "response : " + response);
                                    //if no error in response

                                    if (!response.getBoolean("error") && response.getInt("status") == 200) {

                                        JSONObject jsonMatch = response.getJSONObject("match");
//                                    int teamA_id = jsonMatch.getInt("team1_id");
//                                    int teamB_id = jsonMatch.getInt("team2_id");
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

                                                        if (match1 != null) {//match1.getMatchSync() != 1) {
//                                                        match1.setPost(true);
                                                            match1.setPlayer_post(true);
//                                                        match1.setMatchSync(1);
//                                                        match1.setTeamA_sync(1);
//                                                        match1.setTeamB_sync(1);
                                                            match1.setStatus("CVW");
                                                            match1.setStatusId(3);
                                                            match1.setPlayerSync(1);

//                                                        // Adding team ids
//                                                        match1.setTeamAId(teamA_id);
//                                                        match1.setTeamBId(teamB_id);

                                                            bgRealm.copyToRealm(match1);


                                                            // for players
                                                            // code updated on 12/11/2021
                                                            JSONArray array = jsonMatch.getJSONArray("playerinfo");
                                                            if (array.length() > 0) {
                                                                for (int i = 0; i < array.length(); i++) {
                                                                    JSONObject object = array.getJSONObject(i);
                                                                    Player player = bgRealm.where(Player.class).
                                                                            equalTo("matchID", jsonMatch.getString("app_matchID")).
                                                                            equalTo("playerID", object.getInt("playerid")).
                                                                            findFirst();

                                                                    if (player != null) {
                                                                        player.setSync(1);
                                                                        player.setD4s_playerid(object.getInt("d4splayerid"));
                                                                        bgRealm.copyFromRealm(player);
                                                                    }
                                                                }
                                                            }
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
                                Log.e("captain", "Error Message is  : " + error.getMessage());
                            }
                        });
                MyApplicationClass.getInstance(getApplicationContext()).addToRequestQueue(jsonObjReq, "postRequest");
                Log.d("captain", "jsonObjReq  : " + jsonObjReq);
                Log.d("captain", "postparams  : " + postparams);
            }
        }

        startActivity(new Intent(CaptainActivity.this,  OpenersActivity.class));//TossActivity.class));
//        startActivity(new Intent(CaptainActivity.this,  SelectTeamAXIActivity.class));
        finish();
        progress.dismiss();

    }


    private boolean isNetworkAvailable(){
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }



    @Override
    protected void onStop() {
        super.onStop();
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
//                        match.setAddPlayersA(false);
//                        match.setAddPlayersB(false);
                        match.setCaptain(true);
//                        match.setToss(false);
//                        match.setOpeners(false);
//                        match.setScoring(false);
//                        match.setScoreCard(false);
//                        match.setEndOfInnings(false);
//                        match.setPulledMatch(false);
//                        match.setSelectAXI(false);
                        match.setConfirmBXI(false);
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



    @Override
    public void onBackPressed() {
        startActivity(new Intent(this,HomeActivity.class));
        finish();
    }



    public void displayErrorMessage(String message){

        AlertDialog messageAlert = new AlertDialog.
                Builder(CaptainActivity.this).create();
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



    private void checkAlreadySelected(){

        Match match = realm.where(Match.class).
                equalTo("matchid", matchid).findFirst();
//        if (match.isCaptain()) {

//            saveToSP();
            sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
            captainA = sharedPreferences.getString("sp_captainA", null);
            captainAID = sharedPreferences.getInt("sp_captainA_id", 0);
            captainB = sharedPreferences.getString("sp_captainB", null);
            captainBID = sharedPreferences.getInt("sp_captainB_id", 0);
            viceCaptainA = sharedPreferences.getString("sp_vcA", null);
            viceCaptainAID = sharedPreferences.getInt("sp_vcA_id", 0);
            viceCaptainB = sharedPreferences.getString("sp_vcB", null);
            viceCaptainBID = sharedPreferences.getInt("sp_vcB_id", 0);
            wkA = sharedPreferences.getString("sp_wkA", null);
            wkAID = sharedPreferences.getInt("sp_wkA_id", 0);
            wkB = sharedPreferences.getString("sp_wkB", null);
            wkBID = sharedPreferences.getInt("sp_wkB_id", 0);

//            captainA = getPlayerName(captainAID, 1);
        Log.d("spinner", "captainAID : " + captainAID);
        Log.d("spinner", "captainA : " + captainA);
        Log.d("spinner", "captainBID : " + captainBID);
        Log.d("spinner", "captainB : " + captainB);
        Log.d("spinner", "viceCaptainAID : " + viceCaptainAID);
        Log.d("spinner", "viceCaptainA : " + viceCaptainA);
        Log.d("spinner", "viceCaptainBID : " + viceCaptainBID);
        Log.d("spinner", "viceCaptainB : " + viceCaptainB);
        Log.d("spinner", "wkAID : " + wkAID);
        Log.d("spinner", "wkA : " + wkA);
        Log.d("spinner", "wkBID : " + wkBID);
        Log.d("spinner", "wkB : " + wkB);
//        Log.d("spinner", "teamA_playerIDs : " + teamA_playerIDs);
//        Log.d("spinner", "teamA_playerIDs.indexOf(captainAID) : " + teamA_playerIDs.indexOf(captainAID));
//        Log.d("spinner", "captainAID : " + captainAID);
            spinner_captain_A.setSelection(teamA_playerIDs.indexOf(captainAID) + 1);
            spinner_captain_B.setSelection(teamB_playerIDs.indexOf(captainBID) + 1);
            spinner_vc_A.setSelection(teamA_playerIDs.indexOf(viceCaptainAID) + 1);
            spinner_vc_B.setSelection(teamB_playerIDs.indexOf(viceCaptainBID) + 1);
            spinner_wk_A.setSelection(teamA_playerIDs.indexOf(wkAID) + 1);
            spinner_wk_B.setSelection(teamB_playerIDs.indexOf(wkBID) + 1);

//            spinner_captain_A.setSelection(adapterTeamA.getPosition(captainA));
//            spinner_captain_B.setSelection(adapterTeamB.getPosition(captainB));
//            spinner_vc_A.setSelection(adapterTeamA.getPosition(viceCaptainA));
//            spinner_vc_B.setSelection(adapterTeamB.getPosition(viceCaptainB));
//            spinner_wk_A.setSelection(adapterTeamA.getPosition(wkA));
//            spinner_wk_B.setSelection(adapterTeamB.getPosition(wkB));
//        }
    }



    private String getPlayerName(int playerID, int team) {

         config = new RealmConfiguration.Builder()
                .name(AppConstants.GAME_ID + ".realm")
                .deleteRealmIfMigrationNeeded()
                .build();
        realm = Realm.getInstance(config);


        Player player = realm.where(Player.class).
                equalTo("matchid", matchid).
                equalTo("team", team).
                equalTo("playerID", playerID).findFirst();
        if (player != null)
            return player.getPlayerName();
        else
            return null;
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
        Intent intent = new Intent(CaptainActivity.this, MatchNoteListActivity.class);
        intent.putExtra("matchid", matchid);
        intent.putExtra("matchID", matchID);
        intent.putExtra("innings", 0);
        intent.putExtra("over", 0);
        startActivity(intent);

     /* commented on 19/10/2020

        addNoteBuilder = new AlertDialog.Builder(CaptainActivity.this);
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
                    Toast.makeText(CaptainActivity.this, "Empty match note", Toast.LENGTH_SHORT).show();
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

                                    // added on 08/10/2020
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
//                    postMatchNote();
//                    progress.dismiss();
                }
            }
        });

        addNoteBuilder.setNegativeButton("CANCEL", null);
        AlertDialog alert = addNoteBuilder.create();
        alert.show();*/
    }



    public void displayProgress(){

        progress = new ProgressDialog(this);
        progress.setMessage("Please wait...");
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setIndeterminate(true);
        progress.setProgress(0);
        progress.show();
    }



    /*// added on 23/09/2020
    public void postMatchNote() {

        if (isNetworkAvailable()) {

            Match match = realm.where(Match.class).
                    equalTo("matchID", matchID).findFirst();

            if (match != null) {

                if (match.getMatch_note() == null) {
                    Log.d("MATCH_NOTE", "Scoring, no match note found");
                }

                else {

                    JSONObject json_match = new JSONObject();
                    JSONObject json_note = new JSONObject();
                    JSONObject json_feed = new JSONObject();
                    JSONArray array = new JSONArray();

                    try {
                        json_match.put("matchID", matchID);

                        json_note.put("note", match.getMatch_note());

                        array.put(json_match);
                        array.put(json_note);

                        json_feed.put("AddMatchNote", array);

                    } catch (JSONException e) {
                        Log.d("MATCH_NOTE", "Scoring, JSONException : " + e);
                    }


                    JSONObject postparams = new JSONObject();
                    try {
                        postparams.put("title", "CHASE_POST");
                        postparams.put("feed", json_feed);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    Log.e("MATCH_NOTE", "Scoring, postparams : " + postparams);

                    JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                            Constants.CHASE_CRICKET_MATCH_API, postparams,
                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {

                                    Log.d("MATCH_NOTE", "Scoring, response : " + response);


                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {

//                                progress.dismiss();
                                    Log.e("MATCH_NOTE", "Scoring, Error Message is  : " + error.getMessage());

                                }
                            });

                    MyApplicationClass.getInstance(getApplicationContext()).addToRequestQueue(jsonObjReq, "postRequest");
                    Log.d("MATCH_NOTE", "Scoring, jsonObjReq  : " + jsonObjReq);
                    Log.d("MATCH_NOTE", "Scoring, postparams  : " + postparams);

                }

            }

            else {
                Log.d("MATCH_NOTE", "Scoring, match not found");
            }

        }

        else {
            Log.d("MATCH_NOTE", "Scoring, network not available");
        }
    }*/





/*
    void postMatchNote() {

        int sync, p_status = 1, notes_size = 0;
        String cap = "n", vc = "n", wk = "n", umpire1_v = "", umpire2_v = "", umpire3_v = "" ,
                umpire4_v = "", match_referee_v = "";
        Match match = realm.where(Match.class).equalTo("matchid", matchid).findFirst();

        sync = match.getMatchSync();
        boolean post = match.isPost();
        boolean mpost = match.isPost();
        Log.e("captain", "sync : "+sync);

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
                    jsonMatch.put("players", match.getPlayer());
                    jsonMatch.put("substitute_players", match.getSubst());
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
                    equalTo("substitute", false).findAll();
            Log.e("captain", "team1 players : "+results);
            JSONArray arrayPlayerA = new JSONArray();

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
            Log.e("captain", "team1 substitutes : "+results);
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
            Log.e("captain", "team2 players : "+results);
            JSONArray arrayPlayerB = new JSONArray();

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
            Log.e("captain", "team2 substitutes : "+results);
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

            // added on 09/10/2020

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
            // ======== till here


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
                    Log.d("cap", "match : " + match);
                else {
                    jsonFeed.put("AddMatch", jsonMatch);
//                    jsonFeed.put("AddMatchOfficials", arrayOfficials);
                }
//                jsonFeed.put("AddPlayers", arrayPlayers);             // commented on 09/10/2020
//                jsonFeed.put("matchnote", match.getMatch_note());     // commented on 09/10/2020
                if (notes_size > 0)                             // added on 09/10/2020
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

            Log.e("captain", "postparams : "+postparams);

            JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                    Constants.CHASE_CRICKET_MATCH_API, postparams,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {

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
                                                    matchnote = false;

                                                    Match match1 = bgRealm.where(Match.class).
                                                            equalTo("matchID",
                                                                    jsonMatch.getString("app_matchID")).
                                                            findFirst();

                                                    if (match1 != null) {//match1.getMatchSync() != 1) {
                                                        match1.setPost(true);
                                                        match1.setPost_matchnote(false);
                                                        match1.setMatchSync(1);
                                                        Log.d("matchSync", "captain, match synced");
                                                    }
                                                    match1.setStatus("CVW");
                                                    match1.setStatusId(3);
                                                    bgRealm.copyToRealm(match1);

                                                    Log.d("matchSync", "captain, match : " + match1);

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
                            Log.e("captain", "Error Message is  : " + error.getMessage());


                        }
                    });

            MyApplicationClass.getInstance(getApplicationContext()).addToRequestQueue(jsonObjReq, "postRequest");
            Log.d("captain", "jsonObjReq  : " + jsonObjReq);
            Log.d("captain", "postparams  : " + postparams);

        }

    }
*/



    // added on 29/01/2021
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        if (parent.getId() == R.id.cap_teamA_spinner) {
            if (position != 0) {
                captainA = parent.getItemAtPosition(position).toString();
                captainAID = teamA_playerIDs.get(position - 1);
                Log.d("CAPTAIN", "onItemSelected, captainA : " + captainA);
                Log.d("CAPTAIN", "onItemSelected, captainAID : " + captainAID);
//                displayProgress();
//                getSchoolList(district);
            }
            else {
                captainA = null;
                captainAID = 0;
            }
//            Log.d("SearchSchool", "onItemSelected, district : " + district);
        }

        else if (parent.getId() == R.id.vc_teamA_spinner) {
            if (position != 0) {
                viceCaptainA = parent.getItemAtPosition(position).toString();
                viceCaptainAID = teamA_playerIDs.get(position - 1);
                Log.d("CAPTAIN", "onItemSelected, viceCaptainA : " + viceCaptainA);
                Log.d("CAPTAIN", "onItemSelected, viceCaptainAID : " + viceCaptainAID);
//                displayProgress();
//                getSchoolList(district);
            }
            else {
                viceCaptainA = null;
                viceCaptainAID = 0;
            }
//            Log.d("SearchSchool", "onItemSelected, district : " + district);
        }

        else if (parent.getId() == R.id.wk_teamA_spinner) {
            if (position != 0) {
                wkA = parent.getItemAtPosition(position).toString();
                wkAID = teamA_playerIDs.get(position - 1);
                Log.d("CAPTAIN", "onItemSelected, wkA : " + wkA);
                Log.d("CAPTAIN", "onItemSelected, wkAID : " + wkAID);
//                displayProgress();
//                getSchoolList(district);
            }
            else {
                wkA = null;
                wkAID = 0;
            }
//            Log.d("SearchSchool", "onItemSelected, district : " + district);
        }

        else if (parent.getId() == R.id.cap_teamB_spinner) {
            if (position != 0) {
                captainB = parent.getItemAtPosition(position).toString();
                captainBID = teamB_playerIDs.get(position - 1);
                Log.d("CAPTAIN", "onItemSelected, captainB : " + captainB);
                Log.d("CAPTAIN", "onItemSelected, captainBID : " + captainBID);
//                displayProgress();
//                getSchoolList(district);
            }
            else {
                captainB = null;
                captainBID = 0;
            }
//            Log.d("SearchSchool", "onItemSelected, district : " + district);
        }

        else if (parent.getId() == R.id.vc_teamB_spinner) {
            if (position != 0) {
                viceCaptainB = parent.getItemAtPosition(position).toString();
                viceCaptainBID = teamB_playerIDs.get(position - 1);
                Log.d("CAPTAIN", "onItemSelected, viceCaptainB : " + viceCaptainB);
                Log.d("CAPTAIN", "onItemSelected, viceCaptainBID : " + viceCaptainBID);
//                displayProgress();
//                getSchoolList(district);
            }
            else {
                viceCaptainB = null;
                viceCaptainBID = 0;
            }
//            Log.d("SearchSchool", "onItemSelected, district : " + district);
        }

        else if (parent.getId() == R.id.wk_teamB_spinner) {
            if (position != 0) {
                wkB = parent.getItemAtPosition(position).toString();
                wkBID = teamB_playerIDs.get(position - 1);
                Log.d("CAPTAIN", "onItemSelected, wkB : " + wkB);
                Log.d("CAPTAIN", "onItemSelected, wkBID : " + wkBID);
//                displayProgress();
//                getSchoolList(district);
            }
            else {
                wkB = null;
                wkBID = 0;
            }
//            Log.d("SearchSchool", "onItemSelected, district : " + district);
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }



    // Added on 03/08/2021
    public void postMatchDetails(int matchid) {

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
                                    // added on 11/11/2021
                                    if (!response.getBoolean("error") && response.getInt("status") == 200) {

                                        JSONObject jsonMatch = response.getJSONObject("match");
                                        Log.d("Create", "pod, jsonMatch = " + jsonMatch);

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


    // Added on 19/11/2021
    private void postToss() {

        String capA = null, vcA = null, wkA = null, capB = null, vcB = null, wkB = null,
                tossWinner = null, decision = null;

        Match match = realm.where(Match.class).
                equalTo("matchid", matchid).findFirst();
        Log.d("ScheduledService", "matchid : " + matchid);
        Log.d("ScheduledService", "match : " + match);
        if (match != null) {
//            if (matchID == null)
            matchID = match.getMatchID();
            tossWinner = match.getToss_winner();
            decision = match.getDecision();

            JSONObject jsonToss = new JSONObject();
            try {
                jsonToss.put("matchID", matchID);
                if (match.isNoToss() || match.isUnknownToss()) {
                    jsonToss.put("winner", -1);
                    jsonToss.put("decision", decision);
                    jsonToss.put("battingteam", tossWinner);
                }
                else  {
                    jsonToss.put("winner", tossWinner);
                    jsonToss.put("decision", decision);
                    jsonToss.put("battingteam", 0);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            JSONArray arrayToss = new JSONArray();
            arrayToss.put(jsonToss);

            JSONObject jsonFeed = new JSONObject();
            try {
                jsonFeed.put("AddToss", arrayToss);
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
                    Constants.CHASE_CRICKET_MATCH_API, postparams,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {

                            Log.e("captain", "response : " + response);

                            try {
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
                                                        match1.setTossSync(1);
                                                        match1.setAddToss(1);
                                                        bgRealm.copyToRealm(match1);
                                                    }
                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                } catch (RealmPrimaryKeyConstraintException e) {
                                                    Log.d("SS", "postToss, RealmPrimaryKeyConstraintException = " + e);
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
                            Log.e("captain", "Error Message is  : " + error.getMessage());
                        }
                    });

            MyApplicationClass.getInstance(getApplicationContext()).addToRequestQueue(jsonObjReq, "postRequest");
            Log.d("toss", "jsonObjReq  : " + jsonObjReq);
            Log.d("toss", "postparams  : " + postparams);
        }
    }


    // Added on 30/11/2021
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

}
