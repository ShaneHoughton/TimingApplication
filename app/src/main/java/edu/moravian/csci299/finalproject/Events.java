package edu.moravian.csci299.finalproject;

public enum Events {
    M800(800),
    M1600(1600),
    M3000(3000),
    M5000(5000),
    M10000(10000);

    public final long distance;

    Events(long distance){
        this.distance = distance;
    }
}
