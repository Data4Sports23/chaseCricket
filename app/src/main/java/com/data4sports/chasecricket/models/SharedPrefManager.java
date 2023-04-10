package com.data4sports.chasecricket.models;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import com.data4sports.chasecricket.activities.LoginActivity;

public class SharedPrefManager {

    private static final String SHARED_PREF_NAME = "volleyregisterlogin";
    private static final String KEY_USERNAME = "keyusername";
    private static final String KEY_EMAIL = "keyemail";
    private static final String KEY_PHONE = "keyphone";
    private static final String KEY_ADDRESS = "keyaddress";
    private static final String KEY_TOKEN = "keytoken";
    private static final String KEY_ID = "keyid";
    private static final String KEY_UID = "keyuid";
    private static SharedPrefManager mInstance;
    private static Context ctx;

    private SharedPrefManager(Context context) {
        ctx = context;
    }

    public static synchronized SharedPrefManager getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new SharedPrefManager(context);
        }
        return mInstance;
    }


    //this method will store the user data in shared preferences
    public void userLogin(User user) {
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
//        editor.putString(KEY_TOKEN, user.getToken());
        editor.putInt(KEY_ID, user.getUserId());
        editor.putInt(KEY_UID, user.getD4s_userid());
        editor.putString(KEY_USERNAME, user.getUsername());
        editor.putString(KEY_EMAIL, user.getEmail());
        editor.putString(KEY_PHONE, user.getPhonenumber());
        editor.putString(KEY_ADDRESS, user.getAddress());
        sharedPreferences.getString(KEY_TOKEN, null);
        editor.apply();
    }




    //this method will checker whether user is already logged in or not
    public boolean isLoggedIn() {

        SharedPreferences sharedPreferences = ctx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        Log.d("SPManager", "email :"+sharedPreferences.getString(KEY_EMAIL, null));
        return sharedPreferences.getString(KEY_EMAIL, null) != null;
    }


    public int getD4SUserID() {

        SharedPreferences sharedPreferences = ctx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        Log.d("SPManager", "d4s_userid :"+sharedPreferences.getInt(KEY_UID, 0));
        return sharedPreferences.getInt(KEY_UID, 0) ;

    }

    public int getUserID() {

        SharedPreferences sharedPreferences = ctx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        Log.d("SPManager", "userID :"+sharedPreferences.getInt(KEY_ID, 0));
        return sharedPreferences.getInt(KEY_ID, 0) ;

    }



    //this method will give the logged in user
    public User getUser() {

        SharedPreferences sharedPreferences = ctx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return new User(

                sharedPreferences.getString(KEY_USERNAME, null),
                sharedPreferences.getString(KEY_EMAIL, null),
                sharedPreferences.getString(KEY_PHONE, null),
                sharedPreferences.getString(KEY_ADDRESS, null),
                sharedPreferences.getInt(KEY_ID, 0),
                sharedPreferences.getInt(KEY_UID, 0)

        );
    }




    //this method will logout the user
    public void logout() {
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
        ctx.startActivity(new Intent(ctx, LoginActivity.class));

    }


}
