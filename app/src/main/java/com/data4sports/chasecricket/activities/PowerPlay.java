package com.data4sports.chasecricket.activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.data4sports.chasecricket.R;
import com.data4sports.chasecricket.applicationConstants.AppConstants;
import com.data4sports.chasecricket.models.Constants;
import com.data4sports.chasecricket.models.Events;
import com.data4sports.chasecricket.models.Match;
import com.data4sports.chasecricket.models.MyApplicationClass;
import com.data4sports.chasecricket.models.Power;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;
import io.realm.Sort;
import io.realm.exceptions.RealmException;
import io.realm.exceptions.RealmPrimaryKeyConstraintException;

public class PowerPlay extends AppCompatActivity  {

    EditText et_start, et_end, pop_start, pop_end;
    TextView pop_ok, pop_cancel;
    Button btn_set_pp, btn_pptype;
    TableLayout pp_table;
    LinearLayout linearLayout;

    int matchid, innings, id, count = 0, /*matchOver = 0,*/ checkedItem =-1, pos1 = -1, pos2 = -1,
            eventId = 0, i = 0, ballType = -1;
    float matchOver = 0f;
    ArrayList<Integer> p_id_list;
    String matchID, matchType, /*ppType = null,*/ t_type = "";
    boolean single = false, multi = false, update = false, delete = false, HUNDRED = false;
    JSONArray jsonEventArray;

    Realm realm;
    ImageButton back;
    PopupWindow popupPowerplay;

    RealmConfiguration config;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.power_play);

         config = new RealmConfiguration.Builder()
                .name(AppConstants.GAME_ID + ".realm")
                .deleteRealmIfMigrationNeeded()
                .build();
        realm = Realm.getInstance(config);
        getFromIntent();

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.custom_action_bar_layout);
        View view =getSupportActionBar().getCustomView();

        back = (ImageButton) view.findViewById(R.id.action_bar_back);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*finish();*/
                Log.d("scoring", "oncreate, back button pressd");

//                onBackPressed();

                // added on 09/02/2021
                if (ballType == -1) {
                    Events lastEvents = realm.where(Events.class).equalTo("eventID", eventId).findFirst();
                    if (lastEvents != null) {

                        if (!realm.isInTransaction()) {
                            realm.beginTransaction();
                        }
                        lastEvents.deleteFromRealm();
                        realm.commitTransaction();
                    }
                }
                // ========== till here
//                startActivity(new Intent(PowerPlay.this, ScoringActivity.class));     Commented on 28/07/2021
                startActivity(new Intent(PowerPlay.this, UpdatedScoringActivity.class));    // Added on 28/07/2021
            }
        });

        linearLayout = findViewById(R.id.ll_powerplay);
//        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        jsonEventArray = new JSONArray();
        p_id_list = new ArrayList<>();

        et_start = findViewById(R.id.edit_start_pp);
        et_end = findViewById(R.id.edit_end_pp);
        btn_set_pp = findViewById(R.id.btn_set_pp);
        btn_pptype = findViewById(R.id.btn_pptype);







        /*if (matchType.matches("T20"))
            btn_pptype.setVisibility(View.INVISIBLE);
        else
            btn_pptype.setVisibility(View.VISIBLE);*/

