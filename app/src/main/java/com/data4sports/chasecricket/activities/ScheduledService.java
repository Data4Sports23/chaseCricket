package com.data4sports.chasecricket.activities;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.data4sports.chasecricket.applicationConstants.AppConstants;
import com.data4sports.chasecricket.models.Batsman;
import com.data4sports.chasecricket.models.Bowler;
import com.data4sports.chasecricket.models.Constants;
import com.data4sports.chasecricket.models.Events;
import com.data4sports.chasecricket.models.Match;
import com.data4sports.chasecricket.models.MatchNotes;
import com.data4sports.chasecricket.models.MatchOfficials;
import com.data4sports.chasecricket.models.MyApplicationClass;
import com.data4sports.chasecricket.models.Openers;
import com.data4sports.chasecricket.models.Player;
import com.data4sports.chasecricket.models.Substitution;
import com.data4sports.chasecricket.models.Undo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.Timer;
import java.util.TimerTask;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;
import io.realm.exceptions.RealmException;
import io.realm.exceptions.RealmPrimaryKeyConstraintException;

// created on 18/05/2020
public class ScheduledService extends Service {

    /*@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scheduled_service);
    }*/

    private Timer timer = new Timer();
    int matchid = 0, totalInnings = 0, currentInnings = 0, innings = 0, count = 0;
    String matchID;
    int post = 0;

    Integer game_Id = 0;
    JSONArray jsonEventArray, jsonUndoArray;
    Realm realm;

    RealmConfiguration config;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();


        config = new RealmConfiguration.Builder()
                .name(AppConstants.GAME_ID + ".realm")
                .deleteRealmIfMigrationNeeded()
                .build();
        realm = Realm.getInstance(config);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        matchid = sharedPreferences.getInt("sp_match_id", 0);
        matchID = sharedPreferences.getString("sp_match_ID", null);
        totalInnings = sharedPreferences.getInt("sp_total_innings", 0);
        currentInnings = sharedPreferences.getInt("sp_current_innings", 0);
        //game_Id = sharedPreferences.getInt("game_id",0);

