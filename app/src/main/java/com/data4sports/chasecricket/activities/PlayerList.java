package com.data4sports.chasecricket.activities;

import android.app.ProgressDialog;
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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.data4sports.chasecricket.R;
import com.data4sports.chasecricket.adapters.EditListAdapter;
import com.data4sports.chasecricket.applicationConstants.AppConstants;
import com.data4sports.chasecricket.models.Constants;
import com.data4sports.chasecricket.models.Match;
import com.data4sports.chasecricket.models.MyApplicationClass;
import com.data4sports.chasecricket.models.Player;
import com.data4sports.chasecricket.adapters.EditListAdapter.editButtonListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;
import io.realm.exceptions.RealmException;
import io.realm.exceptions.RealmPrimaryKeyConstraintException;

public class PlayerList extends AppCompatActivity implements editButtonListener {

    Button btn_teamA, btn_teamB;
    TextView teamName, newTextView;
//    TableLayout players_table;
    LinearLayout ll_player_list;
    Realm realm;

    int matchid, playersA, playersB, substitute, i, t = 0, eventId, player_id = 0, flag = 0, checkedItem = -1,
            battingTeamNo = 0, currentInnings = 0;
    String matchID, teamA, teamB, playerName, oldName, message = "";
    boolean score = false, check_substitute = false, cap = false, vc = false, wk = false, none = false,
            SUPER_OVER = false;

    ListView player_list;
//    ArrayList<String> nameList = new ArrayList<String>();
    ArrayList<Player> nameList = new ArrayList<Player>();
    EditListAdapter adapter;

    ImageButton back;
    private ProgressDialog progress;
    RealmConfiguration config;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.player_list);

         config = new RealmConfiguration.Builder()
                .name(AppConstants.GAME_ID + ".realm")
                .deleteRealmIfMigrationNeeded()
                .build();
        realm = Realm.getInstance(config);
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

//                onBackPressed();
//                startActivity(new Intent(PlayerList.this, ScoringActivity.class));  Commented on 28/07/2021
                startActivity(new Intent(PlayerList.this, UpdatedScoringActivity.class));  // 28/07/2021
                finish();
            }
        });


        btn_teamA = findViewById(R.id.btn_team_A);
        btn_teamB = findViewById(R.id.btn_team_B);
        teamName = findViewById(R.id.txt_team);
        ll_player_list = findViewById(R.id.ll_player_list);

        Match match = realm.where(Match.class).equalTo("matchid", matchid).findFirst();
        teamA = match.getTeamA();
        teamB = match.getTeamB();
        playersA = match.getPlayerA();
        playersB = match.getPlayerB();
