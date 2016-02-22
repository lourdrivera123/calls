package com.example.vbfc_bry07.calls.Fragment;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ListView;

import com.example.vbfc_bry07.calls.Adapter.NotesFragmentAdapter;
import com.example.vbfc_bry07.calls.R;

import java.util.ArrayList;
import java.util.HashMap;

public class NotesFragment extends Fragment implements View.OnClickListener {
    FloatingActionButton add_note;
    ListView list_of_notes;

    ArrayList<HashMap<String, String>> array = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_notes, container, false);

        add_note = (FloatingActionButton) v.findViewById(R.id.add_note);
        list_of_notes = (ListView) v.findViewById(R.id.list_of_notes);

        list_of_notes.setAdapter(new NotesFragmentAdapter(getActivity(), array));
        add_note.setOnClickListener(this);

        return v;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add_note:
                AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
                dialog.setView(R.layout.dialog_add_note);
                dialog.setNegativeButton("Cancel", null);
                dialog.setPositiveButton("Save", null);
                dialog.show();
                break;
        }
    }
}
