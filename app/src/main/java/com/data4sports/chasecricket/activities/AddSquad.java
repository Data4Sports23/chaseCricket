package com.data4sports.chasecricket.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.data4sports.chasecricket.R;
import com.data4sports.chasecricket.adapters.PlayerListAdapter;
import com.data4sports.chasecricket.applicationConstants.AppConstants;
import com.data4sports.chasecricket.models.Player;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;
import io.realm.exceptions.RealmException;
import io.realm.exceptions.RealmPrimaryKeyConstraintException;

public class AddSquad extends AppCompatActivity implements PlayerListAdapter.customPlayerListButtonListener {

    TextView tv_team, tv_players, tv_slno, tv_num,  tv_name, tv_total;
    Button btn_add, next;
    EditText et_name;
    LinearLayout linearLayout, ll_details;
    ListView squadList;

    SharedPreferences sharedPreferences;
    int matchid, team, player_count = 0, sub = 0, player_id = 0, slno = 0;
    String matchID, teamName = null, playerName = null, oldName = null;

    Realm realm = null;
//    ArrayList<String> nameList = new ArrayList<String>();
    ArrayList<Player> nameList = new ArrayList<Player>();
    PlayerListAdapter playerListAdapter;
    ImageButton back1;
    RealmConfiguration config;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_squad);

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

                onBackPressed();
            }
        });

        Realm.init(this);
        Realm.getDefaultConfiguration();
        squadList = findViewById(R.id.squad_add_player_list);

        assignElements();
        getFromSP();

        tv_team.setText(teamName);

        playerListAdapter = new PlayerListAdapter(AddSquad.this, nameList);
        playerListAdapter.setCustomListner((PlayerListAdapter.customPlayerListButtonListener) AddSquad.this);
        squadList.setAdapter(playerListAdapter);

        setList();
        ++slno;
        tv_slno.setText(String.valueOf(slno));
        next.setEnabled(slno > player_count);   // Added on 19/11/2021

        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (et_name.getText().toString().matches("")) {
                    displayError("Invalid player name");
                }
                else  {

                    playerName = et_name.getText().toString();
                    if (checkName(playerName, team)) {
                        displayError("Player already exists");
                    }

                    else {
                        addPlayer(playerName);
                    }
                }
            }
        });

        // Added on 18/11/2021
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (team == 1) {
                    startActivity(new Intent(AddSquad.this, SelectTeamAXIActivity.class));
                    finish();
                }
                else if (team == 2) {
                    startActivity(new Intent(AddSquad.this, SelectTeamBXIActivity.class));
                    finish();
                }
            }
        });
        // === till herr
    }


    void assignElements() {

        //assign id's to its corresponding variables

        tv_team = findViewById(R.id.squad_team_name);
        tv_players = findViewById(R.id.squad_players);
        tv_slno = findViewById(R.id.squad_total_players);
        tv_num = findViewById(R.id.squad_txt_slno);
        tv_name = findViewById(R.id.squad_txt_name);
        et_name = findViewById(R.id.squad_edit_player_name);
        btn_add = findViewById(R.id.squad_btn_add_players);
        next = findViewById(R.id.squad_btn_next);

        linearLayout = findViewById(R.id.ll_squad_add_player);
        linearLayout = findViewById(R.id.ll_squad_details);
        next.setEnabled(false);
    }



    void getFromSP() {

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        matchid = sharedPreferences.getInt("sp_match_id", 0);
        matchID = sharedPreferences.getString("sp_match_ID", null);
//        team = sharedPreferences.getInt("sp_team_id", 0);
//        teamName = sharedPreferences.getString("sp_team",null);
//        player_count = sharedPreferences.getInt("sp_player_count", 0);
//        sub = sharedPreferences.getInt("sp_sub_seq", 0);

        Intent i = getIntent();
        team = i.getIntExtra("team_id", 0);
        teamName = i.getStringExtra("team");
        player_count = i.getIntExtra("player_count", 0);
    }



    public void addPlayer(String playerName) {

         config = new RealmConfiguration.Builder()
                .name(AppConstants.GAME_ID + ".realm")
                .deleteRealmIfMigrationNeeded()
                .build();
        realm = Realm.getInstance(config);
        if ((player_count != 0)) {

            Number num = realm.where(Player.class).max("playerID");
            player_id = (num == null) ? 1 : num.intValue() + 1;
            Player p = savePlayerDetails(player_id);
            Log.d("SQD", "addPlayer, p = " + p.toString());
            if (p != null)
                displayList(p);
//            else
//                Log.d("SQD", "addPlayer, p == null");
            ++slno;
        }

        tv_slno.setText(String.valueOf(slno));
        et_name.setText("");

        next.setEnabled(slno > player_count);   // Added on 19/11/2021
    }



    Player savePlayerDetails(int player_id) {
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
                        player.setPlayerName(playerName);
                        player.setMatchid(matchid);
                        player.setTeam(team);
//                        player.setPosition(position);
                        player.setMatchID(matchID);
                        player.setSubstitute(false);
                        player.setRetired(false);
                        player.setRetired_concussion(false);
                        player.setPlaying(false);

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



    public void displayList(Player p) {//String name){

//        nameList.add(name);
        nameList.add(p);
        playerListAdapter.notifyDataSetChanged();
        squadList.setAdapter(playerListAdapter);
    }




    @Override
    public void onEditClickListener(int position, String value) {

        RealmResults<Player> results = realm.where(Player.class).
                equalTo("matchid", matchid).
                equalTo("team", team).findAll();

        int playerID = results.get(position).getPlayerID();
//        String playerName = results.get(position).getPlayerName();

        oldName = value;

        AlertDialog.Builder editBuilder = new AlertDialog.Builder(AddSquad.this);
        editBuilder.setIcon(R.drawable.ball);
        editBuilder.setCancelable(false);
        editBuilder.setTitle("Enter new name");

        final EditText input = new EditText(AddSquad.this);

        input.setInputType(InputType.TYPE_CLASS_TEXT);
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


        AlertDialog delAlert = new AlertDialog.Builder(AddSquad.this).create();
        delAlert.setIcon(R.drawable.ball);
        delAlert.setCancelable(false);
        delAlert.setTitle("Delete Player");
        delAlert.setMessage("Press confirm to delete "+value);
        delAlert.setButton(AlertDialog.BUTTON_POSITIVE, "CONFIRM",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();

//                        Log.e("addplayers", "onDeleteClickListener, position : "+position+", i : "+i+", y : "+y);
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



    public void setList(){

        nameList.clear();

         config = new RealmConfiguration.Builder()
                .name(AppConstants.GAME_ID + ".realm")
                .deleteRealmIfMigrationNeeded()
                .build();
        realm = Realm.getInstance(config);
        RealmResults<Player> results = realm.where(Player.class).
                equalTo("matchid", matchid).
                equalTo("team", team).findAll();

        for (Player player : results){

//            nameList.add(player.getPlayerName());
            nameList.add(player);
            ++slno;
        }

        playerListAdapter.notifyDataSetChanged();
        squadList.setAdapter(playerListAdapter);

        next.setEnabled(slno > player_count);   // Added on 19/11/2021
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



    public void  deletePlayer(String playerName, int playerId){

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

            --slno;
        }

        Toast.makeText(getApplicationContext(), "Player "+playerName+" is deleted", Toast.LENGTH_SHORT).show();

        tv_slno.setText(String.valueOf(slno));
        playerListAdapter.notifyDataSetChanged();
        squadList.setAdapter(playerListAdapter);

        next.setEnabled(slno > player_count);   // Added on 19/11/2021
    }



/*  Commented on 19/11/2021
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.add_squad, menu);//Menu Resource, Menu
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.next:
//                addPlayer();
//                if ((slno - 1) >= (player_count + sub)) {
                    if (team == 1) {
                        startActivity(new Intent(AddSquad.this, SelectTeamAXIActivity.class));
                        finish();
                    }
                    else if (team == 2) {
                        startActivity(new Intent(AddSquad.this, SelectTeamBXIActivity.class));
                        finish();
                    }
//                }
//                else {
//                    displayError("Not enough players for match");
//                }
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }

    }*/




    void displayError(String message) {

        AlertDialog alertDialog = new AlertDialog.Builder(AddSquad.this).create();
        alertDialog.setIcon(R.drawable.ball);
        alertDialog.setCancelable(false);
        alertDialog.setTitle(message);
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        alertDialog.show();
    }



    @Override
    protected void onStop() {
        super.onStop();
    }



    @Override
    protected void onPause() {

        super.onPause();
//
//        Realm realm = null;
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
//                    try {
//
//                        Match match = realm.where(Match.class).
//                                equalTo("matchid", matchid).findFirst();
//                        if (match != null) {
//                            if (team == 1) {
//                                match.setPulledMatch(true);
//                                match.setAddSquad(true);
//                                match.setSelectAXI(false);
//                                match.setSelectBXI(false);
//                            }
//                            else if (team == 2) {
//                                match.setPulledMatch(false);
//                                match.setAddSquad(true);
//                                match.setSelectAXI(true);
//                                match.setSelectBXI(false);
//                            }
//                            match.setCreateMatch(false);
//                            match.setAddPlayers(false);
//                            match.setCaptain(false);
//                            match.setToss(false);
//                            match.setOpeners(false);
//                            match.setScoring(false);
//
//                            realm.copyToRealmOrUpdate(match);
//                        }
//                    } catch (RealmPrimaryKeyConstraintException e) {
//                        Toast.makeText(getApplicationContext(), "Primary Key exists, Press Update instead",
//                                Toast.LENGTH_SHORT).show();
//                    }
//                }
//            });
//        }
//
//        catch (RealmException e) {
//            Log.d("toss", "onclick, Exception : " + e);
//        }
//
//        finally {
//            if (realm != null) {
//                realm.close();
//            }
//        }
    }



    @Override
    public void onBackPressed() {
        super.onBackPressed();
//        startActivity(new Intent(AddSquad.this, HomeActivity.class));
//        finish();

        if (team == 1) {
            startActivity(new Intent(AddSquad.this, SelectTeamAXIActivity.class));
            finish();
        }
        else if (team == 2) {
            startActivity(new Intent(AddSquad.this, SelectTeamBXIActivity.class));
            finish();
        }
    }



}
