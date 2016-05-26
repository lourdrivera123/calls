package com.ece.vbfc_bry07.calls.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ece.vbfc_bry07.calls.activity.ACPActivity;
import com.ece.vbfc_bry07.calls.dialog.ViewDoctorsHistoryDialog;
import com.ece.vbfc_bry07.calls.Helpers;
import com.ece.vbfc_bry07.calls.R;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class DoctorsHistoryAdapter extends BaseAdapter {
    ArrayList<HashMap<String, String>> objects;
    Context context;

    TextView date;
    ImageView image;
    LinearLayout call_now;

    Helpers helpers;

    int checker = 0;

    public DoctorsHistoryAdapter(Context context, ArrayList<HashMap<String, String>> objects) {
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
    public View getView(final int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.item_doctor_history, parent, false);
        }

        date = (TextView) convertView.findViewById(R.id.date);
        call_now = (LinearLayout) convertView.findViewById(R.id.call_now);
        image = (ImageView) convertView.findViewById(R.id.image);

        final Date dateNow = helpers.convertStringToDate(helpers.getCurrentDate("date"));
        final Date history_date = helpers.convertStringToDate(objects.get(position).get("date"));

        date.setText(helpers.convertToAlphabetDate(objects.get(position).get("date"), ""));

        if (!objects.get(position).get("server_id").equals("")) {
            image.setVisibility(View.VISIBLE);
            image.setImageResource(R.mipmap.ic_actual_covered_call);
            call_now.setVisibility(View.INVISIBLE);
        } else if (dateNow.after(history_date) && objects.get(position).get("status").equals("")) {
            image.setVisibility(View.VISIBLE);
            image.setImageResource(R.mipmap.ic_missed_call);
            call_now.setVisibility(View.VISIBLE);
        } else if (dateNow.before(history_date) && objects.get(position).get("status").equals("")) {
            call_now.setVisibility(View.VISIBLE);
            image.setVisibility(View.INVISIBLE);
        } else if (objects.get(position).get("status").equals("2")) {
            image.setVisibility(View.VISIBLE);
            image.setImageResource(R.mipmap.ic_recovered_call);
            call_now.setVisibility(View.INVISIBLE);
        } else if (objects.get(position).get("status").equals("1")) {
            image.setVisibility(View.VISIBLE);
            image.setImageResource(R.mipmap.ic_signed_call);
            call_now.setVisibility(View.INVISIBLE);
        } else if (objects.get(position).get("status").equals("3")) {
            image.setVisibility(View.VISIBLE);
            image.setImageResource(R.mipmap.ic_incidental_call);
            call_now.setVisibility(View.INVISIBLE);
        }

        call_now.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ACPActivity.missed_call_date = objects.get(position).get("date");

                if (dateNow.after(history_date) && objects.get(position).get("status").equals(""))
                    checker = 50;
                else if (dateNow.before(history_date) && objects.get(position).get("status").equals(""))
                    checker = 55;
                else
                    ACPActivity.missed_call_date = "";

                ViewDoctorsHistoryDialog.child_clicked.put("plan_details_id", objects.get(position).get("plan_details_id"));
                ACPActivity.check_adapter_acp = checker;
                ViewDoctorsHistoryDialog.activity.finish();
            }
        });
        return convertView;
    }
}
