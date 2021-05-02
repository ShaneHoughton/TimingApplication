package edu.moravian.csci299.finalproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;

public class RunningActivity extends AppCompatActivity implements ListFragment.Callbacks, SensorEventListener {
    //TODO: should act like the main activity in MoCalendar
    StopWatchFragment stopWatchFragment;
    public double laps;

    //for sensor
    private final static float SHAKE_THRESHOLD = 3f; // in m/s^2
    private final static long MIN_TIME_BETWEEN_SHAKES = 1000000000; // in nanoseconds

    // Variables for the media playback and shake monitoring
    private float lastXAcceleration, lastYAcceleration, lastZAcceleration;
    private boolean isFirstValue = true;
    private long timestampOfLastChange = 0;

    // TODO: Variables for the sensor
    private SensorManager sensorManager;
    private Sensor sensor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_running);

        sensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);


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
}