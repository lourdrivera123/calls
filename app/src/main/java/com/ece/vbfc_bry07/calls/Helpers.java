package com.ece.vbfc_bry07.calls;

import java.text.DateFormat;
import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class Helpers {

    public Helpers() {

    }

    public String convertToAlphabetDate(String input, String type) {
        SimpleDateFormat outputFormat = null;
        Date date = null;
        SimpleDateFormat date_format = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

        try {
            switch (type) {
                case "complete":
                    outputFormat = new SimpleDateFormat("MMM dd yyyy", Locale.getDefault());
                    break;
                case "without_year":
                    outputFormat = new SimpleDateFormat("MM-dd", Locale.getDefault());
                    break;
                default:
                    outputFormat = new SimpleDateFormat("MMM dd", Locale.getDefault());
                    break;
            }

            date = date_format.parse(input);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return outputFormat.format(date);
    }

    public String convertToDayOfWeek(String input) {
        Date date = null;
        SimpleDateFormat sdf = null;
        SimpleDateFormat date_format = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

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
        Calendar cal = Calendar.getInstance();

        if (type.equals("timestamp")) {
            df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        } else
            df = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

        df.setTimeZone(TimeZone.getTimeZone("GMT+8"));

        return df.format(cal.getTime());
    }

    public String getFirstDateOfMonth(int month) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_MONTH, 1);
        cal.set(Calendar.MONTH, month);
        df.setTimeZone(TimeZone.getTimeZone("GMT+8"));

        return df.format(cal.getTime());
    }

    public String getMonthYear() {
        TimeZone zone = TimeZone.getTimeZone("GMT+8");
        Calendar cal = Calendar.getInstance(zone);

        return String.valueOf(android.text.format.DateFormat.format("MMMM yyyy", cal));
    }

    public String Add1MonthToDate(String date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        TimeZone zone = TimeZone.getTimeZone("GMT+8");
        Calendar c = Calendar.getInstance(zone);
        String return_date = "";

        try {
            c.setTime(sdf.parse(date));
            int week_of_month = c.get(Calendar.WEEK_OF_MONTH);
            int day_of_week = c.get(Calendar.DAY_OF_WEEK);

            c.add(Calendar.MONTH, 1);
            int orig_month = c.get(Calendar.MONTH) + 1;
            String orig_date = sdf.format(c.getTime());
            c.set(Calendar.WEEK_OF_MONTH, week_of_month);
            c.set(Calendar.DAY_OF_WEEK, day_of_week);
            int after_month = c.get(Calendar.MONTH) + 1;

            if (orig_month != after_month) {
                if (convertToDayOfWeek(orig_date).equals("Sunday")) {
                    return_date = Add1DayToDate(orig_date);
                } else
                    return_date = orig_date;
            } else
                return_date = sdf.format(c.getTime());

        } catch (Exception e) {
            e.printStackTrace();
        }

        return return_date;
    }

    public String Add1DayToDate(String date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Calendar c = Calendar.getInstance();

        try {
            c.setTime(sdf.parse(date));
            c.add(Calendar.DATE, 1);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return sdf.format(c.getTime());
    }

    //////////////////////////////////////////////////////
    public int convertDateToCycleMonth(String date) {
        Date date1;
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat date_format = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

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
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat date_format = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

        try {
            date1 = date_format.parse(date);
            cal.setTime(date1);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return (cal.get(Calendar.YEAR));
    }

    public String convertIntToStringMonth(int month) {
        return new DateFormatSymbols().getMonths()[month - 1];
    }

    public Date convertStringToDate(String input) {
        Date date = null;

        try {
            DateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            date = format.parse(input);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    public String getTodaysDate() {
        final Calendar c = Calendar.getInstance();
        int todaysDate = (c.get(Calendar.YEAR) * 10000) +
                ((c.get(Calendar.MONTH) + 1) * 100) +
                (c.get(Calendar.DAY_OF_MONTH));

        return (String.valueOf(todaysDate));
    }

    public String getCurrentTime() {
        Calendar cal = Calendar.getInstance();

        int currentTime = (cal.get(Calendar.HOUR_OF_DAY) * 10000) +
                (cal.get(Calendar.MINUTE) * 100) +
                (cal.get(Calendar.SECOND));

        return (String.valueOf(currentTime));
    }
}
