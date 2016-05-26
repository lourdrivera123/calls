package com.ece.vbfc_bry07.calls.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ece.vbfc_bry07.calls.activity.ACPActivity;
import com.ece.vbfc_bry07.calls.dialog.ViewCycleMonth;
import com.ece.vbfc_bry07.calls.Helpers;
import com.ece.vbfc_bry07.calls.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class ACPCalendarAdapter extends BaseAdapter {
    Calendar calendar, pmonth, pmonthmaxset;
    DateFormat df;

    TextView with_plan, count, date;
    LinearLayout parent_layout;

    String currentDateString, itemvalue;
    int firstDay, maxWeeknumber, maxP, calMaxP, mnthlength;

    Helpers helpers;

    List<String> day_string;
    ArrayList<HashMap<String, String>> list_of_calls;
    ViewCycleMonth activity;

    public ACPCalendarAdapter(ViewCycleMonth context, Calendar calendar, ArrayList<HashMap<String, String>> list_of_calls) {
        this.activity = context;
        this.calendar = calendar;
        this.list_of_calls = list_of_calls;

        day_string = new ArrayList<>();
        helpers = new Helpers();
        df = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        df.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        currentDateString = helpers.getCurrentDate("date");
        refreshDays();
    }

    @Override
    public int getCount() {
        return day_string.size();
    }

    public Object getItem(int position) {
        return day_string.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View v = convertView;

        if (convertView == null) {
            LayoutInflater vi = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(R.layout.item_acp_calendar, parent, false);
        }

        with_plan = (TextView) v.findViewById(R.id.with_plan);
        count = (TextView) v.findViewById(R.id.count);
        date = (TextView) v.findViewById(R.id.date);
        parent_layout = (LinearLayout) v.findViewById(R.id.parent_layout);

        date.setTag(position);

        String[] separatedTime = day_string.get(position).split("-");
        String gridvalue = separatedTime[2].replaceFirst("^0*", "");

        if ((Integer.parseInt(gridvalue) > 1) && (position < firstDay)) {
            date.setTextColor(Color.GRAY);
            parent_layout.setEnabled(false);
            with_plan.setVisibility(View.INVISIBLE);
            count.setVisibility(View.INVISIBLE);
        } else if ((Integer.parseInt(gridvalue) < 7) && (position > 28)) {
            date.setTextColor(Color.GRAY);
            parent_layout.setEnabled(false);
            with_plan.setVisibility(View.INVISIBLE);
            count.setVisibility(View.INVISIBLE);
        } else {
            date.setTextColor(Color.BLACK);
            with_plan.setVisibility(View.INVISIBLE);
            count.setVisibility(View.INVISIBLE);

            if (day_string.get(position).equals(currentDateString))
                date.setTextColor(Color.parseColor("#A45F6E"));

            for (int x = 0; x < list_of_calls.size(); x++) {
                if (list_of_calls.get(x).get("date").equals(day_string.get(position))) {
                    with_plan.setVisibility(View.VISIBLE);
                    parent_layout.setEnabled(true);
                    count.setVisibility(View.VISIBLE);
                    count.setText(list_of_calls.get(x).get("dividend") + " / " + list_of_calls.get(x).get("divisor"));
                }
            }
        }

        date.setText(gridvalue);

        parent_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int check = 0;

                for (int x = 0; x < list_of_calls.size(); x++) {
                    if (list_of_calls.get(x).get("date").equals(day_string.get(position))) {
                        ACPActivity.viewotheracp = day_string.get(position);
                        check += 1;
                        activity.finish();
                        break;
                    }
                }

                if (check == 0) {
                    if (day_string.get(position).equals(helpers.getCurrentDate("date"))) {
                        ACPActivity.viewotheracp = day_string.get(position);
                        activity.finish();
                    }
                }
            }
        });

        return v;
    }

    public void refreshDays() {
        day_string.clear();
        pmonth = (Calendar) calendar.clone();
        Calendar frstDay_cal = (Calendar) calendar.clone();
        frstDay_cal.set(Calendar.DAY_OF_MONTH, 1);

        // month start day. ie; sun, mon, etc
        firstDay = frstDay_cal.get(Calendar.DAY_OF_WEEK);
        // finding number of weeks in current month.
        maxWeeknumber = calendar.getActualMaximum(Calendar.WEEK_OF_MONTH);
        // allocating maximum row number for the gridview.
        mnthlength = maxWeeknumber * 7;
        maxP = getMaxP(); // previous month maximum day 31,30....
        calMaxP = maxP - (firstDay - 2);// calendar offday starting 24,25 ...
        //Calendar instance for getting a complete gridview including the three month's (previous,current,next) dates.
        pmonthmaxset = (Calendar) pmonth.clone();

        //setting the start date as previous month's required date.
        pmonthmaxset.set(Calendar.DAY_OF_MONTH, calMaxP);

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
        if (calendar.get(Calendar.MONTH) == calendar
                .getActualMinimum(Calendar.MONTH)) {
            pmonth.set((calendar.get(Calendar.YEAR) - 1),
                    calendar.getActualMaximum(Calendar.MONTH), 1);
        } else {
            pmonth.set(Calendar.MONTH,
                    calendar.get(Calendar.MONTH) - 1);
        }
        maxP = pmonth.getActualMaximum(Calendar.DAY_OF_MONTH);

        return maxP;
    }
}
