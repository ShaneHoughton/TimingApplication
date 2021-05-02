package edu.moravian.csci299.finalproject;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;
import java.util.UUID;

/**
 * DAO for the database. Has funtions to select runners, get all runners, add runners,
 * remove runners, update a runner, and clear the database
 */
@Dao
public interface RunnerDAO {

    @Query("SELECT * FROM runner")
    LiveData<List<Runner>> getRunners();

    @Query("SELECT * FROM runner WHERE id=(:id)")
    LiveData<Runner> getRunner(UUID id);

    @Insert
    void addRunner(Runner... runner);

    @Delete
    void removeRunner(Runner... runner);


    @Update
    void updateRunner(Runner runner);

    @Query("DELETE FROM runner")
    void clearData();


}
