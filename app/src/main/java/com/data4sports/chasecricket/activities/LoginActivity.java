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
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.data4sports.chasecricket.R;
import com.data4sports.chasecricket.applicationConstants.AppConstants;
import com.data4sports.chasecricket.models.Constants;
import com.data4sports.chasecricket.models.MyApplicationClass;
import com.data4sports.chasecricket.models.SharedPrefManager;
import com.data4sports.chasecricket.models.User;

import org.json.JSONException;
import org.json.JSONObject;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.exceptions.RealmException;
import io.realm.exceptions.RealmPrimaryKeyConstraintException;

public class LoginActivity extends AppCompatActivity {

    EditText editUsername, editPassword;
    Button login;
    TextView newRegistration, forgetPassword;
//    ImageButton showPassword;

    Realm realm;

    SharedPreferences mPreferences;
    SharedPreferences.Editor mEditor;
    private ProgressDialog progress;

    RealmConfiguration config;

    int userId = 0, temp = 0, d4s_userid = 0;

//    String LOGIN_API_URL = "https://cricketlive.data4sports.com/chaseweb/public/api/login";

    String uname, upass, name, token;

    boolean flag = false;

    int click = 1;
    //    ProgressBar progressBar;
    RequestQueue queue;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);

        mPreferences = PreferenceManager.getDefaultSharedPreferences(this);

//        Log.d("onCreate", "inside onCreate()\n\n");

        editUsername = findViewById(R.id.editEmail);
        editPassword = findViewById(R.id.editLoginPassword);
        login = findViewById(R.id.btnloginpage);
        newRegistration = findViewById(R.id.tv_new_reg);
//        progressBar = findViewById(R.id.progressBar_login);
        forgetPassword = findViewById(R.id.tv_forget_password);

//         config = new RealmConfiguration.Builder()
//                .name(AppConstants.GAME_ID + ".realm")
//                .deleteRealmIfMigrationNeeded()
//                .build();
//        realm = Realm.getInstance(config);
        realm = Realm.getDefaultInstance();
//        showPassword = findViewById(R.id.display_password);
//        test = findViewById(R.id.test);

//        getFromSP();
        queue = Volley.newRequestQueue(this);


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                   Log.d("onCreate", "inside login btn click, \n\n");
                userLogin();

            }
        });


        newRegistration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("onCreate", "inside newreg btn click\n\n");
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });

        forgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("onCreate", "inside forgetpassword btn click\n\n");
                startActivity(new Intent(LoginActivity.this, ForgotPassword.class));
            }
        });

