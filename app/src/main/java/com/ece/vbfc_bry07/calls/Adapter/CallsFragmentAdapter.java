package com.ece.vbfc_bry07.calls.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ece.vbfc_bry07.calls.Helpers;
import com.ece.vbfc_bry07.calls.R;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class CallsFragmentAdapter extends BaseAdapter {
    Context context;
    ArrayList<HashMap<String, String>> objects;

    TextView date, missed_call_date;
    ImageView image;

    Helpers helpers;

    public CallsFragmentAdapter(Context context, ArrayList<HashMap<String, String>> objects) {
        this.objects = objects;
        this.context = context;
        helpers = new Helpers();
    }

    @Override
    public int getCount() {
        return objects.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
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
            v = vi.inflate(R.layout.adapter_calls_fragment, parent, false);
        }

        date = (TextView) v.findViewById(R.id.date);
        image = (ImageView) v.findViewById(R.id.image);
        missed_call_date = (TextView) v.findViewById(R.id.missed_call_date);

        date.setText(helpers.convertToAlphabetDate(objects.get(position).get("date"), ""));
        image.setVisibility(View.VISIBLE);

        final Date dateNow = helpers.convertStringToDate(helpers.getCurrentDate("date"));
        final Date history_date = helpers.convertStringToDate(objects.get(position).get("date"));

        if (!objects.get(position).get("server_id").equals(""))
            image.setImageResource(R.mipmap.ic_actual_covered_call);
        else if (dateNow.before(history_date) && objects.get(position).get("status").equals(""))
            image.setVisibility(View.INVISIBLE);
        else if (objects.get(position).get("status").equals("1"))
            image.setImageResource(R.mipmap.ic_signed_call);
        else if (objects.get(position).get("status").equals("2")) {
            image.setImageResource(R.mipmap.ic_recovered_call);
            Date date_missed = helpers.convertStringToDate(objects.get(position).get("missed_call_date"));

            if (date_missed.after(history_date))
                missed_call_date.setText("Advanced call for: " + objects.get(position).get("missed_call_date"));
            else
                missed_call_date.setText("Missed call on: " + objects.get(position).get("missed_call_date"));
        } else if (objects.get(position).get("status").equals("3"))
            image.setImageResource(R.mipmap.ic_incidental_call);
        else if (dateNow.after(history_date) && objects.get(position).get("status").equals(""))
            image.setImageResource(R.mipmap.ic_missed_call);

        return v;
    }
}
