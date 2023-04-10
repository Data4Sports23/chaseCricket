package com.data4sports.chasecricket.activities;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.data4sports.chasecricket.R;
import com.data4sports.chasecricket.applicationConstants.AppConstants;
import com.data4sports.chasecricket.models.Constants;
import com.data4sports.chasecricket.models.Match;
import com.data4sports.chasecricket.models.MatchOfficials;
import com.data4sports.chasecricket.models.MetricData;
import com.data4sports.chasecricket.models.MidGen;
import com.data4sports.chasecricket.models.MyApplicationClass;

import org.joda.time.DateTimeComparator;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;
import io.realm.exceptions.RealmException;
import io.realm.exceptions.RealmPrimaryKeyConstraintException;

public class CreateMatchActivity extends AppCompatActivity
        implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    EditText teamA, teamB, venue, end1, end2, event, phase;
    EditText /*c_players, c_subst,*/ c_playersA, c_playersB, c_overs, c_balls, et_max_opb, et_max_bpb/*, et_days*/; // days will be added later
    EditText eventDate, umpire1, umpire2, umpire3, umpire4,scorer, match_referee;
    RadioGroup match_type, rg_innings;
    RadioButton H_100, T20, ODI, Test, Custom, singleInnings, multiInnings;
    CheckBox limited_over, wagon_wheel;
    Button btn_wideRun, btn_noballRun, btn_penaltyRun, next;
    LinearLayout custom_innings, custom_overs, custom_players, custom_values, ll_max_opb, ll_max_bpb;
    Realm realm;
    LinearLayout ground_man1_layout,ground_man2_layout,ground_man3_layout,ground_man4_layout;

    String type = null, team1 = null, team2 = null, venue_v = null, end1_v = null, end2_v = null,
            event_v = null, phase_v = null, innings = null, ttoken = null, token = null,
            umpire1_v = null, umpire2_v = null, umpire3_v = null, umpire4_v = null, match_referee_v = null,
            scorer_v = null, date_v = null;

    int over = 0, balls = 0, /*playr = 11, sub = 0,*/playrA = 11, playrB = 11, wideRun = 1,
            noballRun = 1, penaltyRun = 5,
            userId = 0, matchid, gameid = 0, totalInnings = 0, d4s_userid = 0, max_opb = 0,
            max_bpb = 0;
    String matchID;
    ImageView ground_man1_add, ground_man2_add, ground_man3_add, ground_man4_add, ground_man1_delete,ground_man2_delete, ground_man3_delete,
            ground_man4_delete;

    String spinnerText1;

    private String count1 = null;


    public static boolean ERROR = false;

    SharedPreferences mPreferences;
    SharedPreferences.Editor mEditor;

    final MidGen random = new MidGen();
    boolean single = false, multi = false, limited_over_flag = false, wheel = false, HUNDRED = false;
    final Calendar myCalendar = Calendar.getInstance();

    private ProgressDialog progress;
    ImageButton back;

    RealmConfiguration config;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN);



        setContentView(R.layout.activity_create_match);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.custom_action_bar_layout);
        View view =getSupportActionBar().getCustomView();
//        ground_man1_layout = (LinearLayout)this.findViewById(R.id.ground_man1_layout);
//        ground_man2_layout = (LinearLayout)this.findViewById(R.id.ground_man2_layout);
//        ground_man3_layout = (LinearLayout)this.findViewById(R.id.ground_man3_layout);
//        ground_man4_layout = (LinearLayout)this.findViewById(R.id.ground_man4_layout);
//
//        ground_man1_layout.setVisibility(View.VISIBLE);
//        ground_man2_layout.setVisibility(View.GONE);
//        ground_man3_layout.setVisibility(View.GONE);
//        ground_man4_layout.setVisibility(View.GONE);

        back = view.findViewById(R.id.action_bar_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*finish();*/
                Log.e("scoring", "oncreate, back button pressd");

                onBackPressed();
            }
        });
//        ground_man1_add = (ImageView) findViewById(R.id.ground_man1_add);
//        ground_man1_add.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//              ground_man2_layout.setVisibility(View.VISIBLE);
//            }
//        });
//        ground_man1_delete = (ImageView)findViewById(R.id.ground_man1_delete);
//        ground_man1_delete.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                ground_man1_layout.setVisibility(View.GONE);
//            }
//        });

//        ground_man2_add = (ImageView)findViewById(R.id.ground_man2_add);
//        ground_man2_add.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                ground_man3_layout.setVisibility(View.VISIBLE);
//            }
//        });
//        ground_man2_delete = (ImageView)findViewById(R.id.ground_man2_delete);
//        ground_man2_delete.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                ground_man2_layout.setVisibility(View.GONE);
//            }
//        });
//
//        ground_man3_add = (ImageView)findViewById(R.id.ground_man3_add);
//        ground_man3_add.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                ground_man4_layout.setVisibility(View.VISIBLE);
//            }
//        });
//        ground_man3_delete = (ImageView)findViewById(R.id.ground_man3_delete);
//        ground_man3_delete.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                ground_man3_layout.setVisibility(View.GONE);
//            }
//        });
//
//        ground_man4_delete = (ImageView)findViewById(R.id.ground_man4_delete);
//        ground_man4_delete.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                ground_man4_layout.setVisibility(View.GONE);
//            }
//        });
        mPreferences = PreferenceManager
                .getDefaultSharedPreferences(this);

//        userId = mPreferences.getInt("userId", 0);

        SharedPreferences sharedPreferences = this.getSharedPreferences("volleyregisterlogin", Context.MODE_PRIVATE);
        token = sharedPreferences.getString("keytoken", null);
//        token = mPreferences.getString("sp_teamA", null);




        Realm.init(this);
//         config = new RealmConfiguration.Builder()
//                .name(AppConstants.GAME_ID + ".realm")
//                .deleteRealmIfMigrationNeeded()
//                .build();
//        realm = Realm.getInstance(config);

        getFromSP();

        assignElements();
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

