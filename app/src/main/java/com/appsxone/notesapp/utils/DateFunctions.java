package com.appsxone.notesapp.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DateFunctions {

    public static String getCompleteDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.getDefault());
        String currentDateandTime = sdf.format(new Date());
        return currentDateandTime;
    }

    public static String getCurrentDate() {
        Date todaysdate = new Date();
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
        String date = format.format(todaysdate);
        return date;
    }

    public static String getCalculatedDate(String date, int days) {
        Calendar cal = Calendar.getInstance();
        //SimpleDateFormat s = new SimpleDateFormat("dd-MM-yyyy");
        SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        if (!date.isEmpty()) {
            try {
                cal.setTime(s.parse(date));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        cal.add(Calendar.DAY_OF_YEAR, days);
        return s.format(new Date(cal.getTimeInMillis()));
    }

    public static String getCurrentTime() {
        return new SimpleDateFormat("HH : mm a", Locale.getDefault()).format(new Date());
    }
}