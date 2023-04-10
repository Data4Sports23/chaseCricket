 package com.data4sports.chasecricket.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.data4sports.chasecricket.R;
import com.data4sports.chasecricket.applicationConstants.AppConstants;
import com.data4sports.chasecricket.models.Batsman;
import com.data4sports.chasecricket.models.Bowler;
import com.data4sports.chasecricket.models.Constants;
import com.data4sports.chasecricket.models.Events;
import com.data4sports.chasecricket.models.ExtraCard;
import com.data4sports.chasecricket.models.FOW;
import com.data4sports.chasecricket.models.Match;
import com.data4sports.chasecricket.models.Partnership;
import com.data4sports.chasecricket.models.Penalty;
import com.data4sports.chasecricket.models.Player;
import com.data4sports.chasecricket.models.Power;
import com.data4sports.chasecricket.models.Substitution;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;
import io.realm.Sort;
import io.realm.exceptions.RealmException;
import io.realm.exceptions.RealmPrimaryKeyConstraintException;

public class SavePulledEventActivity extends AppCompatActivity {

    int gameid = 0;
    JSONArray arrayDetails;

    Realm realm;
    private ProgressDialog progress;

    int matchid, eventID = 0, innings = 0, totalInnings = 0, ballType = -1, ballsPerOver, preInningsRuns = 0,
            innings1Runs = 0, innings2Runs = 0, innings3Runs = 0, innings4Runs = 0, remainingRuns = 0,
            remainingBalls = 0, leadingRuns = 0, runs = 0, totalOver = 0, player1ID = 0, player2ID = 0,
             strID = 0, nstrID = 0, bowlerID = 0, preBowlerID = 0, lastPreBowlerID = 0, outType = -1,
             extraType = -1, extraRun = 0 , dismissedPlayerID = 0, newBatsmanID = 0, penaltyType = -1,
             penaltyRun = 0, penaltyTeam = 0, penaltyBallCounted = 0, battingTeam = 0, fieldingTeam = 0,
             newBowlerID = 0, mo = 0, sessionID = 0, sessionNumber = 0, so_innings1Runs = 0, so_innings2Runs = 0,
             substitutionID = 0, revisedTarget = 0, player1Runs = 0, player1Balls = 0, player2Runs = 0,
             player2Balls = 0, strikerRuns = 0, strikerBalls = 0, strikerBattingOrder = 0, strikerDots = 0,
             strikerF4s = 0, strikerS6s = 0, nonStrikerRuns = 0, nonStrikerBalls = 0, nonStrikerBattingOrder = 0,
             nonStrikerDots = 0, nonStrikerF4s = 0, nonStrikerS6s = 0, extras = 0, noballRun = 0, wideRun = 0,
             wicket = 0, score = 0, subID = 0, count = 0, p_id = 0;

     String matchID, matchType, fielderID = null, modified = "", udisplay = "", sessionType = "", sType = "",
             appliedRainRule = "", tem = "";

     boolean newPartnership = false, ballCount = false, maiden  = false, endOfDay = false, SUPER_OVER = false,
             declared = false, freeHit = false, newPartnerships = false, innings_start = false, out = false,
             pre_out = false, endOfOver = false, callBowlerAlert = false, retired = false, bye = false,
             lb = false, wide = false, noBall = false, penalty = false, byeNB = false, lbNB = false,
             extra = false, concussion = false;

     float remainingOvers = 0f, tco = 0f, currentOver = 0f, reducedOver = 0f, temp_total_over = 0f;