//        saveMatchDetails();

        limited_over.setOnCheckedChangeListener(this);
        wagon_wheel.setOnCheckedChangeListener(this);

        btn_wideRun.setOnClickListener(this);
        btn_noballRun.setOnClickListener(this);
        btn_penaltyRun.setOnClickListener(this);

        type = null;

        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

        };

        eventDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(CreateMatchActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

//
        next.setOnClickListener(this);
        //limited_over.setOnClickListener(this);

    }

    private void spinnerAdaptorSetup1(Spinner
                                              spinner1, List<MetricData> unitList1) {

        List<String> divisionList = new ArrayList<>();

        for (MetricData data : unitList1) {
            divisionList.add(data.getStop());
        }
        //adaptor
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, divisionList);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //set the default according to value
        spinner1.setAdapter(adapter);
        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {

                spinnerText1 = parentView.getItemAtPosition(position).toString();
                getData1(unitList1);

            }


            @Override
            public void onNothingSelected(AdapterView<?> parentView) {

            }

        });
        spinner1.getSelectedItem();
    }

    private void getData1(List<MetricData> unitList1) {
        for (MetricData data : unitList1) {
            if (data.getStop().contains(spinnerText1)) {
                count1 = data.getId();
                break;
            }
        }
    }


    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.rb_100:
                set100();
                break;

            case R.id.rb_T20:
                setT20();
                break;

            case R.id.rb_ODI:
                setODI();
                break;

            case R.id.rb_Test:
                setTest();
                break;

            case R.id.rb_Custom:
                setCustomType();
                break;

            case R.id.rb_single:
                single = true;
                multi = false;
                innings = "single";
//                ll_days.setVisibility(View.GONE); Commented (08/07/2021) for later
                break;

            case R.id.rb_multi:
                single = false;
                multi = true;
                innings = "multi";
//                ll_days.setVisibility(View.VISIBLE); Commented (08/07/2021) for later
                break;

//            case R.id.custom_overs:
////                customOver();

            case R.id.btn_change_wide_run:
                changeWideRuns();
                break;

            case R.id.btn_change_noball_run:
                changeNoballRuns();
                break;

            case R.id.btn_change_penalty_run:
                changePenaltyRuns();
                break;

            case R.id.btn_next:
//                checkInput();
//                displayProgress();
                // Added thread on 22/04/2021
                new Thread(new Runnable() {
                    @Override
                    public void run() {

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //your code or your request that you want to run on uiThread
                                saveMatchDetails();
                            }
                        });
                    }
                }).start();
//                saveMatchDetails();   Commented on 22/04/2021
                break;

//            case R.id.checkbox_limited_overs:
//                Toast.makeText(getApplicationContext(), "Match type is not selected", Toast.LENGTH_SHORT).show();
//                setOver();
//                break;

            default:
                Toast.makeText(getApplicationContext(), "Match type is not selected", Toast.LENGTH_SHORT).show();
                break;


        }
    }




    // assign elements to its corresponding UI Elements

    public void assignElements(){

        teamA = findViewById(R.id.edit_team_A);
        teamB = findViewById(R.id.edit_team_B);
        venue = findViewById(R.id.edit_venue);
        end1 = findViewById(R.id.edit_venue_end1);
        end2 = findViewById(R.id.edit_venue_end2);
        event = findViewById(R.id.edit_event);
        phase = findViewById(R.id.edit_event_phase);

//        c_players = findViewById(R.id.edit_no_of_player);
//        c_subst = findViewById(R.id.edit_subst_playes);
        c_playersA = findViewById(R.id.edit_player_teamA);
        c_playersB = findViewById(R.id.edit_player_teamB);
        c_overs = findViewById(R.id.edit_overs);
        c_balls = findViewById(R.id.edit_balls_per_over);

        btn_wideRun = findViewById(R.id.btn_change_wide_run);
        btn_noballRun = findViewById(R.id.btn_change_noball_run);
        btn_penaltyRun = findViewById(R.id.btn_change_penalty_run);

        wagon_wheel = findViewById(R.id.checkbox_wagon_wheel);

        eventDate = findViewById(R.id.edit_event_date);
        umpire1 = findViewById(R.id.edit_umpire1);
        umpire2 = findViewById(R.id.edit_umpire2);
        umpire3 = findViewById(R.id.edit_umpire3);
        umpire4 = findViewById(R.id.edit_umpire4);
        scorer = findViewById(R.id.edit_scorer);
        match_referee = findViewById(R.id.edit_match_referee);
        match_type = findViewById(R.id.rgroup_match_type);
        rg_innings = findViewById(R.id.rgroup_innings);
        singleInnings = findViewById(R.id.rb_single);
        multiInnings = findViewById(R.id.rb_multi);

        H_100 = findViewById(R.id.rb_100); // Added on 23/07/2021
        T20 = findViewById(R.id.rb_T20);
        ODI = findViewById(R.id.rb_ODI);
        Test = findViewById(R.id.rb_Test);
        Custom = findViewById(R.id.rb_Custom);

        next = findViewById(R.id.btn_next);

        custom_innings = findViewById(R.id.custom_innings);
        custom_overs = findViewById(R.id.custom_overs);
        custom_players = findViewById(R.id.custom_players);
        custom_values = findViewById(R.id.custom_values);

        limited_over = findViewById(R.id.checkbox_limited_overs);

        et_max_opb = findViewById(R.id.edit_max_opb);
        ll_max_opb = findViewById(R.id.ll_max_opb);
        ll_max_opb.setVisibility(View.GONE);

        /*  Commented (08/07/2021) for later
        ll_days = findViewById(R.id.ll_days);
        et_days = findViewById(R.id.edit_no_of_days);
        ll_days.setVisibility(View.GONE);*/

        // Added on 30/07/2021
        et_max_bpb = findViewById(R.id.edit_max_bpb);
        ll_max_bpb = findViewById(R.id.ll_max_bpb);
        ll_max_bpb.setVisibility(View.GONE);
    }


    private void updateLabel() {
        String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());
        String myFormat = "yyyy-MM-dd";//"dd/MM/yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.UK);
