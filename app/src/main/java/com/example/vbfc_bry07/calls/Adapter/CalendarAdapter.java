package com.example.vbfc_bry07.calls.Adapter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.vbfc_bry07.calls.Activity.MCPActivity;
import com.example.vbfc_bry07.calls.Helpers;
import com.example.vbfc_bry07.calls.R;

public class CalendarAdapter extends BaseAdapter {
    Context context;
    java.util.Calendar month;
    public Calendar pmonth, pmonthmaxset;
    DateFormat df;

    int firstDay, maxWeeknumber, maxP, calMaxP, mnthlength;
    String itemvalue, currentDateString;

    public static List<String> day_string;
    ArrayList<HashMap<String, ArrayList<HashMap<String, String>>>> plans;

    TextView dayView, with_plan;
    View previousView;
    LinearLayout parent_layout;

    Helpers helpers;

    public CalendarAdapter(Context context, Calendar monthCalendar, ArrayList<HashMap<String, ArrayList<HashMap<String, String>>>> plans_objects) {
        CalendarAdapter.day_string = new ArrayList<>();
        this.month = monthCalendar;
        this.context = context;
        this.plans = plans_objects;
        helpers = new Helpers();
        df = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        currentDateString = df.format(month.getTime());
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

    public View getView(final int position, View convertView, ViewGroup parent) {
        View v = convertView;

        if (convertView == null) {
            LayoutInflater vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(R.layout.cal_item, parent, false);
        }

        dayView = (TextView) v.findViewById(R.id.date);
        with_plan = (TextView) v.findViewById(R.id.with_plan);
        parent_layout = (LinearLayout) v.findViewById(R.id.parent_layout);

        dayView.setTag(position);
        parent_layout.setTag(with_plan);

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
        } else
            dayView.setTextColor(Color.BLACK);

        if (day_string.get(position).equals(currentDateString)) {
            MCPActivity.picked_date.setText(helpers.convertToAlphabetDate(currentDateString));
            MCPActivity.picked_day.setText(helpers.convertToDayOfWeek(currentDateString));
            MCPActivity.date = currentDateString;
            dayView.setTextColor(Color.parseColor("#A45F6E"));
        }

        dayView.setText(gridvalue);
        setEventView(position);

        return v;
    }

    public View setSelected(View view, int pos) {
        if (previousView != null)
            previousView.setBackgroundColor(Color.WHITE);

        view.setBackgroundColor(Color.parseColor("#90413B41"));
        MCPActivity.picked_date.setText(helpers.convertToAlphabetDate(day_string.get(pos)));
        MCPActivity.picked_day.setText(helpers.convertToDayOfWeek(day_string.get(pos)));

        if (day_string.size() > pos)
            previousView = view;

        return view;
    }

    public void refreshDays() {
        day_string.clear();
        pmonth = (Calendar) month.clone();
        // month start day. ie; sun, mon, etc
        firstDay = month.get(Calendar.DAY_OF_WEEK);
        // finding number of weeks in current month.
        maxWeeknumber = month.getActualMaximum(Calendar.WEEK_OF_MONTH);
        // allocating maximum row number for the gridview.
        mnthlength = maxWeeknumber * 7;
        maxP = getMaxP(); // previous month maximum day 31,30....
        calMaxP = maxP - (firstDay - 1);// calendar offday starting 24,25 ...

        //Calendar instance for getting a complete gridview including the three month's (previous,current,next) dates.
        pmonthmaxset = (Calendar) pmonth.clone();

        //setting the start date as previous month's required date.
        pmonthmaxset.set(Calendar.DAY_OF_MONTH, calMaxP + 1);

        //filling calendar gridview.
        for (int n = 0; n < mnthlength; n++) {
            itemvalue = df.format(pmonthmaxset.getTime());
            pmonthmaxset.add(Calendar.DATE, 1);
            day_string.add(itemvalue);
        }

        this.notifyDataSetChanged();
    }

    private int getMaxP() {
        int maxP;
        if (month.get(Calendar.MONTH) == month
                .getActualMinimum(Calendar.MONTH)) {
            pmonth.set((month.get(Calendar.YEAR) - 1),
                    month.getActualMaximum(Calendar.MONTH), 1);
        } else {
            pmonth.set(Calendar.MONTH,
                    month.get(Calendar.MONTH) - 1);
        }
        maxP = pmonth.getActualMaximum(Calendar.DAY_OF_MONTH);

        return maxP;
    }

    public void setEventView(final int pos) {
        int len = plans.size();
        String date_selected = day_string.get(pos);

        for (int x = 0; x < len; x++) {
            HashMap<String, ArrayList<HashMap<String, String>>> map_per_day = plans.get(x);
            String date_on_map = map_per_day.keySet().toString().replace("[", "").replace("]", "");

            if (date_on_map.equals(date_selected))
                with_plan.setVisibility(View.VISIBLE);
        }
    }
}
