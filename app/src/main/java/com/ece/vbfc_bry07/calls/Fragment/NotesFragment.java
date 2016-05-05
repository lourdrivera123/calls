package com.ece.vbfc_bry07.calls.Fragment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.ece.vbfc_bry07.calls.Activity.ACPActivity;
import com.ece.vbfc_bry07.calls.Adapter.NotesFragmentAdapter;
import com.ece.vbfc_bry07.calls.Controller.CallNotesController;
import com.ece.vbfc_bry07.calls.Helpers;
import com.ece.vbfc_bry07.calls.R;

import java.util.ArrayList;
import java.util.HashMap;

public class NotesFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemClickListener {
    static FloatingActionButton add_note;
    static ListView list_of_notes;

    Helpers helpers;
    static CallNotesController cnc;

    public static ArrayList<HashMap<String, String>> array_of_notes = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_notes, container, false);

        add_note = (FloatingActionButton) v.findViewById(R.id.add_note);
        list_of_notes = (ListView) v.findViewById(R.id.list_of_notes);

        helpers = new Helpers();
        cnc = new CallNotesController(getActivity());

        add_note.setOnClickListener(this);
        list_of_notes.setOnCreateContextMenuListener(this);
        list_of_notes.setOnItemClickListener(this);

        return v;
    }

    public static void callNotesFragment() {
        if (!ACPActivity.IDM_id.equals("")) {
            array_of_notes = cnc.listOfNotesByIDM_id(Integer.parseInt(ACPActivity.IDM_id), ACPActivity.current_date);

            if (array_of_notes.size() > 0)
                add_note.setVisibility(View.INVISIBLE);
            else {
                add_note.setVisibility(View.VISIBLE);
                array_of_notes = new ArrayList<>();

                if (ACPActivity.menu_check == 1)
                    add_note.setVisibility(View.INVISIBLE);
            }
        }

        list_of_notes.setAdapter(new NotesFragmentAdapter(ACPActivity.acp, array_of_notes));
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

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (array_of_notes.get(position).get("note").length() > 30) {
            final AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
            final View v = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_view_note, null);
            dialog.setView(v);
            dialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            dialog.show();

            TextView date = (TextView) v.findViewById(R.id.date);
            TextView note = (TextView) v.findViewById(R.id.note);

            date.setText(array_of_notes.get(position).get("date"));
            note.setText(array_of_notes.get(position).get("note"));
        }
    }
}
