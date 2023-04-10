package com.data4sports.chasecricket.models;

import android.app.Application;
import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

public class MyApplicationClass extends Application {

    private RequestQueue requestQueue;
    private static MyApplicationClass mInstance;
    private static Context mCtx;

    private MyApplicationClass(Context context) {
        mCtx = context;
        requestQueue = getRequestQueue();
    }


    public static synchronized MyApplicationClass getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new MyApplicationClass(context);
        }
        return mInstance;
    }

    public RequestQueue getRequestQueue() {
        if (requestQueue == null) {
            // getApplicationContext() is key, it keeps you from leaking the
            // Activity or BroadcastReceiver if someone passes one in.
            requestQueue = Volley.newRequestQueue(mCtx.getApplicationContext());
        }
        return requestQueue;
    }

//    public <T> void addToRequestQueue(Request<T> req) {
//        getRequestQueue().add(req);
//    }

//    public void onCreate() {
//        super.onCreate();
//        mInstance = this;
//    }
//
//    public static synchronized MyApplicationClass getInstance() {
//        return mInstance;
//    }

//    public RequestQueue getRequestQueue() {
//        Log.d("Test","inside RequestQueue method");
//        if (requestQueue == null) {
//            Log.d("Test", "RequestQueue is null");
//            requestQueue = Volley.newRequestQueue(getApplicationContext());
//        }
//        Log.d("Test", "RequestQueue is not null");
//        return requestQueue;
//    }
//
//
    public void addToRequestQueue(Request request, String tag) {
        request.setTag(tag);
        getRequestQueue().add(request);
    }

    /*public void addToRequestQueue(JsonRequest request, String tag) {
        request.setTag(tag);
        getRequestQueue().add(request);
    }

    public void addToRequestQueue(StringRequest request, String tag) {
        request.setTag(tag);
        getRequestQueue().add(request);
    }*/



    public void cancelAllRequests(String tag) {
        getRequestQueue().cancelAll(tag);
    }

}
