package edu.moravian.csci299.finalproject;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
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
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Collections;
import java.util.Date;
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
    private List<Runner> runners = Collections.emptyList();
    private SharedPreferences preferences;
    private long eventRecord;
    private boolean firstFinished;
    private String eventKey;



    private double laps;


    StopWatchFragment stopWatchFragment;

    private LiveData<List<Runner>> liveDataItems;

    public interface Callbacks{
        long getTime();
    }

    public void setPreferences(SharedPreferences preference){
        this.preferences = preference;
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

        stopWatchFragment = StopWatchFragment.newInstance();

        liveDataItems = RunnerRepository.get().getAllRunners();
        liveDataItems.observe(this, (items) -> {
            this.runners = items;
            list.setAdapter(new EventListAdapter());
        });

        setHasOptionsMenu(true);
        firstFinished = true;
    }

    public void setEventRecord(long record){
        eventRecord = record;
    }
    public void setEventKey(String key){
        eventKey = key;
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

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new SwipeToDeleteCallback(eventListAdapter));
        itemTouchHelper.attachToRecyclerView(list);

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
            Runner runner = new Runner();
            runner.lapsToGo = laps;
            RunnerRepository.get().addRunner(runner);
            list.getAdapter().notifyDataSetChanged();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }



    private class EventViewHolder extends RecyclerView.ViewHolder {
        public Runner runner;

        //TextViews and buttons
        private EditText name, number;
        private TextView lapsToGo, projectedTime;
        private Button lap;
        private LinearLayout linearLayout;

        private int blue;
        private int green;


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
            projectedTime = runnerView.findViewById(R.id.projectedTime);
            linearLayout = runnerView.findViewById(R.id.linearLayout);

            green = ContextCompat.getColor(getContext(), R.color.green);
            blue = ContextCompat.getColor(getContext(), R.color.blue);
        }



        private void startAnimation() {

            ObjectAnimator sunsetSkyAnimator = ObjectAnimator
                    .ofInt(linearLayout, "backgroundColor", blue, green)
                    .setDuration(250);
            sunsetSkyAnimator.setEvaluator(new ArgbEvaluator());


            sunsetSkyAnimator.start();
        }
    }

    private class SwipeToDeleteCallback extends ItemTouchHelper.SimpleCallback {
        private EventListAdapter listAdapter;
        private Drawable icon;
        private final ColorDrawable background;

        /**
         * Create the background for the list adapter
         *
         * @param adapter listAdapter
         */
        public SwipeToDeleteCallback(EventListAdapter adapter) {
            super(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT);
            listAdapter = adapter;
            icon = ContextCompat.getDrawable(getContext(), R.drawable.delete);
            background = new ColorDrawable(Color.RED);
        }

        /**
         * When the item is moved this method is called
         *
         * @param recyclerView recyclerView
         * @param viewHolder   ViewHolder
         * @param target       target
         * @return false
         */
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        /**
         * when the item in the viewHolder is swiped, the item is deleted
         *
         * @param viewHolder the viewHolder being swiped
         * @param direction  the direction of the swipe
         */
        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            int position = viewHolder.getAdapterPosition();
            listAdapter.deleteItem(position);
        }

        /**
         * The meat of swipe to delete. This code handles swiping, placing the background behind the list items,
         * setting the bounds of the icon, and swiping left or right
         *
         * @param c                 The canvas which RecyclerView is drawing its children
         * @param recyclerView      The RecyclerView to which ItemTouchHelper is attached to
         * @param viewHolder        The ViewHolder which is being interacted by the User or it was interacted and
         *                          simply animating to its original position
         * @param dX                The amount of horizontal displacement caused by user's action
         * @param dY                The amount of vertical displacement caused by user's action
         * @param actionState       The type of interaction on the View. Is either ACTION_STATE_DRAG or ACTION_STATE_SWIPE
         * @param isCurrentlyActive True if this view is currently being controlled by the user or false it is simply
         *                          animating back to its original state.
         */
        @Override
        public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);

            View itemView = viewHolder.itemView;
            int backgroundCornerOffset = 20; //so background is behind the rounded corners of itemView

            int iconMargin = (itemView.getHeight() - icon.getIntrinsicHeight()) / 2;
            int iconTop = itemView.getTop() + (itemView.getHeight() - icon.getIntrinsicHeight()) / 2;
            int iconBottom = iconTop + icon.getIntrinsicHeight();

            if (dX > 0) { // Swiping to the right
                int iconLeft = itemView.getLeft() + iconMargin + icon.getIntrinsicWidth();
                int iconRight = itemView.getLeft() + iconMargin;
                icon.setBounds(iconLeft, iconTop, iconRight, iconBottom);

                background.setBounds(itemView.getLeft(), itemView.getTop(),
                        itemView.getLeft() + ((int) dX) + backgroundCornerOffset, itemView.getBottom());
            } else if (dX < 0) { // Swiping to the left
                int iconLeft = itemView.getRight() - iconMargin - icon.getIntrinsicWidth();
                int iconRight = itemView.getRight() - iconMargin;
                icon.setBounds(iconLeft, iconTop, iconRight, iconBottom);

                background.setBounds(itemView.getRight() + ((int) dX) - backgroundCornerOffset,
                        itemView.getTop(), itemView.getRight(), itemView.getBottom());
            } else { // view is unSwiped
                background.setBounds(0, 0, 0, 0);
            }

            background.draw(c);
            icon.draw(c);
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
            Runner item = runners.get(position);
            holder.runner = item;
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

                if(item.lapsToGo <= 0) {
                    holder.startAnimation();
//                    holder.itemView.setBackgroundColor(getResources().getColor(R.color.green));
                    if (firstFinished){
                        long finishTimeInMilliseconds = callbacks.getTime();
                        if(eventRecord == 0)
                            preferences.edit().putLong(eventKey, finishTimeInMilliseconds).apply();
                        else if (finishTimeInMilliseconds < eventRecord){
                            preferences.edit().putLong(eventKey, finishTimeInMilliseconds).apply();
                        }
                        firstFinished = false;
                    }
                }
                Log.d("Timing", "Projected time: " + projectedTime(item.lapsToGo));
                holder.projectedTime.setText(projectedTime(item.lapsToGo));
            });

            holder.name.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) { }

                @Override
                public void afterTextChanged(Editable s) {
                    item.name = s.toString();
                }
            });

            holder.number.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    if(!s.toString().equals("")) {
                        item.number = Integer.parseInt(s.toString());
                        RunnerRepository.get().updateRunner(item);
                    }
                }
            });


