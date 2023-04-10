package com.data4sports.chasecricket.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.ImageView;
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
import com.data4sports.chasecricket.models.Constants;
import com.data4sports.chasecricket.models.Events;
import com.data4sports.chasecricket.models.Match;
import com.data4sports.chasecricket.models.MyApplicationClass;
import com.data4sports.chasecricket.models.Player;
import com.data4sports.chasecricket.models.Substitution;
//import com.data4sports.chasecricket.adapters.SubstitutionListAdapter.editSubListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;
import io.realm.exceptions.RealmException;
import io.realm.exceptions.RealmPrimaryKeyConstraintException;

public class SubstitutionActivity extends AppCompatActivity
        implements View.OnClickListener, /*editSubListener,*/ CompoundButton.OnCheckedChangeListener {

    int matchid, team, innings, pin_id, pout_id, d4s_pin_id, d4s_pout_id, bowlerID, preBowlerID,
            newBowlerID, flagp = 0, player_id = 0, checkedItem = -1, subID = 0, eventID = 0;
    String matchID, player_out = "", player_in = "", teamA = "", teamB = "";
    String temp = "", sin = "", sout = "", playerName = "", message = "";
    boolean batting = false, fielding = false, concussion = false, flag = false, newBowler = false,
            check_substitute = false, cap = false, vc = false, wk = false, none = false;

    Realm realm;

    SharedPreferences sharedPreferences;

    HorizontalScrollView scrollView;
    Button btn_player_out, btn_player_in, btn_next_bowler, btn_apply;
    TextView tv_team, tv_player_out, tv_player_in, tv_next_bowler;
    CheckBox cb_concussion;
//    ListView subList;
    TableLayout substitution_table;
    ImageButton back;


    // variables for posting events
    int currentRuns, totalBalls, wicket, ball, preInningsRuns, remainingRuns, remainingBalls, leadingRuns,
            innings1Runs, innings2Runs, innings3Runs, innings4Runs, battingTeamNo, fieldingTeamNo, player1ID,
            player2ID, strID, strikerRuns, strikerBalls, nstrID, nonStrikerRuns, nonStrikerBalls, bwRun,
            bowlerOver, bowlerBalls, bowlerWicket, preBowlerRun, preBowlerOver, preBowlerBalls, preBowlerWicket,
            lastPreBowlerID, run, ballType, outType, dismissedPlayerID, penaltyType, penaltyRun, penaltyTeam,
            penaltyBallCounted, extraType, extraRun, mo, intervalID, sessionID, so_inninngs1Runs, so_inninngs2Runs;

    float currentOver, remainingOver, tco, tbo, tpbo;

    boolean ballCount = false, maiden = false, sww = false, endOfDay = false, SUPER_OVER = false,
            declared = false, freeHit = false, newPartnerships = false, inning_started = false;

    String fielderID = null, commentary = null, modified = null, udisplay = null, sessionType = null;

    RealmConfiguration config;

//    SubstitutionListAdapter adapter;
//    ArrayList<String> dataSub = new ArrayList<String>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_substitution);

         config = new RealmConfiguration.Builder()
                .name(AppConstants.GAME_ID + ".realm")
                .deleteRealmIfMigrationNeeded()
                .build();
        realm = Realm.getInstance(config);
        getFromSP();
        getFromIntent();

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
                // added on 26/11/2021
                if (ballType == -1) {
                    Events lastEvents = realm.where(Events.class).
                            equalTo("eventID", eventID).findFirst();
                    if (lastEvents != null) {

                        if (!realm.isInTransaction()) {
                            realm.beginTransaction();
                        }
                        lastEvents.deleteFromRealm();
                        realm.commitTransaction();
                    }
                }
                // ========== till here
            }
        });


        assignViews();

        displayList();

        Log.e("Sub", "onCreate(), team : "+team);
        Log.e("Sub", "onCreate(), batting : "+batting);
        Log.e("Sub", "onCreate(), fielding : "+fielding);
        Log.e("Sub", "onCreate(), innings : "+innings);

        btn_player_out.setOnClickListener(this);
        btn_player_in.setOnClickListener(this);
        btn_next_bowler.setOnClickListener(this);
        btn_apply.setOnClickListener(this);
        cb_concussion.setOnCheckedChangeListener(this);


//        postJSON();

