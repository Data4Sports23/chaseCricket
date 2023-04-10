package com.data4sports.chasecricket.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.data4sports.chasecricket.models.Constants;
import com.data4sports.chasecricket.models.Match;
import com.data4sports.chasecricket.models.MatchNotes;
import com.data4sports.chasecricket.models.MatchOfficials;
import com.data4sports.chasecricket.models.MyApplicationClass;

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

/**
 * Creatde on16/10/2020
 */

public class MatchNoteListActivity extends AppCompatActivity {

    EditText et_match_note;
    Button btn_add_note;
    LinearLayout ll_previous_notes;

    TableLayout note_table;

    Realm realm;
    ImageButton back;

    int matchid, innings, slno = 0;
    float over;
    String note, matchID;

    RealmConfiguration config;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_match_note_list);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.custom_action_bar_layout);
        View view =getSupportActionBar().getCustomView();

        back = (ImageButton) view.findViewById(R.id.action_bar_back);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*finish();*/
                Log.d("scoring", "oncreate, back button pressd");

                onBackPressed();
//                startActivity(new Intent(MatchNoteListActivity.this, ScoringActivity.class));
            }
        });

        et_match_note = findViewById(R.id.et_note);
        btn_add_note = findViewById(R.id.btn_add_note);
        ll_previous_notes = findViewById(R.id.ll_previous_notes);

         config = new RealmConfiguration.Builder()
                .name(AppConstants.GAME_ID + ".realm")
                .deleteRealmIfMigrationNeeded()
                .build();
        realm = Realm.getInstance(config);

        getFromIntent();

        note_table = findViewById(R.id.notes_table);
        TableRow rowh = new TableRow(this);
        rowh.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, 50));
        rowh.setPadding(0, 20, 0, 20);

        TextView tvh1 = new TextView(this);
        tvh1.setTextSize(14);
        tvh1.setTextColor(Color.BLACK);
        tvh1.setText("SNo.");
        rowh.addView(tvh1);

        /*TextView tvh2 = new TextView(this);
        tvh2.setTextSize(14);
        tvh2.setTextColor(Color.BLACK);
        tvh2.setText("\tInngs");
        rowh.addView(tvh2);

        TextView tvh3 = new TextView(this);
        tvh3.setTextSize(14);
        tvh3.setTextColor(Color.BLACK);
        tvh3.setText("\tOver");
        rowh.addView(tvh3);*/

        TextView tvh4 = new TextView(this);
        tvh4.setTextSize(14);
        tvh4.setTextColor(Color.BLACK);
        tvh4.setText("\tNotes");
        rowh.addView(tvh4);

        note_table.addView(rowh);

       /* TableRow rowh1 = new TableRow(this);
        rowh1.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, 1));
        rowh1.setPadding(10, 10, 10, 10);


       *//* TextView tv11 = new TextView(this);
        tv11.setTextSize(13);
        tv11.setTextColor(Color.BLACK);
        tv11.setText(" \t" + slno);//matchNotes.getSequence());
        row1.addView(tv11);
        *//*
        View view1 = new View(this);
        view1.setBackgroundColor(Color.BLACK);
        view1.setFitsSystemWindows(true);
        rowh1.addView(view1);
        note_table.addView(rowh1);

      *//*  <View
        android:id="@+id/line1"
        android:layout_width="match_parent"
        android:layout_height="1dip"
        android:layout_weight="1"
        android:background="#FF909090"
        android:padding="2dip" />*//*
*/
        displayNotesList();

        btn_add_note.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (et_match_note.getText().toString().matches(""))
                    displayError("Please enter valid match note");

                else {
                    note = et_match_note.getText().toString();
                    saveToDB(true, false, null, note);
                    et_match_note.setText("");
                    Toast.makeText(MatchNoteListActivity.this, "Match note added", Toast.LENGTH_SHORT).show();
//                    displayNotesList();
                }
            }
        });
    }


    //to save notes
    void saveToDB(boolean new_note, boolean edit_note, MatchNotes matchNotes, String note) {

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

                        if (new_note) {

                            Number num = bgrealm.where(MatchNotes.class).max("note_id");
                            int nextId = (num == null) ? 1 : num.intValue() + 1;

                            Number num2 = bgrealm.where(MatchNotes.class).
                                    equalTo("matchid", matchid).max("sequence");
                            int sequence = (num2 == null) ? 1 : num2.intValue() + 1;

                            MatchNotes notes = bgrealm.createObject(MatchNotes.class, nextId);
                            notes.setMatchid(matchid);
                            notes.setMatchID(matchID);
                            notes.setOver(over);
                            notes.setSequence(sequence);
                            notes.setInnings(innings);
                            notes.setAdd(true);
                            notes.setEdit(false);
                            notes.setSync(0);
                            notes.setNote(note);
                            bgrealm.copyToRealm(notes);
                            Log.d("MatchNote", " saveToDB, new_note : " + new_note + "matchNotes : " + matchNotes);

                        }

                        else
                            if (edit_note) {
                                if (matchNotes != null) {

                                    matchNotes.setEdit(true);
                                    matchNotes.setAdd(false);
                                    matchNotes.setSync(0);
                                    matchNotes.setNote(note);
                                    bgrealm.copyToRealmOrUpdate(matchNotes);
                                    Log.d("MatchNote", " saveToDB, edit_note : " + edit_note + "matchNotes : " + matchNotes);
                                }
                            }

                            refreshTable();
                            displayNotesList();
                            postMatchNote();
                    }
                    catch (RealmPrimaryKeyConstraintException e) {
                        Log.d("MatchNote", " Exception : "+e);
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


    // to display match notes
    void displayNotesList() {

        RealmResults<MatchNotes> results = realm.where(MatchNotes.class).
                equalTo("matchid", matchid).
                equalTo("delete", false).
                sort("sequence", Sort.DESCENDING).findAll();

        if (results.size() > 0) {

            ll_previous_notes.setVisibility(View.VISIBLE);

            for (MatchNotes matchNotes : results) {

                ++slno;

                TableRow row1 = new TableRow(this);
                row1.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, 35));
                row1.setPadding(0, 25, 0, 25);

                TextView tv11 = new TextView(this);
                tv11.setTextSize(13);
                tv11.setTextColor(Color.BLACK);
                tv11.setText(" \t" + slno);//matchNotes.getSequence());
                row1.addView(tv11);

                /*TextView tv12 = new TextView(this);
                tv12.setTextSize(13);
                tv12.setTextColor(Color.BLACK);
                tv12.setText(" \t\t" + matchNotes.getInnings());
                row1.addView(tv12);

                TextView tv13 = new TextView(this);
                tv13.setTextSize(13);
                tv13.setTextColor(Color.BLACK);
                tv13.setText(" \t" + (String.valueOf(new DecimalFormat("##.#").format(matchNotes.getOver()))));
                row1.addView(tv13);*/

                TextView tv14 = new TextView(this);
                tv14.setTextSize(13);
                tv14.setTextColor(Color.BLACK);
                tv14.setText(" \t" + matchNotes.getNote());
                row1.addView(tv14);

                TextView tv15 = new TextView(this);
                tv15.setTextSize(16);
                tv15.setText(" \t\t");
                row1.addView(tv15);


                ImageView edit = new ImageView(this);
                edit.setImageResource(R.drawable.edit);
                edit.setColorFilter(Color.GREEN);
                edit.setId(matchNotes.getNote_id());
                edit.setOnClickListener(editListener);
                row1.addView(edit);


                View v = new View(this);
                v.setLayoutParams(new TableRow.LayoutParams( 30, TableLayout.LayoutParams.MATCH_PARENT));
                row1.addView(v);


                ImageView delete = new ImageView(this);
                delete.setImageResource(R.drawable.delete);
                delete.setId(matchNotes.getNote_id());
                delete.setColorFilter(Color.RED);
                delete.setOnClickListener(deleteListener);
                row1.addView(delete);

                note_table.addView(row1);

            }
        }

        else
            ll_previous_notes.setVisibility(View.INVISIBLE);

    }


    public void displayError(String message){

        AlertDialog alertDialog = new AlertDialog.Builder(MatchNoteListActivity.this).create();
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


    void getFromIntent() {

        Intent i = getIntent();
        matchid = i.getIntExtra("matchid", 0);
        matchID = i.getStringExtra("matchID");
        innings = i.getIntExtra("innings", 0);
        over = i.getFloatExtra("over", 0f);
    }


    private final View.OnClickListener editListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {


            Log.d("power", "edit");
            Log.d("power", "edit, matchid : " + matchid);
            Log.d("power", "edit, innings : " + innings);
            Log.d("power", "edit, v.getId() : " + v.getId());


             config = new RealmConfiguration.Builder()
                .name(AppConstants.GAME_ID + ".realm")
                .deleteRealmIfMigrationNeeded()
                .build();
        realm = Realm.getInstance(config);

            MatchNotes matchNotes = realm.where(MatchNotes.class).
                    equalTo("matchid", matchid).
                    equalTo("note_id", v.getId()).findFirst();
            /*Power power = realm.where(Power.class).
                    equalTo("matchid", matchid).
                    equalTo("innings", innings).
                    equalTo("id", v.getId()).findFirst();*/

            Log.d("matchNotes", "edit, matchNotes 1 : " + matchNotes);
            if (matchNotes != null) {

                Log.d("matchNotes", "edit, matchNotes 2 : " + matchNotes);
                editMatchNote(matchNotes);

            }
        }
    };


    void editMatchNote(MatchNotes matchNotes) {

        Log.d("matchnote", "editMatchNote, matchNotes  : " + matchNotes);
        if (matchNotes != null) {

            // added on 30/05/2020
            View editMatchNoteView = View.inflate(this, R.layout.edit_match_note, null);
//            TextView tv_innings = (TextView) editMatchNoteView.findViewById(R.id.tv_edit_innings);
//            TextView tv_over = (TextView) editMatchNoteView.findViewById(R.id.tv_edit_over);
            EditText et_edit_note = (EditText) editMatchNoteView.findViewById(R.id.et_edit_note);

//            tv_innings.setText(String.valueOf(matchNotes.getInnings()));
//            tv_over.setText(String.valueOf(
//                    new DecimalFormat("##.#").format(matchNotes.getOver())));
            et_edit_note.setText(matchNotes.getNote());

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setIcon(R.drawable.ball).
                    setTitle("Edit match note").
                    setView(editMatchNoteView).
                    setCancelable(false).
                    setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            Log.d("editmatchNotes", "editMatchNote, et_edit_note.getText().toString() : "
                                    + et_edit_note.getText().toString());

                            Log.d("editmatchNotes", "editMatchNote, et_edit_note.getText() : "
                                    + et_edit_note.getText());

                            if (!(et_edit_note.getText().toString().matches(""))) {
                                final String edited_note = et_edit_note.getText().toString();
                                saveToDB(false, true, matchNotes, edited_note);
                                Toast.makeText(MatchNoteListActivity.this, "Match note edited",
                                        Toast.LENGTH_SHORT).show();
                            }

                            else
                                displayError("Please enter valid input");
                        }
                    }).
                    setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    }).
                    show();
        }

        else
            Log.d("matchnote", "editMatchNote, matchNote 2  : " + matchNotes);
    }


    private final View.OnClickListener deleteListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {

             config = new RealmConfiguration.Builder()
                .name(AppConstants.GAME_ID + ".realm")
                .deleteRealmIfMigrationNeeded()
                .build();
        realm = Realm.getInstance(config);

            Log.d("matchnote", "delete");
            Log.d("matchnote", "delete, matchid : " + matchid);
            Log.d("matchnote", "delete, innings : " + innings);
            Log.d("matchnote", "delete, v.getId() : " + v.getId());

           /* Power power = realm.where(Power.class).
                    equalTo("matchid", matchid).
                    equalTo("innings", innings).
                    equalTo("id", v.getId()).findFirst();*/

            MatchNotes matchNotes = realm.where(MatchNotes.class).
                    equalTo("matchid", matchid).
                    equalTo("note_id", v.getId()).findFirst();

            AlertDialog alertDialog = new AlertDialog.Builder(MatchNoteListActivity.this).create();
            alertDialog.setIcon(R.drawable.ball);
            alertDialog.setCancelable(false);
            alertDialog.setTitle("Do you want to delete the note \"" + matchNotes.getNote() + "\"");
            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Confirm",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();

                            Log.e("matchnote", "selected, matchNotes : " + matchNotes);

                            if (matchNotes != null) {

                                int sequ = matchNotes.getSequence();

                                deleteMatchNote(matchNotes);
//                                saveEvent(v.getId(), false, false, true, start, end, sequ);

                            }

                            refreshTable();

                            displayNotesList();
                            Toast.makeText(MatchNoteListActivity.this, "Match note deleted", Toast.LENGTH_SHORT).show();

                        }
                    });
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Cancel",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            alertDialog.show();
        }
    };



    void deleteMatchNote(MatchNotes matchNotes) {

        if (matchNotes.getSync() == 1)
            updateDelete(matchNotes);
        else {
            if (matchNotes != null) {

                if (!realm.isInTransaction()) {
                    realm.beginTransaction();
                }

                matchNotes.deleteFromRealm();
                realm.commitTransaction();
            }
        }

        int sequence = matchNotes.getSequence();

        RealmResults<MatchNotes> results = realm.where(MatchNotes.class).
                equalTo("matchid", matchid).
                sort("sequence", Sort.ASCENDING).findAll();

        if (results.size() > 0) {

            for (MatchNotes notes : results) {

                if (sequence < notes.getSequence()){
                    int i = notes.getSequence() - 1;
                    updateMatchNote(notes, i);
                }
            }
        }
    }


    public void updateMatchNote(MatchNotes matchNotes, int sequence) {

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

                        Log.d("deletematchnote", "updateMatchNote 1 , sequence : " + sequence + ", matchNotes : " + matchNotes);
                        matchNotes.setSequence(sequence);

                        bgRealm.copyToRealm(matchNotes);
                        Log.d("deletematchnote", "updateMatchNote 2 , sequence : " + sequence + ", matchNotes : " + matchNotes);

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


    public void refreshTable() {

        slno = 0;

        int row_count = realm.where(MatchNotes.class).
                equalTo("matchid", matchid).
                findAll().size();

        Log.d("power", "refreshTable, count : " + row_count);

        if (row_count > 0) {

            while (note_table.getChildCount() > 1) {
                TableRow tableRow = (TableRow) note_table.getChildAt(1);
                note_table.removeView(tableRow);
            }
        }
    }


    // added on on 20/10/2020
    private boolean isNetworkAvailable(){
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


    public void postMatchNote() {

//        splitting match, new note, and edit note separately
        Match match1 = realm.where(Match.class).
                equalTo("matchid", matchid).findFirst();
        if (match1 != null) {
            if (match1.getMatchSync() == 0)
                postMatch();

            RealmResults<MatchOfficials> result_officials =
                    realm.where(MatchOfficials.class).
                            equalTo("matchid", matchid).
                            equalTo("d4s_id", 0).
                            equalTo("sync", 0).findAll();
            if (result_officials.size() > 0)
                postAddOfficials();

            RealmResults<MatchNotes> notes_result = realm.where(MatchNotes.class).
                    equalTo("matchid", matchid).
                    equalTo("add", true).
                    equalTo("sync", 0).
                    findAll();

            if (notes_result.size() > 0)
                postNewNote();

            RealmResults<MatchNotes> editnotes_result = realm.where(MatchNotes.class).
                    equalTo("matchid", matchid).
                    equalTo("edit", true).
                    equalTo("sync", 0).
                    findAll();

            if (editnotes_result.size() > 0)
                postEditNote();

        }
    }   // till here



    private void postDeleteMatchNote(int sequence) {

        postMatchNote();

        if (isNetworkAvailable()) {

            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("matchID", matchID);
                jsonObject.put("sequence", sequence);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            JSONObject jsonfeed = new JSONObject();
            try {
                jsonfeed.put("DeleteMatchNote", jsonObject);
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
                    Constants.API_URL + "deletematchnote", postparams,
                    new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {

                    Log.d("MATCH_NOTE", "Scoring, response : " + response);
                    try {
                        if ((response.getInt("status") == 200) && !(response.getBoolean("error"))) {
                            deleteNote(response.getString("match"), response.getInt("sequence"));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

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
        }
    }


    // Added on 16/12/2021
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

                                                } catch (RealmPrimaryKeyConstraintException e) {

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



    private void postNewNote() {
        if (isNetworkAvailable()) {

            RealmResults<MatchNotes> notes_result = realm.where(MatchNotes.class).
                    equalTo("matchid", matchid).
                    equalTo("add", true).
                    equalTo("sync", 0).
                    findAll();

            if (notes_result.size() > 0) {

                JSONArray array_notes = new JSONArray();

                for (MatchNotes matchNotes : notes_result) {

                    JSONObject json_notes = new JSONObject();

                    try {
                        json_notes.put("sequence", matchNotes.getSequence());
                        json_notes.put("innings", matchNotes.getInnings());
                        json_notes.put("over", (new DecimalFormat("##.#").format(matchNotes.getOver())));

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
                        Constants.API_URL + "addmatchnote", postparams,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {

                                Log.d("MATCH_NOTE", "Scoring, response : " + response);
                                // need matchID and sequence
                                try {
                                    if ((response.getInt("status") == 200) && !(response.getBoolean("error"))) {

                                        String id = response.getString("match");
                                        JSONArray array = response.getJSONArray("sequence");
                                        for (int i = 0; i < array.length(); i++) {
                                            JSONObject object = array.getJSONObject(i);
                                            syncMatchNote(id, object.getInt("sequence"));
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
                                Log.e("MATCH_NOTE", "Scoring, Error Message is  : " + error.getMessage());
                            }
                        });

                MyApplicationClass.getInstance(getApplicationContext()).addToRequestQueue(jsonObjReq, "postRequest");
                Log.d("MATCH_NOTE", "Scoring, jsonObjReq  : " + jsonObjReq);
                Log.d("MATCH_NOTE", "Scoring, postparams  : " + postparams);
            }
        }
    }


    // Added on 17/12/2021
    private void syncMatchNote (String matchID, int sequence) {

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

                        MatchNotes notes = bgrealm.where(MatchNotes.class).
                                equalTo("matchID", matchID).
                                equalTo("sequence", sequence).
                                findFirst();
                        if (notes != null) {
                            notes.setSync(1);
                            bgrealm.copyToRealmOrUpdate(notes);
                            Log.d("MatchNote", " notes = " + notes);
                        }
                    }
                    catch (RealmPrimaryKeyConstraintException e) {
                        Log.d("MatchNote", " Exception : "+e);
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



    private void postEditNote() {
        if (isNetworkAvailable()) {

            RealmResults<MatchNotes> notes_result = realm.where(MatchNotes.class).
                    equalTo("matchid", matchid).
                    equalTo("add", true).
                    equalTo("sync", 0).
                    findAll();

            if (notes_result.size() > 0) {

                JSONArray array_notes = new JSONArray();

                for (MatchNotes matchNotes : notes_result) {

                    JSONObject json_notes = new JSONObject();

                    try {
                        json_notes.put("sequence", matchNotes.getSequence());
                        json_notes.put("innings", matchNotes.getInnings());
                        json_notes.put("over", (new DecimalFormat("##.#").format(matchNotes.getOver())));

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
                    jsonfeed.put("EditMatchNote", jsonObject);
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
                        Constants.API_URL + "editmatchnote", postparams,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {

                                Log.d("MATCH_NOTE", "Scoring, response : " + response);
                                // need matchID and sequence
                                try {
                                    if ((response.getInt("status") == 200) && !(response.getBoolean("error"))) {

                                        String id = response.getString("match");
                                        JSONArray array = response.getJSONArray("sequence");
                                        for (int i = 0; i < array.length(); i++) {
                                            JSONObject object = array.getJSONObject(i);
                                            syncMatchNote(id, object.getInt("sequence"));
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
                                Log.e("MATCH_NOTE", "Scoring, Error Message is  : " + error.getMessage());
                            }
                        });

                MyApplicationClass.getInstance(getApplicationContext()).addToRequestQueue(jsonObjReq, "postRequest");
                Log.d("MATCH_NOTE", "Scoring, jsonObjReq  : " + jsonObjReq);
                Log.d("MATCH_NOTE", "Scoring, postparams  : " + postparams);
            }
        }
    }


    private void updateDelete(MatchNotes matchNotes) {

        if (matchNotes != null) {
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

                            matchNotes.setDelete(true);
                            bgRealm.copyToRealm(matchNotes);
                            Log.d("Note", "Delete matchNotes = " + matchNotes);

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

        postDeleteMatchNote(matchNotes.getSequence());  // Added on 15/12/2021
    }


    private void deleteNote(String matchID, int sequence) {

        MatchNotes notes = realm.where(MatchNotes.class)
                .equalTo("matchID", matchID)
                .equalTo("sequence", sequence)
                .equalTo("delete", true)
                .findFirst();
        if (notes != null) {

            if (!realm.isInTransaction()) {
                realm.beginTransaction();
            }

            notes.deleteFromRealm();
            realm.commitTransaction();
        }
    }
}