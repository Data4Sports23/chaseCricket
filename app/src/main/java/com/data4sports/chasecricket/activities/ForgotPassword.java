package com.data4sports.chasecricket.activities;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.data4sports.chasecricket.R;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created on 17/05/2021
 */
public class ForgotPassword extends AppCompatActivity implements View.OnClickListener {

    EditText et_email;
    Button btn_submit;
    String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forgot_password);

        findViewById(R.id.fp_btn_submit).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.fp_btn_submit:
                submitEmail();
                break;
        }

    }

    private void submitEmail() {
        et_email = findViewById(R.id.fp_et_email);
        email = et_email.getText().toString();
        if (email.matches("") || email == null) {
            et_email.setError("Please enter the email");
        }
        else if (!isEmailValid(email)) {
            displayError("Invalid email");
        }
        else {

        }
    }

    public static boolean isEmailValid(String email) {
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public void displayError(String message){

        AlertDialog messageAlert = new AlertDialog.
                Builder(ForgotPassword.this).create();
        messageAlert.setIcon(R.drawable.ball);
        messageAlert.setCancelable(false);
        messageAlert.setMessage(message);
        messageAlert.setButton(AlertDialog.BUTTON_POSITIVE, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        messageAlert.show();
    }

}