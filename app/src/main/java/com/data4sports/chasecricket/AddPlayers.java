package com.data4sports.chasecricket;

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
import com.data4sports.chasecricket.activities.CaptainActivity;
import com.data4sports.chasecricket.activities.HomeActivity;
import com.data4sports.chasecricket.adapters.PlayerListAdapter;
import com.data4sports.chasecricket.applicationConstants.AppConstants;
import com.data4sports.chasecricket.models.Constants;
import com.data4sports.chasecricket.models.Match;
import com.data4sports.chasecricket.models.MatchOfficials;
import com.data4sports.chasecricket.models.MyApplicationClass;
import com.data4sports.chasecricket.models.Player;
import com.data4sports.chasecricket.adapters.PlayerListAdapter.customPlayerListButtonListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Locale;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;
import io.realm.exceptions.RealmException;
import io.realm.exceptions.RealmPrimaryKeyConstraintException;

public class AddPlayers extends AppCompatActivity implements customPlayerListButtonListener {

    //Declare Varibles
    TextView teamName, players, Slno, tv_m_start, tv_m_end, tv_num, tv_name, newTextView;
    EditText add_playerName;
    Button add;
    TableLayout player_table;
    LinearLayout ll_add_player;

    boolean btn_flag = true, del_add_flag = true;
    AlertDialog.Builder addNoteBuilder;
    //Player player;
    //Match match;

    String teamA, teamB, playerName, oldName, matchNote = "";
    int matchid, player_count, sub, click_count = 0, flag = 0, tplayer = 0;
    int i = 0, y = 0, player_id, position = -1, team, t = 0, total_players, x = 0;
    String matchID;
    boolean tflag = true, t1 = false, t2 = false, t3 = false, matchnote = false;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor mEditor;

    Realm realm = null;
    private ProgressDialog progress;

    ListView player_list;
    ArrayList<Player> nameList = new ArrayList<Player>();
    PlayerListAdapter playerListAdapter;

    RealmConfiguration config;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.add_players);

        //        sharedPreferences = PreferenceManager
//                .getDefaultSharedPreferences(this);

        Realm.init(this);
        Realm.getDefaultConfiguration();
         config = new RealmConfiguration.Builder()
                .name(AppConstants.GAME_ID + ".realm")
                .deleteRealmIfMigrationNeeded()
                .build();
        realm = Realm.getInstance(config);

        player_list = findViewById(R.id.add_player_list);

        assignElements();

        getFromSP();

        total_players = player_count + sub;
        //commneted on 28/04/2020
        /*//first view
        teamName.setAllCaps(true);
        teamName.setText(teamA);
        Slno.setText("\t1");
        runOnce1();*/

        playerListAdapter = new PlayerListAdapter(AddPlayers.this, nameList);
        playerListAdapter.setCustomListner((PlayerListAdapter.customPlayerListButtonListener) AddPlayers.this);
        player_list.setAdapter(playerListAdapter);

        Log.d("matchid", "oncreate, : " + matchid);
        RealmResults<Player> results = realm.where(Player.class).
                equalTo("matchid", matchid).
//                equalTo("team", 1).findAll();
                findAll();

        if (results.isEmpty()) {

            //first view
            teamName.setAllCaps(true);
            teamName.setText(teamA);
            Slno.setText("\t1");
            runOnce1();
        }

        else {

            RealmResults<Player> resultsA = results.where().equalTo("team", 1).findAll();
            RealmResults<Player> resultsB = results.where().equalTo("team", 2).findAll();
            if (results.isEmpty()) {

                //first view
                teamName.setAllCaps(true);
                teamName.setText(teamA);
                Slno.setText("\t1");
                runOnce1();
            } else if (resultsA.size() < total_players) {
                ++i;
                for (Player playerA : resultsA) {
                    Log.d("playerA", " name : " + playerA);
                    teamName.setAllCaps(true);
                    teamName.setText(teamA);
                    displayList(playerA);
                    ++i;
                }
            } else if ((resultsA.size() == total_players) && (resultsB.size() < total_players)) {
                ++y;
                for (Player playerB : resultsB) {
                    Log.d("playerB", " name : " + playerB);
                    teamName.setAllCaps(true);
                    teamName.setText(teamB);
                    displayList(playerB);
                    ++y;
                }
            }
        }

        /*// commented on 27/04/2020
        playerListAdapter = new PlayerListAdapter(AddPlayers.this, nameList);
        playerListAdapter.setCustomListner((PlayerListAdapter.customPlayerListButtonListener) AddPlayers.this);
        player_list.setAdapter(playerListAdapter);*/



