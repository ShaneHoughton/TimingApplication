package edu.moravian.csci299.lapCounter;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

/**
 *Public class that extends fragment and implements View.OnClickListener interface for the start, stop, pause,
 * and lap ImageButtons. Contains class variables for layout objects such as textview for displaying stopwatch time,
 * image buttons for controlling the stopwatch, a handler for handing thread for our runnable that is driving the
 * stopwatch, a boolean to determine if the stopwatch is currently running, longs for storing information on the milliseconds
 * from the thread such as the start time, the updated time, and the buffering time between pause and its restart, and ints
 * for minutes, seconds, and milliseconds for updating the UI stopwatch textview.
 */
public class StopWatchFragment extends Fragment implements View.OnClickListener, ListFragment.Callbacks{

    private TextView timeTextView;
    private ImageButton startButton, pauseButton, resetButton;
    private Handler handler;
    private boolean isResumed;
    private long timeMilliSeconds, timeStart, timeBuff, timeUpdate = 0L;
    private int seconds, minutes, milliSeconds;
    private static StopWatchFragment stopWatchFragment;
    private boolean isFirstValue;
    private long timestampOfLastChange = 0;
    private SensorManager sensorManager;
    private Sensor sensor;
    private String eventRecordKey;
    private TextView eventRecordText;
    private Callbacks callback;

    public interface Callbacks{
        long getRecord();
    }




    /**
     * Required empty public constructor
     */
    public StopWatchFragment() {

    }




    /**
     * Use this factory method to create a new instance of
     * StopWatch fragment with no parameters
     *
     * @return A new instance of StopWatchFragment.
     */
    public static StopWatchFragment newInstance() {
        if(stopWatchFragment == null){
            return new StopWatchFragment();
        }
        else{
            return stopWatchFragment;
        }
    }

    /**
     *  Called when the StopWatchFragment is created, create a new handler and set resumed to false
     * @param savedInstanceState if the fragment is being re-created from a previous saved state, this is the state.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Create a handler for this thread that we will use for utilizing the runnable that drives our stopwatch
        handler = new Handler();
        //stopwatch is created stopped or paused
        isResumed = false;

    }

    /**
     * Set the callbacks to the context.
     * @param context context of this fragment.
     */
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        callback = (StopWatchFragment.Callbacks) context;
    }

    /**
     * Sets callbacks to null.
     */
    @Override
    public void onDetach() {
        super.onDetach();
        callback = null;
    }

    /**
     * Used to return string in the format that we want for the stopwatch textview that takes int parameters of
     * id for the string that we are formatting to, min for the minutes, sec for the seconds, and milliSec for the milliseconds
     * that are going into the form of "00:00:00"
     * @param id
     * @param min
     * @param sec
     * @param milliSec
     * @return string with parameters in format "00:00:00"
     */
    public String getString(int id, int min, int sec, int milliSec){
        String raw = getString(id);
        return String.format(raw, min, sec, milliSec);
    }

    /**
     * Creates a new Runnable interface that is used by this fragment as it is executed by a thread and when the thread is
     * actively running it will run our run() method.
     */
    public Runnable runnable = new Runnable() {
        @Override
        /**
         * While thread is active, this method is called to appropriately update the milliseconds that the thread has been running
         * and account for the starttime of milliseconds before we call run, as well as the buffering time between pauses or stops
         * of our stop watch. Also breaks down the milliseconds into int values through integer and modular division in order to
         * get the minutes, seconds, and milliseconds to then call getString() to get these integers in the appropriate "00:00:00"
         * form to update our stopwatch textview. Then we delay for 10 milliseconds before the runnable is ran on the thread again.
         */
        public void run() {
            timeMilliSeconds = SystemClock.uptimeMillis() - timeStart;
            timeUpdate = timeBuff + timeMilliSeconds;
            seconds = (int) (timeUpdate / 1000);
            minutes = seconds / 60;
            seconds = seconds % 60;

            milliSeconds = (int) (timeUpdate / 10);
            milliSeconds = milliSeconds % 100;
            String time = getString(R.string.time_text, minutes, seconds, milliSeconds);
            //chronometer.setText(time);
            timeTextView.setText(time);
            //wait 10 milliseconds before running runnable again

            handler.postDelayed(this, 10);

        }
    };

    /**
     * Inflates our fragment_stop_watch layout view and sets up our class variables for the elements of our
     * layout such as the buttons and textviews. Also sets listeners for the buttons to control our stopwatch by interacting with
     * the runnable.
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return inflated fragment_stop_watch view
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View base = inflater.inflate(R.layout.fragment_stop_watch, container, false);
        timeTextView = base.findViewById(R.id.timeTextView);
        startButton = base.findViewById(R.id.startButton);
        startButton.setOnClickListener(this);
        pauseButton = base.findViewById(R.id.pauseButton);
        pauseButton.setOnClickListener(this);
        resetButton = base.findViewById(R.id.resetButton);
        resetButton.setOnClickListener(this);
        eventRecordText = base.findViewById(R.id.stopWatchRecord);
        eventRecordText.setText(getString(R.string.time_text, TimeUtils.getMinutes(callback.getRecord()), TimeUtils.getSeconds(callback.getRecord()), TimeUtils.getMilliSeconds(callback.getRecord())));
        return base;
    }

    /**
     * Called whenever one of our view objects with a listener are clicked. Checks to see which view was clicked and
     * if it is the startbutton then we check to see if stopwatch is currently resumed and if it is not then
     * we use the handler to start running our runnable object on the thread. If paused button is clicked them we check to see
     * if the stopwatch is currently resumed and if it is we get the buffering milliseconds for updating in the future and
     * removes callbacks for runnable and updates the boolean that stopwatch is no longer resumed.
     * @param v the View clicked
     */
    @Override
    public void onClick(View v) {
        if (startButton.getId() == v.getId()){
            start();
        }
        else if (pauseButton.getId() == v.getId()){
            pause();
        }
        else if (resetButton.getId() == v.getId()){
            stop();
            }
    }

    /**
     *  start the stopwatch
     */
    public void start(){
        if (!isResumed){
            timeStart = SystemClock.uptimeMillis();
            handler.postDelayed(runnable, 0);
            //chronometer.start();
            isResumed = true;
        }
    }

    /**
     * Pause the stopwatch
     */
    public void pause(){
        if (isResumed)
            timeBuff += timeMilliSeconds;
        handler.removeCallbacks(runnable);
        //chronometer.stop();
        isResumed = false;
    }

    /**
     * Stop/reset the stopwatch
     */
    public void stop(){
        if(!isResumed){
            timeMilliSeconds = 0L;
            timeStart = 0L;
            timeBuff = 0L;
            timeUpdate = 0L;
            minutes = 0;
            seconds = 0;
            milliSeconds = 0;
            String time = getString(R.string.time_text, minutes, seconds, milliSeconds);
            timeTextView.setText(time);
            //chronometer.setText(time);
        }
    }

    /**
     *  Get the time in milliseconds
     * @return the time in milliseconds
     */
    public long getTime() {
        return timeMilliSeconds;
    }


    /**
     * return true if the stopwatch is currently running
     * @return isResumed
     */
    public boolean getIsResumed(){
        return isResumed;
    }


}