//        temp = " Player Left : Player In : Team : Innings : Concussion ";
//
//        dataSub.add(temp);
//
//        RealmResults<Substitution> s_results = realm.where(Substitution.class).
//                equalTo("matchid", matchid).findAll();
//
//        for (Substitution substitution : s_results){
//
//            sout = realm.where(Player.class).
//                    equalTo("matchid", matchid).
//                    equalTo("playerID", substitution.getPlayer_OUT_ID()).findFirst().getPlayerName();
//
//            sin = realm.where(Player.class).
//                    equalTo("matchid", matchid).
//                    equalTo("playerID", substitution.getPlayer_IN_ID()).findFirst().getPlayerName();
//
//            temp = " " + sout + " , " + sin + " , " + substitution.getTeam() + " , " +
//                    substitution.getInnings() + " , " + substitution.isConcussion();
//
//            dataSub.add(temp);
//        }
//
//        adapter = new SubstitutionListAdapter(SubstitutionActivity.this, dataSub);
//
//        adapter.
//        listView.setAdapter(adapter);

    }



    void getFromSP(){

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        matchid = sharedPreferences.getInt("sp_match_id", 0);
        matchID = sharedPreferences.getString("sp_match_ID", null);
    }




    void getFromIntent(){

        Intent i = getIntent();
        team = i.getIntExtra("team", 0);
        batting = i.getBooleanExtra("batting", false);
        fielding = i.getBooleanExtra("fielding", false);
        innings = i.getIntExtra("innings", 0);
        bowlerID = i.getIntExtra("bowler_id", 0);
        preBowlerID = i.getIntExtra("pre_bowler_id", 0);
//        eventID = i.getIntExtra("event_id", 0);
        eventID = i.getIntExtra("eventId", 0);
        strID = i.getIntExtra("strID", 0);
        nstrID = i.getIntExtra("nstrID", 0);

        Log.d("sub", "getFromIntent, team : " + team);
        Log.d("sub", "getFromIntent, batting : " + batting);
        Log.d("sub", "getFromIntent, fielding : " + fielding);
        Log.d("sub", "getFromIntent, innings : " + innings);
        Log.d("sub", "getFromIntent, bowlerID : " + bowlerID);
        Log.d("sub", "getFromIntent, preBowlerID : " + preBowlerID);
        Log.d("sub", "getFromIntent, eventID : " + eventID);
        Log.d("sub", "getFromIntent, strID : " + strID);
        Log.d("sub", "getFromIntent, nstrID : " + nstrID);


/*//        for saving event
        currentRuns = i.getIntExtra("currentRuns", 0);
        currentOver = i.getFloatExtra("currentOver", 0f);
        totalBalls = i.getIntExtra("totalBalls", 0);
        wicket = i.getIntExtra("wicket", 0);
        ball = i.getIntExtra("ball", 0);
        preInningsRuns = i.getIntExtra("preInningsRuns", 0);
        remainingRuns = i.getIntExtra("remainingRuns", 0);
        remainingBalls = i.getIntExtra("remainingBalls", 0);
        remainingOver = i.getFloatExtra("remainingOver", 0f);
        leadingRuns = i.getIntExtra("leadingRuns", 0);
        innings1Runs = i.getIntExtra("innings1Runs", 0);
        innings2Runs = i.getIntExtra("innings2Runs", 0);
        innings3Runs = i.getIntExtra("innings3Runs", 0);
        innings4Runs = i.getIntExtra("innings4Runs", 0);
        battingTeamNo = i.getIntExtra("battingTeamNo", 0);
        fieldingTeamNo = i.getIntExtra("fieldingTeamNo", 0);
        player1ID = i.getIntExtra("player1ID", 0);
        player2ID = i.getIntExtra("player2ID", 0);
        strID = i.getIntExtra("strID", 0);
        strikerRuns = i.getIntExtra("strikerRuns", 0);
        strikerBalls = i.getIntExtra("strikerBalls", 0);
        nstrID = i.getIntExtra("nstrID", 0);
        nonStrikerRuns = i.getIntExtra("nonStrikerRuns", 0);
        nonStrikerBalls = i.getIntExtra("nonStrikerBalls", 0);
        bwRun = i.getIntExtra("bwRun", 0);
        bowlerOver = i.getIntExtra("bowlerOver", 0);
        bowlerBalls = i.getIntExtra("bowlerBalls", 0);
        bowlerWicket = i.getIntExtra("bowlerWicket", 0);
        preBowlerRun = i.getIntExtra("preBowlerRun", 0);
        preBowlerOver = i.getIntExtra("preBowlerOver", 0);
        preBowlerBalls = i.getIntExtra("preBowlerBalls", 0);
        preBowlerWicket = i.getIntExtra("preBowlerWicket", 0);
        lastPreBowlerID = i.getIntExtra("lastPreBowlerID", 0);
        mo = i.getIntExtra("mo", 0);
        maiden = i.getBooleanExtra("maiden", false);
        modified = i.getStringExtra("modified");
        udisplay = i.getStringExtra("udisplay");
        tco = i.getFloatExtra("tco", 0f);
        tbo = i.getFloatExtra("tbo", 0f);
        tpbo = i.getFloatExtra("tpbo", 0f);
        sessionType = i.getStringExtra("sessionType");
        sessionID = i.getIntExtra("sessionID", 0);
        intervalID = i.getIntExtra("intervalID", 0);
        endOfDay = i.getBooleanExtra("endOfDay", false);
        SUPER_OVER = i.getBooleanExtra("SUPER_OVER", false);
        so_inninngs1Runs = i.getIntExtra("so_inninngs1Runs", 0);
        so_inninngs2Runs = i.getIntExtra("so_inninngs2Runs", 0);
        freeHit = i.getBooleanExtra("freeHit", false);
        inning_started = i.getBooleanExtra("inning_started", false);*/
              /*  0,
                ballType,
                -1,
                0,
                -1,
                0,
                null,
                0,
                0,
                0,
                0,
                false,
                0,
                "",
                0,*/


    }




    void assignViews(){

        tv_team = findViewById(R.id.sub_team);
        scrollView = findViewById(R.id.sub_hsv);
        btn_player_out = findViewById(R.id.btn_spo);
        btn_player_in = findViewById(R.id.btn_spi);
        tv_player_out = findViewById(R.id.tv_spo);
        tv_player_in = findViewById(R.id.tv_spi);
        tv_next_bowler = findViewById(R.id.tv_sub_next_bowler);
        btn_next_bowler = findViewById(R.id.btn_sub_next_bowler);
        cb_concussion = findViewById(R.id.check_concussion);
        btn_apply = findViewById(R.id.btn_sub_apply);
        substitution_table = findViewById(R.id.substitution_table);
//        subList = findViewById(R.id.substitution_list_view);

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.btn_spo:
                displayCurrentPlayers();
                break;

            case R.id.btn_spi:
                Log.d("sub", "btn_spi pressed");
                displaySubstitutePlayers();
                break;

            case R.id.btn_sub_next_bowler:
                selectNextBowler();
                break;

//            case R.id.check_concussion:
//
//                break;

            case R.id.btn_sub_apply:
                saveSubstitute();
                break;
        }
    }



    void displayCurrentPlayers(){ // method for selecting the player who are injured

//        ArrayList<Integer> currentPlayerList = new ArrayList<Integer>();

        final ArrayAdapter<String> currentPlayerAdapter = new ArrayAdapter<String>(SubstitutionActivity.this,
                android.R.layout.select_dialog_singlechoice);

        RealmResults<Player> p_results = realm.where(Player.class).
                equalTo("matchid", matchid).
                equalTo("team", team).
                equalTo("substitute", false).
                equalTo("retired", false).findAll();

        for (Player player : p_results){

            currentPlayerAdapter.add(player.getPlayerName());
        }


        AlertDialog.Builder spoAlert = new AlertDialog.Builder(SubstitutionActivity.this);
        spoAlert.setIcon(R.drawable.ball);
        spoAlert.setCancelable(false);
        spoAlert.setTitle("Select Player");
        spoAlert.setAdapter(currentPlayerAdapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String strName = currentPlayerAdapter.getItem(which);
                displayAlert(strName, true, false);

                dialog.dismiss();
            }
        });

        spoAlert.show();
    }




    public void displaySubstitutePlayers(){

        Log.d("sub", "displaySubstitutePlayers() called");





        final ArrayAdapter<String> substituteAdapter = new ArrayAdapter<String>(SubstitutionActivity.this,
                android.R.layout.select_dialog_singlechoice);

        RealmResults<Player> p_results = realm.where(Player.class).
                equalTo("matchid", matchid).
                equalTo("team", team).
                equalTo("substitute", true).
                equalTo("retired", false).findAll();

        Log.d("sub", "displaySubstitutePlayers, p_results : " + p_results.toString());

        if (p_results.isEmpty()) {

            Log.d("sub", "p_results.isEmpty");

            AlertDialog.Builder spiAlert = new AlertDialog.Builder(SubstitutionActivity.this);
            spiAlert.setIcon(R.drawable.ball);
            spiAlert.setCancelable(false);
            spiAlert.setTitle("No Substitute Players");
            spiAlert.setPositiveButton("+ Add Player", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

//                    addPlayer();
                    addNewPlayer();

                    Log.d("new_bowler", "displaySubstitutePlayers(), Add Player, flag : " + flag);
                }
            });
            spiAlert.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });

            spiAlert.show();

        }

        else {

            Log.d("sub", "p_results is not Empty");

            for (Player player : p_results) {

                substituteAdapter.add(player.getPlayerName());
            }


            AlertDialog.Builder spiAlert = new AlertDialog.Builder(SubstitutionActivity.this);
            spiAlert.setIcon(R.drawable.ball);
            spiAlert.setCancelable(false);
            spiAlert.setTitle("Select Substitute Player");
            spiAlert.setAdapter(substituteAdapter, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    String strName = substituteAdapter.getItem(which);
                    displayAlert(strName, false, true);

                    dialog.dismiss();
                }
            });
            // Added on 16/06/2021
            spiAlert.setPositiveButton("+ Add Player", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

//                    addPlayer();
                    addNewPlayer();

                    Log.d("new_bowler", "displaySubstitutePlayers(), Add Player, flag : " + flag);
                }
            });
            // ==== till here
            spiAlert.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });

            spiAlert.show();
        }


        Log.d("new_bowler", "displaySubstitutePlayers(), end, flag : " + flag);
    }



    void callBowlerAlert(){

        AlertDialog.Builder bowlerAlert = new AlertDialog.Builder(SubstitutionActivity.this);
        bowlerAlert.setIcon(R.drawable.ball);
        bowlerAlert.setCancelable(false);
        bowlerAlert.setTitle("Bolwer Alert");
        bowlerAlert.setMessage("\tSelect another bowler to continue the over.");
        bowlerAlert.setPositiveButton("Select Bowler", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {


            }
        });
    }



    void displayAlert(String name, boolean out, boolean in){


        AlertDialog.Builder builderInner = new AlertDialog.Builder(SubstitutionActivity.this);
        builderInner.setIcon(R.drawable.ball);
        builderInner.setCancelable(false);
//        dialog.dismiss();
        builderInner.setMessage(name);
        builderInner.setTitle("Selected player is ");

        builderInner.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog,int which) {

                if (out) {
                    tv_player_out.setText(name);
                    player_out = name;

                    if (fielding) {

                        int id = realm.where(Player.class).
                                equalTo("matchid", matchid).
                                equalTo("team", team).
                                equalTo("playerName", name).findFirst().getPlayerID();

                        if (id == bowlerID && bowlerID != 0) {

                            flag = true;

                            btn_next_bowler.setVisibility(View.VISIBLE);
                            tv_next_bowler.setVisibility(View.VISIBLE);

                        }

//                        else if (id != 0)
//                            bowlerID = id;
                    }
                }

                else if (in) {
                    tv_player_in.setText(name);
                    player_in = name;
                }


                Log.e("Sub", "displayAlert, flag : "+flag);


                dialog.dismiss();

            }
        });
