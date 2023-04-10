package com.data4sports.chasecricket.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.data4sports.chasecricket.R;
import com.data4sports.chasecricket.applicationConstants.AppConstants;
import com.data4sports.chasecricket.models.Match;
import com.data4sports.chasecricket.models.Player;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;
import io.realm.Sort;
import io.realm.exceptions.RealmException;
import io.realm.exceptions.RealmPrimaryKeyConstraintException;

/**
 * Created on 18/11/2021
 * for confirming players
 */
public class ConfirmBXI extends AppCompatActivity {

    ImageButton back;
    TextView tv_team;
    TableLayout table;

    int matchid;
    String matchID, teamB, confirm_head = " - Selected Players";
    ArrayList<Integer> selectedPID;

    Realm realm;
    SharedPreferences sharedPreferences;
    RealmConfiguration config;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.confirm_bxi);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.custom_action_bar_layout);
        View view =getSupportActionBar().getCustomView();

        back = (ImageButton) view.findViewById(R.id.action_bar_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        Realm.init(this);
        Realm.getDefaultConfiguration();
         config = new RealmConfiguration.Builder()
                .name(AppConstants.GAME_ID + ".realm")
                .deleteRealmIfMigrationNeeded()
                .build();
        realm = Realm.getInstance(config);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        getFromSP();

        tv_team = findViewById(R.id.cb_team);
        table = findViewById(R.id.cb_table);

        tv_team.setText(teamB + confirm_head);

        selectedPID =  (ArrayList<Integer>) getIntent().getSerializableExtra("mylist");
        displayPreviewList();
        Log.d("CAXI", "myList = " + selectedPID.toString());

        findViewById(R.id.cb_confirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirmed();
            }
        });
    }


    void displayPreviewList() {

        RealmResults<Player> results = realm.where(Player.class)
                .equalTo("matchid", matchid)
                .equalTo("team", 2)
                .sort("playerID", Sort.ASCENDING)
                .findAll();
        int i = 0;
        for (Player player : results) {
            Log.d("CBX", "player = " +player);
            if (selectedPID.contains(player.getPlayerID())) {

                ++i;

                TableRow row_c = new TableRow(this);
                row_c.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, 50));
                row_c.setPadding(10, 25, 10, 25);
                row_c.setOrientation(LinearLayout.VERTICAL);
                row_c.setShowDividers(LinearLayout.SHOW_DIVIDER_MIDDLE);
                row_c.setDividerPadding(3);

                TextView tv = new TextView(this);
                tv.setText(i + ".  ");
                tv.setTextColor(Color.BLACK);
                row_c.addView(tv);

                TextView tv1 = new TextView(this);
                tv1.setText(player.getPlayerName());
                tv1.setTextColor(Color.BLACK);
                row_c.addView(tv1);

                table.addView(row_c);
            }
        }
    }


    void getFromSP(){

        matchid = sharedPreferences.getInt("sp_match_id", 0);
        matchID = sharedPreferences.getString("sp_match_ID", null);
        teamB = sharedPreferences.getString("sp_teamB",null);
    }


    void confirmed() {

        RealmResults<Player> results = realm.where(Player.class)
                .equalTo("matchid", matchid)
                .equalTo("team", 2)
                .findAll();

        for (Player p : results) {
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

                        Player player = bgRealm.where(Player.class).
                                equalTo("matchID", matchID).
                                equalTo("playerID", p.getPlayerID()).findFirst();

                        if (player != null) {
                            if (selectedPID.contains(p.getPlayerID())) {
                                player.setSubstitute(false);
                                player.setPlaying(true);
                            } else {
                                player.setSubstitute(true);
                                player.setPlaying(false);
                            }
                            bgRealm.copyToRealm(player);
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

        startActivity(new Intent(ConfirmBXI.this, CaptainActivity.class));
        finish();
    }


    // Added on 17/12/2021
  /*  @Override
    protected void onPause() {

        super.onPause();

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
//                        match.setAddPlayers(false);
                        match.setSelectBXI(false);
                        match.setConfirmBXI(true);
//                        match.setOpeners(false);
//                        match.setScoring(false);
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
    }*/
}