//        substitute = match.getSubst();

        btn_teamA.setText(teamA);
        btn_teamB.setText(teamB);

        player_list = findViewById(R.id.edit_player_list);
        adapter = new EditListAdapter(PlayerList.this, nameList);
        adapter.setCustomListener(PlayerList.this);
        player_list.setAdapter(adapter);


        btn_teamA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayPlayerTable(teamA);
            }
        });

        btn_teamB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayPlayerTable(teamB);
            }
        });

    }



    void getFromIntent () {

        Intent i = getIntent();
        matchid = i.getIntExtra("matchid", 0);
        matchID = i.getStringExtra("matchID");
        battingTeamNo = i.getIntExtra("battingTeamNo", 0);
        currentInnings = i.getIntExtra("currentInnings", 0);
        SUPER_OVER = i.getBooleanExtra("SUPER_OVER", false);
    }




    public void displayPlayerTable(String team){

//        if (ll_player_list.isShown())
//            player_list.setVisibility(View.GONE);
//            players_table.removeAllViews();

        ll_player_list.setVisibility(View.VISIBLE);
        teamName.setText(team);
        i = 0;
        if (team.matches(teamA)) {
            t = 1;
            btn_teamA.setEnabled(false);
            btn_teamB.setEnabled(true);
        }

        else if (team.matches(teamB)) {
            t = 2;
            btn_teamA.setEnabled(true);
            btn_teamB.setEnabled(false);
        }

        setList(t);
    }







    @Override
    public void onEditButtonClickListener(Player player) {//int position, String name) {

        //commented on 29/04/20
//        AddPlayers addPlayers = new AddPlayers();


        RealmResults<Player> results = realm.where(Player.class).
                equalTo("matchid", matchid).
                equalTo("team", t).findAll();

//        int playerID = results.get(position).getPlayerID();
        int playerID = player.getPlayerID();
//        String playerName = results.get(position).getPlayerName();

        oldName = player.getPlayerName(); //name;

        AlertDialog.Builder editBuilder = new AlertDialog.Builder(PlayerList.this);
        editBuilder.setIcon(R.drawable.ball);
        editBuilder.setCancelable(false);
        editBuilder.setTitle("Enter new name");

        final EditText input = new EditText(PlayerList.this);

//        input.setInputType(InputType.TYPE_CLASS_TEXT);
        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_WORDS);
        input.setPadding(20, 15, 20, 15);
        input.setText( player.getPlayerName()); //name);
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

                        if (checkName(playerName, t)){
                            dialog.cancel();
//                            displayError("Player already exists");
                            AlertDialog alertDialog = new AlertDialog.Builder(PlayerList.this).create();
                            alertDialog.setIcon(R.drawable.ball);
                            alertDialog.setCancelable(false);
                            alertDialog.setTitle("Warning");
                            alertDialog.setMessage("Player name already existing. Do you want to proceed");
                            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Proceed",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
//                                            addingPlayers(playerName);
                                            addingPlayers(playerID);
                                            setList(t);
                                            postJSON(matchid, matchID, t, playerID, true);
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
                            // commented on 29/04/20
//                            addPlayers.saveToDB(matchid, playerID, oldName, playerName);

                            addingPlayers(playerID);
                            setList(t);
                            postJSON(matchid, matchID, t, playerID, true);

//                            displayProgress();

                        }




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



    public void addingPlayers(int playerID) {

        AddPlayersA addPlayersA = new AddPlayersA();
        AddPlayersB addPlayersB = new AddPlayersB();

        if (t == 1)
            addPlayersA.saveToDB(matchid, playerID, oldName, playerName, true);
        else if (t == 2)
            addPlayersB.saveToDB(matchid, playerID, oldName, playerName, true);
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
        AlertDialog alertDialog = new AlertDialog.Builder(PlayerList.this).create();
        alertDialog.setIcon(R.drawable.ball);
        alertDialog.setCancelable(false);
        alertDialog.setTitle(message);
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();

                    }
                });
        alertDialog.show();
    }




    public void setList(int team){


        nameList.clear();

         config = new RealmConfiguration.Builder()
                .name(AppConstants.GAME_ID + ".realm")
                .deleteRealmIfMigrationNeeded()
                .build();
        realm = Realm.getInstance(config);
        RealmResults<Player> results = realm.where(Player.class).
                equalTo("matchid", matchid).
                equalTo("team", team).findAll();

        //            nameList.add(player.getPlayerName());
        nameList.addAll(results);

        adapter.notifyDataSetChanged();
        player_list.setAdapter(adapter);
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
                addPlayer();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }

    }


    // for adding new player

    private void addPlayer() {



        if (t == 0) {

            displayError("Please select a team");
        }

        else {

            // new code --> 26/02/2020

            if (t == 1)
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
            /* COMMENTED ON 01/03/2021
            // add checkbox in AlertDialog
            CheckBox checkBox = (CheckBox) checkBoxView.findViewById(R.id.checkbox);
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

                                displayError("Invalid player name");
//                                Toast.makeText(PlayerList.this, "Invalid player name", Toast.LENGTH_SHORT).show();
                            }

                            else {

                                if (checkName(editText.getText().toString(), t)) {
//                                    displayError("Player already exists");
                                    AlertDialog alertDialog = new AlertDialog.Builder(PlayerList.this).create();
                                    alertDialog.setIcon(R.drawable.ball);
                                    alertDialog.setCancelable(false);
                                    alertDialog.setTitle("Warning");
                                    alertDialog.setMessage("Player name already existing. Do you want to proceed");
                                    alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Proceed",
                                            new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int which) {
                                                    dialog.dismiss();
                                                    saveToDB(editText.getText().toString());
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

                                    saveToDB(editText.getText().toString());
                                }

                            }

                           /* Uri uri = Uri.parse("market://details?id=MY_APP_PACKAGE");
                            Intent intent = new Intent (Intent.ACTION_VIEW, uri);
                            startActivity(intent);*/

                        }
                    })
                    .setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    }).show();

        }

    }



    public void saveToDB(String playerName) {

        Number num = realm.where(Player.class).max("playerID");
        player_id = (num == null) ? 1 : num.intValue() + 1;

        Log.d("newplayer", "name : " + playerName + ", player_id : " + player_id);
//        Log.d("newplayer", "check_substitute : " + check_substitute + ", flag : " + flag);
//        if (check_substitute) {     COMMENTED ON 01/03/2021
//                                    displayProgress();
            flag = 1;
            savePlayerDetails(playerName, player_id, t, flag, false, false, false);
            setList(t);

       /* COMMENTED ON 01/03/2021
        } else {
            flag = 0;
            checkPlayerStatus(playerName, player_id, t, flag);
        }*/
    }



    // commented

