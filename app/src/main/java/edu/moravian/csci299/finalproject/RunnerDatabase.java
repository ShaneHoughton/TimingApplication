package edu.moravian.csci299.finalproject;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

/**
 * Abstract class extending RoomDatabase that Room uses to create class for database. We list all entities for Room and in this case
 * it is just our Event class and we create method for assigning our DAO class named CalendarDao.
 */
@Database(entities = {Runner.class}, version = 1)
@TypeConverters(RunnerTypeConverter.class)
public abstract class RunnerDatabase extends RoomDatabase {
    public abstract RunnerDAO runnerDAO();
}