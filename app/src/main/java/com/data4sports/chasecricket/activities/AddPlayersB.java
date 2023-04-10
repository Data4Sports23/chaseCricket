package com.data4sports.chasecricket.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.data4sports.chasecricket.R;
import com.data4sports.chasecricket.adapters.PlayerListAdapter;
import com.data4sports.chasecricket.adapters.PlayerListAdapter.customPlayerListButtonListener;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;
import io.realm.Sort;
import io.realm.exceptions.RealmException;
import io.realm.exceptions.RealmPrimaryKeyConstraintException;

public class AddPlayersB extends AppCompatActivity implements customPlayerListButtonListener {

    //Declare Varibles
    TextView teamName, players, Slno, tv_m_start, tv_msl, tv_num, tv_name;
    EditText add_playerName;
    Button add, next;
    TableLayout player_table;
    LinearLayout ll_add_player, ll_details;

    AlertDialog.Builder addNoteBuilder;

    String teamA, teamB, playerName, oldName, matchNote = "";
    int matchid, player_count, sub, click_count = 0, flag = 0, tplayer = 0;
    int i = 0, y = 0, player_id, position = -1, team, t = 0, total_players, x = 0;
    String matchID;
    boolean tflag = true, t1 = false, t2 = false, t3 = false, nextFlag = false, matchnote = false;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor mEditor;

    Realm realm = null;
    private ProgressDialog progress;

    ListView player_list;
//    ArrayList<String> nameList = new ArrayList<String>();
    ArrayList<Player> nameList = new ArrayList<Player>();
    PlayerListAdapter playerListAdapter;

    RealmConfiguration config;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.add_players_b);

        Realm.init(this);
        Realm.getDefaultConfiguration();
         config = new RealmConfiguration.Builder()
                .name(AppConstants.GAME_ID + ".realm")
                .deleteRealmIfMigrationNeeded()
                .build();
        realm = Realm.getInstance(config);

        assignElements();
        getFromSP();

        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        total_players = player_count + sub;
//        total_players = player_count + sub;
        //first view
        teamName.setAllCaps(true);
        teamName.setText(teamB);
        tv_msl.setText("" + total_players);

        playerListAdapter = new PlayerListAdapter(AddPlayersB.this, nameList);
        playerListAdapter.setCustomListner((PlayerListAdapter.customPlayerListButtonListener) AddPlayersB.this);
        player_list.setAdapter(playerListAdapter);

        RealmResults<Player> resultsB = realm.where(Player.class).
                equalTo("matchid", matchid).
                equalTo("team", 2).findAll();

        Log.d("addB", "oncreate 1, i : " + i);
        if (resultsB == null) {

            Log.d("addB", "oncreate 2, i : " + i);
            ++i;
            Slno.setText("\t" + i);
        }

        else {

            Log.d("addB", "oncreate 3, i : " + i);

            for (Player playerB : resultsB) {

                displayList(playerB);//playerB.getPlayerName());
                ++i;
                Slno.setText(String.valueOf(i));
            }

            Log.d("addB", "oncreate 4, i : " + i);

            //Added on  17/11/2021
            ++i;
            Log.d("add", "oncreate 5, i : " + i);
            Slno.setText(String.valueOf(i));
            if (i >= total_players) {
//                nextFlag = true;
                next.setEnabled(true);
            }
            // till here


            /* Commented on 16/11/2021
            if (i == total_players) {

                Log.d("addB", "oncreate 5, i : " + i);
                nextFlag = true;
                add.setText("NEXT");
                add.setAllCaps(true);

                tv_m_end.setVisibility(View.VISIBLE);
           *//*     tv_num.setVisibility(View.INVISIBLE);
                tv_name.setVisibility(View.INVISIBLE);
                Slno.setVisibility(View.INVISIBLE);*//*
                ll_details.setVisibility(View.GONE);    // Added on 15/06/2021


                Log.d("addB", "oncreate 6, i : " + i);
                Log.d("addB", "oncreate 6, total_players : " + total_players);
            }

            else if (i < total_players) {

                Log.d("addB", "oncreate 6, i : " + i);
                ++i;
                nextFlag = false;
                Slno.setText("\t" + i);
            }*/
        }


        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                /*Log.d("addB", "oncreate 7, i : " + i);

                if (nextFlag) {
                    startActivity(new Intent(AddPlayersB.this, CaptainActivity.class));
                    finish();
                }

                else {*/

                    Log.d("addB", "oncreate 8, i : " + i);

                    final String player_name = add_playerName.getText().toString();
                    if (player_name.matches("")) {
                        Toast.makeText(getApplicationContext(), "Invalid name", Toast.LENGTH_SHORT).show();
                    }

                    else {

                        Log.d("add", "oncreate 9, i : " + i);

                        if (checkName(player_name, 2)) {
//                            displayError("Player already exists");
                            AlertDialog alertDialog = new AlertDialog.Builder(AddPlayersB.this).create();
                            alertDialog.setIcon(R.drawable.ball);
                            alertDialog.setCancelable(false);
                            alertDialog.setTitle("Warning");
                            alertDialog.setMessage("Player name already existing. Do you want to proceed");
                            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Proceed",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
//                                            addingPlayers(playerName);
                                            addingPlayers(player_name);
                                        }
                                    });
                            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Cancel",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
