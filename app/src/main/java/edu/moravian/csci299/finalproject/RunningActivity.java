package edu.moravian.csci299.finalproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.util.Log;

public class RunningActivity extends AppCompatActivity implements ListFragment.Callbacks{
    //TODO: should act like the main activity in MoCalendar
    StopWatchFragment stopWatchFragment;
    public double laps;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_running);


        laps = getIntent().getDoubleExtra("laps",0.0);
        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);

        if (currentFragment == null) {
            // If no fragment is displayed in fragment_container, add one with a transaction
            ListFragment listFragment = ListFragment.newInstance();
            listFragment.setLaps(laps);
            stopWatchFragment = StopWatchFragment.newInstance();
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.fragment_container, stopWatchFragment)
                    .add(R.id.fragment_container, listFragment)
                    .commit();

        }
    }

    @Override
    public long getTime() {
        return stopWatchFragment.getTime();
    }
}