//
        builderInner.show();
    }




    void saveSubstitute() {

//        AddPlayers addPlayers = new AddPlayers();
        AddPlayersA addPlayersA = new AddPlayersA();
        AddPlayersB addPlayersB = new AddPlayersB();

        Log.e("Sub", "saveSubstitute, method");
        Log.e("Sub", "saveSubstitute, player_out : "+player_out);
        Log.e("Sub", "saveSubstitute, player_in : "+player_in);
        Log.e("Sub", "saveSubstitute, batting : "+batting);
        Log.e("Sub", "saveSubstitute, fielding : "+fielding);
        Log.e("Sub", "saveSubstitute, concussion : "+concussion);

        if ((!player_out.matches("")) && (!player_in.matches(""))) {

            Log.e("Sub", "saveSubstitute, player_in & player_out are not null");
            concussion = cb_concussion.isChecked();
            int inID = 0, outID = 0;

            if (batting) {

                if (concussion) {

                    //  save to player table
                    saveToDB(concussion);

                } else
                    displayErrorMessage("No substitution for Batting team");
//                    Toast.makeText(getApplicationContext(),
//                            "Only Concussion can applied to Batting Team", Toast.LENGTH_SHORT).show();
            } else {


                // add new player to Player table
                RealmResults<Player> p_results = realm.where(Player.class).
                        equalTo("matchid", matchid).
                        equalTo("team", team).findAll();
                for (Player p : p_results){

                    Log.e("Sub", "saveSubstitute, Player : "+p+"\n");
                }

                Log.e("Sub", "saveSubstitute, playerName : "+playerName);
                Log.e("Sub", "saveSubstitute, bowlerID : "+bowlerID);

                if (newBowler) {
                    //commented on 29/04/2020
//                    addPlayers.savePlayerDetails(matchid, matchID, playerName, newBowlerID/*bowlerID*/, team, 0);
                   /* COMMENTED ON 01/03/2021
                   if (team == 1)
                        addPlayersA.savePlayerDetails(matchid, matchID, playerName, newBowlerID*//*bowlerID*//*, team, 0);
                    else if (team == 2)
                        addPlayersB.savePlayerDetails(matchid, matchID, playerName, newBowlerID*//*bowlerID*//*, team, 0);*/
                        savePlayerDetails(matchid, matchID, playerName, newBowlerID/*bowlerID*/, team, 0);
                }

                saveToDB(concussion);

                if (concussion) {

                    Realm realms = null;
                    try {
                        realms = Realm.getDefaultInstance();
                        realms.executeTransaction(new Realm.Transaction() {
                            @Override
                            public void execute(Realm bgrealm) {

                                try {

                                    Player player = bgrealm.where(Player.class).
                                            equalTo("matchid", matchid).
                                            equalTo("team", team).
                                            equalTo("playerName", player_in).findFirst();

                                    if (player != null) {

                                        player.setRetired(false);
                                        player.setRetired_concussion(false);
                                        bgrealm.copyToRealmOrUpdate(player);
                                    }

                                } catch (RealmPrimaryKeyConstraintException e) {
                                    Toast.makeText(getApplicationContext(),
                                            "Primary Key exists, Press Update instead", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }

                    catch (RealmException e) {
                        Log.d("scoring", "onclick, Exception : " + e);
                    }

                    finally {
                        if (realms != null) {
                            realms.close();
                        }
                    }
                }


//                if (concussion) {
//
//                    // save to player table
//                    saveToDB(concussion);
//
//                } else {
//
//                    //change the temp array for fielding team
//                    saveToDB(concussion);
//                }
            }
        }

    }




    void saveToDB(boolean concussion){

        Log.e("Sub", "saveToDB, team : "+team+", player_out : "+player_out+", player_in : "+player_in);
        Log.e("Sub", "saveToDB, fielding : "+fielding+", batting : "+batting+", concussion : "+concussion);

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

                        // for player out
                        Player player = bgRealm.where(Player.class).
                                equalTo("matchid", matchid).
                                equalTo("team", team).
                                equalTo("playerName", player_out).
//                                  equalTo("retired", false).
                                findFirst();

                        pout_id = player.getPlayerID();
                        d4s_pout_id = player.getD4s_playerid();


//                        if (batting){
                            if (concussion) {

                                player.setRetired(true);
                                player.setRetired_concussion(true);
                                player.setRetired_innings(innings);
                                bgRealm.copyToRealmOrUpdate(player);
                            }

//                        }
//
//                        else {
//
//
//                        }


                        // for player in
                        player = bgRealm.where(Player.class).
                                equalTo("matchid", matchid).
                                equalTo("team", team).
                                equalTo("playerName", player_in).
//                                  equalTo("retired", false).
                                findFirst();

                        pin_id = player.getPlayerID();
                        d4s_pin_id = player.getD4s_playerid();

                        if (concussion) {

                            player.setSubstitute(false);
                            bgRealm.copyToRealmOrUpdate(player);

                            if (batting) {

                                Number num = bgRealm.where(Batsman.class).max("batsmanID");
                                int nextId = (num == null) ? 1 : num.intValue() + 1;

                                Number num1 = bgRealm.where(Batsman.class).
                                        equalTo("matchid", matchid).
                                        equalTo("innings", innings).
                                        equalTo("team", team).
                                        notEqualTo("battingOrder", 100).
                                        max("battingOrder");

                                int order = (num1 == null) ? 1 : num1.intValue() + 1;
                                Log.d("order", "substitution : matchid : " + matchid);
                                Log.d("order", "substitution : innings : " + innings);
                                Log.d("order", "substitution : battingTeamNo : " + battingTeamNo);
                                Log.d("order", "substitution : num1 : " + num1);
                                Log.d("order", "substitution : " + order);

                                Batsman batsman = bgRealm.createObject(Batsman.class, nextId);
                                batsman.setBatsman_pID(player.getPlayerID());
                                batsman.setMatchid(matchid);
                                batsman.setMatchID(matchID);
//                        batsmann.setBatsmanName(player.getPlayerName());
                                batsman.setTeam(team);
                                if (pout_id == strID || pout_id == nstrID)
                                    batsman.setBattingOrder(order);
                                else
                                    batsman.setBattingOrder(100);
                                batsman.setInnings(innings);
                                batsman.setBatsman_pID(pin_id);

                                bgRealm.insertOrUpdate(batsman);


                                batsman = bgRealm.where(Batsman.class).
                                        equalTo("matchid", matchid).
                                        equalTo("team", team).
                                        equalTo("innings", innings).
                                        equalTo("batsman_pID", pout_id).findFirst();

                                batsman.setPlaying(false);
//                                batsman.setRetired(true);
                                batsman.setOut(true);
                                batsman.setOutType(12);


                                bgRealm.insertOrUpdate(batsman);


                                // added on 29/04/2020
                                Player player1= bgRealm.where(Player.class).
                                        equalTo("matchid", matchid).
                                        equalTo("team", team).
                                        equalTo("playerID", pout_id).findFirst();

                                if (player1 != null) {

                                    player1.setRetired(true);
                                    bgRealm.insertOrUpdate(player1);
                                }

                                Log.d("substitution", "concussion : " + concussion + ", batsman : " + batsman);
                            }


                        }






                        // save to substitution table
                        Number num = bgRealm.where(Substitution.class).max("subID");
                        int nextId = (num == null) ? 1 : num.intValue() + 1;
                        subID = nextId;
                        Substitution substitution = bgRealm.createObject(Substitution.class, nextId);
                        substitution.setMatchid(matchid);
                        substitution.setMatchID(matchID);
                        substitution.setEventID(eventID);
                        substitution.setTeam(team);
                        substitution.setInnings(innings);
                        substitution.setPlayer_OUT_ID(pout_id);
                        substitution.setPlayer_IN_ID(pin_id);
                        substitution.setD4s_player_OUT_ID(d4s_pout_id);
                        substitution.setD4s_player_IN_ID(d4s_pin_id);
                        substitution.setNew_bowler_id(newBowlerID);
                        substitution.setConcussion(concussion);
                        Log.e("substitution", "saveToDB, innings : "+innings+", pout_id : "+pout_id+", pin_id : "+pin_id);
                        bgRealm.copyToRealmOrUpdate(substitution);

                        Log.d("substitution", "concussion : " + concussion + ", substitution : " + substitution);


                    } catch (RealmPrimaryKeyConstraintException e) {
                        Toast.makeText(getApplicationContext(), "Primary Key exists, Press Update instead",
                                Toast.LENGTH_SHORT).show();
                    }
                }
            });

            saveEvent();
        }

        catch (RealmException e) {
            Log.d("substitution", "onclick, Exception : " + e);
        }

        finally {
            if (realm != null) {
                realm.close();
            }
        }



    }



    void saveEvent() {

        ScoringActivity scoring = new ScoringActivity();
        if (concussion)
            ballType = 19;
        else
            ballType = 18;

        // added on 29/04/2020
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

                        Events events = realm.where(Events.class).equalTo("eventID", eventID).findFirst();
                        Log.d("sub", "saveEvent, eventID : "+ eventID);
                        Log.d("sub", "saveEvent, subID : "+ subID);
                        if (events != null) {
                            events.setBallType(ballType);
                            events.setSubstitutionID(subID);

                            if (pout_id == strID || pout_id == nstrID) {
                                events.setDisNewBatsmanID(pin_id);
                                events.setDismissedPlayerID(pout_id);
                                final String str = events.getCurrentOver();
                            }

                            // added on 14/05/2020
                            events.setSub_team(team);
                            events.setSub_playerout_id(pout_id);
                            events.setSub_playerin_id(pin_id);  // till here

                            /*else if (pout_id == bowlerID) {
                                events.setNewBowlerID(newBowlerID);
                            }*/
                            realm.copyToRealmOrUpdate(events);
                            Log.d("sub", "saveEvent, events : " + events);
                        }

                    } catch (RealmPrimaryKeyConstraintException e) {
                        Toast.makeText(getApplicationContext(),
                                "Primary Key exists, Press Update instead", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

        catch (RealmException e) {
            Log.d("sub", "onclick, Exception : " + e);
        }

        finally {
            if (realm != null) {
                realm.close();
            }
        }

//        Intent intent = getIntent();
//        scoring.newEvent(matchid, matchID, innings, currentRuns, currentOver, totalBalls, wicket, ball, ball, preInningsRuns,
//                remainingRuns, remainingBalls, remainingOver, leadingRuns, innings1Runs, innings2Runs,
//                innings3Runs, innings4Runs,  battingTeamNo, fieldingTeamNo, player1ID, player2ID,
//                strID, strikerRuns, strikerBalls, nstrID, nonStrikerRuns, nonStrikerBalls, bowlerID,
//                bwRun, bowlerOver, bowlerBalls, bowlerWicket, /*nextBowlerID,*/preBowlerID, preBowlerRun,
//                preBowlerOver, preBowlerBalls, preBowlerWicket, lastPreBowlerID, run, ballType,
//                -1, 0, -1, 0, null,0,
//                -1, 0, 0, false, 0,
//                commentary, 0, mo, maiden, modified, udisplay, tco, tbo, tpbo, intervalID,
//                sessionType, sessionID, true, /*jsonEventArray, jsonUndoArray,*/ endOfDay,
//                SUPER_OVER, so_inninngs1Runs, so_inninngs2Runs, false, freeHit,
//                false, inning_started, subID);




//        if (concussion)
//            postJSON();


        Log.d("new_bowler", "saveEvent, flag : " + flag);



//        Toast.makeText(SubstitutionActivity.this, "Resume the match",
//                Toast.LENGTH_SHORT).show();
//        Intent i = new Intent(SubstitutionActivity.this, ScoringActivity.class);      commented on 28/07/2021
        Intent i = new Intent(SubstitutionActivity.this, UpdatedScoringActivity.class);
        i.putExtra("matchid", matchid);
        i.putExtra("matchID", matchID);
        i.putExtra("status", "resume");
        i.putExtra("sout_id", pout_id);
        i.putExtra("sin_id", pin_id);
        i.putExtra("batting", batting);
        i.putExtra("fielding", fielding);
        i.putExtra("concussion", concussion);
        i.putExtra("new_bowler", flag);
        i.putExtra("bowler", playerName);
        i.putExtra("bowler_id", newBowlerID);
        i.putExtra("substitution", true);
        i.putExtra("subID", subID);
        i.putExtra("ballType", ballType);

        freeMemory();
        startActivity(i);
    }




    void selectNextBowler(){

        Log.e("Sub", "selectNextBowler, metod called");

        AlertDialog.Builder bowlerAlert = new AlertDialog.Builder(this);
        bowlerAlert.setIcon(R.drawable.ball);
        bowlerAlert.setCancelable(false);
        bowlerAlert.setTitle("Bowler Alert");
        bowlerAlert.setMessage("Select next bowler ");
        bowlerAlert.setPositiveButton("Select Bowler", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();
                selectBowler();
            }
        });
        bowlerAlert.setNeutralButton("Add new player", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                addPlayer();
            }
        });
        bowlerAlert.show();
    }





    void selectBowler(){

//        String bowler = "";

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,
                android.R.layout.select_dialog_singlechoice);

        RealmResults<Player> results = realm.where(Player.class).
                equalTo("matchid", matchid).
                equalTo("team", team).
                equalTo("substitute", false).
                equalTo("retired", false).findAll();




        results.load();
        for (Player player : results) {

            if (player.getPlayerID() != bowlerID){
//            if (!(player.getPlayerName()).matches(bowler)) {
                arrayAdapter.add(player.getPlayerName());
            }

        }
        AlertDialog.Builder bowlerBuilder = new AlertDialog.Builder(this);
        bowlerBuilder.setIcon(R.drawable.ball);
        bowlerBuilder.setCancelable(false);
        bowlerBuilder.setTitle("Select Next Bowler");
        bowlerBuilder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        bowlerBuilder.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String strName = arrayAdapter.getItem(which);
