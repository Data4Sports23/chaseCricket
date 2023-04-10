package com.data4sports.chasecricket.applicationConstants;

import android.text.format.DateFormat;
import android.view.View;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.StringTokenizer;
import java.util.TimeZone;

public class TimeSupporter {

    /**
     * @return current time in millisecond format
     */
    public static long getTime() {
        return System.currentTimeMillis();
    }

    public static String getUnitTime() {
        return String.valueOf(System.currentTimeMillis() / 1000L);
    }

    public static long getUnitLongTime() {
        return System.currentTimeMillis() / 1000L;
    }

    public static int getOnlineCheck(String name, String last_seen) {
        if (last_seen == null) {
            return View.GONE;
        }
        // convert seconds to milliseconds
        Date date = new Date(Long.parseLong(last_seen) * 1000L);
        //time difference
        long milliseconds = Math.abs(System.currentTimeMillis() - date.getTime());
        //condition
        if (milliseconds < 120000) {
            return View.VISIBLE;
        } else {
            return View.GONE;
        }
    }

    public static boolean getOnlineCheck(String last_seen) {
        if (last_seen == null) {
            return false;
        }
        // convert seconds to milliseconds
        Date date = new Date(Long.parseLong(last_seen) * 1000L);
        //time difference
        long milliseconds = Math.abs(System.currentTimeMillis() - date.getTime());
        //condition
        return milliseconds < 300000;
    }

    public static String getOnlineTime(String last_seen) {
        if (last_seen == null) {
            return "";
        }
        // convert seconds to milliseconds
        Date date = new Date(Long.parseLong(last_seen) * 1000L);
        //time difference
        long milliseconds = Math.abs(System.currentTimeMillis() - date.getTime());
        long seconds = (long) milliseconds / 1000;
        if (seconds < 60) {
            //second
            return String.valueOf(seconds) + " sec";
        } else if (seconds > 60 && seconds < 3600) {
            //min
            return String.valueOf((int) seconds / 60) + " min";
        } else {
            return "";
        }
    }

    /**
     * date and time with out space
     *
     * @return : replacing space to %20
     */
    public static String getDateWithOutSpace() {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy%20HH:mm");
        return df.format(cal.getTime());
    }

    /**
     * String of current date and time
     *
     * @return : Date and time
     */
    public static String getDateWithSpace() {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy HH:mm");
        return df.format(cal.getTime());
    }

    /**
     * current
     *
     * @return
     */
    public static String getDisplayDate() {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        return df.format(cal.getTime());
    }

    /**
     * current Time
     *
     * @return String of current Time
     */
    public static String getCheckInTime() {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("HH:mm");
        return df.format(cal.getTime());
    }

    /**
     * current Time
     *
     * @return String of current Time
     */
    public static String getUTCCurrentTime() {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        return sdf.format(cal.getTime());
    }