//                                            add_playerName.setText("");
                                        }
                                    });
                            alertDialog.show();
                        }

                        else {

                           addingPlayers(player_name);
                        }
                    }
                //}
            }
        });



        // to add default team players
        add.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                if (player_count!=0){// && (sub!=0)) {
//                    if (i > 0 && i <= total_players) {
                        callAlert(2, teamB);
//                    }
                }
                return true;
            }
        });

        // Added on 17/11/2021
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                post();  // Added on 27/11/2021
                startActivity(new Intent(AddPlayersB.this, TossActivity.class));
                finish();
            }
        });
    }



    public void addingPlayers(String player_name) {

        addPlayer(player_name);
        add_playerName.setText("");
        ++i;
        Slno.setText(String.valueOf(i));
        if (i > total_players) {
            next.setEnabled(true);  // Commented on 17/11/2021
            /*  Commented on 17/11/2021
            nextFlag = true;
            add.setText("NEXT");
            add.setAllCaps(true);
            ll_details.setVisibility(View.GONE);    // Added on 21/04/2021

            tv_num.setVisibility(View.INVISIBLE);    // Added on 14/06/2021
            tv_name.setVisibility(View.INVISIBLE);    // Added on 14/06/2021*/

            /* Commented on 22/04/2021
            Slno.setVisibility(View.INVISIBLE);
//                                players.setVisibility(View.INVISIBLE);
            tv_m_end.setVisibility(View.VISIBLE);
            tv_num.setVisibility(View.INVISIBLE);
            tv_name.setVisibility(View.INVISIBLE);
            add_playerName.setVisibility(View.INVISIBLE);*/
        }
        else {
            next.setEnabled(false);
            /*Slno.setText(String.valueOf(i));

            if (i <= player_count)
                players.setText("Players");
            else //if (i > player_count)
                players.setText("Sub Players");*/
        }
    }



    void assignElements(){

        //assign id's to its corresponding variables

        teamName = findViewById(R.id.team_name_b);
        players = findViewById(R.id.players_b);
        Slno = findViewById(R.id.total_players_b);
        tv_msl = findViewById(R.id.player_b_length);
//        tv_m_start = findViewById(R.id.txt_message1_b);
//        tv_m_end = findViewById(R.id.txt_message2_b);
        tv_num = findViewById(R.id.txt_slno_b);
        tv_name = findViewById(R.id.txt_name_b);
        add_playerName = findViewById(R.id.edit_player_name_b);
        add = findViewById(R.id.btn_add_players_b);
        next = findViewById(R.id.btn_next_b);   // Added on 17/11/2021

        ll_add_player = findViewById(R.id.ll_add_player_b);
        player_list = findViewById(R.id.add_player_list_b);
        ll_details = findViewById(R.id.ll_details_b);   // Added on 21/04/2021
//        ll_details.setVisibility(View.VISIBLE);  // Added on 21/04/2021
        next.setEnabled(false);
    }


    // get values from Shared Preferences
    void getFromSP(){

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        matchid = sharedPreferences.getInt("sp_match_id", 0);
        matchID = sharedPreferences.getString("sp_match_ID", null);
//        teamA = sharedPreferences.getString("sp_teamA",null);
//        teamB = sharedPreferences.getString("sp_teamB", null);
//        player_count = sharedPreferences.getInt("sp_player_count", 0);
//        sub = sharedPreferences.getInt("sp_sub_seq", 0);
        Match match = realm.where(Match.class)
                .equalTo("matchid", matchid)
                .findFirst();
        if (match != null) {
            teamB = match.getTeamB();
            player_count = match.getPlayerB();
        }
    }



    public void displayList(Player player) {//String name){

//        nameList.add(name);
        nameList.add(player);
        playerListAdapter.notifyDataSetChanged();
        player_list.setAdapter(playerListAdapter);
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



    public void displayError(String message){

//        progress.dismiss();
        AlertDialog alertDialog = new AlertDialog.Builder(AddPlayersB.this).create();
        alertDialog.setIcon(R.drawable.ball);
        alertDialog.setCancelable(false);
        alertDialog.setTitle(message);
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        add_playerName.setText("");
                    }
                });
        alertDialog.show();
    }



    public void displayProgress(){

        progress = new ProgressDialog(this);
        progress.setMessage("Please wait...");
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setIndeterminate(true);
        progress.setProgress(0);
        progress.show();
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
        Intent intent = new Intent(AddPlayersB.this, MatchNoteListActivity.class);
        intent.putExtra("matchid", matchid);
        intent.putExtra("matchID", matchID);
        intent.putExtra("innings", 0);
        intent.putExtra("over", 0);
        startActivity(intent);

     /* commented on 19/10/2020
          addNoteBuilder = new AlertDialog.Builder(AddPlayersB.this);
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

                if (input.getText().equals(null)){
                    Log.d("editResult", "no input ");
                }

                else {

//                    displayProgress();
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

                                   *//* Commented on 09/10/2020
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

                                    Toast.makeText(AddPlayersB.this, "Match note added", Toast.LENGTH_SHORT).show();

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

//                    // post matchnote
//                    postMatchNote();
                }
            }
        });

        addNoteBuilder.setNegativeButton("CANCEL", null);
        AlertDialog alert = addNoteBuilder.create();
        alert.show();*/
    }



    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, HomeActivity.class));
        finish();
    }



    public void removeCurrentPlayers(int team) {

        Log.e("Addplayers1", "removeCurrentPlayers, team : "+team);

        RealmResults<Player> results = realm.where(Player.class).
                equalTo("matchid", matchid).
                equalTo("team", team).findAll();

        for (Player player : results) {
            if (player != null) {

                Log.e("AddplayersA", "removeCurrentPlayers, player : " + player);
                if (!realm.isInTransaction()){
                    realm.beginTransaction();
                }
                player.deleteFromRealm();
                realm.commitTransaction();
            }
        }

        if (team == 2){

            i = 1;
            y = 0;
//            Slno.setText(String.valueOf(i));
        }

        /*if (team == 2) {

            i = total_players + 2;
            y = 1;
//            Slno.setText(String.valueOf(y));
        }*/

    }



    public void saveToDB(int matchid, int playerId, String oldName, String newName, boolean edit){

        if (!oldName.matches(newName)){

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

                            Player player = realm.where(Player.class).
                                    equalTo("matchid", matchid).
                                    equalTo("playerID", playerId).findFirst();
//                                    equalTo("playerName", oldName).

                            if (player != null) {
                                player.setPlayerName(newName);
                                player.setEdit(edit);
                                if (edit)
                                    player.setSync(0);
                                realm.copyToRealmOrUpdate(player);
                                Log.e("AddPlayersB", "saveToDB, player : "+player);
                            }

                            else
                                Log.e("AddPlayers", "saveToDB, player : "+player);

                        } catch (RealmPrimaryKeyConstraintException e) {
                            Toast.makeText(getApplicationContext(),
                                    "Primary Key exists, Press Update instead", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }

            catch (RealmException e) {
                Log.d("playerlist", "onclick, Exception : " + e);
            }

            finally {
                if (realm != null) {
                    realm.close();
                }
            }
        }
    }



    public void setList(){

        nameList.clear();

        RealmResults<Player> results = realm.where(Player.class).
                equalTo("matchid", matchid).
                equalTo("team", 2).findAll();

        /*for (Player player : results){
            nameList.add(player);//player.getPlayerName());
        }*/
        nameList.addAll(results);
        playerListAdapter.notifyDataSetChanged();
        player_list.setAdapter(playerListAdapter);
    }



    public void addDefaultPlayers(int teamNo, String teamName){

         config = new RealmConfiguration.Builder()
                .name(AppConstants.GAME_ID + ".realm")
                .deleteRealmIfMigrationNeeded()
                .build();
        realm = Realm.getInstance(config);
//        Log.e("Addplayers1", "addDefaultPlayers, team : "+team);

        String name = "";
        teamName.toUpperCase(Locale.getDefault());
        String subString = teamName.length() < 3 ? teamName : teamName.substring(0, 3);
//        subString.toUpperCase();

        removeCurrentPlayers(teamNo);

        for (int t = 1; t <= total_players; t++){

//            Log.e("Addplayers1", "addDefaultPlayers, team : "+team);
//            if (t >= 1){

//                flag = 0;
//            }
//            if (t > player_count){
//
//                flag = 1;
//            }

//            if (team == 2) {
//                name = "Team_"+subString+"_Player-";  Commented on 15/09/2021 (OLd)
            name = subString+"_P_"; // Updated on 15/09/2021

                /*if (flag == 1)
//                    name = "Team_"+subString+"_Player_Sub-";  Commened on 15/09/2021 (OLd)
                    name = subString+"_PS_";    // updated on 15/09/2021*/
//            }

            /*else {
                name = "Team_"+subString+"_Player-";

                if (flag == 1)
                    name = "Team_"+subString+"_Player_Sub-";
            }
*/
            Log.e("addplayerB", "team : "+teamName+", no : "+teamNo);

            Number num = realm.where(Player.class).max("playerID");
            player_id = (num == null) ? 1 : num.intValue() + 1;


            // Added on 15/09/2021
            SimpleDateFormat simpleDateFormat_d = new SimpleDateFormat("ddMMyyyy");
            String format_d = simpleDateFormat_d.format(new Date());
            SimpleDateFormat simpleDateFormat_t = new SimpleDateFormat("hhmm");
            String format_t = simpleDateFormat_t.format(new Date());

            name = name + t + "_" + format_d + "_" + format_t;

//            name = name + t;
            position++;

//            readPlayers(name, i, y);
            savePlayerDetails(matchid, matchID, name, player_id, teamNo, flag);
//            displayList(name);

            ++i;
            /*if (y >= 1)
                ++y;*/
        }
        setList();
        next.setEnabled(true);
        Slno.setText(String.valueOf(i));

       /* nextFlag = true;
        add.setText("NEXT");
        add.setAllCaps(true);
        ll_details.setVisibility(View.GONE);
        tv_num.setVisibility(View.INVISIBLE);    // Added on 14/06/2021
        tv_name.setVisibility(View.INVISIBLE);    // Added on 14/06/2021*/

            /* Commented on 22/04/2021
        Slno.setVisibility(View.INVISIBLE);
//                players.setVisibility(View.INVISIBLE);
        tv_m_end.setVisibility(View.VISIBLE);
        tv_num.setVisibility(View.INVISIBLE);
        tv_name.setVisibility(View.INVISIBLE);
        add_playerName.setVisibility(View.INVISIBLE);*/
    }



    public void callAlert(int teamNo, String teamName){

        AlertDialog inningsAlert = new AlertDialog.Builder(AddPlayersB.this).create();
        inningsAlert.setIcon(R.drawable.ball);
        inningsAlert.setCancelable(false);
        inningsAlert.setTitle("Add Default Players?");
        inningsAlert.setMessage("Adding default players will clear currently added players.");
        inningsAlert.setButton(AlertDialog.BUTTON_POSITIVE, "CONFIRM",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        addDefaultPlayers( teamNo, teamName);
                    }
                });

        inningsAlert.setButton(AlertDialog.BUTTON_NEUTRAL, "CANCEL",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        inningsAlert.show();
    }



    // save player's details to local database

    Player savePlayerDetails(int matchid, String matchID, final String player_name, int player_id, int team, int flag){
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

                        Number num = realm.where(Player.class).max("playerID");
//                        player_id = (num == null) ? 1 : num.intValue() + 1;
//                        Log.d("Addplayers", " num : "+num);

//                        Log.d("Addplayers", " player_id : "+player_id);
//                        Toast.makeText(getApplicationContext(), "Inside save Realm database", Toast.LENGTH_SHORT).show();
                        Player player = new Player();
                        player.setPlayerID(player_id);
                        player.setPlayerName(player_name);
                        player.setMatchid(matchid);
                        player.setTeam(team);
                        player.setPosition(position);
                        Log.d("savePlayerDetails", "name : "+player_name+
                                ", playerid : "+player_id+", matchid : "+matchid+
                                ", team : " +team+", flag : "+flag);
                        player.setMatchID(matchID);
//                        player.setCaptain("");
//                        player.setWicketKeeper("N");
//                        if (flag == 0){
                            player.setSubstitute(false);
                            player.setPlaying(true);
//                            player.setRole(0);
                        /*}
                        else {
                            player.setSubstitute(true);
                            player.setPlaying(false);
//                            player.setRole(3);
                        }*/
                        player.setRetired(false);
                        player.setRetired_concussion(false);
//                        Log.d("addplayers" , "player id : "+player_id+"  player :"+player);
//                        Log.d("addplayer","name : "+player_name+", team : "+team+", i : "+i+", y : "+y);

                        realm.copyToRealm(player);
                        Log.d("newplayer", "matchid : " + matchid + "  player : " + player.toString());
//                        saveToSP();
                    }
                    catch (RealmPrimaryKeyConstraintException e) {
                        Log.e("Error",  " RealmPrimaryKeyConstraintException : " + e);
                        /*Toast.makeText(getBaseContext(), "Primary Key exists, Press Update instead",
                                Toast.LENGTH_SHORT).show();*/
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

        Player p = realm.where(Player.class).
                equalTo("matchid", matchid).
                equalTo("team", team).
                equalTo("playerID", player_id).findFirst();
        if (p != null)
            return  p;
        else
            return  null;
    }



    void addPlayer(String name){
//        Log.d("addPlayer", "inside addPlayer");
//        int total_players = player_count + sub;
//        int total2 = player_count + sub;
         config = new RealmConfiguration.Builder()
                .name(AppConstants.GAME_ID + ".realm")
                .deleteRealmIfMigrationNeeded()
                .build();
        realm = Realm.getInstance(config);
        t++;

        if ((player_count != 0)){// && (sub!=0)){

//            if (i > 0 && i <= total_players){
//                Log.d("addPlayer", "inside if(i)");


                Number num = realm.where(Player.class).max("playerID");
                player_id = (num == null) ? 1 : num.intValue() + 1;
                position++;
                team = 2;
                Player player = savePlayerDetails(matchid, matchID, name, player_id, team, flag);




                Slno.setText("\t"+ i);

                /*if (i > player_count){
                    players.setText("Sub players");
//                if (i > player_count ){

                    flag = 1;
                }
*/
                Log.d("addPlayer", "player : "+name+" team : "+team+" flag : "+flag+" i : "+i);
//                btnClick(name,i,y);

                displayList(player);//name);
//                readPlayers(name, i, y);
//            }


/*
            else if (y > 0 && y <= total_players){

                Log.d("addPlayer", "inside else(y)");

                if (tflag)
                    runOnce();

                Number num = realm.where(Player.class).max("playerID");
                player_id = (num == null) ? 1 : num.intValue() + 1;
                position++;
                team = 2;
                savePlayerDetails(matchid, matchID, name, player_id, team, flag);
//                position++;
//                flag = 0;


//                ++y ;


//                if (sub == 0) {
//
//                    displayList(name);
//
//                    Slno.setText("\t" + y);
//
//                    if (y >= player_count) {
//                        players.setText("Sub players");
//
////                if (y > player_count ){
////                    players.setText("Sub Players");
//                        flag = 1;
//                    }
//                    Log.d("addPlayer", "player : " + name + " team : " + team + " flag : " + flag + " i : " + i);
////                btnClick(name,i,y);
////                readPlayers(name, i, y);
//
//
//                }
//
//                else if (sub > 0) {


                Slno.setText("\t" + y);

                if (y >= player_count) {
                    players.setText("Sub players");

//                if (y > player_count ){
//                    players.setText("Sub Players");
                    flag = 1;
                }
                Log.d("addPlayer", "player : " + name + " team : " + team + " flag : " + flag + " i : " + i);
//                btnClick(name,i,y);
//                readPlayers(name, i, y);
                displayList(name);
//                }
            }
*/

        }
    }

    @Override
    public void onEditClickListener(int position, String value) {

        RealmResults<Player> results = realm.where(Player.class).
                equalTo("matchid", matchid).
                equalTo("team", team).findAll();

        int playerID = results.get(position).getPlayerID();
//      String playerName = results.get(position).getPlayerName();

        oldName = value;

        AlertDialog.Builder editBuilder = new AlertDialog.Builder(AddPlayersB.this);
        editBuilder.setIcon(R.drawable.ball);
        editBuilder.setCancelable(false);
        editBuilder.setTitle("Enter new name");

        final EditText input = new EditText(AddPlayersB.this);

//      input.setInputType(InputType.TYPE_CLASS_TEXT);
        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_WORDS);
        input.setPadding(20, 15, 20, 15);
        input.setText(value);
        editBuilder.setView(input);