//        postJSON();

        pp_table = findViewById(R.id.pp_table);
        TableRow rowh = new TableRow(this);
        rowh.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, 50));
        rowh.setPadding(0, 20, 0, 20);

        TextView tvh1 = new TextView(this);
        tvh1.setTextSize(16);
        tvh1.setTextColor(Color.BLACK);
        tvh1.setText("\tStart");
        rowh.addView(tvh1);

        TextView tvh2 = new TextView(this);
        tvh2.setTextSize(16);
        tvh2.setTextColor(Color.BLACK);
        tvh2.setText("\t\t\tEnd");
        rowh.addView(tvh2);

        /*TextView tvh3 = new TextView(this);
        tvh3.setTextSize(16);
        tvh3.setTextColor(Color.BLACK);
        tvh3.setText("  \t\t\tType");
        rowh.addView(tvh3);*/

        /*View v = new View(this);
        v.setLayoutParams(new TableRow.LayoutParams( TableLayout.LayoutParams.MATCH_PARENT, 3));
        v.setBackgroundColor(Color.DKGRAY);
        rowh.addView(v);*/

        pp_table.addView(rowh);

        // divider
        /*TableRow row2 = new TableRow(this);
        row2.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, 3));
        row2.setPadding(0, 10, 0, 10);

        View v1 = new View(this);
        v1.setLayoutParams(new TableRow.LayoutParams( TableLayout.LayoutParams.MATCH_PARENT, 3));
        v1.setBackgroundColor(Color.DKGRAY);
        row2.addView(v1);
        pp_table.addView(row2);*/



        displayPPList();


        /*btn_pptype.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Creating the instance of PopupMenu
                PopupMenu popup = new PopupMenu(PowerPlay.this, btn_pptype);
                //Inflating the Popup using xml file
                popup.getMenuInflater().inflate(R.menu.popup_powerplay, popup.getMenu());

                //registering popup with OnMenuItemClickListener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        ppType = item.getTitle().toString();
                        Toast.makeText(getApplicationContext(),
                                ppType+" is selected", Toast.LENGTH_SHORT).show();
                        Log.d("power", "btn_ppType : " + ppType);

                        return true;
                    }
                });

                popup.show();//showing popup menu
            }
        });*/





        btn_set_pp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!(et_start.getText().toString().matches("")) || /*&&*/
                        !(et_end.getText().toString().matches(""))
                        /*&& ((ppType != null) || matchType.matches("T20"))*/) {

                    final int start = Integer.parseInt(et_start.getText().toString());
                    final int end = Integer.parseInt(et_end.getText().toString());
//                    Power power = realm.where(Power.class).
//                            equalTo("matchid", matchid).
//                            equalTo("innings", innings).findAll().last();

                    RealmResults<Power> results = realm.where(Power.class).
                            equalTo("matchid", matchid).
                            equalTo("innings", innings).findAll();

                    Log.d("power", "results : " + results);
                    Log.d("power", "count : " + count);
                    Log.d("power", "matchType : " + matchType);
                    Log.d("power", "start : " + start + " end: " + end);
//                    Log.d("power", "ppType : " + ppType);


                    Power power = new Power();
                    if (results.isEmpty())
                        Log.d("power", "results : " + results);
                    else {
                        power = results.last();
                        if (start < power.getEnd()) {

                            displayAlert("Powerplay overs must be in increasing order");
                            et_start.setText("");
                            et_end.setText("");
                            /*ppType = "";*/
                        }

//                            Toast.makeText(getApplicationContext(),
//                                    "Powerplay overs must be in increasing order", Toast.LENGTH_SHORT).show();
                    }

                    // Added on 28/07/2021

                    if (HUNDRED) {

                        if (start == end) {
                            displayAlert("Starting and ending overs can not be same");
                            et_start.setText("");
                            et_end.setText("");
                        } else {
                            ++count;
                            if (count > 1) {

                                AlertDialog powerPlayDialog = new AlertDialog.Builder(PowerPlay.this).create();
                                powerPlayDialog.setIcon(R.drawable.ball);
                                powerPlayDialog.setCancelable(false);
                                if (matchType.matches("T20"))
                                    powerPlayDialog.setTitle("T20 match can have only one PowerPlay");
                                else if (matchType.matches("100"))
                                    powerPlayDialog.setTitle("100s match can have only one PowerPlay");
                                powerPlayDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {

                                                dialog.dismiss();
                                            }
                                        });
                                powerPlayDialog.show();

                            } else if (count == 1)
                                addPowerPlay(start, end);
                        }

                    } else {

                        // === till here
                        if ((start > -1) && (end > 0 && end <= matchOver) /*&& (ppType != null || matchType.matches("T20"))*/) {

                            if (start == end) {

                                displayAlert("Starting and ending overs can not be same");
                                et_start.setText("");
                                et_end.setText("");
//                            ppType = "";
//                            Toast.makeText(getApplicationContext(),
//                                    "Starting and ending overs can not be same", Toast.LENGTH_SHORT).show();
//                            et_start.setText("");
//                            et_end.setText("");
                            }

//                        else if (power != null) {
//                            if (start < power.getEnd())
//                                Toast.makeText(getApplicationContext(),
//                                    "Powerplay overs must be in increasing order", Toast.LENGTH_SHORT).show();
//                        }

                            else {

                                ++count;

                                Log.d("power", "inside else, count : " + count + ", matchType : " + matchType);
                                if (matchType.matches("ODI")) {


                                    if (count > 3) {

                                        AlertDialog powerPlayDialog = new AlertDialog.Builder(PowerPlay.this).create();
                                        powerPlayDialog.setIcon(R.drawable.ball);
                                        powerPlayDialog.setCancelable(false);
                                        powerPlayDialog.setTitle("ODI match can not have more than 3 PowerPlays");
                                        powerPlayDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK",
                                                new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int which) {

                                                        dialog.dismiss();
                                                    }
                                                });
                                        powerPlayDialog.show();

                                    } else if (count > 0) {

                                        addPowerPlay(start, end);

                                    /*// commented on 08/05/2020
                                    if (matchOver == 50) {

                                        if (count == 1) {

                                            if ((start >= 0 && start <= 10) && (end >= 0 && end <= 10) && (end > start)) {

                                                addPowerPlay(start, end*//*, ppType*//*);
                                            } else {

                                                powerPlayError(count, 0, 10);
                                            }
                                        } else if (count == 2) {

                                            if ((start >= 11 && start <= 40) && (end >= 11 && end <= 40)) {

                                                addPowerPlay(start, end*//*, ppType*//*);
                                            } else {

                                                powerPlayError(count, 11, 40);
                                            }
                                        } else if (count == 3) {

                                            if ((start >= 41 && start <= 50) && (end >= 41 && end <= 50)) {

                                                addPowerPlay(start, end, ppType);
                                            } else {

                                                powerPlayError(count, 40, 50);
                                            }

                                        }
                                    }

                                    else
                                        addPowerPlay(0, matchOver*//*, ppType*//*);*/
                                    }


                                } else if ((matchType.matches("T20")) || (matchType.matches("100"))) {


                                    if (count > 1) {


                                        AlertDialog powerPlayDialog = new AlertDialog.Builder(PowerPlay.this).create();
                                        powerPlayDialog.setIcon(R.drawable.ball);
                                        powerPlayDialog.setCancelable(false);
                                        if (matchType.matches("T20"))
                                            powerPlayDialog.setTitle("T20 match can have only one PowerPlay");
                                        else if (matchType.matches("100"))
                                            powerPlayDialog.setTitle("100s match can have only one PowerPlay");
                                        powerPlayDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK",
                                                new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int which) {

                                                        dialog.dismiss();
                                                    }
                                                });
                                        powerPlayDialog.show();

                                    } else if (count == 1) {

                                        addPowerPlay(start, end);

                                    /*// commented on 08/05/2020
                                    if ((start >= 0 && start <= 6) && (end >= 1 && end <= 6)) {

                                        addPowerPlay(start, end, "");
                                    } else {

                                        powerPlayError(count, 0, 6);
                                    }*/
                                    }
                                } else
                                    addPowerPlay(start, end/*, ppType*/);

                            }

                            et_start.setText("");
                            et_end.setText("");
                        } else {
                            displayAlert("End over must be less than match over");
                        }
                    }
                }

                else {

                    Toast.makeText(getApplicationContext(), "Please enter a valid number", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }


    void getFromIntent() {



        Intent i = getIntent();
        matchid = i.getIntExtra("matchid", 0);
        matchID = i.getStringExtra("matchID");
        innings = i.getIntExtra("innings", 0);
        matchType = i.getStringExtra("match_type");
//        matchOver = i.getFloatExtra("match_over", 0);
        eventId = i.getIntExtra("eventId", 0);

//        Bundle b = getIntent().getExtras();
//        String Array=b.getString("Array");

        /*  commented on 16/10/2020
        String jsonArray = i.getStringExtra("jsonArray");
        try {
            jsonEventArray = new JSONArray(jsonArray);
            Log.e("pp", "getFromIntent, jsonEventArray");
        } catch (JSONException e) {
            e.printStackTrace();
        }*/

        Match match = realm.where(Match.class).equalTo("matchid", matchid).findFirst();
        matchOver = match.getOver();
        HUNDRED = match.isHundred();

    }


    public void addPowerPlay(int start, int end) {

        ++i;

        Log.d("power", "addPowerPlay");
        Number num = realm.where(Power.class).max("id");
        id = (num == null) ? 1 : num.intValue() + 1;

        p_id_list.add(id);

        TableRow row1 = new TableRow(this);
        row1.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, 50));
        row1.setPadding(0, 25, 0, 25);

        TextView tv11 = new TextView(this);
        tv11.setTextSize(16);
        tv11.setTextColor(Color.BLACK);
        tv11.setText("  \t" + start);
        row1.addView(tv11);

        TextView tv12 = new TextView(this);
        tv12.setTextSize(16);
        tv12.setTextColor(Color.BLACK);
        tv12.setText("  \t\t\t" + end);
        row1.addView(tv12);

