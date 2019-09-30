package com.qclibrary.lib.utils;

import android.content.Context;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;


public class QcTimeUtils {

    public static final int SECOND = 0;
    private static final int MINUTE = 0;
    private static final int HOUR = 0;
    private static final int DAY = 0;
    private static final String PATTERN_YY_DD_MM_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";
    private static final String PATTERN_YY_DD_MM = "yyyy-MM-dd";
    private static final String PATTERN_HH_MM_SS = "HH:mm:ss";
    private static final String DEFAULT_PATTERN = PATTERN_YY_DD_MM_HH_MM_SS;

    public static String millis2String(long millis) {
        return new SimpleDateFormat(DEFAULT_PATTERN, Locale.getDefault()).format(new Date(millis));
    }

    public static String millis2String(long millis, String pattern) {
        return new SimpleDateFormat(pattern, Locale.getDefault()).format(new Date(millis));
    }

    public static String millisToHour(long millis, Context context) {
        if (is24HourMode(context)) {
            return millisTo24Hour(millis);
        } else {
            return millisTo12Hour(millis) + " " + getApm(millis);
        }
    }

    public static String millisTo24Hour(long millis) {
        return new SimpleDateFormat("HH:mm", Locale.getDefault()).format(new Date(millis));
    }

    public static String millisTo12Hour(long millis) {
        return new SimpleDateFormat("h:mm", Locale.getDefault()).format(new Date(millis));
    }


    public static boolean is24HourMode(final Context context) {
        return android.text.format.DateFormat.is24HourFormat(context);
    }