//        Toast.makeText(getApplicationContext(), "match id : " + match_id, Toast.LENGTH_SHORT).show();





        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                btn_flag = true;

//                Log.d("Addplayers", "btnclick, btn_flag : "+btn_flag);
//                Log.d("Addplayers", "btnclick, i : "+i);
//                Log.d("Addplayers", "btnclick, y : "+y);
//                Log.d("Addplayers", "btnclick, total_players : "+total_players);
//                Log.d("Addplayers", "btnclick, player_count : "+player_count);

//                if (btn_flag)
//                    runOnce1();

//                else
                    if (i == (total_players + 2)) {
                    runOnce3();                         // to display messages
                }

                 if (i == (total_players + 3)){
                    runOnce4();
                }

//                else if (y == (player_count + 2)){
//
//                    runOnce5();
//                }

                else if (y == (total_players + 1)){
                    runOnce6();
                }

                else if (i >= 1 || y >= 1){
                    Log.d("add", "i : "+i+", y : "+y);

//                Toast.makeText(getApplicationContext(), "inside button click", Toast.LENGTH_SHORT).show();
//                getFromSP();
//                    if (add_playerName.isShown())
                    final String player_name = add_playerName.getText().toString();


                    if (player_name.matches("")) {

                        Toast.makeText(getApplicationContext(), "Invalid name", Toast.LENGTH_SHORT).show();
                    }

                    else {

                        if (checkName(player_name, team)) {

                            displayError("Player already exists");
                        }

                        else {
                            Log.d("add", "inside else, team : " + team);
                            addPlayer(player_name);
                            add_playerName.setText("");
                            i++;
                            if (y >= 1) {
                                ++y;
                                Slno.setText(String.valueOf(y));
                                Log.d("add", "y : " + y);
                            } else
                                Slno.setText(String.valueOf(i));
//                        saveToSP();

                            if (i > (total_players + 3)) {
                                teamName.setText(teamB);
                                players.setText("Players");
                            }
                            if (i > (total_players + player_count + 3)) {
                                teamName.setText(teamB);
                                players.setText("Sub Players");
                            }
                        }
                    }

                }

            }
        });




        // to add default team players
        add.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                if (player_count!=0){// && (sub!=0)) {


                    if (i > 0 && i <= total_players) {

                        callAlert(1, teamA);
                    }



                    else if (y >=1 && y <= total_players){

                        callAlert(2, teamB);
                    }
                }

                return true;
            }
        });

    }


    void runOnce1(){        // for entering team 1 players

        team = 1;

        Log.e("Addplayers", "runonce 1, b i : "+i);

        btn_flag = false;

//        if (i == 0){
//
////                add.setText("NEXT");
//
//            add.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {

        add.setText("ADD PLAYER");

        tv_m_start.setVisibility(View.GONE);
        teamName.setText(teamA);
        players.setText("Players");
        tv_m_end.setVisibility(View.GONE);

        tv_num.setVisibility(View.VISIBLE);
        tv_name.setVisibility(View.VISIBLE);

        ++i;
        Slno.setVisibility(View.VISIBLE);
        Slno.setText("\t"+ i);

        add_playerName.setVisibility(View.VISIBLE);
        add_playerName.setText("");

        ll_add_player.setVisibility(View.VISIBLE);
        Log.e("Addplayers", "runonce 1, e i : "+i);
//                }
//            });
//        }
    }


    void runOnce2(){  // displaying all team players

        team = 1;
        Log.e("Addplayers", "runonce 2, b i : "+i);

        players.setText("Players");
        tv_m_end.setVisibility(View.VISIBLE);

        tv_num.setVisibility(View.INVISIBLE);
        tv_name.setVisibility(View.INVISIBLE);

        Slno.setVisibility(View.INVISIBLE);
        add_playerName.setVisibility(View.INVISIBLE);
        add.setText("NEXT");

//        if (t1)
//            t2 = true;

//        ll_add_player.setVisibility(View.INVISIBLE);

//        add.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {

        ++i;
        Log.e("Addplayers", "runonce 2, e i : "+i);
//            }
//        });
    }



    void runOnce3(){        // for entering team 2 players
        Log.e("Addplayers", "runonce 3, b i : "+i);

//        Log.d("readPlayers", "n : "+n);
//        while (player_table.getChildCount() > 1){
//            TableRow tableRow = (TableRow) player_table.getChildAt(1);
//            player_table.removeView(tableRow);
//
//        }

        team = 2;
        t1 = true;

        tv_m_start.setVisibility(View.VISIBLE);
        teamName.setText(teamB);
        tv_m_end.setVisibility(View.GONE);
        ++i;

        nameList.clear();
        playerListAdapter.notifyDataSetChanged();
        player_list.setAdapter(playerListAdapter);

        ll_add_player.setVisibility(View.INVISIBLE);
        Log.e("Addplayers", "runonce 3, e i : "+i);

//        add.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//
//            }
//        });
    }


    void runOnce4(){        // for displaying all team 2 players

        Log.e("Addplayers", "runonce 4, b i : "+i+", y : "+y);

        add.setText("ADD PLAYER");
        team = 2;
//        ++i;

        nameList.clear();
        playerListAdapter.notifyDataSetChanged();
        player_list.setAdapter(playerListAdapter);

        tv_m_start.setVisibility(View.GONE);
        teamName.setText(teamB);
        players.setText("Players");
        tv_m_end.setVisibility(View.GONE);

        tv_num.setVisibility(View.VISIBLE);
        tv_name.setVisibility(View.VISIBLE);

        ++i;
        ++y ;
        del_add_flag = true;

        position = -1;
        Slno.setVisibility(View.VISIBLE);
        Slno.setText("\t"+ y);
        add_playerName.setVisibility(View.VISIBLE);
        add_playerName.setText("");
        ll_add_player.setVisibility(View.VISIBLE);
        Log.e("Addplayers", "runonce 4, e i : "+i+", y : "+y);
    }



    void runOnce5(){        //for declaring players are added
        Log.e("Addplayers", "runonce 5, b i : "+i);
        Log.d("Addplayers", "runonce 5,  : player_count"+player_count+", sub  : "+sub);

        players.setText("Players");
        tv_m_end.setVisibility(View.VISIBLE);

        tv_num.setVisibility(View.INVISIBLE);
        tv_name.setVisibility(View.INVISIBLE);

        Slno.setVisibility(View.INVISIBLE);
        add_playerName.setVisibility(View.INVISIBLE);
        add.setText("NEXT");

        if (t1)
            t2 = true;
        Log.e("Addplayers", "runonce 5, e i : "+i);

//        add.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                startActivity(new Intent(AddPlayers1.this, CaptainActivity.class));
//                finish();
//            }
//        });

    }



    void runOnce6(){
        Log.e("Addplayers", "runonce 6, b i : "+i);

        displayProgress();
        t3 = true;

        Toast.makeText(getApplicationContext(), "Players added", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(AddPlayers.this, CaptainActivity.class));
        finish();
        Log.e("Addplayers", "runonce 6, e i : "+i);
        progress.dismiss();
    }




    void assignElements(){

        //assign id's to its corresponding variables

        teamName = findViewById(R.id.team_name);
        players = findViewById(R.id.players);
        Slno = findViewById(R.id.total_players);
        tv_m_start = findViewById(R.id.txt_message1);
        tv_m_end = findViewById(R.id.txt_message2);
        tv_num = findViewById(R.id.txt_slno);
        tv_name = findViewById(R.id.txt_name);
        add_playerName = findViewById(R.id.edit_player_name);
        add = findViewById(R.id.btn_add_players);

        ll_add_player = findViewById(R.id.ll_add_player);

    }



    // get values from Shared Preferences
    void getFromSP(){

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        matchid = sharedPreferences.getInt("sp_match_id", 0);
        matchID = sharedPreferences.getString("sp_match_ID", null);
        teamA = sharedPreferences.getString("sp_teamA",null);
        teamB = sharedPreferences.getString("sp_teamB", null);
//        Match match = realm.where(Match.class).equalTo("matchID", match_id).findFirst();
//        if (match != null) {
//            player_count = match.getPlayer();
//            sub = match.getSubst();
//            Log.d("players" , "plyer = "+player_count);
//            Log.d("players" , "sub = "+sub);
//
//        }
//        else {
//            Toast.makeText(getApplicationContext(), "Could not find match id", Toast.LENGTH_SHORT).show();
//
//        }
        player_count = sharedPreferences.getInt("sp_player_count", 0);
        sub = sharedPreferences.getInt("sp_sub_seq", 0);
//        tplayer = player_count;
//        tsub = sub;
//        Log.d("player_count",String.valueOf(player_count));
//        Log.d("sub",String.valueOf(sub));
//        teamA = "Allllpha";
//        teamB = "Betttta";
//        player_count = 5;
//        sub = 3;
//        match_id = RandomNumber.generate("Match");
//        Toast.makeText(getApplicationContext(), "MatchId : "+ match_id,  Toast.LENGTH_LONG).show();
//        Toast.makeText(getApplicationContext(), " getFromSP() : GOOD", Toast.LENGTH_SHORT).show();
    }





    /*void saveToSP(){

        mEditor = sharedPreferences.edit();
//        Toast.makeText(getApplicationContext(), match_id, Toast.LENGTH_LONG).show();
//        mEditor.putInt("sp_match_id", match_id);
        mEditor.putString("sp_teamA", teamA);
        mEditor.putString("sp_teamB",teamB);
        mEditor.apply();

    }*/




    // seperates the the players as players and sub players in team wise

    void addPlayer(String name){
//        Log.d("addPlayer", "inside addPlayer");
        int total_players = player_count + sub;
        int total2 = player_count + sub;
         config = new RealmConfiguration.Builder()
                .name(AppConstants.GAME_ID + ".realm")
                .deleteRealmIfMigrationNeeded()
                .build();
        realm = Realm.getInstance(config);
        t++;

        if ((player_count!=0)){// && (sub!=0)){

            if (i > 0 && i <= total_players){
//                Log.d("addPlayer", "inside if(i)");


                Number num = realm.where(Player.class).max("playerID");
                player_id = (num == null) ? 1 : num.intValue() + 1;
                position++;
                team = 1;
                Player p = savePlayerDetails(matchid, matchID, name, player_id, team, flag);




                Slno.setText("\t"+ i);

                if (i >= player_count){
                    players.setText("Sub players");
//                if (i > player_count ){

                    flag = 1;
                }

                Log.d("addPlayer", "player : "+name+" team : "+team+" flag : "+flag+" i : "+i);
//                btnClick(name,i,y);

                displayList(p);
//                readPlayers(name, i, y);
            }


            else if (y > 0 && y <= total_players){

                Log.d("addPlayer", "inside else(y)");

                if (tflag)
                    runOnce();

                Number num = realm.where(Player.class).max("playerID");
                player_id = (num == null) ? 1 : num.intValue() + 1;
                position++;
                team = 2;
                Player p = savePlayerDetails(matchid, matchID, name, player_id, team, flag);
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
                    displayList(p);
//                }
            }



            if (i == (total_players)) {
                runOnce2();
            }

            if (y == (total_players)){
                players.setText("Players ");
                runOnce5();
            }


        }
    }




    void btnClick(String name, int n, int y){

        click_count ++;
        Log.d("btnClick", "click_count : "+click_count);
        if (click_count > 2 * (player_count + sub)){

            btnClick();
        }
        else {
            Log.d("btnClick", "before readplayers, click_count : "+click_count);

//            readPlayers(name, n, y);
        }




    }

    void btnClick(){

        add.setText("NEXT");

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


//                saveToSP();
                displayProgress();
                Toast.makeText(getApplicationContext(), "Players added", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(AddPlayers.this, CaptainActivity.class));
                finish();
                displayProgress();
            }
        });

    }

