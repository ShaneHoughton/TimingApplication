package edu.moravian.csci299.finalproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.HashMap;


public class StartActivity extends AppCompatActivity implements View.OnClickListener, EventPickerDialog.Callbacks {

    /**
     * The Spinner for the menus
     */
    String [] events;
    double laps = 0;
    TextView distance;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        events = getResources().getStringArray(R.array.events_array);




        // Set up the "Play" button
        findViewById(R.id.playButton).setOnClickListener(this);

        // Set up the "Two Player" button
        findViewById(R.id.helpButton).setOnClickListener(this);

        distance = findViewById(R.id.distance);
        distance.setOnClickListener(this);


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
        if(v == findViewById(R.id.distance)){
            FragmentManager fm = getSupportFragmentManager();
            EventPickerDialog dialog = EventPickerDialog.newInstance(Events.M800);
            dialog.show(fm, "Events");
        }
        else if (v == findViewById(R.id.playButton)) {
            Intent intent = new Intent(this, RunningActivity.class);
            intent.putExtra("laps", laps);
            startActivity(intent);
        } else {
            Intent intentHelp = new Intent(this, HelpActivity.class);
            startActivity(intentHelp);
        }
    }

    @Override
    public void onEventSelected(Events event) {
        laps = event.distance/400.0;
        distance.setText(String.valueOf(event.distance));

    }
}