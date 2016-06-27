package com.ece.vbfc_bry07.calls.bdm_adapter;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ece.vbfc_bry07.calls.Helpers;
import com.ece.vbfc_bry07.calls.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class CalendarCallAdapter extends BaseAdapter {
    Context context;

    List<String> days;

    TextView date, count_calls;
    LinearLayout parent_layout;

    DateFormat df;
    Calendar month_clone, month, pmonthmaxset;

    String itemvalue;
    int firstDay, maxWeeknumber, maxP, calMaxP, mnthlength;

    public CalendarCallAdapter(Context context, Calendar calendar) {
        this.context = context;
        this.month = calendar;

        df = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        df.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        days = new ArrayList<>();

        setDays();
    }

    @Override
    public int getCount() {
        return days.size();
    }

    @Override
    public Object getItem(int position) {
        return days.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;

        if (convertView == null) {
            LayoutInflater vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(R.layout.item_calendar_call, parent, false);
        }

        date = (TextView) v.findViewById(R.id.date);
        count_calls = (TextView) v.findViewById(R.id.count_calls);
        parent_layout = (LinearLayout) v.findViewById(R.id.parent_layout);

        date.setTag(position);

        String[] separatedTime = days.get(position).split("-");
        String gridvalue = separatedTime[2].replaceFirst("^0*", "");

        if ((Integer.parseInt(gridvalue) > 1) && (position < firstDay)) {
            date.setTextColor(Color.GRAY);
            count_calls.setVisibility(View.INVISIBLE);
            parent_layout.setEnabled(false);
        } else if ((Integer.parseInt(gridvalue) < 7) && (position > 28)) {
            date.setTextColor(Color.GRAY);
            count_calls.setVisibility(View.INVISIBLE);
            parent_layout.setEnabled(false);
        } else {
            date.setTextColor(Color.BLACK);
            count_calls.setVisibility(View.VISIBLE);
        }

        date.setText(new Helpers().convertToAlphabetDate(days.get(position), ""));
        count_calls.setText("0");

        return v;

    }

    void setDays() {
        days.clear();
        month_clone = (Calendar) month.clone();
        Calendar frstDay_cal = (Calendar) month.clone();
        frstDay_cal.set(Calendar.DAY_OF_MONTH, 1);

        // month start day. ie; sun, mon, etc
        firstDay = frstDay_cal.get(Calendar.DAY_OF_WEEK);
        // finding number of weeks in current month.
        maxWeeknumber = month.getActualMaximum(Calendar.WEEK_OF_MONTH);
        // allocating maximum row number for the gridview.
        mnthlength = maxWeeknumber * 7;
        maxP = getMaxP(); // previous month maximum day 31,30....
        calMaxP = maxP - (firstDay - 2);// calendar offday starting 24,25 ...
        //Calendar instance for getting a complete gridview including the three month's (previous,current,next) dates.
        pmonthmaxset = (Calendar) month_clone.clone();

        //setting the start date as previous month's required date.
        pmonthmaxset.set(Calendar.DAY_OF_MONTH, calMaxP);

        //filling calendar gridview.
        for (int n = 0; n < mnthlength; n++) {
            itemvalue = df.format(pmonthmaxset.getTime());
            pmonthmaxset.add(Calendar.DATE, 1);
            days.add(itemvalue);
        }

        this.notifyDataSetChanged();
    }

    private int getMaxP() {
        int maxP;

        if (month.get(Calendar.MONTH) == month.getActualMinimum(Calendar.MONTH))
            month_clone.set((month.get(Calendar.YEAR) - 1), month.getActualMaximum(Calendar.MONTH), 1);
        else
            month_clone.set(Calendar.MONTH, month.get(Calendar.MONTH) - 1);

        maxP = month_clone.getActualMaximum(Calendar.DAY_OF_MONTH);

        return maxP;
    }
}