    public static String getApm(long mills) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(mills);
        return cal.get(Calendar.AM_PM) == 0 ? "AM" : "PM";
    }

    public static String getMonthName(long mills) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date(mills));
        int month = cal.get(Calendar.MONTH);
        if (month == 0) {
            return "Jan";
        } else if (month == 1) {
            return "Feb";
        } else if (month == 2) {
            return "Mar";
        } else if (month == 3) {
            return "Apr";
        } else if (month == 4) {
            return "May";
        } else if (month == 5) {
            return "Jun";
        } else if (month == 6) {
            return "Jul";
        } else if (month == 7) {
            return "Aug";
        } else if (month == 8) {
            return "Sep";
        } else if (month == 9) {
            return "Oct";
        } else if (month == 10) {
            return "Nov";
        } else if (month == 11) {
            return "Dec";
        } else {
            return "";
        }
    }

    public static String getWeekName(long mills) {
        int week = getWeekIndex(mills);
        if (week == 1) {
            return "Sun";
        } else if (week == 2) {
            return "Mon";
        } else if (week == 3) {
            return "Tue";
        } else if (week == 4) {
            return "Wed";
        } else if (week == 5) {
            return "Thu";
        } else if (week == 6) {
            return "Fri";
        } else if (week == 7) {
            return "Sat";
        } else {
            return "";
        }
    }

    public static String getWeekHoleName(long mills) {
        int week = getWeekIndex(mills);
        if (week == 1) {
            return "Sunday";
        } else if (week == 2) {
            return "Monday";
        } else if (week == 3) {
            return "Tuesday";
        } else if (week == 4) {
            return "Wednesday";
        } else if (week == 5) {
            return "Thursday";
        } else if (week == 6) {
            return "Friday";
        } else if (week == 7) {
            return "Saturday";
        } else {
            return "";
        }
    }

    private static int getWeekIndex(long mills) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date(mills));
        return cal.get(Calendar.DAY_OF_WEEK);
    }


    private static int getAge(long birthday) {
        if (birthday == 0)
            return 0;
        Date dateNow = new Date(System.currentTimeMillis());
        Date dateBirthday = new Date(birthday);
        return dateNow.getYear() - dateBirthday.getYear();
    }

    public static int getRemainDay(long expireTime) {
        if (expireTime == 0)
            return 0;
        long nd = 1000 * 24 * 60 * 60;
        long remainDay = (expireTime - System.currentTimeMillis()) / nd;
        long remainTime = (expireTime - System.currentTimeMillis()) % nd;
        if (remainDay > 0)  // >24h
            if (remainTime > 0) {
                remainDay++;
            }
        return (int) remainDay;
    }

    public static int getRemainDay(long startTime, long endTime) {
        if (startTime == 0 || endTime == 0)
            return 0;
        long nd = 1000 * 24 * 60 * 60;
        long remainDay = (endTime - startTime) / nd;
        long remainTime = (endTime - startTime) % nd;
        if (remainDay > 0)  // >24h
            if (remainTime > 0) {
                remainDay++;
            }
        return (int) remainDay;
    }

    public static String convertToHour(long millis) {
        return new SimpleDateFormat("h:mm aa", Locale.getDefault()).format(new Date(millis));
    }

    public static String convertToHourMin(long millis) {
        return new SimpleDateFormat("HH:mm", Locale.getDefault()).format(new Date(millis));
    }

    public static String convertToYearMonth(long millis) {
        return new SimpleDateFormat("MM/yy", Locale.getDefault()).format(new Date(millis));
    }

    public static String convertToDate(long millis) {
        return new SimpleDateFormat("MMM dd", Locale.getDefault()).format(new Date(millis));
    }

    public static String convertToMonth(long millis) {
        return new SimpleDateFormat("MMM", Locale.getDefault()).format(new Date(millis));
    }

    public static String convertToDay(long millis) {
        return new SimpleDateFormat("dd", Locale.getDefault()).format(new Date(millis));
    }


    public static String convertHour(long millis) {
        return new SimpleDateFormat("HH", Locale.getDefault()).format(new Date(millis));
    }

    public static String convertHour() {
        return new SimpleDateFormat("HH", Locale.getDefault()).format(new Date());
    }

    public static String convertToYearWithDate(long millis) {
        return new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date(millis));
    }

    public static String convertToYearWithDate() {
        return new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
    }

    public static boolean isToday(long millis) {
        Calendar c1 = Calendar.getInstance();

        Calendar c2 = Calendar.getInstance();
        c2.setTime(new Date(millis));

        return c1.get(Calendar.YEAR) == c2.get(Calendar.YEAR)
                && c1.get(Calendar.DAY_OF_YEAR) == c2.get(Calendar.DAY_OF_YEAR);
    }

    private static boolean isTomorrow(long millis) {
        Calendar c1 = Calendar.getInstance();
        c1.add(Calendar.DAY_OF_YEAR, 1);

        Calendar c2 = Calendar.getInstance();
        c2.setTime(new Date(millis));

        return c1.get(Calendar.YEAR) == c2.get(Calendar.YEAR)
                && c1.get(Calendar.DAY_OF_YEAR) == c2.get(Calendar.DAY_OF_YEAR);
    }

    public static String getTimeZoneId() {
        return TimeZone.getDefault().getID();
    }

    public static boolean isTheSameDay(long millis1, long millis2) {
        Calendar c1 = Calendar.getInstance();
        c1.setTimeInMillis(millis1);

        Calendar c2 = Calendar.getInstance();
        c2.setTimeInMillis(millis2);

        return c1.get(Calendar.YEAR) == c2.get(Calendar.YEAR)
                && c1.get(Calendar.DAY_OF_YEAR) == c2.get(Calendar.DAY_OF_YEAR);
    }

    public static boolean isBeforeToday(long millis) {
        Calendar c1 = Calendar.getInstance();

        Calendar c2 = Calendar.getInstance();
        c2.setTimeInMillis(millis);

        return c1.get(Calendar.YEAR) > c2.get(Calendar.YEAR)
                || c1.get(Calendar.DAY_OF_YEAR) > c2.get(Calendar.DAY_OF_YEAR);
    }

    public static String formatTimeInterval(long timeInterval) {
        int temp;
        int seconds = (int) (timeInterval / 1000);
        StringBuilder sb = new StringBuilder();
        if (seconds > 3600) {
            temp = seconds / 3600;
            sb.append(temp).append(":");
            temp = seconds % 3600 / 60;
            sb.append((temp < 10) ? "0" + temp + ":" : "" + temp + ":");
            temp = seconds % 3600 % 60;
            sb.append((temp < 10) ? "0" + temp : "" + temp);
        } else {
            temp = seconds % 3600 / 60;
            sb.append(temp).append(":");
            temp = seconds % 3600 % 60;
            sb.append((temp < 10) ? "0" + temp : "" + temp);
        }
        return sb.toString();
    }

    public static String formatPrettyTime(long millis) {
        if (isToday(millis)) {
            return "today";
        } else if (isTomorrow(millis)) {
            return "tomorrow";
        } else {
            return new SimpleDateFormat("MMM dd", Locale.getDefault()).format(new Date(millis));
        }
    }

    public static String formatPrettyWeekDayTime(long millis) {
        if (isToday(millis)) {
            return "today";
        } else if (isTomorrow(millis)) {
            return "tomorrow";
        } else {
            return getWeekHoleName(millis);
        }
    }


    public static long getTimeByStr(String time) {
        return getTimeByStr(time,"yyyy-MM-dd HH:mm");
    }

    public static long getTimeByStr(String time,String format){
        SimpleDateFormat sf = new SimpleDateFormat(format);
        try {
            Date date = sf.parse(time);
            if(date != null){
                return date.getTime();
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static int getDays(long time){
        int days = (int) TimeUnit.SECONDS.toDays(time/1000);
        return days;
    }

}