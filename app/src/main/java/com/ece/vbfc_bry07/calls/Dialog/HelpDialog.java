package com.ece.vbfc_bry07.calls.Dialog;

import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.ece.vbfc_bry07.calls.R;

public class HelpDialog extends AppCompatActivity {
    TextView title;
    ListView list_of_help;

    String[] names;
    TypedArray images;

    ListViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        DisplayMetrics metrics = getResources().getDisplayMetrics();
        int screenWidth = (int) (metrics.widthPixels * 0.60);

        setContentView(R.layout.help_layout);
        getWindow().setLayout(screenWidth, LinearLayout.LayoutParams.WRAP_CONTENT);

        title = (TextView) findViewById(R.id.title);
        list_of_help = (ListView) findViewById(R.id.list_of_help);

        Intent intent = getIntent();

        if (intent.getStringExtra("array_name").equals("color_coded_circles")) {
            names = getResources().getStringArray(R.array.color_coded_circles);
            images = getResources().obtainTypedArray(R.array.images_color_coded_circles);
        }

        adapter = new ListViewAdapter(this, names);
        list_of_help.setAdapter(adapter);
    }

    private class ListViewAdapter extends BaseAdapter {
        String[] array;
        Context activity;

        TextView name;
        ImageView image;

        ListViewAdapter(Context context, String[] array) {
            this.array = array;
            activity = context;
        }

        @Override
        public int getCount() {
            return array.length;
        }

        @Override
        public Object getItem(int position) {
            return array[position];
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View v = convertView;

            if (convertView == null) {
                LayoutInflater vi = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                v = vi.inflate(R.layout.item_help, parent, false);
            }

            name = (TextView) v.findViewById(R.id.name);
            image = (ImageView) v.findViewById(R.id.image);

            name.setText(array[position]);
            image.setImageResource(images.getResourceId(position, -1));

            return v;
        }
    }
}