/*        TextView tv13 = new TextView(this);
        tv13.setTextSize(16);
        tv13.setTextColor(Color.BLACK);
        tv13.setText("  \t\t\t" + ppType);
        row1.addView(tv13);*/

        TextView tv14 = new TextView(this);
        tv14.setTextSize(16);
        tv14.setText("  \t\t\t");
        row1.addView(tv14);

        ImageView edit = new ImageView(this);
        edit.setImageResource(R.drawable.edit);
//        edit.setColorFilter(Color.GREEN);
        edit.setColorFilter(getResources().getColor(R.color.colorText)); // Add tint color
        edit.setId(id);
        edit.setOnClickListener(editListener);
        row1.addView(edit);

        View v = new View(this);
        v.setLayoutParams(new TableRow.LayoutParams( 30, TableLayout.LayoutParams.MATCH_PARENT));
        row1.addView(v);

        ImageView delete= new ImageView(this);
        delete.setImageResource(R.drawable.delete);
//        delete.setColorFilter(Color.RED);
        delete.setColorFilter(getResources().getColor(R.color.colorDelete)); // Add tint color
        delete.setId(id);
        delete.setOnClickListener(mListener);
        row1.addView(delete);

        /*View v = new View(this);
        v.setLayoutParams(new TableRow.LayoutParams( TableLayout.LayoutParams.MATCH_PARENT, 3));
        v.setBackgroundColor(Color.DKGRAY);
        row1.addView(v);*/

        pp_table.addView(row1);

        // divider
        /*TableRow row2 = new TableRow(this);
        row2.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, 3));
        row2.setPadding(0, 10, 0, 10);

        View v1 = new View(this);
        v1.setLayoutParams(new TableRow.LayoutParams( TableLayout.LayoutParams.MATCH_PARENT, 3));
        v1.setBackgroundColor(Color.DKGRAY);
        row2.addView(v1);
        pp_table.addView(row2);*/

        saveToDB(id, start, end, /*ppType,*/ false, true);


    }



    private View.OnClickListener editListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {


            Log.d("power", "edit");
            Log.d("power", "edit, matchid : " + matchid);
            Log.d("power", "edit, innings : " + innings);
            Log.d("power", "edit, v.getId() : " + v.getId());


             config = new RealmConfiguration.Builder()
                .name(AppConstants.GAME_ID + ".realm")
                .deleteRealmIfMigrationNeeded()
                .build();
        realm = Realm.getInstance(config);

            Power power = realm.where(Power.class).
                    equalTo("matchid", matchid).
                    equalTo("innings", innings).
                    equalTo("id", v.getId()).findFirst();

            Log.d("power", "edit, power 1 : " + power);
            if (power != null) {

                Log.d("power", "edit, power 2 : " + power);
                editPowerPlay(power);

            }
        }
    };


    void editPowerPlay(Power power) {

        Log.d("power", "editPowerPlay, power  : " + power);
        if (power != null) {

            // added on 30/05/2020
            View editPowerPlayView = View.inflate(this, R.layout.editpowerplay, null);
            EditText et_start = (EditText) editPowerPlayView.findViewById(R.id.edit_pp_start);
            EditText et_end = (EditText) editPowerPlayView.findViewById(R.id.edit_pp_end);

            et_start.setText(String.valueOf(power.getStart()));
            et_end.setText(String.valueOf(power.getEnd()));

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setIcon(R.drawable.ball).
                    setTitle("Edit powerplay").
                    setView(editPowerPlayView).
                    setCancelable(false).
                    setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            Log.d("editpp", "editPowerPlay, et_start.getText().toString() : "
                                    + et_start.getText().toString());

                            Log.d("editpp", "editPowerPlay, et_start.getText() : "
                                    + et_start.getText());

                            if (!(et_start.getText().toString().matches(""))
                                    && !(et_end.getText().toString().matches(""))) {
                                final int t_start = Integer.parseInt(et_start.getText().toString());
                                final int t_end = Integer.parseInt(et_end.getText().toString());

                                if (t_start > -1 && t_end > 0 && (t_end > t_start)) {

                                    checkType(t_start, t_end, power);
                                }
                            }

                            else
                                displayAlert("Please enter valid input");
                        }
                    }).
                    setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    }).
                    show();
        }

        else
            Log.d("power", "editPowerPlay, power 2 : " + power);
    }


    void checkType(int start, int end, Power power) {


        /*AlertDialog.Builder powerBuilder = new AlertDialog.Builder(PowerPlay.this);
        powerBuilder.setIcon(R.drawable.ball);
        powerBuilder.setCancelable(false);
        powerBuilder.setTitle("Powerplay Type");
        powerBuilder.setSingleChoiceItems(R.array.powerplayType, checkedItem , new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                switch (which){
                    case 0: // Batting
                        t_type = "Batting";
                        break;

                    case 1:
                        // Fielding
                        t_type = "Fielding";
                        break;

                    default:
                        break;
                }
            }
        });


        powerBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();*/

                if (start == power.getStart()) {
                    if (end == power.getEnd()){
                        /*if (t_type.matches(power.getType())) {
                            popupPowerplay.dismiss();*/
                            displayAlert("Powerplay already exists");
                        /*}
                        else {
                            saveToDB(power.getId(), start, end, *//*t_type,*//* true, false);
                            refreshTable();
                            displayPPList();
                        }*/

                    }
                     else {

                         Power power1 = realm.where(Power.class).
                                 equalTo("matchid", matchid).
                                 equalTo("innings", innings).
                                 equalTo("id", power.getId() + 1).findFirst();

                         if (power1 == null) {
                             saveToDB(power.getId(), start, end, /*t_type,*/ true, false);
                             refreshTable();
                             displayPPList();
                         }
                         else  {

                             if (end < power1.getStart()) {
                                 saveToDB(power.getId(), start, end, /*t_type,*/ true, false);
                                 refreshTable();
                                 displayPPList();
                             }
                             else
                                 displayAlert("End value is greater than next powerplay");
                         }
                    }

                }

                else {

                    // need to check more


                    /*Power power1 = realm.where(Power.class).
                            equalTo("matchid", matchid).
                            equalTo("innings", innings).
                            equalTo("id", power.getId() + 1).findFirst();

                    Power power2 = realm.where(Power.class).
                            equalTo("matchid", matchid).
                            equalTo("innings", innings).
                            equalTo("id", power.getId() - 1).findFirst();

                    if (power1 == null) {*/
                        saveToDB(power.getId(), start, end, /*t_type,*/ true, false);
                        refreshTable();
                        displayPPList();

                    /*}  else {

                    }*/
                }



               /* popupPowerplay.dismiss();



            }
        });

        powerBuilder.setNegativeButton("Cancel", null);

        // create and show the alert dialog
        AlertDialog dialog = powerBuilder.create();
        dialog.show();*/
    }


    private View.OnClickListener mListener = new View.OnClickListener() {

        @SuppressLint("SuspiciousIndentation")
        @Override
        public void onClick(View v) {

             config = new RealmConfiguration.Builder()
                .name(AppConstants.GAME_ID + ".realm")
                .deleteRealmIfMigrationNeeded()
                .build();
        realm = Realm.getInstance(config);

            Log.d("power", "delete");
            Log.d("power", "delete, matchid : " + matchid);
            Log.d("power", "delete, innings : " + innings);
            Log.d("power", "delete, v.getId() : " + v.getId());

            Power power = realm.where(Power.class).
                    equalTo("matchid", matchid).
                    equalTo("innings", innings).
                    equalTo("id", v.getId()).findFirst();

            AlertDialog alertDialog = new AlertDialog.Builder(PowerPlay.this).create();
            alertDialog.setIcon(R.drawable.ball);
            alertDialog.setCancelable(false);
            alertDialog.setTitle("Do you want to delete powerplay (" + power.getStart() + " - " + power.getEnd() + ")");
            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Confirm",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();

                            Log.e("power", "selected, power : "+power);

                            if (power != null){

                                int start = power.getStart();
                                int end = power.getEnd();
                                int sequ = power.getCount();

                                //commented on 29/05/2020
                                /*if (!realm.isInTransaction()) {
                                    realm.beginTransaction();
                                }

                                power.deleteFromRealm();
                                realm.commitTransaction();*/

                                // added on 29/05/2020
                                deletePower(sequ);
                                saveEvent(v.getId(), false, false, true, start, end, sequ);

                            }

                            --count;



                            refreshTable();
                            displayPPList();
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


//            TableRow tr = (TableRow) v.getParent();
//            tr.removeView(v);

        }

    };





    public void saveToDB(int id, int start, int end, /*String ppType,*/ boolean edit, boolean new_pp){

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

//                        Number num = bgRealm.where(Match.class).max("matchid");
//                        int nextId = (num == null) ? 1 : num.intValue() + 1;

                        if (edit) {

                            Power power = bgRealm.where(Power.class).
                                    equalTo("matchid", matchid).
                                    equalTo("id", id).findFirst();

                            power.setStart(start);
                            power.setEnd(end);
//                            power.setType(ppType);
                            power.setSync(0);

                            bgRealm.copyToRealmOrUpdate(power);
                            Log.d("power", "edit, saveToDB : " + power);
                        }

                        else {
                            Power power = bgRealm.createObject(Power.class, id);

                            power.setMatchid(matchid);
                            power.setMatchID(matchID);
                            power.setInnings(innings);
                            power.setStart(start);
                            power.setEnd(end);
                            power.setCount(count);  // sequence
//                            power.setType(ppType);

                            bgRealm.copyToRealm(power);
                            Log.d("power", "new, saveToDB : " + power);
                        }



                    } catch (RealmPrimaryKeyConstraintException e) {
                        Toast.makeText(getApplicationContext(),
                                "Primary Key exists, Press Update instead", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

        catch (RealmException e) {
            Log.d("test", "Exception : " + e);
        }

        finally {
            if (realm != null) {
                realm.close();
            }
        }

        saveEvent(id, new_pp, edit, false, start, end, count);

//        postJSON();
    }




    void saveEvent(int pp_id, boolean new_pp, boolean updated_pp, boolean deleted_pp,
                   int start, int end, int sequence) {

        // commented on 14/05/2020
        /*Intent i = new Intent(PowerPlay.this, ScoringActivity.class);
        i.putExtra("matchid", matchid);
        i.putExtra("matchID", matchID);
        i.putExtra("status", "resume");
//        i.putExtra("array_p_id", p_id_list);
        i.putExtra("p_id", pp_id);
        if (new_pp)
            i.putExtra("ballType", 21);
        else if (updated_pp)
            i.putExtra("ballType", 22);
        else if (deleted_pp)
            i.putExtra("ballType", 23);
        i.putExtra("power", true);
        startActivity(i);*/


        // added on 14/05/2020

//        int ballType = 0;     Commented on 08/02/2021

        if (new_pp)
             ballType = 21;
        else if (updated_pp)
            ballType = 22;
        else if (deleted_pp)
            ballType = 23;

        Realm realm = null;
        try {
             config = new RealmConfiguration.Builder()
                .name(AppConstants.GAME_ID + ".realm")
                .deleteRealmIfMigrationNeeded()
                .build();
        realm = Realm.getInstance(config);
            int finalBallType = ballType;
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {

                    try {

                        Log.d("poerplay", "finalBallType  : " + finalBallType);
                        Events events = realm.where(Events.class).equalTo("eventID", eventId).findFirst();
                                //equalTo("innings", innings).findAll().last();
                        Log.d("eventId", "b4, updatePowerplay,   : "+ eventId);
                        Log.d("eventId", "b4, updatePowerplay, events  : "+ events);
                        events.setBallType(finalBallType);
                        events.setPower_id(pp_id);
                        events.setPower_start_over(start);
                        events.setPower_end_over(end);
                        events.setPower_sequence(sequence);
                        realm.copyToRealmOrUpdate(events);
                        Log.d("powerplay", "after, events : " + events);
                    } catch (RealmPrimaryKeyConstraintException e) {
                        Toast.makeText(getApplicationContext(),
                                "Primary Key exists, Press Update instead", Toast.LENGTH_SHORT).show();
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

        /*Intent intent = new Intent(getBaseContext(), ScheduledService.class);
        getBaseContext().startService(intent);*/

        // added on 18/05/2020
//        Intent i = new Intent(PowerPlay.this, ScoringActivity.class);     Commented on 28/07/2021
        Intent i = new Intent(PowerPlay.this, UpdatedScoringActivity.class);  // Added on 28/07/2021
        i.putExtra("matchid", matchid);
        i.putExtra("matchID", matchID);
        i.putExtra("status", "resume");
//        i.putExtra("array_p_id", p_id_list);
        /*i.putExtra("p_id", pp_id);
        if (new_pp)
            i.putExtra("ballType", 21);
        else if (updated_pp)
            i.putExtra("ballType", 22);
        else if (deleted_pp)
            i.putExtra("ballType", 23);*/
        i.putExtra("power", true);

        freeMemory();       // added on 04/06/2020
        startActivity(i);

    }





    public void powerPlayError(int counts, int startLimit, int endLimit){

        String message = "", part = "";

        if (counts == 1)
            part = "1st";
        else if (counts == 2)
            part = "2nd";
        else if (counts == 3)
            part = "3rd";

        if (matchType.matches("ODI"))
            message = "Starting and ending over of "+ part +" PowerPlay is "+ startLimit +" and "+ endLimit;
        else if ((matchType.matches("T20")) || (matchType.matches("100")))
                message = "Powerplay is already setted";
//        message = matchType +
        AlertDialog powerPlayErrorDialog = new AlertDialog.Builder(PowerPlay.this).create();
        powerPlayErrorDialog.setIcon(R.drawable.ball);
        powerPlayErrorDialog.setCancelable(false);
        powerPlayErrorDialog.setTitle(message);
        powerPlayErrorDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();
                        --count;
                    }
                });
        powerPlayErrorDialog.show();
    }



    public void refreshTable() {

        int row_count = realm.where(Power.class).
                equalTo("matchid", matchid).
                equalTo("innings", innings).findAll().size();

        Log.d("power", "refreshTable, count : " + row_count);

        if (row_count > 0) {

            while (pp_table.getChildCount() > 1) {
                TableRow tableRow = (TableRow) pp_table.getChildAt(1);
                pp_table.removeView(tableRow);
            }
        }
    }

    public void displayPPList(){


        RealmResults<Power> results = realm.where(Power.class).
                equalTo("matchid", matchid).
                equalTo("innings", innings).
                sort("count", Sort.ASCENDING).findAll();

        Log.e("power", "displayPPList, results : "+results);

        if (results != null){

            for (Power power : results) {

                TableRow row1 = new TableRow(this);
                row1.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, 50));
                row1.setPadding(0, 25, 0, 25);

                TextView tv11 = new TextView(this);
                tv11.setTextSize(16);
                tv11.setTextColor(Color.BLACK);
                tv11.setText("  \t" + power.getStart());
                row1.addView(tv11);

                TextView tv12 = new TextView(this);
                tv12.setTextSize(16);
                tv12.setTextColor(Color.BLACK);
                tv12.setText("  \t\t\t" + power.getEnd());
                row1.addView(tv12);

                /*TextView tv13 = new TextView(this);
                tv13.setTextSize(16);
                tv13.setTextColor(Color.BLACK);
                tv13.setText("  \t\t\t" + power.getType());//ppType);
                row1.addView(tv13);*/

                TextView tv14 = new TextView(this);
                tv14.setTextSize(16);
                tv14.setText(" \t\t\t");
                row1.addView(tv14);


                ImageView edit = new ImageView(this);
                edit.setImageResource(R.drawable.edit);
//                edit.setColorFilter(Color.GREEN);
                edit.setColorFilter(getResources().getColor(R.color.colorText)); // Add tint color
                edit.setId(power.getId());
                edit.setOnClickListener(editListener);
                row1.addView(edit);


                View v = new View(this);
                v.setLayoutParams(new TableRow.LayoutParams( 30, TableLayout.LayoutParams.MATCH_PARENT));
                row1.addView(v);


                ImageView delete = new ImageView(this);
                delete.setImageResource(R.drawable.delete);
                delete.setId(power.getId());
//                delete.setColorFilter(Color.RED);
                delete.setColorFilter(getResources().getColor(R.color.colorDelete)); // Add tint color
                delete.setOnClickListener(mListener);
                row1.addView(delete);

               /* View v = new View(this);
                v.setLayoutParams(new TableRow.LayoutParams( TableLayout.LayoutParams.MATCH_PARENT, 3));
                v.setBackgroundColor(Color.DKGRAY);
                row1.addView(v);*/

                pp_table.addView(row1);

                // divider
                /*TableRow row2 = new TableRow(this);
                row2.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, 3));
                row2.setPadding(0, 10, 0, 10);

                View v1 = new View(this);
                v1.setLayoutParams(new TableRow.LayoutParams( TableLayout.LayoutParams.MATCH_PARENT, 3));
                v1.setBackgroundColor(Color.DKGRAY);
                row2.addView(v1);
                pp_table.addView(row2);*/


                count = power.getCount();

            }

//            ++ count;

        }

    }


    private void postJSON(){

        if (isNetworkAvailable()) {

            // new updation

            RealmResults<Power> results = realm.where(Power.class).
                    equalTo("matchid", matchid).
                    equalTo("innings", innings).findAll();

            if (results.isEmpty()) {


            } else {

                JSONArray array = new JSONArray();

                JSONObject jsonMatch = new JSONObject();
                try {
                    jsonMatch.put("matchID", matchID);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

//                array.put(jsonMatch);

                Log.d("power", "postJSON, results : " + results);
                for (Power power : results) {

                    Log.d("power", "postJSON, power : " + power);

                    if (power.getSync() == 0) {

                        JSONObject jsonObject = new JSONObject();
                        try {

                            jsonObject.put("start_over", power.getStart());
                            jsonObject.put("end_over", power.getEnd());
                            jsonObject.put("type", null);//power.getType());
                            jsonObject.put("innings", power.getInnings());
                            jsonObject.put("sequence", power.getCount());
                            array.put(jsonObject);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }

//                JSONObject json = new JSONObject();
//                try {
//                    json.put("PowerPlay", array);
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }




                /*// old
                JSONObject jsonPower = new JSONObject();
                try {
                    jsonPower.put("matchID", matchID);
                    jsonPower.put("innings", innings);
                    jsonPower.put("start_over", start);
                    jsonPower.put("end_over", end);
                    jsonPower.put("type", ppType);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                JSONArray arrayPower = new JSONArray();
                arrayPower.put(jsonPower);

                JSONObject jsonEvent = new JSONObject();
                try {
                    jsonEvent.put("PowerPlay", arrayPower);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                jsonEventArray.put(jsonEvent);*/

                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("matchID", matchID);
                    jsonObject.put("powerplay", array);
                } catch (JSONException e) {
                    e.printStackTrace();
                }


                JSONObject jsonFeed = new JSONObject();
                try {
//                    jsonFeed.put("AddBBB", jsonEventArray);
                    jsonFeed.put("PowerPlay", jsonObject);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.e("scoring", "jsonFeed : " + jsonFeed);


                JSONObject postparams = new JSONObject();
                try {
                    postparams.put("title", "CHASE_POST");
                    postparams.put("feed", jsonFeed);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Log.d("power", "postparams : " + postparams);
                JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                        Constants.CHASE_CRICKET_MATCH_API, postparams,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {


                                Log.d("power", "response : " + response);

                                //if no error in response


                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {

//                            Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();


                                Log.d("power", "Error Message is  : " + error.getMessage());
//                        Toast.makeText(getApplicationContext(), "Invalid Username or Password", Toast.LENGTH_SHORT).show();
//                            displayNetworkErrorMessage();

                            }
                        });

                MyApplicationClass.getInstance(getApplicationContext()).addToRequestQueue(jsonObjReq, "postRequest");
                Log.d("power", "jsonObjReq  : " + jsonObjReq);
                Log.d("power", "postparams  : " + postparams);
            }
        }



//        startActivity(new Intent(PowerPlay.this, ScoringActivity.class));

    }



    private boolean isNetworkAvailable(){
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }



    public void displayAlert(String message) {

        AlertDialog messageAlert = new AlertDialog.
                Builder(PowerPlay.this).create();
        messageAlert.setIcon(R.drawable.ball);
        messageAlert.setCancelable(false);
        messageAlert.setMessage(message);
        messageAlert.setButton(AlertDialog.BUTTON_POSITIVE, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        et_start.setText("");
                        et_end.setText("");

                    }

                });

        messageAlert.show();
    }




    void deletePower(int sequence) {


        Number num = realm.where(Power.class).
                equalTo("matchid", matchid).
                equalTo("innings", innings).
                max("count");

        int maxSeq = num.intValue();

        Power power = realm.where(Power.class).
                equalTo("matchid", matchid).
                equalTo("innings", innings).
                equalTo("count", sequence).findFirst();

        if (power != null) {

            if (!realm.isInTransaction()) {
                realm.beginTransaction();
            }

            power.deleteFromRealm();
            realm.commitTransaction();
        }


        if (sequence == maxSeq) {

            // do nothing, becuase the deleted powerplay is the last one in that innings
        }

        else {

             /*if (maxSeq == 2) {

                  power = realm.where(Power.class).
                         equalTo("matchid", matchid).
                         equalTo("innings", innings).findFirst();

                 if (power != null) {
                     power.setCount(1);
                     realm.copyToRealmOrUpdate(power);
                 }
             }

             else */
            if (sequence == 1) {


                RealmResults<Power> results = realm.where(Power.class).
                        equalTo("matchid", matchid).
                        equalTo("innings", innings).
                        sort("count", Sort.ASCENDING).findAll();

                if (results.size() > 0) {

//                   if (sequence == 1) {
                    for (int i = 2; i <= results.size() + 1; i++) {

                        power = realm.where(Power.class).
                                equalTo("matchid", matchid).
                                equalTo("innings", innings).
                                equalTo("count", i).findFirst();

                        if (power != null) {

                            Log.d("deletepp", "deleptPower 1 , i : " + i + ", power : " + power);

                            updatePower(power);
                        }
                    }
//                 }
                }
            }

            else
            if (sequence == 2) {

                RealmResults<Power> results = realm.where(Power.class).
                        equalTo("matchid", matchid).
                        equalTo("innings", innings).
                        sort("count", Sort.ASCENDING).findAll();

                if (results.size() > 0) {

                    for (int i = 3; i <= results.size() + 1; i++) {

                        power = realm.where(Power.class).
                                equalTo("matchid", matchid).
                                equalTo("innings", innings).
                                equalTo("count", i).findFirst();

                        if (i > 1) {

                            if (power != null) {

                                Log.d("deletepp", "deleptPower 2 , i : " + i + ", power : " + power);
                               updatePower(power);
                            }
                        }
                    }
                }
            }
        }
    }



    void updatePower(Power power) {

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

                        int seq = power.getCount();

                        Log.d("deletepp", "updatePower 1 , seq : " + seq + ", power : " + power);
                        --seq;
                        power.setCount(seq);

                        bgRealm.copyToRealm(power);
                        Log.d("deletepp", "updatePower 2 , seq : " + seq + ", power : " + power);

                    }  catch (RealmPrimaryKeyConstraintException e) {
                        Toast.makeText(getApplicationContext(),
                                "Primary Key exists, Press Update instead", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

        catch (RealmException e) {
            Log.d("test", "Exception : " + e);
        }

        finally {
            if (realm != null) {
                realm.close();
            }
        }
    }



    public void freeMemory() {
        System.runFinalization();
        Runtime.getRuntime().gc();
        System.gc();
    }
}