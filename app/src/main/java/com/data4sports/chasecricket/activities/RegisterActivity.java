package com.data4sports.chasecricket.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.data4sports.chasecricket.R;
import com.data4sports.chasecricket.applicationConstants.AppConstants;
import com.data4sports.chasecricket.applicationConstants.ValidationSupportClass;
import com.data4sports.chasecricket.models.Constants;
import com.data4sports.chasecricket.models.MyApplicationClass;
import com.data4sports.chasecricket.models.SharedPrefManager;
import com.data4sports.chasecricket.models.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;
import io.realm.exceptions.RealmException;
import io.realm.exceptions.RealmPrimaryKeyConstraintException;

public class RegisterActivity extends BaseAppCompatActivity {

    EditText editName, editEmail, editPassword, editTextInputPhone, edit_confirm_pswrd, edit_address;
    Button register;
    TextView login;
    Context mContext;

    Realm realm;

    int userId = 0, temp = 0;

    SharedPreferences mPreferences;
    SharedPreferences.Editor mEditor;
    private ProgressDialog progress;
    ImageView profilePreview;
    private static final int SELECT_PICTURE = 0;
    private static final int REQUEST_CAMERA = 1;

    AlertDialog dialog;
    private static final int IMAGE_PICK = 1;
    private static final int IMAGE_CAPTURE = 2;
    private Bitmap profile_imageBitmap;
    RealmConfiguration config;
    String path;
    String profile_Path;
    String setphoto;

    //    String REGISTER_API_URL = "http://cricketlive.data4sports.com/bhavith_new/public/api/register";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_register);
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        editName = findViewById(R.id.edit_name);
        editEmail = findViewById(R.id.edit_email);
        editPassword = findViewById(R.id.edit_pswrd);
        editTextInputPhone = findViewById(R.id.editTextInputPhone);
        edit_confirm_pswrd = findViewById(R.id.edit_confirm_pswrd);
        edit_address = findViewById(R.id.edit_address);

        register = findViewById(R.id.btnregister);
        login = findViewById(R.id.tv_login);
        profilePreview = findViewById(R.id.profilePreview);

        mPreferences = PreferenceManager.getDefaultSharedPreferences(this);

//        config = new RealmConfiguration.Builder()
//                .name(AppConstants.GAME_ID + ".realm")
//                .deleteRealmIfMigrationNeeded()
//                .build();
//        realm = Realm.getInstance(config);


        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayProgress();
                try {
                    newRegistration();
                } catch (JSONException e) {
                    progress.dismiss();
                    e.printStackTrace();
                }
            }
        });


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                RealmResults<User> u_result = realm.where(User.class).findAll();

                Log.e("Register", "onCreate");
                if (u_result.isEmpty())
                    Log.e("Register", "onCreate, user =" + u_result);
                if (u_result.isLoaded()) {
                    for (User user : u_result) {
                        ++temp;
                        Log.e("Register", "onCreate, user " + temp + " : " + user);
                    }
                }
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            }
        });
        profilePreview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (v == profilePreview) {
//                    dialog.show();
                selectImage();
//                }
            }
        });