//                dialog.dismiss();
                AlertDialog.Builder builderInner = new AlertDialog.Builder(SubstitutionActivity.this);
                builderInner.setIcon(R.drawable.ball);
                builderInner.setCancelable(false);
                dialog.dismiss();
                builderInner.setMessage(strName);
                builderInner.setTitle("Selected Bowler is");

                builderInner.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog,int which) {
//                        bowler = strName;
                        dialog.dismiss();
                        playerName = strName;
                        tv_next_bowler.setText(playerName);



                        newBowlerID = realm.where(Player.class).
                                equalTo("matchid", matchid).
                                equalTo("team", team).
                                equalTo("playerName", strName).findFirst().getPlayerID();

                        if (newBowlerID == preBowlerID) {
                            displayErrorMessage("Selected bowler is bowled previous over");
                            tv_next_bowler.setText("");
                        }
                        else
                            newBowler = true;


//                        newBowler(bowlerID, preBowlerID);
//                        syncBowler();
//                        ballType = 6;
//                        Log.e("Scoring", "changeBowler, bowlerBuilder, builderInner, ballType : "+ballType);
//                        newEvent(currentInnings, currentRuns, currentOver, totalBalls, wicket, ball, preInningsRuns,
//                                remainingRuns, remainingBalls, remainingOver, leadingRuns, battingTeamNo, fieldingTeamNo,
//                                player1ID, player2ID, strID, strikerRuns, strikerBalls, nstrID, nonStrikerRuns,
//                                nonStrikerBalls, bowlerID, bwRun, bowlerOver, bowlerBalls, bowlerWicket, nextBowlerID,
//                                preBowlerID, preBowlerRun, preBowlerOver, preBowlerBalls, preBowlerWicket,0, ballType,
//                                0, 0, 0, 0, 0, 0,
//                                0, 0, 0, false, 0, commentary, bowlerID, mo,
//                                maiden, modified, udisplay, tco, tbo, tpbo);
//                        commentary = "";
                    }
                });
