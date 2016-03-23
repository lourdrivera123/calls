package com.example.vbfc_bry07.calls;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class Helpers {

    public Helpers() {

    }

    public String convertToAlphabetDate(String input) {
        SimpleDateFormat outputFormat = null;
        Date date = null;

        try {
            outputFormat = new SimpleDateFormat("MMM dd", Locale.getDefault());
            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            date = inputFormat.parse(input);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return outputFormat.format(date);
    }

    public String convertToDayOfWeek(String input) {
        Date date = null;
        SimpleDateFormat sdf = null;

        try {
            sdf = new SimpleDateFormat("EEEE", Locale.getDefault());
            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            date = inputFormat.parse(input);

        } catch (Exception e) {
            e.printStackTrace();
        }
        assert sdf != null;
        return sdf.format(date);
    }

    public String getCurrentDate(String type) {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat df;

        if (type.equals("timestamp")) {
            df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        } else
            df = new SimpleDateFormat("yyyy-MM-dd");

        df.setTimeZone(TimeZone.getTimeZone("GMT+8"));

        return df.format(cal.getTime());
    }

    public int convertDateToCycleMonth(String date) {
        Date date1;
        Calendar cal = Calendar.getInstance();

        try {
            SimpleDateFormat idf = new SimpleDateFormat("yyyy-MM-dd");
            date1 = idf.parse(date);
            cal.setTime(date1);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return (cal.get(Calendar.MONTH) + 1);
    }

    public String getTodaysDate() {
        final Calendar c = Calendar.getInstance();
        int todaysDate = (c.get(Calendar.YEAR) * 10000) +
                ((c.get(Calendar.MONTH) + 1) * 100) +
                (c.get(Calendar.DAY_OF_MONTH));

        return (String.valueOf(todaysDate));
    }

    public String getCurrentTime() {
        final Calendar c = Calendar.getInstance();
        int currentTime = (c.get(Calendar.HOUR_OF_DAY) * 10000) +
                (c.get(Calendar.MINUTE) * 100) +
                (c.get(Calendar.SECOND));

        return (String.valueOf(currentTime));
    }
}
