package com.ece.vbfc_bry07.calls.Fragment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import com.ece.vbfc_bry07.calls.Adapter.NotesFragmentAdapter;
import com.ece.vbfc_bry07.calls.Helpers;
import com.ece.vbfc_bry07.calls.R;

import java.util.ArrayList;
import java.util.HashMap;

public class NotesFragment extends Fragment implements View.OnClickListener {
    FloatingActionButton add_note;
    ListView list_of_notes;

    Helpers helpers;

    public static ArrayList<HashMap<String, String>> array_of_notes = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_notes, container, false);

        add_note = (FloatingActionButton) v.findViewById(R.id.add_note);
        list_of_notes = (ListView) v.findViewById(R.id.list_of_notes);

        helpers = new Helpers();

        list_of_notes.setAdapter(new NotesFragmentAdapter(getActivity(), array_of_notes));
        add_note.setOnClickListener(this);
        list_of_notes.setOnCreateContextMenuListener(this);

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

                        HashMap<String, String> map = new HashMap<>();
                        map.put("date", date);
                        map.put("note", get_note);
                        array_of_notes.add(map);
                        list_of_notes.setAdapter(new NotesFragmentAdapter(getActivity(), array_of_notes));
                    }
                });
                dialog.show();
                break;
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        getActivity().getMenuInflater().inflate(R.menu.delete_context, menu);
        super.onCreateContextMenu(menu, v, menuInfo);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int position = info.position;

        if (item.getItemId() == R.id.delete) {
            array_of_notes.remove(position);
            list_of_notes.setAdapter(new NotesFragmentAdapter(getActivity(), array_of_notes));
        }

        return super.onContextItemSelected(item);
    }
}
