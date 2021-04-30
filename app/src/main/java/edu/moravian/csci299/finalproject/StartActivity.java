package edu.moravian.csci299.finalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.HashMap;
import java.util.Objects;


public class StartActivity extends AppCompatActivity implements View.OnClickListener {

    /**
     * The Spinner for the menus
     */
    private Spinner spinner;
    HashMap<String, Integer> raceLaps = new HashMap<String, Integer>();
    String [] events;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        events = getResources().getStringArray(R.array.events_array);
        raceLaps.put(events[0], 7);
        raceLaps.put(events[1], 12);
        raceLaps.put(events[2], 25);


        // Set up the difficulty spinner
        spinner = findViewById(R.id.difficultySpinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.events_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        // Set up the "Play" button
        findViewById(R.id.playButton).setOnClickListener(this);

        // Set up the "Two Player" button
        findViewById(R.id.helpButton).setOnClickListener(this);


    }

    /**
     * When the "Play!" button is pressed we proceed to the main game Activity
     *
     * When the secret "Two Player" button is pressed we proceed to the game two player activity
     *
     * @param v the view that was clicked (i.e. the "Play" button)
     */
    @Override
    public void onClick(View v) {
        if (v == findViewById(R.id.playButton)) {
            Intent intent = new Intent(this, RunningActivity.class);
            String event = (spinner.getSelectedItem()).toString();
            intent.putExtra("laps", Objects.requireNonNull(raceLaps.get(event)).intValue());
            startActivity(intent);
        } else {
            Intent intentHelp = new Intent(this, HelpActivity.class);
            startActivity(intentHelp);
        }
    }

}