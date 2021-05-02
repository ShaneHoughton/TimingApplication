package edu.moravian.csci299.finalproject;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;


public class StartActivity extends AppCompatActivity implements View.OnClickListener, EventPickerDialog.Callbacks {


    String [] events;
    double laps = 0;
    TextView distance;
    TextView eventRecordText;
    long eventRecord;
    private SharedPreferences preferences;
    private String eventKey;
    private TextView recordText;


    /**
     * Called when the activity is created. Instantiate the Views and register listeners
     * @param savedInstanceState If the activity is being re-initialized after previously being shut down
     *                           then this Bundle contains the data it most recently supplied in onSaveInstanceState.
     *                           Note: Otherwise it is null.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        events = getResources().getStringArray(R.array.events_array);
        eventRecordText = findViewById(R.id.eventRecord);
        this.preferences = this.getSharedPreferences("edu.moravian.csci299.finalproject", Context.MODE_PRIVATE);

//        To Clear the shared preferences
//        SharedPreferences.Editor preferencesEditor = preferences.edit();
//        preferencesEditor.clear();
//        preferencesEditor.commit();

        // Set up the "Next" button
        findViewById(R.id.nextButton).setOnClickListener(this);

        // Set up the "Help" button
        findViewById(R.id.helpButton).setOnClickListener(this);


        distance = findViewById(R.id.distance);
        distance.setOnClickListener(this);
        setHighScoreText();
    }

    @Override
    protected void onResume() {
        setHighScoreText();
        super.onResume();
    }

    /**
     * When the "Next" button is pressed we proceed to the main Running Activity
     *
     * When the "Help" button is pressed we proceed to the help activity
     *
     * @param v the view that was clicked (i.e. the "Play" button)
     */
    @Override
    public void onClick(View v) {
        if(v == findViewById(R.id.distance)){
            FragmentManager fm = getSupportFragmentManager();
            EventPickerDialog dialog = EventPickerDialog.newInstance(Events.M800);
            dialog.show(fm, "Events");
        }
        else if (v == findViewById(R.id.nextButton)) {
            Intent intent = new Intent(this, RunningActivity.class);
            intent.putExtra("laps", laps);
            intent.putExtra("eventKey", eventKey);
            intent.putExtra("eventRecord", eventRecord);
            startActivity(intent);
        } else {
            Intent intentHelp = new Intent(this, HelpActivity.class);
            startActivity(intentHelp);
        }
    }
    public String getString(int id, int min, int sec, int milliSec){
        String raw = getString(id);
        return String.format(raw, min, sec, milliSec);
    }

    /**
     *  called when something is selected in the dialog picker
     * @param event the event selected in the dialog picker
     */
    @Override
    public void onEventSelected(Events event) {
        laps = event.distance/400.0;
        distance.setText(String.valueOf(event.distance));
        setHighScoreText();
    }

    /**
     * Set the text for the high score text based on which event is selected.
     */
    private void setHighScoreText(){
        if (laps == 2){
            String m800Key = "800_preference";
            eventRecord = preferences.getLong(m800Key, 0);
            eventKey = m800Key;
        }
        else if (laps == 4){
            String m1600Key = "1600_preference";
            eventRecord = preferences.getLong(m1600Key, 0);
            eventKey = m1600Key;
        }
        else if (laps == 7.5){
            String m3000Key = "3000_preference";
            eventRecord = preferences.getLong(m3000Key, 0);
            eventKey = m3000Key;
        }
        else if (laps == 12.5){
            String m5000Key = "5000_preference";
            eventRecord = preferences.getLong(m5000Key, 0);
            eventKey = m5000Key;
        }
        else{
            String m10000Key = "10000_preference";
            eventRecord = preferences.getLong(m10000Key, 0);
            eventKey = m10000Key;

        }
        String formattedEventRecord = getString(R.string.time_text, TimeUtils.getMinutes(eventRecord), TimeUtils.getSeconds(eventRecord), TimeUtils.getMilliSeconds(eventRecord));
        eventRecordText.setText(formattedEventRecord);
    }
}