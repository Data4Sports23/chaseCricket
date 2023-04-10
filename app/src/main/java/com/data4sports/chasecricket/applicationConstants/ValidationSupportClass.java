package com.data4sports.chasecricket.applicationConstants;

import java.util.regex.Pattern;

public class ValidationSupportClass {

    /**
     * checking email is valid or not
     *
     * @return boolean result
     */
    public static boolean isValidateEmail(String mailAddress) {
        return !mailAddress.isEmpty() && android.util.Patterns.EMAIL_ADDRESS.matcher(mailAddress).matches();
    }

    /**
     * checking string empty
     *
     * @param text text input
     * @return boolean result
     */
    public static boolean isValidText(String text) {
        return !text.isEmpty() && text.trim().length() > 0;
    }

    /**
     * checking string empty
     *
     * @param text text input
     * @return boolean result
     */
    public static boolean isValidPassword(String text) {
        return !text.isEmpty() && text.trim().length() > 3;
    }

    /**
     * phone no validation
     *
     * @param phone phone no
     * @return boolean result
     */
    public static boolean isValidMobile(String phone) {
        return !Pattern.matches("[a-zA-Z]+", phone) && phone.length() >= 6 && phone.length() <= 13;
    }

    /**
     * image url issue solve
     *
     * @param imageUrl image
     * @return space remove
     */
    public static String getImageUrl(String imageUrl) {
        return imageUrl.replaceAll(" ", "%20");
    }
}
