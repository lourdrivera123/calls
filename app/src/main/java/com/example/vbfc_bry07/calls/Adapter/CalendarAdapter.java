package com.example.vbfc_bry07.calls.Adapter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.vbfc_bry07.calls.CalendarCollection;
import com.example.vbfc_bry07.calls.R;

public class CalendarAdapter extends BaseAdapter {
    private Context context;
    private java.util.Calendar month;
    public GregorianCalendar pmonth, pmonthmaxset;
    DateFormat df;
    private View previousView;

    int firstDay, maxWeeknumber, maxP, calMaxP, mnthlength;
    String itemvalue, currentDateString;

    public static List<String> day_string;
    public ArrayList<CalendarCollection> date_collection_arr;

    TextView dayView, with_plan;

    public CalendarAdapter(Context context, GregorianCalendar monthCalendar, ArrayList<CalendarCollection> date_collection_arr) {
        this.date_collection_arr = date_collection_arr;
        CalendarAdapter.day_string = new ArrayList<>();
        month = monthCalendar;
        Locale.setDefault(Locale.ENGLISH);
        GregorianCalendar selectedDate = (GregorianCalendar) monthCalendar.clone();
        month.set(GregorianCalendar.DAY_OF_MONTH, 1);

        this.context = context;
        df = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        currentDateString = df.format(selectedDate.getTime());
        refreshDays();
    }

    public int getCount() {
        return day_string.size();
    }

    public Object getItem(int position) {
        return day_string.get(position);
    }

    public long getItemId(int position) {
        return 0;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;

        if (convertView == null) {
            LayoutInflater vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(R.layout.cal_item, parent, false);
        }

        dayView = (TextView) v.findViewById(R.id.date);
        with_plan = (TextView) v.findViewById(R.id.with_plan);

        dayView.setTag(position);
        with_plan.setTag(position);

        String[] separatedTime = day_string.get(position).split("-");
        String gridvalue = separatedTime[2].replaceFirst("^0*", "");

        if ((Integer.parseInt(gridvalue) > 1) && (position < firstDay)) {
            dayView.setTextColor(Color.GRAY);
            dayView.setClickable(false);
            dayView.setFocusable(false);
        } else if ((Integer.parseInt(gridvalue) < 7) && (position > 28)) {
            dayView.setTextColor(Color.GRAY);
            dayView.setClickable(false);
            dayView.setFocusable(false);
        }

        if (day_string.get(position).equals(currentDateString))
            dayView.setTextColor(Color.parseColor("#309635"));

        dayView.setText(gridvalue);
        setEventView(position);

        return v;
    }

    public View setSelected(View view, int pos) {
        if (previousView != null)
            previousView.setBackgroundColor(Color.WHITE);

        view.setBackgroundColor(Color.parseColor("#9098FF98"));

        if (day_string.size() > pos) {
            if (!day_string.get(pos).equals(currentDateString))
                previousView = view;
        }

        return view;
    }

    public void refreshDays() {
        day_string.clear();
        pmonth = (GregorianCalendar) month.clone();
        // month start day. ie; sun, mon, etc
        firstDay = month.get(GregorianCalendar.DAY_OF_WEEK);
        // finding number of weeks in current month.
        maxWeeknumber = month.getActualMaximum(GregorianCalendar.WEEK_OF_MONTH);
        // allocating maximum row number for the gridview.
        mnthlength = maxWeeknumber * 7;
        maxP = getMaxP(); // previous month maximum day 31,30....
        calMaxP = maxP - (firstDay - 1);// calendar offday starting 24,25 ...

        //Calendar instance for getting a complete gridview including the three month's (previous,current,next) dates.
        pmonthmaxset = (GregorianCalendar) pmonth.clone();

        //setting the start date as previous month's required date.
        pmonthmaxset.set(GregorianCalendar.DAY_OF_MONTH, calMaxP + 1);

        //filling calendar gridview.
        for (int n = 0; n < mnthlength; n++) {
            itemvalue = df.format(pmonthmaxset.getTime());
            pmonthmaxset.add(GregorianCalendar.DATE, 1);
            day_string.add(itemvalue);
        }

        this.notifyDataSetChanged();
    }

    private int getMaxP() {
        int maxP;
        if (month.get(GregorianCalendar.MONTH) == month
                .getActualMinimum(GregorianCalendar.MONTH)) {
            pmonth.set((month.get(GregorianCalendar.YEAR) - 1),
                    month.getActualMaximum(GregorianCalendar.MONTH), 1);
        } else {
            pmonth.set(GregorianCalendar.MONTH,
                    month.get(GregorianCalendar.MONTH) - 1);
        }
        maxP = pmonth.getActualMaximum(GregorianCalendar.DAY_OF_MONTH);

        return maxP;
    }


    public void setEventView(int pos) {
        int len = CalendarCollection.date_collection_arr.size();

        for (int i = 0; i < len; i++) {
            CalendarCollection cal_obj = CalendarCollection.date_collection_arr.get(i);

            String date = cal_obj.date;
            int len1 = day_string.size();

            if (len1 > pos) {
                if (day_string.get(pos).equals(date)) {
                    with_plan.setVisibility(View.VISIBLE);
//                    v.setBackgroundResource(R.mipmap.rounded_calendar_item);
                    break;
                } else
                    with_plan.setVisibility(View.INVISIBLE);
//                    v.setBackgroundResource(0);
            }
        }
    }


    public void getPositionList(String date) {
        int len = CalendarCollection.date_collection_arr.size();

        for (int i = 0; i < len; i++) {
            CalendarCollection cal_collection = CalendarCollection.date_collection_arr.get(i);
            String event_date = cal_collection.date;

            String event_message = cal_collection.event_message;

            if (date.equals(event_date)) {
//                Toast.makeText(context, "You have event on this date: " + event_date, Toast.LENGTH_LONG).show();
                break;
            }
        }
    }
}
