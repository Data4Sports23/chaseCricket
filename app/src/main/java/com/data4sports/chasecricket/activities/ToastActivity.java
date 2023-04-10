package com.data4sports.chasecricket.activities;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.data4sports.chasecricket.R;
import com.data4sports.chasecricket.applicationConstants.AppConstants;
import com.data4sports.chasecricket.models.Events;
import com.data4sports.chasecricket.models.SharedPreferenceClass;

import java.util.Timer;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;
import io.realm.exceptions.RealmException;
import io.realm.exceptions.RealmFileException;

public class ToastActivity extends AppCompatActivity {

    Realm realm;
    RealmConfiguration config;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_toast);
        Log.d("TAG", "onCreate: App User id " + AppConstants.GAME_ID);

        Timer t = new java.util.Timer();
        t.schedule(
                new java.util.TimerTask() {
                    @Override
                    public void run() {
                        // your code here
                        // close the thread
                        Log.d("TAG", "onCreate: App User id After 10 s" + AppConstants.GAME_ID);
                        databaseVCall();
                        t.cancel();
                    }
                },
                10000
        );

//        Handler handler = new Handler();
//       // Intent i = new Intent(this, UpdatedScoringActivity.class);
//
//        handler.postDelayed(new Runnable() {
//            public void run() {
//
//
//                finish();
//            }
//        }, 10000);
        //startActivity(i);
    }

    private void databaseVCall(){

        try {
//            finish();
            //recreate();
            //startActivity(getIntent());
            Realm.init(this);
            config = new RealmConfiguration.Builder()
                    .name(AppConstants.GAME_ID + ".realm")
                    .deleteRealmIfMigrationNeeded()
                    .build();
            realm = Realm.getInstance(config);
            Events events = realm.where(Events.class).
                    equalTo("matchid", 1).findFirst();
            Log.d("TAG", "databaseVCall: : " + events);
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    try {
                        Events events = realm.where(Events.class).
                                equalTo("matchid", 3).findFirst();
                        Log.d("TAG", "databaseVCall: : " + events);
//                        SharedPreferenceClass matchValue = realm.where(SharedPreferenceClass.class)
//                                .findFirst();
//                        Log.d("TAG", "getSharedPreferenceValue: " + matchValue);
//                        Log.d("TAG", "getSharedPreferenceValue: " + matchValue);
                    }catch (RealmException e ){
                        Log.d("TAG", "execute: Exception " + e);
                    }

                }
            });
        }catch (RealmException e){
            Log.d("TAG", "onCreate: Exception " + e);
        }finally {
            if (realm != null) {
                realm.close();
            }
        }


    }

}