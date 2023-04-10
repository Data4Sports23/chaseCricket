package com.data4sports.chasecricket.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Region;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextPaint;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;

import com.data4sports.chasecricket.R;
import com.data4sports.chasecricket.applicationConstants.AppConstants;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class WagonWheel extends AppCompatActivity {

//    Bitmap wheel;
    Paint fillPaint, borderPaint, textPaint, selectPaint;
    TextPaint mTextPaint;
    RectF rectF;
    int x = 10, y = 10, width = 0, height = 0, eventId;
    Path path;
    Region region1, region2, region3, region4, region5, region6, region7, region8;
    boolean rg1 = false, rg2 = false, rg3 = false, save = false;
    String wwRegion = null, temp = null;
    int strokeDirection = -1;

    JSONArray jsonEventArray;

    boolean SET_OVER = false, score = false, efo = false;
    Realm realm;

    RealmConfiguration config;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(new WagonWT6(this));



        Intent i = getIntent();
        eventId = i.getIntExtra("eventId", 0);
        score = i.getBooleanExtra("score", false);
//        temp = i.getStringExtra("post_array");
//        if (temp != null) {
//            try {
//                jsonEventArray = new JSONArray(temp);
//                parseJSON(jsonEventArray);
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//        }
//        mo = i.getIntExtra("mo", mo);

        Log.e("WagonWheel", " onCreate, eventId : "+eventId);

         config = new RealmConfiguration.Builder()
                .name(AppConstants.GAME_ID + ".realm")
                .deleteRealmIfMigrationNeeded()
                .build();
        realm = Realm.getInstance(config);
    }

    protected class WagonWT6 extends View implements View.OnTouchListener{

//        private Canvas canvas;

        public WagonWT6(Context context) {
            super(context);
//            wheel = BitmapFactory.decodeResource(getResources(), R.drawable.wagonwheel);
            setBackgroundResource(R.drawable.wagon);

            path = new Path();

            fillPaint = new Paint();
            fillPaint.setStyle(Paint.Style.FILL);
            fillPaint.setColor(Color.GRAY);

            borderPaint = new Paint();
            borderPaint.setStyle(Paint.Style.STROKE);
            borderPaint.setStrokeWidth(4);
            borderPaint.setColor(Color.DKGRAY);

            textPaint = new Paint();
            textPaint.setColor(Color.BLACK);
            textPaint.setStrokeWidth(3);
            textPaint.setStyle(Paint.Style.STROKE);
            textPaint.setTextSize(40f);
            textPaint.setTextAlign(Paint.Align.CENTER);

            mTextPaint = new TextPaint();
            mTextPaint.setAntiAlias(true);
            mTextPaint.setTextSize(16 * getResources().getDisplayMetrics().density);
            mTextPaint.setColor(0xFF000000);

            selectPaint = new Paint();
            selectPaint.setStyle(Paint.Style.FILL);
            selectPaint.setColor(Color.LTGRAY);

        }


        @Override
        protected void onDraw(Canvas canvas) {

            super.onDraw(canvas);
//            this.canvas = canvas;
//            canvas.save();
//            drawShape();
//            Log.e("onDraw", " onnDraw called");
//        }
//
//
//        void drawShape(){
//            Log.e("drawShape", " drawShape called");

//            canvas.drawBitmap(wheel, 0,0,null);

            width = getWidth();
            height = getHeight() / 20;
            rectF = new RectF(x, (3.2f * y), width - (x), height * 19.8f);
//            Log.e("onDraw", "width : "+width+", height : "+height);
//            Log.e("onDraw", "rg1 : "+rg1);

//            rectF = new RectF(x, (2 * y), width - (y), height - (2 * y));


//************************************************************************************************************************************************
//            //
//            // POINT
//            // 180 to 210
//
////            canvas.drawArc(rectF, 180, 30, true, fillPaint);
//            path.arcTo(rectF, 180, 30);
//            path.lineTo(width/2f, (height/2f) - (10 * y));
//            path.lineTo(x , (height/2f) - (10 * y));
//
//
//
//            if (rg1){
//                Log.e("onDraw", "rg1 : "+rg1);
//
//                borderPaint.setColor(Color.BLUE);
////                canvas.drawPath(path, borderPaint);
////                canvas.drawPath(path, selectPaint);
////                try {
////                    wait(1500);
////                } catch (InterruptedException e) {
////                    e.printStackTrace();
////                }
//                rg1 = false;
//            }
//
////            else
//                canvas.drawPath(path, borderPaint);

//            text = "POINT";
//             RectF rectF1 = new RectF();
//            path.computeBounds(rectF1, true);
//            region1 = new Region();
//            region1.setPath(path, new Region((int) rectF1.left, (int) rectF1.top, (int) rectF1.right, (int) rectF1.bottom));
//            Log.e("onDraw", "region1 : "+region1);
////            canvas.drawText(text, rectF1.centerX() + 50, rectF1.centerY() - 50, textPaint);
//            path.reset();


//************************************************************************************************************************************************



            //
            // POINT
            // 180 to 210
            path.arcTo(rectF, 180, 30);
//            path.lineTo(width/2f, (height/2f) - (10 * y));  // center point
            path.lineTo(width/2f, (height * 8.75f));
            path.lineTo(x , (height * 8.75f));
            canvas.drawPath(path, borderPaint);
            canvas.drawPath(path, borderPaint);
            RectF rectF1 = new RectF();
            path.computeBounds(rectF1, true);
            region1 = new Region();
            region1.setPath(path, new Region((int) rectF1.left, (int) rectF1.top, (int) rectF1.right, (int) rectF1.bottom));
//            Log.e("onDraw", "region1 : "+region1);
            canvas.drawText("POINT", rectF1.centerX() - 50, rectF1.centerY() + 20, textPaint);
            path.reset();




            //
            // THIRD MAN
            // 270 to 210
            path.arcTo(rectF, 270, -60);
            path.lineTo(width/2f, (height * 8.75f));
            path.lineTo(width/2f , height);
            canvas.drawPath(path, borderPaint);
            rectF1 = new RectF();
            path.computeBounds(rectF1, true);
            region2 = new Region();
            region2.setPath(path, new Region((int) rectF1.left, (int) rectF1.top, (int) rectF1.right, (int) rectF1.bottom));
//            Log.e("onDraw", "region1 : "+region1);
            canvas.drawText("THIRD MAN", rectF1.centerX() + 50, rectF1.centerY() + 20, textPaint);
            path.reset();



            //
            // FIRST LEG
            // 270 to 330
            path.arcTo(rectF, 270, 60);
            path.lineTo(width/2f, (height * 8.75f));
            path.lineTo(width/2f , height);
            canvas.drawPath(path, borderPaint);
            rectF1 = new RectF();
            path.computeBounds(rectF1, true);
            region3 = new Region();
            region3.setPath(path, new Region((int) rectF1.left, (int) rectF1.top, (int) rectF1.right, (int) rectF1.bottom));
//            Log.e("onDraw", "region1 : "+region1);
            canvas.drawText("FIRST LEG", rectF1.centerX() - 50, rectF1.centerY() + 20, textPaint);
            path.reset();



            //
            // SQUARE LEG
            // 360 to 330
            path.arcTo(rectF, 360, -30);
            path.lineTo(width/2f, (height * 8.75f));
            path.lineTo(width - x, (height * 8.75f));
            canvas.drawPath(path, borderPaint);
            rectF1 = new RectF();
            path.computeBounds(rectF1, true);
            region4 = new Region();
            region4.setPath(path, new Region((int) rectF1.left, (int) rectF1.top, (int) rectF1.right, (int) rectF1.bottom));
//            Log.e("onDraw", "region1 : "+region1);
            canvas.drawText("SQUARE LEG", rectF1.centerX() + 70, rectF1.centerY() + 20, textPaint);
            path.reset();




            //
            // MID WICKET
            // 0 to 40
            path.arcTo(rectF, 352, 48);
            path.lineTo(width/2f, (height * 8.75f));
            path.lineTo(width - x , (height * 8.75f));
            canvas.drawPath(path, borderPaint);
            rectF1 = new RectF();
            path.computeBounds(rectF1, true);
            region5 = new Region();
            region5.setPath(path, new Region((int) rectF1.left, (int) rectF1.top, (int) rectF1.right, (int) rectF1.bottom));
//            Log.e("onDraw", "region1 : "+region1);
            canvas.drawText("MID WICKET", rectF1.centerX() + 50, rectF1.centerY() - 70, textPaint);
            path.reset();




            //
            // LONG ON
            // 90 to 50
            path.arcTo(rectF, 90, -50);
            path.lineTo(width/2f, (height * 8.75f));
            path.lineTo(width/2f, height * 19.8f);
            canvas.drawPath(path, borderPaint);
            rectF1 = new RectF();
            path.computeBounds(rectF1, true);
            region6 = new Region();
            region6.setPath(path, new Region((int) rectF1.left, (int) rectF1.top, (int) rectF1.right, (int) rectF1.bottom));
//            Log.e("onDraw", "region1 : "+region1);
            canvas.drawText("LONG ON", rectF1.centerX() - 30, rectF1.centerY() + 70, textPaint);
            path.reset();




            //
            // LONG OFF
            // 90 to 140
            path.arcTo(rectF, 90, 50);
            path.lineTo(width/2f, (height * 8.75f));
            path.lineTo(width/2f, height * 19.8f);
            canvas.drawPath(path, borderPaint);
            rectF1 = new RectF();
            path.computeBounds(rectF1, true);
            region7 = new Region();
            region7.setPath(path, new Region((int) rectF1.left, (int) rectF1.top, (int) rectF1.right, (int) rectF1.bottom));
//            Log.e("onDraw", "region1 : "+region1);
            canvas.drawText("LONG OFF", rectF1.centerX() + 50, rectF1.centerY() + 70, textPaint);
            path.reset();



            //
            // COVERS
            // 180 to 140
            path.arcTo(rectF, 180, -40);
            path.lineTo(width/2f, (height * 8.75f));
            path.lineTo(x , (height * 8.75f));
            canvas.drawPath(path, borderPaint);
            rectF1 = new RectF();
            path.computeBounds(rectF1, true);
            region8 = new Region();
            region8.setPath(path, new Region((int) rectF1.left, (int) rectF1.top, (int) rectF1.right, (int) rectF1.bottom));
//            Log.e("onDraw", "region1 : "+region1);
            canvas.drawText("COVERS", rectF1.centerX() - 50, rectF1.centerY() - 70, textPaint);
            path.reset();


            this.setOnTouchListener(this);

        }


        @Override
        public boolean onTouch(View v, MotionEvent event) {

            if (event.getAction() == MotionEvent.ACTION_DOWN) {
//            Log.e("onTouch", " onTouch");

//            Toast.makeText(getApplicationContext(), "ontouch", Toast.LENGTH_SHORT).show();
//                Toast.makeText(getApplicationContext(), "save : 1" + save, Toast.LENGTH_SHORT).show();
//                Toast.makeText(getApplicationContext(), "save : 2" + save, Toast.LENGTH_SHORT).show();


                if (region1.contains((int) event.getX(), (int) event.getY())) {

                    wwRegion = "POINT";
                    strokeDirection = 6;

//                Toast.makeText(getApplicationContext(), " POINT ", Toast.LENGTH_SHORT).show();
                } else if (region2.contains((int) event.getX(), (int) event.getY())) {

                    wwRegion = "THIRD MAN";
                    strokeDirection = 7;
//                Toast.makeText(getApplicationContext(), " THIRD MAN ", Toast.LENGTH_SHORT).show();
                } else if (region3.contains((int) event.getX(), (int) event.getY())) {

                    wwRegion = "FIRST LEG";
                    strokeDirection = 0;
//                Toast.makeText(getApplicationContext(), " FIRST LEG ", Toast.LENGTH_SHORT).show();
                } else if (region4.contains((int) event.getX(), (int) event.getY())) {

                    wwRegion = "SQUARE LEG";
                    strokeDirection = 1;
//                Toast.makeText(getApplicationContext(), " SQUARE LEG ", Toast.LENGTH_SHORT).show();
                } else if (region5.contains((int) event.getX(), (int) event.getY())) {

                    wwRegion = "MID WICKET";
                    strokeDirection = 2;
//                Toast.makeText(getApplicationContext(), " MID WICKET ", Toast.LENGTH_SHORT).show();
                } else if (region6.contains((int) event.getX(), (int) event.getY())) {

                    wwRegion = "LONG ON";
                    strokeDirection = 3;
//                Toast.makeText(getApplicationContext(), " LONG ON ", Toast.LENGTH_SHORT).show();
                } else if (region7.contains((int) event.getX(), (int) event.getY())) {

                    wwRegion = "LONG OFF";
                    strokeDirection = 4;
//                Toast.makeText(getApplicationContext(), " LONG OFF ", Toast.LENGTH_SHORT).show();
                } else if (region8.contains((int) event.getX(), (int) event.getY())) {

                    wwRegion = "COVERS";
                    strokeDirection = 5;
//                Toast.makeText(getApplicationContext(), " COVERS ", Toast.LENGTH_SHORT).show();
                } else {

                    wwRegion = null;
//                Toast.makeText(getApplicationContext(), " None selected ", Toast.LENGTH_SHORT).show();
                }


                if (wwRegion != null) {
                    displayRegionAlert(wwRegion);

//                    if (save) {
//                        save = false;

//                    }
                } else
                    displayErrorAlert();

            }



//            Toast.makeText(getApplicationContext(), " Touch is working, x :" + event.getX()+", y : "+event.getY(), Toast.LENGTH_SHORT).show();


            return true;
        }

    }


