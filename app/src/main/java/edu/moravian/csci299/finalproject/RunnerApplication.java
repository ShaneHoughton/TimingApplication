package edu.moravian.csci299.finalproject;

import android.app.Application;

public class RunnerApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        // Initialize the repository with this application as the context
        RunnerRepository.initialize(this);
        RunnerRepository.get().clearDatabase();
    }
}