//
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.playerlist_to_scoring, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
////        int id = item.getItemId();
////        switch (id){
////            case R.id.ww_ok:
//        if (item.getItemId() ==  R.id.plist_back) {
//
//            Intent intent = new Intent(PlayerList.this, ScoringActivity.class);
//            intent.putExtra("status", "resume");
//            intent.putExtra("eventId", eventId);
//            intent.putExtra("score", score);
//            startActivity(intent);
//
//        }
//        return true;
//    }



    private void postJSON(int matchid, String matchID, int team, int player_id, boolean edit) {

        String captain = null, vice_captain = null, wicketkeeper = null;

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

                JSONObject playerobject= new JSONObject();
                JSONObject teamobject= new JSONObject();


                JSONObject jsonTeamB = new JSONObject();
                try {

                    if (edit) {
                        if (player.isSubstitute()) {
                            jsonTeamB.put("players", subarray);
                            jsonTeamB.put("substitutes", arrayPlayerB);
                        } else {
                            jsonTeamB.put("substitutes", subarray);
                            jsonTeamB.put("players", arrayPlayerB);
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
                            captain = player111.getPlayerName();
                        else
                        if (player111.isViceCaptain())
                            vice_captain = player111.getPlayerName();

                        if (player111.isWicketKeeper())
                            wicketkeeper = player111.getPlayerName();
                    }

                    jsonTeamB.put("captain", captain);
                    jsonTeamB.put("vice captain", vice_captain);
                    jsonTeamB.put("wicketkeeper", wicketkeeper);


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
                    if (edit)
                        json_temp.put("substitutes",subarray1);//subarray);

                    jsonA.put("matchID", matchID);

                    /*if (player.getTeam() == 1) {
                        jsonB.put("TeamA", jsonTeamB);
                    }
                    else {
                        jsonB.put("TeamB", jsonTeamB);
                    }*/

                    // updated (put edit cndition) on 12/09/2020
                    if (edit) {
                        if (player.getTeam() == 1) {
                            jsonB.put("TeamA", jsonTeamB);
                            jsonB.put("TeamB", json_temp);
//                        jsonB.put("TeamB", teamobject);
                        } else {
//                        jsonB.put("TeamA", teamobject);
                            jsonB.put("TeamA", json_temp);
                            jsonB.put("TeamB", jsonTeamB);
                        }
                    }

                    else {
                        if (player.getTeam() == 1) {
                            jsonB.put("TeamA", jsonTeamB);
                        }
                        else {
                            jsonB.put("TeamB", jsonTeamB);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                JSONArray array = new JSONArray();
                array.put(jsonA);
                array.put(jsonB);

                JSONObject jsonfeed = new JSONObject();
                try {
                    if (edit) {
//                        jsonfeed.put("EditPlayers", array);
//                        if (team == 1) {
                            if (player.isSubstitute())
                                jsonfeed.put("EditPlayer   Substitute", array);
                            else
                                jsonfeed.put("EditPlayers", array);
//                        }
//                        else if (team == 2) {
//                            if (player.isSubstitute())
//                                jsonfeed.put("EditTeamBPlayerSubstitute", array);
//                            else
//                                jsonfeed.put("EditPlayers", array);
//                        }
                    }
                    else {
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

                    }
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
                                                           Log.d("PlayerList", "player not found, playerid : " +
                                                                   response.getInt("playerid"));
                                                        }

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
                                            Log.d("PlayerList", "Exception : " + e);
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
                                Log.e("PlayerList", "Error Message is  : " + error.getMessage());

                            }
                        });

                MyApplicationClass.getInstance(getApplicationContext()).addToRequestQueue(jsonObjReq, "postRequest");
                Log.d("PlayerList", "jsonObjReq  : " + jsonObjReq);
                Log.d("PlayerList", "postparams  : " + postparams);
            }

            else {
                Log.d("PlayerList", "player : " + player);
            }
        }
    }


    private boolean isNetworkAvailable(){
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }



    public void checkPlayerStatus(String name, int player_id, int t, int flag) {

        AlertDialog.Builder builder = new AlertDialog.Builder(PlayerList.this);
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

//                if (cap) {

//                displayProgress();
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

                    AlertDialog alertDialog = new AlertDialog.Builder(PlayerList.this).create();
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
                                    setList(t);
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

                else {
                    savePlayerDetails(name, player_id, t, flag, cap, vc, wk);
                    setList(t);
                }
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

                        RealmResults<Player> results = bgRealm.where(Player.class).
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
                            bgRealm.copyFromRealm(player);
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
            Log.d("PlayerList", "Exception : " + e);
        } finally {
            if (realm != null) {
                realm.close();
            }
        }


    }



    // save player's details to local database

    public void savePlayerDetails(final String player_name, int player_id, int team, int flag, boolean cap, boolean vc, boolean wk){

//        displayProgress();
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

                        Player player = new Player();
                        player.setPlayerID(player_id);
                        player.setPlayerName(player_name);
                        player.setMatchid(matchid);
                        player.setMatchID(matchID);
                        player.setTeam(team);
                        Log.d("savePlayerDetails", "name : "+player_name+
                                ", playerid : "+player_id+", matchid : "+matchid+
                                ", team : " +team+", flag : "+flag);

                        player.setSubstitute(true);
                        player.setPlaying(false);

                        player.setRetired(false);
                        player.setRetired_concussion(false);

                        realm.copyToRealm(player);
                        Log.d("newplayer", "matchid : " + matchid + "  player : " + player.toString());

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
        }
        finally {

            if (realm != null) {
                realm.close();
            }
        }

        postJSON(matchid, matchID, team, player_id, false);

    }


    public void displayProgress(){

        progress = new ProgressDialog(this);
        progress.setMessage("Please wait...");
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setIndeterminate(true);
        progress.setProgress(0);
        progress.show();
    }
}