        // Added on 02/08/2021
        post = sharedPreferences.getInt("sp_post", 0);


        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                sendRequestToServer();   //Your code here
            }
        }, 0, 1 * 60 * 1000);//1 Minutes
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    // Updated on 02/08/2021
    public void sendRequestToServer() {

        // added on 27/07/2020
        Log.d("ScheduledService", "post() 3: ");
        Log.d("TAG", "sendRequestToServer: App Constant " + AppConstants.GAME_ID);

        jsonUndoArray = new JSONArray();
        int undo_count = 0;
        config = new RealmConfiguration.Builder()
                .name(AppConstants.GAME_ID + ".realm")
                .deleteRealmIfMigrationNeeded()
                .build();
        realm = Realm.getInstance(config);

        // Added on 12/11/2021 from the bottom of this same method
        // added on 19/10/2020
        Log.d("TAG", "sendRequestToServer: match Id " + matchid);
        Match match = realm.where(Match.class).
                equalTo("matchid", matchid).
                findFirst();

        Log.d("TAG", "sendRequestToServer: match Id Match Value " + match);

        if (match != null) {

            if (match.getMatchSync() != 1) {
                postMatch(match);
            }
        }

        // Added on 27/11/2021
        if ((match.getTeamAId() != 0) && (match.getTeamAId() != 0)) {

            Player player = realm.where(Player.class)
                    .equalTo("matchid", matchid)
                    .equalTo("team", 1)
                    .findFirst();
            if ((player != null) && (player.getD4s_playerid() > 0))
                postPlayersAdd(2, match.getMatchID(), match.getTeamBId());

            player = realm.where(Player.class)
                    .equalTo("matchid", matchid)
                    .equalTo("team", 2)
                    .findFirst();
            if ((player != null) && (player.getD4s_playerid() == 0))
                postPlayersAdd(2, match.getMatchID(), match.getTeamBId());
        }


        // === till here

        // added on 12/11/2021
        RealmResults<Player> players_result = realm.where(Player.class).
                equalTo("matchid", matchid).
                equalTo("sync", 0).
                equalTo("edit", false).
                findAll();
        if (players_result.size() > 0)
            postPlayers();
        // === till here

        // updated on 14/09/2020
        /*RealmResults<Player> resultsA = realm.where(Player.class).
                equalTo("matchid", matchid).
                equalTo("team", 1).findAll();

        int count_A = realm.where(Player.class).
                equalTo("matchid", matchid).
                equalTo("team", 1).
                equalTo("sync", 1).findAll().size();

        Log.d("POST()", "scoring 1, resultsA.size() : " + resultsA.size());
        Log.d("POST()", "scoring 1, count_A : " + count_A);

        if (resultsA.size() > count_A) {

            RealmResults<Player> results1 = realm.where(Player.class).
                    equalTo("matchid", matchid).
                    equalTo("team", 1).
                    equalTo("edit", false).
                    equalTo("sync", 0).findAll();

            for (Player player : results1) {

                checkPlayers(player,
                        *//*matchid,*//*
                        matchID, 1, false);
            }*/

        RealmResults<Player> results1 = realm.where(Player.class).
                equalTo("matchid", matchid).
                equalTo("team", 1).
                equalTo("edit", true).
                equalTo("sync", 0).findAll();

        for (Player player : results1) {

            checkPlayers(player, matchID, 1, true);
        }
//        }

        // for team B
        /*RealmResults<Player> resultsB = realm.where(Player.class).
                equalTo("matchid", matchid).
                equalTo("team", 2).findAll();

        int count_B = realm.where(Player.class).
                equalTo("matchid", matchid).
                equalTo("team", 2).
                equalTo("sync", 1).findAll().size();

        Log.d("POST()", "scoring 2, resultsB.size() : " + resultsB.size());
        Log.d("POST()", "scoring 2, count_B : " + count_B);

        if (resultsB.size() > count_B) {

            RealmResults<Player> results1 = realm.where(Player.class).
                    equalTo("matchid", matchid).
                    equalTo("team", 2).
                    equalTo("edit", false).
                    equalTo("sync", 0).findAll();

            for (Player player : results1) {

                checkPlayers(player,
                        *//* matchid, *//*
                        matchID, 2, false);
            }
*/
        RealmResults<Player> results2 = realm.where(Player.class).
                equalTo("matchid", matchid).
                equalTo("team", 2).
                equalTo("edit", true).
                equalTo("sync", 0).findAll();

        for (Player player : results2) {

            checkPlayers(player, matchID, 2, true);
        }
//        }


        // for checking whether team name is edited or not
        Match match1 = realm.where(Match.class).
                equalTo("matchid", matchid).findFirst();

        if (match1 != null) {

            if (match1.getTossSync() == 0)    // added don 12/11/2021
                postToss(match1);

            if (match1.getTeamA_sync() == 0)    // update don 08/10/2021
                postTeam(match1.getTeamA(), match1.getTeamB());
        }

        Log.d("TESTing", "Testing");


        // for checking MatchOfficials
        // Code updated on 11/11/2021
        RealmResults<MatchOfficials> result_officials = realm.where(MatchOfficials.class).
                equalTo("matchid", matchid).
                equalTo("d4s_id", 0).
                equalTo("sync", 0).findAll();
        if (result_officials.size() > 0) {

            postAdd();
        }

        RealmResults<MatchOfficials> result_officials_edit = realm.where(MatchOfficials.class).
                equalTo("matchid", matchid).
                notEqualTo("d4s_id", 0).
                equalTo("sync", 0).findAll();
        if (result_officials_edit.size() > 0) {
            for (MatchOfficials official : result_officials)
                postEdit(official);
        }

        RealmResults<MatchOfficials> result_officials_delete = realm.where(MatchOfficials.class).
                equalTo("matchid", matchid).
                equalTo("delete", true).
                findAll();
        if (result_officials_delete.size() > 0) {
            Log.d("SS", "DO, result_officials_delete = " + result_officials_delete.toString());
            for (MatchOfficials official : result_officials_delete) {

                if (official.getD4s_id() > 0) {
                    postOfficialDeletion(official);
                } else {
                    delete(official);
                }
            }
        }
        // till here

        // for checking MatchNotes
        /*RealmResults<MatchNotes> result_notes = realm.where(MatchNotes.class).
                equalTo("matchid", matchid).
                equalTo("sync", 0).findAll();

        if (result_notes.size() > 0) {
            postMatchNote();
        }*/
        // till here

        RealmResults<Undo> result1 = realm.where(Undo.class).
                equalTo("matchid", matchid).
                equalTo("innings", currentInnings).
                equalTo("sync", 1).
                findAll();


        if (result1.isEmpty()) {

        } else {

            Log.d("ScheduledService", "post() 4 : " + result1);

            for (Undo undoObject : result1) {

//                if (undo_count <= 50) {//10) {  // commented on 0/07/2021
                if (undo_count <= 0) {//10) {
                    ++undo_count;
                    Log.d("ScheduledService", "post() 1 : " + undoObject);

                    jsonUndoArray.put(setUndo(undoObject));

                    Log.d("ScheduledService", "post() 2 : " + jsonUndoArray);
                }
            }
        }

        Log.d("ScheduledService", "post() 5 , isNetworkAvailable : " + isNetworkAvailable());
        Log.d("ScheduledService", "post() 5 , jsonUndoArray : " + jsonUndoArray);

        if (jsonUndoArray.length() > 0) {

            Log.d("ScheduledService", "post() 6 , jsonUndoArray.length() : " + jsonUndoArray.length());
            Log.d("ScheduledService", "post() 7 , jsonUndoArray : " + jsonUndoArray);
            postUndoJSON();

        } else {
            resetJSONEventArray();
            if (jsonEventArray.length() > 0) {
                postJSON(matchid, matchID/*, false*/);
            }

            // Added on 08/10/2021
            // to post match result and man of the march
            else {
                Match match99 = realm.where(Match.class).
                        equalTo("matchid", matchid).
                        findFirst();
                if (match99 != null) {

                    int events_sync = realm.where(Events.class).
                            equalTo("matchid", matchid).
                            equalTo("syncstatus", 0).
                            findAll().size();
                    if (events_sync <= 0) {
                        if (match99.getType() != null) {
                            postJSONResult();
                        }

                        if ((match99.getMom_pid() > 0) && (match99.getMom_sync() == 0)) {
                            postJSONMOM();
                        }
                    }
                }
            }
            /// === till here
        }

    }


    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        Log.d("internet", "ScheduledService, activeNetworkInfo : " + activeNetworkInfo);
        if (activeNetworkInfo != null)
            Log.d("internet", "ScheduledService, activeNetworkInfo.isConnected() : " + activeNetworkInfo.isConnected());
        NetworkInfo wifiInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        Log.d("internet", "ScheduledService, wifiInfo : " + wifiInfo);
        if (wifiInfo != null)
            Log.d("internet", "ScheduledService, wifiInfo.isConnected() : " + wifiInfo.isConnected());
        return ((activeNetworkInfo != null && activeNetworkInfo.isConnected()) || (wifiInfo != null && wifiInfo.isConnected()));
    }


    public JSONObject setEventJSON(Events events) {

        int b = 0, lb = 0, wd = 0, nb = 0, p = 0, wk = 0;
        Match match = realm.where(Match.class).equalTo("matchid", matchid).findFirst();


        //for bowler
        JSONObject jsoncurrentbowler = new JSONObject();
        try {
            jsoncurrentbowler.put("id", events.getBowlerID());
            jsoncurrentbowler.put("d4sid", events.getBowlerD4SID());   // Added on 13/11/2021
            jsoncurrentbowler.put("run", events.getBowlerRuns());
            jsoncurrentbowler.put("over", events.getBowlerOver());
            jsoncurrentbowler.put("balls", events.getBowlerBalls());
            jsoncurrentbowler.put("wicket", events.getBowlerWicket());
            jsoncurrentbowler.put("dots", events.getBowlerDots());//bow.getDots());
            jsoncurrentbowler.put("maiden", events.getBowlerMO());//bow.getMaidenOver());
            jsoncurrentbowler.put("wide", events.getBowlerWides());//bow.getWides());
            jsoncurrentbowler.put("noball", events.getBowlerNoball());//bow.getNoBalls());

        } catch (JSONException e) {
            e.printStackTrace();
        }


        JSONObject jsonotherendbowler = new JSONObject();

        if (events.getPrevBowlerID() != 0) {

            try {
                jsonotherendbowler.put("id", events.getPrevBowlerID());
                jsonotherendbowler.put("d4sid", events.getPrevBowlerD4SID());   // Added on 13/11/2021
                jsonotherendbowler.put("run", events.getPreBowlerRuns());
                jsonotherendbowler.put("over", events.getPreBowlerOver());
                jsonotherendbowler.put("balls", events.getPreBowlerBalls());
                jsonotherendbowler.put("wicket", events.getPreBowlerWicket());
//                if (bow != null) {    // commented on 16/05/2020
                jsonotherendbowler.put("dots", events.getPreBowlerDots());//bow.getDots());
                jsonotherendbowler.put("maiden", events.getPreBowlerMO());//bow.getMaidenOver());
                jsonotherendbowler.put("wide", events.getPreBowlerWides());//bow.getWides());
                jsonotherendbowler.put("noball", events.getPreBowlerNoball());//bow.getNoBalls());

            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            try {
                jsonotherendbowler.put("id", 0);
                jsonotherendbowler.put("d4sid", 0);   // Added on 13/11/2021
                jsonotherendbowler.put("run", 0);
                jsonotherendbowler.put("over", 0);
                jsonotherendbowler.put("balls", 0);
                jsonotherendbowler.put("wicket", 0);
                jsonotherendbowler.put("dots", 0);
                jsonotherendbowler.put("maiden", 0);
                jsonotherendbowler.put("wide", 0);
                jsonotherendbowler.put("noball", 0);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }


        JSONObject jsonbowler = new JSONObject();
        try {
            jsonbowler.put("current", jsoncurrentbowler);
            jsonbowler.put("otherend", jsonotherendbowler);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Added on 13/09/2021
        // Updated Striker - Non striker data
        JSONObject jsonstriker = new JSONObject();
        JSONObject jsonnonstriker = new JSONObject();

        try {
            int balltype = events.getBallType();
            int dismissedid = events.getDismissedPlayerID();
            if ((balltype == 4 || balltype == 5) && (dismissedid > 0)) {
                if (dismissedid == events.getStrikerID()) {
                    // Striker details
                    jsonstriker.put("id", 0);
                    jsonstriker.put("d4sid", 0);   // Added on 13/11/2021
                    jsonstriker.put("run", 0);
                    jsonstriker.put("ball", 0);
                    jsonstriker.put("battingorder", 0);
                    jsonstriker.put("dots", 0);
                    jsonstriker.put("fours", 0);
                    jsonstriker.put("sixes", 0);
                    jsonstriker.put("striker_outtype", -1);
                    jsonstriker.put("notoutindicator_striker", "y");

                    // Non-Striker details
                    jsonnonstriker.put("id", events.getNonStrikerID());
                    jsonnonstriker.put("d4sid", events.getNonStrikerD4SID());   // Added on 13/11/2021
                    jsonnonstriker.put("run", events.getNonStrikerRuns());
                    jsonnonstriker.put("ball", events.getNonStrikerBalls());
                    if (events.getNonStrikerBattingOrder() == 100)
                        jsonnonstriker.put("battingorder", 0);
                    else
                        jsonnonstriker.put("battingorder", events.getNonStrikerBattingOrder());
                    jsonnonstriker.put("dots", events.getNonStrikerDots());
                    jsonnonstriker.put("fours", events.getNonStrikerF4s());
                    jsonnonstriker.put("sixes", events.getNonStrikerS6s());
                    jsonnonstriker.put("nonstriker_outtype", events.getNonStrikerOutType());
                    if (events.getNonStrikerOutType() == -1)
                        jsonnonstriker.put("notoutindicator_nonstriker", "y");
                    else
                        jsonnonstriker.put("notoutindicator_nonstriker", "n");

                } else if (dismissedid == events.getNonStrikerID()) {
                    // Striker details
                    jsonstriker.put("id", events.getStrikerID());
                    jsonstriker.put("d4sid", events.getStrikerD4SID());   // Added on 13/11/2021
                    jsonstriker.put("run", events.getStrikerRuns());
                    jsonstriker.put("ball", events.getStrikerBalls());
                    if (events.getStrikerBattingOrder() == 100)
                        jsonstriker.put("battingorder", 0);
                    else
                        jsonstriker.put("battingorder", events.getStrikerBattingOrder());
                    jsonstriker.put("dots", events.getStrikerDots());
                    jsonstriker.put("fours", events.getStrikerF4s());
                    jsonstriker.put("sixes", events.getStrikerS6s());
                    jsonstriker.put("striker_outtype", events.getStrikerOutType());
                    if (events.getStrikerOutType() == -1)
                        jsonstriker.put("notoutindicator_striker", "y");
                    else
                        jsonstriker.put("notoutindicator_striker", "n");

                    // Non-Striker details
                    jsonnonstriker.put("id", 0);
                    jsonnonstriker.put("d4sid", 0);   // Added on 13/11/2021
                    jsonnonstriker.put("run", 0);
                    jsonnonstriker.put("ball", 0);
                    jsonnonstriker.put("battingorder", 0);
                    jsonnonstriker.put("dots", 0);
                    jsonnonstriker.put("fours", 0);
                    jsonnonstriker.put("sixes", 0);
                    jsonnonstriker.put("nonstriker_outtype", -1);
                    jsonnonstriker.put("notoutindicator_nonstriker", "y");

                }

            } else {

                // Striker details
                jsonstriker.put("id", events.getStrikerID());
                jsonstriker.put("d4sid", events.getStrikerD4SID());   // Added on 13/11/2021
                jsonstriker.put("run", events.getStrikerRuns());
                jsonstriker.put("ball", events.getStrikerBalls());
                if (events.getStrikerBattingOrder() == 100)
                    jsonstriker.put("battingorder", 0);
                else
                    jsonstriker.put("battingorder", events.getStrikerBattingOrder());
                jsonstriker.put("dots", events.getStrikerDots());
                jsonstriker.put("fours", events.getStrikerF4s());
                jsonstriker.put("sixes", events.getStrikerS6s());
                jsonstriker.put("striker_outtype", events.getStrikerOutType());
                if (events.getStrikerOutType() == -1)
                    jsonstriker.put("notoutindicator_striker", "y");
                else
                    jsonstriker.put("notoutindicator_striker", "n");

                // Non-Striker details
                jsonnonstriker.put("id", events.getNonStrikerID());
                jsonnonstriker.put("d4sid", events.getNonStrikerD4SID());   // Added on 13/11/2021
                jsonnonstriker.put("run", events.getNonStrikerRuns());
                jsonnonstriker.put("ball", events.getNonStrikerBalls());
                if (events.getNonStrikerBattingOrder() == 100)
                    jsonnonstriker.put("battingorder", 0);
                else
                    jsonnonstriker.put("battingorder", events.getNonStrikerBattingOrder());
                jsonnonstriker.put("dots", events.getNonStrikerDots());
                jsonnonstriker.put("fours", events.getNonStrikerF4s());
                jsonnonstriker.put("sixes", events.getNonStrikerS6s());
                jsonnonstriker.put("nonstriker_outtype", events.getNonStrikerOutType());
                if (events.getNonStrikerOutType() == -1)
                    jsonnonstriker.put("notoutindicator_nonstriker", "y");
                else
                    jsonnonstriker.put("notoutindicator_nonstriker", "n");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // === till here

        JSONObject jsonbatsman = new JSONObject();
        try {
            jsonbatsman.put("striker", jsonstriker);
            jsonbatsman.put("nonstriker", jsonnonstriker);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        // for adding total extras on events

        b = events.getExtraBye();//extraCard.getByes();
        lb = events.getExtraLb();//extraCard.getLb();
        wd = events.getExtraWd();//extraCard.getWide();// * wideRun;
        nb = events.getExtraNb();//extraCard.getNoBall();// * noballRun;
        p = events.getExtraP();//extraCard.getPenalty();// * penaltyRun;


        JSONObject jsonExtra = new JSONObject();
        try {
//            jsonExtra.put("inningsextras", (b + lb + (wd * wideRun) + (nb * noballRun) + (p * penaltyRun)));
            jsonExtra.put("inningsextras",
                    (b +
                            lb +
                            (wd * match.getWiderun()) +
                            (nb * match.getNoballrun()) +
                            (p * match.getPenaltyrun())));
            jsonExtra.put("byes", b);
            jsonExtra.put("legbyes", lb);
            jsonExtra.put("wide", wd);
            jsonExtra.put("noball", nb);
            jsonExtra.put("penalty", (p * match.getPenaltyrun()));  //p);

        } catch (JSONException e) {
            e.printStackTrace();
        }


        // adding inningssummary

        JSONObject jsonSummery = new JSONObject();
        try {
//            jsonSummery.put("innings", events.getInnings());
            if (events.isSUPER_OVER()) {
                if (events.getInnings() == 99)
                    jsonSummery.put("innings", 1);
                else if (events.getInnings() == 100)
                    jsonSummery.put("innings", 2);
            } else
                jsonSummery.put("innings", events.getInnings());
            jsonSummery.put("totalovers", Float.parseFloat(
                    new DecimalFormat("###.#").format(events.getOvers())));
            jsonSummery.put("totalscore", events.getTotalRuns());
            jsonSummery.put("totalwicket", events.getWicket());
            jsonSummery.put("extras", jsonExtra);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // adding over summary
        JSONObject jsonOverSummary = new JSONObject();
        try {
            jsonOverSummary.put("runs", events.getMo());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // getting fielderIDs
        JSONArray arrayFielder = new JSONArray();
        if (events.getOutType() == 1 || events.getOutType() == 3)
            arrayFielder.put(Integer.parseInt(events.getFielderID()));
        else if (events.getOutType() == 2) {

            String[] arrSplit = events.getFielderID().split(", ");
            for (int i = 0; i < arrSplit.length; i++) {
                arrayFielder.put(arrSplit[i]);
            }
        }

        if ((events.getBallType() == 3) && (events.getOutType() != 10))//ballType == 3 && outType != 10)
            wk = events.getWicket();//wicket;
        else
            wk = events.getWicket() + 1;//wicket + 1;

        Log.d("ScheduledService", "setEventJSON : wk : " + wk);

        JSONObject jsonFOW = new JSONObject();

        try {

            jsonFOW.put("wicketno", events.getP_wicket_no());//partnership.getWicket());
            jsonFOW.put("partnershipsequence", events.getP_sequence_no());//partnership.getPartnershipSequence());
            jsonFOW.put("partnershipruns", events.getP_run());//partnership.getPartnershipRuns());
            jsonFOW.put("partnershipballs", events.getP_ball());//partnership.getPartnershipBalls());
            /*jsonFOW.put("partnershipover", Float.parseFloat(
                    new DecimalFormat("###.#").format(events.getP_over())));//partnership.getPartnershipOver())));*/
            jsonFOW.put("partnershipover", events.getP_over());//partnership.getPartnershipOver())));
            jsonFOW.put("player1id", events.getP_p1Id());//partnership.getPlayer1ID());
            jsonFOW.put("player2id", events.getP_p2Id());//partnership.getPlayer2ID());
            if ((events.getBallType() != 5) && (events.getBallType() != 4)) // added condition on 15/09/2021
                jsonFOW.put("dismissedplayerid", events.getP_disId());
            jsonFOW.put("player1contributedruns", events.getP_p1cr());//partnership.getPlayer1ContributionRuns());
            jsonFOW.put("player1contributedballs", events.getP_p1cb());//partnership.getPlayer1ContributionBalls());
            jsonFOW.put("player2contributedruns", events.getP_p2cr());//partnership.getPlayer2ContributionRuns());
            jsonFOW.put("player2contributedballs", events.getP_p2cb());//partnership.getPlayer2ContributionBalls());

            // added on 15/09/2021
            if (events.getP_disId() == 0) {
                jsonFOW.put("partnershipbroken", "n");  // no
            } else {
                jsonFOW.put("partnershipbroken", "y");  //
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }


        //adding session details
        JSONObject jsonSession = new JSONObject();
        try {
//            if (events.getBallType() == 13) {
            if (events.getSessionId() == 7 || events.getSessionId() == 9 || events.getSessionId() == 11)
                jsonSession.put("type", "end");

            else if (events.getSessionId() == 8 || events.getSessionId() == 10 ||
                    events.getSessionId() == 12 || events.getSessionId() == 6)
                jsonSession.put("type", "start");

            if (events.getSessionId() == 7 || events.getSessionId() == 12 || events.getSessionId() == 6)
                jsonSession.put("number", 1);
            else if (events.getSessionId() == 8 || events.getSessionId() == 9)
                jsonSession.put("number", 2);
            else if (events.getSessionId() == 10 || events.getSessionId() == 11)
                jsonSession.put("number", 3);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        //adding interval details
        JSONObject jsonInterval = new JSONObject();
        try {
            if (events.getBallType() == 12)
                jsonInterval.put("id", events.getIntervalId());
            else
                jsonInterval.put("id", 0);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // for substituion and concussion
        JSONObject jsonSubstitution = new JSONObject();
        if (events.getBallType() == 18 || events.getBallType() == 19) {
            Substitution substitution = realm.where(Substitution.class).
                    equalTo("matchid", events.getMatchid()).
                    equalTo("innings", events.getInnings()).
                    equalTo("subID", events.getSubstitutionID()).findFirst();

            if (substitution != null) {
                try {

                    jsonSubstitution.put("team", events.getSub_team());//substitution.getTeam());
//                    jsonSubstitution.put("innings", substitution.getInnings());
                    jsonSubstitution.put("playerout_id", events.getSub_playerout_id());//substitution.getPlayer_OUT_ID());
                    jsonSubstitution.put("playerin_id", events.getSub_playerin_id());//substitution.getPlayer_IN_ID());
//                    jsonSubstitution.put("playerout", substitution.getPlayer_OUT_ID());
//                    jsonSubstitution.put("playerin", substitution.getPlayer_IN_ID());

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }


        JSONObject jsonPowerplay = new JSONObject();
        try {
            jsonPowerplay.put("start_over", events.getPower_start_over());//power.getStart());
            jsonPowerplay.put("end_over", events.getPower_end_over());//power.getEnd());
            jsonPowerplay.put("type", "");//power.getType());
//                    jsonPowerplay.put("innings", power.getInnings());
            jsonPowerplay.put("sequence", events.getPower_sequence());//power.getCount());

        } catch (JSONException e) {
            e.printStackTrace();
        }

        // adding event details

        JSONObject jsonEvent = new JSONObject();
        try {
            jsonEvent.put("eventid", events.getEventID());
            jsonEvent.put("superover", events.isSUPER_OVER());
            if (events.isSUPER_OVER()) {
                if (events.getInnings() == 99)
                    jsonEvent.put("innings", 1);
                else if (events.getInnings() == 100)
                    jsonEvent.put("innings", 2);
            } else
                jsonEvent.put("innings", events.getInnings());

            jsonEvent.put("battingteam", events.getBattingTeamNo());
            jsonEvent.put("over", (int) events.getOvers());
            jsonEvent.put("ball", events.getCurrentOverBalls());
            jsonEvent.put("strikerid", events.getStrikerID());
            jsonEvent.put("nonstrikerid", events.getNonStrikerID());
            jsonEvent.put("bowlerid", events.getBowlerID());
            jsonEvent.put("balltype", events.getBallType());

            //Added on 15/09/2021
            if ((events.getBallType() == 5) || (events.getBallType() == 4))
                jsonEvent.put("endofover", "y");
            else
                jsonEvent.put("endofover", "n");
            // === till here

            if (events.getFielderID() != null)//.matches(""))
                jsonEvent.put("fielderids", events.getFielderID());
            else
                jsonEvent.put("fielderids", null);

            if (events.getDismissedPlayerID() <= 0)
                jsonEvent.put("dismissedbatsmanid", 0);
            else
                jsonEvent.put("dismissedbatsmanid", events.getDismissedPlayerID());
            jsonEvent.put("disnewbatsmanid", events.getDisNewBatsmanID());
//            if (events.getBallType() != 4 && events.getBallType() != 5) {   // condition added on 13/09/2021
            jsonEvent.put("outtype", events.getOutType());
//            }
            jsonEvent.put("fielderids", arrayFielder);

            jsonEvent.put("extratype", events.getExtraType());
            jsonEvent.put("freehit", events.isFreeHit());
            if (events.getPenaltyType() == 1 || events.getPenaltyType() == 2)
                jsonEvent.put("penaltybool", 1);
            else
                jsonEvent.put("penaltybool", 0);

            jsonEvent.put("penaltytype", events.getPenaltyType());
            jsonEvent.put("runs", events.getCurrentRun());
            jsonEvent.put("extras", events.getExtraRuns());
            if (events.getPenaltyType() > 0)
                jsonEvent.put("penalty", events.getPenaltyRuns());
            else
                jsonEvent.put("penalty", 0);


            if (events.getCommentary() != null)//.matches(""))
                jsonEvent.put("commentary", events.getCommentary());
            else
                jsonEvent.put("commentary", "");


            jsonEvent.put("strokedirection", events.getStrokeDirection());


            jsonEvent.put("bowler", jsonbowler);
//            if (events.getBallType() != 4 && events.getBallType() != 5) {   // condition added on 13/09/2021
            jsonEvent.put("batsman", jsonbatsman);
//            }
            jsonEvent.put("inningssummary", jsonSummery);

            if (events.getBallType() == 4 || events.getBallType() == 5)
                jsonEvent.put("oversummary", jsonOverSummary);

//            if (events.getBallType() == 3 && events.getDismissedPlayerID() > -1)
            jsonEvent.put("fallofwickets", jsonFOW);
            jsonEvent.put("session", jsonSession);
            jsonEvent.put("interval", jsonInterval);

//            if (events.getBallType() == 18 || events.getBallType() == 19) {
            if (events.getBallType() == 18)
                jsonEvent.put("substituion", jsonSubstitution);
            else if (events.getBallType() == 19)
                jsonEvent.put("concussion", jsonSubstitution);

            else if (events.getBallType() == 21 || events.getBallType() == 22 || events.getBallType() == 23)
                jsonEvent.put("powerplay", jsonPowerplay);

            else if (events.getBallType() == 20) {
                jsonEvent.put("rainruleused", "y");
                jsonEvent.put("reducedovers", events.getReducedOver());//match.getOver());
                jsonEvent.put("rainrulemethod", events.getAppliedRainRule());
                jsonEvent.put("revisedtarget", events.getRevisedTarget());
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.e("ScheduledService", "setEventJSON, jsonEvent : " + jsonEvent);

        return jsonEvent;//jsonCurrentEvent;
    }


    private void postJSON(int matchid, String matchID/*, int innings*//*, String matchID, boolean undo*/) {


        Log.d("ScheduledService", "postJSON");

        boolean matchSync = false, playerSync = false, tossSync = false;


        int msync = 0, psync = 0, tsync = 0, osync = 0;
        String capA = null, vcA = null, wkA = null, capB = null, vcB = null, wkB = null,
                /*matchID = null,*/ tossWinner = null, decision = null, umpire1_v = "",
                umpire2_v = "", umpire3_v = "", umpire4_v = "", match_referee_v = "";
        boolean post = false, player_post = false;

        if (isNetworkAvailable()) {

            Match match = realm.where(Match.class).
                    equalTo("matchid", matchid).findFirst();

            Log.d("ScheduledService", "matchid : " + matchid);
            Log.d("ScheduledService", "match : " + match);
            if (match != null) {
//            if (matchID == null)
                matchID = match.getMatchID();
                msync = match.getMatchSync();
                RealmResults<Player> result = realm.where(Player.class).
                        equalTo("matchid", matchid).
                        findAll();
                if (result.size() > 0) {
                    RealmResults<Player> resultt = realm.where(Player.class).
                            equalTo("matchid", matchid).
                            equalTo("sync", 0).
                            findAll();
                    if (resultt.size() > 0)
                        psync = 0;
                }

                tsync = match.getTossSync();
                osync = match.getOpenerSync();
                post = match.isPost();
                player_post = match.isPlayer_post();
                tossWinner = match.getToss_winner();
                decision = match.getDecision();
            }


            // posting officials
            RealmResults<MatchOfficials> result = realm.where(MatchOfficials.class).
                    equalTo("matchID", matchID).findAll();

            JSONArray arrayOfficials = new JSONArray();
            Log.d("ScheduledService", "results 1 : " + result);
            if (result.isEmpty()) {

                Log.d("ScheduledService", "results : " + result);
            } else {

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

            // posting match details if not synced
            JSONObject jsonMatch = new JSONObject();

            if (post)
                Log.d("ScheduledService", "match : " + match);
            else {//msync == 0) {

                try {

                    jsonMatch.put("d4s_gameid", match.getD4s_matchid());
                    jsonMatch.put("d4s_userid", match.getD4s_userid());

                    jsonMatch.put("matchID", match.getMatchID());
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
//                    jsonMatch.put("players", match.getPlayer());
//                    jsonMatch.put("substitute_players", match.getSubst());
                    jsonMatch.put("over", match.getActual_over());
                    jsonMatch.put("balls_per_over", match.getBalls());
                    jsonMatch.put("wide_value", match.getWiderun());
                    jsonMatch.put("noball_value", match.getNoballrun());
                    jsonMatch.put("penalty_value", match.getPenaltyrun());
                    jsonMatch.put("rainruleused", "n");
                /*if (match.getUmpire1() == null)
                    jsonMatch.put("umpire1", "");
                else
                    jsonMatch.put("umpire1", match.getUmpire1());
                if (match.getUmpire2() == null)
                    jsonMatch.put("umpire2", "");
                else
                    jsonMatch.put("umpire2", match.getUmpire2());
                if (match.getUmpire3() == null)
                    jsonMatch.put("umpire3", "");
                else
                    jsonMatch.put("umpire3", match.getUmpire3());
                if (match.getUmpire4() == null)
                    jsonMatch.put("umpire4", "");
                else
                    jsonMatch.put("umpire4", match.getUmpire4());
                if (match.getMatchReferee() == null)
                    jsonMatch.put("match_referee", "");
                else
                    jsonMatch.put("match_referee", match.getMatchReferee());*/
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }


            //posting Players
            // starting of adding Team A players
            JSONObject jsonPlayerA;

            // for adding players

            RealmResults<Player> results = realm.where(Player.class).
                    equalTo("matchid", matchid).
                    equalTo("team", 1).
                    equalTo("substitute", false).findAll();
            JSONArray arrayPlayerA = new JSONArray();

            for (Player player : results) {

                jsonPlayerA = new JSONObject();

                try {
                    jsonPlayerA.put("name", player.getPlayerName());
                    jsonPlayerA.put("player_id", player.getPlayerID());
                    jsonPlayerA.put("d4s_playerid", player.getD4s_playerid());
                    if (player.isCaptain()) {
                        jsonPlayerA.put("captain", "y");
                        capA = player.getPlayerName();
                    } else
                        jsonPlayerA.put("captain", "n");
                    if (player.isViceCaptain()) {
                        jsonPlayerA.put("vice_captain", "y");
                        vcA = player.getPlayerName();
                    } else
                        jsonPlayerA.put("vice_captain", "n");
                    if (player.isWicketKeeper()) {
                        jsonPlayerA.put("wicketkeeper", "y");
                        wkA = player.getPlayerName();
                    } else
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
                jsonTeamA.put("captain", capA);
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
            }// end of adding Team A players

            // starting of adding Team B players
            JSONObject jsonPlayerB;

            // for adding players

            results = realm.where(Player.class).
                    equalTo("matchid", matchid).
                    equalTo("team", 2).
                    equalTo("substitute", false).findAll();
            JSONArray arrayPlayerB = new JSONArray();

            for (Player player : results) {

                jsonPlayerB = new JSONObject();

                try {
                    jsonPlayerB.put("name", player.getPlayerName());
                    jsonPlayerB.put("player_id", player.getPlayerID());
                    jsonPlayerB.put("d4s_playerid", player.getD4s_playerid());
                    if (player.isCaptain()) {
                        jsonPlayerB.put("captain", "y");
                        capB = player.getPlayerName();
                    } else
                        jsonPlayerB.put("captain", "n");
                    if (player.isViceCaptain()) {
                        jsonPlayerB.put("vice_captain", "y");
                        vcB = player.getPlayerName();
                    } else
                        jsonPlayerB.put("vice_captain", "n");
                    if (player.isWicketKeeper()) {
                        jsonPlayerB.put("wicketkeeper", "y");
                        wkB = player.getPlayerName();
                    } else
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
                jsonTeamB.put("captain", capB);
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

//        Log.e("scoring", "2nd matchID : "+matchID);

            // adding toss winner and their decision
            // commented on 19/10/2020
           /* JSONObject jsonToss = new JSONObject();
            try {
                jsonToss.put("matchID", matchID);
                jsonToss.put("winner", tossWinner);
                jsonToss.put("decision", decision);
            } catch (JSONException e) {
                e.printStackTrace();
            }*/
            // added on 19/10/2020
            JSONObject jsonToss = new JSONObject();
            try {
                jsonToss.put("matchID", matchID);
                if (match.isNoToss() || match.isUnknownToss()) {
                    jsonToss.put("winner", -1);
                    jsonToss.put("decision", decision);
                    jsonToss.put("battingteam", tossWinner);
                } else {
                    jsonToss.put("winner", tossWinner);
                    jsonToss.put("decision", decision);
                    jsonToss.put("battingteam", 0);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }


//        Log.e("scoring", "jsonToss : "+jsonToss);

            JSONArray arrayToss = new JSONArray();
            arrayToss.put(jsonToss);
//        Log.e("scoring", "arrayToss : "+arrayToss);

        /*Openers openers = realm.where(Openers.class).
                equalTo("matchid", matchid).
                equalTo("innings", innings).findFirst();

        JSONObject jsonOpener = new JSONObject();
        try {
            jsonOpener.put("matchID", matchID);
            jsonOpener.put("innings", innings);
            if (openers != null) {
                jsonOpener.put("striker", openers.getStrikerID());//striker);
                jsonOpener.put("non_striker", openers.getNonStrikerID());//nonStriker);
                jsonOpener.put("bowler", openers.getBowlerID());//bowler);
            }
//            jsonOpener.put("next_bowler", nextBowler);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JSONArray arrayOpener = new JSONArray();
        arrayOpener.put(jsonOpener);
//        Log.e("toss", "arrayOpener : "+arrayOpener);*/

            JSONObject jsonball = new JSONObject();
            try {
                Log.d("ScheduledService", "postJSON : " + jsonEventArray);
                jsonball.put("ball", jsonEventArray);

            } catch (JSONException e) {
                e.printStackTrace();
            }

            JSONObject jsonbbb = new JSONObject();
            try {
                jsonbbb.put("ballbyball", jsonball);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            JSONObject jsonmatchid = new JSONObject();
            try {
                jsonmatchid.put("matchID", matchID);
            } catch (JSONException e) {
                e.printStackTrace();
            }


            JSONArray arrayAddBBB = new JSONArray();
            arrayAddBBB.put(jsonmatchid);
            arrayAddBBB.put(jsonbbb);


        /*JSONObject jsonUndo = new JSONObject();
        try {
            jsonUndo.put("matchID", matchID);
            jsonUndo.put("innings", currentInnings);
            jsonUndo.put("undo", jsonUndoArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }*/


            // added on 15/05/2020
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("matchID", matchID);
                jsonObject.put("officials", arrayOfficials);
            } catch (JSONException e) {
                e.printStackTrace();
            }


            // adding values into feed
            JSONObject jsonFeed = new JSONObject();
            try {
            /*if (post)
                Log.d("cap", "match : " + match);
            else {//msync == 0) {*/
                if (msync == 0) {
                    jsonFeed.put("AddMatch", jsonMatch);

//            }

//            else {
                    if (psync == 0) {
                    /*if (player_post)
                        Log.d("cap", "match : " + match);
                    else //psync == 0)*/
                        jsonFeed.put("AddPlayers", arrayPlayers);
                    }

                    /*  else {*/ // commented on 15/05/2020

//                    if (psync == 1) {
                    if (tsync == 0) {
                        jsonFeed.put("AddToss", arrayToss);
                    }
//                    }
                    //for openers
//            if (osync == 0)
//                jsonFeed.put("AddOpeners", arrayOpener);
//            jsonFeed.put("AddBBB", jsonEventArray);


                } else {
                    //commented on 15/05/2020
                    /*else {
                            if (tsync == 1) {*/
                    if (jsonUndoArray.length() > 0)
                        jsonFeed.put("UndoBBB", jsonUndoArray);//jsonUndo);
                    else
//                    if (jsonEventArray.length() > 0)  Commented on 15/11/2021
                        jsonFeed.put("AddBBB", arrayAddBBB);
                            /*}
                        }
                }*/// commented on 15/05/2020
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
//        Log.e("scoring", "jsonFeed : "+jsonFeed);

            //adding values to postparams
            JSONObject postparams = new JSONObject();
            try {
                postparams.put("title", "CHASE_POST");
                postparams.put("feed", jsonFeed);
            } catch (JSONException e) {
                e.printStackTrace();
            }

//        Log.e("scoring", "postparams : "+postparams);

            JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                    Constants.CHASE_CRICKET_MATCH_API,
                    postparams,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {


                            Log.e("ScheduledService", "response : " + response);

                            try {

                                //if no error in response

                                if ((response.getInt("status") == 200) && !response.getBoolean("error")) {

                                    JSONObject jsonMatch = response.getJSONObject("match");

                                    //commented on 15/05/2020
//                                JSONArray updatedIDs = response.getJSONArray("updatedIDs");
                                /*JSONArray updatedIDs = new JSONArray();
                                if (response.has("updatedIDs")) {
                                    updatedIDs = response.getJSONArray("updatedIDs");
                                }*/

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
                                                            equalTo("matchID", jsonMatch.getString("app_matchID")).
                                                            findFirst();

                                                    if (match1 != null) {
                                                        match1.setPost(true);

//                                                    if (match1.getMatchSync() == 1) {
                                                        match1.setMatchSync(1);
                                                        match1.setPlayer_post(true);
                                                        if (match1.getPlayerSync() != 1)
                                                            match1.setPlayerSync(1);
                                                        if (match1.getOpenerSync() != 1)
                                                            match1.setOpenerSync(1);
                                                        if (match1.getTossSync() != 1)
                                                            match1.setTossSync(1);
//                                                    }
//                                                    else
//                                                        match1.setMatchSync(1);


                                                        Log.d("ScheduledService", "scoring, match1 synced");

                                                        bgRealm.copyToRealm(match1);
                                                        Log.d("ScheduledService", "scoring, match1 : " + match1);
                                                    }


//                                                =====================================================================================
//                                                =====================================================================================
//                                                need to sync Undo table

//                                                resetJSONUndoArray();
//                                                =====================================================================================
//                                                =====================================================================================


                                                    if (response.has("updatedIDs")) {
                                                        JSONArray updatedIDs = response.getJSONArray("updatedIDs");

                                                        if (updatedIDs.length() > 0) {
                                                            for (int i = 0; i < updatedIDs.length(); i++) {
                                                                Log.d("ScheduledService", "updzaatedIDS : " + updatedIDs.get(i));
                                                                Integer tempID = (Integer) updatedIDs.get(i);
                                                                Events events1 = bgRealm.where(Events.class).
                                                                        equalTo("eventID", (Integer) updatedIDs.get(i)).
                                                                        findFirst();
//                                                            equalTo("matchID", jsonMatch.getString("app_matchID")).

                                                                if (events1 == null) {
                                                                    Log.d("ScheduledService", "events 1 : " + events1);
                                                                } else {

                                                                    events1.setUndoArray(null);
                                                                    events1.setSyncstatus(1);
                                                                    bgRealm.copyToRealm(events1);
                                                                    Log.d("ScheduledService", "events 2 : " + events1);
                                                                    count = 0;
                                                                    sendRequestToServer();
                                                                }

                                                            }
                                                        }
                                                    }

//                                                serverSync();

                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                } catch (RealmPrimaryKeyConstraintException e) {
//                                                progress.dismiss();
                                                    Toast.makeText(getApplicationContext(),
                                                            "Primary Key exists, Press Update instead",
                                                            Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                                    } catch (RealmException e) {
                                        Log.d("ScheduledService", "Exception : " + e);
                                    } finally {
                                        if (realm != null) {
                                            realm.close();
                                        }
                                    }


                                /*Toast.makeText(getApplicationContext(),
                                        response.getString("message"), Toast.LENGTH_SHORT).show();*/

                                    Log.d("ScheduledService", response.getString("message"));

//

                                } else {
                                /*Toast.makeText(getApplicationContext(),
                                        response.getString("message"), Toast.LENGTH_SHORT).show();*/
                                    Log.d("ScheduledService", "error response : " + response.getString("message"));
                                }
                            } catch (JSONException e) {

//                            serverSync();
                                e.printStackTrace();
                                Log.d("ScheduledService", "scorig, " + e);
                            }


                        }

                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
//                        serverSync();
                            Log.e("ScheduledService", "Scoring, onErrorResponse  : " + error);

                        }
                    });

            MyApplicationClass.getInstance(getApplicationContext()).addToRequestQueue(jsonObjReq, "postRequest");
            Log.d("ScheduledService", "jsonObjReq  : " + jsonObjReq);
            Log.d("ScheduledService", "postparams  : " + postparams);

        }
    }


    // added on 27/07/2020

/*
    public void post() {
        Log.d("jsonUndoArray", "post() 3: ");

        jsonUndoArray = new JSONArray();
        int undo_count = 0;
         config = new RealmConfiguration.Builder()
                .name(AppConstants.GAME_ID + ".realm")
                .deleteRealmIfMigrationNeeded()
                .build();
        realm = Realm.getInstance(config);
        RealmResults<Undo> result1 = realm.where(Undo.class).
                equalTo("matchid", matchid).
                equalTo("innings", currentInnings).
                */
    /*equalTo("sync", 1).*//*
findAll();


        if (result1.isEmpty()) {
           */
/* if (isNetworkAvailable()) {
                resetJSONEventArray();*//*

            // commented on 08/07/2020
//                postJSON(matchid, matchID, false);
//            }
//            jsonEventArray = new JSONArray();
        }

        else {

//            if (isNetworkAvailable()) {
                */
/*RealmResults<Undo> result11 = realm.where(Undo.class).
                        equalTo("matchid", matchid).
                        equalTo("innings", currentInnings).findAll();*//*


//
            Log.d("jsonUndoArray", "post() 4 : " + result1);

            for (Undo undoObject : result1) {

                if (undo_count <= 50) {//10) {
                    ++undo_count;
                    Log.d("jsonUndoArray", "post() 1 : " + undoObject);
//                        if (undo.getSync() == 0) {


                    // added on 04/07/2020
                    jsonUndoArray.put(setUndo(undoObject));
//                            jsonUndoArray.put(setUndo(undoObject));


                    // commented on 04/07/2020
//                            setUndoArray(undo.getUndoID(), undo.getEventID(), undo.getInnings(), false, undo.isPreOut());
                    Log.d("jsonUndoArray", "post() 2 : " + jsonUndoArray);
//                        }
                }

//                    else
//                        break;

            }

            // commnted on 08/07/2020
//                postUndoJSON();
//            }
        }

//        if (isNetworkAvailable()) {

        Log.d("jsonUndoArray", "post() 5 , isNetworkAvailable : " + isNetworkAvailable());
        Log.d("jsonUndoArray", "post() 5 , jsonUndoArray : " + jsonUndoArray);


//            if (undo) { // added only to check the undo posting and event posting
//
//                undo = false;
        if (jsonUndoArray.length() > 0){

            Log.d("jsonUndoArray", "post() 6 , jsonUndoArray.length() : " + jsonUndoArray.length());
            Log.d("jsonUndoArray", "post() 7 , jsonUndoArray : " + jsonUndoArray);
            postUndoJSON();

        }
        else {
            resetJSONEventArray();
            if (jsonEventArray.length() > 0)
                postJSON(matchid, matchID, false);
        }
//        }
    }
*/


    public JSONObject setUndo(Undo undoObject) {

        Match match = realm.where(Match.class).
                equalTo("matchid", matchid).findFirst();

        int ub = 0, ulb = 0, uwd = 0, unb = 0, up = 0, uwk = 0;

        //for bowler

        JSONObject jsoncurrentbowler = new JSONObject();
        try {
            // commented on  16/07/2020
//                jsoncurrentbowler.put("id", undoObject.getBowler_pID());
            // added on 16/07/2020
            jsoncurrentbowler.put("playerid", undoObject.getBowler_pID());
            jsoncurrentbowler.put("run", undoObject.getBowlerRuns());
            // commented on  16/07/2020
            jsoncurrentbowler.put("over", undoObject.getBowlerOver());
            jsoncurrentbowler.put("balls", undoObject.getBowlerBalls());
            jsoncurrentbowler.put("wicket", undoObject.getBowlerWicket());
                /*// added on 16/07/2020
                jsoncurrentbowler.put("over",
//                        new DecimalFormat("##.#").format(
//                                Float.parseFloat(
                         undoObject.getBowlerOver() + "." + undoObject.getBowlerBalls());*/
            jsoncurrentbowler.put("fours", undoObject.getBowlerF4s());
            jsoncurrentbowler.put("sixes", undoObject.getBowlerS6s());  // --- till here
            jsoncurrentbowler.put("dots", undoObject.getBowlerDots());//bow.getDots());
            jsoncurrentbowler.put("maiden", undoObject.getBowlerMO());//bow.getMaidenOver());
            jsoncurrentbowler.put("wide", undoObject.getBowlerWides());//bow.getWides());
            jsoncurrentbowler.put("noball", undoObject.getBowlerNoball());//bow.getNoBalls());

        } catch (JSONException e) {
            e.printStackTrace();
        }


        JSONObject jsonotherendbowler = new JSONObject();

        if (undoObject.getPreBowler_pID() != 0) {

            try {
                // commented on  16/07/2020
//                    jsonotherendbowler.put("id", undoObject.getPreBowler_pID());
                // added on  16/07/2020
                jsonotherendbowler.put("playerid", undoObject.getPreBowler_pID());
                jsonotherendbowler.put("run", undoObject.getPreBowlerRuns());
                // commented on  16/07/2020
                jsonotherendbowler.put("over", undoObject.getPreBowlerOver());
                jsonotherendbowler.put("balls", undoObject.getPreBowlerBalls());
                jsonotherendbowler.put("wicket", undoObject.getPreBowlerWicket());
                   /* // added on 16/07/2020
                    jsonotherendbowler.put("over",
//                            new DecimalFormat("##.#").format(
//                                    Float.parseFloat(
                            undoObject.getPreBowlerOver() + "." + undoObject.getPreBowlerBalls());*/
                jsonotherendbowler.put("fours", undoObject.getPreBowlerF4s());
                jsonotherendbowler.put("sixes", undoObject.getPreBowlerS6s());//------ till here
                jsonotherendbowler.put("dots", undoObject.getPreBowlerDots());//bow.getDots());
                jsonotherendbowler.put("maiden", undoObject.getPreBowlerMO());//bow.getMaidenOver());
                jsonotherendbowler.put("wide", undoObject.getPreBowlerWides());//bow.getWides());
                jsonotherendbowler.put("noball", undoObject.getPreBowlerNoball());//bow.getNoBalls());


            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            try {
                // commented on  16/07/2020
//                    jsonotherendbowler.put("id", 0);
                // added on  16/07/2020
                jsonotherendbowler.put("id", 0);
                jsonotherendbowler.put("run", 0);
                // commented on  16/07/2020
                jsonotherendbowler.put("over", 0);
                jsonotherendbowler.put("balls", 0);
                jsonotherendbowler.put("wicket", 0);
                // added on  16/07/2020
                jsonotherendbowler.put("over", 0.0);
                jsonotherendbowler.put("fours", 0);
                jsonotherendbowler.put("sixes", 0); //------ till here
                jsonotherendbowler.put("dots", 0);
                jsonotherendbowler.put("maiden", 0);
                jsonotherendbowler.put("wide", 0);
                jsonotherendbowler.put("noball", 0);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }


        JSONObject jsonbowler = new JSONObject();
        try {
            // commented on  16/07/2020
//                jsonbowler.put("current", jsoncurrentbowler);
            // added on  16/07/2020
            jsonbowler.put("firstend", jsoncurrentbowler);
            jsonbowler.put("otherend", jsonotherendbowler);
            jsonbowler.put("bowlingeventid", undoObject.getUndo_last_event_id());  // Added on 04/08/2021
            // Added on 13/11/2021
            if ((undoObject.getBowlerRuns() == 0) &&
                    (undoObject.getBowlerOver() == 0) &&
                    (undoObject.getBowlerBalls() == 0) &&
                    (undoObject.getBowlerWicket() == 0) &&
                    (undoObject.getBowlerDots() == 0) &&
                    (undoObject.getBowlerMO() == 0) &&
                    (undoObject.getBowlerWides() == 0) &&
                    (undoObject.getBowlerNoball() == 0))
                jsonbowler.put("bowlerdeleteid", undoObject.getBowler_D4SID());
            else
                jsonbowler.put("bowlerdeleteid", 0);

            JSONArray bowlerarray = new JSONArray();
            RealmResults<Bowler> result_bowler = realm.where(Bowler.class).
                    equalTo("matchid", matchid).
                    equalTo("innings", currentInnings).
                    findAll();

            if (result_bowler.size() > 0) {
                Log.d("UB", "result_bowler = " + result_bowler);
                for (Bowler bowler : result_bowler) {
                    Log.d("UB", "result_bowler, bowler = " + bowler);
                    JSONObject b = new JSONObject();
                    Player p = realm.where(Player.class).
                            equalTo("matchid", matchid).
//                            equalTo("team", fieldingTeamNo).
        equalTo("playerID", (bowler.getPlayerID())).
                            findFirst();
                    Log.d("UB", "result_bowler, p = " + p);
                    if (p != null) {
                        try {
                            b.put("d4splayerid", p.getD4s_playerid());
                            bowlerarray.put(b);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
            jsonbowler.put("previousbowlerlist", bowlerarray);
            // == till here
        } catch (JSONException e) {
            e.printStackTrace();
        }


        // for batsman

        JSONObject jsonstriker = new JSONObject();
        try {
            // commented on 11/08/2020
//                jsonstriker.put("id", undoObject.getStriker_pID());
            // added on 11/08/2020
            jsonstriker.put("playerid", undoObject.getStriker_pID());
            // commented on 11/08/2020
//                jsonstriker.put("run", undoObject.getStrikerRuns());
            // added on 11/08/2020
            jsonstriker.put("score", undoObject.getStrikerRuns());
            jsonstriker.put("ball", undoObject.getStrikerBalls());

            if (undoObject.getStrikerBattingOrder() == 100)//bats.getBattingOrder() == 100)
                jsonstriker.put("battingorder", 0);
            else
                jsonstriker.put("battingorder", undoObject.getStrikerBattingOrder());//bats.getBattingOrder());
            jsonstriker.put("dots", undoObject.getStrikerDots());//bats.getDots());
            jsonstriker.put("fours", undoObject.getStrikerF4s());//bats.getF4s());
            jsonstriker.put("sixes", undoObject.getStrikerS6s());//bats.getS6s());
            // added on 11/07/2020 & updated 16/07/2020
            if (undoObject.getStrikerOutType() == -1)
                jsonstriker.put("outtype", "no");
            else
                jsonstriker.put("outtype",
                        setUndoOutType(undoObject.getStrikerOutType()));//undoObject.getStrikerOutType());


        } catch (JSONException e) {
            e.printStackTrace();
        }

        JSONObject jsonnonstriker = new JSONObject();
        try {
            // commented on 11/08/2020
//                jsonnonstriker.put("id", undoObject.getNonStriker_pID());
            // added on 11/08/2020
            jsonnonstriker.put("playerid", undoObject.getNonStriker_pID());
            // commented on 11/08/2020
//                jsonnonstriker.put("run", undoObject.getNonStrikerRuns());
            // added on 11/08/2020
            jsonnonstriker.put("score", undoObject.getNonStrikerRuns());
            jsonnonstriker.put("ball", undoObject.getNonStrikerBalls());

            if (undoObject.getNonStrikerBattingOrder() == 100)//bats.getBattingOrder() == 100)
                jsonnonstriker.put("battingorder", 0);
            else
                jsonnonstriker.put("battingorder", undoObject.getNonStrikerBattingOrder());//bats.getBattingOrder());
            jsonnonstriker.put("dots", undoObject.getNonStrikerDots());//bats.getDots());
            jsonnonstriker.put("fours", undoObject.getNonStrikerF4s());//bats.getF4s());
            jsonnonstriker.put("sixes", undoObject.getNonStrikerS6s());//bats.getS6s());
            // added on 11/07/2020 & updated 16/07/2020
            if (undoObject.getNonStrikerOutType() == -1)
                jsonnonstriker.put("outtype", "no");
            else
                jsonnonstriker.put("outtype",
                        setUndoOutType(undoObject.getNonStrikerOutType()));
            //undoObject.getNonStrikerOutType());

        } catch (JSONException e) {
            e.printStackTrace();
        }


        JSONObject jsonbatsman = new JSONObject();
        try {
            jsonbatsman.put("striker", jsonstriker);
            jsonbatsman.put("nonstriker", jsonnonstriker);
            jsonbatsman.put("battingeventid", undoObject.getUndo_last_event_id());  // Added on 04/08/2021
        } catch (JSONException e) {
            e.printStackTrace();
        }


        // for adding total extras on events

        ub = undoObject.getExtraBye();//extraCard.getByes();
        ulb = undoObject.getExtraLb();//extraCard.getLb();
        uwd = undoObject.getExtraWd();//extraCard.getWide();// * wideRun;
        unb = undoObject.getExtraNb();//extraCard.getNoBall();// * noballRun;
        up = undoObject.getExtraP();//extraCard.getPenalty();// * penaltyRun;

        JSONObject jsonExtra = new JSONObject();
        try {
            jsonExtra.put("inningsextras",
                    (ub +
                            ulb +
                            (uwd * match.getWiderun()) +
                            (unb * match.getNoballrun()) +
                            (up * match.getPenaltyrun())));
            jsonExtra.put("byes", ub);
            jsonExtra.put("legbyes", ulb);
            jsonExtra.put("wide", uwd);
            jsonExtra.put("noball", unb);
            jsonExtra.put("penalty", up);

        } catch (JSONException e) {
            e.printStackTrace();
        }


        // adding inningssummary

        JSONObject jsonSummery = new JSONObject();
        try {

            if (undoObject.isSUPER_OVER()) {
                if (undoObject.getInnings() == 99)
                    jsonSummery.put("innings", 1);
                else if (undoObject.getInnings() == 100)
                    jsonSummery.put("innings", 2);
            } else
                jsonSummery.put("innings", undoObject.getInnings());
            jsonSummery.put("totalovers", Double.parseDouble(
                    new DecimalFormat("###.#").format(undoObject.getTotalovers())));
            jsonSummery.put("totalscore", undoObject.getTotalscore());
            jsonSummery.put("totalwicket", undoObject.getTotalwicket());
            jsonSummery.put("extras", jsonExtra);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        // adding over summary
        JSONObject jsonOverSummary = new JSONObject();
        try {
            jsonOverSummary.put("runs", undoObject.getOverScore());
        } catch (JSONException e) {
            e.printStackTrace();
        }


        // getting fielderIDs
        JSONArray arrayFielder = new JSONArray();
        if (undoObject.getOuttype() == 1 || undoObject.getOuttype() == 3)
            arrayFielder.put(Integer.parseInt(undoObject.getFielderids()));
        else if (undoObject.getOuttype() == 2) {

            String[] arrSplit = undoObject.getFielderids().split(", ");
            for (int i = 0; i < arrSplit.length; i++) {

                arrayFielder.put(arrSplit[i]);
            }
        }
        Log.d("ScheduledService", "setEventJSON, arrayFielder : " + arrayFielder);


        if ((undoObject.getBalltype() == 3) && (undoObject.getOuttype() != 10))//ballType == 3 && outType != 10)
            uwk = undoObject.getTotalwicket();//wicket;
        else
            uwk = undoObject.getTotalwicket() + 1;//wicket + 1;

//        Log.d("Partnership", "setEventJSON : newPartnership : " + newPartnership);
//        Log.d("Partnership", "setEventJSON : wicket : " + wicket);
        Log.d("ScheduledService", "setEventJSON : uwk : " + uwk);


        JSONObject jsonFOW = new JSONObject();

        try {

            jsonFOW.put("wicketno", undoObject.getP_wicket_no());//partnership.getWicket());
            jsonFOW.put("partnershipsequence", undoObject.getP_sequence_no());//partnership.getPartnershipSequence());
            jsonFOW.put("partnershipruns", undoObject.getP_run());//partnership.getPartnershipRuns());
            jsonFOW.put("partnershipballs", undoObject.getP_ball());//partnership.getPartnershipBalls());
            jsonFOW.put("partnershipover", undoObject.getP_over());//partnership.getPartnershipOver())));
            jsonFOW.put("player1id", undoObject.getP_p1Id());//partnership.getPlayer1ID());
            jsonFOW.put("player2id", undoObject.getP_p2Id());//partnership.getPlayer2ID());
            jsonFOW.put("dismissedplayerid", undoObject.getP_disId());//partnership.getDismissedPlayerID());
            jsonFOW.put("player1contributedruns", undoObject.getP_p1cr());//partnership.getPlayer1ContributionRuns());
            jsonFOW.put("player1contributedballs", undoObject.getP_p1cb());//partnership.getPlayer1ContributionBalls());
            jsonFOW.put("player2contributedruns", undoObject.getP_p2cr());//partnership.getPlayer2ContributionRuns());
            jsonFOW.put("player2contributedballs", undoObject.getP_p2cb());//partnership.getPlayer2ContributionBalls());

            if (undoObject.getBalltype() != 3) {
                jsonFOW.put("partnershipbroken", "no");
            } else {
                if (undoObject.getOuttype() == 10)
                    jsonFOW.put("partnershipbroken", "no");
                else if (undoObject.getOuttype() != 10)
                    jsonFOW.put("partnershipbroken", "yes");
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }


        //adding session details
        JSONObject jsonSession = new JSONObject();
        try {
//            if (events.getBallType() == 13) {
            if (undoObject.getSessionId() == 7 || undoObject.getSessionId() == 9 || undoObject.getSessionId() == 11)
                jsonSession.put("type", "end");

            else if (undoObject.getSessionId() == 8 || undoObject.getSessionId() == 10 ||
                    undoObject.getSessionId() == 12 || undoObject.getSessionId() == 6)
                jsonSession.put("type", "start");

            if (undoObject.getSessionId() == 7 || undoObject.getSessionId() == 12 || undoObject.getSessionId() == 6)
                jsonSession.put("number", 1);
            else if (undoObject.getSessionId() == 8 || undoObject.getSessionId() == 9)
                jsonSession.put("number", 2);
            else if (undoObject.getSessionId() == 10 || undoObject.getSessionId() == 11)
                jsonSession.put("number", 3);

        } catch (JSONException e) {
            e.printStackTrace();
        }


        //adding interval details
        JSONObject jsonInterval = new JSONObject();
        try {
            if (undoObject.getBalltype() == 12)
                jsonInterval.put("id", undoObject.getIntervalId());
            else
                jsonInterval.put("id", 0);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // for substituion and concussion
        JSONObject jsonSubstitution = new JSONObject();
        if (undoObject.getBalltype() == 18 || undoObject.getBalltype() == 19) {

            try {

                jsonSubstitution.put("team", undoObject.getSub_team());//substitution.getTeam());
                jsonSubstitution.put("playerout_id", undoObject.getSub_playerout_id());//substitution.getPlayer_OUT_ID());
                jsonSubstitution.put("playerin_id", undoObject.getSub_playerin_id());//substitution.getPlayer_IN_ID());

            } catch (JSONException e) {
                e.printStackTrace();
            }
//            }
        }


        JSONObject jsonPowerplay = new JSONObject();

        try {
            jsonPowerplay.put("start_over", undoObject.getPower_start_over());//power.getStart());
            jsonPowerplay.put("end_over", undoObject.getPower_end_over());//power.getEnd());
            jsonPowerplay.put("type", null);//power.getType());
            jsonPowerplay.put("sequence", undoObject.getPower_sequence());//power.getCount());

        } catch (JSONException e) {
            e.printStackTrace();
        }


        JSONObject jsonInnings = new JSONObject();

        ub = undoObject.getExtraBye();
        ulb = undoObject.getExtraLb();
        uwd = undoObject.getExtraWd();
        unb = undoObject.getExtraNb();
        up = undoObject.getExtraP();


        try {
            jsonInnings.put("eventid", undoObject.getEventID());    // Added on 31/07/2021
            // Added on 02/08/2021
            if (undoObject.isSUPER_OVER()) {
                if (undoObject.getInnings() == 99)
                    jsonInnings.put("inningsnumber", 1);
                else if (undoObject.getInnings() == 100)
                    jsonInnings.put("inningsnumber", 2);
            } else
                jsonInnings.put("inningsnumber", undoObject.getInnings());
            // === till here
            jsonInnings.put("bye", undoObject.getExtraBye());
            jsonInnings.put("legbyes", undoObject.getExtraLb());
            jsonInnings.put("wide", undoObject.getExtraWd());
            jsonInnings.put("noball", undoObject.getExtraNb());
            jsonInnings.put("penalty", undoObject.getExtraP());
            jsonInnings.put("totalextras",
                    (ub +
                            ulb +
                            (uwd * match.getWiderun()) +
                            (unb * match.getNoballrun()) +
                            (up * match.getPenaltyrun())));
            jsonInnings.put("runs", undoObject.getRuns());
            jsonInnings.put("wicket", undoObject.getTotalwicket());
            jsonInnings.put("teamid", undoObject.getBatting_team());
            jsonInnings.put("over", Double.parseDouble(
                    new DecimalFormat("###.#").format(undoObject.getTotalovers())));
            jsonInnings.put("totalscore", undoObject.getTotalscore());
            jsonInnings.put("totalwicket", undoObject.getTotalwicket());

        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Added on 30/08/2021
        JSONObject jsonDismissedDetails = new JSONObject();
        try {
            jsonDismissedDetails.put("dismissedbatsmanid", undoObject.getDismissedbatsmanid());
            jsonDismissedDetails.put("dismissedbatsmanbattingorder", undoObject.getDismissedPlayerBattingOrder());
            jsonDismissedDetails.put("disnewbatsmanid", undoObject.getDisnewbatsmanid());
            jsonDismissedDetails.put("disnewbatsmanbattingorder", undoObject.getDisNewBatsmanBattingOrder());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //=========== till here

        JSONObject jsonUndo = new JSONObject();
        try {
            jsonUndo.put("matchID", undoObject.getMatchID());
            jsonUndo.put("eventid", undoObject.getEventID());
            jsonUndo.put("superover", undoObject.isSUPER_OVER());
            if (undoObject.isSUPER_OVER()) {
                if (undoObject.getInnings() == 99)
                    jsonUndo.put("inningsnumber", 1);
                else if (undoObject.getInnings() == 100)
                    jsonUndo.put("inningsnumber", 2);
            } else
                jsonUndo.put("inningsnumber", undoObject.getInnings());
            jsonUndo.put("battingteam", undoObject.getBatting_team());
            jsonUndo.put("over", undoObject.getOver());
            jsonUndo.put("ball", undoObject.getBall());
            jsonUndo.put("strikerid", undoObject.getStriker_pID());
            jsonUndo.put("nonstrikerid", undoObject.getNonStriker_pID());
            jsonUndo.put("bowlerid", undoObject.getBowler_pID());
            jsonUndo.put("balltype", undoObject.getBalltype());
            jsonUndo.put("innings", jsonInnings);


            if (undoObject.getDismissedbatsmanid() <= 0)
                jsonUndo.put("dismissedbatsmanid", 0);
            else
                jsonUndo.put("dismissedbatsmanid", undoObject.getDismissedbatsmanid());
            jsonUndo.put("disnewbatsmanid", undoObject.getDisnewbatsmanid());
            jsonUndo.put("outtype", undoObject.getOuttype());

            if (arrayFielder.length() == 0) {
                Log.d("ScheduledService", "setEventJSON 2, arrayFielder : " + arrayFielder);
                Log.d("ScheduledService", "setEventJSON 2, arrayFielder.length() : " + arrayFielder.length());
                jsonUndo.put("fielderids", 0);
            } else if (arrayFielder.length() > 0) {
                jsonUndo.put("fielderids", arrayFielder);
            }


            jsonUndo.put("extratype", undoObject.getExtraType());
            jsonUndo.put("freehit", undoObject.isFreehit());
            if (undoObject.getPenaltytype() == 1 || undoObject.getPenaltytype() == 2)
                jsonUndo.put("penaltybool", 1);
            else
                jsonUndo.put("penaltybool", 0);

            jsonUndo.put("penaltytype", undoObject.getPenaltytype());
            jsonUndo.put("runs", undoObject.getRuns());
            // commented on 08/07/2020
//                jsonUndo.put("extras", undoObject.getExtras());

            if (undoObject.getPenaltytype() > 0)
                jsonUndo.put("penaltyrun", undoObject.getPenalty()); // updated (penalty as penaltyrun) on 0/07/2020
            else
                jsonUndo.put("penaltyrun", 0);// updated (penalty as penaltyrun) on 0/07/202


            if (undoObject.getCommentary() != null)//|| !undoObject.getCommentary().matches(""))
                jsonUndo.put("commentary", undoObject.getCommentary());
            else
                jsonUndo.put("commentary", 0);


            jsonUndo.put("strokedirection", undoObject.getStrokedirection());

            // commented on 11/08/2020
//                jsonUndo.put("bowler", jsonbowler);
//                jsonUndo.put("batsman", jsonbatsman);
            // added on 11/08/2020
            jsonUndo.put("bowling", jsonbowler);
            jsonUndo.put("batting", jsonbatsman);

//                jsonUndo.put("inningssummary", jsonSummery);  // commented on 08/07/2020
            // added on 08/07/2020
            jsonUndo.put("extras", jsonExtra);

            if (undoObject.getBalltype() == 4 || undoObject.getBalltype() == 5)
                jsonUndo.put("oversummary", jsonOverSummary);

            // Added on 30/08/2021
            if (undoObject.getBalltype() == 7)
                jsonUndo.put("dismisseddetails", jsonDismissedDetails);
            // ==== till here

            jsonUndo.put("fallofwickets", jsonFOW);
            jsonUndo.put("session", jsonSession);
            jsonUndo.put("interval", jsonInterval);

            if (undoObject.getBalltype() == 18)
                jsonUndo.put("substituion", jsonSubstitution);
            else if (undoObject.getBalltype() == 19)
                jsonUndo.put("concussion", jsonSubstitution);

            else if (undoObject.getBalltype() == 21 || undoObject.getBalltype() == 22 || undoObject.getBalltype() == 23)
                jsonUndo.put("powerplay", jsonPowerplay);

            else if (undoObject.getBalltype() == 20) {
                jsonUndo.put("reducedover", match.getOver());
                if (undoObject.getInnings() == 2) {
                    jsonUndo.put("revisedtarget", undoObject.getRevisedTarget());
                    jsonUndo.put("appliedrainrule", undoObject.getAppliedRainRule());
                }

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }


        Log.d("ScheduledService", "setUndo() 1, jsonUndoArray : " + jsonUndoArray);
        Log.d("ScheduledService", "setUndo() 2, jsonUndo : " + jsonUndo);
        return jsonUndo;
    }


    private void resetJSONEventArray() {

        Log.d("ScheduledService", "resetJSONEventArray method called");

        count = 0;

        jsonUndoArray = new JSONArray();
        jsonEventArray = new JSONArray();
        config = new RealmConfiguration.Builder()
                .name(AppConstants.GAME_ID + ".realm")
                .deleteRealmIfMigrationNeeded()
                .build();
        realm = Realm.getInstance(config);

        RealmResults<Events> result = realm.where(Events.class).
                equalTo("matchid", matchid).
                equalTo("syncstatus", 1).findAll();

        if (result.isEmpty()) {

            RealmResults<Events> results = realm.where(Events.class).
                    equalTo("matchid", matchid).findAll();

            for (Events event : results) {

//                if (count <= 2){//50) {//10) {  // commented on 0/07/2021
                if (count <= 1) {
                    ++count;

                    Log.d("ScheduledService", "event 1 : " + event);
                    if (event.getSyncstatus() == 0) {
                        jsonEventArray.put(setEventJSON(event));
                        Log.d("ScheduledService", "resetJSONEventArray 1 : " + jsonEventArray);
                    }
                } else
                    break;
            }
        } else {


            Events events = result.last();

            RealmResults<Events> results = realm.where(Events.class).
                    equalTo("matchid", matchid).
                    greaterThan("eventID", events.getEventID()).findAll();

            for (Events event : results) {

//                if (count <= 2){//50) {//10) {  // commented on 0/07/2021
                if (count <= 1) {
                    ++count;

                    if (event.getSyncstatus() == 0) {
                        Log.d("ScheduledService", "event 2 : " + event);
                        jsonEventArray.put(setEventJSON(event));
                        Log.d("ScheduledService", "resetJSONEventArray 2 : " + jsonEventArray);
                    }
                } else
                    break;
            }

        }
    }


    //  addded on 27/07/2020

    private void postUndoJSON() {

        Log.d("ScheduledService", "called, matchID : " + matchID);
        Log.d("ScheduledService", "called, jsonUndoArray : " + jsonUndoArray);

        boolean matchSync = false, playerSync = false, tossSync = false;


        int msync = 0, psync = 0, tsync = 0, osync = 0;
        String capA = null, vcA = null, wkA = null, capB = null, vcB = null, wkB = null,
                umpire1_v = "", umpire2_v = "", umpire3_v = "", umpire4_v = "", match_referee_v = "";
        boolean post = false, player_post = false;

        Match match = realm.where(Match.class).
                equalTo("matchid", matchid).findFirst();
        Log.d("ScheduledService", "postJSON, matchid : " + matchid);
        Log.d("ScheduledService", "postJSON, match : " + match);

        if (isNetworkAvailable()) {


            JSONObject jsonball2 = new JSONObject();
            try {
                jsonball2.put("ball", jsonUndoArray);

            } catch (JSONException e) {
                e.printStackTrace();
            }

            JSONObject jsonbbb2 = new JSONObject();
            try {
                jsonbbb2.put("ballbyball", jsonball2);
            } catch (JSONException e) {
                e.printStackTrace();
            }


            JSONObject jsonmatchid = new JSONObject();
            try {
                jsonmatchid.put("matchID", matchID);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            JSONArray arrayAddBBB2 = new JSONArray();
            arrayAddBBB2.put(jsonmatchid);
            arrayAddBBB2.put(jsonbbb2);


            // adding values into feed
            JSONObject jsonFeed = new JSONObject();
            try {

                jsonFeed.put("UndoBBB", arrayAddBBB2);

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

            Log.d("ScheduledService", "post() 8 , postparams : " + postparams);

            JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                    Constants.CHASE_CRICKET_MATCH_UNDO_API,
                    postparams,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {

                            Log.e("ScheduledService", "response : " + response);

                            try {
                                JSONArray array = response.getJSONArray("eventid");
                                Log.e("ScheduledService", "array : " + array);
                                if (array.length() > 0) {


                                    for (int i = 0; i < array.length(); i++) {
                                        Log.d("array", " : " + array.get(i));
                                        Integer tempID = (Integer) array.get(i);
                                        Undo undo1 = realm.where(Undo.class).
                                                equalTo("eventID", (Integer) array.get(i)).
                                                findFirst();

                                        if (undo1 != null) {
                                            if (!realm.isInTransaction()) {
                                                realm.beginTransaction();
                                            }

                                            undo1.deleteFromRealm();
                                            realm.commitTransaction();
                                        }
                                    }

                                    sendRequestToServer();
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }

                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
//                            serverSync();
                            Log.e("ScheduledService", "postundo, onErrorResponse  : " + error);

                        }
                    });

            MyApplicationClass.getInstance(getApplicationContext()).addToRequestQueue(jsonObjReq, "postRequest");
            Log.d("ScheduledService", "jsonObjReq  : " + jsonObjReq);
            Log.d("ScheduledService", "postparams  : " + postparams);
        }

    }


    // added on 20/10/2020
    public void checkPlayers(Player player, /*int matchid, */String matchID, int team, boolean edit) {

        // added on 14/09/2020

        String captain = null, vice_captain = null, wicketkeeper = null;

        if (isNetworkAvailable()) {

            if (player != null) {
                Log.d("ADD", "player : " + player.toString());

                JSONArray arrayPlayerB = new JSONArray();
                JSONObject jsonPlayerB = new JSONObject();

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
//                    }
                    arrayPlayerB.put(jsonPlayerB);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                JSONArray subarray = new JSONArray();
                JSONArray subarray1 = new JSONArray();
                JSONArray parray = new JSONArray();
                JSONArray sarray = new JSONArray();

                JSONObject teampsobject = new JSONObject();
                JSONObject teamobject = new JSONObject();


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
                    } else {

                        if (player.isSubstitute()) {
                            jsonTeamB.put("substitutes", arrayPlayerB);
                        } else {
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
                        else if (player111.isViceCaptain())
                            vice_captain = player111.getPlayerName();

                        if (player111.isWicketKeeper())
                            wicketkeeper = player111.getPlayerName();
                    }

                    jsonTeamB.put("captain", captain);
                    jsonTeamB.put("vice captain", vice_captain);
                    jsonTeamB.put("wicketkeeper", wicketkeeper);

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
                        json_temp.put("substitutes", subarray1);//subarray);    // commented on 28/09/2020
                    jsonA.put("matchID", matchID);
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
                    } else {
                        if (player.getTeam() == 1) {
                            jsonB.put("TeamA", jsonTeamB);
                        } else {
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
                    if (edit)
                        jsonfeed.put("EditPlayers", array);
                    else {
                        // commented om 12/09/2020
//                        jsonfeed.put("AddPlayers", array);
                        // updated on 12/09/2020
                        if (team == 1) {
                            if (player.isSubstitute())
                                jsonfeed.put("AddTeamAPlayerSubstitute", array);
                            else
                                jsonfeed.put("AddNewTeamAPlayers", array);
                        } else if (team == 2) {
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
                Log.e("Scoring", "postparams : " + postparams);

                JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                        Constants.CHASE_CRICKET_MATCH_API, postparams,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {

                                Log.d("Scoring", "response : " + response);
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
                                                        } else {
                                                            Log.d("Scoring", "player not found, playerid : " +
                                                                    response.getInt("playerid"));
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
                                            Log.d("Scoring", "CheckPlayers, Exception : " + e);
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
                                Log.e("Scoring", "Error Message is  : " + error.getMessage());

                            }
                        });

                MyApplicationClass.getInstance(getApplicationContext()).addToRequestQueue(jsonObjReq, "postRequest");
                Log.d("Scoring", "jsonObjReq  : " + jsonObjReq);
                Log.d("Scoring", "postparams  : " + postparams);
            } else {
                Log.d("Scoring", "player : " + player);
            }
        }
    }


    // updated complete code on 12/11/2021
    public void postTeam(String teamA, String teamB) {

        if (isNetworkAvailable()) {

            JSONObject json_match = new JSONObject();
            JSONObject json_teamB = new JSONObject();
            JSONObject json_feed = new JSONObject();
            JSONArray array = new JSONArray();

            try {

                Match match = realm.where(Match.class).
                        equalTo("matchid", matchid).
                        findFirst();
                if (match != null) {

                    json_match.put("matchID", matchID);
                    json_match.put("TeamA", teamA);
                    json_match.put("TeamAID", match.getTeamAId());
                    json_match.put("TeamB", teamB);
                    json_match.put("TeamBID", match.getTeamBId());
                    array.put(json_match);
                    json_feed.put("EditTeamName", array);
                }
            } catch (JSONException e) {
                Log.d("EDIT_TEAM", "Scoring, JSONException : " + e);
            }


            JSONObject postparams = new JSONObject();
            try {
                postparams.put("title", "CHASE_POST");
                postparams.put("feed", json_feed);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            Log.e("EDIT_TEAM", "Scoring, postparams : " + postparams);

            JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                    Constants.CHASE_CRICKET_MATCH_API, postparams,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {

                            Log.d("EDIT_TEAM", "Scoring, response : " + response);

                            // Added on 11/11/2021
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
                                                    equalTo("matchID", matchID).
                                                    findFirst();
                                            if (match1 != null) {
                                                match1.setTeamA_sync(1);
                                                match1.setTeamB_sync(1);
                                                bgRealm.copyToRealm(match1);
                                                Log.d("EDIT_TEAM", "save, match : " + match1);
                                            }
                                        } catch (RealmPrimaryKeyConstraintException e) {
//                                            progress.dismiss();
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
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e("EDIT_TEAM", "Scoring, Error Message is  : " + error.getMessage());
                        }
                    });

            MyApplicationClass.getInstance(getApplicationContext()).addToRequestQueue(jsonObjReq, "postRequest");
            Log.d("EDIT_TEAM", "Scoring, jsonObjReq  : " + jsonObjReq);
            Log.d("EDIT_TEAM", "Scoring, postparams  : " + postparams);
        } else {
            Log.d("EDIT_TEAM", "Scoring, network not available");
        }
    }


    // added on 14/09/2020
/*
    private void postOfficialDetails() {

         config = new RealmConfiguration.Builder()
                .name(AppConstants.GAME_ID + ".realm")
                .deleteRealmIfMigrationNeeded()
                .build();
        realm = Realm.getInstance(config);
//        if (isNetworkAvailable()) {

            RealmResults<MatchOfficials> results = realm.where(MatchOfficials.class).
                    equalTo("matchid", matchid).
                    equalTo("sync", 0).findAll();

            Log.d("ADD_OFFICIALS", "Scoring, results 1 : " + results);
            if (results.isEmpty()) {

                Log.d("ADD_OFFICIALS", "Scoring, results : " + results);
            }

            else {

                JSONArray arrayOfficialsAdd = new JSONArray();
                JSONArray arrayOfficialsEdit = new JSONArray();

                Log.d("ADD_OFFICIALS", "Scoring, matchID : " + matchID);

                for (MatchOfficials officials : results) {

                    JSONObject jsonOfficialsAdd = new JSONObject();
                    JSONObject jsonOfficialsEdit = new JSONObject();

                    try {
                        if (officials.getEdit() == 1) { // added on 04/12/2020
                            if (!officials.getOfficialName().matches("")) {
                                jsonOfficialsAdd.put("name", officials.getOfficialName());

                                if (officials.getStatus().matches("u1") || officials.getStatus().matches("u2"))
                                    jsonOfficialsAdd.put("type", "u");
                                else
                                    jsonOfficialsAdd.put("type", officials.getStatus());

//                                if (officials.getD4s_id() == 0)
//                                    jsonOfficialsAdd.put("d4s_playerid", 0);
//                                else
                                jsonOfficialsAdd.put("d4s_playerid", officials.getD4s_id());

                                arrayOfficialsAdd.put(jsonOfficialsAdd);
                            }
                        }

                        // added on 04/12/2020
                        if (officials.getEdit() == 2) {
                            if (!officials.getOfficialName().matches("")) {
                                jsonOfficialsEdit.put("name", officials.getOfficialName());

                                if (officials.getStatus().matches("u1") || officials.getStatus().matches("u2"))
                                    jsonOfficialsEdit.put("type", "u");
                                else
                                    jsonOfficialsEdit.put("type", officials.getStatus());

//                                if (officials.getD4s_id() == 0)
//                                    jsonOfficialsEdit.put("d4s_playerid", 0);
//                                else
                                jsonOfficialsEdit.put("d4s_playerid", officials.getD4s_id());

                                arrayOfficialsEdit.put(jsonOfficialsEdit);
                            }
                        } // =========== till here

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

                // Added on 10/05/2021
                if (arrayOfficialsEdit.length() > 0)
                    postEdit(arrayOfficialsEdit);
                if (arrayOfficialsAdd.length() > 0)
                    postAdd(arrayOfficialsAdd);
                // === till here

            }

    }
*/


    // added on 05/10/2021
    // updated on 07/10/2021
    // to post edit details
    public void postEdit(MatchOfficials officials) {
        final String TAG = "USA";
        // added on 11/11/2021
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


    private void postAdd() {
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

//                                if (officials.getD4s_id() == 0)
//                                    jsonOfficials.put("d4s_playerid", 0);
//                                else
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

    // ==== till here


    // added on 19/10/2020
    public void postMatch(Match match) {

        if (isNetworkAvailable()) {
            // posting match details if not synced
            JSONObject jsonMatch = new JSONObject();

            try {
                jsonMatch.put("d4s_gameid", match.getD4s_matchid());
                jsonMatch.put("d4s_userid", match.getD4s_userid());

                jsonMatch.put("matchID", match.getMatchID());
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
                jsonMatch.put("playersA", match.getPlayerA());
                jsonMatch.put("playersB", match.getPlayerB());
                jsonMatch.put("overs_per_bowler", match.getMax_opb());
//                jsonMatch.put("players", match.getPlayer());
//                jsonMatch.put("substitute_players", match.getSubst());
                jsonMatch.put("over", match.getActual_over());
                jsonMatch.put("balls_per_over", match.getBalls());
                jsonMatch.put("wide_value", match.getWiderun());
                jsonMatch.put("noball_value", match.getNoballrun());
                jsonMatch.put("penalty_value", match.getPenaltyrun());
                jsonMatch.put("rainruleused", "n");
                jsonMatch.put("max_overs_per_bowler", match.getMax_opb());
                jsonMatch.put("max_balls_per_bowler", match.getMax_bpb());

            } catch (JSONException e) {
                e.printStackTrace();
            }

            JSONObject jsonfeed = new JSONObject();
            try {
                jsonfeed.put("AddMatch", jsonMatch);
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

                            Log.d("EDIT_TEAM", "Scoring, response : " + response);

                            // Added on 02/08/2021
                            try {
                                if (!response.getBoolean("error") && response.getInt("status") == 200) {

                                    JSONObject jsonMatch = response.getJSONObject("match");
                                    String MATCHID = jsonMatch.getString("app_matchID");
                                    int teamA_id = jsonMatch.getInt("team1_id");
                                    int teamB_id = jsonMatch.getInt("team2_id");
                                    Log.d("create", "login(u,p), jsonMatch : " + jsonMatch);
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
                                                    Log.e("SS", "postMatch, RealmPrimaryKeyConstraintException = " + e);
                                                }
                                            }
                                        });
                                    } catch (RealmException e) {
                                        Log.d("SS", "postMatch, RealmException : " + e);
                                    } finally {
                                        if (realm != null) {
                                            realm.close();
                                        }
                                    }
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            // === till here
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

//                                progress.dismiss();
                            Log.e("EDIT_TEAM", "Scoring, Error Message is  : " + error.getMessage());

                        }
                    });

            MyApplicationClass.getInstance(getApplicationContext()).addToRequestQueue(jsonObjReq, "postRequest");
            Log.d("EDIT_TEAM", "Scoring, jsonObjReq  : " + jsonObjReq);
            Log.d("EDIT_TEAM", "Scoring, postparams  : " + postparams);

        } else {
            Log.d("EDIT_TEAM", "Scoring, network not available");
        }
    }


    // updated on on 20/10/2020
    public void postMatchNote() {

        if (isNetworkAvailable()) {

            // updated on 20/10/2020

            Match match = realm.where(Match.class).
                    equalTo("matchid", matchid).findFirst();

            if (match.getMatchSync() == 0) {
                postJSON(matchid, matchID/*, false*/);    // need to check
            }

            RealmResults<MatchNotes> notes_result = realm.where(MatchNotes.class).
                    equalTo("matchid", matchid).
                    equalTo("sync", 0).findAll();

//            JSONObject json_matchID = new JSONObject();
            JSONObject json_notes = new JSONObject();
            JSONArray array_notes = new JSONArray();

            if (notes_result.size() > 0) {

                /*try {
                    json_matchID.put("matchID", matchID);
                } catch (JSONException e) {
                    Log.d("MATCH_NOTE", "Scoring, JSONException : " + e);
                }
                */
                for (MatchNotes matchNotes : notes_result) {

                    try {
                        json_notes.put("sequence", matchNotes.getSequence());
                        json_notes.put("innings", matchNotes.getInnings());
                        json_notes.put("over", (new DecimalFormat("##.#").format(matchNotes.getOver())));
//                        json_notes.put("over", (int) matchNotes.getOver());
//                        json_notes.put("balls", ((matchNotes.getOver()) % ((int) matchNotes.getOver())));
                        json_notes.put("note", matchNotes.getNote());

                        array_notes.put(json_notes);

                    } catch (JSONException e) {
                        Log.d("MATCH_NOTE", "Scoring, JSONException : " + e);
                    }
                }

                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("matchID", matchID);
                    jsonObject.put("matchnotes", array_notes);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                JSONObject jsonfeed = new JSONObject();

                try {
                    jsonfeed.put("AddMatchNote", jsonObject);
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
                                Log.e("MATCH_NOTE", "Scoring, Error Message is  : " + error.getMessage());
                            }
                        });

                MyApplicationClass.getInstance(getApplicationContext()).addToRequestQueue(jsonObjReq, "postRequest");
                Log.d("MATCH_NOTE", "Scoring, jsonObjReq  : " + jsonObjReq);
                Log.d("MATCH_NOTE", "Scoring, postparams  : " + postparams);
            } else {
                Log.d("MATCH_NOTE", "Scoring, no matchnote to sync");
            }
        } else {
            Log.d("MATCH_NOTE", "Scoring, network not available");
        }
    }

    // Added on 01/07/2021
    private String setUndoOutType(int id) {
        String outType = "no";
       /* if (id == -1) {
            outType = "no";
        } else */
        if (id == 0) {
            outType = "b";//"Bowled";
        } else if (id == 1) {
            outType = "c";//Caught";
        } else if (id == 2) {
            outType = "ro";//Runout";
        } else if (id == 3) {
            outType = "st";//Stumped";
        } else if (id == 4) {
            outType = "lb";//LBW";
        } else if (id == 5) {
            outType = "hw";//Hit Wicket";
        } else if (id == 6) {
            outType = "ht";//Hit the Ball Twice";
        } else if (id == 7) {
            outType = "ob";//Obstructing Field";
        } else if (id == 8) {
            outType = "hb";//Handled the Ball";
        } else if (id == 9) {
            outType = "to";//Timed Out";
        } else if (id == 10) {
            outType = "rh";//Retired Hurt";
        } else if (id == 11) {
            outType = "a";//Absent";
        } else if (id == 12) {
            outType = "ro";//Retired Out";
        } else {
            outType = "no";
        }
        return outType;
    }


    // Added on 02/08/2021
    private void postNewMatch() {

        Match match123 = realm.where(Match.class).
                equalTo("matchid", matchid).
                findFirst();

        if (match123 != null) {
            // posting match
            if (match123.getMatchSync() != 1) {
                postMatch(match123);
            }
        }
    }


   /* private void postOfficials() {

        Match match123 = realm.where(Match.class).
                equalTo("matchid", matchid).
                findFirst();

        if (match123 != null) {
            if (match123.getMatchSync() != 1) {
                postMatch(match123);
            }

            RealmResults<MatchOfficials> result_officials = realm.where(MatchOfficials.class).
                    equalTo("matchid", matchid).
                    equalTo("sync", 0).findAll();
            if (result_officials.size() > 0) {
                postOfficialDetails();
            }
        }
    }*/


    private void postNewPlayer() {

        RealmResults<Player> resultsA = realm.where(Player.class).
                equalTo("matchid", matchid).
                equalTo("team", 1).findAll();

        int count_A = realm.where(Player.class).
                equalTo("matchid", matchid).
                equalTo("team", 1).
                equalTo("sync", 1).findAll().size();

        Log.d("POST()", "scoring 1, resultsA.size() : " + resultsA.size());
        Log.d("POST()", "scoring 1, count_A : " + count_A);

        if (resultsA.size() > count_A) {

            RealmResults<Player> results1 = realm.where(Player.class).
                    equalTo("matchid", matchid).
                    equalTo("team", 1).
                    equalTo("edit", false).
                    equalTo("sync", 0).findAll();

            for (Player player : results1) {

                checkPlayers(player, /*matchid,*/ matchID, 1, false);
            }

            RealmResults<Player> results2 = realm.where(Player.class).
                    equalTo("matchid", matchid).
                    equalTo("team", 1).
                    equalTo("edit", true).
                    equalTo("sync", 0).findAll();

            for (Player player : results2) {

                checkPlayers(player,/* matchid, */matchID, 1, true);
            }
        }

        // for team B
        RealmResults<Player> resultsB = realm.where(Player.class).
                equalTo("matchid", matchid).
                equalTo("team", 2).findAll();

        int count_B = realm.where(Player.class).
                equalTo("matchid", matchid).
                equalTo("team", 2).
                equalTo("sync", 1).findAll().size();

        Log.d("POST()", "scoring 2, resultsB.size() : " + resultsB.size());
        Log.d("POST()", "scoring 2, count_B : " + count_B);

        if (resultsB.size() > count_B) {

            RealmResults<Player> results1 = realm.where(Player.class).
                    equalTo("matchid", matchid).
                    equalTo("team", 2).
                    equalTo("edit", false).
                    equalTo("sync", 0).findAll();

            for (Player player : results1) {

                checkPlayers(player,/* matchid, */matchID, 2, false);
            }

            RealmResults<Player> results2 = realm.where(Player.class).
                    equalTo("matchid", matchid).
                    equalTo("team", 2).
                    equalTo("edit", true).
                    equalTo("sync", 0).findAll();

            for (Player player : results2) {

                checkPlayers(player, /*matchid,*/ matchID, 2, true);
            }
        }
    }


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
                } else {
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


    private void postEvents() {

        jsonUndoArray = new JSONArray();
        int undo_count = 0;
        config = new RealmConfiguration.Builder()
                .name(AppConstants.GAME_ID + ".realm")
                .deleteRealmIfMigrationNeeded()
                .build();
        realm = Realm.getInstance(config);
        RealmResults<Undo> result1 = realm.where(Undo.class).
                equalTo("matchid", matchid).
                equalTo("innings", currentInnings).
                findAll();

        if (result1.size() > 0) {//!result1.isEmpty()) {

            Log.d("ScheduledService", "post() 4 : " + result1);

            for (Undo undoObject : result1) {

//                if (undo_count <= 50) {//10) {  // commented on 0/07/2021
                if (undo_count <= 0) {//10) {
                    ++undo_count;
                    Log.d("ScheduledService", "post() 1 : " + undoObject);

                    jsonUndoArray.put(setUndo(undoObject));

                    Log.d("ScheduledService", "post() 2 : " + jsonUndoArray);
                    postUndoJSON(); // Added on 19/11/2021
                }
            }
        }

       /* Commented on 19/11/2021
        Log.d("ScheduledService", "post() 5 , isNetworkAvailable : " + isNetworkAvailable());
        Log.d("ScheduledService", "post() 5 , jsonUndoArray : " + jsonUndoArray);

        if (jsonUndoArray.length() > 0){

            Log.d("ScheduledService", "post() 6 , jsonUndoArray.length() : " + jsonUndoArray.length());
            Log.d("ScheduledService", "post() 7 , jsonUndoArray : " + jsonUndoArray);
            postUndoJSON();

        }*/
        else {
            resetJSONEventArray();
            if (jsonEventArray.length() > 0) {
                postJSON(matchid, matchID/*, false*/);
            }
        }
    }


   /* private void UpdateTeamName() {
        Match match1 = realm.where(Match.class).
                equalTo("matchid", matchid).findFirst();

        if (match1 != null) {

            if (match1.getTeamA_sync() == 0)
                postTeam(1, match1.getTeamA(), match1.getTeamB());

            if (match1.getTeamB_sync() == 0)
                postTeam(2, match1.getTeamB(), match1.getTeamA());
        }
    }*/


    private void postPlayer() {

        String capA = "", capB = "", vcA = "", vcB = "", wkA = "", wkB = "";

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
                    if (player.isCaptain()) {
                        jsonPlayerA.put("captain", "y");
                        capA = player.getPlayerName();
                    } else
                        jsonPlayerA.put("captain", "n");
                    if (player.isViceCaptain()) {
                        jsonPlayerA.put("vice_captain", "y");
                        vcA = player.getPlayerName();
                    } else
                        jsonPlayerA.put("vice_captain", "n");
                    if (player.isWicketKeeper()) {
                        jsonPlayerA.put("wicketkeeper", "y");
                        wkA = player.getPlayerName();
                    } else
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
            Log.e("captain", "team1 substitutes : " + results);
            JSONArray arraySubA = new JSONArray();

            for (Player player : results) {

                jsonPlayerA = new JSONObject();

                try {
                    jsonPlayerA.put("name", player.getPlayerName());
                    jsonPlayerA.put("player_id", player.getPlayerID());
                    jsonPlayerA.put("d4s_playerid", player.getD4s_playerid());
                    if (player.isCaptain()) {
                        jsonPlayerA.put("captain", "y");
                        capA = player.getPlayerName();
                    } else
                        jsonPlayerA.put("captain", "n");
                    if (player.isViceCaptain()) {
                        jsonPlayerA.put("vice_captain", "y");
                        vcA = player.getPlayerName();
                    } else
                        jsonPlayerA.put("vice_captain", "n");
                    if (player.isWicketKeeper()) {
                        jsonPlayerA.put("wicketkeeper", "y");
                        wkA = player.getPlayerName();
                    } else
                        jsonPlayerA.put("wicketkeeper", "n");
                    arraySubA.put(jsonPlayerA);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            JSONObject jsonTeamA = new JSONObject();
            try {
//                if (!squad) {
                jsonTeamA.put("players", arrayPlayerA);
                jsonTeamA.put("substitutes", arraySubA);
//                }
                jsonTeamA.put("captain", capA);
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
                    if (player.isCaptain()) {
                        jsonPlayerB.put("captain", "y");
                        capB = player.getPlayerName();
                    } else
                        jsonPlayerB.put("captain", "n");
                    if (player.isViceCaptain()) {
                        jsonPlayerB.put("vice_captain", "y");
                        vcB = player.getPlayerName();
                    } else
                        jsonPlayerB.put("vice_captain", "n");
                    if (player.isWicketKeeper()) {
                        jsonPlayerB.put("wicketkeeper", "y");
                        wkB = player.getPlayerName();
                    } else
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
            Log.e("captain", "team2 substitutes : " + results);
            JSONArray arraySubB = new JSONArray();

            for (Player player : results) {

                jsonPlayerB = new JSONObject();

                try {
                    jsonPlayerB.put("name", player.getPlayerName());
                    jsonPlayerB.put("player_id", player.getPlayerID());
                    jsonPlayerB.put("d4s_playerid", player.getD4s_playerid());
                    if (player.isCaptain()) {
                        jsonPlayerB.put("captain", "y");
                        capB = player.getPlayerName();
                    } else
                        jsonPlayerB.put("captain", "n");
                    if (player.isViceCaptain()) {
                        jsonPlayerB.put("vice_captain", "y");
                        vcB = player.getPlayerName();
                    } else
                        jsonPlayerB.put("vice_captain", "n");
                    if (player.isWicketKeeper()) {
                        jsonPlayerB.put("wicketkeeper", "y");
                        wkB = player.getPlayerName();
                    } else
                        jsonPlayerB.put("wicketkeeper", "n");
                    arraySubB.put(jsonPlayerB);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            JSONObject jsonTeamB = new JSONObject();
            try {
//                if (!squad) {
                jsonTeamB.put("players", arrayPlayerB);
                jsonTeamB.put("substitutes", arraySubB);
//                }
                jsonTeamB.put("captain", capB);
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

                                                        // for players
                                                        RealmResults<Player> resultsPlayer = bgRealm.where(Player.class).
                                                                equalTo("matchID", jsonMatch.getString("app_matchID")).
                                                                findAll();

                                                        for (Player player : resultsPlayer) {

                                                            player.setSync(1);
                                                            bgRealm.copyFromRealm(player);
                                                            Log.e("RESPONSE", "Captain, synced player : " + player.toString());
                                                        }


                                                        match1.setAddPlayers(1);
                                                        bgRealm.copyToRealm(match1);
                                                        Log.d("matchSync", "captain, match : " + match1);
                                                    } else
                                                        Log.e("RESPONSE", "Captain, match not found");

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

                                } else {

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


    // Added on 08/10/2021
    // for result
    private void postJSONResult() {

        if (isNetworkAvailable()) {

            Match match = realm.where(Match.class).equalTo("matchid", matchid).findFirst();

            if (match != null) {

                Log.d("result", "postJSON 1, match : " + match);

                JSONObject jsonObject = new JSONObject();

                try {
                    jsonObject.put("matchID", match.getMatchID());
                    jsonObject.put("type", match.getType());
                    jsonObject.put("margin", match.getMargin());
                    jsonObject.put("winner", match.getWinner_team());
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                JSONObject jsonFeed = new JSONObject();
                try {
                    if (match.getEditResult() == 0)
                        jsonFeed.put("MatchResult", jsonObject);
                    else
                        jsonFeed.put("EditMatchResult", jsonObject);
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
                        Constants.CHASE_CRICKET_MATCH_API,
                        postparams,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {

                                Log.e("result", "response : " + response);
                                // updated the match model
                                //                                    JSONObject jsonMatch = response.getJSONObject("match");

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
                                                                response.getString("match")).
                                                        findFirst();

                                                if (match1 != null) {

                                                    match1.setResult_sync(1);
                                                    match1.setEditResult(0);
                                                    bgRealm.copyToRealm(match1);
                                                    Log.d("matchSync", "captain, match : " + match1);
                                                } else
                                                    Log.e("RESPONSE", "Captain, match not found");

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

                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {

                                Log.e("result", "volley, onErrorResponse  : " + error);
                            }
                        });

                MyApplicationClass.getInstance(getApplicationContext()).addToRequestQueue(jsonObjReq, "postRequest");
                Log.d("scoring", "jsonObjReq  : " + jsonObjReq);
                Log.d("scoring", "postparams  : " + postparams);


            }

        }
    }


    // for adding man of the match
    private void postJSONMOM() {

        if (isNetworkAvailable()) {

            Match match = realm.where(Match.class).equalTo("matchid", matchid).findFirst();

            if (match != null) {

                Log.d("result", "postJSON 1, match : " + match);

                JSONObject jsonObject = new JSONObject();

                try {
                    jsonObject.put("matchID", match.getMatchID());
                    jsonObject.put("d4s_playerid", match.getMom_d4sID());
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                JSONArray array = new JSONArray();
                array.put(jsonObject);

                JSONObject jsonFeed = new JSONObject();
                try {
                    if (match.getEditMOM() == 0)
                        jsonFeed.put("AddManOfTheMatch", array);
                    else
                        jsonFeed.put("EditManOfTheMatch", array);
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
                        Constants.CHASE_CRICKET_MATCH_API,
                        postparams,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {

                                Log.e("result", "response : " + response);
                                // update match details
                                try {
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
                                                                    jsonMatch.getString("matchid")).
                                                            findFirst();

                                                    if (match1 != null) {

                                                        match1.setMom_sync(1);
                                                        match1.setEditMOM(0);
                                                        bgRealm.copyToRealm(match1);
                                                        Log.d("matchSync", "captain, match : " + match1);
                                                    } else
                                                        Log.e("RESPONSE", "Captain, match not found");

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

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }

                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {

                                Log.e("result", "volley, onErrorResponse  : " + error);
                            }
                        });

                MyApplicationClass.getInstance(getApplicationContext()).addToRequestQueue(jsonObjReq, "postRequest");
                Log.d("scoring", "jsonObjReq  : " + jsonObjReq);
                Log.d("scoring", "postparams  : " + postparams);


            }

        }
    }


    // Added on 11/11/2021
    private void postOfficialDeletion(MatchOfficials officials) {
        final String TAG = "USAD";

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

                            }
                        }
                    } catch (RealmException e) {
                        Toast.makeText(getApplicationContext(),
                                " " + e, Toast.LENGTH_SHORT).show();
                    }
                }
            });

        } catch (RealmException e) {
            Log.d("TAG", "Exception : " + e);
        } finally {
            if (realm != null) {
                realm.close();
            }
        }
    }


    // Added on 12/11/2021
    private void postPlayers() {

        String capA = "", vcA = "", wkA = "", capB = "", vcB = "", wkB = "";

        if (isNetworkAvailable()) {
            // starting of adding Team A players
            JSONObject jsonPlayerA;

            // for adding players
            RealmResults<Player> results = realm.where(Player.class).
                    equalTo("matchid", matchid).
                    equalTo("team", 1).
                    equalTo("edit", false).
                    equalTo("substitute", false).findAll();
            Log.e("captain", "team1 players : " + results);
            JSONArray arrayPlayerA = new JSONArray();

            for (Player player : results) {

                jsonPlayerA = new JSONObject();

                try {
                    jsonPlayerA.put("name", player.getPlayerName());
                    jsonPlayerA.put("player_id", player.getPlayerID());
                    jsonPlayerA.put("d4s_playerid", player.getD4s_playerid());
                    if (player.isCaptain()) {
                        jsonPlayerA.put("captain", "y");
                        capA = player.getPlayerName();
                    } else
                        jsonPlayerA.put("captain", "n");
                    if (player.isViceCaptain()) {
                        jsonPlayerA.put("vice_captain", "y");
                        vcA = player.getPlayerName();
                    } else
                        jsonPlayerA.put("vice_captain", "n");
                    if (player.isWicketKeeper()) {
                        jsonPlayerA.put("wicketkeeper", "y");
                        wkA = player.getPlayerName();
                    } else
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
                    equalTo("edit", false).
                    equalTo("substitute", true).findAll();
            Log.e("captain", "team1 substitutes : " + results);
            JSONArray arraySubA = new JSONArray();

            for (Player player : results) {

                jsonPlayerA = new JSONObject();

                try {
                    jsonPlayerA.put("name", player.getPlayerName());
                    jsonPlayerA.put("player_id", player.getPlayerID());
                    jsonPlayerA.put("d4s_playerid", player.getD4s_playerid());
                    if (player.isCaptain()) {
                        jsonPlayerA.put("captain", "y");
                        capA = player.getPlayerName();
                    } else
                        jsonPlayerA.put("captain", "n");
                    if (player.isViceCaptain()) {
                        jsonPlayerA.put("vice_captain", "y");
                        vcA = player.getPlayerName();
                    } else
                        jsonPlayerA.put("vice_captain", "n");
                    if (player.isWicketKeeper()) {
                        jsonPlayerA.put("wicketkeeper", "y");
                        wkA = player.getPlayerName();
                    } else
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
                jsonTeamA.put("captain", capA);
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
                    equalTo("edit", false).
                    equalTo("substitute", false).findAll();
            Log.e("captain", "team2 players : " + results);
            JSONArray arrayPlayerB = new JSONArray();

            for (Player player : results) {

                jsonPlayerB = new JSONObject();

                try {
                    jsonPlayerB.put("name", player.getPlayerName());
                    jsonPlayerB.put("player_id", player.getPlayerID());
                    jsonPlayerB.put("d4s_playerid", player.getD4s_playerid());
                    if (player.isCaptain()) {
                        jsonPlayerB.put("captain", "y");
                        capB = player.getPlayerName();
                    } else
                        jsonPlayerB.put("captain", "n");
                    if (player.isViceCaptain()) {
                        jsonPlayerB.put("vice_captain", "y");
                        vcB = player.getPlayerName();
                    } else
                        jsonPlayerB.put("vice_captain", "n");
                    if (player.isWicketKeeper()) {
                        jsonPlayerB.put("wicketkeeper", "y");
                        wkB = player.getPlayerName();
                    } else
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
                    equalTo("edit", false).
                    equalTo("substitute", true).findAll();
            Log.e("captain", "team2 substitutes : " + results);
            JSONArray arraySubB = new JSONArray();

            for (Player player : results) {

                jsonPlayerB = new JSONObject();

                try {
                    jsonPlayerB.put("name", player.getPlayerName());
                    jsonPlayerB.put("player_id", player.getPlayerID());
                    jsonPlayerB.put("d4s_playerid", player.getD4s_playerid());
                    if (player.isCaptain()) {
                        jsonPlayerB.put("captain", "y");
                        capA = player.getPlayerName();

                    } else
                        jsonPlayerB.put("captain", "n");
                    if (player.isViceCaptain()) {
                        jsonPlayerB.put("vice_captain", "y");
                        vcB = player.getPlayerName();
                    } else
                        jsonPlayerB.put("vice_captain", "n");
                    if (player.isWicketKeeper()) {
                        jsonPlayerB.put("wicketkeeper", "y");
                        wkB = player.getPlayerName();
                    } else
                        jsonPlayerB.put("wicketkeeper", "n");
                    arraySubB.put(jsonPlayerB);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            JSONObject jsonTeamB = new JSONObject();
            try {
//                if (!squad) {
                jsonTeamB.put("players", arrayPlayerB);
                jsonTeamB.put("substitutes", arraySubB);
//                }
                jsonTeamB.put("captain", capB);
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

                                                        match1.setPlayer_post(true);
                                                        match1.setStatus("CVW");
                                                        match1.setStatusId(3);
                                                        match1.setPlayerSync(1);
                                                        ;

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
                                } else {
                                    Log.d("TOSS", "data saved wrongly");
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


    private void postToss(Match match) {

        if (match != null) {

            if (isNetworkAvailable()) {

                String tossWinner = match.getToss_winner();
                String decision = match.getDecision();

                // added on 19/10/2020
                JSONObject jsonToss = new JSONObject();
                try {
                    jsonToss.put("matchID", matchID);
                    if (match.isNoToss() || match.isUnknownToss()) {
                        jsonToss.put("winner", -1);
                        jsonToss.put("decision", decision);
                        jsonToss.put("battingteam", tossWinner);
                    } else {
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
                Log.e("toss", "jsonFeed : " + jsonFeed);

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


                                Log.e("captain", "response : " + response);

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

                                                        Match match1 = bgRealm.where(Match.class).
                                                                equalTo("matchID",
                                                                        jsonMatch.getString("app_matchID")).
                                                                findFirst();

                                                        if (match1 != null) {

                                                            match1.setStatus("TC");
                                                            match1.setStatusId(4);
                                                            match1.setTossSync(1);

                                                            bgRealm.copyToRealm(match1);

                                                            Log.d("matchSync", "toss, match1 : " + match1);
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

                                    } else {
                                        Log.d("USA", "postToss, wrong data");
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
                                Log.e("captain", "Error Message is  : " + error.getMessage());
                            }
                        });

                MyApplicationClass.getInstance(getApplicationContext()).addToRequestQueue(jsonObjReq, "postRequest");
                Log.d("toss", "jsonObjReq  : " + jsonObjReq);
                Log.d("toss", "postparams  : " + postparams);
            }
        }
    }


    // Added on 27/11/2021
    void postPlayersAdd(int team, String matchID, int d4s_team_id) {

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
