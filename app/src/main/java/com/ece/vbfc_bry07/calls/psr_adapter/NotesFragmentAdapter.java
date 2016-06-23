package com.ece.vbfc_bry07.calls.psr_adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ece.vbfc_bry07.calls.R;

import java.util.ArrayList;
import java.util.HashMap;

public class NotesFragmentAdapter extends BaseAdapter {
    Context context;
    ArrayList<HashMap<String, String>> objects;

    TextView title_note, date;

    public NotesFragmentAdapter(Context context, ArrayList<HashMap<String, String>> objects) {
        this.context = context;
        this.objects = objects;
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
            v = vi.inflate(R.layout.adapter_notes_fragment, parent, false);
        }

        title_note = (TextView) v.findViewById(R.id.title_note);
        date = (TextView) v.findViewById(R.id.date);

        if (objects.get(position).get("note").length() > 35)
            title_note.setText(objects.get(position).get("note").substring(0, 30) + "...");
        else
            title_note.setText(objects.get(position).get("note"));

        date.setText(objects.get(position).get("date"));

        return v;
    }
}
