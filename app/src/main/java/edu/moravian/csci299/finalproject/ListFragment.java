package edu.moravian.csci299.finalproject;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
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
 *
 * Above the list is a text box that states the date being displayed on the list.
 *
 * NOTE: Finish CalendarFragment first then work on this one. Also, look at how a few things
 * related to dates are dealt with in the CalendarFragment and use similar ideas here.
 */
public class ListFragment extends Fragment {
//    public interface Callbacks {
//        /**
//         * Called when an event needs to be displayed.
//         * @param event the event to show.
//         */
//        void showEvent(Event event);
//    }

    // fragment initialization parameters
    private static final String ARG_DATE = "date";

    // data
    private Date date;
    private List<Runner> events = Collections.emptyList();
    private RecyclerView list;
    //private Callbacks callbacks;
    private TextView currentDate;

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


    /**
     * Upon creation need to enable the options menu and update the view for the initial date.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        date = (Date) getArguments().getSerializable(ARG_DATE);
//        onDateChange();


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

        ItemTouchHelper helper =
                new ItemTouchHelper(new SwipeToDeleteCallback(eventListAdapter));
        helper.attachToRecyclerView(list);

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
     * @param item the item selected.
     * @return true if selected, otherwise returns superclasses result.
     */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.new_runner) {
            Log.d("event", "onOptionsItemSelected: ");

            //Make new runner
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    private class EventViewHolder extends RecyclerView.ViewHolder {
        public Runner runner;
        //TextViews and buttons

        /**
         * Sets all fields to their respective views.
         * @param itemView
         */
        public EventViewHolder(@NonNull View itemView) {
            super(itemView);
//            name = itemView.findViewById(R.id.event_item_name);
//            icon = itemView.findViewById(R.id.event_type_icon);
//            description = itemView.findViewById(R.id.event_type_item_desc);
//            startTime = itemView.findViewById(R.id.item_start_time);
//            endTime = itemView.findViewById(R.id.item_end_time);
//            dash = itemView.findViewById(R.id.simple_dash);


//            itemView.setOnClickListener(v -> callbacks.showEvent(event));
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
//            Event event = events.get(position);
//            holder.event = event;
//            holder.name.setText(event.name);
//            holder.icon.setImageResource(event.type.iconResourceId);
//            holder.description.setText(event.description);
//            holder.startTime.setText(DateUtils.toTimeString(event.startTime));

//            if(event.startTime.toString().equals(event.endTime.toString())){
//                holder.endTime.setText("");
//            }
//            else
//                holder.endTime.setText(DateUtils.toTimeString(event.endTime));

        }

        /**
         * @return the total number of events to be displayed in the list
         */
        @Override
        public int getItemCount() {
            return events.size();
        }

        public void deleteEvent(int position) {
//            Event event = events.get(position);
//            notifyItemRemoved(position);
        }

    }

    public class SwipeToDeleteCallback extends ItemTouchHelper.SimpleCallback {
        private EventListAdapter mAdapter;
        private Drawable icon;
        private final ColorDrawable background;

        /**
         * Creates a swipe to delete callback.
         * @param adapter
         */
        @SuppressLint("ResourceAsColor")
        public SwipeToDeleteCallback(EventListAdapter adapter) {
            super(0,ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT);
            mAdapter = adapter;
//            icon = ContextCompat.getDrawable(getContext(),
//                    R.drawable.delete);
            background = new ColorDrawable(R.color.purple_200);
        }

        /**
         * Draw the swiper.
         */
        @Override
        public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
            super.onChildDraw(c, recyclerView, viewHolder, dX,
                    dY, actionState, isCurrentlyActive);
            View itemView = viewHolder.itemView;
            int backgroundCornerOffset = 20;


            int iconMargin = (itemView.getHeight() - icon.getIntrinsicHeight()) / 2;
            int iconTop = itemView.getTop() + (itemView.getHeight() - icon.getIntrinsicHeight()) / 2;
            int iconBottom = iconTop + icon.getIntrinsicHeight();

            if (dX > 0) { // Swiping to the right
                int iconLeft = itemView.getLeft() + iconMargin + icon.getIntrinsicWidth();
                int iconRight = itemView.getLeft() + iconMargin;
                icon.setBounds(iconLeft, iconTop, iconRight, iconBottom);

                background.setBounds(itemView.getLeft(), itemView.getTop(),
                        itemView.getLeft() + ((int) dX) + backgroundCornerOffset,
                        itemView.getBottom());
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

        /**
         * forced to implement.
         * @param recyclerView
         * @param viewHolder
         * @param target
         * @return always returns false.
         */
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView,
                              @NonNull RecyclerView.ViewHolder viewHolder,
                              @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        /**
         * Delete the event from the adapter when it is swiped.
         * @param viewHolder
         * @param direction
         */
        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            int position = viewHolder.getAdapterPosition();
            mAdapter.deleteEvent(position);
        }
    }
}

