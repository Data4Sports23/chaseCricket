package com.data4sports.chasecricket.activities;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;

import com.data4sports.chasecricket.applicationConstants.ApiConstants;
import com.data4sports.chasecricket.applicationConstants.ApplicationConstants;

import java.util.HashMap;
import java.util.Map;

/*
 * Created by akhil on 18/07/20.
 */
public class BaseAppCompatActivity extends AppCompatActivity implements ApplicationConstants, ApiConstants {

//    public AppSharedPreferences appPreferences;
    private long lastClickTime = 0;
    public final String TAG = "activity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //shared preference declaration
//        appPreferences = AppSharedPreferences.getInstance(this, APPLICATION_DATA);
    }

    /**
     * recycler view adaptor display animation
     *
     * @return animation
     */
//    public LayoutAnimationController getItemDisplayAnimation() {
//        return AnimationUtils.loadLayoutAnimation(this, R.anim.layout_animation_fall_down);
//    }

    /**
     * header
     *
     * @return header map
     */
    public Map<String, String> getHeaderMap() {
        //header map
        Map<String, String> map = new HashMap<>();
        map.put("X-API-KEY", API_KEY);
        return map;
    }

    /**
     * header with user id
     *
     * @return header map
     */
    public Map<String, String> getHeaderMapWithID() {
        //header map
        Map<String, String> map = new HashMap<>();
        map.put("X-API-KEY", API_KEY);
//        map.put("X-Api-Key-Id", getUserId());
        return map;
    }

    /**
     * permission
     *
     * @param context     context
     * @param permissions permission
     * @return
     */
    public static boolean hasPermissions(Context context, String... permissions) throws Exception {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

//    public String getUserId() {
//        return appPreferences.getData2(USER_ID);
//
//    }

//    public String getEmailId() {
//        return appPreferences.getData(USER_EMAIL);
//    }
//
//    public String getImage() {
//        return appPreferences.getData2(USER_IMAGE);
//
//    }

//    public String getLocation() {
//        return appPreferences.getData(USER_LOCATION, "");
//
//    }
//
//    public String getAddress() {
//        return getLocation() + ","
//                + getState() + ","
//                + getDistrict();
//
//    }
//
//    public String getLat() {
//        return appPreferences.getData(USER_LAT, "0.0");
//
//    }
//
//    public String getLon() {
//        return appPreferences.getData(USER_LON, "0.0");
//
//    }
//
//    public String getState() {
//        return appPreferences.getData(USER_STATE, "");
//
//    }
//
//    public String getDistrict() {
//        return appPreferences.getData(USER_DISTRICT, "");
//
//    }
//
//    public String getPhoneNo() {
//        return appPreferences.getData(USER_PHONE_NUMBER, "");
//
//    }
//
//    public String getName() {
//        String fName = appPreferences.getData2(USER_FIRST_NAME);
//        String LName = appPreferences.getData2(USER_LAST_NAME);
//        return (fName == null ? "" : fName) + " " + (LName == null ? "" : LName);
//    }
//
//    public int getUserType() {
//        //user type
//        //1-Technician/Service Provider
//        //2-General Public User
//        return appPreferences.getInt(USER_TYPE, 2);
//
//    }
//
//    public String getDefaultContactDisplay() {
//        return appPreferences.getData(CONTACT_NUMBER, "");
//    }
//
//    public String getDefaultWhatsAppDisplay() {
//        return appPreferences.getData(WHATS_APP_NUMBER, "");
//    }
//
//    public String getDefaultAddressDisplay() {
//        return appPreferences.getData(USER_ADDRESS, "");
//    }
//
//    public String getDefaultFullAddressDisplay() {
//        return getDefaultAddressDisplay() + " , "
//                + getLocation() + " , "
//                + getDistrict() + " , "
//                + getState();
//    }

//    /**
//     * Prevent multiple click
//     *
//     * @return true : block , false : can click
//     */
//    public boolean preventMultipleClick() {
//        // Preventing multiple clicks, using threshold of 1 second
//        if (SystemClock.elapsedRealtime() - lastClickTime < 1000) {
//            return true;
//        }
//        lastClickTime = SystemClock.elapsedRealtime();
//        return false;
//    }
//
//    /*
//     * stage allows you to switch between sandboxed and production servers
//     * for CashFree Payment Gateway. The possible values are
//     *
//     * 1. TEST: Use the Test server. You can use this service while integrating
//     *      and testing the CashFree PG. No real money will be deducted from the
//     *      cards and bank accounts you use this stage. This mode is thus ideal
//     *      for use during the development. You can use the cards provided here
//     *      while in this stage: https://docs.cashfree.com/docs/resources/#test-data
//     *
//     * 2. PROD: Once you have completed the testing and integration and successfully
//     *      integrated the CashFree PG, use this value for stage variable. This will
//     *      enable live transactions
//     */
//    public String getStage() {
//        return BuildConfig.STAGE;
//    }
//
//    /**
//     * recycler view Adaptor divider
//     *
//     * @return divider object
//     */
//    public RecyclerView.ItemDecoration getItemDecoration() {
//        //divider
//        DividerItemDecoration itemDecorator = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
//        itemDecorator.setDrawable(ContextCompat.getDrawable(this, R.drawable.divider));
//        // use this setting to improve performance if you know that changes
//        // in content do not change the layout size of the RecyclerView
//        return itemDecorator;
//    }
//
//    /**
//     * place search event
//     *
//     * @param activity Activity
//     */
//    public void placeSearchEventClick(Activity activity) {
//        try {
//            if (!Places.isInitialized()) {
//                Places.initialize(activity, BuildConfig.GOOGLE_API_KEY);
//            }
//            // Set the fields to specify which types of place data to
//            // return after the user has made a selection.
//            List<Place.Field> fields = Arrays.asList(Place.Field.ID,
//                    Place.Field.LAT_LNG,
//                    Place.Field.NAME);
//
//            // Start the autocomplete intent.
//            Intent intent = new Autocomplete.IntentBuilder(
//                    AutocompleteActivityMode.OVERLAY, fields)
//                    .setCountry(COUNTRY_CODE)
//                    .setTypeFilter(TypeFilter.GEOCODE)
//                    .build(activity);
//
//            startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE);
//        } catch (Exception e) {
//            //exception message
//            System.out.println("e = " + e);
//        }
//    }
//
//    /**
//     * Hides the soft keyboard
//     */
//    public void hideSoftKeyboard() {
//        try {
//            InputMethodManager imm = (InputMethodManager) getSystemService(Context.
//                    INPUT_METHOD_SERVICE);
//            if (imm != null && getCurrentFocus() != null) {
//                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
//            }
//        } catch (Exception e) {
//            Log.e(TAG, "onTouchEvent: ", e);
//        }
//    }
//
//    /**
//     * screen tech event
//     *
//     * @param event : MotionEvent
//     * @return : result
//     */
//    @Override
//    public boolean onTouchEvent(MotionEvent event) {
//        hideSoftKeyboard();
//        return true;
//    }
//
//    /**
//     * permission
//     *
//     * @param context     context
//     * @param permissions permission
//     * @return
//     */
//    public static boolean hasPermissions(Context context, String... permissions) throws Exception {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
//            for (String permission : permissions) {
//                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
//                    return false;
//                }
//            }
//        }
//        return true;
//    }
//
//    /**
//     * decode url
//     *
//     * @param afterDecode string
//     * @return string
//     */
//    public String getUrlEncode(String afterDecode) {
//        try {
//            afterDecode = URLDecoder.decode(afterDecode, "UTF-8");
//        } catch (Exception e) {
//            afterDecode = null;
//        }
//        return afterDecode;
//    }
//
//    /**
//     * cropping event calling
//     *
//     * @param source source
//     */
//    public void beginCrop(Uri source) {
//
//        Uri destination = Uri.parse(TempFileHelper.getFileProfileImagePath().toString());
//        Uri sourceUri = Uri.fromFile(new File(source.toString()));
//        UCrop.of(sourceUri, destination)
//                .withAspectRatio(1, 1)
//                //.withMaxResultSize(maxWidth, maxHeight)
//                .start(this);
//
//    }
//
//    /**
//     * fail message
//     */
//    public void failMessage(String msg) {
//        displayMessage(this, msg);
//    }
//
//    public void displayMessage(Context context, String message) {
//        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
//    }
//
//    public void makeCall(String number) {
//        Intent callIntent = new Intent(Intent.ACTION_DIAL);
//        callIntent.setData(Uri.parse("tel:" + number));
//        startActivity(callIntent);
//    }
//
//
//    public void onDownloadClick(String url, View view) {
//
//        //set Storage permission for Android M
//        // The request code used in ActivityCompat.requestPermissions()
//        // and returned in the Activity's onRequestPermissionsResult()
//        String[] PERMISSIONS = {Manifest.permission.WRITE_EXTERNAL_STORAGE,
//                Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE
//        };
//        //permission method
//        Dexter.withActivity(this)
//                .withPermissions(PERMISSIONS)
//                .withListener(new MultiplePermissionsListener() {
//                    @Override
//                    public void onPermissionsChecked(MultiplePermissionsReport report) {
//                        //download
//                        new PdfDownloadAndSave(BaseAppCompatActivity.this,
//                                new PdfDownloadAndSave.ServiceCallback() {
//                                    @Override
//                                    public void onSuccess(String url) {
//
//                                        //message display
//                                        DisplayToastSnack(view,
//                                                getResources().getString(R.string.success));
//                                    }
//
//                                    @Override
//                                    public void onFail(String message) {
//                                        //message display
//                                        DisplayToastSnack(view, message);
//                                    }
//                                }).execute(url);
//                    }
//
//                    @Override
//                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
//                        token.continuePermissionRequest();
//                    }
//                }).check();
//    }
//
//    public void DisplayToastSnack(View mainLayout, String coming_soon) {
//        Snackbar snackbar = Snackbar
//                .make(mainLayout, coming_soon, Snackbar.LENGTH_SHORT);
//        snackbar.show();
//    }
//
//    public void whatsappOpen(String contact) {
//        if (contact == null) {
//            return;
//        }
//        if (contact.length() == 10) {
//            contact = "+91" + contact;
//        }
//        String url = "https://api.whatsapp.com/send?phone=" + contact;
//        try {
//            PackageManager pm = this.getPackageManager();
//            pm.getPackageInfo("com.whatsapp", PackageManager.GET_ACTIVITIES);
//            Intent i = new Intent(Intent.ACTION_VIEW);
//            i.setData(Uri.parse(url));
//            startActivity(i);
//        } catch (PackageManager.NameNotFoundException e) {
//            Toast.makeText(this, "Whatsapp app not installed in your phone", Toast.LENGTH_SHORT).show();
//            e.printStackTrace();
//        }
//    }
}
