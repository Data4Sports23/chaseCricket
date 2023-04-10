package com.data4sports.chasecricket.models;

import android.app.Application;
import android.content.Context;
import android.support.v7.app.AppCompatDelegate;

import com.data4sports.chasecricket.apiClients.RetrofitService;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class MyApplication extends Application {

    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }
    //retrofit object
    private RetrofitService mRetrofitService;

    @Override
    public void onCreate() {
        super.onCreate();
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        Realm.init(getApplicationContext());


        RealmConfiguration config =
                new RealmConfiguration.Builder()
                        .deleteRealmIfMigrationNeeded()
                        .build();

        Realm.setDefaultConfiguration(config);
    }

    /**
     * Context
     *
     * @param context context
     * @return getApplicationContext
     */
    public static MyApplication get(Context context) {
        return (MyApplication) context.getApplicationContext();
    }
    /**
     * return Retrofit object normal api call
     *
     * @return RetrofitService
     */
    public RetrofitService getRetrofitService() {
        if (mRetrofitService == null) {
            mRetrofitService = RetrofitService.Factory.create();
        }
        return mRetrofitService;
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }
}