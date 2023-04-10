                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                        package com.data4sports.chasecricket.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.data4sports.chasecricket.R;
import com.data4sports.chasecricket.models.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created on 30/09/2020
 */
public class MidScoringActivity extends AppCompatActivity {

    ImageButton back;
    EditText et_gameid;
    Button btn_submit;

    SharedPreferences mPreferences;
    SharedPreferences.Editor mEditor;
    private ProgressDialog progress;

    int gameID = 0, userId, d4s_userid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mid_scoring);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.custom_action_bar_layout);
        View view =getSupportActionBar().getCustomView();

        back = (ImageButton) view.findViewById(R.id.action_bar_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*finish();*/
                Log.e("MidScoring", "onCreate, back button pressed");
                onBackPressed();
            }
        });

        mPreferences = PreferenceManager
                .getDefaultSharedPreferences(this);
        userId = mPreferences.getInt("sp_user_id", 0);
        d4s_userid = mPreferences.getInt("d4s_userid", 0);
        Log.d("MidScoring", "onCreate, userId " + userId);
        Log.d("MidScoring", "onCreate, d4s_userid" + d4s_userid);

        et_gameid = findViewById(R.id.et_ms_gameid);
        btn_submit = findViewById(R.id.btn_ms_submit);

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                displayProgress();

                Log.d("MidScoring", "gameID, " + et_gameid.getText().toString());
                if (et_gameid.getText().toString().matches("")) {
                    progress.dismiss();
                    displayError("Please enter a valid Game ID");
                }
                else {
                    gameID = Integer.parseInt(et_gameid.getText().toString());

                    Log.d("MidScoring", "gameID, " + gameID);
                    if (gameID == 0) {
                        progress.dismiss();
                        displayError("Please enter a valid Game ID");
                    }
                    else {
                        if (isNetworkAvailable()) {
                            get();
                        }

                        else {
                            progress.dismiss();
                            displayError("Please check your network connection");
                        }
                    }
                }

            }
        });
    }


    private void get() {

        /* JsonArrayRequest getRequest = new JsonArrayRequest(Request.Method.GET,
                                                               Constants.CHASE_CRICKET_MID_SCORING, null,
                                                               new Response.Listener<JSONArray>()
                                                               {
                                                                   @Override
                                                                   public void onResponse(JSONArray response) {
                                                                       // display response
                                                                       Log.d("Response", response.toString());
                                                                   }
                                                                },
                                                                new Response.ErrorListener()
                                                                {
                                                                    @Override
                                                                    public void onErrorResponse(VolleyError error) {
                                                                        Log.d("Error.Response", error.toString());
                                                                    }
                                                                }
                                                        );*/
                                                
        JSONObject postparams = new JSONObject();
        try {
            postparams.put("matchid", gameID);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET,
                Constants.CHASE_CRICKET_MID_SCORING + "?matchid=" + gameID + "&userid=" + userId, null,
                new Response.Listener<JSONObject>()
                {
                    @Override
                    public void onResponse(JSONObject response) {
                        // display response
                        Log.d("MidScoring", "Response : " + response);
                        progress.dismiss();
                    }
                    },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Error.Response", error.toString());
                        progress.dismiss();
                    }
                }/*,

                                                               {
                                                                    @Override
                                                        protected Map<String,String> getParams(){
                                                                Map<String,String> params = new HashMap<String, String>();
                                                                params.put("matchid",String.valueOf(gameID));
                                                                return params;
                                                            }*/
                                                
        );
        Volley.newRequestQueue(this).add(getRequest);
                                                
                                                
                                                //        RequestQueue queue = Volley.newRequestQueue(this);
                                                
                                                        /*StringRequest sr = new StringRequest(Request.Method.GET,
                                                                Constants.CHASE_CRICKET_MID_SCORING,
                                                                new Response.Listener<String>() {
                                                                    @Override
                                                                    public void onResponse(String response) {
                                                                        Log.e("MidScoring", "success! response: " + response.toString());
                                                                        progress.dismiss();
                                                                    }
                                                                },
                                                                new Response.ErrorListener() {
                                                                    @Override
                                                                    public void onErrorResponse(VolleyError error) {
                                                                        Log.e("MidScoring", "error: " + error.toString());
                                                                        progress.dismiss();
                                                                    }
                                                                })
                                                        {
                                                            @Override
                                                            protected Map<String,String> getParams(){
                                                                Map<String,String> params = new HashMap<String, String>();
                                                                params.put("matchid",String.valueOf(gameID));
                                                                return params;
                                                            }
                                                            @Override
                                                            public Map<String, String> getHeaders() throws AuthFailureError {
                                                                Map<String,String> params = new HashMap<String, String>();
                                                                params.put("Content-Type","application/x-www-form-urlencoded");
                                                                return params;
                                                            }
                                                        };
                                                
                                                        Volley.newRequestQueue(this).add(sr);
                                                */
                                                
                                                
                                                        /*StringRequest getRequest = new StringRequest(Request.Method.GET,
                                                                Constants.CHASE_CRICKET_MID_SCORING + "/" + gameID,
                                                                new Response.Listener<String>() {
                                                                    @Override
                                                                    public void onResponse(String response) {
                                                                        Log.d("MidScoring", "post, response" + response);
                                                                       *//* //hiding the progressbar after completion
                                                //                            progressBar.setVisibility(View.INVISIBLE);
                                                
                                                                        try {
                                                
                                                                            //getting the whole json object from the response
                                                //                                JSONObject obj = new JSONObject(response);
                                                
                                                                            //we have the array named hero inside the object
                                                                            //so here we are getting that json array
                                                                            JSONArray matchArray = new JSONArray(response);
                                                
                                                                            if (matchArray != null) {
                                                
                                                                                //now looping through all the elements of the json array
                                                                                for (int i = 0; i < matchArray.length(); i++) {
                                                                                    //getting the json object of the particular index inside the array
                                                                                    JSONObject matchObject = matchArray.getJSONObject(i);
                                                
                                                                                    dataTeams.add(matchObject.getString("teamA") + " vs " +
                                                                                            matchObject.getString("teamB") + ", " +
                                                                                            matchObject.getString("match_type") + " on " +
                                                                                            matchObject.getString("date"));
                                                                                }
                                                
                                                                                progress.dismiss();
                                                //                                    progressBar.setVisibility(View.INVISIBLE);
                                                                                Intent i = new Intent(HomeActivity.this, AssignedMatchListActivity.class);
                                                                                i.putExtra("mylist", dataTeams);
                                                                                startActivity(i);
                                                
                                                                            }
                                                
                                                                            else {
                                                
                                                                                progress.dismiss();
                                                //                                    progressBar.setVisibility(View.INVISIBLE);
                                                                                Toast.makeText(getApplicationContext(), "No matches are assigned for you",
                                                                                        Toast.LENGTH_LONG).show();
                                                                            }
                                                
                                                                        } catch (JSONException e) {
                                                                            e.printStackTrace();
                                                                        }*//*
                                                                    }
                                                                },
                                                                new Response.ErrorListener() {
                                                                    @Override
                                                                    public void onErrorResponse(VolleyError error) {
                                                
                                                                        progress.dismiss();
                                                //                            progressBar.setVisibility(View.INVISIBLE);
                                                                        //displaying the error in toast if occurs
                                                                        Toast.makeText(getApplicationContext(),
                                                                                "Server error, please try again later", Toast.LENGTH_SHORT).show();
                                                                    }
                                                                })
                                                
                                                        {
                                                            @Override
                                                            protected Map<String, String> getParams()
                                                            {
                                                                Map<String, String> params = new HashMap<String, String>();
                                                                params.put("matchid", String.valueOf(gameID));
                                                                return params;
                                                            }
                                                        };
                                                
                                                        //creating a request queue
                                                        RequestQueue requestQueue = Volley.newRequestQueue(this);
                                                        //adding the string request to request queue
                                                        requestQueue.add(getRequest);*/
                                                
                                                
    }
                                                

    private boolean isNetworkAvailable(){
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
                                                
                                                
                                                
    public void displayError(String message){

        AlertDialog.Builder tempBuilder = new AlertDialog.Builder(this);
        tempBuilder.setIcon(R.drawable.ball);
        tempBuilder.setCancelable(false);       // for disabling the parent activity while displaying Alertbox (false)
        tempBuilder.setTitle(message);
        tempBuilder.setNeutralButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                progress.dismiss();
            }
        });

        tempBuilder.show();
    }

                                                
                                                
    public void displayProgress(){

        progress = new ProgressDialog(this);
        progress.setMessage("Please wait...");
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setIndeterminate(true);
        progress.setProgress(0);
        progress.show();
    }

}