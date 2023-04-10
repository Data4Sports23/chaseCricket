package com.data4sports.chasecricket.activities;


import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;

import com.data4sports.chasecricket.R;
import com.data4sports.chasecricket.adapters.MatchListAdapter;
import com.data4sports.chasecricket.adapters.MatchListAdapter.customButtonListener;

import android.widget.Toast;

import com.data4sports.chasecricket.applicationConstants.AppConstants;
import com.data4sports.chasecricket.models.Batsman;
import com.data4sports.chasecricket.models.Bowler;
import com.data4sports.chasecricket.models.Events;
import com.data4sports.chasecricket.models.ExtraCard;
import com.data4sports.chasecricket.models.FOW;
import com.data4sports.chasecricket.models.Match;
import com.data4sports.chasecricket.models.MatchOfficials;
import com.data4sports.chasecricket.models.Openers;
import com.data4sports.chasecricket.models.Partnership;
import com.data4sports.chasecricket.models.Penalty;
import com.data4sports.chasecricket.models.Player;
import com.data4sports.chasecricket.models.Power;
import com.data4sports.chasecricket.models.Substitution;
import com.data4sports.chasecricket.models.Undo;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;


public class MatchListActivity extends AppCompatActivity implements customButtonListener {

    Realm realm;
    //    ListView matchList;
//    TextView tv_teamA, tv_teamB, tv_slno;
    Button view, resume;
    String teamA = null, teamB = null, tossWinner = null, decision = null, bTeam = null,
            fTeam = null, mType = null, inngs = null;
    int userId, bTeamNo = 0, fTeamNo = 0, c_innings = 0, t_innings = 0;
//    int slno = 1,i = 0, currentInnings = 0;

    MatchListAdapter adapter;
    //    ArrayList<String> dataTeamA = new ArrayList<String>();
//    ArrayList<String> dataTeamB = new ArrayList<String>();
    ArrayList<String> dataTeams = new ArrayList<String>();
    ArrayList<JSONObject> jsonList = new ArrayList<JSONObject>();

    SharedPreferences mPreferences;
    SharedPreferences.Editor mEditor;
    ImageButton back;
    ListView listView;
    private ProgressDialog progress;

    RealmConfiguration config;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_match_list);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.custom_action_bar_layout);
        View view = getSupportActionBar().getCustomView();

        back = (ImageButton) view.findViewById(R.id.action_bar_back);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*finish();*/
                Log.e("scoring", "oncreate, back button pressd");

                onBackPressed();
            }
        });

        Log.d("TAG", "onCreate: App Constants " + AppConstants.GAME_ID);
        // Realm.init(this);
        config = new RealmConfiguration.Builder()
                .name(AppConstants.GAME_ID + ".realm")
                .deleteRealmIfMigrationNeeded()
                .build();
        realm = Realm.getInstance(config);
//        displayProgress();

//         Commented on 26/11/2021
        RealmResults<Match> result = realm.where(Match.class).findAll();
        result.load();
//        if (result != null) {

        long count = realm.where(Match.class).count();
        Log.d("match", "count : " + count);

        for (Match match : result) {

//            teamA[i] = match.getTeamA();
//            teamB[i] = match.getTeamB();
            dataTeams.add(match.getTeamA() + " vs " + match.getTeamB());
            // Added on 28/07/2021
            JSONObject json = new JSONObject();
            try {
                json.put("teamA", match.getTeamA());
                json.put("teamB", match.getTeamB());
                json.put("matchId", match.getD4s_matchid());
                if (match.getMatchType().matches("Custom"))
                    json.put("type", match.getActual_over() + " over");
                else
                    json.put("type", match.getMatchType());
                json.put("date", match.getDate());
            } catch (JSONException e) {
                e.printStackTrace();
            }

            jsonList.add(json);


        }


        if (realm != null) {
            realm.close();
        }

        mPreferences = PreferenceManager
                .getDefaultSharedPreferences(this);
        userId = mPreferences.getInt("userId", 0);

//        }

//        result.load();


//        String[] teamA = getResources().getStringArray(R.array.listdata);

//        List<String> dataTempA = Arrays.asList(teamA);
//        dataTeamA.addAll(dataTempA);
//        List<String> dataTempB = Arrays.asList(teamB);
//        dataTeamB.addAll(dataTempB);
//        List<String> dataTemp = Arrays.asList(teams);
//        dataTeams.addAll(dataTemp);
        listView = (ListView) findViewById(R.id.matchlistView);
//        adapter = new MatchListAdapter(MatchListActivity.this, dataTeamA, dataTeamB);

