package com.data4sports.chasecricket.activities;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.Looper;
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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.data4sports.chasecricket.R;
import com.data4sports.chasecricket.adapters.AssignedListAdapter;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Timer;

import com.data4sports.chasecricket.adapters.AssignedListAdapter.customButtonListener;
import com.data4sports.chasecricket.applicationConstants.AppConstants;
import com.data4sports.chasecricket.applicationConstants.utils.FileUtils;
import com.data4sports.chasecricket.models.Constants;
import com.data4sports.chasecricket.models.Events;
import com.data4sports.chasecricket.models.Match;
import com.data4sports.chasecricket.models.MidGen;
import com.data4sports.chasecricket.models.Player;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;
import io.realm.exceptions.RealmException;
import io.realm.exceptions.RealmPrimaryKeyConstraintException;


public class AssignedMatchListActivity extends AppCompatActivity implements customButtonListener {

    ArrayList<String> dataTeams;

    //Deepak code starts
    ArrayList<Integer> midScore;
    ArrayList<Integer> gameIdArray;
    //Deepak Code ends
    AssignedListAdapter adapter;
    JSONObject eventObject, matchObject, tempObject, playerObject;
    JSONArray teamA_players, teamB_players;

    TextView tv_gameid, tv_teamA, tv_teamB, tv_event, tv_phase, tv_venue, tv_matchDate, tv_matchType,
            tv_umpire1, tv_umpire2, tv_umpire3, tv_umpire4, tv_matchReferee;
    Button btn_save, btn_pull_squad;
    ImageButton back, back1;
    PopupWindow popupMatchDetails;
    LinearLayout linearLayout;
    String token = null;
    boolean flag = false, checkFlag = true, midScoringFlag = false, playersFlag = false, inningsStarted = false, limited_overs = false;
    ;

    String matchID = null, teamA = null, teamB = null, venue = null, end1 = null, end2 = null, phase = null,
            event = null, matchType = null, innings = null, umpire1 = null, umpire2 = null, umpire3 = null,
            umpire4 = null, scorer = null, matchReferee = null, name = null, matchDate = null;

    int matchid = 0, playerA = 0, playerB = 0, over = 6, balls = 0, widerun = 1, noballrun = 1,
            penaltyrun = 5, userId = 0, team = 0, status = -1, player_id, gameid = 0, totalInnings = 0,
            teamAId = 0, teamBId = 0, eventId = 0, venueId = 0, t_innings = 0, d4s_userid = 0, max_opb = 0;
    ;

    float tco = 0, tbo = 0, tpbo = 0;

    boolean isDownload = false;
    final MidGen random = new MidGen();

    SharedPreferences mPreferences;
    SharedPreferences.Editor mEditor;
    private ProgressDialog progress;

