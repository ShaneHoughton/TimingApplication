package edu.moravian.csci299.finalproject;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.room.Room;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Creates the repository for our database in this application. We make the room database a singleton and have
 * three class variables to save the singleton instance of our application's database, our executor, and its DAO.
 */
public class RunnerRepository {
    private final RunnerDatabase database;
    private final RunnerDAO runnerDAO;
    private final Executor executor = Executors.newSingleThreadExecutor(); //for assigning tasks to background thread

    /**
     * Private constructor for creating instance of singleton repository. Set the database and the DAO to the respective
     * class variables and build database using databaseBuilder with the context as the parameter of context from class
     * that calls initialize.
     *
     * @param context context that is passed from initialize() as parameter for building the database repository, in our case
     *                it is RunnerApplication.
     */
    private RunnerRepository(Context context){
        database = Room.databaseBuilder(
                context.getApplicationContext(),
                RunnerDatabase.class,
                "runner_database").build();
        runnerDAO = database.runnerDAO();
    }

    /**
     * public method for for accessing the getRunners() method in RunnerDAO.
     *
     * @return all Runners in database in form of live data list
     */
    public LiveData<List<Runner>> getAllRunners() { return runnerDAO.getRunners(); }

    /**
     * public method for for accessing the getRunner() method in RunnerDAO.
     *
     * @return Runners in database with that id
     */
    public LiveData<Runner> getRunnerById(UUID id) { return runnerDAO.getRunner(id); }

    /**
     * public method for for accessing the addRunner() method in RunnerDAO.
     */
    public void addRunner(Runner... runner){
        executor.execute(() ->{
            runnerDAO.addRunner(runner);
        });
    }

    /**
     * public method for for accessing the removeRunner() method in RunnerDAO.
     */
    public void removeRunner(Runner runner){
        executor.execute(() ->{
            runnerDAO.removeRunner(runner);
        });
    }

    /**
     * public method for for accessing the updateRunner() method in RunnerDAO.
     */
    public void updateRunner(Runner runner){
        executor.execute(() -> {
            runnerDAO.updateRunner(runner);
        });
    }

    /**
     * public method for for accessing the clearDatabase() method in RunnerDAO.
     */
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

    /**
     * If the repository has been initialized, return the repository
     * @return an instance of the RunnerRepository
     */
    public static RunnerRepository get() {
        if (INSTANCE == null) { throw new IllegalStateException("RunnerRepository must be initialized"); }
        return INSTANCE;
    }

}
