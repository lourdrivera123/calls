package com.ece.vbfc_bry07.calls.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ece.vbfc_bry07.calls.Activity.MCPActivity;
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

public class MCPCalendarAdapter extends BaseAdapter {
    DateFormat df;
    Context context;
    java.util.Calendar month;
    Calendar pmonth, pmonthmaxset;

    int firsttouch = 0;
    long time;
    String itemvalue, currentDateString;
    int firstDay, maxWeeknumber, maxP, calMaxP, mnthlength, public_pos = -1;

    List<String> day_string;

    View previousView;
    TextView dayView, with_plan;
    LinearLayout parent_layout;

    Helpers helpers;

    public MCPCalendarAdapter(Context context, Calendar monthCalendar) {
        day_string = new ArrayList<>();
        this.month = monthCalendar;
        this.context = context;
        helpers = new Helpers();
        df = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        df.setTimeZone(TimeZone.getTimeZone("GMT+8"));

        currentDateString = helpers.getCurrentDate("date");
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
            parent_layout.setEnabled(false);
        } else if ((Integer.parseInt(gridvalue) < 7) && (position > 28)) {
            dayView.setTextColor(Color.GRAY);
            parent_layout.setEnabled(false);
        } else {
            if (helpers.convertToDayOfWeek(day_string.get(position)).equals("Sunday")) {
                parent_layout.setEnabled(false);
                dayView.setTextColor(Color.GRAY);
            } else {
                parent.setEnabled(true);
                dayView.setTextColor(Color.BLACK);

                if (day_string.get(position).equals(currentDateString) && (month.get(Calendar.MONTH) + 1) == helpers.convertDateToCycleMonth(currentDateString))
                    dayView.setTextColor(Color.parseColor("#A45F6E"));
            }
        }

        dayView.setText(gridvalue);

        if (MCPActivity.isVisible == 1) {
            if (position == public_pos)
                with_plan.setVisibility(View.VISIBLE);
        } else if (MCPActivity.isVisible == 0) {
            with_plan.setVisibility(View.INVISIBLE);

            if (MCPActivity.list_of_dates.size() > 0) {
                for (int x = 0; x < MCPActivity.list_of_dates.size(); x++) {
                    if (day_string.get(position).equals(MCPActivity.list_of_dates.get(x)))
                        with_plan.setVisibility(View.VISIBLE);
                }
            }
        } else if (MCPActivity.isVisible == 3) {
            if (MCPActivity.child_clicked != null) {
                with_plan.setVisibility(View.INVISIBLE);
                String IDM_id = MCPActivity.child_clicked.get("IDM_id");

                for (int x = 0; x < MCPActivity.list_of_plans.size(); x++) {
                    String keyset = MCPActivity.list_of_plans.get(x).keySet().toString().replace("[", "").replace("]", "");

                    if (keyset.equals(IDM_id)) {
                        ArrayList<String> list_of_date = MCPActivity.list_of_plans.get(x).get(keyset);

                        for (int y = 0; y < list_of_date.size(); y++) {
                            if (list_of_date.get(y).equals(day_string.get(position)))
                                with_plan.setVisibility(View.VISIBLE);
                        }

                        break;
                    }
                }
            }

        }

        parent_layout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == event.ACTION_DOWN) {
                    TextView plan = (TextView) v.getTag();

                    if (firsttouch == 1 && (System.currentTimeMillis() - time) <= 300) {
                        firsttouch += 1;

                        if (MCPActivity.isVisible >= 1 && plan.getVisibility() == View.VISIBLE) {

                            for (int x = 0; x < MCPActivity.list_of_plans.size(); x++) {
                                String keyset = MCPActivity.list_of_plans.get(x).keySet().toString().replace("[", "").replace("]", "");

                                if (keyset.equals(MCPActivity.child_clicked.get("IDM_id"))) {
                                    MCPActivity.list_of_plans.get(x).get(keyset).remove(day_string.get(position));
                                    MCPActivity.isVisible = 3;
                                    MCPCalendarAdapter.this.notifyDataSetChanged();
                                    break;
                                }
                            }
                        }
                    }
                } else {
                    if (firsttouch <= 1) {
                        time = System.currentTimeMillis();
                        setSelected(v, position);
                        public_pos = position;
                        MCPActivity.duringOnClick(day_string.get(position));
                    }

                    firsttouch = 1;
                    return false;
                }

                return true;
            }
        });

        if (MCPActivity.change_view.getVisibility() == View.VISIBLE)
            setEventView(position);

        return v;
    }

    public View setSelected(View view, int pos) {
        if (previousView != null)
            previousView.setBackgroundColor(Color.WHITE);

        view.setBackgroundColor(Color.parseColor("#90413B41"));
        MCPActivity.picked_date.setText(helpers.convertToAlphabetDate(day_string.get(pos), ""));
        MCPActivity.picked_day.setText(helpers.convertToDayOfWeek(day_string.get(pos)));

        if (day_string.size() > pos)
            previousView = view;

        return view;
    }

    public void refreshDays() {
        day_string.clear();
        pmonth = (Calendar) month.clone();
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
        int len = MCPActivity.list_of_plans_per_day.size();
        String date_selected = day_string.get(pos);

        for (int x = 0; x < len; x++) {
            HashMap<String, ArrayList<HashMap<String, String>>> map_per_day = MCPActivity.list_of_plans_per_day.get(x);
            String date_on_map = map_per_day.keySet().toString().replace("[", "").replace("]", "");

            if (date_on_map.equals(date_selected))
                with_plan.setVisibility(View.VISIBLE);
        }
    }
}