//
                builderInner.show();
//                dialog.dismiss();
            }
        });

        bowlerBuilder.show();
    }



    void addPlayer(){

//        AddPlayers addPlayers = new AddPlayers();

        AlertDialog.Builder editBuilder = new AlertDialog.Builder(SubstitutionActivity.this);
        editBuilder.setIcon(R.drawable.ball);
        editBuilder.setCancelable(false);
        editBuilder.setTitle("Enter new player");

        final EditText input = new EditText(SubstitutionActivity.this);

        input.setInputType(InputType.TYPE_CLASS_TEXT);
        input.setPadding(20, 15, 20, 15);
        input.setText("");
        editBuilder.setView(input);
//        Log.d("Test", "inside setCommentry()");
        editBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

            //            @Override
            public void onClick(DialogInterface dialog, int which) {

                if (input.getText().toString().equals("")){
                    Toast.makeText(getApplicationContext(),
                            "Please enter a valid name", Toast.LENGTH_SHORT).show();
                }

                else {
                    playerName = input.getText().toString();
                    Log.d("Test", "player name : " + playerName);


                    if (!playerName.matches("")){

                        Number num = realm.where(Player.class).max("playerID");
                        newBowlerID = (num == null) ? 1 : num.intValue() + 1;

                        newBowler = true;


                        tv_next_bowler.setText(playerName);

//                        addPlayers.saveToDB(matchid, playerID, oldName, playerName);
//
//                        setList(t);
                    }
                    else {
                        Toast.makeText(getApplicationContext(),
                                "Please enter a valid name", Toast.LENGTH_SHORT).show();
                        Log.d("Test","buildertext = null");
                    }

                }
            }
        });

        editBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                selectNextBowler();
            }
        });

        AlertDialog alert = editBuilder.create();
        alert.show();
    }



    void displayList(){




        temp = " Player Left , Player In , Team , Innings , Concussion ";

        Match match = realm.where(Match.class).equalTo("matchid", matchid).findFirst();
        teamA = match.getTeamA();
        teamB = match.getTeamB();

        if (team == 1)
            tv_team.setText(teamA);

        else if (team == 2)
            tv_team.setText(teamB);

        RealmResults<Substitution> s_results = realm.where(Substitution.class).
                equalTo("matchid", matchid).findAll();

        Log.e("Sub", "displayList, SubstituteList : "+s_results);

        if (s_results.isEmpty()){

            substitution_table.setVisibility(View.GONE);
//            subList.setVisibility(View.GONE);
        }

        else {

            // for substitution table display

            substitution_table.setVisibility(View.VISIBLE);
            TableRow rowsh = new TableRow(this);
            rowsh.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,50));
            rowsh.setPadding(10, 25,10, 25);

            TextView tvsh1 = new TextView(this);
            tvsh1.setTextColor(Color.BLACK);
            tvsh1.setText(" \tPlayer Left");
            rowsh.addView(tvsh1);

            TextView tvsh2 = new TextView(this);
            tvsh2.setTextColor(Color.BLACK);
            tvsh2.setText(" \t\tPlayer In");
            rowsh.addView(tvsh2);

            TextView tvsh3 = new TextView(this);
            tvsh3.setTextColor(Color.BLACK);
            tvsh3.setText(" \t\tTeam");
            rowsh.addView(tvsh3);

            TextView tvsh4 = new TextView(this);
            tvsh4.setTextColor(Color.BLACK);
            tvsh4.setText(" \t\tInnings");
            rowsh.addView(tvsh4);

            TextView tvsh5 = new TextView(this);
            tvsh5.setTextColor(Color.BLACK);
            tvsh5.setText(" \t\tConcussion"+"\t\t\t\t\t");
            rowsh.addView(tvsh5);

            ImageView im = new ImageView(this);
            rowsh.addView(im);
            rowsh.setBackgroundResource(R.drawable.separator);

            substitution_table.addView(rowsh);
//            substitution_table.setDividerPadding(5);
//            substitution_table.setShowDividers(LinearLayout.SHOW_DIVIDER_BEGINNING);



            for (Substitution substitution : s_results) {


                sout = realm.where(Player.class).
                        equalTo("matchid", matchid).
                        equalTo("playerID", substitution.getPlayer_OUT_ID()).findFirst().getPlayerName();

                sin = realm.where(Player.class).
                        equalTo("matchid", matchid).
                        equalTo("playerID", substitution.getPlayer_IN_ID()).findFirst().getPlayerName();

                if (substitution.getTeam() == 1)
                    temp = teamA;

                else
                    temp = teamB;


                TableRow rows = new TableRow(this);
                rows.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, 50));
                rows.setPadding(10,25,10,25);

                TextView tvs1 = new TextView(this);
                tvs1.setTextColor(Color.BLACK);
                tvs1.setText("\t"+sout);
                rows.addView(tvs1);

                TextView tvs2 = new TextView(this);
                tvs2.setTextColor(Color.BLACK);
                tvs2.setText("\t\t"+sin);
                rows.addView(tvs2);

                TextView tvs3 = new TextView(this);
                tvs3.setTextColor(Color.BLACK);
                tvs3.setText("\t\t"+temp);
                Log.e("Sub", "displayList, team : "+temp);
                rows.addView(tvs3);

                TextView tvs4 = new TextView(this);
                tvs4.setTextColor(Color.BLACK);
                tvs4.setText("\t\t"+String.valueOf(substitution.getInnings()));
                rows.addView(tvs4);

                TextView tvs5 = new TextView(this);
                tvs5.setTextColor(Color.BLACK);
                tvs5.setText("\t\t"+String.valueOf(substitution.isConcussion()));
                rows.addView(tvs5);

                ImageView edit = new ImageView(this);
                edit.setImageResource(R.drawable.edit);
                edit.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.colorText));
//                edit.setColorFilter(R.color.colorText);
//                DrawableCompat.setTint(edit.getDrawable(), ContextCompat.getColor(getApplicationContext(), R.color.colorText));
//                DrawableCompat.setTint(myImageView.getDrawable(), ContextCompat.getColor(context, R.color.another_nice_color))
//                edit.setForegroundGravity(View.FOCUS_RIGHT);
                edit.setOnClickListener(editListener);
                rows.addView(edit);

                substitution_table.addView(rows);
                substitution_table.setDividerPadding(5);

            }
        }
    }






    private View.OnClickListener editListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {

            // now you have a reference to the row where this Button that was
            // clicked exists
            // do whatever row updates you want.

            Toast.makeText(getApplicationContext(), "Working progress\nSelected row is, \n\t OUT : " + sout +
                    "\n\t IN : " + sin + "\n\t TEAM : " + temp, Toast.LENGTH_SHORT).show();
        }
    };


    /*@Override
    public void onEditSubClickListener(int position, String name) {

        Toast.makeText(getApplicationContext(),
                "Working progress", Toast.LENGTH_SHORT).show();
    }*/

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

//        if (isChecked){

            Log.e("Sub", "onCheckedChanged, isChecked : "+isChecked);

//            if (buttonView.getId() == R.id.checkbox_limited_overs) {

                if (batting) {

                    if (!isChecked){



                    Log.e("Sub", "onCheckedChanged, fielding : "+fielding);

                    AlertDialog concussionAlert = new AlertDialog.Builder(SubstitutionActivity.this).create();
                    concussionAlert.setIcon(R.drawable.ball);
                    concussionAlert.setCancelable(false);
                    concussionAlert.setTitle("Substitution can not applied to Batting team");

                    concussionAlert.setButton(AlertDialog.BUTTON_POSITIVE,"Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            cb_concussion.setChecked(false);
                            concussion = false;
                        }
                    });
                    concussionAlert.show();

                }

                else
                    Log.e("Sub", "onCheckedChanged, fielding : "+fielding+", batting : "+batting);
