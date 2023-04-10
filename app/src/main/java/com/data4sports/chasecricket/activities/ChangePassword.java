package com.data4sports.chasecricket.activities;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.data4sports.chasecricket.R;

/**
 * Created on 14/05/2021
 */
public class ChangePassword extends AppCompatActivity implements View.OnClickListener {

    EditText et_current_password, et_new_password, et_confirm_password;
    String password, current_password, new_password, confirm_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_password);

        findViewById(R.id.cp_btn_reset).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cp_btn_reset:
                resetPassword();
        }
    }

    public void resetPassword() {
        et_current_password = findViewById(R.id.cp_et_cpswd);
        et_new_password = findViewById(R.id.cp_et_npswd);
        et_confirm_password = findViewById(R.id.cp_et_cfpswd);

        current_password = et_current_password.getText().toString();
        new_password = et_new_password.getText().toString();
        confirm_password = et_confirm_password.getText().toString();

        if (current_password.matches("") || current_password.matches(null)) {
            et_current_password.setError("Please enter password");
        }
        else
            if (!password.matches(et_current_password.getText().toString())) {
                displayError("Wrong password");
        }
        else
            if (new_password.matches("") || new_password.matches(null)) {
                et_new_password.setError("Please enter password");
        }
        else
            if (!password.matches(et_current_password.getText().toString())) {
                displayError("Wrong password");
            }
        else
            if (confirm_password.matches("") || confirm_password.matches(null)) {
                et_confirm_password.setError("Please enter password");
            }

    }

    public void displayError(String message){

        AlertDialog messageAlert = new AlertDialog.
                Builder(ChangePassword.this).create();
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