//      Log.d("Test", "inside setCommentry()");
        editBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

            //            @Override
            public void onClick(DialogInterface dialog, int which) {

                if (input.getText().toString().equals(null)){
                    Toast.makeText(getApplicationContext(),
                            "Please enter a valid name", Toast.LENGTH_SHORT).show();
                }

                else {
                    playerName = input.getText().toString();
                    Log.d("Test", "player name : " + playerName);

                    if (!playerName.matches("")){
                        if (checkName(playerName, team)) {
//                            displayError("Player already exists");
                            AlertDialog alertDialog = new AlertDialog.Builder(AddPlayersB.this).create();
                            alertDialog.setIcon(R.drawable.ball);
                            alertDialog.setCancelable(false);
                            alertDialog.setTitle("Warning");
                            alertDialog.setMessage("Player name already existing. Do you want to proceed");
                            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Proceed",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
//                                            addingPlayers(playerName);
                                            saveToDB(matchid, playerID, oldName, playerName, false);
                                            setList();
                                        }
                                    });
                            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Cancel",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
//                                            add_playerName.setText("");
                                        }
                                    });
                            alertDialog.show();

                        }
                        else {

                            saveToDB(matchid, playerID, oldName, playerName, false);
                            setList();
                        }

//                        }
                    }
                    else {
                        Log.d("Test","buildertext = null");
                    }

                }
            }
        });

        editBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        AlertDialog alert = editBuilder.create();
        alert.show();
    }



    @Override
    public void onDeleteClickListener(int position, String value) {

        RealmResults<Player> results = realm.where(Player.class).
                equalTo("matchid", matchid).
                equalTo("team", team).findAll();

        int playerID = results.get(position).getPlayerID();
//      String playername = results.get(position).getPlayerName();

        AlertDialog delAlert = new AlertDialog.Builder(AddPlayersB.this).create();
        delAlert.setIcon(R.drawable.ball);
        delAlert.setCancelable(false);
        delAlert.setTitle("Delete Player");
        delAlert.setMessage("Press confirm to delete "+value);
        delAlert.setButton(AlertDialog.BUTTON_POSITIVE, "CONFIRM",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();

                        Log.e("addplayers", "onDeleteClickListener, position : "+position+", i : "+i+", y : "+y);
                        Log.e("addplayers", "onDeleteClickListener, value : "+value+", playerID : "+playerID);

                        nameList.remove(position);
                        deletePlayer(value, playerID);
                    }
                });

        delAlert.setButton(AlertDialog.BUTTON_NEUTRAL, "CANCEL",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        delAlert.show();
    }


    public void  deletePlayer(String playerName, int playerId){

        Log.e("AddPlayers", "deletePlayer, team : "+team+", y : "+y);
        Player player = realm.where(Player.class).
                equalTo("matchid", matchid).
                equalTo("team", team).
                equalTo("playerID", playerId).findFirst();

        if (player != null){
            if (!realm.isInTransaction()){
                realm.beginTransaction();
            }
            player.deleteFromRealm();
            realm.commitTransaction();
        }

        Toast.makeText(getApplicationContext(), "Player "+playerName+" is deleted", Toast.LENGTH_SHORT).show();

        /*if (del_add_flag){
            del_add_flag = false;
            i = i - 3;

        }

        else {
            i = i - 2;

        }

        if (y > 0)
            y = y - 2;*/

        Log.e("AddPlayers", "deletePlayer, b 1 i : "+i);

//        if (team == 2) {
//            runOnce1();
            i = 1;
            Slno.setText(String.valueOf(i));
//        }
       /* else if (team == 2) {

            runOnce4();
            Slno.setText(String.valueOf(y));
            Log.e("AddPlayers", "deletePlayer, b 1 y : "+y);
        }*/

//                displayPlayerList(team);

        Log.e("AddPlayers", "deletePlayer, i : "+i+", y : "+y+", team : "+team);

        playerListAdapter.notifyDataSetChanged();
        player_list.setAdapter(playerListAdapter);
    }



    @Override
    protected void onPause() {

        super.onPause();
        Log.d("AddB", "onPause");
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
//                        if (t1)

                        match.setAddPlayersB(true);
//                        else {
//                            match.setAddPlayers(false);
//                         }

//                        match.setAddPlayers(false);
                        match.setAddPlayersA(false);
//                        match.setCaptain(false);
//                        match.setToss(false);
//                        match.setOpeners(false);
//                        match.setScoring(false);
//                        match.setScoreCard(false);
//                        match.setEndOfInnings(false);
//                        match.setPulledMatch(false);
//                        match.setSelectAXI(false);
//                        match.setSelectBXI(false);
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

        Log.d("toss", "t1 " + t1 +", t2 "+t2);
        Log.e("AddPlayer", "onPause called t1 : " + t1);
    }



    @Override
    protected void onStop() {

        super.onStop();
//        Toast.makeText(getApplicationContext(), "onStop called 2 ", Toast.LENGTH_LONG).show();
        Log.e("AddB", "onStop called");

    }




    // added on 23/09/2020