    /**
     * string convert to utf
     *
     * @return : utf
     */
    public static String convertStringToUTC(String date) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        try {
            return String.valueOf(sdf.format(sdf.parse(date)));
        } catch (ParseException ex) {
            return date;
        }
    }

    /**
     * utf convert to string
     *
     * @return : String
     */
    public static String convertUTCToString(String date) {
        if (date == null) {
            return "";
        }

//        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        SimpleDateFormat existingUTCFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat requiredFormat = new SimpleDateFormat("dd MMMM yyyy");
        requiredFormat.setTimeZone(TimeZone.getDefault());
        try {
            return String.valueOf(requiredFormat.format(existingUTCFormat.parse(date)));
        } catch (ParseException ex) {
            return date;
        }
    }

    /**
     * current Time
     *
     * @return String of current Time
     */
    public static String getCheckHHMMTime() {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("HHmm");
        return df.format(cal.getTime());
    }

    /**
     * current date
     *
     * @return String current date
     */
    public static String getCurrentDate() {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        return df.format(cal.getTime());
    }

    /**
     * current date and time
     *
     * @return string of current date and time
     */
    public static String getCurrent() {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        return df.format(cal.getTime());
    }

    /**
     * time converter for
     *
     * @param time time
     * @return time display
     */
    public static String NotificationDisplayTime(String time) {
        Date dateTime = new Date(Long.parseLong(time) * 1000L);
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        String date = df.format(dateTime);
        return getDateWithOutFeture(Long.parseLong(time));
    }

    public static String getFormattedDate(String time) {
        long smsTimeInMilis = Long.parseLong(time) * 1000L;
        Calendar smsTime = Calendar.getInstance();
        smsTime.setTimeInMillis(smsTimeInMilis);

        Calendar now = Calendar.getInstance();

        final String timeFormatString = "h:mm aa";
        final String dateTimeFormatString = "EEEE, yyyy MMMM d";
        final long HOURS = 60 * 60 * 60;
        if (now.get(Calendar.DATE) == smsTime.get(Calendar.DATE)) {
            return "Today " + DateFormat.format(timeFormatString, smsTime);
        } else if (now.get(Calendar.DATE) - smsTime.get(Calendar.DATE) == 1) {
            return "Yesterday " + DateFormat.format(timeFormatString, smsTime);
        } else if (now.get(Calendar.YEAR) == smsTime.get(Calendar.YEAR)) {
            return DateFormat.format(dateTimeFormatString, smsTime).toString();
        } else {
            return DateFormat.format("MMMM dd yyyy", smsTime).toString();
        }
    }

    public static String getDate(String dateTime) {
        StringTokenizer tk = new StringTokenizer(dateTime);
        return tk.nextToken();  // <---  yyyy-mm-dd
//        String time = tk.nextToken();  // <---  hh:mm:ss
    }

    public static String getDateFormat(String time) {
        long smsTimeInMilis = Long.parseLong(time) * 1000L;
        Calendar smsTime = Calendar.getInstance();
        smsTime.setTimeInMillis(smsTimeInMilis);
        return DateFormat.format("dd MMMM yyyy", smsTime).toString();
    }

    /**
     * time difference
     *
     * @param time time
     * @return
     */
    public static String setLastSeenTime(String time) {

        long milliseconds = Math.abs(System.currentTimeMillis()
                - convertTimeStringINToMillis(time));

        String lastSeen = "";

        long seconds = (long) milliseconds / 1000;
        if (seconds < 60)
            lastSeen = String.valueOf(seconds) + " sec";
        else if (seconds > 60 && seconds < 3600)
            lastSeen = String.valueOf((int) seconds / 60) + " min";
        else if (seconds > 3600 && seconds < 86400) {
            int hour = ((int) seconds / 3600);
            if (hour == 1) {
                lastSeen = String.valueOf(hour) + " hr";
            } else {
                lastSeen = String.valueOf(hour) + " hrs";
            }

        } else if (seconds > 86400 && seconds < 172800)
            lastSeen = " 1 d";
        else if (seconds > 172800 && seconds < 2592000)
            lastSeen = String.valueOf((int) (seconds / (24 * 3600)))
                    + " d";
        else if (seconds > 2592000) {
            int month = (int) (seconds / (30 * 24 * 3600));
            if (month == 1) {
                lastSeen = String.valueOf(month) + " mth";
            } else {
                if (month > 12) {
                    lastSeen = String.valueOf(month / 12) + "." + String.valueOf(month % 12) + " yrs";
                } else {
                    lastSeen = String.valueOf(month) + " moths";
                }
            }
        }
        return lastSeen;
    }

    /**
     * Function to convert milliseconds time to
     * Timer Format
     * Hours:Minutes:Seconds
     */
    public static String formateMilliSeccond(long milliseconds) {
        String finalTimerString = "";
        String secondsString = "";

        // Convert total duration into time
        int hours = (int) (milliseconds / (1000 * 60 * 60));
        int minutes = (int) (milliseconds % (1000 * 60 * 60)) / (1000 * 60);
        int seconds = (int) ((milliseconds % (1000 * 60 * 60)) % (1000 * 60) / 1000);

        // Add hours if there
        if (hours > 0) {
            finalTimerString = hours + ":";
        }

        // Prepending 0 to seconds if it is one digit
        if (seconds < 10) {
            secondsString = "0" + seconds;
        } else {
            secondsString = "" + seconds;
        }

        finalTimerString = finalTimerString + minutes + ":" + secondsString;

        //      return  String.format("%02d Min, %02d Sec",
        //                TimeUnit.MILLISECONDS.toMinutes(milliseconds),
        //                TimeUnit.MILLISECONDS.toSeconds(milliseconds) -
        //                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(milliseconds)));

        // return timer string
        return finalTimerString;
    }

    /**
     * @param time time
     * @return result
     */
    public static String setLastSeenMinTime(String time) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.getDefault());
        SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm a", Locale.getDefault());
        Date date;
        String returnTime = "";
        try {

            simpleDateFormat.setLenient(false);
            simpleDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
            date = simpleDateFormat.parse(time);
            returnTime = timeFormat.format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (!returnTime.equals("")) {
            return returnTime;
        } else {
            return time;
        }
    }

    /**
     * display date
     *
     * @param time String time
     * @return date format
     */
    public static String getDateWithOutFeture(long time) {
        // convert seconds to milliseconds
        Date date = new Date(time * 1000L);

        long milliseconds = Math.abs(System.currentTimeMillis() - date.getTime());

        String lastSeen = "";

        long seconds = (long) milliseconds / 1000;
        if (seconds < 60)
            lastSeen = String.valueOf(seconds) + " sec ago";
        else if (seconds > 60 && seconds < 3600)
            lastSeen = String.valueOf((int) seconds / 60) + " min ago";
        else if (seconds > 3600 && seconds < 86400) {
            int hour = ((int) seconds / 3600);
            if (hour == 1) {
                lastSeen = String.valueOf(hour) + " hour ago";
            } else {
                lastSeen = String.valueOf(hour) + " hours ago";
            }

        } else if (seconds > 86400 && seconds < 172800)
            lastSeen = " Yesterday";
        else if (seconds > 172800 && seconds < 2592000)
            lastSeen = String.valueOf((int) (seconds / (24 * 3600)))
                    + " Days";
        else if (seconds > 2592000) {
            int month = (int) (seconds / (30 * 24 * 3600));
            if (month == 1) {
                lastSeen = String.valueOf(month) + " Month";
            } else {
                if (month > 12) {
                    lastSeen = String.valueOf(month / 12) + "." + String.valueOf(month % 12) + " Years";
                } else {
                    lastSeen = String.valueOf(month) + " Months";
                }

            }
        }
        return lastSeen;
    }

    /**
     * @param time time
     * @return display
     */
    public static String setTimeDifference(long time) {
        // convert seconds to milliseconds
        Date date = new Date(time * 1000L);

        long milliseconds = Math.abs(System.currentTimeMillis() - date.getTime());

        String lastSeen = "";

        long seconds = (long) milliseconds / 1000;
        if (seconds < 60)
            lastSeen = String.valueOf(seconds) + " sec ago";
        else if (seconds > 60 && seconds < 3600)
            lastSeen = String.valueOf((int) seconds / 60) + " min ago";
        else if (seconds > 3600 && seconds < 86400) {
            int hour = ((int) seconds / 3600);
            if (hour == 1) {
                lastSeen = String.valueOf(hour) + " hour ago";
            } else {
                lastSeen = String.valueOf(hour) + " hours ago";
            }

        } else if (seconds > 86400 && seconds < 172800)
            lastSeen = " Yesterday";
        else if (seconds > 172800 && seconds < 2592000)
            lastSeen = String.valueOf((int) (seconds / (24 * 3600)))
                    + " days ago";
        else if (seconds > 2592000) {
            int month = (int) (seconds / (30 * 24 * 3600));
            if (month == 1) {
                lastSeen = String.valueOf(month) + " month ago";
            } else {
                if (month > 12) {
                    lastSeen = String.valueOf(month / 12) + "." + String.valueOf(month % 12) + " years ago";
                } else {
                    lastSeen = String.valueOf(month) + " months ago";
                }

            }
        }
        return lastSeen;
    }

    public String parseDateToddMMyyyy(String time) {
        String inputPattern = "yyyy-MM-dd HH:mm:ss";
        String outputPattern = "dd-MMM-yyyy h:mm a";
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);

        Date date = null;
        String str = null;

        try {
            date = inputFormat.parse(time);
            str = outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return str;
    }

    /**
     * converting time to milli second
     *
     * @param time
     * @return
     */
    private static long convertTimeStringINToMillis(String time) {

        long milliseconds = 0;
        try {
            if (time != null) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
                Date date = sdf.parse(time);
                milliseconds = date.getTime();
            }
        } catch (ParseException e) {
            milliseconds = 0;
            e.printStackTrace();
        }
        return milliseconds;
    }

    /**
     * converting time to milli second
     *
     * @param time time
     * @return
     */
    private static long convertTimeStringMillis(String time) {

        long milliseconds = 0;
        try {
            if (time != null) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date date = sdf.parse(time);
                milliseconds = date.getTime();
            }
        } catch (ParseException e) {
            milliseconds = 0;
            e.printStackTrace();
        }
        return milliseconds;
    }
}
