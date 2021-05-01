package edu.moravian.csci299.finalproject;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.room.Room;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class RunnerRepository {
    private final RunnerDatabase database;
    private final RunnerDAO runnerDAO;
    private final Executor executor = Executors.newSingleThreadExecutor(); //for assigning tasks to background thread


    private RunnerRepository(Context context){
        database = Room.databaseBuilder(
                context.getApplicationContext(),
                RunnerDatabase.class,
                "runner_database").build();
        runnerDAO = database.runnerDAO();
    }


    public LiveData<List<Runner>> getAllRunners() { return runnerDAO.getRunners(); }

    public LiveData<Runner> getRunnerById(UUID id) { return runnerDAO.getRunner(id); }

    public void addRunner(Runner... runner){
        executor.execute(() ->{
            runnerDAO.addRunner(runner);
        });
    }

    public void removeRunner(Runner runner){
        executor.execute(() ->{
            runnerDAO.removeRunner(runner);
        });
    }

    public void updateRunner(Runner runner){
        executor.execute(() -> {
            runnerDAO.updateRunner(runner);
        });
    }

    public void clearDatabase(){
        executor.execute(() -> {
            runnerDAO.clearData();
        });
    }

    // The single instance of the repository
    private static RunnerRepository INSTANCE;
    public static void initialize(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new RunnerRepository(context);
        }
    }

    public static RunnerRepository get() {
        if (INSTANCE == null) { throw new IllegalStateException("RunnerRepository must be initialized"); }
        return INSTANCE;
    }

}