//            }

        }

        else {

            concussion = cb_concussion.isChecked();

            Log.e("Sub", "onCheckedChanged, fielding : "+fielding);
        }
    }



    public void displayErrorMessage(String message){

        AlertDialog messageAlert = new AlertDialog.
                Builder(SubstitutionActivity.this).create();
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



/*
    private void postJSON() {

        if (isNetworkAvailable()) {

            RealmResults<Substitution> results = realm.where(Substitution.class).
                    equalTo("matchid", matchid).
//                    equalTo("concussion", true).
                    notEqualTo("sync", 1).findAll();

            if (results.isEmpty()) {


            }

            else {

                JSONArray array = new JSONArray();

                JSONObject jsonMatch = new JSONObject();
                try {
                    jsonMatch.put("matchID", matchID);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                array.put(jsonMatch);

                Log.d("substitution", "postJSON, results : " + results);

                for (Substitution substitution : results) {

                    Log.d("substitution", "postJSON, substitution : " + substitution);

                    if (substitution.getSync() == 0) {

                        JSONObject object = new JSONObject();
                        try {

                            object.put("team", substitution.getTeam());
                            object.put("innings", substitution.getTeam());
                            object.put("player_id_out", substitution.getPlayer_OUT_ID());
                            object.put("d4s_playerid_out", substitution.getD4s_player_OUT_ID());
                            object.put("player_id_in", substitution.getPlayer_IN_ID());
                            object.put("d4s_playerid_in", substitution.getPlayer_IN_ID());
                            array.put(object);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }


                JSONObject jsonFeed = new JSONObject();
                try {
                    jsonFeed.put("Concussion", array);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.e("substitution", "jsonFeed : " + jsonFeed);


                JSONObject postparams = new JSONObject();
                try {
                    postparams.put("title", "CHASE_POST");
                    postparams.put("feed", jsonFeed);
                } catch (JSONException e) {
                    e.printStackTrace();
                }


                Log.d("substitution", "postparams : " + postparams);
                JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                        Constants.CHASE_CRICKET_MATCH_API, postparams,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {


                                Log.d("substitution", "response : " + response);

                                //if no error in response


                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {

//                            Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();


                                Log.d("substitution", "Error Message is  : " + error.getMessage());
//                        Toast.makeText(getApplicationContext(), "Invalid Username or Password", Toast.LENGTH_SHORT).show();
//                            displayNetworkErrorMessage();

                            }
                        });

                MyApplicationClass.getInstance(getApplicationContext()).addToRequestQueue(jsonObjReq, "postRequest");
                Log.d("substitution", "jsonObjReq  : " + jsonObjReq);
                Log.d("substitution)", "postparams  : " + postparams);
            }

        }
    }
*/



    private boolean isNetworkAvailable(){
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }



    private void addNewPlayer() {





//        if (team == 0) {
//
//            displayError("Please select a team");
//        }
//
//        else {
//
            // new code --> 18/03/2020

            if (team == 1)
                message = "Add new player for "/*team "*/ + teamA;
            else
                message = "Add new player for "/*team "*/ + teamB;

//            AddPlayers addPlayers = new AddPlayers();
             config = new RealmConfiguration.Builder()
                .name(AppConstants.GAME_ID + ".realm")
                .deleteRealmIfMigrationNeeded()
                .build();
        realm = Realm.getInstance(config);

            View checkBoxView = View.inflate(this, R.layout.checkbox, null);
            EditText editText = (EditText) checkBoxView.findViewById(R.id.edittext);
            /*CheckBox checkBox = (CheckBox) checkBoxView.findViewById(R.id.checkbox);
            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                    check_substitute = isChecked;
                    // Save to shared preferences
                }
            });*/
//            checkBox.setText("Sub");

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setIcon(R.drawable.ball);
            builder.setTitle(message);
            builder//.setMessage(" MY_TEXT ")
                    .setView(checkBoxView)
                    .setCancelable(false)
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                            if (editText.getText().toString().matches("")) {

                                displayErrorMessage("Invalid player name");
//                                Toast.makeText(PlayerList.this, "Invalid player name", Toast.LENGTH_SHORT).show();
                            }

                            else {

                                if (checkName(editText.getText().toString(), team)) {

                                    displayErrorMessage("Player already exists");
                                }

                                else {

                                    Number num = realm.where(Player.class).max("playerID");
                                    player_id = (num == null) ? 1 : num.intValue() + 1;
                                    check_substitute = true;

                                    Log.d("newplayer", "name : " + editText.getText().toString() + ", player_id : " + player_id);
                                    Log.d("newplayer", "check_substitute : " + check_substitute + ", flag : " + flag);
//                                    if (check_substitute) {   Commented on 16/06/2021
//                                    displayProgress();
                                        flagp = 1;
                                        savePlayerDetails(editText.getText().toString(),
                                                player_id, team, flagp, false, false, false);
//                                    setList(t);

                                   /* Commented on 16/06/2021
                                    } else {
                                        flagp = 0;
                                        checkPlayerStatus(editText.getText().toString(), player_id, team, flagp);
                                    }*/


                                    String strName = editText.getText().toString();
                                    displayAlert(strName, false, true);

                                    Log.d("new_bowler", "addNewPlayer , flag : " + flag);
                                }
                            }
                        }
                    })
                    .setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    }).show();


