package edu.moravian.csci299.finalproject;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EventPickerDialog#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EventPickerDialog extends DialogFragment {
    private static final Events[] EVENTS = Events.values();

    public interface Callbacks{

        void onEventSelected(Events event);
    }

    public EventPickerDialog() {
        // Required empty public constructor
    }

    private static final String ARG_INITIAL_TYPE = "initial_type";



    // TODO: Rename and change types and number of parameters
    public static EventPickerDialog newInstance(Events event) {
        EventPickerDialog fragment = new EventPickerDialog();
        Bundle args = new Bundle();
        args.putString(ARG_INITIAL_TYPE, event.name());
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder b = new AlertDialog.Builder(getContext());
        b.setAdapter(new EventTypesListAdapter(), (dialog, which) -> {
            dialog.dismiss();
            ((Callbacks)getActivity()).onEventSelected(EVENTS[which]);
        });
        return b.create();
    }

    private class EventTypesListAdapter extends BaseAdapter {
        @Override
        public int getCount() { return EVENTS.length; }

        @Override
        public Object getItem(int position) { return EVENTS[position]; }

        @Override
        public long getItemId(int position) { return EVENTS[position].hashCode(); }

        @Override
        public boolean hasStableIds() { return true; }

        @Override
        public View getView(int position, View view, ViewGroup parent) {
            // reuse the view or load it new
            if (view == null) {
                view = getLayoutInflater().inflate(R.layout.event_item, parent, false);
            }

            // set the icon and name
            Events type = EVENTS[position];
            ((TextView)view.findViewById(R.id.eventDistance)).setText(String.valueOf(type.distance));

            // returns the view
            return view;
        }

    }

}