////        myCalendar.get(Calendar.YEAR)
//        Date c = Calendar.getInstance().getTime();
//        Log.e("CreateMatchActivity", "updateLabel, date 1 " +c);
//        SimpleDateFormat df = new SimpleDateFormat("dd/MMM/yyyy");
//        Log.e("CreateMatchActivity", "updateLabel, date 2 " +df);
//        String formattedDate = df.format(c);
//        Log.e("CreateMatchActivity", "updateLabel, date 3 " +formattedDate);
        date_v = sdf.format(myCalendar.getTime());

        Log.e("CreateMatchActivity", "updateLabel, date 4 " +sdf.format(new Date()));
        Log.e("CreateMatchActivity", "updateLabel, date 5 : "+date_v);
        Log.e("CreateMatchActivity", "updateLabel, currentDateTimeString : "+currentDateTimeString);

        // to compare date

        DateTimeComparator dateTimeComparator = DateTimeComparator.getDateOnlyInstance();
        Date currentDate = new Date();
        Date matchDate = myCalendar.getTime();

        Log.e("CreateMatchActivity", "updateLabel, currentDate " +currentDate);
        Log.e("CreateMatchActivity", "updateLabel, matchDate " +matchDate);

        int retVal = dateTimeComparator.compare(currentDate, matchDate);

        if(retVal == 0)
        //both dates are equal
            Log.e("CreateMatchActivity", "updateLabel, retVal 1 : " +retVal); // current date or todays date (0)
        else if(retVal < 0) {
            //myDateOne is before myDateTwo
            Log.e("CreateMatchActivity", "updateLabel, retVal 2 : " + retVal);// future date (-1)

            date_v = null;
        }
        else if(retVal > 0)
        //myDateOne is after myDateTwo
            Log.e("CreateMatchActivity", "updateLabel, retVal 3 : " +retVal); // selected date is previous date (1)

        if (date_v != null)
            eventDate.setText(date_v);
        else
            Toast.makeText(getApplicationContext(), "Invalid date", Toast.LENGTH_SHORT).show();
    }




    public void saveMatchDetails() {

        ERROR = false;
        String str_max_opb = "", str_max_bpb = "";
        displayProgress();
        team1 = teamA.getText().toString();
        team2 = teamB.getText().toString();
        venue_v = venue.getText().toString();
        end1_v = end1.getText().toString();
        end2_v = end2.getText().toString();
        event_v = event.getText().toString();
        phase_v = phase.getText().toString();

        umpire1_v = umpire1.getText().toString();
        umpire2_v = umpire2.getText().toString();
        umpire3_v = umpire3.getText().toString();
        umpire4_v = umpire4.getText().toString();
        match_referee_v = match_referee.getText().toString();
        scorer_v = scorer.getText().toString();
        Log.d("CREATE", "saveMatchDetails, team1 : " + team1);
        Log.d("CREATE", "saveMatchDetails, team2 : " + team2);
        Log.d("CREATE", "saveMatchDetails, venue_v : " + venue_v);
        Log.d("CREATE", "saveMatchDetails, end1_v : " + end1_v);
        Log.d("CREATE", "saveMatchDetails, end2_v : " + end2_v);
        Log.d("CREATE", "saveMatchDetails, event_v : " + event_v);
        Log.d("CREATE", "saveMatchDetails, phase_v : " + phase_v);
        Log.d("CREATE", "saveMatchDetails, type : " + type);
        Log.d("CREATE", "saveMatchDetails, date_v : " + date_v);
        Log.d("CREATE", "saveMatchDetails, innings : " + innings);
        Log.d("CREATE", "saveMatchDetails, ll_max_opb : " + ll_max_opb.getVisibility());
        Log.d("CREATE", "saveMatchDetails, umpire1_v : " + umpire1_v);
        Log.d("CREATE", "saveMatchDetails, umpire2_v : " + umpire2_v);
        Log.d("CREATE", "saveMatchDetails, umpire3_v : " + umpire3_v);
        Log.d("CREATE", "saveMatchDetails, umpire4_v : " + umpire4_v);
        Log.d("CREATE", "saveMatchDetails, match_referee_v : " + match_referee_v);
        Log.d("CREATE", "saveMatchDetails, scorer_v : " + scorer_v);

        // Moved from bottom on 10/09/2021
        if (type.matches("Custom")) {


            if (rg_innings.getCheckedRadioButtonId() == -1) {
                // no radio buttons are checked
                Toast.makeText(getApplicationContext(), "Please select the innings", Toast.LENGTH_SHORT).show();
            } else {
                // one of the radio buttons is checked
                if (singleInnings.isChecked()) {
                    single = true;
                    multi = false;
                    totalInnings = 2;
                }
                if (multiInnings.isChecked()) {
                    multi = true;
                    single = false;
                    totalInnings = 4;
                }
            }
        }
        else
            setInnings();

        if (playrA == 0 || playrB == 0) {

//            String str = c_players.getText().toString();
            String strA = c_playersA.getText().toString();
            String strB = c_playersB.getText().toString();

            Log.d("CREATE", "saveMatchDetails, strA : " + strA);
            Log.d("CREATE", "saveMatchDetails, strB : " + strB);
            Log.d("CREATE", "saveMatchDetails, playrA : " + playrA);
            Log.d("CREATE", "saveMatchDetails, playrB : " + playrB);
            if ((strA.matches("")) || (strB.matches(""))) {
                ERROR = true;
                displayError("Please enter number of players");
            }
            else {
                Log.d("CREATE", "saveMatchDetails, Integer.parseInt(str) : " + Integer.parseInt(strA));
                Log.d("CREATE", "saveMatchDetails, Integer.parseInt(str) : " + Integer.parseInt(strB));
                playrA = Integer.parseInt(strA);
                playrB = Integer.parseInt(strB);
                if (playrA < 3 || playrA > 30) {
                    ERROR = true;
                    displayError("Invalid number of Players");
                    c_playersA.setText("");
                    playrA = 0;
                }
                if (playrB < 3 || playrB > 30) {
                    ERROR = true;
                    displayError("Invalid number of Players");
                    c_playersB.setText("");
                    playrB = 0;
                }
            }
        }

//        String s = c_subst.getText().toString();
//        if (!s.matches("")) {
//            sub = Integer.parseInt(s);
//        }

        if (limited_over.isChecked()) {                         // Updated on 29/04/2021
            String str = c_overs.getText().toString();
            if (str.matches("")) {
                ERROR = true;
                displayError("Please enter total overs");
            }
            else {
                over = Integer.parseInt(str);
                if ((over <= 0) || (over > 500)) {
                    ERROR = true;
                    displayError("Invalid Over");
                    c_overs.setText("");
                    over = 0;
                }
            }

            String str1 = c_balls.getText().toString();
            if (str1.matches("")) {
                ERROR = true;
                displayError("Please enter balls per over");
            }
            else {
                balls = Integer.parseInt(str1);
                if ((balls < 3) || balls > 10) {
                    ERROR = true;
                    displayError("Min is 3, max value is 10");
                    c_balls.setText("");
                    balls = 0;
                }
            }

        }

        if (single) {
            innings = "single";
        } else if (multi) {
            innings = "multi";
        }


        if (wagon_wheel.isChecked())
            wheel = true;

        else if (!wagon_wheel.isChecked())
            wheel = false;


        // === till here

        if (limited_over_flag){
            // Added on 30/07/2021
            if (HUNDRED) {
                str_max_bpb = et_max_bpb.getText().toString();
                if (!str_max_bpb.matches(""))
                    max_bpb = Integer.parseInt(str_max_bpb);
            } else {
                str_max_opb = et_max_opb.getText().toString();
                if (!str_max_opb.matches(""))
                    max_opb = Integer.parseInt(str_max_opb);
            }

        }
        else
            max_opb = 0;




        Log.d("CREATE", "saveMatchDetails, str_max_opb : " + str_max_opb);
        Log.d("CREATE", "saveMatchDetails, max_opb : " + max_opb);
        Log.d("CREATE", "saveMatchDetails, over : " + over);
        Log.d("CREATE", "saveMatchDetails, limited_over_flag : " + limited_over_flag);

        if (team1.matches("") || team1 == null) {
            ERROR = true;
            displayError("Team names can not be empty");
        }
        else if (team2.matches("") || team2 == null) {
            ERROR = true;
            displayError("Team names can not be empty");
        }
        else if (team1.equalsIgnoreCase(team2)) {
            ERROR = true;
            displayError("Team names can not be same");
        }
        else if (venue_v.matches("") || venue_v == null) {
            ERROR = true;
            displayError("Please enter the required fields");
        }
        else if (event_v.matches("") || event_v == null) {
            ERROR = true;
            displayError("Please enter the required fields");
        }
        else if (/*type.matches("") || */type == null) {
            ERROR = true;
            displayError("Please select a match type");
        }
//        else if (limited_over_flag) { //(ll_max_opb.getVisibility() == View.VISIBLE)/* && (max_opb == 0)*/) {

        else if (!HUNDRED && (limited_over_flag && (str_max_opb.matches("")))) {// || str_max_opb == null) {
//                et_max_opb.setError("Please enter the field");
            ERROR = true;
            displayError("Please enter maximum overs per bowler");
        }
//            else {
//                max_opb = Integer.parseInt(str_max_opb);
        else if (!HUNDRED && (limited_over_flag && (max_opb == 0))) {
            ERROR = true;
            displayError("Please enter maximum overs per bowler");
        }
        else if (!HUNDRED && (limited_over_flag && (max_opb >= over))) {
            ERROR = true;
            displayError("Total over and maximum overs per bowler cannot be same");
        }
//            }
//        }
        else if (/*date_v.matches("") ||*/ date_v == null) {
            ERROR = true;
            displayError("Please select date");
        }
        else if (umpire1_v.matches("") || umpire1_v == null
                || umpire2_v.matches("") || umpire2_v == null) {
            ERROR = true;
            displayError("Invalid officials");
        }
        else {


            Log.d("CREATE", "saveMatchDetails, else");
            Log.d("CREATE", "umpire1_v = " + umpire1_v);
            Log.d("CREATE", "umpire2_v = " + umpire2_v);

           /* if (type.matches("Custom")) {


                if (rg_innings.getCheckedRadioButtonId() == -1) {
                    // no radio buttons are checked
                    Toast.makeText(getApplicationContext(), "Please select the innings", Toast.LENGTH_SHORT).show();
                } else {
                    // one of the radio buttons is checked
                    if (singleInnings.isChecked()) {
                        single = true;
                        multi = false;
                        totalInnings = 2;
                    }
                    if (multiInnings.isChecked()) {
                        multi = true;
                        single = false;
                        totalInnings = 4;
                       *//* Commented on 08/07/2021 (for later)
                        str_days = et_days.getText().toString();
                        if (str_days.matches("")) {
                            ERROR = true;
                            displayError("Please enter total number of days");
                        }
                        else {
                            days = Integer.parseInt(str_days);
                            if (days == 0) {
                                ERROR = true;
                                displayError("Please enter valid number of days");
                            }
                        }*//*
                    }

                }
            }
             else
                 setInnings();*/

//            if (playr == 0) {
//
//                String str = c_players.getText().toString();
//
//                Log.d("CREATE", "saveMatchDetails, str : " + str);
//                Log.d("CREATE", "saveMatchDetails, playr : " + playr);
//                if (str.matches("")) {
//                    ERROR = true;
//                    displayError("Please enter number of players");
//                }
//                else {
//                    Log.d("CREATE", "saveMatchDetails, Integer.parseInt(str) : " + Integer.parseInt(str));
//                    playr = Integer.parseInt(str);
//                    if (playr < 3 || playr > 25) {
//                        ERROR = true;
//                        displayError("Invalid number of Players");
//                        c_players.setText("");
//                        playr = 0;
//                    }
//                }
//            }
//
////            if (sub == 0) {
//                String s = c_subst.getText().toString();
//                if (!s.matches("")) {
////                    displayError("Empty field", "Please enter number of substitute players");
////                }
////                else {
//
//                    sub = Integer.parseInt(s);
///*                    if (sub < 0) {
//                        displayError("Invalid number of Substitute Players");
//                        c_subst.setText("");
//                        sub = 0;
//                    } else if (sub >= 10) {
//                        displayError("Maximum Substitute Player limit is 10");
//                        c_subst.setText("");
//                        sub = 0;
//                    }*/
//
//                }
////            }
//
////            if (/*custom_overs.isShown()*/limited_over_flag) {    // Commented on 29/04/2021
//            if (limited_over.isChecked()) {                         // Updated on 29/04/2021
//                String str = c_overs.getText().toString();
//                if (str.matches("")) {
//                    ERROR = true;
//                    displayError("Please enter total overs");
//                }
//                else {
//                    over = Integer.parseInt(str);
//                    if ((over <= 0) || (over > 500)) {
//                        ERROR = true;
//                        displayError("Invalid Over");
//                        c_overs.setText("");
//                        over = 0;
//                    }
//                }
//               /* else if (over <= ) {
//                    displayError("Minimum Over is 3");
//                    c_overs.setText("");
//                    over = 0;
//                }*/
//
//                String str1 = c_balls.getText().toString();
//                if (str1.matches("")) {
//                    ERROR = true;
//                    displayError("Please enter balls per over");
//                }
//                else {
//                    balls = Integer.parseInt(str1);
//                    if ((balls < 3) || balls > 10) {
//                        ERROR = true;
//                        displayError("Min is 3, max value is 10");
//                        c_balls.setText("");
//                        balls = 0;
//                    }
//                }
//
//            }
//
//            if (single) {
//                innings = "single";
//            } else if (multi) {
//                innings = "multi";
//            }
//
//
//            if (wagon_wheel.isChecked())
//                wheel = true;
//
//            else if (!wagon_wheel.isChecked())
//                wheel = false;



            Log.e("CreateMatchActivity", "saveMatchDetails, team1 : " + team1 + ", team2 : " + team2
                    + ", venue_v : " + venue_v + ", event_v : " + event_v + ", phase_v : " + phase_v +
                    ", type : " + type + ", innings : " + innings + ", playrA : " + playrA + ", playrB : " +
                    playrB + ", over : " + over + ", balls : " + balls);

            /*if ((team1 != null) && (team2 != null) && (!team1.matches(team2)) && (venue_v != null) &&
                    (event_v != null) && (phase_v != null) && (type != null) && (innings != null) && (totalInnings > 0) &&
                    (playr >= 3 && playr < 25) && (sub >= 0 && sub < 11) && (((type.matches("Test")) ||
                    ((type.matches("Custom")) && (innings.matches("multi"))) ||
                    (over >=1 && over < 500)) && (balls >= 3 && balls < 10))) {*/

                // storing the match in device



//                matchID = random.genId();


            Log.d("CREATE", "saveMatchDetails 1, ERROR : " + ERROR);

            if (!ERROR) {
                Log.d("CREATE", "saveMatchDetails 2, ERROR : " + ERROR);
                 config = new RealmConfiguration.Builder()
                .name(AppConstants.GAME_ID + ".realm")
                .deleteRealmIfMigrationNeeded()
                .build();
        realm = Realm.getInstance(config);
                matchID = checkID(random.genId());


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

                                Number num = bgRealm.where(Match.class).max("matchid");
                                int nextId = (num == null) ? 1 : num.intValue() + 1;

                                Match match = bgRealm.createObject(Match.class, nextId);
                                matchid = nextId;
                                match.setMatchID(matchID);
                                match.setUserId(userId);
                                match.setD4s_userid(d4s_userid);

                                match.setTeamA(team1);
                                match.setTeamB(team2);

                                match.setVenue(venue_v);
                                match.setEnd1(end1_v);
                                match.setEnd2(end2_v);
                                match.setEvent(event_v);
                                match.setPhase(phase_v);

                                match.setMatchType(type);
                                match.setMax_opb(max_opb);
                                match.setInnings(innings);
                                match.setTotalInnings(totalInnings);

//                                match.setPlayer(playr);
                                match.setPlayerA(playrA);
                                match.setPlayerB(playrB);
//                                match.setSubst(sub);
//                                match.setSubstA(sub);
//                                match.setSubstB(sub);
                                match.setOver(over);
                                match.setBalls(balls);
                                match.setActual_over(over);

                                match.setWiderun(wideRun);
                                match.setNoballrun(noballRun);
                                match.setPenaltyrun(penaltyRun);
                                match.setLimited_overs(limited_over_flag);

                                match.setWagonwheel(wheel);

                                match.setDate(date_v);

                                match.setHundred(HUNDRED);  // Added on 24/07/2021
                                match.setMax_bpb(max_bpb); // Added on 23/07/2021

/*                                match.setUmpire1(umpire1_v);
                                match.setUmpire2(umpire2_v);
                                match.setUmpire3(umpire3_v);
                                match.setUmpire4(umpire4_v);
                                match.setMatchReferee(match_referee_v);
                                match.setScorer(scorer_v);*/

                                bgRealm.copyToRealm(match);
                                Toast.makeText(CreateMatchActivity.this,
                                        "Match created successfully", Toast.LENGTH_SHORT).show();

                                saveToSP();

                            } catch (RealmPrimaryKeyConstraintException e) {
                                Toast.makeText(getApplicationContext(),
                                        "Primary Key exists, Press Update instead", Toast.LENGTH_SHORT).show();
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

                if (!umpire1_v.matches(""))
                    saveOfficial(umpire1_v, "u1");
                if (!umpire2_v.matches(""))
                    saveOfficial(umpire2_v, "u2");
                if (!umpire3_v.matches(""))
                    saveOfficial(umpire3_v, "t");
                if (!umpire4_v.matches(""))
                    saveOfficial(umpire4_v, "f");
                if (!match_referee_v.matches(""))
                    saveOfficial(match_referee_v, "r");
                if (!scorer_v.matches(""))
                    saveOfficial(scorer_v, "s");

                //updated on 20/10/2020
                startActivity(new Intent(CreateMatchActivity.this, AddPlayersA.class));
//
                progress.dismiss();
                finish();
                postMatchDetails(matchID, false);
                // Added on 03/08/2021
                RealmResults<MatchOfficials> result_officials = realm.where(MatchOfficials.class).
                        equalTo("matchid", matchid).
                        equalTo("sync", 0).findAll();
                if (result_officials.size() > 0) {
                    postOfficialDetails();
                }
            }


            // saving match details on server


            /*}
            else {
                displayError("Empty fields", "Please enter required fields");
            }*/
        }
    }




    public void postMatchDetails(String matchID, boolean pulled) {

//        umpire1_v = "";
//        umpire2_v = "";
//        umpire3_v = "";
//        umpire4_v = "";
//        match_referee_v = "";
//        scorer_v = "";

         config = new RealmConfiguration.Builder()
                .name(AppConstants.GAME_ID + ".realm")
                .deleteRealmIfMigrationNeeded()
                .build();
        realm = Realm.getInstance(config);
        if (isNetworkAvailable()) {

            Match match = realm.where(Match.class).equalTo("matchID", matchID).findFirst();
            Log.d("create", "match : " + match);

            JSONObject matchObject = new JSONObject();
            try {
                if (match != null) {
                    matchObject.put("d4s_gameid", match.getD4s_matchid());
                    matchObject.put("d4s_userid", match.getD4s_userid());
                    matchObject.put("matchID", matchID);
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
//                    matchObject.put("players", match.getPlayer());
                    matchObject.put("teamAplayers", match.getPlayerA());
                    matchObject.put("teamBplayers", match.getPlayerB());
//                    matchObject.put("substitute_players", match.getSubst());
//                matchObject.put("substitute_players", match.getSubstA());
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
//                jsonObject.put("officials", arrayOfficials);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            JSONObject jsonfeed = new JSONObject();
            try {
                jsonfeed.put("AddMatch", matchObject);
//                jsonfeed.put("AddMatchOfficials", jsonObject);
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
                                    Log.d("create", "Create, jsonMatch : " + jsonMatch);
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

                                                    /*RealmResults<MatchOfficials> results = bgRealm.where(MatchOfficials.class).
                                                            equalTo("matchID",
                                                                    jsonMatch.getString("app_matchID")).
                                                            findAll();

                                                    if (results.size() > 0) {

                                                        for (MatchOfficials officials : results) {
                                                            officials.setSync(1);
                                                            bgRealm.copyToRealm(officials);
                                                        }
                                                    }*/

                                                }/* catch (JSONException e) {
                                                    e.printStackTrace();
                                                }*/ catch (RealmPrimaryKeyConstraintException e) {
                                                    progress.dismiss();
                                                    Toast.makeText(getApplicationContext(),
                                                            "Primary Key exists, Press Update instead",
                                                            Toast.LENGTH_SHORT).show();
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


                                    //                                    Toast.makeText(getApplicationContext(),
//                                            response.getString("message"), Toast.LENGTH_SHORT).show();


                                    //starting the profile activity

//                                        startActivity(new Intent(CreateMatchActivity.this, AddPlayers.class));
//                                        finish();*/

                                } else {
                                    //                                    Toast.makeText(getApplicationContext(),
//                                            response.getString("message"), Toast.LENGTH_SHORT).show();
                                }
                                progress.dismiss();

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


        Toast.makeText(getApplicationContext(), "Match created", Toast.LENGTH_SHORT).show();

    }



    void displayNetworkErrorMessage(){

        Log.d("disnetEmsg", "inside displayNetworkErrorMessage\n\n");

        AlertDialog alertDialog = new AlertDialog.Builder(CreateMatchActivity.this).create();
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





//    void check(){
//        if (teamA.getText().toString().matches("")) {
//            Toast.makeText(getApplicationContext(), "empty field", Toast.LENGTH_SHORT).show();
//        } else {
//
//            Toast.makeText(getApplicationContext(), teamA.getText().toString(), Toast.LENGTH_SHORT).show();
//
//        }
//    }




    void saveToSP(){

        mEditor = mPreferences.edit();
        Log.d("matchid",String.valueOf(matchid));
        Log.d("matchID",matchID);
        Log.d("teamA",team1);
        Log.d("teamB",team2);
        Log.d("Venue",venue_v);
        Log.d("event",event_v);
        Log.d("phase",phase_v);
        Log.d("type",type);
        Log.d("single",String.valueOf(single));
        Log.d("multi",String.valueOf(multi));
        Log.d("innings",innings);
        Log.d("over",String.valueOf(over));
        Log.d("ball",String.valueOf(balls));
        Log.d("playerA",String.valueOf(playrA));
        Log.d("playerB",String.valueOf(playrB));
//        Log.d("sub",String.valueOf(sub));
        Log.d("wide",String.valueOf(wideRun));
        Log.d("noball",String.valueOf(noballRun));
        Log.d("penalty",String.valueOf(penaltyRun));
        Log.d("date",date_v);
        Log.d("sp_hundred", String.valueOf(HUNDRED));

        mEditor.putInt("sp_match_id", matchid);
        mEditor.putInt("sp_game_id", gameid);
        mEditor.putString("sp_match_ID", matchID);
//        mEditor.putString("sp_token", token);
        mEditor.putString("sp_teamA", team1);
        mEditor.putString("sp_teamB", team2);
        mEditor.putInt("sp_playerA", playrA);
        mEditor.putInt("sp_playerB", playrB);
//        mEditor.putInt("sp_sub_seq",sub);
        mEditor.putString("sp_innings", innings);
        mEditor.putInt("sp_over", over);
        mEditor.putInt("sp_balls", balls);
        mEditor.putString("sp_match_type", type);
        mEditor.putInt("sp_wide_value", wideRun);
        mEditor.putInt("sp_noball_value", noballRun);
        mEditor.putInt("sp_penalty_value", penaltyRun);
        mEditor.putInt("sp_current_innings", 0);
        mEditor.putInt("sp_total_innings", totalInnings);
        mEditor.putBoolean("sp_wagon_wheel", wheel);
        mEditor.putBoolean("sp_hundred", HUNDRED);

        // Added for posting
        mEditor.putInt("sp_post", 2);

        mEditor.apply();
    }




    void changeWideRuns(){

        AlertDialog.Builder builder = new AlertDialog.Builder(CreateMatchActivity.this);
        builder.setIcon(R.drawable.ball);
        builder.setCancelable(false);
        builder.setTitle("Change Wide Run");

        final EditText input = new EditText(this);

        input.setInputType(InputType.TYPE_CLASS_NUMBER);
        input.setText(String.valueOf(wideRun));
        builder.setView(input);
//        Log.d("Test", "inside methods()");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

            //            @Override
            public void onClick(DialogInterface dialog, int which) {

                if (input.getText().equals(null)){
                    Toast.makeText(getApplicationContext(),
                            "Please enter the Run", Toast.LENGTH_SHORT).show();
                }
                else {
                    wideRun = Integer.parseInt(input.getText().toString());
//                    Log.d("Test", "BuilderText : " + builderText);
                    if (wideRun == 0){

                        AlertDialog.Builder builderInner = new AlertDialog.Builder(CreateMatchActivity.this);
                        builderInner.setIcon(R.drawable.ball);
                        builderInner.setCancelable(false);
                        dialog.dismiss();
                        builderInner.setMessage("Please enter another value for WIDE ");
//                        builderInner.setTitle("");

                        builderInner.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,int which) {
                                dialog.dismiss();
                            }
                        });
                        builderInner.show();
                        wideRun = Integer.parseInt(input.getText().toString());
                    }
                }
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        AlertDialog alert = builder.create();
        alert.show();

    }




    void changeNoballRuns(){

        AlertDialog.Builder builder = new AlertDialog.Builder(CreateMatchActivity.this);
        builder.setIcon(R.drawable.ball);
        builder.setCancelable(false);
        builder.setTitle("Change Noball Run");

        final EditText input = new EditText(this);

        input.setInputType(InputType.TYPE_CLASS_NUMBER);
        input.setText(String.valueOf(noballRun));
        builder.setView(input);
//        Log.d("Test", "inside methods()");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

            //            @Override
            public void onClick(DialogInterface dialog, int which) {

                if (input.getText().equals(null)){
                    Toast.makeText(getApplicationContext(),
                            "Please enter the Run", Toast.LENGTH_SHORT).show();
                } else {
                    noballRun = Integer.parseInt(input.getText().toString());

                    if (noballRun == 0){

                        AlertDialog.Builder builderInner = new AlertDialog.Builder(CreateMatchActivity.this);
                        builderInner.setIcon(R.drawable.ball);
                        builderInner.setCancelable(false);
                        dialog.dismiss();
                        builderInner.setMessage("Please enter another value for NOBALL ");
//                        builderInner.setTitle("");
                        builderInner.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,int which) {
                                dialog.dismiss();
                            }
                        });
//
                        builderInner.show();
                        noballRun = Integer.parseInt(input.getText().toString());
                    }
                }
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        AlertDialog alert = builder.create();
        alert.show();

    }






    void changePenaltyRuns(){

        AlertDialog.Builder builder = new AlertDialog.Builder(CreateMatchActivity.this);
        builder.setIcon(R.drawable.ball);
        builder.setCancelable(false);
        builder.setTitle("Change Penalty Run");

        final EditText input = new EditText(this);

        input.setInputType(InputType.TYPE_CLASS_NUMBER);
        input.setText(String.valueOf(penaltyRun));
        builder.setView(input);
//        Log.d("Test", "inside methods()");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

            //            @Override
            public void onClick(DialogInterface dialog, int which) {

//                displayProgress();
                if (input.getText().equals(null)){
                    progress.dismiss();
                    Toast.makeText(getApplicationContext(),
                            "Please enter the Run", Toast.LENGTH_SHORT).show();
                }

                else {
                    penaltyRun = Integer.parseInt(input.getText().toString());
//                    Log.d("Test", "BuilderText : " + builderText);


                    if (penaltyRun == 0){

                        progress.dismiss();
                        AlertDialog.Builder builderInner = new AlertDialog.Builder(CreateMatchActivity.this);
                        builderInner.setIcon(R.drawable.ball);
                        builderInner.setCancelable(false);
                        dialog.dismiss();
                        builderInner.setMessage("Please enter another value for PENALTY ");
//                        builderInner.setTitle("");

                        builderInner.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,int which) {

                                dialog.dismiss();

                            }
                        });
//
                        builderInner.show();
                        penaltyRun = Integer.parseInt(input.getText().toString());
                    }
                    else progress.dismiss();
                }
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        AlertDialog alert = builder.create();
        alert.show();

    }




    public void checkInput(){

        if ( teamA.getText().toString().matches("") || (teamB.getText().toString()).isEmpty() || (venue.getText().toString()).isEmpty() ||
                (event.getText().toString()).isEmpty() || (phase.getText().toString()).isEmpty()) {

            Toast.makeText(getApplicationContext(),
                    "One or more necessary fields are empty", Toast.LENGTH_SHORT).show();
        }
        else if ((playrA == 0) || (playrB == 0)) {// || (sub == 0) ){
            Toast.makeText(getApplicationContext(),
                    "Player count fields are empty", Toast.LENGTH_SHORT).show();
        }
        if(limited_over.isChecked()){
            if((over == 0) || (balls == 0)) {
                Toast.makeText(getApplicationContext(),
                        "One or more necessary fields are empty", Toast.LENGTH_SHORT).show();
            }
        }
    }


    void displayDetails(){

        custom_innings.setVisibility(View.VISIBLE);
        custom_players.setVisibility(View.VISIBLE);
        custom_values.setVisibility(View.VISIBLE);
//        ll_days.setVisibility(View.VISIBLE);
    }





    void hideDetails(){
        custom_innings.setVisibility(View.GONE);
        custom_players.setVisibility(View.GONE);
        custom_values.setVisibility(View.GONE);
        custom_overs.setVisibility(View.GONE);
//        ll_days.setVisibility(View.GONE); Commented (08/07/2021) for later
    }


    // Added on 23/07/2021
    void set100(){

        hideDetails();
        type = "100";
        playrA = 11;
        playrB = 11;
//        sub = 0;//5;
        over = 20;
        balls = 5;
//        totalBalls = 100;  // Added on 23/07/2021
        HUNDRED = true;  // Added on 24/07/2021
        max_bpb = 20;  // Added on 24/07/2021
        single = true;
        multi = false;
        innings = "single";
        limited_over_flag = true;
        ll_max_opb.setVisibility(View.GONE);
        ll_max_bpb.setVisibility(View.VISIBLE); // Added on 30/07/2021
        limited_over.setChecked(false);
    }



    void setT20(){

        hideDetails();
        type = "T20";
        playrA = 11;
        playrB = 11;
//        playr = 11;
//        sub = 0;//5;
        over = 20;
        balls = 6;
        max_bpb = 0;  // Added on 24/07/2021
        HUNDRED = false;  // Added on 24/07/2021
        single = true;
        multi = false;
        innings = "single";
        limited_over_flag = true;  // Added on 28/04/2021
        ll_max_opb.setVisibility(View.VISIBLE); // added on 08/07/2021
        ll_max_bpb.setVisibility(View.GONE); // Added on 30/07/2021
        limited_over.setChecked(false); // added on 08/07/2021
    }




    void setCustomType() {

        //String a ,b;

        displayDetails();

        ll_max_opb.setVisibility(View.GONE);
        ll_max_bpb.setVisibility(View.GONE); // Added on 30/07/2021

        playrA = 0;
        playrB = 0;
//        playr = 0;
//        sub = 0;
        type = "Custom";
        max_bpb = 0;  // Added on 24/07/2021
        HUNDRED = false;  // Added on 24/07/2021
        over = 1000;
        balls = 6;
        limited_over_flag = false;  // Added on 28/04/2021
//        Toast.makeText(getApplicationContext(), type, Toast.LENGTH_SHORT).show();
        limited_over.setChecked(false); // added on 08/07/2021
    }




    void setODI(){

        hideDetails();
        type = "ODI";
        playrA = 11;
        playrB = 11;
//        playr = 11;
//        sub = 0;//5;
        over = 50;
        balls = 6;
        max_bpb = 0;  // Added on 24/07/2021
        HUNDRED = false;  // Added on 24/07/2021
        single = true;
        multi = false;
        innings = "single";
        limited_over_flag = true;  // Added on 28/04/2021
        ll_max_opb.setVisibility(View.VISIBLE); // added on 08/07/2021
        ll_max_bpb.setVisibility(View.GONE); // Added on 30/07/2021
        limited_over.setChecked(false); // added on 08/07/2021
    }





    void setTest(){

        hideDetails();
        type = "Test";
        playrA = 11;
        playrB = 11;
//        playr = 11;
//        sub = 0;//5;
        over = 1000;
        balls = 6;
        max_bpb = 0;  // Added on 24/07/2021
        HUNDRED = false;  // Added on 24/07/2021
        single = false;
        multi = true;
        innings = "multi";
        limited_over_flag = false;  // Added on 28/04/2021
        ll_max_opb.setVisibility(View.GONE); // added on 08/07/2021
        ll_max_bpb.setVisibility(View.GONE); // Added on 30/07/2021
        limited_over.setChecked(false); // added on 08/07/2021
    }


    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

        if (isChecked){
            if (buttonView.getId() == R.id.checkbox_limited_overs) {
                custom_overs.setVisibility(View.VISIBLE);
                over = 0;
                balls = 0;
                Log.d("Test", "custom : " + isChecked);
                limited_over_flag = true;
                ll_max_opb.setVisibility(View.VISIBLE); // added on 08/07/2021
                ll_max_bpb.setVisibility(View.GONE); // Added on 30/07/2021
//                over = Integer.parseInt(c_overs.getText().toString());
//                balls = Integer.parseInt(c_balls.getText().toString());
            }
        }
        else {
            custom_overs.setVisibility(View.GONE);
            over = 1000;
            balls = 6;
            limited_over_flag = false;
            ll_max_opb.setVisibility(View.GONE); // added on 08/07/2021
            ll_max_bpb.setVisibility(View.GONE); // Added on 30/07/2021
        }
    }





    void getFromSP() {

        mPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        ttoken = mPreferences.getString("user_token", null);
        userId = mPreferences.getInt("sp_user_id", 0);
        d4s_userid = mPreferences.getInt("d4s_userid", 0);
        Log.d("create" , "userid : "+userId);
        Log.d("create" , "d4s_userid : "+d4s_userid);
    }


    private boolean isNetworkAvailable(){
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }




