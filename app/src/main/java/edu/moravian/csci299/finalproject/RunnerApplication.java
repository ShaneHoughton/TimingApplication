package edu.moravian.csci299.finalproject;

import android.app.Application;

/**
 * Initialize the runner repository and when the app is opened clear the runners left from the last race.
 */
public class RunnerApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        // Initialize the repository with this application as the context
        RunnerRepository.initialize(this);
        RunnerRepository.get().clearDatabase();
    }
}