    RealmConfiguration config;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save_pulled_event);

         config = new RealmConfiguration.Builder()
                .name(AppConstants.GAME_ID + ".realm")
                .deleteRealmIfMigrationNeeded()
                .build();
        realm = Realm.getInstance(config);

        Intent i = getIntent();
        matchid = i.getIntExtra("matchid", 0);

        Match match = realm.where(Match.class).
                equalTo("matchid", matchid).findFirst();

        if (match != null) {

            matchID = match.getMatchID();
            matchType = match.getMatchType();
            totalInnings = match.getTotalInnings();
            wideRun = match.getWiderun();
            noballRun = match.getNoballrun();
            penaltyRun = match.getPenaltyrun();

            pullDetails();
        }

        else
            Toast.makeText(this, "No match found", Toast.LENGTH_SHORT).show();

    }



     private boolean isNetworkAvailable(){
         ConnectivityManager connectivityManager
                 = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
         NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
         return activeNetworkInfo != null && activeNetworkInfo.isConnected();
     }


    public void pullDetails() {

        if (isNetworkAvailable()) {

            StringRequest stringRequest = new StringRequest(Request.Method.GET,
                    Constants.CHASE_CRICKET_MATCH_API + "" + gameid,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            try {

                                JSONArray array = new JSONArray(response);
                                Log.d("array", "" + array);

                                JSONObject jsonSquad = array.getJSONObject(0);
                                Log.d("jsonSquad", "" + jsonSquad);

                                arrayDetails = jsonSquad.getJSONArray("");

                                for (int i = 0; i < arrayDetails.length(); i++) {

                                    JSONObject jsonDetails = arrayDetails.getJSONObject(i);
                                    saveDetails(jsonDetails);
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

//            tempObject = matchObject;
//            saveToDevice();

        }


        else {

            progress.dismiss();
            AlertDialog alertDialog = new AlertDialog.Builder(SavePulledEventActivity.this).create();
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


     public void displayProgress(){

         progress = new ProgressDialog(this);
         progress.setMessage("Please wait...");
         progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
         progress.setIndeterminate(true);
         progress.setProgress(0);
         progress.show();
     }



     public void saveDetails(JSONObject jsonDetails) {

         JSONObject jsonSummary = null;
         JSONObject jsonInterval = null;
         JSONObject jsonSession = null;
         try {
             jsonSummary = jsonDetails.getJSONObject("inningssummary");
             jsonInterval = jsonDetails.getJSONObject("interval");
             jsonSession = jsonDetails.getJSONObject("session");



             float over = (float) jsonSummary.getDouble("totalovers");
             int over1 = (int) over;
             int over2 = (int) ((over - over1) *10);
             int totalBalls = over2 * ballsPerOver;

             // set totalInnings, according to multi innings type

             score = jsonSummary.getInt("totalscore");
             currentOver = (float) jsonSummary.getDouble("totalovers");
             wicket = jsonSummary.getInt("totalwicket");
             battingTeam = jsonDetails.getInt("battingteam");
             fieldingTeam = (jsonDetails.getInt("battingteam") == 1) ? 2 : 1;
             strID = jsonDetails.getInt("strikerid");
             nstrID = jsonDetails.getInt("nonstrikerid");
             ballType = jsonDetails.getInt("balltype");
             runs = jsonDetails.getInt("runs");
             innings = jsonDetails.getInt("innings");
             bowlerID = jsonDetails.getInt("bowlerid");
             outType = jsonDetails.getInt("outtype");
             extraType = jsonDetails.getInt("extratype");
             extraRun = jsonDetails.getInt("extras");   // extrarun
             dismissedPlayerID = jsonDetails.getInt("dismissedbatsmanid");
             newBatsmanID = jsonDetails.getInt("disnewbatsmanid");
             penaltyType = jsonDetails.getInt("penaltytype");
//             penaltyRun = jsonDetails.getInt("penalty");      // commented on 27/05/2020, penaltyRun is already reading from match details
             sessionNumber = jsonSession.getInt("number");
             sessionType = jsonSession.getString("type");
             SUPER_OVER = jsonDetails.getBoolean("superover");

             // reading striker - nonStriker runs
             JSONObject jsonBatsman = jsonDetails.getJSONObject("batsman");
             JSONObject jsonStriker = jsonBatsman.getJSONObject("striker");
             strikerRuns = jsonStriker.getInt("run");
             strikerBalls = jsonStriker.getInt("ball");
             strikerBattingOrder = jsonStriker.getInt("battingorder");
             strikerDots = jsonStriker.getInt("dots");
             strikerF4s = jsonStriker.getInt("fours");
             strikerS6s = jsonStriker.getInt("sixes");

             JSONObject jsonNonStriker = jsonBatsman.getJSONObject("nonstriker");
             nonStrikerRuns = jsonNonStriker.getInt("run");
             nonStrikerBalls = jsonNonStriker.getInt("ball");
             nonStrikerBattingOrder = jsonNonStriker.getInt("battingorder");
             nonStrikerDots = jsonNonStriker.getInt("dots");
             nonStrikerF4s = jsonNonStriker.getInt("fours");
             nonStrikerS6s = jsonNonStriker.getInt("sixes");

             // setting player1 runs & player2 runs
             if ((player1ID > 0) && (player2ID > 0)) {

                 if (player1ID == strID) {

                     player1Runs = strikerRuns;
                     player1Balls = strikerBalls;
                     player2Runs = nonStrikerRuns;
                     player2Balls = nonStrikerBalls;
                 }

                 else if (player2ID == strID) {

                     player2Runs = strikerRuns;
                     player2Balls = strikerBalls;
                     player1Runs = nonStrikerRuns;
                     player1Balls = nonStrikerBalls;
                 }

                 syncBatsman(strID);
             }

             // saving bowler details
             JSONObject jsonBowler = jsonDetails.getJSONObject("bowler");
             if (bowlerID > 0)
                 syncBowler(jsonBowler.getJSONObject("current"));

             if (SUPER_OVER) {
                 if (innings == 1)
                     innings = 99;
                 else
                     if (innings == 2)
                         innings = 100;
             }

             if (sessionNumber == 1) {
                 // possibilities 6,7 & 12
                 if (sessionType.matches("start")) {
                     sessionID = 12;        // Session 1 Started
                     sType = "SS1";
                 }
                 else
                     if (sessionType.matches("end")) {
                         sessionID = 7;        // Session 1 Ended
                         sType = "SE1";
                     }
             }

             else
                 if (sessionNumber == 2) {
                     // possibilities 8 & 9
                     if (sessionType.matches("start")) {
                         sessionID = 8;        // Session 2 Started
                         sType = "SS2";
                     }
                     else
                         if (sessionType.matches("end")) {
                             sessionID = 9;        // Session 2 Ended
                             sType = "SE2";
                         }
                 }

             else
                 if (sessionNumber == 3) {
                     // possibilities 10 & 11
                     if (sessionType.matches("start")) {
                         sessionID = 10;        // Session 3 Started
                         sType = "SS3";
                     }
                     else
                         if (sessionType.matches("end")) {
                             sessionID = 11;        // Session 3 Ended
                             sType = "SE3";
                         }
                 }

             mo = mo + runs + extraRun;
             remainingRuns = remainingRuns - runs - extraRun;
             leadingRuns = leadingRuns + runs + extraRun;




             if (ballType == 1 || ballType == 2) {

                 udisplay = modified;
                 modified = modified + "\t\t" + runs;
                 newPartnerships = false;
             }
             else
                 if (ballType == 3) {

                     newPartnerships = false;
                     out = true;

                     if (outType == 1 || outType == 3) {

                         JSONArray fielderArray = jsonDetails.getJSONArray("fielderids");
                         fielderID = String.valueOf(fielderArray.getInt(0));
                     }

                     else
                         if (outType == 2) {

                             JSONArray fielderArray = jsonDetails.getJSONArray("fielderids");
                             for (int i = 0; i < fielderArray.length(); i++) {

                                 if (i == 0)
                                     fielderID = String.valueOf(fielderArray.getInt(i));
                                 else {

//                                      fielderID = ", " + String.valueOf(fielderArray.getInt(i));           // convert int to string method 1
//                                      fielderID = ", " + Integer.toString(fielderArray.getInt(i));         // convert int to string method 2
//                                      fielderID = ", " + new Integer(fielderArray.getInt(i)).toString();   // convert int to string method 3
                                     fielderID = ", " + fielderArray.getInt(i);
                                 }
                             }
                         }

                     else
                         if (outType == 10) {

                             retired = true;
                         }

                     udisplay = modified;
                     if (runs > 0)
                         modified = modified + "\t\t W(" + runs + ")";
                     else
                         if (extraType == 2 || extraType == 3 || extraType == 6)
                             modified = modified + "\t\t W(nb)";
                     else
                         if (outType == 10)
                             modified = modified + "";
                     else
                         modified = modified + "\t\t W";

                     syncBatsman(dismissedPlayerID);

                     if (extraType > -1) {
                         if (extraType == 0) {      // bye

                             bye = true;
                             lb = false;
                             wide = false;
                             noBall = false;
                             byeNB = false;
                             lbNB = false;
                             penalty = false;

                             extras = extraRun;
                         }

                         else
                         if (extraType == 1) {     // leg bye

                             bye = false;
                             lb = true;
                             wide = false;
                             noBall = false;
                             byeNB = false;
                             lbNB = false;
                             penalty = false;

                             extras = extraRun;
                         }

                         else
                         if (extraType == 2) {     // noball + none  | noball + runs off bat

                             bye = false;
                             lb = false;
                             wide = false;
                             noBall = true;
                             byeNB = false;
                             lbNB = false;
                             penalty = false;

                             extras = extraRun - noballRun;
                         }

                         else
                         if (extraType == 3) {     // noball + bye

                             bye = false;
                             lb = false;
                             wide = false;
                             noBall = true;
                             byeNB = true;
                             lbNB = false;
                             penalty = false;

                             extras = extraRun - noballRun;
                         }

                         else
                         if (extraType == 4) {     // wide

                             bye = false;
                             lb = false;
                             wide = true;
                             noBall = false;
                             byeNB = false;
                             lbNB = false;
                             penalty = false;

                             extras = extraRun - wideRun;
                         }

                         else
                         if (extraType == 5) {     // penalty

                             /*bye = false;
                             lb = false;
                             wide = false;
                             noBall = false;
                             byeNB = false;
                             lbNB = false;*/
                             penalty = false;

                             if (penaltyType == 1) {

                                 penaltyTeam = battingTeam;
                                 remainingRuns = remainingRuns - penaltyRun;
                                 leadingRuns = leadingRuns + penaltyRun;
                                 penalty = true;
                             }
                             else
                             if (penaltyType == 2)
                                 penaltyTeam = fieldingTeam;
                         }

                         else
                         if (extraType == 6) {     //noball + leg bye

                             bye = false;
                             lb = false;
                             wide = false;
                             noBall = true;
                             byeNB = false;
                             lbNB = true;
                             penalty = false;

                             extras = extraRun - noballRun;
                         }

                         setExtraCard();
                         if (jsonDetails.getInt("penaltybool") == 1)
                             setPenalty();
                         setFOW(dismissedPlayerID, true);
                     }
                     else
                         setFOW(dismissedPlayerID, false);

                 }

             else
                 if (ballType == 4 || ballType == 5) {

                     lastPreBowlerID = preBowlerID;
                     preBowlerID = bowlerID;

                     maiden = mo == 0;                                                              // simplified if condition
                     tco = currentOver;
                     endOfOver = true;
                 }

             else
                 if (ballType == 6) {

                     newPartnerships = false;
                     endOfOver = false;

//                     JSONObject jsonBowler = jsonDetails.getJSONObject("bowler");
                     JSONObject jsonCurrentBowler = jsonDetails.getJSONObject("current");
                     newBowlerID = jsonCurrentBowler.getInt("id");
                 }

             else
                 if (ballType == 7) {

                     newPartnerships = true;
                     out = false;

                     if (strID == dismissedPlayerID){
                         strID = newBatsmanID;
                     }
                     else
                         if (nstrID == dismissedPlayerID){
                             nstrID = newBatsmanID;
                     }

                     if (player1ID == dismissedPlayerID){
                         player1ID = newBatsmanID;
                     }
                     else
                         if (player2ID == dismissedPlayerID){
                             player2ID = newBatsmanID;
                     }
                 }

             else
                 if (ballType == 8) {

                     newPartnerships = false;

                     if (extraType == 0) {      // bye

                         bye = true;
                         lb = false;
                         wide = false;
                         noBall = false;
                         byeNB = false;
                         lbNB = false;
                         penalty = false;

                         extras = extraRun;
                     }

                     else
                         if (extraType == 1) {     // leg bye

                             bye = false;
                             lb = true;
                             wide = false;
                             noBall = false;
                             byeNB = false;
                             lbNB = false;
                             penalty = false;

                             extras = extraRun;
                     }

                     else
                         if (extraType == 2) {     // noball + none  | noball + runs off bat

                             bye = false;
                             lb = false;
                             wide = false;
                             noBall = true;
                             byeNB = false;
                             lbNB = false;
                             penalty = false;

                             extras = extraRun - noballRun;
                     }

                     else
                         if (extraType == 3) {     // noball + bye

                             bye = false;
                             lb = false;
                             wide = false;
                             noBall = true;
                             byeNB = true;
                             lbNB = false;
                             penalty = false;

                             extras = extraRun - noballRun;
                     }

                     else
                         if (extraType == 4) {     // wide

                             bye = false;
                             lb = false;
                             wide = true;
                             noBall = false;
                             byeNB = false;
                             lbNB = false;
                             penalty = false;

                             extras = extraRun - wideRun;
                     }

                     else
                         if (extraType == 5) {     // penalty

                             bye = false;
                             lb = false;
                             wide = false;
                             noBall = false;
                             byeNB = false;
                             lbNB = false;
                             penalty = false;

                             if (penaltyType == 1) {

                                 penaltyTeam = battingTeam;
                                 remainingRuns = remainingRuns - penaltyRun;
                                 leadingRuns = leadingRuns + penaltyRun;
                                 penalty = true;
                             }
                             else
                                 if (penaltyType == 2)
                                     penaltyTeam = fieldingTeam;
                     }

                     else
                         if (extraType == 6) {     //noball + leg bye

                             bye = false;
                             lb = false;
                             wide = false;
                             noBall = true;
                             byeNB = false;
                             lbNB = true;
                             penalty = false;

                             extras = extraRun - noballRun;
                     }

                     udisplay = modified;

//        modified = tvCurrentOver.getText().toString();
                     if(extraType == 0)
                         modified = modified + "\t\tb(" + extraRun+")";
                     else if(extraType == 1)
                         modified = modified + "\t\tlb(" + extraRun+")";
                     else if(extraType == 2)
                         modified = modified + "\t\tnb(" + (extraRun + runs)+")";
                     else if(extraType == 3)
                         modified = modified + "\t\tnbb(" + extraRun+")";
                     else if(extraType == 4)
                         modified = modified + "\t\twd(" + extraRun+")";
                     else if(extraType == 5) {
                         if (penaltyType == 1)
                             modified = modified + "\t\tp";
                     }
                     else if(extraType == 6)
                         modified = modified + "\t\tnblb(" + extraRun+")";
                     else
                         modified = modified + "\t\t" + runs;

                     setExtraCard();
                 }

             else
                 if ((ballType == 9) || (ballType == 10)) {

                     newPartnerships = false;

                 preInningsRuns = jsonSummary.getInt("totalscore");
                 if (innings == 1)
                     innings1Runs = preInningsRuns;
                 else
                    if (innings == 2)
                        innings2Runs = preInningsRuns;
                 else
                    if (innings == 3)
                        innings3Runs = preInningsRuns;
                 else
                    if (innings == 4)
                        innings4Runs = preInningsRuns;
                 else
                     if (innings == 99)
                         so_innings1Runs = preInningsRuns;
                 else
                     if (innings == 100)
                         so_innings2Runs = preInningsRuns;

                 if (innings > 1) {

                     remainingRuns = preInningsRuns;
                     leadingRuns = preInningsRuns * (-1);
                 }

                 else {

                     remainingRuns = 0;
                     leadingRuns = 0;
                 }

                 if (ballType == 9 && totalInnings == 4)
                     declared = true;
                 else
                     declared = false;

             }

             else
                 if (ballType == 11 || ballType == 14) {

                     newPartnerships = false;

                 // ballType = 11 --> End Of Match
                 // ballType = 14 --> Force End Of Match

             }

             else
                 if (ballType == 12) {

                     if (out)
                         pre_out = true;
                     else
                         pre_out = false;

                     if (endOfOver)
                         callBowlerAlert = true;
                     else
                         callBowlerAlert = false;
                 }

             else
                 if (ballType == 15) {      // starting of innings

                     newPartnerships = false;
                     innings_start = true;

                     addsToBatsman();

                     if (strikerBattingOrder == 1) {
                         player1ID = strID;
                         player2ID = nstrID;
                     }

                     else if (strikerBattingOrder == 2) {

                         player2ID = strID;
                         player1ID = nstrID;
                     }

                     syncBatsman(player1ID);
                     syncBatsman(player2ID);
                 }

             else
                 if (ballType == 16) {     // end of day

                     endOfDay = true;
                 }

             else
                 if (ballType == 18 || ballType == 19) {    //  18 -> Substitution, 19 -> Concussion

                     if (ballType == 18)
                         concussion = false;
                     else
                         concussion = true;
                 }

             else
                 if (ballType == 20) {      // ballType = 20 --> Over Reduced

                     reducedOver = (float) jsonDetails.getDouble("reducedover");
                     if (innings == 2) {
                         revisedTarget = jsonDetails.getInt("revisedtarget");
                         appliedRainRule = jsonDetails.getString("appliedrainrule");
                     }
                 }

             else
                 if (ballType == 21 || ballType == 22) {

                     int start = jsonDetails.getInt("start_over");
                     int end = jsonDetails.getInt("end_over");
                     int sequence = jsonDetails.getInt("sequence");

                     if (ballType == 21 || ballType == 22)
                         savePower(ballType, start, end, sequence);
                     else {
                         p_id = deletePower(sequence);
                         savePowerEvent(ballType, p_id, start, end, sequence);
                     }

                 }

             else {


                 /*remainingRuns = remainingRuns - runs;
                 leadingRuns = leadingRuns + runs;
                 mo = mo + runs;*/


                 if (!(matchType.matches("Test")) || (totalOver < 1000)) {

                     if ((ballType == 15)) {// && (innings > 1)) {

                         remainingBalls = totalOver * ballsPerOver;
                         remainingOvers = totalOver;
                     }

                     else {

                         --remainingBalls;
                         float temp_ro = (remainingOvers - (int)remainingOvers);
                         if (temp_ro > .1f && temp_ro <= (.1f * ballsPerOver))
                             remainingOvers = remainingOvers - .1f;
                         else if ((int)(temp_ro * 10) == 0) {
                             remainingOvers = remainingOvers - 1;
                             remainingOvers = remainingOvers + (.1f * (ballsPerOver - 1));
                         }
                     }
                 }
             }

             if (totalInnings == 2)
                 freeHit = jsonDetails.getBoolean("freehit");

             savePartnership(jsonDetails.getJSONObject("fallofwickets"));
             newEvent(
                     matchid,
                     matchID,
                     innings,                                                  // for current innings
                     score,                                                    // for total runs
                     currentOver,                                              // for current over
                     totalBalls,
                     wicket,                                                   // for wicket
                     jsonDetails.getInt("ball"),                         // for current over balls
                     preInningsRuns,
                     remainingRuns,
                     remainingBalls,
                     remainingOvers,
                     leadingRuns,
                     innings1Runs,
                     innings2Runs,
                     innings3Runs,
                     innings4Runs,
                     battingTeam,                                               // batting team number
                     fieldingTeam,                                              // fielding team number
                     player1ID,
                     player2ID,
                     strID,
                     nstrID,
                     bowlerID,
                     preBowlerID,
                     lastPreBowlerID,
                     runs,
                     ballType,
                     extraType,
                     extraRun,
                     outType,
                     dismissedPlayerID,
                     fielderID,                                                 // convert int to string with comma
                     newBatsmanID,
                     penaltyType,
                     penaltyRun,
                     penaltyTeam,
                     ballCount,
                     penaltyBallCounted,
                     jsonDetails.getString("commentary"),
                     newBowlerID,
                     mo,
                     maiden,
                     modified,
                     udisplay,
                     tco,
                     jsonInterval.getInt("id"),
                     sType,
                     sessionID,
                     endOfDay,
                     SUPER_OVER,
                     so_innings1Runs,
                     so_innings2Runs,
                     declared,
                     freeHit,
                     newPartnerships,
                     innings_start,
                     substitutionID,
                     reducedOver,
                     revisedTarget,
                     appliedRainRule,
                     pre_out,
                     callBowlerAlert
                     );

             if (ballType == 4 || ballType == 5) {

                 mo = 0;
                 maiden = false;
                 modified = "";
             }

             else
                 if (ballType == 18 || ballType == 19) {

                     if (ballType == 18) {
                         JSONObject jsonSubstitution = jsonDetails.getJSONObject("substituion");
                         saveSubstitution(ballType,
                                 jsonSubstitution.getInt("team"),
                                 jsonSubstitution.getInt("playerout_id"),
                                 jsonSubstitution.getInt("playerin_id"), concussion);
                     }

                     else  {
                         JSONObject jsonConcussion = jsonDetails.getJSONObject("concussion");
                         saveSubstitution(ballType,
                                 jsonConcussion.getInt("team"),
                                 jsonConcussion.getInt("playerout_id"),
                                 jsonConcussion.getInt("playerin_id"), concussion);
                     }

                 }

                 else if (ballType == 20 && innings == 2) {

                     // update match table
                     temp_total_over = (float) totalOver;
                     resetMatchDetails(reducedOver);



                     // resetting remaining overs, remaining balls and remaining runs

                         Log.d("remainingRuns", "resetRemainingRuns, reducedOver 1, : " + reducedOver);
                         Log.d("remainingRuns", "resetRemainingRuns, revisedTarget 1, : " + revisedTarget);
                         Log.d("remainingRuns", "resetRemainingRuns, temp_total_over 1, : " + temp_total_over);


                         // added on 30/05/2020
                         Log.d("remainingRuns", "resetRemainingRuns, temp_total_over : " + temp_total_over);
                         Log.d("remainingRuns", "resetRemainingRuns, remainingOver : " + remainingOvers);
                         Log.d("remainingRuns", "resetRemainingRuns, ballsPerOver : " + ballsPerOver);

                         if (remainingOvers == temp_total_over || (remainingBalls == (temp_total_over * ballsPerOver))) {

                             remainingOvers = reducedOver;
                             Log.d("remainingRuns", "resetRemainingRuns, reducedOver : " + reducedOver);
                             Log.d("remainingRuns", "resetRemainingRuns, remainingOver : " + remainingOvers);
                             int remianining_balls1 = ((int) remainingOvers) * ballsPerOver;
                             Log.d("remainingRuns", "resetRemainingRuns, remianining_balls1 : " + remianining_balls1);
                             int remianining_balls2 = (int) ((remainingOvers - ((int) remainingOvers)) * 0.1f);
                             Log.d("remainingRuns", "resetRemainingRuns, remianining_balls2 : " + remianining_balls2);
                             remainingBalls = remianining_balls1 + remianining_balls2;
                             Log.d("remainingRuns", "resetRemainingRuns, remainingBalls : " + remainingBalls);
                         }

                         else {
                             int total_balls1 = (int) temp_total_over * ballsPerOver;
                             Log.d("remainingRuns", "resetRemainingRuns, total_balls1 : " + total_balls1);
                             float total_balls2 = (temp_total_over - (int) temp_total_over) * 10;
                             Log.d("remainingRuns", "resetRemainingRuns, total_balls2 : " + total_balls2);
                             int total_balls = total_balls1 + (int) total_balls2;
                             Log.d("remainingRuns", "resetRemainingRuns, total_balls : " + total_balls);

                             Log.d("remainingRuns", "resetRemainingRuns, reducedOver : " + reducedOver);
                             int reduced_balls1 = (int) reducedOver * ballsPerOver;
                             Log.d("remainingRuns", "resetRemainingRuns, reduced_balls1 : " + reduced_balls1);
                             float reduced_balls2 = (reducedOver - (int) reducedOver) * 10;
                             Log.d("remainingRuns", "resetRemainingRuns, reduced_balls2 : " + reduced_balls2);
                             int reduced_balls = reduced_balls1 + (int) reduced_balls2;
                             Log.d("remainingRuns", "resetRemainingRuns, reduced_balls : " + reduced_balls);


                             int ball_difference = total_balls - reduced_balls;
                             Log.d("remainingRuns", "resetRemainingRuns, ball_difference : " + ball_difference);


                             Log.d("remainingRuns", "resetRemainingRuns, remainingBalls 1, : " + remainingBalls);
                             remainingBalls = remainingBalls - ball_difference;
                             Log.d("remainingRuns", "resetRemainingRuns, remainingBalls 2, : " + remainingBalls);
                             int remaining_over1 = remainingBalls / ballsPerOver;
                             Log.d("remainingRuns", "resetRemainingRuns, remaining_over1 : " + remaining_over1);
                             int remaining_over2 = remainingBalls % ballsPerOver;
                             Log.d("remainingRuns", "resetRemainingRuns, remaining_over2 : " + remaining_over2);
                             remainingOvers = remaining_over1 + (remaining_over2 * 0.1f);
                             Log.d("remainingRuns", "resetRemainingRuns, remainingOver : " + remainingOvers);

                         }

                         // remainingRuns
                         Log.d("remainingRuns", "resetRemainingRuns, remainingRuns : " + remainingRuns);
                         Log.d("remainingRuns", "resetRemainingRuns, revisedTarget : " + revisedTarget);
                         Log.d("remainingRuns", "resetRemainingRuns, currentRuns : " + score);
                         remainingRuns = revisedTarget - score;
                         Log.d("remainingRuns", "resetRemainingRuns, after remainingRuns : " + remainingRuns);


                 }

             innings_start = false;
             retired = false;



         } catch (JSONException e) {
             e.printStackTrace();
         }
     }



     public void newEvent(int matchid, String matchID,
                          int innings, int totalRuns, float over, int balls, int wicket, int currentOverBalls,/* int temp_balls,*/
                          int preInningsRuns, int remainingRuns, int remainingBalls, float remainingOver, int leadingRuns,
                          int innings1Runs, int innings2Runs, int innings3Runs, int innings4Runs,
                          int battingTeamNo, int fieldingTeamNo,
                          int player1ID, int player2ID,
                          int strikerID, /*int strikerRuns, int strikerBalls,*/
                          int nonStrikerID, /*int nonStrikerRuns, int nonStrikerBalls,*/
                          int bowllerID, /*int bowlerRuns, int bowlerOver, int bowlerBalls, int bowlerWicket,*/
                          int preBowlerID, /*int preBowlerRuns, int preBowlerOver, int preBowlerBalls, int preBowlerWicket,*/
                          int lastPreBowlerID,
                          int run, int balType, int extraType, int extraRun,
                          int outType, int dismissedPlayerID, String fielderID, int newBatsmanID,
                          int penaltyType, int penaltyRun, int penaltyTeam, boolean ballCount, int penaltyBallCounted,
                          String com, int newBowlerID, int mo, boolean maidenOver, String currentOver, String undoDisplay,
                          float tco, /*float tbo, float tpbo,*/
                          int intervalId, String sessionType, int sessionId,
                          /*boolean sww,  if !sww the post json to server*//*JSONArray postArray, JSONArray undoArray,*/
                          boolean endOfDay, boolean super_over, int so_innings1Runs, int so_innings2Runs,
                          boolean declared, boolean freeHit, boolean newPartnerships, boolean innings_start,
                          int substitutionID, float reducedOver, int revisedTarget, String appliedRainRule,
                          boolean pre_out, boolean callBowlerAlert) {

/*
         Log.e("scoring", "newEvent, before, noBall : "+noBall);
         Log.e("scoring", "newEvent, before, runsOffBatNB : "+runsOffBatNB);
         Log.e("scoring", "newEvent, before, runsOffBat : "+runsOffBat);

         Log.d("checkFreeHit","newEvent 1, : "+checkFreeHit);
         Log.d("freeHit","newEvent 1, : "+freeHit);*/

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
                 public void execute(Realm bgRealm) {


                     try {

                         Number num = bgRealm.where(Events.class).max("eventID");
                         eventID = (num == null) ? 1 : num.intValue() + 1;

                         Events events = bgRealm.createObject(Events.class, eventID);
                         Log.d("eventId", " newEvent, eventId : "+eventID);
                         Log.d("newEvent", " after, eventId : "+eventID);

                         events.setMatchid(matchid);
                         events.setMatchID(matchID);
                         events.setInnings(innings);           // current innings
                         events.setTotalRuns(totalRuns);         // current total runs
                         events.setOvers(Float.parseFloat(
                                 new DecimalFormat("###.#").format(over))); // current playing run
                         events.setBalls(balls);                  // current total balls played
                         events.setWicket(wicket);               // current wicketas playing
                         events.setCurrentOverBalls(currentOverBalls);
                         events.setPreInningsRuns(preInningsRuns);
                         events.setLeadingRuns(leadingRuns);
                         events.setRemainingRuns(remainingRuns);
                         events.setRemainingBalls(remainingBalls);
                         events.setRemainingOvers(remainingOver);
                         events.setBattingTeamNo(battingTeamNo);
                         events.setFieldingTeamNo(fieldingTeamNo);

                         events.setInnings1Runs(innings1Runs);
                         events.setInnings2Runs(innings2Runs);
                         events.setInnings3Runs(innings3Runs);
                         events.setInnings4Runs(innings4Runs);

                         events.setPlayer1ID(player1ID);
                         events.setPlayer2ID(player2ID);

                         if (lastPreBowlerID != 0)
                             events.setLastPreBowlerID(lastPreBowlerID);

                         events.setCurrentRun(run);
                         events.setBallType(balType);

                         events.setExtraType(extraType);
                         events.setExtraRuns(extraRun);

                         events.setOutType(outType);
                         events.setDismissedPlayerID(dismissedPlayerID);// Added on 13/11/2021
                         if (dismissedPlayerID != 0) {
                             Player pd = bgRealm.where(Player.class).
                                     equalTo("matchid", matchid).
                                     equalTo("team", battingTeamNo).
                                     equalTo("playerID", dismissedPlayerID).findFirst();

                             if (pd != null)
                                 events.setDismissedPlayerD4SID(pd.getD4s_playerid());
                         }
                         // === till here
                         events.setFielderID(fielderID);
                         events.setDisNewBatsmanID(newBatsmanID);
                         // Added on 13/11/2021
                         if (newBatsmanID != 0) {
                             Player pnb = bgRealm.where(Player.class).
                                     equalTo("matchid", matchid).
                                     equalTo("team", battingTeamNo).
                                     equalTo("playerID", newBatsmanID).findFirst();

                             if (pnb != null)
                                 events.setDisNewBatsmanD4SID(pnb.getD4s_playerid());
                         }
                         // === till here

                         events.setPenaltyType(penaltyType);
                         events.setPenaltyRuns(penaltyRun);
                         events.setPenaltyRunTeam(penaltyTeam);
                         events.setPenaltyBallCount(ballCount);
                         events.setPenaltyBallCounted(penaltyBallCounted);
                         events.setCommentary(com);

                         events.setNewBowlerID(newBowlerID);
                         // Added on 13/11/2021
                         if (newBowlerID != 0) {
                             Player pnbw = bgRealm.where(Player.class).
                                     equalTo("matchid", matchid).
                                     equalTo("team", fieldingTeamNo).
                                     equalTo("playerID", newBowlerID).findFirst();

                             if (pnbw != null)
                                 events.setNewBowlerD4SID(pnbw.getD4s_playerid());
                         }
                         // === till here
                         events.setMo(mo);
                         events.setMaidenOver(maidenOver);
                         events.setCurrentOver(currentOver);
                         events.setUndoDisplay(undoDisplay);

                         events.setTco(tco);
//                         events.setTbo(tbo);
//                         events.setTpbo(tpbo);

                         events.setIntervalId(intervalId);
                         events.setSession(sessionType);
                         events.setSessionId(sessionId);

                         events.setEndOfDay(endOfDay);

                         events.setSUPER_OVER(super_over);
                         if (super_over) {
                             events.setSuper_over_innings1runs(so_innings1Runs);
                             events.setSuper_over_innings2runs(so_innings2Runs);
                         }

                         if (totalInnings == 4)
                             events.setDeclared(declared);

                         events.setFreeHit(freeHit);
                         events.setNewPartnership(newPartnerships);
                         events.setInning_started(innings_start);

                         events.setSubstitutionID(substitutionID);
                         events.setReducedOver(reducedOver);
                         events.setRevisedTarget(revisedTarget);
                         events.setAppliedRainRule(appliedRainRule);

                         events.setPre_out(pre_out);
                         events.setCallBowlerAlert(callBowlerAlert);

                         // striker details
                         events.setStrikerID(strikerID);
                         // Added on 13/11/2021
                         Player ps = bgRealm.where(Player.class).
                                 equalTo("matchid", matchid).
                                 equalTo("team", battingTeamNo).
                                 equalTo("playerID", strikerID).findFirst();

                         if (ps != null)
                             events.setStrikerD4SID(ps.getD4s_playerid());
                         // === till here

                         Batsman bat = bgRealm.where(Batsman.class).
                                 equalTo("matchid", matchid).
                                 equalTo("innings", innings).
                                 equalTo("team", battingTeamNo).
                                 equalTo("batsman_pID", strikerID).findFirst();
                         if (bat != null) {

                             events.setStrikerRuns(bat.getRuns());
                             events.setStrikerBalls(bat.getBalls());
                             events.setStrikerBattingOrder(bat.getBattingOrder());
                             events.setStrikerDots(bat.getDots());
                             events.setStrikerF4s(bat.getF4s());
                             events.setStrikerS6s(bat.getS6s());
                         }

                         //nonstriker details
                         events.setNonStrikerID(nonStrikerID);
                         // Added on 13/11/2021
                         Player pns = bgRealm.where(Player.class).
                                 equalTo("matchid", matchid).
                                 equalTo("team", battingTeamNo).
                                 equalTo("playerID", nonStrikerID).findFirst();

                         if (pns != null)
                             events.setNonStrikerD4SID(pns.getD4s_playerid());
                         // === till here
                         bat = bgRealm.where(Batsman.class).
                                 equalTo("matchid", matchid).
                                 equalTo("innings", innings).
                                 equalTo("team", battingTeamNo).
                                 equalTo("batsman_pID", nonStrikerID).findFirst();
                         if (bat != null) {

                             events.setNonStrikerRuns(bat.getRuns());
                             events.setNonStrikerBalls(bat.getBalls());
                             events.setNonStrikerBattingOrder(bat.getBattingOrder());
                             events.setNonStrikerDots(bat.getDots());
                             events.setNonStrikerF4s(bat.getF4s());
                             events.setNonStrikerS6s(bat.getS6s());
                         }


                         // bowler details
                         events.setBowlerID(bowllerID);
                         // Added on 13/11/2021
                         Player pb = bgRealm.where(Player.class).
                                 equalTo("matchid", matchid).
                                 equalTo("team", fieldingTeamNo).
                                 equalTo("playerID", bowllerID).findFirst();

                         if (pb != null)
                             events.setBowlerD4SID(pb.getD4s_playerid());
                         // === till here
                         Bowler bows = bgRealm.where(Bowler.class).
                                 equalTo("matchid", matchid).
                                 equalTo("innings", innings).
                                 equalTo("team", fieldingTeamNo).
                                 equalTo("playerID", bowllerID).findFirst();
                         if (bows != null) {

                             events.setBowlerRuns(bows.getRuns());
                             events.setBowlerOver(bows.getOver());
                             events.setBowlerBalls(bows.getBalls());
                             events.setBowlerWicket(bows.getWicket());
                             events.setBowlerDots(bows.getDots());
                             events.setBowlerMO(bows.getMaidenOver());
                             events.setBowlerWides(bows.getWides());
                             events.setBowlerNoball(bows.getNoBalls());
                         }

                         // pre bowler details
                         if (preBowlerID != 0) {
                             events.setPrevBowlerID(preBowlerID);
                             // Added on 13/11/2021
                             Player ppb = bgRealm.where(Player.class).
                                     equalTo("matchid", matchid).
                                     equalTo("team", fieldingTeamNo).
                                     equalTo("playerID", preBowlerID).findFirst();

                             if (ppb != null)
                                 events.setPrevBowlerD4SID(ppb.getD4s_playerid());
                             // === till here
                             bows = bgRealm.where(Bowler.class).
                                     equalTo("matchid", matchid).
                                     equalTo("innings", innings).
                                     equalTo("team", fieldingTeamNo).
                                     equalTo("playerID", preBowlerID).findFirst();
                             if (bows != null) {

                                 events.setPreBowlerRuns(bows.getRuns());
                                 events.setPreBowlerBalls(bows.getBalls());
                                 events.setPreBowlerOver(bows.getOver());
                                 events.setPreBowlerWicket(bows.getWicket());
                                 events.setPreBowlerDots(bows.getDots());
                                 events.setPreBowlerMO(bows.getMaidenOver());
                                 events.setPreBowlerWides(bows.getWides());
                                 events.setPreBowlerNoball(bows.getNoBalls());
                             }
                         }


                         // set extras details
                         ExtraCard extraCard = bgRealm.where(ExtraCard.class).
                                 equalTo("matchid", matchid).
                                 equalTo("innings",innings).findFirst();
                         if (extraCard != null){

                             events.setExtraBye(extraCard.getByes());
                             events.setExtraLb(extraCard.getLb());
                             events.setExtraWd(extraCard.getWide());
                             events.setExtraNb(extraCard.getNoBall());
                             events.setExtraP(extraCard.getPenalty());
                         }


                         //partnership details
                         int wk = 0;
                         if (balType == 3 && outType != 10)
                             wk = wicket;
                         else
                             wk = wicket + 1;

                         RealmResults<Partnership> results = bgRealm.where(Partnership.class).
                                 equalTo("matchid", events.getMatchid()).//matchid).
                                 equalTo("innings", events.getInnings()).//currentInnings).
//                                 equalTo("wicket", wk).   commented on 13/11/2021
                                 findAll();

                         if (results.isEmpty()) {

                             events.setP_wicket_no(wk);
                             events.setP_sequence_no(1);
                             events.setP_disId(0);
                             events.setP_broken(true);

                         }
                         else {
                             /* Commented on 13/11/2021
                             Partnership partnership = results.last();
                             if (partnership != null) {
                                 events.setP_wicket_no(partnership.getWicket());
                                 events.setP_sequence_no(partnership.getPartnershipSequence());
                                 events.setP_run(partnership.getPartnershipRuns());
                                 events.setP_ball(partnership.getPartnershipBalls());
                                 events.setP_over(partnership.getPartnershipOver());
                                 events.setP_p1Id(partnership.getPlayer1ID());
                                 events.setP_p2Id(partnership.getPlayer2ID());
                                 events.setP_disId(partnership.getDismissedPlayerID());
                                 events.setP_p1cr(partnership.getPlayer1ContributionRuns());
                                 events.setP_p1cb(partnership.getPlayer1ContributionBalls());
                                 events.setP_p2cr(partnership.getPlayer2ContributionRuns());
                                 events.setP_p2cb(partnership.getPlayer2ContributionBalls());

                                 if (balType != 3) {
                                     events.setP_broken(false);
                                 } else {
                                     if (outType == 10)
                                         events.setP_broken(false);
                                     else //if (outType != 10)
                                         events.setP_broken(true);
                                 }
                             }*/

                             // Added on 13/11/2021
                             Partnership partnership = results.last();
                             if (partnership != null &&
                                     ((partnership.getPlayer1ID() > 0) && (partnership.getPlayer2ID() > 0))) {
                                 // Added on 13/11/2021
                                 Player pp1 = bgRealm.where(Player.class).
                                         equalTo("matchid", matchid).
                                         equalTo("team", battingTeamNo).
                                         equalTo("playerID", partnership.getPlayer1ID()).findFirst();

                                 if (pp1 != null)
                                     events.setP_p1D4SID(pp1.getD4s_playerid());

                                 pp1 = bgRealm.where(Player.class).
                                         equalTo("matchid", matchid).
                                         equalTo("team", battingTeamNo).
                                         equalTo("playerID", partnership.getPlayer2ID()).findFirst();

                                 if (pp1 != null)
                                     events.setP_p2D4SID(pp1.getD4s_playerid());

                                 pp1 = bgRealm.where(Player.class).
                                         equalTo("matchid", matchid).
                                         equalTo("team", battingTeamNo).
                                         equalTo("playerID", partnership.getDismissedPlayerID()).findFirst();

                                 if (pp1 != null)
                                     events.setP_disD4SID(pp1.getD4s_playerid());
                                 // === till here
                                 events.setP_wicket_no(partnership.getWicket());
                                 events.setP_sequence_no(partnership.getPartnershipSequence());
                                 events.setP_run(partnership.getPartnershipRuns());
                                 events.setP_ball(partnership.getPartnershipBalls());
                                 events.setP_over(partnership.getPartnershipOver());
                                 events.setP_p1Id(partnership.getPlayer1ID());
                                 events.setP_p2Id(partnership.getPlayer2ID());
                                 events.setP_disId(partnership.getDismissedPlayerID());
                                 events.setP_p1cr(partnership.getPlayer1ContributionRuns());
                                 events.setP_p1cb(partnership.getPlayer1ContributionBalls());
                                 events.setP_p2cr(partnership.getPlayer2ContributionRuns());
                                 events.setP_p2cb(partnership.getPlayer2ContributionBalls());

                                 if (balType != 3) {
                                     events.setP_broken(false);
                                 } else {
                                     if (outType == 10)
                                         events.setP_broken(false);
                                     else //if (outType != 10)
                                         events.setP_broken(true);
                                 }
                             }

                             else {

                                 Partnership partnership1  = bgRealm.where(Partnership.class).
                                         equalTo("matchid", events.getMatchid()).//matchid).
                                         equalTo("innings", events.getInnings()).//currentInnings).
                                         equalTo("wicket", wicket).findAll().last();

                                 // Added on 13/11/2021
                                 Player pp1 = bgRealm.where(Player.class).
                                         equalTo("matchid", matchid).
                                         equalTo("team", battingTeamNo).
                                         equalTo("playerID", partnership1.getPlayer1ID()).findFirst();

                                 if (pp1 != null)
                                     events.setP_p1D4SID(pp1.getD4s_playerid());

                                 pp1 = bgRealm.where(Player.class).
                                         equalTo("matchid", matchid).
                                         equalTo("team", battingTeamNo).
                                         equalTo("playerID", partnership1.getPlayer2ID()).findFirst();

                                 if (pp1 != null)
                                     events.setP_p2D4SID(pp1.getD4s_playerid());

                                 pp1 = bgRealm.where(Player.class).
                                         equalTo("matchid", matchid).
                                         equalTo("team", battingTeamNo).
                                         equalTo("playerID", partnership1.getDismissedPlayerID()).findFirst();

                                 if (pp1 != null)
                                     events.setP_disD4SID(pp1.getD4s_playerid());
                                 // === till here

                                 events.setP_wicket_no(partnership1.getWicket());
                                 events.setP_sequence_no(partnership1.getPartnershipSequence());
                                 events.setP_run(partnership1.getPartnershipRuns());
                                 events.setP_ball(partnership1.getPartnershipBalls());
                                 events.setP_over(partnership1.getPartnershipOver());
                                 events.setP_p1Id(partnership1.getPlayer1ID());
                                 events.setP_p2Id(partnership1.getPlayer2ID());
                                 events.setP_disId(partnership1.getDismissedPlayerID());
                                 events.setP_p1cr(partnership1.getPlayer1ContributionRuns());
                                 events.setP_p1cb(partnership1.getPlayer1ContributionBalls());
                                 events.setP_p2cr(partnership1.getPlayer2ContributionRuns());
                                 events.setP_p2cb(partnership1.getPlayer2ContributionBalls());

                                 if (balType != 3) {
                                     events.setP_broken(false);
                                 } else {
                                     if (outType == 10)
                                         events.setP_broken(false);
                                     else //if (outType != 10)
                                         events.setP_broken(true);
                                 }
                             }
                             // till here
                         }

                         // set sync of events
                         events.setSyncstatus(1);

                         bgRealm.copyToRealm(events);


                         Log.d("Scoring", "newEvents, events : "+events);


                         if (ballType == 7 && newPartnership)
                             newPartnership = false;

                         Log.d("internet", "newEvents 1, isNetworkAvailable() : "+isNetworkAvailable());

                         Log.d("scoring", "newEvent : "+events);
                     }
                     catch (RealmPrimaryKeyConstraintException e) {
                         Toast.makeText(getApplicationContext(),
                                 "Primary Key exists, Press Update instead", Toast.LENGTH_SHORT).show();
                     }
                 }
             });
         }
         catch (RealmException e){
             Toast.makeText(getApplicationContext(), "Exception : "+ e, Toast.LENGTH_SHORT).show();
         }
         finally {

             if (realm != null) {
                 realm.close();
             }
         }
     }



     void savePartnership(JSONObject jsonPartnership) {

//        Log.d("Partnership", "syncPartnership, newPartnership : " + retired);
        // add new variable for new partnership when retiredHurt

          config = new RealmConfiguration.Builder()
                .name(AppConstants.GAME_ID + ".realm")
                .deleteRealmIfMigrationNeeded()
                .build();
        realm = Realm.getInstance(config);

         realm.executeTransaction(new Realm.Transaction() {
             @Override
             public void execute(Realm realm) {

                 try {

                     Partnership partnership;

                     RealmResults<Partnership> result_partnership = realm.where(Partnership.class).
                             equalTo("matchid", matchid).
                             equalTo("innings", innings).
                             equalTo("wicket", jsonPartnership.getInt("wicketno")).findAll();

                     if (result_partnership.isEmpty() || retired) {

//               newPartnership = false;

                         Number num = realm.where(Partnership.class).max("partnershipID");
                         int psId = (num == null) ? 1 : num.intValue() + 1;

                         Number ps = realm.where(Partnership.class).
                                 equalTo("matchid", matchid).
                                 equalTo("innings", innings).
                                 max("partnershipSequence");

                         /*int pseq = (ps == null) ? 1 : ps.intValue() + 1;

                         Log.d("Partnership", "psId : " + psId);
                         Log.d("Partnership", "sequence : " + pseq);*/
                         partnership = realm.createObject(Partnership.class, psId);
                         partnership.setPartnershipSequence(jsonPartnership.getInt("partnershipsequence"));
                         partnership.setMatchid(matchid);
                         partnership.setMatchID(matchID);
                         partnership.setInnings(innings);
                         partnership.setSUPER_OVER(SUPER_OVER);
                         partnership.setWicket(jsonPartnership.getInt("wicketno"));
                         partnership.setPlayer1ID(jsonPartnership.getInt("player1id"));
                         partnership.setPlayer2ID(jsonPartnership.getInt("player2id"));
                     }

//             else {

//                newPartnership = false;
                     partnership = result_partnership.last();
                     if (partnership != null) {
                         partnership.setPartnershipRuns(jsonPartnership.getInt("partnershipruns"));

                         partnership.setPartnershipBalls(jsonPartnership.getInt("partnershipballs"));
                         partnership.setPartnershipOver(jsonPartnership.getInt("partnershipover"));
                         partnership.setDismissedPlayerID(jsonPartnership.getInt("dismissedplayerid"));

                         if ((jsonPartnership.getInt("player1id") == player1ID) &&
                                 (jsonPartnership.getInt("player2id") == player2ID)) {

                             partnership.setPlayer1Runs(player1Runs);
                             partnership.setPlayer1Balls(player1Balls);
                             partnership.setPlayer1ContributionBalls(jsonPartnership.getInt("player1contributedballs"));
                             partnership.setPlayer1ContributionRuns(jsonPartnership.getInt("player1contributedruns"));

                             partnership.setPlayer2Runs(player2Runs);
                             partnership.setPlayer2Balls(player2Balls);
                             partnership.setPlayer2ContributionBalls(jsonPartnership.getInt("player2contributedballs"));
                             partnership.setPlayer2ContributionRuns(jsonPartnership.getInt("player2contributedruns"));

                         } else if ((jsonPartnership.getInt("player1id") == player2ID) &&
                                 (jsonPartnership.getInt("player2id") == player1ID)) {

                             partnership.setPlayer1Runs(player2Runs);
                             partnership.setPlayer1Balls(player2Balls);
                             partnership.setPlayer1ContributionBalls(jsonPartnership.getInt("player1contributedballs"));
                             partnership.setPlayer1ContributionRuns(jsonPartnership.getInt("player1contributedruns"));

                             partnership.setPlayer2Runs(player1Runs);
                             partnership.setPlayer2Balls(player1Balls);
                             partnership.setPlayer2ContributionBalls(jsonPartnership.getInt("player2contributedballs"));
                             partnership.setPlayer2ContributionRuns(jsonPartnership.getInt("player2contributedruns"));

                         }
                     }
//

                     realm.copyToRealmOrUpdate(partnership);
                     Log.d("partnership", "syncPartnership : " + partnership);

                 } catch (JSONException e) {
                     e.printStackTrace();
                 }


//            }
             }
         });
     }


     void syncBatsman(int batsmanID) {

          config = new RealmConfiguration.Builder()
                .name(AppConstants.GAME_ID + ".realm")
                .deleteRealmIfMigrationNeeded()
                .build();
        realm = Realm.getInstance(config);

         realm.executeTransaction(new Realm.Transaction() {
             @Override
             public void execute(Realm realm) {

                 Batsman batsmann = realm.where(Batsman.class).
                         equalTo("matchid", matchid).
                         equalTo("innings", innings).
                         equalTo("team", battingTeam).
                         equalTo("batsman_pID", batsmanID).findFirst();
                 Log.d("Scoring","synchBatsmann, batsmann :"+batsmann);

                 if (batsmann == null){

                     Number num = realm.where(Batsman.class).max("batsmanID");
                     int batId = (num == null) ? 1 : num.intValue() + 1;

//                    int batId = RandomNumber.generate("Batsman");
                     batsmann = realm.createObject(Batsman.class, batId);
                     batsmann.setBatsman_pID(batsmanID);
                     batsmann.setMatchid(matchid);
                     batsmann.setMatchID(matchID);
//                    batsmann.setBatsmanName(batsman);
                     batsmann.setTeam(battingTeam);
                     batsmann.setInnings(innings);
                     batsmann.setPlaying(true);
                     batsmann.setSUPER_OVER(SUPER_OVER);

                     if (batsmanID == strID)
                         batsmann.setBattingOrder(strikerBattingOrder);
                     else
                         if (batsmanID == nstrID)
                             batsmann.setBattingOrder(nonStrikerBattingOrder);

                     Log.d("Scoring","synchBatsmann, null, bid :"+batsmanID);
                     Log.d("Scoring","synchBatsmann, null, matchid :"+matchid);
                     Log.d("Scoring","synchBatsmann, null, matchID :"+matchID);
//                    Log.d("Scoring","synchBatsmann, null, batsman :"+batsman);
                     Log.d("Scoring","synchBatsmann, null, player.getTeam() :"+ battingTeam);
                     Log.d("Scoring","synchBatsmann, null, currentInnings :"+innings);
                 }

                 if (batsmanID == strID) {

                     batsmann.setRuns(strikerRuns);
                     batsmann.setBalls(strikerBalls);
                     batsmann.setDots(strikerDots);
                     batsmann.setF4s(strikerF4s);
                     batsmann.setS6s(strikerS6s);
                     batsmann.setRetired(retired);
                     Log.d("Scoring","synchBatsmann, not null, strikerRuns :"+strikerRuns);
                     Log.d("Scoring","synchBatsmann, not null, strikerBalls :"+strikerBalls);
                     Log.d("Scoring","synchBatsmann, not null, strikerDots :"+strikerDots);
                     Log.d("Scoring","synchBatsmann, not null, strikerF4s :"+strikerF4s);
                     Log.d("Scoring","synchBatsmann, not null, strikerS6s :"+strikerS6s);
                     if (strikerRuns > 0 || strikerBalls > 0) {
                         batsmann.setPlaying(true);
                         batsmann.setToBeBatted(false);
                     }

                 }

                 else if (batsmanID == nstrID) {

                     batsmann.setRuns(nonStrikerRuns);
                     batsmann.setBalls(nonStrikerBalls);
                     batsmann.setDots(nonStrikerDots);
                     batsmann.setF4s(nonStrikerF4s);
                     batsmann.setS6s(nonStrikerS6s);
                     Log.d("Scoring","synchBatsmann, not null, nonStrikerRuns :"+nonStrikerRuns);
                     Log.d("Scoring","synchBatsmann, not null, nonStrikerBalls :"+nonStrikerBalls);
                     Log.d("Scoring","synchBatsmann, not null, nonStrikerDots :"+nonStrikerDots);
                     Log.d("Scoring","synchBatsmann, not null, nonStrikerF4s :"+nonStrikerF4s);
                     Log.d("Scoring","synchBatsmann, not null, nonStrikerS6s :"+nonStrikerS6s);
                 }


                 if (out) {
                     batsmann.setOut(true);
                     batsmann.setPlaying(false);
                     batsmann.setRetired(false);
                     batsmann.setOutType(outType);
                     batsmann.setBowler_pID(bowlerID);
                     batsmann.setFielder_pID(fielderID);
//                    batsmann.setRetired(retired);
                 }



                 else {
                     batsmann.setOut(false);
                     batsmann.setPlaying(true);
                 }

                 batsmann.setToBeBatted(false);

                 if (retired){
                     batsmann.setRetired(true);
                     batsmann.setPlaying(false);
                 }

                 if (SUPER_OVER)
                     realm.copyToRealm(batsmann);

                 else
                     realm.copyToRealmOrUpdate(batsmann);
             }
         });
     }


     void addsToBatsman() {

         RealmResults<Player> resultb = realm.where(Player.class).
                 equalTo("matchid", matchid).
                 equalTo("team", battingTeam).
                 equalTo("retired", false).
                 equalTo("substitute", false).findAll();

         resultb.load();
         for (Player player : resultb) {

              config = new RealmConfiguration.Builder()
                .name(AppConstants.GAME_ID + ".realm")
                .deleteRealmIfMigrationNeeded()
                .build();
        realm = Realm.getInstance(config);
             realm.executeTransaction(new Realm.Transaction() {
                 @Override
                 public void execute(Realm realm) {

                     Batsman batsmann = realm.where(Batsman.class).
                             equalTo("matchid", matchid).
                             equalTo("innings", innings).
                             equalTo("team", battingTeam).
                             equalTo("batsman_pID", player.getPlayerID()).findFirst();


                     Log.e("Scoring", "addsToBatsman, batsman : "+batsmann);

                     if (batsmann == null){

                         Number num = realm.where(Batsman.class).max("batsmanID");
                         int batId = (num == null) ? 1 : num.intValue() + 1;
//                        int batId = RandomNumber.generate("Batsman");
                         batsmann = realm.createObject(Batsman.class, batId);
                         batsmann.setBatsman_pID(player.getPlayerID());
                         batsmann.setMatchid(matchid);
                         batsmann.setMatchID(matchID);
//                        batsmann.setBatsmanName(player.getPlayerName());
                         batsmann.setTeam(battingTeam);
                         batsmann.setInnings(innings);

                        /*Number pos = realm.where(Batsman.class).
                                equalTo("team", team).
                                equalTo("innings", currentInnings).max("position");
                        int nextPos = (pos == null) ? 1 : pos.intValue() + 1;
                        batsmann.setPosition(nextPos);*/

                         Log.d("Scoring", "addsToBatsman, method, player.getPlayerID() : "+player.getPlayerID());
//                        Log.d("Scoring", "addsToBatsman, method, nextPos : "+nextPos);
                     }

                     Log.d("Scoring", "addsToBatsman, method, player : "+player);

                     realm.insertOrUpdate(batsmann);
                 }
             });

         }
     }


     // for saving bowler details
     void syncBowler(JSONObject jsonBowler) {

          config = new RealmConfiguration.Builder()
                .name(AppConstants.GAME_ID + ".realm")
                .deleteRealmIfMigrationNeeded()
                .build();
        realm = Realm.getInstance(config);
         realm.executeTransaction(new Realm.Transaction() {
             @Override
             public void execute(Realm realm) {

                 try {

                     Bowler bowller = realm.where(Bowler.class).
                             equalTo("matchid", matchid).
                             equalTo("innings", innings).
                             equalTo("team", fieldingTeam).
                             equalTo("playerID", bowlerID).findFirst();
                     Log.d("synchBowler", "bowlerID : " + bowlerID);
                     Log.d("synchBowler", "bowler : " + bowller);


                     // commented because details already getting from jsonBowler
                     /*if (bowlerBalls >= ballsPerOver) {
                         ++bowlerOver;
                         if (bowlerBalls == ballsPerOver)
                             bowlerBalls = 0;
                         else if (bowlerBalls == (ballsPerOver + 1))
                             bowlerBalls = 1;
                     }*/



                     if (bowller != null) {

                         try {
                             bowller.setOver(jsonBowler.getInt("over"));
                             bowller.setBalls(jsonBowler.getInt("balls"));
                             bowller.setTotalBalls(jsonBowler.getInt("totalballs"));
                             bowller.setMaidenOver(jsonBowler.getInt("maiden"));
                             bowller.setRuns(jsonBowler.getInt("run"));
                             bowller.setWicket(jsonBowler.getInt("wicket"));
                             bowller.setDots(jsonBowler.getInt("dots"));
                             bowller.setF4s(jsonBowler.getInt("fours"));
                             bowller.setS6s(jsonBowler.getInt("sixes"));
                             bowller.setWides(jsonBowler.getInt("wide"));
                             bowller.setNoBalls(jsonBowler.getInt("noball"));

                         } catch (JSONException e) {
                             e.printStackTrace();
                         }

                         realm.insertOrUpdate(bowller);
                     }
                 }

                 catch (RealmException e){
                     Log.d("SynchBowler", "Exception  : "+e);
                 }
             }
         });
     }



     void setExtraCard() {

        Log.d("Scoring", "setExtraCard, inside setExtraCard");

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

                        ExtraCard extraCard = bgrealm.where(ExtraCard.class).equalTo("matchid", matchid).
                                equalTo("innings", innings).findFirst();

                        Log.d("Scoring", "setExtraCard, ExtraCard : "+extraCard);

                        if (extraCard == null) {

                            extraCard = new ExtraCard();
                            extraCard.setInnings(innings);
                            extraCard.setMatchid(matchid);
                            extraCard.setMatchID(matchID);
                            extraCard.setTeam(battingTeam);

//                            newBowler();
                        }

                        int ecByes = extraCard.getByes();
                        int ecLB = extraCard.getLb();
                        int ecNoball = extraCard.getNoBall();
                        int ecWide = extraCard.getWide();
                        int ecPenlaty = extraCard.getPenalty();
//                Log.d("scoring", "object ; " + extraCard);
                        Log.d("scoring", "b4 b ; " + ecByes);
                        Log.d("scoring", "b4 lb ; " + ecLB);
                        Log.d("scoring", "b4 nb ; " + ecNoball);
                        Log.d("scoring", "b4 wd ; " + ecWide);
                        Log.d("scoring", "b4 p ; " + ecPenlaty);

                        if (bye) {
                            ecByes = ecByes + extras;
                        }

                        else
                            if (lb) {
                                ecLB = ecLB + extras;
                            }

                        else
                            if (wide) {
                                ecWide = ecWide + extras;
                            }

                        else
                            if (noBall) {
                                ++ecNoball;

                                if (byeNB) {
                                    ecByes = ecByes + extras;
                                }

                                if (lbNB) {
                                    ecLB = ecLB + extras;
                                }
                            }

                        else
                            if (penalty) {
                                ++ecPenlaty;
                            }

                        Log.d("scoring", " b ; " + ecByes);
                        Log.d("scoring", " lb ; " + ecLB);
                        Log.d("scoring", " nb ; " + ecNoball);
                        Log.d("scoring", " wd ; " + ecWide);
                        Log.d("scoring", " p ; " + ecPenlaty);

                        extraCard.setByes(ecByes);
                        extraCard.setLb(ecLB);
                        extraCard.setWide(ecWide);
                        extraCard.setNoBall(ecNoball);
                        extraCard.setPenalty(ecPenlaty);

                        bgrealm.insertOrUpdate(extraCard);

                    } catch (RealmPrimaryKeyConstraintException e) {
                        Toast.makeText(getApplicationContext(),
                                "Primary Key exists, Press Update instead", Toast.LENGTH_SHORT).show();
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



     private void setFOW(int dismissedPlayerID, boolean extras) {         // adding dismissed players to FOW table

         RealmResults<FOW> result= realm.where(FOW.class).
                 equalTo("matchid", matchid).
                 equalTo("innings", innings).findAll();

         FOW tfow = new FOW();

         if (result.isEmpty())
             tem = "" ;

         else {
             tfow = result.last();

             if (tfow == null)
                 tem = "";
             else
                 tem = tfow.getOver();
         }

          config = new RealmConfiguration.Builder()
                .name(AppConstants.GAME_ID + ".realm")
                .deleteRealmIfMigrationNeeded()
                .build();
        realm = Realm.getInstance(config);
         realm.executeTransaction(new Realm.Transaction() {
             @Override
             public void execute(Realm bgRealm) {

                 try {


                     Number num = bgRealm.where(FOW.class).max("fowID");
                     int nextId = (num == null) ? 1 : num.intValue() + 1;

                     FOW fow = bgRealm.createObject(FOW.class, nextId);

//                    fow = realm.createObject(FOW.class);
                     fow.setMatchid(matchid);
                     fow.setMatchID(matchID);
                     fow.setInnings(innings);
                     fow.setTeam(battingTeam);
                     fow.setRun(runs);
                     fow.setWicket(wicket);
                     fow.setDismissedPlayerID(dismissedPlayerID);
                     Log.d("scoring", "setFOW, currentOver : " +
                             (String.valueOf(new DecimalFormat("##.#").format(currentOver))));

                     if (extras) {
                         Log.d("scoring", "setFOW, tem : " + tem);
                         Log.d("scoring", "setFOW, extras : " + extras +
                                 ", currentOver : " + (String.valueOf(new DecimalFormat("##.#").format(currentOver)) + 0.1));

                         Log.d("scoring", "setFOW, currentOver : " + (new DecimalFormat("##.#").format(currentOver)));
                         Log.d("scoring", "setFOW, currentOver new : " +
                                 (new DecimalFormat("##.#").format(currentOver)) + ".1");

                         String xyz1 = (new DecimalFormat("##.#").format(currentOver)) + ".1";
                         String xyz2 = (new DecimalFormat("##.#").format(currentOver)) + ".2";
                         String xyz3 = (new DecimalFormat("##.#").format(currentOver)) + ".3";
                         String xyz4 = (new DecimalFormat("##.#").format(currentOver)) + ".4";
                         String xyz5 = (new DecimalFormat("##.#").format(currentOver)) + ".5";

                         if (tem == null || tem.matches(""))
                             fow.setOver(xyz1);
                         else
                         if (xyz1.matches(tem))
                             fow.setOver(xyz2);
                         else
                         if (xyz2.matches(tem))
                             fow.setOver(xyz3);
                         else
                         if (xyz3.matches(tem))
                             fow.setOver(xyz4);
                         else
                         if (xyz4.matches(tem))
                             fow.setOver(xyz5);
                     }

                     else
                         fow.setOver(String.valueOf(new DecimalFormat("##.#").format(currentOver)));
//                    fow.setBatsmanName(dismissedPlayer);
                     bgRealm.insertOrUpdate(fow);
                 }

                 catch (RealmException e){
                     Log.d("SynchBowler", "Exception  : "+e);
                 }
             }
         });
     }



     void setPenalty() {

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

                         Number num = bgRealm.where(Penalty.class).max("id");
                         int nextId = (num == null) ? 1 : num.intValue() + 1;

                         Penalty penalty = bgRealm.createObject(Penalty.class, nextId);

                         penalty.setMatchid(matchid);
                         penalty.setMatchID(matchID);
                         penalty.setInnings(innings);
                         penalty.setType(penaltyType);
                         penalty.setPenaltyRun(penaltyRun);
                         if (penaltyType == 1)
                             penalty.setBenefitTeam(battingTeam);
//                         penalty.setForTeam(penaltyRunTeam);
                         else
                             if (penaltyType == 2)
                                 penalty.setBenefitTeam(fieldingTeam);
//                         penalty.setBallCount(ballcount);       // get this from post variable, later

                         if (noBall || wide)
                             penalty.setBallCount(false);

                         /*else {       // penalty ball count in normal case
                             if (extra) {

//                             else
//                                 penalty.setBallCount(false);
                             } else if (out) {

                             }
                         }*/

                         if (penaltyType == 1) {        //  Fielding Penalty--> runs goes to batting team
                             penalty.setBowball(true);
                             penalty.setBatsball(false);
                         }

                         else if (penaltyType == 2) {       //  Batting Penalty--> runs goes to fielding team
                             penalty.setBowball(false);
                             penalty.setBatsball(true);
                         }


                         if (penaltyType == 1)
                             penalty.setSync(true);
                         else
                             if (penaltyType == 2) {

                                 if ((totalInnings == innings) || SUPER_OVER)
                                     penalty.setSync(true);
                                 else
                                     penalty.setSync(false);
                             }
//                        else if (penaltyType == 2)
//                            penalty.setSync(false);

                         bgRealm.copyToRealm(penalty);


                     } catch (RealmPrimaryKeyConstraintException e) {
                         Toast.makeText(getApplicationContext(),
                                 "Primary Key exists, Press Update instead", Toast.LENGTH_SHORT).show();
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




     // to save concussion-substitution

     void saveSubstitution(int ballType, int team, int pout_id, int pin_id, boolean concussion) {

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
                                 equalTo("playerID", pout_id).
                                 findFirst();

                         int d4s_pout_id = player.getD4s_playerid();

                         if (concussion) {

                             player.setRetired(true);
                             player.setRetired_innings(innings);
                             bgRealm.copyToRealmOrUpdate(player);
                         }




                         // for player in
                         player = bgRealm.where(Player.class).
                                 equalTo("matchid", matchid).
                                 equalTo("team", team).
                                 equalTo("playerID", pin_id).
                                 findFirst();

                         int d4s_pin_id = player.getD4s_playerid();

                         if (concussion) {

                             player.setSubstitute(false);
                             bgRealm.copyToRealmOrUpdate(player);

                             if (team == battingTeam) {

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
                                 Log.d("order", "substitution : battingTeamNo : " + battingTeam);
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
             saveSubEvent(ballType, subID, pout_id, pin_id, team);
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




     void saveSubEvent(int ballType, int subID, int pout_id, int pin_id, int team) {

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

                         Events events = realm.where(Events.class).//equalTo("eventID", eventID).findFirst();
                                 equalTo("matchid", matchid).
                                 findAll().last();
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
     }




     public void savePower(int ballType, int start, int end, int sequence) {

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

//                         p_id = id;

                         if (ballType == 22) {      // update Powerplay

                             Power power = bgRealm.where(Power.class).
                                     equalTo("matchid", matchid).
                                     equalTo("count", sequence).findFirst();

                             p_id = power.getId();
                             power.setStart(start);
                             power.setEnd(end);
                             power.setSync(1);

                             bgRealm.copyToRealmOrUpdate(power);
                             Log.d("power", "edit, saveToDB : " + power);
                         }

                         else
                             if (ballType == 21){       // new powerplay

                                 Number num = bgRealm.where(Substitution.class).max("id");
                                 p_id = (num == null) ? 1 : num.intValue() + 1;
                                 Power power = bgRealm.createObject(Power.class, p_id);

                                 power.setMatchid(matchid);
                                 power.setMatchID(matchID);
                                 power.setInnings(innings);
                                 power.setStart(start);
                                 power.setEnd(end);

                                 /*num = bgRealm.where(Substitution.class).
                                         equalTo("matchid", matchid).
                                         equalTo("innings", innings).
                                         max("count");
                                 count = (num == null) ? 1 : num.intValue() + 1;*/
                                 power.setCount(sequence);     // sequence

                                 bgRealm.copyToRealm(power);
                                 Log.d("power", "new, saveToDB : " + power);
                             }

                     } catch (RealmPrimaryKeyConstraintException e) {
                         Toast.makeText(getApplicationContext(),
                                 "Primary Key exists, Press Update instead", Toast.LENGTH_SHORT).show();
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

         savePowerEvent(ballType, p_id, start, end, sequence);//id, new_pp, edit, false, start, end, count);
     }




     void savePowerEvent(int ballType, int pp_id, int start, int end, int sequence) {

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

                         Log.d("poerplay", "finalBallType  : " + ballType);
                         Events events = realm.where(Events.class).//equalTo("eventID", eventId).findFirst();
                         //equalTo("innings", innings).findAll().last();
                                 equalTo("matchid", matchid).
                                 findAll().last();
                         Log.d("eventId", "updateSubstitution, events  : "+ events);
                         events.setBallType(ballType);
                         events.setPower_id(pp_id);
                         events.setPower_start_over(start);
                         events.setPower_end_over(end);
                         events.setPower_sequence(sequence);
                         realm.copyToRealmOrUpdate(events);
                         Log.d("powerplay", "events : " + events);
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



     // for deleting powerplay record
     int deletePower(int sequence) {

         int maxSeq = (int) realm.where(Power.class).
                 equalTo("matchid", matchid).
                 equalTo("innings", innings).
                 max("count");

         Power power = realm.where(Power.class).
                 equalTo("matchid", matchid).
                 equalTo("innings", innings).
                 equalTo("count", sequence).findFirst();

         int p_id = power.getId();

//         if (power != null) {

             if (!realm.isInTransaction()) {
                 realm.beginTransaction();
             }

             power.deleteFromRealm();
             realm.commitTransaction();
//         }


         if (sequence == maxSeq) {

             // do nothing, becuase the deleted powerplay is the last one in that innings
         }

         else {

             /*if (maxSeq == 2) {

                  power = realm.where(Power.class).
                         equalTo("matchid", matchid).
                         equalTo("innings", innings).findFirst();

                 if (power != null) {
                     power.setCount(1);
                     realm.copyToRealmOrUpdate(power);
                 }
             }

             else */
             if (sequence == 1) {


                 RealmResults<Power> results = realm.where(Power.class).
                         equalTo("matchid", matchid).
                         equalTo("innings", innings).
                         sort("count", Sort.ASCENDING).findAll();

                 if (results.size() > 0) {

//                   if (sequence == 1) {
                     for (int i = 2; i <= results.size() + 1; i++) {

                         power = realm.where(Power.class).
                                 equalTo("matchid", matchid).
                                 equalTo("innings", innings).
                                 equalTo("count", i).findFirst();

                         if (power != null) {

                             updatePower(power);
                         }
                     }
//                 }
                 }
             }

             else
                 if (sequence == 2) {

                     RealmResults<Power> results = realm.where(Power.class).
                             equalTo("matchid", matchid).
                             equalTo("innings", innings).
                             sort("count", Sort.ASCENDING).findAll();

                     if (results.size() > 0) {

                         for (int i = 3; i <= results.size() + 1; i++) {

                             power = realm.where(Power.class).
                                     equalTo("matchid", matchid).
                                     equalTo("innings", innings).
                                     equalTo("count", i).findFirst();

                             if (i > 1) {

                                 if (power != null) {

                                     updatePower(power);
                                 }
                             }
                         }
                     }
                 }
         }

         return p_id;
     }



     // added on 29/05/2020
     // to change the sequence of the powerplay
     void updatePower(Power power) {

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

                         int seq = power.getCount();
                         --seq;
                         power.setCount(seq);

                         bgRealm.copyToRealm(power);

                     }  catch (RealmPrimaryKeyConstraintException e) {
                         Toast.makeText(getApplicationContext(),
                                 "Primary Key exists, Press Update instead", Toast.LENGTH_SHORT).show();
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
     }



     // added on 30/05/2020
     // updating total over after over reduced
     void resetMatchDetails(float reducedOver) {

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

                         Match match = bgRealm.where(Match.class).
                                 equalTo("matchid", matchid).findFirst();

                         if (match != null) {

                             match.setOver(reducedOver);
                             bgRealm.copyToRealm(match);
                         }

                     }  catch (RealmPrimaryKeyConstraintException e) {
                         Toast.makeText(getApplicationContext(),
                                 "Primary Key exists, Press Update instead", Toast.LENGTH_SHORT).show();
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
     }

 }