//    @Override
//    public void onBackPressed() {
//        finish();
////        super.onBackPressed();
//    }


    //    void readPlayers(String name, int n, int y){
//
//
////        Toast.makeText(getApplicationContext(), "Inside table display", Toast.LENGTH_SHORT).show();
//        //display names of players
//
//        TableRow row = new TableRow(this);
//        TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
//        row.setPadding(10, 25,10, 25);
//        if (t % 2 != 0)
//            row.setBackgroundResource(R.drawable.row_border);
//        row.setLayoutParams(lp);
//        TextView dataSl = new TextView(this);
//        TextView dataName = new TextView(this);
//        ImageView editName = new ImageView(this);
//        ImageView deleteName = new ImageView(this);
////        LinearLayout.LayoutParams params = new LinearLayout
////                .LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//
//        if (y > 0){
//
//            dataSl.setText("\t\t\t" + y + " ");
//        }
//        else {
//
//            dataSl.setText("\t\t\t" + i + " ");
//        }
//        row.addView(dataSl);
//
//        dataName.setText("\t" + name+"\t\t\t");
//        Log.d("readPlayers", "player : "+name);
//        row.addView(dataName);
//
//        editName.setImageResource(R.drawable.edit);
////        editName.setId(player_id);
//        editName.setOnClickListener(editListener);
////        editName.setForegroundGravity(View.FOCUS_RIGHT);
//        row.addView(editName);
//
//        deleteName.setImageResource(R.drawable.delete);
////        deleteName.setId(player_id);
//        deleteName.setOnClickListener(deleteListener);
//        row.addView(deleteName);
//
//        editName.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//
//            }
//        });
//
//
//        player_table.addView(row);
//
////        i++;
//
//
//        //clear table after displaying all players from Team 1
//
////        if (n == (total_players + 2)){
//
//
////        }
//
//    }




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
                        if (flag == 0){
                            player.setSubstitute(false);
                            player.setPlaying(true);
//                            player.setRole(0);
                        }
                        else {
                            player.setSubstitute(true);
                            player.setPlaying(false);
//                            player.setRole(3);
                        }
                        player.setRetired(false);
                        player.setRetired_concussion(false);

