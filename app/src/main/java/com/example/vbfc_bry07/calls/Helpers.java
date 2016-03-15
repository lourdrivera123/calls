package com.example.vbfc_bry07.calls;

import android.widget.ExpandableListView;

import com.example.vbfc_bry07.calls.Adapter.ExpandableListAdapter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
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
}
