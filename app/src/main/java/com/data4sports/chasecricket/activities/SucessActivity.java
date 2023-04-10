package com.data4sports.chasecricket.activities;//https:cricketlive.data4sports.com/chaseweb/public/api/post

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;

import com.data4sports.chasecricket.R;

/**
 *
 */
public class SucessActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_sucess);
    }
}
