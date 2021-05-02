package edu.moravian.csci299.lapCounter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;

public class RunningActivity extends AppCompatActivity implements ListFragment.Callbacks, SensorEventListener, StopWatchFragment.Callbacks {
    StopWatchFragment stopWatchFragment;
    public double laps;
    private SharedPreferences preferences;
    private long eventRecord;
    private String eventKey;

    //for sensor
    private final static float SHAKE_THRESHOLD = 3f; // in m/s^2
    private final static long MIN_TIME_BETWEEN_SHAKES = 1000000000; // in nanoseconds

    // Variables for the media playback and shake monitoring
    private float lastXAcceleration, lastYAcceleration, lastZAcceleration;
    private boolean isFirstValue = true;
    private long timestampOfLastChange = 0;

    private SensorManager sensorManager;
    private Sensor sensor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_running);
        preferences = this.getSharedPreferences("edu.moravian.csci299.lapCounter", Context.MODE_PRIVATE);

        sensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        Intent intent = getIntent();
        laps = intent.getDoubleExtra("laps",0.0);
        eventRecord = intent.getLongExtra("eventRecord", 0L);
        eventKey = intent.getStringExtra("eventKey");

        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);

        if (currentFragment == null) {
            // If no fragment is displayed in fragment_container, add one with a transaction
            ListFragment listFragment = ListFragment.newInstance();
            listFragment.setLaps(laps);
            listFragment.setEventRecord(eventRecord);
            listFragment.setEventKey(eventKey);
            listFragment.setPreferences(this.preferences);
            stopWatchFragment = StopWatchFragment.newInstance();
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.fragment_container, stopWatchFragment)
                    .add(R.id.fragment_container, listFragment)
                    .commit();

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        sensorManager.unregisterListener(this);
        super.onPause();
        stopWatchFragment.pause();
        stopWatchFragment.stop();
    }

    @Override
    public long getTime() {
        return stopWatchFragment.getTime();
    }


    @Override
    public void onSensorChanged(SensorEvent event) {
        float x = event.values[0];
        float y = event.values[1];
        float z = event.values[2];

        playOnShake(x,y,z,event.timestamp);
        Log.d("onSensorChanged", "changed");

    }


    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }


    private void playOnShake(float xAcceleration, float yAcceleration, float zAcceleration, long timestamp) {
        if (!isFirstValue && timestamp - timestampOfLastChange >= MIN_TIME_BETWEEN_SHAKES ) {
            boolean xShake = Math.abs(lastXAcceleration - xAcceleration) > SHAKE_THRESHOLD;
            boolean yShake = Math.abs(lastYAcceleration - yAcceleration) > SHAKE_THRESHOLD;
            boolean zShake = Math.abs(lastZAcceleration - zAcceleration) > SHAKE_THRESHOLD;
            if ((xShake && (yShake || zShake)) || (yShake && zShake)) {
                timestampOfLastChange = timestamp;
                stopWatchFragment.start();
            }
        }
        // save for comparing to next time
        lastXAcceleration = xAcceleration;
        lastYAcceleration = yAcceleration;
        lastZAcceleration = zAcceleration;
        isFirstValue = false;
    }

    @Override
    public long getRecord() {
        return eventRecord;
    }
}