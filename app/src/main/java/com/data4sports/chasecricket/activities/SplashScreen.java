package com.data4sports.chasecricket.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;

import com.data4sports.chasecricket.R;
import com.data4sports.chasecricket.applicationConstants.AppConstants;
import com.data4sports.chasecricket.models.SharedPrefManager;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class SplashScreen extends AppCompatActivity {

    Realm realm;

    int userId = 0, d4s_userid= 0;
    boolean loggedStatus = false;
    String token = null;

    SharedPreferences mPreferences;
    SharedPreferences.Editor mEditor;
    RealmConfiguration config;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);
        mPreferences = PreferenceManager
                .getDefaultSharedPreferences(this);

         config = new RealmConfiguration.Builder()
                .name(AppConstants.GAME_ID + ".realm")
                .deleteRealmIfMigrationNeeded()
                .build();
        realm = Realm.getInstance(config);
//        RealmResults<User> result = realm.where(User.class).findAll();
//        result.load();
//        if (result!=null) {
//            for (User user : result) {
//                Log.d("User", "id:" + user.getUserId() + ", name:" + user.getUsername() + ", email:" + user.getEmail());
//            }
//        }

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {

            @Override
            public void run() {

//                User user = realm.where(User.class).equalTo("loggedInStatus", true).findFirst();
//                if (user != null){
//                    Log.d("LoggedIn" , "user : "+user);
//                    loggedStatus = true;
//                    userId = user.getUserId();
//                }
//                else {
//                    Log.d("LoggedIn" , "user : "+user);
//
//                    loggedStatus = false;
//
//                }
//
//                if (loggedStatus){
//
//                    Intent i = new Intent(SplashScreen.this, HomeActivity.class);
//                    i.putExtra("userId", userId);
//                    finish();
//                    startActivity(i);
//                }
//                else {
//
//                    finish();
//                    startActivity(new Intent(SplashScreen.this, LoginActivity.class));
//                }

               //last commented

                if (!SharedPrefManager.getInstance(SplashScreen.this).isLoggedIn()) {
                    finish();
                    startActivity(new Intent(SplashScreen.this, LoginActivity.class));
                }
                else {

                    d4s_userid = SharedPrefManager.getInstance(SplashScreen.this).getD4SUserID();
                    userId = SharedPrefManager.getInstance(SplashScreen.this).getUserID();
//                    User user1 = SharedPrefManager.getInstance(SplashScreen.this).getUser();
                    /*User user = realm.where(User.class).equalTo("loggedInStatus", true).findFirst();
                    Log.d("splash", "SharedPrefManager.getInstance(SplashScreen.this).isLoggedIn() : " +
                            SharedPrefManager.getInstance(SplashScreen.this).isLoggedIn());
                    Log.d("splash", "" + user);

                    Log.d("splash 1", "" + user);
                    if (user != null) {
                        Log.d("splash 1", "" + user.getUserId());
                        Log.d("splash 1", "" + user.getD4s_userid());
                        Log.d("splash 1", "" + user.getEmail());
                        userId = user.getUserId();
                        token = user.getToken();
//                        d4s_userid = user.getD4s_userid();
                    }*/
                    mEditor = mPreferences.edit();
                    mEditor.putInt("sp_user_id", userId);
                    mEditor.putInt("d4s_userid", d4s_userid);
                    mEditor.putString("user_token", token);
                    mEditor.apply();

                    Intent i = new Intent(SplashScreen.this, HomeActivity.class);
//                    i.putExtra("sp_user_id", userId);
//                    Log.d("entrypage" , "userid : "+userId);
                    startActivity(i);
                }

//                startActivity(new Intent(SplashScreen.this, HomeActivity.class));


                SplashScreen.this.finish();
            }
        }, 1500);
    }
}