/*
    public void displayError(String title, String message){

        progress.dismiss();
        AlertDialog alertDialog = new AlertDialog.Builder(CreateMatchActivity.this).create();
        alertDialog.setIcon(R.drawable.ball);
        alertDialog.setCancelable(false);
//        alertDialog.setTitle(title);
        alertDialog.setMessage(message);
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();
    }
*/



    public void displayError(String message){

        progress.dismiss();
        AlertDialog alertDialog = new AlertDialog.Builder(CreateMatchActivity.this).create();
        alertDialog.setIcon(R.drawable.ball);
        alertDialog.setCancelable(false);
        alertDialog.setMessage(message);
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();
    }



//    @Override
//    public void onBackPressed() {
//        startActivity(new Intent(this, HomeActivity.class));
//        finish();
//    }




    void setInnings(){

        if (innings != null) {

            if ((single && !multi) || (type.matches("100")) ||
                    (type.matches("T20")) || (type.matches("ODI"))) {

                totalInnings = 2;
            }
            else if (!single && multi) {

                totalInnings = 4;
            }

            Log.e("Scoring", "setInnings, totalInnings : "+totalInnings);

        }

    }

    public void displayProgress(){

        progress = new ProgressDialog(this);
        progress.setMessage("Please wait...");
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setIndeterminate(true);
        progress.setProgress(0);
        progress.show();
    }




    private void saveOfficial(String name, String type) {

        Realm realm = null;
//        try {
         config = new RealmConfiguration.Builder()
                .name(AppConstants.GAME_ID + ".realm")
                .deleteRealmIfMigrationNeeded()
                .build();
        realm = Realm.getInstance(config);
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm bgRealm) {

                try {

                    Number num = bgRealm.where(MatchOfficials.class).max("officialID");
                    int nextId = (num == null) ? 1 : num.intValue() + 1;

                    MatchOfficials officials = bgRealm.createObject(MatchOfficials.class, nextId);
                    officials.setMatchid(matchid);
                    officials.setMatchID(matchID);
                    officials.setOfficialName(name);
                    officials.setStatus(type);
                    // added single line on 04/12/2020
                    officials.setEdit(1);

                    bgRealm.copyToRealm(officials);
                    Log.d("officials", "saveOfficial, : " + officials);

                } catch (RealmException e) {
                    Toast.makeText(getApplicationContext(),
                            " " + e, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    String checkID(String matchID) {

        Match match_test = realm.where(Match.class).equalTo("matchID", matchID).findFirst();
        if (match_test != null)
            return checkID(random.genId());
        else
            return matchID;
    }


    // Added on 03/08/2021

    private void postOfficialDetails() {

         config = new RealmConfiguration.Builder()
                .name(AppConstants.GAME_ID + ".realm")
                .deleteRealmIfMigrationNeeded()
                .build();
        realm = Realm.getInstance(config);
        if (isNetworkAvailable()) {

            RealmResults<MatchOfficials> results = realm.where(MatchOfficials.class).
                    equalTo("matchid", matchid).
                    equalTo("sync", 0).findAll();

            Log.d("ADD_OFFICIALS", "Scoring, results 1 : " + results);
            if (results.isEmpty()) {

                Log.d("ADD_OFFICIALS", "Scoring, results : " + results);
            }

            else {

                JSONArray arrayOfficials = new JSONArray();

                Log.d("ADD_OFFICIALS", "Scoring, matchID : " + matchID);

                for (MatchOfficials officials : results) {

                    JSONObject jsonOfficials = new JSONObject();

                    try {
                        if (!officials.getOfficialName().matches("")) {
                            jsonOfficials.put("name", officials.getOfficialName());

                            if (officials.getStatus().matches("u1") || officials.getStatus().matches("u2"))
                                jsonOfficials.put("type", "u");
                            else
                                jsonOfficials.put("type", officials.getStatus());

                            if (officials.getD4s_id() == 0)
                                jsonOfficials.put("d4s_playerid", 0);
                            else
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
                                    // added on 14/09/2020
                                    if (!response.getBoolean("error") && response.getInt("status") == 200) {

                                        JSONObject jsonMatch = response.getJSONObject("match");
                                        Log.d("Create", "pod, jsonMatch = " + jsonMatch);

                                        // Added on 10/11/2021
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
//                                                                            equalTo("status", "u1").or().equalTo("status", "u2").
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
                                        // till here

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
}
