package edu.moravian.csci299.finalproject;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Chronometer;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TimerFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TimerFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";



    private Chronometer chronometer;
    private Button start;
    private Button pause;
    private Button reset;
    private long pausedOffset;


    private boolean running;

    public TimerFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *

     * @return A new instance of fragment timerFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TimerFragment newInstance() {
        TimerFragment fragment = new TimerFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        running = false;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View base = inflater.inflate(R.layout.fragment_timer, container, false);


        chronometer = base.findViewById(R.id.chronometer);
        start = base.findViewById(R.id.start);
        pause = base.findViewById(R.id.pause);
        reset = base.findViewById(R.id.reset);



        start.setOnClickListener(v -> {
            if(!running) {
                chronometer.setBase(SystemClock.elapsedRealtime() - pausedOffset);
                chronometer.start();
                running = true;
            }
        });

        pause.setOnClickListener(v -> {
            if(running){
                chronometer.stop();
                pausedOffset = SystemClock.elapsedRealtime() - chronometer.getBase();
                running = false;
            }
        });

        reset.setOnClickListener(v -> {
            chronometer.setBase(SystemClock.elapsedRealtime());
            pausedOffset = 0;
                }
        );
        return base;
    }
}