//                        Log.d("addplayers" , "player id : "+player_id+"  player :"+player);
//                        Log.d("addplayer","name : "+player_name+", team : "+team+", i : "+i+", y : "+y);

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

        Player p = realm.where(Player.class).
                equalTo("matchid", matchid).
                equalTo("team", team).
                equalTo("playerID", player_id).findFirst();
        if (p != null)
            return  p;
        else
            return  null;
    }


    void runOnce(){

        tflag = false;
        t = 1;
        flag = 0;
//        t1 = true;
//        position = 0;
    }




    public void callAlert(int teamNo, String teamName){


        AlertDialog inningsAlert = new AlertDialog.Builder(AddPlayers.this).create();
        inningsAlert.setIcon(R.drawable.ball);
        inningsAlert.setCancelable(false);
        inningsAlert.setTitle("Add Default Players?");
        inningsAlert.setMessage("Adding default players will clear currently added players.");
        inningsAlert.setButton(AlertDialog.BUTTON_POSITIVE, "CONFIRM",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();

                        addDefaultPlayers(teamNo, teamName);
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
            if (t >= 1){

                flag = 0;
            }
            if (t > player_count){

                flag = 1;
            }

            if (team == 1) {
                name = "Team_"+subString+"_Player-";

                if (flag == 1)
                    name = "Team_"+subString+"_Player_Sub-";
            }

            else {
                name = "Team_"+subString+"_Player-";

                if (flag == 1)
                    name = "Team_"+subString+"_Player_Sub-";
            }

            Log.e("addplayer", "team : "+teamName+", no : "+teamNo);

//            Log.e("Addplayers1", "addDefaultPlayers, name : "+name);


            Number num = realm.where(Player.class).max("playerID");
//            Log.i("Addplayers1", "addDefaultPlayers, num : "+num);
            player_id = (num == null) ? 1 : num.intValue() + 1;
//            Log.i("Addplayers1", "addDefaultPlayers, player_id : "+player_id);

            name = name + t;
            position++;

//            readPlayers(name, i, y);
            savePlayerDetails(matchid, matchID, name, player_id, teamNo, flag);
//            displayList(name);

            ++i;

            if (y >= 1)
                ++y;
        }

        setList();

        if (team ==1)
            runOnce2();

        else
            runOnce5();

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

                if (del_add_flag){
                    del_add_flag = false;
                    i = i - 3;

                }

                else {
                    i = i - 2;

                }

                if (y > 0)
                    y = y - 2;

                Log.e("AddPlayers", "deletePlayer, b 1 i : "+i);

                if (team == 1) {
                    runOnce1();
                    Slno.setText(String.valueOf(i));
                }
                else if (team == 2) {

                    runOnce4();
                    Slno.setText(String.valueOf(y));
                    Log.e("AddPlayers", "deletePlayer, b 1 y : "+y);
                }




//                displayPlayerList(team);

                Log.e("AddPlayers", "deletePlayer, i : "+i+", y : "+y+", team : "+team);

                playerListAdapter.notifyDataSetChanged();
                player_list.setAdapter(playerListAdapter);

    }



    public void displayList(Player p){//String name){

        nameList.add(p);
        playerListAdapter.notifyDataSetChanged();
        player_list.setAdapter(playerListAdapter);
    }


    @Override
    public void onEditClickListener(int position, String value) {

        RealmResults<Player> results = realm.where(Player.class).
                equalTo("matchid", matchid).
                equalTo("team", team).findAll();

        int playerID = results.get(position).getPlayerID();
//        String playerName = results.get(position).getPlayerName();

        oldName = value;

        AlertDialog.Builder editBuilder = new AlertDialog.Builder(AddPlayers.this);
        editBuilder.setIcon(R.drawable.ball);
        editBuilder.setCancelable(false);
        editBuilder.setTitle("Enter new name");

        final EditText input = new EditText(AddPlayers.this);

//        input.setInputType(InputType.TYPE_CLASS_TEXT);
        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_WORDS);
        input.setPadding(20, 15, 20, 15);
        input.setText(value);
        editBuilder.setView(input);