//
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        MenuItem item = menu.findItem(R.id.ww_ok);
//        if (wwRegion == null)
//            item.setVisible(false);
//        else
//            item.setVisible(true);
//
//        getMenuInflater().inflate(R.menu.wagon_wheel_ok, menu);
//        return true;
//    }
//
//
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
////        int id = item.getItemId();
////        switch (id){
////            case R.id.ww_ok:
//        if (item.getItemId() ==  R.id.ww_ok) {
//            if (wwRegion == null)
//                Toast.makeText(getApplicationContext(), "Select one region", Toast.LENGTH_LONG).show();
//
//            else {
//
//                Toast.makeText(getApplicationContext(), wwRegion, Toast.LENGTH_LONG).show();
//                saveDetails();
////                Intent i = new Intent(WagonWheel.this, ScoringActivity.class);
////                i.putExtra("status", "resume");
////                startActivity(i);
//            }
//
////                Toast.makeText(getApplicationContext(), text, Toast.LENGTH_LONG).show();
//
//        }
//
//        return true;
//
//    }
//
//


    void saveDetails(){

//        Intent intent = new Intent(WagonWheel.this, ScoringActivity.class);   Commented on 28/07/2021
        Intent intent = new Intent(WagonWheel.this, UpdatedScoringActivity.class); // 28/07/2021
        intent.putExtra("wagon_wheel", wwRegion);
        intent.putExtra("stroke_direction", strokeDirection);
        Log.d("wheel ", "ww, wwRegion : " + wwRegion);
        Log.d("wheel ", "ww, strokeDirection : " + strokeDirection);
        Log.d("wheel ", "ww, eventId : " + eventId);
        intent.putExtra("SET_OVER", true);
        intent.putExtra("status", "resume");
        intent.putExtra("wheel", true);
//        intent.putExtra("event_array", String.valueOf(parseJSON(jsonEventArray)));
//        intent.putExtra("eventId", eventId);
        intent.putExtra("score", score);
        intent.putExtra("efo", efo);
        intent.putExtra("eventId", eventId);

//        intent.putExtra("mo", mo);
        startActivity(intent);
    }



    private JSONArray parseJSON(JSONArray eventArray) {

        try {

            JSONObject jsonEvent = eventArray.getJSONObject(eventArray.length() - 1);
            jsonEvent.put("stroke_direction", strokeDirection);
            eventArray.put(jsonEvent);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return eventArray;
    }



    @Override
    public void onBackPressed() {

        AlertDialog messageAlert = new AlertDialog.
                Builder(WagonWheel.this).create();
        messageAlert.setIcon(R.drawable.ball);
        messageAlert.setCancelable(false);
        messageAlert.setTitle("Alert");
        messageAlert.setMessage("Please select one region");
        messageAlert.setButton(AlertDialog.BUTTON_POSITIVE, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }

                });

        messageAlert.show();
    }


    void displayRegionAlert(String region){

        Log.d("disnetEmsg", "inside displayNetworkErrorMessage\n\n");

        AlertDialog alertDialog = new AlertDialog.Builder(WagonWheel.this).create();
        alertDialog.setIcon(R.drawable.ball);
        alertDialog.setCancelable(false);
        alertDialog.setTitle("Selected Region is : ");
        alertDialog.setMessage(region);
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Ok",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        saveDetails();
                    }
                });
//        alertDialog.setCancelable(false);
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Cancel",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
//                        Toast.makeText(getApplicationContext(), "cancel", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();

//                        wwRegion = null;
//                        strokeDirection = -1;
                    }
                });
        alertDialog.show();
    }



    void displayErrorAlert(){

        Log.d("disnetEmsg", "inside displayNetworkErrorMessage\n\n");

        AlertDialog alertDialog = new AlertDialog.Builder(WagonWheel.this).create();
        alertDialog.setIcon(R.drawable.ball);
        alertDialog.setCancelable(false);
        alertDialog.setMessage("Please select one region");
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Ok",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
//                        saveDetails();
                    }
                });
        alertDialog.show();
    }








}
