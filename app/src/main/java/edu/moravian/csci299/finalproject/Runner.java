package edu.moravian.csci299.finalproject;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.UUID;

@Entity
public class Runner {
    @PrimaryKey
    @NonNull
    public UUID id = UUID.randomUUID();

    @NonNull
    public String name = "Name";

    public int number = 0;


    public double lapsToGo = 12.5;


}
