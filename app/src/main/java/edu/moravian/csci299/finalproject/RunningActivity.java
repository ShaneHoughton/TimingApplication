package edu.moravian.csci299.finalproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;

public class RunningActivity extends AppCompatActivity {
    //TODO: should act like the main activity in MoCalendar

    public int laps;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_running);


        laps = getIntent().getIntExtra("laps",0);
        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);

        if (currentFragment == null) {
            // If no fragment is displayed in fragment_container, add one with a transaction
            ListFragment listFragment = ListFragment.newInstance();
            listFragment.setLaps(laps);
            TimerFragment timer = TimerFragment.newInstance();
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.fragment_container, timer)
                    .add(R.id.fragment_container, listFragment)
                    .commit();
        }
    }
}