package com.example.vbfc_bry07.calls.Fragment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.ListView;

import com.example.vbfc_bry07.calls.Adapter.NotesFragmentAdapter;
import com.example.vbfc_bry07.calls.Helpers;
import com.example.vbfc_bry07.calls.R;

import java.util.ArrayList;
import java.util.HashMap;

public class NotesFragment extends Fragment implements View.OnClickListener {
    FloatingActionButton add_note;
    ListView list_of_notes;

    Helpers helpers;

    ArrayList<HashMap<String, String>> array_of_notes = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_notes, container, false);

        add_note = (FloatingActionButton) v.findViewById(R.id.add_note);
        list_of_notes = (ListView) v.findViewById(R.id.list_of_notes);

        helpers = new Helpers();

        list_of_notes.setAdapter(new NotesFragmentAdapter(getActivity(), array_of_notes));
        add_note.setOnClickListener(this);

        return v;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add_note:
                final AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
                final View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_add_note, null);
                dialog.setView(view);
                dialog.setNegativeButton("Cancel", null);
                dialog.setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        EditText note = (EditText) view.findViewById(R.id.note);
                        String date = helpers.getCurrentDate("timestamp");
                        String get_note = note.getText().toString();
                    }
                });
                dialog.show();
                break;
        }
    }
}