//        }

    }


    public boolean checkName(String name, int team) {

         config = new RealmConfiguration.Builder()
                .name(AppConstants.GAME_ID + ".realm")
                .deleteRealmIfMigrationNeeded()
                .build();
        realm = Realm.getInstance(config);
        Player tempPlayer = realm.where(Player.class).
                equalTo("matchid", matchid).
                equalTo("team", team).
                equalTo("playerName", name).findFirst();

        if (tempPlayer == null)
            return false;

        else
            return true;
    }



    public void checkPlayerStatus(String name, int player_id, int t, int flag) {


        AlertDialog.Builder builder = new AlertDialog.Builder(SubstitutionActivity.this);
        builder.setIcon(R.drawable.ball);
        builder.setCancelable(false);
        builder.setTitle("Player Status");
        builder.setSingleChoiceItems(R.array.playerkStatus, checkedItem , new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                switch (which) {
                    case 0:
                        cap = true;
                        vc = false;
                        wk = false;
                        none = false;
                        break;

                    case 1:
                        cap = false;
                        vc = true;
                        wk = false;
                        none = false;
                        break;

                    case 2:
                        cap = false;
                        vc = false;
                        wk = true;
                        none = false;
                        break;

                    case 3:
                        cap = false;
                        vc = false;
                        wk = false;
                        none = true;
                        break;

                    default:
                        break;


                }
            }
        });

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {


                Log.d("newplayer", "playerstatus, name : " + name +
                        ", player_id : " + player_id + ", t : " + t + ", flag : " + flag);
                Log.d("newplayer", "playerstatus, cap : " + cap + ", vc : " + vc + ", wk : " + wk);

                if (cap /*|| vc */|| wk) {

                    String message = "";
                    if (cap)
                        message = "Replace existing captain ?";
                    else if (vc)
                        message = "Replace existing vice captain ?";
                    else if (wk)
                        message = "Replace existing wicketkeeper ?";

                    AlertDialog alertDialog = new AlertDialog.Builder(SubstitutionActivity.this).create();
                    alertDialog.setIcon(R.drawable.ball);
                    alertDialog.setCancelable(false);
                    alertDialog.setTitle("Warning");
                    alertDialog.setMessage(message);
                    alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Proceed",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    replaceStatus(name, player_id, t, cap,/* vc,*/ wk);
                                    savePlayerDetails(name, player_id, t, flag, cap, vc, wk);

                                }
                            });
                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Cancel",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    cap = false;
                                    vc = false;
                                    wk = false;
                                    none = false;
                                    checkPlayerStatus(playerName, player_id, t, flag);
                                }
                            });
                    alertDialog.show();
                }

                else

                     savePlayerDetails(name, player_id, t, flag, cap, vc, wk);
                /*}

                else if (vc) {
                    savePlayerDetails(name, player_id, t, flag);
                }

                else if (wk) {
                    savePlayerDetails(name, player_id, t, flag);
                }

                else if (none) {
                    savePlayerDetails(name, player_id, t, flag);
                }*/
            }
        });

        builder.setNegativeButton("Cancel", null);

        // create and show the alert dialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }


    public void replaceStatus(String name, int player_id, int team, boolean cap, /*boolean vc,*/ boolean wk) {

        RealmResults<Player> results = realm.where(Player.class).
                equalTo("matchid", matchid).
                equalTo("team", team).findAll();

        for (Player player : results) {

            if (player.isCaptain() || player.isWicketKeeper()) {
                if (cap) {
                    player.setCaptain(false);
                }

                else if (wk) {
                    player.setWicketKeeper(false);
                }
            }
        }
    }



    private void postJSON(int matchid, String matchID, int player_id/*, boolean edit*/) {

        String cap = null, vc = null, wk = null;

        Log.d("matchID", "postJSON, 1, matchID : " + matchID);

        if (isNetworkAvailable()) {

            Player player = realm.where(Player.class).
                    equalTo("matchid", matchid).
                    equalTo("playerID", player_id).findFirst();

            if (player != null) {

                Log.d("ADD", "player : " + player.toString());

                JSONArray arrayPlayerB = new JSONArray();
                JSONObject  jsonPlayerB = new JSONObject();

                try {
                    jsonPlayerB.put("name", player.getPlayerName());
                    jsonPlayerB.put("player_id", player.getPlayerID());
                    jsonPlayerB.put("d4s_playerid", player.getD4s_playerid());
//                    if (edit) {
//
//                    }
//
//                    else {
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
//                    }
                    arrayPlayerB.put(jsonPlayerB);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                JSONArray subarray = new JSONArray();
                JSONArray subarray1 = new JSONArray();
                JSONArray parray = new JSONArray();
                JSONArray sarray = new JSONArray();

                JSONObject teampsobject= new JSONObject();
                JSONObject teamobject= new JSONObject();


                JSONObject jsonTeamB = new JSONObject();
                try {

                   /* if (edit) {
                        if (player.isSubstitute()) {
                            jsonTeamB.put("players", subarray);
                            jsonTeamB.put("substitutes", arrayPlayerB);
                        } else {
                            jsonTeamB.put("substitutes", subarray);
                            jsonTeamB.put("players", arrayPlayerB);
                        }
                    }

                    else {*/

                        if (player.isSubstitute()) {
                            jsonTeamB.put("substitutes", arrayPlayerB);
                        }
                        else {
                            jsonTeamB.put("players", arrayPlayerB);
                        }
//                    }

                    // edited

                    RealmResults<Player> players_list = realm.where(Player.class).
                            equalTo("matchID", matchID).
                            equalTo("team", player.getTeam()).findAll();

                    for (Player player111 : players_list) {

                        if (player111.isCaptain())
                            cap = player111.getPlayerName();
                        else
                        if (player111.isViceCaptain())
                            vc = player111.getPlayerName();

                        if (player111.isWicketKeeper())
                            wk = player111.getPlayerName();
                    }

                    jsonTeamB.put("captain", cap);
                    jsonTeamB.put("vice captain", vc);
                    jsonTeamB.put("wicketkeeper", wk);


                    /*if (player.isCaptain())
                        jsonTeamB.put("captain", player.getPlayerName());
                    else
                        jsonTeamB.put("captain", "");

                    if (player.isViceCaptain())
                        jsonTeamB.put("vice captain", player.getPlayerName());
                    else
                        jsonTeamB.put("vice captain", "");

                    if (player.isWicketKeeper())
                        jsonTeamB.put("wicketkeeper", player.getPlayerName());
                    else
                        jsonTeamB.put("wicketkeeper", "");*/

                    // added on 08/09/2020
                    teamobject.put("players", parray);
                    teamobject.put("substitutes", sarray);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                JSONObject jsonA = new JSONObject();
                JSONObject jsonB = new JSONObject();
                JSONObject json_temp = new JSONObject();
                try {

                    JSONObject json_p = new JSONObject();
                    json_p.put("player_id", 0);

                    subarray1.put(json_p);

                    json_temp.put("players", subarray1);
//                    if (edit)
//                        json_temp.put("substitutes", subarray);

                    jsonA.put("matchID", matchID);

                    /*if (player.getTeam() == 1) {
                        jsonB.put("TeamA", jsonTeamB);
                    }
                    else {
                        jsonB.put("TeamB", jsonTeamB);
                    }*/

                    // updated (put edit cndition) on 12/09/2020
//                    if (edit) {
//                        if (player.getTeam() == 1) {
//                            jsonB.put("TeamA", jsonTeamB);
//                            jsonB.put("TeamB", json_temp);
////                        jsonB.put("TeamB", teamobject);
//                        } else {
////                        jsonB.put("TeamA", teamobject);
//                            jsonB.put("TeamA", json_temp);
//                            jsonB.put("TeamB", jsonTeamB);
//                        }
//                    }
//
//                    else {
                        if (player.getTeam() == 1) {
                            jsonB.put("TeamA", jsonTeamB);
                        }
                        else {
                            jsonB.put("TeamB", jsonTeamB);
                        }
//                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                JSONArray array = new JSONArray();
                array.put(jsonA);
                array.put(jsonB);

                JSONObject jsonfeed = new JSONObject();
                try {
//                    if (edit)
//                        jsonfeed.put("EditPlayers", array);
//                    else {
                        // commented om 12/09/2020
//                        jsonfeed.put("AddPlayers", array);
                        // updated on 12/09/2020
                        if (team == 1) {
                            if (player.isSubstitute())
                                jsonfeed.put("AddTeamAPlayerSubstitute", array);
                            else
                                jsonfeed.put("AddNewTeamAPlayers", array);
                        }
                        else if (team == 2) {
                            if (player.isSubstitute())
                                jsonfeed.put("AddTeamBPlayerSubstitute", array);
                            else
                                jsonfeed.put("AddNewTeamBPlayers", array);
                        }

//                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                //adding values to postparams
                JSONObject postparams = new JSONObject();
                try {
                    postparams.put("title", "CHASE_POST");
                    postparams.put("feed", jsonfeed);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Log.e("captain", "postparams : "+postparams);

                JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                        Constants.CHASE_CRICKET_MATCH_API, postparams,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {

                                Log.d("SubstitutionActivity", "response : " + response);
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

                                                        Player player1 = bgRealm.where(Player.class).
                                                                equalTo("matchID", jsonMatch.getString("app_matchID")).
                                                                equalTo("playerID", response.getInt("playerid")).
                                                                findFirst();

                                                        if (player1 != null) {
                                                            player1.setSync(1);
                                                            if (player1.isEdit())
                                                                player1.setEdit(false);
                                                            bgRealm.copyFromRealm(player1);
                                                        }

                                                        else {
                                                            Log.d("SubstitutionActivity", "player not found, playerid : " +
                                                                    response.getInt("playerid"));
                                                        }

                                                    } catch (JSONException e) {
                                                        e.printStackTrace();
                                                    } catch (RealmPrimaryKeyConstraintException e) {
//                                                        progress.dismiss();
                                                        Toast.makeText(getApplicationContext(),
                                                                "Primary Key exists, Press Update instead",
                                                                Toast.LENGTH_SHORT).show();
                                                    }
                                                }

                                            });
                                        } catch (RealmException e) {
                                            Log.d("SubstitutionActivity", "Exception : " + e);
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

//                                progress.dismiss();
                                Log.e("SubstitutionActivity", "Error Message is  : " + error.getMessage());

                            }
                        });

                MyApplicationClass.getInstance(getApplicationContext()).addToRequestQueue(jsonObjReq, "postRequest");
                Log.d("SubstitutionActivity", "jsonObjReq  : " + jsonObjReq);
                Log.d("SubstitutionActivity", "postparams  : " + postparams);


            }

            else
                Log.d("SubstitutionActivity", "no player found, playerid : " + player_id);
            /*{

                JSONArray arrayPlayerB = new JSONArray();
                JSONArray array_temp = new JSONArray();
                JSONArray array_temp1 = new JSONArray();
                JSONObject  json_temp = new JSONObject();
                JSONObject  jsonPlayerB = new JSONObject();

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


                JSONObject jsonTeamB = new JSONObject();
                try {

                    if (edit) {
                        if (player.isSubstitute()) {
                            jsonTeamB.put("players", array_temp);
                            jsonTeamB.put("substitutes", arrayPlayerB);
                        } else {
                            jsonTeamB.put("players", arrayPlayerB);
                            jsonTeamB.put("substitutes", array_temp);
                        }
                    }

                    else {
                        if (player.isSubstitute()) {
                            jsonTeamB.put("substitutes", arrayPlayerB);
                        }
                        else {
                            jsonTeamB.put("players", arrayPlayerB);
                        }
                    }


                    // edited

                    RealmResults<Player> players_list = realm.where(Player.class).
                            equalTo("matchID", matchID).
                            equalTo("team", player.getTeam()).findAll();

                    for (Player player111 : players_list) {

                        if (player111.isCaptain())
                            cap = player111.getPlayerName();
                        else
                            if (player111.isViceCaptain())
                                vc = player111.getPlayerName();

                        if (player111.isWicketKeeper())
                            wk = player111.getPlayerName();
                    }

                    jsonTeamB.put("captain", cap);
                    jsonTeamB.put("vice captain", vc);
                    jsonTeamB.put("wicketkeeper", wk);

                    */
//            }
        /*if (player.isCaptain())
                        jsonTeamB.put("captain", "y");
                    else
                        jsonTeamB.put("captain", "n");

                    if (player.isViceCaptain())
                        jsonTeamB.put("vice captain", "y");
                    else
                        jsonTeamB.put("vice captain", "n");

                    if (player.isWicketKeeper())
                        jsonTeamB.put("wicketkeeper", "y");
                    else
                        jsonTeamB.put("wicketkeeper", "n");*//*

                    // till here
                } catch (JSONException e) {
                    e.printStackTrace();
                }


                JSONObject jsonA = new JSONObject();
                JSONObject jsonB = new JSONObject();
                try {

                    JSONObject json_p = new JSONObject();
                    json_p.put("player_id", 0);

                    array_temp1.put(json_p);

                    // for dummy value to work player edit option
                    json_temp.put("players", array_temp1);
                    json_temp.put("substitutes", array_temp);

                    jsonA.put("matchID", matchID);
                    if (player.getTeam() == 1) {
                        jsonB.put("TeamA", jsonTeamB);
                        jsonB.put("TeamB", json_temp);  // dummy json
                    }
                    else
                        jsonB.put("TeamA", json_temp);  // dummy json
                        jsonB.put("TeamB", jsonTeamB);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                JSONArray array = new JSONArray();
                array.put(jsonA);
                array.put(jsonB);

                JSONObject jsonfeed = new JSONObject();
                try {
                    jsonfeed.put("AddPlayers", array);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                //adding values to postparams
                JSONObject postparams = new JSONObject();
                try {
                    postparams.put("title", "CHASE_POST");
                    postparams.put("feed", jsonfeed);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Log.e("captain", "postparams : "+postparams);

                JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                        Constants.CHASE_CRICKET_MATCH_API, postparams,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {

                                Log.d("newplayer", "response : " + response);
//                                try {
//
//
//                                } catch (JSONException e) {
//                                    e.printStackTrace();
//                                }
//                            startActivity(new Intent(CaptainActivity.this, TossActivity.class));

                            }

                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {

//                                progress.dismiss();
                                Log.e("captain", "Error Message is  : " + error.getMessage());

                            }
                        });

                MyApplicationClass.getInstance(getApplicationContext()).addToRequestQueue(jsonObjReq, "postRequest");
                Log.d("captain", "jsonObjReq  : " + jsonObjReq);
                Log.d("captain", "postparams  : " + postparams);

            }*/

            /*else {
                Log.d("playerlist", "player : " + player);
            }*/

//            startActivity(new Intent(PlayerList.this,  ScoringActivity.class));
//        startActivity(new Intent(CaptainActivity.this,  SelectTeamAXIActivity.class));
//            finish();
//            progress.dismiss();
        }
    }



    public void savePlayerDetails(final String player_name, int player_id, int team, int flag, boolean cap, boolean vc, boolean wk){
//        Log.d("Addplayers", " savePlayerDetails, team : "+team);

//        Toast.makeText(getApplicationContext(), "Inside savePlayerDetails", Toast.LENGTH_SHORT).show();
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

//                        Number num = realm.where(Player.class).max("playerID");
//                        player_id = (num == null) ? 1 : num.intValue() + 1;
//                        Log.d("Addplayers", " num : "+num);

//                        Log.d("Addplayers", " player_id : "+player_id);
//                        Toast.makeText(getApplicationContext(), "Inside save Realm database", Toast.LENGTH_SHORT).show();
                        Player player = new Player();
                        player.setPlayerID(player_id);
                        player.setPlayerName(player_name);
                        player.setMatchid(matchid);
                        player.setMatchID(matchID);
                        player.setTeam(team);
                        Log.d("savePlayerDetails", "name : "+player_name+
                                ", playerid : "+player_id+", matchid : "+matchid+
                                ", team : " +team+", flag : "+flag);

//                        player.setCaptain("");
//                        player.setWicketKeeper("N");
                        if (flag == 0){
                            player.setSubstitute(false);
//                            player.setRole(0);
                        }
                        else {
                            player.setSubstitute(true);
//                            player.setRole(3);
                        }
                        player.setRetired(false);

                        if (cap)
                            player.setCaptain(true);
                        else
                        if (vc)
                            player.setViceCaptain(true);
                        else
                        if (wk)
                            player.setWicketKeeper(true);


                        realm.copyToRealm(player);
                        Log.d("newplayer", "matchid : " + matchid + "  player : " + player.toString());
//                        saveToSP();
                    }
                    catch (RealmPrimaryKeyConstraintException e) {
                        Toast.makeText(getApplicationContext(), "Primary Key exists, Press Update instead",
                                Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
        catch (RealmException e){
            Log.e("addplayers", "Exception : "+e);
//            Toast.makeText(getApplicationContext(), "Exception : "+ e, Toast.LENGTH_SHORT).show();
        }
        finally {

            if (realm != null) {
                realm.close();
            }
        }

        Log.d("matchID", "savePlayerDetails, end, matchID : " + matchID);
        postJSON(matchid, matchID, player_id/*, false*/);

    }



    public void freeMemory() {
        System.runFinalization();
        Runtime.getRuntime().gc();
        System.gc();
    }



    // ADDED ON 01/03/2021
    void savePlayerDetails(int matchid, String matchID, final String player_name, int player_id, int team, int flag){
//        Log.d("Addplayers", " savePlayerDetails, team : "+team);

//        Toast.makeText(getApplicationContext(), "Inside savePlayerDetails", Toast.LENGTH_SHORT).show();
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

//                        Number num = realm.where(Player.class).max("playerID");
                        Player player = new Player();
                        player.setPlayerID(player_id);
                        player.setPlayerName(player_name);
                        player.setMatchid(matchid);
                        player.setTeam(team);
                        Number num = realm.where(Player.class).
                                equalTo("matchid", matchid).
                                equalTo("team", team).
                                max("position");
                        int position = (num == null) ? 1 : num.intValue() + 1;
                        player.setPosition(position);
                        Log.d("savePlayerDetails", "name : "+player_name+
                                ", playerid : "+player_id+", matchid : "+matchid+
                                ", team : " +team+", flag : "+flag);
                        player.setMatchID(matchID);

                        if (flag == 0){
                            player.setSubstitute(false);
                            player.setPlaying(true);
                        }
                        else {
                            player.setSubstitute(true);
                            player.setPlaying(false);
                        }
                        player.setRetired(false);
                        player.setRetired_concussion(false);

                        realm.copyToRealm(player);
                        Log.d("newplayer", "matchid : " + matchid + "  player : " + player.toString());
                    }
                    catch (RealmPrimaryKeyConstraintException e) {
                        Log.e("Error", "RealmPrimaryKeyConstraintException : " + e);
                    }
                }
            });
        }
        catch (RealmException e){
            Log.e("addplayers", "Exception : "+e);
        }
        finally {

            if (realm != null) {
                realm.close();
            }
        }
    }

}