//        }
    }


    void userLogin() {


        Log.d("userLogin", "inside userLogin()\n\n");
        final String username = editUsername.getText().toString();
        final String password = editPassword.getText().toString();

//        editPassword.setTransformationMethod(new PasswordTransformationMethod());


        //validating inputs
        if (TextUtils.isEmpty(username)) {
            editUsername.setError("Please enter your username");
            editUsername.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(password)) {
            editPassword.setError("Please enter your password");
            editPassword.requestFocus();
            return;
        }
//        Log.d("Test" , "brfore click");

//        //set visibile of password
//        showPassword.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//
//                if (click == 1 ){
//
//
//                    showPassword.setImageResource(R.drawable.ic_visibility_black_24dp);
//                    click = 2;
//                    Log.d("Test" , "Click : "+click);
//                    //
//
//                    editUsername.setTransformationMethod(new HideReturnsTransformationMethod());
//                }
//                else {
//
//                    Log.d("Test" , "Click : "+click);
//                    editPassword.setTransformationMethod(new PasswordTransformationMethod());
//                    showPassword.setImageResource(R.drawable.ic_visibility_off_black_24dp);
//                    click =1;
//                }
//
//            }
//        });


        Log.d("Login", "isNetworkAvailable = " + isNetworkAvailable());
        if (isNetworkAvailable()) {

            displayProgress();
            try {
                login(username, password);
            } catch (JSONException e) {

                progress.dismiss();
                Toast.makeText(getApplicationContext(), " " + e.getMessage(), Toast.LENGTH_SHORT).show();
                Log.d("response", "JSONException : " + e);
                e.printStackTrace();
            }
        } else {

            displayNetworkErrorMessage();

//            AlertDialog alertDialog = new AlertDialog.Builder(LoginActivity.this).create();
//            alertDialog.setIcon(R.drawable.ball);
//            alertDialog.setTitle("Network Error");
//            alertDialog.setMessage("Please check your INTERNET connection");
//            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
//                    new DialogInterface.OnClickListener() {
//                        public void onClick(DialogInterface dialog, int which) {
//                            dialog.dismiss();
//                        }
//                    });
//            alertDialog.show();

        }

    }


    void displayNetworkErrorMessage() {

//        Log.d("disnetEmsg", "inside displayNetworkErrorMessage\n\n");

        AlertDialog alertDialog = new AlertDialog.Builder(LoginActivity.this).create();
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


    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        Log.d("Login", "connectivityManager = " + connectivityManager);
        Log.d("Login", "activeNetworkInfo = " + activeNetworkInfo);
        Log.d("Login", "activeNetworkInfo.isConnected = " + activeNetworkInfo.isConnected());
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }


    public void login(final String username, final String password) throws JSONException {

        Log.d("login 1", "inside login(" + username + ", " + password + ")\n\n");

        if (isNetworkAvailable()) {
            Log.d("login 2", "network available");


            JSONObject postparams = new JSONObject();
            postparams.put("email", username);
            postparams.put("password", password);

            Log.e("login 3", "postparams : " + postparams);
//        Log.d("LOGIN METHOD", "JSONObject is created");
//        Log.d("LOGIN METHOD", "JSONObject = postparams<"+postparams+">");

            //comment starts here
            JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                    Constants.API_URL + "login?email=" + username + "&password=" + password,
                    null,// postparams,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {

                            Log.d("login 4.1 success", "response : " + response);
                            Log.d("login 4.2 success", "Constants.LOGIN_API_URL : " + Constants.LOGIN_API_URL);
                            Log.d("login 4.3 success", "jsonObjReq : " + postparams);

                            try {
//                            Log.d("login(u,p)", "error = "+response.getString("error"));
//                            Log.d("login(u,p)", "message = "+response.getString("message"));
////                            Log.d("login(u,p)", "inside JsonObjectRequest\n\n");
//                            Log.d("login(u,p)", "response = "+response);
//                            Log.d("login(u,p)", "error = "+response.getString("error"));

                                //if no error in response
                                if (!response.getBoolean("error")) {
//                                Toast.makeText(getApplicationContext(), response.getString("message"), Toast.LENGTH_SHORT).show();
//
//

                                    //getting the user from the response
                                    JSONObject userJson = response.getJSONObject("user");
                                    Log.d("login 4.4 success", "userJson : " + userJson);


                                   /* //creating a new user object
                                    User user = new User(

                                            userJson.getInt("id"),
                                            userJson.getString("name"),
                                            userJson.getString("email"),
                                            userJson.getString("token"),
                                            userJson.getString("access_token")
                                    );
                                    Log.d("login 5", "Value read is : name = " +
                                            user.getUsername() + ", email = " + user.getEmail() +
                                            ", access_token = " + user.getToken());


                                    Log.d("Login 6 ", "response = " + response);*/
//                                    token = user.getToken();
//                                    name = user.getUsername();
//                                    RealmResults<User> results = realm.where(User.class).findAll();
//
//                                    results.load();
                                    /*for (User usr : results) {

//                                    flag = false;

                                        if (usr.getEmail().matches(user.getEmail()) && usr.getUsername().matches(user.getUsername())) {

                                            userId = usr.getUserId();
                                            user.setUserId(userId);

                                            Log.d("login 7", "user id = " + userId);
//                                        flag = false;
                                            break;
                                        } else {*/

                                    Realm realm = null;
                                    try {
//            Toast.makeText(getApplicationContext(), "Inside savePlayerDetails", Toast.LENGTH_SHORT).show();
//                                        config = new RealmConfiguration.Builder()
//                                                .name(AppConstants.GAME_ID + ".realm")
//                                                .deleteRealmIfMigrationNeeded()
//                                                .build();
                                        realm = Realm.getDefaultInstance();
                                        realm.executeTransaction(new Realm.Transaction() {
                                            @Override
                                            public void execute(Realm bgRealm) {

//                                                        bgRealm.beginTransaction();
//                                                    User user1 = new User();

                                                try {

//                                                        user1.setUsername(user.getUsername());
//                                                        user1.setEmail(user.getEmail());

//                                                        Number number = bgRealm.where(User.class).max("userId");
//                                                        if (number != null)
//                                                            temp = number.intValue();
//                                                        Log.d("login", "user id = " + temp);
//                                                        userId = temp++;
//                                  setEmail                      user1.setUserId(userId);
                                                    Number num = bgRealm.where(User.class).max("userId");
                                                    int nextId = (num == null) ? 1 : num.intValue() + 1;
                                                    userId = nextId;
                                                    User user1 = bgRealm.createObject(User.class, nextId);
                                                    Log.d("login", "user id = " + nextId);
//                                                        user1.setUsername(username);
                                                    d4s_userid = userJson.getInt("id");
                                                    token = userJson.getString("token");
                                                    user1.setD4s_userid(d4s_userid);
                                                    user1.setUsername(userJson.getString("name"));
                                                    user1.setEmail(username);
                                                    user1.setToken(userJson.getString("token"));
                                                    user1.setAccess_token(userJson.getString("access_token"));
                                                    user1.setLoggedInStatus(true);
                                                    bgRealm.copyToRealm(user1);

                                                    saveToSP();
                                                            /*mEditor = mPreferences.edit();
                                                            mEditor.putInt("sp_user_id", userId);
                                                            mEditor.putString("sp_user_name", name);
                                                            mEditor.putString("user_token", token);
                                                            mEditor.putInt("d4s_userid", d4s_userid);
                                                            mEditor.apply();*/

                                                    Log.d("login", "user = " + user1);
                                                    Log.d("login", "userId = " + userId);
                                                    Log.d("login", "d4s_userid = " + d4s_userid);
                                                    Log.d("login", "token = " + token);
                                                    SharedPrefManager.getInstance(getApplicationContext()).userLogin(user1);
                                                    Intent i = new Intent(LoginActivity.this, HomeActivity.class);
//                                                            i.putExtra("userId", userId);
//                                                            i.putExtra("d4s_userid", d4s_userid);
//                                                            i.putExtra("token", token);
                                                    finish();
                                                    startActivity(i);

                                                } catch (RealmPrimaryKeyConstraintException e) {
//                                                        progressBar.setVisibility(View.GONE);
                                                    Toast.makeText(getApplicationContext(),
                                                            "Primary Key exists, Press Update instead",
                                                            Toast.LENGTH_SHORT).show();
                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        });
                                    } catch (RealmException e) {
                                        progress.dismiss();
                                        Toast.makeText(getApplicationContext(),
                                                "Exception : " + e, Toast.LENGTH_SHORT).show();
                                        Log.d("RealmException 8", "" + e);
                                    } finally {

                                        if (realm != null) {
                                            realm.close();
                                        }
                                    }
                                       /* }

                                    }
*/

//                                if (flag) {
//                                    Number number = realm.where(User.class).max("userId");
//                                    int temp = number.intValue();
//                                    Log.d("login", "user id = " + temp);
//                                    userId = temp++;
//                                    user.setUserId(userId);
//                                }


                                    progress.dismiss();
                                    Toast.makeText(getApplicationContext(), response.getString("message"), Toast.LENGTH_SHORT).show();
                                    Log.d("login 9", "" + response);


//                                        public int getNextKey() {
//                                            try {
//                                                Number number = realm.where(User.class).max("id");
//                                                if (number != null) {
//                                                    return number.intValue() + 1;
//                                                } else {
//                                                    return 0;
//                                                }
//                                            } catch (ArrayIndexOutOfBoundsException e) {
//                                                return 0;
//                                            }
//                                        }


//                                    if (!(player.getPlayerName()).matches(bowler)) {
//                                        arrayAdapter.add(player.getPlayerName());
//                                    }


                                    //starting the profile activity
                                    finish();
                                    startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                                } else {
                                    progress.dismiss();
                                    Toast.makeText(getApplicationContext(), response.getString("message"), Toast.LENGTH_SHORT).show();
                                    editUsername.setText("");
                                    editPassword.setText("");
                                    Log.d("login 10", "inside else error = " + response);

                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Log.d("JSONException 11", "" + e);
                            }

                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                            progress.dismiss();
                            Log.d("login 12", "VolleyError  : " + error);
                            Toast.makeText(LoginActivity.this, "VolleyError  : " + error, Toast.LENGTH_SHORT).show();

                            if (error instanceof TimeoutError || error instanceof NoConnectionError) {
//                                Toast.makeText(context,
//                                        context.getString(R.string.error_network_timeout),
//                                        Toast.LENGTH_LONG).show();
                                Log.d("login 13", "TimeoutError / NoConnectionError  : " + error);
                            } else if (error instanceof AuthFailureError) {
                                //TODO
                                Log.d("login 14", "AuthFailureError  : " + error);
                            } else if (error instanceof ServerError) {
                                //TODO
                                Log.d("login 15", "ServerError  : " + error);
                            } else if (error instanceof NetworkError) {
                                //TODO
                                Log.d("login 16", "NetworkError  : " + error);
                            } else if (error instanceof ParseError) {
                                //TODO
                                Log.d("login 17", "ParseError  : " + error);
                            }


                           /*if (isNetworkAvailable()) {
                                progressBar.setVisibility(View.GONE);
                                Toast.makeText(getApplicationContext(), "Invalid Email or Password", Toast.LENGTH_SHORT).show();
                            } else {

                                displayNetworkErrorMessage();
                            }
//                        displayNetworkErrorMessage();*/
                            editUsername.setText("");
                            editPassword.setText("");
                        }
                    });

            //extra added

            /*{
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    HashMap<String, String> hashMap = new HashMap<>();
                    hashMap.put("email", username);
                    hashMap.put("password", password);
                    return hashMap;
                }
            };*/

//            queue.add(jsonObjReq);

            Log.d("login 18", "Request : " + jsonObjReq);
            Log.d("login 19", "postparams : " + postparams);
//            Log.d("login 20", "queue : " + queue);

            MyApplicationClass.getInstance(getApplicationContext()).addToRequestQueue(jsonObjReq, "postRequest");
//            MyApplicationClass.getInstance(getApplicationContext()).addToRequestQueue(jsonObjReq, );



           /* StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.LOGIN_API_URL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response1) {
                            progressBar.setVisibility(View.GONE);

                            try {
                                //converting response to json object
                                JSONObject response = new JSONObject(response1);

                                //if no error in response
                                if (!response.getBoolean("error")) {

                                    JSONObject userJson = response.getJSONObject("user");

                                    //creating a new user object
                                    User user = new User(

                                            userJson.getString("name"),
                                            userJson.getString("email"),
                                            userJson.getString("token")
                                    );
                                    Log.d("login 5", "Value read is : name = " +
                                            user.getUsername() + ", email = " + user.getEmail() +
                                            ", access_token = " + user.getToken());


                                    Log.d("Login 6 ", "response = " + response);
                                    token = user.getToken();
                                    name = user.getUsername();
                                    RealmResults<User> results = realm.where(User.class).findAll();

                                    results.load();
                                    for (User usr : results) {

                                        if (usr.getEmail().matches(user.getEmail()) && usr.getUsername().matches(user.getUsername())) {

                                            userId = usr.getUserId();
                                            user.setUserId(userId);

                                            Log.d("login 7", "user id = " + userId);
//                                        flag = false;
                                            break;
                                        } else {

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

                                                        bgRealm.beginTransaction();
//                                                    User user1 = new User();

                                                        try {

                                                            Number num = bgRealm.where(User.class).max("userId");
                                                            int nextId = (num == null) ? 1 : num.intValue() + 1;

                                                            User user1 = bgRealm.createObject(User.class, nextId);
//                                                        user1.setUsername(username);
                                                            user1.setEmail(username);
                                                            user1.setLoggedInStatus(true);
                                                            bgRealm.copyToRealm(user1);
                                                            SharedPrefManager.getInstance(getApplicationContext()).userLogin(user);
                                                            Intent i = new Intent(LoginActivity.this, HomeActivity.class);
                                                            i.putExtra("userId", userId);
                                                            finish();
                                                            startActivity(i);

                                                        } catch (RealmPrimaryKeyConstraintException e) {
//                                                        progressBar.setVisibility(View.GONE);
                                                            Toast.makeText(getApplicationContext(),
                                                                    "Primary Key exists, Press Update instead",
                                                                    Toast.LENGTH_SHORT).show();
                                                        }
                                                    }
                                                });
                                            } catch (RealmException e) {
                                                progressBar.setVisibility(View.GONE);
                                                Toast.makeText(getApplicationContext(),
                                                        "Exception : " + e, Toast.LENGTH_SHORT).show();
                                                Log.d("RealmException 8", "" + e);
                                            } finally {

                                                if (realm != null) {
                                                    realm.close();
                                                }
                                            }
                                        }

                                    }

                                    saveToSP();
                                    progressBar.setVisibility(View.GONE);
                                    Toast.makeText(getApplicationContext(), response.getString("message"), Toast.LENGTH_SHORT).show();
                                    Log.d("login 9", "" + response);


                                    //starting the profile activity
                                    finish();
                                    startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                                } else {
                                    progressBar.setVisibility(View.GONE);
                                    Toast.makeText(getApplicationContext(), response.getString("message"), Toast.LENGTH_SHORT).show();
                                    Log.d("login 10", "inside else error = "+response);
                                }

//                                //if no error in response
//                                if (!obj.getBoolean("error")) {
//                                    Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();
//
//                                    //getting the user from the response
//                                    JSONObject userJson = obj.getJSONObject("user");
//
//                                    //creating a new user object
//                                    User user = new User(
//                                            userJson.getInt("id"),
//                                            userJson.getString("username"),
//                                            userJson.getString("email"),
//                                            userJson.getString("gender")
//                                    );
//
//                                    //storing the user in shared preferences
//                                    SharedPrefManager.getInstance(getApplicationContext()).userLogin(user);
//
//                                    //starting the profile activity
//                                    finish();
//                                    startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
//                                } else {
//                                    Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();
//                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
//                            Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);

                            Log.d("login 12", "VolleyError  : "+error);
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(getApplicationContext(), "Invalid Email or Password", Toast.LENGTH_SHORT).show();

                            editUsername.setText("");
                            editPassword.setText("");
                        }
                    }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("email", username);
                    params.put("password", password);
                    return params;
                }
            };

            MyApplicationClass.getInstance(this).addToRequestQueue(stringRequest, "login");*/

        } else
            displayNetworkErrorMessage();
//        Volley.newRequestQueue(this).add(jsonObjReq);

    }


    void getFromSP() {

        mPreferences = this.getSharedPreferences("Login", 0);
        uname = mPreferences.getString("email", null);
        upass = mPreferences.getString("password", null);
        Log.d("test 1", "username : " + uname);
        Log.d("test 1", "password : " + upass);

    }


    void saveToSP() {

        mEditor = mPreferences.edit();
        mEditor.putInt("sp_user_id", userId);
        mEditor.putString("sp_user_name", name);
        mEditor.putString("user_token", token);
        mEditor.putInt("d4s_userid", d4s_userid);
        mEditor.apply();
        Log.d("userId", "Login, : " + userId);
        Log.d("userId", "Login, : " + userId);
    }


    public void displayProgress() {

        progress = new ProgressDialog(this);
        progress.setMessage("Please wait...");
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setIndeterminate(true);
        progress.setProgress(0);
        progress.show();
    }


}


