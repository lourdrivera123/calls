package com.example.vbfc_bry07.calls;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class Helpers {
    Calendar cal;
    SimpleDateFormat date_format;

    public Helpers() {
        cal = Calendar.getInstance();
        date_format = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
    }

    public String convertToAlphabetDate(String input) {
        SimpleDateFormat outputFormat = null;
        Date date = null;

        try {
            outputFormat = new SimpleDateFormat("MMM dd", Locale.getDefault());
            date = date_format.parse(input);
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
            date = date_format.parse(input);

        } catch (Exception e) {
            e.printStackTrace();
        }
        assert sdf != null;
        return sdf.format(date);
    }

    public String getCurrentDate(String type) {
        SimpleDateFormat df;

        if (type.equals("timestamp")) {
            df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        } else
            df = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

        df.setTimeZone(TimeZone.getTimeZone("GMT+8"));

        return df.format(cal.getTime());
    }

    public int convertDateToCycleMonth(String date) {
        Date date1;

        try {
            date1 = date_format.parse(date);
            cal.setTime(date1);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return (cal.get(Calendar.MONTH) + 1);
    }

    public int convertDateToCycleSet(String date) {
        Date date1;

        try {
            date1 = date_format.parse(date);
            cal.setTime(date1);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return (cal.get(Calendar.YEAR));
    }

    public String getTodaysDate() {
        final Calendar c = Calendar.getInstance();
        int todaysDate = (c.get(Calendar.YEAR) * 10000) +
                ((c.get(Calendar.MONTH) + 1) * 100) +
                (c.get(Calendar.DAY_OF_MONTH));

        return (String.valueOf(todaysDate));
    }

    public String getCurrentTime() {
        int currentTime = (cal.get(Calendar.HOUR_OF_DAY) * 10000) +
                (cal.get(Calendar.MINUTE) * 100) +
                (cal.get(Calendar.SECOND));

        return (String.valueOf(currentTime));
    }
}
