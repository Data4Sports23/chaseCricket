package com.data4sports.chasecricket.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.data4sports.chasecricket.R;
import com.data4sports.chasecricket.apiClients.ProfileDetailApiController;
import com.data4sports.chasecricket.applicationConstants.AppConstants;
import com.data4sports.chasecricket.models.Constants;
import com.data4sports.chasecricket.models.Match;
import com.data4sports.chasecricket.models.ProfileDetailsModel;
import com.data4sports.chasecricket.models.SharedPrefManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;

public class HomeActivity extends BaseAppCompatActivity {

    Realm realm;
    int userId = 0, d4s_userid = 0;

    Button btn_newMatch, btn_listMatches, /*btn_userprofile*/
            btn_midScoring, btn_assignedMatchList;
    TextView userName;
    //    ProgressBar progressBar;
    private ProgressDialog progress;

    ArrayList<String> dataTeams = new ArrayList<String>();

    // Deepak Code starts
    ArrayList<Integer> midScoreValue = new ArrayList<Integer>();
    ArrayList<Integer> gameIdArray = new ArrayList<Integer>();
    //Deepak Code ends

    int flag = 0;
    String token;

    SharedPreferences sharedPreferences;

    private ProfileDetailsModel.Response response;

    RealmConfiguration config;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_home);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        btn_newMatch = findViewById(R.id.btn_new_match);
        btn_listMatches = findViewById(R.id.btn_list_of_matches);
        userName = findViewById(R.id.userName);
//        btn_midScoring = findViewById(R.id.btn_midScoring); COMMENTED ON 26/02/2021
//        btn_userprofile = findViewById(R.id.btn_profile);
        btn_assignedMatchList = findViewById(R.id.btn_assigned_match_list);
//        btn_userprofile.setVisibility(View.GONE);
//        btn_userprofile.setVisibility(View.GONE);

//        progressBar = findViewById(R.id.progressBar);

//         config = new RealmConfiguration.Builder()
//                .name(AppConstants.GAME_ID + ".realm")
//                .deleteRealmIfMigrationNeeded()
//                .build();
//        realm = Realm.getInstance(config);
        realm = Realm.getDefaultInstance();

//        Intent i = getIntent();
//        userId = i.getIntExtra("userId", 0);
//        d4s_userid = i.getIntExtra("d4s_userid", 0);
//        token = i.getStringExtra("token");


        token = sharedPreferences.getString("user_token", null);
        userId = sharedPreferences.getInt("sp_user_id", 0);
        d4s_userid = sharedPreferences.getInt("d4s_userid", 0);

        /*Intent i = getIntent();
        userId = i.getIntExtra("userId", 0);
*/
        Log.d("home", "userid : " + userId);
        Log.d("home", "d4s_userid : " + d4s_userid);
        Log.d("home", "token : " + token);


        btn_newMatch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /*String[] wonby = {"An Innings", "Runs", "Wickets"};
                String[] temp1 = wonby;
                String[] temp2 = wonby.clone();
                for (int i = 0; i < wonby.length; i++) {
                Log.d("ARRAY", "wonby = " + wonby[i]);
                Log.d("ARRAY", "temp1 = " + temp1[i]);
                Log.d("ARRAY", "temp2 = " + temp2[i]);
                }*/

                displayProgress();
                Intent intent = new Intent(HomeActivity.this, CreateMatchActivity.class);
                intent.putExtra("userId", userId);
                progress.dismiss();
                startActivity(intent);

            }
        });


        btn_listMatches.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayProgress();
//                progressBar.setVisibility(View.VISIBLE);
                Log.d(TAG, "onClick: User id " + userId);
                RealmResults<Match> result = realm.where(Match.class).
                        equalTo("userId", userId).findAll();
//                result.load();
                if (result.size() == 0) {
                    progress.dismiss();
                    Toast.makeText(getApplicationContext(), "There is no saved matches", Toast.LENGTH_LONG).show();
//                    progressBar.setVisibility(View.INVISIBLE);

                } else /*if (result.size() > 0)*/ {
//                    Toast.makeText(getApplicationContext(), "The saved matches, result == "+result.size(), Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(HomeActivity.this, MatchListActivity.class));
//                    progressBar.setVisibility(View.INVISIBLE);
                    progress.dismiss();
                    finish();
                }

//                Toast.makeText(getApplicationContext(), "Temp", Toast.LENGTH_SHORT).show();
            }
        });


        /*btn_userprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayProgress();
//                progressBar.setVisibility(View.VISIBLE);
                startActivity(new Intent(HomeActivity.this, SucessActivity.class));
            }
        });*/

        /* COMMENTED ON 26/02/2021
        btn_midScoring.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                displayProgress();
                startActivity(new Intent(HomeActivity.this, MidScoringActivity.class));
            }
        });*/


        btn_assignedMatchList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                displayProgress();