//        adapter = new MatchListAdapter(MatchListActivity.this, dataTeams);    // Commented on 28/07/2021
        adapter = new MatchListAdapter(MatchListActivity.this, jsonList);// Updated on 28/07/2021
        adapter.setCustomButtonListner(MatchListActivity.this);
        listView.setAdapter(adapter);

      /*  mPreferences = PreferenceManager
                .getDefaultSharedPreferences(this);
        userId = mPreferences.getInt("userId", 0);
        listView = (ListView) findViewById(R.id.matchlistView);
//        displayList();  // Added on 26/11/2021

        RealmResults<Match> result = realm.where(Match.class).findAll();
        result.load();

        long count = realm.where(Match.class).count();
        Log.d("match", "count : " + count);

        for (Match match : result) {

            dataTeams.add(match.getTeamA() + " vs " + match.getTeamB());
            JSONObject json = new JSONObject();
            try {
                json.put("teamA", match.getTeamA());
                json.put("teamB", match.getTeamB());
                if (match.getMatchType().matches("Custom"))
                    json.put("type", match.getActual_over() + " over");
                else
                    json.put("type", match.getMatchType());
                json.put("date", match.getDate());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            jsonList.add(json);

        }

        if (realm != null) {
            realm.close();
        }

        adapter = new MatchListAdapter(MatchListActivity.this, jsonList);// Updated on 28/07/2021
        adapter.setCustomButtonListner(MatchListActivity.this);*/


    }


    @Override
    public void onViewButtonClickListener(int position, String value) {

        displayProgress();
        config = new RealmConfiguration.Builder()
                .name(AppConstants.GAME_ID + ".realm")
                .deleteRealmIfMigrationNeeded()
                .build();
        realm = Realm.getInstance(config);
        RealmResults<Match> result = realm.where(Match.class).findAll();
        int matchid = result.get(position).getMatchid();
        String matchID = result.get(position).getMatchID();
        Match match = realm.where(Match.class).equalTo("matchid", matchid).findFirst();
        Log.d("MatchList", "view btn click, match : " + match);

//        result.load();
//        Toast.makeText(MatchListActivity.this, "Button click " + matchid,
//                Toast.LENGTH_SHORT).show();

/*        SharedPreferences sharedPreferences  = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putInt("sp_match_id", matchid);
        editor.putString("sp_match_ID", matchID);
        editor.putString("sp_teamA", match.getTeamA());
        editor.putString("sp_teamB", match.getTeamB());
        editor.putString("sp_innings", match.getInnings());*/

//        Events events = realm.where(Events.class).equalTo("matchid", matchid).findAll().last();
        RealmResults<Events> event_results = realm.where(Events.class).equalTo("matchid", matchid).findAll();
        if (event_results.size() > 0) {
            Events events = event_results.last();
            Log.d("MatchList", "view btn click, events : " + events);

            if (events != null) {

                int status = realm.where(Match.class).equalTo("matchid", matchid).findFirst().getMatchStatus();
                Intent i = new Intent(MatchListActivity.this, ScoreCardActivity.class);
                i.putExtra("matchid", matchid);
                i.putExtra("endofinnings", false);
                i.putExtra("innings", events.getInnings());
                i.putExtra("matchstatus", status);
                startActivity(i);
                progress.dismiss();
                finish();

/*            int battingTeamNo = events.getBattingTeamNo();
            if (battingTeamNo == 1) {

                editor.putString("sp_batting_team", match.getTeamA());
                editor.putString("sp_fielding_team", match.getTeamB());
            } else {

                editor.putString("sp_batting_team", match.getTeamB());
                editor.putString("sp_fielding_team", match.getTeamA());
            }

            editor.putString("sp_striker", events.getStriker());
            editor.putString("sp_non_striker", events.getNonStriker());
            editor.putString("sp_bowler", events.getBowler());
            editor.putInt("sp_pre_bowler", events.getPrevBowlerId());
            editor.putInt("sp_bowler_id", events.getBowlerId());
            editor.putString("sp_match_type", match.getMatchType());
            editor.putInt("sp_over", match.getOver());
            editor.putInt("sp_balls", match.getBalls());
            editor.putInt("sp_current_innings", events.getInnings());
            editor.putInt("sp_noball_value", match.getNoballrun());
            editor.putInt("sp_wide_value", match.getWiderun());
            editor.putInt("sp_penalty_value", match.getPenaltyrun());

            editor.apply();*/
            }


        } else {

            progress.dismiss();
            Toast.makeText(getApplicationContext(), "This match doesn't started yet", Toast.LENGTH_SHORT).show();
        }


    }

    @Override
    public void onResumeButtonClickListener(int position, String value) {

        displayProgress();
        config = new RealmConfiguration.Builder()
                .name(AppConstants.GAME_ID + ".realm")
                .deleteRealmIfMigrationNeeded()
                .build();
        realm = Realm.getInstance(config);
        RealmResults<Match> result = realm.where(Match.class).findAll();
        int matchid = result.get(position).getMatchid();
        String matchID = result.get(position).getMatchID();
        Match match = realm.where(Match.class).equalTo("matchid", matchid).findFirst();
        int matchStatus = match.getMatchStatus();
        Log.d("matchlist", "resume,  matchid : " + match);

        tossWinner = match.getToss_winner();
        decision = match.getDecision();
        teamA = match.getTeamA();
        teamB = match.getTeamB();
        int innings = 0;

        mType = match.getMatchType();
        inngs = match.getInnings();
        Log.d("MatchList", "Resume, mType : " + mType);
        Log.d("MatchList", "Resume, inngs : " + inngs);
        if (mType != null && inngs != null) {

            if (mType.matches("T20") || mType.matches("ODI") || inngs.matches("single"))
                t_innings = 2;
            else if (mType.matches("Test") || inngs.matches("multi"))
                t_innings = 4;
        }

//        result.load();
//        Toast.makeText(MatchListActivity.this, "Button click " + matchid,
//                Toast.LENGTH_SHORT).show();
        Log.d("MLA", "matchStatus = " + matchStatus);
        if (matchStatus == 0) {

            /*//Added on 03/09/2021

            if ((match.getAbandoned_match_flag() == 1) || (match.getAbandoned_after_toss_match_flag() == 1)) {

                Intent i = new Intent(MatchListActivity.this, DisplayResultActivity.class);
                i.putExtra("matchid", matchid);
                i.putExtra("matchID", matchID);
                startActivity(i);
            }

            // till here
             else*/
            if (match.isScoreCard() && match.isEndOfInnings()) {

                Events events = realm.where(Events.class).
                        equalTo("matchid", matchid).
                        notEqualTo("ballType", 13).findAll().last();

                Log.d("MatchList", "Resume, t_innings : " + t_innings);
                Log.d("MatchList", "Resume, events : " + events);
//                Log.d("MatchList", "Resume, events1 : " + events1);

                if (events != null) {

                    if (events.getBallType() == 9 || events.getBallType() == 10 || events.getBallType() == 26) {

                        Intent i = new Intent(MatchListActivity.this, ScoreCardActivity.class);
                        i.putExtra("matchid", matchid);
                        i.putExtra("endofinnings", match.isEndOfInnings());
                        i.putExtra("interval", false);
                        i.putExtra("session", false);
                        i.putExtra("declare", events.isDeclared());
                        i.putExtra("forfeit", events.isForfeit());
                        i.putExtra("forfeit_team", events.getForfeit_team());
                        i.putExtra("forfeit_innings", events.getForfeit_innings());
                        i.putExtra("innings", events.getInnings());
                        i.putExtra("matchstatus", match.getStatus());
                        i.putExtra("inningsnotstarted", false);
                        i.putExtra("SUPER_OVER", match.isSUPER_OVER());
                        Log.d("displayScoreCard", " SUPER_OVER :" + match.isSUPER_OVER());
                        /*if (events.isEndOfDay() ||  || match.isEndOfInnings() || endOfMatch || allOUT)//SUPER_OVER)
                            i.putExtra("back_type", 2);
                        else*/
                        i.putExtra("back_type", 1);
                        startActivity(i);
                        progress.dismiss();
                        finish();
                    }
                }
            } else if (match.isScoring()) {

                Log.d("MatchList", "Resume, match.isScoring() : " + match.isScoring());

                RealmResults<Events> results = realm.where(Events.class).
                        equalTo("matchid", matchid).findAll();

                Log.d("MatchList", "Resume, results : " + results);

                if (results.isEmpty()) {

                    newInnings(match, 1);
                } else {

                    Events events = results.last();

                        /* Events events = realm.where(Events.class).
                            equalTo("matchid", matchid).findAll().last();*/

                    Log.d("MatchList", "Resume, events : " + events);


                        /*if (events == null) {

                            newInnings(match, 1);
                        }

                        else {*/

                    if (events.getBallType() == 13) {

                        Toast.makeText(MatchListActivity.this, "Resume the match",
                                Toast.LENGTH_SHORT).show();

                        mEditor = mPreferences.edit();
                        mEditor.putString("sp_status", "resume");
                        mEditor.putInt("sp_match_id", matchid);
                        mEditor.putString("sp_match_ID", matchID);
                        mEditor.putString("sp_teamA", teamA);
                        mEditor.putString("sp_teamB", teamB);
                        mEditor.apply();
//                            Intent i = new Intent(MatchListActivity.this, ScoringActivity.class);  Commented on 26/07/2021
                        Intent i = new Intent(MatchListActivity.this, UpdatedScoringActivity.class);
                        // i.putExtra("midscore", true);
                        i.putExtra("matchid", matchid);
                        i.putExtra("matchID", matchID);
                        //                        i.putExtra("status", "resume");
                        progress.dismiss();
                        startActivity(i);
                    } else {

                            /*Events events = realm.where(Events.class).
                                    equalTo("matchid", matchid).notEqualTo("ballType", 13).findAll().last();*/

                        Log.d("MatchList", "Resume, t_innings : " + t_innings);
                        Log.d("MatchList", "Resume, events : " + events);
//                Log.d("MatchList", "Resume, events1 : " + events1);

//                    if (events != null) {

                        if (events.getBallType() != 9 && events.getBallType() != 10 && events.getBallType() != 26) {

                            Toast.makeText(MatchListActivity.this, "Resume the match",
                                    Toast.LENGTH_SHORT).show();

                            mEditor = mPreferences.edit();
                            mEditor.putString("sp_status", "resume");
                            mEditor.putInt("sp_match_id", matchid);
                            mEditor.putString("sp_match_ID", matchID);
                            mEditor.putString("sp_teamA", teamA);
                            mEditor.putString("sp_teamB", teamB);
                            mEditor.apply();
//                                Intent i = new Intent(MatchListActivity.this, ScoringActivity.class);  Commented on 26/07/2021
                            Intent i = new Intent(MatchListActivity.this, UpdatedScoringActivity.class);
                            //i.putExtra("midscore", true);
                            i.putExtra("matchid", matchid);
                            i.putExtra("matchID", matchID);
//                        i.putExtra("status", "resume");
                            progress.dismiss();
                            startActivity(i);
                        } else {

                            if (events.getInnings() == 1) {
                                if (events.getBallType() == 9 || events.getBallType() == 10 || events.getBallType() == 26) {
                                    newInnings(match, 2);
//                                displayScorecard(match, 1);
                                } else {

                                    Toast.makeText(MatchListActivity.this, "Resume the match",
                                            Toast.LENGTH_SHORT).show();
//                                        Intent i = new Intent(MatchListActivity.this, ScoringActivity.class);  Commented on 26/07/2021
                                    Intent i = new Intent(MatchListActivity.this, UpdatedScoringActivity.class);
                                    //i.putExtra("midscore", true);
                                    i.putExtra("matchid", matchid);
                                    i.putExtra("matchID", matchID);
                                    i.putExtra("status", "resume");
                                    progress.dismiss();
                                    startActivity(i);
                                }

                            } else if (events.getInnings() < t_innings) {

                                if (events.getInnings() == 2 &&
                                        (events.getBallType() == 9 || events.getBallType() == 10 || events.getBallType() == 26))
                                    newInnings(match, 3);

                                else if (events.getInnings() == 3 &&
                                        (events.getBallType() == 9 || events.getBallType() == 10 || events.getBallType() == 26))
                                    newInnings(match, 4);

                            } else if (events.getInnings() == t_innings) {

                                Intent i = new Intent(MatchListActivity.this, ScoreCardActivity.class);
                                i.putExtra("matchid", matchid);
                                i.putExtra("matchID", matchID);
                                i.putExtra("innings", 1);
                                i.putExtra("back_type", 2);
                                i.putExtra("endofinnings", true);
                                i.putExtra("endofmatch", true);
                                i.putExtra("match_finished", true);
                                i.putExtra("continue_match", false);

//                        i.putExtra("status", "resume");
                                progress.dismiss();
                                startActivity(i);

                           /* AlertDialog messageAlert = new AlertDialog.
                                    Builder(MatchListActivity.this).create();
                            messageAlert.setIcon(R.drawable.ball);
                            messageAlert.setCancelable(false);
                            messageAlert.setMessage("Match is already over");
                            messageAlert.setButton(AlertDialog.BUTTON_POSITIVE, "OK",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                        }

                                    });

                            messageAlert.show();*/
                            } else if (events.getInnings() == 99) {
                                if (events.getBallType() == 10)
                                    newInnings(match, 99);
                            } else if (events.getInnings() == 100) {
                                if (events.getBallType() == 10)
                                    newInnings(match, 100);
                            }

                        }
//                        newInnings(match, 2);

//                    Events lastEvents = realm.where(Events.class).
//                            equalTo("matchid", matchid).
//                            equalTo("", events+1).findAll().last();

                    /*} else {

                        newInnings(match, 1);
                    }*/


                    }


                }


                    /*else {

                        newInnings(match, 1);
                    }*/


            } else if (match.isOpeners()) {

                Log.d("MatchList", "Resume, match.isOpeners() : " + match.isOpeners());

                mEditor = mPreferences.edit();

                mEditor.putInt("sp_match_id", matchid);
                mEditor.putString("sp_match_ID", matchID);
                mEditor.putBoolean("SUPER_OVER", match.isSUPER_OVER());
                mEditor.apply();

                if (teamA != null && teamB != null && tossWinner != null && decision != null) {

                    if (tossWinner.matches(teamA)) {

                        if (decision.matches("Batting")) {

                            bTeam = teamA;
                            fTeam = teamB;
                            bTeamNo = 1;
                            fTeamNo = 2;
                        } else if (decision.matches("Fielding")) {

                            bTeam = teamB;
                            fTeam = teamA;
                            bTeamNo = 2;
                            fTeamNo = 1;
                        }
                    } else if (tossWinner.matches(teamB)) {

                        if (decision.matches("Batting")) {

                            bTeam = teamB;
                            fTeam = teamA;
                            bTeamNo = 2;
                            fTeamNo = 1;
                        } else if (decision.matches("Fielding")) {

                            bTeam = teamA;
                            fTeam = teamB;
                            bTeamNo = 1;
                            fTeamNo = 2;
                        }
                    }
                }

                if (match.isSUPER_OVER()) {

                    Events events = realm.where(Events.class).
                            equalTo("matchid", matchid).
                            equalTo("matchID", matchID).findAll().last();
                    innings = events.getInnings();
                }


                Intent intent = new Intent(MatchListActivity.this, OpenersActivity.class);
                intent.putExtra("batting_team", bTeam);
                intent.putExtra("batting_team_no", bTeamNo);
                intent.putExtra("fielding_team", fTeam);
                intent.putExtra("fielding_team_no", fTeamNo);
                intent.putExtra("super_over_innings", innings);
                progress.dismiss();
                startActivity(intent);
                finish();
            } else if (match.isCaptain()) {

                Log.d("MatchList", "Resume, match.isCaptain() : " + match.isCaptain());

                mEditor = mPreferences.edit();
                mEditor.putInt("sp_match_id", matchid);
                mEditor.putString("sp_match_ID", matchID);
                mEditor.putString("sp_teamA", teamA);
                mEditor.putString("sp_teamB", teamB);
                mEditor.apply();
                startActivity(new Intent(MatchListActivity.this, CaptainActivity.class));
                progress.dismiss();
                finish();
            } else if (match.isSelectBXI()) {

                mEditor = mPreferences.edit();

                mEditor.putInt("sp_match_id", matchid);
                mEditor.putString("sp_match_ID", matchID);
                mEditor.putString("sp_teamA", teamA);
                mEditor.putString("sp_teamB", teamB);
                mEditor.putInt("sp_playerA", match.getPlayerA());
                mEditor.putInt("sp_playerB", match.getPlayerB());
                mEditor.apply();
                startActivity(new Intent(MatchListActivity.this, SelectTeamBXIActivity.class));
                progress.dismiss();
                finish();
            } else if (match.isSelectAXI()) {// && !(match.isAddSquad())) {

                mEditor = mPreferences.edit();

                mEditor.putInt("sp_match_id", matchid);
                mEditor.putString("sp_match_ID", matchID);
                mEditor.putString("sp_teamA", teamA);
                mEditor.putString("sp_teamB", teamB);
                mEditor.putInt("sp_playerA", match.getPlayerA());
                mEditor.putInt("sp_playerB", match.getPlayerB());
//                mEditor.putInt("sp_player_count", match.getPlayer());
//                mEditor.putInt("sp_sub_seq", match.getSubst());
                mEditor.apply();
                startActivity(new Intent(MatchListActivity.this, SelectTeamAXIActivity.class));
                progress.dismiss();
                finish();
            } else if (match.isToss()) {

                Log.d("MatchList", "Resume, match.isToss() : " + match.isToss());

                mEditor = mPreferences.edit();

                mEditor.putInt("sp_match_id", matchid);
                mEditor.putString("sp_match_ID", matchID);
                mEditor.putString("sp_teamA", teamA);
                mEditor.putString("sp_teamB", teamB);
                mEditor.putString("sp_innings", match.getMatchType());
//                mEditor.putString("user_token", );
                mEditor.apply();

                startActivity(new Intent(MatchListActivity.this, TossActivity.class));
                progress.dismiss();
                finish();
            }


            // added on 29/04/20
            else if (match.isAddPlayersB()) {

                mEditor = mPreferences.edit();

                mEditor.putInt("sp_match_id", matchid);
                mEditor.putString("sp_match_ID", matchID);
                mEditor.putString("sp_teamA", teamA);
                mEditor.putString("sp_teamB", teamB);
                mEditor.putInt("sp_playerA", match.getPlayerA());
                mEditor.putInt("sp_playerB", match.getPlayerB());
//                mEditor.putInt("sp_player_count", match.getPlayer());
//                mEditor.putInt("sp_sub_seq", match.getSubst());
                mEditor.apply();
                startActivity(new Intent(MatchListActivity.this, AddPlayersB.class));
                progress.dismiss();
                finish();
            }

            // added on 28/04/20
            else if (match.isAddPlayersA()) {

                mEditor = mPreferences.edit();

                mEditor.putInt("sp_match_id", matchid);
                mEditor.putString("sp_match_ID", matchID);
                mEditor.putString("sp_teamA", teamA);
                mEditor.putString("sp_teamB", teamB);
                mEditor.putInt("sp_playerA", match.getPlayerA());
                mEditor.putInt("sp_playerB", match.getPlayerB());
//                mEditor.putInt("sp_sub_seq", match.getSubst());
                mEditor.apply();
                startActivity(new Intent(MatchListActivity.this, AddPlayersA.class));
                progress.dismiss();
                finish();
            }




            /*else if (match.isAddPlayers()){

                Log.d("MatchList", "Resume, match.isAddPlayers() : " + match.isAddPlayers());

                mEditor = mPreferences.edit();

                mEditor.putInt("sp_match_id", matchid);
                mEditor.putString("sp_match_ID", matchID);
                mEditor.putString("sp_teamA", teamA);
                mEditor.putString("sp_teamB", teamB);
                mEditor.putInt("sp_player_count", match.getPlayer());
                mEditor.putInt("sp_sub_seq", match.getSubst());
                mEditor.apply();
                startActivity(new Intent(MatchListActivity.this, AddPlayers.class));
                progress.dismiss();
                finish();
            }*/


          /*  else if (match.isPulledMatch() && match.isAddSquad()) {

                mEditor = mPreferences.edit();

                mEditor.putInt("sp_match_id", matchid);
                mEditor.putString("sp_match_ID", matchID);
                mEditor.putString("sp_team", teamA);
                mEditor.putInt("sp_team_id", 1);
                mEditor.putInt("sp_player_count", match.getPlayer());
                mEditor.putInt("sp_sub_seq", match.getSubst());
                mEditor.apply();
                startActivity(new Intent(MatchListActivity.this, AddSquad.class));
                progress.dismiss();
                finish();
            }


            else if (match.isAddSquad() && match.isSelectAXI()) {

                mEditor = mPreferences.edit();

                mEditor.putInt("sp_match_id", matchid);
                mEditor.putString("sp_match_ID", matchID);
                mEditor.putString("sp_team", teamB);
                mEditor.putInt("sp_team_id", 2);
                mEditor.putInt("sp_player_count", match.getPlayer());
                mEditor.putInt("sp_sub_seq", match.getSubst());
                mEditor.apply();
                startActivity(new Intent(MatchListActivity.this, AddSquad.class));
                progress.dismiss();
                finish();
            }*/

           /* else if (match.isSelectAXI()) {// && !(match.isAddSquad())) {

                mEditor = mPreferences.edit();

                mEditor.putInt("sp_match_id", matchid);
                mEditor.putString("sp_match_ID", matchID);
                mEditor.putString("sp_teamA", teamA);
                mEditor.putString("sp_teamB", teamB);
                mEditor.putInt("sp_playerA", match.getPlayerA());
                mEditor.putInt("sp_playerB", match.getPlayerB());
//                mEditor.putInt("sp_player_count", match.getPlayer());
//                mEditor.putInt("sp_sub_seq", match.getSubst());
                mEditor.apply();
                startActivity(new Intent(MatchListActivity.this, SelectTeamAXIActivity.class));
                progress.dismiss();
                finish();
            }


            if (match.isSelectBXI()) {

                mEditor = mPreferences.edit();

                mEditor.putInt("sp_match_id", matchid);
                mEditor.putString("sp_match_ID", matchID);
                mEditor.putString("sp_teamA", teamA);
                mEditor.putString("sp_teamB", teamB);
                mEditor.putInt("sp_playerA", match.getPlayerA());
                mEditor.putInt("sp_playerB", match.getPlayerB());
                mEditor.apply();
                startActivity(new Intent(MatchListActivity.this, SelectTeamBXIActivity.class));
                progress.dismiss();
                finish();
            }*/
        } else {

            Log.d("MLA", "else, match.getAbandoned_match_flag() = " + match.getAbandoned_match_flag());
            Log.d("MLA", "else, match.getAbandoned_after_toss_match_flag() = " + match.getAbandoned_after_toss_match_flag());

            //Added on 03/09/2021

            if ((match.getAbandoned_match_flag() == 1) || (match.getAbandoned_after_toss_match_flag() == 1)) {

                Log.d("MLA", "else 1");

                Intent i = new Intent(MatchListActivity.this, DisplayResultActivity.class);
                i.putExtra("matchid", matchid);
                i.putExtra("matchID", matchID);
                progress.dismiss();
                startActivity(i);
            } else {   // till here

                Log.d("MLA", "else 2");

                Intent i = new Intent(MatchListActivity.this, ScoreCardActivity.class);
                i.putExtra("matchid", matchid);
                i.putExtra("matchID", matchID);
                if (match.getInnings4Runs() > 0)
                    c_innings = 4;
                else if (match.getInnings3Runs() > 0)
                    c_innings = 3;
                else if (match.getInnings2Runs() > 0)
                    c_innings = 2;
                else if (match.getInnings1Runs() > 0)
                    c_innings = 1;

                if (match.isSUPER_OVER()) {
                    if (match.getSuper_over_innings2runs() > 0)
                        c_innings = 100;
                    else if (match.getSuper_over_innings1runs() > 0)
                        c_innings = 99;
                }
                i.putExtra("innings", c_innings);
                i.putExtra("back_type", 2);
                i.putExtra("endofinnings", true);
                i.putExtra("endofmatch", true);
                if (match.getResult() != null) {//|| !match.getMatch_result().matches(""))
                    i.putExtra("match_finished", true);
                    i.putExtra("view", true);
                } else {
                    i.putExtra("match_finished", false);
                    i.putExtra("view", false);
                }
                i.putExtra("continue_match", false);

                Log.d("MatchList", "Resume, matchStatus : " + match.getMatchStatus() +
                        ", c_innings : " + c_innings);
//                        i.putExtra("status", "resume");
                progress.dismiss();
                startActivity(i);
            }

            finish();
        }

    }

    @Override
    public void onDeleteButtonClickListener(int position, String value) {

        AlertDialog alertDialog = new AlertDialog.Builder(MatchListActivity.this).create();
        alertDialog.setIcon(R.drawable.ball);
        alertDialog.setCancelable(false);
        alertDialog.setTitle("Do you want to delete the match?");
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "No",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        dialog.cancel();
                        displayProgress();
                        config = new RealmConfiguration.Builder()
                                .name(AppConstants.GAME_ID + ".realm")
                                .deleteRealmIfMigrationNeeded()
                                .build();
                        realm = Realm.getInstance(config);
                        RealmResults<Match> result = realm.where(Match.class).findAll();
                        int matchid = result.get(position).getMatchid();
                        String matchID = result.get(position).getMatchID();
                        Match match = realm.where(Match.class).equalTo("matchid", matchid).findFirst();
//                        int matchStatus = match.getMatchStatus();
//                        if (matchStatus == 1)
                        if (match != null) {
                            if (!realm.isInTransaction()) {
                                realm.beginTransaction();
                            }

                            match.deleteFromRealm();
                            realm.commitTransaction();
                        }

                        RealmResults<Player> results1 = realm.where(Player.class).
                                equalTo("matchid", matchid).findAll();

                        for (Player player : results1) {
//                            if (player != null) {

                            if (!realm.isInTransaction()) {
                                realm.beginTransaction();
                            }

                            player.deleteFromRealm();
                            realm.commitTransaction();
//                            }
                        }

                        RealmResults<Batsman> results2 = realm.where(Batsman.class).
                                equalTo("matchid", matchid).findAll();

                        for (Batsman batsman : results2) {
//                            if (batsman != null) {

                            if (!realm.isInTransaction()) {
                                realm.beginTransaction();
                            }

                            batsman.deleteFromRealm();
                            realm.commitTransaction();
//                            }
                        }

                        RealmResults<Bowler> results3 = realm.where(Bowler.class).
                                equalTo("matchid", matchid).findAll();

                        for (Bowler bowler : results3) {
//                            if (bowler != null) {

                            if (!realm.isInTransaction()) {
                                realm.beginTransaction();
                            }

                            bowler.deleteFromRealm();
                            realm.commitTransaction();
//                            }
                        }

                        RealmResults<FOW> results4 = realm.where(FOW.class).
                                equalTo("matchid", matchid).findAll();

                        for (FOW fow : results4) {
//                            if (fow != null) {

                            if (!realm.isInTransaction()) {
                                realm.beginTransaction();
                            }

                            fow.deleteFromRealm();
                            realm.commitTransaction();
//                            }
                        }

                        RealmResults<Events> results5 = realm.where(Events.class).
                                equalTo("matchid", matchid).findAll();

                        for (Events events : results5) {
//                            if (events != null) {

                            if (!realm.isInTransaction()) {
                                realm.beginTransaction();
                            }

                            events.deleteFromRealm();
                            realm.commitTransaction();
//                            }
                        }

                        RealmResults<Undo> results6 = realm.where(Undo.class).
                                equalTo("matchid", matchid).findAll();

                        for (Undo undo : results6) {
//                            if (undo != null) {

                            if (!realm.isInTransaction()) {
                                realm.beginTransaction();
                            }

                            undo.deleteFromRealm();
                            realm.commitTransaction();
//                            }
                        }

                        RealmResults<Substitution> results7 = realm.where(Substitution.class).
                                equalTo("matchid", matchid).findAll();

                        for (Substitution substitution : results7) {
//                            if (substitution != null) {

                            if (!realm.isInTransaction()) {
                                realm.beginTransaction();
                            }

                            substitution.deleteFromRealm();
                            realm.commitTransaction();
//                            }
                        }

                        RealmResults<ExtraCard> results8 = realm.where(ExtraCard.class).
                                equalTo("matchid", matchid).findAll();

                        for (ExtraCard extraCard : results8) {
//                            if (extraCard != null) {

                            if (!realm.isInTransaction()) {
                                realm.beginTransaction();
                            }

                            extraCard.deleteFromRealm();
                            realm.commitTransaction();
//                            }
                        }

                        RealmResults<MatchOfficials> results9 = realm.where(MatchOfficials.class).
                                equalTo("matchid", matchid).findAll();

                        for (MatchOfficials matchOfficials : results9) {
//                            if (matchOfficials != null) {

                            if (!realm.isInTransaction()) {
                                realm.beginTransaction();
                            }

                            matchOfficials.deleteFromRealm();
                            realm.commitTransaction();
//                            }
                        }

                        RealmResults<Openers> results10 = realm.where(Openers.class).
                                equalTo("matchid", matchid).findAll();

                        for (Openers openers : results10) {
//                            if (openers != null) {

                            if (!realm.isInTransaction()) {
                                realm.beginTransaction();
                            }

                            openers.deleteFromRealm();
                            realm.commitTransaction();
//                            }
                        }

                        RealmResults<Partnership> results11 = realm.where(Partnership.class).
                                equalTo("matchid", matchid).findAll();

                        for (Partnership partnership : results11) {
//                            if (partnership != null) {

                            if (!realm.isInTransaction()) {
                                realm.beginTransaction();
                            }

                            partnership.deleteFromRealm();
                            realm.commitTransaction();
//                            }
                        }

                        RealmResults<Penalty> results12 = realm.where(Penalty.class).
                                equalTo("matchid", matchid).findAll();

                        for (Penalty penalty : results12) {
//                            if (penalty != null) {

                            if (!realm.isInTransaction()) {
                                realm.beginTransaction();
                            }

                            penalty.deleteFromRealm();
                            realm.commitTransaction();
//                            }
                        }

                        RealmResults<Power> results13 = realm.where(Power.class).
                                equalTo("matchid", matchid).findAll();

                        for (Power power : results13) {
//                            if (power != null) {

                            if (!realm.isInTransaction()) {
                                realm.beginTransaction();
                            }

                            power.deleteFromRealm();
                            realm.commitTransaction();
//                            }
                        }


                      /* Commented on 26/11/2021
                        RealmResults<Match> result14 = realm.where(Match.class).findAll();

                        long count = realm.where(Match.class).count();
                        Log.d("match14", "count : " + count);
                        dataTeams.clear();

                        for (Match matchs : result14) {

//            teamA[i] = match.getTeamA();
//            teamB[i] = match.getTeamB();
                            dataTeams.add(matchs.getTeamA() + " vs " + matchs.getTeamB());


                        }
                        adapter.notifyDataSetChanged();
                        listView.setAdapter(adapter);
                        progress.dismiss();*/

                        displayList();  //Added on 26/11/2021
                        adapter.notifyDataSetChanged(); // Added on 26/11/2021

                    }
                });
        alertDialog.show();


    }


    private void newInnings(Match match, int innings) {


        mEditor = mPreferences.edit();

        mEditor.putBoolean("SUPER_OVER", match.isSUPER_OVER());
//        mEditor.putString("sp_batting_team", );
//        mEditor.putString("sp_fielding_team", fieldingTeam);
//        mEditor.putString("sp_striker", striker);
//        mEditor.putInt("sp_striker_id", strID);
//        mEditor.putString("sp_non_striker",non_striker);
//        mEditor.putInt("sp_non_striker_id", nstrID);
//        mEditor.putString("sp_bowler", bowler);
//        mEditor.putInt("sp_bowler_id", bowlerID);
//        mEditor.putString("sp_next_bowler", next_bowler);
//        mEditor.putInt("sp_next_bowler_id", nextBowlerID);
        mEditor.putInt("sp_current_innings", innings);
//        mEditor.putInt("sp_batting_team_no", bTeam);
//        mEditor.putInt("sp_fielding_team_no", fTeam);


        if (match.isSUPER_OVER()) {

            mEditor.putInt("sp_over", 1);
//            mEditor.putInt("sp_playerA", 2);
//            mEditor.putInt("sp_playerB", 2);
            mEditor.putInt("sp_player_count", 2);
//            mEditor.putInt("sp_sub_seq", 0);
        }

        mEditor.apply();

        Toast.makeText(MatchListActivity.this, "Resume the match",
                Toast.LENGTH_SHORT).show();
        // for displaying scoring
//        Intent i = new Intent(MatchListActivity.this, ScoringActivity.class);  Commented on 26/07/2021
        Intent i = new Intent(MatchListActivity.this, UpdatedScoringActivity.class);
        i.putExtra("midscore", true);
        i.putExtra("matchid", match.getMatchid());
        i.putExtra("matchID", match.getMatchID());
        i.putExtra("status", "start");
        i.putExtra("runonce", true);
        i.putExtra("score", true);
        i.putExtra("inningsnotstarted", true);
        i.putExtra("initialize", true);
        progress.dismiss();
        startActivity(i);
        finish();

    }


    private void displayScorecard(Match match, int innings) {

        mEditor = mPreferences.edit();

        mEditor.putBoolean("SUPER_OVER", match.isSUPER_OVER());
//                    mEditor.putString("sp_batting_team", );
//                    mEditor.putString("sp_fielding_team", fieldingTeam);
//                    mEditor.putString("sp_striker", striker);
//                    mEditor.putInt("sp_striker_id", strID);
//                    mEditor.putString("sp_non_striker",non_striker);
//                    mEditor.putInt("sp_non_striker_id", nstrID);
//                    mEditor.putString("sp_bowler", bowler);
//                    mEditor.putInt("sp_bowler_id", bowlerID);
//                    mEditor.putString("sp_next_bowler", next_bowler);
//                    mEditor.putInt("sp_next_bowler_id", nextBowlerID);
        mEditor.putInt("sp_current_innings", innings);

//                    mEditor.putInt("sp_batting_team_no", bTeam);
//                    mEditor.putInt("sp_fielding_team_no", fTeam);


        if (match.isSUPER_OVER()) {

            mEditor.putInt("sp_over", 1);
//            mEditor.putInt("sp_playerA", 2);
//            mEditor.putInt("sp_playerB", 2);
            mEditor.putInt("sp_player_count", 2);
//            mEditor.putInt("sp_sub_seq", 0);
        }

        mEditor.apply();

        Toast.makeText(MatchListActivity.this, "Resume the match",
                Toast.LENGTH_SHORT).show();

        // for displaying directly scorecard
        int status = match.getMatchStatus();

        Intent i = new Intent(MatchListActivity.this, ScoreCardActivity.class);

        Log.d("displayScorecard", "currentInnings :" + innings);


        i.putExtra("matchid", match.getMatchid());
        i.putExtra("matchID", match.getMatchID());
        i.putExtra("endofinnings", true);
//       1 i.putExtra("continue_match", continueMatch);
//        i.putExtra("declare", declare);
        i.putExtra("innings", innings);
        i.putExtra("matchstatus", status);
        i.putExtra("inningsnotstarted", false);
        i.putExtra("endofmatch", false);
        i.putExtra("SUPER_OVER", false);
        i.putExtra("match_finished", false);
        i.putExtra("back_type", 2);
        startActivity(i);
        finish();
    }


    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, HomeActivity.class));
        finish();
    }


    public void displayProgress() {

        progress = new ProgressDialog(this);
        progress.setMessage("Please wait...");
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setIndeterminate(true);
        progress.setProgress(0);
        progress.show();
    }


    // made it a seperate method on 26/11/2021
    // code is moved from onCreate() method
    void displayList() {

        dataTeams.clear();
        Log.d("displayList", "dataTeams : " + dataTeams.toString());

        RealmResults<Match> result = realm.where(Match.class).findAll();
        result.load();

        long count = realm.where(Match.class).count();
        Log.d("match", "count : " + count);

        for (Match match : result) {

            dataTeams.add(match.getTeamA() + " vs " + match.getTeamB());
            JSONObject json = new JSONObject();
            try {
                json.put("teamA", match.getTeamA());
                json.put("teamB", match.getTeamB());
                json.put("matchId", match.getD4s_matchid());
                if (match.getMatchType().matches("Custom"))
                    json.put("type", match.getActual_over() + " over");
                else
                    json.put("type", match.getMatchType());
                json.put("date", match.getDate());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            jsonList.add(json);

        }

        if (realm != null) {
            realm.close();
        }

        progress.dismiss();
//        listView.setAdapter(adapter);
    }
}