//            RunnerRepository.get().updateRunner(item);
        }

        /**
         * @return the total number of events to be displayed in the list
         */
        @Override
        public int getItemCount() {
            return ListFragment.this.runners.size();
        }

        public void deleteItem(int position) {
            RunnerRepository.get().removeRunner(ListFragment.this.runners.get(position));
            notifyItemRemoved(position);
        }

    }


    /**
     * Set the callbacks to the context.
     * @param context context of this fragment.
     */
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        callbacks = (Callbacks) context;
    }

    /**
     * Sets callbacks to null.
     */
    @Override
    public void onDetach() {
        super.onDetach();
        callbacks = null;
    }

    public String projectedTime(double lapsToGo){

        long currentTimeInMilliseconds = callbacks.getTime();


        Log.d("Timing", "currentTime: " + currentTimeInMilliseconds);

        float distanceTravelled = (float)(laps - lapsToGo) * 400;//gets distance travelled so far

        Log.d("Timing", ""+ distanceTravelled);
        double raceDistance = laps * 400;
        float timeOverDistance = currentTimeInMilliseconds/distanceTravelled;


        long newMilliseconds = (long)(raceDistance * timeOverDistance);

        long minutes = (newMilliseconds / 1000) / 60;

        long seconds = (newMilliseconds / 1000) % 60;

        if(seconds < 10)
            return minutes + ":0" + seconds;

        return minutes + ":" + seconds;

    }



}