//        Log.d("Test", "inside setCommentry()");
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


                        saveToDB(matchid, playerID, oldName, playerName);

                        setList();
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
//        String playername = results.get(position).getPlayerName();


        AlertDialog delAlert = new AlertDialog.Builder(AddPlayers.this).create();
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




    public void setList(){

        nameList.clear();

        RealmResults<Player> results = realm.where(Player.class).
                equalTo("matchid", matchid).
                equalTo("team", team).findAll();

        for (Player player : results){

            nameList.add(player);
        }

        playerListAdapter.notifyDataSetChanged();
        player_list.setAdapter(playerListAdapter);
    }




    public void saveToDB(int matchid, int playerId, String oldName, String newName){

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

                                realm.copyToRealmOrUpdate(player);
                            }

                            else
                                Log.e("AddPlayers", "saveToDB, player : "+player);

//                            Bowler bowler = realm.where(Bowler.class).
//                                    equalTo("matchid", matchid).
//                                    equalTo("bowlerName", oldName).
//                                    equalTo("playerID", playerId).
//                                    findFirst();
//                            if (bowler != null){
//
////                                bowler.setBowlerName(newName);
//                                realm.copyToRealmOrUpdate(bowler);
//                            }
//
//                            Batsman batsman = realm.where(Batsman.class).
//                                    equalTo("matchid", matchid).
//                                    equalTo("batsmanName", oldName).
//                                    equalTo("batsman_pID", playerId).
//                                    findFirst();
//                            if (batsman != null){
////                                batsman.setBatsmanName(newName);
//                                realm.copyToRealmOrUpdate(batsman);
//                            }
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





    public void removeCurrentPlayers(int team){

        Log.e("Addplayers1", "removeCurrentPlayers, team : "+team);

        RealmResults<Player> results = realm.where(Player.class).
                equalTo("matchid", matchid).
                equalTo("team", team).findAll();

        for (Player player : results){

            if (player != null){

                Log.e("Addplayers1", "removeCurrentPlayers, player : "+player);

                if (!realm.isInTransaction()){

                    realm.beginTransaction();
                }

                player.deleteFromRealm();
                realm.commitTransaction();
            }



            Log.e("Addplayers1", "removeCurrentPlayers, i : "+i+", y : "+y);


        }

        if (team == 1){

            i = 1;
            y = 0;
//            Slno.setText(String.valueOf(i));

        }

        if (team == 2) {

            i = total_players + 2;
            y = 1;
//            Slno.setText(String.valueOf(y));
        }

    }





    @Override
    protected void onStop() {

        super.onStop();
//        Toast.makeText(getApplicationContext(), "onStop called 2 ", Toast.LENGTH_LONG).show();
        Log.e("AddPlayer", "onStop called");

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
//                        if (t1)

                            match.setAddPlayers(true);
//                        else {
//                            match.setAddPlayers(false);
//                         }

                        match.setCaptain(false);
                        match.setToss(false);
                        match.setOpeners(false);
                        match.setScoring(false);
                        match.setScoreCard(false);
                        match.setEndOfInnings(false);
                        match.setPulledMatch(false);
                        match.setSelectAXI(false);
                        match.setSelectBXI(false);
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

        // commented 28/04/20

       /* if (t1) {

            if (!t2) {

//            if (t1)
                deleteAll(1);

//            if (t2)
//                deleteAll(2);
            } else
                Log.d("t2", "" + t2);

            if (!t3) {
                deleteAll(1);
                deleteAll(2);
            } else
                Log.d("t3", "" + t3);
        }*/

        Log.e("AddPlayer", "onPause called t1 : " + t1);
    }


    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, HomeActivity.class));
        finish();
    }



    private void deleteAll(int team) {

        RealmResults<Player> results = realm.where(Player.class).
                equalTo("matchid", matchid).
                equalTo("team", team).findAll();

        for (Player player : results) {

            if (player != null) {

                if (!realm.isInTransaction()) {
                    realm.beginTransaction();
                }

                player.deleteFromRealm();
                realm.commitTransaction();
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

        addNoteBuilder = new AlertDialog.Builder(AddPlayers.this);
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
                            public void execute(Realm realm) {

                                try {

                                    Match match = realm.where(Match.class).
                                            equalTo("matchid", matchid).findFirst();
                                    match.setMatch_note(matchNote);
                                    match.setPost_matchnote(true);
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

                  /* Commented on 31/07/2021
                   // post matchnote
                    postMatchNote();*/
                }
            }
        });

        addNoteBuilder.setNegativeButton("CANCEL", null);
        AlertDialog alert = addNoteBuilder.create();
        alert.show();

    }



    public void displayProgress(){

        progress = new ProgressDialog(this);
        progress.setMessage("Please wait...");
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setIndeterminate(true);
        progress.setProgress(0);
        progress.show();
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
        AlertDialog alertDialog = new AlertDialog.Builder(AddPlayers.this).create();
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


    private boolean isNetworkAvailable(){
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }


/*
    void postMatchNote() {

        int sync, p_status = 1;
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


            if (post)
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

            // adding values into feed
            JSONObject jsonFeed = new JSONObject();
            try {
//                if (sync == 0) {
                if (post)
                    Log.d("cap", "match : " + match);
                else {
                    jsonFeed.put("AddMatch", jsonMatch);
                    jsonFeed.put("matchofficials", arrayOfficials);
                }
                jsonFeed.put("matchnote", match.getMatch_note());
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

                                    progress.dismiss();
                                    Toast.makeText(getApplicationContext(),
                                            response.getString("message"), Toast.LENGTH_SHORT).show();

                                } else {
                                    progress.dismiss();
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

                            progress.dismiss();
                            Log.e("captain", "Error Message is  : " + error.getMessage());

                        }
                    });

            MyApplicationClass.getInstance(getApplicationContext()).addToRequestQueue(jsonObjReq, "postRequest");
            Log.d("captain", "jsonObjReq  : " + jsonObjReq);
            Log.d("captain", "postparams  : " + postparams);

        }

    }
*/

}
