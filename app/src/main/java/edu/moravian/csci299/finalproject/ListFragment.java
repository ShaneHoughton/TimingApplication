package edu.moravian.csci299.finalproject;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.lifecycle.ViewModelProvider;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Date;
import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * A fragment that displays a list of events. The list is a RecyclerView. When an event on the list
 * is clicked, a callback method is called to inform the hosting activity. When an item on the list
 * is swiped, it causes the event to be deleted (see https://medium.com/@zackcosborn/step-by-step-recyclerview-swipe-to-delete-and-undo-7bbae1fce27e).
 * This is the fragment that also controls the menu of options in the app bar.
 * <p>
 * Above the list is a text box that states the date being displayed on the list.
 * <p>
 * NOTE: Finish CalendarFragment first then work on this one. Also, look at how a few things
 * related to dates are dealt with in the CalendarFragment and use similar ideas here.
 */
public class ListFragment extends Fragment{

    // fragment initialization parameters
    private static final String ARG_DATE = "date";

    // data
    private RecyclerView list;
    private Callbacks callbacks;
    private RunnerList runnerList ;

    private double laps;

    long getCurrentMilliseconds;
//    private long currentTimeInMilliseconds;



    StopWatchFragment stopWatchFragment;

    public interface Callbacks{
        long getTime();
    }


    /**
     * Use this factory method to create a new instance of this fragment that
     * lists events for today.
     *
     * @return a new instance of fragment ListFragment
     */
    public static ListFragment newInstance() {
        return newInstance(new Date());
    }

    /**
     * Use this factory method to create a new instance of this fragment that
     * lists events for the given day.
     *
     * @param date the date to show the event list for
     * @return a new instance of fragment ListFragment
     */
    public static ListFragment newInstance(Date date) {
//        date = ListFragment.resetToStartOfDay(date);

        ListFragment fragment = new ListFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_DATE, date);
        fragment.setArguments(args);
        return fragment;
    }

    public void setLaps(double laps){
        this.laps = laps;
    }


    /**
     * Upon creation need to enable the options menu and update the view for the initial date.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        runnerList = new ViewModelProvider(this).get(RunnerList.class);
        stopWatchFragment = StopWatchFragment.newInstance();


        setHasOptionsMenu(true);
    }

    /**
     * Create the view for this layout. Also sets up the adapter for the RecyclerView, its swipe-to-
     * delete helper, and gets the date text view.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View base = inflater.inflate(R.layout.fragment_list, container, false);


        EventListAdapter eventListAdapter = new EventListAdapter();
        list = base.findViewById(R.id.list_view);
        list.setLayoutManager(new LinearLayoutManager(getContext()));
        list.setAdapter(eventListAdapter);

        // return the base view
        return base;
    }


    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.list_menu, menu);
    }


    /**
     * Creates a new event, adds it to the database, and triggers callbacks with it.
     *
     * @param item the item selected.
     * @return true if selected, otherwise returns superclasses result.
     */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.new_runner) {
            Log.d("event", "onOptionsItemSelected: ");

            //Make new runner
            runnerList.runners.add(new Runner("Name",0 , laps));
            list.getAdapter().notifyDataSetChanged();


            Log.d("event", "New Runner");

            return true;
        }

        return super.onOptionsItemSelected(item);
    }



    private class EventViewHolder extends RecyclerView.ViewHolder {
        public Runner runner;

        //TextViews and buttons
        private EditText name;
        private TextView lapsToGo;
        private Button lap;
        private EditText number;

        /**
         * Sets all fields to their respective views.
         *
         * @param runnerView
         */
        public EventViewHolder(@NonNull View runnerView) {
            super(runnerView);
            name = runnerView.findViewById(R.id.runnerName);
            lapsToGo = runnerView.findViewById(R.id.lapsToGo);
            lap = runnerView.findViewById(R.id.lap);
            number = runnerView.findViewById(R.id.number);
        }
    }

    /**
     * The adapter for the items list to be displayed in a RecyclerView.
     */
    private class EventListAdapter extends RecyclerView.Adapter<EventViewHolder> {
        /**
         * To create the view holder we inflate the layout we want to use for
         * each item and then return an ItemViewHolder holding the inflated
         * view.
         */
        @NonNull
        @Override
        public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View v = inflater.inflate(R.layout.runner_item, parent, false);

            return new EventViewHolder(v);
        }

        /**
         * When we bind a view holder to an item (i.e. use the view with a view
         * holder to display a specific item in the list) we need to update the
         * various views within the holder for our new values.
         *
         * @param holder   the ItemViewHolder holding the view to be updated
         * @param position the position in the list of the item to display
         */
        @Override
        public void onBindViewHolder(@NonNull EventViewHolder holder, int position) {
            Runner item = runnerList.runners.get(position);
            holder.name.setText(item.name);
            holder.lapsToGo.setText(Double.toString(item.lapsToGo));
            holder.number.setText(Integer.toString(item.number));

            holder.lap.setOnClickListener(v -> {
                if(item.lapsToGo == 7.5 || item.lapsToGo == 12.5){
                    item.lapsToGo = item.lapsToGo - 0.5;
                    holder.lapsToGo.setText(Integer.toString((int)item.lapsToGo));
                }

                else if (item.lapsToGo > 0) {
                    item.lapsToGo--;
                    holder.lapsToGo.setText(Integer.toString((int)item.lapsToGo));
                }

                Log.d("Timing", "Projected time: " + projectedTime(item.lapsToGo));
            });




        }

        /**
         * @return the total number of events to be displayed in the list
         */
        @Override
        public int getItemCount() {
            return runnerList.runners.size();
        }

    }

    public String projectedTime(double lapsToGo){ //TODO: Get callbacks to not be null!!!

        long currentTimeInMilliseconds = callbacks.getTime();


        Log.d("Timing", "currentTime: " + currentTimeInMilliseconds);

        float distanceTravelled = (float)(laps - lapsToGo) * 400;//gets distance travelled so far

        Log.d("Timing", ""+ distanceTravelled);
        double raceDistance = laps * 400;
        float timeOverDistance = currentTimeInMilliseconds/distanceTravelled;


        long newMilliseconds = (long)(raceDistance * timeOverDistance);


        long minutes = (newMilliseconds / 1000) / 60;

        // formula for conversion for
        // milliseconds to seconds
        long seconds = (newMilliseconds / 1000) % 60;


        return newMilliseconds + " Milliseconds = " + minutes + " minutes and " + seconds + " seconds.";
    }


}