    private Thread mThread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_assigned_match_list);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.custom_action_bar_layout);
        View view = getSupportActionBar().getCustomView();

        back1 = (ImageButton) view.findViewById(R.id.action_bar_back);

        back1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*finish();*/
                Log.e("AssignedMatchList", "oncreate, back button pressed");

                onBackPressed();
            }
        });

        Intent i = getIntent();
        dataTeams = i.getStringArrayListExtra("mylist");
        //Deepak Code starts
        midScore = i.getIntegerArrayListExtra("midScore");
        gameIdArray = i.getIntegerArrayListExtra("game_id");
        //Deepak Code ends
        linearLayout = findViewById(R.id.ll_assigned_match_list);

        mPreferences = PreferenceManager
                .getDefaultSharedPreferences(this);
        userId = mPreferences.getInt("sp_user_id", 0);
        d4s_userid = mPreferences.getInt("d4s_userid", 0);
        token = mPreferences.getString("user_token", null);
        Log.d("userId", "onCreate, " + userId);

        ListView listView = findViewById(R.id.listView);

        adapter = new AssignedListAdapter(AssignedMatchListActivity.this, dataTeams, midScore, gameIdArray);
        adapter.setCustomButtonListner(AssignedMatchListActivity.this);

        listView.setAdapter(adapter);

    }

    @Override
    public void onSaveButtonClickListener(int position, String value) {

//        Toast.makeText(getApplicationContext(), " position : "+position+", value : "+value, Toast.LENGTH_LONG).show();

        Log.d("button click", "position : " + position + ", value : " + value);

        displayProgress();
        viewObject(position);
    }

    // Deepak Code starts
    @Override
    public void onMidScoreClickListener(int position, Integer gameIdValue) {
        Log.d("TAG", "onMidScoreClickListener: Click MidScore");
        AppConstants.GAME_ID = gameIdValue;
        Log.d("TAG", "onMidScoreClickListener: " + AppConstants.GAME_ID);
        viewObjectMidScore(position);
//        Realm realm;
//        RealmConfiguration config;
//        config = new RealmConfiguration.Builder()
//                .name(AppConstants.GAME_ID + ".realm")
//                .deleteRealmIfMigrationNeeded()
//                .build();
//        realm = Realm.getInstance(config);

        Log.d("TAG", "onMidScoreClickListener: Download " + isDownload);

//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//
//                //Update the value background thread to UI thread
//
//
//            }
//        }).start();


        mThread =  new Thread(){
            @Override
            public void run(){
                // Perform thread commands...
                dowmload(AssignedMatchListActivity.this);
//                for (int i=0; i < 5000; i++)
//                {
//
//                }

                // Call the stopThread() method.
                stopThread(this);
            }
        };

        // Start the thread.
        mThread.start();

        // databaseValue(realm);


    }


    private synchronized void stopThread(Thread theThread)
    {
        if (theThread != null)
        {
            theThread = null;
        }
    }
    // Deepak Code ends

    // Deepak Code Starts

    public void dowmload(Context context) {
        //FileCallBack callBack = null;

        try {
            File file = new File(context.getFilesDir()
                    + "/" + AppConstants.GAME_ID + ".realm");
            if(file.exists()){
                Log.d("TAG", "dowmload: File path exist" );
                file.delete();
            }else {
                Log.d("TAG", "dowmload: File path not exist" );
            }
                Log.d("TAG", "dowmload: " + AppConstants.GAME_ID);
            //Your code goes here
            int count;
            try {
                String f_url = "http://chaseweb.data4sports.com/api/match/restore/" + AppConstants.GAME_ID;
                URL url = new URL(f_url);
                URLConnection connection = url.openConnection();
                connection.connect();

                // this will be useful so that you can show a tipical 0-100%
                // progress bar
                int lenghtOfFile = connection.getContentLength();

                // download the file
                InputStream input = new BufferedInputStream(url.openStream(),
                        8192);

                // Output stream
                OutputStream output = null;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                    output = Files.newOutputStream(Paths.get(context.getFilesDir()
                            + "/" + AppConstants.GAME_ID + ".realm"));

                 //   RealmResults<Events> Tasks = backgroundThreadRealm.where(Task.class).findAll();
//                    Realm.init(this); // context, usually an Activity or Application
//                    String realmName = "My Project";
//                    Realm realm;
//                    RealmConfiguration config = new RealmConfiguration.Builder().name(AppConstants.GAME_ID + ".realm").build();
//                    Realm.getInstance(config);

                    //output = Files.newOutputStream(Paths.get(String.valueOf(context.getFilesDir())));
                }

                byte data[] = new byte[1024];

                long total = 0;

                while ((count = input.read(data)) != -1) {
                    total += count;
                    // publishing the progress....
                    // After this onProgressUpdate will be called
                    // publishProgress("" + (int) ((total * 100) / lenghtOfFile));
                    Log.d("TAG", "dowmload: Print " + (int) ((total * 100) / lenghtOfFile));

                    // writing data to file
                    output.write(data, 0, count);

                }

                // flushing output
                output.flush();

                // closing streams
                output.close();
                input.close();

//                dowmloadNew(context,realm);


                //dowmloadNew(context,realm);

                // Log.d("TAG", "onMidScoreClickListener After : ");
                //PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, new Intent(context, UpdatedScoringActivity.class), FLAG_IMMUTABLE);


                Handler handler = new Handler(Looper.getMainLooper());

                handler.postDelayed(new Runnable() {
                    public void run() {
                        Toast.makeText(AssignedMatchListActivity.this,
                                "Downloading Completed", Toast.LENGTH_SHORT).show();
                        Toast.makeText(AssignedMatchListActivity.this,
                                "Match Saved on device", Toast.LENGTH_SHORT).show();
                    }
                }, 1000);

                Intent i = new Intent(context, UpdatedScoringActivity.class);
                mEditor = mPreferences.edit();
                mEditor.putBoolean("midscoreTest", true);
                mEditor.apply();
                context.startActivity(i);
//

                //doRestart(context);


            } catch (Exception e) {
                Log.e("Error: ", e.getMessage());
            }

        } catch (Exception e) {
            Log.d("TAG", "dowmload: File Dwnload  "+ e.getMessage() );
            e.printStackTrace();
        }


    }



    public void dowmloadNew(Context context, Realm realm) {
        //FileCallBack callBack = null;

        try {
            Log.d("TAG", "dowmload: " + AppConstants.GAME_ID);
            //Your code goes here
            int count;
            try {
                String f_url = "http://chaseweb.data4sports.com/api/match/restore/{" + AppConstants.GAME_ID + "}";
                URL url = new URL(f_url);
                URLConnection connection = url.openConnection();
                connection.connect();

                // this will be useful so that you can show a tipical 0-100%
                // progress bar
                int lenghtOfFile = connection.getContentLength();

                // download the file
                InputStream input = new BufferedInputStream(url.openStream(),
                        8192);

                // Output stream
                OutputStream output = null;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                    output = Files.newOutputStream(Paths.get(context.getFilesDir()
                            + "/" + AppConstants.GAME_ID + ".realm"));
                }

                byte data[] = new byte[1024];

                long total = 0;

                while ((count = input.read(data)) != -1) {
                    total += count;
                    // publishing the progress....
                    // After this onProgressUpdate will be called
                    // publishProgress("" + (int) ((total * 100) / lenghtOfFile));

                    // writing data to file
                    output.write(data, 0, count);
                }

                // flushing output
                output.flush();

                // closing streams
                output.close();
                input.close();



//                Handler handler = new Handler(Looper.getMainLooper());
//
//                handler.postDelayed(new Runnable() {
//                    public void run() {
//
//                    }
//                }, 1000);


                // Log.d("TAG", "onMidScoreClickListener After : ");
                //PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, new Intent(context, UpdatedScoringActivity.class), FLAG_IMMUTABLE);

//                Intent i = new Intent(context, ToastActivity.class);
//
//                context.startActivity(i);


            } catch (Exception e) {
                Log.e("Error: ", e.getMessage());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public void midScoreValue() {
        String teamA = null, teamB = null, tossWinner = null, decision = null, bTeam = null,
                fTeam = null, mType = null, inngs = null;

        int userId, bTeamNo = 0, fTeamNo = 0, c_innings = 0, t_innings = 0;
        //displayProgress();
        Realm realm;
        RealmConfiguration config;
//        config = new RealmConfiguration.Builder()
//                .name(AppConstants.GAME_ID + ".realm")
//                .deleteRealmIfMigrationNeeded()
//                .build();
//        realm = Realm.getInstance(config);
        realm = Realm.getDefaultInstance();
        RealmResults<Match> result = realm.where(Match.class).findAll();
        int matchid = result.get(result.size() - 1).getMatchid();
        String matchID = result.get(result.size() - 1).getMatchID();
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

                        Intent i = new Intent(AssignedMatchListActivity.this, ScoreCardActivity.class);
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

                        Toast.makeText(AssignedMatchListActivity.this, "Resume the match",
                                Toast.LENGTH_SHORT).show();

                        mEditor = mPreferences.edit();
                        mEditor.putString("sp_status", "resume");
                        mEditor.putInt("sp_match_id", matchid);
                        mEditor.putString("sp_match_ID", matchID);
                        mEditor.putString("sp_teamA", teamA);
                        mEditor.putString("sp_teamB", teamB);
                        mEditor.apply();
//                            Intent i = new Intent(MatchListActivity.this, ScoringActivity.class);  Commented on 26/07/2021
                        Intent i = new Intent(AssignedMatchListActivity.this, UpdatedScoringActivity.class);
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

                            Toast.makeText(AssignedMatchListActivity.this, "Resume the match",
                                    Toast.LENGTH_SHORT).show();

                            mEditor = mPreferences.edit();
                            mEditor.putString("sp_status", "resume");
                            mEditor.putInt("sp_match_id", matchid);
                            mEditor.putString("sp_match_ID", matchID);
                            mEditor.putString("sp_teamA", teamA);
                            mEditor.putString("sp_teamB", teamB);
                            mEditor.apply();
//                                Intent i = new Intent(MatchListActivity.this, ScoringActivity.class);  Commented on 26/07/2021
                            Intent i = new Intent(AssignedMatchListActivity.this, UpdatedScoringActivity.class);
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

                                    Toast.makeText(AssignedMatchListActivity.this, "Resume the match",
                                            Toast.LENGTH_SHORT).show();
//                                        Intent i = new Intent(MatchListActivity.this, ScoringActivity.class);  Commented on 26/07/2021
                                    Intent i = new Intent(AssignedMatchListActivity.this, UpdatedScoringActivity.class);
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

                                Intent i = new Intent(AssignedMatchListActivity.this, ScoreCardActivity.class);
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

                            } else if (events.getInnings() == 99) {
                                if (events.getBallType() == 10)
                                    newInnings(match, 99);
                            } else if (events.getInnings() == 100) {
                                if (events.getBallType() == 10)
                                    newInnings(match, 100);
                            }

                        }


                    }


                }


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


                Intent intent = new Intent(AssignedMatchListActivity.this, OpenersActivity.class);
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
                startActivity(new Intent(AssignedMatchListActivity.this, CaptainActivity.class));
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
                startActivity(new Intent(AssignedMatchListActivity.this, SelectTeamBXIActivity.class));
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
                startActivity(new Intent(AssignedMatchListActivity.this, SelectTeamAXIActivity.class));
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

                startActivity(new Intent(AssignedMatchListActivity.this, TossActivity.class));
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
                startActivity(new Intent(AssignedMatchListActivity.this, AddPlayersB.class));
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
                startActivity(new Intent(AssignedMatchListActivity.this, AddPlayersA.class));
                progress.dismiss();
                finish();
            }


        } else {

            Log.d("MLA", "else, match.getAbandoned_match_flag() = " + match.getAbandoned_match_flag());
            Log.d("MLA", "else, match.getAbandoned_after_toss_match_flag() = " + match.getAbandoned_after_toss_match_flag());

            //Added on 03/09/2021

            if ((match.getAbandoned_match_flag() == 1) || (match.getAbandoned_after_toss_match_flag() == 1)) {

                Log.d("MLA", "else 1");

                Intent i = new Intent(AssignedMatchListActivity.this, DisplayResultActivity.class);
                i.putExtra("matchid", matchid);
                i.putExtra("matchID", matchID);
                progress.dismiss();
                startActivity(i);
            } else {   // till here

                Log.d("MLA", "else 2");

                Intent i = new Intent(AssignedMatchListActivity.this, ScoreCardActivity.class);
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

        Toast.makeText(AssignedMatchListActivity.this, "Resume the match",
                Toast.LENGTH_SHORT).show();
        // for displaying scoring
//        Intent i = new Intent(MatchListActivity.this, ScoringActivity.class);  Commented on 26/07/2021
        Intent i = new Intent(AssignedMatchListActivity.this, UpdatedScoringActivity.class);
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

    public void databaseValue(Realm realm) {
        Log.d("TAG", "databaseValue: Invoke");


        Integer match_id = 0;
        String match_ID = "";
        String teamA = "";
        String teamB = "";
        String sp_innings = "";
        String battingTeam = "";
        String fieldingTeam = "";
        int battingTeamNo = 0;
        int fieldingTeamNo = 0;

        boolean justStarted = false;
        String status = "";
        String matchType = "";
        int ballsPerOver = 0;
        int noBallRun = 0;
        int penaltyRun = 0;
        int wideRun = 0;

        String wicketKeeper = "";
        int wk_id = 0;
        int keeper_position = 0;
        RealmResults<Match> match = realm.where(Match.class).
                equalTo("d4s_matchid", AppConstants.GAME_ID).findAll();
        Log.d("TAG", "databaseValue: " + match.size());
        if (match.size() > 0) {
            match_id = match.get(match.size() - 1).getMatchid();
            match_ID = match.get(match.size() - 1).getMatchID();
            teamA = match.get(match.size() - 1).getTeamA();
            teamB = match.get(match.size() - 1).getTeamB();
            sp_innings = match.get(match.size() - 1).getInnings();

            justStarted = match.get(match.size() - 1).isJustStarted();
            status = match.get(match.size() - 1).getStatus();
            matchType = match.get(match.size() - 1).getMatchType();
            ballsPerOver = match.get(match.size() - 1).getBalls();
            noBallRun = match.get(match.size() - 1).getNoballrun();
            penaltyRun = match.get(match.size() - 1).getPenaltyrun();
            wideRun = match.get(match.size() - 1).getWiderun();


            if (match.get(match.size() - 1).getToss_winner().equals(teamA)) {
                if (match.get(match.size() - 1).getDecision().equals("Batting")) {
                    battingTeam = teamA;
                    fieldingTeam = teamB;
                    battingTeamNo = 2;
                    fieldingTeamNo = 1;
                } else {
                    battingTeam = teamB;
                    fieldingTeam = teamA;
                    fieldingTeamNo = 2;
                    battingTeamNo = 1;
                }
            } else {
                if (match.get(match.size() - 1).getDecision().equals("Batting")) {
                    battingTeam = teamB;
                    fieldingTeam = teamA;
                    fieldingTeamNo = 1;
                    battingTeamNo = 2;
                } else {
                    battingTeam = teamA;
                    fieldingTeam = teamB;
                    battingTeamNo = 1;
                    fieldingTeamNo = 2;
                }
            }
        }
        // Log.d("TAG", "databaseValue: Match ID " + match.get(match.size() - 1).getMatchid());


        // Player List
        int player1ID = 0;
        int player2ID = 0;
        int bowlerId = 0;

        int currentInnings = 0;

        RealmResults<Events> event_results = realm.where(Events.class).equalTo("matchid", match_id).findAll();
        Log.d("TAG", "databaseValue: Events size " + event_results);

        if (event_results.size() > 0) {
            Log.d("TAG", "databaseValue: " + event_results.get(event_results.size() - 1).getStrikerID() + " Non Striker " + event_results.get(event_results.size() - 1).getNonStrikerID());
            player1ID = event_results.get(event_results.size() - 1).getStrikerID();
            player2ID = event_results.get(event_results.size() - 1).getNonStrikerID();
            bowlerId = event_results.get(event_results.size() - 1).getBowlerID();
            currentInnings = event_results.get(event_results.size() - 1).getInnings();

        }

        // Player Name
        String player1 = "";
        String player2 = "";
        String bowlerName = "";
        Player playerOne = realm.where(Player.class)
                .equalTo("gameid", AppConstants.GAME_ID)
                .equalTo("matchid", match_id)
                .equalTo("playerID", player1ID).findFirst();
        Log.d("TAG", "databaseValue: Player " + playerOne);
        if (playerOne != null) {
            player1 = playerOne.getPlayerName();
        }

        Player playerTwo = realm.where(Player.class)
                .equalTo("gameid", AppConstants.GAME_ID)
                .equalTo("matchid", match_id)
                .equalTo("playerID", player2ID).findFirst();
        if (playerTwo != null) {
            player2 = playerTwo.getPlayerName();
        }

        Player bowlerNameDB = realm.where(Player.class)
                .equalTo("gameid", AppConstants.GAME_ID)
                .equalTo("matchid", match_id)
                .equalTo("playerID", bowlerId).findFirst();
        if (bowlerNameDB != null) {
            bowlerName = bowlerNameDB.getPlayerName();
        }


        // total Innings
        int totalInnings = 0;
        totalInnings = match.get(match.size() - 1).getTotalInnings();

        // Player Count
        int player_count = 2;
        boolean hundred = false;
        boolean HUNDRED = match.get(match.size() - 1).isHundred();

        String striker = "";
        String nonStriker = "";
        int strID = 0;
        int nstrID = 0;
        int ps1ID = 0;
        int ps2ID = 0;

        striker = player1;
        nonStriker = player2;
        strID = player1ID;
        nstrID = player2ID;
        ps1ID = player1ID;
        ps2ID = player2ID;

        // Just Started


        //currentInnings = sharedPreferences.getInt("sp_current_innings", 0);

        Log.d("TAG", "getFromSP: First set preference call Testttt" + "Match id " + match_id + " sp_match_id " + match_ID +
                " Team A " + teamA + " Team B " + teamB + " sp innings " + sp_innings + " Batting Team " + battingTeam +
                " Fielding Team " + fieldingTeam + " Batting Team Number " + battingTeam + " Fielding Team Number " + fieldingTeam +
                " Player 1 " + player1 + " Player 2 " + player2 + " Player 1 ID " + player1ID + " Player 2 ID " + player2ID +
                " Total Innings " + totalInnings + " Player Count " + player_count + " Hundreds " + hundred + " Striker " + striker +
                " Non Stricker " + nonStriker + " STR_ID " + strID + " NSTR_ID " + nstrID + " PS1ID " + ps1ID +
                " PS2ID " + ps2ID + " Bowler " + bowlerName + " Bowler ID " + bowlerId + " Just Started " + justStarted +
                " Status " + status + " Match Type " + matchType + " Balls per Over " + ballsPerOver + " Current Innings " + currentInnings +
                " No Ball Run " + noBallRun + " Wide Run " + wideRun + " Penalty Run " + penaltyRun);


//        Player wk_player = realm.where(Player.class).
//                equalTo("matchid", matchid).
//                equalTo("team", fieldingTeamNo).
//                equalTo("new_wk", true).findFirst();

//        wicketkeeper = wk_player.getPlayerName();
//        wk_id = wk_player.getPlayerID();
//        keeper_position = wk_player.getWicketkeeping_position();


        //battingOrder = sharedPreferences.getInt("sp_batting_order", 0);


        //events.get(events.size() - 1).getStrikerID();
        //Log.d("TAG", "databaseValue: " +   events.get(events.size() - 1).getStrikerID()+ " Non Striker " +   events.get(events.size() - 1).getNonStrikerID());

    }

    private void saveToDevice() {


        displayProgress();

        CreateMatchActivity createMatch = new CreateMatchActivity();

        Log.d("saveToDevice", "gameid = " + gameid);
        Log.d("saveToDevice", "teamA = " + teamA);
        Log.d("saveToDevice", "teamB = " + teamB);
        Log.d("saveToDevice", "venue = " + venue);
        Log.d("saveToDevice", "event = " + event);
        Log.d("saveToDevice", "matchDate = " + matchDate);
        Log.d("saveToDevice", "matchType = " + matchType);
        Log.d("saveToDevice", "innings = " + innings);
        Log.d("saveToDevice", "playerA = " + playerA);
        Log.d("saveToDevice", "playerB = " + playerB);
        Log.d("saveToDevice", "overs = " + over);
        Log.d("saveToDevice", "balls = " + balls);
        Log.d("saveToDevice", "max opb = " + max_opb);

        if (gameid <= 0) {
            checkFlag = false;
        } else if (teamA.matches("") || teamA == null) {
            checkFlag = false;
        } else if (teamB.matches("") || teamB == null) {
            checkFlag = false;
        } else if (venue.matches("") || venue == null) {
            checkFlag = false;
        } else if (event.matches("") || event == null) {
            checkFlag = false;
        } else if (matchDate.matches("") || matchDate == null) {
            checkFlag = false;
        } else if (matchType.matches("") || matchType == null) {
            checkFlag = false;
        } else if (innings.matches("") || innings == null) {
            checkFlag = false;
        } else if (playerA == 0 || playerB == 0 || balls == 0) {
            checkFlag = false;
        } else {
            checkFlag = true;
        }

        matchID = checkID(random.genId());

        if (checkFlag) {

            if (innings.matches("single"))
                totalInnings = 2;
            else if (innings.matches("multi"))
                totalInnings = 4;

            Realm realm1 = null;
            try {
//                config = new RealmConfiguration.Builder()
//                        .name(AppConstants.GAME_ID + ".realm")
//                        .deleteRealmIfMigrationNeeded()
//                        .build();
                realm1 = Realm.getDefaultInstance();
                realm1.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm bgRealm) {

                        try {

                            Number num = bgRealm.where(Match.class).max("matchid");
                            int nextId = (num == null) ? 1 : num.intValue() + 1;

                            Match match = bgRealm.createObject(Match.class, nextId);
                            matchid = nextId;

                            match.setD4s_matchid(gameid);
                            match.setMatchID(matchID);
                            match.setUserId(userId);
                            match.setD4s_userid(d4s_userid);

                            match.setTeamA(teamA);
                            match.setTeamAId(teamAId);
                            match.setTeamB(teamB);
                            match.setTeamBId(teamBId);

                            match.setVenue(venue);
                            match.setVenueId(venueId);
                            match.setEnd1(end1);
                            match.setEnd2(end2);
                            match.setEvent(event);
                            match.setEventId(eventId);
                            match.setPhase(phase);

                            match.setMatchType(matchType);
                            match.setMax_opb(max_opb);
                            match.setInnings(innings);
                            match.setTotalInnings(totalInnings);

                            match.setPlayerA(playerA);
                            match.setPlayerB(playerB);
//                                match.setSubst(subst);
                            match.setOver(over);
                            match.setActual_over(over);
                            match.setBalls(balls);

                            match.setWiderun(widerun);
                            match.setNoballrun(noballrun);
                            match.setPenaltyrun(penaltyrun);

                            match.setDate(matchDate);

                            match.setPulled(true);
//                            match.setPulled_squad(squad);
//                            match.setPulled_squadA(squadA); // Added on 19/11/2021
//                            match.setPulled_squadB(squadB); // Added on 19/11/2021
//                            match.setHundred(hundred);    // Added on 29/07/2021
//                            match.setMax_bpb(max_bpb);    // Added on 29/07/2021


                            match.setLimited_overs(limited_overs);    // Added on 29/07/2021

                             /*   match.setUmpire1(umpire1);
                                match.setUmpire2(umpire2);
                                match.setUmpire3(umpire3);
                                match.setUmpire4(umpire4);
                                match.setMatchReferee(matchReferee);*/

//                                match.setMatchSync(1);
//                            match.setScorer(scorer);

//                                                    realm.commitTransaction();
                            bgRealm.copyToRealm(match);

                            // posting match details on CreateMatch table

                            Log.d("match", "saveToDevice, " + match);
                            //Log.d("squad", "saveToDevice, " + squad);
//                                postMatchDetails();
//                                if (squad)
//                                    saveSquad();
                            Toast.makeText(AssignedMatchListActivity.this,
                                    "Downloading the File Please wait ......", Toast.LENGTH_SHORT).show();


                            saveToSP();

                            //check thecondition that whether it has squad details or not, or whether it is a mid scoring assignment
                                /*if (midScoringFlag)
                                    pullDetails();
                                else
                                    deleteFromList();*/
//                                                    saveToServer();
                        } catch (RealmPrimaryKeyConstraintException e) {
                            progress.dismiss();
                            Toast.makeText(getApplicationContext(), "Primary Key exists, Press Update instead",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });


            } catch (RealmException e) {
                Log.d("test", "Exception : " + e);
            } finally {
                if (realm1 != null) {
                    realm1.close();
                }
            }

//                if (!umpire1.matches(""))
//                    saveOfficial(umpire1, "u1");
//                if (!umpire2.matches(""))
//                    saveOfficial(umpire2, "u2");
//                if (!umpire3.matches(""))
//                    saveOfficial(umpire3, "t");
//                if (!umpire4.matches(""))
//                    saveOfficial(umpire4, "f");
//                if (!scorer.matches(""))
//                    saveOfficial(scorer, "s");
//                if (!matchReferee.matches(""))
//                    saveOfficial(matchReferee, "r");

               /* if (mid_scoring) {     not currently working on mid scoring

                    saveSquad();
                    saveDetails();
                }

                else {*/
//            if (squad)
//                // saveSquad();
//
//                postMatchDetailsLocal(matchID, squad);
//                    postOfficialDetails();// added on 03/08/2021

            //}

        } else {

            progress.dismiss();
            new AlertDialog.Builder(AssignedMatchListActivity.this)
                    .setIcon(R.drawable.ball)
                    .setTitle("Save Match Failed")
                    .setMessage("Required fields are missing. Report to admin.")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    })
                    .show();
//                Toast.makeText(getApplicationContext(), "Can not save match due to some required fields are empty",
//                        Toast.LENGTH_LONG).show();
        }
//        }
    }

    String checkID(String matchID) {

        Realm realm;
        realm = Realm.getDefaultInstance();
        Match match_test = realm.where(Match.class).equalTo("matchID", matchID).findFirst();
        if (match_test != null)
            return checkID(random.genId());
        else
            return matchID;
    }
    // Deepak Code Ends

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    //Deepak Code

    private void viewObjectMidScore(int position) {

        if (isNetworkAvailable()) {

            StringRequest stringRequest = new StringRequest(Request.Method.GET,
                    Constants.CHASE_CRICKET_PULL_MATCH_API_TEST + d4s_userid,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            try {

                                Log.d("response", "AssignedMatchList, " + response);

                                // based on gib=ven dummy values
                                JSONArray array = new JSONArray(response);
                                Log.d("array", "AssignedMatchList, " + array);


                                if (array.length() > 0) {

                                    for (int i = 0; i < array.length(); i++) {
                                        //getting the json object of the particular index inside the array

                                        if (i == position) {

                                            /*if (midScoringFlag) {
                                                ++t_innings;
                                                eventObject = new JSONObject();
                                                tempObject = eventObject;
                                                saveEvent();
                                            }

                                            else {*/
                                            matchObject = new JSONObject();

                                            matchObject = array.getJSONObject(i);
                                            tempObject = matchObject;
                                            try {
                                                gameid = Integer.parseInt(tempObject.getString("gameid"));
                                                AppConstants.GAME_ID = gameid;
                                                Log.d("TAG", "onResponse: Gamed id " + AppConstants.GAME_ID);
//            mid_scoring = jsonMatch.getBoolean("midscoring");
                                                teamA = tempObject.getString("teamA");
                                                teamAId = Integer.parseInt(tempObject.getString("teamA_id"));
                                                Log.d("teamAId", "oncreate, : " + teamAId);
                                                teamB = tempObject.getString("teamB");
                                                teamBId = Integer.parseInt(tempObject.getString("teamB_id"));
                                                event = tempObject.getString("event");
                                                eventId = Integer.parseInt(tempObject.getString("event_id"));
                                                phase = tempObject.getString("phase");
                                                venue = tempObject.getString("venue");
                                                venueId = Integer.parseInt(tempObject.getString("venue_id"));
                                                if (tempObject.has("end1"))
                                                    end1 = tempObject.getString("end1");
                                                else
                                                    end1 = null;
                                                if (tempObject.has("end2"))
                                                    end2 = tempObject.getString("end2");
                                                else
                                                    end2 = null;
//            URLEncoder.encode("-", "UTF8");
//            URLEncoder.encode("\t", "UTF8");
                                                matchDate = tempObject.getString("date");
                                                Log.d("matchObject", "viewDetails : " + tempObject);
                                                Log.d("date", "viewDetails : " + tempObject.getString("date"));
//            final StringBuilder builder = matchObject.ge("date");
                                                matchType = tempObject.getString("match_type");
                                                // Added on 08/12/2021
                                                if ((tempObject.getString("teamAplayers") != null) &&
                                                        (tempObject.getString("teamBplayers") != null)) {
                                                    playerA = Integer.parseInt(tempObject.getString("teamAplayers"));
                                                    playerB = Integer.parseInt(tempObject.getString("teamBplayers"));
                                                } else
                                                    Toast.makeText(getApplicationContext(),
                                                            "Invalid player count", Toast.LENGTH_SHORT).show();
                                                if (tempObject.getString("scheduled_overs") == null)
                                                    over = 0;
                                                else
                                                    over = Integer.parseInt(tempObject.getString("scheduled_overs"));
                                                balls = Integer.parseInt(tempObject.getString("balls_per_over"));
                                                Log.d("PMD", "max_overs_per_bowler = " + tempObject.getString("max_overs_per_bowler"));
                                                if (tempObject.getString("max_overs_per_bowler") == null)
                                                    max_opb = 0;
                                                else if (tempObject.getString("max_overs_per_bowler").matches("null"))
                                                    max_opb = 0;
                                                else
                                                    max_opb = Integer.parseInt(tempObject.getString("max_overs_per_bowler"));
                                                innings = tempObject.getString("innings");

                                                if (over > 0)
                                                    limited_overs = true;

                                                if (innings.matches("multi") && (over == 0) && (max_opb == 0)) {
                                                    over = 1000;
                                                    // if so remove the visibility of overs and max overs/bowler
                                                }
                                                saveToDevice();

                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
//                                            Intent intent = new Intent(AssignedMatchListActivity.this,
//                                                    PulledMatchDetailsActivity.class);
////                                                intent.putExtra("jsonmatch", matchObject.toString());
//                                            intent.putExtra("position", position);
//                                            startActivity(intent);
//                                            finish();
                                            progress.dismiss();

                                            //finish();
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
                            Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();

                        }


                    });


            //creating a request queue
            RequestQueue requestQueue = Volley.newRequestQueue(this);

            //adding the string request to request queue
            requestQueue.add(stringRequest);

            Log.d("AMLA", "pulled match, stringRequest  : " + stringRequest);

        } else {

            AlertDialog alertDialog = new AlertDialog.Builder(AssignedMatchListActivity.this).create();
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

    private void viewObject(int position) {

        if (isNetworkAvailable()) {

            StringRequest stringRequest = new StringRequest(Request.Method.GET,
                    Constants.CHASE_CRICKET_PULL_MATCH_API_TEST + d4s_userid,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            try {

                                Log.d("response", "AssignedMatchList, " + response);

                                // based on gib=ven dummy values
                                JSONArray array = new JSONArray(response);
                                Log.d("array", "AssignedMatchList, " + array);


                                if (array.length() > 0) {

                                    for (int i = 0; i < array.length(); i++) {
                                        //getting the json object of the particular index inside the array

                                        if (i == position) {

                                            /*if (midScoringFlag) {
                                                ++t_innings;
                                                eventObject = new JSONObject();
                                                tempObject = eventObject;
                                                saveEvent();
                                            }

                                            else {*/
                                            matchObject = new JSONObject();

                                            matchObject = array.getJSONObject(i);
                                            tempObject = matchObject;
                                            Intent intent = new Intent(AssignedMatchListActivity.this,
                                                    PulledMatchDetailsActivity.class);
//                                                intent.putExtra("jsonmatch", matchObject.toString());
                                            intent.putExtra("position", position);
                                            startActivity(intent);
                                            finish();
                                            progress.dismiss();

                                            finish();
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
                            Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();

                        }


                    });


            //creating a request queue
            RequestQueue requestQueue = Volley.newRequestQueue(this);

            //adding the string request to request queue
            requestQueue.add(stringRequest);

            Log.d("AMLA", "pulled match, stringRequest  : " + stringRequest);

        } else {

            AlertDialog alertDialog = new AlertDialog.Builder(AssignedMatchListActivity.this).create();
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


    private void deleteFromList() {


        if (flag) {

            flag = false;

            JSONArray jsonArray = new JSONArray();
            jsonArray.put(tempObject);

            JSONObject json2 = new JSONObject();
            try {
                json2.put("user_token", token);
                json2.put("assigned_match", jsonArray);

            } catch (JSONException e) {
                e.printStackTrace();
            }

            JSONArray jsonArray2 = new JSONArray();
            jsonArray2.put(json2);


            JSONObject json3 = new JSONObject();
            try {
                json3.put("title", "DELETE_OBJECT_FROM_LIST");
                json3.put("game", jsonArray2);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            final String requestBody = json3.toString();
            Log.d("deleteFromList", " requestBody : " + requestBody);


            // uncomment the following code
/*            RequestQueue requestQueue1 = Volley.newRequestQueue(this);

            StringRequest stringRequest1 = new StringRequest(Request.Method.POST, Constants.ASSIGNED_MATCHES_API_URL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.i("VOLLEY", response);

                            saveToDevice();
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("VOLLEY", error.toString());
                }
            }) {
                @Override
                public String getBodyContentType() {
                    return "application/json; charset=utf-8";
                }

                @Override
                public byte[] getBody() throws AuthFailureError {

                    try {
                        return requestBody == null ? null : requestBody.getBytes("utf-8");
                    } catch (UnsupportedEncodingException uee) {
                        VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", requestBody, "utf-8");
                        return null;
                    }
                }

                @Override
                protected Response<String> parseNetworkResponse(NetworkResponse response) {
                    String responseString = "";
                    if (response != null) {
                        responseString = String.valueOf(response.statusCode);
                        // can get more details such as response.headers
                    }
                    return Response.success(responseString, HttpHeaderParser.parseCacheHeaders(response));
                }
            };

            requestQueue1.add(stringRequest1);
*/


            if (playersFlag)    // checking whether the assigned match details has sqad details or not
                startActivity(new Intent(AssignedMatchListActivity.this, CaptainActivity.class));
            else
//                startActivity(new Intent(AssignedMatchListActivity.this, AddPlayers.class));
                startActivity(new Intent(AssignedMatchListActivity.this, AddPlayersA.class));


        }
    }


    public void saveToSP() {

        mEditor = mPreferences.edit();
        mEditor.putInt("sp_match_id", matchid);
        mEditor.putString("sp_match_ID", matchID);
//        mEditor.putString("sp_token", token);
        mEditor.putString("sp_teamA", teamA);
        mEditor.putString("sp_teamB", teamB);
        mEditor.putInt("sp_playerA", playerA);
        mEditor.putInt("sp_playerB", playerB);
//        mEditor.putInt("sp_sub_seq",subst);
        mEditor.putString("sp_innings", innings);
        mEditor.putInt("sp_over", over);
        mEditor.putInt("sp_balls", balls);
        mEditor.putString("sp_match_type", matchType);
        mEditor.putInt("sp_wide_value", widerun);
        mEditor.putInt("sp_noball_value", noballrun);
        mEditor.putInt("sp_penalty_value", penaltyrun);
        mEditor.putInt("sp_current_innings", 0);

        mEditor.apply();
    }


    public void displayProgress() {

        progress = new ProgressDialog(this);
        progress.setMessage("Please wait...");
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setIndeterminate(true);
        progress.setProgress(0);
        progress.show();
    }


}