//                progressBar.setVisibility(View.VISIBLE);
/*//                loadMatchList();      // uncomment this
                displayAlert();        // using only because there are no api*/

                loadMatchList();

            }
        });
        getProfileData();
    }


    private void getProfileData() {

        new ProfileDetailApiController()
                .setContext(HomeActivity.this)
                .setHeader(getHeaderMapWithID())
                .setProfileCallback(new ProfileDetailsModel.ProfileCallback() {
                    @Override
                    public void onSuccess(boolean result,
                                          ProfileDetailsModel.Response resultBean) {
                        //dismiss
                        DisplayProfileDetails(resultBean);
                        response = resultBean;
                    }

                    @Override
                    public void onFail(String message) {
                        //dismiss

                    }
                }).getProfileDetailsCall(d4s_userid);

    }

    private void DisplayProfileDetails(ProfileDetailsModel.Response responses) {

        userName.setText(responses.getName());
    }

    private void loadMatchList() {

        dataTeams.clear();
        midScoreValue.clear();
        gameIdArray.clear();

        Log.d("Home", "loadMatchList");

        if (isNetworkAvailable()) {

            Log.d("Home", "network available");

//            progressBar.setVisibility(View.VISIBLE);
//            String JSON_URL = "http://data4basketball.com/appapi/getmatches";
            StringRequest stringRequest = new StringRequest(Request.Method.GET,
                    Constants.CHASE_CRICKET_PULL_MATCH_API_TEST + d4s_userid,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            //hiding the progressbar after completion
//                            progressBar.setVisibility(View.INVISIBLE);
                            Log.d("Home", "response = " + response);

                            try {
                                progress.dismiss();

                                //getting the whole json object from the response
//                                JSONObject obj = new JSONObject(response);

                                //we have the array named hero inside the object
                                //so here we are getting that json array
                                JSONArray matchArray = new JSONArray(response);

                                if (matchArray.length() > 0) {// != null) {

                                    //now looping through all the elements of the json array
                                    for (int i = 0; i < matchArray.length(); i++) {
                                        Log.d("Home", "i = " + i);

                                        //getting the json object of the particular index inside the array
                                        JSONObject matchObject = matchArray.getJSONObject(i);
                                        Log.d("Home", "matchObject = " + matchObject.toString());

                                        dataTeams.add(matchObject.getString("teamA") + " vs " +
                                                matchObject.getString("teamB") + ", \n" +
                                                matchObject.getString("match_type") + " on " +
                                                matchObject.getString("date"));

                                        //Deepak Code Starts
                                        midScoreValue.add( matchObject.getInt("mid_scoring"));
                                        gameIdArray.add(matchObject.getInt("gameid"));
                                        //Deepak Code ends

                                    }

//                                    progress.dismiss();
//                                    progressBar.setVisibility(View.INVISIBLE);
                                    Intent i = new Intent(HomeActivity.this, AssignedMatchListActivity.class);
                                    i.putExtra("mylist", dataTeams);
                                    //Deepak code starts
                                    i.putExtra("midScore", midScoreValue);
                                    i.putExtra("game_id",gameIdArray);
                                    //Deepak code ends
                                    startActivity(i);

                                } else {

//                                    progress.dismiss();
//                                    progressBar.setVisibility(View.INVISIBLE);
                                    Toast.makeText(getApplicationContext(), "No matches are assigned for you",
                                            Toast.LENGTH_LONG).show();
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
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
                    });

            //creating a request queue
            RequestQueue requestQueue = Volley.newRequestQueue(this);

            //adding the string request to request queue
            requestQueue.add(stringRequest);
            Log.d("Home", "stringRequest = " + stringRequest);
            Log.d("Home", "requestQueue = " + requestQueue);

        } else {

            progress.dismiss();
//            progressBar.setVisibility(View.INVISIBLE);

            AlertDialog alertDialog = new AlertDialog.Builder(HomeActivity.this).create();
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


    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        Log.d("Home", "connectivityManager = " + connectivityManager);
        Log.d("Home", "activeNetworkInfo = " + activeNetworkInfo);
        Log.d("Home", "activeNetworkInfo.isConnected = " + activeNetworkInfo.isConnected());
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }


    public void displayAlert() {

        AlertDialog.Builder tempBuilder = new AlertDialog.Builder(this);
        tempBuilder.setIcon(R.drawable.ball);
        tempBuilder.setCancelable(false);       // for disabling the parent activity while displaying Alertbox (false)
        tempBuilder.setTitle("No URL is assigned");
        tempBuilder.setNeutralButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
//                progressBar.setVisibility(View.GONE);
                progress.dismiss();
            }
        });

        tempBuilder.show();
    }


    @Override
    public void onBackPressed() {

        flag++;
        if (flag == 1) {

            Toast.makeText(getApplicationContext(), "Press again to exit", Toast.LENGTH_SHORT).show();
        } else if (flag >= 2)
            finish();


    }


    public void displayProgress() {

        progress = new ProgressDialog(this);
        progress.setMessage("Please wait...");
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setIndeterminate(true);
        progress.setProgress(0);
        progress.show();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.signout, menu);//Menu Resource, Menu
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.signout) {
            signOut();
            return true;
        }
        return super.onOptionsItemSelected(item);

    }


    private void signOut() {

        AlertDialog alertDialog = new AlertDialog.Builder(HomeActivity.this).create();
        alertDialog.setIcon(R.drawable.ball);
        alertDialog.setCancelable(false);
        alertDialog.setTitle("Do you want to sign out from this account");
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Confirm",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        SharedPrefManager.getInstance(HomeActivity.this).logout();
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

}
