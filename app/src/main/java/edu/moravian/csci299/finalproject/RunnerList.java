package edu.moravian.csci299.finalproject;


import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;


public class RunnerList extends ViewModel {
    List<Runner> runners = new ArrayList<>();

    public RunnerList() {
        runners.add(new Runner("Shane", 1, 12));
        runners.add(new Runner("Dom", 2, 12));

    }
}