/*
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
    }
*/


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
                    jsonMatch.put("teamAplayers", match.getPlayerA());
                    jsonMatch.put("teamBplayers", match.getPlayerB());
//                    jsonMatch.put("substitute_players", match.getSubst());
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

            // adding values into feed
            JSONObject jsonFeed = new JSONObject();
            try {
                // added on 09/10/2020
                if (sync == 1) {
//                if (post)
                    Log.d("cap", "match : " + match);
                }
                else {
                    jsonFeed.put("AddMatch", jsonMatch);
//                    jsonFeed.put("AddMatchOfficials", arrayOfficials);
                }
                if (notes_size > 0)                 // added on 09/10/2020
                    jsonFeed.put("AddMatchNote", jsonNote);
//                jsonFeed.put("matchnote", match.getMatch_note());
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
//                                                        match1.setPost_matchnote(false);
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
                                    Toast.makeText(getApplicationContext(),
                                            response.getString("message"), Toast.LENGTH_SHORT).show();


//                                    //starting the profile activity
//
//                                   startActivity(new Intent(CaptainActivity.this, TossActivity.class));
//                                    finish();*/

                                } else {
//                                    progress.dismiss();
                                    Toast.makeText(getApplicationContext(),
                                            response.getString("message"), Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
//                            startActivity(new Intent(CaptainActivity.this, TossActivity.class));

                        }

                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

//                            progress.dismiss();
                            Log.e("captain", "Error Message is  : " + error.getMessage());

//                            AlertDialog.Builder builderInner = new AlertDialog.Builder(CaptainActivity.this);
//                            builderInner.setIcon(R.drawable.ball);
//                            builderInner.setCancelable(false);
//                            builderInner.setMessage("Server Error");
//                            builderInner.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialog, int which) {
//                                    dialog.dismiss();
//                                    startActivity(new Intent(CaptainActivity.this, TossActivity.class));
//                                }
//                            });
//                            builderInner.show();

                        }
                    });

            MyApplicationClass.getInstance(getApplicationContext()).addToRequestQueue(jsonObjReq, "postRequest");
            Log.d("captain", "jsonObjReq  : " + jsonObjReq);
            Log.d("captain", "postparams  : " + postparams);

        }

    }



    private boolean isNetworkAvailable(){
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }


    // Added on 27/11/2021
    void post() {
        if (isNetworkAvailable()) {
            Match match = realm.where(Match.class)
                    .equalTo("matchid", matchid)
                    .findFirst();
            if (match != null) {
                if ((match.getTeamAId() != 0) && (match.getTeamAId() != 0)) {

                    Player player = realm.where(Player.class)
                            .equalTo("matchid", matchid)
                            .equalTo("team", 1)
                            .findFirst();
                    if (player != null) {
                        if (player.getD4s_playerid() == 0)
                            postPlayers(1, match.getMatchID(), match.getTeamAId());
                        postPlayers(2, match.getMatchID(), match.getTeamBId());
                    }

                    /*if ((player != null ) && (player.getD4s_playerid() > 0) )
                        postPlayers(2, match.getMatchID(), match.getTeamBId());
                    else {
                        postPlayers(1, match.getMatchID(), match.getTeamAId());
                        postPlayers(2, match.getMatchID(), match.getTeamBId());
                    }*/
                } else {
                    postMatch();
                }
            }
        }
    }


    void postMatch() {
         config = new RealmConfiguration.Builder()
                .name(AppConstants.GAME_ID + ".realm")
                .deleteRealmIfMigrationNeeded()
                .build();
        realm = Realm.getInstance(config);
        if (isNetworkAvailable()) {

            Match match = realm.where(Match.class).equalTo("matchid", matchid).findFirst();
            Log.d("APA", "match : " + match);

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
//                    matchObject.put("substitute_players", match.getSubst());
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
                                                            equalTo("matchID",MATCHID).
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

                                                    RealmResults<MatchOfficials> result_officials =
                                                            bgRealm.where(MatchOfficials.class).
                                                                    equalTo("matchid", matchid).
                                                                    equalTo("d4s_id", 0).
                                                                    equalTo("sync", 0).findAll();
                                                    if (result_officials.size() > 0)
                                                        postAddOfficials();


                                                } catch (RealmPrimaryKeyConstraintException e) {
                                                    progress.dismiss();
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
                            Log.d("CREATE", "Error Message is  : " + error);
                        }
                    });

            MyApplicationClass.getInstance(getApplicationContext()).
                    addToRequestQueue(jsonObjReq, "postRequest");
            Log.d("create", "jsonObjReq  : " + jsonObjReq);
            Log.d("create", "postparams  : " + postparams);

        }
    }


    private void postAddOfficials() {
         config = new RealmConfiguration.Builder()
                .name(AppConstants.GAME_ID + ".realm")
                .deleteRealmIfMigrationNeeded()
                .build();
        realm = Realm.getInstance(config);
        if (isNetworkAvailable()) {
            RealmResults<MatchOfficials> results = realm.where(MatchOfficials.class).
                    equalTo("matchid", matchid).
                    equalTo("d4s_id", 0).
                    equalTo("sync", 0).findAll();

            Log.d("ADD_OFFICIALS", "Scoring, results 1 : " + results);
            if (results.isEmpty()) {

                Log.d("ADD_OFFICIALS", "Scoring, results : " + results);
            } else {

                JSONArray arrayOfficials = new JSONArray();

                for (MatchOfficials officials : results) {

                    JSONObject jsonOfficials = new JSONObject();

                    try {
                        if (!officials.getOfficialName().matches("")) {
                            jsonOfficials.put("name", officials.getOfficialName());

                            if (officials.getStatus().matches("u1") || officials.getStatus().matches("u2"))
                                jsonOfficials.put("type", "u");
                            else
                                jsonOfficials.put("type", officials.getStatus());
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
                            try {
                                JSONArray array = response.getJSONArray("playerdet");
                                for (int i = 0; i < array.length(); i++) {
                                    syncPlayer(array.getJSONObject(i));
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

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


    // Added on 14/12/2021
    private void syncPlayer(JSONObject object) {

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

                        try {
                            Player player = realm.where(Player.class)
                                    .equalTo("matchID", object.getString("matchid"))
                                    .equalTo("playerID", object.getInt("app_player_id"))
                                    .findFirst();

                            if (player != null) {
                                player.setD4s_playerid(object.getInt("d4splayerid"));
                                realm.copyToRealmOrUpdate(player);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

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
}
