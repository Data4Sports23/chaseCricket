package com.data4sports.chasecricket.applicationConstants;


public interface ApplicationConstants {
    //Permission code
    int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;
    int ACTIVITY_CALL_BACK = 11098;
    int ACTIVITY_CALL_BACK_SECOND = 11100;
    int ACTIVITY_CALL_BACK_THIRD = 17890;
    int ACTIVITY_CALL_BACK_FOURTH = 17892;
    int ACTIVITY_CALL_BACK_SEARCH = 17896;
    int ACTIVITY_CALL_BACK_VERIFY = 17899;
    int ACTIVITY_CALL_BACK_EMAIL_VERIFY = 17900;
    int ACTIVITY_CALL_BACK_PROFILE_UPDATE = 17911;
    int ACTIVITY_CALL_BACK_LANGUAGE = 17914;
    int REQUEST_CHECK_SETTINGS = 2;
    long LOCATION_REFRESH_TIME = 1000;
    float LOCATION_REFRESH_DISTANCE = 5f;
    int PICK_IMAGE_REQUEST = 210;
    int WRITE_EXTERNAL_STORAGE = 201;
    int CAMERA_PERMISSION = 227;
    int GALLERY_PERMISSION = 228;
    int AUDIO_PERMISSION = 229;
    int FILE_PERMISSION = 230;
    int READ_SMS = 204;
    int FILE_PICK_REQUEST_CODE = 532;
    int DOC_FILE_PICK_REQUEST_CODE = 534;
    int AUDIO_FILE_PICK_REQUEST_CODE = 535;
    int AUDIO_RECORD_PERMISSION = 538;
    int READ_CONTACTS = 203;
    int READ_ACCOUNT = 207;
    int RECORD_AUDIO = 208;
    int PHONE_STATE = 2001;
    int IMAGE_CAPTURE = 200;
    int REQUEST_SEND_SMS = 215;
    int NEW_CONTACTS = 240;
    int SETTINGS_PERMISSIONS = 241;
    int RC_SIGN_IN = 1007;
    int MIN_USER_AGE = 10;
    //activity call back
    int ACTIVITY_PAYMENT = 1288;
    int RESULT_CLOSE_ALL = 11011;
    int RESULT_LOAD_IMAGE = 1234;
    int ENQUIRY_DOC_FILE_PICK_REQUEST_CODE = 1534;
    int PAYMENT_GATEWAY_CALL_BACK = 9919;


    /**
     * shared preference page name
     */
    String APPLICATION_DATA = "rayzKartApplicationData";
    String LOGIN_STATUS = "userLoginStatus";
    String USER_ID = "userId";
    String LOC_ID = "locationId";
    String USER_NAME = "userName";
    String STOP_NAME = "stopName";
    String LOC_NAME = "locationName";
    String ROUTE_TYPE = "routeType";
    String ROUTE_ID = "routeId";
    String STOP_ID = "stopId";
    String IMG_URL = "imageUrl";
    String COLOR = "color";
    String BLUE = "blue";
    String RED = "red";
    String IDENTIFIER = "identifier";

    String USER_PASSWORD = "userPassword";
    String USER_ACCESS_TOKEN = "access_token";
    String USER_RESET_TOKEN = "reset_token";
    String USER_TOKEN_EXPIRED = "userTokenExpired";
    String WEB_URL = "webUrl";
    String WEB_TITLE = "webTitle";

    //token type
    String typeType = "Bearer";

    /**
     * Application refresh message
     */
    String REFRESH_POST_UPDATE = "identifierRefreshPost";
    String REFRESH_LOGIN_UPDATE = "identifierRefreshLogin";

    /**
     * shared preference key name and value
     */
    String USER_TOKEN = "userToken";

    //application constant
    String CATEGORY_ID = "categoryId";
    String CATEGORY_NAME = "categoryName";
    String PRODUCT_ID = "productId";
    // location updates interval - 10sec
    long UPDATE_INTERVAL_IN_MILLISECONDS = 10000;

    // fastest updates interval - 5 sec
    // location updates will be received if another app is requesting the locations
    // than your app can handle
    long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS = 5000;

    //enquiry status
    //0-pending, 1- accepted, 2- rejected
    int PENDING = 0;
    int ACCEPTED = 1;
    int REJECTED = 2;

    String PHONE_NUMBER = "phoneNumber";
    String EMAIL = "email";
    String MESSAGE = "message";
    String OTP = "otp";

    String DATA = "data";

    //adaptor layout display
    int LAYOUT_BANNER = 1;
    int LAYOUT_CATEGORY = 2;
    int LAYOUT_PRODUCT = 3;
    //Layout Constants
    String SEARCH_BLOCK = " SEARCH BAR";
    String BANNER_BLOCK = "SINGLE WIDE VIEW";
    String CATEGORY_BLOCK = "CATEGORY BLOCK";
    String SINGLE_ROW_BLOCK = "SINGLE ROW";
    String SIX_X_SIX_BLOCK = "6 X 6 BLOCK";
    String SINGLE_ROW_TWO_X_TWO = "SINGLE ROW 2 X 2";

    String GUEST_USER_NAME = "guest@rayzkart.com";
    String GUEST_USER_PASSWORD="guest@1234";

    String IMAGE = "img";
}