//        final String[] items = new String[] { "Take from camera",
//                "Select from gallery" };
//        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
//                android.R.layout.select_dialog_item, items);
//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//
//        builder.setTitle("Select Image");
//        builder.setAdapter(adapter, new DialogInterface.OnClickListener() {
//            public void onClick(DialogInterface dialog, int item) {
//
//                if (item == 0) {
//                    path = "";
//                    Intent intent = new Intent(
//                            "android.media.action.IMAGE_CAPTURE");
//                    File folder = new File(Environment
//                            .getExternalStorageDirectory() + "/LoadImg");
//
//                    if (!folder.exists()) {
//                        folder.mkdir();
//                    }
//                    final Calendar c = Calendar.getInstance();
//                    String new_Date = c.get(Calendar.DAY_OF_MONTH) + "-"
//                            + ((c.get(Calendar.MONTH)) + 1) + "-"
//                            + c.get(Calendar.YEAR) + " " + c.get(Calendar.HOUR)
//                            + "-" + c.get(Calendar.MINUTE) + "-"
//                            + c.get(Calendar.SECOND);
//
//                    path = String.format(
//                            Environment.getExternalStorageDirectory()
//                                    + "/LoadImg/%s.png", "LoadImg(" + new_Date
//                                    + ")");
//                    File photo = new File(path);
//                    intent.putExtra(MediaStore.EXTRA_OUTPUT,
//                            Uri.fromFile(photo));
//                    startActivityForResult(intent, 2);
//
//                } else { // pick from file
//                    Intent intent = new Intent(
//                            Intent.ACTION_PICK,
//                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//                    intent.setType("image/*");
//                    startActivityForResult(
//                            Intent.createChooser(intent, "Choose a Photo"),
//                            IMAGE_PICK);
//                }
//            }
//        });
//
//        dialog = builder.create();
    }

    private void selectImage() {
        final CharSequence[] items = {"Take Photo", "Choose from Library",
                "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("Take Photo")) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    File f = new File(android.os.Environment
                            .getExternalStorageDirectory(), "temp.jpg");
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
                    startActivityForResult(intent, REQUEST_CAMERA);
                } else if (items[item].equals("Choose from Library")) {
                    Intent intent = new Intent(
                            Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/*");
                    startActivityForResult(
                            Intent.createChooser(intent, "Select File"),
                            SELECT_PICTURE);
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_CAMERA) {
                File f = new File(Environment.getExternalStorageDirectory()
                        .toString());
                for (File temp : f.listFiles()) {
                    if (temp.getName().equals("temp.jpg")) {
                        f = temp;
                        break;
                    }
                }
                try {
                    Bitmap bm;
                    BitmapFactory.Options btmapOptions = new BitmapFactory.Options();
                    btmapOptions.inSampleSize = 2;
                    bm = BitmapFactory.decodeFile(f.getAbsolutePath(),
                            btmapOptions);

                    // bm = Bitmap.createScaledBitmap(bm, 70, 70, true);
                    profilePreview.setImageBitmap(bm);

                    String path = android.os.Environment
                            .getExternalStorageDirectory()
                            + File.separator
                            + "test";
                    f.delete();
                    OutputStream fOut = null;
                    File file = new File(path, String.valueOf(System
                            .currentTimeMillis()) + ".jpg");
                    fOut = new FileOutputStream(file);
                    bm.compress(Bitmap.CompressFormat.JPEG, 85, fOut);
                    fOut.flush();
                    fOut.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (requestCode == SELECT_PICTURE) {
                Uri selectedImageUri = data.getData();
                String tempPath = getPath(selectedImageUri, this);
                Bitmap bm;
//                btmapOptions.inSampleSize = 2;
                BitmapFactory.Options btmapOptions = new BitmapFactory.Options();
                bm = BitmapFactory.decodeFile(tempPath, btmapOptions);
                profilePreview.setImageBitmap(bm);
            }
        }
    }

    public String getPath(Uri uri, Activity activity) {
        String[] projection = {MediaStore.MediaColumns.DATA};
        Cursor cursor = activity
                .managedQuery(uri, projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//
//        if (resultCode == Activity.RESULT_OK && requestCode == IMAGE_PICK
//                || requestCode == IMAGE_CAPTURE) {
//            switch (requestCode) {
//                case IMAGE_PICK:
//                    this.imageFromGallery(resultCode, data);
//
//                    profilePreview.setImageBitmap(null);
//
//                    profilePreview.setImageBitmap(profile_imageBitmap);
//
//                    break;
//
//                case IMAGE_CAPTURE:
//
//                    this.imageFromGallery(resultCode, data);
//
//                    profilePreview.setImageBitmap(null);
//
//                    profilePreview.setImageBitmap(profile_imageBitmap);
//
//                    break;
//                default:
//                    break;
//            }
//        }
//    }

//    private void imageFromGallery(int resultCode, Intent data) {
//        Uri selectedImage = data.getData();
//        String[] filePathColumn = { MediaStore.Images.Media.DATA };
//
//        Cursor cursor = getContentResolver().query(selectedImage,
//                filePathColumn, null, null, null);
//        cursor.moveToFirst();
//
//        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
//
//        profile_Path = cursor.getString(columnIndex);
//        cursor.close();
//
//        profile_imageBitmap = BitmapFactory.decodeFile(profile_Path);
//
//    }
//
//    private void imageFromCamera(int resultCode, Intent data) {
//        updateImageView((Bitmap) data.getExtras().get("data"));
//    }
//
//    private void updateImageView(Bitmap newImage) {
//        profile_imageBitmap = newImage.copy(Bitmap.Config.ARGB_8888, true);
//    }
//
//    public String getPath(Uri uri) {
//        String[] projection = { MediaStore.Images.Media.DATA };
//        Cursor cursor = managedQuery(uri, projection, null, null, null);
//        int column_index = cursor
//                .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
//        cursor.moveToFirst();
//        return cursor.getString(column_index);
//    }

    void newRegistration() throws JSONException {

        final String username = editName.getText().toString().trim();
        final String email = editEmail.getText().toString().trim();
        final String password = editPassword.getText().toString().trim();
        final String confirmPassword = edit_confirm_pswrd.getText().toString().trim();
        final String phonenumber = editTextInputPhone.getText().toString().trim();
        final String address = edit_address.getText().toString().trim();


        //first we will do the validations
        if (TextUtils.isEmpty(username)) {
            progress.dismiss();
            editName.setError("Please enter username");
            editName.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(address)) {
            progress.dismiss();
            edit_address.setError("Please enter address");
            edit_address.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(email)) {
            progress.dismiss();
            editEmail.setError("Please enter your email");
            editEmail.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(phonenumber)) {
            progress.dismiss();
            editTextInputPhone.setError("Please enter your Phone Number");
            editTextInputPhone.requestFocus();
            return;
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            progress.dismiss();
            editEmail.setError("Enter a valid email");
            editEmail.requestFocus();
            return;
        }

        if (!ValidationSupportClass.isValidPassword(editPassword.getText().toString())) {
            //password validation
            Toast.makeText(this, getString(R.string.password_validation_msg), Toast.LENGTH_LONG).show();
        } else if (!ValidationSupportClass.isValidPassword(edit_confirm_pswrd.getText().toString())) {
            //password validation
            Toast.makeText(this, getString(R.string.password_validation_msg), Toast.LENGTH_LONG).show();
        } else if (editPassword.getText().toString().equals(edit_confirm_pswrd.getText().toString())) {
            //method
//            resetPassword(phone, edit_confirm_pswrd.getText().toString());
        } else {
            Toast.makeText(this, R.string.cannot_match_password, Toast.LENGTH_LONG).show();
        }


        if (isNetworkAvailable()) {

            JSONObject postparams = new JSONObject();
            postparams.put("name", username);
            postparams.put("email", email);
            postparams.put("password", password);
            postparams.put("phonenumber", phonenumber);
            postparams.put("address", address);
//            Log.d("Reg", "JSONObject is created with ( U : " + email + " & P : " + password);
            JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                    Constants.REGISTER_API_URL,
//               url,
                    postparams,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {

                            try {

                                //if no error in response
//                            if (!response.getBoolean("error")) {
//                                Toast.makeText(getApplicationContext(), response.getString("message"), Toast.LENGTH_SHORT).show();

                                Log.d("register", "inside register");
                               // Log.d("register", "response " + response.getString("msg"));
                                Log.d("register", "response Registration" + response);


                                //getting the user from the response

                                final String access_token = response.getString("access_token");
                                final String refresh_token = response.getString("refresh_token");

                                Log.d("register", "access_token " + access_token);
                                Log.d("register", "refresh_token " + refresh_token);


                            /*JSONObject userJson = response.getJSONObject("user");

                                //creating a new user object
                                User user = new User(

                                        userJson.getInt("id"),
                                        userJson.getString("name"),
                                        userJson.getString("email"),
                                        userJson.getString("token"),
                                        userJson.getString("access_token")
                                );*/


                                Realm realm = null;
                                try {
//            Toast.makeText(getApplicationContext(), "Inside savePlayerDetails", Toast.LENGTH_SHORT).show();
//                                    config = new RealmConfiguration.Builder()
//                                            .name(AppConstants.GAME_ID + ".realm")
//                                            .deleteRealmIfMigrationNeeded()
//                                            .build();
                                    realm = Realm.getDefaultInstance();
                                    realm.executeTransaction(new Realm.Transaction() {
                                        @Override
                                        public void execute(Realm bgRealm) {

//                                        final User user1 = bgRealm.createObject(User.class);

                                            try {

//

//                                            Number number = bgRealm.where(User.class).max("userId");
//                                            if (number != null)
//                                            temp = number.intValue();
//                                            Log.d("login", "user id = " + temp);
//                                            userId = temp++;

                                                Number num = bgRealm.where(User.class).max("userId");
                                                userId = (num == null) ? 1 : num.intValue() + 1;

                                                User user1 = bgRealm.createObject(User.class, userId);
                                                user1.setUsername(username);
                                                user1.setEmail(email);
                                                user1.setPhonenumber(phonenumber);
                                                user1.setAddress(address);
                                                user1.setAccess_token(access_token);
                                                user1.setRefresh_token(refresh_token);
//                                            user1.setUserId(userId);
                                                user1.setLoggedInStatus(true);
                                                bgRealm.copyToRealm(user1);
                                                Log.d("user1", "" + user1);
                                                saveToSP(username);
                                                Intent i = new Intent(RegisterActivity.this, HomeActivity.class);
                                                i.putExtra("userId", userId);
                                                SharedPrefManager.getInstance(getApplicationContext()).userLogin(user1);
                                                finish();
                                                progress.dismiss();
                                                startActivity(i);
                                                Toast.makeText(getApplicationContext(), "Register Successful", Toast.LENGTH_SHORT).show();


                                            } catch (RealmPrimaryKeyConstraintException e) {
                                                progress.dismiss();
                                                Toast.makeText(getApplicationContext(),
                                                        "Primary Key exists, Press Update instead", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                } catch (RealmException e) {
                                    progress.dismiss();
                                    Toast.makeText(getApplicationContext(), "Exception : " + e, Toast.LENGTH_SHORT).show();
                                } finally {

                                    if (realm != null) {
                                        realm.close();
                                    }
                                }
                                Log.d("Register", "get Value from JSONObject");


//                            saveToSP(username);

                                //starting the profile activity
//                                finish();
//                                startActivity(new Intent(getApplicationContext(), SucessActivity.class));
//                            } else {

//                            }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                            Log.d("Test", "Error : " + error);
                            progress.dismiss();

                            editName.setText("");
                            editEmail.setText("");
                            editPassword.setText("");
                            editTextInputPhone.setText("");


//                        Toast.makeText(getApplicationContext(), "Invalid Username or Password", Toast.LENGTH_SHORT).show();
//                        editEmail.setText("");
//                        editPassword.setText("");

                            // As of f605da3 the following should work
                        /*NetworkResponse response = error.networkResponse;
                        if (error instanceof ServerError && response != null) {
                            try {
                                String res = new String(response.data,
                                        HttpHeaderParser.parseCharset(response.headers, "utf-8"));
                                // Now you can use any deserializer to make sense of data
                                JSONObject obj = new JSONObject(res);
                            } catch (UnsupportedEncodingException e1) {
                                // Couldn't properly decode data to string
                                e1.printStackTrace();
                            } catch (JSONException e2) {
                                // returned data is not JSONObject?
                                e2.printStackTrace();
                            }
                        }*/
                        }
                    });

            Log.d("Test", "Request : " + jsonObjReq);
            MyApplicationClass.getInstance(getApplicationContext()).addToRequestQueue(jsonObjReq, "postRequest");
//        Volley.newRequestQueue(this).add(jsonObjReq);
        } else
            displayNetworkErrorMessage();
    }


    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }


    void saveToSP(String name) {


        mEditor = mPreferences.edit();
        mEditor.putInt("sp_user_id", userId);
        mEditor.putString("sp_user_name", name);
        mEditor.apply();

        Log.e("Register", "saveToSP, userId : " + userId);
    }


    public void displayProgress() {

        progress = new ProgressDialog(this);
        progress.setMessage("Please wait...");
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setIndeterminate(true);
        progress.setProgress(0);
        progress.show();
    }


    void displayNetworkErrorMessage() {

//        Log.d("disnetEmsg", "inside displayNetworkErrorMessage\n\n");

        AlertDialog alertDialog = new AlertDialog.Builder(RegisterActivity.this).create();
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


